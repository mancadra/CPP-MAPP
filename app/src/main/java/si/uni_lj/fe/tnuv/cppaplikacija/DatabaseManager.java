package si.uni_lj.fe.tnuv.cppaplikacija;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// todo: metodo getAllQuestions razdelit v getQuestionsListByCategoryId(), get10RandomQuestions(), ... oz neki tazga
public class DatabaseManager {
    private final DatabaseReference databaseReference;
    private Map<Integer, Question> questionMap;

    public DatabaseManager() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("questions");
        questionMap = new HashMap<>();
    }

    public interface DataFetchCallback {
        void onDataFetched(ArrayList<Question>[] questionList);
    }

    public void getAllQuestions(final DataFetchCallback callback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int nrOfCategories = 14;
                ArrayList<Question>[] questions = new ArrayList[nrOfCategories]; // tabela seznamov vseh kategorij

                for (int i = 0; i < nrOfCategories; i++) {
                    questions[i] = new ArrayList<Question>(); // inicializacija seznamov vprasanj posamezne kategorije
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String questionId = dataSnapshot.child("question_id").getValue(String.class);
                    String questionText = dataSnapshot.child("question_text").getValue(String.class);

                    // preverimo, če id ni null, preskočimo vprašanje, če je
                    if (questionId == null) {
                        Log.e("DatabaseManager", "Question ID is null for data: " + dataSnapshot.getKey());
                        continue;
                    }

                    // Iz Jsona dobimo String, ki ga pretvorimo v int
                    int nrCorrAnswers = 0;
                    try {
                        String nrCorrAnswersString = dataSnapshot.child("num_of_correct_answers").getValue(String.class);
                        if (nrCorrAnswersString != null && !nrCorrAnswersString.isEmpty()) {
                            nrCorrAnswers = Integer.parseInt(nrCorrAnswersString);
                        }
                    } catch (NumberFormatException e) {
                        Log.e("DatabaseManager", "Error parsing num_of_correct_answers: " + e);
                    }

                    int categoryId = 0;
                    try {
                        String categoryIdString = dataSnapshot.child("categoryId").getValue(String.class);
                        if (categoryIdString != null && !categoryIdString.isEmpty()) {
                            categoryId = Integer.parseInt(categoryIdString);
                            //Log.d("DatabaseManager", String.valueOf(categoryId));
                        }
                    } catch (NumberFormatException e) {
                        Log.e("DatabaseManager", "Error parsing category_id: " + e);
                    }

                    List<String> answers = new ArrayList<>();
                    for (DataSnapshot answerSnapshot : dataSnapshot.child("answers").getChildren()) {
                        String answer = answerSnapshot.getValue(String.class);
                        if (answer != null) {
                            answers.add(answer);
                        }
                    }

                    List<Integer> correctAnswers = new ArrayList<>();
                    for (DataSnapshot corrAnswerSnapshot : dataSnapshot.child("correct_answers").getChildren()) {
                        Integer corrAnswer = corrAnswerSnapshot.getValue(Integer.class);
                        if (corrAnswer != null) {
                            correctAnswers.add(corrAnswer);
                        }
                    }

                    int[] correctAnswersArray = new int[correctAnswers.size()];
                    for (int i = 0; i < correctAnswers.size(); i++) {
                        correctAnswersArray[i] = correctAnswers.get(i);
                    }

                    Question question = new Question(questionText, nrCorrAnswers, answers.toArray(new String[0]), categoryId, correctAnswersArray, R.drawable.placeholder_image, Integer.parseInt(questionId));
                    questions[categoryId].add(question);
                    questionMap.put(Integer.parseInt(questionId), question); // Dodamo v slovar
                }
                callback.onDataFetched(questions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseManager","Failed to get data from database.");
            }
        });
    }
    public Question getQuestion(int questionId) {
        return questionMap.get(questionId);
    }

    // Vrne če je/ni prav obkroženo
    public boolean[] checkResults(int[] givenAnswers, int questionId) {
        Question question = getQuestion(questionId);
        if (question == null) {
            Log.e("DatabaseManager", "Question not found for ID: " + questionId);
            return null;
        }

        int[] correctAnswers = question.getCorrectAnswers();
        if (correctAnswers == null) {
            Log.e("DatabaseManager", "Invalid correct answers array");
            return null;
        }

        String[] answers = question.getAnswers();
        if (answers == null) {
            Log.e("DatabaseManager", "Invalid answers array or correct answers array");
            return null;
        }
        int nrAnswers = answers.length;
        boolean[] results = new boolean[nrAnswers];
        for (int i = 0; i < nrAnswers; i++) {
            results[i] = true;
            int occursInBoth = 0;
            for (int j = 0; j < correctAnswers.length; j++){
                if (correctAnswers[j] == i) {
                    occursInBoth++;
                }
            }
            for (int m = 0; m < givenAnswers.length; m++) {
                if (givenAnswers[m] == i) {
                    occursInBoth++;
                }
            }
            if (occursInBoth == 2) results[i] = true;
            else if (occursInBoth == 1) results[i] = false;
        }
        return results;
    }

    // vprne seznam vprašanj glede na seznam id-jev
    public void getQuestionsByIds(List<Integer> questionIds, final DataFetchCallback callback) {

        getAllQuestions(new DataFetchCallback() {
            @Override
            public void onDataFetched(ArrayList<Question>[] questionList) {
                ArrayList<Question>[] questions = new ArrayList[1];
                Log.d("DatabaseManager", "Received all questions. Filtering by IDs...");

                // filtriranje  vprašanj
                for (ArrayList<Question> categoryQuestions : questionList) {
                    for (Question question : categoryQuestions) {
                        if (questionIds.contains(question.getId())) {
                            questions[0] = new ArrayList<>();
                            questions[0].add(question);
                            Log.d("DatabaseManager", "Added question to the filtered list: " + question.getQuestionText());
                        }
                    }
                }
                callback.onDataFetched(questions);
            }
        });
    }

    // vrne seznam vprašanj glede na categoryId
    public void getCategoryQuestions(int categoryId, final DataFetchCallback callback) {
        getAllQuestions(new DataFetchCallback() {
            @Override
            public void onDataFetched(ArrayList<Question>[] questionList) {
                ArrayList<Question> filteredQuestions = new ArrayList<>();

                // filtriranje po kategoriji
                for (ArrayList<Question> categoryQuestions : questionList) {
                    for (Question question : categoryQuestions) {
                        if (categoryId == question.getCategoryId()) {
                            filteredQuestions.add(question);
                        }
                    }
                }
                Log.d("DatabaseManager", "Fetched " + filteredQuestions.size() + "successfully.");

                // callback s filtriranimi vprašanji
                ArrayList<Question>[] questions = new ArrayList[1];
                questions[0] = filteredQuestions;
                callback.onDataFetched(questions);
            }
        });
    }
}


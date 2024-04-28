package si.uni_lj.fe.tnuv.cppaplikacija;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

// todo: metodo getAllQuestions razdelit v getQuestionsListByCategoryId(), get10RandomQuestions(), ... oz neki tazga
public class DatabaseManager {
    private final DatabaseReference databaseReference;

    public DatabaseManager() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("questions");
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
                        Log.e("Database", "Question ID is null for data: " + dataSnapshot.getKey());
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
                        Log.e("Database", "Error parsing num_of_correct_answers: " + e);
                    }

                    int categoryId = 0;
                    try {
                        String categoryIdString = dataSnapshot.child("categoryId").getValue(String.class);
                        if (categoryIdString != null && !categoryIdString.isEmpty()) {
                            categoryId = Integer.parseInt(categoryIdString);
                            Log.e("UPDATED CATEGORY ID", String.valueOf(categoryId));
                        }
                    } catch (NumberFormatException e) {
                        Log.e("Database", "Error parsing category_id: " + e);
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
                    Log.d("ZAPIS_KATEGORIJA", "Category ID: " + categoryId);
                }
                callback.onDataFetched(questions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database","Failed to get data from database.");
            }
        });
    }
}


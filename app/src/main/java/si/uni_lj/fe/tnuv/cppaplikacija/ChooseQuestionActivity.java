package si.uni_lj.fe.tnuv.cppaplikacija;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ChooseQuestionActivity extends AppCompatActivity {

    // deklaracija sprmelnjivk za bazo in referenca nanjo
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private TextView retrieveTV;
    private int categoryId;
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_question);

        // inicializacija za bazo in refernce nanjo
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("questions");

        // inicializacija recycle view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Metodo kličemo asinhrono, ko fetchamo vse podatke
        getdata(new DataFetchCallback() {
            @Override
            public void onDataFetched(List<Question> questionList) {
                Log.d("DataFetch", "Data fetched successfully: " + questionList.size() + " questions");
                QuestionAdapter questionAdapter = new QuestionAdapter(ChooseQuestionActivity.this, questionList);
                recyclerView.setAdapter(questionAdapter);
            }
        });

        // dobimo naslov, in id izbrane kategorije
        /*Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category_title") && intent.hasExtra("category_id")) {
            String categoryTitle = intent.getStringExtra("category_title");
            categoryId = intent.getIntExtra("category_id", 1);

            TextView catTitle = findViewById(R.id.catTitleInList);
            catTitle.setText(categoryTitle);
        }*/
    }


    public interface DataFetchCallback {
        void onDataFetched(List<Question> questionList);
    }

    // Pridobivanje podatkov iz baze
    public void getdata(final DataFetchCallback callback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Question> questionList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String questionId = dataSnapshot.child("question_id").getValue(String.class);
                    String questionText = dataSnapshot.child("question_text").getValue(String.class);

                    // Iz Jsona dobimo String, ki ga pretvorimo v int
                    String nrCorrAnswersString = dataSnapshot.child("num_of_correct_answers").getValue(String.class);
                    int nrCorrAnswers = 0;
                    if (nrCorrAnswersString != null && !nrCorrAnswersString.isEmpty()) {
                        try {
                            nrCorrAnswers = Integer.parseInt(nrCorrAnswersString);
                        } catch (NumberFormatException e) {
                            // Handle parsing error
                            e.printStackTrace();
                        }
                    }

                    String categoryIdString = dataSnapshot.child("category_id").getValue(String.class);
                    int categoryId = 0;
                    if (categoryIdString != null && !categoryIdString.isEmpty()) {
                        try {
                            categoryId = Integer.parseInt(categoryIdString);
                        } catch (NumberFormatException e) {
                            // Handle parsing error
                            e.printStackTrace();
                        }
                    }

                    List<String> answers = new ArrayList<>();
                    for (DataSnapshot answerSnapshot : dataSnapshot.child("answers").getChildren()) {
                        String answer = answerSnapshot.getValue(String.class);
                        answers.add(answer);
                    }

                    List<Integer> correctAnswers = new ArrayList<>();
                    for (DataSnapshot corrAnswerSnapshot : dataSnapshot.child("correct_answers").getChildren()) {
                        int corrAnswer = corrAnswerSnapshot.getValue(Integer.class).intValue();
                        correctAnswers.add(corrAnswer);
                    }

                    int[] correctAnswersArray = new int[correctAnswers.size()];
                    for (int i = 0; i < correctAnswers.size(); i++) {
                        correctAnswersArray[i] = correctAnswers.get(i);
                    }

                    Question question = new Question(questionText, nrCorrAnswers, answers.toArray(new String[0]), categoryId, correctAnswersArray, R.drawable.placeholder_image, Integer.parseInt(questionId));
                    questionList.add(question);
                    // Log.d("TESTNI_IZPIS", questionId); //String.valueOf(questionId)
                }
                callback.onDataFetched(questionList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChooseQuestionActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

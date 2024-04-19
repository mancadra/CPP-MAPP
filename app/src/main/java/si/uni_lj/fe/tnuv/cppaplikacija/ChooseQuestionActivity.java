package si.uni_lj.fe.tnuv.cppaplikacija;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        // inicializacija za bazo in refernce nanjo
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("questions");


        // inicializacija recycle view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        retrieveQuestions(new OnQuestionsRetrievedListener() {
            @Override
            public void onQuestionsRetrieved(List<Question> questionList) {
                // Create adapter and set it to RecyclerView
                QuestionAdapter questionAdapter = new QuestionAdapter(ChooseQuestionActivity.this, questionList);
                recyclerView.setAdapter(questionAdapter);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });

        // dobimo naslov, in id izbrane kategorije
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category_title") && intent.hasExtra("category_id")) {
            String categoryTitle = intent.getStringExtra("category_title");
            categoryId = intent.getIntExtra("category_id", 1);

            TextView catTitle = findViewById(R.id.catTitleInList);
            catTitle.setText(categoryTitle);
        }
    }

    // Method to retrieve questions from the database
    private void retrieveQuestions(final OnQuestionsRetrievedListener listener) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Question> questionList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    if (question != null) {
                        questionList.add(question);
                    }
                }
                listener.onQuestionsRetrieved(questionList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    // Interface to handle callbacks when questions are retrieved
    interface OnQuestionsRetrievedListener {
        void onQuestionsRetrieved(List<Question> questionList);
        void onError(String errorMessage);
    }
        // ustvarimo adapter
        /*List<Question> questionList = getQuestions(); // Get questions from the database
        QuestionAdapter questionAdapter = new QuestionAdapter(this, questionList);
        recyclerView.setAdapter(questionAdapter);
    }

    // metoda nam vrne vsa vprašanja v bazi
    private List<Question> getQuestions() {
        return null;
    }*/
}

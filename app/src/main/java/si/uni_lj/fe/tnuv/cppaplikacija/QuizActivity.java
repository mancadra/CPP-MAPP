package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    private int categoryId;
    private String type;
    private int questionId;
    DatabaseManager databaseManager = new DatabaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // nastavimo navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_invisible);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_favorites) {
                Intent intent = new Intent(QuizActivity.this, ChooseQuestionActivity.class);
                intent.putExtra("category_id", 15);
                intent.putExtra("category_title", "Moja vprašanja");
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(QuizActivity.this, ChooseCategoryActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_history) {
                startActivity(new Intent(QuizActivity.this, HistoryActivity.class));
                return true;
            } else {
                return false;
            }
        });

        // dobimo naslov, in id izbrane kategorije
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category_title") && intent.hasExtra("category_id" ) && intent.hasExtra("question_id" ) && intent.hasExtra("type" )) {
            String categoryTitle = intent.getStringExtra("category_title");
            categoryId = intent.getIntExtra("category_id", 1);
            type = intent.getStringExtra("type");
            // če je id -1, pomeni, da je uporabnik kliknil na MIX
            questionId = intent.getIntExtra("question_id", -1);
            TextView catTitle = findViewById(R.id.catTitleInQuiz);
            catTitle.setText(categoryTitle);

            if(type.equals("mix")) {
                getRandomQuestion(categoryId);
            } else {
                getQuestion(categoryId, questionId);
            }
        }
    }

    private void getQuestion(int categoryId, int questionId) {
        Log.d("QuizActivity","Specific question requested.");

        // Dobimo vprašanja iz kategorije
        databaseManager.getCategoryQuestions(categoryId, questionList ->  {
            if (questionList != null && questionList.length > 0 && questionList[0] != null) {
                Log.d("QuizActivity", "Data fetched successfully: " + questionList[0].size() + " questions");
            } else {
                Log.e("QuizActivity", "No questions fetched or questions array is null.");
            }
        });

    }

    private void getRandomQuestion(int categoryId) {
        Log.d("QuizActivity","Random question requested.");

    }
}

/* todo:
*   - on click each answer (odvisno ali ima vprašanje več možnih odgovorov)
*   - display da je več možnih vprašanj, kjer je
*   - on click preveri
*   - on click next (plus implementacija dobit vse iz baze, iterirat in če je mix, je 10 random vprašanj)
*   - shranit odgovore in rezultate v shared preferences
*   - favorites*/

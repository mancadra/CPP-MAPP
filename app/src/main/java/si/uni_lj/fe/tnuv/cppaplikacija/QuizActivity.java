package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    private int categoryId;
    private String type;
    private int questionId;
    private ArrayList<Question>[] questions;
    private int currentQuestionIndex = 0;
    private int randomQuestionCount = 0;
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

            getQuestions(categoryId);
        }
    }

    private void getQuestions(int categoryId) {
        Log.d("QuizActivity","Specific question requested.");

        // Dobimo vprašanja iz kategorije
        databaseManager.getCategoryQuestions(categoryId, questionList ->  {
            if (questionList != null && questionList.length > 0 && questionList[0] != null) {
                Log.d("QuizActivity", "Data fetched successfully: " + questionList[0].size() + " questions");
                questions = questionList;
                currentQuestionIndex = getFirstQuestionIndex();
                displayQuestion();
            } else {
                Log.e("QuizActivity", "No questions fetched or questions array is null.");
            }
        });

    }

    private void displayQuestion() {
        if (questions != null && !questions[0].isEmpty()) {
            Question currentQuestion;
            if (type.equals("mix")) {
                currentQuestion = getRandomQuestion();
            } else {
                currentQuestion = getSpecificQuestion();
            }
            if(currentQuestion != null) updateUI(currentQuestion);
        }
    }

    private Question getSpecificQuestion() {
        // Poiščemo indeks vprašanja z ID-jem
        int index = currentQuestionIndex;
        currentQuestionIndex++;

        if (index >= 0 && index < questions[0].size()) {
            return questions[0].get(index);
        } else {
            // Če ID vprašanja ni, vrnemo prvo vprašanje
            return questions[0].get(0);
        }
    }

    private int getFirstQuestionIndex() {
        int index = -1;

        if(questions == null) {
            Log.e("QuizActivity", "questions array is null");
            return -1;
        }

        for (int i = 0; i < questions[0].size(); i++) {
            if (questions[0].get(i).getId() == questionId) {
                index = i;
                break;
            }
        }

        return index;
    }

    private Question getRandomQuestion() {
        randomQuestionCount++;
        if (randomQuestionCount <= 10) {
            // // Generiramo random indeks
            int randomIndex = (int) (Math.random() * questions[0].size());
            return questions[0].get(randomIndex);
        } else {
            // Zaključimo kviz, če je bilo prikazanih 10  vprašanj
            finishQuiz();
            return null;
        }
    }

    private void updateUI(Question question) {
        // Posodobimo UI
        TextView questionTextView = findViewById(R.id.tv_question);
        questionTextView.setText(question.getQuestionText());

        Button nextButton = findViewById(R.id.btn_next_question);
        nextButton.setEnabled(true);
    }

    private void finishQuiz() {
        // Začnemo ResultsActivity za prikaz rezultatov
        Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);

        startActivity(intent);
        // Zaključimo QuizActivity
        finish();
    }


    public void onNextButtonClick(View view) {
        // Onemogočimo gumb "Naprej", preprečimo večkratne klike
        Button nextButton = findViewById(R.id.btn_next_question);
        nextButton.setEnabled(false);


        displayQuestion();
    }


}

/* todo:
*   - on click each answer (odvisno ali ima vprašanje več možnih odgovorov)
*   - display da je več možnih vprašanj, kjer je
*   - on click preveri
*   - on click next (plus implementacija dobit vse iz baze, iterirat in če je mix, je 10 random vprašanj)
*   - shranit odgovore in rezultate v shared preferences
*   - favorites*/

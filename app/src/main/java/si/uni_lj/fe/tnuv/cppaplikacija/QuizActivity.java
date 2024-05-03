package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;

// TODO: SHRANI VPRAŠANJE V PREFERENCES (v odgovore in pravilne / napačne)
// TODO: SHRANI rezultate 10 odgovorov na mix  - pošlji v ResultsActivity (intent)
// TODO: DODAJ gumb za shraniti vprašanje pod priljubljene in implementiraj funkionalnost
public class QuizActivity extends AppCompatActivity {
    private int categoryId;
    private String type;
    private int questionId;
    private ArrayList<Question>[] questions;
    private int currentQuestionIndex = 0;
    private int randomQuestionCount = 0;
    boolean[] selectedAnswers;
    int[] correctAnswers;
    DatabaseManager databaseManager = new DatabaseManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Disable gumb preveri
        Button checkButton = findViewById(R.id.btn_check_answer);
        checkButton.setEnabled(false);

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
            correctAnswers = questions[0].get(index).getCorrectAnswers();
            return questions[0].get(index);
        } else {
            // Če ID vprašanja ni, vrnemo prvo vprašanje
            correctAnswers = questions[0].get(0).getCorrectAnswers();
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
            correctAnswers = questions[0].get(randomIndex).getCorrectAnswers();
            return questions[0].get(randomIndex);
        } else {
            // Zaključimo kviz, če je bilo prikazanih 10  vprašanj
            finishQuiz();
            return null;
        }
    }

    private void updateUI(Question question) {
        // Gumb preveri
        Button checkButton = findViewById(R.id.btn_check_answer);
        checkButton.setEnabled(false);
        // Tekst vprašanje
        TextView questionTextView = findViewById(R.id.tv_question);
        questionTextView.setText(question.getQuestionText());

        // Odgovori
        LinearLayout layoutAnswers = findViewById(R.id.layout_answers);
        layoutAnswers.removeAllViews();

        String[] answers = question.getAnswers();
        selectedAnswers = new boolean[answers.length];
        Arrays.fill(selectedAnswers, false);

        for (int i = 0; i < answers.length; i++) {
            String answer = answers[i];
            CustomGradientDrawable customDrawable = new CustomGradientDrawable(
                    Color.parseColor("#6804ec"),
                    Color.parseColor("#6804ec"),
                    10
            );
            Button answerButton = new Button(this);
            answerButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            answerButton.setText(answer);
            answerButton.setBackground(customDrawable);
            answerButton.setTextColor(Color.WHITE);
            answerButton.setTag(i);


            // ob kliku se spremeni barva OBROBE odgovora

            answerButton.setOnClickListener(view -> {
                int index = (int) view.getTag();
                selectedAnswers[index] = !selectedAnswers[index];

                Drawable backgroundDrawable = answerButton.getBackground();
                if (backgroundDrawable instanceof CustomGradientDrawable) {
                    CustomGradientDrawable customDrawable2 = (CustomGradientDrawable) backgroundDrawable;

                    int currentStrokeColor = customDrawable2.getStrokeColor();

                    int color1 = Color.parseColor("#6804ec");
                    int color2 = Color.parseColor("#ffbb00");

                    int newStrokeColor = (currentStrokeColor == color1) ? color2 : color1;

                    customDrawable.setStrokeColor(newStrokeColor);
                    answerButton.setBackground(customDrawable);
                }

                // Gumb preveri
                checkButton.setEnabled(true);
            });



            layoutAnswers.addView(answerButton);
        }



        // Gumb naprej
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

    public void onCheckButtonClick(View view) {
        LinearLayout layoutAnswers = findViewById(R.id.layout_answers);
        // array za pravilne odgovore
        boolean[] correctness  = new boolean[selectedAnswers.length];
        Arrays.fill(correctness, false);

        for (int i = 0; i < selectedAnswers.length; i++) {
            for (int correctAnswer : correctAnswers) {
                if (i == correctAnswer) {
                    correctness[i] = true;
                    break;
                }
            }
        }

        // iteracija po gumbih
        for (int i = 0; i < layoutAnswers.getChildCount(); i++) {
            View childView = layoutAnswers.getChildAt(i);
            if (childView instanceof Button) {

                Button answerButton = (Button) childView;
                // če uporabnik odgovoril pravilno
                if (correctness[i]) {
                    if(selectedAnswers[i]) {

                        int bgColor = Color.parseColor("#008000");
                        int strokeColor = Color.parseColor("#ffbb00");
                        CustomGradientDrawable customDrawable = new CustomGradientDrawable(bgColor, strokeColor, 10);
                        answerButton.setBackground(customDrawable);

                    } else {
                        answerButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC);
                    }
                // če je uporabnik odgovoril nepravilno
                } else {
                    if (selectedAnswers[i]) {
                        int bgColor = Color.RED;
                        int strokeColor = Color.parseColor("#ffbb00");
                        CustomGradientDrawable customDrawable = new CustomGradientDrawable(bgColor, strokeColor, 10);
                        answerButton.setBackground(customDrawable);
                    }
                }

            }
        }
    }


}


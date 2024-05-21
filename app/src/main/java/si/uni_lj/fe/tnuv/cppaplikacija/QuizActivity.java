package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;


import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;


public class QuizActivity extends AppCompatActivity {
    private int categoryId;
    private String type; // mix ali po vrsti
    private int questionId;
    private boolean isFav;
    private int questionIdMix; // če smo izbrali tip mix se bo tu zapisal random index
    private ArrayList<Question>[] questions;
    private ArrayList<Question> categoryQuestions;
    private int currentQuestionIndex = 0;
    private int randomQuestionCount = 0; // števec za mix kviz (do 10)
    private int mixQuizQuestionIx = -1; // isto kot randomQuestionCount
    boolean[] selectedAnswers; // uporabnikovi odgovori
    boolean[] points = new boolean[10];

    // pravilni odgovori na trenutno vprašanje : 2 bliki
    int[] correctAnswers;
    boolean[] correctness;
    private ImageView ivQuestionImage;
    DatabaseManager databaseManager = new DatabaseManager();



    /** @noinspection deprecation*/
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
        // zastarela metoda
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
            questionId = intent.getIntExtra("question_id", -1);
            TextView catTitle = findViewById(R.id.catTitleInQuiz);
            catTitle.setText(categoryTitle);

            getQuestions(categoryId);
        }



        // Updata favourites ikono
        ImageView favourite = findViewById(R.id.iv_favourite);

        favourite.setOnClickListener(view -> {
            Log.d("QuizActivity", "OnClick. Add question to favorites. Question with id was clicked: " + questionId);
            PreferencesManager preferencesManager = new PreferencesManager(QuizActivity.this);
            isFav = preferencesManager.addRemoveFavoriteQuestion(questionId);
            updateFavoriteIcon(favourite, isFav);
        });
    }

    private void updateFavoriteIcon(ImageView favourite, boolean isFav) {
        if (isFav) {
            favourite.setImageResource(R.drawable.ic_favorites_filled);
        } else {
            favourite.setImageResource(R.drawable.ic_favorites);
        }
    }

    private void getQuestions(int categoryId) {
        Log.d("QuizActivity","A question requested. Type of quiz is " + type);

        questions = QuestionsSingleton.getInstance().getQuestionList();
        if (categoryId < 15)  {
            categoryQuestions = questions[categoryId];
        } else if (categoryId == 15) {
            PreferencesManager preferencesManager = new PreferencesManager(QuizActivity.this);
            categoryQuestions = preferencesManager.getFavoriteQuestions();
        }
        if (questions != null && questions.length > 0 && categoryQuestions != null && !categoryQuestions.isEmpty()) {
            Log.d("QuizActivity", "Data fetched successfully: " + categoryQuestions.size() + " questions");
            currentQuestionIndex = getFirstQuestionIndex();

            displayQuestion();
        } else {
            Log.e("QuizActivity", "No questions fetched or questions array is null.");
        }
    }

    private void displayQuestion() {
        if (categoryQuestions != null && !categoryQuestions.isEmpty()) {
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

        if (index >= 0 && index < categoryQuestions.size()) {
            correctAnswers = categoryQuestions.get(index).getCorrectAnswers();
            return categoryQuestions.get(index);
        } else {
            // Če ID vprašanja ni, vrnemo prvo vprašanje
            correctAnswers = categoryQuestions.get(0).getCorrectAnswers();
            return categoryQuestions.get(0);
        }
    }

    private int getFirstQuestionIndex() {
        int index = -1;

        if(categoryQuestions == null) {
            Log.e("QuizActivity", "questions array is null");
            return -1;
        }

        for (int i = 0; i < categoryQuestions.size(); i++) {
            if (categoryQuestions.get(i).getId() == questionId) {
                index = i;
                break;
            }
        }
        return index;
    }

    private Question getRandomQuestion() {
        randomQuestionCount++;
        if (randomQuestionCount <= 10) {
            mixQuizQuestionIx++;
            // // Generiramo random indeks
            int randomIndex = (int) (Math.random() * categoryQuestions.size());
            questionIdMix = randomIndex;
            questionId = randomIndex;
            correctAnswers = categoryQuestions.get(randomIndex).getCorrectAnswers();
            return categoryQuestions.get(randomIndex);
        } else {
            // Zaključimo kviz, če je bilo prikazanih 10  vprašanj
            finishQuiz();
            return null;
        }
    }

    private void updateUI(Question question) {
        questionId = question.getId();
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

        ivQuestionImage = (ImageView) findViewById(R.id.iv_question_image);
        String imageURL = question.getImageResource();
        Log.d("QuizActivity", "Image URL: " + imageURL);
        Glide.with(QuizActivity.this).load(imageURL).into(ivQuestionImage);


        // Pobarva favourites, če so v seznamu
        PreferencesManager preferencesManager = new PreferencesManager(QuizActivity.this);
        ImageView favourite = findViewById(R.id.iv_favourite);
        isFav = preferencesManager.isQuestionFavorite(questionId);
        updateFavoriteIcon(favourite, isFav);

        int topMarginInDp = 10;
        float density = getResources().getDisplayMetrics().density;
        int topMarginInPixels = (int) (topMarginInDp * density);

        for (int i = 0; i < answers.length; i++) {
            String answer = answers[i];
            CustomGradientDrawable customDrawable = new CustomGradientDrawable(
                    ContextCompat.getColor(this, R.color.teal_shade),
                    ContextCompat.getColor(this, R.color.teal_shade),
                    10
            );
            Button answerButton = new Button(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.topMargin = topMarginInPixels;
            answerButton.setLayoutParams(layoutParams);

            answerButton.setText(answer);
            answerButton.setBackground(customDrawable);
            answerButton.setTextColor(Color.WHITE);
            answerButton.setTag(i);
            answerButton.setAllCaps(false);

            // ob kliku se spremeni barva OBROBE odgovora
            answerButton.setOnClickListener(view -> {
                Log.d("QuizActivity", "OnClick: Answer selected/deselected.");
                int index = (int) view.getTag();
                selectedAnswers[index] = !selectedAnswers[index];

                Drawable backgroundDrawable = answerButton.getBackground();
                if (backgroundDrawable instanceof GradientDrawable) {
                    CustomGradientDrawable customDrawable2 = (CustomGradientDrawable) backgroundDrawable;
                    int currentStrokeColor = customDrawable2.getStrokeColor();

                    int color1 = ContextCompat.getColor(this, R.color.teal_shade); // teal
                    int color2 = ContextCompat.getColor(this, R.color.yellow); // yellow
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
        // Začnemo ResultsActivity za prikaz rezultatov in mu pošljemo rezultate
        Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
        intent.putExtra("pointsArray", points);

        startActivity(intent);
        // Zaključimo QuizActivity
        finish();
    }


    public void onNextButtonClick(View view) {
        Log.d("QuizActivity", "OnClick: Next button.");
        // array za pravilne odgovore
        correctness  = new boolean[selectedAnswers.length];
        Arrays.fill(correctness, false);

        for (int i = 0; i < selectedAnswers.length; i++) {
            for (int correctAnswer : correctAnswers) {
                if (i == correctAnswer) {
                    correctness[i] = true;
                    break;
                }
            }
        }

        // ali je odg pravilno ?
        boolean pravilnost = Arrays.equals(correctness, selectedAnswers);
        // shranjevanje odgovora (zgodovina)
        PreferencesManager preferencesManager = new PreferencesManager(QuizActivity.this);
        preferencesManager.addAnsweredQuestion(questionId, pravilnost);

        // correctness in selectedAnswers primerjamo
        // če sta enaki, dodamo točko v tabelo points
        if (mixQuizQuestionIx >= 0 && pravilnost) {
            points[mixQuizQuestionIx] = true;
            Log.d("QuizActivity", "Score saved!");
        }
        Log.d("QuizActivity", "The points are " + Arrays.toString(points));

        // Onemogočimo gumb "Naprej", preprečimo večkratne klike
        Button nextButton = findViewById(R.id.btn_next_question);
        nextButton.setEnabled(false);

        displayQuestion();
    }

    public void onCheckButtonClick(View view) {
        Log.d("QuizActivity", "OnClick: Check answer button.");
        LinearLayout layoutAnswers = findViewById(R.id.layout_answers);
        // array za pravilne odgovore
        correctness  = new boolean[selectedAnswers.length];
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
                // če uporabnik odgovoril pravilno - spremenimo barvo gumbov
                if (correctness[i]) {
                    if(selectedAnswers[i]) {

                        //int bgColor = Color.parseColor("#008000"); // zelena (correct)
                        //int strokeColor = Color.parseColor("#ffbb00"); // rumena outline
                        int bgColor = ContextCompat.getColor(this, R.color.green); // zelena (correct)
                        int strokeColor = ContextCompat.getColor(this, R.color.yellow); // rumena outline
                        CustomGradientDrawable customDrawable = new CustomGradientDrawable(bgColor, strokeColor, 10);
                        answerButton.setBackground(customDrawable);

                    } else {
                        int bgColor = ContextCompat.getColor(this, R.color.green); // zelena (correct)
                        answerButton.getBackground().setColorFilter(bgColor, PorterDuff.Mode.SRC);
                    }
                // če je uporabnik odgovoril nepravilno - spremenimo barvo gumbov
                } else {
                    if (selectedAnswers[i]) {
                        int bgColor = ContextCompat.getColor(this, R.color.red); // rdeča (wrong)
                        int strokeColor = ContextCompat.getColor(this, R.color.yellow); // rumena outline
                        CustomGradientDrawable customDrawable = new CustomGradientDrawable(bgColor, strokeColor, 10);
                        answerButton.setBackground(customDrawable);
                    }
                }
            }
        }
    }
}


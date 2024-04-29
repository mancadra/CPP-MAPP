package si.uni_lj.fe.tnuv.cppaplikacija;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HistoryActivity extends AppCompatActivity {
    private PreferencesManager preferencesManager;
    private TextView tvCorrectAnswers, tvIncorrectAnswers, tvTotalAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // prfereces manager in posodobitev statistike
        preferencesManager = new PreferencesManager(this);

        tvCorrectAnswers = findViewById(R.id.tvCorrectAnswers);
        tvIncorrectAnswers = findViewById(R.id.tvIncorrectAnswers);
        tvTotalAnswers = findViewById(R.id.tvTotalAnswers);

        updateQuestionStats();

        // nastavimo navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_history);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_favorites) {
                Intent intent = new Intent(HistoryActivity.this, ChooseQuestionActivity.class);
                intent.putExtra("category_id", 15);
                intent.putExtra("category_title", "Moja vprašanja");
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(HistoryActivity.this, ChooseCategoryActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_history) {
                startActivity(new Intent(HistoryActivity.this, HistoryActivity.class));
                return true;
            } else {
                return false;
            }
        });
    }

    private void updateQuestionStats() {
        int totalAnswered = preferencesManager.getTotalAnsweredQuestions();
        int totalCorrect = preferencesManager.getTotalCorrectlyAnsweredQuestions();
        int totalIncorrect = preferencesManager.getTotalFalselyAnsweredQuestions();

        String correctAnswersText = getString(R.string.correct_answers, totalCorrect);
        String incorrectAnswersText = getString(R.string.incorrect_answers, totalIncorrect);
        String totalAnswersText = getString(R.string.total_answers, totalAnswered);

        tvCorrectAnswers.setText(correctAnswersText);
        tvIncorrectAnswers.setText(incorrectAnswersText);
        tvTotalAnswers.setText(totalAnswersText);
    }
}
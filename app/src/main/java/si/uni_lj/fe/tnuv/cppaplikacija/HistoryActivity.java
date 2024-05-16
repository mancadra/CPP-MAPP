package si.uni_lj.fe.tnuv.cppaplikacija;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;


import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private PreferencesManager preferencesManager;
    private TextView tvCorrectAnswers, tvIncorrectAnswers, tvTotalAnswers, tvTotal;
    private int totalCorrect, totalIncorrect, totalUnanswered;

    /** @noinspection deprecation*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // prfereces manager in posodobitev statistike
        preferencesManager = new PreferencesManager(this);


        tvCorrectAnswers = findViewById(R.id.tvCorrectAnswers);
        tvIncorrectAnswers = findViewById(R.id.tvIncorrectAnswers);
        tvTotalAnswers = findViewById(R.id.tvTotalAnswers);
        tvTotal = findViewById(R.id.tvTotal);

        updateQuestionStats();

        // nastavimo navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_history);

        // zastarela metoda
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

        // tortni diagram
        PieChart pieChart = findViewById(R.id.pieChart);
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalCorrect, "Pravilno"));
        entries.add(new PieEntry(totalIncorrect, "Nepravilno"));
        entries.add(new PieEntry(totalUnanswered, "Neodgovorjeno"));

        PieDataSet dataSet = new PieDataSet(entries, "Answers");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);

// Customize legend
        Legend legend = pieChart.getLegend();
        legend.setTextSize(14f);

        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh chart
    }

    // pridobi podatke iz shared preferences
    private void updateQuestionStats() {
        int totalQuestions = QuestionsSingleton.getInstance().getTotalNumberOfQuestions();
        int totalAnswered = preferencesManager.getTotalAnsweredQuestions();
        totalCorrect = preferencesManager.getTotalCorrectlyAnsweredQuestions();
        totalIncorrect = preferencesManager.getTotalFalselyAnsweredQuestions();
        totalUnanswered = totalQuestions - totalAnswered;

        String correctAnswersText = getString(R.string.correct_answers, totalCorrect);
        String incorrectAnswersText = getString(R.string.incorrect_answers, totalIncorrect);
        String totalAnswersText = getString(R.string.total_answers, totalAnswered);
        String total = getString(R.string.total, totalQuestions);


        tvCorrectAnswers.setText(correctAnswersText);
        tvIncorrectAnswers.setText(incorrectAnswersText);
        tvTotalAnswers.setText(totalAnswersText);
        tvTotal.setText(total);
    }
}
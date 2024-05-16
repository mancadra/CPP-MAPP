package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

// TODO: Pridobi in prikaži podatke iz quizactivity + OK btn, ki odpelje na homescreen
public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // dobimo rezultate
        boolean[] pointsArray = getIntent().getBooleanArrayExtra("pointsArray");
        assert pointsArray != null;
        int score = calculatePoints(pointsArray);

        // prikažemo rezultate
        TextView tvScore = findViewById(R.id.tv_score);
        String scoreText = getString(R.string.result, score);
        tvScore.setText(scoreText);
    }

    private int calculatePoints(boolean[] pointsArray) {
        int points = 0;
        for (boolean point : pointsArray) {
            if (point) {
                points++;
            }
        }
        return points;
    }
}
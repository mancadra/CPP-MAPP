package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // dobimo rezultate
        boolean[] pointsArray = getIntent().getBooleanArrayExtra("pointsArray");
        if (pointsArray == null) {
            Log.e("ResultsActivity", "onCreate: pointsArray is null");
            return;
        }
        int score = calculatePoints(pointsArray);

        // prikažemo rezultate
        TextView tvScore = findViewById(R.id.tv_score);
        String scoreText = getString(R.string.result, score);
        tvScore.setText(scoreText);

        // če klikne OK, se odpre zgodovina
        Button okButton = findViewById(R.id.ok);
        okButton.setOnClickListener(v -> {
            Log.d("ResultsActivity", "onClick: OK button clicked");
            startActivity(new Intent(ResultsActivity.this, HistoryActivity.class));
        });
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
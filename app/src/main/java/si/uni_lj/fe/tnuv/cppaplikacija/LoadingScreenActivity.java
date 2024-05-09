package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class LoadingScreenActivity extends AppCompatActivity {
    private static final int LOADING_DELAY_MILLISECONDS = 4000;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        // Pridobivanje podatkov iz baze na cloudu in zapisovanje v singleton
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.getAllQuestions(questions -> {
            // Set the fetched data in the ViewModel
            Log.d("DataFetch-LoadingScreenActivity", "Data fetched successfully: " + questions[1].size() + " questions");
            QuestionsSingleton.getInstance().setQuestionList(questions);
        });

        // po 4 sekundah se prikaže seznam kategorij
        new Handler().postDelayed(() -> {
            // zagon ChooseCategoryActivity
            Intent intent = new Intent(LoadingScreenActivity.this, ChooseCategoryActivity.class);
            startActivity(intent);
            // konec LoadingScreenActivity
            finish();
        }, LOADING_DELAY_MILLISECONDS);
    }
}
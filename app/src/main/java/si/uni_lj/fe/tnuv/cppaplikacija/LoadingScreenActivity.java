package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;



public class LoadingScreenActivity extends AppCompatActivity {
    private static final int LOADING_DELAY_MILLISECONDS = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

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
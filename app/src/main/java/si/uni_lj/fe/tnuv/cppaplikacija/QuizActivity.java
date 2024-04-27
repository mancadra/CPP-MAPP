package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity {
    private int categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // dobimo naslov, in id izbrane kategorije
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category_title") && intent.hasExtra("category_id")) {
            String categoryTitle = intent.getStringExtra("category_title");
            categoryId = intent.getIntExtra("category_id", 1);
            TextView catTitle = findViewById(R.id.catTitleInQuiz);
            catTitle.setText(categoryTitle);
        }
    }
}

/* todo:
*   - on click each answer (odvisno ali ima vprašanje več možnih odgovorov)
*   - display da je več možnih vprašanj, kjer je
*   - on click preveri
*   - on click next (plus implementacija dobit vse iz baze, iterirat in če je mix, je 10 random vprašanj)
*   - shranit odgovore in rezultate v shared preferences
*   - favorites*/

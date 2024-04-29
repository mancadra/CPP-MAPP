package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChooseQuizTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz_type);

        // nastavimo navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_invisible);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_favorites) {
                Intent intent = new Intent(ChooseQuizTypeActivity.this, ChooseQuestionActivity.class);
                intent.putExtra("category_id", 15);
                intent.putExtra("category_title", "Moja vprašanja");
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(ChooseQuizTypeActivity.this, ChooseCategoryActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_history) {
                startActivity(new Intent(ChooseQuizTypeActivity.this, HistoryActivity.class));
                return true;
            } else {
                return false;
            }
        });

        final int[] categoryId = {1};
        int imageRID;
        final String[] categoryTitle = {getResources().getString(R.string.category_title)};

        // dobimo naslov, sliko in id izbrane kategorije
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category_title") && intent.hasExtra("category_id")) {
            categoryTitle[0] = intent.getStringExtra("category_title");
            categoryId[0] = intent.getIntExtra("category_id", 1);
            imageRID = intent.getIntExtra("image", R.drawable.placeholder_image);

            TextView catTitleView = findViewById(R.id.chooseQuizCatTitle);
            ImageView image = findViewById(R.id.imageView);
            catTitleView.setText(categoryTitle[0]);
            image.setImageResource(imageRID);
        }

        // gumb -> mešani kviz
        Button mixQuizButton = findViewById(R.id.button_mix_quiz);
        mixQuizButton.setOnClickListener(v -> {
            Intent quizIntent = new Intent(ChooseQuizTypeActivity.this, QuizActivity.class);
            quizIntent.putExtra("category_title", categoryTitle[0]);
            quizIntent.putExtra("category_id", categoryId[0]);
            startActivity(quizIntent);
        });

        // gumb -> seznam vseh vprašanj
        Button listQuestionsButton = findViewById(R.id.button_list_questions);
        listQuestionsButton.setOnClickListener(v -> {
            Intent listIntent = new Intent(ChooseQuizTypeActivity.this, ChooseQuestionActivity.class);
            listIntent.putExtra("category_title", categoryTitle[0]);
            listIntent.putExtra("category_id", categoryId[0]);
            startActivity(listIntent);
        });
    }
}
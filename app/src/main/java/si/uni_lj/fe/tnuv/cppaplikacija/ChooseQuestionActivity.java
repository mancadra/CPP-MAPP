package si.uni_lj.fe.tnuv.cppaplikacija;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class ChooseQuestionActivity extends AppCompatActivity {
    private int categoryId;
    private String categoryTitle;
    PreferencesManager preferencesManager;

    /** @noinspection deprecation*/
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_question);

        ArrayList<Question>[] questions = QuestionsSingleton.getInstance().getQuestionList();

        // dobimo naslov, in id izbrane kategorije ter diplayamo Naslov
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category_title") && intent.hasExtra("category_id")) {
            categoryTitle = intent.getStringExtra("category_title");
            categoryId = intent.getIntExtra("category_id", 1);

            TextView catTitle = findViewById(R.id.catTitleInList);
            catTitle.setText(categoryTitle);
        }

        // nastavimo navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if(categoryId == 15) {
            bottomNavigationView.setSelectedItemId(R.id.nav_favorites);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.nav_invisible);
        }
        // zastarela metoda
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_favorites) {
                Intent i = new Intent(ChooseQuestionActivity.this, ChooseQuestionActivity.class);
                i.putExtra("category_id", 15);
                i.putExtra("category_title", "Moja vprašanja");
                startActivity(i);
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(ChooseQuestionActivity.this, ChooseCategoryActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_history) {
                startActivity(new Intent(ChooseQuestionActivity.this, HistoryActivity.class));
                return true;
            } else {
                return false;
            }
        });

        // inicializacija recycle view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Če je veljavna kategorija
        QuestionAdapter questionAdapter;
        if(categoryId < 15 && categoryId >= 0) {
            Log.d("ChooseQuestionActivity", "Data fetched successfully: " + questions[1].size() + " questions");

            // Seznam vseh vprašanj vseh kategorij
            if (categoryId == 0) {
                List<Question> allQuestions = new ArrayList<>();
                for (ArrayList<Question> list : questions) {
                    allQuestions.addAll(list);
                }
                AllQuestionAdapter allQuestionAdapter = new AllQuestionAdapter(allQuestions);
                allQuestionAdapter.categoryId = categoryId;
                allQuestionAdapter.categoryTitle = categoryTitle;
                recyclerView.setAdapter(allQuestionAdapter);
            }
            else {
                questionAdapter = new QuestionAdapter(questions[categoryId]);
                questionAdapter.categoryId = categoryId;
                questionAdapter.categoryTitle = categoryTitle;
                recyclerView.setAdapter(questionAdapter);
            }
        // priljubljena vprašanja
        } else if (categoryId == 15) {
            preferencesManager = new PreferencesManager(getApplicationContext());
            ArrayList<Question> favoriteQuestions = preferencesManager.getFavoriteQuestions();

                if (favoriteQuestions != null && !favoriteQuestions.isEmpty()) {
                    Log.d("ChooseQuestionActivity", "DataFetch: Favourites fetched successfully: " + favoriteQuestions.size() + " questions");
                } else {
                    Log.e("ChooseQuestionActivity", "DataFetch: No questions fetched or questions array is null.");
                }

                questionAdapter = new QuestionAdapter(favoriteQuestions);
                questionAdapter.categoryId = categoryId;
                questionAdapter.categoryTitle = "Moja vprašanja";
                recyclerView.setAdapter(questionAdapter);

        }
    }
}

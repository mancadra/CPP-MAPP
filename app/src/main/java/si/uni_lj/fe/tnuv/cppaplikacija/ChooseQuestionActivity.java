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
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private AllQuestionAdapter allQuestionAdapter;
    PreferencesManager preferencesManager;
    DatabaseManager databaseManager = new DatabaseManager();

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
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializacija DatabaseManagerja in fetchanje podatkov
        // Če je veljavna kategorija
        if(categoryId < 15 && categoryId >= 0) {
            Log.d("DataFetch", "Data fetched successfully: " + questions[1].size() + " questions");

            // Seznam vseh kategorij
            if (categoryId == 0) {
                List<Question> allQuestions = new ArrayList<>();
                for (ArrayList<Question> list : questions) {
                    allQuestions.addAll(list);
                }
                allQuestionAdapter = new AllQuestionAdapter(allQuestions);
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
        } else if (categoryId == 15) {
            // todo  : display favorites
            preferencesManager = new PreferencesManager(getApplicationContext());
            List<Integer> favoriteQuestionIds = preferencesManager.getFavoriteQuestions();
            databaseManager.getQuestionsByIds(favoriteQuestionIds, favoriteQuestions -> {
                if (favoriteQuestions != null && favoriteQuestions.length > 0 && favoriteQuestions[0] != null) {
                    Log.d("DataFetch", "Data fetched successfully: " + favoriteQuestions[0].size() + " questions");
                } else {
                    Log.e("DataFetch", "No questions fetched or questions array is null.");
                }

                questionAdapter = new QuestionAdapter(favoriteQuestions[0]);
                questionAdapter.categoryId = categoryId;
                questionAdapter.categoryTitle = "Moja vprašanja";
                recyclerView.setAdapter(questionAdapter);
            });
            // vsa vprašanja?
        } else {

        }
    }
}

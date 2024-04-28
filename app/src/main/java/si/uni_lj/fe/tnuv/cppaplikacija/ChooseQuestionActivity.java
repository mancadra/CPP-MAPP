package si.uni_lj.fe.tnuv.cppaplikacija;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ChooseQuestionActivity extends AppCompatActivity {


    private int categoryId;
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_question);

        // dobimo naslov, in id izbrane kategorije ter diplayamo Naslov
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category_title") && intent.hasExtra("category_id")) {
            String categoryTitle = intent.getStringExtra("category_title");
            categoryId = intent.getIntExtra("category_id", 1);

            TextView catTitle = findViewById(R.id.catTitleInList);
            catTitle.setText(categoryTitle);
        }


        // inicializacija recycle view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Inicializacija DatabaseManagerja in fetchanje podatkov
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.getAllQuestions(questions -> {
            Log.d("DataFetch", "Data fetched successfully: " + questions[0].size() + " questions");
            questionAdapter = new QuestionAdapter(questions[categoryId]);
            recyclerView.setAdapter(questionAdapter);

            Log.d("KATEGORIJA", "Category ID: " + categoryId);

        });
        // dobimo naslov, in id izbrane kategorije
        /*Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category_title") && intent.hasExtra("category_id")) {
            String categoryTitle = intent.getStringExtra("category_title");
            categoryId = intent.getIntExtra("category_id", 1);

            TextView catTitle = findViewById(R.id.catTitleInList);
            catTitle.setText(categoryTitle);
        }*/
    }


}

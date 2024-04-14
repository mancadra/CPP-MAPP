package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChooseCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        // inicializacija recycle view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ustvarimo adapter
        List<Category> categoryList = getCategories(); // Get categories from a data source
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoryList);
        recyclerView.setAdapter(categoryAdapter);
    }

    // za zdaj dodamo nekaj podatkov
    private List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Category 1", 10, R.drawable.placeholder_image));
        categories.add(new Category("Category 2", 15, R.drawable.placeholder_image));

        return categories;
    }
}

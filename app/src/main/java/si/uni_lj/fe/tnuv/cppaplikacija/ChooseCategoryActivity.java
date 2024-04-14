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

    // dodajanje kategorij
    private List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Prometni znaki in talne označbe", 10, R.drawable.placeholder_image, 1));
        categories.add(new Category("Križišča", 15, R.drawable.placeholder_image, 2));
        categories.add(new Category("Križišča s semaforji, delo na cesti, krožišča in železniška proga", 15, R.drawable.placeholder_image, 3));
        categories.add(new Category("Ustavljanje, parkiranje in obračanje", 15, R.drawable.placeholder_image, 4));
        categories.add(new Category("Prehitevanje, vožnja mimo in srečanje", 15, R.drawable.placeholder_image, 5));
        categories.add(new Category("Razvrščanje", 15, R.drawable.placeholder_image, 6));
        categories.add(new Category("Vleka priklopnika in vozil v okvari", 15, R.drawable.placeholder_image, 7));
        categories.add(new Category("Specifikacije vozila in voznika", 15, R.drawable.placeholder_image, 8));
        categories.add(new Category("Razmere na cesti", 15, R.drawable.placeholder_image, 9));
        categories.add(new Category("Zakonski predpisi s področja prometa", 15, R.drawable.placeholder_image, 10));
        categories.add(new Category("Psihofizično stanje voznika in moteči dejavniki", 15, R.drawable.placeholder_image, 11));
        categories.add(new Category("Policija", 15, R.drawable.placeholder_image, 12));
        categories.add(new Category("Avtocesta, hitra cesta in varovanje okolja", 15, R.drawable.placeholder_image, 13));



        return categories;
    }
}

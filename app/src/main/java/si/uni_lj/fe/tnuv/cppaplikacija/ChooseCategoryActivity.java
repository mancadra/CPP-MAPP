package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class ChooseCategoryActivity extends AppCompatActivity {

    /** @noinspection deprecation*/
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

        // nastavimo navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // zastarela metoda
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_favorites) {
                Intent intent = new Intent(ChooseCategoryActivity.this, ChooseQuestionActivity.class);
                intent.putExtra("category_id", 15);
                intent.putExtra("category_title", "Moja vprašanja");
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_home) {

                return true;
            } else if (item.getItemId() == R.id.nav_history) {
                startActivity(new Intent(ChooseCategoryActivity.this, HistoryActivity.class));
                return true;
            } else {
                return false;
            }
        });
    }

    // kategorije
    private List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Povadi vse kategorije", 1210, R.drawable.iv_vse, 0));
        categories.add(new Category("Prometni znaki in talne označbe", 10, R.drawable.iv_stop, 1));
        categories.add(new Category("Križišča", 15, R.drawable.iv_krizisce, 2));
        categories.add(new Category("Križišča s semaforji, delo na cesti, krožišča in železniška proga", 15, R.drawable.iv_krizisca_semaforji, 3));
        categories.add(new Category("Ustavljanje, parkiranje in obračanje", 15, R.drawable.iv_parking, 4));
        categories.add(new Category("Prehitevanje, vožnja mimo in srečanje", 15, R.drawable.iv_razvrscanje, 5));
        categories.add(new Category("Razvrščanje", 15, R.drawable.iv_razvrscanje, 6));
        categories.add(new Category("Vleka priklopnika in vozil v okvari", 15, R.drawable.iv_vleka, 7));
        categories.add(new Category("Specifikacije vozila in voznika", 15, R.drawable.iv_vozilo, 8));
        categories.add(new Category("Razmere na cesti", 15, R.drawable.iv_cars, 9));
        categories.add(new Category("Zakonski predpisi s področja prometa", 15, R.drawable.iv_predpisi, 10));
        categories.add(new Category("Psihofizično stanje voznika in moteči dejavniki", 15, R.drawable.iv_psihofizicno, 11));
        categories.add(new Category("Policija", 15, R.drawable.iv_policija, 12));
        categories.add(new Category("Avtocesta, hitra cesta in varovanje okolja", 15, R.drawable.iv_razmere, 13));
        categories.add(new Category("Priljubljena vprašanja", 15, R.drawable.iv_cars, 15));

        return categories;
    }
}

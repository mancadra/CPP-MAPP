package si.uni_lj.fe.tnuv.cppaplikacija;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseQuizTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz_type);

        // dobimo naslov in id izbrane kategorije
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category_title") && intent.hasExtra("category_id")) {
            String categoryTitle = intent.getStringExtra("category_title");
            int categoryId = intent.getIntExtra("category_id", 1);
            int imageRID = intent.getIntExtra("image", R.drawable.placeholder_image);

            TextView catTitleView = findViewById(R.id.chooseQuizCatTitle);
            ImageView image = findViewById(R.id.imageView);
            catTitleView.setText(categoryTitle);
            image.setImageResource(imageRID);
        }
    }
}
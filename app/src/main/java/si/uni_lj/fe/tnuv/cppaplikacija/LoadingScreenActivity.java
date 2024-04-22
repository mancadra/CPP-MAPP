package si.uni_lj.fe.tnuv.cppaplikacija;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LoadingScreenActivity extends AppCompatActivity {
    private static final int LOADING_DELAY_MILLISECONDS = 4000;

    @SuppressLint("MissingInflatedId")
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
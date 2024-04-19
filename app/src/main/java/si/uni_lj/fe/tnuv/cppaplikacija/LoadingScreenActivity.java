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


public class LoadingScreenActivity extends AppCompatActivity {
    private static final int LOADING_DELAY_MILLISECONDS = 4000;
    // Write a message to the database

    // deklaracija sprmelnjivk za bazo in referenca nanjo
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private TextView retrieveTV;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        // inicializacija za bazo in refernce nanjo
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("message");

        // initializing our object class variable.
        retrieveTV = findViewById(R.id.idTVRetrieveData);

        // calling method
        // for getting data.
        getdata();

        // po 4 sekundah se prikaže seznam kategorij
        new Handler().postDelayed(() -> {
            // zagon ChooseCategoryActivity
            Intent intent = new Intent(LoadingScreenActivity.this, ChooseCategoryActivity.class);
            startActivity(intent);
            // konec LoadingScreenActivity
            finish();
        }, LOADING_DELAY_MILLISECONDS);
    }

    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                String value = snapshot.getValue(String.class);

                // after getting the value we are setting
                // our value to our text view in below line.
                retrieveTV.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(LoadingScreenActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class SettingsActivity extends AppCompatActivity {

    TextView current_deadlift_textview;
    TextView current_squat_textview;
    TextView current_ohp_textview;
    TextView current_benchpress_textview;

    EditText deadlift_max_edittext;
    EditText squat_max_edittext;
    EditText ohp_max_edittext;
    EditText benchpress_max_edittext;

    Button settings_max_confirmation_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        current_deadlift_textview = findViewById(R.id.current_deadlift_textview);
        current_squat_textview = findViewById(R.id.current_squat_textview);
        current_ohp_textview = findViewById(R.id.current_ohp_textview);
        current_benchpress_textview = findViewById(R.id.current_benchpress_textview);

        deadlift_max_edittext = findViewById(R.id.deadlift_max_edittext);
        squat_max_edittext = findViewById(R.id.squat_max_edittext);
        ohp_max_edittext = findViewById(R.id.ohp_max_edittext);
        benchpress_max_edittext = findViewById(R.id.benchpress_max_edittext);

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();


        final User found_user = db.userDao().findByName("Bob","Jer");
        current_deadlift_textview.setText("Deadlift: " + found_user.deadliftmax + "lbs");
        current_squat_textview.setText("Squat: " + found_user.squatmax + "lbs");
        current_ohp_textview.setText("OHP: " + found_user.ohpmax + "lbs");
        current_benchpress_textview.setText("Bench: " + found_user.benchpressmax + "lbs");

        findViewById(R.id.settings_max_confirmation_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( !deadlift_max_edittext.getText().toString().matches("") ){
                    found_user.deadliftmax = deadlift_max_edittext.getText().toString();
                    db.userDao().updateDeadlift(found_user.uid,found_user.deadliftmax);
                    //db.userDao().delete(found_user);
                    //db.userDao().insertAll(found_user);
                    current_deadlift_textview.setText("Deadlift: " + found_user.deadliftmax + "lbs");

                }

                if ( !squat_max_edittext.getText().toString().matches("") ){
                    found_user.squatmax = squat_max_edittext.getText().toString();
                    db.userDao().updateSquat(found_user.uid,found_user.squatmax);
                    //db.userDao().delete(found_user);
                    //db.userDao().insertAll(found_user);
                    current_squat_textview.setText("Squat: " + found_user.squatmax + "lbs");

                }

                if ( !ohp_max_edittext.getText().toString().matches("") ){

                    found_user.ohpmax = ohp_max_edittext.getText().toString();
                    db.userDao().updateOHP(found_user.uid,found_user.ohpmax);
                    //db.userDao().delete(found_user);
                    //db.userDao().insertAll(found_user);
                    current_ohp_textview.setText("OHP: " + found_user.ohpmax + "lbs");

                }

                if ( !benchpress_max_edittext.getText().toString().matches("") ){
                    found_user.benchpressmax = benchpress_max_edittext.getText().toString();
                    db.userDao().updateBench(found_user.uid, found_user.benchpressmax);
                    //db.userDao().delete(found_user);
                    //db.userDao().insertAll(found_user);
                    current_benchpress_textview.setText("Bench: " + found_user.benchpressmax + "lbs");

                    User the_user = db.userDao().findByName("Bob","Jer");
                    System.out.println("The user: " + the_user.benchpressmax);

                }
                /*
                db.userDao().delete(found_user);
                db.userDao().insertAll(found_user);
                current_benchpress_textview.setText("Bench: " + found_user.benchpressmax + "lbs");
                current_ohp_textview.setText("OHP: " + found_user.ohpmax + "lbs");
                current_squat_textview.setText("Squat: " + found_user.squatmax + "lbs");
                current_deadlift_textview.setText("Deadlift: " + found_user.deadliftmax + "lbs");

                 */

            }
        });
    }
}

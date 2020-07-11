package com.example.fitness;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Fragment {

    TextView current_deadlift_textview;
    TextView current_squat_textview;
    TextView current_ohp_textview;
    TextView current_benchpress_textview;

    EditText deadlift_max_edittext;
    EditText squat_max_edittext;
    EditText ohp_max_edittext;
    EditText benchpress_max_edittext;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_settings, container, false);


        current_deadlift_textview = (TextView) view.findViewById(R.id.current_deadlift_textview);
        current_squat_textview =  (TextView) view.findViewById(R.id.current_squat_textview);
        current_ohp_textview =  (TextView) view.findViewById(R.id.current_ohp_textview);
        current_benchpress_textview =  (TextView) view.findViewById(R.id.current_benchpress_textview);

        deadlift_max_edittext = (EditText) view.findViewById(R.id.deadlift_max_edittext);
        squat_max_edittext = (EditText) view.findViewById(R.id.squat_max_edittext);
        ohp_max_edittext = (EditText) view.findViewById(R.id.ohp_max_edittext);
        benchpress_max_edittext = (EditText) view.findViewById(R.id.benchpress_max_edittext);

        final AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();


        final User found_user = db.userDao().findByName("Bob", "Jer");
        current_deadlift_textview.setText("Deadlift: " + found_user.deadliftmax + "lbs");
        current_squat_textview.setText("Squat: " + found_user.squatmax + "lbs");
        current_ohp_textview.setText("OHP: " + found_user.ohpmax + "lbs");
        current_benchpress_textview.setText("Bench: " + found_user.benchpressmax + "lbs");

        view.findViewById(R.id.settings_max_confirmation_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!deadlift_max_edittext.getText().toString().matches("")) {
                    found_user.deadliftmax = deadlift_max_edittext.getText().toString();
                    db.userDao().updateDeadlift(found_user.uid, found_user.deadliftmax);
                    current_deadlift_textview.setText("Deadlift: " + found_user.deadliftmax + "lbs");

                }

                if (!squat_max_edittext.getText().toString().matches("")) {
                    found_user.squatmax = squat_max_edittext.getText().toString();
                    db.userDao().updateSquat(found_user.uid, found_user.squatmax);
                    current_squat_textview.setText("Squat: " + found_user.squatmax + "lbs");

                }

                if (!ohp_max_edittext.getText().toString().matches("")) {

                    found_user.ohpmax = ohp_max_edittext.getText().toString();
                    db.userDao().updateOHP(found_user.uid, found_user.ohpmax);
                    current_ohp_textview.setText("OHP: " + found_user.ohpmax + "lbs");

                }

                if (!benchpress_max_edittext.getText().toString().matches("")) {
                    found_user.benchpressmax = benchpress_max_edittext.getText().toString();
                    db.userDao().updateBench(found_user.uid, found_user.benchpressmax);
                    current_benchpress_textview.setText("Bench: " + found_user.benchpressmax + "lbs");

                    User the_user = db.userDao().findByName("Bob", "Jer");
                    System.out.println("The user: " + the_user.benchpressmax);

                }


            }
        });


        return view;
    }


}

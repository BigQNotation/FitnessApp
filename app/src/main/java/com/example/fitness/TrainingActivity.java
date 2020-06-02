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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Math;

public class TrainingActivity extends AppCompatActivity {

    TextView training_header_textview;
    TextView training_current_set;
    TextView training_current_set_number;
    TextView training_max_explanation_textview;
    TextView training_max_offset_textview;
    Button training_max_confirm_button;
    Button training_max_up_button;
    EditText training_calorie_textedit;

    int current_set = 0;
    Boolean main_exercise = true;
    Boolean finished_exercise = false;
    String training_day = "";

    Button training_next_button;
    Button training_previous_button;

    User user;
    int max_offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        training_header_textview = findViewById(R.id.training_header_textview);
        training_current_set = findViewById(R.id.training_current_set);
        training_previous_button = findViewById(R.id.training_previous_button);
        training_next_button = findViewById(R.id.training_next_button);
        training_max_explanation_textview = findViewById(R.id.training_max_explanation_textview);
        training_max_offset_textview = findViewById(R.id.training_max_offset_textview);
        training_max_confirm_button = findViewById(R.id.training_max_confirm_button);
        training_max_up_button = findViewById(R.id.training_max_up_button);
        training_calorie_textedit = findViewById(R.id.training_calorie_textedit);
        training_current_set_number = findViewById(R.id.training_current_set_number);

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        user = db.userDao().findByName("Bob","Jer");
        Dates date = db.datesDao().findById(user.workingdateid);

        // Get the current training day name
        DateFormat simple = new SimpleDateFormat("E");
        Date result = new Date(date.date_name);
        training_day = simple.format(result);

        training_header_textview.setText("Training " + training_day);
        displaySets();

        findViewById(R.id.training_previous_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_set > 0){
                    current_set--;
                    displaySets();
                }
            }
        });

        findViewById(R.id.training_max_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                max_offset += 5;
                training_max_offset_textview.setText("Increase by: " + max_offset + " lbs");
            }
        });

        findViewById(R.id.training_max_confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!training_calorie_textedit.getText().toString().matches("")) {


                    // update max for user if its tues-fri
                    if (training_day.matches("Tue")) {
                        int new_squat = Integer.parseInt(user.squatmax) + max_offset;
                        db.userDao().updateSquat(user.uid, Integer.toString(new_squat));
                    }
                    if (training_day.matches("Wed")) {
                        int new_ohp = Integer.parseInt(user.ohpmax) + max_offset;
                        db.userDao().updateOHP(user.uid, Integer.toString(new_ohp));
                    }
                    if (training_day.matches("Thu")) {
                        int new_deadlift = Integer.parseInt(user.deadliftmax) + max_offset;
                        db.userDao().updateDeadlift(user.uid, Integer.toString(new_deadlift));
                    }
                    if (training_day.matches("Fri")) {
                        int new_bench = Integer.parseInt(user.benchpressmax) + max_offset;
                        db.userDao().updateBench(user.uid, Integer.toString(new_bench));
                    }

                    user = db.userDao().findByName("Bob", "Jer");
                    Dates date = db.datesDao().findById(user.workingdateid);

                    // set stats for today's date
                    int calories = Integer.parseInt(training_calorie_textedit.getText().toString());
                    System.out.println("calories before insertion: " + calories);
                    db.datesDao().updateCalories(date.date_name, calories);
                    db.datesDao().updateBench(date.date_name, Long.parseLong(user.benchpressmax));
                    db.datesDao().updateSquat(date.date_name, Long.parseLong(user.squatmax));
                    db.datesDao().updateDeadlift(date.date_name, Long.parseLong(user.deadliftmax));
                    db.datesDao().updateOHP(date.date_name, Long.parseLong(user.ohpmax));
                    db.datesDao().updateWeightIncrease(date.date_name, max_offset);
                    training_max_offset_textview.setText("Increased by: " + max_offset + " lbs");

                }
            }
        });



        findViewById(R.id.training_next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (main_exercise && current_set < 8){
                    current_set++;
                    displaySets();
                }
                else if (main_exercise && current_set == 8){
                    current_set = 0;
                    main_exercise = false;
                    displaySets();
                }
                else if (!main_exercise && current_set != 7){
                    current_set++;
                    displaySets();
                }
                else if (!main_exercise && current_set == 7){
                    finished_exercise = true;
                    //current_set++;
                    displaySets();
                }




            }
        });






    }
    void displaySets(){
        // Display current training set for respective training day


        if ( training_day.matches("Sun")){
            System.out.println("benchpressmax: " + user.benchpressmax);
            int bench_max = Integer.parseInt(user.benchpressmax);
            double working_bench = (0.85*bench_max);

            int ohp_max = Integer.parseInt(user.ohpmax);
            double working_ohp = (0.85*ohp_max);

            if (current_set == 0 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 8");
            }
            if (current_set == 1 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 2 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 3 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 4 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 5 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.8 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 6 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 8 && main_exercise ){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 8+");
            }

            // overhead press secondary
            if (current_set == 0 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.5 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 1 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.6 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 2 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 3");
            }
            if (current_set == 3 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 4 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 5 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 6 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 8");
            }

              if (finished_exercise){
                training_max_explanation_textview.setVisibility(View.VISIBLE);
                training_max_offset_textview.setVisibility(View.VISIBLE);
                training_max_confirm_button.setVisibility(View.VISIBLE);
                training_max_up_button.setVisibility(View.VISIBLE);
                training_calorie_textedit.setVisibility(View.VISIBLE);
                training_current_set.setText("Main lifts complete.\n Accessory Workout for: Chest, Arms, Back");
            }



        }
        // Monday
        if ( training_day.matches("Mon")){
            System.out.println("benchpressmax: " + user.benchpressmax);
            int bench_max = Integer.parseInt(user.benchpressmax);
            double working_bench = (0.85*bench_max);

            int ohp_max = Integer.parseInt(user.ohpmax);
            double working_ohp = (0.85*ohp_max);

            if (current_set == 0 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 8");
            }
            if (current_set == 1 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 2 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 3 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 4 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 5 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.8 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 6 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 8 && main_exercise ){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench press " + Double.toString(final_number) + " per side for 8+");
            }

            // overhead press secondary
            if (current_set == 0 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.5 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 1 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.6 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 2 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 3");
            }
            if (current_set == 3 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 4 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 5 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 6 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 8");
            }

            if (finished_exercise){
                training_max_explanation_textview.setVisibility(View.VISIBLE);
                training_max_offset_textview.setVisibility(View.VISIBLE);
                training_max_confirm_button.setVisibility(View.VISIBLE);
                training_max_up_button.setVisibility(View.VISIBLE);
                training_calorie_textedit.setVisibility(View.VISIBLE);
                training_current_set.setText("Main lifts complete. \nAccessory Workout for: Chest, Arms, Back");
            }

        }



        // Tuesday
        if ( training_day.matches("Tue")){
            int squat_max = Integer.parseInt(user.squatmax);
            double working_squat = (0.85*squat_max);

            int sumo_max = Integer.parseInt(user.deadliftmax);
            double working_sumo = (0.85*sumo_max);

            if (current_set == 0 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 8");
            }
            if (current_set == 1 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 2 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 3 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 4 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 5 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.8 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 6 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 8 && main_exercise ){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 8+");
            }

            // overhead press secondary
            if (current_set == 0 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.5 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 1 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.6 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 2 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 3");
            }
            if (current_set == 3 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 4 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 5 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 6 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 8");
            }

            if (finished_exercise){
                training_max_explanation_textview.setVisibility(View.VISIBLE);
                training_max_offset_textview.setVisibility(View.VISIBLE);
                training_max_confirm_button.setVisibility(View.VISIBLE);
                training_max_up_button.setVisibility(View.VISIBLE);
                training_calorie_textedit.setVisibility(View.VISIBLE);
                training_current_set.setText("Main lifts complete. \nAccessory Workout for: Legs, Abs");
            }



        }

        // Wednesday
        if ( training_day.matches("Wed")){
            int ohp_max = Integer.parseInt(user.ohpmax);
            double working_ohp = (0.85*ohp_max);

            int bench_max = Integer.parseInt(user.benchpressmax);
            double working_bench = (0.85*bench_max);

            if (current_set == 0 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 8");
            }
            if (current_set == 1 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 2 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 3 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 4 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 5 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.8 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 6 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 8 && main_exercise ){
                double final_number = 5*(Math.ceil(Math.abs((working_ohp*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Overhead Press " + Double.toString(final_number) + " per side for 8+");
            }

            // overhead press secondary
            if (current_set == 0 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.5 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Incline Bench " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 1 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.6 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Incline Bench " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 2 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Incline Bench " + Double.toString(final_number) + " per side for 3");
            }
            if (current_set == 3 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Incline Bench " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 4 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Incline Bench " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 5 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Incline Bench " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 6 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Incline Bench " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Incline Bench " + Double.toString(final_number) + " per side for 8");
            }

            if (finished_exercise){
                training_max_explanation_textview.setVisibility(View.VISIBLE);
                training_max_offset_textview.setVisibility(View.VISIBLE);
                training_max_confirm_button.setVisibility(View.VISIBLE);
                training_max_up_button.setVisibility(View.VISIBLE);
                training_calorie_textedit.setVisibility(View.VISIBLE);
                training_current_set.setText("Main lifts complete. \nAccessory Workout for: Shoulders, Chest");
            }



        }

        // Thursday
        if ( training_day.matches("Thu")){
            int deadlift_max = Integer.parseInt(user.deadliftmax);
            double working_deadlift = (0.85*deadlift_max);

            int squat_max = Integer.parseInt(user.squatmax);
            double working_squat = (0.85*squat_max);

            if (current_set == 0 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_deadlift*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Deadlift " + Double.toString(final_number) + " per side for 8");
            }
            if (current_set == 1 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_deadlift*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Deadlift " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 2 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_deadlift*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Deadlift " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 3 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_deadlift*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Deadlift " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 4 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_deadlift*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Deadlift " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 5 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_deadlift*0.8 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Deadlift " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 6 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_deadlift*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Deadlift " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_deadlift*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Deadlift" + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 8 && main_exercise ){
                double final_number = 5*(Math.ceil(Math.abs((working_deadlift*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Deadlift " + Double.toString(final_number) + " per side for 8+");
            }

            // front squat secondary
            if (current_set == 0 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.5 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Front Squat " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 1 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.6 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Front Squat " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 2 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Front Squat " + Double.toString(final_number) + " per side for 3");
            }
            if (current_set == 3 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Front Squat " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 4 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Front Squat " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 5 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Front Squat " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 6 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Front Squat " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Front Squat " + Double.toString(final_number) + " per side for 8");
            }

            if (finished_exercise){
                training_max_explanation_textview.setVisibility(View.VISIBLE);
                training_max_offset_textview.setVisibility(View.VISIBLE);
                training_max_confirm_button.setVisibility(View.VISIBLE);
                training_max_up_button.setVisibility(View.VISIBLE);
                training_calorie_textedit.setVisibility(View.VISIBLE);
                training_current_set.setText("Main lifts complete. \nAccessory Workout for: Back, Abs");
            }



        }

        // Friday
        if ( training_day.matches("Fri")){
            int bench_max = Integer.parseInt(user.benchpressmax);
            double working_bench = (0.85*bench_max);
            double working_cgb = (0.85*bench_max);

            if (current_set == 0 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench Press " + Double.toString(final_number) + " per side for 8");
            }
            if (current_set == 1 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench Press " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 2 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench Press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 3 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench Press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 4 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench Press " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 5 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.8 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench Press " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 6 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench Press " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench Press " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 8 && main_exercise ){
                double final_number = 5*(Math.ceil(Math.abs((working_bench*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Bench Press " + Double.toString(final_number) + " per side for 8+");
            }

            // front squat secondary
            if (current_set == 0 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_cgb*0.5 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Close Grip Bench " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 1 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_cgb*0.6 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Close Grip Bench " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 2 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_cgb*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Close Grip Bench " + Double.toString(final_number) + " per side for 3");
            }
            if (current_set == 3 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_cgb*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Close Grip Bench " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 4 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_cgb*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Close Grip Bench " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 5 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_cgb*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Close Grip Bench " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 6 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_cgb*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Close Grip Bench " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_cgb*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Close Grip Bench " + Double.toString(final_number) + " per side for 8");
            }

            if (finished_exercise){
                training_max_explanation_textview.setVisibility(View.VISIBLE);
                training_max_offset_textview.setVisibility(View.VISIBLE);
                training_max_confirm_button.setVisibility(View.VISIBLE);
                training_max_up_button.setVisibility(View.VISIBLE);
                training_calorie_textedit.setVisibility(View.VISIBLE);
                training_current_set.setText("Main lifts complete. \nAccessory Workout for: Arms, Other");
            }



        }



        // Saturday
        if ( training_day.matches("Sat")){
            int squat_max = Integer.parseInt(user.squatmax);
            double working_squat = (0.85*squat_max);

            int sumo_max = Integer.parseInt(user.deadliftmax);
            double working_sumo = (0.85*sumo_max);

            if (current_set == 0 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 8");
            }
            if (current_set == 1 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 2 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 3 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 4 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.85 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 5 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.8 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 6 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.75 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 8 && main_exercise ){
                double final_number = 5*(Math.ceil(Math.abs((working_squat*0.65 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Squat " + Double.toString(final_number) + " per side for 8+");
            }

            // overhead press secondary
            if (current_set == 0 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.5 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 1 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.6 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 2 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 3");
            }
            if (current_set == 3 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 5");
            }
            if (current_set == 4 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 7");
            }
            if (current_set == 5 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 4");
            }
            if (current_set == 6 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 6");
            }
            if (current_set == 7 && !main_exercise){
                double final_number = 5*(Math.ceil(Math.abs((working_sumo*0.7 - 45)/5)));
                final_number = final_number/2;
                training_current_set.setText("Sumo deadlift " + Double.toString(final_number) + " per side for 8");
            }

            if (finished_exercise){
                training_max_explanation_textview.setVisibility(View.VISIBLE);
                training_max_offset_textview.setVisibility(View.VISIBLE);
                training_max_confirm_button.setVisibility(View.VISIBLE);
                training_max_up_button.setVisibility(View.VISIBLE);
                training_calorie_textedit.setVisibility(View.VISIBLE);
                training_current_set.setText("Main lifts complete. \nAccessory Workout for: Legs, Abs");
            }


        }

        if (main_exercise){

            training_current_set_number.setText("On main exercise. Set " + current_set + " of 8.");
        }
        if (!main_exercise) {
            if (current_set <= 7){
                training_current_set_number.setText("On secondary exercise. Set " + current_set + " of 7.");
            }


        }
    }
}

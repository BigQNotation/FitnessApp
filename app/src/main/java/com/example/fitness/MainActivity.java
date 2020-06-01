package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    CalendarView calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        calendar = findViewById(R.id.main_calendarview);

        // Create user. only needs to be done once
        if (db.userDao().findByName("Bob","Jer") == null){
            User bob = new User();
            bob.firstName = "Bob";
            bob.lastName = "Jer";
            bob.uid = 12341110;
            bob.deadliftmax = "135";
            bob.benchpressmax = "135";
            bob.ohpmax = "135";
            bob.squatmax = "135";
            db.userDao().insertAll(bob);
        }

        final User found_user = db.userDao().findByName("Bob","Jer");

    /*
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                selected_date = String.valueOf(dayOfMonth);
                view.
            }
        });

     */
        findViewById(R.id.progress_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProgressActivity.class));
            }
        });


        findViewById(R.id.training_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();

                // Create new date and insert it into db
                // Once primary key is generated, updated date value
                Dates date = new Dates();
                long date_my_id;
                date_my_id = db.datesDao().insert(date);
                date = db.datesDao().findById(date_my_id);
                // get today. time was previously off by a day but after power outage its fixed?
                // also, cut off the extra hours and minutes so we can easily retrieve days later
                // without knowing the exact time in ms
                long day = (calendar.getDate()) - ((calendar.getDate()+86400000)%86400000);
                db.datesDao().update(date.date_id, day);
                System.out.println("day: " + day);
                // Set user db entry to know what the current working date id is
                db.userDao().updateWDI(found_user.uid, date.date_id);


                startActivity(new Intent(MainActivity.this, TrainingActivity.class));
            }

        });

        findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();

                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }

        });


    }
/*
    public void insertUser() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().insertAll(databaseCreator.getRandomUserList());
                return null;
            }
        }.execute();
    }

 */
}

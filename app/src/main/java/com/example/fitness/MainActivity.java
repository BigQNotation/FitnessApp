package com.example.fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    CalendarView calendar;
    Button menu_button;

    AppDatabase db;
    User found_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        calendar = findViewById(R.id.main_calendarview);

        // drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeActivity()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }



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

        found_user = db.userDao().findByName("Bob","Jer");


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeActivity()).commit();
                break;
            case R.id.nav_training:
                Context context = getApplicationContext();

                // Create new date and insert it into db
                // Once primary key is generated, updated date value
                Dates date = new Dates();
                long date_my_id;
                date_my_id = db.datesDao().insert(date);
                date = db.datesDao().findById(date_my_id);
                // get today. time was previously off by a day but after power outage its fixed?
                // update: nevermind it changed back so i added the day increment back again
                // also, cut off the extra hours and minutes so we can easily retrieve days later
                // without knowing the exact time in ms
                long day = (calendar.getDate() + 86400000) - ((calendar.getDate()+86400000)%86400000);
                db.datesDao().update(date.date_id, day);
                System.out.println("day: " + day);
                // Set user db entry to know what the current working date id is
                db.userDao().updateWDI(found_user.uid, date.date_id);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TrainingActivity()).commit();
                break;
            case R.id.nav_progress:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProgressActivity()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsActivity()).commit();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}

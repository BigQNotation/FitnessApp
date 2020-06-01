package com.example.fitness;

import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Dates.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase implements Runnable {
    public abstract UserDao userDao();
    public abstract DatesDao datesDao();

    @Override
    public void run() {
        userDao();
        datesDao();
    }


}


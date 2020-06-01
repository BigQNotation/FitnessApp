package com.example.fitness;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class Access extends AppCompatActivity implements Runnable {


    AppDatabase db;
    Context context;
    Access(Context mycontext) {
        this.context  = mycontext;
    }


    @Override
    public void run() {
        db = Room.databaseBuilder(context,
                AppDatabase.class, "database-name").build();


        User bob = new User();
        bob.firstName = "Bob";
        bob.lastName = "Jer";
        db.userDao().insertAll(bob);
    }


    public User getUser() {
        return db.userDao().findByName("Bob","Jer");

    }
}

/*
public class Access extends Thread {

    User user;
    AppDatabase db;

    Access(User myuser, AppDatabase mydb) {
        this.user = myuser;
        this.db = mydb;
    }

    public void run() {
        // compute primes larger than minPrime
        db.userDao().insertAll(user);
        User accessed_user = db.userDao().findByName("Bob","Jer");
    }

}
*/
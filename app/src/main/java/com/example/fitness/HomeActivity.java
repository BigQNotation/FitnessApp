package com.example.fitness;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_home, container, false);

        final AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        final User found_user = db.userDao().findByName("Bob", "Jer");

        return view;
    }


}

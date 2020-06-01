package com.example.fitness;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "deadlift_max")
    public String deadliftmax;

    @ColumnInfo(name = "benchpress_max")
    public String benchpressmax;

    @ColumnInfo(name = "squat_max")
    public String squatmax;

    @ColumnInfo(name = "ohp_max")
    public String ohpmax;

    @ColumnInfo(name = "working_dateid")
    public long workingdateid;

}



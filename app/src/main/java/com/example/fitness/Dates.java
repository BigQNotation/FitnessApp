package com.example.fitness;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Dates{
    @PrimaryKey(autoGenerate = true)
    public int date_id;

    @ColumnInfo(name = "date_name")
    public long date_name;

    @ColumnInfo(name = "bench")
    public long bench;

    @ColumnInfo(name = "deadlift")
    public long deadlift;

    @ColumnInfo(name = "squat")
    public long squat;

    @ColumnInfo(name = "ohp")
    public long ohp;

    @ColumnInfo(name = "calories")
    public long calories;

    @ColumnInfo(name = "date_milli")
    public long datemilli;

    @ColumnInfo(name = "weight_increase")
    public long weightincrease;
}

package com.example.fitness;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DatesDao {
    @Query("SELECT * FROM dates")
    List<Dates> getAll();

    @Query("SELECT * FROM dates WHERE date_id IN (:datesIds)")
    List<Dates> loadAllByIds(int[] datesIds);

    @Query("SELECT * FROM dates WHERE date_name LIKE :date_ms LIMIT 1")
    Dates findByDate(long date_ms);

    @Query("SELECT * FROM dates WHERE date_id LIKE :id LIMIT 1")
    Dates findById(long id);

    @Insert
    void insertAll(Dates... dates);

    @Insert
    long insert(Dates dates);

    @Delete
    void delete(Dates dates);

    @Query("UPDATE dates SET date_name = :date WHERE date_id = :id")
    void update(int id, long date);

    @Query("UPDATE dates SET bench = :bench WHERE date_name = :date")
    void updateBench(long date, float bench);

    @Query("UPDATE dates SET squat = :squat WHERE date_name = :date")
    void updateSquat(long date, float squat);

    @Query("UPDATE dates SET deadlift = :deadlift WHERE date_name = :date")
    void updateDeadlift(long date, float deadlift);

    @Query("UPDATE dates SET ohp = :ohp WHERE date_name = :date")
    void updateOHP(long date, float ohp);

    @Query("UPDATE dates SET calories = :calories WHERE date_name = :date")
    void updateCalories(long date, float calories);

    @Query("UPDATE dates SET weight_increase = :weight_offset WHERE date_name = :date")
    void updateWeightIncrease(long date, float weight_offset);

}

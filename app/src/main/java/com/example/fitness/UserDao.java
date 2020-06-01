package com.example.fitness;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE uid LIKE (:userId) LIMIT 1")
    User loadById(int userId);


    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Query("UPDATE user SET benchpress_max = :bench WHERE uid = :id")
    void updateBench(int id, String bench);

    @Query("UPDATE user SET deadlift_max = :deadlift WHERE uid = :id")
    void updateDeadlift(int id, String deadlift);

    @Query("UPDATE user SET squat_max = :squat WHERE uid = :id")
    void updateSquat(int id, String squat);

    @Query("UPDATE user SET ohp_max = :ohp WHERE uid = :id")
    void updateOHP(int id, String ohp);

    @Query("UPDATE user SET working_dateid = :wdi WHERE uid = :id")
    void updateWDI(int id, float wdi);

}

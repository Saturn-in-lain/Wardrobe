package com.wardrob.wardrob.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao
{
    @Query("SELECT * FROM user_table")
    List<UserObject> getAll();

    @Query("SELECT * FROM user_table WHERE uid IN (:userIds)")
    List<UserObject> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user_table WHERE user_name LIKE :first LIMIT 1")
    UserObject findByName(String first);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserObject... item);

    @Delete
    void delete(UserObject item);


    @Query("SELECT * FROM user_table WHERE is_active LIKE 1 LIMIT 1")
    UserObject findActiveUser();

    @Query("UPDATE user_table SET is_active=0 WHERE is_active is 1")
    void resetAllActive();

}

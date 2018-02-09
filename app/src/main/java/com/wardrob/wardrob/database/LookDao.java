package com.wardrob.wardrob.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LookDao
{

    @Query("SELECT * FROM look_table")
    List<LookObject> getAll();

    @Query("SELECT * FROM look_table WHERE uid IN (:Ids)")
    List<LookObject> loadAllByIds(int[] Ids);

    @Query("SELECT * FROM look_table WHERE user_name LIKE :first")
    List<LookObject> findByUserName(String first);

    @Query("SELECT * FROM look_table WHERE look_name LIKE :first LIMIT 1")
    LookObject findByName(String first);

    @Query("SELECT * FROM look_table WHERE uid LIKE :id LIMIT 1")
    LookObject findById(int id);

    @Query("SELECT * FROM look_table WHERE look_finished LIKE :flag LIMIT 1")
    LookObject findFinished(boolean flag);

//    @Query("SELECT * FROM item_table WHERE item_main_category LIKE :category AND user_name LIKE :user")
//    List<LookObject> findItemsByCategoryAndUser(String category, String user);


    @Insert
    void insertAll(LookObject... item);

    @Delete
    void delete(LookObject item);


}

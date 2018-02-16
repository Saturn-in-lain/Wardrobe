package com.wardrob.wardrob.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ItemDao
{
    @Query("SELECT * FROM item_table")
    List<ItemObject> getAll();

    @Query("SELECT * FROM item_table WHERE uid IN (:userIds)")
    List<ItemObject> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM item_table WHERE user_name LIKE :first LIMIT 1")
    ItemObject findByName(String first);

    @Query("SELECT * FROM item_table WHERE uid LIKE :id LIMIT 1")
    ItemObject findById(int id);

    @Query("SELECT * FROM item_table WHERE item_main_category LIKE :category AND user_name LIKE :user")
    List<ItemObject> findItemsByCategoryAndUser(String category, String user);

    @Insert
    void insertAll(ItemObject... item);

    @Delete
    void delete(ItemObject item);

}

package com.wardrob.wardrob.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

//TODO: User should have unique name or we must not give ooportunity to create same name

@Entity (tableName = "user_table")
public class UserObject
{
    @PrimaryKey(autoGenerate = true)
    private int uid;
    public int getUid() {return uid;}
    public void setUid(int uid) {this.uid = uid;}
    //=============================================================
    @ColumnInfo(name = "user_name")
    private String userName;
    public void setUserName(String name)
    { userName = name;}
    public String getUserName()
    { return userName;}
    //=============================================================
    @ColumnInfo(name = "user_sex")
    private String userGender;
    public void setUserGender(String name)
    { userGender = name;}
    public String getUserGender()
    { return userGender;}
    //=============================================================
    @ColumnInfo(name = "user_avatar")
    private String userImageName;
    public void setUserImageName(String name)
    { userImageName = name;}
    public String getUserImageName()
    { return userImageName;}
    //=============================================================
    @ColumnInfo(name = "is_active")
    private Integer isActive;
    public void setIsActive(Integer Active)
    { isActive = Active;}
    public Integer getIsActive()
    { return isActive;}
    //=============================================================

}

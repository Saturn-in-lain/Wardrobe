package com.wardrob.wardrob.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "look_table")
public class LookObject
{
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "look_name")
    private String lookName;
    @ColumnInfo(name = "look_hat")
    private int lookHatObject;
    @ColumnInfo(name = "look_accessories")
    private int lookAccessoriesObject;
    @ColumnInfo(name = "look_upperlevel")
    private int lookUpperLevelObject;
    @ColumnInfo(name = "look_lowerlevel")
    private int lookLowerLevelObject;
    @ColumnInfo(name = "look_warm")
    private int lookWarmLevelObject;
    @ColumnInfo(name = "look_boots")
    private int lookBootsLevelObject;
    @ColumnInfo(name = "look_finished")
    private boolean isFinished;
    //=============================================================
    public void setUid(int id)
    { uid = id;}
    public int getUid()
    { return uid;}
    //=============================================================
    public void setLookName(String name)
    { lookName = name;}
    public String getLookName()
    { return lookName;}
    //=============================================================
    public void setLookHatObject(int id)
    { lookHatObject = id;}
    public int getLookHatObject()
    { return lookHatObject;}
    //=============================================================
    public void setLookAccessoriesObject(int id)
    { lookAccessoriesObject = id;}
    public int getLookAccessoriesObject()
    { return lookAccessoriesObject;}
    //=============================================================
    public void setLookUpperLevelObject(int id)
    { lookUpperLevelObject = id;}
    public int getLookUpperLevelObject()
    { return lookUpperLevelObject;}
    //=============================================================
    public void setLookLowerLevelObject(int id)
    { lookLowerLevelObject = id;}
    public int getLookLowerLevelObject()
    { return lookLowerLevelObject;}
    //=============================================================
    public void setLookWarmLevelObject(int id)
    { lookWarmLevelObject = id;}
    public int getLookWarmLevelObject()
    { return lookWarmLevelObject;}
    //=============================================================
    public void setLookBootsLevelObject(int id)
    { lookBootsLevelObject = id;}
    public int getLookBootsLevelObject()
    { return lookBootsLevelObject;}
    //=============================================================
    public void setIsFinished(boolean f)
    { isFinished = f;}
    public boolean getIsFinished()
    { return isFinished;}
    //=============================================================
}

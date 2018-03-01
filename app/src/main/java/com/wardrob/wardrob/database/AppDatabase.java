package com.wardrob.wardrob.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ItemObject.class, UserObject.class, LookObject.class}, version = 17)
public abstract class AppDatabase extends RoomDatabase
{
    private static AppDatabase INSTANCE;

    public abstract ItemDao itemDao();
    public abstract UserDao userDao();
    public abstract LookDao lookDao();

    private static String database = "item-database";
    public static Context ctx;


    public static AppDatabase getAppDatabase(Context context)
    {
        if (INSTANCE == null)
        {
            ctx = context;
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                                         AppDatabase.class,
                                        database).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance()
    {
        INSTANCE = null;
    }


    /**
     * Function: getDataBasePath
     * @return: currentDBPath
     */
    public String getDataBasePath()
    {
        String currentDBPath = ctx.getDatabasePath(database+".db").getAbsolutePath();
        return currentDBPath;
    }
}

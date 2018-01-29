package com.wardrob.wardrob.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ItemObject.class, UserObject.class, LookObject.class}, version = 16)
public abstract class AppDatabase extends RoomDatabase
{
    private static AppDatabase INSTANCE;

    public abstract ItemDao itemDao();
    public abstract UserDao userDao();
    public abstract LookDao lookDao();

    public static AppDatabase getAppDatabase(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                                         AppDatabase.class,
                                         "item-database").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance()
    {
        INSTANCE = null;
    }

}

// -------------------------------------------------------------------------------------------------
//    private static User addUser(final AppDatabase db, User user) {
//        db.userDao().insertAll(user);
//        return user;
//    }
//
//    private static void populateWithTestData(AppDatabase db) {
//        User user = new User();
//        user.setFirstName("Ajay");
//        user.setLastName("Saini");
//        user.setAge(25);
//        addUser(db, user);
//    }
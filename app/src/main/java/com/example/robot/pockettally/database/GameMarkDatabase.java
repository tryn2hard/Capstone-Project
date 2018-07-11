package com.example.robot.pockettally.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {GameMark.class}, version = 1, exportSchema = false)
public abstract class GameMarkDatabase extends RoomDatabase {

    private static final String LOG_TAG = GameMark.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "gameMarkList";
    private static GameMarkDatabase sInstance;

    public static GameMarkDatabase getsInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        GameMarkDatabase.class, GameMarkDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract GameMarkDao gameMarkDao();
}

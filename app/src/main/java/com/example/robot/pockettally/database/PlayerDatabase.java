package com.example.robot.pockettally.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {Player.class}, version = 1, exportSchema = false)
@TypeConverters({ClosedMarksConverter.class, MarkCountsConverter.class})
public abstract class PlayerDatabase extends RoomDatabase{

    private static final String LOG_TAG = PlayerDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "playerlist";
    private static PlayerDatabase sInstance;

    public static PlayerDatabase getsInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        PlayerDatabase.class, PlayerDatabase.DATABASE_NAME)
                        // Todo don't forget to remove this. It is only to be used for seeing that the database works
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract PlayerDao playerDao();
}

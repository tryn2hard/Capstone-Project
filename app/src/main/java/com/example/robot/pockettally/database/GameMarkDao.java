package com.example.robot.pockettally.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GameMarkDao {

    @Query("SELECT * FROM gameMarks ORDER BY markID")
    List<GameMark> loadAllGameMarks();

    @Insert
    void insertGameMark(GameMark gameMark);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGameMark(GameMark gameMark);

    @Delete
    void deleteGameMark(GameMark gameMark);

    @Query("DELETE FROM gameMarks")
    void delete();
}

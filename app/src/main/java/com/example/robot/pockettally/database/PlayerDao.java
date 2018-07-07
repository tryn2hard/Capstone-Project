package com.example.robot.pockettally.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {

    @Query("SELECT * FROM players ORDER BY playerId")
    List<Player> loadAllPlayers();

    @Insert
    void insertPlayer(Player player);

    @Insert
    void insertAllPlayers(List<Player> players);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlayer(Player player);

    @Delete
    void deletePlayer(Player player);

    @Query("DELETE FROM players")
    void delete();
}

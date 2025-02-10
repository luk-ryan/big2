package com.example.big2.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.big2.data.entities.Game;

@Dao
public interface GameDao {
    @Insert
    long insertGame(Game game);

    @Query("SELECT * FROM games WHERE isCompleted = 0 ORDER BY startTime DESC LIMIT 1")
    Game getOngoingGame();

    @Query("UPDATE games SET isCompleted = 1 WHERE gameId = :gameId")
    void completeGame(int gameId);

    @Update
    void updateGame(Game game);
}

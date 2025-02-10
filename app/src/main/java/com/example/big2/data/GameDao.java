package com.example.big2.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.big2.data.entities.Game;

import java.util.List;

@Dao
public interface GameDao {
    @Insert
    long insertGame(Game game);
    @Update
    void updateGame(Game game);
    @Delete
    void deleteGame(Game game);
    @Query("SELECT * FROM games WHERE isCompleted = 0")
    Game getOngoingGame();
    @Query("UPDATE games SET isCompleted = 1 WHERE gameId = :gameId")
    void completeGame(int gameId);
    @Query("SELECT * FROM games")
    List<Game> getAllGames();
    @Query("SELECT * FROM games WHERE gameId = :gameId")
    Game getGameById(long gameId);
}

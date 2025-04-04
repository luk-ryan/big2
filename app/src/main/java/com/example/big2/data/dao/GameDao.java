package com.example.big2.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.big2.data.entity.Game;

import java.util.List;

@Dao
public interface GameDao {

    // Insert a new game
    @Insert
    void insert(Game game);

    // Update an existing game
    @Update
    void update(Game game);

    @Query("UPDATE games SET s1 = :totalS1, s2 = :totalS2, s3 = :totalS3, s4 = :totalS4 WHERE gameId = :gameId")
    void updateTotalScores(int gameId, int totalS1, int totalS2, int totalS3, int totalS4);

    @Query("UPDATE games SET gameName = :title, p1 = :p1, p2 = :p2, p3 = :p3, p4 = :p4, cardValue = :cardValue WHERE gameId = :gameId")
    void updateGameHeaders(int gameId, String title, String p1, String p2, String p3, String p4, double cardValue);

    // Update isCompleted field only by gameId
    @Query("UPDATE games SET isCompleted = :isCompleted WHERE gameId = :gameId")
    void updateIsCompleted(int gameId, boolean isCompleted);

    // Delete a game
    @Delete
    void delete(Game game);
    @Query("DELETE FROM games WHERE gameId = :gameId")
    void deleteByGameId(int gameId);

    // Get all games (ordered by ID in descending order)
    @Query("SELECT * FROM games ORDER BY gameId DESC")
    LiveData<List<Game>> getAllGames();

    // Get a specific game by ID
    @Query("SELECT * FROM games WHERE gameId = :id LIMIT 1")
    LiveData<Game> getGameById(int id);

    // Get all completed games
    @Query("SELECT * FROM games WHERE isCompleted = 1 ORDER BY gameId DESC")
    LiveData<List<Game>> getCompletedGames();

    // Get all ongoing games
    @Query("SELECT * FROM games WHERE isCompleted = 0 ORDER BY gameId DESC")
    LiveData<List<Game>> getOngoingGames();
}

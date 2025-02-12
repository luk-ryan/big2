package com.example.big2.data.dao;

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

    // Delete a game
    @Delete
    void delete(Game game);

    // Get all games (ordered by ID in descending order)
    @Query("SELECT * FROM games ORDER BY gameId DESC")
    List<Game> getAllGames();

    // Get a specific game by ID
    @Query("SELECT * FROM games WHERE gameId = :id LIMIT 1")
    Game getGameById(int id);

    // Get all completed games
    @Query("SELECT * FROM games WHERE isCompleted = 1 ORDER BY gameId DESC")
    List<Game> getCompletedGames();

    // Get all ongoing games
    @Query("SELECT * FROM games WHERE isCompleted = 0 ORDER BY gameId DESC")
    List<Game> getOngoingGames();
}

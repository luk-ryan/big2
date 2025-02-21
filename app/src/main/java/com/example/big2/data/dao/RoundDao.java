package com.example.big2.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.big2.data.entity.Round;

import java.util.List;

@Dao
public interface RoundDao {

    // Insert a new round
    @Insert
    void insert(Round round);

    // Update an existing round
    @Update
    void update(Round round);

    // Delete a round
    @Delete
    void delete(Round round);

    // Get all rounds for a specific game, ordered by round number
    @Query("SELECT * FROM rounds WHERE gameId = :gameId ORDER BY roundNumber ASC")
    LiveData<List<Round>> getRoundsByGameId(int gameId);

    // Get a specific round by its ID
    @Query("SELECT * FROM rounds WHERE roundId = :roundId LIMIT 1")
    LiveData<Round> getRoundById(int roundId);

    // Get the most recent round for a specific game
    @Query("SELECT * FROM rounds WHERE gameId = :gameId ORDER BY roundNumber DESC LIMIT 1")
    LiveData<Round> getMostRecentRound(int gameId);
}

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

    // Update all round numbers after one is deleted
    @Query("UPDATE rounds SET roundNumber = roundNumber - 1 WHERE gameId = :gameId AND roundNumber > :deletedRoundNumber")
    void updateRoundNumbersAfterDeletion(int gameId, int deletedRoundNumber);

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

    // Get the most recent round for a specific game
    @Query("SELECT * FROM rounds WHERE gameId = :gameId ORDER BY roundNumber DESC LIMIT 1")
    Round getMostRecentRoundSync(int gameId);  // Synchronous version

    // Get total scores for all rounds of each player (4 players)
    @Query("SELECT SUM(s1) FROM rounds WHERE gameId = :gameId")
    int getTotalS1ForGame(int gameId);

    @Query("SELECT SUM(s2) FROM rounds WHERE gameId = :gameId")
    int getTotalS2ForGame(int gameId);

    @Query("SELECT SUM(s3) FROM rounds WHERE gameId = :gameId")
    int getTotalS3ForGame(int gameId);

    @Query("SELECT SUM(s4) FROM rounds WHERE gameId = :gameId")
    int getTotalS4ForGame(int gameId);
}

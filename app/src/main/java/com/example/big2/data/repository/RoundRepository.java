package com.example.big2.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.big2.data.AppDatabase;
import com.example.big2.data.dao.GameDao;
import com.example.big2.data.dao.RoundDao;
import com.example.big2.data.entity.Round;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RoundRepository {

    private RoundDao roundDao;
    private GameDao gameDao;

    public RoundRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        roundDao = db.roundDao();
        gameDao = db.gameDao();
    }

    // Insert a new round into the database (default insert)
    public void insert(Round round) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            roundDao.insert(round);
            updateGameTotalScores(round.getGameId());
        });
    }

    // Insert a new round with auto-incrementing roundNumber
    public void insertWithAutoRoundNumber(int gameId, int s1, int s2, int s3, int s4) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            int newRoundNumber = getNextRoundNumber(gameId);
            Round newRound = new Round(gameId, newRoundNumber, s1, s2, s3, s4);
            insert(newRound);
        });
    }

    // Fetch the next round number synchronously in a background thread
    private int getNextRoundNumber(int gameId) {
        Future<Round> future = AppDatabase.databaseWriteExecutor.submit(() -> roundDao.getMostRecentRoundSync(gameId));
        try {
            Round latestRound = future.get();
            return (latestRound != null) ? latestRound.getRoundNumber() + 1 : 1;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 1;
        }
    }

    // Update an existing round
    public void update(Round round) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            roundDao.update(round);
            updateGameTotalScores(round.getGameId());
        });
    }

    // Delete a round
    public void delete(Round round) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            roundDao.delete(round);
            roundDao.updateRoundNumbersAfterDeletion(round.getGameId(), round.getRoundNumber());
            updateGameTotalScores(round.getGameId());
        });
    }

    // Delete all rounds for a specific gameId
    public void deleteRoundsByGameId(int gameId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            roundDao.deleteRoundsByGameId(gameId);
            updateGameTotalScores(gameId);
        });
    }

    // Get all rounds for a specific game, ordered by round number
    public LiveData<List<Round>> getRoundsByGameId(int gameId) {
        return roundDao.getRoundsByGameId(gameId);  // Directly return LiveData from DAO
    }

    // Get the most recent round for a specific game
    public LiveData<Round> getMostRecentRound(int gameId) {
        return roundDao.getMostRecentRound(gameId);  // Directly return LiveData from DAO
    }

    private void updateGameTotalScores(int gameId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            int totalS1 = roundDao.getTotalS1ForGame(gameId);
            int totalS2 = roundDao.getTotalS2ForGame(gameId);
            int totalS3 = roundDao.getTotalS3ForGame(gameId);
            int totalS4 = roundDao.getTotalS4ForGame(gameId);

            gameDao.updateTotalScores(gameId, totalS1, totalS2, totalS3, totalS4);
        });
    }
}

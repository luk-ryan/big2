package com.example.big2.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.big2.data.AppDatabase;
import com.example.big2.data.dao.RoundDao;
import com.example.big2.data.entity.Round;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RoundRepository {

    private RoundDao roundDao;

    public RoundRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        roundDao = db.roundDao();
    }

    // Insert a new round into the database (default insert)
    public void insert(Round round) {
        AppDatabase.databaseWriteExecutor.execute(() -> roundDao.insert(round));
    }

    // Insert a new round with auto-incrementing roundNumber
    public void insertWithAutoRoundNumber(int gameId, int s1, int s2, int s3, int s4) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            int newRoundNumber = getNextRoundNumber(gameId);
            Round newRound = new Round(gameId, newRoundNumber, s1, s2, s3, s4);
            roundDao.insert(newRound);
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
        AppDatabase.databaseWriteExecutor.execute(() -> roundDao.update(round));
    }

    // Delete a round
    public void delete(Round round) {
        AppDatabase.databaseWriteExecutor.execute(() -> roundDao.delete(round));
    }

    // Get all rounds for a specific game, ordered by round number
    public LiveData<List<Round>> getRoundsByGameId(int gameId) {
        return roundDao.getRoundsByGameId(gameId);  // Directly return LiveData from DAO
    }

    // Get the most recent round for a specific game
    public LiveData<Round> getMostRecentRound(int gameId) {
        return roundDao.getMostRecentRound(gameId);  // Directly return LiveData from DAO
    }
}

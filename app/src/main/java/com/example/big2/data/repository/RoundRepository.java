package com.example.big2.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.big2.data.AppDatabase;
import com.example.big2.data.dao.RoundDao;
import com.example.big2.data.entity.Round;

import java.util.List;

public class RoundRepository {

    private RoundDao roundDao;

    public RoundRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        roundDao = db.roundDao();
    }

    // Insert a new round into the database
    public void insert(Round round) {
        AppDatabase.databaseWriteExecutor.execute(() -> roundDao.insert(round));
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

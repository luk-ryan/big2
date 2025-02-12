package com.example.big2.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.big2.data.AppDatabase;
import com.example.big2.data.dao.RoundDao;
import com.example.big2.data.entity.Round;

import java.util.List;

public class RoundRepository {

    private RoundDao roundDao;
    private MutableLiveData<List<Round>> roundsByGameIdLiveData;
    private MutableLiveData<Round> mostRecentRoundLiveData;

    public RoundRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        roundDao = db.roundDao();

        roundsByGameIdLiveData = new MutableLiveData<>();
        mostRecentRoundLiveData = new MutableLiveData<>();
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
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Round> rounds = roundDao.getRoundsByGameId(gameId);
            roundsByGameIdLiveData.postValue(rounds);
        });
        return roundsByGameIdLiveData;
    }

    // Get the most recent round for a specific game
    public LiveData<Round> getMostRecentRound(int gameId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Round round = roundDao.getMostRecentRound(gameId);
            mostRecentRoundLiveData.postValue(round);
        });
        return mostRecentRoundLiveData;
    }
}

package com.example.big2.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.big2.data.AppDatabase;
import com.example.big2.data.dao.GameDao;
import com.example.big2.data.entity.Game;

import java.util.List;

public class GameRepository {

    private GameDao gameDao;
    private MutableLiveData<List<Game>> allGamesLiveData;
    private MutableLiveData<List<Game>> completedGamesLiveData;
    private MutableLiveData<List<Game>> ongoingGamesLiveData;

    public GameRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        gameDao = db.gameDao();

        allGamesLiveData = new MutableLiveData<>();
        completedGamesLiveData = new MutableLiveData<>();
        ongoingGamesLiveData = new MutableLiveData<>();
    }

    // Insert a new game into the database
    public void insert(Game game) {
        AppDatabase.databaseWriteExecutor.execute(() -> gameDao.insert(game));
    }

    // Update an existing game
    public void update(Game game) {
        AppDatabase.databaseWriteExecutor.execute(() -> gameDao.update(game));
    }

    // Delete a game
    public void delete(Game game) {
        AppDatabase.databaseWriteExecutor.execute(() -> gameDao.delete(game));
    }

    // Get all games (ordered by gameId in descending order)
    public LiveData<List<Game>> getAllGames() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Game> games = gameDao.getAllGames();
            allGamesLiveData.postValue(games);
        });
        return allGamesLiveData;
    }

    // Get a specific game by ID
    public LiveData<Game> getGameById(int gameId) {
        MutableLiveData<Game> gameLiveData = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Game game = gameDao.getGameById(gameId);
            gameLiveData.postValue(game);
        });
        return gameLiveData;
    }

    // Get all completed games
    public LiveData<List<Game>> getCompletedGames() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Game> completedGames = gameDao.getCompletedGames();
            completedGamesLiveData.postValue(completedGames);
        });
        return completedGamesLiveData;
    }

    // Get all ongoing games
    public LiveData<List<Game>> getOngoingGames() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Game> ongoingGames = gameDao.getOngoingGames();
            ongoingGamesLiveData.postValue(ongoingGames);
        });
        return ongoingGamesLiveData;
    }
}

package com.example.big2.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.big2.data.AppDatabase;
import com.example.big2.data.dao.GameDao;
import com.example.big2.data.entity.Game;

import java.util.List;

public class GameRepository {

    private GameDao gameDao;

    public GameRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        gameDao = db.gameDao();
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
        return gameDao.getAllGames();  // Directly return LiveData from DAO
    }

    // Get a specific game by ID
    public LiveData<Game> getGameById(int gameId) {
        return gameDao.getGameById(gameId);  // Directly return LiveData from DAO
    }

    // Get all completed games
    public LiveData<List<Game>> getCompletedGames() {
        return gameDao.getCompletedGames();  // Directly return LiveData from DAO
    }

    // Get all ongoing games
    public LiveData<List<Game>> getOngoingGames() {
        return gameDao.getOngoingGames();  // Directly return LiveData from DAO
    }
}

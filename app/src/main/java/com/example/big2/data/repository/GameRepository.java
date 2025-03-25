package com.example.big2.data.repository;

import android.app.Application;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.big2.data.AppDatabase;
import com.example.big2.data.dao.GameDao;
import com.example.big2.data.entity.Game;

import java.util.ArrayList;
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

    // Get pairs of players and their scores in order
    public LiveData<List<Pair<String, Integer>>> getSortedScoresWithPlayers(int gameId) {
        return Transformations.map(gameDao.getGameById(gameId), game -> {
            if (game != null) {
                List<Pair<String, Integer>> playerScores = new ArrayList<>();
                playerScores.add(new Pair<>("P1", game.getS1()));
                playerScores.add(new Pair<>("P2", game.getS2()));
                playerScores.add(new Pair<>("P3", game.getS3()));
                playerScores.add(new Pair<>("P4", game.getS4()));

                // Stable sort by score in descending order (same order for ties)
                playerScores.sort((a, b) -> {
                    int scoreComparison = Integer.compare(b.second, a.second); // Sort by score descending
                    return scoreComparison != 0 ? scoreComparison : Integer.compare(a.first.compareTo(b.first), 0);
                });

                return playerScores;
            }
            return new ArrayList<>();
        });
    }

}

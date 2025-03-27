package com.example.big2.ui.viewmodel;

import android.app.Application;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.big2.data.entity.Game;
import com.example.big2.data.repository.GameRepository;

import java.util.List;

public class GameViewModel extends AndroidViewModel {

    private GameRepository gameRepository;
    private LiveData<List<Game>> allGames;
    private LiveData<List<Game>> completedGames;
    private LiveData<List<Game>> ongoingGames;

    public GameViewModel(Application application) {
        super(application);
        gameRepository = new GameRepository(application);

        // Initialize LiveData with data from the repository
        allGames = gameRepository.getAllGames();
        completedGames = gameRepository.getCompletedGames();
        ongoingGames = gameRepository.getOngoingGames();
    }

    // Insert a new game
    public void insert(Game game) {
        gameRepository.insert(game);
    }

    // Update an existing game
    public void update(Game game) {
        gameRepository.update(game);
    }

    // Delete a game
    public void delete(Game game) {
        gameRepository.delete(game);
    }

    // Delete a game by Game Id
    public void deleteGameById(int gameId) { gameRepository.deleteGameById(gameId); }

    // Get all games (ordered by gameId in descending order)
    public LiveData<List<Game>> getAllGames() {
        return allGames;
    }

    // Get a specific game by ID
    public LiveData<Game> getGameById(int gameId) {
        return gameRepository.getGameById(gameId);
    }

    // Get all completed games
    public LiveData<List<Game>> getCompletedGames() {
        return completedGames;
    }

    // Get all ongoing games
    public LiveData<List<Game>> getOngoingGames() {
        return ongoingGames;
    }

    // Get sorted scores with corresponding player names
    public LiveData<List<Pair<String, Integer>>> getSortedScoresWithPlayers(int gameId) {
        return gameRepository.getSortedScoresWithPlayers(gameId);
    }
}

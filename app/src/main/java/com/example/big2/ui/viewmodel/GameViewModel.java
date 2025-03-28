package com.example.big2.ui.viewmodel;

import android.app.Application;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.big2.data.entity.Game;
import com.example.big2.data.repository.GameRepository;

import java.util.List;
import java.util.concurrent.Executors;

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

    // Update game title, Names and Card Value
    public void updateGameHeaders(int gameId, String title, String p1, String p2, String p3, String p4, double cardValue) {
        Executors.newSingleThreadExecutor().execute(() -> {
            gameRepository.updateGameTitle(gameId, title, p1, p2, p3, p4, cardValue);
        });
    }

    // Update isCompleted (either finish a game or continue a game)
    public void updateIsCompleted(int gameId, boolean isCompleted) {
        gameRepository.updateIsCompleted(gameId, isCompleted);
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

    public LiveData<Double> calculatePlayerTotal(int gameId, String player) {
        return Transformations.map(gameRepository.getGameById(gameId), game -> {
            if (game != null) {
                double playerTotal;

                switch (player) {
                    case "P1":
                        playerTotal = (3 * game.getS1() - (game.getS2() + game.getS3() + game.getS4())) * -1;
                        break;
                    case "P2":
                        playerTotal = (3 * game.getS2() - (game.getS1() + game.getS3() + game.getS4())) * -1;
                        break;
                    case "P3":
                        playerTotal = (3 * game.getS3() - (game.getS1() + game.getS2() + game.getS4())) * -1;
                        break;
                    case "P4":
                        playerTotal = (3 * game.getS4() - (game.getS1() + game.getS2() + game.getS3())) * -1;
                        break;
                    default:
                        return -1.0; // Invalid player
                }

                return playerTotal * game.getCardValue();
            }
            return 0.0;
        });
    }


}

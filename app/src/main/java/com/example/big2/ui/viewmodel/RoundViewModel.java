package com.example.big2.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.big2.data.entity.Round;
import com.example.big2.data.repository.RoundRepository;

import java.util.List;

public class RoundViewModel extends AndroidViewModel {

    private RoundRepository roundRepository;
    private LiveData<List<Round>> roundsByGameId;
    private LiveData<Round> mostRecentRound;

    public RoundViewModel(Application application) {
        super(application);
        roundRepository = new RoundRepository(application);

        // Initialize LiveData with data from the repository
        roundsByGameId = roundRepository.getRoundsByGameId(0); // default, will be updated later based on actual gameId
        mostRecentRound = roundRepository.getMostRecentRound(0); // default, will be updated later based on actual gameId
    }

    // Insert a new round
    public void insert(Round round) {
        roundRepository.insert(round);
    }

    // Update an existing round
    public void update(Round round) {
        roundRepository.update(round);
    }

    // Delete a round
    public void delete(Round round) {
        roundRepository.delete(round);
    }

    // Get all rounds for a specific game, ordered by round number
    public LiveData<List<Round>> getRoundsByGameId(int gameId) {
        roundsByGameId = roundRepository.getRoundsByGameId(gameId);
        return roundsByGameId;
    }

    // Get the most recent round for a specific game
    public LiveData<Round> getMostRecentRound(int gameId) {
        mostRecentRound = roundRepository.getMostRecentRound(gameId);
        return mostRecentRound;
    }
}

package com.example.big2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.big2.R;
import com.example.big2.ui.viewmodel.GameViewModel;
import com.example.big2.ui.viewmodel.RoundViewModel;

public class GameplayActivity extends AppCompatActivity {

    private TextView tvTitle, tvP1, tvP2, tvP3, tvP4, tvS1, tvS2, tvS3, tvS4, tvRoundNumber;
    private TextView tvP1Input, tvP2Input, tvP3Input, tvP4Input;
    private NumberPicker npP1, npP2, npP3, npP4;
    private Button btnSummary, btnBack, btnNext;
    private GameViewModel gameViewModel;
    private RoundViewModel roundViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);

        // Initialize ViewModels
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        roundViewModel = new ViewModelProvider(this).get(RoundViewModel.class);

        // Score Views
        tvTitle = findViewById(R.id.title);
        tvP1 = findViewById(R.id.tvP1);
        tvP2 = findViewById(R.id.tvP2);
        tvP3 = findViewById(R.id.tvP3);
        tvP4 = findViewById(R.id.tvP4);
        tvS1 = findViewById(R.id.tvS1);
        tvS2 = findViewById(R.id.tvS2);
        tvS3 = findViewById(R.id.tvS3);
        tvS4 = findViewById(R.id.tvS4);
        tvRoundNumber = findViewById(R.id.tvRoundNumber);

        // Input Text Views
        tvP1Input = findViewById(R.id.tvP1Input);
        tvP2Input = findViewById(R.id.tvP2Input);
        tvP3Input = findViewById(R.id.tvP3Input);
        tvP4Input = findViewById(R.id.tvP4Input);

        // NumberPicker Views
        npP1 = findViewById(R.id.npP1Score);
        npP2 = findViewById(R.id.npP2Score);
        npP3 = findViewById(R.id.npP3Score);
        npP4 = findViewById(R.id.npP4Score);

        // Button Views
        btnBack = findViewById(R.id.btnBack);
        btnSummary = findViewById(R.id.btnSummary);
        btnNext = findViewById(R.id.btnNext);

        // Retrieve gameId from the Intent
        int gameId = getIntent().getIntExtra("gameId", -1);

        if (gameId == -1) {
            // Show an error message and close the activity if gameId is invalid
            Toast.makeText(this, "Error: Invalid Game ID", Toast.LENGTH_SHORT).show();
            finish(); // Closes the activity
        }

        // Observe game details and update UI
        gameViewModel.getGameById(gameId).observe(this, game -> {
            if (game != null) {
                tvTitle.setText(game.getGameName());

                tvP1.setText(game.getP1());
                tvP2.setText(game.getP2());
                tvP3.setText(game.getP3());
                tvP4.setText(game.getP4());

                tvS1.setText(String.valueOf(game.getS1()));
                tvS2.setText(String.valueOf(game.getS2()));
                tvS3.setText(String.valueOf(game.getS3()));
                tvS4.setText(String.valueOf(game.getS4()));

                tvP1Input.setText(game.getP1());
                tvP2Input.setText(game.getP2());
                tvP3Input.setText(game.getP3());
                tvP4Input.setText(game.getP4());

            } else {
                tvTitle.setText("Game not found.");
            }
        });

        // Observe the most recent round for the given gameId
        roundViewModel.getMostRecentRound(gameId).observe(this, round -> {
            int nextRoundNumber = (round != null) ? round.getRoundNumber() + 1 : 1;
            tvRoundNumber.setText("Round: " + nextRoundNumber);
        });

        // Configure NumberPickers
        setupNumberPicker(npP1);
        setupNumberPicker(npP2);
        setupNumberPicker(npP3);
        setupNumberPicker(npP4);

        // Next Round Button - Adds selected scores to the current round and moves to the next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if exactly one number picker is set to zero
                int zeroCount = 0;
                if (npP1.getValue() == 0) zeroCount++;
                if (npP2.getValue() == 0) zeroCount++;
                if (npP3.getValue() == 0) zeroCount++;
                if (npP4.getValue() == 0) zeroCount++;

                // Only proceed if exactly one picker is set to zero
                if (zeroCount == 1) {
                    // Insert the new round with number picker values
                    roundViewModel.insert(gameId,
                            getScore(npP1.getValue()),
                            getScore(npP2.getValue()),
                            getScore(npP3.getValue()),
                            getScore(npP4.getValue())
                    );

                    // Reset the NumberPickers to zero
                    npP1.setValue(0);
                    npP2.setValue(0);
                    npP3.setValue(0);
                    npP4.setValue(0);

                    // Refresh the round number UI
                    roundViewModel.getMostRecentRound(gameId).observe(GameplayActivity.this, round -> {
                        int nextRoundNumber = (round != null) ? round.getRoundNumber() + 1 : 1;
                        tvRoundNumber.setText("Round: " + nextRoundNumber);

                        // Animate the round number for a clear transition
                        tvRoundNumber.setAlpha(0f); // Start invisible
                        tvRoundNumber.animate().alpha(1f).setDuration(1000); // Fade-in animation

                    });

                    // Update the player scores UI after inserting the round
                    tvS1.setText(String.valueOf(npP1.getValue()));
                    tvS2.setText(String.valueOf(npP2.getValue()));
                    tvS3.setText(String.valueOf(npP3.getValue()));
                    tvS4.setText(String.valueOf(npP4.getValue()));

                } else if (zeroCount > 1) {
                    // Show a toast or a message that Too many people are set to zero
                    Toast.makeText(GameplayActivity.this, "Only one person can be the winner of a round", Toast.LENGTH_SHORT).show();
                } else {
                    // Show a toast or a message that no one's number is set to zero
                    Toast.makeText(GameplayActivity.this, "One person must be the winner of a round to move on", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Game Summary Button - Navigate to Game Summary Activity
        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameplayActivity.this, GameSummaryActivity.class);
                intent.putExtra("gameId", gameId);
                startActivity(intent);
            }
        });

        // Back button - closes activity and sends user back to main menu
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupNumberPicker(NumberPicker numberPicker) {
        numberPicker.setMinValue(0);  // Set minimum value
        numberPicker.setMaxValue(13); // Set maximum value (adjust as needed)
        numberPicker.setWrapSelectorWheel(false);
    }

    // Helper method to calculate score based on the value
    private int getScore(int value) {
        if (value == 11) {
            return value * 2;  // Score = 11 * 2 = 22
        } else if (value == 12) {
            return value * 2;  // Score = 12 * 2 = 24
        } else if (value == 13) {
            return value * 3;  // Score = 13 * 3 = 39
        } else {
            return value;      // Default, just return the number if no special rule applies
        }
    }
}

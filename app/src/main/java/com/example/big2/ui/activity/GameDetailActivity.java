package com.example.big2.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.big2.R;
import com.example.big2.data.entity.Game;
import com.example.big2.ui.viewmodel.GameViewModel;

public class GameDetailActivity extends AppCompatActivity {

    private TextView tvGameDetails;
    private Button btnBack;
    private GameViewModel gameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_details);

        // Initialize ViewModel
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        // Initialize Views
        tvGameDetails = findViewById(R.id.tvGameDetails);  // Make sure to use the correct ID for the TextView
        btnBack = findViewById(R.id.btnBack);

        // Retrieve gameId from the Intent
        int gameId = getIntent().getIntExtra("gameId", -1);

        if (gameId != -1) {
            // Observe the LiveData returned by getGameById
            gameViewModel.getGameById(gameId).observe(this, game -> {
                if (game != null) {
                    // Set the TextView to display the game details
                    tvGameDetails.setText("Game ID: " + game.getGameId() + "\nGame Name: " + game.getGameName());
                } else {
                    tvGameDetails.setText("Game not found.");
                }
            });
        } else {
            tvGameDetails.setText("Invalid game ID.");
        }

        // Back button - closes activity and sends user back to main menu
        btnBack.setOnClickListener(v -> finish());
    }
}
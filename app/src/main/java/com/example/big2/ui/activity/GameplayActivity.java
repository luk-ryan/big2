package com.example.big2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.big2.R;
import com.example.big2.ui.viewmodel.GameViewModel;

public class GameplayActivity extends AppCompatActivity {

    private TextView tvGameDetails;
    private Button btnSummary, btnBack;
    private GameViewModel gameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);

        // Initialize Views
        btnBack = findViewById(R.id.btnBack);
        btnSummary = findViewById(R.id.btnSummary);
        tvGameDetails = findViewById(R.id.tvGameDetails);

        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        // Retrieve gameId from the Intent
        int gameId = getIntent().getIntExtra("gameId", -1);

        if (gameId != -1) {
            // Observe the LiveData returned by getGameById
            gameViewModel.getGameById(gameId).observe(this, game -> {
                if (game != null) {
                    tvGameDetails.setText("Game ID: " + game.getGameId() + "\nGame Name: " + game.getGameName());

                } else {
                    tvGameDetails.setText("Game not found.");
                }
            });

        } else {
            tvGameDetails.setText("Invalid game ID.");
        }

        // Game Summary Button - Navigate to Game Summary Activity
        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameplayActivity.this, GameSummaryActivity.class);
                startActivity(intent);
                intent.putExtra("gameId", gameId);
            }
        });

        // Back button - closes activity and sends user back to main menu
        btnBack.setOnClickListener(v -> finish());
    }
}

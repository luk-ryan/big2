package com.example.big2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.big2.R;
import com.example.big2.data.entity.Game;
import com.example.big2.data.entity.Round;
import com.example.big2.ui.viewmodel.GameViewModel;
import com.example.big2.ui.viewmodel.RoundViewModel;

import java.util.List;

public class GameDetailActivity extends AppCompatActivity {

    private TextView tvGameDetails;
    private TextView tvP1Header, tvP2Header, tvP3Header, tvP4Header;
    private Button btnAdd, btnEdit, btnDelete, btnBack;
    private TableLayout tlGameDetails;
    private GameViewModel gameViewModel;
    private RoundViewModel roundViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_details);

        // Initialize ViewModel
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        roundViewModel = new ViewModelProvider(this).get(RoundViewModel.class);

        // Initialize Views
        tvGameDetails = findViewById(R.id.tvGameDetails);
        tlGameDetails = findViewById(R.id.tlGameDetails); // table layout
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);

        // Map Header TextViews
        tvP1Header = findViewById(R.id.tvP1Header);
        tvP2Header = findViewById(R.id.tvP2Header);
        tvP3Header = findViewById(R.id.tvP3Header);
        tvP4Header = findViewById(R.id.tvP4Header);

        // Retrieve gameId from the Intent
        int gameId = getIntent().getIntExtra("gameId", -1);

        if (gameId != -1) {
            // Observe the LiveData returned by getGameById
            gameViewModel.getGameById(gameId).observe(this, game -> {
                if (game != null) {
                    tvGameDetails.setText("Game ID: " + game.getGameId() + "\nGame Name: " + game.getGameName());

                    // Set player names in headers
                    tvP1Header.setText(game.getP1());
                    tvP2Header.setText(game.getP2());
                    tvP3Header.setText(game.getP3());
                    tvP4Header.setText(game.getP4());
                } else {
                    tvGameDetails.setText("Game not found.");
                }
            });

            // Fetch Round Data
            roundViewModel.getRoundsByGameId(gameId).observe(this, this::populateTableWithRounds);
        } else {
            tvGameDetails.setText("Invalid game ID.");
        }

        // Add Round Button
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(GameDetailActivity.this, AddRoundActivity.class);
            intent.putExtra("gameId", gameId);
            startActivity(intent);
        });

        // Back button - closes activity and sends user back to main menu
        btnBack.setOnClickListener(v -> finish());
    }

    private void populateTableWithRounds(List<Round> rounds) {

        // Remove all rows, but leave the headers intact
        int childCount = tlGameDetails.getChildCount();
        for (int i = 1; i < childCount; i++) { // Start from 1 to skip the header row
            tlGameDetails.removeViewAt(1); // Remove from index 1 to avoid removing the header
        }

        for (Round round : rounds) {
            TableRow row = new TableRow(this);

            TextView roundNumber = new TextView(this);
            roundNumber.setText(String.valueOf(round.getRoundNumber()));
            roundNumber.setGravity(android.view.Gravity.CENTER);
            row.addView(roundNumber);

            TextView s1 = new TextView(this);
            s1.setText(String.valueOf(round.getS1()));
            s1.setGravity(android.view.Gravity.CENTER);
            row.addView(s1);

            TextView s2 = new TextView(this);
            s2.setText(String.valueOf(round.getS2()));
            s2.setGravity(android.view.Gravity.CENTER);
            row.addView(s2);

            TextView s3 = new TextView(this);
            s3.setText(String.valueOf(round.getS3()));
            s3.setGravity(android.view.Gravity.CENTER);
            row.addView(s3);

            TextView s4 = new TextView(this);
            s4.setText(String.valueOf(round.getS4()));
            s4.setGravity(android.view.Gravity.CENTER);
            row.addView(s4);

            tlGameDetails.addView(row);
        }
    }

}
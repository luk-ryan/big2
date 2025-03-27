package com.example.big2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.big2.R;
import com.example.big2.data.entity.Round;
import com.example.big2.ui.viewmodel.GameViewModel;
import com.example.big2.ui.viewmodel.RoundViewModel;

import java.util.List;

public class GameSummaryActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvP1Header, tvP2Header, tvP3Header, tvP4Header;
    private TextView tvTotalP1, tvTotalP2, tvTotalP3, tvTotalP4;
    private ImageView ivBack, ivDelete;
    private TableLayout tlGameDetails;
    private GameViewModel gameViewModel;
    private RoundViewModel roundViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_summary);

        // Initialize ViewModels
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        roundViewModel = new ViewModelProvider(this).get(RoundViewModel.class);

        // Initialize Views
        tvTitle = findViewById(R.id.tvTitle);
        ivDelete = findViewById(R.id.ivDelete);
        ivBack = findViewById(R.id.ivBack);
        tlGameDetails = findViewById(R.id.tlGameDetails);

        // Map Header TextViews
        tvP1Header = findViewById(R.id.tvP1Header);
        tvP2Header = findViewById(R.id.tvP2Header);
        tvP3Header = findViewById(R.id.tvP3Header);
        tvP4Header = findViewById(R.id.tvP4Header);

        // Map Total Score TextViews
        tvTotalP1 = findViewById(R.id.tvTotalP1);
        tvTotalP2 = findViewById(R.id.tvTotalP2);
        tvTotalP3 = findViewById(R.id.tvTotalP3);
        tvTotalP4 = findViewById(R.id.tvTotalP4);

        // Retrieve gameId from the Intent
        int gameId = getIntent().getIntExtra("gameId", -1);

        if (gameId != -1) {
            // Observe the LiveData returned by getGameById
            gameViewModel.getGameById(gameId).observe(this, game -> {
                if (game != null) {
                    tvTitle.setText(game.getGameName());

                    // Set player names in headers
                    tvP1Header.setText(game.getP1());
                    tvP2Header.setText(game.getP2());
                    tvP3Header.setText(game.getP3());
                    tvP4Header.setText(game.getP4());

                    // Set player total scores
                    tvTotalP1.setText(String.valueOf(game.getS1()));
                    tvTotalP2.setText(String.valueOf(game.getS2()));
                    tvTotalP3.setText(String.valueOf(game.getS3()));
                    tvTotalP4.setText(String.valueOf(game.getS4()));
                } else {
                    tvTitle.setText("Game not found.");
                }
            });

            // Fetch Round Data
            roundViewModel.getRoundsByGameId(gameId).observe(this, this::populateTableWithRounds);
        } else {
            tvTitle.setText("Invalid game ID.");
        }

        // Back button - closes activity and sends user back to main menu
        ivBack.setOnClickListener(v -> finish());
    }

    private void populateTableWithRounds(List<Round> rounds) {

        View totalRow = null;
        // Remove all rows, except for header and total row
        while (tlGameDetails.getChildCount() > 2) {
            tlGameDetails.removeViewAt(1);
        }

        // Check if there is already a total row and store it
        if (tlGameDetails.getChildCount() > 1) {
            totalRow = tlGameDetails.getChildAt(tlGameDetails.getChildCount() - 1);
            tlGameDetails.removeView(totalRow); // Remove it temporarily
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

        // Re-add the total row at the bottom if it exists
        if (totalRow != null) {
            tlGameDetails.addView(totalRow);
        }
    }
}
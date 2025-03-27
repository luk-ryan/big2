package com.example.big2.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.big2.R;
import com.example.big2.ui.adapter.RoundRecyclerViewAdapter;
import com.example.big2.ui.viewmodel.GameViewModel;
import com.example.big2.ui.viewmodel.RoundViewModel;

public class GameSummaryActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView ivBack, ivEditTitle, ivCancelEdit, ivDelete;
    private EditText etTitle;
    private TextView tvP1Header, tvP2Header, tvP3Header, tvP4Header;
    private RecyclerView rvRounds;
    private RoundRecyclerViewAdapter roundRecyclerViewAdapter;
    private TextView tvTotalP1, tvTotalP2, tvTotalP3, tvTotalP4;
    private GameViewModel gameViewModel;
    private RoundViewModel roundViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_summary);

        // Retrieve gameId from the Intent
        int gameId = getIntent().getIntExtra("gameId", -1);

        if (gameId == -1) {
            // Show an error message and close the activity if gameId is invalid
            Toast.makeText(this, "Error: Invalid Game ID", Toast.LENGTH_SHORT).show();
            finish(); // Closes the activity
        }

        // Initialize ViewModels
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        roundViewModel = new ViewModelProvider(this).get(RoundViewModel.class);

        // Initialize Views
        tvTitle = findViewById(R.id.tvTitle);
        ivEditTitle = findViewById(R.id.ivEditTitle);
        etTitle = findViewById(R.id.etTitle);
        ivCancelEdit = findViewById(R.id.ivCancelEdit);

        ivDelete = findViewById(R.id.ivDelete);
        ivBack = findViewById(R.id.ivBack);

        // Map Header TextViews
        tvP1Header = findViewById(R.id.tvP1Header);
        tvP2Header = findViewById(R.id.tvP2Header);
        tvP3Header = findViewById(R.id.tvP3Header);
        tvP4Header = findViewById(R.id.tvP4Header);

        // Round Recycler View Setup
        rvRounds = findViewById(R.id.rvRounds);
        rvRounds.setLayoutManager(new LinearLayoutManager(this));
        roundRecyclerViewAdapter = new RoundRecyclerViewAdapter();
        rvRounds.setAdapter(roundRecyclerViewAdapter);

        // Map Total Score TextViews
        tvTotalP1 = findViewById(R.id.tvTotalP1);
        tvTotalP2 = findViewById(R.id.tvTotalP2);
        tvTotalP3 = findViewById(R.id.tvTotalP3);
        tvTotalP4 = findViewById(R.id.tvTotalP4);

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

        // Observe rounds and update RecyclerView
        roundViewModel.getRoundsByGameId(gameId).observe(this, rounds -> {
            if (rounds != null) {
                roundRecyclerViewAdapter.setRoundsList(rounds);
            }
        });

        // Edit Title Button
        ivEditTitle.setOnClickListener( v ->  enableEdit(tvTitle.getText().toString()));

        etTitle.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                saveEdit(gameId, etTitle.getText().toString().trim());
                return true;
            }
            return false;
        });

        ivCancelEdit.setOnClickListener( v -> cancelEdit());

        // Back Button - closes activity and sends user back to Gameplay
        ivBack.setOnClickListener(v -> finish());

        ivDelete.setOnClickListener(v -> {
            // Create an AlertDialog to confirm deletion
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete this game?")
                    .setCancelable(false) // Prevent dialog from being dismissed when tapping outside
                    .setPositiveButton("Yes", (dialog, id) -> {
                        // If user confirms, delete the game
                        gameViewModel.deleteGameById(gameId);
                        finish();
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        // If user cancels, dismiss the dialog
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        });
    }

    private void enableEdit(String title) {
        etTitle.setText(title); // Pre-fill EditText
        tvTitle.setVisibility(View.GONE);
        ivEditTitle.setVisibility(View.INVISIBLE);
        etTitle.setVisibility(View.VISIBLE);
        ivCancelEdit.setVisibility(View.VISIBLE);
        etTitle.requestFocus();
    }

    private void saveEdit(int gameId, String title) {
        if (!title.isEmpty()) {
            tvTitle.setText(title);

            // Update the game title in the database
            gameViewModel.getGameById(gameId).observe(this, game -> {
                if (game != null) {
                    game.setGameName(title);
                    gameViewModel.update(game);
                }
            });
        }
        cancelEdit();
    }

    private void cancelEdit() {
        etTitle.setVisibility(View.GONE);
        ivCancelEdit.setVisibility(View.GONE);
        ivEditTitle.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
    }
}
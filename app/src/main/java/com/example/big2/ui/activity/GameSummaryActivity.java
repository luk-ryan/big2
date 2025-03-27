package com.example.big2.ui.activity;

import android.animation.ObjectAnimator;
import android.media.Image;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.big2.R;
import com.example.big2.ui.adapter.RoundRecyclerViewAdapter;
import com.example.big2.ui.viewmodel.GameViewModel;
import com.example.big2.ui.viewmodel.RoundViewModel;

import java.util.HashMap;
import java.util.Map;

public class GameSummaryActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView ivBack, ivEditTitle, ivCancelEdit, ivDelete;
    private EditText etTitle;
    private TextView tvP1Header, tvP2Header, tvP3Header, tvP4Header;
    private RecyclerView rvRounds;
    private RoundRecyclerViewAdapter roundRecyclerViewAdapter;
    private TextView tvCardValue, tvTotalP1, tvTotalP2, tvTotalP3, tvTotalP4;
    private TextView tvP1, tvP2, tvP3, tvP4, tvS1, tvS2, tvS3, tvS4;
    private ImageView ivCardValueIcon, ivSuitP1, ivSuitP2, ivSuitP3, ivSuitP4;
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
        tvCardValue = findViewById(R.id.tvCardValue);

        // Card Value Icon
        ivCardValueIcon = findViewById(R.id.ivCardValueIcon);

        // Player Names
        tvP1 = findViewById(R.id.tvP1);
        tvP2 = findViewById(R.id.tvP2);
        tvP3 = findViewById(R.id.tvP3);
        tvP4 = findViewById(R.id.tvP4);

        // Player Scores
        tvS1 = findViewById(R.id.tvS1);
        tvS2 = findViewById(R.id.tvS2);
        tvS3 = findViewById(R.id.tvS3);
        tvS4 = findViewById(R.id.tvS4);

        // Player Suit Ranks
        ivSuitP1 = findViewById(R.id.ivSuitP1);
        ivSuitP2 = findViewById(R.id.ivSuitP2);
        ivSuitP3 = findViewById(R.id.ivSuitP3);
        ivSuitP4 = findViewById(R.id.ivSuitP4);

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

                tvCardValue.setText(String.format("$%.2f", game.getCardValue()));

                tvP1.setText(game.getP1());
                tvP2.setText(game.getP2());
                tvP3.setText(game.getP3());
                tvP4.setText(game.getP4());

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

        gameViewModel.calculatePlayerTotal(gameId, "P1").observe(this, score -> {
            tvS1.setText(formatScore(score));
        });

        gameViewModel.calculatePlayerTotal(gameId, "P2").observe(this, score -> {
            tvS2.setText(formatScore(score));
        });

        gameViewModel.calculatePlayerTotal(gameId, "P3").observe(this, score -> {
            tvS3.setText(formatScore(score));
        });

        gameViewModel.calculatePlayerTotal(gameId, "P4").observe(this, score -> {
            tvS4.setText(formatScore(score));
        });

        animateCardValueIcon(ivCardValueIcon);
        updatePlayerRankVisuals(gameId);

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

    // Helper method to format the score
    private String formatScore(double score) {
        if (score >= 0) {
            return String.format("+ $%.2f", score);  // Positive values
        } else {
            return String.format("- $%.2f", score * -1);  // Negative values
        }
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

    // Animation for Card Value Icon
    public void animateCardValueIcon(ImageView ivCardValueIcon) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(ivCardValueIcon, "rotationY", 0f, 180f, 360f);
        rotate.setDuration(3000);
        rotate.setRepeatCount(ObjectAnimator.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.start();
    }

    private void updatePlayerRankVisuals(int gameId) {
        gameViewModel.getSortedScoresWithPlayers(gameId).observe(this, sortedScores -> {
            if (sortedScores.size() == 4) {
                // Create a map to store the current suit for each player
                Map<String, Integer> previousRankings = new HashMap<>();

                // Create a mapping for player names to their layout IDs
                Map<String, Integer> playerToViewId = new HashMap<>();
                playerToViewId.put("P1", R.id.clP1);
                playerToViewId.put("P2", R.id.clP2);
                playerToViewId.put("P3", R.id.clP3);
                playerToViewId.put("P4", R.id.clP4);

                // Get the parent layout that holds the players
                ConstraintLayout layout = findViewById(R.id.clScore);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(layout);

                // Create a Transition object (ChangeBounds) and set the duration
                Transition changeBounds = new ChangeBounds();
                changeBounds.setDuration(1000); // Set the duration to 1000 milliseconds (1 second) or any other value you prefer

                // Begin the transition animation
                TransitionManager.beginDelayedTransition(layout, changeBounds);  // Add this line for animation

                // Loop through sortedScores in ascending order (lowest to highest)
                for (int i = 0; i < sortedScores.size(); i++) {
                    String player = sortedScores.get(i).first;
                    int score = sortedScores.get(i).second;
                    int playerViewId = playerToViewId.get(player); // Get the corresponding player layout ID

                    // Reset the horizontal position constraint before applying the new layout
                    constraintSet.clear(playerViewId, ConstraintSet.START);
                    constraintSet.clear(playerViewId, ConstraintSet.END);

                    // For the first player, align them to the left of the parent (START)
                    if (i == 0) {
                        constraintSet.connect(playerViewId, ConstraintSet.END, R.id.clScore, ConstraintSet.END);
                    } else {
                        // For the other players, connect the previous player's END to this player's START
                        constraintSet.connect(playerViewId, ConstraintSet.END, playerToViewId.get(sortedScores.get(i - 1).first), ConstraintSet.START);
                    }

                    // For the last player, ensure they are connected to the parent's END
                    if (i == sortedScores.size() - 1) {
                        constraintSet.connect(playerViewId, ConstraintSet.START, R.id.clScore, ConstraintSet.START);
                    }

                    // Set the width for each player's container to a fixed percentage of the parent width
                    constraintSet.constrainPercentWidth(playerViewId, 0.25f); // Ensure each player takes up 25% of the width

                    // Set the image index directly based on the sorted order (lowest to highest)
                    int imageIndex = i; // No reversal needed, just use the current position in the sorted list

                    // Check if the player has a new score, and update only if it's changed
                    if (!previousRankings.containsKey(player) || previousRankings.get(player) != score) {
                        // Update the player's suit image
                        switch (player) {
                            case "P1":
                                ivSuitP1.setImageResource(getSuitForRank(imageIndex));
                                break;
                            case "P2":
                                ivSuitP2.setImageResource(getSuitForRank(imageIndex));
                                break;
                            case "P3":
                                ivSuitP3.setImageResource(getSuitForRank(imageIndex));
                                break;
                            case "P4":
                                ivSuitP4.setImageResource(getSuitForRank(imageIndex));
                                break;
                        }
                    }

                    // Store the current score to track for the next update
                    previousRankings.put(player, score);
                }

                // Apply the constraint changes to move the player views
                constraintSet.applyTo(layout);
            }
        });
    }

    // Helper method to get the view ID based on the player name
    private int getPlayerViewId(String player) {
        switch (player) {
            case "P1":
                return R.id.clP1;
            case "P2":
                return R.id.clP2;
            case "P3":
                return R.id.clP3;
            case "P4":
                return R.id.clP4;
            default:
                return -1;  // Invalid player, you could handle this case more gracefully
        }
    }

    // Helper method to assign suits based on rank (lowest to highest)
    private int getSuitForRank(int rank) {
        switch (rank) {
            case 0:
                return R.drawable.card_suit_diamond;  // Highest score
            case 1:
                return R.drawable.card_suit_club;
            case 2:
                return R.drawable.card_suit_heart;
            case 3:
                return R.drawable.card_suit_spade; // Lowest score
            default:
                return R.drawable.card_suit_diamond;
        }
    }
}
package com.example.big2.ui.activity;

import com.example.big2.ui.utils.DialogUtils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
    private ImageView ivBack, ivEditTitle, ivCancelEdit, ivSaveEdit, ivDelete;
    private EditText etTitle, etP1Header, etP2Header, etP3Header, etP4Header, etCardValue;
    private TextView tvRoundHeader, tvP1Header, tvP2Header, tvP3Header, tvP4Header;
    private RecyclerView rvRounds;
    private RoundRecyclerViewAdapter roundRecyclerViewAdapter;
    private TextView tvCardValue, tvTotalP1, tvTotalP2, tvTotalP3, tvTotalP4;
    private TextView tvP1, tvP2, tvP3, tvP4, tvS1, tvS2, tvS3, tvS4;
    private ImageView ivCardValueIcon, ivSuitP1, ivSuitP2, ivSuitP3, ivSuitP4;
    private Button btnFinishGame, btnRestartGame, btnContinueGame;
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
        ivSaveEdit = findViewById(R.id.ivSaveEdit);

        ivDelete = findViewById(R.id.ivDelete);
        ivBack = findViewById(R.id.ivBack);

        // Header Text Views
        tvRoundHeader = findViewById(R.id.tvRoundHeader);
        tvP1Header = findViewById(R.id.tvP1Header);
        tvP2Header = findViewById(R.id.tvP2Header);
        tvP3Header = findViewById(R.id.tvP3Header);
        tvP4Header = findViewById(R.id.tvP4Header);

        // Header Edit Text Views
        etP1Header = findViewById(R.id.etP1Header);
        etP2Header = findViewById(R.id.etP2Header);
        etP3Header = findViewById(R.id.etP3Header);
        etP4Header = findViewById(R.id.etP4Header);

        // Round Recycler View Setup
        rvRounds = findViewById(R.id.rvRounds);
        rvRounds.setLayoutManager(new LinearLayoutManager(this));
        roundRecyclerViewAdapter = new RoundRecyclerViewAdapter(roundViewModel);
        rvRounds.setAdapter(roundRecyclerViewAdapter);

        // Map Total Score TextViews
        tvTotalP1 = findViewById(R.id.tvTotalP1);
        tvTotalP2 = findViewById(R.id.tvTotalP2);
        tvTotalP3 = findViewById(R.id.tvTotalP3);
        tvTotalP4 = findViewById(R.id.tvTotalP4);

        // Card Value
        tvCardValue = findViewById(R.id.tvCardValue);
        etCardValue = findViewById(R.id.etCardValue);
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

        // Buttons
        btnFinishGame = findViewById(R.id.btnFinishGame);
        btnRestartGame = findViewById(R.id.btnRestartGame);
        btnContinueGame = findViewById(R.id.btnContinueGame);

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

                if (game.isCompleted()) {
                    btnContinueGame.setVisibility(View.VISIBLE);
                    btnFinishGame.setVisibility(View.GONE);
                    switchRestartButtonConstraint(btnContinueGame);
                } else {
                    btnContinueGame.setVisibility(View.GONE);
                    btnFinishGame.setVisibility(View.VISIBLE);
                    switchRestartButtonConstraint(btnFinishGame);
                }
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

        rvRounds.post(() -> {
            // Get the screen height using DisplayMetrics
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenHeight = displayMetrics.heightPixels;  // Get the screen height in pixels

            // Set max height as 60% of the screen height
            int maxHeight = (int) (screenHeight * 0.45);

            // Apply max height to RecyclerView's constraint
            ConstraintLayout parentLayout = (ConstraintLayout) rvRounds.getParent();
            if (parentLayout != null) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(parentLayout);
                constraintSet.constrainMaxHeight(rvRounds.getId(), maxHeight); // Dynamically set max height
                constraintSet.applyTo(parentLayout);
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

        // Edit Mode
        ivEditTitle.setOnClickListener( v ->  toggleEditMode(true));
        ivSaveEdit.setOnClickListener(v ->  saveEdit(gameId));
        ivCancelEdit.setOnClickListener( v -> toggleEditMode(false));
        setEditorActionListeners();

        // Back Button - closes activity and sends user back to Gameplay
        ivBack.setOnClickListener(v -> finish());

        ivDelete.setOnClickListener(v -> DialogUtils.showConfirmationDialog(this, "Delete Game", "Are you sure you want to delete this game?", () -> deleteGame(gameId)));
        btnFinishGame.setOnClickListener(v -> DialogUtils.showConfirmationDialog(this, "Finish Game", "Are you sure you want to finish this game?", () -> finishGame(gameId)));
        btnRestartGame.setOnClickListener(v -> DialogUtils.showConfirmationDialog(this, "Restart Game", "Are you sure you want to restart this game?", () -> restartGame(gameId)));
        btnContinueGame.setOnClickListener(v -> DialogUtils.showConfirmationDialog(this, "Continue Game", "Are you sure you want to continue this game?", () -> continueGame(gameId)));
    }

    // Helper method to format the score
    private String formatScore(double score) {
        if (score >= 0) {
            return String.format("+ $%.2f", score);  // Positive values
        } else {
            return String.format("- $%.2f", score * -1);  // Negative values
        }
    }

    private void saveEdit(int gameId) {
        // Get the values from the EditText fields
        String title = etTitle.getText().toString().trim();
        String p1Header = etP1Header.getText().toString().trim();
        String p2Header = etP2Header.getText().toString().trim();
        String p3Header = etP3Header.getText().toString().trim();
        String p4Header = etP4Header.getText().toString().trim();
        String cardValue = etCardValue.getText().toString().trim();
        String cardValueDisplay = cardValue;

        // Remove the $ symbol for validation and saving
        if (cardValue.startsWith("$")) {
            cardValue = cardValue.substring(1);  // Remove the leading $
        }

        // Check if any of the fields are empty
        if (title.isEmpty() || p1Header.isEmpty() || p2Header.isEmpty() || p3Header.isEmpty() || p4Header.isEmpty() || cardValue.isEmpty()) {
            // Optionally, show a message to the user
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return; // Exit the method if any field is empty
        }

        // Validate if the card value is a valid decimal number
        try {
            double cardValueNumeric = Double.parseDouble(cardValue);

            // Update player headers and title
            tvTitle.setText(title);
            tvP1Header.setText(p1Header);
            tvP2Header.setText(p2Header);
            tvP3Header.setText(p3Header);
            tvP4Header.setText(p4Header);
            tvCardValue.setText(cardValueDisplay);

            // Update database with the new values (including the REAL card value)
            gameViewModel.updateGameHeaders(gameId, title, p1Header, p2Header, p3Header, p4Header, cardValueNumeric);

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

            // Toggle edit mode off
            toggleEditMode(false);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number for card value", Toast.LENGTH_SHORT).show();
        }
    }

    // Toggles between text and edit mode for headers
    private void toggleEditMode(boolean isEditing) {
        int editVisibility = isEditing ? View.VISIBLE : View.GONE;
        int textVisibility = isEditing ? View.GONE : View.VISIBLE;

        // Title Display
        tvTitle.setVisibility(textVisibility);
        ivEditTitle.setVisibility(textVisibility);
        tvRoundHeader.setVisibility(textVisibility);
        rvRounds.setVisibility(textVisibility);

        // Header Display
        tvP1Header.setVisibility(textVisibility);
        tvP2Header.setVisibility(textVisibility);
        tvP3Header.setVisibility(textVisibility);
        tvP4Header.setVisibility(textVisibility);
        tvCardValue.setVisibility(textVisibility);

        // Title Edit
        etTitle.setVisibility(editVisibility);
        ivCancelEdit.setVisibility(editVisibility);
        ivSaveEdit.setVisibility(editVisibility);

        // Header Edit Text
        etP1Header.setVisibility(editVisibility);
        etP2Header.setVisibility(editVisibility);
        etP3Header.setVisibility(editVisibility);
        etP4Header.setVisibility(editVisibility);
        etCardValue.setVisibility(editVisibility);

        if (isEditing) {
            // Set current edit text values and focus title first
            etTitle.setText(tvTitle.getText().toString());
            etTitle.requestFocus();
            etP1Header.setText(tvP1Header.getText().toString());
            etP2Header.setText(tvP2Header.getText().toString());
            etP3Header.setText(tvP3Header.getText().toString());
            etP4Header.setText(tvP4Header.getText().toString());
            etCardValue.setText(tvCardValue.getText().toString());

            // Show keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etTitle, InputMethodManager.SHOW_IMPLICIT);
        } else {
            // Hide Keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etTitle.getWindowToken(), 0);
        }
    }

    // Shift to next input field when pressing done
    private void setEditorActionListeners() {
        // For Title EditText
        etTitle.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Focus goes to P1 header EditText
                etP1Header.requestFocus();
                return true;
            }
            return false;
        });

        // For P1 Header EditText
        etP1Header.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Focus goes to P2 header EditText
                etP2Header.requestFocus();
                return true;
            }
            return false;
        });

        // For P2 Header EditText
        etP2Header.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Focus goes to P3 header EditText
                etP3Header.requestFocus();
                return true;
            }
            return false;
        });

        // For P3 Header EditText
        etP3Header.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Focus goes to P4 header EditText
                etP4Header.requestFocus();
                return true;
            }
            return false;
        });

        // For P4 Header EditText
        etP4Header.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Focus goes to Card Value EditText
                etCardValue.requestFocus();
                return true;
            }
            return false;
        });

        // For Card Value EditText
        etCardValue.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Focus goes back to Title EditText (or save the data if it's the last field)
                etTitle.requestFocus();
                return true;
            }
            return false;
        });

        etCardValue.setSelection(etCardValue.getText().length()); // Place cursor at the end

        etCardValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No need to do anything before text change
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                // Ensure the dollar sign is at the start of the string
                if (charSequence.length() > 0 && !charSequence.toString().startsWith("$")) {
                    etCardValue.setText("$" + charSequence.toString().substring(1));
                    etCardValue.setSelection(etCardValue.getText().length()); // Ensure cursor is at the end
                }

                // Automatically add '0' before decimal if no number exists before it
                String text = charSequence.toString();
                if (text.startsWith("$") && text.length() > 1 && text.indexOf(".") == 1) {
                    // If the text starts with '$' and there's a '.' at index 1 (e.g., '$.'), prepend '0'
                    etCardValue.setText("$0" + text.substring(1));
                    etCardValue.setSelection(etCardValue.getText().length()); // Ensure the cursor is at the end
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Optionally handle other changes here
            }
        });

        // Prevent backspace when the cursor is at the start
        etCardValue.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && etCardValue.getText().toString().startsWith("$") && etCardValue.getSelectionStart() == 1) {
                // Prevent backspace when cursor is at index 1 (immediately after $)
                return true;  // Don't allow the delete action
            }
            return false;
        });
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

    private void switchRestartButtonConstraint(Button referenceButton) {
        ConstraintLayout layout = findViewById(R.id.clButtons);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        constraintSet.clear(R.id.btnRestartGame, ConstraintSet.START);
        constraintSet.connect(R.id.btnRestartGame, ConstraintSet.START, referenceButton.getId(), ConstraintSet.END);

        constraintSet.applyTo(layout);
    }

    private void finishGame(int gameId) {
        gameViewModel.updateIsCompleted(gameId, true);
        Toast.makeText(this, "Game Finished", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void restartGame(int gameId) {
        gameViewModel.updateIsCompleted(gameId, false);
        roundViewModel.deleteRoundsByGameId(gameId);

        // Start GameplayActivity or bring it to the front if it's already running
        Intent intent = new Intent(this, GameplayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("gameId", gameId);
        startActivity(intent);

        Toast.makeText(this, "Game Restarted", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void continueGame(int gameId) {
        gameViewModel.updateIsCompleted(gameId, false);

        Intent intent = new Intent(this, GameplayActivity.class);
        intent.putExtra("gameId", gameId);
        startActivity(intent);

        Toast.makeText(this, "Game Continued", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void deleteGame(int gameId) {
        gameViewModel.deleteGameById(gameId);
        Toast.makeText(this, "Game Deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
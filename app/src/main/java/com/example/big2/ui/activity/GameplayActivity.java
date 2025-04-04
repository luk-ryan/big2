package com.example.big2.ui.activity;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;

import com.example.big2.R;
import com.example.big2.ui.fragment.InfoFragment;
import com.example.big2.ui.utils.DialogUtils;
import com.example.big2.ui.viewmodel.GameViewModel;
import com.example.big2.ui.viewmodel.RoundViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class GameplayActivity extends AppCompatActivity {

    private TextView tvTitle, tvP1, tvP2, tvP3, tvP4, tvS1, tvS2, tvS3, tvS4, tvRoundNumber;
    private ImageView ivBack, ivSuitP1, ivSuitP2, ivSuitP3, ivSuitP4;
    private ImageView ivInfo, ivRules, ivRoundDirection;
    private ImageView ivStar, ivP1Star, ivP2Star, ivP3Star, ivP4Star;
    private TextView tvP1Input, tvP2Input, tvP3Input, tvP4Input;
    private NumberPicker npP1, npP2, npP3, npP4;
    private ImageView ivP1Suit, ivP2Suit, ivP3Suit, ivP4Suit;
    private Button btnSummary, btnFinishGame, btnNext;
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

        ivBack = findViewById(R.id.ivBack);

        // Card Suit Image Views
        ivSuitP1 = findViewById(R.id.ivSuitP1);
        ivSuitP2 = findViewById(R.id.ivSuitP2);
        ivSuitP3 = findViewById(R.id.ivSuitP3);
        ivSuitP4 = findViewById(R.id.ivSuitP4);

        // Info Fragment Button
        ivInfo = findViewById(R.id.ivInfo);
        ivRules = findViewById(R.id.ivRules);

        // Round Control Image Views
        ivRoundDirection = findViewById(R.id.ivRoundDirection);
        ivStar = findViewById(R.id.ivStar);
        ivP1Star = findViewById(R.id.ivP1Star);
        ivP2Star = findViewById(R.id.ivP2Star);
        ivP3Star = findViewById(R.id.ivP3Star);
        ivP4Star = findViewById(R.id.ivP4Star);

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

        // Card Suit Image Views
        ivP1Suit = findViewById(R.id.ivP1Suit);
        ivP2Suit = findViewById(R.id.ivP2Suit);
        ivP3Suit = findViewById(R.id.ivP3Suit);
        ivP4Suit = findViewById(R.id.ivP4Suit);

        // Button Views
        btnFinishGame = findViewById(R.id.btnFinishGame);
        btnSummary = findViewById(R.id.btnSummary);
        btnNext = findViewById(R.id.btnNext);

        // Retrieve gameId from the Intent
        int gameId = getIntent().getIntExtra("gameId", -1);

        if (gameId == -1) {
            finish(); // Closes the activity
        }

        // Observe game details and update UI
        gameViewModel.getGameById(gameId).observe(this, game -> {
            if (game != null) {
                if (game.isCompleted()) {
                    finish(); // Closes GameplayActivity immediately
                } else {
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
                }
            } else {
                tvTitle.setText("Game not found.");
                finish();
            }
        });

        // Update Activity with gameId
        updateCurrentRound(gameId);
        updatePlayerRankVisuals(gameId);

        // Configure NumberPickers
        setupNumberPicker(npP1);
        setupNumberPicker(npP2);
        setupNumberPicker(npP3);
        setupNumberPicker(npP4);

        // Back button - closes activity and sends user back to main menu
        ivBack.setOnClickListener(v -> finish());

        // Info Fragment Button - Opens information fragment
        ivInfo.setOnClickListener(v -> {
            // Begin FragmentTransaction to show the InfoFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new InfoFragment()) // Replace with InfoFragment
                    .addToBackStack(null) // Allows the back button to close it
                    .commit();
        });

        // Rules Button - Navigate to Rules Activity
        ivRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameplayActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });

        // Finish Game Button -
        btnFinishGame.setOnClickListener(v -> {
            // Create a new AlertDialog to confirm finishing the game

            String msgFinish = "Are you sure you want to finish this game? Game will be marked as Completed.";
            DialogUtils.showConfirmationDialog(this, "Finish Game", msgFinish, () -> finishGame(gameId));
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
                            calculateScore(npP1.getValue()),
                            calculateScore(npP2.getValue()),
                            calculateScore(npP3.getValue()),
                            calculateScore(npP4.getValue())
                    );

                    // Reset the NumberPickers to zero
                    npP1.setValue(0);
                    npP2.setValue(0);
                    npP3.setValue(0);
                    npP4.setValue(0);

                    updateCurrentRound(gameId);
                    updatePlayerRankVisuals(gameId);

                } else if (zeroCount > 1) {
                    // Show a message that Too many people are set to zero
                    Snackbar.make(findViewById(android.R.id.content),
                            "Only one person can be the winner of a round (Set one value to 0)",
                            Snackbar.LENGTH_SHORT).show();
                } else {
                    // Show a message that no one's number is set to zero
                    Snackbar.make(findViewById(android.R.id.content),
                            "One person must be the winner of a round to move on (Set one value to 0)",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateCurrentRound(int gameId) {
        // Refresh the round number UI
        roundViewModel.getMostRecentRound(gameId).observe(GameplayActivity.this, round -> {
            int nextRoundNumber = (round != null) ? round.getRoundNumber() + 1 : 1;
            tvRoundNumber.setText("Round: " + nextRoundNumber);

            // Animate the round number for a clear transition
            tvRoundNumber.setAlpha(0f); // Start invisible
            tvRoundNumber.animate().alpha(1f).setDuration(1000); // Fade-in animation

            String winRoundPlayer = "";

            if (round != null) {
                // Get the scores for each player
                int[] scores = {round.getS1(), round.getS2(), round.getS3(), round.getS4()};
                String[] players = {"P1", "P2", "P3", "P4"};

                // Find the player who won the last round (score of 0)
                int maxScore = scores[0];
                winRoundPlayer = players[0];
                for (int i = 1; i < scores.length; i++) {
                    if (scores[i] == 0) {
                        winRoundPlayer = players[i];
                        break;
                    }
                }
            }

            // Reset all stars to invisible
            ivStar.setVisibility(View.INVISIBLE);
            ivP1Star.setVisibility(View.INVISIBLE);
            ivP2Star.setVisibility(View.INVISIBLE);
            ivP3Star.setVisibility(View.INVISIBLE);
            ivP4Star.setVisibility(View.INVISIBLE);

            ImageView star = getPlayerStarId(winRoundPlayer);
            star.setVisibility(View.VISIBLE);

            star.setAlpha(0f); // Start invisible
            star.animate().alpha(1f).setDuration(1000); // Fade-in animation
            TooltipCompat.setTooltipText(star, "First to Play this Round");

            if (nextRoundNumber % 2 == 0) {
                ivRoundDirection.setImageResource(R.drawable.rotate_left);
                TooltipCompat.setTooltipText(ivRoundDirection, "Play is Counter Clockwise");
            } else {
                ivRoundDirection.setImageResource(R.drawable.rotate_right);
                TooltipCompat.setTooltipText(ivRoundDirection, "Play is Clockwise");
            }

            // Animate the round number for a clear transition
            ivRoundDirection.setAlpha(0f); // Start invisible
            ivRoundDirection.animate().alpha(1f).setDuration(1000); // Fade-in animation

            // Start a continuous rotation animation
            ObjectAnimator rotation = ObjectAnimator.ofFloat(ivRoundDirection, "rotation",
                    0f, (nextRoundNumber % 2 == 0) ? -360f : 360f);
            rotation.setDuration(5000);
            rotation.setRepeatCount(ObjectAnimator.INFINITE);
            rotation.setRepeatMode(ObjectAnimator.RESTART);
            rotation.start();
        });
    }

    // Helper method to get the Image view based on player String
    private ImageView getPlayerStarId(String winRoundPlayer) {
        switch (winRoundPlayer) {
            case "P1":
                return ivP1Star;
            case "P2":
                return ivP2Star;
            case "P3":
                return ivP3Star;
            case "P4":
                return ivP4Star;
            default:
                return ivStar;
        }
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
                                setSharedImageP1(getSuitForRank(imageIndex));
                                fadeIn(ivP1Suit);
                                break;
                            case "P2":
                                setSharedImageP2(getSuitForRank(imageIndex));
                                fadeIn(ivP2Suit);
                                break;
                            case "P3":
                                setSharedImageP3(getSuitForRank(imageIndex));
                                fadeIn(ivP3Suit);
                                break;
                            case "P4":
                                setSharedImageP4(getSuitForRank(imageIndex));
                                fadeIn(ivP4Suit);
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

    // Method to set the same image to both ImageViews
    public void setSharedImageP1(int imageResource) {
        ivSuitP1.setImageResource(imageResource);
        ivP1Suit.setImageResource(imageResource);
    }

    // Method to set the same image to both ImageViews
    public void setSharedImageP2(int imageResource) {
        ivSuitP2.setImageResource(imageResource);
        ivP2Suit.setImageResource(imageResource);
    }

    // Method to set the same image to both ImageViews
    public void setSharedImageP3(int imageResource) {
        ivSuitP3.setImageResource(imageResource);
        ivP3Suit.setImageResource(imageResource);
    }

    // Method to set the same image to both ImageViews
    public void setSharedImageP4(int imageResource) {
        ivSuitP4.setImageResource(imageResource);
        ivP4Suit.setImageResource(imageResource);
    }

    // Helper method to Fade in each card Image View
    private void fadeIn(ImageView imageView) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 0.8f);
        fadeIn.setDuration(1000);  // Duration of the fade-in animation
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeIn.start();
    }

    // 4 Number Pickers all have te same options
    private void setupNumberPicker(NumberPicker numberPicker) {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(13);
        numberPicker.setWrapSelectorWheel(false);

    }

    // Helper method to calculate score based on the value
    private int calculateScore(int value) {
        if (value == 11) {
            return value * 2;  // Score = 11 * 2 = 22
        } else if (value == 12) {
            return value * 2;  // Score = 12 * 2 = 24
        } else if (value == 13) {
            return value * 3;  // Score = 13 * 3 = 39
        } else {
            return value;
        }
    }

    private void finishGame(int gameId) {
        gameViewModel.updateIsCompleted(gameId, true);
        Intent intent = new Intent(GameplayActivity.this, GameSummaryActivity.class);
        intent.putExtra("gameId", gameId);
        startActivity(intent);
        finish();
    }
}

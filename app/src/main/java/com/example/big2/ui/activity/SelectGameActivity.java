package com.example.big2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.big2.R;
import com.example.big2.data.entity.Game;
import com.example.big2.ui.adapter.GameRecyclerViewAdapter;
import com.example.big2.ui.adapter.GameStatusAdapter;
import com.example.big2.ui.fragment.CreateGameFragment;
import com.example.big2.ui.utils.DialogUtils;
import com.example.big2.ui.viewmodel.GameViewModel;
import com.example.big2.ui.viewmodel.RoundViewModel;

import java.util.ArrayList;
import java.util.List;

public class SelectGameActivity extends AppCompatActivity {

    private Button btnStart;
    private ImageView ivBack, ivDelete, ivCreate;
    Spinner spinnerFilter;
    private RecyclerView rvGames;
    private GameRecyclerViewAdapter gameRecyclerViewAdapter;
    private GameViewModel gameViewModel;
    private RoundViewModel roundViewModel;
    private TextView tvNoGames;
    private List<Game> allGames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_games);

        // Initialize ViewModel
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        roundViewModel = new ViewModelProvider(this).get(RoundViewModel.class);

        // Initialize Views
        ivCreate = findViewById(R.id.ivCreate);
        btnStart = findViewById(R.id.btnStart);
        ivDelete = findViewById(R.id.ivDelete);
        ivBack = findViewById(R.id.ivBack);
        rvGames = findViewById(R.id.rvGames);
        tvNoGames = findViewById(R.id.tvNoGames);
        spinnerFilter = findViewById(R.id.spinnerFilter);

        // Initialize RecyclerView with gameRecyclerViewAdapter
        rvGames.setLayoutManager(new LinearLayoutManager(this));
        gameRecyclerViewAdapter = new GameRecyclerViewAdapter(this, roundViewModel);
        rvGames.setAdapter(gameRecyclerViewAdapter);

        // Observe LiveData from ViewModel
        gameViewModel.getAllGames().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(List<Game> games) {
                if (games == null || games.isEmpty()) {
                    tvNoGames.setVisibility(View.VISIBLE); // Show "No Games" message
                    rvGames.setVisibility(View.GONE); // Hide RecyclerView
                } else {
                    tvNoGames.setVisibility(View.GONE); // Hide "No Games" message
                    rvGames.setVisibility(View.VISIBLE); // Show RecyclerView
                    gameRecyclerViewAdapter.setGameList(games);
                    allGames = new ArrayList<>(games); // Store all games
                    applyStatusFiltering(); // Apply filter logic
                }
            }
        });

        // Define game status options
        String[] gameStatuses = {"All", "Not Started", "In Progress", "Completed"};

        // Use custom adapter for spinner
        GameStatusAdapter adapter = new GameStatusAdapter(this, gameStatuses);
        spinnerFilter.setAdapter(adapter);
        spinnerFilter.setSelection(0); // Default to "All"

        // Handle Filter Selection
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyStatusFiltering();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Create Game button
        ivCreate.setOnClickListener(v -> {
            // In your Activity (e.g., MainActivity or wherever you want to show the dialog)
            CreateGameFragment createGameFragment = new CreateGameFragment();
            createGameFragment.show(getSupportFragmentManager(), "CreateGameFragment");
        });

        // Start Game Button
        btnStart.setOnClickListener(v -> {

            Game selectedGame = gameRecyclerViewAdapter.getSelectedGame();
            if (selectedGame != null) {
                if (selectedGame.isCompleted()) {
                    // Load the selected game into the GameSummaryActivity
                    Intent intent = new Intent(SelectGameActivity.this, GameSummaryActivity.class);
                    intent.putExtra("gameId", selectedGame.getGameId());
                    startActivity(intent);
                } else {
                    // Load the selected game into the GameplayActivity
                    Intent intent = new Intent(SelectGameActivity.this, GameplayActivity.class);
                    intent.putExtra("gameId", selectedGame.getGameId());
                    startActivity(intent);
                }
            } else {
                Toast.makeText(SelectGameActivity.this, "Please select a game", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete Game Button
        ivDelete.setOnClickListener(v -> {
            Game selectedGame = gameRecyclerViewAdapter.getSelectedGame();
            if (selectedGame != null) {
                DialogUtils.showConfirmationDialog(
                        SelectGameActivity.this,
                        "Delete Game",
                        "Are you sure you want to delete this game?",
                        () -> {
                            gameViewModel.delete(selectedGame);
                            allGames.remove(selectedGame);
                            gameRecyclerViewAdapter.setSelectedPosition(-1);
                            applyStatusFiltering();
                            Toast.makeText(SelectGameActivity.this, "Game deleted", Toast.LENGTH_SHORT).show();
                        }
                );
            } else {
                Toast.makeText(SelectGameActivity.this, "Please select a game", Toast.LENGTH_SHORT).show();
            }
        });


        // Back button - closes activity and sends user back to main menu
        ivBack.setOnClickListener(v -> finish());
    }

    // Filtering method based on game status logic
    private void applyStatusFiltering() {
        String selectedStatus = spinnerFilter.getSelectedItem().toString();
        List<Game> filteredGames = new ArrayList<>();
        LifecycleOwner lifecycleOwner = this;

        for (Game game : allGames) {
            roundViewModel.getRoundsByGameId(game.getGameId()).observe(lifecycleOwner, rounds -> {
                String gameStatus;

                if (game.isCompleted()) {
                    gameStatus = "Completed";
                } else if (rounds == null || rounds.isEmpty()) {
                    gameStatus = "Not Started";
                } else {
                    gameStatus = "In Progress";
                }

                // Filter based on the selected status
                if (selectedStatus.equals("All") || selectedStatus.equals(gameStatus)) {
                    filteredGames.add(game);
                }

                // Update RecyclerView after filtering
                if (filteredGames.isEmpty()) {
                    tvNoGames.setVisibility(View.VISIBLE);
                    rvGames.setVisibility(View.GONE);
                } else {
                    tvNoGames.setVisibility(View.GONE);
                    rvGames.setVisibility(View.VISIBLE);
                    gameRecyclerViewAdapter.setGameList(filteredGames);
                }
            });
        }
    }

}

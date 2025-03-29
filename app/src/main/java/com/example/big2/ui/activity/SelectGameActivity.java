package com.example.big2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.big2.R;
import com.example.big2.data.entity.Game;
import com.example.big2.ui.adapter.GameRecyclerViewAdapter;
import com.example.big2.ui.fragment.CreateGameFragment;
import com.example.big2.ui.viewmodel.GameViewModel;
import com.example.big2.ui.viewmodel.RoundViewModel;

import java.util.List;

public class SelectGameActivity extends AppCompatActivity {

    private Button btnCreate, btnStart, btnDelete;
    private ImageView ivBack;
    private RecyclerView rvGames;
    private GameRecyclerViewAdapter gameRecyclerViewAdapter;
    private GameViewModel gameViewModel;
    private RoundViewModel roundViewModel;
    private TextView tvNoGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_games);

        // Initialize ViewModel
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        roundViewModel = new ViewModelProvider(this).get(RoundViewModel.class);

        // Initialize Views
        btnCreate = findViewById(R.id.btnCreate);
        btnStart = findViewById(R.id.btnStart);
        btnDelete = findViewById(R.id.btnDelete);
        ivBack = findViewById(R.id.ivBack);
        rvGames = findViewById(R.id.rvGames);
        tvNoGames = findViewById(R.id.tvNoGames);

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
                }
            }
        });

        // Create Game button
        btnCreate.setOnClickListener(v -> {
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
        btnDelete.setOnClickListener(v -> {
            Game selectedGame = gameRecyclerViewAdapter.getSelectedGame();
            if (selectedGame != null) {
                new AlertDialog.Builder(SelectGameActivity.this)
                        .setTitle("Delete Game")
                        .setMessage("Are you sure you want to delete this game?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // User confirmed deletion
                            gameViewModel.delete(selectedGame);
                            Toast.makeText(SelectGameActivity.this, "Game deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // User canceled, do nothing
                            dialog.dismiss();
                        })
                        .show();
            } else {
                Toast.makeText(SelectGameActivity.this, "Please select a game", Toast.LENGTH_SHORT).show();
            }
        });

        // Back button - closes activity and sends user back to main menu
        ivBack.setOnClickListener(v -> finish());
    }

}

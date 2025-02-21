package com.example.big2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.big2.R;
import com.example.big2.data.entity.Game;
import com.example.big2.ui.adapter.GameRecyclerViewAdapter;
import com.example.big2.ui.viewmodel.GameViewModel;

import java.util.List;

public class SelectGameActivity extends AppCompatActivity {

    private Button btnCreate, btnLoad, btnBack;
    private RecyclerView recyclerView;
    private GameRecyclerViewAdapter gameRecyclerViewAdapter;
    private GameViewModel gameViewModel;
    private TextView tvNoGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_games);

        // Initialize ViewModel
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        // Initialize Views
        btnCreate = findViewById(R.id.btnCreate);
        btnLoad = findViewById(R.id.btnLoad);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.gamesRecyclerView);
        tvNoGames = findViewById(R.id.tvNoGames);

        // Initialize RecyclerView with gameRecyclerViewAdapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gameRecyclerViewAdapter = new GameRecyclerViewAdapter();
        recyclerView.setAdapter(gameRecyclerViewAdapter);


        // Observe LiveData from ViewModel
        gameViewModel.getAllGames().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(List<Game> games) {
                if (games == null || games.isEmpty()) {
                    tvNoGames.setVisibility(View.VISIBLE); // Show "No Games" message
                    recyclerView.setVisibility(View.GONE); // Hide RecyclerView
                } else {
                    tvNoGames.setVisibility(View.GONE); // Hide "No Games" message
                    recyclerView.setVisibility(View.VISIBLE); // Show RecyclerView
                    gameRecyclerViewAdapter.setGameList(games);
                }
            }
        });

        // Create Game button
        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(SelectGameActivity.this, CreateGameActivity.class);
            startActivityForResult(intent, 1);
        });

        // Load Game Button
        btnLoad.setOnClickListener(v -> {
            Game selectedGame = gameRecyclerViewAdapter.getSelectedGame();
            if (selectedGame != null) {
                // Load the selected game into the GameDetailActivity
                Intent intent = new Intent(SelectGameActivity.this, GameDetailActivity.class);
                intent.putExtra("gameId", selectedGame.getGameId());
                startActivity(intent);
            } else {
                // Show a message or do nothing if no game is selected
                Toast.makeText(SelectGameActivity.this, "Please select a game", Toast.LENGTH_SHORT).show();
            }
        });

        // Back button - closes activity and sends user back to main menu
        btnBack.setOnClickListener(v -> finish());
    }

}

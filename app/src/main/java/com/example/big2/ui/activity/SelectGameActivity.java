package com.example.big2.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    private Button btnBack;
    private RecyclerView recyclerView;
    private GameRecyclerViewAdapter gameAdapter;
    private GameViewModel gameViewModel;
    private TextView tvNoGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_games);

        // Initialize Views
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.gamesRecyclerView);
        tvNoGames = findViewById(R.id.tvNoGames); // "No Games" message

        // Initialize ViewModel
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gameAdapter = new GameRecyclerViewAdapter();
        recyclerView.setAdapter(gameAdapter);

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
                    gameAdapter.setGameList(games);
                }
            }
        });

        // Back button closes activity and sends user back to main menu
        btnBack.setOnClickListener(v -> finish());
    }
}

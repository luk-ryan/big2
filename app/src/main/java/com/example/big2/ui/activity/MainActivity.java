package com.example.big2.ui.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.big2.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
//
//package com.example.big2.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.ListView;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//import com.example.big2.adapters.GameListAdapter;
//import com.example.big2.data.entity.Game;
//import com.example.big2.viewmodel.GameViewModel;
//
//public class MainActivity extends AppCompatActivity {
//
//    private GameViewModel gameViewModel;
//    private GameListAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        ListView gameListView = findViewById(R.id.gameListView);
//        Button newGameButton = findViewById(R.id.newGameButton);
//
//        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
//
//        adapter = new GameListAdapter(this);
//        gameListView.setAdapter(adapter);
//
//        gameViewModel.getAllGames().observe(this, games -> adapter.setGames(games));
//
//        gameListView.setOnItemClickListener((parent, view, position, id) -> {
//            Game selectedGame = adapter.getItem(position);
//            Intent intent = new Intent(MainActivity.this, GameDetailActivity.class);
//            intent.putExtra("gameId", selectedGame.getGameId());
//            startActivity(intent);
//        });
//
//        newGameButton.setOnClickListener(v -> {
//            Game newGame = new Game();
//            long gameId = gameViewModel.insertGame(newGame);
//            Intent intent = new Intent(MainActivity.this, GameDetailActivity.class);
//            intent.putExtra("gameId", gameId);
//            startActivity(intent);
//        });
//    }
//}

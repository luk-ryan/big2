//package com.example.big2.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.ListView;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//import com.example.big2.adapters.RoundListAdapter;
//import com.example.big2.viewmodel.GameViewModel;
//import com.example.big2.viewmodel.RoundViewModel;
//
//public class GameDetailActivity extends AppCompatActivity {
//
//    private GameViewModel gameViewModel;
//    private RoundViewModel roundViewModel;
//    private long gameId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_game_detail);
//
//        gameId = getIntent().getLongExtra("gameId", -1);
//        ListView roundListView = findViewById(R.id.roundListView);
//        Button addRoundButton = findViewById(R.id.addRoundButton);
//        Button finishGameButton = findViewById(R.id.finishGameButton);
//
//        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
//        roundViewModel = new ViewModelProvider(this).get(RoundViewModel.class);
//
//        RoundListAdapter adapter = new RoundListAdapter(this);
//        roundListView.setAdapter(adapter);
//
//        roundViewModel.getRoundsForGame(gameId).observe(this, adapter::setRounds);
//
//        addRoundButton.setOnClickListener(v -> {
//            Intent intent = new Intent(GameDetailActivity.this, AddRoundActivity.class);
//            intent.putExtra("gameId", gameId);
//            startActivity(intent);
//        });
//
//        finishGameButton.setOnClickListener(v -> {
//            gameViewModel.completeGame(gameId);
//            finish();
//        });
//    }
//}

//package com.example.big2.ui.activity;
//
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.lifecycle.Observer;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.widget.TextView;
//import android.widget.Button;
//
//import com.example.big2.R;
//import com.example.big2.data.entity.Game;
//import com.example.big2.data.entity.Round;
//import com.example.big2.ui.viewmodel.GameViewModel;
//import com.example.big2.ui.viewmodel.RoundViewModel;
//import com.example.big2.ui.adapter.RoundAdapter;
//
//import java.util.List;
//
//public class GameDetailActivity extends AppCompatActivity {
//
//    private GameViewModel gameViewModel;
//    private RoundViewModel roundViewModel;
//
//    private TextView gameNameTextView;
//    private TextView gameStatusTextView;
//    private RecyclerView roundsRecyclerView;
//    private Button finishGameButton;
//
//    private RoundAdapter roundAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_game_detail);
//
//        // Initialize Views
//        gameNameTextView = findViewById(R.id.gameNameTextView);
//        gameStatusTextView = findViewById(R.id.gameStatusTextView);
//        roundsRecyclerView = findViewById(R.id.roundsRecyclerView);
//        finishGameButton = findViewById(R.id.finishGameButton);
//
//        // Initialize ViewModels
//        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
//        roundViewModel = new ViewModelProvider(this).get(RoundViewModel.class);
//
//        // Setup RecyclerView for rounds
//        roundAdapter = new RoundAdapter();
//        roundsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        roundsRecyclerView.setAdapter(roundAdapter);
//
//        // Get Game ID from Intent (assumes it's passed via Intent extras)
//        int gameId = getIntent().getIntExtra("GAME_ID", -1);
//
//        // Load game details by gameId
//        loadGameDetails(gameId);
//        loadRounds(gameId);
//
//        // Set up a click listener for the "Finish Game" button
//        finishGameButton.setOnClickListener(v -> finishGame(gameId));
//    }
//
//    private void loadGameDetails(int gameId) {
//        // Observe game data from the ViewModel
//        gameViewModel.getGameById(gameId).observe(this, new Observer<Game>() {
//            @Override
//            public void onChanged(Game game) {
//                if (game != null) {
//                    gameNameTextView.setText(game.getName());
//                    gameStatusTextView.setText(game.getStatus());
//                }
//            }
//        });
//    }
//
//    private void loadRounds(int gameId) {
//        // Observe rounds data from the RoundViewModel
//        roundViewModel.getRoundsByGameId(gameId).observe(this, new Observer<List<Round>>() {
//            @Override
//            public void onChanged(List<Round> rounds) {
//                // Update the RecyclerView with rounds data
//                roundAdapter.setRounds(rounds);
//            }
//        });
//    }
//
//    private void finishGame(int gameId) {
//        // Perform the finish game action (e.g., update game status in database)
//        // This could involve calling an update method on the GameViewModel to change the game status
//        Game game = new Game(gameId, "Finished", "Finished"); // Example
//        gameViewModel.update(game);
//        // You might also want to navigate away from this activity, e.g., finish();
//    }
//}

//package com.example.big2.activity;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//import com.example.big2.data.entity.Round;
//import com.example.big2.viewmodel.RoundViewModel;
//
//public class RoundDetailActivity extends AppCompatActivity {
//
//    private RoundViewModel roundViewModel;
//    private long gameId;
//    private EditText player1Score, player2Score, player3Score, player4Score;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_round);
//
//        gameId = getIntent().getLongExtra("gameId", -1);
//        roundViewModel = new ViewModelProvider(this).get(RoundViewModel.class);
//
//        player1Score = findViewById(R.id.player1Score);
//        player2Score = findViewById(R.id.player2Score);
//        player3Score = findViewById(R.id.player3Score);
//        player4Score = findViewById(R.id.player4Score);
//        Button saveButton = findViewById(R.id.saveButton);
//
//        saveButton.setOnClickListener(v -> {
//            Round round = new Round(gameId,
//                    Integer.parseInt(player1Score.getText().toString()),
//                    Integer.parseInt(player2Score.getText().toString()),
//                    Integer.parseInt(player3Score.getText().toString()),
//                    Integer.parseInt(player4Score.getText().toString()));
//            roundViewModel.insertRound(round);
//            finish();
//        });
//    }
//}

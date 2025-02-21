package com.example.big2.ui.activity;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.big2.R;
import com.example.big2.ui.viewmodel.GameViewModel;
import com.example.big2.ui.viewmodel.RoundViewModel;

public class AddRoundActivity extends AppCompatActivity {

    private EditText etScoreP1, etScoreP2, etScoreP3, etScoreP4;
    private Button btnSaveRound, btnCancel;
    private RoundViewModel roundViewModel;
    private int gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_round);

        // Initialize ViewModel
        roundViewModel = new ViewModelProvider(this).get(RoundViewModel.class);

        // Initialize Views
        etScoreP1 = findViewById(R.id.etScoreP1);
        etScoreP2 = findViewById(R.id.etScoreP2);
        etScoreP3 = findViewById(R.id.etScoreP3);
        etScoreP4 = findViewById(R.id.etScoreP4);
        btnSaveRound = findViewById(R.id.btnSaveRound);
        btnCancel = findViewById(R.id.btnCancel);

        // Retrieve gameId from the Intent
        gameId = getIntent().getIntExtra("gameId", -1);

        if (gameId == -1) {
            finish();
        }

        // Save Round Button Click Listener
        btnSaveRound.setOnClickListener(v -> saveRound());

        // Cancel Button Click Listener
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveRound() {
        try {
            int scoreP1 = Integer.parseInt(etScoreP1.getText().toString());
            int scoreP2 = Integer.parseInt(etScoreP2.getText().toString());
            int scoreP3 = Integer.parseInt(etScoreP3.getText().toString());
            int scoreP4 = Integer.parseInt(etScoreP4.getText().toString());

            // Insert round into database
            roundViewModel.insert(gameId, scoreP1, scoreP2, scoreP3, scoreP4);

            // Show success message
            Toast.makeText(this, "Round saved successfully!", Toast.LENGTH_SHORT).show();

            // Close activity
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid scores", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.big2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.big2.R;
import com.example.big2.data.entity.Game;
import com.example.big2.ui.viewmodel.GameViewModel;

public class CreateGameActivity extends AppCompatActivity {
    private EditText etGameName, etP1, etP2, etP3, etP4, etCardValue;
    private Button btnSave, btnCancel;
    private GameViewModel gameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);

        // Initialize ViewModel
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        // UI Elements
        etGameName = findViewById(R.id.etGameName);
        etP1 = findViewById(R.id.etP1);
        etP2 = findViewById(R.id.etP2);
        etP3 = findViewById(R.id.etP3);
        etP4 = findViewById(R.id.etP4);
        etCardValue = findViewById(R.id.etCardValue);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Save Button
        btnSave.setOnClickListener(v -> saveGame());

        // Cancel Button
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveGame() {
        String gameName = etGameName.getText().toString().trim();
        String p1 = etP1.getText().toString().trim();
        String p2 = etP2.getText().toString().trim();
        String p3 = etP3.getText().toString().trim();
        String p4 = etP4.getText().toString().trim();
        String cardValueStr = etCardValue.getText().toString().trim();

        if (gameName.isEmpty() || p1.isEmpty() || p2.isEmpty() || p3.isEmpty() || p4.isEmpty() || cardValueStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double cardValue = Double.parseDouble(cardValueStr);

        // Create game and insert into database
        Game newGame = new Game(gameName, p1, p2, p3, p4, cardValue);
        gameViewModel.insert(newGame);

        // Notify success and go back
        Toast.makeText(this, "Game added!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}

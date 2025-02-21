package com.example.big2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.big2.R;

public class MainActivity extends AppCompatActivity {

    private Button btnPlayGame, btnRules, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        btnPlayGame = findViewById(R.id.btnPlayGame);
        btnRules = findViewById(R.id.btnRules);
        btnExit = findViewById(R.id.btnExit);

        // Play Game Button - Navigate to Game Activity
        btnPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectGameActivity.class);
                startActivity(intent);
            }
        });

        // Rules Button - Navigate to Rules Activity
        btnRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });

        // Exit Button - Close the app
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // Close all activities
            }
        });
    }
}
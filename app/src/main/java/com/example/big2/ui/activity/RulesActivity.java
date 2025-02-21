package com.example.big2.ui.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.big2.R;

public class RulesActivity extends AppCompatActivity {

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);

        // Initialize Views
        btnBack = findViewById(R.id.btnBack);

        // Back button - closes activity and sends user back to main menu
        btnBack.setOnClickListener(v -> finish());
    }
}

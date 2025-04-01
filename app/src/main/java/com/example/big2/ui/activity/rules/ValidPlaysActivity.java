package com.example.big2.ui.activity.rules;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.big2.R;

public class ValidPlaysActivity extends AppCompatActivity {

    private static final Class<?> nextPage = WinningActivity.class;
    private static final Class<?> prevPage = SetupActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_valid_plays);

        // Back button
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Left and Right navigation buttons functionality
        ImageButton btnNext = findViewById(R.id.btnNext);
        ImageButton btnPrev = findViewById(R.id.btnPrev);

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(ValidPlaysActivity.this, nextPage);

            // this removes the current page from back stack, so that back button leads to rule.xml
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            // Custom animation for nextButton (in right, out left)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        btnPrev.setOnClickListener(v -> {
            Intent intent = new Intent(ValidPlaysActivity.this, prevPage);

            // this removes the current page from back stack, so that back button leads to rule.xml
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            // Custom animation slide from left when going back
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });
    }
}
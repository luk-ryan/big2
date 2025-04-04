package com.example.big2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.big2.R;

public class MainActivity extends AppCompatActivity {

    private Button btnPlayGame, btnRules, btnExit;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        btnPlayGame = findViewById(R.id.btnPlayGame);
        btnRules = findViewById(R.id.btnRules);
        btnExit = findViewById(R.id.btnExit);
        logo = findViewById(R.id.logo);

        // Create a ScaleAnimation to zoom in and then back
        ScaleAnimation zoomInOut = new ScaleAnimation(
                1f, 1.2f, // Start and end values for the X axis scaling
                1f, 1.2f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X (center)
                Animation.RELATIVE_TO_SELF, 0.5f  // Pivot Y (center)
        );
        zoomInOut.setDuration(1000); // total duration of one cycle (zoom in + zoom out)
        zoomInOut.setRepeatCount(1);
        zoomInOut.setRepeatMode(Animation.REVERSE); // reverse animation to zoom back
        logo.startAnimation(zoomInOut);


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
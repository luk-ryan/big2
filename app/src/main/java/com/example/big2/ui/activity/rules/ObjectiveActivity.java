package com.example.big2.ui.activity.rules;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.big2.R;

public class ObjectiveActivity extends AppCompatActivity {

    private static final Class<?> nextPage = SetupActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_objective);

        // Back button
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnNext = findViewById(R.id.btnNext);
        ImageButton btnPrev = findViewById(R.id.btnPrev);

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(ObjectiveActivity.this, nextPage);

            // this removes the current page from back stack, so that back button leads to rule.xml
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            // Custom animation for nextButton (in right, out left)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        // Since Objective is the first rule, hide prevButton
        btnPrev.setVisibility(View.INVISIBLE);
    }
}
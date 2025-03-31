package com.example.big2.ui.activity.rules;

/* Still a work in progress. Trying to apply gesture motions to move
 * between activities. */

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.big2.R;

public class ObjectiveActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_objective);

        // Back button
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        /*
        // Optional: arrow button to go to SetupActivity
        ImageButton btnNext = findViewById(R.id.btnNext); // Add to XML if not present
        if (btnNext != null) {
            btnNext.setOnClickListener(v -> onSwipeLeft());
        }

        ImageButton btnPrev = findViewById(R.id.btnPrev); // Optional: should be hidden
        if (btnPrev != null) {
            btnPrev.setOnClickListener(v ->
                    Toast.makeText(this, "This is the first rule.", Toast.LENGTH_SHORT).show()
            );
        } */

        // Gesture detector for swipe gestures
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private void onSwipeLeft() {
        Intent intent = new Intent(this, SetupActivity.class); // Go to next
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void onSwipeRight() {
        Toast.makeText(this, "This is the first rule.", Toast.LENGTH_SHORT).show();
    }

}
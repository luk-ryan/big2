package com.example.big2.ui.activity;

/* Main rule page activity functionality:
*  -------------------------------------
*  > Rule cards list initialization
*  > Onclick event handlers
*  > Save scroll position on activity
*  > Scroll bar
*  */

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import com.example.big2.R;
import com.example.big2.ui.adapter.RulesRecyclerViewAdapter;
import com.example.big2.ui.model.RulesCard;
import java.util.ArrayList;
import java.util.List;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class RulesActivity extends AppCompatActivity {

    private Button btnBack;
    private RecyclerView rulesRecyclerView;
    private LinearLayout dotsLayout;
    private ImageView[] dots;

    // 🔐 Keys for saving scroll state/position
    public static final String PREFS_NAME = "RulesPrefs";
    public static final String LAST_POSITION_KEY = "LastScrollPosition";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialize Views
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);
        rulesRecyclerView = findViewById(R.id.rulesRecyclerView);
        //scrollProgress = findViewById(R.id.scrollProgress);

        // Back button - closes activity and sends user back to main menu
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // List of visible cards for the rules (add more here...)
        List<RulesCard> rules = new ArrayList<>();

        String descr_objective = "Learn the goal and rules of Big 2.";
        String descr_setup = "Learn how to start a game of Big 2.";
        String descr_plays = "Outlines the types of card combinations that can be played. ";
        String descr_win = "Details how a round ends and how penalty points are given. ";
        String descr_score = "Outlines how points are calculated after each round, and optional rules when playing with money.";

        rules.add(new RulesCard(R.drawable.card_suit_spade, "The Objective of The Game", descr_objective, "A", "♠"));
        rules.add(new RulesCard(R.drawable.card_suit_heart, "Game Setup", descr_setup, "2", "♥"));
        rules.add(new RulesCard(R.drawable.card_suit_club, "Valid Plays", descr_plays, "3", "♣"));
        rules.add(new RulesCard(R.drawable.card_suit_diamond, "Winning a Round", descr_win, "4", "♦"));
        rules.add(new RulesCard(R.drawable.card_suit_spade, "Scoring System", descr_score, "5", "♠"));

        // Initialize RecyclerView Adapters
        RulesRecyclerViewAdapter adapter = new RulesRecyclerViewAdapter(this, rules);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        // Add padding for rule cards to center them
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int recyclerHeight = rulesRecyclerView.getHeight();
        int backHeight = btnBack.getHeight();
        int topPadding = (screenHeight - recyclerHeight) / 4;

        rulesRecyclerView.setLayoutManager(layoutManager);
        rulesRecyclerView.setAdapter(adapter);
        rulesRecyclerView.setPadding(100, topPadding-backHeight*2, 100, 0);

        // Add card-snap-to behavior
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rulesRecyclerView);

        // 🔁 Restore last position when exiting rules page
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int lastPosition = prefs.getInt(LAST_POSITION_KEY, 0);
        rulesRecyclerView.scrollToPosition(lastPosition);

        // Initialize scroll dots progress (based on last position/page state)
        dotsLayout = findViewById(R.id.dotsLayout);
        addDotsIndicator(lastPosition, rules.size());

        // Handle dots scroll bar progress
        rulesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        addDotsIndicator(position, rules.size());
                    }
                }
            }
        });
    }

    /* Save scroll position on activity when user leaves the rules directory page.
    *  Ensures user doesn't have to scroll back to the same location each time. */
    @Override
    protected void onPause() {
        super.onPause();
        LinearLayoutManager layoutManager = (LinearLayoutManager) rulesRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();

        //Correct the slide if card clicked is not the first one (otherwise always 1 card behind)
        if(position != 0){
            position += 1;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putInt(LAST_POSITION_KEY, position).apply();
    }

    // Initialize the dot progress bar
    private void addDotsIndicator(int position, int count) {
        dotsLayout.removeAllViews();
        dots = new ImageView[count];

        for (int i = 0; i < count; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.dot_inactive); // Create dot image
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dots[i].setLayoutParams(params);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0 && position < dots.length) {
            dots[position].setImageResource(R.drawable.dot_active);
        }

    }

}

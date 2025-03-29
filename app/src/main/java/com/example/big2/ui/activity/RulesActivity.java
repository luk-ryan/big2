package com.example.big2.ui.activity;

/* Main rule page activity functionality
*  > Rules (tutorial) card list initialization
*  > Onclick event handlers
*   */

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.big2.R;
import com.example.big2.ui.adapter.RulesRecyclerViewAdapter;
import com.example.big2.ui.model.RulesCard;

import java.util.ArrayList;
import java.util.List;

public class RulesActivity extends AppCompatActivity {

    private Button btnBack;
    private RecyclerView rulesRecyclerView;
    //private ProgressBar scrollProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);

        // Initialize Views
        btnBack = findViewById(R.id.btnBack);

        // Back button - closes activity and sends user back to main menu
        btnBack.setOnClickListener(v -> finish());


        rulesRecyclerView = findViewById(R.id.rulesRecyclerView);
        //scrollProgress = findViewById(R.id.scrollProgress);

        // List of visible cards for the rules (add more here...)
        List<RulesCard> rules = new ArrayList<>();
        rules.add(new RulesCard(R.drawable.card_suit_spade, "The Objective of The Game", "Collect cards and win."));
        rules.add(new RulesCard(R.drawable.card_suit_diamond, "Game Setup", "Each player gets 5 cards."));
        rules.add(new RulesCard(R.drawable.card_suit_club, "How to Play", "Each player gets 5 cards."));
        rules.add(new RulesCard(R.drawable.card_suit_heart, "Winning a Round", "Each player gets 5 cards."));
        rules.add(new RulesCard(R.drawable.card_suit_spade, "Scoring System", "Each player gets 5 cards."));


        RulesRecyclerViewAdapter adapter = new RulesRecyclerViewAdapter(this, rules);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        // Get screen height
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        // Get RecyclerView height
        int recyclerHeight = rulesRecyclerView.getHeight();
        int backHeight = btnBack.getHeight();
        // Calculate top padding to center the RecyclerView vertically
        int topPadding = (screenHeight - recyclerHeight) / 4;

        rulesRecyclerView.setLayoutManager(layoutManager);
        rulesRecyclerView.setAdapter(adapter);
        rulesRecyclerView.setPadding(100, topPadding-backHeight*2, 100, 0); //TODO make this variable, not fixed

        /*
        // Handle scroll progress
        rulesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int offset = recyclerView.computeHorizontalScrollOffset();
                int extent = recyclerView.computeHorizontalScrollExtent();
                int range = recyclerView.computeHorizontalScrollRange();

                int progress = (int) ((offset / (float)(range - extent)) * 100);
                scrollProgress.setProgress(progress);
            }
        }); */
    }


}

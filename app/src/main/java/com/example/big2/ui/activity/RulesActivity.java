package com.example.big2.ui.activity;

/* Main rule page activity functionality:
*  > Rules (tutorial) card list initialization
*  > Onclick event handlers
*  > Save scroll position on activity
*  > Scroll bar //TODO
*   */

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
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
import android.widget.ProgressBar;

public class RulesActivity extends AppCompatActivity {

    private Button btnBack;
    private RecyclerView rulesRecyclerView;
    private ProgressBar scrollProgress;

    // 🔐 Keys for saving scroll state/position
    public static final String PREFS_NAME = "RulesPrefs";
    public static final String LAST_POSITION_KEY = "LastScrollPosition";

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


        // Add snap behavior
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rulesRecyclerView);

        // 🔁 Restore last position
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int lastPosition = prefs.getInt(LAST_POSITION_KEY, 0);
        rulesRecyclerView.scrollToPosition(lastPosition);

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


}

package com.example.big2.ui.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.big2.R;
import com.example.big2.data.entity.Game;

import java.util.ArrayList;
import java.util.List;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameRecyclerViewAdapter.GameViewHolder> {

    private List<Game> gameList = new ArrayList<>();
    private int selectedPosition = -1;  // Track the selected position

    @NonNull
    @Override
    public GameRecyclerViewAdapter.GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this is where you inflate the layout (giving a look to our rows)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_recyclerview_row, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameRecyclerViewAdapter.GameViewHolder holder, int position) {
        // assigning values to the views we created in the recyclerview_row.xml layout file
        // based on the position of the recycler view
        Game game = gameList.get(position);

        // for an image, use holder.tvImageName.setImageResource() instead
        holder.tvGameName.setText(game.getGameName());

        // Highlight selected row
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.accent));  // Highlight color
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.light_primary));  // Default color
        }

        holder.itemView.setOnClickListener(v -> {
            // Update selected position
            int prevSelectedPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            Log.d("DEBUG", "Selected Position: " + selectedPosition);

            // Notify the adapter to update the UI
            notifyItemChanged(prevSelectedPosition);
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        // the recycler view just wanted to know the number of items you want displayed
        return gameList.size();
    }

    // Set game list data
    public void setGameList(List<Game> games) {
        // updates the list when any changes to the database is made
        this.gameList = games;
        notifyDataSetChanged();
    }

    public Game getSelectedGame() {
        if (selectedPosition != -1) {
            return gameList.get(selectedPosition);
        }
        return null;
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        // grabbing the views from our recyclerview_row.xml layout file
        // Similar to onCreate method
        TextView tvGameName;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGameName = itemView.findViewById(R.id.tvGameName);
        }
    }
}
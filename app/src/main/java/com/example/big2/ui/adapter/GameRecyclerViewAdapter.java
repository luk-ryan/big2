package com.example.big2.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.big2.R;
import com.example.big2.data.entity.Game;
import java.util.ArrayList;
import java.util.List;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameRecyclerViewAdapter.GameViewHolder> {

    private List<Game> gameList = new ArrayList<>();

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
        holder.tvGameName.setText(gameList.get(position).getGameName());

        // for an image, use holder.tvImageName.setImageResource() instead

    }

    @Override
    public int getItemCount() {
        // the recycler view just wanted to know the number of items you want displayed
        return gameList.size();
    }

    public void setGameList(List<Game> games) {
        // updates the list when any changes to the database is made
        this.gameList = games;
        notifyDataSetChanged();
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
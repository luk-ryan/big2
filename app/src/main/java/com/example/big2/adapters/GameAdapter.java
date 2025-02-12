//package com.example.big2.adapters;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.big2.R;
//import com.example.big2.data.entities.Game;
//import java.text.SimpleDateFormat;
//import java.util.List;
//import java.util.Locale;
//
//public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
//
//    private List<Game> gameList;
//    private OnGameClickListener listener;
//
//    public interface OnGameClickListener {
//        void onGameClick(Game game);
//    }
//
//    public GameAdapter(List<Game> gameList, OnGameClickListener listener) {
//        this.gameList = gameList;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
//        return new GameViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
//        Game game = gameList.get(position);
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
//        holder.gameName.setText("Game ID: " + game.gameId);
//        holder.startTime.setText("Started: " + sdf.format(game.startTime));
//
//        holder.itemView.setOnClickListener(v -> listener.onGameClick(game));
//    }
//
//    @Override
//    public int getItemCount() {
//        return gameList.size();
//    }
//
//    public void updateGames(List<Game> newGames) {
//        this.gameList = newGames;
//        notifyDataSetChanged();
//    }
//
//    static class GameViewHolder extends RecyclerView.ViewHolder {
//        TextView gameName, startTime;
//
//        public GameViewHolder(@NonNull View itemView) {
//            super(itemView);
//            gameName = itemView.findViewById(R.id.text_game_name);
//            startTime = itemView.findViewById(R.id.text_start_time);
//        }
//    }
//}

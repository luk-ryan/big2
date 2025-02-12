//package com.example.big2.adapters;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.big2.R;
//import com.example.big2.data.entities.Round;
//import java.util.List;
//
//public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundViewHolder> {
//
//    private List<Round> roundList;
//
//    public RoundAdapter(List<Round> roundList) {
//        this.roundList = roundList;
//    }
//
//    @NonNull
//    @Override
//    public RoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_round, parent, false);
//        return new RoundViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RoundViewHolder holder, int position) {
//        Round round = roundList.get(position);
//        holder.roundNumber.setText("Round " + (position + 1));
//        holder.scores.setText("Scores: " + round.playerScores[0] + ", " +
//                round.playerScores[1] + ", " + round.playerScores[2] + ", " +
//                round.playerScores[3]);
//    }
//
//    @Override
//    public int getItemCount() {
//        return roundList.size();
//    }
//
//    public void updateRounds(List<Round> newRounds) {
//        this.roundList = newRounds;
//        notifyDataSetChanged();
//    }
//
//    static class RoundViewHolder extends RecyclerView.ViewHolder {
//        TextView roundNumber, scores;
//
//        public RoundViewHolder(@NonNull View itemView) {
//            super(itemView);
//            roundNumber = itemView.findViewById(R.id.text_round_number);
//            scores = itemView.findViewById(R.id.text_round_scores);
//        }
//    }
//}

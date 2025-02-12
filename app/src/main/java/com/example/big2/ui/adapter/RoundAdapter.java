//package com.example.big2.ui.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.big2.R;
//import com.example.big2.data.entity.Round;
//
//import java.util.List;
//
//public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundViewHolder> {
//
//    private List<Round> rounds;
//
//    @Override
//    public RoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_round, parent, false);
//        return new RoundViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(RoundViewHolder holder, int position) {
//        Round round = rounds.get(position);
//        holder.roundNumberTextView.setText("Round " + round.getRoundNumber());
//        holder.scoresTextView.setText("S1: " + round.getS1() + ", S2: " + round.getS2() + ", S3: " + round.getS3() + ", S4: " + round.getS4());
//    }
//
//    @Override
//    public int getItemCount() {
//        return rounds != null ? rounds.size() : 0;
//    }
//
//    public void setRounds(List<Round> rounds) {
//        this.rounds = rounds;
//        notifyDataSetChanged();
//    }
//
//    static class RoundViewHolder extends RecyclerView.ViewHolder {
//        TextView roundNumberTextView;
//        TextView scoresTextView;
//
//        RoundViewHolder(View itemView) {
//            super(itemView);
//            roundNumberTextView = itemView.findViewById(R.id.roundNumberTextView);
//            scoresTextView = itemView.findViewById(R.id.scoresTextView);
//        }
//    }
//}

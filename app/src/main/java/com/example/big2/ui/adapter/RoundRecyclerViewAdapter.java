package com.example.big2.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.big2.R;
import com.example.big2.data.entity.Round;

import java.util.ArrayList;
import java.util.List;

public class RoundRecyclerViewAdapter extends RecyclerView.Adapter<RoundRecyclerViewAdapter.RoundViewHolder> {

    private List<Round> rounds = new ArrayList<>();
    private int selectedPosition = -1;  // Track the selected position

    @NonNull
    @Override
    public RoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this is where you inflate the layout (giving a look to our rows)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.round_recyclerview_row, parent, false);
        return new RoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoundViewHolder holder, int position) {
        // assigning values to the views we created in the recyclerview_row.xml layout file
        // based on the position of the recycler view
        Round round = rounds.get(position);

        // for an image, use holder.tvImageName.setImageResource() instead
        holder.tvRoundNumber.setText(String.valueOf(round.getRoundNumber()));
        holder.tvS1.setText(String.valueOf(round.getS1()));
        holder.tvS2.setText(String.valueOf(round.getS2()));
        holder.tvS3.setText(String.valueOf(round.getS3()));
        holder.tvS4.setText(String.valueOf(round.getS4()));

        // Highlight selected row
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.divider));  // Highlight color
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
        return rounds == null ? 0 : rounds.size();
    }

    // Set round list data
    public void setRoundsList(List<Round> rounds) {
        // updates the list when any changes to the database is made
        selectedPosition = -1; // Reset selection when data changes
        this.rounds = rounds;
        notifyDataSetChanged();

    }

    public Round getSelectedRound() {
        if (selectedPosition != -1) {
            return rounds.get(selectedPosition);
        }
        return null;
    }

    public static class RoundViewHolder extends RecyclerView.ViewHolder {
        // grabbing the views from our recyclerview_row.xml layout file
        // Similar to onCreate method
        TextView tvRoundNumber, tvS1, tvS2, tvS3, tvS4;

        public RoundViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoundNumber = itemView.findViewById(R.id.tvRoundNumber);
            tvS1 = itemView.findViewById(R.id.tvS1);
            tvS2 = itemView.findViewById(R.id.tvS2);
            tvS3 = itemView.findViewById(R.id.tvS3);
            tvS4 = itemView.findViewById(R.id.tvS4);
        }
    }
}
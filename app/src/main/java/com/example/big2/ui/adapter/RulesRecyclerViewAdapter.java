package com.example.big2.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.big2.ui.model.RulesCard;
import java.util.List;
import com.example.big2.R;

public class RulesRecyclerViewAdapter extends RecyclerView.Adapter<RulesRecyclerViewAdapter.RuleViewHolder> {

    private List<RulesCard> ruleList;

    public RulesRecyclerViewAdapter(List<RulesCard> ruleList) {
        this.ruleList = ruleList;
    }

    @NonNull
    @Override
    public RuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rules_recyclerview_card, parent, false);
        return new RuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RuleViewHolder holder, int position) {
        RulesCard item = ruleList.get(position);
        holder.imageView.setImageResource(item.getImageId());
        holder.titleView.setText(item.getTitle());
        holder.descView.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return ruleList.size();
    }

    static class RuleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView descView;

        public RuleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ruleImage);
            titleView = itemView.findViewById(R.id.ruleTitle);
            descView = itemView.findViewById(R.id.ruleDescription);
        }
    }
}
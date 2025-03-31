package com.example.big2.ui.adapter;

/* Adapter for rules recycler view. Includes:
*  > onclick listeners/event handling when clicking on rule card */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.big2.ui.activity.rules.ObjectiveActivity;
import com.example.big2.ui.activity.rules.ScoringActivity;
import com.example.big2.ui.activity.rules.SetupActivity;
import com.example.big2.ui.activity.rules.ValidPlaysActivity;
import com.example.big2.ui.activity.rules.WinningActivity;
import com.example.big2.ui.model.RulesCard;
import java.util.List;
import com.example.big2.R;

public class RulesRecyclerViewAdapter extends RecyclerView.Adapter<RulesRecyclerViewAdapter.RuleViewHolder> {

    private List<RulesCard> ruleList;
    private Context context;

    public RulesRecyclerViewAdapter(Context context, List<RulesCard> ruleList) {
        this.context = context;
        this.ruleList = ruleList;
    }

    @NonNull
    @Override
    //Called only when a new card view needs to be created (not reused).
    public RuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rules_recyclerview_card, parent, false);
        return new RuleViewHolder(view);
    }

    @Override
    //Rule card template
    public void onBindViewHolder(@NonNull RuleViewHolder holder, int position) {
        RulesCard item = ruleList.get(position);
        holder.imageView.setImageResource(item.getImageId());
        holder.titleView.setText(item.getTitle());
        holder.descView.setText(item.getDescription());

        String displayValue = item.getRank() + "\n" + item.getSuitSymbol();
        holder.topCorner.setText(displayValue);
        holder.bottomCorner.setText(displayValue);

        // Handle on-click events (load xml based on which card was pressed in the rulesList)
        holder.itemView.setOnClickListener(v -> {
            switch (position) {
                case 0:
                    context.startActivity(new Intent(context, ObjectiveActivity.class));
                    break;
                case 1:
                    context.startActivity(new Intent(context, SetupActivity.class));
                    break;
                case 2:
                    context.startActivity(new Intent(context, ValidPlaysActivity.class));
                    break;
                case 3:
                    context.startActivity(new Intent(context, WinningActivity.class));
                    break;
                case 4:
                    context.startActivity(new Intent(context, ScoringActivity.class));
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return ruleList.size();
    }

    //holds references for xml IDs for onBindViewHolder
    static class RuleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView descView;
        TextView topCorner, bottomCorner;

        public RuleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ruleImage);
            titleView = itemView.findViewById(R.id.ruleTitle);
            descView = itemView.findViewById(R.id.ruleDescription);
            topCorner = itemView.findViewById(R.id.cardCornerTop);
            bottomCorner = itemView.findViewById(R.id.cardCornerBottom);
        }
    }
}
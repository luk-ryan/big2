package com.example.big2.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.example.big2.R;

public class GameStatusAdapter extends BaseAdapter {
    private Context context;
    private String[] statuses;
    private int[] statusColors;

    public GameStatusAdapter(Context context, String[] statuses) {
        this.context = context;
        this.statuses = statuses;
        this.statusColors = new int[]{
                android.R.color.transparent, // No color for "All"
                R.color.not_started,         // Not Started
                R.color.in_progress,         // In Progress
                R.color.completed            // Completed
        };
    }

    @Override
    public int getCount() {
        return statuses.length;
    }

    @Override
    public Object getItem(int position) {
        return statuses[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_game_status, parent, false);
        }

        ImageView ivStatusIcon = convertView.findViewById(R.id.ivStatusIcon);
        TextView tvStatusText = convertView.findViewById(R.id.tvStatusText);

        tvStatusText.setText(statuses[position]);

        if (position == 0) { // "All" case
            ivStatusIcon.setVisibility(View.GONE); // Hide the icon
        } else {
            ivStatusIcon.setVisibility(View.VISIBLE);
            ivStatusIcon.setColorFilter(ContextCompat.getColor(context, statusColors[position]));
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}

package com.example.big2.ui.adapter;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.big2.R;
import com.example.big2.data.entity.Round;
import com.example.big2.ui.viewmodel.RoundViewModel;

import java.util.ArrayList;
import java.util.List;

public class RoundRecyclerViewAdapter extends RecyclerView.Adapter<RoundRecyclerViewAdapter.RoundViewHolder> {

    private List<Round> rounds = new ArrayList<>();
    private int selectedPosition = -1;  // Track the selected position
    private int editingPosition = -1; // Track the currently edited position
    private boolean isInteractable;
    RoundViewModel roundViewModel;

    public RoundRecyclerViewAdapter(RoundViewModel roundViewModel) {
        this.roundViewModel = roundViewModel;
    }

    @NonNull
    @Override
    public RoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this is where you inflate the layout (giving a look to our rows)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.round_recyclerview_row, parent, false);
        return new RoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoundViewHolder holder, int position) {
        if (isInteractable) {
            holder.itemView.setClickable(true);
            holder.itemView.setEnabled(true);
        } else {
            holder.itemView.setClickable(false);
            holder.itemView.setEnabled(false);
        }

        // Assign values to the views
        Round round = rounds.get(position);

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

        // Toggle selection
        holder.itemView.setOnClickListener(v -> {
            int prevSelectedPosition = selectedPosition;

            // If clicking the already selected row, deselect it
            if (selectedPosition == holder.getAdapterPosition()) {
                selectedPosition = -1;
            } else {
                // If editing a different row, cancel it
                if (editingPosition != -1 && editingPosition != holder.getAdapterPosition()) {
                    notifyItemChanged(editingPosition);  // Restore previous editing row
                    editingPosition = -1;
                }

                selectedPosition = holder.getAdapterPosition();
            }

            // Update UI for both the previous and current selected positions
            notifyItemChanged(prevSelectedPosition);
            notifyItemChanged(selectedPosition);

            // Show Popup Menu only if the row is selected
            if (selectedPosition == holder.getAdapterPosition()) {
                showPopupMenu(v, round, holder);
            }
        });

//        // Show Popup Menu on long press, only if the round is selected
//        holder.itemView.setOnLongClickListener(v -> {
//            if (position == selectedPosition) {  // Check if the row is selected
//                showPopupMenu(v, round, holder);  // Show the popup menu on long press
//                return true;  // Return true to indicate that the long press was handled
//            }
//            return false;  // Return false if the row is not selected
//        });
    }

    @Override
    public int getItemCount() {
        // the recycler view just wanted to know the number of items you want displayed
        return rounds == null ? 0 : rounds.size();
    }

    public void setIsInteractable(boolean interactable) {
        isInteractable = interactable;
    }

    // Set round list data
    public void setRoundsList(List<Round> rounds) {
        selectedPosition = -1;
        this.rounds = rounds;
        notifyDataSetChanged();
    }

    public Round getSelectedRound() {
        if (selectedPosition != -1) {
            return rounds.get(selectedPosition);
        }
        return null;
    }

    // In the ViewHolder's onBindViewHolder method
    private void showPopupMenu(View view, Round round, RoundViewHolder holder) {
        // Create the PopupMenu
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.round_popup_menu, popupMenu.getMenu());

        popupMenu.setGravity(Gravity.END | Gravity.TOP);

        // Set the click listener for the menu actions
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_edit) {
                // Handle the edit action
                handleEdit(holder, round);
                Log.d("DEBUG", "showPopupMenu: Edit");
            } else if (id == R.id.menu_delete) {
                handeDelete(holder, round);
            }
            return false;
        });

        // Show the menu
        popupMenu.show();
    }

    private void handeDelete(RoundViewHolder holder, Round round) {
        roundViewModel.delete(round);
        // Show Delete Message
        Toast.makeText(holder.itemView.getContext(), "Round " + round.getRoundNumber() + " Deleted", Toast.LENGTH_SHORT).show();
    }

    private void handleEdit(RoundViewHolder holder, Round round) {

        // If another row is already being edited, cancel it
        if (editingPosition != -1 && editingPosition != holder.getAdapterPosition()) {
            notifyItemChanged(editingPosition);
        }

        editingPosition = holder.getAdapterPosition();
        replaceWithNumberPickers(holder);

        // Save button
        holder.ivSaveRound.setOnClickListener(v -> {
            int zeroCount = 0;

            // Count how many NumberPickers are set to zero
            if (holder.npS1.getValue() == 0) zeroCount++;
            if (holder.npS2.getValue() == 0) zeroCount++;
            if (holder.npS3.getValue() == 0) zeroCount++;
            if (holder.npS4.getValue() == 0) zeroCount++;

            if (zeroCount == 1) {

                round.setS1(convertToScore(holder.npS1.getValue()));
                round.setS2(convertToScore(holder.npS2.getValue()));
                round.setS3(convertToScore(holder.npS3.getValue()));
                round.setS4(convertToScore(holder.npS4.getValue()));

                // Only proceed if exactly one player has a score of zero
                roundViewModel.update(round);
                replaceWithTextViews(holder, round);
                editingPosition = -1;
            } else if (zeroCount > 1) {
                // Show message that too many players have zero scores
                Toast.makeText(holder.itemView.getContext(), "Only one person can be the winner of a round", Toast.LENGTH_SHORT).show();
            } else {
                // Show message that no one has zero score
                Toast.makeText(holder.itemView.getContext(), "One person must be the winner of a round", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel button
        holder.ivCancelEditRound.setOnClickListener(v -> {
            replaceWithTextViews(holder, round);
            editingPosition = -1;
        });
    }

    private void replaceWithNumberPickers(RoundViewHolder holder) {
        // Hide the TextViews
        holder.tvS1.setVisibility(View.GONE);
        holder.tvS2.setVisibility(View.GONE);
        holder.tvS3.setVisibility(View.GONE);
        holder.tvS4.setVisibility(View.GONE);

        // Show the NumberPickers
        holder.npS1.setVisibility(View.VISIBLE);
        holder.npS2.setVisibility(View.VISIBLE);
        holder.npS3.setVisibility(View.VISIBLE);
        holder.npS4.setVisibility(View.VISIBLE);

        // Set the current value of the NumberPickers based on the existing data
        holder.npS1.setValue(convertToCards(Integer.parseInt(holder.tvS1.getText().toString())));
        holder.npS2.setValue(convertToCards(Integer.parseInt(holder.tvS2.getText().toString())));
        holder.npS3.setValue(convertToCards(Integer.parseInt(holder.tvS3.getText().toString())));
        holder.npS4.setValue(convertToCards(Integer.parseInt(holder.tvS4.getText().toString())));

        holder.ivSaveRound.setVisibility(View.VISIBLE);
        holder.ivCancelEditRound.setVisibility(View.VISIBLE);
    }

    private void replaceWithTextViews(RoundViewHolder holder, Round round) {
        // Hide the NumberPickers
        holder.npS1.setVisibility(View.GONE);
        holder.npS2.setVisibility(View.GONE);
        holder.npS3.setVisibility(View.GONE);
        holder.npS4.setVisibility(View.GONE);

        // Show the TextViews
        holder.tvS1.setVisibility(View.VISIBLE);
        holder.tvS2.setVisibility(View.VISIBLE);
        holder.tvS3.setVisibility(View.VISIBLE);
        holder.tvS4.setVisibility(View.VISIBLE);

        // Update TextViews with the current values of the NumberPickers
        holder.tvS1.setText(String.valueOf(round.getS1()));
        holder.tvS2.setText(String.valueOf(round.getS2()));
        holder.tvS3.setText(String.valueOf(round.getS3()));
        holder.tvS4.setText(String.valueOf(round.getS4()));

        holder.ivSaveRound.setVisibility(View.GONE);
        holder.ivCancelEditRound.setVisibility(View.GONE);

        editingPosition = -1;
    }

    // Helper method to calculate score based on the value
    private int convertToScore(int value) {
        if (value == 11) {
            return value * 2;  // Score = 11 * 2 = 22
        } else if (value == 12) {
            return value * 2;  // Score = 12 * 2 = 24
        } else if (value == 13) {
            return value * 3;  // Score = 13 * 3 = 39
        } else {
            return value;
        }
    }

    // Helper method to calculate number of cards based on score
    private int convertToCards(int value) {
        if (value == 22) {
            return value / 2;  // Score = 11 * 2 = 22
        } else if (value == 24) {
            return value / 2;  // Score = 12 * 2 = 24
        } else if (value == 39) {
            return value / 3;  // Score = 13 * 3 = 39
        } else {
            return value;
        }
    }

    public static class RoundViewHolder extends RecyclerView.ViewHolder {
        // Similar to onCreate method
        TextView tvRoundNumber, tvS1, tvS2, tvS3, tvS4;
        NumberPicker npS1, npS2, npS3, npS4;
        ImageView ivSaveRound, ivCancelEditRound;

        public RoundViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoundNumber = itemView.findViewById(R.id.tvRoundNumber);
            tvS1 = itemView.findViewById(R.id.tvS1);
            tvS2 = itemView.findViewById(R.id.tvS2);
            tvS3 = itemView.findViewById(R.id.tvS3);
            tvS4 = itemView.findViewById(R.id.tvS4);

            // Initialize NumberPickers
            npS1 = itemView.findViewById(R.id.npS1);
            npS2 = itemView.findViewById(R.id.npS2);
            npS3 = itemView.findViewById(R.id.npS3);
            npS4 = itemView.findViewById(R.id.npS4);

            ivSaveRound = itemView.findViewById(R.id.ivSaveRound);
            ivCancelEditRound = itemView.findViewById(R.id.ivCancelEditRound);

            // Set min and max values programmatically
            setupNumberPicker(npS1);
            setupNumberPicker(npS2);
            setupNumberPicker(npS3);
            setupNumberPicker(npS4);

            // Initially set NumberPickers to gone
            npS1.setVisibility(View.GONE);
            npS2.setVisibility(View.GONE);
            npS3.setVisibility(View.GONE);
            npS4.setVisibility(View.GONE);

            ivSaveRound.setVisibility(View.GONE);
            ivCancelEditRound.setVisibility(View.GONE);
        }

        private void setupNumberPicker(NumberPicker numberPicker) {
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(13);
            numberPicker.setWrapSelectorWheel(false);
        }
    }
}
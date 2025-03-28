package com.example.big2.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.big2.R;
import com.example.big2.data.entity.Game;
import com.example.big2.ui.viewmodel.GameViewModel;

public class CreateGameFragment extends DialogFragment {
    private EditText etGameName, etP1, etP2, etP3, etP4, etCardValue;
    private ImageView ivBack;
    private Button btnSave;
    private GameViewModel gameViewModel;

    public CreateGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_game, container, false);

        // Initialize ViewModel
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        // UI Elements
        etGameName = view.findViewById(R.id.etGameName);
        etP1 = view.findViewById(R.id.etP1);
        etP2 = view.findViewById(R.id.etP2);
        etP3 = view.findViewById(R.id.etP3);
        etP4 = view.findViewById(R.id.etP4);
        etCardValue = view.findViewById(R.id.etCardValue);
        btnSave = view.findViewById(R.id.btnSave);
        ivBack = view.findViewById(R.id.ivBack);

        // Save Button
        btnSave.setOnClickListener(v -> saveGame());

        // Cancel Button
        ivBack.setOnClickListener(v -> dismiss());

        return view;
    }

    private void saveGame() {
        String gameName = etGameName.getText().toString().trim();
        String p1 = etP1.getText().toString().trim();
        String p2 = etP2.getText().toString().trim();
        String p3 = etP3.getText().toString().trim();
        String p4 = etP4.getText().toString().trim();
        String cardValueStr = etCardValue.getText().toString().trim();

        if (gameName.isEmpty() || p1.isEmpty() || p2.isEmpty() || p3.isEmpty() || p4.isEmpty() || cardValueStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double cardValue = Double.parseDouble(cardValueStr);

        // Create game and insert into database
        Game newGame = new Game(gameName, p1, p2, p3, p4, cardValue);
        gameViewModel.insert(newGame);

        // Notify success and dismiss dialog
        Toast.makeText(getContext(), "Game added!", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}

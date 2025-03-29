package com.example.big2.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.big2.R;
import com.example.big2.data.entity.Game;
import com.example.big2.ui.activity.GameplayActivity;
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
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Set a wider width for the dialog
            getDialog().getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
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

        // Show keyboard when fragment opens
        etGameName.requestFocus();
        etGameName.post(() -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etGameName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        setEditorActionListeners();

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
        String cardValue = etCardValue.getText().toString().trim();

        // Remove the $ symbol for validation and saving
        if (cardValue.startsWith("$")) {
            cardValue = cardValue.substring(1);  // Remove the leading $
        }

        if (gameName.isEmpty() || p1.isEmpty() || p2.isEmpty() || p3.isEmpty() || p4.isEmpty() || cardValue.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double cardValueNumeric = Double.parseDouble(cardValue);

        // Create game and insert into database
        Game newGame = new Game(gameName, p1, p2, p3, p4, cardValueNumeric);
        gameViewModel.insert(newGame);

        // Notify success and dismiss dialog
        Toast.makeText(getContext(), "Game added!", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    // Editor Behaviours
    private void setEditorActionListeners() {
        // For Title EditText
        etGameName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Focus goes to P1 header EditText
                etP1.requestFocus();
                return true;
            }
            return false;
        });

        // For P1 Header EditText
        etP1.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Focus goes to P2 header EditText
                etP2.requestFocus();
                return true;
            }
            return false;
        });

        // For P2 Header EditText
        etP2.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Focus goes to P3 header EditText
                etP3.requestFocus();
                return true;
            }
            return false;
        });

        // For P3 Header EditText
        etP3.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Focus goes to P4 header EditText
                etP4.requestFocus();
                return true;
            }
            return false;
        });

        // For P4 Header EditText
        etP4.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Focus goes to Card Value EditText
                etCardValue.requestFocus();
                return true;
            }
            return false;
        });

        // For Card Value EditText
        etCardValue.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                // Clear focus from the EditText
                etCardValue.clearFocus();
                return true;
            }
            return false;
        });

        etCardValue.setSelection(etCardValue.getText().length()); // Place cursor at the end

        etCardValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No need to do anything before text change
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                // Ensure the dollar sign is at the start of the string
                if (charSequence.length() > 0 && !charSequence.toString().startsWith("$")) {
                    etCardValue.setText("$" + charSequence.toString().substring(1));
                    etCardValue.setSelection(etCardValue.getText().length()); // Ensure cursor is at the end
                }

                // Automatically add '0' before decimal if no number exists before it
                String text = charSequence.toString();
                if (text.startsWith("$") && text.length() > 1 && text.indexOf(".") == 1) {
                    // If the text starts with '$' and there's a '.' at index 1 (e.g., '$.'), prepend '0'
                    etCardValue.setText("$0" + text.substring(1));
                    etCardValue.setSelection(etCardValue.getText().length()); // Ensure the cursor is at the end
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Optionally handle other changes here
            }
        });

        // Prevent backspace when the cursor is at the start
        etCardValue.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && etCardValue.getText().toString().startsWith("$") && etCardValue.getSelectionStart() == 1) {
                // Prevent backspace when cursor is at index 1 (immediately after $)
                return true;  // Don't allow the delete action
            }
            return false;
        });
    }
}

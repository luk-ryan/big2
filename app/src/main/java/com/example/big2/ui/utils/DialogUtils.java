package com.example.big2.ui.utils;

import android.content.Context;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.example.big2.R;

public class DialogUtils {

    public static void showConfirmationDialog(Context context, String title, String message, Runnable action) {
        AlertDialog dialog = new AlertDialog.Builder(context, R.style.CustomAlertDialog)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", (d, which) -> action.run())
                .setNegativeButton("No", null)
                .create();

        dialog.setOnShowListener(dlg -> {
            // Force the text color to primary color for both buttons
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            // Set the button text color manually to your primary color
            positiveButton.setTextColor(context.getColor(R.color.dark_primary));
            negativeButton.setTextColor(context.getColor(R.color.dark_primary));
        });

        dialog.show();
    }
}

package com.example.big2.ui.utils;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;

public class DialogUtils {

    public static void showConfirmationDialog(Context context, String title, String message, Runnable action) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> action.run())
                .setNegativeButton("No", null)
                .show();
    }
}

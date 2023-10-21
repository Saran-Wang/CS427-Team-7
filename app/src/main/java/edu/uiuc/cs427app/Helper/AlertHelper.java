package edu.uiuc.cs427app.Helper;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import edu.uiuc.cs427app.R;

public class AlertHelper {

    public static void displayDialog(Context context, String errorMessage) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.app_name))
                .setMessage(errorMessage)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}

package com.denis.music_rec;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.denis.music_rec.ui.BaseActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class Dialog {

    public static AlertDialog showInputList(BaseActivity activity, String title, CharSequence[] items, DialogInterface.OnClickListener onClickListener, String negativeLabel, DialogInterface.OnClickListener onNegative, DialogInterface.OnCancelListener onCancel) {
        if ((activity == null) || (activity.isFinishing())) {
            return null;
        }
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(activity)
                .setItems(items, onClickListener);
        dialogBuilder.setOnCancelListener(onCancel);
        dialogBuilder.setCancelable(true);
        if (title != null) {
            dialogBuilder.setTitle(title);
        }
        dialogBuilder.setNegativeButton(negativeLabel, onNegative);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        return alertDialog;
    }


    public static void showSong(Context context, String title, MusicView musicView, @Nullable String positiveText, @Nullable String negativeText, @Nullable DialogInterface.OnClickListener onPositive, @Nullable DialogInterface.OnClickListener onNegative, DialogInterface.OnDismissListener onDismiss) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context)
                .setView(musicView);

        dialogBuilder.setOnDismissListener(onDismiss);

        if (positiveText != null) {
            dialogBuilder.setPositiveButton(positiveText, onPositive);
        }
        if (negativeText != null) {
            dialogBuilder.setNegativeButton(negativeText, onNegative);
        }
        if (title != null) {
            dialogBuilder.setTitle(title);
        }

        dialogBuilder.show();
    }

    public static AlertDialog show(Context context, String title, String message, @Nullable String positiveText, @Nullable String negativeText, @Nullable DialogInterface.OnClickListener onPositive, @Nullable DialogInterface.OnClickListener onNegative) {
        return show(context, title, message, positiveText, negativeText, onPositive, onNegative, true);
    }

    public static AlertDialog show(Context context, String title, String message, @Nullable String positiveText, @Nullable String negativeText, @Nullable DialogInterface.OnClickListener onPositive, @Nullable DialogInterface.OnClickListener onNegative, boolean cancelable) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context)
                .setMessage(message);
        dialogBuilder.setCancelable(cancelable);

        if (positiveText != null) {
            dialogBuilder.setPositiveButton(positiveText, onPositive);
        }
        if (negativeText != null) {
            dialogBuilder.setNegativeButton(negativeText, onNegative);
        }
        if (title != null) {
            dialogBuilder.setTitle(title);
        }

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        return alertDialog;
    }

    public static void showSnack(View contextView, String message) {
        Dialog.showSnack(contextView, message, null, null);
    }

    public static void showSnack(View contextView, String message, String buttonName, View.OnClickListener clickListener) {
        Snackbar snackbar = Snackbar.make(contextView, message, Snackbar.LENGTH_LONG);
        if ((clickListener != null) && buttonName != null) {
            snackbar.setAction(buttonName, clickListener);
        }
        snackbar.show();
    }
}

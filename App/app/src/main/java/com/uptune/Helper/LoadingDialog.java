package com.uptune.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.uptune.R;

public class LoadingDialog {
    private final Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoadingAnimation() {
        LayoutInflater inflater = activity.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        this.dialog = builder.setView(inflater.inflate(R.layout.custom_dialog, null))
                .setCancelable(false)
                .create();

        this.dialog.show();
    }

    public void dismissLoadingDialog() {
        this.dialog.dismiss();
    }
}

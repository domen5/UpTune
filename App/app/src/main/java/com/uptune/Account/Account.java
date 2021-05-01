package com.uptune.Account;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uptune.MainActivity;
import com.uptune.R;

public class Account extends Fragment {

    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton logout = view.findViewById(R.id.btn_logout);
        dialog = new Dialog(getContext());
        logout.setOnClickListener(v -> {
            openLogoutDialog();
        });
    }

    private void openLogoutDialog() {
        dialog.setContentView(R.layout.dialog_no_connection);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnCancel = dialog.findViewById(R.id.cancel);
        TextView txt = dialog.findViewById(R.id.txtDialog);
        Button btnConfirm = dialog.findViewById(R.id.confirm);
        btnConfirm.setText("Exit");
        txt.setText("Are you sure to log out?");
        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

}
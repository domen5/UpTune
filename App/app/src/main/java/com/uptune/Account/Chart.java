package com.uptune.Account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uptune.Helper.CaptureAct;
import com.uptune.Login.Login;
import com.uptune.Login.SignUp;
import com.uptune.MainActivity;
import com.uptune.R;

public class Chart extends AppCompatActivity {


    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        //region Nav Bottom
        BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav);
        Button logout = findViewById(R.id.log);
        bottomNavView.setSelectedItemId(R.id.charts);

        Button scan = findViewById(R.id.scan);
        dialog = new Dialog(this);
        bottomNavView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class)); //start
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.charts:
                    startActivity(new Intent(getApplicationContext(), Chart.class)); //start
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), Catalog.class)); //start
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.user:
                    startActivity(new Intent(getApplicationContext(), Account.class)); //start
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });
        //endregion

        logout.setOnClickListener(v -> {
            openLogoutDialog();
        });

        scan.setOnClickListener(v -> {
            scanCode();
        });
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan album code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult res = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (res != null) {
            if (res.getContents() != null) {
                AlertDialog.Builder build = new AlertDialog.Builder(this);
                build.setMessage(res.getContents());
                build.setTitle("Scan res");
                build.setPositiveButton("Scan again", (dialog, which) -> scanCode()).setNegativeButton("finish", (dialog, which) -> finish());
                AlertDialog dialog = build.create();
                dialog.show();
            } else
                Toast.makeText(this, "no res", Toast.LENGTH_SHORT).show();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void openLogoutDialog() {
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnCancel = dialog.findViewById(R.id.cancel);
        Button btnConfirm = dialog.findViewById(R.id.confirm);
        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(Chart.this, MainActivity.class);
            startActivity(intent);
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
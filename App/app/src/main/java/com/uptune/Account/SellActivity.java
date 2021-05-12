package com.uptune.Account;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uptune.Helper.CaptureAct;
import com.uptune.R;

public class SellActivity extends AppCompatActivity {

    MaterialButton scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        scan= findViewById(R.id.scan_code);
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
                build.setPositiveButton("Scan again", (dialog, which) -> scanCode()).setNegativeButton("finish", (dialog, which) -> closeContextMenu());
                AlertDialog dialog = build.create();
                dialog.show();
            } else
                Toast.makeText(this, "no res", Toast.LENGTH_SHORT).show();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }


}
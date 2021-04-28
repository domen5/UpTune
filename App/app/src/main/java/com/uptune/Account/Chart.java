package com.uptune.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        Button cancel, confirm;
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
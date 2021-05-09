package com.uptune;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uptune.Catalog.CategoriesDetails;
import com.uptune.Login.Login;
import com.uptune.Navigation.SpaceTab;

import java.io.IOException;


public class MainActivity extends AppCompatActivity  {

    Animation topAnim, bottomAnim;
    ImageView img;
    TextView loadTitle, loadText;

    private static int ANIMATION_TIME = 3000;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
        img = findViewById(R.id.imageView);
        loadText = findViewById(R.id.loadText);
        loadTitle = findViewById(R.id.loadTitle);

        img.setAnimation(topAnim);
        loadTitle.setAnimation(bottomAnim);
        loadText.setAnimation(bottomAnim);
        if (!isNetworkAvailable()) {
            openSettings();
        } else {
            try {
                Web.httpCall();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, SpaceTab.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(img, "logo_img");
                pairs[1] = new Pair<View, String>(loadTitle, "loadTitle");
                ActivityOptions opt = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                startActivity(intent, opt.toBundle());
            }, ANIMATION_TIME);
        }
        //endregion
    }

    private void openSettings() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_no_connection);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnCancel = dialog.findViewById(R.id.cancel);
        btnCancel.setText("Exit");
        Button btnConfirm = dialog.findViewById(R.id.confirm);
        btnConfirm.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            dialog.dismiss();
        });
        btnCancel.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });
        dialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

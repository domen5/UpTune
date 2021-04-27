package com.uptune;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView img;
    TextView loadTitle, loadText;

    private static int ANIMATION_TIME = 3000;

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

        new Handler().postDelayed(()-> {

                Intent intent = new Intent(MainActivity.this, Login.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(img, "logo_img");
                pairs[1] = new Pair<View, String>(loadTitle, "loadTitle");

                ActivityOptions opt = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                startActivity(intent, opt.toBundle());

        }, ANIMATION_TIME);
        //endregion
    }
}

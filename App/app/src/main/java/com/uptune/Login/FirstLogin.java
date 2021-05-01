package com.uptune.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;

import com.uptune.Account.Account;
import com.uptune.Login.Welcome.fragment_welcome1;
import com.uptune.Login.Welcome.fragment_welcome2;
import com.uptune.Login.Welcome.fragment_welcome3;
import com.uptune.R;

public class FirstLogin extends AppCompatActivity {
    private final int NUM_PAG = 3;
    private ViewPager viewPager;
    sliderPage sliderPage;
    Animation anim;
    ImageButton bt;
    Button skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        bt = findViewById(R.id.button_back);
        skip = findViewById(R.id.button_skip);
        bt.setVisibility(View.GONE);

        viewPager = findViewById(R.id.liquid);

        sliderPage = new sliderPage(getSupportFragmentManager());
        if (sliderPage.getPos() == 0)
            bt.setVisibility(View.GONE);

        viewPager.setAdapter(sliderPage);
        // anim = AnimationUtils.loadAnimation(this, R.anim.start_anim_reg);
        //viewPager.startAnimation(anim);

        bt.setOnClickListener(v -> {
            sliderPage = new sliderPage(getSupportFragmentManager(), sliderPage.getPos() - 1);
            viewPager.setAdapter(sliderPage);
        });

        skip.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Account.class);

            /*
            intent.putExtra("name", name);
            intent.putExtra("username", username);
            intent.putExtra("email", mail);
            intent.putExtra("phone", phone);
            */
            startActivity(intent);
        });
    }

    private class sliderPage extends FragmentStatePagerAdapter {
        private int pos = 0;

        public sliderPage(@NonNull FragmentManager fm) {
            super(fm);
        }

        public sliderPage(@NonNull FragmentManager fm, int pos) {
            super(fm, pos);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    pos = 0;
                    return new fragment_welcome1();
                case 1:
                    pos = 1;
                    return new fragment_welcome2();
                case 2:
                    pos = 2;
                    return new fragment_welcome3();
            }
            return null;
        }

        public int getPos() {
            return pos;
        }

        @Override
        public int getCount() {
            return NUM_PAG;
        }
    }
}

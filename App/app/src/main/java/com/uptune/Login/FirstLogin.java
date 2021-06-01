package com.uptune.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.uptune.Login.Welcome.fragment_welcome1;
import com.uptune.Login.Welcome.fragment_welcome2;
import com.uptune.Login.Welcome.fragment_welcome3;
import com.uptune.Navigation.SpaceTab;
import com.uptune.R;

public class FirstLogin extends AppCompatActivity {
    private final int NUM_PAG = 3;
    private ViewPager viewPager;
    sliderPage sliderPage;
    Button skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        skip = findViewById(R.id.button_skip);

        viewPager = findViewById(R.id.liquid);

        sliderPage = new sliderPage(getSupportFragmentManager());


        viewPager.setAdapter(sliderPage);


        skip.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SpaceTab.class);
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

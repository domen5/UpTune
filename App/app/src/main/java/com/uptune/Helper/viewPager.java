package com.uptune.Helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.uptune.Login.fragment_1;
import com.uptune.Login.fragment_2;
import com.uptune.Login.fragment_3;

public class viewPager extends FragmentPagerAdapter {

    public viewPager(@NonNull FragmentManager fm, int behaviour) {
        super(fm, behaviour);
    }

    @NonNull

    @Override

    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new fragment_1();
            case 1:
                return new fragment_2();
            case 2:
                return new fragment_3();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
package com.uptune.Helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.uptune.Login.Welcome.fragment_welcome1;
import com.uptune.Login.Welcome.fragment_welcome2;
import com.uptune.Login.Welcome.fragment_welcome3;

public class viewPager extends FragmentPagerAdapter {

    public viewPager(@NonNull FragmentManager fm, int behaviour) {
        super(fm, behaviour);
    }

    @NonNull

    @Override

    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new fragment_welcome1();
            case 1:
                return new fragment_welcome2();
            case 2:
                return new fragment_welcome3();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
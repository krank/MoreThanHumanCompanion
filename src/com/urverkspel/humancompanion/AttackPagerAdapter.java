package com.urverkspel.humancompanion;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AttackPagerAdapter extends FragmentPagerAdapter {

    public AttackPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return new AttackFragment();
            case 1:
                return new AttackArmorFragment();
            case 2:
                return new AttacktResultFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}

package com.urverkspel.humancompanion;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class AttackPagerAdapter extends FragmentPagerAdapter {
	
	private ArrayList<Fragment> fragmentsArray = new ArrayList<Fragment>();

    public AttackPagerAdapter(FragmentManager fm) {
        super(fm);
		
		fragmentsArray.add(new AttackFragment());
		fragmentsArray.add(new AttackArmorFragment());
		fragmentsArray.add(new AttackResultFragment());
		System.out.println("Loaded all attack fragments");
		
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return fragmentsArray.get(0);
            case 1:
                return fragmentsArray.get(1);
            case 2:
                return fragmentsArray.get(2);
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}

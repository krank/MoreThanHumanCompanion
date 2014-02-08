package com.urverkspel.humancompanion;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CombatPagerAdapter extends FragmentPagerAdapter {

	public CombatPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		
		switch(i) {
			case 0:
				return new CombatFragment();
			case 1:
				return new CombatResultFragment();
		}
		
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}
	
}

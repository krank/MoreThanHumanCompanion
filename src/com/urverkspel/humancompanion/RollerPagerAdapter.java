package com.urverkspel.humancompanion;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RollerPagerAdapter extends FragmentPagerAdapter {

	public RollerPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		
		switch(i) {
			case 0:
				return new RollerFragment();
		}
		
		return null;
	}

	@Override
	public int getCount() {
		return 1;
	}
	
}

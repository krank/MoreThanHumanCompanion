package com.urverkspel.humancompanion;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MultiPagerAdapter extends FragmentPagerAdapter {

	public int currentSegment;

	public static final int SEGMENT_ROLLER = 0;
	public static final int SEGMENT_ATTACK = 1;

	public MultiPagerAdapter(FragmentManager fm) {
		super(fm);
		currentSegment = SEGMENT_ROLLER;
	}

	@Override
	public Fragment getItem(int i) {

		System.out.println(currentSegment);
		
		switch (currentSegment) {
			case SEGMENT_ROLLER:
				return new RollerFragment();
			case SEGMENT_ATTACK:

				switch (i) {
					case 0:
						return new AttackFragment();
					case 1:
						return new AttackArmorFragment();
					case 2:
						return new AttackResultFragment();
				}
				break;
		}

		return null;
	}

	@Override
	public int getCount() {
		switch (currentSegment) {
			case SEGMENT_ROLLER:
				return 1;
			case SEGMENT_ATTACK:
				return 3;
			default:
				return 0;
		}
	}

}

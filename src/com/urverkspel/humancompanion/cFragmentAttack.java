package com.urverkspel.humancompanion;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class cFragmentAttack extends ContainerFragment {

	private View rootView;

	private ViewPager viewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get root view of fragment
		this.rootView = inflater.inflate(R.layout.cfragment_attack, container, false);

		viewPager = (ViewPager) rootView.findViewById(R.id.attackpager);
		PagerAdapter pagerAdapter = new AttackPagerAdapter(this.getChildFragmentManager());

		viewPager.setAdapter(pagerAdapter);
		
		viewPager.setOnPageChangeListener(
				new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						getActivity().getActionBar().setSelectedNavigationItem(position);
					}
				}
		);

		return rootView;
	}

	@Override
	public void changePage(int index) {
		if (viewPager != null) {
			viewPager.setCurrentItem(index);
		}
	}

}

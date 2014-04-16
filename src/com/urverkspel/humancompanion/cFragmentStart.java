package com.urverkspel.humancompanion;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class cFragmentStart extends ContainerFragment {

	private View rootView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get root view of fragment
		this.rootView = inflater.inflate(R.layout.cfragment_start, container, false);

		

		return rootView;
	}

}

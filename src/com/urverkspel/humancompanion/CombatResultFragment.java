package com.urverkspel.humancompanion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CombatResultFragment extends Fragment{
	
	private View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Create local references to general things
		rootView = inflater.inflate(R.layout.fragment_combat_result, container, false);
		
		return rootView;
	}
	
}

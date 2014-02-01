package com.urverkspel.humancompanion;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class CombatFragment extends Fragment {

	// Preferences
	SharedPreferences prefs;

	// General things
	private View rootView;
	private CombatFragment thisFragment;
	private Activity parentActivity;

	// UI elements
	private LinearLayout armorLayout;
	private LinearLayout protectionLayout;
	private LinearLayout protectionSlider;
	

	private CheckBox useArmor;
	
	private RangeSeekBar<Integer> protectionSeekbar;
	
	

	public CombatFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Create local references to general things
		thisFragment = this;
		parentActivity = this.getActivity();
		rootView = inflater.inflate(R.layout.fragment_combat, container, false);

		
		findInterfaceElements();
		addRangeSelector();
		setListeners();

		return rootView;
	}
	
	private void addRangeSelector() {
		protectionSeekbar = new RangeSeekBar<Integer>(1,32, parentActivity);
		
		
		protectionLayout.addView(protectionSeekbar);
		
	}

	private void findInterfaceElements() {
		armorLayout = (LinearLayout) rootView.findViewById(R.id.layout_armor);
		useArmor = (CheckBox) rootView.findViewById(R.id.chk_use_armor);

		protectionLayout = (LinearLayout) armorLayout.findViewById(R.id.layout_protection);
	}

	private void setListeners() {

		useArmor.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					armorLayout.setVisibility(View.VISIBLE);
				} else {
					armorLayout.setVisibility(View.GONE);
				}
			}
		});

	}

}

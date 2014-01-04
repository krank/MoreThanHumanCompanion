package com.urverkspel.humancompanion;

import android.os.Bundle;
import android.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Load the preferences from XML
		this.addPreferencesFromResource(R.xml.preferences);
	}
}

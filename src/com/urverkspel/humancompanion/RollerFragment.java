package com.urverkspel.humancompanion;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class RollerFragment extends Fragment {

	// This view
	private MainActivity thisActivity;

	// Views
	private SeekBar valueSeekBar;
	private SeekBar thresholdSeekBar;

	private EditText valueEditText;
	private EditText thresholdEditText;

	private TextView blackTextView;
	private TextView whiteTextView;
	private TextView resultTextView;

	private Button rollButton;

	// Constants
	static final int DEFAULT_VALUE = 9;
	static final int DEFAULT_THRESHOLD = 0;

	static final String PREF_USELUCK = "pref_use_luck";

	static final String STATE_WHITE = "whiteValue";
	static final String STATE_BLACK = "blackValue";
	static final String STATE_USEDLUCK = "usedLuck";
	static final String STATE_VALUE = "value";
	static final String STATE_THRESHOLD = "threshold";

	// Variables
	private Boolean lastRollUsedLuck;
	private int lastValue;
	private int lastThreshold;

	// Preferences
	SharedPreferences prefs;
	
	
	public RollerFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_roller, container, false);
		
		return rootView;
	}

}

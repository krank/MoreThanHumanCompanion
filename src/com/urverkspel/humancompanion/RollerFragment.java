package com.urverkspel.humancompanion;

import android.app.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import voltroll.VoltResult;

public class RollerFragment extends Fragment {

	// This view
	private View rootView;
	private RollerFragment thisFragment;
	private Activity parentActivity;

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

		// Get root view of fragment
		this.rootView = inflater.inflate(R.layout.fragment_roller, container, false);

		// Make instance of this
		thisFragment = this;
		parentActivity = this.getActivity();

		findViews();
		setListeners();
		setZeroes();

		// Get shared preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

		return rootView;
	}

	private void findViews() {

		// Find the template views
		LinearLayout valueSlider = (LinearLayout) rootView.findViewById(R.id.slider_value);
		LinearLayout thresholdSlider = (LinearLayout) rootView.findViewById(R.id.slider_threshold);
		
		LinearLayout resultBox = (LinearLayout) rootView.findViewById(R.id.view_result);
		
		// Find seek bars
		valueSeekBar = (SeekBar) valueSlider.findViewById(R.id.seekbar);
		thresholdSeekBar = (SeekBar) thresholdSlider.findViewById(R.id.seekbar);

		// Find TextEdits
		valueEditText = (EditText) valueSlider.findViewById(R.id.textbox);
		thresholdEditText = (EditText) thresholdSlider.findViewById(R.id.textbox);

		// Find TextViews
		TextView valueHeader = (TextView) valueSlider.findViewById(R.id.header);
		TextView thresholdHeader = (TextView) thresholdSlider.findViewById(R.id.header);

		blackTextView = (TextView) resultBox.findViewById(R.id.result_black);
		whiteTextView = (TextView) resultBox.findViewById(R.id.result_white);

		resultTextView = (TextView) rootView.findViewById(R.id.result_text);

		// Find button
		rollButton = (Button) rootView.findViewById(R.id.roller_button);

		// Set descriptions of slider views
		valueHeader.setText(parentActivity.getString(R.string.value));
		thresholdHeader.setText(parentActivity.getString(R.string.threshold));

	}

	private void setListeners() {
		// Set listeners for seek bars
		valueSeekBar.setOnSeekBarChangeListener(new SeekListener(valueEditText));
		thresholdSeekBar.setOnSeekBarChangeListener(new SeekListener(thresholdEditText));

		// Set listeners for EditTexts
		valueEditText.addTextChangedListener(new EditWatcher(valueSeekBar, valueEditText));
		thresholdEditText.addTextChangedListener(new EditWatcher(thresholdSeekBar, thresholdEditText));

		// Set button listener
		rollButton.setOnClickListener(new RollClicker());
	}

	private void setZeroes() {
		// Set initial zeros
		valueEditText.setText(String.valueOf(DEFAULT_VALUE));
		valueSeekBar.setProgress(DEFAULT_VALUE);
		thresholdEditText.setText(String.valueOf(DEFAULT_THRESHOLD));
		thresholdSeekBar.setProgress(DEFAULT_THRESHOLD);
	}

	private void displayRollResult(VoltResult vr) {

		thisFragment.blackTextView.setText(String.valueOf(vr.black.value));
		thisFragment.whiteTextView.setText(String.valueOf(vr.white.value));

		String post;
		if (vr.bothSuccessful) {
			post = thisFragment.getString(R.string.double_success);
		} else if (vr.successful) {
			post = thisFragment.getString(R.string.success);
		} else {
			post = thisFragment.getString(R.string.fail);
		}

		thisFragment.resultTextView.setText(vr.result + " " + post);
	}

	/*
	 LISTENERS
	 */

	private class RollClicker implements View.OnClickListener {

		public void onClick(View v) {

			// Get Luck preference
			thisFragment.lastRollUsedLuck = prefs.getBoolean(PREF_USELUCK, true);

			// Get value and threshold
			thisFragment.lastValue = Integer.valueOf(thisFragment.valueEditText.getText().toString());
			thisFragment.lastThreshold = Integer.valueOf(thisFragment.thresholdEditText.getText().toString());

			VoltResult vr = new VoltResult(thisFragment.lastValue,
					thisFragment.lastThreshold,
					thisFragment.lastRollUsedLuck);

			thisFragment.displayRollResult(vr);
		}
	}

}

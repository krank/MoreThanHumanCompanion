package com.urverkspel.humancompanion;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
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
	private class SeekListener implements SeekBar.OnSeekBarChangeListener {

		private final EditText editText;

		public SeekListener(EditText editText) {
			this.editText = editText;
		}

		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (fromUser) {
				editText.setText(String.valueOf(progress));
			}
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	}

	private class EditWatcher implements TextWatcher {

		private final SeekBar seekBar;
		private final EditText editText;

		public EditWatcher(SeekBar seekBar, EditText editText) {
			this.seekBar = seekBar;
			this.editText = editText;
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		public void afterTextChanged(Editable s) {
			if (editText.hasFocus() && s.length() != 0
					&& Integer.valueOf(s.toString()) != seekBar.getProgress()) {
				int p = Math.min(Integer.valueOf(s.toString()), 32);
				seekBar.setProgress(p);
			}
		}

	}

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

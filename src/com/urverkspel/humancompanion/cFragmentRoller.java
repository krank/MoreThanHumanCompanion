package com.urverkspel.humancompanion;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import voltroll.VoltResult;

public class cFragmentRoller extends ContainerFragment {

	// General things
	private View rootView;
	private LinearLayout resultDisplayLayout;

	// UI elements
	private SeekBar valueSeekBar;
	private SeekBar thresholdSeekBar;

	private EditText valueEditText;
	private EditText thresholdEditText;
	
	private Button rollButton;

	// Data reference
	public RollData sharedRollData;
	
	// Preferences
	SharedPreferences prefs;

	@Override
	public void onAttach(Activity activity) { // Happens on rotation, before others
		attachSharedRollData();
		super.onAttach(activity);
	}

	@Override
	public void onStart() { // Happens on switch from distant tab, before setMenuVisibility on rotation
		if (sharedRollData != null) {
			setFromData();
			displayResultFromData();
		}
		super.onStart();
	}

	@Override
	public void setMenuVisibility(final boolean visible) { // Happens on switch from other tab, before onStart() unless rotating
		super.setMenuVisibility(visible);

		if (visible) {
			if (sharedRollData != null) {
				setFromData();
				displayResultFromData();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get root view of fragment
		this.rootView = inflater.inflate(R.layout.fragment_roller, container, false);

		findInterfaceElements();
		setListeners();
		setFromData();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		return rootView;
	}

	private void findInterfaceElements() {
		LinearLayout valueLayout = (LinearLayout) rootView.findViewById(R.id.slider_value);
		LinearLayout thresholdLayout = (LinearLayout) rootView.findViewById(R.id.slider_threshold);

		resultDisplayLayout = (LinearLayout) rootView.findViewById(R.id.display_result);

		// Seekbar
		valueSeekBar = (SeekBar) valueLayout.findViewById(R.id.seekbar);
		thresholdSeekBar = (SeekBar) thresholdLayout.findViewById(R.id.seekbar);

		// Seekbar-editTexts
		valueEditText = (EditText) valueLayout.findViewById(R.id.textbox);
		thresholdEditText = (EditText) thresholdLayout.findViewById(R.id.textbox);
		
		// Rolling button
		rollButton = (Button) rootView.findViewById(R.id.roller_button);
	}

	private void setListeners() {

		// SEEKBAR LISTENERS
		valueSeekBar.setOnSeekBarChangeListener(new SeekListener(valueEditText) {
			@Override
			public void saveData(int i) {
				sharedRollData.value = i;
			}
		});
		thresholdSeekBar.setOnSeekBarChangeListener(new SeekListener(thresholdEditText) {
			@Override
			public void saveData(int i) {
				sharedRollData.threshold = i;
			}
		});

		// EDITTEXT LISTENERS
		valueEditText.addTextChangedListener(new EditWatcher(valueSeekBar, valueEditText) {
			@Override
			public void saveData(int i) {
				sharedRollData.value = i;
			}
		});
		thresholdEditText.addTextChangedListener(new EditWatcher(thresholdSeekBar, thresholdEditText) {
			@Override
			public void saveData(int i) {
				sharedRollData.threshold = i;
			}
		});
		
		rollButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {

				boolean useLuck = prefs.getBoolean("pref_use_luck", true);

				sharedRollData.roll(useLuck);
				displayResultFromData();
				
			}
		});

	}

	private void setFromData() {

		attachSharedRollData();

		if (sharedRollData != null) {
			System.out.println("Setting from data. value is " + sharedRollData.value);
			valueEditText.setText(String.valueOf(sharedRollData.value));
			thresholdEditText.setText(String.valueOf(sharedRollData.threshold));

			valueSeekBar.setProgress(sharedRollData.value);
			thresholdSeekBar.setProgress(sharedRollData.threshold);
		}
	}

	private void displayResultFromData() {
		resultDisplayLayout.setVisibility(View.GONE);

		if (sharedRollData.result != null) {
			// TOHIT DISPLAY
			TextView header = (TextView) resultDisplayLayout.findViewById(R.id.display_header);
			TextView text = (TextView) resultDisplayLayout.findViewById(R.id.display_text);
			TextView icon = (TextView) resultDisplayLayout.findViewById(R.id.display_icon);

			header.setText(sharedRollData.result.result + " ");
			
			if (sharedRollData.result.bothSuccessful) {
				header.append(getActivity().getString(R.string.double_success) + "!");
			} else if (sharedRollData.result.successful) {
				header.append(getActivity().getString(R.string.success) + "!");
			} else {
				header.append(getActivity().getString(R.string.fail) + "!");
			}
			icon.setText(String.valueOf(sharedRollData.result.result));

			if (sharedRollData.result.wasLucky == VoltResult.LUCK_LUCKY) {
				icon.setBackgroundResource(R.drawable.yellow_bkg);
				header.append(" (" + getActivity().getString(R.string.lucky) + ")");
			} else if (sharedRollData.result.wasLucky == VoltResult.LUCK_UNLUCKY) {
				icon.setBackgroundResource(R.drawable.black_bkg);
				header.append(" (" + getActivity().getString(R.string.unlucky) + ")");
			} else if (sharedRollData.result.successful) {
				icon.setBackgroundResource(R.drawable.green_bkg);
			} else {
				icon.setBackgroundResource(R.drawable.red_bkg);
			}

			// Text
			text.setText(MainActivity.buildDiceResults(sharedRollData.result, getActivity()));

			// Make visible
			resultDisplayLayout.setVisibility(View.VISIBLE);
		}
	}

	public void attachSharedRollData() {
		if (sharedRollData == null) {
			RollDataHolder hold = (RollDataHolder) getActivity();
			sharedRollData = hold.getRollData();
		}
		
		System.out.println("Got data: " + sharedRollData);
	}

}

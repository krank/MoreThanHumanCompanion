package com.urverkspel.humancompanion;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

public class AttackArmorFragment extends Fragment {

	// Preferences
	SharedPreferences prefs;

	// General things
	private View rootView;
	private ScrollView scrollView;

	// UI elements
	private TextView penetrationHeader;
	private TextView coverageHeader;
	private TextView protectionHeader;

	private SeekBar penetrationSeekBar;
	private SeekBar coverageSeekBar;

	private EditText penetrationEditText;
	private EditText coverageEditText;
	private EditText protectionMinEditText;
	private EditText protectionMaxEditText;

	private CheckBox useArmorCheckBox;

	private RangeSeekBar<Integer> protectionSeekBar;

	// Data reference
	public AttackData sharedAttackData;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		AttackDataHolder hold = (AttackDataHolder) activity;

		sharedAttackData = hold.getAttackData();

	}

	@Override
	public void onStart() {
		this.setFromData();
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Create local references to general things
		rootView = inflater.inflate(R.layout.fragment_combat_armor, container, false);

		findInterfaceElements();
		addRangeSelector();
		updateHeadings();
		setListeners();

		setFromData();

		return rootView;
	}

	private void addRangeSelector() {
		protectionSeekBar = new RangeSeekBar<Integer>(0, 32, getActivity());

		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.layout_protection);

		layout.addView(protectionSeekBar);
	}

	private void findInterfaceElements() {

		scrollView = (ScrollView) rootView.findViewById(R.id.scroller);

		LinearLayout penetrationLayout = (LinearLayout) rootView.findViewById(R.id.slider_penetration);
		LinearLayout coverageLayout = (LinearLayout) rootView.findViewById(R.id.slider_coverage);

		LinearLayout protectionLayout = (LinearLayout) rootView.findViewById(R.id.slider_protection);

		penetrationHeader = (TextView) penetrationLayout.findViewById(R.id.header);
		coverageHeader = (TextView) coverageLayout.findViewById(R.id.header);
		protectionHeader = (TextView) protectionLayout.findViewById(R.id.header);

		// Seekbars
		penetrationSeekBar = (SeekBar) penetrationLayout.findViewById(R.id.seekbar);
		coverageSeekBar = (SeekBar) coverageLayout.findViewById(R.id.seekbar);

		// Seekbar-editTexts
		penetrationEditText = (EditText) penetrationLayout.findViewById(R.id.textbox);
		coverageEditText = (EditText) coverageLayout.findViewById(R.id.textbox);
		protectionMinEditText = (EditText) protectionLayout.findViewById(R.id.textbox_min);
		protectionMaxEditText = (EditText) protectionLayout.findViewById(R.id.textbox_max);

		// Checkbox
		useArmorCheckBox = (CheckBox) rootView.findViewById(R.id.chk_use_armor);

	}

	private void setListeners() {

		// CHECKBOX LISTENER
		useArmorCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sharedAttackData.useArmor = isChecked;
			}
		});

		// SEEKBAR LISTENERS
		penetrationSeekBar.setOnSeekBarChangeListener(new SeekListener(penetrationEditText) {
			@Override
			public void saveData(int i) {
				sharedAttackData.penetration = i;
			}
		});
		coverageSeekBar.setOnSeekBarChangeListener(new SeekListener(coverageEditText) {
			@Override
			public void saveData(int i) {
				sharedAttackData.coverage = i;
			}
		});

		protectionSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
			public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Integer minValue, Integer maxValue) {
				protectionMinEditText.setText(minValue.toString());
				protectionMaxEditText.setText(maxValue.toString());

				sharedAttackData.protectionMin = minValue;
				sharedAttackData.protectionMax = maxValue;

			}
		});

		// EDITTEXT LISTENERS
		penetrationEditText.addTextChangedListener(new EditWatcher(penetrationSeekBar, penetrationEditText) {
			@Override
			public void saveData(int i) {
				sharedAttackData.penetration = i;
			}
		});
		coverageEditText.addTextChangedListener(new EditWatcher(coverageSeekBar, coverageEditText) {
			@Override
			public void saveData(int i) {
				sharedAttackData.coverage = i;
			}
		});

		// SPECIAL PROTECTION SEEKBAR THINGS
		protectionMinEditText.addTextChangedListener(new EditWatcherRanged(protectionSeekBar, protectionMinEditText, false) {
			@Override
			public void saveData(int i) {
				sharedAttackData.protectionMin = i;
			}
		});
		protectionMaxEditText.addTextChangedListener(new EditWatcherRanged(protectionSeekBar, protectionMaxEditText, true) {
			@Override
			public void saveData(int i) {
				sharedAttackData.protectionMax = i;
			}
		});

		// Ugly fix to avoid min being more than max. Uses values from seekbar.
		OnFocusChangeListener protectionFocusChangeListener = new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					protectionMinEditText.setText(protectionSeekBar.getSelectedMinValue().toString());
					protectionMaxEditText.setText(protectionSeekBar.getSelectedMaxValue().toString());
				}
			}
		};

		protectionMinEditText.setOnFocusChangeListener(protectionFocusChangeListener);
		protectionMaxEditText.setOnFocusChangeListener(protectionFocusChangeListener);

		// Fix seekbar + scroller issue
		OnTouchListener overrideListener = new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
					scrollView.requestDisallowInterceptTouchEvent(true);
				}

				return false;
			}
		};

		penetrationSeekBar.setOnTouchListener(overrideListener);
		coverageSeekBar.setOnTouchListener(overrideListener);

	}

	private void updateHeadings() {
		penetrationHeader.setText(getActivity().getString(R.string.weapon_penetration));
		coverageHeader.setText(getActivity().getString(R.string.armor_coverage));
		protectionHeader.setText(getActivity().getString(R.string.armor_protection));

	}

	private void setFromData() {
		if (sharedAttackData == null) {
			AttackDataHolder hold = (AttackDataHolder) getActivity();

			sharedAttackData = hold.getAttackData();
		}
		if (sharedAttackData != null) {
			penetrationEditText.setText(String.valueOf(sharedAttackData.penetration));
			coverageEditText.setText(String.valueOf(sharedAttackData.coverage));
			protectionMinEditText.setText(String.valueOf(sharedAttackData.protectionMin));
			protectionMaxEditText.setText(String.valueOf(sharedAttackData.protectionMax));

			penetrationSeekBar.setProgress(sharedAttackData.penetration);
			coverageSeekBar.setProgress(sharedAttackData.coverage);
			protectionSeekBar.setSelectedMinValue(sharedAttackData.protectionMin);
			protectionSeekBar.setSelectedMaxValue(sharedAttackData.protectionMax);
		}

	}

}

package com.urverkspel.humancompanion;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

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

	private TextView valueHeader;
	private TextView thresholdHeader;
	private TextView damageHeader;
	private TextView penetrationHeader;
	private TextView coverageHeader;
	private TextView protectionHeader;

	private SeekBar valueSeekBar;
	private SeekBar thresholdSeekBar;
	private SeekBar damageSeekBar;
	private SeekBar penetrationSeekBar;
	private SeekBar coverageSeekBar;

	private EditText valueEditText;
	private EditText thresholdEditText;
	private EditText damageEditText;
	private EditText penetrationEditText;
	private EditText coverageEditText;
	private EditText protectionMinEditText;
	private EditText protectionMaxEditText;

	private CheckBox useArmor;

	private RangeSeekBar<Integer> protectionSeekbar;

	// Constants
	static final int DEFAULT_VALUE = 9;
	static final int DEFAULT_THRESHOLD = 0;
	static final int DEFAULT_DAMAGE = 5;
	static final int DEFAULT_PENETRATION = 0;
	static final int DEFAULT_COVERAGE = 0;
	static final int DEFAULT_PROTECTION_MIN = 0;
	static final int DEFAULT_PROTECTION_MAX = 5;

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
		updateHeadings(false);
		setListeners();

		setDefaults();

		return rootView;
	}

	private void addRangeSelector() {
		protectionSeekbar = new RangeSeekBar<Integer>(1, 32, parentActivity);

		protectionLayout.addView(protectionSeekbar);

	}

	private void findInterfaceElements() {

		LinearLayout valueLayout = (LinearLayout) rootView.findViewById(R.id.slider_value);
		LinearLayout thresholdLayout = (LinearLayout) rootView.findViewById(R.id.slider_threshold);
		LinearLayout damageLayout = (LinearLayout) rootView.findViewById(R.id.slider_damage);
		LinearLayout penetrationLayout = (LinearLayout) rootView.findViewById(R.id.slider_penetration);
		LinearLayout coverageLayout = (LinearLayout) rootView.findViewById(R.id.slider_coverage);

		armorLayout = (LinearLayout) rootView.findViewById(R.id.layout_armor);
		protectionLayout = (LinearLayout) armorLayout.findViewById(R.id.layout_protection);

		useArmor = (CheckBox) rootView.findViewById(R.id.chk_use_armor);

		// Headers
		valueHeader = (TextView) valueLayout.findViewById(R.id.header);
		thresholdHeader = (TextView) thresholdLayout.findViewById(R.id.header);
		damageHeader = (TextView) damageLayout.findViewById(R.id.header);
		penetrationHeader = (TextView) penetrationLayout.findViewById(R.id.header);
		coverageHeader = (TextView) coverageLayout.findViewById(R.id.header);
		protectionHeader = (TextView) protectionLayout.findViewById(R.id.header);

		// Seekbars
		valueSeekBar = (SeekBar) valueLayout.findViewById(R.id.seekbar);
		thresholdSeekBar = (SeekBar) thresholdLayout.findViewById(R.id.seekbar);
		damageSeekBar = (SeekBar) damageLayout.findViewById(R.id.seekbar);
		penetrationSeekBar = (SeekBar) penetrationLayout.findViewById(R.id.seekbar);
		coverageSeekBar = (SeekBar) coverageLayout.findViewById(R.id.seekbar);

		// Seekbar-editTexts
		valueEditText = (EditText) valueLayout.findViewById(R.id.textbox);
		thresholdEditText = (EditText) thresholdLayout.findViewById(R.id.textbox);
		damageEditText = (EditText) damageLayout.findViewById(R.id.textbox);
		penetrationEditText = (EditText) penetrationLayout.findViewById(R.id.textbox);
		coverageEditText = (EditText) coverageLayout.findViewById(R.id.textbox);
		protectionMinEditText = (EditText) armorLayout.findViewById(R.id.textbox_min);
		protectionMaxEditText = (EditText) armorLayout.findViewById(R.id.textbox_max);
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

		RadioGroup combatType = (RadioGroup) rootView.findViewById(R.id.radio_combat_type);

		combatType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radio_closecombat) {
					updateHeadings(true);
				} else {
					updateHeadings(false);
				}
			}
		});

		valueSeekBar.setOnSeekBarChangeListener(new SeekListener(valueEditText));
		thresholdSeekBar.setOnSeekBarChangeListener(new SeekListener(thresholdEditText));
		damageSeekBar.setOnSeekBarChangeListener(new SeekListener(damageEditText));
		penetrationSeekBar.setOnSeekBarChangeListener(new SeekListener(penetrationEditText));
		coverageSeekBar.setOnSeekBarChangeListener(new SeekListener(coverageEditText));

		valueEditText.addTextChangedListener(new EditWatcher(valueSeekBar, valueEditText));
		thresholdEditText.addTextChangedListener(new EditWatcher(thresholdSeekBar, thresholdEditText));
		damageEditText.addTextChangedListener(new EditWatcher(damageSeekBar, damageEditText));
		penetrationEditText.addTextChangedListener(new EditWatcher(penetrationSeekBar, penetrationEditText));
		coverageEditText.addTextChangedListener(new EditWatcher(coverageSeekBar, coverageEditText));

	}

	private void updateHeadings(Boolean isCloseCombat) {
		if (isCloseCombat) {
			valueHeader.setText(parentActivity.getString(R.string.attack));
			thresholdHeader.setText(parentActivity.getString(R.string.defense));
		} else {
			valueHeader.setText(parentActivity.getString(R.string.rkf));
			thresholdHeader.setText(parentActivity.getString(R.string.range));
		}
		damageHeader.setText(parentActivity.getString(R.string.weapon_damage));
		penetrationHeader.setText(parentActivity.getString(R.string.weapon_penetration));
		coverageHeader.setText(parentActivity.getString(R.string.armor_coverage));
		protectionHeader.setText(parentActivity.getString(R.string.armor_protection));

	}

	private void setDefaults() {
		valueEditText.setText(String.valueOf(DEFAULT_VALUE));
		thresholdEditText.setText(String.valueOf(DEFAULT_THRESHOLD));
		damageEditText.setText(String.valueOf(DEFAULT_DAMAGE));
		penetrationEditText.setText(String.valueOf(DEFAULT_PENETRATION));
		coverageEditText.setText(String.valueOf(DEFAULT_COVERAGE));
		protectionMinEditText.setText(String.valueOf(DEFAULT_PROTECTION_MIN));
		protectionMaxEditText.setText(String.valueOf(DEFAULT_PROTECTION_MAX));
	}

}

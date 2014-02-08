package com.urverkspel.humancompanion;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

public class CombatFragment extends Fragment {

	// Preferences
	SharedPreferences prefs;

	// General things
	private View rootView;
	private Activity parentActivity;
	private ScrollView scrollView;

	// UI elements
	private LinearLayout armorLayout;
	private LinearLayout protectionLayout;
	
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
	
	private RangeSeekBar<Integer> protectionSeekBar;

	// Constants
	static final int DEFAULT_VALUE = 9;
	static final int DEFAULT_THRESHOLD = 0;
	static final int DEFAULT_DAMAGE = 5;
	static final int DEFAULT_PENETRATION = 0;
	static final int DEFAULT_COVERAGE = 0;
	static final int DEFAULT_PROTECTION_MIN = 0;
	static final int DEFAULT_PROTECTION_MAX = 5;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Create local references to general things
		parentActivity = this.getActivity();
		rootView = inflater.inflate(R.layout.fragment_combat, container, false);
		
		findInterfaceElements();
		addRangeSelector();
		updateHeadings(false);
		setListeners();
		
		setDefaults();
		
		return rootView;
	}
	
	private void setupActionBar() {
		ActionBar actionBar = parentActivity.getActionBar();

		// Enable tab navigation mode
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
	}
	
	private void addRangeSelector() {
		protectionSeekBar = new RangeSeekBar<Integer>(0, 32, parentActivity);
		protectionLayout.addView(protectionSeekBar);
	}
	
	private void findInterfaceElements() {
		
		scrollView = (ScrollView) rootView.findViewById(R.id.scroller);
		
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
		
		// USE ARMOR CHECKBOX
		useArmor.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					armorLayout.setVisibility(View.VISIBLE);
				} else {
					armorLayout.setVisibility(View.GONE);
				}
			}
		});
		
		// COMBAT TYPE
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
		
		// SEEKBARS & TEXT BOXES
		valueSeekBar.setOnSeekBarChangeListener(new SeekListener(valueEditText));
		thresholdSeekBar.setOnSeekBarChangeListener(new SeekListener(thresholdEditText));
		damageSeekBar.setOnSeekBarChangeListener(new SeekListener(damageEditText));
		penetrationSeekBar.setOnSeekBarChangeListener(new SeekListener(penetrationEditText));
		coverageSeekBar.setOnSeekBarChangeListener(new SeekListener(coverageEditText));
		
		protectionSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
			public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
				protectionMinEditText.setText(minValue.toString());
				protectionMaxEditText.setText(maxValue.toString());
			}
		});
		
		valueEditText.addTextChangedListener(new EditWatcher(valueSeekBar, valueEditText));
		thresholdEditText.addTextChangedListener(new EditWatcher(thresholdSeekBar, thresholdEditText));
		damageEditText.addTextChangedListener(new EditWatcher(damageSeekBar, damageEditText));
		penetrationEditText.addTextChangedListener(new EditWatcher(penetrationSeekBar, penetrationEditText));
		coverageEditText.addTextChangedListener(new EditWatcher(coverageSeekBar, coverageEditText));
		
		// SPECIAL PROTECTION SEEKBAR THINGS
		protectionMinEditText.addTextChangedListener(new EditWatcherRanged(protectionSeekBar, protectionMinEditText, false));
		protectionMaxEditText.addTextChangedListener(new EditWatcherRanged(protectionSeekBar, protectionMaxEditText, true));
		
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
		
		valueSeekBar.setOnTouchListener(overrideListener);
		thresholdSeekBar.setOnTouchListener(overrideListener);
		damageSeekBar.setOnTouchListener(overrideListener);
		penetrationSeekBar.setOnTouchListener(overrideListener);
		coverageSeekBar.setOnTouchListener(overrideListener);
		
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

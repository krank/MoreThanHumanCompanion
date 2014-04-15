package com.urverkspel.humancompanion;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

public class AttackFragment extends Fragment {

	// Preferences
	SharedPreferences prefs;

	// General things
	private View rootView;
	private ScrollView scrollView;

	// UI elements
	private TextView valueHeader;
	private TextView thresholdHeader;
	private TextView damageHeader;

	private SeekBar valueSeekBar;
	private SeekBar thresholdSeekBar;
	private SeekBar damageSeekBar;

	private EditText valueEditText;
	private EditText thresholdEditText;
	private EditText damageEditText;

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
		rootView = inflater.inflate(R.layout.fragment_combat, container, false);

		findInterfaceElements();
		updateHeadings(false);
		

		setFromData();
		
		setListeners();

		return rootView;
	}

	private void findInterfaceElements() {

		scrollView = (ScrollView) rootView.findViewById(R.id.scroller);

		LinearLayout valueLayout = (LinearLayout) rootView.findViewById(R.id.slider_value);
		LinearLayout thresholdLayout = (LinearLayout) rootView.findViewById(R.id.slider_threshold);
		LinearLayout damageLayout = (LinearLayout) rootView.findViewById(R.id.slider_damage);

		// Headers
		valueHeader = (TextView) valueLayout.findViewById(R.id.header);
		thresholdHeader = (TextView) thresholdLayout.findViewById(R.id.header);
		damageHeader = (TextView) damageLayout.findViewById(R.id.header);

		// Seekbars
		valueSeekBar = (SeekBar) valueLayout.findViewById(R.id.seekbar);
		thresholdSeekBar = (SeekBar) thresholdLayout.findViewById(R.id.seekbar);
		damageSeekBar = (SeekBar) damageLayout.findViewById(R.id.seekbar);

		// Seekbar-editTexts
		valueEditText = (EditText) valueLayout.findViewById(R.id.textbox);
		thresholdEditText = (EditText) thresholdLayout.findViewById(R.id.textbox);
		damageEditText = (EditText) damageLayout.findViewById(R.id.textbox);
	}

	private void setListeners() {

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

		// SEEKBAR LISTENERS
		valueSeekBar.setOnSeekBarChangeListener(new SeekListener(valueEditText) {
			@Override
			public void saveData(int i) {
				sharedAttackData.value = i;
			}
		});
		thresholdSeekBar.setOnSeekBarChangeListener(new SeekListener(thresholdEditText) {
			@Override
			public void saveData(int i) {
				sharedAttackData.threshold = i;
			}
		});
		damageSeekBar.setOnSeekBarChangeListener(new SeekListener(damageEditText) {
			@Override
			public void saveData(int i) {
				sharedAttackData.damage = i;
			}
		});

		// EDITTEXT LISTENERS
		valueEditText.addTextChangedListener(new EditWatcher(valueSeekBar, valueEditText) {
			@Override
			public void saveData(int i) {
				sharedAttackData.value = i;
			}
		});
		thresholdEditText.addTextChangedListener(new EditWatcher(thresholdSeekBar, thresholdEditText) {
			@Override
			public void saveData(int i) {
				sharedAttackData.threshold = i;
			}
		});
		damageEditText.addTextChangedListener(new EditWatcher(damageSeekBar, damageEditText) {
			@Override
			public void saveData(int i) {
				sharedAttackData.damage = i;
			}
		});

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

	}

	private void updateHeadings(Boolean isCloseCombat) {
		Activity a = getActivity();
		if (isCloseCombat) {
			valueHeader.setText(a.getString(R.string.attack));
			thresholdHeader.setText(a.getString(R.string.defense));
		} else {
			valueHeader.setText(a.getString(R.string.rcf));
			thresholdHeader.setText(a.getString(R.string.range));
		}
		damageHeader.setText(a.getString(R.string.weapon_damage));
	}

	private void setFromData() {

		if (sharedAttackData == null) {
			AttackDataHolder hold = (AttackDataHolder) getActivity();

			sharedAttackData = hold.getAttackData();
		}
		
		if (sharedAttackData != null) {
			valueEditText.setText(String.valueOf(sharedAttackData.value));
			thresholdEditText.setText(String.valueOf(sharedAttackData.threshold));
			damageEditText.setText(String.valueOf(sharedAttackData.damage));

			valueSeekBar.setProgress(sharedAttackData.value);
			thresholdSeekBar.setProgress(sharedAttackData.threshold);
			damageSeekBar.setProgress(sharedAttackData.damage);
		}
	}

}

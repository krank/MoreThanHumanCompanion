package com.urverkspel.humancompanion;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class CombatFragment extends Fragment {

    // Preferences
    SharedPreferences prefs;

    // General things
    private View rootView;
    private Activity parentActivity;
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

    // Constants
    static final int DEFAULT_VALUE = 9;
    static final int DEFAULT_THRESHOLD = 0;
    static final int DEFAULT_DAMAGE = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Create local references to general things
        parentActivity = this.getActivity();
        rootView = inflater.inflate(R.layout.fragment_combat, container, false);

        findInterfaceElements();
        updateHeadings(false);
        setListeners();

        setDefaults();

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

        // SEEKBARS & TEXT BOXES
        valueSeekBar.setOnSeekBarChangeListener(new SeekListener(valueEditText));
        thresholdSeekBar.setOnSeekBarChangeListener(new SeekListener(thresholdEditText));
        damageSeekBar.setOnSeekBarChangeListener(new SeekListener(damageEditText));

        valueEditText.addTextChangedListener(new EditWatcher(valueSeekBar, valueEditText));
        thresholdEditText.addTextChangedListener(new EditWatcher(thresholdSeekBar, thresholdEditText));
        damageEditText.addTextChangedListener(new EditWatcher(damageSeekBar, damageEditText));

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
        if (isCloseCombat) {
            valueHeader.setText(parentActivity.getString(R.string.attack));
            thresholdHeader.setText(parentActivity.getString(R.string.defense));
        } else {
            valueHeader.setText(parentActivity.getString(R.string.rkf));
            thresholdHeader.setText(parentActivity.getString(R.string.range));
        }
        damageHeader.setText(parentActivity.getString(R.string.weapon_damage));

    }

    private void setDefaults() {
        valueEditText.setText(String.valueOf(DEFAULT_VALUE));
        thresholdEditText.setText(String.valueOf(DEFAULT_THRESHOLD));
        damageEditText.setText(String.valueOf(DEFAULT_DAMAGE));

        valueSeekBar.setProgress(3);
        thresholdSeekBar.setProgress(DEFAULT_THRESHOLD);
        damageSeekBar.setProgress(DEFAULT_DAMAGE);
    }

    @Override
    public void onStart() {
        this.setDefaults();
        super.onStart();
    }

}

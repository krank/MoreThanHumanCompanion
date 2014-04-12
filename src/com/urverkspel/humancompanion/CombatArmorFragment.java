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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

public class CombatArmorFragment extends Fragment {

    // Preferences
    SharedPreferences prefs;

    // General things
    private View rootView;
    private Activity parentActivity;
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

    private RangeSeekBar<Integer> protectionSeekBar;

    // Constants
    static final int DEFAULT_PENETRATION = 0;
    static final int DEFAULT_COVERAGE = 0;
    static final int DEFAULT_PROTECTION_MIN = 0;
    static final int DEFAULT_PROTECTION_MAX = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Create local references to general things
        parentActivity = this.getActivity();
        rootView = inflater.inflate(R.layout.fragment_combat_armor, container, false);

        findInterfaceElements();
        addRangeSelector();
        updateHeadings();
        setListeners();

        setDefaults();

        return rootView;
    }

    private void addRangeSelector() {
        protectionSeekBar = new RangeSeekBar<Integer>(0, 32, parentActivity);

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

    }

    private void setListeners() {

        // SEEKBARS & TEXT BOXES
        penetrationSeekBar.setOnSeekBarChangeListener(new SeekListener(penetrationEditText));
        coverageSeekBar.setOnSeekBarChangeListener(new SeekListener(coverageEditText));

        protectionSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                protectionMinEditText.setText(minValue.toString());
                protectionMaxEditText.setText(maxValue.toString());
            }
        });

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

        penetrationSeekBar.setOnTouchListener(overrideListener);
        coverageSeekBar.setOnTouchListener(overrideListener);

    }

    private void updateHeadings() {
        penetrationHeader.setText(parentActivity.getString(R.string.weapon_penetration));
        coverageHeader.setText(parentActivity.getString(R.string.armor_coverage));
        protectionHeader.setText(parentActivity.getString(R.string.armor_protection));

    }

    private void setDefaults() {
        penetrationEditText.setText(String.valueOf(DEFAULT_PENETRATION));
        coverageEditText.setText(String.valueOf(DEFAULT_COVERAGE));
        protectionMinEditText.setText(String.valueOf(DEFAULT_PROTECTION_MIN));
        protectionMaxEditText.setText(String.valueOf(DEFAULT_PROTECTION_MAX));
    }

}

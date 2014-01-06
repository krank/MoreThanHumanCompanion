package com.urverkspel.humancompanion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import voltroll.VoltResult;

public class MainActivity extends Activity {

	// Drawer things
	private String[] drawerListItems;
	private ListView drawerListView;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.roller);

		thisActivity = this;
		
		
		drawerListItems = getResources().getStringArray(R.array.drawer_items);
		drawerListView = (ListView) findViewById(R.id.left_drawer);
		
		drawerListView.setAdapter(new ArrayAdapter<String>(this, 
		R.layout.drawer_listview_item,
		drawerListItems));
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		actionBarDrawerToggle = new ActionBarDrawerToggle(
				this,
				drawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close
		);
		
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		

		// Find seek bars
		valueSeekBar = (SeekBar) this.findViewById(R.id.roller_value_seekbar);
		thresholdSeekBar = (SeekBar) this.findViewById(R.id.roller_threshold_seekbar);

		// Find TextEdits
		valueEditText = (EditText) this.findViewById(R.id.roller_value_edittext);
		thresholdEditText = (EditText) this.findViewById(R.id.roller_threshold_edittext);

		// Find TextViews
		blackTextView = (TextView) this.findViewById(R.id.roller_black);
		whiteTextView = (TextView) this.findViewById(R.id.roller_white);

		resultTextView = (TextView) this.findViewById(R.id.roller_result);

		// Find button
		rollButton = (Button) this.findViewById(R.id.roller_button);

		// Set listeners for seek bars
		valueSeekBar.setOnSeekBarChangeListener(new SeekListener(valueEditText));
		thresholdSeekBar.setOnSeekBarChangeListener(new SeekListener(thresholdEditText));

		// Set listeners for EditTexts
		valueEditText.addTextChangedListener(new EditWatcher(valueSeekBar, valueEditText));
		thresholdEditText.addTextChangedListener(new EditWatcher(thresholdSeekBar, thresholdEditText));

		// Set button listener
		rollButton.setOnClickListener(new RollClicker());

		// Set initial zeros
		valueEditText.setText(String.valueOf(DEFAULT_VALUE));
		valueSeekBar.setProgress(DEFAULT_VALUE);
		thresholdEditText.setText(String.valueOf(DEFAULT_THRESHOLD));
		thresholdSeekBar.setProgress(DEFAULT_THRESHOLD);

		// Get shared preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		// Set default preferences
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

	}

	private class RollClicker implements View.OnClickListener {

		public void onClick(View v) {

			// Get Luck preference
			thisActivity.lastRollUsedLuck = prefs.getBoolean(PREF_USELUCK, true);

			// Get value and threshold
			thisActivity.lastValue = Integer.valueOf(thisActivity.valueEditText.getText().toString());
			thisActivity.lastThreshold = Integer.valueOf(thisActivity.thresholdEditText.getText().toString());

			VoltResult vr = new VoltResult(thisActivity.lastValue,
					thisActivity.lastThreshold,
					thisActivity.lastRollUsedLuck);

			thisActivity.displayRollResult(vr);
		}
	}

	private void displayRollResult(VoltResult vr) {

		thisActivity.blackTextView.setText(String.valueOf(vr.black.value));
		thisActivity.whiteTextView.setText(String.valueOf(vr.white.value));

		String post;
		if (vr.bothSuccessful) {
			post = thisActivity.getString(R.string.double_success);
		} else if (vr.successful) {
			post = thisActivity.getString(R.string.success);
		} else {
			post = thisActivity.getString(R.string.fail);
		}

		thisActivity.resultTextView.setText(vr.result + " " + post);
	}

	private class SeekListener implements OnSeekBarChangeListener {

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		this.getMenuInflater().inflate(R.menu.rollermenu, menu);
		return true;

	}

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		switch (item.getItemId()) {
			case R.id.roller_settings:
				Intent intent = new Intent(thisActivity, SettingsActivity.class);
				thisActivity.startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);

		}

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		if (thisActivity.whiteTextView.length() > 0) {
			// Save white and black results
			savedInstanceState.putInt(STATE_BLACK, Integer.valueOf(thisActivity.blackTextView.getText().toString()));
			savedInstanceState.putInt(STATE_WHITE, Integer.valueOf(thisActivity.whiteTextView.getText().toString()));

			// Save wether roll used luck
			savedInstanceState.putBoolean(STATE_USEDLUCK, thisActivity.lastRollUsedLuck);

			// Save value and threshold
			savedInstanceState.putInt(STATE_VALUE, thisActivity.lastValue);
			savedInstanceState.putInt(STATE_THRESHOLD, thisActivity.lastThreshold);
		}

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_BLACK)) {

			int black = savedInstanceState.getInt(STATE_BLACK);
			int white = savedInstanceState.getInt(STATE_WHITE);

			thisActivity.lastValue = savedInstanceState.getInt(STATE_VALUE);
			thisActivity.lastThreshold = savedInstanceState.getInt(STATE_THRESHOLD);
			thisActivity.lastRollUsedLuck = savedInstanceState.getBoolean(STATE_USEDLUCK);

			VoltResult vr = new VoltResult(thisActivity.lastValue,
					thisActivity.lastThreshold,
					thisActivity.lastRollUsedLuck,
					black,
					white);

			thisActivity.displayRollResult(vr);
		}
	}

}

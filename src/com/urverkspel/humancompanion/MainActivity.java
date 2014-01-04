package com.urverkspel.humancompanion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {

	private MainActivity thisActivity;

	private SeekBar valueSeekBar;
	private SeekBar thresholdSeekBar;

	private EditText valueEditText;
	private EditText thresholdEditText;
	
	private final int DEFAULT_VALUE = 9;
	private final int DEFAULT_THRESHOLD = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.roller);

		thisActivity = this;

		// Find seek bars
		valueSeekBar = (SeekBar) this.findViewById(R.id.roller_value_seekbar);
		thresholdSeekBar = (SeekBar) this.findViewById(R.id.roller_threshold_seekbar);

		// Find TextEdits
		valueEditText = (EditText) this.findViewById(R.id.roller_value_edittext);
		thresholdEditText = (EditText) this.findViewById(R.id.roller_threshold_edittext);

		// Set listeners for seek bars
		valueSeekBar.setOnSeekBarChangeListener(new SeekListener(valueEditText));
		thresholdSeekBar.setOnSeekBarChangeListener(new SeekListener(thresholdEditText));

		// Set listeners for EditTexts
		valueEditText.addTextChangedListener(new EditWatcher(valueSeekBar, valueEditText));
		thresholdEditText.addTextChangedListener(new EditWatcher(thresholdSeekBar, thresholdEditText));
		
		// Set initial zeros
		valueEditText.setText(String.valueOf(DEFAULT_VALUE));
		valueSeekBar.setProgress(DEFAULT_VALUE);
		thresholdEditText.setText(String.valueOf(DEFAULT_THRESHOLD));
		thresholdSeekBar.setProgress(DEFAULT_THRESHOLD);

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
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.roller_settings:
				Intent intent = new Intent(thisActivity, SettingsActivity.class);
				thisActivity.startActivity(intent);
				return true;
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);

		}

	}
}

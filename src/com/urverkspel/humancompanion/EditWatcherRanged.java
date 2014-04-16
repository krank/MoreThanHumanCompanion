package com.urverkspel.humancompanion;

import android.text.Editable;
import android.text.TextWatcher;

public class EditWatcherRanged implements TextWatcher {

	private final RangeSeekBar seekBar;

	private final Boolean watchMax;

	public EditWatcherRanged(RangeSeekBar seekBar, Boolean watchMax) {
		this.seekBar = seekBar;
		this.watchMax = watchMax;
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	public void afterTextChanged(Editable s) {

		if (s.length() != 0) {
			int p = Math.min(Integer.valueOf(s.toString()), 32);
			if (!watchMax) {
				seekBar.setSelectedMinValue(p);
			} else {
				seekBar.setSelectedMaxValue(p);
			}
			saveData(p);
		}
	}

	public void saveData(int i) {

	}

}

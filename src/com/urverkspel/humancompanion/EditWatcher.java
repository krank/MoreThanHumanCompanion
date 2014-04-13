package com.urverkspel.humancompanion;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;

public class EditWatcher implements TextWatcher {

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
			saveData(p);
		}
	}

	public void saveData(int i) {

	}
}

package com.urverkspel.humancompanion;

import android.widget.EditText;
import android.widget.SeekBar;

public class SeekListener implements SeekBar.OnSeekBarChangeListener {

	private final EditText editText;

	public SeekListener(EditText editText) {
		this.editText = editText;
	}

	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser) {
			editText.setText(String.valueOf(progress));
			saveData(progress);
		}
	}

	public void saveData(int i) {
		
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}

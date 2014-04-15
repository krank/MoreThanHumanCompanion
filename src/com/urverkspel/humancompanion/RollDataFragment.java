package com.urverkspel.humancompanion;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class RollDataFragment extends Fragment {
	private RollData rollData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
	}
	
	public void setData(RollData data) {
		rollData = data;
	}
	
	public RollData getData() {
		if (rollData == null) {
			setData(new RollData());
		}
		return rollData;
	}
}

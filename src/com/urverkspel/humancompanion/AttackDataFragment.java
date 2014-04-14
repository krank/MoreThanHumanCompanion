package com.urverkspel.humancompanion;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class AttackDataFragment extends Fragment {
	private AttackData attackData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
	}
	
	public void setData(AttackData data) {
		attackData = data;
	}
	
	public AttackData getData() {
		if (attackData == null) {
			setData(new AttackData());
		}
		return attackData;
	}
}

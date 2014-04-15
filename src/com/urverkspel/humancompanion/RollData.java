package com.urverkspel.humancompanion;

import voltroll.VoltResult;

public class RollData {
	public int value;
	public int threshold;
	
	public VoltResult result;
	
	static final int DEFAULT_VALUE = 9;
	static final int DEFAULT_THRESHOLD = 0;
	
	public RollData() {
		setDefaults();
	}
	
	public final void setDefaults() {
		value = DEFAULT_VALUE;
		threshold = DEFAULT_THRESHOLD;
	}
	
	public void roll(boolean useLuck) {
		result = new VoltResult(value, threshold, useLuck);
	}
}

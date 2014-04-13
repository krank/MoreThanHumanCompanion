package com.urverkspel.humancompanion;

public class AttackData {

    public int value;
    public int threshold;
    public int damage;
    public boolean useArmor;
	public int penetration;
    public int coverage;
    public int protectionMin;
    public int protectionMax;
	
	// Constants
    static final int DEFAULT_PENETRATION = 0;
    static final int DEFAULT_COVERAGE = 0;
    static final int DEFAULT_PROTECTION_MIN = 0;
    static final int DEFAULT_PROTECTION_MAX = 5;
	static final boolean DEFAULT_USEARMOR = false;
	
	static final int DEFAULT_VALUE = 9;
	static final int DEFAULT_THRESHOLD = 0;
	static final int DEFAULT_DAMAGE = 5;
	
	public AttackData() {
		setDefaults();
	}
	
	public final void setDefaults() {
		value = DEFAULT_VALUE;
		threshold = DEFAULT_THRESHOLD;
		damage = DEFAULT_DAMAGE;
		
		useArmor = DEFAULT_USEARMOR;
		penetration = DEFAULT_PENETRATION;
		coverage = DEFAULT_COVERAGE;
		protectionMin = DEFAULT_PROTECTION_MIN;
		protectionMax = DEFAULT_PROTECTION_MAX;
		
	}
    
}

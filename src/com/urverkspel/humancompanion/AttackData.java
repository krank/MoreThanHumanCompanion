package com.urverkspel.humancompanion;

import android.app.Activity;
import voltroll.VoltResult;

public class AttackData {

	public int value;
	public int threshold;
	public int damage;
	public boolean useArmor;
	public int penetration;
	public int coverage;
	public int protectionMin;
	public int protectionMax;
	public boolean isChock;

	public VoltResult hitResult;
	public VoltResult damageResult;

	public int resultHits;
	public int resultDamage;
	public boolean resultIsChock;
	public int resultArmorEffect;
	/*
	 Armor setting OFF
	 Insufficient Coverage ( white > coverage )
	 Completely penetrated ( penetration > protection )
	 Partially penetrated ( penetration in protection )
	 Completely protected (penetration < protection )
	 */

	// Constants
	static final int ARMOR_OFF = 0;
	static final int ARMOR_NOT_COVERED = 1;
	static final int ARMOR_FULLY_PENETRATED = 2;
	static final int ARMOR_PARTIALLY_PENETRATED = 3;
	static final int ARMOR_COMPLETELY_PROTECTED = 4;

	static final int DEFAULT_PENETRATION = 0;
	static final int DEFAULT_COVERAGE = 0;
	static final int DEFAULT_PROTECTION_MIN = 0;
	static final int DEFAULT_PROTECTION_MAX = 5;
	static final boolean DEFAULT_USEARMOR = false;

	static final int DEFAULT_VALUE = 9;
	static final int DEFAULT_THRESHOLD = 0;
	static final int DEFAULT_DAMAGE = 5;
	static final boolean DEFAULT_IS_CHOCK = false;

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

	public String makeResultString() {
		StringBuilder sb = new StringBuilder(); // String builder to construct result string

		// THE TOHIT ROLL
		sb.append("Roll to hit: White ");
		sb.append(hitResult.white.value);

		sb.append(" Black ");
		sb.append(hitResult.black.value);
		sb.append("\n");

		if (resultHits == 2) {
			sb.append("FULL HIT!\n");
		} else if (resultHits == 1) {
			sb.append("Hit!\n");
		} else {
			sb.append("Miss!\n");
		}

		if (resultHits > 0) {
			switch (resultArmorEffect) {
				case ARMOR_OFF:
					sb.append("Armor setting: Disabled.");
					break;
				default:
					sb.append("Penetration/Protection:\n");
				case ARMOR_NOT_COVERED:
					sb.append("Coverage insufficient; attack ignores armor.");
					break;
				case ARMOR_FULLY_PENETRATED:
					sb.append("Penetration sufficient to ignore armor completely.");
					break;
				case ARMOR_PARTIALLY_PENETRATED:
					sb.append("Armor is penetrated but still offers some protection.");
					break;
				case ARMOR_COMPLETELY_PROTECTED:
					sb.append("Insufficient Penetration to breach armor.");
					break;
			}

			sb.append("\n");

			// THE DAMAGE ROLL
			sb.append("Roll to damage: White ");
			sb.append(damageResult.white.value);

			sb.append(" Black ");
			sb.append(damageResult.black.value);

			sb.append("\n");

			if (resultDamage >= 0) {

				sb.append("Skada: ");
				sb.append(resultDamage);

				if (resultIsChock) {
					sb.append(" Chock.");
				} else {
					sb.append(" Trauma.");
				}
			} else {
				sb.append("No damage");
			}

		}
		return sb.toString();
	}

	public void roll() {

		int effectiveDamage = damage;
		int effectivePenetration = penetration;
		
		resultIsChock = isChock;

		// HITTING
		hitResult = new VoltResult(value, threshold, false);

		// full hit = increase effective damage
		if (hitResult.bothSuccessful) {
			effectiveDamage += 5;
			resultHits = 2;
		} else if (hitResult.successful) {
			resultHits = 1;
		}

		if (hitResult.successful) {

			// Armor
			if (useArmor) {

				if (hitResult.white.value < coverage) {
					if (penetration < protectionMin) {
						resultArmorEffect = ARMOR_COMPLETELY_PROTECTED;
						effectiveDamage = 0;
					} else if (effectivePenetration > protectionMax) {
						resultArmorEffect = ARMOR_FULLY_PENETRATED;
					} else {
						resultArmorEffect = ARMOR_PARTIALLY_PENETRATED;
						effectiveDamage -= 5;
						resultIsChock = true;
					}
				} else {
					resultArmorEffect = ARMOR_NOT_COVERED;
				}
			} else {
				resultArmorEffect = ARMOR_OFF;
			}

			// Damage
			damageResult = new VoltResult(effectiveDamage, false);

			if (damageResult.successful) {
				resultDamage = damageResult.result;
			} else {
				resultDamage = -1;
			}
		} else {
			resultHits = 0;
		}

	}

}

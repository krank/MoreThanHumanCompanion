package voltroll;

public class VoltResult {

	public VoltDice white;
	public VoltDice black;

	public Boolean successful = true;
	public Boolean bothSuccessful = false;
	public int result;

	public VoltResult(int level) {
		this(level, 0, true);
	}

	public VoltResult(int level, int threshold) {
		this(level, threshold, true);
	}

	public VoltResult(int level, Boolean useLuck) {
		this(level, 0, useLuck, 0, 0);
	}

	public VoltResult(int level, int threshold, Boolean useLuck) {
		this(level, threshold, useLuck, 0, 0);
	}

	public VoltResult(int level, int threshold, Boolean useLuck, int blackValue, int whiteValue) {

		// Create dice & roll them
		white = new VoltDice("white", whiteValue);
		black = new VoltDice("black", blackValue);

		VoltDice[] vd = {white, black};
		for (VoltDice d : vd) {
			d.success = (d.value > threshold) // value is > threshold
					// Is true if d.value is below level, or black is 1, or if luck rule isn't in effect
					&& (d.value <= level || black.value == 1 || !useLuck)
					// Is true if d.value is below level or luck rule is in effect
					&& (d.value <= level || useLuck); // 
		}

		if (black.value == 20 && useLuck) {
			result = maxResult(level, white.value);
			successful = false;
		} else if (black.success && white.success) {
			result = Math.max(
					maxResult(black.value, level),
					maxResult(white.value, level)
			);
			bothSuccessful = true;
		} else if (black.success) {
			result = maxResult(black.value, level);
		} else if (white.success) {
			result = maxResult(white.value, level);
		} else {
			result = Math.max(black.value, white.value);
			successful = false;
		}
	}

	private int maxResult(int level, int result) {
		int space = result - level;
		return level + space - (space % 20);
	}

}

package voltroll;

import java.util.Random;

public final class VoltDice {

	 public int value;
	 public String color;
	 public Boolean success;

	 public VoltDice(String color) {
		  this(color, 0);
	 }

	 public VoltDice(String color, int value) {
		  if (value != 0) {
				this.value = value;
		  } else {
				this.roll();
		  }
		  this.color = color;
	 }

	 public void roll() {
		  Random gen = new Random();
		  this.value = gen.nextInt(20) + 1;
	 }
	

}

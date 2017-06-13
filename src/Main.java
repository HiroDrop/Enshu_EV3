import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LCD.drawString("Hello world", 0, 4);
		Delay.msDelay(5000);
	}

}

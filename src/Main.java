import block.Controller;
import block.Plant;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {
		Controller controller = new Controller();	// Controller
		Plant ev3 = new Plant();	// Plant (EV3)
		
		double motorSpeed = 0.0f;	// �����̏o�́C�v�����g�ւ̓���
		double ev3Output = 0.0f;	//�@�v�����g�̏o�́C�����ւ̓���
		while(true){
			motorSpeed = controller.calc(ev3Output);
			ev3Output = ev3.calc(motorSpeed);
		}
	}

}

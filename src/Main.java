import block.Controller;
import block.Plant;
import lejos.utility.Delay;

public class Main{

	static int key;
	static final double SETPOINT = 90.0f;
	
	public static void main(String[] args) {
		Controller controller = new Controller();	// Controller
		Plant ev3 = new Plant();	// Plant (EV3)
		
		Delay.msDelay(5000);
		
		double motorSpeed = 0.0f;	// 制御器の出力，プラントへの入力
		double ev3Output = 0.0f;	//　プラントの出力，制御器への入力
		while(true){
			motorSpeed = controller.calc(SETPOINT - ev3Output);
			ev3Output = ev3.calc(motorSpeed);
		}
	}


}

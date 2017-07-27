import block.Controller;
import block.Plant;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;

public class Main{

	static int key;
	static final double SETPOINT = 0.0f;
	
	public static void main(String[] args) {
		Controller controller = new Controller();	// Controller
		Plant ev3 = new Plant();	// Plant (EV3)
		Stopwatch stopwatch = new Stopwatch();
		double before_time = stopwatch.elapsed();
		double now_time;
		final double T = 100.0f;

		double motorSpeed = 0.0f;	// �����̏o�́C�v�����g�ւ̓���
		double ev3Output = 0.0f;	//�@�v�����g�̏o�́C�����ւ̓���
		while(true){
			now_time = stopwatch.elapsed();
			if(now_time - before_time > T){
				motorSpeed = controller.calc((ev3.isGoodCond()? 0.7f : 1.0f) * (SETPOINT - ev3Output));
			}
			ev3Output = ev3.calc(motorSpeed);
		}
	}


}

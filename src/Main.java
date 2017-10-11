import block.Controller;
import block.Plant;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import lejos.hardware.lcd.LCD;

public class Main{

	static final double SETPOINT = 0.0f;			// �Z�b�g�|�C���g
	
	public static void main(String[] args) {
		Controller controller = new Controller();	// Controller
		Plant ev3 = new Plant();					// Plant (EV3)
		Stopwatch stopwatch = new Stopwatch();		// �o�ߎ��Ԏ擾�p�X�g�b�v�E�H�b�`
		double before_time = stopwatch.elapsed();	// �ߋ��o�ߎ���
		double now_time;							// ���݌o�ߎ���
		double T = 10.0f;						// �^�C�����O

		
		double motorSpeed = 0.0f;	// ���[�^�[�̊p���x�i�����̏o�́C�v�����g�ւ̓��́j
		double ev3Output = 0.0f;	//�@�{�̂̊p���x�i�v�����g�̏o�́C�����ւ̓��́j
		while(true){
			if(ev3.getDeg() < 20.0f || ev3.getDeg() > 160.0f){
				T = 1000.0f;
			}
			else{
				T =0.0f;
			}
			now_time = stopwatch.elapsed();		// ���ݎ����̎擾
			double x_motorSpeed = controller.calc((ev3.isGoodCond()? -0.4f : 1.3f) * (SETPOINT - ev3Output));	// �����ɂ�鐧��J�n
			if(now_time - before_time > T){		// ��莞�Ԍo�߂�����
				before_time = now_time;			// �ߋ��o�ߎ��Ԃ��X�V
				motorSpeed = x_motorSpeed;
			}
			ev3Output = ev3.calc(motorSpeed);	// �W���C���Z���T����p���x���擾
		}
	}
}

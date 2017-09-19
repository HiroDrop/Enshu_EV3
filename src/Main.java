import block.Controller;
import block.Plant;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;

public class Main{

	static final double SETPOINT = 0.0f;			// �Z�b�g�|�C���g
	
	public static void main(String[] args) {
		Controller controller = new Controller();	// Controller
		Plant ev3 = new Plant();					// Plant (EV3)
		Stopwatch stopwatch = new Stopwatch();		// �o�ߎ��Ԏ擾�p�X�g�b�v�E�H�b�`
		double before_time = stopwatch.elapsed();	// �ߋ��o�ߎ���
		double now_time;							// ���݌o�ߎ���
		final double T = 5.0f;						// �^�C�����O

		double motorSpeed = 0.0f;	// ���[�^�[�̊p���x�i�����̏o�́C�v�����g�ւ̓��́j
		double ev3Output = 0.0f;	//�@�{�̂̊p���x�i�v�����g�̏o�́C�����ւ̓��́j
		while(true){
			now_time = stopwatch.elapsed();		// ���ݎ����̎擾
			if(now_time - before_time > T){		// ��莞�Ԍo�߂�����
				motorSpeed = controller.calc((ev3.isGoodCond()? -0.4f : 1.0f) * (SETPOINT - ev3Output));	// �����ɂ�鐧��J�n
				before_time = now_time;			// �ߋ��o�ߎ��Ԃ��X�V
			}
			ev3Output = ev3.calc(motorSpeed);	// �W���C���Z���T����p���x���擾
		}
	}
}

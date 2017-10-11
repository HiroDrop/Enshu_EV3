import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import block.Controller;
import block.Plant;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;

public class Main{

	static final double SETPOINT = 0.0f;			// �Z�b�g�|�C���g
	
	public static void main(String[] args) {
		Controller controller = new Controller();	// Controller
		Plant ev3 = new Plant();					// Plant (EV3)
		Stopwatch stopwatch = new Stopwatch();		// �o�ߎ��Ԏ擾�p�X�g�b�v�E�H�b�`
		double before_time = stopwatch.elapsed();	// �ߋ��o�ߎ���
		double now_time;							// ���݌o�ߎ���
		double T = 0.0f;						// �^�C�����O

        EV3TouchSensor touch = new EV3TouchSensor(SensorPort.S2);
        SensorMode touchMode = touch.getTouchMode();
        float[] sampleTouch = new float[touchMode.sampleSize()];
		
		double motorSpeed = 0.0f;	// ���[�^�[�̊p���x�i�����̏o�́C�v�����g�ւ̓��́j
		double ev3Output = 0.0f;	//�@�{�̂̊p���x�i�v�����g�̏o�́C�����ւ̓��́j

		Client client = new Client();
		
		while(true){
	        touchMode.fetchSample(sampleTouch, 0);
	        if((int)sampleTouch[0] != 0) break;

			now_time = stopwatch.elapsed();		// ���ݎ����̎擾
			if(now_time - before_time > T){		// ��莞�Ԍo�߂�����
//				double x_motorSpeed = controller.calc((ev3.isGoodCond()? -0.4f : 1.3f) * (SETPOINT - ev3Output));	// �����ɂ�鐧��J�n
				double x_motorSpeed = controller.calc(1.3f * (SETPOINT - ev3Output));	// �����ɂ�鐧��J�n
				before_time = now_time;			// �ߋ��o�ߎ��Ԃ��X�V
				motorSpeed = x_motorSpeed;
			}
			ev3Output = ev3.calc(motorSpeed);	// �W���C���Z���T����p���x���擾
		}
	}
}

class Client{
	private Socket s;
	private PrintWriter pw;

	public Client(){
		try{
			s = new Socket("10.0.1.1", 6000);
			pw = new PrintWriter(s.getOutputStream(), true);
		}catch(Exception e){
			System.err.println("Failed to connect: " + e);
			System.exit(1);
		}
		System.out.println("connected");
	}
	
	public void print(String msg){
		pw.println(msg);
	}
}

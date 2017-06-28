package block;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;

/*
 * Plant program (EV3)
 */
public class Plant implements block{

    private EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);
    private RegulatedMotor leftMotor = Motor.A;
    private RegulatedMotor rightMotor = Motor.B;	
    private SensorMode gyro = gyroSensor.getMode(1);
    
    public Plant(){
    	gyroSensor.reset();
    }
    
	@Override
	public double calc(double input) {
		//ジャイロセンサーの値
		float gyrovalue[] = new float[gyro.sampleSize()];

		//モーターのスピードと向きを設定
		leftMotor.setSpeed(Math.abs((int)input));
		rightMotor.setSpeed(Math.abs((int)input));
		if(input < 0.0f){
			leftMotor.backward();
			rightMotor.backward();
		}
		else{
			leftMotor.forward();
			rightMotor.forward();
		}
		
		//ジャイロセンサーの値を取得
		gyro.fetchSample(gyrovalue, 0);

		//ジャイロセンサーの値を[0.0-359.9]に正規化
		double output = (double)gyrovalue[0];
		while(output > 360.0f) output -= 360.0f;

		//画面にジャイロセンサーの角度を表示
		LCD.clear();
		LCD.drawString("degree:"+output, 1, 0);
		LCD.refresh();
		
		return output;
	}

}

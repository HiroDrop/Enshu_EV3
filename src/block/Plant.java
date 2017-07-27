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
    private SensorMode gyro = gyroSensor.getMode(0);

    public Plant(){
    	gyroSensor.reset();
		LCD.clear();
		LCD.drawString("reset!", 1, 0);
		LCD.refresh();
    }

	@Override
	public double calc(double input) {
		//角度格納用
		float gyrovalue[] = new float[gyro.sampleSize()];

		//モータースピードをセット
		leftMotor.setSpeed(Math.abs((int)input));
		rightMotor.setSpeed(Math.abs((int)input));

		//print input
//		LCD.clear();
//		LCD.drawString("speed:"+ input + "\n", 1, 0);
//		LCD.refresh();

		if(input < 0.0f){
			leftMotor.backward();
			rightMotor.backward();
		}
		else{
			leftMotor.forward();
			rightMotor.forward();
		}

		//角度を取得
		gyro.fetchSample(gyrovalue, 0);

		//値の正規化
		double output = (double)gyrovalue[0];
//		while(output > 360.0f) output -= 360.0f;

		//画面出力
		LCD.clear();
		LCD.drawString("degree:"+output, 1, 0);
		LCD.refresh();

		return output;
	}

}
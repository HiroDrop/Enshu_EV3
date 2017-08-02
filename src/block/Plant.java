package block;

import data.PlantData;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Stopwatch;
import lejos.hardware.Button;

/*
 * Plant program (EV3)
 */
public class Plant implements block{

    private EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);
    private RegulatedMotor leftMotor = Motor.A;
    private RegulatedMotor rightMotor = Motor.B;
    private SensorMode gyro_v = gyroSensor.getMode(0);
    private SensorMode gyro_deg = gyroSensor.getMode(1);
    private PlantData outdata = new PlantData();

    public Plant(){
    	gyroSensor.reset();
		LCD.clear();
		LCD.drawString("reset!", 1, 0);
		LCD.refresh();
    }

    public boolean isGoodCond(){
    	return outdata.isGoodCond();
    }
    
	@Override
	public double calc(double input) {
		//角度格納用
		float gyrovalue_v[] = new float[gyro_v.sampleSize()];
		float gyrovalue_deg[] = new float[gyro_deg.sampleSize()];

		//モータースピードをセット
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

		//角度を取得
		gyro_v.fetchSample(gyrovalue_v, 0);
		gyro_deg.fetchSample(gyrovalue_deg, 0);

		//値の正規化
		double v = (double)gyrovalue_v[0];
		double deg = (double)gyrovalue_deg[0];
//		while(output > 360.0f) output -= 360.0f;

		if(deg > 90.0f) Button.LEDPattern(1);
		else Button.LEDPattern(2);
		
		//画面出力
		//print input
		LCD.clear();
		LCD.drawString("v:"+ v, 1, 0);
		LCD.drawString("deg:"+ deg, 1, 1);
		LCD.refresh();


		outdata.setData(v, deg);
		return v;
	}

}
package block;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lejos.hardware.lcd.LCD;
import lejos.utility.Stopwatch;

public class Controller implements block{

	static private final byte flags = 0x03;
	private final int history_size = 5;
	private final double K_p = 4.0f;
	private final double K_i = history_size / 5f;
	private final double K_d = 0.0f;
	private final double backP = 1.3f;
	private double before = 0.0f;
	private double before_time;
	private Stopwatch stopwatch;
	private boolean startflg = false;
	private List<Double>history = new ArrayList<Double>();

	public Controller(){
		stopwatch = new Stopwatch();
		before_time = stopwatch.elapsed();
//		System.out.println("K_p,K_i,K_d,speed,error,"+K_p+","+K_i+","+K_d+"\n");
	}

	@Override
	public double calc(double input) {
		if(!startflg){
			startflg = true;
			return 0.0f;
		}
		
		double motorSpeed = 0.0f;
			double nowtime = stopwatch.elapsed();

			if((flags & 0x01) > 0x00){	//比例制御
				motorSpeed += K_p * input;
//				System.out.print(K_p * input + ",");
			}
			if((flags & 0x02) > 0x00){	//積分制御
				if(history.size() == 5) history.remove(0);
				history.add(input);
				double sum = 0.0f;
				for(Double data : history){
					sum += K_i * data;
				}
				motorSpeed += sum;
//				System.out.print(sum + ",");
			}
			if((flags & 0x04) > 0x00){	//微分制御
				motorSpeed += K_d * (input - before) / (nowtime - before_time);
				before_time = nowtime;
				before = input;
			}

			int signed = (motorSpeed > 0.0f)? 1:-1;
			double motorabs = Math.abs(motorSpeed);

			motorSpeed = signed * Math.pow(motorabs / 780.0f, 1.7f) * 780.0f;
//			System.out.println(motorSpeed);
			
//			motorSpeed = signed * Math.pow(motorabs / 20.0f, 2.0f) / Math.pow(motorabs / 390.0f - 2.0f, 2.0f);
			
//			if(motorSpeed < 0.0f) motorSpeed *= backP;
			
		return motorSpeed;
	}

}
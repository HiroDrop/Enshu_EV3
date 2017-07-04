package block;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import lejos.utility.Stopwatch;

public class Controller implements block{

	static private final byte flags = 0x07;
	private final double SETPOINT = 94.73f;
	private final double K_p = 1.2f;
	private final double K_i = 0.25f;
	private final double K_d = 0.4f;
	private double iHistory = 0.0f;
	private double before = 0.0f;
	private double before_time;
	private Stopwatch stopwatch;
	private boolean startflg = false;

	public Controller(){
		stopwatch = new Stopwatch();
		before_time = stopwatch.elapsed();
		try{
			File csv = new File("outputData.csv");
			BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
			bw.write("K_p,K_i,K_d,speed,error,"+K_p+","+K_i+","+K_d+"\n");
			bw.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public double calc(double input) {
		if(!startflg){
			startflg = true;
			return 0.0f;
		}
		double degree = 0.0f;
		double motorSpeed = 0.0f;
		try{
			File csv = new File("outputData.csv");
			BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));

			double nowtime = stopwatch.elapsed();
			String newline = "";

			if((flags & 0x01) > 0x00){	//比例制御
				degree += input * K_p;
				newline += input * K_p + ",";
			}
			if((flags & 0x02) > 0x00){	//積分制御
				iHistory += input * K_i;
				degree += iHistory;
				newline += iHistory + ",";
			}
			if((flags & 0x04) > 0x00){	//微分制御
				degree += K_d * (input - before) / (nowtime - before_time);
				newline += K_d * (input - before) / (nowtime - before_time)+",";
				before_time = nowtime;
				before = input;
			}
			
			motorSpeed = 2.6f * (input < 0.0f? -1.0f:1.0f) * 14.0f * Math.sqrt(2.0f * (1.0f - Math.cos(Math.abs(input - degree)))) / (11.0f * Math.PI) * 360.0f;
			
			newline += motorSpeed+","+input+"\n";
			bw.write(newline);
			bw.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return motorSpeed;
	}

}
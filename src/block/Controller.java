package block;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lejos.utility.Stopwatch;

public class Controller implements block{

	static private final byte flags = 0x03;
	private final double K_p = 0.7f;
	private final double K_i = 0.45f;
	private final double K_d = 0.0f;
	private final double backP = 1.3f;
	private double before = 0.0f;
	private double before_time;
	private Stopwatch stopwatch;
	private boolean startflg = false;
	private List<Double> history = new ArrayList<Double>();

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
		
		
		double motorSpeed = 0.0f;
		try{
			File csv = new File("outputData.csv");
			BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));

			String newline = "";

			double nowtime = stopwatch.elapsed();

			if((flags & 0x01) > 0x00){	//比例制御
				motorSpeed += K_p * input;
				newline += input * K_p + ",";
			}
			else newline += 0.0f + ",";
			if((flags & 0x02) > 0x00){	//積分制御
				if(history.size() == 5) history.remove(0);
				history.add(input);
				double sum = 0.0f;
				for(Double data : history){
					sum += K_i * data;
				}
				motorSpeed += sum;
				newline += sum + ",";
			}
			else newline += 0.0f + ",";
			if((flags & 0x04) > 0x00){	//微分制御
				motorSpeed += K_d * (input - before) / (nowtime - before_time);
				newline += K_d * (input - before) / (nowtime - before_time)+",";
				before_time = nowtime;
				before = input;
			}
			else newline += 0.0f + ",";
			
			
//			motorSpeed = 14.0f * Math.sqrt(2.0f * (1.0f - Math.cos(Math.toRadians(Math.abs(input) + Math.abs(degree))))) / (11.0f * Math.PI) * 360.0f;
//			motorSpeed = (input < 0.0f? -1.0f:1.0f) * 0.0014f * motorSpeed * motorSpeed;
		
			if(motorSpeed > 0.0f) motorSpeed *= backP;
			
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
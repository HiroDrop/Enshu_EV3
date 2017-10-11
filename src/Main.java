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

	static final double SETPOINT = 0.0f;			// セットポイント
	
	public static void main(String[] args) {
		Controller controller = new Controller();	// Controller
		Plant ev3 = new Plant();					// Plant (EV3)
		Stopwatch stopwatch = new Stopwatch();		// 経過時間取得用ストップウォッチ
		double before_time = stopwatch.elapsed();	// 過去経過時間
		double now_time;							// 現在経過時間
		double T = 0.0f;						// タイムラグ

        EV3TouchSensor touch = new EV3TouchSensor(SensorPort.S2);
        SensorMode touchMode = touch.getTouchMode();
        float[] sampleTouch = new float[touchMode.sampleSize()];
		
		double motorSpeed = 0.0f;	// モーターの角速度（制御器の出力，プラントへの入力）
		double ev3Output = 0.0f;	//　本体の角速度（プラントの出力，制御器への入力）

		Client client = new Client();
		
		while(true){
	        touchMode.fetchSample(sampleTouch, 0);
	        if((int)sampleTouch[0] != 0) break;

			now_time = stopwatch.elapsed();		// 現在時刻の取得
			if(now_time - before_time > T){		// 一定時間経過したら
//				double x_motorSpeed = controller.calc((ev3.isGoodCond()? -0.4f : 1.3f) * (SETPOINT - ev3Output));	// 制御器による制御開始
				double x_motorSpeed = controller.calc(1.3f * (SETPOINT - ev3Output));	// 制御器による制御開始
				before_time = now_time;			// 過去経過時間を更新
				motorSpeed = x_motorSpeed;
			}
			ev3Output = ev3.calc(motorSpeed);	// ジャイロセンサから角速度を取得
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

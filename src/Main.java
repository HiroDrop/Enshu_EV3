import block.Controller;
import block.Plant;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;

public class Main{

	static final double SETPOINT = 0.0f;			// セットポイント
	
	public static void main(String[] args) {
		Controller controller = new Controller();	// Controller
		Plant ev3 = new Plant();					// Plant (EV3)
		Stopwatch stopwatch = new Stopwatch();		// 経過時間取得用ストップウォッチ
		double before_time = stopwatch.elapsed();	// 過去経過時間
		double now_time;							// 現在経過時間
		final double T = 5.0f;						// タイムラグ

		double motorSpeed = 0.0f;	// モーターの角速度（制御器の出力，プラントへの入力）
		double ev3Output = 0.0f;	//　本体の角速度（プラントの出力，制御器への入力）
		while(true){
			now_time = stopwatch.elapsed();		// 現在時刻の取得
			if(now_time - before_time > T){		// 一定時間経過したら
				motorSpeed = controller.calc((ev3.isGoodCond()? -0.4f : 1.0f) * (SETPOINT - ev3Output));	// 制御器による制御開始
				before_time = now_time;			// 過去経過時間を更新
			}
			ev3Output = ev3.calc(motorSpeed);	// ジャイロセンサから角速度を取得
		}
	}
}

package block;

/*
 * Controller program
 */
public class Controller implements block{

	static private final byte flags = 0x07;
	private final double SETPOINT = 90.0f;
	private final double K_p = 0.0f;
	private final double K_i = 0.0f;
	private final double K_d = 0.0f;
	private double motorSpeed = 0.0f;
	private double iHistory = 0.0f;
	
	@Override
	public double calc(double input) {
		motorSpeed = 0.0f;
		if((flags & 0x01) > 0x00){	//”ä—á§Œä
			motorSpeed += input * K_i;
		}
		if((flags & 0x02) > 0x00){	//Ï•ª§Œä
			iHistory += input * K_p;
			motorSpeed = iHistory;
		}
		if((flags & 0x04) > 0x00){	//”÷•ª§Œä
			//¡‚ÍƒpƒX
		}
		return motorSpeed;
	}

}

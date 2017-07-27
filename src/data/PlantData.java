package data;

public class PlantData {
	private double v;
	private double deg;
	
	public boolean isGoodCond(){
		if((deg > 90.0f && v < 0.0f) || (deg < 90.0f && v > 0.0f)) return true;
		return false;
	}
	
	public void setData(double dv, double ddeg){
		v = dv;
		deg = ddeg;
	}
}

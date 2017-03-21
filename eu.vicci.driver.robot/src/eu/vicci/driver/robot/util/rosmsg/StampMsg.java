package eu.vicci.driver.robot.util.rosmsg;

public class StampMsg {

	private double secs;
	private double nsecs;
	
	public StampMsg(){
	}

	public double getSecs() {
		return secs;
	}

	public double getNsecs() {
		return nsecs;
	}

	@Override
	public String toString() {
		return "StampMsg [secs=" + secs + ", nsecs=" + nsecs + "]";
	}
	
}

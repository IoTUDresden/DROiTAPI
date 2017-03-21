package eu.vicci.driver.robot.util.rosmsg;

public class PoseMsgStamped {

	HeaderMsg header;
	PoseMsg pose;
	
	public HeaderMsg getHeader() {
		return header;
	}
	
	public void setHeader(HeaderMsg header) {
		this.header = header;
	}
	
	public PoseMsg getPose() {
		return pose;
	}
	
	public void setPose(PoseMsg pose) {
		this.pose = pose;
	}

	@Override
	public String toString() {
		return "PoseMsgStamped [header=" + header + ", pose=" + pose + "]";
	}
	
}

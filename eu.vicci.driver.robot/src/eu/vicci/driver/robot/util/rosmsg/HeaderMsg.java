package eu.vicci.driver.robot.util.rosmsg;


public class HeaderMsg {

	private StampMsg stamp;
	private String frame_id;
	private int seq;
	
	public HeaderMsg(){
	}

	public StampMsg getStamp() {
		return stamp;
	}

	public void setStamp(StampMsg stamp) {
		this.stamp = stamp;
	}

	public String getFrame_id() {
		return frame_id;
	}

	public void setFrame_id(String frame_id) {
		this.frame_id = frame_id;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	@Override
	public String toString() {
		return "HeaderMsg [stamp=" + stamp + ", frame_id=" + frame_id
				+ ", seq=" + seq + "]";
	}
	
}

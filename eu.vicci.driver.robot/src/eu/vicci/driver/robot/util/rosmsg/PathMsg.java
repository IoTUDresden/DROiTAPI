package eu.vicci.driver.robot.util.rosmsg;

import java.util.ArrayList;
import java.util.List;

public class PathMsg {

	HeaderMsg header;
	List<PoseMsgStamped> poses = new ArrayList<PoseMsgStamped>();

	public HeaderMsg getHeader() {
		return header;
	}
	public void setHeader(HeaderMsg header) {
		this.header = header;
	}
	public List<PoseMsgStamped> getPoses() {
		return poses;
	}
	public void setPoses(List<PoseMsgStamped> poses) {
		this.poses = poses;
	}
	@Override
	public String toString() {
		return "PathMsg [header=" + header + ", poses=" + poses + "]";
	}
	
}

package eu.vicci.driver.robot.util.rosmsg;

import java.util.ArrayList;
import java.util.List;

public class JointStateMsg {

	private HeaderMsg header;
	private List<String> name = new ArrayList<String>();
	private List<Double> position = new ArrayList<Double>();
	private List<Double> velocity = new ArrayList<Double>();
	private List<Double> effort = new ArrayList<Double>();

	public JointStateMsg(){
	}

	public HeaderMsg getHeader() {
		return header;
	}

	public void setHeader(HeaderMsg header) {
		this.header = header;
	}

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}

	public List<Double> getPosition() {
		return position;
	}

	public void setPosition(List<Double> position) {
		this.position = position;
	}

	public List<Double> getVelocity() {
		return velocity;
	}

	public void setVelocity(List<Double> velocity) {
		this.velocity = velocity;
	}

	public List<Double> getEffort() {
		return effort;
	}

	public void setEffort(List<Double> effort) {
		this.effort = effort;
	}
	
}

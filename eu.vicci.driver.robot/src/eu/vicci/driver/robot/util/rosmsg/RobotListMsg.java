package eu.vicci.driver.robot.util.rosmsg;

import java.util.ArrayList;
import java.util.List;

public class RobotListMsg {

	List<RobotInfoMsg> robots = new ArrayList<RobotInfoMsg>();

	public List<RobotInfoMsg> getRobots() {
		return robots;
	}

	public void setRobots(List<RobotInfoMsg> robots) {
		this.robots = robots;
	}
	
}

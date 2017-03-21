package eu.vicci.driver.robot.tools;

import java.util.List;

import eu.vicci.driver.robot.util.rosmsg.PositionMsg;

public interface TrajectoryListener {

	public void onTrajectoryMessageRecieved(List<PositionMsg> path);
	
}

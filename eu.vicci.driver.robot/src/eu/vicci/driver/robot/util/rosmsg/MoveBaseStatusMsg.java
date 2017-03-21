package eu.vicci.driver.robot.util.rosmsg;

import java.util.ArrayList;
import java.util.List;

public class MoveBaseStatusMsg {

	private HeaderMsg header;
	List<GoalStatusMsg> status_list = new ArrayList<GoalStatusMsg>();
	
	public MoveBaseStatusMsg(){
	}

	public HeaderMsg getHeader() {
		return header;
	}

	public List<GoalStatusMsg> getStatus_list() {
		return status_list;
	}
	
}

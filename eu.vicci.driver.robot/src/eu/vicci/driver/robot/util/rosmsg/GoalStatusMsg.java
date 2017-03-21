package eu.vicci.driver.robot.util.rosmsg;

public class GoalStatusMsg {
	
	public static final int PENDING        	= 0;   	// The goal has yet to be processed by the action server
	public static final int ACTIVE          = 1;   	// The goal is currently being processed by the action server
	public static final int PREEMPTED       = 2;   	// The goal received a cancel request after it started executing
													//   and has since completed its execution (Terminal State)
	public static final int SUCCEEDED       = 3;   	// The goal was achieved successfully by the action server (Terminal State)
	public static final int ABORTED         = 4;   	// The goal was aborted during execution by the action server due
	                            					//    to some failure (Terminal State)
	public static final int REJECTED        = 5;   	// The goal was rejected by the action server without being processed,
	                            					//    because the goal was unattainable or invalid (Terminal State)
	public static final int PREEMPTING      = 6;   	// The goal received a cancel request after it started executing
	                            					//    and has not yet completed execution
	public static final int RECALLING       = 7;   	// The goal received a cancel request before it started executing,
	                            					//    but the action server has not yet confirmed that the goal is canceled
	public static final int RECALLED        = 8;   	// The goal received a cancel request before it started executing
	                            					//    and was successfully cancelled (Terminal State)
	public static final int LOST            = 9;   	// An action client can determine that a goal is LOST. This should not be
	                            					//    sent over the wire by an action server
			                            
			                            
	private double status;
	private String text;
	private GoalIdMsg goal_id;
	
	public GoalStatusMsg(){
	}

	public double getStatus() {
		return status;
	}

	public String getText() {
		return text;
	}

	public GoalIdMsg getGoal_id() {
		return goal_id;
	}
	
}

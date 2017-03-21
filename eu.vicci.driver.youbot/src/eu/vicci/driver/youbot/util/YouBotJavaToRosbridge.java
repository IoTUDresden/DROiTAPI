package eu.vicci.driver.youbot.util;

import java.lang.reflect.Type;

import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.vicci.driver.robot.location.UnnamedLocation;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.driver.robot.util.RobotJavaToRosbridge;
import eu.vicci.driver.robot.util.rosmsg.GoalStatusMsg;
import eu.vicci.driver.robot.util.rosmsg.MoveBaseStatusMsg;
import eu.vicci.driver.robot.util.rosmsg.PoseMsg;
import eu.vicci.driver.robot.util.rosmsg.RosMsg;

public class YouBotJavaToRosbridge extends RobotJavaToRosbridge {

	
	private static final String MOVEBASE_STATUS_TOPIC = "/move_base/status";
	//private static final String YOUBOT_VELOCITY_TOPIC = "/cmd_vel";
	private static final String YOUBOT_VELOCITY_TOPIC = "/key_cmd_vel";
	
	private static final String TF_TRANSFORM_TOPIC = "/tf_map_transform";

	private static long GOAL_TIMEOUT = 4000;
	
	private UnnamedLocation currentLocation;
	private int navigationStatus = -1;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(String message) {
		
		Gson gson = new Gson();
		RosMsg<Object> rosMsg = new RosMsg<Object>();
		rosMsg = gson.fromJson(message, rosMsg.getClass());
		String topic = rosMsg.getTopic();
		
		if(topic.equals(MOVEBASE_STATUS_TOPIC)){						
			Type msgType = new TypeToken<RosMsg<MoveBaseStatusMsg>>(){}.getType();
			MoveBaseStatusMsg msg = (MoveBaseStatusMsg) ((RosMsg<MoveBaseStatusMsg>)gson.fromJson(message, msgType)).getMsg();
			navigationStatus = getNavigationStatusFromMsg(msg);
			
		} else if(topic.equals(TF_TRANSFORM_TOPIC)){
			Type msgType = new TypeToken<RosMsg<PoseMsg>>(){}.getType();
			PoseMsg msg = (PoseMsg) ((RosMsg<PoseMsg>)gson.fromJson(message, msgType)).getMsg();
			currentLocation = msg.toUnnamedLocation();
				
		}
	}
	
	private int getNavigationStatusFromMsg(MoveBaseStatusMsg msg) {
		for(GoalStatusMsg item : msg.getStatus_list()) {
			if(item.getStatus()==GoalStatusMsg.ACTIVE) return GoalStatusMsg.ACTIVE;
		} 
		return GoalStatusMsg.SUCCEEDED;
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		super.onOpen(serverHandshake);
		subscribe(MOVEBASE_STATUS_TOPIC);
		subscribe(TF_TRANSFORM_TOPIC);
	}
	
	public YouBotJavaToRosbridge(String ip, int port) {
		super(ip, port);
	}

	public void publishVelocityCommand(double linear, double angular){
		super.publishVelocityCommand(YOUBOT_VELOCITY_TOPIC, linear, angular);
	}
	
	public void publishStopCommand(){
		super.publishVelocityCommand(YOUBOT_VELOCITY_TOPIC, 0.0, 0.0);
	}
	
	public void moveTo(Position position, Orientation orientation) {

		if (navigationStatus == 1) {
			while (navigationStatus != 3) {
				Thread.yield();
			}
		}
		super.publishGoalCanceable(position,orientation);
		long time = System.currentTimeMillis();
		long timer = 0;
		while (navigationStatus != 1) {
			timer = System.currentTimeMillis() - time;
			if (timer > GOAL_TIMEOUT) {
				break;
			} else {
				Thread.yield();
			}
		}
		while (navigationStatus != 3) {
			Thread.yield();
		}
	}
	
	public void moveToNonblocking(Position position, Orientation orientation) {
		super.publishGoal(position,orientation);
	}
	
	@Override
	public UnnamedLocation getLocation(){
		return currentLocation;
	}
	
}

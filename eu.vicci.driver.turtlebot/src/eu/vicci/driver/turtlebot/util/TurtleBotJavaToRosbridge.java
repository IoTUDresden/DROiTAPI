package eu.vicci.driver.turtlebot.util;

import java.lang.reflect.Type;
import java.util.Map;

import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import eu.vicci.driver.robot.location.UnnamedLocation;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;
import eu.vicci.driver.robot.util.RobotJavaToRosbridge;
import eu.vicci.driver.robot.util.rosmsg.AutodockMsg;
import eu.vicci.driver.robot.util.rosmsg.GoalStatusMsg;
import eu.vicci.driver.robot.util.rosmsg.MoveBaseStatusMsg;
import eu.vicci.driver.robot.util.rosmsg.PoseMsg;
import eu.vicci.driver.robot.util.rosmsg.RosMsg;

/**
 * Functionality specific to the TurtleBot such as auto docking.
 */
public class TurtleBotJavaToRosbridge extends RobotJavaToRosbridge {

	private static final String TURTLEBOT_VELOCITY_TOPIC = "/mobile_base/commands/velocity";
//	private static final String TURTLEBOT_VELOCITY_TOPIC = "/key_cmd_vel";

	private static final String LED1_TOPIC = "/mobile_base/commands/led1";
	private static final String LED2_TOPIC = "/mobile_base/commands/led2";
	private static final String SOUND_TOPIC = "/mobile_base/commands/sound";

	private static final String AUTODOCKING_TOPIC = "/autodocking";
	private static final String MOVEBASE_STATUS_TOPIC = "/move_base/status";
	private static final String TF_TRANSFORM_TOPIC = "/tf_map_transform";
	
	// BeSpoon Topics 
	private static final String SRL_BESPOON_TOPIC = "/simpleRosLocation";
	private static final String SBL_BESPOON_TOPIC = "/simpleBeSpoonLocation";
	private static final String SNG_BESPOON_TOPIC = "/simple_coord_nav_goal";	

	private static final long DOCKING_STRING_TIMEOUT = 4000;
	private static long GOAL_TIMEOUT = 4000;
	
	private static final int INVALID_STATUS = -1;
	
	private String dockingInfo = "";
	private UnnamedLocation currentLocation;
	private int navigationStatus = INVALID_STATUS;
	private boolean doDocking;
	private Docking docking;
	private UnnamedLocation simpleRosLocation;
	private UnnamedLocation simpleBespoonLocation;

	public TurtleBotJavaToRosbridge(String ip, int port) {
		super(ip, port);
	}

	
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
			
		} else if(topic.equals(AUTODOCKING_TOPIC)){
			Type msgType = new TypeToken<RosMsg<AutodockMsg>>(){}.getType();
			AutodockMsg msg = (AutodockMsg) ((RosMsg<AutodockMsg>)gson.fromJson(message, msgType)).getMsg();
			dockingInfo = msg.getData();
			if (doDocking) docking.doDocking(dockingInfo);

		} else if(topic.equals(SRL_BESPOON_TOPIC)){
			Type msgType = new TypeToken<RosMsg<PoseMsg>>(){}.getType();
			PoseMsg msg = (PoseMsg) ((RosMsg<PoseMsg>)gson.fromJson(message, msgType)).getMsg();
			simpleRosLocation = msg.toUnnamedLocation();
			
		} else if(topic.equals(SBL_BESPOON_TOPIC)){
			Type msgType = new TypeToken<RosMsg<PoseMsg>>(){}.getType();
			PoseMsg msg = (PoseMsg) ((RosMsg<PoseMsg>)gson.fromJson(message, msgType)).getMsg();
			simpleBespoonLocation = msg.toUnnamedLocation();
			
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
		subscribe(AUTODOCKING_TOPIC, 500);
		subscribe(MOVEBASE_STATUS_TOPIC, 500);
		subscribe(TF_TRANSFORM_TOPIC);
		subscribe(SRL_BESPOON_TOPIC);
		subscribe(SBL_BESPOON_TOPIC);		
		subscribe(RobotJavaToRosbridge.CANCEL_GOAL_TOPIC);
		setLED1(0);
		setLED2(0);
		System.out.println("connected");
	}
	
	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		super.onClose(arg0, arg1, arg2);
		System.out.println("closed");
	}

	public void publishVelocityCommand(double linear, double angular) {
		super.publishVelocityCommand(TURTLEBOT_VELOCITY_TOPIC, linear, angular);
	}

	public void moveTo(Position position, Orientation orientation) {
		if (!isDockingInfoReady())
			return;
		if (navigationStatus == GoalStatusMsg.ACTIVE) {
			while (navigationStatus != GoalStatusMsg.SUCCEEDED) {
				Thread.yield();
			}
		}
		prepareMoveTo();

		super.publishGoalCanceable(position, orientation);
		long time = System.currentTimeMillis();
		long timer = 0;
		while (navigationStatus != GoalStatusMsg.ACTIVE) {
			timer = System.currentTimeMillis() - time;
			if (timer > GOAL_TIMEOUT) {
				break;
			} else {
				Thread.yield();
			}
		}
		while (navigationStatus != GoalStatusMsg.SUCCEEDED) {
			Thread.yield();
		}
	}

	public void moveToNonblocking(Position position, Orientation orientation) {
		prepareMoveTo();
		super.publishGoal(position, orientation);
	}

	public void prepareMoveTo() {
		if (isInDockingStation()) {
			driveBackwards();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isInDockingStation() {
		return (dockingInfo.toCharArray()[0] == '1');
	}

	public void driveBackwards() {

		for (int i = 0; i < 14; i++) {
			publishVelocityCommand(-0.10f, 0f);
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			thread.start();

			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public UnnamedLocation getLocation() {
		return currentLocation;
	}

	public void doDocking() {
		if (!isDockingInfoReady())
			return;
		else {
			docking = new Docking(this);
			doDocking = true;
		}
	}

	private boolean isDockingInfoReady() {
		if (dockingInfo.isEmpty()) {
			waitOnDockingInfo();
			if (dockingInfo.isEmpty()) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	private void waitOnDockingInfo() {
		long time = System.currentTimeMillis();
		long timer = 0;
		while (true) {
			timer = System.currentTimeMillis() - time;
			if ((timer > DOCKING_STRING_TIMEOUT) || !dockingInfo.isEmpty()) {
				return;
			} else {
				Thread.yield();
			}
		}
	}

	public void stopDocking() {
		doDocking = false;
	}

	/**
	 * @param color
	 *            black = 0, green = 1, orange = 2, red = 3
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setLED1(int color) {
		Map call = new LinkedTreeMap();
		call.put("value", color);
		publish(LED1_TOPIC, call);
	}

	/**
	 * @param color
	 *            black = 0, green = 1, orange = 2, red = 3
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setLED2(int color) {
		Map call = new LinkedTreeMap();
		call.put("value", color);
		publish(LED2_TOPIC, call);
	}

	/**
	 * @param sound
	 *            on = 0, off = 1, recharge = 2, button = 3, error = 4,
	 *            cleaningstart = 5, cleaningend = 6
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doSound(int sound) {
		Map call = new LinkedTreeMap();
		call.put("value", sound);
		publish(SOUND_TOPIC, call);
	}

	
	public UnnamedLocation getSimpleRosLocation() {
		return simpleRosLocation;
	}
	
	public UnnamedLocation getSimpleBeSpoonLocation() {
		return simpleBespoonLocation;
	}
}

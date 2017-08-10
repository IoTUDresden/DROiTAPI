package eu.vicci.driver.robot.util;

import java.net.URI;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import eu.vicci.driver.robot.location.Location;
import eu.vicci.driver.robot.location.UnnamedLocation;


public class RobotJavaToRosbridge extends WebSocketClient {

	private static final String SIMPLE_GOAL_TOPIC = "/move_base_simple/goal";
	private static final String GOAL_TOPIC = "/move_base/goal";
	private static final String INITIAL_POSE_TOPIC = "/initialpose";
	public static final String CANCEL_GOAL_TOPIC = "/move_base/cancel";
	
	public static final int DEFAULT_PORT = 9090;
	private static final String DEFAULT_GOAL_ID = "goal";
	
	public RobotJavaToRosbridge(String host) {
		super(URI.create("ws://" + host + ":" + DEFAULT_PORT));
	}

	public RobotJavaToRosbridge(String host, int port) {
		super(URI.create("ws://" + host + ":" + port));
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
	}

	@Override
	public void onError(Exception e) {
	}

	@Override
	public void onMessage(String m) {
	}

	@Override
	public void onOpen(ServerHandshake sh) {
	}
	
	public boolean isConnected(){
		return (getReadyState() == WebSocket.READYSTATE.OPEN); 
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void serviceCall(String service_name){
		Map call = new LinkedTreeMap();
		call.put("op", "call_service");
		call.put("service",service_name);
		send(new Gson().toJson(call));
	}
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void publish(String topic, Map msg) {
		Map call = new LinkedTreeMap();
		call.put("op", "publish");
		call.put("topic",topic);
		call.put("msg",msg);
		send(new Gson().toJson(call));
	}
		
	public void subscribe(String topic) {
		Map<String,String> call = new LinkedTreeMap<String,String>();
		call.put("op", "subscribe");
		call.put("topic", topic);
		send(new Gson().toJson(call));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void subscribe(String topic, int throttleRate) {
		Map call = new LinkedTreeMap();
		call.put("op", "subscribe");
		call.put("topic", topic);
		call.put("throttle_rate", throttleRate);
		send(new Gson().toJson(call));
	}

	public void publishVelocityCommand(String topic, double linear, double angular){
		Map<String,Map<String,Double>> msg = new LinkedTreeMap<String,Map<String,Double>>();
		Map<String,Double> linearVector = new LinkedTreeMap<String,Double>();
		Map<String,Double> angularVector = new LinkedTreeMap<String,Double>();
		
		linearVector.put("x", linear);
		linearVector.put("y", 0.0);
		linearVector.put("z", 0.0);
		
		angularVector.put("x", 0.0);
		angularVector.put("y", 0.0);
		angularVector.put("z", angular);
		
		msg.put("linear", linearVector);
		msg.put("angular",angularVector);
		publish(topic, msg);
	}
	
	private int seq = 0;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void publishGoalCanceable(Position pos, Orientation ori) {
		Map header = new LinkedTreeMap();
		header.put("seq",seq);
		header.put("stamp", getTimeStamp());
		header.put("frame_id", "/map");
		
		Map goalID = new LinkedTreeMap();
		int time = (int) (System.currentTimeMillis() / 1000L);
		goalID.put("stamp", time);
		goalID.put("id", DEFAULT_GOAL_ID + pos.getX() + "");
		
		Map posVector = new LinkedTreeMap();
		posVector.put("x", pos.getX());
		posVector.put("y", pos.getY());
		posVector.put("z", pos.getZ());
		
		Map oriVector = new LinkedTreeMap();
		oriVector.put("x", ori.getO_X());
		oriVector.put("y", ori.getO_Y());
		oriVector.put("z", ori.getO_Z());
		oriVector.put("w", ori.getO_W());
		
		Map pose = new LinkedTreeMap();
		pose.put("position", posVector);
		pose.put("orientation", oriVector);
		
		Map targetPose = new LinkedTreeMap();
		targetPose.put("header", header);
		targetPose.put("pose", pose);
		
		Map goal = new LinkedTreeMap();
		goal.put("target_pose", targetPose);
		
		
		Map call = new LinkedTreeMap();
		call.put("header", header);
		call.put("goal_id", goalID);
		call.put("goal", goal);
		
		publish(GOAL_TOPIC, call);
		seq++;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void publishGoal(Position pos, Orientation ori) {
		Map header = new LinkedTreeMap();
		header.put("seq",seq);
		header.put("stamp", getTimeStamp());
		header.put("frame_id", "/map");
		
		Map posVector = new LinkedTreeMap();
		posVector.put("x", pos.getX());
		posVector.put("y", pos.getY());
		posVector.put("z", pos.getZ());
		
		Map oriVector = new LinkedTreeMap();
		oriVector.put("x", ori.getO_X());
		oriVector.put("y", ori.getO_Y());
		oriVector.put("z", ori.getO_Z());
		oriVector.put("w", ori.getO_W());
		
		Map pose = new LinkedTreeMap();
		pose.put("position", posVector);
		pose.put("orientation", oriVector);
		
		Map msg = new LinkedTreeMap();
		msg.put("header", header);
		msg.put("pose", pose);
		
		publish(SIMPLE_GOAL_TOPIC, msg);
		seq++;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })	
	public void publishInitialPose(Position pos, Orientation ori) {
		Map header = new LinkedTreeMap();
		header.put("seq",seq);
		header.put("stamp", getTimeStamp());
		header.put("frame_id", "/map");
		
		Map posVector = new LinkedTreeMap();
		posVector.put("x", pos.getX());
		posVector.put("y", pos.getY());
		posVector.put("z", pos.getZ());
		
		Map oriVector = new LinkedTreeMap();
		oriVector.put("x", ori.getO_X());
		oriVector.put("y", ori.getO_Y());
		oriVector.put("z", ori.getO_Z());
		oriVector.put("w", ori.getO_W());
				
		Map pose = new LinkedTreeMap();
		pose.put("position", posVector);
		pose.put("orientation", oriVector);
		
		float[] cov = new float[36];
		cov[0] = 0.25f;
		cov[7] = 0.25f;
		cov[35] = 0.06853891945200942f;
		
		Map poseWithCovariance = new LinkedTreeMap();
		poseWithCovariance.put("pose", pose);
		poseWithCovariance.put("covariance", cov);
		
		Map call = new LinkedTreeMap();
		call.put("header", header);
		call.put("pose", poseWithCovariance);
		
		publish(INITIAL_POSE_TOPIC, call);
		seq++;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })	
	public void cancelMoveTo() {
		
		Map stamp = new LinkedTreeMap();
		stamp.put("secs", 0);
		stamp.put("nsecs", 0);
		
		Map msg = new LinkedTreeMap();
		msg.put("stamp", stamp);
		msg.put("id", "");
		
		publish(CANCEL_GOAL_TOPIC, msg);
	}
	
	private String getTimeStamp() {
		int nano = 0;
		long sec = System.currentTimeMillis() / 1000L;

		int s = (int) sec;
		return "" + s + nano;
	}
	
	public UnnamedLocation getLocation(){
		return null;
	}
	
	public Location getSimpleRosLocation() {		
		return null;
	}
	
	public Location getSimpleBeSpoonLocation() {
		return null;
	}
}

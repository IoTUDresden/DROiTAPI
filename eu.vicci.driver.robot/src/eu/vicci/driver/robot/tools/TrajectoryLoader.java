package eu.vicci.driver.robot.tools;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.vicci.driver.robot.util.RobotJavaToRosbridge;
import eu.vicci.driver.robot.util.rosmsg.OrientationMsg;
import eu.vicci.driver.robot.util.rosmsg.PathMsg;
import eu.vicci.driver.robot.util.rosmsg.PoseMsg;
import eu.vicci.driver.robot.util.rosmsg.PoseMsgStamped;
import eu.vicci.driver.robot.util.rosmsg.PositionMsg;
import eu.vicci.driver.robot.util.rosmsg.RosMsg;


/**
 * For downloading trajectory from a ROS-robot using Rosbridge.
 * After executing moveTo, using this class, you will get a List of Trajectory Points every 5s
 * Example use:
 * 
 * String robotIP = "192.168.1.6";
 * TrajectoryLoader loader = new TrajectoryLoader(robotIP);
 *		
 * loader.setTrajectoryListener(new TrajectoryListener() {
 *			@Override
 *			public void onTrajectoryMessageRecieved(List<PositionMsg> path) {				
 *				doStuffWithTrajectory(path);
 *			}
 * });
 * loader.connect();
 * 
 */
public class TrajectoryLoader {

	private static final String TRAJECTORY_TOPIC = "/move_base/TrajectoryPlannerROS/global_plan";
	private static final String ODOM_TO_MAP_TRANSFORM_TOPIC = "/tf_odom_to_map_transform";
	private static final int SUBSCRIBE_FREQUENCY = 5000;
	
	private String host;
	private int port;
	private RobotJavaToRosbridge bridge;
	private Thread bridgeThread;
	private TrajectoryListener listener;
	
	public TrajectoryLoader(String host) {
		this.host = host;
		this.port = RobotJavaToRosbridge.DEFAULT_PORT;
	}
	
	public TrajectoryLoader(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	private boolean isTransformReceived = false;
	private TMatrix transformMatrix;
	
	public boolean connect(){
		bridge = new RobotJavaToRosbridge(host,port){

			@SuppressWarnings("unchecked")
			@Override
			public void onMessage(String message) {
				Gson gson = new Gson();
				RosMsg<Object> rosMsg = new RosMsg<Object>();
				rosMsg = gson.fromJson(message, rosMsg.getClass());
				String topic = rosMsg.getTopic();
				
				if(topic.equals(ODOM_TO_MAP_TRANSFORM_TOPIC)){						
					Type msgType = new TypeToken<RosMsg<PoseMsg>>(){}.getType();
					PoseMsg msg = (PoseMsg) ((RosMsg<PoseMsg>)gson.fromJson(message, msgType)).getMsg();
					transformMatrix = new TMatrix(msg);
					if(!isTransformReceived){
						subscribe(TRAJECTORY_TOPIC, SUBSCRIBE_FREQUENCY);
						isTransformReceived = true;
					}
				} else if(topic.equals(TRAJECTORY_TOPIC)){
					Type msgType = new TypeToken<RosMsg<PathMsg>>(){}.getType();
					PathMsg msg = (PathMsg) ((RosMsg<PathMsg>)gson.fromJson(message, msgType)).getMsg();
					if(listener!=null) {
						try {
							listener.onTrajectoryMessageRecieved(transformMatrix.transformPathMsg(msg));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						
					}
				}
			}

			@Override
			public void onOpen(ServerHandshake sh) {
				subscribe(ODOM_TO_MAP_TRANSFORM_TOPIC, SUBSCRIBE_FREQUENCY);
			}
			
		};
		bridgeThread = new Thread(bridge);
		bridgeThread.start();
		while(!bridge.isConnected()){
			Thread.yield();
		}
		return true;		
	}
	
	public void setTrajectoryListener(TrajectoryListener l){
		this.listener = l;
	}
	
	
	// Transform Matrix
	private class TMatrix{
		
		private double[][] matrix;;
		
		public TMatrix(PoseMsg pose){
			PositionMsg pos = pose.getPosition();
			OrientationMsg ori = pose.getOrientation();
			double x = pos.getX();
			double y = pos.getY();
			double z = pos.getZ();
			
			double qw = ori.getW();
			double qx = ori.getX();
			double qy = ori.getY();
			double qz = ori.getZ();
			
			// Conversion Quaternion to Rotation Matrix
			matrix = new double[4][4];
			matrix[0][0] = 1 - 2*qy*qy - 2*qz*qz;
			matrix[0][1] = 2*qx*qy - 2*qz*qw;
			matrix[0][2] = 2*qx*qz + 2*qy*qw;
			matrix[0][3] = x;
			matrix[1][0] = 2*qx*qy + 2*qz*qw;
			matrix[1][1] = 1 - 2*qx*qx - 2*qz*qz;
			matrix[1][2] = 2*qy*qz - 2*qx*qw;
			matrix[1][3] = y;
			matrix[2][0] = 2*qx*qz - 2*qy*qw;
			matrix[2][1] = 2*qy*qz + 2*qx*qw;
			matrix[2][2] = 1 - 2*qx*qx - 2*qy*qy;
			matrix[2][3] = z;
			matrix[3][0] = 0;
			matrix[3][1] = 0;
			matrix[3][2] = 0;
			matrix[3][3] = 1;
		}
				
		public PositionMsg multiply(PositionMsg pos){
			double[] result = new double[4];
			double[] odomPos = {pos.getX(),pos.getY(),pos.getZ(),1};
			for(int row=0 ; row < 4; row++) {
				for(int col=0; col < 4; col++){
					result[row] += matrix[row][col]*odomPos[col]; 	
				}
			}
			PositionMsg mapPos = new PositionMsg();
			mapPos.setX(result[0]);
			mapPos.setY(result[1]);
			mapPos.setZ(result[2]);
			return mapPos;
		}
		
		public List<PositionMsg> transformPathMsg(PathMsg msg){
			List<PositionMsg> pathInMapFrame = new ArrayList<PositionMsg>();
			for(PoseMsgStamped poseStamped: msg.getPoses()){
				PoseMsg pose = poseStamped.getPose();
				pathInMapFrame.add(multiply(pose.getPosition()));
			}
			return pathInMapFrame;
		}
		
	}
	
}

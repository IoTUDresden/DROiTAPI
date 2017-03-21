package eu.vicci.driver.robot.tools;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import eu.vicci.driver.robot.Robot;
import eu.vicci.driver.robot.exception.TimeoutException;
import eu.vicci.driver.robot.exception.UnknownRobotException;
import eu.vicci.driver.robot.util.RobotJavaToRosbridge;
import eu.vicci.driver.robot.util.RosServiceCaller;
import eu.vicci.driver.robot.util.rosmsg.RobotInfoMsg;
import eu.vicci.driver.robot.util.rosmsg.RobotInfoResponseMsg;
import eu.vicci.driver.robot.util.rosmsg.ServiceResponseMsg;

/**	
 *
 *	Example:
 *
 *	RobotServer rs = new RobotServer("roscore-seus.local");
 *	try {
 *		List<Robot> robots = rs.getRobotsFromServer();
 *		for (Robot r : robots){
 *			System.out.println(r.getName());
 *			r.connect();
 *			r.move(new Speed(SpeedLevel.MEDIUM, false), 0);
 *			r.disconnect();
 *		}
 *	} catch (NotConnectedException e) {
 *		e.printStackTrace();
 *	} catch (TimeoutException e) {
 *		e.printStackTrace();
 *	}
 *
 *	Output:
 *		turtlebot-Satellite-R630
 *		ubuntu
 *
 */
public class RobotServer {
	private static final Type msgType = new TypeToken<ServiceResponseMsg<RobotInfoResponseMsg>>(){}.getType();
	private static final String SERVICE_NAME = "/get_robots";
	
	private String host;
	private int port;
	
	public RobotServer(String host) {
		this.host = host;
		this.port = RobotJavaToRosbridge.DEFAULT_PORT;
	}
	
	public RobotServer(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public List<Robot> getRobotsFromServer() throws TimeoutException {
		List<Robot> robotList = new LinkedList<Robot>();
		RobotInfoResponseMsg info = loadRobots();	
		List<RobotInfoMsg> robotInfoList = info.getRobotInfo().getRobots();
		for(RobotInfoMsg robotInfo: robotInfoList){
			Robot robot;
			try {
				robot = new Robot(robotInfo.getHostname(),robotInfo.getIp());
				robotList.add(robot);
			} catch (UnknownRobotException e) {
				e.printStackTrace();
			}
		}
		return robotList;
	}
	
	@SuppressWarnings("unchecked")
	private RobotInfoResponseMsg loadRobots() throws TimeoutException{
		RosServiceCaller caller = new RosServiceCaller(host, port, msgType);
		ServiceResponseMsg<RobotInfoResponseMsg> msg = (ServiceResponseMsg<RobotInfoResponseMsg>) caller.call(SERVICE_NAME);
		return msg.getValues();
	}
}

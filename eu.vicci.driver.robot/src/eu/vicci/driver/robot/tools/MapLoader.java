package eu.vicci.driver.robot.tools;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

import eu.vicci.driver.robot.exception.TimeoutException;
import eu.vicci.driver.robot.util.RobotJavaToRosbridge;
import eu.vicci.driver.robot.util.RosServiceCaller;
import eu.vicci.driver.robot.util.rosmsg.MapMsg;
import eu.vicci.driver.robot.util.rosmsg.ServiceResponseMsg;



/**
 * MapLoader for downloading map from ROS using Rosbridge.
 * 
 * Example use:
 * 
 * String mapServerIP = "192.168.1.1";
 * MapLoader loader = new MapLoader(mapServerIP);
 * MapMsg mapMsg = loader.load();	// blocking
 * OccupancyGridMsg map = mapMsg.getMap();
 * 
 * int height = map.getInfo().getHeight();
 * int width = map.getInfo().getWidth();
 * double resolution = map.getInfo().getResolution();
 * List<Integer> pixels = mapMsg.getMap().getData();
 *  
 */
public class MapLoader {
	
	private static final Type msgType = new TypeToken<ServiceResponseMsg<MapMsg>>(){}.getType();
	private static final String SERVICE_NAME = "/static_map";
	
	private String host;
	private int port;
	
	public MapLoader(String host) {
		this.host = host;
		this.port = RobotJavaToRosbridge.DEFAULT_PORT;
	}
	
	public MapLoader(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@SuppressWarnings("unchecked")
	public MapMsg loadMap() throws TimeoutException{
		RosServiceCaller caller = new RosServiceCaller(host, port, msgType);
		ServiceResponseMsg<MapMsg> msg = (ServiceResponseMsg<MapMsg>) caller.call(SERVICE_NAME);
		return msg.getValues();
	}
	
}

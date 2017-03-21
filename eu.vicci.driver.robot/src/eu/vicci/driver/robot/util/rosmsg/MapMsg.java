package eu.vicci.driver.robot.util.rosmsg;

public class MapMsg {

	public OccupancyGridMsg map;

	public MapMsg() {
	}

	public OccupancyGridMsg getMap() {
		return map;
	}

	public void setMap(OccupancyGridMsg map) {
		this.map = map;
	}
	
}

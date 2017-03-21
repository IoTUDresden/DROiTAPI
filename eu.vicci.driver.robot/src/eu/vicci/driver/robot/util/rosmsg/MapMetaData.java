package eu.vicci.driver.robot.util.rosmsg;

public class MapMetaData {
	
	private StampMsg map_load_time;
	private double resolution;
	private int width;
	private int height;
	private PoseMsg origin;
	
	public MapMetaData(){
	}

	public StampMsg getMap_load_time() {
		return map_load_time;
	}

	public void setMap_load_time(StampMsg map_load_time) {
		this.map_load_time = map_load_time;
	}

	public double getResolution() {
		return resolution;
	}

	public void setResolution(double resolution) {
		this.resolution = resolution;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public PoseMsg getOrigin() {
		return origin;
	}

	public void setOrigin(PoseMsg origin) {
		this.origin = origin;
	}
	
}

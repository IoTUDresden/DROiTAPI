package eu.vicci.driver.robot.util.rosmsg;

import java.util.ArrayList;
import java.util.List;

// Map Massage
public class OccupancyGridMsg {
	
	private HeaderMsg header;
	private MapMetaData info;
	List<Integer> data = new ArrayList<Integer>();

	private OccupancyGridMsg(){
	}

	public HeaderMsg getHeader() {
		return header;
	}

	public void setHeader(HeaderMsg header) {
		this.header = header;
	}

	public MapMetaData getInfo() {
		return info;
	}

	public void setInfo(MapMetaData info) {
		this.info = info;
	}

	public List<Integer> getData() {
		return data;
	}

	public void setData(List<Integer> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "OccupancyGridMsg [header=" + header + ", info=" + info
				+ ", data= [" + "]";
	}
		
}

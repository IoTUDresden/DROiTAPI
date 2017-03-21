package eu.vicci.driver.robot.location;

import java.io.Serializable;

public class NamedLocationDatabaseWrapper implements Serializable{

	private static final long serialVersionUID = -5531807685395018627L;
	
	private String name;
	private String description;
	
	private String mapID="";

	public NamedLocationDatabaseWrapper(){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMapID() {
		return mapID;
	}

	public void setMapID(String mapID) {
		this.mapID = mapID;
	}

}

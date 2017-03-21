package eu.vicci.driver.robot.location.serialization;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import eu.vicci.driver.robot.location.NamedLocation;

public abstract class LocationManagerSerializer {
	
	private Map<String, NamedLocationPacker> locationPackers;
	private static final String separator = ", ";
	
	public LocationManagerSerializer() {
		locationPackers = new HashMap<String, NamedLocationPacker>();
	}
	
	public void registerLocationPacker(Class<? extends NamedLocation> c, NamedLocationPacker locationPacker) {
		String className = c.getCanonicalName();
		locationPackers.put(className, locationPacker);
	}
	
	protected NamedLocationPacker getLocationPacker(NamedLocation location) {
		String className = location.getClass().getCanonicalName();
		return getLocationPacker(className);
	}
	
	protected NamedLocationPacker getLocationPacker(String className) {
		
		try {
			//Preload the class to ensure that its static initializer is
			//invoked where the respective LocationPacker is registered
			//with the LocationManager.
			Class.forName(className);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		NamedLocationPacker locationPacker = locationPackers.get(className);
		
		if (locationPacker == null) {
			throw new InvalidParameterException("No " + NamedLocationPacker.class.getSimpleName() + " registered for \"" + className + "\".");
		}
		
		return locationPacker;
	}
	
	protected String serializeLocation(NamedLocation location) {
		List<String> wrappedAttributes = new ArrayList<String>();
		
		NamedLocationPacker locationPacker = getLocationPacker(location);
		
		locationPacker.packAttributes(location, wrappedAttributes);
		
		String serialized = location.getClass().getCanonicalName();
		
		for (String wrappedAttribute : wrappedAttributes) {
			serialized += separator + wrappedAttribute;
		}
		
		return serialized;
	}
	
	protected NamedLocation deserializeLocation(String serializedLocation) {
		String[] rawWrappedAttributes = serializedLocation.split(Pattern.quote(separator));
		
		List<String> wrappedAttributes = new ArrayList<String>(Arrays.asList(rawWrappedAttributes));
		
		//First entry is the class name - strip that.
		String className = wrappedAttributes.remove(0);
		
		NamedLocationPacker locationPacker = getLocationPacker(className);
		
		if (locationPacker == null) {
			return null;
		}
		
		return locationPacker.unpackAttributes(wrappedAttributes);
	}
	
	public abstract List<NamedLocation> load();
	public abstract void save(List<NamedLocation> locations);
}

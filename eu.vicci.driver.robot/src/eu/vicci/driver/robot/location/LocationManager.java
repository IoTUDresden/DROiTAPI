package eu.vicci.driver.robot.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.vicci.driver.robot.location.serialization.LocationManagerDatabaseSerializer;
import eu.vicci.driver.robot.location.serialization.LocationManagerFileSerializer;
import eu.vicci.driver.robot.location.serialization.LocationManagerSerializer;
import eu.vicci.driver.robot.location.serialization.NamedLocationPacker;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * Singleton class to manage all locations.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class LocationManager {
	private static final Position neglectedSearchPosition = null;
	private static final double neglectedSearchRadius = -1.0;
	
	private static final LocationManagerSerializer serializer = new LocationManagerDatabaseSerializer();
	private static LocationManager instance = null;

	private List<NamedLocation> locations;

	
	private LocationManager() {
		//Private for singleton
	}
	
	protected static LocationManager getInstance() {
		if (instance == null) {
			instance = new LocationManager();
		}

		return instance;
	}
	
	public static void registerLocationPacker(Class<? extends NamedLocation> c, NamedLocationPacker locationPacker) {
		serializer.registerLocationPacker(c, locationPacker);
	}
	
	// Call this before any public read/write access to ensure
	// that the locations are loaded on demand and have a chance to
	// use custom location packers.
	private static void ensureLoaded() {
		LocationManager instance = getInstance();
		
		if (instance.locations == null) {
			instance.locations = serializer.load();
		}
	}
	
	// This will probably vanish once a server is in place.
	public static void save() {
		serializer.save(getInstance().locations);
	}
	

	public static void addLocation(NamedLocation namedLocation) {
		ensureLoaded();
		
		String name = namedLocation.getName();
		
		//Overwrite existing locations
		doRemoveLocation(name);
		
		getInstance().locations.add(namedLocation);
		save();
	}
	
	public static boolean removeLocation(String name) {
		ensureLoaded();
		
		if (doRemoveLocation(name)) {
			save();
			return true;
		}
		
		return false;
	}
	
	private static boolean doRemoveLocation(String name){
		for (NamedLocation location : getInstance().locations) {
			if (location.getName().equals(name)) {
				getInstance().locations.remove(location);
				return true;
			}
		}

		return false;
	}
	
	public static NamedLocation getLocation(String searchLocationName) {
		ensureLoaded();
		
		NamedLocation.checkNamedLocationName(searchLocationName);

		for (NamedLocation location : getInstance().locations) {
			String locationName = location.getName();

			if (searchLocationName.equals(locationName)) {
				return location;
			}
		}
		
		return null;
	}

	public static List<NamedLocation> getLocations() {
		return getLocations(NamedLocation.class, neglectedSearchPosition, neglectedSearchRadius);
	}

	public static <T extends NamedLocation> List<T> getLocations(Class<T> locationType) {
		return getLocations(locationType, neglectedSearchPosition, neglectedSearchRadius);
	}
	
	public static List<NamedLocation> getLocations(Position searchPosition, double searchRadius) {
		return getLocations(NamedLocation.class, searchPosition, searchRadius);
	}

	public static <T extends NamedLocation> List<T> getLocations(Class<T> locationType, Position searchPosition, double searchRadius) {
		ensureLoaded();
		
		List<T> matchingLocations = new ArrayList<T>();
		final Map<Location, Double> locationToDistanceMapping = new HashMap<Location, Double>();

		for (Location location : getInstance().locations) {
			if (!(locationType.isAssignableFrom(location.getClass()))) {
				continue;
			}

			if (searchPosition != neglectedSearchPosition) {
				if (searchRadius == neglectedSearchRadius || searchRadius <= 0.0) {
					continue;
				}

				Position position = location.getPosition();
				double distanceToSearchPosition = position.calculateDistanceTo(searchPosition);

				locationToDistanceMapping.put(location, distanceToSearchPosition);

				if (distanceToSearchPosition > searchRadius) {
					// Location is too far away
					continue;
				}
			}

			try {
				@SuppressWarnings("unchecked")
				T matchingLocation = (T) location;
				matchingLocations.add(matchingLocation);
			} catch (ClassCastException e) {
				// Should not happen as we checked the type before cast.
			}
		}

		if (searchPosition != neglectedSearchPosition) {
			// Sort by distance to search position if search position was given.
			Collections.sort(matchingLocations, new Comparator<Location>() {
				@Override
				public int compare(Location location1, Location location2) {
					double distanceToSearchPosition1 = locationToDistanceMapping.get(location1);
					double distanceToSearchPosition2 = locationToDistanceMapping.get(location2);

					return Double.compare(distanceToSearchPosition1, distanceToSearchPosition2);
				}
			});
		}

		return matchingLocations;
	}
	
	
	/**
	 * Deprecated as of 13.12.2013.
	 * 
	 * @deprecated use <code>Site site = new</code> {@link Site}(), {@link addLocation}(site) instead.   
	 */
	@Deprecated
	public static Site createSite(String name, Position position, Orientation orientation) {
		Site site = new Site(name, position, orientation);
		addLocation(site);
		return site;
	}
	
	/**
	 * Deprecated as of 16.12.2013 as LocationManager should not need to be aware of concrete subclasses of Location.   
	 * 
	 * @deprecated use {@link getLocations(Site.class)} instead.
	 */
	@Deprecated
	public static List<Site> getSites() {
		ensureLoaded();
		
		return getLocations(Site.class, neglectedSearchPosition, neglectedSearchRadius);
	}

	/**
	 * Deprecated as of 16.12.2013 as LocationManager should not need to be aware of concrete subclasses of Location.   
	 * 
	 * @deprecated use {@link getLocations(Site.class, searchPosition, searchRadius)} instead.  
	 */
	@Deprecated
	public static List<Site> getSites(Position searchPosition, double searchRadius) {
		ensureLoaded();
		
		return getLocations(Site.class, searchPosition, searchRadius);
	}

}

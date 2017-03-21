package eu.vicci.driver.robot.location;

import java.util.List;

import eu.vicci.driver.robot.location.serialization.NamedLocationPacker;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

/**
 * A named location that has some significance. Used, e.g.,
 * to store popular locations such as a cabinet or trash bin.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class Site extends NamedLocation {
	private String description;
	
	static {
		//Register a packer to save/load this type of location
		LocationManager.registerLocationPacker(Site.class, new NamedLocationPacker() {
			@Override
			public void packAttributes(NamedLocation namedLocation, List<String> packedAttributes) {
				super.packAttributes(namedLocation, packedAttributes);
				
				Site site = (Site) namedLocation;
				
				String description = encodeString(site.getDescription());
				packedAttributes.add(description);
			}
			
			@Override
			protected Site unpackAttributes(String name, Position position, Orientation orientation, List<String> wrappedAttributes) {
				Site site = new Site(name, position, orientation);
				
				final int n = wrappedAttributes.size();
				
				if (n > 8) {
					String description = decodeString(wrappedAttributes.get(8));
					site.setDescription(description);
				}
				
				return site;
			}
		});
	}
	
	public Site(String name, Position position, Orientation orientation) {
		this(name, position, orientation, "");
	}
	
	public Site(String name, Position position, Orientation orientation, String description) {
		super(name, position, orientation);
		
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}

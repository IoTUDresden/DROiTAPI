package eu.vicci.driver.robot.location.serialization;

import java.util.List;

import eu.vicci.driver.robot.location.NamedLocation;
import eu.vicci.driver.robot.util.Orientation;
import eu.vicci.driver.robot.util.Position;

public abstract class NamedLocationPacker {
	public void packAttributes(NamedLocation namedLocation, List<String> packedAttributes) {
		String name = namedLocation.getName();
				
		Position position = namedLocation.getPosition();
		Orientation orientation = namedLocation.getOrientation();
		
		packedAttributes.add(encodeString(name));
		
		packedAttributes.add(encodeDouble(position.getX()));
		packedAttributes.add(encodeDouble(position.getY()));
		packedAttributes.add(encodeDouble(position.getZ()));
		
		packedAttributes.add(encodeDouble(orientation.getO_X()));
		packedAttributes.add(encodeDouble(orientation.getO_Y()));
		packedAttributes.add(encodeDouble(orientation.getO_Z()));
		packedAttributes.add(encodeDouble(orientation.getO_W()));
	}
	
	public NamedLocation unpackAttributes(List<String> packedAttributes) {
		String rawName = packedAttributes.get(0);
		String name = decodeString(rawName);
		
		String rawX = packedAttributes.get(1);
		String rawY = packedAttributes.get(2);
		String rawZ = packedAttributes.get(3);
		
		String rawO_X = packedAttributes.get(4);
		String rawO_Y = packedAttributes.get(5);
		String rawO_Z = packedAttributes.get(6);
		String rawO_W = packedAttributes.get(7);
		
		
		double x = decodeDouble(rawX);
		double y = decodeDouble(rawY);
		double z = decodeDouble(rawZ);
		
		Position position = new Position(x, y, z);
		
		double o_x = decodeDouble(rawO_X);
		double o_y = decodeDouble(rawO_Y);
		double o_z = decodeDouble(rawO_Z);
		double o_w = decodeDouble(rawO_W);
		
		Orientation orientation = new Orientation(o_x, o_y, o_z, o_w);
		
		return unpackAttributes(name, position, orientation, packedAttributes);
	}
	
	protected abstract NamedLocation unpackAttributes(String name, Position position, Orientation orientation, List<String> wrappedAttributes);
	
	protected static String encodeDouble(Double value) {
		return Double.toString(value);
	}
	
	protected static Double decodeDouble(String encodedValue) {
		return Double.parseDouble(encodedValue);
	}
	
	protected static String encodeString(String value) {
		//Encode commas as they are used as separators
		String encodedValue = value.replaceAll(",", "&#44;");
		
		return "\"" + encodedValue + "\"";
	}
	
	protected static String decodeString(String encodedValue) {
		if (encodedValue == null) {
			return encodedValue;
		}
		
		String decodedValue = encodedValue;
		
		if (decodedValue.startsWith("\"") && decodedValue.endsWith("\"")) {
			decodedValue = decodedValue.substring(1, encodedValue.length() - 1);
		}
		
		//Decode previously encoded commas
		decodedValue = decodedValue.replaceAll("&#44;", ",");
		
		return decodedValue;
	}
}

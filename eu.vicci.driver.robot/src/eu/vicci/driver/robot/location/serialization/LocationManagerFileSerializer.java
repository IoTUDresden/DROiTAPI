package eu.vicci.driver.robot.location.serialization;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import eu.vicci.driver.robot.location.NamedLocation;

/**
 * This is a low performance implementation, which will be replaced later on.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class LocationManagerFileSerializer extends LocationManagerSerializer {
	private static final String locationFileName = "Locations.txt";
	
	@Override
	public List<NamedLocation> load() {
		List<NamedLocation> list = new ArrayList<NamedLocation>();
		try {
			FileInputStream fileInputStream = new FileInputStream(locationFileName);
			DataInputStream dataInputStream = new DataInputStream(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));

			String line;
			
			while ((line = bufferedReader.readLine()) != null) {
				NamedLocation location = deserializeLocation(line);
				
				list.add(location);
			}
			
			dataInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	@Override
	public void save(List<NamedLocation> locations) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(locationFileName, "UTF-8");
			
			for (NamedLocation location : locations) {
				String outputLine = serializeLocation(location);
				writer.println(outputLine);
			}
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}

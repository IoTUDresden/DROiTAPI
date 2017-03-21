package eu.vicci.driver.robot;

/**
 * Common interface for all entities carrying a name.
 *
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public interface NamedEntity {
	public static final String DEFAULT_NAME = "";
	
	public String getName();
}

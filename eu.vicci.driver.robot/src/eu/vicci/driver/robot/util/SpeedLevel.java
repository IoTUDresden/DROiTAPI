package eu.vicci.driver.robot.util;

/**
 * Enumeration of literal values for various speed levels.
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public enum SpeedLevel {
	FASTEST(1.0), FAST(0.75), MEDIUM(0.5), SLOW(0.25), SLOWEST(0.25);
	
	private double percentualSpeed;
	
	private SpeedLevel(double percentualSpeed) {
		this.percentualSpeed = percentualSpeed;
	}
	
	public double getPercentualSpeed() {
		return percentualSpeed;
	}
}

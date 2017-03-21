package eu.vicci.driver.robot.util;

/**
 * Representation of a percentual speed with a direction (forward/backward, left/right etc.).
 * 
 * @author Christoph Seidl (Christoph.Seidl@tu-dresden.de)
 */
public class Speed {
	private double percentualSpeed;
	private boolean backward;
	
	public Speed(double relativeSpeed) {
		this(relativeSpeed, false);
	}
	
	public Speed(SpeedLevel speedLevel) {
		this(speedLevel.getPercentualSpeed(), false);
	}
	
	public Speed(double percentualSpeed, boolean backward) {
		this.backward = backward;
		setPercentualSpeed(percentualSpeed);
	}
	
	public Speed(SpeedLevel speedLevel, boolean backward) {
		this(speedLevel.getPercentualSpeed(), backward);
	}

	public double getPercentualSpeed() {
		return percentualSpeed;
	}

	public void setPercentualSpeed(double percentualSpeed) {
		if (percentualSpeed > 1.0) {
			percentualSpeed = 1.0;
		}
		
		if (percentualSpeed < 0.0) {
			percentualSpeed = 0.0;
		}
		
		this.percentualSpeed = percentualSpeed;
	}

	public boolean getBackward() {
		return backward;
	}

	public void setBackward(boolean backward) {
		this.backward = backward;
	}
	
	public double getRawSpeed(double maximumRawSpeed) {
		return (backward ? -1 : 1) * percentualSpeed * maximumRawSpeed;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Speed)) {
			return false;
		}
		
		Speed speed2 = (Speed) other;
		
		return percentualSpeed == speed2.percentualSpeed && backward == speed2.backward;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " (" + (percentualSpeed * 100) + "% " + (backward ? "backward" : "forward") + ")";
	}
}

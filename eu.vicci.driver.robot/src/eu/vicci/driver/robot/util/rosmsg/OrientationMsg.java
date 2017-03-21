package eu.vicci.driver.robot.util.rosmsg;

public class OrientationMsg {

	private double x,y,z,w;
	
	public OrientationMsg(){
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	@Override
	public String toString() {
		return "OrientationMsg [x=" + x + ", y=" + y + ", z=" + z + ", w=" + w
				+ "]";
	}
	
}

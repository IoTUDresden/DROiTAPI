package eu.vicci.driver.robot.util.rosmsg;

public class ServiceResponseMsg<T> {

	String op;
	String id;
	String service;
	T values;

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
	
	public T getValues() {
		return values;
	}

	public void setValues(T values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "ServiceResponseMsg [op=" + op + ", id=" + id + ", service="
				+ service + ", values=" + values + "]";
	}

}

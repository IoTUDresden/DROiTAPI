package eu.vicci.driver.robot.util.rosmsg;

public class RosMsg<T>{
	private String topic;
	private T msg;
	private String op;
	
	public RosMsg(){
	}

	public String getTopic() {
		return topic;
	}

	public T getMsg() {
		return msg;
	}

	public String getOp() {
		return op;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setMsg(T msg) {
		this.msg = msg;
	}

	public void setOp(String op) {
		this.op = op;
	}
	
	
}

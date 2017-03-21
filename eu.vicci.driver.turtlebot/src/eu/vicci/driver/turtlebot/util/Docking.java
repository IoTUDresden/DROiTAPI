package eu.vicci.driver.turtlebot.util;

public class Docking {

	private static final float LINEAR_NEAR_SPEED = 0.10f;
	private static final float LINEAR_FAR_SPEED = 0.15f;
	private static final float ANGULAR_STEER_SPEED = 0.4f;
	private static final float ANGULAR_ROTATE_SPEED = 0.7f;
	private static final long TIMEOUT = 10000;
	private static final int HISTORY_SIZE = 4;
	private static final int MAX_NUMBER_OF_DOCKING_TRYS = 3;
	
	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	
	private static final int c = 0, l = 1, r = 2, ll = 3, rr = 4, no = 5, timeout = 6;
	
	private char[] docking_c = null;
	private TurtleBotJavaToRosbridge ws = null;
	
	int fsm[][] = {{1,2,3,4,5,6,0},
					{1,0,0,0,0,0,0},
					{0,2,0,0,0,0,0},
					{0,0,3,0,0,0,0},
					{0,0,0,4,0,7,0},
					{0,0,0,0,5,8,0},
					{0,0,0,0,0,6,9},
					{0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0},
					{0,0,0,0,0,9,6},
					{0,0,0,0,0,0,0}
					};
	
	private int main_z = 0;
	private long time, timer;
	
	private float[] linear_avrg = new float[HISTORY_SIZE];
	private float[] angular_avrg = new float[HISTORY_SIZE];
	private int array_ind = 0;
	
	private int try_count = 0;
	private int no_contact_count = 0;
	private int last_dir = LEFT;
	
	public Docking(TurtleBotJavaToRosbridge ws){
		this.ws = ws;
	}

	
	private long time2=0;
	private int back_count = 0;
	private int state = 0;
	//							0			1				2				3
	// structure of string s: charging, bumper left,	bumper center,	bumper right,	
	public void doDocking(String s){
		docking_c = s.toCharArray();
		
		boolean bumper = docking_c[1]=='1'||docking_c[2]=='1'||docking_c[3]=='1';
		boolean contact = docking_c[0]=='1';

		switch(state){
		case 0:
			if(bumper||contact){
				state = 1;
			} else {
				doDock();
			}
			break;
		case 1:
			if(time2 == 0) time2 = System.currentTimeMillis();
			if((System.currentTimeMillis()- time2) > 3000){
				state = 2;
				time2 = 0;
			}
			break;
		case 2:
			if(contact){
				ws.setLED1(0);
				ws.setLED2(0);
				ws.stopDocking();
			} else {
				if(try_count > MAX_NUMBER_OF_DOCKING_TRYS){
					ws.setLED1(0);
					ws.setLED2(0);
					ws.stopDocking();
				} else {
					for(int i = 0; i<angular_avrg.length; i++) angular_avrg[i] = 0;
					state = 3;
				}
			}
			break;
		case 3:
			if(time2 == 0) time2 = System.currentTimeMillis();
			if((System.currentTimeMillis()- time2) > 500){
				if(no_contact_count< 3){
					state = 5;
					time2 = 0;
				} else {
					state = 4;
					time2 = 0;
				}
			}
			break;
		case 4:
			if(back_count > 14){
				no_contact_count = 0;
				back_count = 0;
				state = 0;
				try_count++;
			} else {
				backward();
				back_count++;
				state = 3;
			}
			break;
		case 5:
			if(no_contact_count==0){
				ws.publishVelocityCommand(0f, -1f);
			} else {
				ws.publishVelocityCommand(0f, 1f);
			}
			no_contact_count++;
			state = 1;
			break;
		}

	}

	private void doDock(){
		boolean left,center,right;
		
		left = docking_c[13]=='1' || docking_c[10]=='1';
		center =docking_c[12]=='1' || docking_c[11]=='1';
		right = docking_c[15]=='1' || docking_c[12]=='1';

		int eingabe = 0;
		if (left&&right&&center){
			ws.setLED1(1);
			ws.setLED2(1);
			eingabe = c;
			time = 0;
		} else 
		if(left&&center){
			ws.setLED1(1);
			ws.setLED2(0);
			eingabe = l;
			time = 0;
		} else if(right&&center) {
			ws.setLED1(0);
			ws.setLED2(1);
			eingabe = r;
			time = 0;
		} else if(center){
			ws.setLED1(1);
			ws.setLED2(1);
			eingabe = c;
			time = 0;
		} else if(left){
			ws.setLED1(2);
			ws.setLED2(0);
			eingabe = ll;
			time = 0;
		} else if(right){
			ws.setLED1(0);
			ws.setLED2(2);
			eingabe = rr;
			time = 0;
		} else {
			eingabe = no;
			if(time == 0){
				time = System.currentTimeMillis();
			}
			timer = System.currentTimeMillis() - time;
			if(timer > TIMEOUT) {
				time = 0;
				eingabe = timeout;
				ws.setLED1(0);
				ws.setLED2(3);
			} else {
				ws.setLED1(3);
				ws.setLED2(0);
			}
		}
		
		switch(main_z) {
		case 0:
			break;
		case 1:
			forward();
			break;
		case 2:
			drive_right();
			last_dir = RIGHT;
			break;
		case 3:
			drive_left();
			last_dir = LEFT;
			break;
		case 4:
			turn_right();
			last_dir = RIGHT;
			break;
		case 5:
			turn_left();
			last_dir = LEFT;
			break;
		case 6:
			if(last_dir==RIGHT){
				rotate_left();
			} else {
				rotate_right();
			}
			break;
		case 7:
			turn_left();
			last_dir = LEFT;
			break;
		case 8:
			turn_right();
			last_dir = RIGHT;
			break;
		case 9:
			if(last_dir!=RIGHT){
				rotate_left();
			} else {
				rotate_right();
			}
			break;
		case 10:
			System.err.println("Docking ERROR!");
			break;
		default:
			break;
		}
		//System.out.print("main_Z[i] = " +main_z + ", e = " +eingabe);
		main_z = fsm[main_z][eingabe];
		//System.out.println(", main_Z[i+1] = "+ main_z);
		//System.out.println("linear: " +average(linear_avrg) + ", angular: "+average(angular_avrg));
	}
	
	private void rotate_left() {
		linear_avrg[array_ind] = 0;
		angular_avrg[array_ind] = ANGULAR_ROTATE_SPEED+1;
		
		ws.publishVelocityCommand(average(linear_avrg), average(angular_avrg));
		
		array_ind = (array_ind+1)%HISTORY_SIZE;
	}

	private void rotate_right() {
		linear_avrg[array_ind] = 0;
		angular_avrg[array_ind] = -(ANGULAR_ROTATE_SPEED+1);

		
		ws.publishVelocityCommand(average(linear_avrg), average(angular_avrg));
		
		array_ind = (array_ind+1)%HISTORY_SIZE;
	}	
	
	private void turn_left() {
		if(isNear()){
			linear_avrg[array_ind] = LINEAR_NEAR_SPEED-0.03f;	
		}else {
			linear_avrg[array_ind] = LINEAR_FAR_SPEED;
		}
		angular_avrg[array_ind] = ANGULAR_ROTATE_SPEED;
		
		ws.publishVelocityCommand(average(linear_avrg), average(angular_avrg));
		
		array_ind = (array_ind+1)%HISTORY_SIZE;
	}

	private void turn_right() {
		if(isNear()){
			linear_avrg[array_ind] = LINEAR_NEAR_SPEED-0.03f;	
		} else {
			linear_avrg[array_ind] = LINEAR_FAR_SPEED;
		}
		angular_avrg[array_ind] = -ANGULAR_ROTATE_SPEED;

		
		ws.publishVelocityCommand(average(linear_avrg), average(angular_avrg));
		
		array_ind = (array_ind+1)%HISTORY_SIZE;
	}

	private void drive_left() {
		if(isNear()){
			linear_avrg[array_ind] = LINEAR_NEAR_SPEED-0.04f;	
		}else {
			linear_avrg[array_ind] = LINEAR_FAR_SPEED-0.06f;
		}
		angular_avrg[array_ind] = ANGULAR_STEER_SPEED;
		
		ws.publishVelocityCommand(average(linear_avrg), average(angular_avrg));
		
		array_ind = (array_ind+1)%HISTORY_SIZE;
	}

	private void drive_right() {
		if(isNear()){
			linear_avrg[array_ind] = LINEAR_NEAR_SPEED-0.03f;	
		}else {
			linear_avrg[array_ind] = LINEAR_FAR_SPEED;
		}
		
		angular_avrg[array_ind] = -ANGULAR_STEER_SPEED;
		
		ws.publishVelocityCommand(average(linear_avrg), average(angular_avrg));
		
		array_ind = (array_ind+1)%HISTORY_SIZE;
	}

	private void forward(){
		if(isNear()){
			linear_avrg[array_ind] = LINEAR_NEAR_SPEED;
			angular_avrg[array_ind] = 0;
		} else {
			linear_avrg[array_ind] = LINEAR_FAR_SPEED;
			angular_avrg[array_ind] = 0;	
		}
		
		ws.publishVelocityCommand(average(linear_avrg), average(angular_avrg));
		
		array_ind = (array_ind+1)%HISTORY_SIZE;
	}
	
	private void backward(){
		linear_avrg[array_ind] = -LINEAR_FAR_SPEED;
		angular_avrg[array_ind] = 0;
		
		ws.publishVelocityCommand(average(linear_avrg), average(angular_avrg));
		
		array_ind = (array_ind+1)%HISTORY_SIZE;
	}
		
	private float average(float[] array){
		float sum = 0;
		for(int i = 0; i< array.length; i++){
			sum += array[i];
		}
		return (sum/array.length);
	}
	
	
	public boolean isNear(){
		return (docking_c[13]=='1')||(docking_c[14]=='1')||(docking_c[15]=='1');
	}

}

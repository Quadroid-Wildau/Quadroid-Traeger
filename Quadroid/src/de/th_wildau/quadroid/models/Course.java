package de.th_wildau.quadroid.models;

/**
 * this model define an course it contains direction and speed
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 12.12.2013, (JDK 7)
 * 
 * */

public class Course {


	
	/**
	 * current speed
	 */
	private float speed;
	
	/**
	 * course at degree, between magnetic 
	 * Nord and aircraft (LOP - Line Of Position)
	 */
	private float angleofnordreference;
	
	
	/**
	 * Getter Function
	 * 
	 * @return current speed
	 * 
	 * */
	public float getSpeed() {
		return speed;
	}

	/**
	 * Setter Method
	 * 
	 * @param speed - set an new speed value
	 * 
	 * */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * Getter Function
	 * 
	 * @return get course at degree, between magnetic 
	 * Nord and aircraft (LOP - Line of Position)
	 * 
	 * */
	public float getAngleReference() {
		return angleofnordreference;
	}

	/**
	 * Setter Method
	 * 
	 * @param angle - set an new course at degree (between magnetic 
	 * Nord and aircraft (LOP - Line of Position))
	 * 
	 * */
	public void setAngleReference(float angle) {
		this.angleofnordreference = angle;
	}
	
	@Override
	public String toString() {
		//print all data
		return "Speed: " + this.speed + 
				"\nAngle: " + this.angleofnordreference;
		
	}
	
}

package de.th_wildau.quadroid.models;

/**
 * this model define the attitude of airplane 
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 12.12.2013, (JDK 7)
 * 
 * 
 * */

public class Attitude 
{
	
	/**
	 * angle of rotation axis z
	 */
	private float yaw;
	

	/**
	 * angle of rotation axis x (main direction)
	 */
	private float roll;
	
	/**
	 * angle of rotation axis y (diagonally to main direction)
	 */
	private float pitch;
	
	
	/**
	 * Getter Function
	 * 
	 * @return get angle of rotation for axis z
	 * 
	 * */
	public float getYaw() {
		return yaw;
	}

	/**
	 * Setter Method
	 * 
	 * @param yaw - set an new angle of rotation for axis z
	 * 
	 * */
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	/**
	 * Getter Function
	 * 
	 * @return get angle of rotation for axis x (at main direction)
	 * 
	 * */
	public float getRoll() {
		return roll;
	}

	/**
	 * Setter Method
	 * 
	 * @param roll - set an new angle of rotation for axis x (at main direction)
	 * 
	 * */
	public void setRoll(float roll) {
		this.roll = roll;
	}

	/**
	 * Getter Function
	 * 
	 * @return get angle of rotation for axis y (diagonally to main direction)
	 * 
	 * */
	public float getPitch() {
		return pitch;
	}

	/**
	 * Setter Method
	 * 
	 * @param pitch - set an new angle of rotation for axis y (diagonally to main direction)
	 * 
	 * */
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
}

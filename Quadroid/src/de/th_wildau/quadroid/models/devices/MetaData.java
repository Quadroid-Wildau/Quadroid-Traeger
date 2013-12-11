package de.th_wildau.quadroid.models.devices;

/**
 * 
 * 
 * @author Stefan ...
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 11.12.2013, (JDK 7)
 * 
 * */

public class MetaData {
	
	/**
	 * save an geoposition
	 */
	private GeoData gdata;
	
	/**
	 * save an time
	 */
	private long time;
	
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
	 * battery power state [0 ... 100] (percent) 
	 */
	private byte batteryState;
	
	/**
	 * current speed
	 */
	private float speed;
	
	/**
	 * course at degree, between magnetic 
	 * Nord and aircraft (LOP - Line Of Position)
	 */
	private float course;
	
	/**
	 * Getter Function
	 * 
	 * @return get an position see {@link GeoData}
	 * 
	 * */
	public GeoData GeoData() {
		return gdata;
	}
	
	/**
	 * Setter Method
	 * 
	 * @param gdata - set an position 
	 * 
	 * */
	public void setGeoData(GeoData gdata) {
		this.gdata = gdata;
	}

	/**
	 * Getter Function
	 * 
	 * @return get an time
	 * 
	 * */
	public long getTime() {
		return time;
	}

	/**
	 * Setter Method
	 * 
	 * @param time - set an new time
	 * 
	 * */
	public void setTime(long time) {
		this.time = time;
	}

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

	/**
	 * Getter Function
	 * 
	 * @return value indicated the power of battery
	 * this value can be between [0 ... 100] (percent)
	 * 
	 * */
	public byte getBatteryState() {
		return batteryState;
	}

	/**
	 * Setter Method
	 * 
	 * @param batteryState - set new battery state,
	 * value can be between [0 ... 100] (percent)
	 * 
	 * */
	public void setBatteryState(byte batteryState) {
		this.batteryState = batteryState;
	}

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
	public float getCourse() {
		return course;
	}

	/**
	 * Setter Method
	 * 
	 * @param course - set an new course at degree (between magnetic 
	 * Nord and aircraft (LOP - Line of Position))
	 * 
	 * */
	public void setCourse(float course) {
		this.course = course;
	}

}
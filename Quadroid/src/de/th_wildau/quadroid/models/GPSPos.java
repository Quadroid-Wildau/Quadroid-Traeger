package de.th_wildau.quadroid.models;

/**
 * Data-Model for Flight-Ctrl GPS data
 * 
 * @author Alex
 * @version 1.0, 11.01.2014, (JDK 7)
 * 
 * */


public class GPSPos 
{	
	/**save longitude in 1E-7 deg*/
	private float longitude;  
	/**save latitude in 1E-7 deg*/
	private float latitude; 
	/**save altitude in mm*/
	private float altitude; 
	/**save validity of data*/
	private short status;
	/**save current time of this position*/
	private long time;
	
	/**
	 * Getter Function
	 * 
	 * @return the longitude value
	 * 
	 * */
	public float getLongitude() {
		return longitude;
	}
	
	/**
	 * Setter Method
	 * 
	 * @param longitude - hand over an new value for longitude
	 * 
	 * */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * Getter Function
	 * 
	 * @return get value for latitude
	 * 
	 * */
	public float getLatitude() {
		return latitude;
	}
	
	/**
	 * Setter Method
	 * 
	 * @param latitude - set an new value for latitude
	 * 
	 * */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Getter Function
	 * 
	 * @return get value for current altitude
	 * 
	 * */
	public float getAltitude() {
		return altitude;
	}
	
	/**
	 * Setter Method
	 * 
	 * @param altitude - hand over an new value for altitude
	 * 
	 * */
	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}
	
	/**
	 * Getter Function
	 * 
	 * @return get current state of GPS
	 * 
	 * */
	public short getStatus() {
		return status;
	}
	
	/**
	 * Setter Method
	 * 
	 * @param status - hand over an new state for GPS
	 * 
	 * */
	public void setStatus(short status) {
		this.status = status;
	}
	
	/**
	 * Getter Function
	 * 
	 * @return current time stamp for this position
	 * 
	 * */
	public long getTime() 
	{
		return time;
	}
	
	/**
	 * Setter Method
	 * 
	 * @param time - hand over an new timestamp for this position
	 * 
	 * */
	public void setTime(long time) 
	{
		this.time = time;
	}
}

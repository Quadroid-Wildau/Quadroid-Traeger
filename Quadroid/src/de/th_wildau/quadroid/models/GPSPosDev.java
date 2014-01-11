package de.th_wildau.quadroid.models;

/**
 * Model for ? TODO: 
 * 
 * @author Alex
 * @version 1.0, 11.01.2014, (JDK 7)
 * 
 * 
 * */

public class GPSPosDev 
{
	/**save value for distance*/
	private short distance;
	/**save value for bearing*/
	private short bearing;
	
	/**
	 * Getter Function
	 * 
	 * @return an distance 
	 * 
	 * */
	public short getDistance() 
	{
		return distance;
	}
	
	/**
	 * Setter Method
	 * 
	 * @param distance - hand over an new distance to set
	 * 
	 * */
	public void setDistance(short distance) 
	{
		this.distance = distance;
	}
	
	/**
	 * Getter Function
	 * 
	 * @return the bearing
	 * 
	 * */
	public short getBearing() 
	{
		return bearing;
	}
	
	/**
	 * Setter Method
	 * 
	 * @param bearing - hand over new bearing value
	 * 
	 * */
	public void setBearing(short bearing) 
	{
		this.bearing = bearing;
	}
}

package de.th_wildau.quadroid.models;

/**
 * this model define an position with latitude, longitude and height
 * 
 * @author Stefan ...
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 11.12.2013, (JDK 7)
 * 
 * */

public class GNSS {

	
	/**save the latitude value*/
	private float latitude;

	/**save the longitude value*/
	private float longitude;
	
	/**save height over null*/
	private float height;

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
	 * @return get value for longitude
	 * 
	 * */
	public float getLongitude() {
		return longitude;
	}
	
	/**
	 * Setter Method
	 * 
	 * @param longitude - set an new value for longitude
	 * 
	 * */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	/**
	 * Getter Function
	 * 
	 * @return get value for height over null
	 * 
	 * */
	public float getHeight() {
		return height;
	}

	/**
	 * Setter Method
	 * 
	 * @param height - set an new value for height over null
	 * 
	 * */
	public void setHeight(float height) {
		this.height = height;
	}

}

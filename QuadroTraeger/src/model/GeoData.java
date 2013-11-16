package model;

public class GeoData {
	
	
	/**
	 * Breitengrad
	 */
	private float latitude;

	
	/**
	 * Laengengrad
	 */
	private float longitude;
	
	/**
	 * Hoehe ueber normal Null
	 */
	private float height;

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
}

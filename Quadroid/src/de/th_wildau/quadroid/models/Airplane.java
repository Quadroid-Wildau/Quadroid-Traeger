package de.th_wildau.quadroid.models;

/**
 * this model define our airplane the quadroid
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 12.12.2013, (JDK 7)
 * 
 * */

public class Airplane {

	
	/**
	 * save an time
	 */
	private long time;
	
	
	/**
	 * battery power state in volt 
	 */
	private short batteryState;
	
	/**
	 * save an geoposition
	 */
	private GNSS gdata;
	
	

	/**
	 * Getter Function
	 * 
	 * @return get an position see {@link GNSS}
	 * 
	 * */
	public GNSS GeoData() {
		return gdata;
	}
	
	/**
	 * Setter Method
	 * 
	 * @param gdata - set an position 
	 * 
	 * */
	public void setGeoData(GNSS gdata) {
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
	 * @return value indicated the power of battery
	 * this value can be between [0 ... 100] (percent)
	 * 
	 * */
	public short getBatteryState() {
		return batteryState;
	}

	/**
	 * Setter Method
	 * 
	 * @param batteryState - set new battery state,
	 * value can be between [0 ... 100] (percent)
	 * 
	 * */
	public void setBatteryState(short batteryState) {
		this.batteryState = batteryState;
	}
	
	
	@Override
	public String toString() {
		//print all data
		String result = "";
		result += "Battery Power: " + this.batteryState + "\nTime: "
				+ this.time;
		if(this.gdata != null)
		result += "\nPosition: " + this.gdata.toString();
		return result;
	}
	
}

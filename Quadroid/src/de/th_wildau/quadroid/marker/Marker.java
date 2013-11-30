package de.th_wildau.quadroid.marker;

/**
 * This enum contains marker for transmissions
 * <ul>
 * <li>Picture bytes</li>
 * <li>GNSS position</li>
 * <li>Gyro data</li>
 * <li>Height data</li>
 * <li>Akku state</li>
 * <li>Time value</li>
 * <li>Speed value</li>
 * <li>Course data</li>
 * <li>CRC data</li>
 * </ul>
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 27.11.2013 (JDK 7) 
 * 
 * */

public enum Marker 
{

	PICTURESTART("||>"), 
	PICTUREEND("<||"),
	GNSSSTART("##>"), 
	GNSSEND("<##"),
	GYROSTART("//>"), 
	GYROEND("<//"),
	HEIGHTSTART("??>"), 
	HEIGHTEND("<??"),
	AKKUSTART("!!>"), 
	AKKUEND("<!!"),
	TIMESTART(";;>"), 
	TIMEEND("<;;"),
	SPEEDSTART("::>"), 
	SPEEDEND("<::"),
	COURSESTART("&&>"), 
	COURSEEND("<&&"),
	CRCSTART("$$>"), 
	CRCEND("<$$");
	
	/**save marker*/
	private String val = null;
	
	/**
	 * invisible constructor 
	 * */
	private Marker(String value)
	{
		this.val = value;
	}

	/**
	 * Getter Function
	 * 
	 * @return marker for images 
	 * */
	public String getMarker()
	{
		return this.val;
	}
}

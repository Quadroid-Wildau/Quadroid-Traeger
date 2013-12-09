package de.th_wildau.quadroid.enums;

/**
 * This enum define the communication with Flight-Ctrl 2.5
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 09.12.2013, (JDK 7)
 * 
 * */

public enum FlightCtrl {

	//TODO: set communication parameter for Flight-Ctrl 2.5
	BAUD(-1), //transmission speed
	PARITY(-1), //parity flag
	DATABITS(-1), //number of databits
	STOPBITS(-1), //stop flag
	PORT(""), //connection port
	DEVICENAME(""); //device name
	
	
	
	
	/**save an parameter*/
	private int value = -1;
	/**parameter for describe device or port*/
	private String name = null;
	
	/**
	 * no public Constructor
	 * @param value hand over an parameter for define the communication with Flight-Ctrl
	 * */
	private FlightCtrl(int value)
	{
		this.value = value;
	}
	
	/**
	 * no public Constructor
	 * @param value hand over an name for communication port of name of device 
	 * */
	private FlightCtrl(String name)
	{
		this.name = name;
	}
	
	/**
	 * Getter Function
	 * 
	 * @return get an parameter for describe communication with Flight-Ctrl
	 * */
	public int getValue()
	{
		return this.value;
	}
	
	/**
	 * Getter Function
	 * 
	 * @return get an port description or name of device
	 * 
	 * */
	public String getName()
	{
		return this.name;
	}
	
}

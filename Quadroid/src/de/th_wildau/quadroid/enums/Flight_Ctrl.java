package de.th_wildau.quadroid.enums;

import purejavacomm.SerialPort;

/**
 * This enum define the communication with Flight-Ctrl 2.5
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 09.12.2013, (JDK 7)
 * 
 * */

public enum Flight_Ctrl {

	//TODO: set communication parameter for Flight-Ctrl 2.5
	BAUD(57600), //transmission speed
	PARITY(SerialPort.PARITY_NONE), //parity flag
	DATABITS(SerialPort.DATABITS_8), //number of databits
	STOPBITS(SerialPort.STOPBITS_1), //stop flag
	PORT("ttyUSB1"), //connection port
	DEVICENAME(""); //device name
	
	
	
	
	/**save an parameter*/
	private int value = -1;
	/**parameter for describe device or port*/
	private String name = null;
	
	/**
	 * no public Constructor
	 * @param value hand over an parameter for define the communication with Flight-Ctrl
	 * */
	private Flight_Ctrl(int value)
	{
		this.value = value;
	}
	
	/**
	 * no public Constructor
	 * @param value hand over an name for communication port of name of device 
	 * */
	private Flight_Ctrl(String name)
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

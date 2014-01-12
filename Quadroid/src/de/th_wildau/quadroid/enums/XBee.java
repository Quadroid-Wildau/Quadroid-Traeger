package de.th_wildau.quadroid.enums;

import purejavacomm.SerialPort;


/**
 * This enum define the communication with XBee
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 09.12.2013, (JDK 7)
 * 
 * */

public enum XBee {

	BAUD(57600), //transmission speed
	PARITY(SerialPort.PARITY_NONE), //parity flag
	DATABITS(SerialPort.DATABITS_8), //number of databits
	STOPBITS(SerialPort.STOPBITS_1), //stop flag
	PORT("ttyUSB0"), //port for connection
	DEVICENAME("xBee5sPro");//device name

	/** save an parameter */
	private int value = -1;
	/** parameter for describe device or port */
	private String name = null;

	/**
	 * no public Constructor
	 * 
	 * @param value
	 *            hand over an parameter for define the communication with XBee
	 * */
	private XBee(int value) {
		this.value = value;
	}

	/**
	 * no public Constructor
	 * 
	 * @param value
	 *            hand over an name for communication port of name of device
	 * */
	private XBee(String name) {
		this.name = name;
	}

	/**
	 * Getter Function
	 * 
	 * @return get an parameter for describe communication with XBee
	 * */
	public int getValue() {
		return this.value;
	}

	/**
	 * Getter Function
	 * 
	 * @return get an port description or name of device
	 * 
	 * */
	public String getName() {
		return this.name;
	}

}

package de.th_wildau.quadroid.interfaces;

/**
 * this interface define an device for connect to serial port
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 05.12.2013, (JDK 7)
 * */

public interface IDevice {

	/**
	 * Getter Function
	 * 
	 * @return connection port for zigbee
	 * */
	public String getPort();

	/**
	 * Getter Function
	 * 
	 * @return get an unique name of device
	 * */
	public String getDeviceName();

	/**
	 * Getter Function
	 * 
	 * @return get bautrate for communication
	 * */
	public int getBaud();

	/**
	 * Getter Function
	 * 
	 * @return get type of parity
	 * */
	public int getParity();

	/**
	 * Getter Function
	 * 
	 * @return get number of databits
	 * 
	 * */
	public int getDatabits();

	/**
	 * Getter Function
	 * 
	 * @return number of stopbits
	 * */
	public int getStopBits();


}

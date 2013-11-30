package de.th_wildau.quadroid.interfaces;

import java.io.InputStream;
import java.io.OutputStream;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import org.slf4j.Logger;

/**
 * This interface realized an information system for serial events 
 * advantage of this interface is, directly access to Input- or OutputStream  
 * for read and write data  
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 17.11.2013 (JDK 7) 
 * 
 * */
public interface ISerialPortListener
{
	/**
	 * this method is called once detect an event of {@link gnu.io.SerialPortEvent} 
	 * prove event with: if(envt.getEventType() == SerialPortEvent.DATA_AVAILABLE) 
	 * All data will be receives and transmit in 
	 * {@link org.apache.commons.codec.binary.Base64InputStream},
	 * {@link org.apache.commons.codec.binary.Base64OutputStream}
	 * 
	 * @param evnt for this see {@link gnu.io.SerialPortEvent}
	 * 
	 * @param istream reference of {@link java.io.InputStream} 
	 * for reading data from serial port
	 * 
	 * @param ostream reference of {@link java.io.OutputStream}
	 * for writing data to serial port
	 * 
	 * @param logger reference of {@link org.slf4j.Logger}
	 * for logging
	 * 
	 * @param port instance of {@link gnu.io.SerialPort} for 
	 * enable oder disable data input
	 *   
	 * 
	 * */
	public void serialEvent(SerialPortEvent evnt, SerialPort port, InputStream istream, OutputStream ostream, Logger logger);
	
}

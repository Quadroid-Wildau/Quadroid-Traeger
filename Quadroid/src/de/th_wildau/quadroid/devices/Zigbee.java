package de.th_wildau.quadroid.devices;
import gnu.io.SerialPort;

/**
 * This enum describe parameters for 
 * communication with zigbee devices
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 16.11.2013 (JDK 7) 
 *
 */

public enum Zigbee 
{
		/**step speed for communication (baudrate)*/
		BAUDRATE(57600), 
		/**type of parity even, ode, none*/
		PARITY(SerialPort.PARITY_NONE),
		/**number of stop bits*/
		STOPBITS(SerialPort.STOPBITS_1),
		/**number of data bits*/
		DATABITS(SerialPort.DATABITS_8),
		/**
		 * in linaro you can find connected devices on <tt>/dev/ttyUSBn</tt> 
		 * this information is needed to communicate with this
		 * and describe the connected port for example <tt>/dev/ttyUSB0</tt>
		 * */
		ZIGBEE_IDETIFIER("/dev/ttyUSB0");
		
		/**save an value to describe the Zigbee communication*/
		private int value = -1;
		/**save the connection port*/
		private String port = null;
		
		/**
		 * Invisible Constructor
		 * 
		 * @param value 
		 * hand over values to describe Zigbee the communication
		 *  
		 * */
		private Zigbee(int value)
		{
			this.value = value;
		}
	
		/**
		 * Invisible Constructor
		 * 
		 * @param port
		 * hand over the port description 
		 * */
		private Zigbee(String port)
		{
			this.port = port;
		}
		
		/**
		 * Getter Function
		 * @return get an value to describe the serial communication
		 * between two zigbee devices
		 * */
		public int getValue()
		{
			return this.value;
		}
		
		/**
		 * Getter Function
		 * @return connection port for zigbee
		 * */
		public String getPort()
		{
			return this.port;
		}
	
}

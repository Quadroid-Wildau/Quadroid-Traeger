package de.th_wildau.quadroid.models.devices;
import de.th_wildau.quadroid.interfaces.IDevice;

/**
 * This Device-Model contains parameter for define an 
 * serial connection
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 16.11.2013 (JDK 7) 
 *
 */

public class XBee implements IDevice
{
		/**step speed for communication (baudrate)*/
		private int baud = 0; 
		/**type of parity even, ode, none*/
		private int parity = 0;
		/**number of stop bits*/
		private int stopbits = 0;
		/**number of data bits*/
		private int databits = 0;
		/**save an unique device name*/
		private String devicename = null;
		/**port for connection*/
		private String port = null;

		
		
		
		/**
		 * @param baud the baud to set
		 */
		public void setBaud(int baud) {
			this.baud = baud;
		}

		/**
		 * @param parity the parity to set
		 */
		public void setParity(int parity) {
			this.parity = parity;
		}

		/**
		 * @param stopbits the stopbits to set
		 */
		public void setStopbits(int stopbits) {
			this.stopbits = stopbits;
		}

		/**
		 * @param databits the databits to set
		 */
		public void setDatabits(int databits) {
			this.databits = databits;
		}

		/**
		 * @param devicename the devicename to set
		 */
		public void setDevicename(String devicename) {
			this.devicename = devicename;
		}

		/**
		 * @param port the port to set
		 */
		public void setPort(String port) {
			this.port = port;
		}

		@Override
		public String getPort() 
		{
			return this.port;
		}

		@Override
		public String getDeviceName() 
		{
			return this.devicename;
		}

		@Override
		public int getBaud() 
		{
			return this.baud;
		}

		@Override
		public int getParity() 
		{
			return this.parity;
		}

		@Override
		public int getDatabits() 
		{
			return this.databits;
		}

		@Override
		public int getStopBits() 
		{
			return this.stopbits;
		}


}

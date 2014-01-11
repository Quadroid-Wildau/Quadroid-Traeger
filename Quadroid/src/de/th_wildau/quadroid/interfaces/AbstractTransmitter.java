package de.th_wildau.quadroid.interfaces;

import org.slf4j.Logger;
import de.th_wildau.quadroid.QuadroidMain;
import de.th_wildau.quadroid.connection.Connect;

/**
 * this class abstract the transmission part and must be overwrite from xbee and flight-ctrl transmission handler
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 09.12.2013, (JDK 7)
 * 
 * */

public abstract class AbstractTransmitter {

	/**save reference of connection*/
	private Connect connection = null;
	/**save logger for global logging*/
	private Logger logger = QuadroidMain.logger;
	
	
	/**
	 * no public constructor
	 * */
	@SuppressWarnings("unused")
	private AbstractTransmitter()
	{	
	}
	
	/**
	 * Getter Function
	 * 
	 * @return get reference of {@link Connect}
	 * */
	public Connect getConnection()
	{
		return this.connection;
	}
	
	
	/**
	 * public Constructor 
	 * 
	 * @param connection -
	 * hand over an connection of typ {@link de.th_wildau.quadroid.connection.Connect}
	 * 
	 * @throws NullPointerException if connection are <b>null</b> 
	 * 
	 * */
	public AbstractTransmitter(Connect connection) throws NullPointerException
	{	
		if(connection == null)//if no usable connection available abort here
		{	logger.error("Given Reference of \"connection\" are null...");
			throw new NullPointerException("Hand over null for parameter \"connection\" ");
		}	
		this.connection = connection;
	}
	


	/**
	 * this function transmit data to connected device
	 * 
	 * @param msg - hand over data to transmit it
	 * */
	public synchronized void transmit(byte[] msg)
	{
		//handle transmission data 
	}
	
}

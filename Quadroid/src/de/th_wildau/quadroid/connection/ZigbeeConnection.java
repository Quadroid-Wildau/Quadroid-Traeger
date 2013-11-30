package de.th_wildau.quadroid.connection;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.slf4j.Logger;
import de.th_wildau.quadroid.devices.Zigbee;
import de.th_wildau.quadroid.interfaces.ISerialPortListener;


/**
 * This Class realized the communication between 
 * zigbee ground and mobile station. 
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 13.11.2013 (JDK 7)
 * @see SerialPortEventListener 
 * */

public final class ZigbeeConnection implements SerialPortEventListener
{
	/**for singleton implementation save an reference of this class*/
	private static ZigbeeConnection zch = null;
	/**logger for logging operations with {@link org.slf4j.Logger}*/
	private Logger logger = null;
	/**timeout for calling serial port*/
	private static final int TIMEOUT = 2000;
	/**state of connection*/
	private boolean isconnected = false;
	/**zigbee inputstream so read data*/
	private InputStream is = null;
	/**zigbee outputstream to write data*/
	private OutputStream os = null;
	/**save an instance of serial port for disconnect later */
	private SerialPort serialport = null;
	/**contains all registered listener*/
	private ArrayList<ISerialPortListener> listenerlist = null;


	
	/**
	 * invisible Constructor
	 * 
	 * @param logger
	 * hand over an logger to logging all operations
	 * */
	private ZigbeeConnection(Logger logger)
	{
		//if logger null do not get an reference back
		if(logger == null)
			return;
		//set static reference for singleton
		ZigbeeConnection.zch = this;
		//save reference of logger
		this.logger = logger;
		//working chain
		this.getSerialPort(this.getCommPort(this.getCommPortIdent(Zigbee.ZIGBEE_IDETIFIER.getPort())));
	
	}

	/**invisible Constructor*/
	private ZigbeeConnection()
	{
	}
		
	/**
	 * Singleton Constructor
	 * 
	 * @param logger 
	 * <p>this param must be an Logger of <b>log4j</b> see {@link http://www.slf4j.org/}
	 * <h1>Important: </h1> if hand over an <tt>null</tt>-reference for logging, 
	 * you will become an <tt>null</tt>-reference from singleton constructor!</p>
	 * 
	 * @return get an instance of {@link ZigbeeConnection} or <b>null</b> if logger null
	 * 
	 * */
	public static ZigbeeConnection getInstance(Logger logger)
	{
		if(ZigbeeConnection.zch == null)
		   new ZigbeeConnection(logger);
		return ZigbeeConnection.zch;
	}

	
	/**
	 * this function return an reference of {@link CommPortIdentifier}
	 * if device available.
	 * 
	 * @param ident hand over an identifier for device
	 * 
	 * @return get an reference of {@link CommPortIdentifier}, if not available
	 * you will become <tt>null</tt> reference 
	 *   
	 * */
	private CommPortIdentifier getCommPortIdent(final String ident)
	{
		try 
		{
			CommPortIdentifier commport = CommPortIdentifier.getPortIdentifier(ident);
			this.logger.debug("have found device on: " + ident);
			return commport;
			
		} 
		catch (NoSuchPortException e) 
		{
			this.logger.error("can t find device with ident: " + ident , e);
			return null;
		}
	}
	
	/**
	 * this function try to connect with comm port
	 * 
	 * @param commportident
	 * hand over an instance of {@link CommPortIdentifier} to connect with this port
	 * 
	 * @return get an instance of {@link CommPort},
	 * return <tt>null</tt> if not successfully or param unusable 
	 * 
	 * */
	private CommPort getCommPort(CommPortIdentifier commportident)
	{
		//end here if param unusable, it must be an Serial Port 
		if(commportident == null || commportident.getPortType() != CommPortIdentifier.PORT_SERIAL)
		{
			this.logger.error("CommPortIdentifier are unusable: " + commportident);
			return null;
		}	
		
		try 
		{	//try to connect... first param string to describe port, second param time for timeout 
			CommPort port = commportident.open(Zigbee.ZIGBEE_IDETIFIER.getPort(), TIMEOUT);			
			this.isconnected = true;//set state of connection
			this.logger.debug("Open CommPort Successfully");
			return port;
		} 
		catch (PortInUseException e) 
		{
			this.logger.error("can t get CommPort: ", e);
			return null;
		}
	}
	
	/**
	 * this function get an instance of SerialPort 
	 * 
	 * @param commport
	 * hand over an instance of {@link CommPort} for cast to {@link SerialPort}
	 * 
	 *  @return get an instance of {@link SerialPort},
	 *  return <tt>null</tt> if bad parameter or operation 
	 * 
	 * */
	private SerialPort getSerialPort(CommPort commport)
	{
		//bad param
		if(commport == null)
		{
			this.logger.error("Can t cast to SerialPort input " + commport);
			return null;
		}
		
		try
		{	//try to cast commport 
			this.serialport = (SerialPort) commport;
			this.serialport.setSerialPortParams(Zigbee.BAUDRATE.getValue(), 
												 Zigbee.DATABITS.getValue(), 
												 Zigbee.STOPBITS.getValue(), 
												 Zigbee.PARITY.getValue());
		
			
			this.is = this.serialport.getInputStream();
			this.os = this.serialport.getOutputStream();
			this.serialport.addEventListener(this);
			this.serialport.notifyOnDataAvailable(true);//listener will be notify if received data
			//other notifications possible
			
			this.logger.debug("Cast to SerialPort Successfully");
			this.logger.info("Connected to " + this.serialport + " with " + 
												this.serialport.getBaudRate() + " Baud " +
												this.serialport.getDataBits() + " DataBits " + 
												this.serialport.getStopBits() + " StopBits " +
												this.serialport.getParity() + " ParityBits ");
			return this.serialport;
		}
		catch(Exception e)
		{
			this.logger.error("Can t Cast to SerialPort ", e);
			return null;
		}	
	}
	
	/**
	 * This function must be called once the application should be kill
	 * it will disconnect the connection
	 * 
	 * */
	public void disconnect()
	{
		boolean is = false;//state of is
		boolean os = false;//state of os
		
		//prove of connection 
		if(this.isconnected && 
		   this.serialport != null)
		{
			try 
			{
				this.is.close();//try to close inputstream if successfully set state at true
				is = true;
				this.logger.debug("InputStream closed");
				
			} catch (IOException e) 
			{
				this.logger.error("Can t close InputStream: ", e);
			}
			
			try 
			{
				this.os.close();//try to close outputstream if successfully set state at true
				os = true;
				this.logger.debug("OutputStream closed");
				
			} catch (IOException e) 
			{
				this.logger.error("Can t close OutputStream: ", e);
			}
			
			//this.serialport.close();//firstly close serial port 
			this.logger.debug("SerialPort closed");
			
			if(is && os)
			{//once all streams are closed set state of connection to false
				this.isconnected = false;
				zch = null;
				this.logger.debug("disconnected");
				this.logger.info("disconnected");
			}	
		}
	}
	
	/**
	 * this method registered the {@link ISerialPortListener} for notify
	 * if data available
	 * 
	 * @param get an reference of {@link ISerialPortListener} 
	 * 
	 * */
	public void addSerialPortEventListener(ISerialPortListener evnt)
	{
		if(evnt == null)
		   return;
		if(this.listenerlist == null)
			this.listenerlist = new ArrayList<ISerialPortListener>();
		this.listenerlist.add(evnt);

	}
	
	/**
	 * This Method removes an {@link ISerialPortListener}
	 * 
	 * */
	public void removeSerialPortEventListener(ISerialPortListener toremovelistener)
	{
		if(this.listenerlist != null && toremovelistener != null)
		   this.listenerlist.remove(toremovelistener);
	}
	
	/**
	 * Getter Function
	 * 
	 * @return get state of connection 
	 * <ul>
	 * <li>true = connected successfully</li>
	 * <li>false = no connection</li>
	 * </ul>
	 * 
	 * */
 	public boolean isConnected()
	{
		return this.isconnected;
	}
	
 		
	/**
	 * Getter Function
	 * 
	 * @return the {@link InputStream} of serial communication with zigbee
	 */
	public InputStream getInputStream() 
	{
		return is;
	}

	/**
	 * Getter Function
	 * 
	 * @return the {@link OutputStream} of serial communication with zigbee
	 */
	public OutputStream getOutputStream() 
	{
		return os;
	}

	/**
	 * Getter Function
	 * @return reference of {@link org.slf4j.Logger}
	 * */
	public Logger getLogger()
	{
		return this.logger;
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) 
	{	//check for available registered listener 
		if(this.listenerlist != null)
		for(ISerialPortListener listener: this.listenerlist)
		{	//notify all registered listener
			listener.serialEvent(arg0, this.serialport, 
					this.getInputStream(), this.getOutputStream(), 
					this.logger);
		}
		
	}
		
}

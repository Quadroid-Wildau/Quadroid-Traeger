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
import de.th_wildau.quadroid.QuadroidMain;
import de.th_wildau.quadroid.interfaces.IDevice;
import de.th_wildau.quadroid.interfaces.AbstractReceiver;


/**
 * This class create serial connections for an specific device
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 13.11.2013 (JDK 7)
 * @see SerialPortEventListener 
 * */

public final class Connect implements SerialPortEventListener
{
	/**logger for logging operations with {@link org.slf4j.Logger}*/
	private Logger logger = QuadroidMain.logger;
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
	private ArrayList<AbstractReceiver> listenerlist = null;
	/**save the unique device name for search given device*/
	private IDevice dev = null;


	/**invisible Constructor*/
	private Connect()
	{
	}
	
	/**
	 * this method connect an given device to serial port 
	 * 
	 * @param device 
	 * <p>this param must be an device of typ {@link de.th_wildau.quadroid.interfaces.IDevice}
	 * hand over <tt>null</tt> reference function return null</p>
	 * 
	 * @param cc 
	 * <p>hand over an reference of {@link ConnectionContainer}, this parameter can not be null</p>
	 * 
	 * @return get an instance of {@link Connect} for given device, return  <b>null</b> if 
	 * {@link IDevice} or {@link ConnectionContainer} device are null
	 * 
	 * */
	public static Connect getInstance(IDevice device)
	{
		//if an reference null --> abort
		if(device == null)
			return null;
		ConnectionContainer cc = ConnectionContainer.getReference();
		//if already connected return this reference
		Connect connection = 
				cc.getConnectionForPort(device.getPort());
		if(connection != null)
			return connection; //abort if already connected
		
		//no connection --> create a new connection
		connection = new Connect();
		connection.dev = device;
		//working chain create serial connection
		connection.getSerialPort(connection.getCommPort(connection.getCommPortIdent(connection.dev.getPort())));
		
		//if successfully connected? 
		if(connection.serialport != null)
		{
			//add device to device list
			cc.addConnection(connection);
			
			return connection;
		}
		return null;		
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
			this.logger.debug("CommPortIdentifier operation successfully: " + ident);
			return commport;
			
		} 
		catch (NoSuchPortException e) 
		{
			this.logger.error("NoSuchPortException can not connect device to: " + ident , e);
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
		{	
			//try to connect... first param string to describe port, second param time for timeout 
			CommPort port = commportident.open(dev.getPort(), TIMEOUT);			
			this.isconnected = true;//set state of connection
			this.logger.debug("Open comm port successfully");
			return port;
		} 
		catch (PortInUseException e) 
		{
			this.logger.error("PortInUseException port already in use: ", e);
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
			this.logger.error("Can not create an serial port " + commport);
			return null;
		}
		
		try
		{	//try to cast commport 
			this.serialport = (SerialPort) commport;
			//connect with specific parameter to serial port
			this.serialport.setSerialPortParams(dev.getBaud(), dev.getDatabits(), dev.getStopBits(), dev.getParity());
			
			this.is = this.serialport.getInputStream();//get stream pipes to communicate about serial port
			this.os = this.serialport.getOutputStream();
			this.serialport.addEventListener(this);//register listener for receive incoming data
			this.serialport.notifyOnDataAvailable(true);//activate events for incoming data
			//other notifications possible
			
			this.logger.debug("Connect to port successfully");
			this.logger.info("Connect "+ dev.getDeviceName() +" to " + this.serialport + " with " + 
												this.serialport.getBaudRate() + " Baud " +
												this.serialport.getDataBits() + " DataBits " + 
												this.serialport.getStopBits() + " StopBits " +
												this.serialport.getParity() + " ParityBits ");
			return this.serialport;
		}
		catch(Exception e)
		{
			this.logger.error("Can not connect ", e);
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
		boolean inputstream = false;//state of is
		boolean outputstream = false;//state of os
		
		//prove of connection 
		if(this.isconnected && 
		   this.serialport != null)
		{
			
			try 
			{
				this.is.close();//try to close inputstream if successfully set state at true
				inputstream = true;
				this.logger.debug("InputStream closed");
				
			} catch (IOException e) 
			{
				this.logger.error("Can t close InputStream: ", e);
			}
			
			try 
			{
				this.os.close();//try to close outputstream if successfully set state at true
				outputstream = true;
				this.logger.debug("OutputStream closed");
				
			} catch (IOException e) 
			{
				this.logger.error("Can t close OutputStream: ", e);
			}
			
			this.serialport.close();// close serial port 
			this.logger.debug("SerialPort closed");
			
			if(inputstream && outputstream)
			{//once all streams are closed set state of connection to false
				ConnectionContainer.getReference().removeConnection(this);
				this.isconnected = false;
				this.dev = null;
				this.logger.debug("disconnected");
				this.logger.info("disconnected");
			}	
		}
	}
	
	/**
	 * this method registered the {@link AbstractReceiver} for notify
	 * if data available
	 * 
	 * @param get an reference of {@link AbstractReceiver} 
	 * 
	 * */
	public void addSerialPortEventListener(AbstractReceiver evnt)
	{
		if(evnt == null)
		   return;
		if(this.listenerlist == null)
			this.listenerlist = new ArrayList<AbstractReceiver>();
		this.listenerlist.add(evnt);

	}
	
	/**
	 * This Method removes an {@link AbstractReceiver}
	 * 
	 * */
	public void removeSerialPortEventListener(AbstractReceiver toremovelistener)
	{
		if(this.listenerlist != null && toremovelistener != null && 
		   this.listenerlist.contains(toremovelistener))
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
	 * @return the {@link InputStream} of serial communication
	 */
	public InputStream getInputStream() 
	{
		return is;
	}

	/**
	 * Getter Function
	 * 
	 * @return the {@link OutputStream} of serial communication
	 */
	public OutputStream getOutputStream() 
	{
		return os;
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) 
	{	//check for available registered listener 
		if(this.listenerlist != null)
		for(AbstractReceiver listener: this.listenerlist)
		{	//notify all registered listener
			listener.serialEvent(arg0, this.serialport, 
					this.getInputStream(), this.getOutputStream(), 
					this.logger);
		}
		
	}

	/**
	 * Getter Function
	 * 
	 * @return get instance of connected {@link IDevice}
	 * 
	 * */
	public IDevice getDevice()
	{
		return this.dev;
	}
	
}

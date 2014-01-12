package de.th_wildau.quadroid.models;

import purejavacomm.SerialPort;
import de.th_wildau.quadroid.interfaces.IDevice;
import de.th_wildau.quadroid.utils.Properties;

/**
 * GPS Device Model for serial connection 
 * 
 * @author Alex
 * @version 1.0, 11.01.2014, (JDK 7)
 * @see IDevice
 * 
 * */


public class GPSModule implements IDevice{

	@Override
	public String getPort() 
	{
		return Properties.getInstance().getProperty("gpsport");
	}

	@Override
	public String getDeviceName() 
	{
		return "u Block GPS Device";
	}

	@Override
	public int getBaud() 
	{	
		return 9600;
	}

	@Override
	public int getParity() 
	{
		return SerialPort.PARITY_NONE;
	}

	@Override
	public int getDatabits() 
	{
		return SerialPort.DATABITS_8;
	}

	@Override
	public int getStopBits() 
	{
		return SerialPort.STOPBITS_1;
	}

}

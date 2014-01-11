package de.th_wildau.quadroid.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import purejavacomm.SerialPort;
import purejavacomm.SerialPortEvent;
import de.th_wildau.quadroid.interfaces.AbstractReceiver;
import de.th_wildau.quadroid.models.GPSPos;
import de.th_wildau.quadroid.models.NaviDataContainer;


/**
 * This class handle incoming data from GPS module
 * 
 * @author Alex
 * @version 1.0, 11.01.2014, (JDK 7)
 * @see AbstractReceiver
 * 
 * */

public class GPSModuleReceiveHandler extends AbstractReceiver {
	
	
	/**
	 * receiver for handle input date from gps module
	 * 
	 * */
	@Override
	public void serialEvent(SerialPortEvent evnt, SerialPort port, 
			InputStream istream, OutputStream ostream, Logger logger) {
		
		
		
		switch(evnt.getEventType()) {
        case SerialPortEvent.BI://ignore flow controlling for serial communication
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE:
        	//buffer for input data
			ByteArrayOutputStream buffer =  new ByteArrayOutputStream();
			int rx = 0;
			try 
			{
				while((rx = istream.read()) != -1)//reader input stream
				{
					byte b = (byte)rx;
					if(b == 10)//end marker 
						break;
					buffer.write(b);//save data into bytestream
				}
			} catch (IOException e) 
			{
				logger.error("Receiver-Exception: ", e);
			}	
			
			String string = buffer.toString();
			
			//GPGGA,104506.00,5219.15347,N,01337.98647,E,1,05,3.32,28.4,M,44.7,M,,*6D
			if (string.startsWith("$GPGGA")) {
				GPSPos pos = this.decodeGPSPos(string);
				logger.debug("gps pos: lat:" + pos.getLatitude() + " lng: " + pos.getLongitude() + "height :" + pos.getAltitude());
				NaviDataContainer.getInstance().addGPSPosition(pos);
			}
        }
	}
	
	
	/**
	 * This function decode incoming <b>NMEA</b> data to {@link GPSPos} data
	 * 
	 *  @param string - hand over <b>NMEA</b> String to decode
	 * 
	 * 	@return current position as {@link GPSPos} object
	 * */
	private GPSPos decodeGPSPos(String string) {
		GPSPos pos = new GPSPos();
		//incoming data are comma separated values
		String[] strings = string.split(",");
		float latitude = 0;
		float longitude = 0;
		int time = 0;
		float altitude = 0;
		//second data are time 
		//contain second string data? then set to time
		if(strings[1] != null && !strings[1].equals("")) {
			time = (int)Float.parseFloat(strings[1]);//gps time data
		}
		// if data available contains degree minute second into third string
		//data for latitude
		if(strings[2] != null && !strings[2].equals("")) {
			int pointIndex = strings[2].indexOf(".");
			String deg = strings[2].substring(0, pointIndex - 2);//save degree
			String minute = strings[2].substring(pointIndex-2, pointIndex);//save minute
			String second = strings[2].substring(pointIndex + 1, strings[2].length());//save second
			float degVal = Float.parseFloat(deg);
			float minuVal = Float.parseFloat(minute);
			float secVal = Float.parseFloat(second) / 1000;
			//compute latitude value
			latitude = degVal + minuVal / 60 + secVal / 3600;
		}
		//N side or S of geoid
		if(strings[3] != null) 
		{	//N or S direction
			if(strings[3].equals("S")) 
			{
				latitude *= -1;
			}
		}
		//data for longitude
		//if data available save degree minute second
		if(strings[4] != null && !strings[4].equals("")) {
			int pointIndex = strings[4].indexOf(".");
			String deg = strings[4].substring(0, pointIndex - 2);//save degree
			String minute = strings[4].substring(pointIndex-2, pointIndex);//save minute
			String second = strings[4].substring(pointIndex + 1, strings[4].length());//second 
			float degVal = Float.parseFloat(deg);
			float minuVal = Float.parseFloat(minute);
			float secVal = Float.parseFloat(second) / 1000;
			//compute longitude value
			longitude = degVal + minuVal / 60 + secVal / 3600;
		}
		//W side or E of geoid
		if(strings[5] != null) 
		{	//W or E direction
			if(strings[5].equals("W")) 
			{
				longitude *= -1;
			}
		}
		//get altitude data
		if(strings[11] != null && !strings[11].isEmpty()) {
			altitude = Float.parseFloat(strings[11]);
		}
		//add values to object 
		pos.setLatitude(latitude);
		pos.setLongitude(longitude);
		pos.setTime(time);
		pos.setAltitude(altitude);
		
		return pos;
	}
}

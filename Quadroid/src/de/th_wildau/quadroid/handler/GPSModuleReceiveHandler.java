package de.th_wildau.quadroid.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;

import purejavacomm.SerialPort;
import purejavacomm.SerialPortEvent;
import de.th_wildau.quadroid.decoder.RxDataDecoder;
import de.th_wildau.quadroid.interfaces.AbstractReceiver;
import de.th_wildau.quadroid.models.GPSPos;
import de.th_wildau.quadroid.models.NaviDataContainer;

public class GPSModuleReceiveHandler extends AbstractReceiver {
	
	@Override
	public void serialEvent(SerialPortEvent evnt, SerialPort port, 
			InputStream istream, OutputStream ostream, Logger logger) {
		
		switch(evnt.getEventType()) {
        case SerialPortEvent.BI:
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
				while((rx = istream.read()) != -1)
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
	
	private GPSPos decodeGPSPos(String string) {
		GPSPos pos = new GPSPos();
		String[] strings = string.split(",");
		float latitude = 0;
		float longitude = 0;
		int time = 0;
		float altitude = 0;
		
		if(strings[1] != null && !strings[1].equals("")) {
			time = (int)Float.parseFloat(strings[1]);
		}
		
		if(strings[2] != null && !strings[2].equals("")) {
			int pointIndex = strings[2].indexOf(".");
			String deg = strings[2].substring(0, pointIndex - 2);
			String minute = strings[2].substring(pointIndex-2, pointIndex);
			String second = strings[2].substring(pointIndex + 1, strings[2].length());
			float degVal = Float.parseFloat(deg);
			float minuVal = Float.parseFloat(minute);
			float secVal = Float.parseFloat(second) / 1000;
			
			latitude = degVal + minuVal / 60 + secVal / 3600;
		}
		
		if(strings[3] != null) {
			if(strings[3].equals("S")) {
				latitude *= -1;
			}
		}
		
		if(strings[4] != null && !strings[4].equals("")) {
			int pointIndex = strings[4].indexOf(".");
			String deg = strings[4].substring(0, pointIndex - 2);
			String minute = strings[4].substring(pointIndex-2, pointIndex);
			String second = strings[4].substring(pointIndex + 1, strings[4].length());
			float degVal = Float.parseFloat(deg);
			float minuVal = Float.parseFloat(minute);
			float secVal = Float.parseFloat(second) / 1000;
			
			longitude = degVal + minuVal / 60 + secVal / 3600;
		}
		
		if(strings[5] != null) {
			if(strings[5].equals("W")) {
				longitude *= -1;
			}
		}
		
		if(strings[11] != null && !strings[11].isEmpty()) {
			altitude = Float.parseFloat(strings[11]);
		}
		
		pos.setLatitude(latitude);
		pos.setLongitude(longitude);
		pos.setTime(time);
		pos.setAltitude(altitude);
		
		return pos;
	}
}

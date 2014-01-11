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
		logger.info("GPSModule evnt: "+ evnt.getEventType());
		
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
					logger.debug("receive gps data");
					if(b == 10)//end marker 
						break;
					buffer.write(b);//save data into bytestream
				}
			} catch (IOException e) 
			{
				logger.error("Receiver-Exception: ", e);
			}	
			
			String string = buffer.toString();
			
			if (string.startsWith("$GPGLL")) {
				logger.debug("received complete data :" + string);
				GPSPos pos = this.decodeGPSPos(string);
				
				NaviDataContainer.getInstance().addGPSPosition(pos);
				logger.debug("pos lat:" + pos.getLatitude() + " lng:" + pos.getLongitude());
			}
        }
	}
	
	private GPSPos decodeGPSPos(String string) {
		GPSPos pos = new GPSPos();
		String[] strings = string.split(",");
		float latitude = 0;
		float longitude = 0;
		
		if(strings[1] != null && !strings[1].equals("")) {
			int pointIndex = strings[1].indexOf(".");
			String deg = strings[1].substring(0, pointIndex - 2);
			String minute = strings[1].substring(pointIndex-2, pointIndex);
			String second = strings[1].substring(pointIndex + 1, strings[1].length());
			float degVal = Float.parseFloat(deg);
			float minuVal = Float.parseFloat(minute);
			float secVal = Float.parseFloat(second) / 1000;
			
			System.out.println("deg :" + degVal + " minVal :" + minuVal + " secVal:" + secVal);
			latitude = degVal + minuVal / 60 + secVal / 3600;
		}
		
		if(strings[2] != null) {
			if(strings[2].equals("S")) {
				latitude *= -1;
			}
		}
		
		if(strings[3] != null && !strings[3].equals("")) {
			int pointIndex = strings[3].indexOf(".");
			String deg = strings[3].substring(0, pointIndex - 2);
			String minute = strings[3].substring(pointIndex-2, pointIndex);
			String second = strings[3].substring(pointIndex + 1, strings[1].length());
			float degVal = Float.parseFloat(deg);
			float minuVal = Float.parseFloat(minute);
			float secVal = Float.parseFloat(second) / 1000;
			
			System.out.println("deg :" + degVal + " minVal :" + minuVal + " secVal:" + secVal);
			longitude = degVal + minuVal / 60 + secVal / 3600;
		}
		
		if(strings[4] != null) {
			if(strings[4].equals("W")) {
				longitude *= -1;
			}
		}
		
		pos.setLatitude(latitude);
		pos.setLongitude(longitude);
		
		return pos;
	}
}

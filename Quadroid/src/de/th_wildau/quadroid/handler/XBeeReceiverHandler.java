package de.th_wildau.quadroid.handler;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import de.th_wildau.quadroid.interfaces.AbstractReceiver;

/**
 * this class manage incoming data for xbee device
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 28.11.2013 (JDK 7)
 * @see AbstractReceiver 
 * 
 * */

public class XBeeReceiverHandler extends AbstractReceiver
{
	
	@Override
	public void serialEvent(SerialPortEvent evnt, SerialPort port, 
			InputStream istream, OutputStream ostream, Logger logger) 
	{
		//if data available get in
		if(evnt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
		{	
			logger.info("DataAvailable");
			//buffer for input data
			ByteArrayOutputStream rxbase64buffer =  new ByteArrayOutputStream();
			int rx = 0;
			try 
			{
				while((rx = istream.read()) != -1)
				{
					byte b = (byte)rx;
					if((char)b == '<')//end marker 
						break;
					rxbase64buffer.write(b);//save data into bytestream
				}
			} catch (IOException e) 
			{
				logger.error("Receiver-Exception: ", e);
			}	
				//kill receiving, all data received! last characters are end marker
				port.notifyOnDataAvailable(false);
				
				//decode data from base64 so data bytes
				byte[] data = Base64.decodeBase64(rxbase64buffer.toByteArray()); 
				//notify all observer
				if(data.length >= 4)//only notify if really data are available and no marker
					
				//TODO: Decode Array of data info objects and notify listeners
					
				try 
				{
					rxbase64buffer.close();//close buffer
				} catch (IOException e)
				{
					logger.error("BufferCloseException", e);
				}
				//reactivate receiving
				port.notifyOnDataAvailable(true);
				
			
		
		}
	}

}

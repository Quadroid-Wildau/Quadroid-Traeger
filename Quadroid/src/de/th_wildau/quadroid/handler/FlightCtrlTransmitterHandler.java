package de.th_wildau.quadroid.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import de.th_wildau.quadroid.QuadroidMain;
import de.th_wildau.quadroid.connection.Connect;
import de.th_wildau.quadroid.interfaces.AbstractTransmitter;
import de.th_wildau.quadroid.models.GPSPos;
import de.th_wildau.quadroid.models.Waypoint;


/**
 * 
 * 
 * @author Alex
 * @version 1.0, 11.01.2014, (JDK 7)
 * @see AbstractTransmitter
 * 
 * 
 * 
 * **/


public class FlightCtrlTransmitterHandler extends AbstractTransmitter {
	/**reference of logger*/
	private Logger logger = QuadroidMain.logger;
	/**allocate blocking queue for 1000 transmissions*/
	private ArrayBlockingQueue<byte[]> queue = new ArrayBlockingQueue<byte[]>(1000);
	/**internal class for transmission to Flight-Ctrl*/
	private SenderThread senderThread = new SenderThread();
	
	/**
	 * public Constructor 
	 * 
	 * @param connection -
	 * hand over an connection of typ {@link de.th_wildau.quadroid.connection.Connect}
	 * 
	 * @throws NullPointerException if connection are <b>null</b> 
	 * 
	 * */
	public FlightCtrlTransmitterHandler(Connect connection) throws NullPointerException {	
		super(connection);
		this.senderThread.start();
	}
		

	/**
	 * this method transmit to the Navi-Ctrl an 
	 * command for requested data
	 * 
	 * 
	 * */
	public void requestNaviData() {
		//command for requested data
		char[] chars = new char[] {'#', 'a', 'o', 10};
		
		this.transmit(chars);
	}
	
	/**
	 * This method transmit data, before transmission put crc checksum to data 
	 * 
	 * @param chars - hand over data to transmit to Flight-Ctrl
	 * 
	 * */
	public void transmit(char[] chars) 
	{
		int crc = 0;
		int crc1 = 0;
		int crc2 = 0;
		
		char[] sendChar = new char[chars.length + 3];
		
		//*************************************************
		//compute crc value
		for(int i = 0; i < (chars.length); i++) {
			crc += chars[i];  
		}	
		
		crc %= 4096;
		crc1 = '=' + crc / 64;
		crc2 = '=' + crc % 64;
		//**************************************************
		//copy array
		for (int i = 0; i < chars.length; i++) 
		{
			sendChar[i] = chars[i];
		}
		
		sendChar[chars.length] = (char) crc1;//put first crc value
		sendChar[chars.length + 1] = (char) crc2;//put second crc value
		sendChar[chars.length + 2] = '\r';//set end marker for detection transmission end
		//add all data + crc to transmission byte array before cast all data to bytes
		byte[] b = new byte[sendChar.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) sendChar[i];
		}
		//transmit
		this.transmit(b);
	}
	
	/**
	 * this function transmit data to Flight-Ctrl ground station
	 * 
	 * @param msg - hand over data to transmit
	 * */
	@Override
	public void transmit(byte[] msg) {		
		try 
		{
			//logger.debug("before size :" + this.queue.size()); 
			this.queue.put(msg);//put to blocking queue
			//logger.debug("after size :" + this.queue.size());
		} catch (InterruptedException e) {
			//no logging for interrupt exception!
		}
	}
	
	
	/**
	 * Internal class for transmission data from blocking queue 
	 * @author Alex
	 * @version 1.0, 11.01.2014, (JDK 7)
	 * @see Thread
	 * 
	 * */
	private class SenderThread extends Thread 
	{
		/**state of running thread*/
		private boolean isRunning = true;
		/**
		 * kill running thread
		 * */
		@SuppressWarnings("unused")
		public synchronized void cancel() {
			this.isRunning = false;
		}
		
		@Override
		public void run() 
		{
			while(isRunning) 
			{
				try 
				{
					logger.debug("queue size : " + queue.size());
					//blocking queue main 500 MILLISECONDS to get next
					byte[] bytes = queue.poll(500, TimeUnit.MILLISECONDS);
					
					if(bytes != null) 
					{
						logger.debug("byte length : " + bytes.length);
						//transmission
						getConnection().getOutputStream().write(bytes);
					}
				} catch (IOException e) 
				{
					logger.error("Transmission Exception: ", e);
				} catch (InterruptedException e) 
				{
					//no logging for thread sleeping!
				}
			}
		}
	}
}

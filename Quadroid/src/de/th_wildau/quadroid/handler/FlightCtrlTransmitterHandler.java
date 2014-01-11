package de.th_wildau.quadroid.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import sun.misc.Queue;
import de.th_wildau.quadroid.QuadroidMain;
import de.th_wildau.quadroid.connection.Connect;
import de.th_wildau.quadroid.interfaces.AbstractTransmitter;
import de.th_wildau.quadroid.models.GPSPos;
import de.th_wildau.quadroid.models.Waypoint;

public class FlightCtrlTransmitterHandler extends AbstractTransmitter {
	/**reference of logger*/
	private Logger logger = QuadroidMain.logger;
	private ArrayBlockingQueue<byte[]> queue = new ArrayBlockingQueue<byte[]>(1000);
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
		

	public void requestNaviData() {
		char[] chars = new char[] {'#', 'a', 'o', 10};
		
		this.transmit(chars);
	}
	
	public void addWaypoints(Waypoint waypoint) {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		
		GPSPos geoPos = new GPSPos();
		//this.transmit(msg);
	}
	
	public void transmit(char[] chars) {
		int crc = 0;
		int crc1 = 0;
		int crc2 = 0;
		char[] sendChar = new char[chars.length + 3];
		
		for(int i = 0; i < (chars.length); i++) {
			crc += chars[i];  
		}	
		
		crc %= 4096;
		crc1 = '=' + crc / 64;
		crc2 = '=' + crc % 64;
		
		for (int i = 0; i < chars.length; i++) {
			sendChar[i] = chars[i];
		}
		
		sendChar[chars.length] = (char) crc1;
		sendChar[chars.length + 1] = (char) crc2;
		sendChar[chars.length + 2] = '\r';
		
		System.out.println(sendChar);
		byte[] b = new byte[sendChar.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) sendChar[i];
		}
		
		this.transmit(b);
	}
	
	/**
	 * this function transmit data to xBee ground station
	 * 
	 * @param msg - hand over data to transmit
	 * */
	@Override
	public void transmit(byte[] msg) {		
		try {
			logger.debug("before size :" + this.queue.size());
			this.queue.put(msg);
			logger.debug("after size :" + this.queue.size());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//			this.getConnection().getOutputStream().write(msg);
//		} catch (IOException e) {
//			logger.error("Transmission Exception: ", e);
//		}
	}
	
//	private byte[] encodePoint(Point point) {
//		
//	}
//	
//	private byte[] encodeGPSPos(GPSPos gpsPos) {
//		
//	}
//	
//	public byte endcodeU8(short value) {
//		ByteBuffer buffer = ByteBuffer.allocate(1);
//		buffer.
//		
//		return buffer.get();
//	}
	
	private class SenderThread extends Thread {
		private boolean isRunning = true;
		
		public synchronized void cancel() {
			this.isRunning = false;
		}
		
		@Override
		public void run() {
			while(isRunning) {
				try {
					logger.debug("queue size : " + queue.size());
					byte[] bytes = queue.poll(500, TimeUnit.MILLISECONDS);
					
					if(bytes != null) {
						logger.debug("byte length : " + bytes.length);
						getConnection().getOutputStream().write(bytes);
					}
				} catch (IOException e) {
					logger.error("Transmission Exception: ", e);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

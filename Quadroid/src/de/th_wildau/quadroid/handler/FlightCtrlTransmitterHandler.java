package de.th_wildau.quadroid.handler;

import java.io.IOException;

import org.slf4j.Logger;

import de.th_wildau.quadroid.QuadroidMain;
import de.th_wildau.quadroid.connection.Connect;
import de.th_wildau.quadroid.interfaces.AbstractTransmitter;

public class FlightCtrlTransmitterHandler extends AbstractTransmitter {
	/**reference of logger*/
	private Logger logger = QuadroidMain.logger;
	
	
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
	}
		

	public void requestNaviData() {
		char[] chars = new char[] {'#', 'a', 'o', 100};
		
		this.transmit(chars);
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
			this.getConnection().getOutputStream().write(msg);
		} catch (IOException e) {
			logger.error("Transmission Exception: ", e);
		}
	}
}

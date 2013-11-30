package de.th_wildau.quadroid.handler;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import de.th_wildau.quadroid.QuadroidMain;
import de.th_wildau.quadroid.connection.ZigbeeConnection;
import de.th_wildau.quadroid.marker.Marker;

/**
 * 
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 27.11.2013 (JDK 7)
 *  
 * */

public class TransmitterHandler
{
	/**reference of logger*/
	private Logger logger = QuadroidMain.logger;
	
	/**save connection to zigbee*/
	private ZigbeeConnection comm = null;
	
	/**
	 * Constructor 
	 * init zigbee communication 
	 * */
	public TransmitterHandler()
	{	//set reference if not available
		this.comm = ZigbeeConnection.getInstance(QuadroidMain.logger);
	}
		
	/**
	 * this function appends data - enclosed with marker-from second byte array to first array
	 * 
	 * @param availabledata - hand over available date at byte-array if no data available hand over <tt>null</tt>
	 * 
	 * @param startmarker - hand over an start marker see {@link de.th_wildau.quadroid.marker.Marker} 
	 * 
	 * @param appenddata - hand over data for append 
	 * 
	 * @param endmarker - hand over an end marker see {@link de.th_wildau.quadroid.marker.Marker} 
	 * 
	 * @return returns an byte array with data from first an second array, or <tt>null</tt> 
	 * if first or second array or marker are <tt>null</tt>
	 *  
	 * */
	public byte[] appendData(byte[] availabledata, String startmarker,
			byte[] appenddata, String endmarker) 
	{	//interrupt if data not valid
		if(availabledata == null || 
			 startmarker == null || 
			  appenddata == null || 
			  endmarker == null)
			return null;
		
		//length for allocate
		int length = (availabledata.length + startmarker.length() + 
				appenddata.length + endmarker.length());
		//create buffer with specific length
		ByteBuffer buffer = ByteBuffer.allocate(length);
		//set avaiable data
		buffer.put(availabledata);
		//set start marker for embedded data
		buffer.put(startmarker.getBytes());
		//append data
		buffer.put(appenddata);
		//set end marker
		buffer.put(endmarker.getBytes());
				
		return buffer.array();
	}

	/**
	 * this function append the CRC32 Checksum at last bytes into the array
	 * 
	 * @param data - hand over data for calculate crc checksum 
	 * 
	 * @return an array of bytes with appended CRC32 Checksum,
	 * if data <tt>null</tt> return null
	 *   
	 * */
	public byte[] appendCRC32(byte[] data) 
	{
		if(data == null)
			return null;
			
		CRC32 crc = new CRC32();
		//crc calculation
		crc.update(data);
		
		String crcvalue = String.valueOf(crc.getValue());
		String startmarker = Marker.CRCSTART.getMarker();
		String endmarker = Marker.CRCEND.getMarker();
		//length for allocation
		int length = (data.length + crcvalue.length() + 
				startmarker.length() + endmarker.length());
		//init buffer
		ByteBuffer buffer = ByteBuffer.allocate(length);
		//set data
		buffer.put(data);
		//set start marker
		buffer.put(startmarker.getBytes());
		//set crc value
		buffer.put(crcvalue.getBytes());
		//set end marker
		buffer.put(endmarker.getBytes());
		return buffer.array();
	}

	/**
	 * this function transmit data to zigbee ground station
	 * 
	 * @param msg - hand over data to transmit
	 * */
	public void transmit(byte[] msg)
	{
		if(msg == null)
			return;
		//encode data
		byte[] tx = Base64.encodeBase64(msg);
		logger.debug("EncodeBase64");
		
	  try
	  {	
		this.comm.getOutputStream().write(tx);//send data
		this.comm.getOutputStream().write("<<<".getBytes());//send end marker
		this.comm.getOutputStream().flush();//flush pipe
		logger.debug("Transmit Data");
	  }catch(Exception e)
	  {
		  logger.error("Transmission Exception: ", e);
	  }	
		
	}
	
	
}

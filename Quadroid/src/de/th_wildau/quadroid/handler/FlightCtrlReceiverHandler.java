package de.th_wildau.quadroid.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.slf4j.Logger;

import purejavacomm.SerialPort;
import purejavacomm.SerialPortEvent;
import de.th_wildau.quadroid.interfaces.AbstractReceiver;
import de.th_wildau.quadroid.models.GPSPos;
import de.th_wildau.quadroid.models.GPSPosDev;
import de.th_wildau.quadroid.models.NaviData;
import de.th_wildau.quadroid.models.NaviDataContainer;

/**
 * This class handle incoming data from Flight-Ctrl
 * 
 * @author Alex
 * @version 1.0, 11.01.2014, (JDK 7)
 * @see AbstractReceiver
 * 
 * 
 * */


public class FlightCtrlReceiverHandler extends AbstractReceiver {
	
	@Override
	public void serialEvent(SerialPortEvent event, SerialPort port, 
			InputStream inputStream, OutputStream ostream, Logger logger) {
		//logger.info("FlightCtrlReceiverHandler evnt: "+ event.getEventType());
		
		switch(event.getEventType()) 
		{
        case SerialPortEvent.BI://ignore flow control for serial communication
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE://data available!
        	//buffer for incomming data
            byte[] readBuffer = new byte[1024];
            int current;

            try {
            	int i = 0;
            	//read inputstream 
                while ((current = inputStream.read()) > -1) 
                {
                    if ((current & 0xFF) != '\r') //endmarker for incomming data if reach --> transmission ending
                    {	//save incomming data
                    	readBuffer[i] = (byte) current;
                    	i++;
                    } else 
                    {
                    	if(i > 3) 
                    	{	//decode modified base64 data at specific position 
                    		byte[] bytes = decode64(readBuffer, 3, i - 3);
                        	
                        	if(readBuffer[2] == 'O') 
                        	{	//convert all data
                        		converteData(bytes);
                        		logger.debug("convert data");
                        	}
                        	readBuffer = new byte[1024];
                        	i = 0;
                    	}
                    }
                }
            } catch (IOException e) {logger.error(e.getLocalizedMessage());}
            break;
            default:
            	logger.debug("Default value into FlightCtrlReceiverHandler");
            break;	
        }
	}
	
	/**
	 * This Method create and {@link NaviData} model and put it to data container
	 * all received data are available in {@link NaviDataContainer} 
	 * 
	 * @param bytes - hand over data from flight-ctrl to convert
	 * 
	 * */
	public void converteData(byte[] bytes) 
	{
        NaviData model = new NaviData();
		
        // all decoder become an subarray and decode the specific data
        
        
        model.setVersion(this.decodeU8(bytes[0]));
        model.setCurrentPosition(this.decodeGPSPos(Arrays.copyOfRange(bytes, 1, 14)));
        model.setTargetPosition(this.decodeGPSPos(Arrays.copyOfRange(bytes, 14, 27)));
        model.setTargetPositionDeviation(this.decodeGPSPosDev(Arrays.copyOfRange(bytes, 27, 31)));
        model.setHomePosition(this.decodeGPSPos(Arrays.copyOfRange(bytes, 31, 44)));
        model.setHomePositionDeviation(this.decodeGPSPosDev(Arrays.copyOfRange(bytes, 44, 48)));
        model.setWaypointIndex(this.decodeU8(bytes[48]));
        model.setWaypointNumber(this.decodeU8(bytes[49]));
        model.setSatsInUse(this.decodeU8(bytes[50]));
        model.setAltimeter(this.decodeS16(Arrays.copyOfRange(bytes, 51, 53)));
        model.setVariometer(this.decodeS16(Arrays.copyOfRange(bytes, 53, 55)));
        model.setFlyingTime(this.decodeU16(Arrays.copyOfRange(bytes, 55, 57)));
        model.setuBat(this.decodeU8(bytes[57]));
        model.setGroundSpeed(this.decodeU16(Arrays.copyOfRange(bytes, 58, 60)));
        model.setHeading(this.decodeS16(Arrays.copyOfRange(bytes, 60, 62)));
        model.setCompassHeading(this.decodeS16(Arrays.copyOfRange(bytes, 62, 64)));
        model.setAngleNick(this.decodeS8(bytes[64]));
        model.setAngleRoll(this.decodeS8(bytes[65]));
        model.setRcQuality(this.decodeU8(bytes[66]));
        model.setfCStatusFlags(this.decodeU8(bytes[67]));
        model.setnCFlags(this.decodeU8(bytes[68]));
        model.setErrorcode(this.decodeU8(bytes[69]));
        model.setOperatingRadius(this.decodeU8(bytes[70]));
        model.setTopSpeed(this.decodeS16(Arrays.copyOfRange(bytes, 71, 73)));
        model.setTargetHoldTime(this.decodeU8(bytes[73]));
        model.setfCStatusFlags2(this.decodeU8(bytes[74]));
        model.setSetpointAltitude(this.decodeU16(Arrays.copyOfRange(bytes, 75, 77)));
        model.setGas(this.decodeU8(bytes[77]));
        model.setCurrent(this.decodeU16(Arrays.copyOfRange(bytes, 78, 80)));
        model.setUsedCapacity(this.decodeU16(Arrays.copyOfRange(bytes, 80, 83)));
        
        NaviDataContainer.getInstance().addNaviData(model);
	}

	/**
	 * decoder for {@link GPSPos} 
	 * 
	 * @param bytes - hand over an subarray contains data for GPS
	 * 
	 * */
	private GPSPos decodeGPSPos(byte[] bytes) {
		GPSPos model = new GPSPos();
		//decode and set GNSS data
		model.setLongitude(this.decodeS32(Arrays.copyOfRange(bytes, 0, 4)));
		model.setLatitude(this.decodeS32(Arrays.copyOfRange(bytes, 4, 8)));
		model.setAltitude(this.decodeS32(Arrays.copyOfRange(bytes, 8, 12)));
		model.setAltitude(this.decodeU8(bytes[12]));
		
		return model;
	}
	/**
	 * decoder for deviation of {@link GPSPosDev}
	 * 
	 * @param bytes - hand over an subarray contains data for GPS Deviation
	 * 
	 * */
	private GPSPosDev decodeGPSPosDev(byte[] bytes) {
		GPSPosDev model = new GPSPosDev();
		
		model.setDistance(this.decodeS16(Arrays.copyOfRange(bytes, 0, 2)));
		model.setBearing(this.decodeS16(Arrays.copyOfRange(bytes, 2, 4)));
		
		return model;
	}
	
	/**
	 * decoder for modified bast64 from Flight-Ctrl
	 * 
	 * @param in_arr - hand over data contains base64 coded data
	 * @param offset - start position of base64 data into <tt>in_arr</tt>
	 * @param len - end position of base64 coded data into <tt>in_arr</tt>
	 * 
	 * @return get decoded data 
	 * 
	 * */
	public byte[] decode64(byte[] in_arr, int offset, int len) 
	{
		
		//TODO: Alex its your turn 
		
        int ptrIn = offset;
        byte a, b, c, d, x, y, z;
        int ptr = 0;

        int max = len - (len / 4);
        byte[] out_arr = new byte[max];

        while (max != 0) {
            a = 0;
            b = 0;
            c = 0;
            d = 0;
            
            try {
                a = (byte)(in_arr[ptrIn++] - '=');
                b = (byte)(in_arr[ptrIn++] - '=');
                c = (byte)(in_arr[ptrIn++] - '=');
                d = (byte)(in_arr[ptrIn++] - '=');
            } catch (Exception e) {
            	e.printStackTrace();
            }
            
            x = (byte) ((a << 2) | (b >> 4));
            y = (byte) (((b & 0x0f) << 4) | (c >> 2));
            z = (byte) (((c & 0x03) << 6) | d);

            if ((max--) != 0) {
                out_arr[ptr++] = (byte) x;
            } else {
                break;
            }
            if ((max--) != 0) {
                out_arr[ptr++] = (byte) y;
            } else {
                break;
            }
            if ((max--) != 0) {
                out_arr[ptr++] = (byte) z;
            } else {
                break;
            }
        }

        return out_arr;
    }
	
	/**
	 * decoder for unsigned byte from C
	 * 
	 * @param b - hand over byte data to decode 
	 * 
	 * @return decoded C value
	 * 
	 * */
	protected short decodeU8(byte b) {
		//TODO: Alex
		String s = "";
		s += (b & 0xFF);
		
		return Short.parseShort(s);
	}
	
	/**
	 * decoder for singned byte from C ---> <tt>Null-Function</tt>
	 * 
	 * @param b - hand over byte data to convert 
	 * 
	 * @return decoded C value 
	 * 
	 * */
	protected short decodeS8(byte b) {
		String s = "";
		s += (b);
		
		return Short.parseShort(s);
	}
	
	/**
	 * decoder for unsigned short from C
	 * 
	 * @param bytes - hand over short data to convert 
	 * 
	 * @return decoded C value to java format
	 * 
	 * */
	protected short decodeU16(byte[] bytes) 
	{	//TODO: Alex 
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(bytes[0]);
		bb.put(bytes[1]);
		short shortVal = bb.getShort(0);
		
		return (short)(shortVal & 0x00FF);
	}
	
	/**
	 * decoder for signed short from C --> <tt>Null-Function</tt>
	 * 
	 * @param bytes - hand over byte data for C short value
	 * 
	 *  @return decoded short value into java format
	 * 
	 * */
	protected short decodeS16(byte[] bytes) {
		//TODO: Alex
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(bytes[0]);
		bb.put(bytes[1]);
		short shortVal = bb.getShort(0);
		
		return shortVal;
	}
	
	/**
	 * decoder for signed integer value from C --> <tt>Null-Function</tt>
	 * 
	 * @param bytes - hand over data bytes from C integer value 
	 * 
	 * @return decoded integer value into java format
	 * 
	 * */
	protected int decodeS32(byte[] bytes) {
		//TODO: Alex
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(bytes[0]);
		bb.put(bytes[1]);
		bb.put(bytes[2]);
		bb.put(bytes[3]);
		int intVal = bb.getInt(0);
		
		return intVal;
	}
	
	/**
	 * test function for decoding data
	 * 
	 * @param arr - data to decoding process 
	 * 
	 * @return decoded data to show
	 * 
	 * */
    public String arrayDump(byte[] arr) {
    	
    	//TODO: @Alex should be remove if no using ?!
    	String s = "";
    	for (byte b : arr) {
    		s += "[" + (b & 0xFF) + "]";
    	}
    	return s;
    }
    
    /**
     * test  function for decoding data 
     * 
     * @param arr - data to decoding process 
     * 
     * @return decoded data to show
     * 
     * */
    public String arrayDumpS(byte[] arr) {
    	//TODO: @Alex should be remove if no using ?!
    	String s = "";
    	for (byte b : arr) {
    		s += "[" + (char)(b & 0xFF) + "]";
    	}
    	return s;
    }
}

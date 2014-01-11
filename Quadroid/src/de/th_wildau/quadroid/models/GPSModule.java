package de.th_wildau.quadroid.models;

import purejavacomm.SerialPort;
import de.th_wildau.quadroid.interfaces.IDevice;

public class GPSModule implements IDevice{

	@Override
	public String getPort() {
		// TODO Auto-generated method stub
		return "tty.usbmodem1411";
	}

	@Override
	public String getDeviceName() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public int getBaud() {
		// TODO Auto-generated method stub
		return 9600;
	}

	@Override
	public int getParity() {
		// TODO Auto-generated method stub
		return SerialPort.PARITY_NONE;
	}

	@Override
	public int getDatabits() {
		// TODO Auto-generated method stub
		return SerialPort.DATABITS_8;
	}

	@Override
	public int getStopBits() {
		// TODO Auto-generated method stub
		return SerialPort.STOPBITS_1;
	}

}

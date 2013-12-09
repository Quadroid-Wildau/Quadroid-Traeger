package de.th_wildau.quadroid.interfaces;

/**
 * this function is called if new data are available
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 28.11.2013 (JDK 7) 
 * 
 * */

public interface IDataReceiver 
{
	/**
	 * this method is called if new data are available
	 * 
	 * @param data - received data as byte-array for
	 * selection see {@link de.th_wildau.quadroid.enums.Marker} 
	 * 
	 * */
	public void dataInputNotyfier(byte[] data);
}

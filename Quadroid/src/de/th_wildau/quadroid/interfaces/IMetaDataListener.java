package de.th_wildau.quadroid.interfaces;

import de.th_wildau.quadroid.models.MetaData;

/**
 * this function is called if new {@link MetaData} are available
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 28.11.2013 (JDK 7) 
 * 
 * */

public interface IMetaDataListener 
{
	/**
	 * this method is called if new {@link MetaData} are available
	 * 
	 * @param data - received data as {@link MetaData}
	 * 
	 * */
	public void dataInputNotyfier(MetaData data);
}
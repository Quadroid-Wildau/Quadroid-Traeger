package de.th_wildau.quadroid.interfaces;

import de.th_wildau.quadroid.models.RxData;

/**
 * this listener notify all observer if new data available
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 15.12.2013, (JDK 7)
 * 
 * */

public interface IRxListener 
{
	
	/**
	 * this method is called if new data available 
	 * 
	 * @param data - hand over all received models
	 * 
	 * */
	public void rx(RxData data);
}

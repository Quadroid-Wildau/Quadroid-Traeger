package de.th_wildau.quadroid.handler;

import java.util.ArrayList;
import java.util.List;

import de.th_wildau.quadroid.interfaces.IRxListener;
import de.th_wildau.quadroid.models.RxData;

/**
 * the notification handler informed all registered observer about new data
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 15.12.2013, (JDK 7)
 * */

public class ObserverHandler 
{
	/**list for notification all registred observer will informed*/
	private List<IRxListener> notificationlist = null; 
	
	/**reference of this class for singleton*/
	private static ObserverHandler oh = null;
	
	/**no public constructor
	 * for singleton*/
	private ObserverHandler()
	{
		this.notificationlist = new ArrayList<IRxListener>();
		oh = this;
	}
	
	/**
	 * Singleton Constructor
	 * 
	 * @return get an reference of this class
	 * 
	 * */
	public static ObserverHandler getReference()
	{
		if(oh == null)
			new ObserverHandler();
		return oh;
	}
	
	/**
	 * this function register all observer they will
	 * informed if new data available
	 * 
	 * @param observer - hand over an reference of {@link IRxListener}
	 * to register the observer
	 * 
	 * */
	public void register(IRxListener observer)
	{	//aboard if unusable reference
		if(observer == null)
			return;
		//add to list
		this.notificationlist.add(observer);
	}
	
	/**
	 * this function remove an observer from list this
	 * observer is nor informed at next event
	 * 
	 * @param removeobserver - hand over an registered observer to remove
	 * 
	 * */
	public void removeFromObserverList(IRxListener removeobserver)
	{	//prove if available
		if(this.notificationlist.contains(removeobserver))
			this.notificationlist.remove(removeobserver);
	}
	
	/**
	 * this method informed all registered observer in list
	 * about new data
	 * 
	 * @param data - hand over an reference of {@link RxData}
	 * to send notification data
	 *  
	 * */
	public void notification(RxData data)
	{	//notification all observer in list
		for(IRxListener l : this.notificationlist)
		{
			l.rx(data);
		}
	}
	
}

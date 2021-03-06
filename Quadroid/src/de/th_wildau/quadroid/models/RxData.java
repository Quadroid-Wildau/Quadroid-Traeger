package de.th_wildau.quadroid.models;

import java.util.ArrayList;
import java.util.List;

/**
 * this class contains all receiving models 
 * in connection with IRxListener all observer will
 * informed and obtain this model container
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 15.12.2013, (JDK 7)
 * 
 * */

public class RxData 
{
	/**
	 * public constructor
	 * initial all lists
	 * */
	public RxData() 
	{
		this.airplanelist = new ArrayList<Airplane>();
		this.attitudelist = new ArrayList<Attitude>();
		this.courselist = new ArrayList<Course>();
		this.gnsslist = new ArrayList<GNSS>();
		this.landmarklist = new ArrayList<Landmark>();
		this.metadatalist = new ArrayList<MetaData>();
		this.waypointlist = new ArrayList<Waypoint>();
	}
	
	private List<Attitude> attitudelist = null;
	private List<Course> courselist = null;
	private List<GNSS> gnsslist = null;
	private List<Landmark> landmarklist = null;
	private List<MetaData> metadatalist = null;
	private List<Waypoint> waypointlist = null;
	private List<Airplane> airplanelist = null;
	
	
	/**
	 * @return the attitudelist
	 */
	public List<Attitude> getAttitudelist() {
		return attitudelist;
	}
	/**
	 * @param attitudelist the attitudelist to set
	 */
	public void setAttitudelist(List<Attitude> attitudelist) {
		this.attitudelist = attitudelist;
	}
	/**
	 * @return the courselist
	 */
	public List<Course> getCourselist() {
		return courselist;
	}
	/**
	 * @param courselist the courselist to set
	 */
	public void setCourselist(List<Course> courselist) {
		this.courselist = courselist;
	}
	/**
	 * @return the gnsslist
	 */
	public List<GNSS> getGnsslist() {
		return gnsslist;
	}
	/**
	 * @param gnsslist the gnsslist to set
	 */
	public void setGnsslist(List<GNSS> gnsslist) {
		this.gnsslist = gnsslist;
	}
	/**
	 * @return the landmarklist
	 */
	public List<Landmark> getLandmarklist() {
		return landmarklist;
	}
	/**
	 * @param landmarklist the landmarklist to set
	 */
	public void setLandmarklist(List<Landmark> landmarklist) {
		this.landmarklist = landmarklist;
	}
	/**
	 * @return the metadatalist
	 */
	public List<MetaData> getMetadatalist() {
		return metadatalist;
	}
	/**
	 * @param metadatalist the metadatalist to set
	 */
	public void setMetadatalist(List<MetaData> metadatalist) {
		this.metadatalist = metadatalist;
	}
	/**
	 * @return the waypointlist
	 */
	public List<Waypoint> getWaypointlist() {
		return waypointlist;
	}
	/**
	 * @param waypointlist the waypointlist to set
	 */
	public void setWaypointlist(List<Waypoint> waypointlist) {
		this.waypointlist = waypointlist;
	}
	/**
	 * @return the airplanelist
	 */
	public List<Airplane> getAirplanelist() {
		return airplanelist;
	}
	/**
	 * @param airplanelist the airplanelist to set
	 */
	public void setAirplanelist(List<Airplane> airplanelist) {
		this.airplanelist = airplanelist;
	}
	
	
	
}

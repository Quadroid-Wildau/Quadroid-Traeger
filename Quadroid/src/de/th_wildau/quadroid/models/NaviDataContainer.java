package de.th_wildau.quadroid.models;

import java.util.Observable;
import java.util.Vector;

/**
 * Container for MetaData contains all information from Flight-Ctrl
 * 
 * @author Alex
 * @version 1.0, 11.01.2014, (JDK 7)
 * @see Observable
 * 
 * */

public class NaviDataContainer extends Observable 
{
	/**save last update from flight ctrl*/
	private long lastUpdated;
	/**reference of this class for singleton implementation*/
	static NaviDataContainer instance;
	/**save all updates*/
	private Vector<NaviData> naviDatas;
	/**container for gps data*/
	private Vector<GPSPos> currentPositionVector;
	
	/**Singleton Constructor
	 * @return instance of {@link NaviDataContainer}
	 * */
	public static NaviDataContainer getInstance() {
		if(instance == null) {
			instance = new NaviDataContainer();
		}
		
		return instance;
	}

	/**
	 * no public constructor for singleton
	 * init container for navi and gps data
	 * */
	private NaviDataContainer() {
		this.setLastUpdated(0);
		this.naviDatas = new Vector<NaviData>();
		this.currentPositionVector = new Vector<GPSPos>();
	}
	
	/**
	 * function return the last element of NaviData
	 * 
	 * @return the last element of {@link NaviData} from container 
	 * of <b>null</b> if container empty
	 * */
	public NaviData getLastNaviData() 
	{
		if(this.naviDatas.isEmpty())
			return null;
		return this.naviDatas.lastElement();
	}
	
	/**
	 * This function return current {@link MetaData} from Flight-Ctrl
	 * 
	 * @return last {@link MetaData} update from Flight-Ctrl 
	 * */
	public MetaData getLastMetaData() {
		MetaData metaData = new MetaData();
		metaData.setAttitude(this.getLastAttitude());
		metaData.setAirplane(this.getLastAirplane());
		metaData.setCourse(this.getLastCourse());
		
		return metaData;
	}
	
	
	/**
	 * This function return the current {@link Course}
	 * 
	 *  @return the last update of {@link Course} data from Flight-Ctrl
	 * */
	
	public Course getLastCourse() {
		Course course = new Course();
		course.setAngleReference(this.getLastNaviData().getCompassHeading());
		course.setSpeed(this.getLastNaviData().getGroundSpeed());
		
		return course;
	}
	
	/**
	 * This function return current {@link Airplane} data
	 * 
	 * @return the last update of {@link Airplane} data from Flight-Ctrl
	 * 
	 * */
	
	public Airplane getLastAirplane() {
		Airplane airplane = new Airplane();
		airplane.setGeoData(this.getLastGNSS());
		airplane.setBatteryState(this.getLastNaviData().getuBat());
		
		if(this.getCurrentPosition() != null) {
			airplane.setTime(this.getCurrentPosition().getTime());
		}
		
		return airplane;
	}
	
	/**
	 * This function return current {@link GNSS} data
	 * 
	 * @return the last update of {@link GNSS} data from Flight-Ctrl
	 * 
	 * */
	
	public GNSS getLastGNSS() {
		GNSS gnss = new GNSS();
		gnss.setLongitude(this.getLastNaviData().getCurrentPosition().getLongitude());
		gnss.setLatitude(this.getLastNaviData().getCurrentPosition().getLatitude());
		gnss.setHeight(this.getLastNaviData().getCurrentPosition().getAltitude());
		
		return gnss;
	}
	
	/**
	 * This function return current {@link Attitude} data
	 * 
	 * @return the last update of {@link Attitude} data from Flight-Ctrl
	 * 
	 * */
	
	public Attitude getLastAttitude() {
		Attitude attitude = new Attitude();
		attitude.setYaw(this.getLastNaviData().getCompassHeading());
		attitude.setRoll(this.getLastNaviData().getAngleRoll());
		attitude.setPitch(this.getLastNaviData().getAngleNick());
		
		return attitude;
	}
	
	/**
	 * This function return the last {@link GPSPos} 
	 * 
	 * @return the last GPSPos or <b>null</b> if container are empty
	 * 
	 * */
	
	public GPSPos getCurrentPosition() {
		if(currentPositionVector.size() > 0) {
			return currentPositionVector.lastElement();
		} else {
			return null;
		}
	}
	
	
	/**
	 * This function set new {@link NaviData} and informed all observer
	 * 
	 * @param naviData - hand over an new instance of {@link NaviData}
	 * 
	 * */
	
	synchronized public void addNaviData(NaviData naviData) {
		if(this.getCurrentPosition() != null) {
			naviData.setCurrentPosition(this.getCurrentPosition());
		}
		
		this.naviDatas.add(naviData);
		this.setLastUpdated(System.currentTimeMillis());
		this.hasChanged();
		this.notifyObservers();
	}
	
	
	/**
	 * this function put an new {@link GPSPos} to container
	 * 
	 * @param position - hand over an {@link GPSPos} to set
	 * 
	 * */
	
	synchronized public void addGPSPosition(GPSPos position) {
		this.currentPositionVector.add(position);
	}
	
	/**
	 * this function return the last update time
	 * 
	 * @param return timestamp of last update
	 * 
	 * */

	synchronized public long getLastUpdated() {
		return lastUpdated;
	}
	
	/**
	 * This method set the last update time 
	 * 
	 * @param lastUpdated - hand over an timestamp 
	 * 
	 * */

	synchronized public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	
}

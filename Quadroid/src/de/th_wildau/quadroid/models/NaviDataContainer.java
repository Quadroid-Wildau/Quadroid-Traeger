package de.th_wildau.quadroid.models;

import java.util.Observable;
import java.util.Vector;

public class NaviDataContainer extends Observable {
	private long lastUpdated;
	static NaviDataContainer instance;
	private Vector<NaviData> naviDatas;
	
	public static NaviDataContainer getInstance() {
		if(instance == null) {
			instance = new NaviDataContainer();
		}
		
		return instance;
	}

	private NaviDataContainer() {
		this.setLastUpdated(0);
		this.naviDatas = new Vector<NaviData>();
	}
	
	public NaviData getLastNaviData() {
		return this.naviDatas.lastElement();
	}
	
	public MetaData getLastMetaData() {
		MetaData metaData = new MetaData();
		metaData.setAttitude(this.getLastAttitude());
		metaData.setAirplane(this.getLastAirplane());
		metaData.setCourse(this.getLastCourse());
		
		return metaData;
	}
	
	public Course getLastCourse() {
		Course course = new Course();
		course.setAngleReference(this.getLastNaviData().getCompassHeading());
		course.setSpeed(this.getLastNaviData().getGroundSpeed());
		
		return course;
	}
	
	public Airplane getLastAirplane() {
		Airplane airplane = new Airplane();
		airplane.setGeoData(this.getLastGNSS());
		airplane.setBatteryState(this.getLastNaviData().getuBat());
		
		return airplane;
	}
	
	public GNSS getLastGNSS() {
		GNSS gnss = new GNSS();
		gnss.setLongitude(this.getLastNaviData().getCurrentPosition().getLongitude());
		gnss.setLatitude(this.getLastNaviData().getCurrentPosition().getLatitude());
		gnss.setHeight(this.getLastNaviData().getCurrentPosition().getAltitude());
		
		return gnss;
	}
	
	public Attitude getLastAttitude() {
		Attitude attitude = new Attitude();
		attitude.setRoll(this.getLastNaviData().getAngleRoll());
		attitude.setPitch(this.getLastNaviData().getAngleNick());
		
		return attitude;
	}
	
	synchronized public void addNaviData(NaviData naviData) {
		this.naviDatas.add(naviData);
		this.setLastUpdated(System.currentTimeMillis());
		this.hasChanged();
		this.notifyObservers();
	}

	synchronized public long getLastUpdated() {
		return lastUpdated;
	}

	synchronized public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}

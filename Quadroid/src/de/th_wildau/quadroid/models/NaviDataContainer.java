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
		return new MetaData();
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

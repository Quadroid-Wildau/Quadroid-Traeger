package de.th_wildau.quadroid.models;

/**
 * define the waypoint model it contains an position and has the same 
 * options how {@link Landmark}
 * 
 * 
 * @author Stefan ...
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.1, 11.12.2013, (JDK 7)
 * @see Landmark
 * */

public class Waypoint extends Landmark{

	/**save an position of type {@link GeoData}*/
	private GeoData sollPos;

	/**
	 * Getter Function
	 * 
	 * @return get an Position of type {@link GeoData}
	 * */
	public GeoData getPosition() {
		return sollPos;
	}

	/**
	 * Setter Method
	 * 
	 * @param sollPos set an new position
	 * */
	public void setPosition(GeoData sollPos) {
		this.sollPos = sollPos;
	}
}

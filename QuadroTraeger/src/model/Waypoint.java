package model;

public class Waypoint extends Landmark{

	
	/**
	 * Zus�tzliches Attribut:
	 * Sollposition des Wegpunkts.
	 */
	private GeoData sollPos;

	public GeoData getSollPos() {
		return sollPos;
	}

	public void setSollPos(GeoData sollPos) {
		this.sollPos = sollPos;
	}
}

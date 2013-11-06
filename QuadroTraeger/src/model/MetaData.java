package model;

public class MetaData {
	
	/**
	 * Geoposition
	 */
	private GeoData gdata;
	
	/**
	 * Zeitpunkt
	 */
	private long time;
	
	/**
	 * Drehwinkel um die z-Achse (vertikale Achse)
	 */
	private float yaw;
	
	/**
	 * Drehwinkel und die x-Achse (horizontale Achse in Bewegungsrichtung)
	 */
	private float roll;
	
	/**
	 * Drehwinkel um die y-Achse (horizontale Achse quer zur Bewegungsrichtung)
	 */
	private float pitch;
	
	/**
	 * Ladezustand der Batterie (0-100%)
	 */
	private byte batteryState;
	
	/**
	 * aktuelle Geschwindigkeit
	 */
	private float speed;
	
	/**
	 * Aktueller Kurs des Quadrokopters in Grad
	 * bezogen zu magnetisch Nord.
	 */
	private float course;

	public GeoData getGdata() {
		return gdata;
	}

	public void setGdata(GeoData gdata) {
		this.gdata = gdata;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public byte getBatteryState() {
		return batteryState;
	}

	public void setBatteryState(byte batteryState) {
		this.batteryState = batteryState;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getCourse() {
		return course;
	}

	public void setCourse(float course) {
		this.course = course;
	}

}

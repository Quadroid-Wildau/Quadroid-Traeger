package de.th_wildau.quadroid.models;

/**
 * 
 * Data-Model for Flight-Ctrl data
 * 
 * 
 * @author Alex
 * @version 1.0, 11.01.2014, (JDK 7)
 * 
 * 
 * 
 * */


public class NaviData 
{
	/**save protocol version of incoming data*/
	private short version;
	/**save current position */
	private GPSPos currentPosition;		// see ubx.h for details
	/**save target position*/
	private GPSPos targetPosition;
	/**deviation of target position*/
	private GPSPosDev targetPositionDeviation;
	/**save the home position deviation*/
	private GPSPosDev homePositionDeviation;
	/**save the home position*/
	private GPSPos homePosition;
	/**save index of waypoint*/
	private short waypointIndex;
	/**save number of waypoint*/
	private short waypointNumber;
	/**number of using satellites*/
	private short satsInUse;
	/**current altimeter*/
	private short altimeter;
	/**save variometer data*/
	private short variometer;
	/**save flying time of quadroid*/
	private short flyingTime;
	/**battery voltage: */
	private short uBat;
	/**save current speed*/
	private short groundSpeed;
	/**save direction*/
	private short heading;
	/**save compass heading (direction to N reference)*/
	private short compassHeading;
	/**save angle of nick for airplane*/
	private short angleNick;
	/**save angel of roll for airplane*/
	private short angleRoll;
	/**connection quality of the remote control: ?*/
	private short rcQuality;
	/**save Flight-Ctrl state flags*/
	private short fCStatusFlags;
	/**save Navi-Ctrl flags*/
	private short nCFlags;
	/**save errorcode from Flight-Ctrl*/
	private short errorcode;
	/**save radius of operation*/
	private short operatingRadius;
	/**save max speed value*/
	private short topSpeed;
	/**save an target hold time*/
	private short targetHoldTime;
	/**save second Flight-Ctrl state flag*/
	private short fCStatusFlags2;
	/**save an specific altitude point*/
	private short setpointAltitude;
	/**save current gas of airplane*/
	private short gas;
	/**current used amper: ???*/
	private short current;
	/**used voltage: ???*/
	private short usedCapacity;
	
	
	/**
	 * Getter Function
	 * 
	 * @return get curretn {@link GPSPosDev}
	 * 
	 * */
	public GPSPosDev getTargetPositionDeviation() {
		return targetPositionDeviation;
	}
	
	
	/**
	 * 
	 * Setter Method
	 * 
	 * @param targetPositionDeviation - hand over an new deviation to target position
	 * 
	 * */
	public void setTargetPositionDeviation(GPSPosDev targetPositionDeviation) {
		this.targetPositionDeviation = targetPositionDeviation;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get current deviation of home position
	 * 
	 * 
	 * */
	public GPSPosDev getHomePositionDeviation() {
		return homePositionDeviation;
	}
	
	
	/**
	 * Setter Method 
	 * 
	 * @param homePositionDeviation - hand over an new deviation for home position
	 * 
	 * */
	public void setHomePositionDeviation(GPSPosDev homePositionDeviation) {
		this.homePositionDeviation = homePositionDeviation;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get used flight-ctrl protocol version
	 * 
	 * */
	public short getVersion() {
		return version;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param version - hand over an new version of flight ctrl protocol
	 * 
	 * */
	public void setVersion(short version) {
		this.version = version;
	}
	
	
	/**
	 * Getter Function 
	 * 
	 * @return get index of current waypoint
	 * 
	 * */
	public short getWaypointIndex() {
		return waypointIndex;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param waypointIndex - hand over an new index of waypoint
	 * 
	 * */
	public void setWaypointIndex(short waypointIndex) {
		this.waypointIndex = waypointIndex;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get number of waypoint
	 * 
	 * 
	 * */
	public short getWaypointNumber() {
		return waypointNumber;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param waypointNumber - hand over an new number of waypoint
	 * 
	 * */
	public void setWaypointNumber(short waypointNumber) {
		this.waypointNumber = waypointNumber;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get using satellites  
	 * 
	 * 
	 * */
	public short getSatsInUse() {
		return satsInUse;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param satsInUse - hand over an new number of using satellites
	 * 
	 * */
	public void setSatsInUse(short satsInUse) {
		this.satsInUse = satsInUse;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get current altimeter
	 * 
	 * */
	public short getAltimeter() {
		return altimeter;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param altimeter - hand over an new altimeter value
	 * 
	 * */
	public void setAltimeter(short altimeter) {
		this.altimeter = altimeter;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get veriometer value 
	 * 
	 * 
	 * */
	public short getVariometer() {
		return variometer;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param variometer - hand over an new variometer value
	 * 
	 * */
	public void setVariometer(short variometer) {
		this.variometer = variometer;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get flying time for airplane
	 * 
	 * */
	public short getFlyingTime() {
		return flyingTime;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param flyingTime - hand over an new flying time 
	 * 
	 * */
	public void setFlyingTime(short flyingTime) {
		this.flyingTime = flyingTime;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * voltage of the battery
	 * 
	 * */
	public short getuBat() {
		return uBat;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param uBat - hand over an voltage value of the battery
	 * 
	 * 
	 * */
	public void setuBat(short uBat) {
		this.uBat = uBat;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get current speed over ground
	 * 
	 * 
	 * */
	public short getGroundSpeed() {
		return groundSpeed;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param groundSpeed - set new speed value over ground
	 * 
	 * */
	public void setGroundSpeed(short groundSpeed) {
		this.groundSpeed = groundSpeed;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return direction
	 * 
	 * */
	public short getHeading() {
		return heading;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param heading - hand over an new direction
	 * 
	 * 
	 * */
	public void setHeading(short heading) {
		this.heading = heading;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return compass heading (direction to N reference) 
	 * 
	 * */
	public short getCompassHeading() {
		return compassHeading;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param compassHeading - set an new compass heading (direction to N reference)
	 * 
	 * */
	public void setCompassHeading(short compassHeading) {
		this.compassHeading = compassHeading;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * 
	 * @return get angle for nick 
	 * 
	 * */
	public short getAngleNick() {
		return angleNick;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param angleNick - set an new angle for nick 
	 * 
	 * */
	public void setAngleNick(short angleNick) {
		this.angleNick = angleNick;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get angle for roll 
	 * 
	 * */
	public short getAngleRoll() {
		return angleRoll;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param angleRoll - set new value for roll
	 * 
	 * */
	public void setAngleRoll(short angleRoll) {
		this.angleRoll = angleRoll;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return remote control signal quality
	 * 
	 * */
	public short getRcQuality() {
		return rcQuality;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * 
	 * @param rC_Quality - set an new value for remote control signal quality
	 * 
	 * */
	public void setRcQuality(short rC_Quality) {
		this.rcQuality = rC_Quality;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get current Flight-Ctrl state
	 * 
	 * */
	public short getfCStatusFlags() {
		return fCStatusFlags;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param fCStatusFlags - set an new state for Flight-Ctrl
	 * 
	 * */
	public void setfCStatusFlags(short fCStatusFlags) {
		this.fCStatusFlags = fCStatusFlags;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get flags for Navi-Ctrl
	 * 
	 * 
	 * */
	public short getnCFlags() {
		return nCFlags;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param nCFlags - set new flags for Navi-Ctrl
	 * 
	 * 
	 * */
	public void setnCFlags(short nCFlags) {
		this.nCFlags = nCFlags;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get an error code
	 * 
	 * */
	public short getErrorcode() {
		return errorcode;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param errorcode - set error code
	 * 
	 * */
	public void setErrorcode(short errorcode) {
		this.errorcode = errorcode;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get radius of operation
	 * 
	 * 
	 * */
	public short getOperatingRadius() {
		return operatingRadius;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param operatingRadius - set an new radius of operation
	 * 
	 * */
	public void setOperatingRadius(short operatingRadius) {
		this.operatingRadius = operatingRadius;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get max flying speed
	 * 
	 * */
	public short getTopSpeed() {
		return topSpeed;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param topSpeed - set an new flying speed
	 * 
	 * 
	 * */
	public void setTopSpeed(short topSpeed) {
		this.topSpeed = topSpeed;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get target hold time
	 * 
	 * */
	public short getTargetHoldTime() {
		return targetHoldTime;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param targetHoldTime - set an new time for target hold 
	 * 
	 * */
	public void setTargetHoldTime(short targetHoldTime) {
		this.targetHoldTime = targetHoldTime;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get second flag for Flight-Ctrl
	 * 
	 * */
	public short getfCStatusFlags2() {
		return fCStatusFlags2;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param fCStatusFlags2 - set new flags for Flight-Ctrl
	 * 
	 * 
	 * */
	public void setfCStatusFlags2(short fCStatusFlags2) {
		this.fCStatusFlags2 = fCStatusFlags2;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get point for altitude
	 * 
	 * */
	public short getSetpointAltitude() {
		return setpointAltitude;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param setpointAltitude - set an new point for altitude
	 * 
	 * 
	 * */
	public void setSetpointAltitude(short setpointAltitude) {
		this.setpointAltitude = setpointAltitude;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get current gas level
	 * 
	 * */
	public short getGas() {
		return gas;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param gas - set new gas level
	 * 
	 * */
	public void setGas(short gas) {
		this.gas = gas;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get current used voltage
	 * 
	 * */
	public short getCurrent() {
		return current;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * set current used voltage
	 * 
	 * */
	public void setCurrent(short current) {
		this.current = current;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get used ampere
	 * 
	 * */
	public short getUsedCapacity() {
		return usedCapacity;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * set used ampere
	 * 
	 * */
	public void setUsedCapacity(short usedCapacity) {
		this.usedCapacity = usedCapacity;
	}
	
	
	
	/**
	 * Getter Function
	 * 
	 * @return get the current {@link GPSPos} position 
	 * 
	 * */
	public GPSPos getCurrentPosition() {
		return currentPosition;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param currentPosition - set an new position 
	 * 
	 * */
	public void setCurrentPosition(GPSPos currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * 
	 * @return get target position
	 * 
	 * */
	public GPSPos getTargetPosition() {
		return targetPosition;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param targetPosition - set an new target position
	 * 
	 * */
	public void setTargetPosition(GPSPos targetPosition) {
		this.targetPosition = targetPosition;
	}
	
	
	/**
	 * Getter Function
	 * 
	 * @return get home position
	 * 
	 * */
	public GPSPos getHomePosition() {
		return homePosition;
	}
	
	
	/**
	 * Setter Method
	 * 
	 * @param homePosition - set an new home position
	 * 
	 * */
	public void setHomePosition(GPSPos homePosition) {
		this.homePosition = homePosition;
	}
	
	
}

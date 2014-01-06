package de.th_wildau.quadroid.models;

public class NaviData {
	private short version;
	private GPSPos currentPosition;		// see ubx.h for details
	private GPSPos targetPosition;
	private GPSPosDev targetPositionDeviation;
	private GPSPosDev homePositionDeviation;
	private GPSPos homePosition;
	private short waypointIndex;
	private short waypointNumber;
	private short satsInUse;
	private short altimeter;
	private short variometer;
	private short flyingTime;
	private short uBat;
	private short groundSpeed;
	private short heading;
	private short compassHeading;
	private short angleNick;
	private short angleRoll;
	private short rcQuality;
	private short fCStatusFlags;
	private short nCFlags;
	private short errorcode;
	private short operatingRadius;
	private short topSpeed;
	private short targetHoldTime;
	private short fCStatusFlags2;
	private short setpointAltitude;
	private short gas;
	private short current;
	private short usedCapacity;
	
	public GPSPosDev getTargetPositionDeviation() {
		return targetPositionDeviation;
	}
	public void setTargetPositionDeviation(GPSPosDev targetPositionDeviation) {
		this.targetPositionDeviation = targetPositionDeviation;
	}
	public GPSPosDev getHomePositionDeviation() {
		return homePositionDeviation;
	}
	public void setHomePositionDeviation(GPSPosDev homePositionDeviation) {
		this.homePositionDeviation = homePositionDeviation;
	}
	public short getVersion() {
		return version;
	}
	public void setVersion(short version) {
		this.version = version;
	}
	public short getWaypointIndex() {
		return waypointIndex;
	}
	public void setWaypointIndex(short waypointIndex) {
		this.waypointIndex = waypointIndex;
	}
	public short getWaypointNumber() {
		return waypointNumber;
	}
	public void setWaypointNumber(short waypointNumber) {
		this.waypointNumber = waypointNumber;
	}
	public short getSatsInUse() {
		return satsInUse;
	}
	public void setSatsInUse(short satsInUse) {
		this.satsInUse = satsInUse;
	}
	public short getAltimeter() {
		return altimeter;
	}
	public void setAltimeter(short altimeter) {
		this.altimeter = altimeter;
	}
	public short getVariometer() {
		return variometer;
	}
	public void setVariometer(short variometer) {
		this.variometer = variometer;
	}
	public short getFlyingTime() {
		return flyingTime;
	}
	public void setFlyingTime(short flyingTime) {
		this.flyingTime = flyingTime;
	}
	public short getuBat() {
		return uBat;
	}
	public void setuBat(short uBat) {
		this.uBat = uBat;
	}
	public short getGroundSpeed() {
		return groundSpeed;
	}
	public void setGroundSpeed(short groundSpeed) {
		this.groundSpeed = groundSpeed;
	}
	public short getHeading() {
		return heading;
	}
	public void setHeading(short heading) {
		this.heading = heading;
	}
	public short getCompassHeading() {
		return compassHeading;
	}
	public void setCompassHeading(short compassHeading) {
		this.compassHeading = compassHeading;
	}
	public short getAngleNick() {
		return angleNick;
	}
	public void setAngleNick(short angleNick) {
		this.angleNick = angleNick;
	}
	public short getAngleRoll() {
		return angleRoll;
	}
	public void setAngleRoll(short angleRoll) {
		this.angleRoll = angleRoll;
	}
	public short getRcQuality() {
		return rcQuality;
	}
	public void setRcQuality(short rC_Quality) {
		this.rcQuality = rC_Quality;
	}
	public short getfCStatusFlags() {
		return fCStatusFlags;
	}
	public void setfCStatusFlags(short fCStatusFlags) {
		this.fCStatusFlags = fCStatusFlags;
	}
	public short getnCFlags() {
		return nCFlags;
	}
	public void setnCFlags(short nCFlags) {
		this.nCFlags = nCFlags;
	}
	public short getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(short errorcode) {
		this.errorcode = errorcode;
	}
	public short getOperatingRadius() {
		return operatingRadius;
	}
	public void setOperatingRadius(short operatingRadius) {
		this.operatingRadius = operatingRadius;
	}
	public short getTopSpeed() {
		return topSpeed;
	}
	public void setTopSpeed(short topSpeed) {
		this.topSpeed = topSpeed;
	}
	public short getTargetHoldTime() {
		return targetHoldTime;
	}
	public void setTargetHoldTime(short targetHoldTime) {
		this.targetHoldTime = targetHoldTime;
	}
	public short getfCStatusFlags2() {
		return fCStatusFlags2;
	}
	public void setfCStatusFlags2(short fCStatusFlags2) {
		this.fCStatusFlags2 = fCStatusFlags2;
	}
	public short getSetpointAltitude() {
		return setpointAltitude;
	}
	public void setSetpointAltitude(short setpointAltitude) {
		this.setpointAltitude = setpointAltitude;
	}
	public short getGas() {
		return gas;
	}
	public void setGas(short gas) {
		this.gas = gas;
	}
	public short getCurrent() {
		return current;
	}
	public void setCurrent(short current) {
		this.current = current;
	}
	public short getUsedCapacity() {
		return usedCapacity;
	}
	public void setUsedCapacity(short usedCapacity) {
		this.usedCapacity = usedCapacity;
	}
	public GPSPos getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(GPSPos currentPosition) {
		this.currentPosition = currentPosition;
	}
	public GPSPos getTargetPosition() {
		return targetPosition;
	}
	public void setTargetPosition(GPSPos targetPosition) {
		this.targetPosition = targetPosition;
	}
	public GPSPos getHomePosition() {
		return homePosition;
	}
	public void setHomePosition(GPSPos homePosition) {
		this.homePosition = homePosition;
	}
}

package de.th_wildau.quadroid.enums;

/**
 * This enum contains marker for transmissions
 * <ul>
 * <li>Picture bytes</li>
 * <li>GNSS position</li>
 * <li>Gyro data</li>
 * <li>Height data</li>
 * <li>Akku state</li>
 * <li>Time value</li>
 * <li>Speed value</li>
 * <li>Course data</li>
 * <li>CRC data</li>
 * <li></li>
 * <li></li>
 * <li></li>
 * 
 * </ul>
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 27.11.2013 (JDK 7) 
 * 
 * */

public enum Marker 
{
	/**marker ||> for picture values*/
	PICTURESTART("||>"), 
	/**marker < || for picture values*/
	PICTUREEND("<||"),
	/**marker ##> for GNSS data*/
	GNSSSTART("##>"), 
	/**marker < ## for GNSS data*/
	GNSSEND("<##"),
	/**marker //> for gyro data*/
	ATTITUDESTART("//>"), 
	/**marker < // for gyro data*/
	ATTITUDEEND("<//"),
	/**marker ??> for height data*/
	//HEIGHTSTART("??>"), 
	/**marker < ?? for height data*/
	//HEIGHTEND("<??"),
	/**marker !!> for Akku data*/
	//AKKUSTART("!!>"), 
	/**marker < !! for Akku data*/
	//AKKUEND("<!!"),
	/**marker ;;> for time value*/
	//TIMESTART(";;>"), 
	/**marker < ;; for time value*/
	//TIMEEND("<;;"),
	/**marker ::> for speed value */
	//SPEEDSTART("::>"), 
	/**marker < :: for speed value*/
	//SPEEDEND("<::"),
	/**marker &&> for course value*/
	COURSESTART("&&>"), 
	/**marker < && for course value*/
	COURSEEND("<&&"),
	/**marker $$> for crc32 value */
	CRCSTART("$$>"), 
	/**marker < $$ for crc32 value */
	CRCEND("<$$"),
	/**marker < Y> for yaw data*/
	YAWSTART("<Y>"),
	/**marker < /Y> for yaw data*/
	YAWEND("</Y"),
	/**marker < R> for roll data*/
	ROLLSTART("<R>"),
	/**marker < /R for roll data*/
	ROLLEND("</R"),
	/**marker < P> for pitch data*/
	PITCHSTART("<P>"),
	/**marker < /P for pitch data*/
	PITCHEND("</P"),
	/**marker < A> for angel data*/
	ANGLESTART("<A>"),
	/**marker < /A for angle data */
	ANGELEND("</A"),
	/**marker < S> for speed data*/
	SPEEDSTART("<S>"),
	/**marker < /S* for speed data*/
	SPEEDEND("</S"),
	/**marker < LA> for latitude data*/
	LATITUDESTART("<LA>"),
	/**marker < /LA for latitude data*/
	LATITUDEEND("</LA"),
	/**marker < LO> for longitude data*/
	LONGITUDESTART("<LO>"),
	/**marker < /LO for longitude data*/
	LONGITUDEEND("</LO"),
	/**marker < H> for height data*/
	HEIGHTSTART("<H>"),
	/**marker < /H for height data*/
	HEIGHTEND("</H"),
	/**marker < L> for landmark data*/
	LANDMARKSTART("<L>"),
	/**marker < /L for landmark data*/
	LANDMARKEND("</L"),
	/**marker < M> for meta data */
	METADATASTART("<M>"),
	/**marker < /M for meta data */
	METADATAEND("</M"),
	/**marker < Q> for airplane data*/
	AIRPLANESTART("<Q>"),
	/**marker < /Q for airplane data*/
	AIRPLANEEND("</Q"),
	/**marker < T> for time data*/
	TIMESTART("<T>"),
	/**marker < /T for time data*/
	TIMEEND("</T"),
	/**marker < AK> for akku data*/
	AKKUSTART("<AK>"),
	/**marker < /AK for akku data*/
	AKKUEND("</AK"),
	/**marker < W> for waypoint data*/
	WAYPOINTSTART("<W>"),
	/**marker < /W for waypoint data*/
	WAYPOINTEND("</W"),
	/**standard image type for transmit und receiving*/
	IMAGETYPE("jpg");
	
	/**save marker*/
	private String val = null;
	
	/**
	 * invisible constructor 
	 * */
	private Marker(String value)
	{
		this.val = value;
	}

	/**
	 * Getter Function
	 * 
	 * @return marker for images 
	 * */
	public String getMarker()
	{
		return this.val;
	}
}

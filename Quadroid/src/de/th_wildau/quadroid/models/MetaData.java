package de.th_wildau.quadroid.models;

/**
 * this model define data from Flight-Ctrl
 * 
 * @author Stefan ...
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 11.12.2013, (JDK 7)
 * 
 * */

public class MetaData {
	
	/**save the attitude of airplane*/
	private Attitude attitude = null;
	
	/**save current course of airplane*/
	private Course course = null;
	
	/**save all relevant data to our quadroid*/
	private Airplane airplane = null;

	/**
	 * Getter Function
	 * 
	 * @return the attitude of our airplane see {@link Attitude}
	 */
	public Attitude getAttitude() {
		return attitude;
	}

	/**
	 * Setter Method
	 * 
	 * @param attitude set an new attitude for airplane
	 */
	public void setAttitude(Attitude attitude) {
		this.attitude = attitude;
	}

	/**
	 * Getter Function
	 * 
	 * @return the course of our airplane see {@link Course}
	 * 
	 */
	public Course getCourse() {
		return course;
	}

	/**
	 * Setter Method
	 * 
	 * @param course - set an new course for airplane
	 */
	public void setCourse(Course course) {
		this.course = course;
	}

	/**
	 * Getter Function
	 * 
	 * @return information about quadroid airplane see {@link Airplane} 
	 */
	public Airplane getAirplane() {
		return airplane;
	}

	/**
	 * Setter Method
	 * 
	 * @param airplane - set net state of airplane
	 */
	public void setAirplane(Airplane airplane) {
		this.airplane = airplane;
	}
	
	

	@Override
	public String toString() {
		//print all data
		String result = "";
		if(this.course != null)
		result += "Course: " + this.course.toString();
		if(this.airplane != null)
		result += "\nAirplane: " + this.airplane.toString();
		if(this.attitude != null)
		result += "\nAttitude: " + this.attitude.toString();
		return result;
	}




}

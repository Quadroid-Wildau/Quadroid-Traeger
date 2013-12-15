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

	/**save an position of type {@link GNSS}*/
	private GNSS sollPos;

	/**
	 * Getter Function
	 * 
	 * @return get an Position of type {@link GNSS}
	 * */
	public GNSS getPosition() {
		return sollPos;
	}

	/**
	 * Setter Method
	 * 
	 * @param sollPos set an new position
	 * */
	public void setPosition(GNSS sollPos) {
		this.sollPos = sollPos;
	}
	
	@Override
	public String toString() 
	{	//print all informations
		String result = "";
		if(this.sollPos != null)
		result += "Position: "+ this.sollPos.toString() + "\nMetaData: ";
		if(this.getMetaData() != null)
		result += this.getMetaData().toString() + "\n";
		if(this.getPictureoflandmark() != null)
		result += "IMG: " + this.getPictureoflandmark().getWidth() + " " 
		+ this.getPictureoflandmark().getHeight();
		return result;
	}
}

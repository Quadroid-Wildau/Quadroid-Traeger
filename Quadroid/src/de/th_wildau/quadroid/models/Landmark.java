package de.th_wildau.quadroid.models;

import java.awt.image.BufferedImage;

/**
 * this model define an detected landmark 
 * 
 * @author Stefan ...
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 11.12.2013, (JDK 7)
 * 
 * */

public class Landmark {
	
	
	/**save meta data for detected landmark*/
	private MetaData tdata;
	
	/**save an picture of detected landmark */
	private BufferedImage pictureoflandmark;

	/**
	 * Getter Function
	 * 
	 * @return all meta data see {@link MetaData}
	 */
	public MetaData getMetaData() {
		return tdata;
	}

	/**
	 * Setter Method
	 * 
	 * @param tdata set new meta data see {@link MetaData}
	 */
	public void setMetaData(MetaData tdata) {
		this.tdata = tdata;
	}

	/**
	 * Getter Function
	 * 
	 * @return get an {@link BufferedImage} as picture from detected landmark
	 */
	public BufferedImage getPictureoflandmark() {
		return pictureoflandmark;
	}

	/**
	 * Setter Method
	 * 
	 * @param pictureoflandmark - set new {@link BufferedImage} as picture for detected landmark
	 *
	 */
	public void setPictureoflandmark(BufferedImage pictureoflandmark) {
		this.pictureoflandmark = pictureoflandmark;
	}


	@Override
	public String toString() {
		//print all data
		String result = "";
		result += "MetaData: " + this.tdata.toString() + "\nIMG: " +
		this.pictureoflandmark.getWidth() + " " + this.pictureoflandmark.getHeight();
		return result;
	}
	
	
}

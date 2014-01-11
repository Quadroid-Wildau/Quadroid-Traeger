package de.th_wildau.quadroid.landmark;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Mainclass for Landmarkdetection.
 * Contains preprocessing for landmarkdetection
 * @author Stephan Funke
 *
 */
public class MainLandmark{

	
	public MainLandmark(){
		
	}
	
	/**
	 * Starts the landmarkdetection with preprocessing of the Image
	 * @param img
	 * @return boolean - TRUE = landmark detected, FALSE = no landmark is found
	 */
	public boolean checkLandmark(BufferedImage img){
//		//System.loadLibrary("opencv_java245");
//		img = convertSourceImage(img);
//		
//		Raster src = img.getRaster();	//get the Raster from the source image
//		
//		
		OpCheckLandmark checkLM = new OpCheckLandmark();
//		WritableRaster dest = src.createCompatibleWritableRaster();
		
		//Calling the Landmarkdetection
		return checkLM.findLm(img);
	}
	
	/**
	 * Checks the sourceimagetype
	 * If the type is not TYPE_INT_RGB, it will be set
	 * @param src
	 * @return
	 */
	protected BufferedImage convertSourceImage(BufferedImage src) {
		if (src.getType() == BufferedImage.TYPE_INT_RGB)
			return src;
		BufferedImage bufferedImage = new BufferedImage(src.getWidth(null),
				src.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(src, 0, 0, src.getWidth(null),
				src.getHeight(null), null);
		return bufferedImage;
	}
	
	
	 
}
package de.th_wildau.quadroid.landmark;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.googlecode.javacv.cpp.opencv_photo;

/**
 * Mainclass for Landmarkdetection.
 * Contains preprocessing for landmarkdetection
 * @author Stephan Funke
 *
 */
public class MainLandmark extends JFrame {
	private JFrame frame = null; 
	
	public MainLandmark(){
		super("Fenster");
		setSize(300,300);
		setLocation(300,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * Starts the landmarkdetection with preprocessing of the Image
	 * @param img
	 * @return boolean - TRUE = landmark detected, FALSE = no landmark is found
	 */
	public boolean checkLandmark(BufferedImage img){
		
        this.getContentPane().add(new JLabel(new ImageIcon(img)));
		this.repaint();
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
package de.th_wildau.quadroid.landmark;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class MainLandmark implements Runnable{

	
	public MainLandmark(){
		Thread lmthread = new Thread(this);
		lmthread.start();
		
	}
	
	public boolean checkLandmark(BufferedImage img){
		img = convertSourceImage(img);
		
		Raster src = img.getRaster();
		
		OpCheckLandmark checkLM = new OpCheckLandmark();
		WritableRaster dest = src.createCompatibleWritableRaster();
		checkLM.findLm(src, dest);
		
		return false;
	}
	
	
	 protected BufferedImage convertSourceImage(BufferedImage src) {
		  if (src.getType() == BufferedImage.TYPE_BYTE_GRAY)
			  return src;
		  BufferedImage bufferedImage = new BufferedImage(src.getWidth(null),
				  src.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
		  Graphics2D g = bufferedImage.createGraphics();
		  // Bild in das BufferedImage zeichnen
		  g.drawImage(src, 0, 0, src.getWidth(null),
				  src.getHeight(null), null);
		  return bufferedImage;
	  }
	
	
	 
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
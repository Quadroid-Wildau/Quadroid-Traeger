package de.th_wildau.quadroid.landmark;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestLM {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.loadLibrary("opencv_java245");
		BufferedImage test = null;
		
		PropertyConfigurator.configure("log4j.properties");
		
		Logger logger = null;
		logger = LoggerFactory.getLogger(TestLM.class.getName());
		logger.info("Init Logger");
		UsbCamHandler usbcamera = UsbCamHandler.getInstance(logger);
		while(true){
			
			BufferedImage qwer = usbcamera.getImage();
			try {
				ImageIO.write(qwer, "JPG", new File("testqwer.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			File file = new File("test1.jpg"); 
        	try { 
            	test = ImageIO.read(file); 
        	} 	catch (IOException ex) { 
            	ex.printStackTrace(); 
        	} 
        	boolean erg = false;
        	MainLandmark asdf = new MainLandmark();
        	
        	//Testbild von Platte
        	erg = asdf.checkLandmark(test);
        	System.out.println("von Platte "+erg);
        
        	//Testbild von Webcam
        	erg = asdf.checkLandmark(qwer);
        	System.out.println("Webcam "+erg);
        	
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

package de.th_wildau.quadroid.test.landmark;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


import de.th_wildau.quadroid.landmark.MainLandmark;

import junit.framework.TestCase;

public class TestOpCheckLandmark extends TestCase {
	
	
	public void testLMCheck(){
		try {
			System.loadLibrary("opencv_java245");

			BufferedImage positivTest = ImageIO.read( new File( "testKamera.png" ) );//Load Testpicture with landmark
			BufferedImage negativTest = ImageIO.read( new File( "k-DSC_4243.jpg" ) );//Load Testpicture without landmark
			MainLandmark lm = new MainLandmark();
			//Test positive Picture
			assertTrue(lm.checkLandmark(positivTest));
			//Test negative Picture
			assertFalse(lm.checkLandmark(negativTest));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

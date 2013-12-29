package de.th_wildau.quadroid.landmark;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TestLM {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BufferedImage test = null;
		
		File file = new File("test1.jpg"); 
        try { 
            test = ImageIO.read(file); 
        } catch (IOException ex) { 
            ex.printStackTrace(); 
        } 
        
        MainLandmark asdf = new MainLandmark();
        boolean erg = asdf.checkLandmark(test);

        System.out.println(erg);
	}

}

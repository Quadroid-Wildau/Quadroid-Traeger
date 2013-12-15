package de.th_wildau.quadroid;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.th_wildau.quadroid.decoder.RxDataDecoder;
import de.th_wildau.quadroid.encoder.TxDataEncoder;
import de.th_wildau.quadroid.models.Attitude;
import de.th_wildau.quadroid.models.Course;
import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.MetaData;
import de.th_wildau.quadroid.models.QuadroidAirplane;
import de.th_wildau.quadroid.models.Waypoint;


/**
 * 
 * Main Method for quadroid project
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 28.11.2013 (JDK 7) 
 * 
 * */

public class QuadroidMain
{
	
	
	/**Logger for Logging with {@link org.slf4j.Logger}*/
	public static Logger logger = null;
	


	public static void main(String[] args) throws IOException
	{
		logger = LoggerFactory.getLogger(QuadroidMain.class.getName());
		QuadroidMain.logger.info("StartQuadroid");
		
		TxDataEncoder encoder = new TxDataEncoder();
		RxDataDecoder decoder = null;
		
		
		BufferedImage img = ImageIO.read(new File("test.jpg"));
		
		GNSS g1 = new GNSS();
		GNSS g2 = new GNSS();
		GNSS g3 = new GNSS();
		GNSS g4 = new GNSS();
		GNSS g5 = new GNSS();
		GNSS g6 = new GNSS();
		GNSS g7 = new GNSS();
		GNSS g8 = new GNSS();
		
		Waypoint wp = new Waypoint();
		MetaData md = new MetaData();
		QuadroidAirplane qa = new QuadroidAirplane();
		Course course = new Course();
		Attitude attitude = new Attitude();
		
		
		
		g1.setLatitude(52.1234f);
		g1.setLongitude(13.1431f);
		g1.setHeight(500.00f);
		
		g2.setHeight(45.003f);
		g2.setLatitude(53.1114f);
		g2.setLongitude(14.663f);
		
		attitude.setPitch(100.01f);
		attitude.setRoll(245.4f);
		attitude.setYaw(36.47f);
		
		course.setSpeed(45.123f);
		course.setAngleReference(56.00f);
		
		qa.setBatteryState((byte) 50);
		qa.setTime(100254);
		qa.setGeoData(g1);
		
		md.setAirplane(qa);
		md.setAttitude(attitude);
		md.setCourse(course);
		
		wp.setMetaData(md);
		wp.setPictureoflandmark(img);
		wp.setPosition(g2);
		byte[] data = encoder.appendBytes(encoder.geodataToBytes(g4), encoder.waypointToBytes(wp));
		
		data = encoder.appendBytes(data, encoder.geodataToBytes(g3));
		data = encoder.appendBytes(data, encoder.geodataToBytes(g5));
		data = encoder.appendBytes(data, encoder.geodataToBytes(g6));
		data = encoder.appendBytes(data, encoder.geodataToBytes(g7));
		data = encoder.appendBytes(data, encoder.geodataToBytes(g8));

		decoder = new RxDataDecoder(data);
		

	}

	
}

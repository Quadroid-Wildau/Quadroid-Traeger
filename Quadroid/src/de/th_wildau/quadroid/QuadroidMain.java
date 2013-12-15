package de.th_wildau.quadroid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.th_wildau.quadroid.decoder.RxDataDecoder;
import de.th_wildau.quadroid.encoder.TxDataEncoder;
import de.th_wildau.quadroid.models.Attitude;
import de.th_wildau.quadroid.models.Course;
import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.Landmark;
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
	


	public static void main(String[] args)
	{
		logger = LoggerFactory.getLogger(QuadroidMain.class.getName());
		TxDataEncoder tx = new TxDataEncoder();
		QuadroidMain.logger.info("StartQuadroid");
		
		Attitude a = new Attitude();
		Course c = new Course();
		GNSS g = new GNSS();
		Landmark l = new Landmark();
		MetaData m = new MetaData();
		QuadroidAirplane q = new QuadroidAirplane();
		Waypoint w = new Waypoint();
		
		GNSS g2 = new GNSS();
		g2.setHeight(70);
		g2.setLatitude(65);
		g2.setLongitude(99);
		
		//System.out.println(new String(tx.geodataToBytes(g2)));
		
		g.setHeight(50);
		g.setLatitude(100);
		g.setLongitude(200);
		
		//System.out.println(new String(tx.geodataToBytes(g)));
		
		q.setBatteryState((byte) 0x00);
		q.setTime(1234567890);
		q.setGeoData(g);
		
		//System.out.println(new String(tx.quadroidairplaneToBytes(q)));
		
		a.setPitch(1.0f);
		a.setRoll(2.0f);
		a.setYaw(3.0f);
		
		
		RxDataDecoder decoder = new RxDataDecoder(null);
		Attitude attitude = decoder.stringToAttitude(new String(tx.attitudeToBytes(a)));
		System.out.println("Data: " + attitude.getPitch() + "  " 
				+ attitude.getRoll() + "  " + attitude.getYaw());
		
		//System.out.println(new String(tx.attitudeToBytes(a)));
		
		c.setAngleReference(1.0f);
		c.setSpeed(100);
		
		//System.out.println(new String(tx.courseToBytes(c)));
		
		m.setAirplane(q);
		m.setAttitude(a);
		m.setCourse(c);
		
		//System.out.println(new String(tx.metadataToBytes(m)));
		
		w.setMetaData(m);
		w.setPictureoflandmark(null);
		w.setPosition(g2);
		
		
		//System.out.println( new String(tx.waypointToBytes(w)) );
	}

	
}

package de.th_wildau.quadroid;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.zip.CRC32;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.th_wildau.quadroid.decoder.RxDataDecoder;
import de.th_wildau.quadroid.encoder.TxDataEncoder;
import de.th_wildau.quadroid.enums.Marker;
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
		RxDataDecoder decoder = new RxDataDecoder(null);
		QuadroidMain.logger.info("StartQuadroid");
		
		Attitude a = new Attitude();
		Course c = new Course();
		GNSS g = new GNSS();
		Landmark l = new Landmark();
		//MetaData m = new MetaData();
		QuadroidAirplane q = new QuadroidAirplane();
		Waypoint w = new Waypoint();
		GNSS g2 = new GNSS();
		
		q.setBatteryState((byte) 78);
		q.setTime(154876582);
		g.setHeight(300.12547f);
		g.setLatitude(52.23564f);
		g.setLongitude(13.21546f);
		q.setGeoData(g);
		a.setPitch(54f);
		a.setRoll(65f);
		a.setYaw(32f);
		c.setAngleReference(45f);
		c.setSpeed(125);
		//m.setAirplane(q);
		//m.setAttitude(a);
		//m.setCourse(c);
		g2.setHeight(455);
		g2.setLatitude(12.2145f);
		g2.setLongitude(53.1547f);
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("test.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//w.setMetaData(null);
		//w.setPosition(null);
		w.setPictureoflandmark(img);
		
		String data = new String(tx.waypointToBytes(w));
		System.out.println(data);
		
		Waypoint newpoint = decoder.stringToWaypoint(data);
		
		int bild = (data.indexOf(Marker.PICTURESTART.getMarker()) + 3);
		int bildend = data.indexOf(Marker.PICTUREEND.getMarker());
		
		int crcstart = (data.indexOf(Marker.CRCSTART.getMarker()) + 3);
		int crcend = data.indexOf(Marker.CRCEND.getMarker());
		
		CRC32 crc = new CRC32();
		byte[] da = tx.imageToByteArray(img, Marker.IMAGETYPE.getMarker());
		
		crc.update(da );
		System.out.println( "Berechnetes CRC " + crc.getValue() );
		System.out.println( "Enthalten CRC " + data.substring(crcstart, crcend) );
		
		
		System.out.println(newpoint);
		System.out.println(newpoint + "   " +  newpoint.toString() );
	
		
	}

	
}

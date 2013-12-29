package de.th_wildau.quadroid;


import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.th_wildau.quadroid.connection.Connect;
import de.th_wildau.quadroid.encoder.TxDataEncoder;
import de.th_wildau.quadroid.enums.XBee;
import de.th_wildau.quadroid.handler.ObserverHandler;
import de.th_wildau.quadroid.handler.XBeeReceiverHandler;
import de.th_wildau.quadroid.handler.XBeeTransmitterHandler;
import de.th_wildau.quadroid.interfaces.IRxListener;
import de.th_wildau.quadroid.landmark.MainLandmark;
import de.th_wildau.quadroid.models.Attitude;
import de.th_wildau.quadroid.models.Course;
import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.MetaData;
import de.th_wildau.quadroid.models.Airplane;
import de.th_wildau.quadroid.models.RxData;
import de.th_wildau.quadroid.models.Waypoint;
import de.th_wildau.quadroid.models.XBeeRxTx;


/**
 * 
 * Main Method for quadroid project
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 28.11.2013 (JDK 7) 
 * 
 * */

public class QuadroidMain implements IRxListener
{
	
	
	/**Logger for Logging with {@link org.slf4j.Logger}*/
	public static Logger logger = null;
	
	private static long time = 0;
	private Connect connection = null;


	public static void main(String[] args) throws IOException
	{
		/*
		QuadroidMain main = new QuadroidMain();
		logger = LoggerFactory.getLogger(QuadroidMain.class.getName());
		QuadroidMain.logger.info("StartQuadroid");
		
		XBeeRxTx xbee = new XBeeRxTx();
		xbee.setBaud(XBee.BAUD.getValue());
		xbee.setDatabits(XBee.DATABITS.getValue());
		xbee.setParity(XBee.PARITY.getValue());
		xbee.setStopbits(XBee.STOPBITS.getValue());
		xbee.setPort(XBee.PORT.getName());
		xbee.setDevicename(XBee.DEVICENAME.getName());
		
		ObserverHandler.getReference().register(main);
		
		main.connection = Connect.getInstance(xbee);
		
		main.connection.addSerialPortEventListener(new XBeeReceiverHandler());
		
		XBeeTransmitterHandler tx = new XBeeTransmitterHandler(main.connection);
		
		TxDataEncoder encoder = new TxDataEncoder();
		
		
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
		Airplane qa = new Airplane();
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

		time = System.currentTimeMillis();
		tx.transmit(data);
		*/

		
		System.loadLibrary("opencv_java245");
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



	@Override
	public void rx(RxData data) 
	{
		this.connection.disconnect();
		time = (System.currentTimeMillis() - time);
		logger.info("Data Rx: " + time);
		
		System.out.println(data.getWaypointlist().get(0).toString());
		
		BufferedImage img = data.getWaypointlist().get(0).getPictureoflandmark();
		if(img != null)
		{
		JFrame frame = new JFrame();
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon((Image)img));
		frame.setBounds(100, 100, img.getWidth(), img.getHeight());
		frame.getContentPane().add(label);
		frame.setVisible(true);
		
		
		
		}
	}

	
}

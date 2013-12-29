package de.th_wildau.quadroid;


import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.log4j.PropertyConfigurator;
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
	private static final String LOGGERPROPERTIES = "log4j.properties";
	/**operation time*/
	private static long time = 0;
	/**save xbee connection*/
	private Connect connection = null;
	/**save handler reference for transmission*/
	private XBeeTransmitterHandler tx = null;
	/**instance for encoder*/
	private TxDataEncoder encoder = null;
	
	
	
	/**
	 * Init the xBee connection to given port,
	 * this method wait for xBee device until 
	 * an connection are available
	 * 
	 * */
	private void initxBee()
	{
		XBeeRxTx xbee = new XBeeRxTx();// create an new device
		xbee.setBaud(XBee.BAUD.getValue());// set baudrate for communication speed
		xbee.setDatabits(XBee.DATABITS.getValue());// set number of databits
		xbee.setParity(XBee.PARITY.getValue());// set parity type
		xbee.setStopbits(XBee.STOPBITS.getValue());// set number of stopbits
		xbee.setPort(XBee.PORT.getName());// set port for connection
		xbee.setDevicename(XBee.DEVICENAME.getName());// set an device name
		
		while(true)// wait for xbee device
		{
			if(this.connection == null)// try to connect only if no connection available
			{	// get all available ports to prove if xbee are connected
				Enumeration<?> commports = CommPortIdentifier.getPortIdentifiers();
				
				while(commports.hasMoreElements())
				{
					CommPortIdentifier port = (CommPortIdentifier) commports.nextElement();
					
					if(port.getName().equals(xbee.getPort()) && // only connect to specific port
					   port.getPortType() == CommPortIdentifier.PORT_SERIAL &&// prove port type
					   !port.isCurrentlyOwned())// prove if port already in use
					{
						this.connection = Connect.getInstance(xbee);// connect device to port 
						// connection successfully?
						if(this.connection != null)
							return;// exit While-Loop
					}
				}
				
			}
			logger.warn("Wait for xBee Device");
			
			try 
			{
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}

	}
	

	public static void main(String[] args) throws IOException
	{
		// init logger
		PropertyConfigurator.configure(LOGGERPROPERTIES);
		logger = LoggerFactory.getLogger(QuadroidMain.class.getName());
		logger.info("Init Logger");
		// load library
		System.loadLibrary("opencv_java245");
		logger.info("Load OpenCV library");
		// instance of this class
		QuadroidMain main = new QuadroidMain();
		// init xbee
		main.initxBee();
		logger.info("Init xBee device");
		// registered rx handler
		main.connection.addSerialPortEventListener(new XBeeReceiverHandler());
		logger.info("Registered Rx handler");
		// registered tx handler
		main.tx = new XBeeTransmitterHandler(main.connection);
		logger.info("Registered Tx handler");
		// registered observer
		ObserverHandler.getReference().register(main);
		logger.info("Registered Rx observer");
		// additional things
		main.encoder = new TxDataEncoder();
		// 
		QuadroidMain.logger.info("StartQuadroid");
		
		
		
		
		
		/*
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

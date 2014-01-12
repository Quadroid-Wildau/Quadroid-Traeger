package de.th_wildau.quadroid;

import java.awt.image.BufferedImage;
import java.util.Enumeration;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.th_wildau.quadroid.connection.Connect;
import de.th_wildau.quadroid.connection.USBCamConnection;
import de.th_wildau.quadroid.encoder.TxDataEncoder;
import de.th_wildau.quadroid.enums.Flight_Ctrl;
import de.th_wildau.quadroid.enums.XBee;
import de.th_wildau.quadroid.handler.FlightCtrlReceiverHandler;
import de.th_wildau.quadroid.handler.FlightCtrlTransmitterHandler;
import de.th_wildau.quadroid.handler.GPSModuleReceiveHandler;
import de.th_wildau.quadroid.handler.ObserverHandler;
import de.th_wildau.quadroid.handler.XBeeReceiverHandler;
import de.th_wildau.quadroid.handler.XBeeTransmitterHandler;
import de.th_wildau.quadroid.interfaces.IRxListener;
import de.th_wildau.quadroid.landmark.MainLandmark;
import de.th_wildau.quadroid.models.FlightCtrl;
import de.th_wildau.quadroid.models.GPSModule;
import de.th_wildau.quadroid.models.Landmark;
import de.th_wildau.quadroid.models.MetaData;
import de.th_wildau.quadroid.models.NaviDataContainer;
import de.th_wildau.quadroid.models.RxData;
import de.th_wildau.quadroid.models.XBeeRxTx;
import de.th_wildau.quadroid.utils.Properties;
import purejavacomm.*;


/**
 *
 * Main Method for quadroid project
 *
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 28.11.2013 (JDK 7)
 *
 * */

public class QuadroidMain implements IRxListener {


	/**Logger for Logging with {@link org.slf4j.Logger}*/
	public static Logger logger = null;
	/**describe the properties file for log4j logging*/
	private static final String LOGGERPROPERTIES = "log4j.properties";
	/**save xbee connection*/
	private Connect xbeeconnection = null;
	/**save flight ctrl connection*/
	private Connect flightctrlconnection = null;
	private volatile FlightCtrlTransmitterHandler flightCtrlTransmitterHandler = null;
	private GPSModuleReceiveHandler gpsModuleReceiveHandler = null;
	private Connect gpsModuleConnection = null;
	/**save handler reference for transmission*/
	private XBeeTransmitterHandler tx = null;
	/**this value define an interval for Flight-Ctrls updates*/
	private static final long UPDATE_TIME_FLIGHTCTRLUPDATER = 100;
	/**this value define an interval for updates of metadate 
	 * in milliseconds for transmission to ground station*/
	private static final long UPDATE_TIME_STATETRANSMITTER = 200;
	/**this flag managed metadata updates it is set <b>true</b>, 
	 *current metadata will be transmit to ground station*/
	private static boolean statetransmitter = false;
	@SuppressWarnings("unused")
	private FlightCtrlUpdater naviCtrlPoller = null;


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
		//xbee.setPort("/dev/ttyUSB0");// set port for connection
		xbee.setPort(Properties.getInstance().getProperty("xbeeport"));// set port for connection
		xbee.setDevicename(XBee.DEVICENAME.getName());// set an device name

		while(true)// wait for xbee device
		{
			if(this.xbeeconnection == null)// try to connect only if no connection available
			{	// get all available ports to prove if xbee are connected
				Enumeration<?> commports = CommPortIdentifier.getPortIdentifiers();

				while(commports.hasMoreElements())
				{
					CommPortIdentifier port = (CommPortIdentifier) commports.nextElement();

					if(port.getName().equals(xbee.getPort()) && // only connect to specific port
							port.getPortType() == CommPortIdentifier.PORT_SERIAL &&// prove port type
							!port.isCurrentlyOwned())// prove if port already in use
					{
						this.xbeeconnection = Connect.getInstance(xbee);// connect device to port 
						// connection successfully?
						if(this.xbeeconnection != null)
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


	/**
	 * Init the Flight-Ctrl connection to given port,
	 * this method wait for Flight-Ctrl device until 
	 * an connection are available
	 * 
	 * */
	private void initFlight_Ctrl()
	{
		FlightCtrl flightctrl = new FlightCtrl();// create an new device
		flightctrl.setBaud(Flight_Ctrl.BAUD.getValue());// set baudrate for communication speed
		flightctrl.setDatabits(Flight_Ctrl.DATABITS.getValue());// set number of databits
		flightctrl.setParity(Flight_Ctrl.PARITY.getValue());// set parity type
		flightctrl.setStopbits(Flight_Ctrl.STOPBITS.getValue());// set number of stopbits
		//flightctrl.setPort(Flight_Ctrl.PORT.getName());// set port for connection
		flightctrl.setPort(Properties.getInstance().getProperty("flightctrlport"));
		flightctrl.setDevicename(Flight_Ctrl.DEVICENAME.getName());// set an device name

		while(true)// wait for FlightCtrl device
		{
			if(this.flightctrlconnection == null)// try to connect only if no connection available
			{	// get all available ports to prove if FlightCtrl are connected
				Enumeration<?> commports = CommPortIdentifier.getPortIdentifiers();

				while(commports.hasMoreElements())
				{
					CommPortIdentifier port = (CommPortIdentifier) commports.nextElement();

					if(port.getName().equals(flightctrl.getPort()) && // only connect to specific port
							port.getPortType() == CommPortIdentifier.PORT_SERIAL &&// prove port type
							!port.isCurrentlyOwned())// prove if port already in use
					{
						this.flightctrlconnection = Connect.getInstance(flightctrl);// connect device to port 
						// connection successfully?
						if(this.flightctrlconnection != null)
							flightCtrlTransmitterHandler = new FlightCtrlTransmitterHandler(this.flightctrlconnection);
						flightctrlconnection.addSerialPortEventListener(new FlightCtrlReceiverHandler());
						logger.info("Register FlightCtrl RX Handler");

						naviCtrlPoller = new FlightCtrlUpdater(flightCtrlTransmitterHandler);
						logger.info("start Flight-Ctrl updater");

						return;// exit While-Loop
					}
				}

			}
			logger.warn("Wait for Flight-Ctrl Device");

			try 
			{
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}

	}

	/**
	 * Init the GPSModule connection to given port,
	 * this method wait for GPS device until 
	 * an connection are available
	 * 
	 * */
	private void initGPSModule()
	{
		GPSModule gpsModule = new GPSModule();// create an new device

		while(true)// wait for FlightCtrl device
		{
			if(this.gpsModuleConnection == null)// try to connect only if no connection available
			{	// get all available ports to prove if FlightCtrl are connected
				Enumeration<?> commports = CommPortIdentifier.getPortIdentifiers();

				while(commports.hasMoreElements())
				{
					CommPortIdentifier port = (CommPortIdentifier) commports.nextElement();

					if(port.getName().equals(gpsModule.getPort()) && // only connect to specific port
							port.getPortType() == CommPortIdentifier.PORT_SERIAL &&// prove port type
							!port.isCurrentlyOwned())// prove if port already in use
					{
						this.gpsModuleConnection = Connect.getInstance(gpsModule);// connect device to port 
						// connection successfully?
						if(this.gpsModuleConnection != null) {
							gpsModuleReceiveHandler = new GPSModuleReceiveHandler();
							gpsModuleConnection.addSerialPortEventListener(gpsModuleReceiveHandler);
							logger.info("Register GPS Module RX Handler");

							return;// exit While-Loop
						}
					}
				}

			}
			logger.warn("Wait for GPSModuleDevice");

			try 
			{
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}

	}


	public static void main(String[] args)
	{
		long time = System.currentTimeMillis();
		// init logger
		PropertyConfigurator.configure(LOGGERPROPERTIES);
		logger = LoggerFactory.getLogger(QuadroidMain.class.getName());
		logger.info("Init Logger");
		// load library
		try
		{
			//System.loadLibrary("opencv_java245");
			logger.info("Load OpenCV library");
		}catch(Exception e)
		{
			logger.error("can not load OpenCV library",e);
			System.exit(-1);
		}	
		// instance of this class
		QuadroidMain main = new QuadroidMain();
		// init xbee
		main.initxBee();
		logger.info("Init xBee device");
		//init flight ctrl
		main.initFlight_Ctrl();
		logger.info("Init Flight-Ctrl device");
		// registered xbee rx handler

		// init gps module
		logger.info("Init GPS device");
		main.initGPSModule();

		main.xbeeconnection.addSerialPortEventListener(new XBeeReceiverHandler());
		logger.info("Registered Xbee Rx handler");
		// registered xbee tx handler
		main.tx = new XBeeTransmitterHandler(main.xbeeconnection);
		logger.info("Registered Xbee Tx handler");

		// registered observer
		ObserverHandler.getReference().register(main);
		logger.info("Registered Rx observer");

		// start metadata updater
		new StateTransmitter(main);
		logger.info("start metadata updater");
		// start landmark detection
		new LandmarkDetection(main);
		logger.info("start landmark detection");

		// additional things
		QuadroidMain.logger.info("StartQuadroid");

		time = (System.currentTimeMillis() - time);
		logger.info("time for init " + time + " ms");
		QuadroidMain.logger.info("StartQuadroid");
	}


	/**
	 * this internal class pull MetaData updates from Flight-Ctrl
	 * 
	 * @author Alexander Schrot
	 * @see Runnable
	 * 
	 * 
	 * */
	public class FlightCtrlUpdater implements Runnable {
		private FlightCtrlTransmitterHandler txHandler;

		public FlightCtrlUpdater(FlightCtrlTransmitterHandler handler) {
			txHandler = handler;
			Thread thread = new Thread(this);

			thread.start();
		}

		public void run() {
			while(true) {
				double lastUpdate = System.currentTimeMillis() - NaviDataContainer.getInstance().getLastUpdated();

				if(lastUpdate > 300) {
					logger.debug("FlightCtrlUpdater");
					txHandler.requestNaviData();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					try {
						Thread.sleep(UPDATE_TIME_FLIGHTCTRLUPDATER);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	@Override
	public void rx(RxData data) 
	{
		if(data != null && data.getWaypointlist() != null) {
			this.flightCtrlTransmitterHandler.addWaypoints(null);

		} else {
			if(data == null) {
				logger.debug("data is null");
			}

			if(data.getWaypointlist() == null) {
				logger.debug("data.getWaypointlist() is null");
			}
		}
	}

	/**
	 * this internal class realized the transmission of metadata
	 * to ground station into separate thread
	 *
	 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
	 * @version 1.0, 05.01.2014, (JDK 7)
	 * @see Runnable
	 *
	 *
	 * */
	public static class StateTransmitter implements Runnable
	{
		/**reference of main class for using fields*/
		private QuadroidMain mainref = null;
		/**reference of TxDataEncoder to encode object zu bytes*/
		private TxDataEncoder encoder = null;
		private long lastMetaData;

		/**
		 * no public constructor
		 * only use in main
		 * */
		private StateTransmitter()
		{
			lastMetaData = 0;
		}

		/**
		 * Constructor
		 *
		 * @param ref - hand over an reference of {@link QuadroidMain} for starting
		 * state transmitter
		 *
		 * @throws NullPointerException - if parameter <b>null</b>
		 * */
		private StateTransmitter(QuadroidMain ref)
		{
			if(ref == null)
			{
				logger.error("StateTransmitter - reference of QuadroidMain are null");
				throw new NullPointerException("StateTransmitter - reference of QuadroidMain are null");
			}
			this.mainref = ref;
			this.encoder = new TxDataEncoder();
			Thread updater = new Thread(this);
			updater.start();
		}

		@Override
		public void run()
		{
			while(true)
			{
				try
				{        //wait any time before transmit next metadata to ground station
					Thread.sleep(UPDATE_TIME_STATETRANSMITTER);
				} catch (InterruptedException e) {}

				//is transmission are enable?
				//must be interrupt from landmark detection
				//to transmit image data after transmission enable StateTransmitter
				if(statetransmitter)
					continue;

				logger.debug("last metadata: " + NaviDataContainer.getInstance().getLastUpdated());
				if(lastMetaData != NaviDataContainer.getInstance().getLastUpdated()) {
					logger.debug("transmit new meta data");
					this.lastMetaData = NaviDataContainer.getInstance().getLastUpdated();
					MetaData update = NaviDataContainer.getInstance().getLastMetaData();
					//encode object into bytes
					byte[] metadata = encoder.metadataToBytes(update);
					//transmit data to ground station
					this.mainref.tx.transmit(metadata);
				}
			}
		}

	}


	/**
	 * this internal class realized the landmark detection
	 * into separate thread
	 *
	 * @author Stefan
	 * @see Runnable
	 * */
	public static class LandmarkDetection implements Runnable{

		/**instance of QuadroidMain for using fields*/
		private QuadroidMain mainref = null;

		/**no public constructor class only use in main*/
		@SuppressWarnings("unused")
		private LandmarkDetection()
		{

		}

		/**
		 * Constructor
		 *
		 * @param ref - hand over an reference of {@link QuadroidMain}
		 * for starting the landmark detection
		 *
		 * @throws NullPointerException - if parameter <b>null</b>
		 * */
		public LandmarkDetection(QuadroidMain ref)
		{
			if(ref == null)
			{
				logger.error("LandmarkDetection - reference of QuadroidMain are null");
				throw new NullPointerException("LandmarkDetection - reference of QuadroidMain are null");
			}

			this.mainref = ref;
			Thread thread = new Thread(this);
			thread.start();
		}

		/**
		 * Flowcontrolthread for the Landmarkdetection
		 */
		public void run() {
			Landmark landmark = new Landmark();
			BufferedImage bimg = null;
			long t1 = 0;
			long t2 = 0;
			boolean lmcheck = false;
			TxDataEncoder encoder = new TxDataEncoder();
			byte[] bytedata;

			MainLandmark lm = new MainLandmark();
			Logger logger = null;
			logger = LoggerFactory.getLogger(LandmarkDetection.class.getName());
			logger.info("Init Logger");
			//Getting connection to the USBCam
			USBCamConnection usbcamera = USBCamConnection.getInstance(logger);

			//Flowcontrol for Landmarkdetection
			while(true){
				t1 = System.currentTimeMillis();
				bimg = usbcamera.getImage();
				lmcheck = lm.checkLandmark(bimg); //Performing Landmarkcheck
				logger.debug("detect landmark");
				if(lmcheck == true){
					logger.debug("found landmark");
					landmark.setPictureoflandmark(bimg); //If found, set the current image
					//set metadata to landmark
					landmark.setMetaData(NaviDataContainer.getInstance().getLastMetaData());
					statetransmitter = true;        //Lock the xbee channel for transmitting
					bytedata = encoder.landmarkToBytes(landmark);        //encode Data
					this.mainref.tx.transmit(bytedata);                                //Transmit Landmark

					try {
						Thread.sleep(1000 * 10);                        //Sleep for 10s (approx. time for transmitting the Landmark)
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					statetransmitter = false;        //unlock the xbee channel

				}else{ //if no landmark is detected
					t2 = System.currentTimeMillis();

					//Sleep 500ms before next landmark check
					try {
						//Thread.sleep(500-(t2-t1));
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}


			}

		}

	}


}

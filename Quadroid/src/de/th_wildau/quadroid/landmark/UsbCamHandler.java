package de.th_wildau.quadroid.landmark;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import org.slf4j.Logger;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamLock;
import com.github.sarxos.webcam.WebcamResolution;


/**
 * 
 * The Usb-Camera will be locked once connect it, therefore 
 * it can t connect a second one.  
 * 
 * To use: 
 * UsbCamHandler uch = UsbCamHandler.getInstance();  <--- ca. 4056 ms
 * 
 * if(isConnected())
 * {
 * 		BufferedImage image = uch.getImage();
 * }
 * 
 * to disconnect... use ---> uch.disconnectCamera();
 * 
 * @author Thomas Rohde TM-12, trohde@th-wildau.de, 10.11.2013 
 * @version 1.0, 10.11.2013, (Java JDK 7)
 * 
 * 
 * */

public class UsbCamHandler 
{
	/**save an return reference of {@link UsbCamHandler} 
	 * for Singleton implementation*/
	private static UsbCamHandler usbcamerahandler = null;
	/**save camera resolution*/
	private static final Dimension RESOLUTION = WebcamResolution.VGA.getSize();
	/**save an instance of Webcam for access of usb camera*/
	private Webcam usbcam = null;
	/**logger for logging actions*/
	private Logger logger = null;
	
	
	
	/**
	 * Invisible Constructor 
	 * for Singleton implementation 
	 * */
	private UsbCamHandler()
	{	
	}
	
	/**
	 * Invisible Constructor for Singleton 
	 * @param logger instance of {@link org.slf4j.Logger}
	 * */
	private UsbCamHandler(Logger logger)
	{		
		this.logger = logger;
		//set reference
		UsbCamHandler.usbcamerahandler = this;
		//start camera
		this.usbcam = this.connectToCamera(RESOLUTION);
	}
	
	
	/**
	 * Singleton function
	 * 
	 * @param logger - reference of {@link org.slf4j.Logger}
	 * @return reference of {@link UsbCamHandler} 
	 * */
	public static UsbCamHandler getInstance(Logger logger)
	{	//Singleton Pattern
		if(UsbCamHandler.usbcamerahandler == null)
			new UsbCamHandler(logger);
		return UsbCamHandler.usbcamerahandler;
	}
	
	/**
	 * This Function try to connect to USB-Camera
	 * it will be get an reference of {@link Webcam}
	 * if all operation are successfully
	 * 
	 * @param resolution
	 * <p>hand over an resolution for snapshot function
	 * to see supported resolution look at {@link WebcamResolution}</p>
	 * 
	 * @return an instance of {@link Webcam} if successfully or <b>null</b>
	 * if 
	 * <ul>
	 * <li>Camera already connect and open</li> or
	 * <li>Camera is locked and can t be use (in this case system must be reboot)</li>
	 * </ul>
	 * 
	 * */
	private Webcam connectToCamera(Dimension resolution)
	{
		Webcam camera = Webcam.getDefault();
		
		
		if(camera == null || camera.isOpen())
		   return null;
		/*camera already in use can t open next session
		 * device is locked!*/
		
		WebcamLock lock = camera.getLock();
		
		if(lock.isLocked())
			return null;
		/*camera is locked can t connect! restart is 
		 * necessary*/
		
		//set an camera resolution it can be 
		//WebcamResolution.VGA.getSize() or other....
		camera.setViewSize(resolution);
		logger.debug("Set Resolution: " + resolution);
		
		try
		{	//try open without blocking mode
			camera.open(true);
		}catch(Exception ex)
		{	
			logger.error("Exception: ", ex);
		}
		
		//successfully operation camera stream is open
		return camera;
	} 
	
	/**
	 * this function get a new picture from usb camera
	 * if connect and available 
	 * 
	 * @return a new instance of {@link BufferedImage} or <b>null</b>
	 * if usb camera not available
	 * 
	 * */
	public BufferedImage getImage()
	{
		if(this.usbcam != null)
			return this.usbcam.getImage();
		return null;
	}
	
	/**
	 * This Method disconnect the usb camera
	 * */
	public void disconnectCamera()
	{
		if(this.isConnected())
		{
			this.usbcam.close();
			this.usbcam = null;
		}
	}
	
	/**
	 * Getter Function
	 * 
	 * @return 
	 * <ul>
	 * <li>true = connect to usb camera</li>
	 * <li>false = no connect</li>
	 * </ul>
	 * */
	public boolean isConnected()
	{
		if(this.usbcam == null)
			return false;
		return this.usbcam.isOpen();
	}
}

package de.th_wildau.quadroid;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.th_wildau.quadroid.converter.DataConverter;
import de.th_wildau.quadroid.handler.ReceiverHandler;
import de.th_wildau.quadroid.handler.TransmitterHandler;
import de.th_wildau.quadroid.interfaces.IReceiver;


/**
 * 
 * Main Method for quadroid project
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 28.11.2013 (JDK 7) 
 * 
 * */

public class QuadroidMain extends JFrame implements IReceiver
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static QuadroidMain qm = null;
	
	private static BufferedImage rximage = null;
	
	/**Logger for Logging with {@link org.slf4j.Logger}*/
	public static Logger logger = null;
	
	private static DataConverter dc = null;
	
	/**Main Method*/
	public static void main(String[] args)
	{
		qm = new QuadroidMain();
		logger = LoggerFactory.getLogger(QuadroidMain.class.getName());
		ReceiverHandler rh = new ReceiverHandler();
		TransmitterHandler th = new TransmitterHandler();
		rh.addIReceiverObserver(qm);
		
		qm.setBounds(100, 100, 500, 500);
		qm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try 
		{
			BufferedImage image = ImageIO.read(new File("Oktoberfest.jpg"));
			dc = new DataConverter();
			th.transmit(dc.convertImageToByteArray(image, "jpg"));
		} catch (IOException e) { 
			e.printStackTrace();
			System.exit(0);
		}
		
	}

	@Override
	public void paint(Graphics g) 
	{
		if(rximage != null)
		{
			qm.setBounds(100, 100, rximage.getWidth(), rximage.getHeight());
			g.drawImage(rximage, 0, 0, rximage.getWidth(), rximage.getHeight(), null);
		}
	}
	
	@Override
	public void dataInputNotyfier(byte[] data) 
	{
		qm.setVisible(false);
		rximage = dc.convertByteToBufferedImage(data);
		qm.repaint();
		qm.setVisible(true);
	}

	
}

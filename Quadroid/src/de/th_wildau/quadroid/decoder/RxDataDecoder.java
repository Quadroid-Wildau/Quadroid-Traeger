package de.th_wildau.quadroid.decoder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import de.th_wildau.quadroid.QuadroidMain;

/**
 * this class decoded data to objects after receiving
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 11.12.2013, (JDK 7)
 * 
 * */

public class RxDataDecoder 
{

	/**Logger for logging with log4j*/
	private Logger logger = QuadroidMain.logger;
	
	/**
	 * this function convert an byte-array to {@link java.awt.image.BufferedImage} object
	 * 
	 * @param imagedata - hand over image data for convert into {@link java.awt.image.BufferedImage}
	 * 
	 * @return return an instance of {@link java.awt.image.BufferedImage},
	 * if <tt>imagedata null</tt> return <b>null</b> 
	 * 
	 * */
	public BufferedImage convertByteToBufferedImage(byte[] imagedata)
	{
		if(imagedata == null)
			return null;
		//hand over data to stream
		ByteArrayInputStream bais = new ByteArrayInputStream(imagedata);
		BufferedImage bufferedimage = null;
		try 
		{	//create new BufferedImage object with given data from Stream
			bufferedimage = ImageIO.read(bais);
			logger.debug("Create new BufferedImage object " + 
			bufferedimage.getWidth() + " " + 
			bufferedimage.getHeight());
		} catch (IOException e) 
		{
			logger.error("ImageIO.read(bais) Exception: ", e);
		}
		finally
		{
			try 
			{	//close stream
				bais.close();
			} catch (IOException e) 
			{
				logger.error("bais.close() Exception: ", e);
			}
		}
		
		return bufferedimage;
	}
	
}

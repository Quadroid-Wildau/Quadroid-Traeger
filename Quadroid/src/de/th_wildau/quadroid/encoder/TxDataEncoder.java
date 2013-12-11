package de.th_wildau.quadroid.encoder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import de.th_wildau.quadroid.QuadroidMain;


/**
 * This class encoded objects to byte data before transmission 
 * 
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 30.11.2013 (JDK 7) 
 * 
 * */

public class TxDataEncoder 
{
	/**instance of {@link org.slf4j.Logger} for logging*/
	private static Logger logger = QuadroidMain.logger;
	
	/**
	 * this function convert an {@link java.awt.image.BufferedImage} object to
	 * bytes
	 * 
	 * @param bufferedimage - hand over an reference of {@link java.awt.image.BufferedImage}
	 * 
	 * @param type - hand over an type of file for example: "jpg", "png", "bmp" or other...
	 * 
	 * @return return an byte-array contains data from given BufferedImage,
	 * return <b>null</b> if parameters are <tt>null</tt> 
	 * */
	public byte[] convertImageToByteArray(BufferedImage bufferedimage, String type)
	{	//prove parameter
		if(bufferedimage == null || type == null)
			return null;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try 
		{	//write image date into ByteArrayOutputStream
			ImageIO.write(bufferedimage, type, bos);
			logger.debug("Write data into ByteStream");
		} catch (IOException e) 
		{
			logger.error("ImageIO.write(bufferedimage, type, bos) Exception: ", e);
		}
		try 
		{
			bos.flush();//clean pipe
		} catch (IOException e) 
		{
			logger.error("bos.flush() Exception: ", e);
		}
		finally
		{
			try 
			{
				bos.close();//close pipe
			} catch (IOException e) 
			{
				logger.error("bos.close() Exception: ", e);
			}
		}	
		
		return bos.toByteArray();
	}
	
}

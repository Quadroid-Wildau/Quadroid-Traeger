package de.th_wildau.quadroid.converter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import de.th_wildau.quadroid.QuadroidMain;

/**
 * This Class convert different objects to bytes 
 * and back
 * 
 * 
 * @author Thomas Rohde, trohde@th-wildau.de
 * @version 1.0 30.11.2013 (JDK 7) 
 * 
 * */

public class DataConverter 
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

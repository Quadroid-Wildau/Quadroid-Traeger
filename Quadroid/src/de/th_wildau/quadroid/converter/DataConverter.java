package de.th_wildau.quadroid.converter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import javax.imageio.ImageIO;
import org.slf4j.Logger;
import de.th_wildau.quadroid.QuadroidMain;
import de.th_wildau.quadroid.enums.Marker;

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

	/**
	 * this function compute the crc32 checksum, for data between startmarker and
	 * endmarker. 
	 * 
	 * @param data <p>hand over an array of bytes, contains data enclose with 
	 * startmaker and endmarker. For this data will be compute the crc32 
	 * checksum, additional for compare data must be contain an transmit checksum 
	 * enclose with CRCSTART and CRCEND see {@link de.th_wildau.quadroid.enums.Marker} 
	 * </p>
	 * 
	 * @param startmarker <p>marked begin of data for computing crc32 checksum</p>
	 * 
	 * @param endmarker <p>marked end of data for computing crc32 checksum</p>
	 * 
	 * @return 
	 * <ul>
	 * <li>true = computed CRC32 equals with contained</li>
	 * <li>false = computed CRC32 unequal with contained</li>
	 * or
	 * <li>false = no contained CRC32 enclosed with CRCSTART and CRCEND</li>
	 * or
	 * <li>false = marker out of bounds</li>
	 * or
	 * <li>false = reference of data <b>null</b></li>
	 * </ul>
	 * 
	 * */
	public boolean proveCRC(byte[] data, String startmarker, String endmarker)
	{
		if(data == null)//prove data
		   return false;
		CRC32 crc32 = new CRC32();//create instance of crc32
		try
		{
			String convert = new String(data);//convert to string for easy search marker
			//search index for start and end marker and return substring between markers
			//if no available an exception will be throw
			String crcdata = convert.substring(convert.indexOf(startmarker), 
							  				   convert.indexOf(endmarker));
			
			logger.debug("StartMarkerOfData: " + convert.indexOf(startmarker) + " " +
						 "EndMarkerOfData: " + convert.indexOf(endmarker));
			// -||-
			String crc = convert.substring(convert.indexOf(Marker.CRCSTART.getMarker()), 
										   convert.indexOf(Marker.CRCEND.getMarker()));
			logger.debug("StartMarkerOfCRC: " + convert.indexOf(Marker.CRCSTART.getMarker()) + " " +
					     "EndMarkerOfCRC: " + convert.indexOf(Marker.CRCEND.getMarker()));
			//convert so long for comparing
			long containscrc = Long.valueOf(crc);
			logger.debug("Contains CRC: " + containscrc);
			//compute crc over substring
			crc32.update(crcdata.getBytes());
			//get crc checksum 
			long camputedcrc = crc32.getValue();
			logger.debug("Computed CRC: " + camputedcrc);
			//compare checksum
			if(containscrc == camputedcrc)
			{
				logger.info("CRC32 OK");
				return true;//if equals return true
			}
			logger.info("CRC32 Unequal");
		}catch(Exception e)
		{
			logger.error("CRC32 Exception: " , e);
			return false;//
		}	
		
		return false;
	}

	public static void main(String[] val)
	{
		String data = Marker.AKKUSTART.getMarker() + "1234567890" + Marker.AKKUEND.getMarker();
		CRC32 crc = new CRC32();
		
		
		crc.update(data.getBytes(), data.indexOf(Marker.AKKUSTART.getMarker()) + 3, data.indexOf(Marker.AKKUEND.getMarker()) - 1);
		
		//System.out.println( "Index: " + data.charAt(data.indexOf(Marker.AKKUSTART.getMarker()) + 3) + "  " + data.charAt(data.indexOf(Marker.AKKUEND.getMarker()) - 1));
		
		String crc32 = String.valueOf(crc.getValue());
		
		data += Marker.CRCSTART.getMarker() + crc32 + Marker.CRCEND.getMarker();
		
		System.out.println( data );
		
		DataConverter dc = new DataConverter();
		System.out.println( dc.proveCRC(data.getBytes(), Marker.AKKUSTART.getMarker(), Marker.AKKUEND.getMarker()) );
	}
	
}

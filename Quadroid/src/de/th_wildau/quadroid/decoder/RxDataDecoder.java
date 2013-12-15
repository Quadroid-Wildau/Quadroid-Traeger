package de.th_wildau.quadroid.decoder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import de.th_wildau.quadroid.QuadroidMain;
import de.th_wildau.quadroid.enums.Marker;
import de.th_wildau.quadroid.models.Attitude;
import de.th_wildau.quadroid.models.Course;
import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.Landmark;
import de.th_wildau.quadroid.models.MetaData;
import de.th_wildau.quadroid.models.QuadroidAirplane;
import de.th_wildau.quadroid.models.Waypoint;

/**
 * this class decoded data to objects after receiving
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 11.12.2013, (JDK 7)
 * @see Runnable
 * 
 * */

public class RxDataDecoder implements Runnable
{
	/**save received data*/
	private byte[] rx = null;
	/**state of decoding*/
	private boolean isdecoded = false;
	/**state of crc32*/
	private boolean iscrcok = false;
	/**logger for logging with log4j*/
	private Logger logger = QuadroidMain.logger;
	/**offset for index of search*/
	private static final byte OFFSET = 3;
	/**marker for replacing substring*/
	private static final String REPLACE = "°_^";
	
	/**
	 * public constructor 
	 * 
	 * @param data - hand over received data as encoded base64 byte-array
	 * 
	 * */
	public RxDataDecoder(byte[] data)
	{
		this.rx = data;
		Thread decode = new Thread(this);
		decode.start();// decode data from array
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
	public BufferedImage byteToBufferedImage(byte[] imagedata)
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
	 * prove contained crc32 value
	 * 
	 * @param startmarker - 
	 * hand over startmarker for begin of computing crc
	 * 
	 * @param endmarker -
	 * hand over endmarker for end of computing crc
	 * 
	 * @param data -
	 * hand over data contains startmarker, endmarker, data, 
	 * crcstartmarker, crc-sum, crcendmarker
	 * 
	 * @return 
	 * <ul>
	 * <li>true = contained CRC32 equal computed CRC32</li>
	 * <li>false = crc computing failed or CRC32 unequal</li>
	 * </ul>
	 * 
	 * */
	public boolean proveCRC(String startmarker, String endmarker, byte[] data)
	{
		if(startmarker == null || endmarker == null || data == null)
			return false;//aboard when parameters invalid
		
		// convert to string for searching 
		// und create substrings
		String crcdata = new String(data); 
		String crcval = null;// for saving crc substring
		long crcvalue = -1;// saving long value
		byte[] computdata = null;//saving data for computung crc
		
		
		
		//prove at marker available
		if(crcdata.contains(startmarker) && crcdata.contains(endmarker) &&
			crcdata.contains(Marker.CRCSTART.getMarker()) && 
			crcdata.contains(Marker.CRCEND.getMarker()))
		{	
			logger.info("all marker available!");
			//get index position of marker for select substring, offset is needed to
			//get right position for start-marker
			int begindata = (crcdata.indexOf(startmarker) + OFFSET);
			int enddata = crcdata.indexOf(endmarker);
			int begincrc = (crcdata.indexOf(Marker.CRCSTART.getMarker()) + OFFSET);
			int endcrc = crcdata.indexOf(Marker.CRCEND.getMarker());
			
			crcval = crcdata.substring(begincrc, endcrc);// get substring for crc
			crcdata = crcdata.substring(begindata, enddata);// get substring for data
			
			computdata = crcdata.getBytes();
			
			try
			{//try to cast to long 
				crcvalue = Long.valueOf(crcval);
				logger.debug("Cast CRCVal to Long...");
			}catch(Exception e)
			{
				logger.error("Cast Exception Long.valueOf(crcval): ", e);
			}	
			
			CRC32 crc32 = new CRC32();
			crc32.update(computdata);//comput crc value
			logger.info("Contain CRC " + crcvalue + " Computed CRC " + crc32.getValue());
			if(crc32.getValue() == crcvalue)//CRC32 equals?
			{
				logger.debug("Equals CRC32");
				logger.info("CRC OK");
				return true;
			}
			logger.debug("Unequal CRC32");
		}
		
		return false;
	}
	
	/**
	 * Getter Function
	 * 
	 * @return get state of decoding
	 * <ul>
	 * <li>true = end of decoding successfully</li>
	 * <li>false = working in process</li>
	 * </ul>
	 * 
	 * */
	public boolean isDecode()
	{
		return this.isdecoded;
	}
	
	/**
	 * this Function can be use to parse specific String-Data to 
	 * Object
	 * 
	 * @param data - hand over the specific string contains
	 * marker for identifier {@link Attitude}
	 * 
	 * @return get an object of {@link Attitude} or <b>null</b>
	 * if parsing was unsuccessfully
	 * 
	 * */
	public Attitude stringToAttitude(String data)
	{
		Attitude attitude = new Attitude();
		
		try
		{
			//get position of yaw data 
			int ystart = (data.indexOf(Marker.YAWSTART.getMarker()) + OFFSET);
			int yend = data.indexOf(Marker.YAWEND.getMarker());
			//get position of roll data 
			int rstart = (data.indexOf(Marker.ROLLSTART.getMarker()) + OFFSET);
			int rend = data.indexOf(Marker.ROLLEND.getMarker());
			//get position of pitch data 
			int pstart = (data.indexOf(Marker.PITCHSTART.getMarker()) + OFFSET);
			int pend = data.indexOf(Marker.PITCHEND.getMarker());
			
			//convert yaw data from string to float 
			attitude.setYaw(Float.valueOf(data.substring(ystart, yend)));
			//convert roll data from string to float
			attitude.setRoll(Float.valueOf(data.substring(rstart, rend)));
			//convert pitch data from string to float
			attitude.setPitch(Float.valueOf(data.substring(pstart, pend)));
			
			logger.debug("Create Attitude");
			return attitude;
		}catch(Exception e)
		{
			logger.error("bytesToAttitude Exception: ", e);
			return null;
		}	
	}
	
	/**
	 * This function can be use to convert an specific Data-String to
	 * GNSS object
	 * 
	 * @param data - hand over an specific string contains marker
	 * for identifier GNSS data
	 * 
	 * @return get an object 
	 * 
	 * */
	public GNSS stringToGNSS(String data)
	{
		GNSS geo = new GNSS();
		
		try
		{
			//get position of latitude data 
			int lastart = (data.indexOf(Marker.LATITUDESTART.getMarker()) + OFFSET);
			int laend = data.indexOf(Marker.LATITUDEEND.getMarker());
			//get position of longitude data 
			int lostart = (data.indexOf(Marker.LONGITUDESTART.getMarker()) + OFFSET);
			int loend = data.indexOf(Marker.LONGITUDEEND.getMarker());
			//get position of height data 
			int hstart = (data.indexOf(Marker.HEIGHTSTART.getMarker()) + OFFSET);
			int hend = data.indexOf(Marker.HEIGHTEND.getMarker());
			
			//convert latitude data from string to float 
			geo.setLatitude(Float.valueOf(data.substring(lastart, laend)));
			//convert longitude data from string to float
			geo.setLongitude(Float.valueOf(data.substring(lostart, loend)));
			//convert height data from string to float
			geo.setHeight(Float.valueOf(data.substring(hstart, hend)));
			
			logger.debug("Create GNSS");
			return geo;
		}catch(Exception e)
		{
			logger.error("stringToGNSS Exception: ", e);
			return null;
		}
	}
	
	/**
	 * This function can be use to convert an specific Data-String
	 * to Course object
	 * 
	 * @param data - hand over an specific string contains
	 * marker for identifier {@link Course} data see {@link Marker}
	 * 
	 * @return get an object of {@link Course} or <b>null</b> if 
	 * parsing no successfully 
	 * 
	 * */
	public Course stringToCourse(String data)
	{
		Course course = new Course();
		
		try
		{
			//get position of angle data 
			int astart = (data.indexOf(Marker.ANGLESTART.getMarker()) + OFFSET);
			int aend = data.indexOf(Marker.ANGELEND.getMarker());
			//get position of speed data 
			int sstart = (data.indexOf(Marker.SPEEDSTART.getMarker()) + OFFSET);
			int send = data.indexOf(Marker.SPEEDEND.getMarker());
			
			//convert latitude data from string to float 
			course.setAngleReference(Float.valueOf(data.substring(astart, aend)));
			//convert longitude data from string to float
			course.setSpeed(Float.valueOf(data.substring(sstart, send)));
			
			logger.debug("Create Course");
			return course;
		}catch(Exception e)
		{
			logger.error("stringToCourse Exception: ", e);
			return null;
		}
	}
	
	/**
	 * this function can be use to parse an specific Data-String
	 * to QuadroidAirplane
	 * 
	 * @param data - hand over an specific Data-String contains 
	 * marker for identify QuadroidAirplane data
	 * 
	 * @return get an object of {@link QuadroidAirplane} or <b>null</b>
	 * if parsing was no successfully 
	 * 
	 * */
	public QuadroidAirplane stringToAirplane(String data)
	{
		QuadroidAirplane qa = new QuadroidAirplane();
		
		try
		{	//position of battery data
			int bstart = (data.indexOf(Marker.AKKUSTART.getMarker()) + OFFSET);
			int bend = data.indexOf(Marker.AKKUEND.getMarker());
			//position of time data
			int tstart = (data.indexOf(Marker.TIMESTART.getMarker()) + OFFSET);
			int tend = data.indexOf(Marker.TIMEEND.getMarker());
			
			//is available?
			if(data.contains(Marker.GNSSSTART.getMarker()) && 
					data.contains(Marker.GNSSEND.getMarker()))
			{
				//position of gnss data
				int gstart = (data.indexOf(Marker.GNSSSTART.getMarker()) + OFFSET);
				int gend = data.indexOf(Marker.GNSSEND.getMarker());
				//parse an set data from string to GNSS
				qa.setGeoData(this.stringToGNSS(data.substring(gstart, gend)));
			}	
			
			//parse and set data from string to byte
			qa.setBatteryState(Byte.valueOf(data.substring(bstart, bend)));
			//parse and set data from string to long
			qa.setTime(Long.valueOf(data.substring(tstart, tend)));
			
			logger.debug("Create QuadroidAirplane");
			return qa;
		}catch(Exception e)
		{
			logger.error("stringToAirplane - Exception", e);
			return null;
		}
	}

	/**
	 * This function can be use to parse an specific Data-String to
	 * Waypoint model
	 * 
	 * @param data - hand over an specific Data-String contains 
	 * marker to identify {@link Waypoint} data see {@link Marker}
	 * 
	 * @return get an object of {@link Waypoint} or <b>null</b> if
	 * parsing was unsuccessfully
	 * 
	 * */
	public Waypoint stringToWaypoint(String data)
	{
		Waypoint point = new Waypoint();
		
		try
		{	//is an image available?
			if(data.contains(Marker.PICTURESTART.getMarker()) &&
					data.contains(Marker.PICTUREEND.getMarker()))
			{
				logger.info("Image contains prove CRC ...");
				//prove CRC32 Data, if check successfully go in
				if(this.proveCRC(Marker.PICTURESTART.getMarker(), 
						Marker.PICTUREEND.getMarker(), 
						data.getBytes()))
				{
					logger.info("CRC OK convert Image");
					//position of image data
					int pstart = (data.indexOf(Marker.PICTURESTART.getMarker()) + OFFSET);
					int pend = data.indexOf(Marker.PICTUREEND.getMarker());
					//convert data bytes to buffered image an set value into waypoint
					point.setPictureoflandmark(
							this.byteToBufferedImage(
									data.substring(pstart, pend).getBytes()));
				
				}else
				{
					logger.info("CRC fail no convert Image");
				}

			}
			//metadata available?
			if(data.contains(Marker.METADATASTART.getMarker()) && 
					data.contains(Marker.METADATAEND.getMarker()))
			{	//start position of metadata
				int mstart = (data.indexOf(Marker.METADATASTART.getMarker()) + OFFSET);
				int mend = data.indexOf(Marker.METADATAEND.getMarker());
				//convert und set metadata into waypoint
				point.setMetaData(
						this.stringToMetaData(
								data.substring(mstart, mend)));
			}
			//gnss data available?
			if(data.contains(Marker.GNSSSTART.getMarker()) && 
					data.contains(Marker.GNSSEND.getMarker()))
			{	//position of GNSS data
				int gstart = (data.indexOf(Marker.GNSSSTART.getMarker()) + OFFSET);
				int gend = data.indexOf(Marker.GNSSEND.getMarker());
				//convert und set data into gnss field 
				point.setPosition(
						this.stringToGNSS(
								data.substring(gstart, gend)));
			}
			
			logger.debug("Create Waypoint");
			return point;
		}catch(Exception e)
		{
			logger.error("stringToWaypoint - Exception",e);
			return null;
		}
	}
	
	/**
	 * this function can be use to parse an specific Data-String to 
	 * MetaData object
	 * 
	 * @param data - hand over an specific Data-String contains
	 * marker for identify {@link MetaData} data see {@link Marker}
	 * 
	 * @return get an object of {@link MetaData} or <b>null</b> if
	 * parsing was unsuccessfully
	 * 
	 * */
	public MetaData stringToMetaData(String data)
	{
		MetaData md = new MetaData();
		
		try
		{
			//is airplane data available?
			if(data.contains(Marker.AIRPLANESTART.getMarker()) && 
					data.contains(Marker.AIRPLANEEND.getMarker()))
			{	//position of airplane data
				int astart = (data.indexOf(Marker.AIRPLANESTART.getMarker()) + OFFSET);
				int aend = data.indexOf(Marker.AIRPLANEEND.getMarker());
				//convert and set airplane data
				md.setAirplane(
						this.stringToAirplane(
								data.substring(astart, aend)));
			}
			
			//is attitude data available?
			if(data.contains(Marker.ATTITUDESTART.getMarker()) && 
					data.contains(Marker.ATTITUDEEND.getMarker()))
			{	//position of attitude data
				int astart = (data.indexOf(Marker.ATTITUDESTART.getMarker()) + OFFSET);
				int aend = data.indexOf(Marker.ATTITUDEEND.getMarker());
				//convert and set attitude data
				md.setAttitude(
						this.stringToAttitude(
								data.substring(astart, aend)));
			}
			//is course data available?
			if(data.contains(Marker.COURSESTART.getMarker()) && 
					data.contains(Marker.COURSEEND.getMarker()))
			{//position of course data
				int cstart = (data.indexOf(Marker.COURSESTART.getMarker()) + OFFSET);
				int cend = data.indexOf(Marker.COURSEEND.getMarker());
				//convert and set course data
				md.setCourse(
						this.stringToCourse(
								data.substring(cstart, cend)));
			}
			
			logger.debug("Create MetaData");
			return md;
		}catch(Exception e)
		{
			logger.error("stringToMetaData - Exception", e);
			return null;
		}
		

	}
	
	/**
	 * this function can be use to parse an specific Data-String to
	 * Landmark object
	 * 
	 * @param data - hand over an specific Data-String contains
	 * marker for identify {@link Landmark} data see {@link Marker}
	 * 
	 * @return get an object of {@link Landmark} or <b>null</b> if
	 * parsing was unsuccessfully
	 * 
	 * */
	public Landmark stringToLandmark(String data)
	{
		Landmark lm = new Landmark();
		
		try
		{
			//is picture available?
			if(data.contains(Marker.PICTURESTART.getMarker()) && 
					data.contains(Marker.PICTUREEND.getMarker()))
			{
				logger.info("Image contains prove CRC...");
				//prove crc32 checksum is equals parse image
				if(this.proveCRC(Marker.PICTURESTART.getMarker(), 
						Marker.PICTUREEND.getMarker(), 
						data.getBytes()))
				{
					logger.info("CRC OK convert Image");
					//position of picture
					int mstart = (data.indexOf(Marker.PICTURESTART.getMarker()) + OFFSET);
					int mend = data.indexOf(Marker.PICTUREEND.getMarker());
					//convert and set image
					lm.setPictureoflandmark(
							this.byteToBufferedImage(
									data.substring(mstart, mend).getBytes()));
				}else
				{
					logger.info("CRC fail no convert Image");
				}
				
			}
			//is an metadata available?
			if(data.contains(Marker.METADATASTART.getMarker()) && 
					data.contains(Marker.METADATAEND.getMarker()))
			{	//position of metadata
				int mstart = (data.indexOf(Marker.METADATASTART.getMarker()) + OFFSET);
				int mend = data.indexOf(Marker.METADATAEND.getMarker());
				//convert und set metadata
				lm.setMetaData(
						this.stringToMetaData(
								data.substring(mstart, mend)));
			}
			
			logger.debug("Create Landmark");
			return lm;
		}catch(Exception e)
		{
			logger.error("stringToLandmark - Exception", e);
			return null;
		}
		
	}
	
	@Override
	public void run() 
	{
		if(this.rx == null)
			return;//aboard 
		
		//decoding from base64
		byte[] data = Base64.decodeBase64(rx);
		
		
		String stringdata = new String(data);// for index and searching convert to string 
		
		if(stringdata.contains(Marker.METADATASTART.getMarker()))
		{
			int metadatastart = stringdata.indexOf(Marker.METADATASTART.getMarker() + OFFSET);
			int metadataend = stringdata.indexOf(Marker.METADATAEND.getMarker());
			
			//Attitude attitude = this.stringToAttitude(stringdata.substring(metadatastart, metadataend));
			
			stringdata.replaceAll(Marker.METADATASTART.getMarker(), REPLACE);
			stringdata.replaceAll(Marker.METADATAEND.getMarker(), REPLACE);
		}
		
		
		//prove CRC32 if an picture available
		this.iscrcok = this.proveCRC(Marker.PICTURESTART.getMarker(), 
									  Marker.PICTUREEND.getMarker(), data);
		//go into, picture must be available and CRC32 OK!
		if(this.iscrcok)
		{
			int startpicture = (stringdata.indexOf(Marker.PICTURESTART.getMarker()) + OFFSET); // get start index of image
			int endpicture = stringdata.indexOf(Marker.PICTUREEND.getMarker()); // get end index of image
			// select only image data and convert to bytes
			BufferedImage imgdata = this.byteToBufferedImage(stringdata.substring(startpicture, endpicture).getBytes());
		}
		
		//TODO: decoding other data here
		
		this.isdecoded = true;
	}
	
}

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
import de.th_wildau.quadroid.handler.ObserverHandler;
import de.th_wildau.quadroid.models.Attitude;
import de.th_wildau.quadroid.models.Course;
import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.Landmark;
import de.th_wildau.quadroid.models.MetaData;
import de.th_wildau.quadroid.models.Airplane;
import de.th_wildau.quadroid.models.RxData;
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
	/**logger for logging with log4j*/
	private Logger logger = QuadroidMain.logger;
	/**offset for index of search*/
	private static final byte OFFSET = 3;
	/**reference of observer handler to inform all observer*/
	private ObserverHandler observers = null;
	
	
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
	 * public constructor 
	 * 
	 * @param data - hand over received data as encoded base64 byte-array
	 * 
	 * @param observerhandler - hand over an observer handler if should be
	 * inform all observer after decoding
	 * 
	 * */
	public RxDataDecoder(byte[] data, ObserverHandler observerhandler)
	{
		this.observers = observerhandler;
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
		//decoded from base64 
		byte[] decodedbase64img = Base64.decodeBase64(imagedata);
		ByteArrayInputStream bais = new ByteArrayInputStream(decodedbase64img);
		BufferedImage bufferedimage = null;
		try 
		{	//create new BufferedImage object with given data from Stream
			bufferedimage = ImageIO.read(bais);
			logger.debug("Create new BufferedImage object " + 
			bufferedimage.getWidth() + " " + 
			bufferedimage.getHeight());
		} catch (IOException e) 
		{
			logger.error("cant create image");
			logger.debug("byteToBufferedImage",e);
		}
		
		finally
		{
			try 
			{	//close stream
				bais.close();
			} catch (IOException e) 
			{
				logger.error("cant close stream");
				logger.debug("byteToBufferedImage",e);
			}
		}
		
		return bufferedimage;
	}

	/**
	 * prove contain crc32 value
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
	@Deprecated
	protected boolean proveCRC(String startmarker, String endmarker, byte[] data)
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
			}catch(Exception e)
			{
				logger.error("Cast Exception Long.valueOf(crcval): ", e);
			}	
			
			CRC32 crc32 = new CRC32();
			crc32.update(computdata);//comput crc value
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
	 * this Function can be use to parse specific Byte-Data to 
	 * Object
	 * 
	 * @param data - hand over the specific String contains
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
			//convert yaw data from string to float 
			attitude.setYaw(Float.valueOf(data.substring(ystart, yend)));
		}catch(Exception e)
		{
			logger.error("can create yaw");
			logger.debug("stringToAttitude",e);
			return null;
		}
		try
		{
			//get position of roll data 
			int rstart = (data.indexOf(Marker.ROLLSTART.getMarker()) + OFFSET);
			int rend = data.indexOf(Marker.ROLLEND.getMarker());
			//convert roll data from string to float
			attitude.setRoll(Float.valueOf(data.substring(rstart, rend)));
		}catch(Exception e)
		{
			logger.error("can create roll");
			logger.debug("stringToAttitude",e);
			return null;
		}
		try
		{
			//get position of pitch data 
			int pstart = (data.indexOf(Marker.PITCHSTART.getMarker()) + OFFSET);
			int pend = data.indexOf(Marker.PITCHEND.getMarker());
			//convert pitch data from string to float
			attitude.setPitch(Float.valueOf(data.substring(pstart, pend)));
		}catch(Exception e)
		{
			logger.error("can create pitch");
			logger.debug("stringToAttitude",e);
			return null;
		}	
		
		return attitude;	
	}
	
	/**
	 * This function can be use to convert an specific String to
	 * GNSS object
	 * 
	 * @param data - hand over an specific String contains marker
	 * for identifier GNSS data
	 * 
	 * @return get an object of type {@link GNSS}
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
			//convert latitude data from string to float 
			geo.setLatitude(Float.valueOf(data.substring(lastart, laend)));
		}catch(Exception e)
		{
			logger.error("cant create latitude");
			logger.debug("stringToGNSS",e);
			//if no correct data available return value are null it is necessary 
			//to guarantee safety flight gnss data must be right
			return null;
		}
		try
		{
			//get position of longitude data 
			int lostart = (data.indexOf(Marker.LONGITUDESTART.getMarker()) + OFFSET);
			int loend = data.indexOf(Marker.LONGITUDEEND.getMarker());
			//convert longitude data from string to float
			geo.setLongitude(Float.valueOf(data.substring(lostart, loend)));
		}
		catch(Exception e)
		{
			logger.error("cant create longitude");
			logger.debug("stringToGNSS",e);
			//if no correct data available return value are null it is necessary 
			//to guarantee safety flight gnss data must be right
			return null;
		}
		try
		{
			//get position of height data 
			int hstart = (data.indexOf(Marker.HEIGHTSTART.getMarker()) + OFFSET);
			int hend = data.indexOf(Marker.HEIGHTEND.getMarker());
			//convert height data from string to float
			geo.setHeight(Float.valueOf(data.substring(hstart, hend)));
		}catch(Exception e)
		{
			logger.error("cant create height");
			logger.debug("stringToGNSS",e);
			//if no correct data available return value are null it is necessary 
			//to guarantee safety flight gnss data must be right
			return null;
		}	
		return geo;	
	}
	
	/**
	 * This function can be use to convert an specific String
	 * to Course object
	 * 
	 * @param data - hand over an specific String contains
	 * marker for identifier {@link Course} data see {@link Marker}
	 * 
	 * @return get an object of {@link Course} or <b>null</b> 
	 * if parsing was no successfully
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
			
			course.setAngleReference(Float.valueOf(data.substring(astart, aend)));
		}catch(Exception e)
		{
			logger.error("cant create angle");
			logger.debug("stringToCourse",e);
			return null;
		}	
			
		try
		{	
			//get position of speed data 
			int sstart = (data.indexOf(Marker.SPEEDSTART.getMarker()) + OFFSET);
			int send = data.indexOf(Marker.SPEEDEND.getMarker());
			
			course.setSpeed(Float.valueOf(data.substring(sstart, send)));
		}
		catch(Exception e)
		{
			logger.error("cant create speed");
			logger.debug("stringToCourse",e);
			return null;
		}
			return course;
	}
	
	/**
	 * this function can be use to parse an specific String
	 * to QuadroidAirplane
	 * 
	 * @param data - hand over an specific String contains 
	 * marker for identify QuadroidAirplane data
	 * 
	 * @return get an object of {@link Airplane}
	 * 
	 * */
	public Airplane stringToAirplane(String data)
	{
		Airplane qa = new Airplane();
		qa.setBatteryState((byte) 0x55);//default value
		qa.setTime(Long.MAX_VALUE);//default value
			try
			{	//position of battery data
				int bstart = (data.indexOf(Marker.AKKUSTART.getMarker()) + OFFSET);
				int bend = data.indexOf(Marker.AKKUEND.getMarker());
			
				//parse and set data from string to byte
				qa.setBatteryState(Byte.valueOf(data.substring(bstart, bend)));
			}catch(Exception e)
			{
				logger.error("cant create battery");
				logger.debug("stringToAirplane", e);
			}	
			try
			{
				//position of time data
				int tstart = (data.indexOf(Marker.TIMESTART.getMarker()) + OFFSET);
				int tend = data.indexOf(Marker.TIMEEND.getMarker());
			
				//parse and set data from string to long
				qa.setTime(Long.valueOf(data.substring(tstart, tend)));
			}catch(Exception e)
			{
				logger.error("cant create time");
				logger.debug("stringToAirplane", e);
			}
			//is available?
			if(data.contains(Marker.GNSSSTART.getMarker()) && 
					data.contains(Marker.GNSSEND.getMarker()))
			{
				try
				{
					//position of gnss data
					int gstart = (data.indexOf(Marker.GNSSSTART.getMarker()) + OFFSET);
					int gend = data.indexOf(Marker.GNSSEND.getMarker());
					//parse an set data from string to GNSS
					qa.setGeoData(
						this.stringToGNSS(
								data.substring(gstart, gend)));
				}catch(Exception e)
				{
					logger.error("cant create gnss");
					logger.debug("stringToAirplane", e);
				}	
			}	
			return qa;

	}

	/**
	 * This function can be use to parse an specific String to
	 * Waypoint model
	 * 
	 * @param data - hand over an specific String contains 
	 * marker to identify {@link Waypoint} data see {@link Marker}
	 * 
	 * @return get an object of {@link Waypoint} 
	 * 
	 * */
	public Waypoint stringToWaypoint(String data)
	{
		Waypoint point = new Waypoint();
		
			//is an image available?
			if(data.contains(Marker.PICTURESTART.getMarker()) &&
					data.contains(Marker.PICTUREEND.getMarker()))
			{
				
				try
				{
					logger.info("convert image..");
					//position of image data
					int pstart = (data.indexOf(Marker.PICTURESTART.getMarker()) + OFFSET);
					int pend = data.indexOf(Marker.PICTUREEND.getMarker());
					//convert data bytes to buffered image an set value into waypoint
					point.setPictureoflandmark(
							this.byteToBufferedImage(
									data.substring(pstart, pend).getBytes()));
				
				}catch(Exception e)
				{
					logger.error("cant create image");
					logger.debug("stringToWaypoint",e);
				}

			}
			//metadata available?
			if(data.contains(Marker.METADATASTART.getMarker()) && 
					data.contains(Marker.METADATAEND.getMarker()))
			{	
				try
				{
					//start position of metadata
					int mstart = (data.indexOf(Marker.METADATASTART.getMarker()) + OFFSET);
					int mend = data.indexOf(Marker.METADATAEND.getMarker());
					//convert und set metadata into waypoint
					point.setMetaData(
						this.stringToMetaData(
								data.substring(mstart, mend)));
				}catch(Exception e)
				{
					logger.error("cant create metadata");
					logger.debug("stringToWaypoint ",e);
				}	
			}
			//gnss data available?
			if(data.contains(Marker.GNSSSTART.getMarker()) && 
					data.contains(Marker.GNSSEND.getMarker()))
			{
				try
				{
					//position of GNSS data
					int gstart = (data.indexOf(Marker.GNSSSTART.getMarker()) + OFFSET);
					int gend = data.indexOf(Marker.GNSSEND.getMarker());
					//convert und set data into gnss field 
					point.setPosition(
						this.stringToGNSS(
								data.substring(gstart, gend)));
				}catch(Exception e)
				{
					logger.error("cant create gnss");
					logger.debug("stringToWaypoint ",e);
				}	
			}
			
			return point;
	}
	
	/**
	 * this function can be use to parse an specific String to 
	 * MetaData object
	 * 
	 * @param data - hand over an specific String contains
	 * marker for identify {@link MetaData} data see {@link Marker}
	 * 
	 * @return get an object of {@link MetaData}
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
			{	
				try
				{
					//position of airplane data
					int astart = (data.indexOf(Marker.AIRPLANESTART.getMarker()) + OFFSET);
					int aend = data.indexOf(Marker.AIRPLANEEND.getMarker());
					//convert and set airplane data
					md.setAirplane(
						this.stringToAirplane(
								data.substring(astart, aend)));
				}
				catch(Exception e)
				{
					logger.error("cant create airplane");
					logger.debug("stringToMetaData ", e);
				}
			}
			
			//is attitude data available?
			if(data.contains(Marker.ATTITUDESTART.getMarker()) && 
					data.contains(Marker.ATTITUDEEND.getMarker()))
			{	
				try
				{
					//position of attitude data
					int astart = (data.indexOf(Marker.ATTITUDESTART.getMarker()) + OFFSET);
					int aend = data.indexOf(Marker.ATTITUDEEND.getMarker());
					//convert and set attitude data
					md.setAttitude(
						this.stringToAttitude(
								data.substring(astart, aend)));
				}
				catch(Exception e)
				{
					logger.error("cant create attitude");
					logger.debug("stringToMetaData ", e);
				}
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
			
			return md;
		}catch(Exception e)
		{
			logger.error("cant create course");
			logger.debug("stringToMetaData ",e);
			return md;
		}
		

	}
	
	/**
	 * this function can be use to parse an specific String to
	 * Landmark object
	 * 
	 * @param data - hand over an specific String contains
	 * marker for identify {@link Landmark} data see {@link Marker}
	 * 
	 * @return get an object of {@link Landmark}
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
				try
				{
					int mstart = (data.indexOf(Marker.PICTURESTART.getMarker()) + OFFSET);
					int mend = data.indexOf(Marker.PICTUREEND.getMarker());
					//convert and set image
					lm.setPictureoflandmark(
							this.byteToBufferedImage(
									data.substring(mstart, mend).getBytes()));
				}
				catch(Exception e)
				{
					logger.error("Cant create image");
					logger.debug("stringToLandmark", e);
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
			return lm;
		}catch(Exception e)
		{
			logger.error("cant create metadata");
			logger.debug("stringToLandmark ", e);
			return lm;
		}
		
	}
	
	
	
	@Override
	public void run() 
	{
		
		long timetodecode = System.currentTimeMillis();
		
		if(this.rx == null)
			return;//aboard
		StringBuilder data = new StringBuilder();
		
		try
		{	// for windows --> UTF-8
			data.append(new String(this.rx ));
			
		}catch(Exception e)
		{
			logger.error("cant convert to UTF-8");
			logger.debug("decode run",e);
			return;
		}
		
		RxData container = new RxData();
		byte mindata = (OFFSET * 2);// min data must be available
		int loopcount = 0;//loop counter, maximum loop 100
	 do
	  {	
		try
		{
			//contains waypoint data?
			if(data.substring(0).contains(Marker.WAYPOINTSTART.getMarker()) && 
				data.substring(0).contains(Marker.WAYPOINTEND.getMarker()))
			{
				//position of waypoint data
				int wps = (data.indexOf(Marker.WAYPOINTSTART.getMarker()) + OFFSET);
				int wpe = data.indexOf(Marker.WAYPOINTEND.getMarker());
				//select substring with waypoint data
				String wpdata = data.substring(wps, wpe);
				//pars string to waypoint data
				Waypoint wp = this.stringToWaypoint(wpdata);
				//put to container
				container.getWaypointlist().add(wp);
				//remove waypoint data from string (removing reduce dataset)
				data.delete(wps - OFFSET, wpe + OFFSET);//+OFFSET and -OFFSET for removing marker
			}
		}catch(Exception e)
		{
			logger.error("cant create waypoint");
			logger.debug("waypoint",e);
		}
		try
		{
			//contains landmark data ?
			if(data.substring(0).contains(Marker.LANDMARKSTART.getMarker()) && 
					data.substring(0).contains(Marker.LANDMARKEND.getMarker()))
			{	//position of landmark data 
				int lms = (data.indexOf(Marker.LANDMARKSTART.getMarker()) + OFFSET);
				int lme = data.indexOf(Marker.LANDMARKEND.getMarker());
				//select substring with landmark data
				String lmdata = data.substring(lms, lme);
				//pars string to landmark
				Landmark lm = this.stringToLandmark(lmdata);
				//put to container
				container.getLandmarklist().add(lm);
				//remove landmark data from string (removing reduce dataset)
				data.delete(lms - OFFSET, lme + OFFSET);
			}
		}catch(Exception e)
		{
			logger.error("cant create landmark");
			logger.debug("landmark",e);
		}
		try
		{
			//contains metadata data?
			if(data.substring(0).contains(Marker.METADATASTART.getMarker()) && 
					data.substring(0).contains(Marker.METADATAEND.getMarker()))
			{
				//position of metadata data
				int mds = (data.indexOf(Marker.METADATASTART.getMarker()) + OFFSET);
				int mde = data.indexOf(Marker.METADATAEND.getMarker());
				//select substring with metadata data
				String mddata = data.substring(mds, mde);
				//parse string to metadata 
				MetaData md = this.stringToMetaData(mddata);
				//put to container
				container.getMetadatalist().add(md);
				//remove substring from string (removing reduce dataset)
				data.delete(mds - OFFSET, mde + OFFSET);//+ and - offset for removing marker
			}
		}catch(Exception e)
		{
			logger.error("cant create metadata");
			logger.debug("metadata",e);
		}	
		try
		{
			//contains QuadroidAirplane data?
			if(data.substring(0).contains(Marker.AIRPLANESTART.getMarker()) && 
					data.substring(0).contains(Marker.AIRPLANEEND.getMarker()))
			{
				//position of quadroidairplane data
				int qas = (data.indexOf(Marker.AIRPLANESTART.getMarker()) + OFFSET);
				int qae = data.indexOf(Marker.AIRPLANEEND.getMarker());
				//select substring with quadroidairplane data
				String qadata = data.substring(qas, qae);
				//parse string to quadroidairplane
				Airplane qa = this.stringToAirplane(qadata);
				//put to container
				container.getAirplanelist().add(qa);
				//remove substring from data string (removing reduce dataset)
				data.delete(qas - OFFSET, qae + OFFSET);
			}
		}catch(Exception e)
		{
			logger.error("cant create airplane");
			logger.debug("airplane",e);
		}
		try
		{
			//contains GNSS data?
			if(data.substring(0).contains(Marker.GNSSSTART.getMarker()) && 
					data.substring(0).contains(Marker.GNSSEND.getMarker()))
			{	//position of GNSS data
				int gns = (data.indexOf(Marker.GNSSSTART.getMarker()) + OFFSET);
				int gne = data.indexOf(Marker.GNSSEND.getMarker());
				//select substring with GNSS data
				String gndata = data.substring(gns, gne);
				//parse string to GNSS 
				GNSS gnss = this.stringToGNSS(gndata);
				//put to container
				container.getGnsslist().add(gnss);
				//remove substring from data string (removing reduce dataset)
				data.delete(gns - OFFSET, gne + OFFSET);//+3 and -3 for removing marker
			}
		}catch(Exception e)
		{
			logger.error("cant create gnss");
			logger.debug("gnss",e);
		}
		try
		{
			//contains Course data?
			if(data.substring(0).contains(Marker.COURSESTART.getMarker()) && 
					data.substring(0).contains(Marker.COURSEEND.getMarker()))
			{
				//startposition of course data
				int cos = (data.indexOf(Marker.COURSESTART.getMarker()) + OFFSET);
				int coe = data.indexOf(Marker.COURSEEND.getMarker());
				//select substring with data for course
				String codata = data.substring(cos, coe);
				//parse string to course
				Course course = this.stringToCourse(codata);
				//put to container
				container.getCourselist().add(course);
				//removsubstring from data string (removing reduce dataset)
				data.delete(cos - OFFSET, coe + OFFSET);//+ and - OFFSET for removing marker
				
			}
		}catch(Exception e)
		{
			logger.error("cant create course");
			logger.debug("course",e);
		}
		try
		{
			//Contains Attitude Data?
			if(data.substring(0).contains(Marker.ATTITUDESTART.getMarker()) && 
					data.substring(0).contains(Marker.ATTITUDEEND.getMarker()))
			{//position of Attitude data
				int ats = (data.indexOf(Marker.ATTITUDESTART.getMarker()) + OFFSET);
				int ate = data.indexOf(Marker.ATTITUDEEND.getMarker());
				//select substring with attitude data
				String atdata = data.substring(ats, ate);
				Attitude attitude = this.stringToAttitude(atdata);
				//put to container
				container.getAttitudelist().add(attitude);
				//removing data from data string 
				data.delete(ats - OFFSET, ate + OFFSET); //+ and - OFFSET for removing marker
			}
		}catch(Exception e)
		{
			logger.error("cant create attitude");
			logger.debug("attitude",e);
		}
		
		loopcount++;
	  }	while(data.length() > mindata  && loopcount <= 100);
	 
	 	timetodecode = (System.currentTimeMillis() - timetodecode);
	 	logger.info("decoded time: " + timetodecode + " ms");
	 	
	 	this.observers.notification(container);
	 	
		this.isdecoded = true;
	}
	
}

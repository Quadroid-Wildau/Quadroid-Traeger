/**
 * 
 */
package de.th_wildau.quadroid.test.decoder;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import de.th_wildau.quadroid.QuadroidMain;
import de.th_wildau.quadroid.decoder.RxDataDecoder;
import de.th_wildau.quadroid.encoder.TxDataEncoder;
import de.th_wildau.quadroid.enums.Marker;
import de.th_wildau.quadroid.models.Airplane;
import de.th_wildau.quadroid.models.Attitude;
import de.th_wildau.quadroid.models.Course;
import de.th_wildau.quadroid.models.GNSS;
import de.th_wildau.quadroid.models.Landmark;
import de.th_wildau.quadroid.models.MetaData;
import de.th_wildau.quadroid.models.Waypoint;

/**
 * JUnit Testclass for RxDataDecoder
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 12.01.2014, (JDK 7)
 * 
 *
 */
public class RxDataDecoderTest {

	/**test data*/
	private byte[] validdatagnss = null;
	//for logging 
	private static final String LOGGERPROPERTIES = "log4j.properties";
	//test object
	private GNSS geo = null;
	//reference object
	private GNSS refgeo = null;
	//data offset for marker
	private static final byte OFFSET = 3;
	//test string
	private String attitude = null;
	//result from decoding
	private Attitude resultattitude = null;
	//reference to prove
	private Attitude refattitude = null;
	//reference course
	private Course refcourse = null;
	//valid reference data 
	private String validcourse = null;
	//ref and test data for airplane
	private Airplane refairplane = null;
	private String validairplane = null;
	//ref and test data for landmark
	private Landmark reflandmark = null;
	private String validlandmark = null;
	//ref and test data for metadata
	private MetaData refmetadata = null;
	private String validmetadata = null;
	//ref and test data for waypoint
	private Waypoint refwaypoint = null;
	private String validwaypoint = null;
	
	//de- and encoder
	private TxDataEncoder encoder = null;
	private RxDataDecoder decoder = null;
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		//set logger for using de- and encoder all of this will be logging
		PropertyConfigurator.configure(LOGGERPROPERTIES);
		QuadroidMain.logger = LoggerFactory.getLogger(RxDataDecoderTest.class.getName());
		
		
		encoder = new TxDataEncoder();
		decoder = new RxDataDecoder(null);
		
		
		this.geo = new GNSS();
		
		//Valid reference position 
		this.refgeo = new GNSS();
		this.refgeo.setLatitude(52.4562f);
		this.refgeo.setLongitude(13.2134f);
		this.refgeo.setHeight(300.001f);
		//valid reference attitude
		this.refattitude = new Attitude();
		this.refattitude.setPitch(100);
		this.refattitude.setRoll(200);
		this.refattitude.setYaw(300);
		//valid reference for course
		this.refcourse = new Course();
		this.refcourse.setAngleReference(11.11f);
		this.refcourse.setSpeed(123.0f);
		//valid reference for airplane
		this.refairplane = new Airplane();
		this.refairplane.setBatteryState((short) 12);
		this.refairplane.setGeoData(geo);
		this.refairplane.setTime(159753);
		//valid reference for metadata
		this.refmetadata = new MetaData();
		this.refmetadata.setAirplane(refairplane);
		this.refmetadata.setAttitude(refattitude);
		this.refmetadata.setCourse(refcourse);
		//valid reference for landmark
		this.reflandmark = new Landmark();
		this.reflandmark.setMetaData(refmetadata);
		this.reflandmark.setPictureoflandmark(null);
		//valid reference for waypoint
		this.refwaypoint = new Waypoint();
		this.refwaypoint.setMetaData(refmetadata);
		this.refwaypoint.setPictureoflandmark(null);
		this.refwaypoint.setPosition(geo);
		
		
		//create test data
		this.validdatagnss = encoder.geodataToBytes(refgeo);
		this.attitude = new String(encoder.attitudeToBytes(this.refattitude));
		this.validcourse = new String(encoder.courseToBytes(this.refcourse));
		this.validairplane = new String(encoder.quadroidairplaneToBytes(refairplane));
		this.validmetadata = new String(encoder.metadataToBytes(refmetadata));
		this.validlandmark = new String(encoder.landmarkToBytes(reflandmark));
		this.validwaypoint = new String(encoder.waypointToBytes(refwaypoint));
	
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception 
	{
		//clean all data 
		this.geo = null;
		this.refgeo = null;
		
		this.refattitude = null;
		this.resultattitude = null;
		
		this.refairplane = null;
		this.refcourse = null;
		this.reflandmark = null;
		this.refmetadata = null;
		this.refwaypoint = null;
		
		this.validairplane = null;
		this.validcourse = null;
		this.validlandmark = null;
		this.validmetadata = null;
		this.validwaypoint = null;
	}

	
	
	/**
	 * Test method for {@link de.th_wildau.quadroid.decoder.RxDataDecoder#stringToAttitude(java.lang.String)}.
	 */
	@Test
	public void testStringToAttitude() 
	{
		resultattitude = decoder.stringToAttitude(this.attitude);
		//result can not be null
		assertNotNull(resultattitude);
		//data must be equals with reference data 
		assertTrue(resultattitude.getPitch() == refattitude.getPitch());
		assertTrue(resultattitude.getRoll() == refattitude.getRoll());
		assertTrue(resultattitude.getYaw() == refattitude.getYaw());
	}

	/**
	 * Test method for {@link de.th_wildau.quadroid.decoder.RxDataDecoder#stringToGNSS(java.lang.String)}.
	 */
	@Test
	public void testStringToGNSS() 
	{
		String data = new String(validdatagnss);
		try
		{
			//get position of latitude data 
			int lastart = (data.indexOf(Marker.LATITUDESTART.getMarker()) + OFFSET);
			int laend = data.indexOf(Marker.LATITUDEEND.getMarker());
			
			assertTrue((lastart >= 0 && lastart < validdatagnss.length));
			assertTrue((laend >= 0 && laend < validdatagnss.length));
			
			//convert latitude data from string to float 
			String substring = data.substring(lastart, laend);
			assertNotNull(substring);
			
			float result = Float.valueOf(substring);
			geo.setLatitude(result);
			
			assertTrue((geo.getLatitude() == this.refgeo.getLatitude()));
		}catch(Exception e)
		{
			fail("throw Exception");
			//if no correct data available return value are null it is necessary 
			//to guarantee safety flight gnss data must be right
			
		}
		try
		{
			//get position of longitude data 
			int lostart = (data.indexOf(Marker.LONGITUDESTART.getMarker()) + OFFSET);
			int loend = data.indexOf(Marker.LONGITUDEEND.getMarker());
			
			assertTrue((lostart >= 0 && lostart < validdatagnss.length));
			assertTrue((loend >= 0 && loend < validdatagnss.length));
			
			String substring = data.substring(lostart, loend);
			assertNotNull(substring);
			
			float result = Float.valueOf(substring);
	
			//convert longitude data from string to float
			geo.setLongitude(result);
			assertTrue((geo.getLongitude() == this.refgeo.getLongitude()));
		}
		catch(Exception e)
		{
			fail("throws Exception");
			//if no correct data available return value are null it is necessary 
			//to guarantee safety flight gnss data must be right
			
		}
		try
		{
			//get position of height data 
			int hstart = (data.indexOf(Marker.HEIGHTSTART.getMarker()) + OFFSET);
			int hend = data.indexOf(Marker.HEIGHTEND.getMarker());
			
			assertTrue((hstart >= 0 && hstart < validdatagnss.length));
			assertTrue((hend >= 0 && hend < validdatagnss.length));
			
			String substring = data.substring(hstart, hend);
			assertNotNull(substring);
			float result = Float.valueOf(substring);
			//convert height data from string to float
			geo.setHeight(result);
			assertTrue((geo.getLongitude() == this.refgeo.getLongitude()));
		}catch(Exception e)
		{
			fail("throws Exception");
			//if no correct data available return value are null it is necessary 
			//to guarantee safety flight gnss data must be right
			
		}
	}

	/**
	 * Test method for {@link de.th_wildau.quadroid.decoder.RxDataDecoder#stringToCourse(java.lang.String)}.
	 */
	@Test
	public void testStringToCourse() 
	{
		// hand oder valid string to prove decoder
		Course course = decoder.stringToCourse(validcourse);
		//prove results
		assertNotNull(course);
		//all data must be equals
		assertTrue(course.getAngleReference() == this.refcourse.getAngleReference());
		assertTrue(course.getSpeed() == this.refcourse.getSpeed());
	}

	/**
	 * Test method for {@link de.th_wildau.quadroid.decoder.RxDataDecoder#stringToAirplane(java.lang.String)}.
	 */
	@Test
	public void testStringToAirplane() 
	{	
		//hand over an valid string contains protocol specific data to prove decoder
		Airplane airplane = decoder.stringToAirplane(validairplane);
		//prove results
		assertNotNull(airplane);
		//prove contain data 
		assertTrue(airplane.getBatteryState() == this.refairplane.getBatteryState());
		assertTrue(airplane.getTime() == this.refairplane.getTime());
		
			GNSS gnssairplane = airplane.GeoData();
			//prove geo data
			assertTrue(gnssairplane.getLatitude() == this.geo.getLatitude());
			assertTrue(gnssairplane.getLongitude() == this.geo.getLongitude());
			assertTrue(gnssairplane.getHeight() == this.geo.getHeight());
	}

	/**
	 * Test method for {@link de.th_wildau.quadroid.decoder.RxDataDecoder#stringToWaypoint(java.lang.String)}.
	 */
	@Test
	public void testStringToWaypoint() 
	{
		//hand over an valid string to prove decoder
		Waypoint wp = decoder.stringToWaypoint(this.validwaypoint);
		//prove return value
		assertNotNull(wp);
		
		MetaData md = wp.getMetaData();
		BufferedImage img = wp.getPictureoflandmark();
		GNSS gnsswp = wp.getPosition();
		//prove objects
		assertNull(img);//profe image must be null
		assertNotNull(gnsswp);//prove gnss data
		
				//prove geo data
				assertTrue(gnsswp.getLatitude() == this.geo.getLatitude());
				assertTrue(gnsswp.getLongitude() == this.geo.getLongitude());
				assertTrue(gnsswp.getHeight() == this.geo.getHeight());
		
		assertNotNull(md);//prove metadata
			
				Attitude attitude = md.getAttitude();
				
						assertNotNull(attitude);//prove attitude
						//prove attitude data
						assertTrue(attitude.getPitch() == this.refattitude.getPitch());
						assertTrue(attitude.getRoll() == this.refattitude.getRoll());
						assertTrue(attitude.getYaw() == this.refattitude.getYaw());
				
				Course course = md.getCourse();
				
						assertNotNull(course);//prove course
						//prove values
						assertTrue( course.getAngleReference() == this.refcourse.getAngleReference());
						assertTrue( course.getSpeed() == this.refcourse.getSpeed());
		
				Airplane airplane = md.getAirplane();
				
						assertNotNull(airplane);//prove airplane
						
						GNSS gnssairplane = airplane.GeoData();
						
								assertNotNull(gnssairplane);
								
								//prove geo data
								assertTrue(gnssairplane.getLatitude() == this.geo.getLatitude());
								assertTrue(gnssairplane.getLongitude() == this.geo.getLongitude());
								assertTrue(gnssairplane.getHeight() == this.geo.getHeight());
			
						//prove airplane data
						assertTrue(airplane.getBatteryState() == this.refairplane.getBatteryState());
						assertTrue(airplane.getTime() == this.refairplane.getTime());		

	}

	/**
	 * Test method for {@link de.th_wildau.quadroid.decoder.RxDataDecoder#stringToMetaData(java.lang.String)}.
	 */
	@Test
	public void testStringToMetaData() 
	{
		MetaData md = decoder.stringToMetaData(validmetadata);
		
		assertNotNull(md);//prove metadata
		
		Attitude attitude = md.getAttitude();
		
				assertNotNull(attitude);//prove attitude
				//prove attitude data
				assertTrue(attitude.getPitch() == this.refattitude.getPitch());
				assertTrue(attitude.getRoll() == this.refattitude.getRoll());
				assertTrue(attitude.getYaw() == this.refattitude.getYaw());
		
		Course course = md.getCourse();
		
				assertNotNull(course);//prove course
				//prove values
				assertTrue( course.getAngleReference() == this.refcourse.getAngleReference());
				assertTrue( course.getSpeed() == this.refcourse.getSpeed());

		Airplane airplane = md.getAirplane();
		
				assertNotNull(airplane);//prove airplane
				
				GNSS gnssairplane = airplane.GeoData();
				
						assertNotNull(gnssairplane);
						
						//prove geo data
						assertTrue(gnssairplane.getLatitude() == this.geo.getLatitude());
						assertTrue(gnssairplane.getLongitude() == this.geo.getLongitude());
						assertTrue(gnssairplane.getHeight() == this.geo.getHeight());
	
				//prove airplane data
				assertTrue(airplane.getBatteryState() == this.refairplane.getBatteryState());
				assertTrue(airplane.getTime() == this.refairplane.getTime());
	}

	/**
	 * Test method for {@link de.th_wildau.quadroid.decoder.RxDataDecoder#stringToLandmark(java.lang.String)}.
	 */
	@Test
	public void testStringToLandmark() 
	{	//hand over an sring contains valid data for testing decoder
		Landmark lm = decoder.stringToLandmark(this.validlandmark);
		
		assertNotNull(lm);//prove landmark
		
			BufferedImage img = lm.getPictureoflandmark();
			//prove image
			assertNull(img);//must be null
			
				MetaData md = lm.getMetaData();
			
				assertNotNull(md);//prove metadata
				
				Attitude attitude = md.getAttitude();
				
						assertNotNull(attitude);//prove attitude
						//prove attitude data
						assertTrue(attitude.getPitch() == this.refattitude.getPitch());
						assertTrue(attitude.getRoll() == this.refattitude.getRoll());
						assertTrue(attitude.getYaw() == this.refattitude.getYaw());
				
				Course course = md.getCourse();
				
						assertNotNull(course);//prove course
						//prove values
						assertTrue( course.getAngleReference() == this.refcourse.getAngleReference());
						assertTrue( course.getSpeed() == this.refcourse.getSpeed());

				Airplane airplane = md.getAirplane();
				
						assertNotNull(airplane);//prove airplane
						
						GNSS gnssairplane = airplane.GeoData();
						
								assertNotNull(gnssairplane);
								
								//prove geo data
								assertTrue(gnssairplane.getLatitude() == this.geo.getLatitude());
								assertTrue(gnssairplane.getLongitude() == this.geo.getLongitude());
								assertTrue(gnssairplane.getHeight() == this.geo.getHeight());
			
						//prove airplane data
						assertTrue(airplane.getBatteryState() == this.refairplane.getBatteryState());
						assertTrue(airplane.getTime() == this.refairplane.getTime());
	}

}

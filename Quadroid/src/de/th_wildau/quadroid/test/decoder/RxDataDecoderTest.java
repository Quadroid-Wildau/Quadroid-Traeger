/**
 * 
 */
package de.th_wildau.quadroid.test.decoder;

import static org.junit.Assert.*;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import de.th_wildau.quadroid.QuadroidMain;
import de.th_wildau.quadroid.decoder.RxDataDecoder;
import de.th_wildau.quadroid.encoder.TxDataEncoder;
import de.th_wildau.quadroid.enums.Marker;
import de.th_wildau.quadroid.models.Attitude;
import de.th_wildau.quadroid.models.GNSS;

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
	private static final String LOGGERPROPERTIES = "log4j.properties";
	private GNSS geo = null;
	private GNSS refgeo = null;
	private static final byte OFFSET = 3;
	private String attitude = null;
	private Attitude resultattitude = null;
	private Attitude refattitude = null;
	private TxDataEncoder encoder = null;
	private RxDataDecoder decoder = null;
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
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
		
		this.validdatagnss = encoder.geodataToBytes(refgeo);
		this.attitude = new String(encoder.attitudeToBytes(this.refattitude));
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception 
	{
		this.geo = null;
		this.refgeo = null;
		
		this.refattitude = null;
		this.resultattitude = null;
	}

	
	
	
	
	
	/**
	 * Test method for {@link de.th_wildau.quadroid.decoder.RxDataDecoder#stringToAttitude(java.lang.String)}.
	 */
	@Test
	public void testStringToAttitude() 
	{
		resultattitude = decoder.stringToAttitude(this.attitude);
		
		assertNotNull(resultattitude);
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
	public void testStringToCourse() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.th_wildau.quadroid.decoder.RxDataDecoder#stringToAirplane(java.lang.String)}.
	 */
	@Test
	public void testStringToAirplane() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.th_wildau.quadroid.decoder.RxDataDecoder#stringToWaypoint(java.lang.String)}.
	 */
	@Test
	public void testStringToWaypoint() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.th_wildau.quadroid.decoder.RxDataDecoder#stringToMetaData(java.lang.String)}.
	 */
	@Test
	public void testStringToMetaData() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.th_wildau.quadroid.decoder.RxDataDecoder#stringToLandmark(java.lang.String)}.
	 */
	@Test
	public void testStringToLandmark() {
		fail("Not yet implemented");
	}

}

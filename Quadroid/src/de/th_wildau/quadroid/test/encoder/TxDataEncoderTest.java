package de.th_wildau.quadroid.test.encoder;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import de.th_wildau.quadroid.QuadroidMain;
import de.th_wildau.quadroid.decoder.RxDataDecoder;
import de.th_wildau.quadroid.encoder.TxDataEncoder;
import de.th_wildau.quadroid.enums.Marker;

/**
 * JUnit Testclass for TxDataEncoder
 * 
 * @author Thomas Rohde TM-12, trohde(at)th-wildau.de
 * @version 1.0, 12.01.2014, (JDK 7)
 * 
 * */

public class TxDataEncoderTest {

	// for logging
	private static final String LOGGERPROPERTIES = "log4j.properties";
	//data for append test
	private byte[] data1 = null;
	private byte[] data2 = null;
	//test data for prove image encoder
	private BufferedImage img = null;
	
	private TxDataEncoder encoder = null;
	private RxDataDecoder decoder = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// set logger for using de- and encoder all of this will be logging
		PropertyConfigurator.configure(LOGGERPROPERTIES);
		QuadroidMain.logger = LoggerFactory.getLogger(TxDataEncoderTest.class
				.getName());

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
	}

	@Before
	public void setUp() throws Exception 
	{
		encoder = new TxDataEncoder();
		decoder = new RxDataDecoder(null);
		//create empty buffered image for testing
		this.img = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		//create data for testing
		this.data1 = "12345".getBytes();
		this.data2 = "67890".getBytes();
	}

	@After
	public void tearDown() throws Exception 
	{
		//clean all data
		encoder = null;
		
		this.data1 = null;
		this.data2 = null;
	}

	
	
	@Test
	public void testImageToByteArray() 
	{
		byte[] coded = encoder.imageToByteArray(this.img, Marker.IMAGETYPE.getMarker());
		//prove encoding
		assertNotNull(coded);
		//decoding
		BufferedImage image = decoder.byteToBufferedImage(coded);
		//prove results
		assertNotNull(image);
		//results must be equal to new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		assertTrue(image.getWidth() == 640);
		assertTrue(image.getHeight() == 480);
	}

	@Test
	public void testAppendBytes() 
	{
		byte[] result = (encoder.appendBytes(this.data1, this.data2));
		//prove result
		assertNotNull(result);
		assertArrayEquals("1234567890".getBytes(), result);
		
		result = (encoder.appendBytes(this.data2, this.data1));
		
		//prove result
		assertNotNull(result);
		assertArrayEquals("6789012345".getBytes(), result);
	}

}

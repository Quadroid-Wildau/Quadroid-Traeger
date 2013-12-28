package de.th_wildau.quadroid.landmark;

import java.awt.Color;
import java.awt.image.*;

import javax.activation.UnsupportedDataTypeException;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


/**
 * @author Stephan Funke
 */
public class OpCheckLandmark {
	
	private final float hue = 0.078f;
	private final float tolerance = 0.08f;
	private final float minSaturation = 0.5f;
	private final float minBrightness = 0.1f;
	private int height = 0, width = 0;
	private int col = 420, row = 260;
	private final int minRadius = 10;
	private final int maxRadius = 500;
	private int houghThreshold = 45;
	private WritableRaster destination;
	
	
	
	/**
	 * Constructor
	 */
	public OpCheckLandmark()
	{
		
	}
	
	
	/**
	 * Computes a binary image by applying a threshold to the source image.
	 * A pixel is set to value 255 if it is larger than the threshold, otherwise
	 * it is set to 0.
	 */
	public boolean findLm(Raster src, WritableRaster dest)
	{
		// Initialisierung
		destination = dest;
		width = src.getWidth();
		height = src.getHeight();
//		int size = width*height;
//		int[] srcPixels = src.getPixels(0, 0, width, height, (int[])null);
//		int[] destPixels = new int[size];
//		float[] hsv = new float[3];
//		
//		for (int i=0; i<size; i++) {
//			// Read RGB values from source raster
//			int red = srcPixels[i*3];
//			int green = srcPixels[i*3+1];
//			int blue = srcPixels[i*3+2];
//			// Transform to HSV
//			Color.RGBtoHSB(red, green, blue, hsv);
//			
//			if(i == 240000 || i == 81366 || i == 115456)
//				System.out.println("H: "+hsv[0]+ " S: "+hsv[1]+ " V: "+hsv[2]);
//			
//			// Calculate the difference of the target hue from the actual hue
//			float hueDiff = hue - hsv[0];
//			if (hueDiff < -0.5)
//				hueDiff += 1.0f;
//			if (hueDiff > 0.5)
//				hueDiff -= 1.0f;
//			// Segmentation
//			if (Math.abs(hueDiff) < tolerance && hsv[1] > minSaturation && hsv[2] > minBrightness)
//				destPixels[i] = 255;
//			else
//				destPixels[i] = 0;
//		}
//		System.out.println(destPixels.length);
//		// Pixelfeld im Zielbild setzen
//		dest.setPixels(0, 0, width, height, destPixels);
//		int help = 0;
//		System.out.println(destPixels[row*width+col]);
//		//if(destPixels[row*width+col]==255){
//		for (int i = 0; i < 10; i++){
//			//dest = findArea(dest,row ,col);
//			dest = erodeArea(dest);
//			
//		}
//		for(int c = 0; c < size; c++){
//			if(destPixels[c] == 255)
//				help++;
//		}
//		System.out.println("Anzahl weißer Punkte: "+help);
//		else{
//			
//		}
		//System.out.println(count);
		
//		dest = findCircles(src);
//		if(dest == null)
//			dest = (WritableRaster) src;
//		return dest;
		return findCircles(src);
	}
	
	
	
//	private WritableRaster erodeArea(WritableRaster raster){
//		int[] rasterPixels = raster.getPixels(0, 0, width, height, (int[])null);
//		int[] destPixels = new int[width*height];
//		
//		// Morphologische Operation
//		Morphology morph = new Morphology();
//		morph.erode(255, "1,1,1|1,1,1|1,1,1", rasterPixels, destPixels, width, height);
//
//		// Pixelfeld im Zielbild setzen
//		raster.setPixels(0, 0, width, height, destPixels);
//		return raster;
//	
//	}
	
	//Erode and Segmentation of Colour in the Circlearea
	private boolean findColourInCircle(double rad, Point mPoint, Raster src){
		
		int size = width*height;
		int[] srcPixels = src.getPixels(0, 0, width, height, (int[])null);
		int[] destPixels = new int[size];
		float[] hsv = new float[3];
		double dist = 0; //Square dist between one Point and the middle of the Circle
		double srad = rad*rad;
		
		for (int i=0; i<size; i++) {
			// Read RGB values from source raster
			int red = srcPixels[i*3];
			int green = srcPixels[i*3+1];
			int blue = srcPixels[i*3+2];
			// Transform to HSV
			Color.RGBtoHSB(red, green, blue, hsv);
			
			if(i == 240000 || i == 81366 || i == 115456)
				System.out.println("H: "+hsv[0]+ " S: "+hsv[1]+ " V: "+hsv[2]);
			
			// Calculate the difference of the target hue from the actual hue
			float hueDiff = hue - hsv[0];
			if (hueDiff < -0.5)
				hueDiff += 1.0f;
			if (hueDiff > 0.5)
				hueDiff -= 1.0f;
			dist = (i%width - mPoint.x) * (i%width - mPoint.x) + (i/width - mPoint.y) * (i/width - mPoint.y);
			// Segmentation in the circlearea
			if (Math.abs(hueDiff) < tolerance && hsv[1] > minSaturation && hsv[2] > minBrightness && dist <= srad)
				destPixels[i] = 255;
			else
				destPixels[i] = 0;
		}
		System.out.println(destPixels.length);
		// Pixelfeld im Zielbild setzen
		destination.setPixels(0, 0, width, height, destPixels);
		int help = 0;
		System.out.println(destPixels[row*width+col]);
		//if(destPixels[row*width+col]==255){
//		for (int i = 0; i < 10; i++)
//			destination = erodeArea(destination);
			
		
		for(int c = 0; c < size; c++){
			if(destPixels[c] == 255)
				help++;
		}
		System.out.println("Anzahl weißer Punkte: "+help);
		if(rad*rad/4<help)
			return true;
		
		return false;
	}
	
	/**
	 * Find Circles in the given colourimage
	 * if no circle is found, houghtreshold will be decreased step by step from 45 to a minimum of 25
	 * @param src
	 * @return
	 */
	private boolean findCircles(Raster src){
		Mat in = new Mat();
		boolean erg = false;
		try {
			in = OpenCvConverter.convertToMat(src);
		} catch (UnsupportedDataTypeException e) {
			e.printStackTrace();
		}
		
		// Convert the src image to grey
		Mat grey = new Mat(in.rows(), in.cols(), CvType.CV_8UC1);
		Imgproc.cvtColor(in, grey, Imgproc.COLOR_BGR2GRAY);
		
		// Reduce the noise in the image to improve circle detection
		Imgproc.GaussianBlur(grey, grey, new Size(9, 9), 2, 2);
		
		// This matrix will contain the circles in the format
		
		Mat circles = new Mat();
		// Call OpenCV Hough transform
		int histogramResolution = 1;
		double minDistOfCircles = 2*minRadius+2;
		double cannyThreshold = 200;
		
		//reduce the Houghtreshold if no circle is found
		for (int i = 0; i < 5; i = i++){
			Imgproc.HoughCircles(grey, circles, Imgproc.CV_HOUGH_GRADIENT, histogramResolution, 
				minDistOfCircles, cannyThreshold, houghThreshold, minRadius, maxRadius);
			int numCircles = circles.cols();

			System.out.println(numCircles);
			//If minimum 1 circle is found, check for colour in this circle(s)
			if(numCircles != 0){
				System.out.println("" + numCircles + " found.");
				//double[] rad = new double[numCircles];
				//Point[] points = new Point[numCircles];
				
				//Coloursegmentation in the area of found circle
				for (int j = 0; j < numCircles; j++){
					if(true == findColourInCircle(circles.get(0,j)[2], new Point(circles.get(0,j)[0], circles.get(0,j)[1]), src))
						erg = true;
				}
				return erg;
			}
			houghThreshold = houghThreshold - 5; //reduce the houghTreshold if no circle is found
		}
		
		return false;
	}

	
//	private WritableRaster findArea(WritableRaster raster, int row, int col){
//		Point seedPoint = new Point(row, col);
//		// Converting to OpenCV Mat data structure
//		Mat in;
//		try {
//			in = OpenCvConverter.convertToMat(raster);
//		} catch (UnsupportedDataTypeException e) {return null;}
//		
//		// Create a second image with the same properties
//		Mat out = new Mat(height+2, width+2, CvType.CV_8UC1, new Scalar(0));
//		
//		Scalar newVal = new Scalar(255);
//		Scalar lowDiff = new Scalar(0);
//		Scalar upDiff = new Scalar(0);
//		
//		// Call OpenCV floodfill operator
//		 Imgproc.floodFill(in,out,seedPoint, newVal, null, lowDiff, upDiff, Imgproc.FLOODFILL_MASK_ONLY);
//		// Set pixels in destination image
//		int[] destPixels = OpenCvConverter.convertToIntegerArray(in);
//		raster.setPixels(0, 0, width, height, destPixels);
//		
//		return raster;
//	}
	
}

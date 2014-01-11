package de.th_wildau.quadroid.landmark;

import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvPointFrom32f;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_imgproc.BORDER_DEFAULT;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_HOUGH_GRADIENT;
import static com.googlecode.javacv.cpp.opencv_imgproc.GaussianBlur;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvHoughCircles;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvPoint2D32f;
import com.googlecode.javacv.cpp.opencv_core.CvPoint3D32f;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


/**
 * Class thats detect an orange ball as a landmark.
 * @author Stephan Funke
 */
public class OpCheckLandmark {
	
	private final float hue = 0.05f;
	private final float tolerance = 0.05f;
	private final float minSaturation = 0.5f;
	private final float minBrightness = 0.1f;
	private int height = 0, width = 0;
	private final int minRadius = 10;
	private final int maxRadius = 500;
	private int houghThreshold = 45;
	
	
	
	/**
	 * Constructor
	 */
	public OpCheckLandmark()
	{
		
	}
	
	
	/**
	 * starting the landmarkdetection
	 * @param src - Sourceraster of the given image
	 * @return boolean TRUE = landmark is found, FALSE = no landmark is detected
	 */
	public boolean findLm(BufferedImage src)
	{
		
		width = src.getWidth();
		height = src.getHeight();
		return findCircles(src);
	}
	
	

	
	/**
	 * Checks the amount of orange pixels in the given circle.
	 * @param rad - Radius of the circle
	 * @param mPoint - midpoint of the circle
	 * @param src - src raster
	 * @return boolean - true = if there is a specific amount of orange pixels in the area of the circle
	 */
	private boolean findColourInCircle(double rad, Point mPoint, BufferedImage src){
		
		int size = width*height;
		int[] srcPixels = src.getData().getPixels(0, 0, width, height, (int[])null);
		int[] destPixels = new int[size];
		float[] hsv = new float[3];
		double dist = 0; 
		double srad = rad*rad;//Square dist between one Point and the middle of the Circle
		
		for (int i=0; i<size; i++) {
			// Read RGB values from source raster
			int red = srcPixels[i*3];
			int green = srcPixels[i*3+1];
			int blue = srcPixels[i*3+2];
			// Transform to HSV
			Color.RGBtoHSB(red, green, blue, hsv);
			
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
		int help = 0;
		
		
		for(int c = 0; c < size; c++){
			if(destPixels[c] == 255)
				help++;
		}
		//System.out.println("Anzahl weißer Punkte: "+help);
		if(rad*rad/4<help)
			return true;
		
		return false;
	}
	
	/**
	 * Find Circles in the given colourimage, using HoughCircles from openCV.
	 * if no circle is found, houghtreshold will be decreased step by step from 45 to a minimum of 25
	 * @param src - Source Raster of the colourimage for circledetection
	 * @return
	 */
	private boolean findCircles(BufferedImage src){
		IplImage image = IplImage.createFrom(src);
		CvMat grey = CvMat.create(src.getHeight(), src.getWidth(), opencv_core.CV_8UC1);
		cvCvtColor(image, grey, opencv_core.CV_8UC1);
		GaussianBlur(grey, grey, cvSize(9, 9), 2, 2, BORDER_DEFAULT);
		
		CvMat unused = new CvMat();
		int histogramResolution = 1;
		double minDistOfCircles = 2*minRadius+2;
		double cannyThreshold = 160;
		
		//reduce the Houghtreshold if no circle is found
		for (int i = 0; i < 5; i = i++){
			System.out.println("hough: "+houghThreshold);
			System.out.println("canny: "+cannyThreshold);
			if(houghThreshold <= 33 || cannyThreshold <= 25)
				return false;
			
			CvSeq circles = cvHoughCircles(grey, unused, CV_HOUGH_GRADIENT, histogramResolution, 
				minDistOfCircles, cannyThreshold, houghThreshold, minRadius, maxRadius);
			
			if(circles.total() > 0){
				for(int j = 0; j < circles.total(); j++){
				    CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, j));
				    CvPoint center = cvPointFrom32f(new CvPoint2D32f(circle.x(), circle.y()));
				    int radius = Math.round(circle.z());      
				    
				    if(findColourInCircle(radius, new Point(center.x(), center.y()), src) == true) {
				    	return true;
				    }
				}
			}
			
			houghThreshold = houghThreshold - 5; //reduce the houghTreshold if no circle is found
			cannyThreshold = cannyThreshold - 10; //also reduce the cannyThreshold for better circle detection
		}
		
		return false;
	}
	
}

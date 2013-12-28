package de.th_wildau.quadroid.landmark;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import javax.activation.UnsupportedDataTypeException;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Class for converting BufferedImages to OpenCV Mat objects and vice versa
 * @author Ralf Vandenhouten
 */
public class OpenCvConverter {
	
	/**
	 * Converts a {@link BufferedImage} into an OpenCV {@link Mat}
	 * @param image - the {@link BufferedImage} to be converted
	 * @return the resulting {@link Mat} representing the image for use with OpenCV
	 * @throws UnsupportedDataTypeException 
	 */
	public static Mat convertToMat(BufferedImage image) 
			throws UnsupportedDataTypeException 
	{
		Mat mat = null;
		switch (image.getType()) {
		case BufferedImage.TYPE_BYTE_GRAY:
			mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);
			break;
		case BufferedImage.TYPE_3BYTE_BGR:
			mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
			break;
		default:
			throw new UnsupportedDataTypeException("Unsupported type of BufferedImage");
		}
		int width = image.getWidth(), height = image.getHeight();
		int[] pixels = image.getRaster().getPixels(0, 0, width, height, (int[])null);
		byte[] data = new byte[pixels.length];
		for (int i=0; i<data.length; i++)
			data[i] = (byte)pixels[i];
		mat.put(0, 0, data);
		return mat;
	}
	
	/**
	 * Converts a {@link Raster} into an OpenCV {@link Mat}
	 * @param raster - the {@link Raster} to be converted
	 * @return the resulting {@link Mat} representing the image for use with OpenCV
	 * @throws UnsupportedDataTypeException
	 */
	public static Mat convertToMat(Raster raster) 
			throws UnsupportedDataTypeException 
	{
		int width = raster.getWidth(), height = raster.getHeight(), size = width*height;
		int[] pixels = raster.getPixels(0, 0, width, height, (int[])null);
		byte[] data = new byte[pixels.length];
		for (int i=0; i<data.length; i++)
			data[i] = (byte)pixels[i];
		int channels = pixels.length / size;
		Mat mat = null;
		switch (channels) {
		case 1:
			mat = new Mat(height, width, CvType.CV_8UC1);
			break;
		case 3:
			mat = new Mat(height, width, CvType.CV_8UC3);
			break;
		default:
			throw new UnsupportedDataTypeException("Unsupported type of Raster");
		}
		mat.put(0, 0, data);
		return mat;
	}
	
	/**
	 * Converts an OpenCV {@link Mat} to an int array
	 * @param mat - the {@link Mat} object to be converted
	 * @return an int array containing the pixel values of the input {@link Mat}
	 */
	public static int[] convertToIntegerArray(Mat mat) 
	{
		int channels = CvType.channels(mat.type());
		int width = mat.width(), height = mat.height(), size=width*height;
		byte[] data = new byte[size*channels];
		mat.get(0, 0, data);
		int[] pixels = new int[size*channels];
		for (int i=0; i<data.length; i++)
			pixels[i] = (byte)data[i];
		return pixels;
	}
	
	/**
	 * Converts an OpenCV {@link Mat} into a {@link Raster}
	 * @param mat - the {@link Mat} to be converted
	 * @return the {@link Raster} containing the pixels of the input {@link Mat}
	 */
	public static Raster convertToRaster(Mat mat)
	{
		int channels = CvType.channels(mat.type());
		int width = mat.width(), height = mat.height();
		int[] pixels = convertToIntegerArray(mat);
		WritableRaster raster = WritableRaster.createPackedRaster(DataBuffer.TYPE_BYTE, width, height, channels, 8, null);
		raster.setPixels(0, 0, width, height, pixels);
		return raster;
	}
	
	/**
	 * Converts an OpenCV {@link Mat} into a {@link BufferedImage}
	 * @param mat - the input {@link Mat} to be converted
	 * @return the {@link BufferedImage} containing the pixels of the input {@link Mat}
	 * @throws UnsupportedDataTypeException
	 */
	public static BufferedImage convertToBufferedImage(Mat mat) 
			throws UnsupportedDataTypeException 
	{
		int channels = CvType.channels(mat.type());
		int width = mat.width(), height = mat.height();
		int type;
		switch (channels) {
		case 1:
			type = BufferedImage.TYPE_BYTE_GRAY;
			break;
		case 3:
			type = BufferedImage.TYPE_3BYTE_BGR;
			break;
		default:
			throw new UnsupportedDataTypeException("Data type not supported");
		}
		BufferedImage image = new BufferedImage(width, height, type);
		image.setData(convertToRaster(mat));
		return image;
	}
}

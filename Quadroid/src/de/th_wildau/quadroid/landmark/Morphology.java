package de.th_wildau.quadroid.landmark;

import java.util.*;

/**
 * Morphology 
 * @author rvandenh
 * Created on 07.05.2009
 */
public class Morphology {
	private int numFilterRows = 0;
	private int numFilterCols = 0;
	private int numIndices = 0;
	private boolean[] filterMask = null;
	private int[] indexedFilterMask = null;
	
	/**
	 * Morphological erosion method
	 * @param label The label of the object to be eroded
	 * @param filterMask String containing the morphological structure element
	 * @param src The source array containg the image labels
	 * @param dest The destination array for the result label image
	 * @param width The width of the image
	 * @param height The height of the image
	 * @return <code>true</code> if the image was modified by the filter, 
	 *         otherwise <code>false</code>
	 */
	public boolean erode(int label, String filterMask, int[] src, int[] dest, int width, int height)
	{
		boolean modified = false;
		parseFilterMask(filterMask);
		createIndexedFilterMask(width);
		int rowBorder = numFilterRows/2;
		int colBorder = numFilterCols/2;
		// Copy the source to the destination array, so the border can be left untouched
		System.arraycopy(src, 0, dest, 0, width*height);
		// Erosion
		for (int i=rowBorder; i<height-rowBorder; i++) {
			int offset = i*width;
			COLUMNS:
			for (int j=colBorder; j<width-colBorder; j++) {
				int index = offset+j;
				for (int k=0; k<numIndices; k++) {
					if (src[index+indexedFilterMask[k]] != label) {// Erosionsbedingung
						if (src[index] == label) {// Pixel gehört zu Objekt
							dest[index] = 255 - label; // Invertieren
							modified = true;
						}
						continue COLUMNS;
					}
				}
				// Strukturelement liegt vollständig im Objekt
				if (src[index] != label) {
					dest[index] = label;
					modified = true;
				}
			}
		}
		return modified;
	}
	/**
	 * Morphological dilation method
	 * @param label The label of the object to be dilated
	 * @param filterMask String containing the morphological structure element
	 * @param src The source array containg the image labels
	 * @param dest The destination array for the result label image
	 * @param width The width of the image
	 * @param height The height of the image
	 * @return <code>true</code> if the image was modified by the filter, 
	 *         otherwise <code>false</code>
	 */
	public boolean dilate(int label, String filterMask, int[] src, int[] dest, int width, int height)
	{
		boolean modified = false;
		parseFilterMask(filterMask);
		createIndexedFilterMask(width);
		int rowBorder = numFilterRows/2;
		int colBorder = numFilterCols/2;
		// Copy the source to the destination array, so the border can be left untouched
		System.arraycopy(src, 0, dest, 0, width*height);
		// Dilatation
		for (int i=rowBorder; i<height-rowBorder; i++) {
			int offset = i*width;
			COLUMNS:
			for (int j=colBorder; j<width-colBorder; j++) {
				int index = offset+j;
				for (int k=0; k<numIndices; k++) {
					if (src[index+indexedFilterMask[k]] == label) {// Dilatationsbedingung
						if (src[index] != label) {// Pixel gehört zu Objekt
							dest[index] = label;
							modified = true;
						}
						continue COLUMNS;
					}
				}
				// Strukturelement hat keine Schnittmenge mit Objekt
				if (src[index] == label) {
					dest[index] = 255 - label; // Invertieren
					modified = true;
				}
			}
		}
		return modified;
	}
	/**
	 * Morphological thinning method using Zhang-Suen algorithm
	 * @param label The label of the object to be dilated
	 * @param src The source array containg the image labels
	 * @param dest The destination array for the result label image
	 * @param width The width of the image
	 * @param height The height of the image
	 * @return <code>true</code> if the image was modified by the filter, 
	 *         otherwise <code>false</code>
	 */
	public boolean thin(int label, int[] src, int[] dest, int width, int height)
	{
		boolean modified = false;
		int rowBorder = 1;
		int colBorder = 1;
		numIndices = 8;
		int[] indexedFilterMask = {-width, -width+1, 1, width+1, width, width-1, -1, -width-1};
		boolean[] neighbors = new boolean[numIndices];
		// Copy the source to the destination array, so the border can be left untouched
		System.arraycopy(src, 0, dest, 0, width*height);
		// Thinning
		// First pass
		for (int i=rowBorder; i<height-rowBorder; i++) {
			int offset = i*width;
			for (int j=colBorder; j<width-colBorder; j++) {
				int index = offset+j;
				// Nur bearbeiten, falls Pixel zum Objekt gehört 
				if (src[index] == label) { 
					int numNeighbors = 0;
					// Nachbarmaske generieren
					for (int k=0; k<numIndices; k++) {
						if (src[index+indexedFilterMask[k]] == label) {
							neighbors[k] = true;
							numNeighbors++;
						} else {
							neighbors[k] = false;
						}
					}
					if (numNeighbors<2 || numNeighbors>6)
						continue;
					// Transitionen zählen
					int numTransitions = 0;
					for (int k=0; k<numIndices; k++) {
						if ((!neighbors[k]) && neighbors[(k+1)%numIndices])
							numTransitions++;
					}			
					if (numTransitions != 1)
						continue;
					if (neighbors[0] && neighbors[2] && neighbors[4])
						continue;
					if (neighbors[2] && neighbors[4] && neighbors[6])
						continue;
					dest[index] = 255 - label;
					modified = true;
				}
			}
		}
		System.arraycopy(dest, 0, src, 0, width*height);
		// Second pass
		for (int i=rowBorder; i<height-rowBorder; i++) {
			int offset = i*width;
			for (int j=colBorder; j<width-colBorder; j++) {
				int index = offset+j;
				// Nur bearbeiten, falls Pixel zum Objekt gehört 
				if (src[index] == label) { 
					int numNeighbors = 0;
					// Nachbarmaske generieren
					for (int k=0; k<numIndices; k++) {
						if (src[index+indexedFilterMask[k]] == label) {
							neighbors[k] = true;
							numNeighbors++;
						} else {
							neighbors[k] = false;
						}
					}
					if (numNeighbors<2 || numNeighbors>6)
						continue;
					// Transitionen zählen
					int numTransitions = 0;
					for (int k=0; k<numIndices; k++) {
						if ((!neighbors[k]) && neighbors[(k+1)%numIndices])
							numTransitions++;
					}			
					if (numTransitions != 1)
						continue;
					if (neighbors[0] && neighbors[2] && neighbors[6])
						continue;
					if (neighbors[0] && neighbors[4] && neighbors[6])
						continue;
					dest[index] = 255 - label;
					modified = true;
				}
			}
		}
		return modified;
	}
	/**
	 * Parsing the String that represents the structure element of
	 * the morphologic filter and converting it into a 2D boolean array
	 * @param filterMaskAsString The String to parse
	 */
	private void parseFilterMask(String filterMaskAsString)
	throws IllegalArgumentException
	{
		// Filterkoeffizienten parsen
		StringTokenizer st = new StringTokenizer(filterMaskAsString, "|");
		numFilterRows = st.countTokens();
		int offset = 0;
		// Filtermaske als Array generieren
		while (st.hasMoreElements()) {
			String rowAsString = st.nextToken();
			StringTokenizer rowTok = new StringTokenizer(rowAsString, ", ");
			if (numFilterCols == 0)
				numFilterCols = rowTok.countTokens();
			if (numFilterCols != rowTok.countTokens() || numFilterCols == 0)
				throw new IllegalArgumentException("Filter mask must have rectangular format");
			if (filterMask == null)
				filterMask = new boolean[numFilterRows*numFilterCols];
			int col = 0;
			while (rowTok.hasMoreElements()) {
				String elementAsString = rowTok.nextToken();
				try {
					int val = Integer.parseInt(elementAsString);
					if (val!=0 && val!=1)
						throw new IllegalArgumentException("Structure element must only have coefficients 0 or 1");
					filterMask[offset + col++] = val==0 ? false : true;
				}
				catch (NumberFormatException e) {
					throw new IllegalArgumentException("Filter mask not well defined");
				}
			}
			offset += numFilterCols;
		}
		
	}
	/**
	 * Convert the 2D boolean array containing the structure element
	 * into an array of relative indices pointing to neighboring pixels
	 * where the structure element is 1
	 * @param imageWidth Width of the image
	 */
	private void createIndexedFilterMask(int imageWidth)
	{
		indexedFilterMask = new int[numFilterRows*numFilterCols];
		numIndices = 0;
		for (int i=0; i<numFilterRows; i++)
			for (int j=0; j<numFilterCols; j++)
				if (filterMask[i*numFilterCols+j]) {
					indexedFilterMask[numIndices] = (i-numFilterRows/2)*imageWidth+j-numFilterCols/2;
					numIndices++;
				}	
	}
}

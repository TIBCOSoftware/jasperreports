package net.sf.jasperreports.engine.util;

import java.io.FileInputStream;

import net.sf.jasperreports.engine.JRImage;


public class JRTypeSniffer
{
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isGIF(byte[] data) {
		// chech if the image data length is less than 3 bytes
		if(data.length < 3) {
			return false;
		}
		
		// get the first three characters
		byte[] first = new byte[3];
		System.arraycopy(data, 0, first, 0, 3);
		if((new String(first)).equalsIgnoreCase("GIF")){
			return true;
		}
	
		return false;
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isJPEG(byte[] data) {
		// check if the image data length is less than 2 bytes
		if(data.length < 2) {
			return false;
		}
		
		//0xff is -1 and 0xd8 is -40
		if(data[0] == -1 && data[1] == -40) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isPNG(byte[] data) {
		if(data.length < 8) {
			return false;
		}
		
		if(data[0] == -119 &&
			data[1] == 80 &&
			data[2] == 78 &&
			data[3] == 71 &&
			data[4] == 13 && 
			data[5] == 10 &&
			data[6] == 26 &&
			data[7] == 10) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isTIFF(byte[] data) {
		if(data.length < 2) {
			return false;
		}
		
		if((data[0] == 73 && data[1] == 73) || 
		   (data[0] == 77 && data[1] == 77)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static byte getImageType(byte[] data) {
		if (isGIF(data)) {
			return JRImage.TYPE_GIF;
		}
		else if (isJPEG(data)) {
			return JRImage.TYPE_JPEG;
		}
		else if (isPNG(data)) {
			return JRImage.TYPE_PNG;
		}
		else if (isTIFF(data)) {
			return JRImage.TYPE_TIFF;
		}
		
		return JRImage.TYPE_UNKNOWN;
	}
}

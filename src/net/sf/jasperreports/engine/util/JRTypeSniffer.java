/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.JRImage;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRTypeSniffer
{
	/**
	 * 
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

/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRenderable;

import com.keypoint.PngEncoderB;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDefaultImageEncoder extends JRAbstractImageEncoder
{

	
	/**
	 *
	 */
	public byte[] encode(BufferedImage bi, byte imageType) throws JRException
	{
		byte[] bytes = null;
		
		switch (imageType)
		{
			case JRRenderable.IMAGE_TYPE_PNG :
			{
				bytes = new PngEncoderB(bi).pngEncode();
				break;
			}
			case JRRenderable.IMAGE_TYPE_JPEG :
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				try
				{
					JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);
					JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
					param.setQuality(1f, true);//1f = JPG_QUALITY
					encoder.encode(bi, param);
				}
				catch (IOException e)
				{
					throw new JRException(e);
				}
				
				bytes = baos.toByteArray();
				break;
			}
			case JRRenderable.IMAGE_TYPE_GIF :
			{
				throw new JRException("Image type GIF not supported by this image encoder.");
			}
			case JRRenderable.IMAGE_TYPE_TIFF :
			{
				throw new JRException("Image type TIFF not supported by this image encoder.");
			}
			case JRRenderable.IMAGE_TYPE_UNKNOWN :
			default:
			{
				throw new JRException("Image type UNKNOWN not supported by this image encoder.");
			}
		}
		
		return bytes;
	}


}

/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRenderable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRImageLoader.java 1229 2006-04-19 13:27:35 +0300 (Wed, 19 Apr 2006) teodord $
 */
public class JRJdk14ImageEncoder extends JRAbstractImageEncoder
{

	
	/**
	 *
	 */
	public byte[] encode(BufferedImage bi, byte imageType) throws JRException
	{
		String formatName = null;

		switch (imageType)
		{
			case JRRenderable.IMAGE_TYPE_GIF :
			{
				formatName = "gif";
				break;
			}
			case JRRenderable.IMAGE_TYPE_PNG :
			{
				formatName = "png";
				break;
			}
			case JRRenderable.IMAGE_TYPE_TIFF :
			{
				formatName = "tiff";
				break;
			}
			case JRRenderable.IMAGE_TYPE_JPEG :
			case JRRenderable.IMAGE_TYPE_UNKNOWN :
			default:
			{
				formatName = "jpeg";
				break;
			}
		}
		
		boolean success = false;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try
		{
			success = ImageIO.write(bi, formatName, baos);
		}
		catch (IOException e)
		{
			throw new JRException(e);
		}
		
		if (!success)
		{
			throw new JRException("No appropriate image writer found for the \"" + formatName + "\" format.");
		}
		
		return baos.toByteArray();
	}


}

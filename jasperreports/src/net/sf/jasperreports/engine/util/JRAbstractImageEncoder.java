/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
 * (at your option) any later version.
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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRenderable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractImageEncoder implements JRImageEncoder
{

	
	/**
	 *
	 */
	public byte[] encode(Image image, byte imageType) throws JRException
	{
		BufferedImage bi = null;
		
		if (image instanceof BufferedImage)
		{
			bi = (BufferedImage)image;
		}
		else
		{
			bi =
				new BufferedImage(
					image.getWidth(null),
					image.getHeight(null),
					// avoid creating JPEG images with transparency that would result 
					// in invalid image files for some viewers (browsers)
					(imageType == JRRenderable.IMAGE_TYPE_GIF || imageType == JRRenderable.IMAGE_TYPE_PNG)  
						? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB 
					);

			Graphics g = bi.createGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
		}

		return encode(bi, imageType);
	}


	/**
	 *
	 */
	public abstract byte[] encode(BufferedImage bi, byte imageType) throws JRException;


}

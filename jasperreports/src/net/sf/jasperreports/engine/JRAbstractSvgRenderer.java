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
package net.sf.jasperreports.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRProperties;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractSvgRenderer extends JRAbstractRenderer
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	
	/**
	 *
	 */
	public byte getType()
	{
		return TYPE_SVG;
	}


	/**
	 *
	 */
	public byte getImageType()
	{
		return IMAGE_TYPE_PNG;
	}


	/**
	 *
	 */
	public Dimension2D getDimension()
	{
		return null;
	}


	/**
	 *
	 */
	public Color getBackcolor()
	{
		return null;
	}


	/**
	 *
	 */
	public byte[] getImageData() throws JRException
	{
		int dpi = JRProperties.getIntegerProperty(PROPERTY_IMAGE_DPI, 72);
		double scale = dpi/72d;
		
		Dimension2D dimension = getDimension();
		if (dimension != null)
		{
			byte imageType = getImageType();
			BufferedImage bi =
				new BufferedImage(
					(int) (scale * dimension.getWidth()),
					(int) (scale * dimension.getHeight()),
					// avoid creating JPEG images with transparency that would result 
					// in invalid image files for some viewers (browsers)
					(imageType == JRRenderable.IMAGE_TYPE_GIF || imageType == JRRenderable.IMAGE_TYPE_PNG)  
						? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB 
					);

			Graphics2D g = createGraphics(bi);
			g.scale(scale, scale);
			Color backcolor = getBackcolor();
			if (backcolor != null)
			{
				g.setColor(backcolor);
				g.fillRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
			}
			render(g, new Rectangle((int)dimension.getWidth(), (int)dimension.getHeight()));
			g.dispose();
			
			return JRImageLoader.loadImageDataFromAWTImage(bi, getImageType());
		}
		return null;
	}


	protected Graphics2D createGraphics(BufferedImage bi)
	{
		return bi.createGraphics();
	}


}

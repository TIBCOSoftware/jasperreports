/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.util.JRImageLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class JRAbstractSvgRenderer extends JRAbstractRenderer
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	
	/**
	 * @deprecated Replaced by {@link #getTypeValue()}.
	 */
	public byte getType()
	{
		return getTypeValue().getValue();
	}


	/**
	 * @deprecated Replaced by {@link #getImageTypeValue()}.
	 */
	public byte getImageType()
	{
		return getImageTypeValue().getValue();
	}


	/**
	 *
	 */
	public RenderableTypeEnum getTypeValue()
	{
		return RenderableTypeEnum.SVG;
	}


	/**
	 *
	 */
	public ImageTypeEnum getImageTypeValue()
	{
		return ImageTypeEnum.PNG;
	}


	/**
	 *
	 */
	public Dimension2D getDimension(JasperReportsContext jasperReportsContext)
	{
		return null;
	}


	/**
	 * @deprecated Replaced by {@link #getDimension(JasperReportsContext)}.
	 */
	public Dimension2D getDimension()
	{
		return getDimension(DefaultJasperReportsContext.getInstance());
	}


	/**
	 *
	 */
	public Color getBackcolor()
	{
		return null;
	}


	/**
	 * @deprecated Replaced by {@link #getImageData(JasperReportsContext)}.
	 */
	public byte[] getImageData() throws JRException
	{
		return getImageData(DefaultJasperReportsContext.getInstance());
	}


	/**
	 *
	 */
	public byte[] getImageData(JasperReportsContext jasperReportsContext) throws JRException
	{
		int dpi = getImageDataDPI(jasperReportsContext);
		double scale = dpi/72d;
		
		Dimension2D dimension = getDimension(jasperReportsContext);
		if (dimension != null)
		{
			ImageTypeEnum imageType = getImageTypeValue();
			BufferedImage bi =
				new BufferedImage(
					(int) (scale * dimension.getWidth()),
					(int) (scale * dimension.getHeight()),
					// avoid creating JPEG images with transparency that would result 
					// in invalid image files for some viewers (browsers)
					(imageType == ImageTypeEnum.GIF || imageType == ImageTypeEnum.PNG)  
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
			render(jasperReportsContext, g, new Rectangle((int)dimension.getWidth(), (int)dimension.getHeight()));
			g.dispose();
			
			return JRImageLoader.getInstance(jasperReportsContext).loadBytesFromAwtImage(bi, getImageTypeValue());
		}
		return null;
	}


	protected int getImageDataDPI(JasperReportsContext jasperReportsContext)
	{
		return JRPropertiesUtil.getInstance(jasperReportsContext).getIntegerProperty(PROPERTY_IMAGE_DPI, 72);
	}


	protected Graphics2D createGraphics(BufferedImage bi)
	{
		return bi.createGraphics();
	}


}

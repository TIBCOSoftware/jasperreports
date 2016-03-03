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
package net.sf.jasperreports.renderers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

import net.sf.jasperreports.engine.DimensionRenderable;
import net.sf.jasperreports.engine.Graphics2DRenderable;
import net.sf.jasperreports.engine.ImageRenderable;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.SvgRenderable;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.util.JRImageLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractRenderToImageRenderer extends AbstractRenderToImageAwareRenderer implements ImageRenderable, DimensionRenderable
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String EXCEPTION_MESSAGE_KEY_DIMENSION_MUST_OVERRIDE = "engine.renderable.svg.dimension.must.override";
	public static final String EXCEPTION_MESSAGE_KEY_DIMENSION_NULL_NOT_ALLOWED = "engine.renderable.svg.dimension.null.not.allowed";

	
	/**
	 * @deprecated Replaced by {@link ImageRenderable}, {@link SvgRenderable} and {@link Graphics2DRenderable}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.RenderableTypeEnum getTypeValue()
	{
		return net.sf.jasperreports.engine.type.RenderableTypeEnum.SVG;
	}


	/**
	 * @deprecated Replaced by {@link #getImageType()}.
	 */
	@Override
	public ImageTypeEnum getImageTypeValue()
	{
		return getImageType();
	}


	@Override
	public ImageTypeEnum getImageType()
	{
		return ImageTypeEnum.PNG;
	}


	@Override
	@SuppressWarnings("deprecation")
	public Dimension2D getDimension(JasperReportsContext jasperReportsContext) throws JRException
	{
		throw 
			new JRException(
				EXCEPTION_MESSAGE_KEY_DIMENSION_MUST_OVERRIDE, 
				new Object[]{this.getClass().getName()}
				);
	}


	@Override
	@SuppressWarnings("deprecation")
	public byte[] getImageData(JasperReportsContext jasperReportsContext) throws JRException
	{
		Dimension2D dimension = getDimension(jasperReportsContext);
		
		if (dimension == null)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_DIMENSION_NULL_NOT_ALLOWED, 
					(Object[])null
					);
		}

		int dpi = getImageDataDPI(jasperReportsContext);
		double scale = dpi/72d;
		
		ImageTypeEnum imageType = getImageType();
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
		try
		{
			g.scale(scale, scale);
			Color backcolor = getBackcolor();
			if (backcolor != null)
			{
				g.setColor(backcolor);
				g.fillRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
			}
			render(jasperReportsContext, g, new Rectangle((int)dimension.getWidth(), (int)dimension.getHeight()));
		}
		finally
		{
			g.dispose();
		}
		
		return JRImageLoader.getInstance(jasperReportsContext).loadBytesFromAwtImage(bi, getImageType());
	}


	protected Color getBackcolor()
	{
		return null;
	}
}

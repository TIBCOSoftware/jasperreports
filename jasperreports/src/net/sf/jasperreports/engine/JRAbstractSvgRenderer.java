/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.renderers.AbstractRenderToImageDataRenderer;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated Replaced by {@link AbstractRenderToImageDataRenderer}.
 */
public abstract class JRAbstractSvgRenderer extends JRAbstractRenderer
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String EXCEPTION_MESSAGE_KEY_DIMENSION_MUST_OVERRIDE = "engine.renderable.svg.dimension.must.override";
	public static final String EXCEPTION_MESSAGE_KEY_DIMENSION_NULL_NOT_ALLOWED = "engine.renderable.svg.dimension.null.not.allowed";

	
	/**
	 * @deprecated Replaced by {@link #getTypeValue()}.
	 */
	@Override
	public byte getType()
	{
		return getTypeValue().getValue();
	}


	/**
	 * @deprecated Replaced by {@link #getImageTypeValue()}.
	 */
	@Override
	public byte getImageType()
	{
		return getImageTypeValue().getValue();
	}


	@Override
	public net.sf.jasperreports.engine.type.RenderableTypeEnum getTypeValue()
	{
		return net.sf.jasperreports.engine.type.RenderableTypeEnum.SVG;
	}


	@Override
	public ImageTypeEnum getImageTypeValue()
	{
		return ImageTypeEnum.PNG;
	}


	@Override
	public Dimension2D getDimension(JasperReportsContext jasperReportsContext) throws JRException
	{
		throw 
			new JRException(
				EXCEPTION_MESSAGE_KEY_DIMENSION_MUST_OVERRIDE, 
				new Object[]{this.getClass().getName()}
				);
	}


	/**
	 * @deprecated Replaced by {@link #getDimension(JasperReportsContext)}.
	 */
	@Override
	public Dimension2D getDimension() throws JRException
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
	@Override
	public byte[] getImageData() throws JRException
	{
		return getImageData(DefaultJasperReportsContext.getInstance());
	}


	/**
	 * @deprecated Replaced by {@link #render(JasperReportsContext, Graphics2D, Rectangle2D)}.
	 */
	@Override
	public void render(Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		render(DefaultJasperReportsContext.getInstance(), grx, rectangle);
	}


	@Override
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
		
		return JRImageLoader.getInstance(jasperReportsContext).loadBytesFromAwtImage(bi, getImageTypeValue());
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

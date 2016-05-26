/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.util.JRTypeSniffer;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated To be removed.
 */
public class WrappingDeprecatedRenderable implements net.sf.jasperreports.engine.Renderable
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private final String id;
	private final Graphics2DRenderable grxRenderable;
	private final DataRenderable dataRenderable;
	private final DimensionRenderable dimensionRenderable;
	private ImageTypeEnum imageType = null;
	
	
	/**
	 *
	 */
	public WrappingDeprecatedRenderable(
		String id,
		Graphics2DRenderable grxRenderable,
		DataRenderable dataRenderable,
		DimensionRenderable dimensionRenderable
		)
	{
		this.id = id;
		this.grxRenderable = grxRenderable;
		this.dataRenderable = dataRenderable;
		this.dimensionRenderable = dimensionRenderable;
	}
	
	
	@Override
	public String getId() 
	{
		return id;
	}


	@Override
	public byte getType() 
	{
		return getTypeValue().getValue();
	}


	@Override
	public byte getImageType() 
	{
		return getImageTypeValue().getValue();
	}


	@Override
	public Dimension2D getDimension() throws JRException 
	{
		return getDimension(DefaultJasperReportsContext.getInstance());
	}


	@Override
	public byte[] getImageData() throws JRException 
	{
		return getImageData(DefaultJasperReportsContext.getInstance());
	}


	@Override
	public void render(
		Graphics2D grx, 
		Rectangle2D rectangle
		) throws JRException 
	{
		render(DefaultJasperReportsContext.getInstance(), grx, rectangle);
	}


	@Override
	public RenderableTypeEnum getTypeValue() 
	{
		if (grxRenderable instanceof WrappingDataToGraphics2DRenderer)
		{
			return ((WrappingDataToGraphics2DRenderer)grxRenderable).getRenderableType();
		}
		return RenderableTypeEnum.SVG;
	}


	@Override
	public ImageTypeEnum getImageTypeValue() 
	{
		if (imageType == null)
		{
			try
			{
				return JRTypeSniffer.getImageTypeValue(getImageData());
			}
			catch (JRException e)
			{
				return ImageTypeEnum.UNKNOWN;
			}
		}
		
		return imageType;
	}


	@Override
	public Dimension2D getDimension(JasperReportsContext jasperReportsContext) throws JRException 
	{
		return dimensionRenderable == null ? null : dimensionRenderable.getDimension(jasperReportsContext);
	}


	@Override
	public byte[] getImageData(JasperReportsContext jasperReportsContext) throws JRException 
	{
		byte[] imageData = dataRenderable.getData(jasperReportsContext);
		imageType = JRTypeSniffer.getImageTypeValue(imageData);
		return imageData;
	}


	@Override
	public void render(
		JasperReportsContext jasperReportsContext,
		Graphics2D grx, 
		Rectangle2D rectangle
		) throws JRException 
	{
		grxRenderable.render(jasperReportsContext, grx, rectangle);
	}

}

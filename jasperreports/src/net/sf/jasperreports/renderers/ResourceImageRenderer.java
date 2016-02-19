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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.SoftReference;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractRenderer;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRTypeSniffer;
import net.sf.jasperreports.repo.RepositoryUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ResourceImageRenderer extends JRAbstractRenderer
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	/**
	 *
	 */
	private String imageLocation;

	private transient byte[] imageData;
	private transient ImageTypeEnum imageTypeValue = ImageTypeEnum.UNKNOWN;

	/**
	 *
	 */
	private transient SoftReference<Image> awtImageRef;


	/**
	 *
	 */
	protected ResourceImageRenderer(String imageLocation)
	{
		this.imageLocation = imageLocation;
	}


	/**
	 *
	 */
	public static ResourceImageRenderer getInstance(String imageLocation)
	{
		if (imageLocation == null)
		{
			return null;
		}
		
		return new ResourceImageRenderer(imageLocation);
	}


	/**
	 *
	 */
	protected Image getImage(JasperReportsContext jasperReportsContext) throws JRException
	{
		if (awtImageRef == null || awtImageRef.get() == null)
		{
			Image awtImage = JRImageLoader.getInstance(jasperReportsContext).loadAwtImageFromBytes(getImageData(jasperReportsContext));
			awtImageRef = new SoftReference<Image>(awtImage);
		}
		return awtImageRef.get();
	}


	/**
	 *
	 */
	public String getImageLocation()
	{
		return imageLocation;
	}


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
	public RenderableTypeEnum getTypeValue()
	{
		return RenderableTypeEnum.IMAGE;
	}
	
	
	@Override
	public ImageTypeEnum getImageTypeValue()
	{
		return imageTypeValue;
	}
	

	/**
	 * @deprecated Replaced by {@link #getDimension(JasperReportsContext)}.
	 */
	@Override
	public Dimension2D getDimension() throws JRException
	{
		return getDimension(DefaultJasperReportsContext.getInstance());
	}


	@Override
	public Dimension2D getDimension(JasperReportsContext jasperReportsContext) throws JRException
	{
		Image img = getImage(jasperReportsContext);
		return new Dimension(img.getWidth(null), img.getHeight(null));
	}


	@Override
	public byte[] getImageData(JasperReportsContext jasperReportsContext) throws JRException
	{
		if (imageData == null)
		{
			imageData = RepositoryUtil.getInstance(jasperReportsContext).getBytesFromLocation(imageLocation);
			
			if (imageData != null) 
			{
				imageTypeValue = JRTypeSniffer.getImageTypeValue(imageData);
			}
		}

		return imageData;
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


	/**
	 *
	 */
	@Override
	public void render(JasperReportsContext jasperReportsContext, Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		Image img = getImage(jasperReportsContext);

		grx.drawImage(
			img, 
			(int)rectangle.getX(), 
			(int)rectangle.getY(), 
			(int)rectangle.getWidth(), 
			(int)rectangle.getHeight(), 
			null
			);
	}
}

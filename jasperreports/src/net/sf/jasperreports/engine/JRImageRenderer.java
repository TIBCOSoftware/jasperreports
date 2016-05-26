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
package net.sf.jasperreports.engine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.SoftReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRTypeSniffer;
import net.sf.jasperreports.renderers.DataRenderable;
import net.sf.jasperreports.renderers.ResourceRenderer;
import net.sf.jasperreports.repo.RepositoryUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated Replaced by {@link ResourceRenderer} and {@link DataRenderable}.
 */
public class JRImageRenderer extends JRAbstractRenderer implements DataRenderable
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private static final Log log = LogFactory.getLog(JRImageRenderer.class);
	
	/**
	 *
	 */
	private byte[] imageData;
	private String imageLocation;
	private ImageTypeEnum imageTypeValue = ImageTypeEnum.UNKNOWN;

	/**
	 *
	 */
	private transient SoftReference<Image> awtImageRef;


	/**
	 *
	 */
	protected JRImageRenderer(byte[] imageData)
	{
		this.imageData = imageData;
		
		if(imageData != null) 
		{
			imageTypeValue = JRTypeSniffer.getImageTypeValue(imageData);
		}
			
	}


	/**
	 * @deprecated
	 */
	protected JRImageRenderer(String imageLocation)
	{
		this.imageLocation = imageLocation;
	}


	/**
	 *
	 */
	public static JRImageRenderer getInstance(byte[] imageData)
	{
		return new JRImageRenderer(imageData);
	}


	/**
	 * @deprecated To be removed.
	 */
	public static JRImageRenderer getOnErrorRendererForImage(JasperReportsContext jasperReportsContext, JRImageRenderer renderer, OnErrorTypeEnum onErrorType) throws JRException
	{
		JRImageRenderer result;
		try
		{
			renderer.getImage(jasperReportsContext);
			result = renderer;
		}
		catch (Exception e)
		{
			result = (JRImageRenderer)RenderableUtil.getInstance(jasperReportsContext).handleImageError(e, onErrorType);
			
			if (log.isDebugEnabled())
			{
				log.debug("handled image error with type " + onErrorType, e);
			}
		}
		return result;
	}


	/**
	 * @deprecated Replaced by {@link RenderableUtil#getOnErrorRenderer(OnErrorTypeEnum, JRException)}.
	 */
	public static JRImageRenderer getOnErrorRenderer(OnErrorTypeEnum onErrorType, JRException e) throws JRException
	{
		return (JRImageRenderer)RenderableUtil.getInstance(DefaultJasperReportsContext.getInstance()).getOnErrorRenderer(onErrorType, e);
	}

	/**
	 * @deprecated Replaced by {@link RenderableUtil#getOnErrorRenderer(OnErrorTypeEnum, JRRuntimeException)}.
	 */
	public static JRImageRenderer getOnErrorRenderer(OnErrorTypeEnum onErrorType, JRRuntimeException e) throws JRRuntimeException
	{
		return (JRImageRenderer)RenderableUtil.getInstance(DefaultJasperReportsContext.getInstance()).getOnErrorRenderer(onErrorType, e);
	}


	/**
	 *
	 */
	public Image getImage(JasperReportsContext jasperReportsContext) throws JRException
	{
		if (awtImageRef == null || awtImageRef.get() == null)
		{
			Image awtImage = JRImageLoader.getInstance(jasperReportsContext).loadAwtImageFromBytes(getImageData(jasperReportsContext));
			awtImageRef = new SoftReference<Image>(awtImage);
		}
		return awtImageRef.get();
	}


	/**
	 * @deprecated Replaced by {@link #getImage(JasperReportsContext)}.
	 */
	public Image getImage() throws JRException
	{
		return getImage(DefaultJasperReportsContext.getInstance());
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
	public byte[] getImageData(JasperReportsContext jasperReportsContext)
			throws JRException
	{
		if (imageData == null)
		{
			imageData = RepositoryUtil.getInstance(jasperReportsContext).getBytesFromLocation(imageLocation);
			
			if(imageData != null) 
			{
				imageTypeValue = JRTypeSniffer.getImageTypeValue(imageData);
			}
		}

		return imageData;
	}


	@Override
	public byte[] getData(JasperReportsContext jasperReportsContext)
			throws JRException
	{
		return getImageData(jasperReportsContext);
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

	
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private byte imageType;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_4_6_0)
		{
			imageTypeValue = ImageTypeEnum.getByValue(imageType);
		}
	}

}

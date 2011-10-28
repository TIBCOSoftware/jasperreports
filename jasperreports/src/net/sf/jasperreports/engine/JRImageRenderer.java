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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLStreamHandlerFactory;

import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRTypeSniffer;
import net.sf.jasperreports.repo.RepositoryUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRImageRenderer extends JRAbstractRenderer
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private byte[] imageData;
	private String imageLocation;
	private byte imageType = IMAGE_TYPE_UNKNOWN;

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
			imageType = JRTypeSniffer.getImageType(imageData);
		}
			
	}


	/**
	 *
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
	 *
	 */
	public static JRRenderable getInstance(String imageLocation) throws JRException
	{
		return getInstance(imageLocation, OnErrorTypeEnum.ERROR, true);
	}


	/**
	 * 
	 */
	public static JRRenderable getInstance(String imageLocation, OnErrorTypeEnum onErrorType) throws JRException
	{
		return getInstance(imageLocation, onErrorType, true);
	}


	/**
	 * 
	 */
	public static JRRenderable getInstance(String imageLocation, OnErrorTypeEnum onErrorType, boolean isLazy) throws JRException
	{
		if (imageLocation == null)
		{
			return null;
		}

		if (isLazy)
		{
			return new JRImageRenderer(imageLocation);
		}

		try
		{
			byte[] data = RepositoryUtil.getBytes(imageLocation);
			return new JRImageRenderer(data);
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e);
		}
	}

	
	/**
	 * @deprecated Replaced by {@link #getInstance(String, OnErrorTypeEnum, boolean)}.
	 */
	public static JRRenderable getInstance(
		String imageLocation, 
		OnErrorTypeEnum onErrorType, 
		boolean isLazy, 
		ClassLoader classLoader,
		URLStreamHandlerFactory urlHandlerFactory,
		FileResolver fileResolver
		) throws JRException
	{
		if (imageLocation == null)
		{
			return null;
		}

		if (isLazy)
		{
			return new JRImageRenderer(imageLocation);
		}

		try
		{
			byte[] data = JRLoader.loadBytesFromLocation(imageLocation, classLoader, urlHandlerFactory, fileResolver);
			return new JRImageRenderer(data);
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e);
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(Image img, OnErrorTypeEnum onErrorType) throws JRException
	{
		byte type = JRRenderable.IMAGE_TYPE_JPEG;
		if (img instanceof RenderedImage)
		{
			ColorModel colorModel = ((RenderedImage) img).getColorModel();
			//if the image has transparency, encode as PNG
			if (colorModel.hasAlpha() 
					&& colorModel.getTransparency() != Transparency.OPAQUE)
			{
				type = JRRenderable.IMAGE_TYPE_PNG;
			}
		}
		
		return getInstance(img, type, onErrorType);
	}


	/**
	 * Creates and returns an instance of the JRImageRenderer class after encoding the image object using an image
	 * encoder that supports the supplied image type.
	 * 
	 * @param image the java.awt.Image object to wrap into a JRImageRenderer instance
	 * @param imageType the type of the image as specified by one of the constants defined in the JRRenderable interface
	 * @param onErrorType one of the error type constants defined in the {@link OnErrorTypeEnum}.
	 * @return the image renderer instance
	 */
	public static JRRenderable getInstance(Image image, byte imageType, OnErrorTypeEnum onErrorType) throws JRException
	{
		try
		{
			return new JRImageRenderer(JRImageLoader.loadImageDataFromAWTImage(image, imageType));
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e); 
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(InputStream is, OnErrorTypeEnum onErrorType) throws JRException
	{
		try
		{
			return new JRImageRenderer(JRLoader.loadBytes(is));
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e); 
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(URL url, OnErrorTypeEnum onErrorType) throws JRException
	{
		try
		{
			return new JRImageRenderer(JRLoader.loadBytes(url));
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e); 
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(File file, OnErrorTypeEnum onErrorType) throws JRException
	{
		try
		{
			return new JRImageRenderer(JRLoader.loadBytes(file));
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e); 
		}
	}


	/**
	 *
	 */
	public static JRRenderable getOnErrorRendererForDimension(JRRenderable renderer, OnErrorTypeEnum onErrorType) throws JRException
	{
		try
		{
			renderer.getDimension();
			return renderer;
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e); 
		}
	}


	/**
	 *
	 */
	public static JRRenderable getOnErrorRendererForImageData(JRRenderable renderer, OnErrorTypeEnum onErrorType) throws JRException
	{
		try
		{
			renderer.getImageData();
			return renderer;
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e); 
		}
	}


	/**
	 *
	 */
	public static JRImageRenderer getOnErrorRendererForImage(JRImageRenderer renderer, OnErrorTypeEnum onErrorType) throws JRException
	{
		try
		{
			renderer.getImage();
			return renderer;
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e); 
		}
	}


	/**
	 * 
	 */
	public static JRImageRenderer getOnErrorRenderer(OnErrorTypeEnum onErrorType, JRException e) throws JRException
	{
		JRImageRenderer renderer = null;
		
		switch (onErrorType)
		{
			case ICON :
			{
				renderer = new JRImageRenderer(JRImageLoader.NO_IMAGE_RESOURCE);
				//FIXME cache these renderers
				break;
			}
			case BLANK :
			{
				break;
			}
			case ERROR :
			default :
			{
				throw e;
			}
		}

		return renderer;
	}


	/**
	 *
	 */
	public Image getImage() throws JRException
	{
		if (awtImageRef == null || awtImageRef.get() == null)
		{
			Image awtImage = JRImageLoader.loadImage(getImageData());
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
	 *
	 */
	public byte getType()
	{
		return TYPE_IMAGE;
	}
	
	
	public byte getImageType() {
		return imageType;
	}
	
	

	/**
	 *
	 */
	public Dimension2D getDimension() throws JRException
	{
		Image img = getImage();
		return new Dimension(img.getWidth(null), img.getHeight(null));
	}


	/**
	 *
	 */
	public byte[] getImageData() throws JRException
	{
		if (imageData == null)
		{
			imageData = RepositoryUtil.getBytes(imageLocation);
			
			if(imageData != null) 
			{
				imageType = JRTypeSniffer.getImageType(imageData);
			}
		}

		return imageData;
	}


	/**
	 *
	 */
	public void render(Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		Image img = getImage();

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

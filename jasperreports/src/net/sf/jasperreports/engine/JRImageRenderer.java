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
package net.sf.jasperreports.engine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLStreamHandlerFactory;

import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRResourcesUtil;
import net.sf.jasperreports.engine.util.JRTypeSniffer;


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
	private byte[] imageData = null;
	private String imageLocation = null;
	private byte onErrorType = JRImage.ON_ERROR_TYPE_ERROR;
	private byte imageType = IMAGE_TYPE_UNKNOWN;

	/**
	 *
	 */
	private transient SoftReference awtImageRef = null;


	/**
	 *
	 */
	private JRImageRenderer(byte[] imageData, byte onErrorType)
	{
		this.imageData = imageData;
		this.onErrorType = onErrorType;
		
		if(imageData != null) 
		{
			imageType = JRTypeSniffer.getImageType(imageData);
		}
			
	}


	/**
	 *
	 */
	private JRImageRenderer(String imageLocation, byte onErrorType)
	{
		this.imageLocation = imageLocation;
		this.onErrorType = onErrorType;
	}


	/**
	 * @deprecated replaced by
	 * {@link JRResourcesUtil#getThreadClassLoader() JRResourcesUtil.getThreadClassLoader()}
	 */
	public static ClassLoader getClassLoader()
	{
		return JRResourcesUtil.getThreadClassLoader();
	}


	/**
	 * @deprecated replace by
	 * {@link JRResourcesUtil#setThreadClassLoader(ClassLoader) JRResourcesUtil.setThreadClassLoader(ClassLoader)}
	 */
	public static void setClassLoader(ClassLoader classLoader)
	{
		JRResourcesUtil.setThreadClassLoader(classLoader);
	}


	/**
	 *
	 */
	public static JRImageRenderer getInstance(byte[] imageData)
	{
		return getInstance(imageData, JRImage.ON_ERROR_TYPE_ERROR);
	}


	/**
	 *
	 */
	public static JRImageRenderer getInstance(byte[] imageData, byte onErrorType)
	{
//		if (imageData == null || imageData.length == 0)
//		{
//			return null;
//		}
		return new JRImageRenderer(imageData, onErrorType);
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(String imageLocation) throws JRException
	{
		return getInstance(imageLocation, JRImage.ON_ERROR_TYPE_ERROR, true);
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(String imageLocation, byte onErrorType) throws JRException
	{
		return getInstance(imageLocation, onErrorType, true);
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(String imageLocation, byte onErrorType, boolean isLazy) throws JRException
	{
		return getInstance(imageLocation, onErrorType, isLazy, null, null);
	}

	
	public static JRRenderable getInstance(
		String imageLocation, 
		byte onErrorType, 
		boolean isLazy, 
		ClassLoader classLoader
		) throws JRException
	{
		return getInstance(imageLocation, onErrorType, isLazy, classLoader);
	}
	
	/**
	 *
	 */
	public static JRRenderable getInstance(
		String imageLocation, 
		byte onErrorType, 
		boolean isLazy, 
		ClassLoader classLoader,
		URLStreamHandlerFactory urlHandlerFactory
		) throws JRException
	{
		if (imageLocation == null)
		{
			return null;
		}

		if (isLazy)
		{
			return new JRImageRenderer(imageLocation, onErrorType);
		}

		try
		{
			byte[] data = JRLoader.loadBytesFromLocation(imageLocation, classLoader, urlHandlerFactory);
			return new JRImageRenderer(data, onErrorType);
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e);
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(Image img, byte onErrorType) throws JRException
	{
		return getInstance(img, JRRenderable.IMAGE_TYPE_JPEG, onErrorType);
	}


	/**
	 * Creates and returns an instance of the JRImageRenderer class after encoding the image object using an image
	 * encoder that supports the supplied image type.
	 * 
	 * @param image the java.awt.Image object to wrap into a JRImageRenderer instance
	 * @param imageType the type of the image as specified by one of the constants defined in the JRRenderable interface
	 * @param onErrorType one of the error type constants defined in the JRImage interface
	 * @return the image renderer instance
	 */
	public static JRRenderable getInstance(Image image, byte imageType, byte onErrorType) throws JRException
	{
		try
		{
			return new JRImageRenderer(JRImageLoader.loadImageDataFromAWTImage(image, imageType), onErrorType);
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e); 
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(InputStream is, byte onErrorType) throws JRException
	{
		try
		{
			return new JRImageRenderer(JRLoader.loadBytes(is), onErrorType);
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e); 
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(URL url, byte onErrorType) throws JRException
	{
		try
		{
			return new JRImageRenderer(JRLoader.loadBytes(url), onErrorType);
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e); 
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(File file, byte onErrorType) throws JRException
	{
		try
		{
			return new JRImageRenderer(JRLoader.loadBytes(file), onErrorType);
		}
		catch (JRException e)
		{
			return getOnErrorRenderer(onErrorType, e); 
		}
	}


	/**
	 *
	 */
	private static JRImageRenderer getOnErrorRenderer(byte onErrorType, JRException e) throws JRException
	{
		JRImageRenderer renderer = null;
		
		switch (onErrorType)
		{
			case JRImage.ON_ERROR_TYPE_ICON :
			{
				renderer = new JRImageRenderer("net/sf/jasperreports/engine/images/noimage.GIF", JRImage.ON_ERROR_TYPE_ERROR);
				//FIXME cache these renderers
				break;
			}
			case JRImage.ON_ERROR_TYPE_BLANK :
			{
				renderer = new JRImageRenderer("net/sf/jasperreports/engine/images/pixel.GIF", JRImage.ON_ERROR_TYPE_ERROR);
				break;
			}
			case JRImage.ON_ERROR_TYPE_ERROR :
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
		Image awtImage = null;
		if (awtImageRef == null || awtImageRef.get() == null) {
			try
			{
				awtImage = JRImageLoader.loadImage(getImageData());
				awtImageRef = new SoftReference(awtImage);
			}
			catch (JRException e)
			{
				return getOnErrorRenderer(onErrorType, e).getImage();
			}
		}
		return (Image) awtImageRef.get();
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
			try
			{
				imageData = JRLoader.loadBytesFromLocation(imageLocation);
			}
			catch (JRException e)
			{
				imageData = getOnErrorRenderer(onErrorType, e).getImageData();
			}
		}

		return imageData;
	}


	/**
	 *
	 */
	public void render(Graphics2D grx, Rectangle2D rectanle) throws JRException
	{
		Image img = getImage();

		grx.drawImage(
			img, 
			(int)rectanle.getX(), 
			(int)rectanle.getY(), 
			(int)rectanle.getWidth(), 
			(int)rectanle.getHeight(), 
			null
			);
	}


}

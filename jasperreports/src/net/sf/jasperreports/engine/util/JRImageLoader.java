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
package net.sf.jasperreports.engine.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLStreamHandlerFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRenderable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRImageLoader
{


	/**
	 *
	 */
	public static final byte NO_IMAGE = 1;
	public static final byte SUBREPORT_IMAGE = 2;
	public static final byte CHART_IMAGE = 3;
	public static final byte CROSSTAB_IMAGE = 4;

	private static final String str_NO_IMAGE = "net/sf/jasperreports/engine/images/noimage.GIF";
	private static final String str_SUBREPORT_IMAGE = "net/sf/jasperreports/engine/images/subreport.GIF";
	private static final String str_CHART_IMAGE = "net/sf/jasperreports/engine/images/chart.GIF";
	private static final String str_CROSSTAB_IMAGE = "net/sf/jasperreports/engine/images/crosstab.GIF";
	private static Image img_NO_IMAGE = null;
	private static Image img_SUBREPORT_IMAGE = null;
	private static Image img_CHART_IMAGE = null;
	private static Image img_CROSSTAB_IMAGE = null;

	/**
	 *
	 */
	private static JRImageReader imageReader = null;
	private static JRImageEncoder imageEncoder = null;
	

	static
	{
		try 
		{
			JRClassLoader.loadClassForName("javax.imageio.ImageIO");

			Class clazz = JRClassLoader.loadClassForName("net.sf.jasperreports.engine.util.JRJdk14ImageReader");	
			imageReader = (JRImageReader) clazz.newInstance();
		}
		catch (Exception e)
		{
			imageReader = new JRJdk13ImageReader();
		}

		try 
		{
			JRClassLoader.loadClassForName("javax.imageio.ImageIO");

			Class clazz = JRClassLoader.loadClassForName("net.sf.jasperreports.engine.util.JRJdk14ImageEncoder");	
			imageEncoder = (JRImageEncoder) clazz.newInstance();
		}
		catch (Exception e)
		{
			imageEncoder = new JRDefaultImageEncoder();
		}
	}


	/**
	 * @deprecated Replaced by {@link JRLoader#loadBytes(File)}.
	 */
	public static byte[] loadImageDataFromFile(File file) throws JRException
	{
		return JRLoader.loadBytes(file);
	}


	/**
	 * @deprecated Replaced by {@link JRLoader#loadBytes(URL)}.
	 */
	public static byte[] loadImageDataFromURL(URL url) throws JRException
	{
		return JRLoader.loadBytes(url);
	}


	/**
	 * @deprecated Replaced by {@link JRLoader#loadBytes(InputStream)}.
	 */
	public static byte[] loadImageDataFromInputStream(InputStream is) throws JRException
	{
		return JRLoader.loadBytes(is);
	}


	/**
	 * @deprecated Replaced by {@link JRLoader#loadBytesFromLocation(String)}.
	 */
	public static byte[] loadImageDataFromLocation(String location) throws JRException
	{
		return JRLoader.loadBytesFromLocation(location);
	}

	/**
	 * @deprecated Replaced by {@link JRLoader#loadBytesFromLocation(String, ClassLoader)}.
	 */
	public static byte[] loadImageDataFromLocation(String location, ClassLoader classLoader) throws JRException
	{
		return JRLoader.loadBytesFromLocation(location, classLoader);
	}
	
	/**
	 * @deprecated Replaced by {@link JRLoader#loadBytesFromLocation(String, ClassLoader, URLStreamHandlerFactory)}.
	 */
	public static byte[] loadImageDataFromLocation(
		String location, 
		ClassLoader classLoader,
		URLStreamHandlerFactory urlHandlerFactory
		) throws JRException
	{
		return JRLoader.loadBytesFromLocation(location, classLoader, urlHandlerFactory);
	}


	/**
	 * Encoding the image object using an image encoder that supports the supplied image type.
	 * 
	 * @param image the java.awt.Image object to encode
	 * @param imageType the type of the image as specified by one of the constants defined in the JRRenderable interface
	 * @return the encoded image data
	 */
	public static byte[] loadImageDataFromAWTImage(Image image, byte imageType) throws JRException
	{
		return imageEncoder.encode(image, imageType);
	}


	/**
	 * Encodes the image object using an image encoder that supports the JRRenderable.IMAGE_TYPE_JPEG image type.
	 * 
	 * @deprecated Replaced by {@link JRImageLoader#loadImageDataFromAWTImage(Image, byte)}.
	 */
	public static byte[] loadImageDataFromAWTImage(BufferedImage bi) throws JRException
	{
		return loadImageDataFromAWTImage(bi, JRRenderable.IMAGE_TYPE_JPEG);
	}


	/**
	 * Encodes the image object using an image encoder that supports the JRRenderable.IMAGE_TYPE_JPEG image type.
	 * 
	 * @deprecated Replaced by {@link JRImageLoader#loadImageDataFromAWTImage(Image, byte)}.
	 */
	public static byte[] loadImageDataFromAWTImage(Image image) throws JRException
	{
		return loadImageDataFromAWTImage(image, JRRenderable.IMAGE_TYPE_JPEG);
	}


	/**
	 *
	 */
	public static Image getImage(byte index) throws JRException
	{
		Image image = null;

		switch(index)
		{
			case NO_IMAGE:
			{
				if (img_NO_IMAGE == null)
				{
					img_NO_IMAGE = loadImage(str_NO_IMAGE);
				}
				image = img_NO_IMAGE;
				break;
			}
			case SUBREPORT_IMAGE:
			{
				if (img_SUBREPORT_IMAGE == null)
				{
					img_SUBREPORT_IMAGE = loadImage(str_SUBREPORT_IMAGE);
				}
				image = img_SUBREPORT_IMAGE;
				break;
			}
			case CHART_IMAGE:
			{
				if (img_CHART_IMAGE == null)
				{
					img_CHART_IMAGE = loadImage(str_CHART_IMAGE);
				}
				image = img_CHART_IMAGE;
				break;
			}
			case CROSSTAB_IMAGE:
			{
				if (img_CROSSTAB_IMAGE == null)
				{
					img_CROSSTAB_IMAGE = loadImage(str_CROSSTAB_IMAGE);
				}
				image = img_CROSSTAB_IMAGE;
				break;
			}
		}
		
		return image;
	}


	/**
	 *
	 */
	public static Image loadImage(byte[] bytes) throws JRException
	{
		return imageReader.readImage(bytes);
	}


	/**
	 * Loads an image from an specified resource.
	 * 
	 * @param image the resource name
	 * @throws JRException
	 */
	protected static Image loadImage(String image) throws JRException 
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource(image);
		if (url == null)
		{
			//if (!wasWarning)
			//{
			//	if (log.isWarnEnabled())
			//		log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRImageLoader class. Using JRImageLoader.class.getClassLoader() instead.");
			//	wasWarning = true;
			//}
			classLoader = JRImageLoader.class.getClassLoader();
		}
		InputStream is;
		if (classLoader == null)
		{
			is = JRImageLoader.class.getResourceAsStream("/" + image);
		}
		else
		{
			is = classLoader.getResourceAsStream(image);
		}
		
		return imageReader.readImage(JRLoader.loadBytes(is));
	}

}

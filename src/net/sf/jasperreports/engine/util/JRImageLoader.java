/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLStreamHandlerFactory;

import net.sf.jasperreports.engine.JRException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


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
	//private static boolean wasWarning = false;


	/**
	 *
	 */
	public static byte[] loadImageDataFromFile(File file) throws JRException
	{
		try
		{
			return JRLoader.loadBytes(file);
		}
		catch (JRException e)
		{
			throw new JRException("Error loading image data : " + file, e);
		}
	}


	/**
	 *
	 */
	public static byte[] loadImageDataFromURL(URL url) throws JRException
	{
		try
		{
			return JRLoader.loadBytes(url);
		}
		catch (JRException e)
		{
			throw new JRException("Error loading image data : " + url, e);
		}
	}


	/**
	 *
	 */
	public static byte[] loadImageDataFromInputStream(InputStream is) throws JRException
	{
		try
		{
			return JRLoader.loadBytes(is);
		}
		catch (JRException e)
		{
			throw new JRException("Error loading image data from input stream.", e);
		}
	}


	/**
	 *
	 */
	public static byte[] loadImageDataFromLocation(String location) throws JRException
	{
		return loadImageDataFromLocation(location, null, null);
	}

	public static byte[] loadImageDataFromLocation(String location, ClassLoader classLoader) throws JRException
	{
		return loadImageDataFromLocation(location, classLoader, null);
	}
	
	/**
	 *
	 */
	public static byte[] loadImageDataFromLocation(String location, ClassLoader classLoader,
			URLStreamHandlerFactory urlHandlerFactory) throws JRException
	{
		URL url = JRResourcesUtil.createURL(location, urlHandlerFactory);
		if (url != null)
		{
			return loadImageDataFromURL(url);
		}

		File file = new File(location);
		if (file.exists() && file.isFile())
		{
			return loadImageDataFromFile(file);
		}

		url = JRResourcesUtil.findClassLoaderResource(location, classLoader, JRImageLoader.class);
		if (url != null)
		{
			return loadImageDataFromURL(url);
		}

		throw new JRException("Image not found : " + location);
	}


	/**
	 *
	 */
	public static byte[] loadImageDataFromAWTImage(BufferedImage bi) throws JRException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
		try
		{
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
			param.setQuality(1f, true);//1f = JPG_QUALITY
			encoder.encode(bi, param);
		}
		catch (IOException e)
		{
			throw new JRException("Error trying to load image data from AWT image.", e);
		}
		
		return baos.toByteArray();
	}


	/**
	 *
	 */
	public static byte[] loadImageDataFromAWTImage(Image img) throws JRException
	{
		BufferedImage bi =
			new BufferedImage(
				img.getWidth(null),
				img.getHeight(null),
				BufferedImage.TYPE_INT_RGB
				);

		Graphics g = bi.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();

		return loadImageDataFromAWTImage(bi);
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
		Image image = Toolkit.getDefaultToolkit().createImage( bytes );

		MediaTracker tracker = new MediaTracker(new Panel());
		tracker.addImage(image, 0);
		try
		{
			tracker.waitForID(0);
		}
		catch (Exception e)
		{
			//image = null;
			throw new JRException(e);
		}

		try
		{
			if(tracker.isErrorID(0)) {
				throw new JRException("Image failed to load.");
			}
		}
		catch (JRException e)
		{
			throw e;
		}

		return image;
	}


	/**
	 * Loads an image from an specified resource.
	 * 
	 * @param image the resource name
	 * @throws JRException
	 */
	protected static Image loadImage(String image) throws JRException {
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
		
		return loadImage(
			loadImageDataFromInputStream(is)
			);
	}

}

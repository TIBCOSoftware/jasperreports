/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import net.sf.jasperreports.engine.util.JRImageLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRImageRenderer implements JRRenderable
{

	/**
	 *
	 */
	private static final long serialVersionUID = 605;

	/**
	 *
	 */
	private byte[] imageData = null;
	private String imageLocation = null;
	private byte whenNotAvailableType = JRImage.WHEN_NOT_AVAILABLE_TYPE_NONE;

	/**
	 *
	 */
	private transient Image awtImage = null;

	
	/**
	 *
	 */
	private JRImageRenderer(byte[] imageData, byte whenNotAvailableType)
	{
		this.imageData = imageData;
		this.whenNotAvailableType = whenNotAvailableType;
	}


	/**
	 *
	 */
	private JRImageRenderer(String imageLocation, byte whenNotAvailableType)
	{
		this.imageLocation = imageLocation;
		this.whenNotAvailableType = whenNotAvailableType;
	}


	/**
	 *
	 */
	public static JRImageRenderer getInstance(byte[] imageData)
	{
		return getInstance(imageData, JRImage.WHEN_NOT_AVAILABLE_TYPE_NONE);
	}


	/**
	 *
	 */
	public static JRImageRenderer getInstance(byte[] imageData, byte whenNotAvailableType)
	{
		if (imageData == null || imageData.length == 0)
		{
			return null;
		}
		else
		{
			return new JRImageRenderer(imageData, whenNotAvailableType);
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(String imageLocation) throws JRException
	{
		return getInstance(imageLocation, JRImage.WHEN_NOT_AVAILABLE_TYPE_NONE, false);
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(String imageLocation, byte whenNotAvailableType) throws JRException
	{
		return getInstance(imageLocation, whenNotAvailableType, false);
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(String imageLocation, byte whenNotAvailableType, boolean isLazy) throws JRException
	{
		if (imageLocation == null)
		{
			return null;
		}
		else
		{
			if (isLazy)
			{
				try
				{
					return new JRImageRenderer(JRImageLoader.loadImageDataFromLocation(imageLocation), whenNotAvailableType);
				}
				catch (JRException e)
				{
					return getWhenNotAvailableRenderer(whenNotAvailableType, e);
				}
			}
			else
			{
				return new JRImageRenderer(imageLocation, whenNotAvailableType);
			}
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(Image img, byte whenNotAvailableType) throws JRException
	{
		try
		{
			return new JRImageRenderer(JRImageLoader.loadImageDataFromAWTImage(img), whenNotAvailableType);
		}
		catch (JRException e)
		{
			return getWhenNotAvailableRenderer(whenNotAvailableType, e); 
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(InputStream is, byte whenNotAvailableType) throws JRException
	{
		try
		{
			return new JRImageRenderer(JRImageLoader.loadImageDataFromInputStream(is), whenNotAvailableType);
		}
		catch (JRException e)
		{
			return getWhenNotAvailableRenderer(whenNotAvailableType, e); 
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(URL url, byte whenNotAvailableType) throws JRException
	{
		try
		{
			return new JRImageRenderer(JRImageLoader.loadImageDataFromURL(url), whenNotAvailableType);
		}
		catch (JRException e)
		{
			return getWhenNotAvailableRenderer(whenNotAvailableType, e); 
		}
	}


	/**
	 *
	 */
	public static JRRenderable getInstance(File file, byte whenNotAvailableType) throws JRException
	{
		try
		{
			return new JRImageRenderer(JRImageLoader.loadImageDataFromFile(file), whenNotAvailableType);
		}
		catch (JRException e)
		{
			return getWhenNotAvailableRenderer(whenNotAvailableType, e); 
		}
	}


	/**
	 *
	 */
	private static JRImageRenderer getWhenNotAvailableRenderer(byte whenNotAvailableType, JRException e) throws JRException
	{
		JRImageRenderer renderer = null;
		
		switch (whenNotAvailableType)
		{
			case JRImage.WHEN_NOT_AVAILABLE_TYPE_ICON :
			{
				renderer = new JRImageRenderer("net/sf/jasperreports/engine/images/noimage.GIF", JRImage.WHEN_NOT_AVAILABLE_TYPE_NONE);
				//FIXME cache these renderers
				break;
			}
			case JRImage.WHEN_NOT_AVAILABLE_TYPE_BLANK :
			{
				renderer = new JRImageRenderer("net/sf/jasperreports/engine/images/pixel.GIF", JRImage.WHEN_NOT_AVAILABLE_TYPE_NONE);
				break;
			}
			case JRImage.WHEN_NOT_AVAILABLE_TYPE_NONE :
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
		if (awtImage == null)
		{
			try
			{
				awtImage = JRImageLoader.loadImage(getImageData());
			}
			catch (JRException e)
			{
				awtImage = getWhenNotAvailableRenderer(whenNotAvailableType, e).getImage();
			}
		}
		return awtImage;
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
				imageData = JRImageLoader.loadImageDataFromLocation(imageLocation);
			}
			catch (JRException e)
			{
				imageData = getWhenNotAvailableRenderer(whenNotAvailableType, e).getImageData();
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

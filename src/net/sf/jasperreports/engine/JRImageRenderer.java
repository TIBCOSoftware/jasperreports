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

	/**
	 *
	 */
	private transient Image awtImage = null;

	
	/**
	 *
	 */
	protected JRImageRenderer(byte[] imageData)
	{
		this.imageData = imageData;
	}


	/**
	 *
	 */
	public static JRImageRenderer getInstance(byte[] imageData)
	{
		if (imageData == null || imageData.length == 0)
		{
			return null;
		}
		else
		{
			return new JRImageRenderer(imageData);
		}
	}


	/**
	 *
	 */
	private Image getImage()
	{
		if (awtImage == null)
		{
			awtImage = JRImageLoader.loadImage(imageData);
		}
		return awtImage;
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
	public Dimension2D getDimension()
	{
		Image img = getImage();
		return new Dimension(img.getWidth(null), img.getHeight(null));
	}


	/**
	 *
	 */
	public byte[] getImageData()
	{
		return imageData;
	}


	/**
	 *
	 */
	public void render(Graphics2D grx, Rectangle2D rectanle)
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

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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

import net.sf.jasperreports.engine.util.JRImageLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractSvgRenderer implements JRRenderable
{

	/**
	 *
	 */
	private static final long serialVersionUID = 10001;

	
	/**
	 *
	 */
	public byte getType()
	{
		return TYPE_SVG;
	}


	/**
	 *
	 */
	public Dimension2D getDimension()
	{
		return null;
	}


	/**
	 *
	 */
	public Color getBackcolor()
	{
		return null;
	}


	/**
	 *
	 */
	public byte[] getImageData() throws JRException
	{
		Dimension2D dimension = getDimension();
		if (dimension != null)
		{
			BufferedImage bi =
				new BufferedImage(
					(int)dimension.getWidth(),
					(int)dimension.getHeight(),
					BufferedImage.TYPE_INT_RGB
					);

			Graphics2D g = bi.createGraphics();
			Color backcolor = getBackcolor();
			if (backcolor != null)
			{
				g.setColor(backcolor);
				g.fillRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
			}
			render(g, new Rectangle((int)dimension.getWidth(), (int)dimension.getHeight()));
			g.dispose();
			
			try
			{
				return JRImageLoader.loadImageDataFromAWTImage(bi);
			}
			catch (JRException e)
			{
				throw new JRRuntimeException(e);
			}
		}
		return null;
	}


}

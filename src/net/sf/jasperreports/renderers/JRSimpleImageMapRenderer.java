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
package net.sf.jasperreports.renderers;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImageMapRenderer;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRRenderable;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRSimpleImageMapRenderer implements JRImageMapRenderer
{
	
	private JRRenderable imageRenderer;
	private List areaHyperlinks;
	
	/**
	 * 
	 * 
	 */
	public JRSimpleImageMapRenderer(JRImageRenderer imageRenderer, List areaHyperlinks) {
		this.imageRenderer = imageRenderer;
		this.areaHyperlinks = areaHyperlinks;
	}

	/**
	 * 
	 * 
	 */
	public JRSimpleImageMapRenderer(byte[] imageData, List areaHyperlinks) {
		this.imageRenderer = JRImageRenderer.getInstance(imageData);
		this.areaHyperlinks = areaHyperlinks;
	}

	/**
	 * 
	 * 
	 */
	public JRSimpleImageMapRenderer(String imageLocation, List areaHyperlinks) throws JRException
	{
		this.imageRenderer = JRImageRenderer.getInstance(imageLocation);
		this.areaHyperlinks = areaHyperlinks;
	}

	/**
	 * 
	 * 
	 */
	public List getImageAreaHyperlinks(Rectangle2D renderingArea) throws JRException 
	{
		return areaHyperlinks;
	}

	/**
	 * 
	 * 
	 */
	public Dimension2D getDimension() throws JRException {
	    return imageRenderer.getDimension();
    }

	/**
	 * 
	 * 
	 */
	public String getId() {
	    return imageRenderer.getId();
    }

	/**
	 * 
	 * 
	 */
	public byte[] getImageData() throws JRException {
	    return imageRenderer.getImageData();
    }

	/**
	 * 
	 * 
	 */
	public byte getImageType() {
	    return imageRenderer.getImageType();
    }

	/**
	 * 
	 * 
	 */
	public byte getType() {
	    return imageRenderer.getType();
    }

	/**
	 * 
	 * 
	 */
	public void render(Graphics2D grx, Rectangle2D rectangle)
            throws JRException {
		imageRenderer.render(grx, rectangle);
    }
	
}

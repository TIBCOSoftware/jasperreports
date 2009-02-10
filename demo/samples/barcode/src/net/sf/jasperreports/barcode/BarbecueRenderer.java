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
package net.sf.jasperreports.barcode;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.JRAbstractSvgRenderer;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.output.OutputException;


/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarbecueRenderer extends JRAbstractSvgRenderer
{

	private static final long serialVersionUID = 1L;
	
	private Barcode barcode = null;

	public BarbecueRenderer(Barcode barcode) 
	{
		this.barcode = barcode;
	}

	public Dimension2D getDimension()
	{
		return barcode.getSize();
	}

	public void render(Graphics2D grx, Rectangle2D rectangle) 
	{
		AffineTransform origTransform = grx.getTransform();
		try
		{
			Dimension size = barcode.getSize();
			grx.translate(rectangle.getX(), rectangle.getY());
			grx.scale(rectangle.getWidth() / size.getWidth(), rectangle.getHeight() / size.getHeight());
			barcode.draw(grx, 0, 0);
		}
		catch (OutputException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			grx.setTransform(origTransform);
		}
	}
	
}

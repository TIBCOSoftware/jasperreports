/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.components.list;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.JRAbstractSvgRenderer;
import net.sf.jasperreports.engine.JRException;

/**
 * Am image renderer used to mark the unused vertical space in a list
 * component preview.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class UnusedSpaceImageRenderer extends JRAbstractSvgRenderer
{

	private static final long serialVersionUID = 1L;

	// transparent light grey
	private static final Color FILL = new Color(224, 224, 224, 128);
	
	public static final UnusedSpaceImageRenderer INSTANCE = new UnusedSpaceImageRenderer();
	
	private final int lineGap = 15;
	private final int lineWidth = 10;
	
	public void render(Graphics2D grx, Rectangle2D rectangle)
			throws JRException
	{
		Graphics2D graphics = (Graphics2D) grx.create();
		graphics.translate(rectangle.getX(), rectangle.getY());
		graphics.setColor(FILL);
		
		int width = (int) rectangle.getWidth();
		int limit = width + (int) rectangle.getHeight();
		int increment = lineGap + lineWidth;
		int reverseOffset = (width - 4 * lineWidth / 3) % increment;
		for (int x = 0; x <= limit; x += increment)
		{
			graphics.fillPolygon(
					new int[]{x, x + lineWidth, 0, 0},
					new int[]{0, 0, x + lineWidth, x},
					4);

			graphics.fillPolygon(
					new int[]{width - x - reverseOffset, width - x - lineWidth - reverseOffset, width, width},
					new int[]{0, 0, x + lineWidth, x},
					4);
		}
	}

}

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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export.draw;

import java.awt.Graphics2D;
import java.awt.Stroke;

import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintLine;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
 */
public class LineDrawer extends ElementDrawer
{

	/**
	 *
	 */
	public void draw(Graphics2D grx, JRPrintElement element, int offsetX, int offsetY)
	{
		JRPrintLine line = (JRPrintLine)element;
		
		grx.setColor(line.getLinePen().getLineColor());
		
		Stroke stroke = getBorderStroke(line.getLinePen());

		if (stroke != null)
		{
			grx.setStroke(stroke);
			
			if (line.getWidth() == 1)
			{
				if (line.getHeight() == 1)
				{
					//Nothing to draw
				}
				else
				{
					//Vertical line
					grx.translate(0.5, 0);
					grx.drawLine(
						line.getX() + offsetX, 
						line.getY() + offsetY,
						line.getX() + offsetX,  
						line.getY() + offsetY + line.getHeight()
						);
					grx.translate(-0.5, 0);
				}
			}
			else
			{
				if (line.getHeight() == 1)
				{
					//Horizontal line
					grx.translate(0, 0.5);
					grx.drawLine(
						line.getX() + offsetX, 
						line.getY() + offsetY,
						line.getX() + offsetX + line.getWidth(),  
						line.getY() + offsetY
						);
					grx.translate(0, -0.5);
				}
				else
				{
					//Oblique line
					if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
					{
						grx.drawLine(
							line.getX() + offsetX, 
							line.getY() + offsetY,
							line.getX() + offsetX + line.getWidth(),  
							line.getY() + offsetY + line.getHeight()
							);
					}
					else
					{
						grx.drawLine(
							line.getX() + offsetX, 
							line.getY() + offsetY + line.getHeight(),
							line.getX() + offsetX + line.getWidth(),  
							line.getY() + offsetY
							);
					}
				}
			}
		}
	}

}

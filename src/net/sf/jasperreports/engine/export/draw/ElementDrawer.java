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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRPrintElement;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
 */
public abstract class ElementDrawer
{

	private static final double THIN_CORNER_OFFSET = 0.25d;
	private static final double ONE_POINT_CORNER_OFFSET = 0.5d;
	
	private static final Stroke STROKE_THIN = new BasicStroke(0.5f);
	private static final Stroke STROKE_1_POINT = new BasicStroke(1f);
	private static final Stroke STROKE_2_POINT = new BasicStroke(2f);
	private static final Stroke STROKE_4_POINT = new BasicStroke(4f);
	private static final Stroke STROKE_DOTTED = 
		new BasicStroke(
			1f,
			BasicStroke.CAP_SQUARE,
			BasicStroke.JOIN_MITER,
			10f,
			new float[]{5f, 3f},
			0f
			);
	
	private static final Stroke BORDER_STROKE_THIN = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	private static final Stroke BORDER_STROKE_1_POINT = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	private static final Stroke BORDER_STROKE_DOTTED = 
		new BasicStroke(
			1f,
			BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER,
			10f,
			new float[]{5f, 3f},
			0f
			);

	/**
	 *
	 */
	public abstract void draw(Graphics2D grx, JRPrintElement element, int offsetX, int offsetY) throws JRException;
	
	
	/**
	 *
	 */
	protected void drawBox(Graphics2D grx, JRBox box, JRPrintElement element, int offsetX, int offsetY)
	{
		Stroke topStroke = null;
		Stroke leftStroke = null;
		Stroke bottomStroke = null;
		Stroke rightStroke = null;
		
		if (box != null)
		{
			topStroke = getBorderStroke(box.getTopBorder());
			leftStroke = getBorderStroke(box.getLeftBorder());
			bottomStroke = getBorderStroke(box.getBottomBorder());
			rightStroke = getBorderStroke(box.getRightBorder());
		}

		if (topStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(box.getTopBorder());
			
			grx.setStroke(topStroke);
			grx.setColor(box.getTopBorderColor() == null ? element.getForecolor() : box.getTopBorderColor());
	
			grx.translate(0, cornerOffset);
			grx.drawLine(
				element.getX() + offsetX, 
				element.getY() + offsetY, 
				element.getX() + offsetX + element.getWidth(),
				element.getY() + offsetY
				);
			grx.translate(0, -cornerOffset);
		}

		if (leftStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(box.getLeftBorder());
			
			grx.setStroke(leftStroke);
			grx.setColor(box.getLeftBorderColor() == null ? element.getForecolor() : box.getLeftBorderColor());
	
			grx.translate(cornerOffset, 0);
			grx.drawLine(
				element.getX() + offsetX, 
				element.getY() + offsetY, 
				element.getX() + offsetX,
				element.getY() + offsetY + element.getHeight()
				);
			grx.translate(-cornerOffset, 0);
		}

		if (bottomStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(box.getBottomBorder());
			
			grx.setStroke(bottomStroke);
			grx.setColor(box.getBottomBorderColor() == null ? element.getForecolor() : box.getBottomBorderColor());
	
			grx.translate(0, -cornerOffset);
			grx.drawLine(
				element.getX() + offsetX, 
				element.getY() + offsetY + element.getHeight(),
				element.getX() + offsetX + element.getWidth(),
				element.getY() + offsetY + element.getHeight()
				);
			grx.translate(0, cornerOffset);
		}

		if (rightStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(box.getRightBorder());
			
			grx.setStroke(rightStroke);
			grx.setColor(box.getRightBorderColor() == null ? element.getForecolor() : box.getRightBorderColor());
	
			grx.translate(-cornerOffset, 0);
			grx.drawLine(
				element.getX() + offsetX + element.getWidth(),
				element.getY() + offsetY,
				element.getX() + offsetX + element.getWidth(),
				element.getY() + offsetY + element.getHeight()
				);
			grx.translate(cornerOffset, 0);
		}
	}

	
	/**
	 * 
	 */
	protected static Stroke getStroke(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				return STROKE_DOTTED;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				return STROKE_4_POINT;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				return STROKE_2_POINT;
			}
			case JRGraphicElement.PEN_NONE :
			{
				return null;
			}
			case JRGraphicElement.PEN_THIN :
			{
				return STROKE_THIN;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				return STROKE_1_POINT;
			}
		}
	}

	
	/**
	 * 
	 */
	protected static double getBorderCornerOffset(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_THIN :
			{
				return THIN_CORNER_OFFSET;
			}
			case JRGraphicElement.PEN_1_POINT :
			case JRGraphicElement.PEN_DOTTED :
			{
				return ONE_POINT_CORNER_OFFSET;
			}
			default :
			{
				return 0;
			}
		}
	}


	/**
	 * 
	 */
	protected static int getRectangleSizeAdjust(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_1_POINT:
			case JRGraphicElement.PEN_DOTTED:
				return 1;
			default:
				return 0;
		}
	}


	/**
	 * 
	 */
	private static Stroke getBorderStroke(byte pen)
	{
		switch (pen)
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				return BORDER_STROKE_DOTTED;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				return STROKE_4_POINT;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				return STROKE_2_POINT;
			}
			case JRGraphicElement.PEN_NONE :
			{
				return null;
			}
			case JRGraphicElement.PEN_THIN :
			{
				return BORDER_STROKE_THIN;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				return BORDER_STROKE_1_POINT;
			}
		}
	}


}

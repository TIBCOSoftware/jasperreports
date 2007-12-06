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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
 */
public abstract class ElementDrawer
{

//	private static final double THIN_CORNER_OFFSET = 0.25d;
//	private static final double ONE_POINT_CORNER_OFFSET = 0.5d;
//	
//	private static final Stroke STROKE_THIN = new BasicStroke(0.5f);
//	private static final Stroke STROKE_1_POINT = new BasicStroke(1f);
//	private static final Stroke STROKE_2_POINT = new BasicStroke(2f);
//	private static final Stroke STROKE_4_POINT = new BasicStroke(4f);
//	private static final Stroke STROKE_DOTTED = 
//		new BasicStroke(
//			1f,
//			BasicStroke.CAP_SQUARE,
//			BasicStroke.JOIN_MITER,
//			10f,
//			new float[]{5f, 3f},
//			0f
//			);
//	
//	private static final Stroke BORDER_STROKE_THIN = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
//	private static final Stroke BORDER_STROKE_1_POINT = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
//	private static final Stroke BORDER_STROKE_DOTTED = 
//		new BasicStroke(
//			1f,
//			BasicStroke.CAP_BUTT,
//			BasicStroke.JOIN_MITER,
//			10f,
//			new float[]{5f, 3f},
//			0f
//			);

	/**
	 *
	 */
	public abstract void draw(Graphics2D grx, JRPrintElement element, int offsetX, int offsetY) throws JRException;
	
	
	/**
	 *
	 */
	protected void drawBox(Graphics2D grx, JRLineBox box, JRPrintElement element, int offsetX, int offsetY)
	{
		drawTopPen(grx, box.getTopPen(), element, offsetX, offsetY);
		drawLeftPen(grx, box.getLeftPen(), element, offsetX, offsetY);
		drawBottomPen(grx, box.getBottomPen(), element, offsetX, offsetY);
		drawRightPen(grx, box.getRightPen(), element, offsetX, offsetY);
	}

	
	/**
	 *
	 */
	protected void drawPen(Graphics2D grx, JRPen pen, JRPrintElement element, int offsetX, int offsetY)
	{
		drawTopPen(grx, pen, element, offsetX, offsetY);
		drawLeftPen(grx, pen, element, offsetX, offsetY);
		drawBottomPen(grx, pen, element, offsetX, offsetY);
		drawRightPen(grx, pen, element, offsetX, offsetY);
	}

	
	/**
	 *
	 */
	protected void drawTopPen(Graphics2D grx, JRPen topPen, JRPrintElement element, int offsetX, int offsetY)
	{
		Stroke topStroke = getBorderStroke(topPen);

		if (topStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(topPen);
			
			grx.setStroke(topStroke);
			grx.setColor(topPen.getLineColor());
	
			grx.translate(0, cornerOffset);
			grx.drawLine(
				element.getX() + offsetX, 
				element.getY() + offsetY, 
				element.getX() + offsetX + element.getWidth(),
				element.getY() + offsetY
				);
			grx.translate(0, -cornerOffset);
		}
	}

	
	/**
	 *
	 */
	protected void drawLeftPen(Graphics2D grx, JRPen leftPen, JRPrintElement element, int offsetX, int offsetY)
	{
		Stroke leftStroke = getBorderStroke(leftPen);

		if (leftStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(leftPen);
			
			grx.setStroke(leftStroke);
			grx.setColor(leftPen.getLineColor());
	
			grx.translate(cornerOffset, 0);
			grx.drawLine(
				element.getX() + offsetX, 
				element.getY() + offsetY, 
				element.getX() + offsetX,
				element.getY() + offsetY + element.getHeight()
				);
			grx.translate(-cornerOffset, 0);
		}
	}

	
	/**
	 *
	 */
	protected void drawBottomPen(Graphics2D grx, JRPen bottomPen, JRPrintElement element, int offsetX, int offsetY)
	{
		Stroke bottomStroke = getBorderStroke(bottomPen);

		if (bottomStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(bottomPen);
			
			grx.setStroke(bottomStroke);
			grx.setColor(bottomPen.getLineColor());
	
			grx.translate(0, -cornerOffset);
			grx.drawLine(
				element.getX() + offsetX, 
				element.getY() + offsetY + element.getHeight(),
				element.getX() + offsetX + element.getWidth(),
				element.getY() + offsetY + element.getHeight()
				);
			grx.translate(0, cornerOffset);
		}
	}

	
	/**
	 *
	 */
	protected void drawRightPen(Graphics2D grx, JRPen rightPen, JRPrintElement element, int offsetX, int offsetY)
	{
		Stroke rightStroke = getBorderStroke(rightPen);

		if (rightStroke != null)
		{
			double cornerOffset = getBorderCornerOffset(rightPen);
			
			grx.setStroke(rightStroke);
			grx.setColor(rightPen.getLineColor());
	
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
	protected static Stroke getStroke(JRPen pen)
	{
		float lineWidth = pen.getLineWidth().floatValue();
		byte lineStyle = pen.getLineStyle().byteValue();
		
		switch (lineStyle)
		{
			case JRPen.LINE_STYLE_DASHED :
			{
				return
					new BasicStroke(
						lineWidth,
						BasicStroke.CAP_SQUARE,
						BasicStroke.JOIN_MITER,
						10f,
						new float[]{5f, 3f},
						0f
						);
			}
			case JRPen.LINE_STYLE_SOLID :
			default :
			{
				return new BasicStroke(lineWidth);
			}
		}
	}

	
	/**
	 * 
	 */
	protected static double getBorderCornerOffset(JRPen pen)
	{
		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth > 1)
		{
			return 0;
		}
		else
		{
			return lineWidth / 2;
		}
//		switch (pen)
//		{
//			case JRGraphicElement.PEN_THIN :
//			{
//				return THIN_CORNER_OFFSET;
//			}
//			case JRGraphicElement.PEN_1_POINT :
//			case JRGraphicElement.PEN_DOTTED :
//			{
//				return ONE_POINT_CORNER_OFFSET;
//			}
//			default :
//			{
//				return 0;
//			}
//		}
	}


	/**
	 * 
	 */
	protected static int getRectangleSizeAdjust(JRPen pen)
	{
		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth == 1f)
		{
			return 1;
		}
		else
		{
			return 0;
		}
//		switch (pen)
//		{
//			case JRGraphicElement.PEN_1_POINT:
//			case JRGraphicElement.PEN_DOTTED:
//				return 1;
//			default:
//				return 0;
//		}
	}


	/**
	 * 
	 */
	private static Stroke getBorderStroke(JRPen pen)
	{
		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth > 0f)
		{
			switch (pen.getLineStyle().byteValue())
			{
				case JRPen.LINE_STYLE_DASHED :
				{
					return
						new BasicStroke(
							lineWidth,
							BasicStroke.CAP_BUTT,//FIXMEBORDER border stroke have different cap? why? test this again. look into history.
							BasicStroke.JOIN_MITER,
							10f,
							new float[]{5f, 3f},
							0f
							);
				}
				case JRPen.LINE_STYLE_SOLID :
				default :
				{
					return new BasicStroke(lineWidth);
				}
			}
		}
		
		return null;
	}


}

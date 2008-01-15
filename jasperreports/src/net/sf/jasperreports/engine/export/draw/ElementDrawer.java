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
import java.awt.geom.AffineTransform;

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

	/**
	 *
	 */
	public abstract void draw(Graphics2D grx, JRPrintElement element, int offsetX, int offsetY) throws JRException;
	
	
	/**
	 *
	 */
	protected void drawBox(Graphics2D grx, JRLineBox box, JRPrintElement element, int offsetX, int offsetY)
	{
		drawTopPen(
			grx, 
			box.getTopPen(), 
			box.getLeftPen(), 
			box.getRightPen(), 
			element, 
			offsetX, 
			offsetY
			);
		drawLeftPen(
			grx, 
			box.getTopPen(), 
			box.getLeftPen(), 
			box.getBottomPen(), 
			element, 
			offsetX, 
			offsetY
			);
		drawBottomPen(
			grx, 
			box.getLeftPen(), 
			box.getBottomPen(), 
			box.getRightPen(), 
			element, 
			offsetX, 
			offsetY
			);
		drawRightPen(
			grx, 
			box.getTopPen(), 
			box.getBottomPen(), 
			box.getRightPen(), 
			element, 
			offsetX, 
			offsetY
			);
	}

	
	/**
	 *
	 */
	protected void drawPen(Graphics2D grx, JRPen pen, JRPrintElement element, int offsetX, int offsetY)
	{
		drawTopPen(grx, pen, pen, pen, element, offsetX, offsetY);
		drawLeftPen(grx, pen, pen, pen, element, offsetX, offsetY);
		drawBottomPen(grx, pen, pen, pen, element, offsetX, offsetY);
		drawRightPen(grx, pen, pen, pen, element, offsetX, offsetY);
	}

	
	/**
	 *
	 */
	protected void drawTopPen(
		Graphics2D grx, 
		JRPen topPen, 
		JRPen leftPen, 
		JRPen rightPen, 
		JRPrintElement element, 
		int offsetX, 
		int offsetY
		)
	{
		Stroke topStroke = getStroke(topPen, BasicStroke.CAP_BUTT);

		if (topStroke != null)
		{
			grx.setStroke(topStroke);
			grx.setColor(topPen.getLineColor());
	
			AffineTransform oldTx = grx.getTransform();

			if (topPen.getLineStyle().byteValue() == JRPen.LINE_STYLE_DOUBLE)
			{
				grx.translate(
					element.getX() + offsetX - leftPen.getLineWidth().floatValue() / 2, 
					element.getY() + offsetY - topPen.getLineWidth().floatValue() / 3
					);
				grx.scale(
					(element.getWidth() + (leftPen.getLineWidth().floatValue() + rightPen.getLineWidth().floatValue()) / 2) 
						/ element.getWidth(), 
					1
					);
				grx.drawLine(
					0, 
					0, 
					element.getWidth(),
					0
					);

				grx.setTransform(oldTx);

				grx.translate(
					element.getX() + offsetX + leftPen.getLineWidth().floatValue() / 6, 
					element.getY() + offsetY + topPen.getLineWidth().floatValue() / 3
					);
				grx.scale(
					(element.getWidth() - (leftPen.getLineWidth().floatValue() + rightPen.getLineWidth().floatValue()) / 6) 
						/ element.getWidth(), 
					1
					);
				grx.drawLine(
					0, 
					0, 
					element.getWidth(),
					0
					);
			}
			else
			{
				grx.translate(
					element.getX() + offsetX - leftPen.getLineWidth().floatValue() / 2, 
					element.getY() + offsetY
					);
				grx.scale(
					(element.getWidth() + (leftPen.getLineWidth().floatValue() + rightPen.getLineWidth().floatValue()) / 2) 
						/ element.getWidth(), 
					1
					);
				grx.drawLine(
					0, 
					0, 
					element.getWidth(),
					0
					);
			}
			
			grx.setTransform(oldTx);
		}
	}

	
	/**
	 *
	 */
	protected void drawLeftPen(
		Graphics2D grx, 
		JRPen topPen, 
		JRPen leftPen, 
		JRPen bottomPen, 
		JRPrintElement element, 
		int offsetX, 
		int offsetY
		)
	{
		Stroke leftStroke = getStroke(leftPen, BasicStroke.CAP_BUTT);

		if (leftStroke != null)
		{
			grx.setStroke(leftStroke);
			grx.setColor(leftPen.getLineColor());
	
			AffineTransform oldTx = grx.getTransform();

			if (leftPen.getLineStyle().byteValue() == JRPen.LINE_STYLE_DOUBLE)
			{
				grx.translate(
					element.getX() + offsetX - leftPen.getLineWidth().floatValue() / 3, 
					element.getY() + offsetY - topPen.getLineWidth().floatValue() / 2
					);
				grx.scale(
					1,
					(element.getHeight() + (topPen.getLineWidth().floatValue() + bottomPen.getLineWidth().floatValue()) / 2) 
						/ element.getHeight() //FIXMEBORDER this might be zero
					);
				grx.drawLine(
					0, 
					0, 
					0,
					element.getHeight()
					);

				grx.setTransform(oldTx);

				grx.translate(
					element.getX() + offsetX + leftPen.getLineWidth().floatValue() / 3, 
					element.getY() + offsetY + topPen.getLineWidth().floatValue() / 6
					);
				grx.scale(
					1,
					(element.getHeight() - (topPen.getLineWidth().floatValue() + bottomPen.getLineWidth().floatValue()) / 6) 
						/ element.getHeight()
					);
				grx.drawLine(
					0, 
					0, 
					0,
					element.getHeight()
					);
			}
			else
			{
				grx.translate(
					element.getX() + offsetX, 
					element.getY() + offsetY - topPen.getLineWidth().floatValue() / 2
					);
				grx.scale(
					1,
					(element.getHeight() + (topPen.getLineWidth().floatValue() + bottomPen.getLineWidth().floatValue()) / 2) 
						/ element.getHeight()
					);
				grx.drawLine(
					0, 
					0, 
					0,
					element.getHeight()
					);
			}

			grx.setTransform(oldTx);
		}
	}

	
	/**
	 *
	 */
	protected void drawBottomPen(
		Graphics2D grx, 
		JRPen leftPen, 
		JRPen bottomPen, 
		JRPen rightPen, 
		JRPrintElement element, 
		int offsetX, 
		int offsetY
		)
	{
		Stroke bottomStroke = getStroke(bottomPen, BasicStroke.CAP_BUTT);

		if (bottomStroke != null)
		{
			grx.setStroke(bottomStroke);
			grx.setColor(bottomPen.getLineColor());
	
			AffineTransform oldTx = grx.getTransform();

			if (bottomPen.getLineStyle().byteValue() == JRPen.LINE_STYLE_DOUBLE)
			{
				grx.translate(
					element.getX() + offsetX - leftPen.getLineWidth().floatValue() / 2, 
					element.getY() + offsetY + element.getHeight() + bottomPen.getLineWidth().floatValue() / 3
					);
				grx.scale(
					(element.getWidth() + (leftPen.getLineWidth().floatValue() + rightPen.getLineWidth().floatValue()) / 2) 
						/ element.getWidth(), 
					1
					);
				grx.drawLine(
					0, 
					0,
					element.getWidth(),
					0
					);

				grx.setTransform(oldTx);

				grx.translate(
					element.getX() + offsetX + leftPen.getLineWidth().floatValue() / 6, 
					element.getY() + offsetY + element.getHeight() - bottomPen.getLineWidth().floatValue() / 3
					);
				grx.scale(
					(element.getWidth() - (leftPen.getLineWidth().floatValue() + rightPen.getLineWidth().floatValue()) / 6) 
						/ element.getWidth(), 
					1
					);
				grx.drawLine(
					0, 
					0,
					element.getWidth(),
					0
					);
			}
			else
			{
				grx.translate(
					element.getX() + offsetX - leftPen.getLineWidth().floatValue() / 2, 
					element.getY() + offsetY + element.getHeight()
					);
				grx.scale(
					(element.getWidth() + (leftPen.getLineWidth().floatValue() + rightPen.getLineWidth().floatValue()) / 2) 
						/ element.getWidth(), 
					1
					);
				grx.drawLine(
					0, 
					0,
					element.getWidth(),
					0
					);
			}

			grx.setTransform(oldTx);
		}
	}

	
	/**
	 *
	 */
	protected void drawRightPen(
		Graphics2D grx, 
		JRPen topPen, 
		JRPen bottomPen, 
		JRPen rightPen, 
		JRPrintElement element, 
		int offsetX, 
		int offsetY
		)
	{
		Stroke rightStroke = getStroke(rightPen, BasicStroke.CAP_BUTT);

		if (rightStroke != null)
		{
			grx.setStroke(rightStroke);
			grx.setColor(rightPen.getLineColor());
	
			AffineTransform oldTx = grx.getTransform();

			if (rightPen.getLineStyle().byteValue() == JRPen.LINE_STYLE_DOUBLE)
			{
				grx.translate(
					element.getX() + offsetX + element.getWidth() + rightPen.getLineWidth().floatValue() / 3, 
					element.getY() + offsetY - topPen.getLineWidth().floatValue() / 2
					);
				grx.scale(
					1,
					(element.getHeight() + (topPen.getLineWidth().floatValue() + bottomPen.getLineWidth().floatValue()) / 2) 
						/ element.getHeight() 
					);
				grx.drawLine(
					0,
					0,
					0,
					element.getHeight()
					);

				grx.setTransform(oldTx);

				grx.translate(
					element.getX() + offsetX + element.getWidth() - rightPen.getLineWidth().floatValue() / 3, 
					element.getY() + offsetY + topPen.getLineWidth().floatValue() / 6
					);
				grx.scale(
					1,
					(element.getHeight() - (topPen.getLineWidth().floatValue() + bottomPen.getLineWidth().floatValue()) / 6) 
						/ element.getHeight() 
					);
				grx.drawLine(
					0,
					0,
					0,
					element.getHeight()
					);
			}
			else
			{
				grx.translate(
					element.getX() + offsetX + element.getWidth(), 
					element.getY() + offsetY - topPen.getLineWidth().floatValue() / 2
					);
				grx.scale(
					1,
					(element.getHeight() + (topPen.getLineWidth().floatValue() + bottomPen.getLineWidth().floatValue()) / 2) 
						/ element.getHeight() 
					);
				grx.drawLine(
					0,
					0,
					0,
					element.getHeight()
					);
			}

			grx.setTransform(oldTx);
		}
	}

	
	/**
	 * 
	 */
	protected static Stroke getStroke(JRPen pen, int lineCap)
	{
		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth > 0f)
		{
			byte lineStyle = pen.getLineStyle().byteValue();
			
			switch (lineStyle)
			{
				case JRPen.LINE_STYLE_DOUBLE :
				{
					return 
						new BasicStroke(
							lineWidth / 3,
							lineCap,
							BasicStroke.JOIN_MITER
							);
				}
				case JRPen.LINE_STYLE_DOTTED :
				{
					switch (lineCap)
					{
						case BasicStroke.CAP_SQUARE :
						{
							return
								new BasicStroke(
									lineWidth,
									lineCap,
									BasicStroke.JOIN_MITER,
									10f,
									new float[]{0, 2 * lineWidth},
									0f
									);
						}
						case BasicStroke.CAP_BUTT :
						{
							return
								new BasicStroke(
									lineWidth,
									lineCap,
									BasicStroke.JOIN_MITER,
									10f,
									new float[]{lineWidth, lineWidth},
									0f
									);
						}
					}
				}
				case JRPen.LINE_STYLE_DASHED :
				{
					switch (lineCap)
					{
						case BasicStroke.CAP_SQUARE :
						{
							return
								new BasicStroke(
									lineWidth,
									lineCap,
									BasicStroke.JOIN_MITER,
									10f,
									new float[]{4 * lineWidth, 4 * lineWidth},
									0f
									);
						}
						case BasicStroke.CAP_BUTT :
						{
							return
								new BasicStroke(
									lineWidth,
									lineCap,
									BasicStroke.JOIN_MITER,
									10f,
									new float[]{5 * lineWidth, 3 * lineWidth},
									0f
									);
						}
					}
				}
				case JRPen.LINE_STYLE_SOLID :
				default :
				{
					return 
						new BasicStroke(
							lineWidth,
							lineCap,
							BasicStroke.JOIN_MITER
							);
				}
			}
		}
		
		return null;
	}

	
}

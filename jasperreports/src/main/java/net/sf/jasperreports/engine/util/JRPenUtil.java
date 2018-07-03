/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.util;

import java.awt.BasicStroke;
import java.awt.Stroke;

import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.PenEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class JRPenUtil
{

	/**
	 * 
	 */
	public static void setLinePenFromPen(PenEnum pen, JRPen linePen)
	{
		if (pen != null)
		{
			switch (pen)
			{
				case THIN :
				{
					linePen.setLineWidth(0.5f);
					linePen.setLineStyle(LineStyleEnum.SOLID);
					break;
				}
				case ONE_POINT :
				{
					linePen.setLineWidth(1f);
					linePen.setLineStyle(LineStyleEnum.SOLID);
					break;
				}
				case TWO_POINT :
				{
					linePen.setLineWidth(2f);
					linePen.setLineStyle(LineStyleEnum.SOLID);
					break;
				}
				case FOUR_POINT :
				{
					linePen.setLineWidth(4f);
					linePen.setLineStyle(LineStyleEnum.SOLID);
					break;
				}
				case DOTTED :
				{
					linePen.setLineWidth(1f);
					linePen.setLineStyle(LineStyleEnum.DASHED);
					break;
				}
				case NONE :
				{
					linePen.setLineWidth(0f);
					linePen.setLineStyle(LineStyleEnum.SOLID);
					break;
				}
			}
		}
	}

	/**
	 *
	 */
	public static Stroke getStroke(JRPen pen, int lineCap)
	{
		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth > 0f)
		{
			LineStyleEnum lineStyle = pen.getLineStyleValue();
			
			switch (lineStyle)
			{
				case DOUBLE :
				{
					return 
						new BasicStroke(
							lineWidth / 3,
							lineCap,
							BasicStroke.JOIN_MITER
							);
				}
				case DOTTED :
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
				case DASHED :
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
				case SOLID :
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
	
	
	private JRPenUtil()
	{
	}
}

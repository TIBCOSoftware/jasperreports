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
package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRPen;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRStyleResolver.java 1577 2007-02-09 11:25:48Z teodord $
 */
public class JRPenUtil
{

	/**
	 * 
	 */
	public static void setLinePenFromPen(byte pen, JRPen linePen)
	{
		setLinePenFromPen(new Byte(pen), linePen);
	}
	
	/**
	 * 
	 */
	public static void setLinePenFromPen(Byte pen, JRPen linePen)
	{
		if (pen != null)
		{
			switch (pen.byteValue())
			{
				case JRGraphicElement.PEN_THIN :
				{
					linePen.setLineWidth(0.5f);
					linePen.setLineStyle(JRPen.LINE_STYLE_SOLID);
					break;
				}
				case JRGraphicElement.PEN_1_POINT :
				{
					linePen.setLineWidth(1f);
					linePen.setLineStyle(JRPen.LINE_STYLE_SOLID);
					break;
				}
				case JRGraphicElement.PEN_2_POINT :
				{
					linePen.setLineWidth(2f);
					linePen.setLineStyle(JRPen.LINE_STYLE_SOLID);
					break;
				}
				case JRGraphicElement.PEN_4_POINT :
				{
					linePen.setLineWidth(4f);
					linePen.setLineStyle(JRPen.LINE_STYLE_SOLID);
					break;
				}
				case JRGraphicElement.PEN_DOTTED :
				{
					linePen.setLineWidth(1f);
					linePen.setLineStyle(JRPen.LINE_STYLE_DASHED);
					break;
				}
				case JRGraphicElement.PEN_NONE :
				{
					linePen.setLineWidth(0f);
					linePen.setLineStyle(JRPen.LINE_STYLE_SOLID);
					break;
				}
			}
		}
	}

	/**
	 * 
	 */
	public static byte getPenFromLinePen(JRPen linePen)
	{
		float lineWidth = linePen.getLineWidth().floatValue();
		if (lineWidth <= 0f)
		{
			return JRGraphicElement.PEN_NONE;
		}
		else if (0f < lineWidth && lineWidth < 1f)
		{
			return JRGraphicElement.PEN_THIN;
		}
		else if (1f <= lineWidth && lineWidth < 2f)
		{
			if (linePen.getLineStyle().byteValue() == JRPen.LINE_STYLE_DASHED)
			{
				return JRGraphicElement.PEN_DOTTED;
			}
			else
			{
				return JRGraphicElement.PEN_1_POINT;
			}
		}
		else if (2f <= lineWidth && lineWidth < 4f)
		{
			return JRGraphicElement.PEN_2_POINT;
		}

		return JRGraphicElement.PEN_4_POINT;
	}

	/**
	 * 
	 */
	public static Byte getOwnPenFromLinePen(JRPen linePen)
	{
		if (linePen.getOwnLineWidth() == null && linePen.getOwnLineStyle() == null)
		{
			return null;
		}
		
		return new Byte(getPenFromLinePen(linePen));
	}

}

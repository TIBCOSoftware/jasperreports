/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPenFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRCommonGraphicElement graphicElement = (JRCommonGraphicElement) digester.peek();
		setPenAttributes(atts, graphicElement.getLinePen());
		return graphicElement;
	}


	protected static void setPenAttributes(Attributes atts, JRPen pen)
	{
		String lineWidth = atts.getValue(JRXmlConstants.ATTRIBUTE_lineWidth);
		if (lineWidth != null && lineWidth.length() > 0)
		{
			pen.setLineWidth(Float.parseFloat(lineWidth));
		}

		LineStyleEnum lineStyle = LineStyleEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_lineStyle));
		if (lineStyle != null)
		{
			pen.setLineStyle(lineStyle);
		}

		String lineColor = atts.getValue(JRXmlConstants.ATTRIBUTE_lineColor);
		if (lineColor != null && lineColor.length() > 0)
		{
			pen.setLineColor(JRColorUtil.getColor(lineColor, null));
		}
	}
	

	/**
	 * 
	 */
	public static class Style extends JRPenFactory
	{
		public Object createObject(Attributes atts)
		{
			JRStyle style = (JRStyle) digester.peek();
			setPenAttributes(atts, style.getLinePen());
			return style;
		}
	}
	
	/**
	 * 
	 */
	public static class Box extends JRPenFactory
	{
		public Object createObject(Attributes atts)
		{
			JRLineBox box = (JRLineBox) digester.peek();
			setPenAttributes(atts, box.getPen());
			return box;
		}
	}
	
	/**
	 * 
	 */
	public static class Top extends JRPenFactory
	{
		public Object createObject(Attributes atts)
		{
			JRLineBox box = (JRLineBox) digester.peek();
			setPenAttributes(atts, box.getTopPen());
			return box;
		}
	}
	
	/**
	 * 
	 */
	public static class Left extends JRPenFactory
	{
		public Object createObject(Attributes atts)
		{
			JRLineBox box = (JRLineBox) digester.peek();
			setPenAttributes(atts, box.getLeftPen());
			return box;
		}
	}
	
	/**
	 * 
	 */
	public static class Bottom extends JRPenFactory
	{
		public Object createObject(Attributes atts)
		{
			JRLineBox box = (JRLineBox) digester.peek();
			setPenAttributes(atts, box.getBottomPen());
			return box;
		}
	}
	
	/**
	 * 
	 */
	public static class Right extends JRPenFactory
	{
		public Object createObject(Attributes atts)
		{
			JRLineBox box = (JRLineBox) digester.peek();
			setPenAttributes(atts, box.getRightPen());
			return box;
		}
	}
}

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
package net.sf.jasperreports.engine.xml;

import java.util.Map;

import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintElement;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintElementFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRPrintXmlLoader printXmlLoader = (JRPrintXmlLoader)digester.peek(digester.getCount() - 1);
		JasperPrint jasperPrint = (JasperPrint)digester.peek(digester.getCount() - 2);
		JRBasePrintElement element = (JRBasePrintElement)digester.peek();

		String key = atts.getValue(JRXmlConstants.ATTRIBUTE_key);
		if (key != null)
		{
			element.setKey(key);
		}
		
		Byte mode = (Byte)JRXmlConstants.getModeMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_mode));
		if (mode != null)
		{
			element.setMode(mode);
		}
		
		String x = atts.getValue(JRXmlConstants.ATTRIBUTE_x);
		if (x != null && x.length() > 0)
		{
			element.setX(Integer.parseInt(x));
		}

		String y = atts.getValue(JRXmlConstants.ATTRIBUTE_y);
		if (y != null && y.length() > 0)
		{
			element.setY(Integer.parseInt(y));
		}

		String width = atts.getValue(JRXmlConstants.ATTRIBUTE_width);
		if (width != null && width.length() > 0)
		{
			element.setWidth(Integer.parseInt(width));
		}

		String height = atts.getValue(JRXmlConstants.ATTRIBUTE_height);
		if (height != null && height.length() > 0)
		{
			element.setHeight(Integer.parseInt(height));
		}

		String forecolor = atts.getValue(JRXmlConstants.ATTRIBUTE_forecolor);
		if (forecolor != null && forecolor.length() > 0)
		{
			element.setForecolor(JRXmlConstants.getColor(forecolor, null));
		}

		String backcolor = atts.getValue(JRXmlConstants.ATTRIBUTE_backcolor);
		if (backcolor != null && backcolor.length() > 0)
		{
			element.setBackcolor(JRXmlConstants.getColor(backcolor, null));
		}

		if (atts.getValue(JRXmlConstants.ATTRIBUTE_style) != null)
		{
			Map stylesMap = jasperPrint.getStylesMap();

			if ( !stylesMap.containsKey(atts.getValue(JRXmlConstants.ATTRIBUTE_style)) )
			{
				printXmlLoader.addError(new Exception("Unknown report style : " + atts.getValue(JRXmlConstants.ATTRIBUTE_style)));
			}

			element.setStyle((JRStyle) stylesMap.get(atts.getValue(JRXmlConstants.ATTRIBUTE_style)));
		}

		String origin = atts.getValue(JRXmlConstants.ATTRIBUTE_origin); 
		if (origin != null && origin.length() > 0)
		{
			element.setOrigin((JROrigin)jasperPrint.getOriginsList().get(Integer.parseInt(origin)));
		}

		return element;
	}
	

}

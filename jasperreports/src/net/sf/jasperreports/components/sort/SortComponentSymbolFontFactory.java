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
package net.sf.jasperreports.components.sort;

import java.util.Map;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignFont;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRFontFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class SortComponentSymbolFontFactory extends JRFontFactory
{

	public JRFont getFont()
	{
		int i = 0;
		JRComponentElement component = null;
		while (component == null && i < digester.getCount())
		{
			Object obj = digester.peek(i);
			component = obj instanceof JRComponentElement ? (JRComponentElement)obj : null;
			i++;
		}
		
		return new JRDesignFont(component);
	}
	
	public void setStyle(JRFont font, Attributes atts)
	{
		JRDesignFont designFont = (JRDesignFont)font;
			String styleName = atts.getValue(JRXmlConstants.ATTRIBUTE_reportFont);
			
			if (styleName != null)
			{
				JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);
				Map<String,JRStyle> stylesMap = jasperDesign.getStylesMap();

				if (stylesMap.containsKey(styleName))
				{
					JRStyle style = stylesMap.get(styleName);
					designFont.setStyle(style);
				}
				else
				{
					designFont.setStyleNameReference(styleName);
				}
			}
	}
	
}

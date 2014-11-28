/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import java.util.Map;

import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.DesignStyleContainer;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class StyleContainerRule extends Rule
{

	public static final String DEFAULT_STYLE_ATTRIBUTE = "style";
	
	private final String styleAttribute;
	
	public StyleContainerRule()
	{
		this(DEFAULT_STYLE_ATTRIBUTE);
	}
	
	public StyleContainerRule(String styleAttribute)
	{
		this.styleAttribute = styleAttribute;
	}

	@Override
	public void begin(String namespace, String name, Attributes attributes)
			throws Exception
	{
		DesignStyleContainer styleContainer = (DesignStyleContainer) digester.peek();
		
		JasperDesign jasperDesign = (JasperDesign) digester.peek(digester.getCount() - 2);
		styleContainer.setDefaultStyleProvider(jasperDesign);
		
		String styleName = attributes.getValue(styleAttribute);
		if (styleName != null)
		{
			Map<String,JRStyle> stylesMap = jasperDesign.getStylesMap();
			if (stylesMap.containsKey(styleName))
			{
				JRStyle style = stylesMap.get(styleName);
				styleContainer.setStyle(style);
			}
			else
			{
				styleContainer.setStyleNameReference(styleName);
			}
		}
	}

}

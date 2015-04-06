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

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JRDesignStyle;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRPrintStyleFactory extends JRAbstractStyleFactory
{

	protected void setParentStyle(JRDesignStyle currentStyle, String parentStyleName)
	{
		JRPrintXmlLoader printXmlLoader = (JRPrintXmlLoader) digester.peek(digester.getCount() - 1);
		JasperPrint jasperPrint = (JasperPrint) digester.peek(digester.getCount() - 2);
		Map<String,JRStyle> stylesMap = jasperPrint.getStylesMap();

		if (!stylesMap.containsKey(parentStyleName))
		{
			printXmlLoader.addError(
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_UNKNOWN_REPORT_STYLE,
					new Object[]{parentStyleName}
				)
			);
		}
		
		JRStyle parent = stylesMap.get(parentStyleName);
		currentStyle.setParentStyle(parent);
	}

}

/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2025 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.engine.xml.print;

import java.util.function.Consumer;

import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintRectangle;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class RectangleLoader
{
	
	private static final RectangleLoader INSTANCE = new RectangleLoader();
	
	public static RectangleLoader instance()
	{
		return INSTANCE;
	}

	public void loadRectangle(XmlLoader xmlLoader, JasperPrint jasperPrint, Consumer<? super JRPrintRectangle> consumer)
	{
		JRBasePrintRectangle rectangle = new JRBasePrintRectangle(jasperPrint.getDefaultStyleProvider());
		xmlLoader.setIntAttribute(JRXmlConstants.ATTRIBUTE_radius, rectangle::setRadius);
		
		xmlLoader.loadElements(element -> 
		{
			switch (element)
			{
			case JRXmlConstants.ELEMENT_reportElement:
				ReportElementLoader.instance().loadReportElement(xmlLoader, jasperPrint, rectangle);
				break;
			case JRXmlConstants.ELEMENT_graphicElement:
				ReportElementLoader.instance().loadGraphicElement(xmlLoader, rectangle);
				break;
			default:
				xmlLoader.unexpectedElement(element);
				break;
			}
		});
		
		consumer.accept(rectangle);
	}
	
}

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
package net.sf.jasperreports.components.html;

import java.io.IOException;

import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentXmlWriter;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class HtmlComponentXmlWriter implements ComponentXmlWriter {

	public void writeToXml(ComponentKey componentKey, Component component,
			JRXmlWriter reportWriter) throws IOException {
		if (component instanceof HtmlComponent) {
			HtmlComponent htmlComponent = (HtmlComponent) component;
			writeHtmlComponent(htmlComponent, componentKey, reportWriter);
		}
	}
	
	protected void writeHtmlComponent(HtmlComponent htmlComponent, ComponentKey componentKey,
			JRXmlWriter reportWriter) throws IOException {
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		
		XmlNamespace namespace = new XmlNamespace(
				HtmlComponentExtensionsRegistryFactory.NAMESPACE, 
				componentKey.getNamespacePrefix(),
				HtmlComponentExtensionsRegistryFactory.XSD_LOCATION);
		
		writer.startElement("html", namespace);
		
		writer.addAttribute(HtmlComponent.PROPERTY_SCALE_TYPE, htmlComponent.getScaleType());
		writer.addAttribute(HtmlComponent.PROPERTY_HORIZONTAL_ALIGN, htmlComponent.getHorizontalAlign());
		writer.addAttribute(HtmlComponent.PROPERTY_VERTICAL_ALIGN, htmlComponent.getVerticalAlign());
		writer.writeExpression(HtmlComponent.PROPERTY_HTMLCONTENT_EXPRESSION, htmlComponent.getHtmlContentExpression(), false);
		
		if (htmlComponent.getEvaluationTime() != EvaluationTimeEnum.NOW) {
			writer.addAttribute(HtmlComponent.PROPERTY_EVALUATION_TIME, htmlComponent.getEvaluationTime());
		}
		writer.addAttribute(HtmlComponent.PROPERTY_EVALUATION_GROUP, htmlComponent.getEvaluationGroup());
		writer.addAttribute(HtmlComponent.PROPERTY_CLIP_ON_OVERFLOW, htmlComponent.getClipOnOverflow());

		writer.closeElement();
	}
}

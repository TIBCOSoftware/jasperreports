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
package net.sf.jasperreports.components.html;

import java.io.IOException;

import net.sf.jasperreports.components.AbstractComponentXmlWriter;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class HtmlComponentXmlWriter extends AbstractComponentXmlWriter 
{
	/**
	 * @deprecated Replaced by {@link HtmlComponentXmlWriter#HtmlComponentXmlWriter(JasperReportsContext)}.
	 */
	public HtmlComponentXmlWriter()
	{
		super(DefaultJasperReportsContext.getInstance());
	}
	
	public HtmlComponentXmlWriter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}
	
	public boolean isToWrite(JRComponentElement componentElement, JRXmlWriter reportWriter) 
	{
		return true;
	}
	
	public void writeToXml(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException 
	{
		Component component = componentElement.getComponent();
		if (component instanceof HtmlComponent) 
		{
			writeHtmlComponent(componentElement, reportWriter);
		}
	}
	
	protected void writeHtmlComponent(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException 
	{
		Component component = componentElement.getComponent();
		HtmlComponent htmlComponent = (HtmlComponent) component;
		ComponentKey componentKey = componentElement.getComponentKey();

		XmlNamespace namespace = new XmlNamespace(
				HtmlComponentExtensionsRegistryFactory.NAMESPACE, 
				componentKey.getNamespacePrefix(),
				HtmlComponentExtensionsRegistryFactory.XSD_LOCATION);
		
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();

		writer.startElement("html", namespace);
		
		writer.addAttribute(HtmlComponent.PROPERTY_SCALE_TYPE, htmlComponent.getScaleType());
		writer.addAttribute(HtmlComponent.PROPERTY_HORIZONTAL_ALIGN, htmlComponent.getHorizontalImageAlign());
		writer.addAttribute(HtmlComponent.PROPERTY_VERTICAL_ALIGN, htmlComponent.getVerticalImageAlign());
		writeExpression(HtmlComponent.PROPERTY_HTMLCONTENT_EXPRESSION, htmlComponent.getHtmlContentExpression(), false, componentElement, reportWriter);
		
		if (htmlComponent.getEvaluationTime() != EvaluationTimeEnum.NOW) {
			writer.addAttribute(HtmlComponent.PROPERTY_EVALUATION_TIME, htmlComponent.getEvaluationTime());
		}
		writer.addAttribute(HtmlComponent.PROPERTY_EVALUATION_GROUP, htmlComponent.getEvaluationGroup());
		writer.addAttribute(HtmlComponent.PROPERTY_CLIP_ON_OVERFLOW, htmlComponent.getClipOnOverflow());

		writer.closeElement();
	}
}

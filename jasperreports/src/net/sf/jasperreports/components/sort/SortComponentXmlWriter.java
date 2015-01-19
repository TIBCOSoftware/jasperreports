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
package net.sf.jasperreports.components.sort;

import java.io.IOException;

import net.sf.jasperreports.components.AbstractComponentXmlWriter;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.VersionComparator;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class SortComponentXmlWriter extends AbstractComponentXmlWriter 
{
	// used for versioning backward compatibility only
	public static final String PROPERTY_HANDLER_FONT_SIZE = "handlerFontSize";
	
	/**
	 * @deprecated Replaced by {@link #SortComponentXmlWriter(JasperReportsContext)}.
	 */
	public SortComponentXmlWriter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}

	/**
	 * 
	 */
	public SortComponentXmlWriter(JasperReportsContext jasperReportsContext)
	{
		this(jasperReportsContext, null, new VersionComparator());
	}
	
	
	/**
	 * @deprecated Replaced by {@link #SortComponentXmlWriter(JasperReportsContext)}.
	 */
	public SortComponentXmlWriter(JasperReportsContext jasperReportsContext, String version, VersionComparator versionComparator)
	{
		super(jasperReportsContext);
	}


	public boolean isToWrite(JRComponentElement componentElement, JRXmlWriter reportWriter) 
	{
		return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_1_1);
	}
	
	
	public void writeToXml(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException 
	{
		Component component = componentElement.getComponent();
		if (component instanceof SortComponent) 
		{
			writeSortComponent(componentElement, reportWriter);
		}
	}
	
	protected void writeSortComponent(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException 
	{
		Component component = componentElement.getComponent();
		
		SortComponent sortComponent = (SortComponent) component;
		ComponentKey componentKey = componentElement.getComponentKey();

		String namespaceURI = componentKey.getNamespace();
		String schemaLocation = 
			ComponentsEnvironment.getInstance(jasperReportsContext)
				.getBundle(namespaceURI).getXmlParser().getPublicSchemaLocation();
		XmlNamespace componentNamespace = new XmlNamespace(namespaceURI, componentKey.getNamespacePrefix(),
				schemaLocation);
		
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();

		writer.startElement("sort", componentNamespace);
		
		if(isOlderVersionThan(componentElement, reportWriter, JRConstants.VERSION_4_1_3))
		{
			writer.addAttribute(SortComponent.PROPERTY_COLUMN_NAME, sortComponent.getSortFieldName());
			writer.addAttribute(SortComponent.PROPERTY_COLUMN_TYPE, sortComponent.getSortFieldType());
			writer.addAttribute(SortComponent.PROPERTY_HANDLER_COLOR, sortComponent.getHandlerColor());
			writer.addAttribute(PROPERTY_HANDLER_FONT_SIZE, sortComponent.getSymbolFont().getFontsize());
			writer.addAttribute(SortComponent.PROPERTY_HANDLER_HORIZONTAL_ALIGN, sortComponent.getHandlerHorizontalImageAlign());
			writer.addAttribute(SortComponent.PROPERTY_HANDLER_VERTICAL_ALIGN, sortComponent.getHandlerVerticalImageAlign());
		}
		if (sortComponent.getEvaluationTime() != EvaluationTimeEnum.NOW) {
			writer.addAttribute(SortComponent.PROPERTY_EVALUATION_TIME, sortComponent.getEvaluationTime());
		}
		writer.addAttribute(SortComponent.PROPERTY_EVALUATION_GROUP, sortComponent.getEvaluationGroup());
		
		if(isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_1_3))
		{
			// write symbol settings
			writer.startElement("symbol");
			if (sortComponent.getHandlerColor() != null)
			{
				writer.addAttribute(SortComponent.PROPERTY_HANDLER_COLOR, sortComponent.getHandlerColor());
			}
			writer.addAttribute(SortComponent.PROPERTY_COLUMN_TYPE, sortComponent.getSortFieldType());
			writer.addAttribute(SortComponent.PROPERTY_COLUMN_NAME, sortComponent.getSortFieldName());
			writer.addAttribute(SortComponent.PROPERTY_HANDLER_HORIZONTAL_ALIGN, sortComponent.getHandlerHorizontalImageAlign());
			writer.addAttribute(SortComponent.PROPERTY_HANDLER_VERTICAL_ALIGN, sortComponent.getHandlerVerticalImageAlign());
			reportWriter.writeFont(sortComponent.getSymbolFont());
			writer.closeElement();
		}

		writer.closeElement();
	}
}

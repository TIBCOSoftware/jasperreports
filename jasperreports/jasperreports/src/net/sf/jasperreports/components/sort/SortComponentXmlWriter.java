/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentXmlWriter;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.VersionComparator;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class SortComponentXmlWriter implements ComponentXmlWriter 
{
	// used for versioning backward compatibility only
	public static final String PROPERTY_HANDLER_FONT_SIZE = "handlerFontSize";
	
	private JasperReportsContext jasperReportsContext;
	private final String version;
	private final VersionComparator versionComparator;
	
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
	
	
	public SortComponentXmlWriter(JasperReportsContext jasperReportsContext, String version, VersionComparator versionComparator)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.version = version;
		this.versionComparator = versionComparator;
	}


	public boolean isToWrite(JRComponentElement componentElement, JRXmlWriter reportWriter) 
	{
		return isNewerVersionOrEqual(version, JRConstants.VERSION_4_1_1);
	}
	
	
	public void writeToXml(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException 
	{
		Component component = componentElement.getComponent();
		if (component instanceof SortComponent) {
			SortComponent sortComponent = (SortComponent) component;
			ComponentKey componentKey = componentElement.getComponentKey();
			writeSortComponent(sortComponent, componentKey, reportWriter);
		}
	}
	
	protected void writeSortComponent(SortComponent sortComponent, ComponentKey componentKey,
			JRXmlWriter reportWriter) throws IOException {
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		
		String namespaceURI = componentKey.getNamespace();
		String schemaLocation = 
			ComponentsEnvironment.getInstance(jasperReportsContext).getBundle(namespaceURI).getXmlParser().getPublicSchemaLocation();
		XmlNamespace componentNamespace = new XmlNamespace(namespaceURI, componentKey.getNamespacePrefix(),
				schemaLocation);
		
		writer.startElement("sort", componentNamespace);
		
		if(isOlderVersionThan(version, JRConstants.VERSION_4_1_3))
		{
			writer.addAttribute(SortComponent.PROPERTY_COLUMN_NAME, sortComponent.getSortFieldName());
			writer.addAttribute(SortComponent.PROPERTY_COLUMN_TYPE, sortComponent.getSortFieldType());
			writer.addAttribute(SortComponent.PROPERTY_HANDLER_COLOR, sortComponent.getHandlerColor());
			writer.addAttribute(PROPERTY_HANDLER_FONT_SIZE, sortComponent.getSymbolFont().getFontSize());
			writer.addAttribute(SortComponent.PROPERTY_HANDLER_HORIZONTAL_ALIGN, sortComponent.getHandlerHorizontalAlign());
			writer.addAttribute(SortComponent.PROPERTY_HANDLER_VERTICAL_ALIGN, sortComponent.getHandlerVerticalAlign());
		}
		if (sortComponent.getEvaluationTime() != EvaluationTimeEnum.NOW) {
			writer.addAttribute(SortComponent.PROPERTY_EVALUATION_TIME, sortComponent.getEvaluationTime());
		}
		writer.addAttribute(SortComponent.PROPERTY_EVALUATION_GROUP, sortComponent.getEvaluationGroup());
		
		if(isNewerVersionOrEqual(version, JRConstants.VERSION_4_1_3))
		{
			// write symbol settings
			writer.startElement("symbol");
			if (sortComponent.getHandlerColor() != null)
			{
				writer.addAttribute(SortComponent.PROPERTY_HANDLER_COLOR, sortComponent.getHandlerColor());
			}
			writer.addAttribute(SortComponent.PROPERTY_COLUMN_TYPE, sortComponent.getSortFieldType());
			writer.addAttribute(SortComponent.PROPERTY_COLUMN_NAME, sortComponent.getSortFieldName());
			writer.addAttribute(SortComponent.PROPERTY_HANDLER_HORIZONTAL_ALIGN, sortComponent.getHandlerHorizontalAlign());
			writer.addAttribute(SortComponent.PROPERTY_HANDLER_VERTICAL_ALIGN, sortComponent.getHandlerVerticalAlign());
			reportWriter.writeFont(sortComponent.getSymbolFont());
			writer.closeElement();
		}

		writer.closeElement();
	}
	
	protected boolean isNewerVersionOrEqual(String currentVersion, String oldVersion) 
	{
		return versionComparator.compare(currentVersion, oldVersion) >= 0;
	}
	
	protected boolean isOlderVersionThan(String currentVersion, String version) 
	{
		return versionComparator.compare(currentVersion, version) < 0;
	}

	@SuppressWarnings("deprecation")
	protected void writeExpression(String name, XmlNamespace namespace, JRExpression expression, boolean writeClass, JRXmlWriteHelper writer)  throws IOException
	{
		if(isNewerVersionOrEqual(version, JRConstants.VERSION_4_1_1))
		{
			writer.writeExpression(name, namespace, expression);
		}
		else
		{
			writer.writeExpression(name, namespace, expression, writeClass);
		}
	}
	
}

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
package net.sf.jasperreports.components;

import java.io.IOException;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentXmlWriter;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.VersionComparator;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlBaseWriter;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @see ComponentsExtensionsRegistryFactory
 */
public abstract class AbstractComponentXmlWriter implements ComponentXmlWriter
{
	/**
	 * 
	 */
	public static final String PROPERTY_COMPONENTS_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "components.";

	/**
	 * 
	 */
	public static final String PROPERTY_COMPONENTS_VERSION_SUFFIX = ".version";

	protected final JasperReportsContext jasperReportsContext;
	protected final VersionComparator versionComparator;
	
	/**
	 * 
	 */
	public AbstractComponentXmlWriter(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.versionComparator = new VersionComparator();
	}
	
	/**
	 * 
	 */
	public static String getVersion(JasperReportsContext jasperReportsContext, JRComponentElement componentElement, JRXmlWriter reportWriter) 
	{
		String version = null;

		ComponentKey componentKey = componentElement.getComponentKey();
		String versionProperty = PROPERTY_COMPONENTS_PREFIX + componentKey.getName() + PROPERTY_COMPONENTS_VERSION_SUFFIX;
		
		if (componentElement.getPropertiesMap().containsProperty(versionProperty))
		{
			version = componentElement.getPropertiesMap().getProperty(versionProperty);
		}
		else
		{
			JRReport report = reportWriter.getReport();
			version = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(report, versionProperty);
			
			if (version == null)
			{
				version = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(report, JRXmlBaseWriter.PROPERTY_REPORT_VERSION);
			}
		}
		
		return version;
	}
	
	/**
	 * 
	 */
	protected boolean isNewerVersionOrEqual(JRComponentElement componentElement, JRXmlWriter reportWriter, String oldVersion) //FIXMEVERSION can we pass something else then reportWriter?
	{
		return versionComparator.compare(getVersion(jasperReportsContext, componentElement, reportWriter), oldVersion) >= 0;
	}

	/*
	 *
	 */
	protected boolean isOlderVersionThan(JRComponentElement componentElement, JRXmlWriter reportWriter, String version) 
	{
		return versionComparator.compare(getVersion(jasperReportsContext, componentElement, reportWriter), version) < 0;
	}

	@SuppressWarnings("deprecation")
	protected void writeExpression(String name, JRExpression expression, boolean writeClass, JRComponentElement componentElement, JRXmlWriter reportWriter)  throws IOException
	{
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		if(isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_1_1))
		{
			writer.writeExpression(name, expression);
		}
		else
		{
			writer.writeExpression(name, expression, writeClass);
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void writeExpression(String name, XmlNamespace namespace, JRExpression expression, boolean writeClass, JRComponentElement componentElement, JRXmlWriter reportWriter)  throws IOException
	{
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		if(isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_1_1))
		{
			writer.writeExpression(name, namespace, expression);
		}
		else
		{
			writer.writeExpression(name, namespace, expression, writeClass);
		}
	}
}

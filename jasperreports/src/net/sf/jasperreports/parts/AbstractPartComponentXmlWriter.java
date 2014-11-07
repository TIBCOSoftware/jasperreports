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
package net.sf.jasperreports.parts;

import net.sf.jasperreports.components.AbstractComponentXmlWriter;
import net.sf.jasperreports.components.ComponentsExtensionsRegistryFactory;
import net.sf.jasperreports.engine.JRPart;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.part.PartComponentXmlWriter;
import net.sf.jasperreports.engine.util.VersionComparator;
import net.sf.jasperreports.engine.xml.JRXmlBaseWriter;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ComponentsXmlWriter.java 6251 2013-06-11 08:43:46Z shertage $
 * @see ComponentsExtensionsRegistryFactory
 */
public abstract class AbstractPartComponentXmlWriter implements PartComponentXmlWriter
{
	/**
	 * 
	 */
	public static final String PROPERTY_PART_COMPONENTS_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "parts.";

	protected final JasperReportsContext jasperReportsContext;
	protected final VersionComparator versionComparator;
	
	/**
	 * 
	 */
	public AbstractPartComponentXmlWriter(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.versionComparator = new VersionComparator();
	}
	
	/**
	 * 
	 */
	public static String getVersion(JasperReportsContext jasperReportsContext, JRPart part, JRXmlWriter reportWriter) 
	{
		String version = null;

		ComponentKey componentKey = part.getComponentKey();
		String versionProperty = PROPERTY_PART_COMPONENTS_PREFIX + componentKey.getName() + AbstractComponentXmlWriter.PROPERTY_COMPONENTS_VERSION_SUFFIX;
		
		if (part.getPropertiesMap().containsProperty(versionProperty))
		{
			version = part.getPropertiesMap().getProperty(versionProperty);
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
	protected boolean isNewerVersionOrEqual(JRPart part, JRXmlWriter reportWriter, String oldVersion) //FIXMEVERSION can we pass something else then reportWriter?
	{
		return versionComparator.compare(getVersion(jasperReportsContext, part, reportWriter), oldVersion) >= 0;
	}

	/*
	 *
	 */
	protected boolean isOlderVersionThan(JRPart part, JRXmlWriter reportWriter, String version) 
	{
		return versionComparator.compare(getVersion(jasperReportsContext, part, reportWriter), version) < 0;
	}
}

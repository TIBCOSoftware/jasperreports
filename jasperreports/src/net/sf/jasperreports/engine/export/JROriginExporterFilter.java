/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

/*
 * Contributors:
 * Greg Hilton 
 */

package net.sf.jasperreports.engine.export;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.fill.JRTemplatePrintElement;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRProperties.PropertySuffix;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JROriginExporterFilter implements ResetableExporterFilter
{

	/**
	 * The prefix of origin exclusion properties.
	 */
	public static final String PROPERTY_EXCLUDE_ORIGIN_PREFIX = "exclude.origin.";
	
	private static final String KEEP_FIRST_PREFIX = "keep.first.";
	private static final String BAND_PREFIX = "band.";
	private static final String GROUP_PREFIX = "group.";
	private static final String REPORT_PREFIX = "report.";
	
	private Map originsToExclude = new HashMap();
	private Map firstOccurrences = new HashMap();
	
	public void addOrigin(JROrigin origin)
	{
		addOrigin(origin, false);
	}
	
	public void addOrigin(JROrigin origin, boolean keepFirst)
	{
		originsToExclude.put(origin, keepFirst ? Boolean.TRUE : Boolean.FALSE);
	}
	
	public void removeOrigin(JROrigin origin)
	{
		originsToExclude.remove(origin);
	}
	
	public void reset()
	{
		firstOccurrences = new HashMap();
	}
	
	public boolean isToExport(JRPrintElement element)
	{
		JROrigin origin = element.getOrigin();

		Boolean keepFirst = (origin == null ? null : (Boolean)originsToExclude.get(origin));
		boolean originMatched = keepFirst != null;

		return
			!originMatched 
			|| (keepFirst.booleanValue() 
				&& (!(element instanceof JRTemplatePrintElement) 
					|| isFirst((JRTemplatePrintElement)element)));
	}
	
	private boolean isFirst(JRTemplatePrintElement element)
	{
		Object template = element.getTemplate();
		Object firstElement = firstOccurrences.get(template);
		if (firstElement == null || firstElement == element)
		{
			firstOccurrences.put(template, element);
			return true;
		}
		return false;
	}
	
	public static JROriginExporterFilter getFilter(JRPropertiesMap propertiesMap, String originFilterPrefix)
	{
		JROriginExporterFilter filter = null;
		
		filter = addOriginsToFilter(filter, propertiesMap, originFilterPrefix, false);
		filter = addOriginsToFilter(filter, propertiesMap, originFilterPrefix + KEEP_FIRST_PREFIX, true);
		
		return filter;
	}
	
	private static JROriginExporterFilter addOriginsToFilter(
		JROriginExporterFilter filter, 
		JRPropertiesMap propertiesMap, 
		String originFilterPrefix,
		boolean keepFirst
		)
	{
		List properties = JRProperties.getProperties(originFilterPrefix + BAND_PREFIX);
		properties.addAll(JRProperties.getProperties(propertiesMap, originFilterPrefix + BAND_PREFIX));
		
		if (!properties.isEmpty())
		{
			filter = (filter == null ? new JROriginExporterFilter(): filter);
				
			for(Iterator it = properties.iterator(); it.hasNext();)
			{
				PropertySuffix propertySuffix = (PropertySuffix)it.next();
				String suffix = propertySuffix.getSuffix();
				Byte bandType = 
					(Byte)JRXmlConstants.getBandTypeMap().get(
						JRProperties.getProperty(propertiesMap, propertySuffix.getKey())
						);
				if (bandType != null)
				{
					filter.addOrigin(
						new JROrigin(
							JRProperties.getProperty(propertiesMap, originFilterPrefix + REPORT_PREFIX + suffix),
							JRProperties.getProperty(propertiesMap, originFilterPrefix + GROUP_PREFIX + suffix),
							bandType.byteValue()
							),
						keepFirst
						);
				}
			}
		}
		
		return filter;
	}
	
}

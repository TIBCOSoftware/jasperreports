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
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRProperties.PropertySuffix;

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
	
	private Map<JROrigin,Boolean> originsToExclude = new HashMap<JROrigin,Boolean>();
	private Map<Integer,JRPrintElement> firstOccurrences = new HashMap<Integer,JRPrintElement>();
	
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
		firstOccurrences = new HashMap<Integer,JRPrintElement>();
	}
	
	public boolean isToExport(JRPrintElement element)
	{
		JROrigin origin = element.getOrigin();

		Boolean keepFirst = (origin == null ? null : (Boolean)originsToExclude.get(origin));
		boolean originMatched = keepFirst != null;

		return
			!originMatched 
			|| (keepFirst.booleanValue() 
				&& isFirst(element));
	}
	
	private boolean isFirst(JRPrintElement element)
	{
		int elementId = element.getSourceElementId();
		if (elementId == JRPrintElement.UNSET_SOURCE_ELEMENT_ID)
		{
			// for some reason the element doesn't have a valid source Id.
			// do not exclude the element in that case.
			return true;
		}
		
		// FIXME this doesn't work well with batch exporting
		JRPrintElement firstElement = firstOccurrences.get(elementId);
		if (firstElement == null || firstElement == element)
		{
			firstOccurrences.put(elementId, element);
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
		List<PropertySuffix> properties = JRProperties.getProperties(originFilterPrefix + BAND_PREFIX);
		properties.addAll(JRProperties.getProperties(propertiesMap, originFilterPrefix + BAND_PREFIX));
		
		if (!properties.isEmpty())
		{
			filter = (filter == null ? new JROriginExporterFilter(): filter);
				
			for(Iterator<PropertySuffix> it = properties.iterator(); it.hasNext();)
			{
				PropertySuffix propertySuffix = it.next();
				String suffix = propertySuffix.getSuffix();
				BandTypeEnum bandType = 
					BandTypeEnum.getByName(
						JRProperties.getProperty(propertiesMap, propertySuffix.getKey())
						);
				if (bandType != null)
				{
					filter.addOrigin(
						new JROrigin(
							JRProperties.getProperty(propertiesMap, originFilterPrefix + REPORT_PREFIX + suffix),
							JRProperties.getProperty(propertiesMap, originFilterPrefix + GROUP_PREFIX + suffix),
							bandType
							),
						keepFirst
						);
				}
			}
		}
		
		return filter;
	}
	
}

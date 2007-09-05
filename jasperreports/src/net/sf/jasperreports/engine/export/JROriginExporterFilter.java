/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Greg Hilton 
 */

package net.sf.jasperreports.engine.export;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRProperties.PropertySuffix;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ExporterNature.java 1711 2007-04-30 15:43:58Z lucianc $
 */
public class JROriginExporterFilter implements ExporterFilter
{

	private static final String ORIGIN_EXPORTER_FILTER_PREFIX = JRProperties.PROPERTY_PREFIX + "export.exclude.origin.";
	private static final String ORIGIN_EXPORTER_FILTER_BAND_PREFIX = ORIGIN_EXPORTER_FILTER_PREFIX + "band.";
	private static final String ORIGIN_EXPORTER_FILTER_GROUP_PREFIX = ORIGIN_EXPORTER_FILTER_PREFIX + "group.";
	private static final String ORIGIN_EXPORTER_FILTER_REPORT_PREFIX = ORIGIN_EXPORTER_FILTER_PREFIX + "report.";
	
	private Set originsToExclude = new HashSet();
	
	public void addOrigin(JROrigin origin)
	{
		originsToExclude.add(origin);
	}
	
	public void removeOrigin(JROrigin origin)
	{
		originsToExclude.remove(origin);
	}
	
	public boolean isToExport(JRPrintElement element)
	{
		JROrigin origin = element.getOrigin();
		return origin == null || !originsToExclude.contains(origin);
	}
	
	public static JROriginExporterFilter getFilter(JRPropertiesMap propertiesMap)
	{
		JROriginExporterFilter filter = null;
		
		List properties = JRProperties.getProperties(ORIGIN_EXPORTER_FILTER_BAND_PREFIX);
		properties.addAll(JRProperties.getProperties(propertiesMap, ORIGIN_EXPORTER_FILTER_BAND_PREFIX));
		
		if (!properties.isEmpty())
		{
			filter = new JROriginExporterFilter();
			
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
							JRProperties.getProperty(propertiesMap, ORIGIN_EXPORTER_FILTER_REPORT_PREFIX + suffix),
							JRProperties.getProperty(propertiesMap, ORIGIN_EXPORTER_FILTER_GROUP_PREFIX + suffix),
							bandType.byteValue()
							)
						);
				}
			}
		}
		
		return filter;
	}
	
}

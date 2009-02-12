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
package net.sf.jasperreports.chartthemes.simple;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.chartthemes.ChartThemeMapBundle;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class XmlChartThemeExtensionsRegistryFactory implements
		ExtensionsRegistryFactory
{

	public final static String XML_CHART_THEME_PROPERTY_PREFIX = 
		JRProperties.PROPERTY_PREFIX + "xml.chart.theme.";
	
	public ExtensionsRegistry createRegistry(String registryId,
			JRPropertiesMap properties)
	{
		List themeProperties = JRProperties.getProperties(properties, 
				XML_CHART_THEME_PROPERTY_PREFIX);
		Map themes = new HashMap();
		for (Iterator it = themeProperties.iterator(); it.hasNext();)
		{
			JRProperties.PropertySuffix themeProp = (JRProperties.PropertySuffix) it.next();
			String themeName = themeProp.getSuffix();
			String themeLocation = themeProp.getValue();
			XmlChartTheme theme = new XmlChartTheme(themeLocation);
			themes.put(themeName, theme);
		}
		
		ChartThemeMapBundle bundle = new ChartThemeMapBundle();
		bundle.setThemes(themes);
		return new ChartThemeBundlesExtensionsRegistry(bundle);
	}

}

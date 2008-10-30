package net.sf.jasperreports.charts.themes;
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

import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.charts.ChartThemeBundle;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRFillChart.java 2278 2008-08-14 16:14:54Z teodord $
 */
public class SampleExtensionsRegistryFactory implements ExtensionsRegistryFactory
{
	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties) 
	{
		return 
			new ExtensionsRegistry()
			{
				public List getExtensions(Class extensionType) 
				{
					if (ChartThemeBundle.class.equals(extensionType))
					{
						return Collections.singletonList(SampleChartThemeBundle.getInstance());
					}
					return null;
				}
			};
	}
}

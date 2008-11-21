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

import net.sf.jasperreports.charts.ChartTheme;
import net.sf.jasperreports.charts.ChartThemeBundle;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRFillChart.java 2278 2008-08-14 16:14:54Z teodord $
 */
public class SampleChartThemeBundle implements ChartThemeBundle
{
	private String[] names = new String[]{"simple", "eye.candy.sixties", "aegean"};

	private static final SampleChartThemeBundle INSTANCE = new SampleChartThemeBundle();
	
	public static SampleChartThemeBundle getInstance() 
	{
		return INSTANCE;
	}
	
	private SampleChartThemeBundle() 
	{
	}
	
	public String[] getChartThemeNames() 
	{
		return names;
	}
	
	public ChartTheme getChartTheme(String themeName) 
	{
		if ("simple".equals(themeName))
		{
			return new SimpleChartTheme(); 
		}
		else if ("eye.candy.sixties".equals(themeName))
		{
			return new EyeCandySixtiesChartTheme(); 
		}
		else if ("aegean".equals(themeName))
		{
			return new AegeanChartTheme(); 
		}
		return null;
	}
}

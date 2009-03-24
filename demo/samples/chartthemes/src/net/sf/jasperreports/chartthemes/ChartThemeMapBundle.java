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
package net.sf.jasperreports.chartthemes;

import java.util.Map;

import net.sf.jasperreports.charts.ChartTheme;
import net.sf.jasperreports.charts.ChartThemeBundle;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net) 
 * @version $Id$
 */
public class ChartThemeMapBundle implements ChartThemeBundle
{

	private Map themes;
	
	public ChartTheme getChartTheme(String themeName)
	{
		return (ChartTheme) themes.get(themeName);
	}

	public String[] getChartThemeNames()
	{
		return (String[]) themes.keySet().toArray(new String[themes.size()]);
	}

	/**
	 * @return the themes
	 */
	public Map getThemes()
	{
		return themes;
	}

	/**
	 * @param themes the themes to set
	 */
	public void setThemes(Map themes)
	{
		this.themes = themes;
	}

}

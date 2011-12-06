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
package net.sf.jasperreports.governors;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRProperties;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class MaxPagesGovernor extends JRDefaultScriptlet
{

	/**
	 *
	 */
	public static final String PROPERTY_MAX_PAGES_ENABLED = JRProperties.PROPERTY_PREFIX + "governor.max.pages.enabled";
	public static final String PROPERTY_MAX_PAGES = JRProperties.PROPERTY_PREFIX + "governor.max.pages";

	/**
	 *
	 */
	private int pages;
	private int maxPages;

	
	/**
	 *
	 */
	public MaxPagesGovernor(int maxPages)
	{
		this.maxPages = maxPages;
	}


	/**
	 *
	 */
	public void afterPageInit() throws JRScriptletException
	{
		// cannot use PAGE_NUMBER variable because of timing issues and because of isResetPageNumber flag
		pages++;
		if (maxPages < pages)
		{
			throw 
				new MaxPagesGovernorException(
					((JasperReport)getParameterValue(JRParameter.JASPER_REPORT, false)).getName(),
					maxPages
					);
		}
	}


}

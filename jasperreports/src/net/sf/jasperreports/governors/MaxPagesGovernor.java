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
package net.sf.jasperreports.governors;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRProperties;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRDefaultScriptlet.java 1229 2006-04-19 10:27:35Z teodord $
 */
public class MaxPagesGovernor extends JRDefaultScriptlet
{

	/**
	 *
	 */
	public static final String PROPERTY_MAX_PAGES = JRProperties.PROPERTY_PREFIX + "governor.max.pages";

	/**
	 *
	 */
	private int pages = 0;
	private int maxPages = 0;

	
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

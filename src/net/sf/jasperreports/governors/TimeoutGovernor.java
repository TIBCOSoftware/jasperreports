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
public class TimeoutGovernor extends JRDefaultScriptlet
{

	/**
	 *
	 */
	public static final String PROPERTY_TIMEOUT = JRProperties.PROPERTY_PREFIX + "governor.timeout";

	/**
	 *
	 */
	private long startTime = 0;
	private long timeout = 0;

	
	/**
	 *
	 */
	public TimeoutGovernor(long timeout)
	{
		this.timeout = timeout;
	}


	/**
	 *
	 */
	public void beforeReportInit() throws JRScriptletException
	{
		startTime = System.currentTimeMillis();
	}


	/**
	 *
	 */
	public void afterPageInit() throws JRScriptletException
	{
		long ellapsedTime = System.currentTimeMillis() - startTime;
		if (timeout < ellapsedTime)
		{
			throw 
				new TimeoutGovernorException(
					((JasperReport)getParameterValue(JRParameter.JASPER_REPORT)).getName(),
					timeout
					);
		}
	}


}

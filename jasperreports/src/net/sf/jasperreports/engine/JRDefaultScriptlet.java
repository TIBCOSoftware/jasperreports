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
package net.sf.jasperreports.engine;



/**
 * This class provides default empty implementations for scriptlet events. It is a convenience class that users
 * can extend when not all methods need to be implemented.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDefaultScriptlet extends JRAbstractScriptlet
{


	/**
	 *
	 */
	public JRDefaultScriptlet()
	{
	}


	/**
	 *
	 */
	public void beforeReportInit() throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void afterReportInit() throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void beforePageInit() throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void afterPageInit() throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void beforeColumnInit() throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void afterColumnInit() throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void beforeGroupInit(String groupName) throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void afterGroupInit(String groupName) throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void beforeDetailEval() throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void afterDetailEval() throws JRScriptletException
	{
	}


}

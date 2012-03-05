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
package net.sf.jasperreports.engine.fill;

import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillDatasetScriptlet extends JRAbstractScriptlet
{
	
	/**
	 *
	 */
	private JRFillDataset dataset;

	/**
	 *
	 */
	public JRFillDatasetScriptlet(JRFillDataset dataset)
	{
		this.dataset = dataset;
	}


	/**
	 *
	 */
	public void setData(
		Map<String,JRFillParameter> parsm,
		Map<String,JRFillField> fldsm,
		Map<String,JRFillVariable> varsm,
		JRFillGroup[] grps
		)
	{
		super.setData(parsm, fldsm, varsm, grps);
		
		for(Iterator<JRAbstractScriptlet> it = dataset.scriptlets.iterator(); it.hasNext();)
		{
			it.next().setData(parsm, fldsm, varsm, grps);
		}
	}


	/**
	 *
	 */
	public void beforeReportInit() throws JRScriptletException
	{
		for(Iterator<JRAbstractScriptlet> it = dataset.scriptlets.iterator(); it.hasNext();)
		{
			it.next().beforeReportInit();
		}
	}


	/**
	 *
	 */
	public void afterReportInit() throws JRScriptletException
	{
		for(Iterator<JRAbstractScriptlet> it = dataset.scriptlets.iterator(); it.hasNext();)
		{
			it.next().afterReportInit();
		}
	}


	/**
	 *
	 */
	public void beforePageInit() throws JRScriptletException
	{
		for(Iterator<JRAbstractScriptlet> it = dataset.scriptlets.iterator(); it.hasNext();)
		{
			it.next().beforePageInit();
		}
	}


	/**
	 *
	 */
	public void afterPageInit() throws JRScriptletException
	{
		for(Iterator<JRAbstractScriptlet> it = dataset.scriptlets.iterator(); it.hasNext();)
		{
			it.next().afterPageInit();
		}
	}


	/**
	 *
	 */
	public void beforeColumnInit() throws JRScriptletException
	{
		for(Iterator<JRAbstractScriptlet> it = dataset.scriptlets.iterator(); it.hasNext();)
		{
			it.next().beforeColumnInit();
		}
	}


	/**
	 *
	 */
	public void afterColumnInit() throws JRScriptletException
	{
		for(Iterator<JRAbstractScriptlet> it = dataset.scriptlets.iterator(); it.hasNext();)
		{
			it.next().afterColumnInit();
		}
	}


	/**
	 *
	 */
	public void beforeGroupInit(String groupName) throws JRScriptletException
	{
		for(Iterator<JRAbstractScriptlet> it = dataset.scriptlets.iterator(); it.hasNext();)
		{
			it.next().beforeGroupInit(groupName);
		}
	}


	/**
	 *
	 */
	public void afterGroupInit(String groupName) throws JRScriptletException
	{
		for(Iterator<JRAbstractScriptlet> it = dataset.scriptlets.iterator(); it.hasNext();)
		{
			it.next().afterGroupInit(groupName);
		}
	}


	/**
	 *
	 */
	public void beforeDetailEval() throws JRScriptletException
	{
		for(Iterator<JRAbstractScriptlet> it = dataset.scriptlets.iterator(); it.hasNext();)
		{
			it.next().beforeDetailEval();
		}
	}


	/**
	 *
	 */
	public void afterDetailEval() throws JRScriptletException
	{
		for(Iterator<JRAbstractScriptlet> it = dataset.scriptlets.iterator(); it.hasNext();)
		{
			it.next().afterDetailEval();
		}
	}


}

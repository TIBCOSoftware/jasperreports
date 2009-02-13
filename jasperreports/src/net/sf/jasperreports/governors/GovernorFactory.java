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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.scriptlets.ScriptletFactory;
import net.sf.jasperreports.engine.scriptlets.ScriptletFactoryContext;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public class GovernorFactory implements ScriptletFactory
{

	private static final GovernorFactory INSTANCE = new GovernorFactory();
	
	private GovernorFactory()
	{
	}
	
	/**
	 * 
	 */
	public static GovernorFactory getInstance()
	{
		return INSTANCE;
	}

	/**
	 *
	 */
	public List getScriplets(ScriptletFactoryContext context) throws JRException
	{
		List scriptlets = new ArrayList();

		boolean maxPagesEnabled = JRProperties.getBooleanProperty(context.getJasperReport(), MaxPagesGovernor.PROPERTY_MAX_PAGES_ENABLED, true);
		if (maxPagesEnabled)
		{
			int maxPages = JRProperties.getIntegerProperty(context.getJasperReport(), MaxPagesGovernor.PROPERTY_MAX_PAGES, 0);
			if (maxPages > 0)
			{
				scriptlets.add(new MaxPagesGovernor(maxPages));
			}
		}
		
		boolean timeoutEnabled = JRProperties.getBooleanProperty(context.getJasperReport(), TimeoutGovernor.PROPERTY_TIMEOUT_ENABLED, true);
		if (timeoutEnabled)
		{
			long timeout = JRProperties.getLongProperty(context.getJasperReport(), TimeoutGovernor.PROPERTY_TIMEOUT, 0);
			if (timeout > 0)
			{
				scriptlets.add(new TimeoutGovernor(timeout));
			}
		}
		
		return scriptlets;
	}
	
	/**
	 *
	 */
	protected JRAbstractScriptlet getScriptlet(String scriptletClassName) throws JRException
	{
		JRAbstractScriptlet scriptlet = null;

		try
		{
			Class scriptletClass = JRClassLoader.loadClassForName(scriptletClassName);	
			scriptlet = (JRAbstractScriptlet) scriptletClass.newInstance();
		}
		catch (ClassNotFoundException e)
		{
			throw new JRException("Error loading scriptlet class : " + scriptletClassName, e);
		}
		catch (Exception e)
		{
			throw new JRException("Error creating scriptlet class instance : " + scriptletClassName, e);
		}
		
		return scriptlet;
	}
	
}

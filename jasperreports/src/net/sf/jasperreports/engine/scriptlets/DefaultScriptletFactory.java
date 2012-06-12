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
package net.sf.jasperreports.engine.scriptlets;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRScriptlet;
import net.sf.jasperreports.engine.util.JRClassLoader;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class DefaultScriptletFactory implements ScriptletFactory
{

	private static final DefaultScriptletFactory INSTANCE = new DefaultScriptletFactory();
	
	private DefaultScriptletFactory()
	{
	}
	
	/**
	 * 
	 */
	public static DefaultScriptletFactory getInstance()
	{
		return INSTANCE;
	}

	/**
	 *
	 */
	public List<JRAbstractScriptlet> getScriplets(ScriptletFactoryContext context) throws JRException
	{
		List<JRAbstractScriptlet> scriptlets = new ArrayList<JRAbstractScriptlet>();

		JRAbstractScriptlet scriptlet = (JRAbstractScriptlet)context.getParameterValues().get(JRParameter.REPORT_SCRIPTLET);
		if (scriptlet == null)
		{
			String scriptletClassName = context.getDataset().getScriptletClass();
			if (scriptletClassName != null)
			{
				scriptlet = getScriptlet(scriptletClassName);
				context.getParameterValues().put(JRParameter.REPORT_SCRIPTLET, scriptlet);
			}
		}

		if (scriptlet != null)
		{
			scriptlets.add(scriptlet);
		}
		
		JRScriptlet[] scriptletsArray = context.getDataset().getScriptlets();
		if (scriptletsArray != null)
		{
			for (int i = 0; i < scriptletsArray.length; i++)
			{
				String paramName = scriptletsArray[i].getName() 
						+ JRScriptlet.SCRIPTLET_PARAMETER_NAME_SUFFIX;
				scriptlet = (JRAbstractScriptlet)context.getParameterValues().get(paramName);
				if (scriptlet == null)
				{
					scriptlet = getScriptlet(scriptletsArray[i].getValueClassName());
					context.getParameterValues().put(paramName, scriptlet);
				}
				
				scriptlets.add(scriptlet);
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
			Class<?> scriptletClass = JRClassLoader.loadClassForName(scriptletClassName);	
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

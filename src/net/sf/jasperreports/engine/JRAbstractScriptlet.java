/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine;

import java.util.Map;

import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillGroup;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractScriptlet
{


	/**
	 *
	 */
	protected Map parametersMap = null;
	protected Map fieldsMap = null;
	protected Map variablesMap = null;
	protected JRFillGroup[] groups = null;


	/**
	 *
	 */
	public JRAbstractScriptlet()
	{
	}


	/**
	 *
	 */
	public void setData(
		Map parsm,
		Map fldsm,
		Map varsm,
		JRFillGroup[] grps
		)
	{
		parametersMap = parsm;
		fieldsMap = fldsm;
		variablesMap = varsm;
		groups = grps;
	}


	/**
	 *
	 */
	public Object getParameterValue(String parameterName) throws JRScriptletException
	{
		JRFillParameter parameter = (JRFillParameter)this.parametersMap.get(parameterName);
		if (parameter == null)
		{
			throw new JRScriptletException("Parameter not found : " + parameterName);
		}
		return parameter.getValue();
	}


	/**
	 *
	 */
	public Object getFieldValue(String fieldName) throws JRScriptletException
	{
		JRFillField field = (JRFillField)this.fieldsMap.get(fieldName);
		if (field == null)
		{
			throw new JRScriptletException("Field not found : " + fieldName);
		}
		return field.getValue();
	}


	/**
	 *
	 */
	public Object getVariableValue(String variableName) throws JRScriptletException
	{
		JRFillVariable variable = (JRFillVariable)this.variablesMap.get(variableName);
		if (variable == null)
		{
			throw new JRScriptletException("Variable not found : " + variableName);
		}
		return variable.getValue();
	}


	/**
	 *
	 */
	public void setVariableValue(String variableName, Object value) throws JRScriptletException
	{
		JRFillVariable variable = (JRFillVariable)this.variablesMap.get(variableName);
		if (variable == null)
		{
			throw new JRScriptletException("Variable not found : " + variableName);
		}
		
		if (value != null && !variable.getValueClass().isInstance(value) )
		{
			throw new JRScriptletException("Incompatible value assigned to variable " + variableName + ". Expected " + variable.getValueClassName() + ".");
		}
		
		variable.setValue(value);
	}


	/**
	 *
	 */
	public void callBeforeReportInit() throws JRScriptletException
	{
		this.beforeReportInit();
		this.beforePageInit();
		this.beforeColumnInit();

		if(groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				this.beforeGroupInit( groups[i].getName() );
			}
		}
	}


	/**
	 *
	 */
	public void callAfterReportInit() throws JRScriptletException
	{
		if(groups != null && groups.length > 0)
		{
			for(int i = groups.length - 1; i >= 0; i--)
			{
				this.afterGroupInit( groups[i].getName() );
			}
		}

		this.afterColumnInit();
		this.afterPageInit();
		this.afterReportInit();
	}


	/**
	 *
	 */
	public void callBeforePageInit() throws JRScriptletException
	{
		this.beforePageInit();
		this.beforeColumnInit();
	}


	/**
	 *
	 */
	public void callAfterPageInit() throws JRScriptletException
	{
		this.afterColumnInit();
		this.afterPageInit();
	}


	/**
	 *
	 */
	public void callBeforeColumnInit() throws JRScriptletException
	{
		this.beforeColumnInit();
	}


	/**
	 *
	 */
	public void callAfterColumnInit() throws JRScriptletException
	{
		this.afterColumnInit();
	}


	/**
	 *
	 */
	public void callBeforeGroupInit() throws JRScriptletException
	{
		if(groups != null && groups.length > 0)
		{
			JRFillGroup group = null;
			for(int i = 0; i < groups.length; i++)
			{
				group = groups[i];
				if (group.hasChanged())
				{
					this.beforeGroupInit(group.getName());
				}
			}
		}
	}


	/**
	 *
	 */
	public void callAfterGroupInit() throws JRScriptletException
	{
		if(groups != null && groups.length > 0)
		{
			JRFillGroup group = null;
			for(int i = groups.length - 1; i >= 0; i--)
			{
				group = groups[i];
				if (group.hasChanged())
				{
					this.afterGroupInit(group.getName());
				}
			}
		}
	}


	/**
	 *
	 */
	public void callBeforeDetailEval() throws JRScriptletException
	{
		this.beforeDetailEval();
	}


	/**
	 *
	 */
	public void callAfterDetailEval() throws JRScriptletException
	{
		this.afterDetailEval();
	}


	/**
	 *
	 */
	public abstract void beforeReportInit() throws JRScriptletException;


	/**
	 *
	 */
	public abstract void afterReportInit() throws JRScriptletException;


	/**
	 *
	 */
	public abstract void beforePageInit() throws JRScriptletException;


	/**
	 *
	 */
	public abstract void afterPageInit() throws JRScriptletException;


	/**
	 *
	 */
	public abstract void beforeColumnInit() throws JRScriptletException;


	/**
	 *
	 */
	public abstract void afterColumnInit() throws JRScriptletException;


	/**
	 *
	 */
	public abstract void beforeGroupInit(String groupName) throws JRScriptletException;


	/**
	 *
	 */
	public abstract void afterGroupInit(String groupName) throws JRScriptletException;


	/**
	 *
	 */
	public abstract void beforeDetailEval() throws JRScriptletException;


	/**
	 *
	 */
	public abstract void afterDetailEval() throws JRScriptletException;


}

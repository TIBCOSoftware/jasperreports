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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

/**
 * The base implementation of {@link net.sf.jasperreports.engine.JRDataset JRDataset}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseDataset implements JRDataset, Serializable, JRChangeEventsSupport
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_WHEN_RESOURCE_MISSING_TYPE = "whenResourceMissingType";

	protected final boolean isMain;
	protected String name = null;
	protected String scriptletClass = null;
	protected JRParameter[] parameters = null;
	protected JRQuery query = null;
	protected JRField[] fields = null;
	protected JRSortField[] sortFields = null;
	protected JRVariable[] variables = null;
	protected JRGroup[] groups = null;
	protected String resourceBundle = null;
	protected byte whenResourceMissingType = WHEN_RESOURCE_MISSING_TYPE_NULL;
	protected JRPropertiesMap propertiesMap;
	protected JRExpression filterExpression;
	
	protected JRBaseDataset(boolean isMain)
	{
		this.isMain = isMain;
		
		propertiesMap = new JRPropertiesMap();
	}
	
	protected JRBaseDataset(JRDataset dataset, JRBaseObjectFactory factory)
	{
		factory.put(dataset, this);
		
		name = dataset.getName();
		scriptletClass = dataset.getScriptletClass();
		resourceBundle = dataset.getResourceBundle();
		whenResourceMissingType = dataset.getWhenResourceMissingType();

		/*   */
		this.propertiesMap = dataset.getPropertiesMap().cloneProperties();

		query = factory.getQuery(dataset.getQuery());

		isMain = dataset.isMainDataset();
		
		/*   */
		JRParameter[] jrParameters = dataset.getParameters();
		if (jrParameters != null && jrParameters.length > 0)
		{
			parameters = new JRParameter[jrParameters.length];
			for(int i = 0; i < parameters.length; i++)
			{
				parameters[i] = factory.getParameter(jrParameters[i]);
			}
		}
		
		/*   */
		JRField[] jrFields = dataset.getFields();
		if (jrFields != null && jrFields.length > 0)
		{
			fields = new JRField[jrFields.length];
			for(int i = 0; i < fields.length; i++)
			{
				fields[i] = factory.getField(jrFields[i]);
			}
		}

		/*   */
		JRSortField[] jrSortFields = dataset.getSortFields();
		if (jrSortFields != null && jrSortFields.length > 0)
		{
			sortFields = new JRSortField[jrSortFields.length];
			for(int i = 0; i < sortFields.length; i++)
			{
				sortFields[i] = factory.getSortField(jrSortFields[i]);
			}
		}

		/*   */
		JRVariable[] jrVariables = dataset.getVariables();
		if (jrVariables != null && jrVariables.length > 0)
		{
			variables = new JRVariable[jrVariables.length];
			for(int i = 0; i < variables.length; i++)
			{
				variables[i] = factory.getVariable(jrVariables[i]);
			}
		}

		/*   */
		JRGroup[] jrGroups = dataset.getGroups();
		if (jrGroups != null && jrGroups.length > 0)
		{
			groups = new JRGroup[jrGroups.length];
			for(int i = 0; i < groups.length; i++)
			{
				groups[i] = factory.getGroup(jrGroups[i]);
			}
		}
		
		filterExpression = factory.getExpression(dataset.getFilterExpression());
	}

	
	/**
	 *
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 *
	 */
	public String getScriptletClass()
	{
		return scriptletClass;
	}

	/**
	 *
	 */
	public JRQuery getQuery()
	{
		return query;
	}

	/**
	 *
	 */
	public JRParameter[] getParameters()
	{
		return parameters;
	}

	/**
	 *
	 */
	public JRField[] getFields()
	{
		return fields;
	}

	/**
	 *
	 */
	public JRSortField[] getSortFields()
	{
		return sortFields;
	}

	/**
	 *
	 */
	public JRVariable[] getVariables()
	{
		return variables;
	}

	/**
	 *
	 */
	public JRGroup[] getGroups()
	{
		return groups;
	}

	public boolean isMainDataset()
	{
		return isMain;
	}

	public String getResourceBundle()
	{
		return resourceBundle;
	}

	public byte getWhenResourceMissingType()
	{
		return whenResourceMissingType;
	}

	public void setWhenResourceMissingType(byte whenResourceMissingType)
	{
		byte old = this.whenResourceMissingType;
		this.whenResourceMissingType = whenResourceMissingType;
		getEventSupport().firePropertyChange(PROPERTY_WHEN_RESOURCE_MISSING_TYPE, old, this.whenResourceMissingType);
	}

	public boolean hasProperties()
	{
		return propertiesMap != null && propertiesMap.hasProperties();
	}

	public JRPropertiesMap getPropertiesMap()
	{
		return propertiesMap;
	}

	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}

	public JRExpression getFilterExpression()
	{
		return filterExpression;
	}
	
	/**
	 * 
	 */
	public Object clone() 
	{
		JRBaseDataset clone = null;

		try
		{
			clone = (JRBaseDataset)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		if (query != null)
		{
			clone.query = (JRQuery)query.clone();
		}
		if (filterExpression != null)
		{
			clone.filterExpression = (JRExpression)filterExpression.clone();
		}
		if (propertiesMap != null)
		{
			clone.propertiesMap = (JRPropertiesMap)propertiesMap.clone();
		}

		if (parameters != null)
		{
			clone.parameters = new JRParameter[parameters.length];
			for(int i = 0; i < parameters.length; i++)
			{
				clone.parameters[i] = (JRParameter)parameters[i].clone();
			}
		}

		if (fields != null)
		{
			clone.fields = new JRField[fields.length];
			for(int i = 0; i < fields.length; i++)
			{
				clone.fields[i] = (JRField)fields[i].clone();
			}
		}

		if (sortFields != null)
		{
			clone.sortFields = new JRSortField[sortFields.length];
			for(int i = 0; i < sortFields.length; i++)
			{
				clone.sortFields[i] = (JRSortField)sortFields[i].clone();
			}
		}

		if (variables != null)
		{
			clone.variables = new JRVariable[variables.length];
			for(int i = 0; i < variables.length; i++)
			{
				clone.variables[i] = (JRVariable)variables[i].clone();
			}
		}

		if (groups != null)
		{
			clone.groups = new JRGroup[groups.length];
			for(int i = 0; i < groups.length; i++)
			{
				clone.groups[i] = (JRGroup)groups[i].clone();
			}
		}

		return clone;
	}

	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}
}

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
package net.sf.jasperreports.engine.design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.base.JRBaseDatasetRun;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * Implementation of {@link net.sf.jasperreports.engine.JRDatasetRun JRDatasetRun} to be used for report desing.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignDatasetRun extends JRBaseDatasetRun implements JRChangeEventsSupport
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private Map<String, JRDatasetParameter> parametersMap;
	private List<JRDatasetParameter> parametersList;
	
	public static final String PROPERTY_CONNECTION_EXPRESSION = "connectionExpression";
	
	public static final String PROPERTY_DATASET_NAME = "datasetName";
	
	public static final String PROPERTY_DATA_SOURCE_EXPRESSION = "dataSourceExpression";
	
	public static final String PROPERTY_PARAMETERS_MAP_EXPRESSION = "parametersMapExpression";
	
	public static final String PROPERTY_PARAMETERS = "parameters";
	
	
	/**
	 * Creates an empty dataset instantiation.
	 */
	public JRDesignDatasetRun()
	{
		parametersMap = new HashMap<String, JRDatasetParameter>();
		parametersList = new ArrayList<JRDatasetParameter>();
	}
	
	
	/**
	 * Adds a parameter value.
	 * 
	 * @param parameter the parameter value
	 * @throws JRException
	 * @see net.sf.jasperreports.engine.JRDatasetRun#getParameters()
	 */
	public void addParameter(JRDatasetParameter parameter) throws JRException
	{
		if (parametersMap.containsKey(parameter.getName()))
		{
			throw new JRException("Duplicate declaration of dataset parameter : " + parameter.getName());
		}
		
		parametersMap.put(parameter.getName(), parameter);
		parametersList.add(parameter);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_PARAMETERS, 
				parameter, parametersList.size() - 1);
	}
	
	
	/**
	 * Removes a parameter value.
	 * 
	 * @param parameterName the parameter name
	 * @return the removed parameter value
	 */
	public JRDatasetParameter removeParameter(String parameterName)
	{
		JRDatasetParameter param = parametersMap.remove(parameterName);
		if (param != null)
		{
			int idx = parametersList.indexOf(param);
			if (idx >= 0)
			{
				parametersList.remove(idx);
			}
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_PARAMETERS, param, idx);
		}
		
		return param;
	}
	
	
	/**
	 * Removes a parameter value.
	 * 
	 * @param parameter the parameter value
	 * @return the parameter value
	 */
	public JRDatasetParameter removeParameter(JRDatasetParameter parameter)
	{
		return removeParameter(parameter.getName());
	}

	
	/**
	 * Sets the connection expression.
	 * 
	 * @param connectionExpression the connection expression
	 * @see net.sf.jasperreports.engine.JRDatasetRun#getConnectionExpression()
	 */
	public void setConnectionExpression(JRExpression connectionExpression)
	{
		Object old = this.connectionExpression;
		this.connectionExpression = connectionExpression;
		getEventSupport().firePropertyChange(PROPERTY_CONNECTION_EXPRESSION, old, this.connectionExpression);
	}

	
	/**
	 * Sets the sub dataset name.
	 * 
	 * @param datasetName the sub dataset name
	 * @see net.sf.jasperreports.engine.JRDatasetRun#getDatasetName()
	 */
	public void setDatasetName(String datasetName)
	{
		Object old = this.datasetName;
		this.datasetName = datasetName;
		getEventSupport().firePropertyChange(PROPERTY_DATASET_NAME, old, this.datasetName);
	}

	
	/**
	 * Sets the data source expression.
	 * 
	 * @param dataSourceExpression the data source expression
	 * @see net.sf.jasperreports.engine.JRDatasetRun#getDataSourceExpression()
	 */
	public void setDataSourceExpression(JRExpression dataSourceExpression)
	{
		Object old = this.dataSourceExpression;
		this.dataSourceExpression = dataSourceExpression;
		getEventSupport().firePropertyChange(PROPERTY_DATA_SOURCE_EXPRESSION, old, this.dataSourceExpression);
	}

	
	/**
	 * Sets the parameters map expression.
	 * 
	 * @param parametersMapExpression the parameters map expression
	 * @see net.sf.jasperreports.engine.JRDatasetRun#getParametersMapExpression()
	 */
	public void setParametersMapExpression(JRExpression parametersMapExpression)
	{
		Object old = this.parametersMapExpression;
		this.parametersMapExpression = parametersMapExpression;
		getEventSupport().firePropertyChange(PROPERTY_PARAMETERS_MAP_EXPRESSION, old, this.parametersMapExpression);
	}

	public JRDatasetParameter[] getParameters()
	{
		JRDatasetParameter[] params = new JRDatasetParameter[parametersList.size()];
		parametersList.toArray(params);
		return params;
	}

	/**
	 * 
	 */
	public Object clone() 
	{
		JRDesignDatasetRun clone = (JRDesignDatasetRun)super.clone();
		
		if (parametersList != null)
		{
			clone.parametersList = new ArrayList<JRDatasetParameter>(parametersList.size());
			clone.parametersMap = new HashMap<String, JRDatasetParameter>(parametersList.size());
			for(int i = 0; i < parametersList.size(); i++)
			{
				JRDatasetParameter parameter = JRCloneUtils.nullSafeClone(parametersList.get(i));
				clone.parametersList.add(parameter);
				clone.parametersMap.put(parameter.getName(), parameter);
			}
		}
		
		clone.eventSupport = null;
		
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

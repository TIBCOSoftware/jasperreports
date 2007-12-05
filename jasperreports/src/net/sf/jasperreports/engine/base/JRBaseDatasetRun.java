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
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * Base implementation of the {@link net.sf.jasperreports.engine.JRDatasetRun JRDatasetRun} interface.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseDatasetRun implements JRDatasetRun, Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected String datasetName;
	protected JRExpression parametersMapExpression;
	protected JRDatasetParameter[] parameters;
	protected JRExpression connectionExpression;
	protected JRExpression dataSourceExpression;
	
	
	/**
	 * Creates an empty object.
	 */
	protected JRBaseDatasetRun()
	{
	}

	
	/**
	 * Creates a copy of a dataset instantiation.
	 * 
	 * @param datasetRun the dataset instantiation
	 * @param factory the base object factory
	 */
	protected JRBaseDatasetRun(JRDatasetRun datasetRun, JRBaseObjectFactory factory)
	{
		factory.put(datasetRun, this);
		
		datasetName = datasetRun.getDatasetName();
		parametersMapExpression = factory.getExpression(datasetRun.getParametersMapExpression());
		connectionExpression = factory.getExpression(datasetRun.getConnectionExpression());
		dataSourceExpression = factory.getExpression(datasetRun.getDataSourceExpression());
		
		JRDatasetParameter[] datasetParams = datasetRun.getParameters();
		if (datasetParams != null && datasetParams.length > 0)
		{
			parameters = new JRBaseDatasetParameter[datasetParams.length];
			for (int i = 0; i < parameters.length; i++)
			{
				parameters[i] = factory.getDatasetParameter(datasetParams[i]);
			}
		}
	}

	public String getDatasetName()
	{
		return datasetName;
	}

	public JRExpression getParametersMapExpression()
	{
		return parametersMapExpression;
	}

	public JRDatasetParameter[] getParameters()
	{
		return parameters;
	}

	public JRExpression getConnectionExpression()
	{
		return connectionExpression;
	}

	public JRExpression getDataSourceExpression()
	{
		return dataSourceExpression;
	}

	/**
	 * 
	 */
	public Object clone() 
	{
		JRBaseDatasetRun clone = null;
		
		try
		{
			clone = (JRBaseDatasetRun)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}

		if (parametersMapExpression != null)
		{
			clone.parametersMapExpression = (JRExpression)parametersMapExpression.clone();
		}
		if (connectionExpression != null)
		{
			clone.connectionExpression = (JRExpression)connectionExpression.clone();
		}
		if (dataSourceExpression != null)
		{
			clone.dataSourceExpression = (JRExpression)dataSourceExpression.clone();
		}

		if (parameters != null)
		{
			clone.parameters = new JRDatasetParameter[parameters.length];
			for(int i = 0; i < parameters.length; i++)
			{
				clone.parameters[i] = (JRDatasetParameter)parameters[i].clone();
			}
		}

		return clone;
	}
}

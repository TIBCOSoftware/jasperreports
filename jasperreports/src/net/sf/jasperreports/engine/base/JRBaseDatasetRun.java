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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRCloneUtils;

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

		clone.parametersMapExpression = JRCloneUtils.nullSafeClone(parametersMapExpression);
		clone.connectionExpression = JRCloneUtils.nullSafeClone(connectionExpression);
		clone.dataSourceExpression = JRCloneUtils.nullSafeClone(dataSourceExpression);

		clone.parameters = JRCloneUtils.cloneArray(parameters);

		return clone;
	}
}

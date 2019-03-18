/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import java.util.Map;

import net.sf.jasperreports.repo.RepositoryContext;
import net.sf.jasperreports.repo.SimpleRepositoryContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ParameterContributorContext implements Cloneable
{

	private JasperReportsContext jasperReportsContext;
	private RepositoryContext repositoryContext;
	private JRDataset dataset;
	private Map<String,Object> parameterValues;

	/**
	 *
	 */
	public ParameterContributorContext(
		JasperReportsContext jasperReportsContext,
		JRDataset dataset,
		Map<String,Object> parameterValues
		)
	{
		this(SimpleRepositoryContext.of(jasperReportsContext),
				dataset, parameterValues);
	}
	
	public ParameterContributorContext(
		RepositoryContext repositoryContext,
		JRDataset dataset,
		Map<String,Object> parameterValues
		)
	{
		this.jasperReportsContext = repositoryContext.getJasperReportsContext();
		this.repositoryContext = repositoryContext;
		this.dataset = dataset;
		this.parameterValues = parameterValues;
	}
	
	public ParameterContributorContext withRepositoryContext(RepositoryContext repositoryContext)
	{
		try
		{
			ParameterContributorContext clone = (ParameterContributorContext) clone();
			clone.repositoryContext = repositoryContext;
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			// should not happen
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	public JasperReportsContext getJasperReportsContext()
	{
		return jasperReportsContext;
	}
	
	public RepositoryContext getRepositoryContext()
	{
		return repositoryContext;
	}
	
	/**
	 *
	 */
	public Map<String,Object> getParameterValues()
	{
		return parameterValues;
	}
	
	/**
	 * Returns the dataset for which scriptlets are to be created.
	 * 
	 * @return a dataset
	 */
	public JRDataset getDataset()
	{
		return dataset;
	}
}

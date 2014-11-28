/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ParameterContributorContext
{

	/**
	 * @deprecated To be removed.
	 */
	private JasperReport jasperReport;
	private JasperReportsContext jasperReportsContext;
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
		this.jasperReportsContext = jasperReportsContext;
		this.dataset = dataset;
		this.parameterValues = parameterValues;
		this.jasperReport = (JasperReport)parameterValues.get(JRParameter.JASPER_REPORT);
	}

	/**
	 * @deprecated Replaced by {@link #ParameterContributorContext(JasperReportsContext, JRDataset, Map)}.
	 */
	public ParameterContributorContext(Map<String,Object> parameterValues, JRDataset dataset)
	{
		this(DefaultJasperReportsContext.getInstance(), dataset, parameterValues);
	}

	/**
	 * Returns the {@link JasperReport} object for this context.
	 * 
	 * <p>
	 * Note that this context might correspond to a subdataset in the report.
	 * Use {@link #getDataset()} to retrieve the dataset for which scriptlets
	 * are to be created
	 * </p>
	 * 
	 * @return the current {@link JasperReport} object
	 * @see #getDataset()
	 * @deprecated To be removed.
	 */
	public JasperReport getJasperReport()
	{
		return jasperReport;
	}
	
	/**
	 *
	 */
	public JasperReportsContext getJasperReportsContext()
	{
		return jasperReportsContext;
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

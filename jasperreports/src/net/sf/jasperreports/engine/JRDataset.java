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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;

/**
 * Interface representing a data set that can be used in a report.
 * <p>
 * A data set consists of parameters, fields, variables, groups and an optional query.
 * When a data set gets instantiated, parameter values and a data source is passed to it.
 * <p>
 * A report has one main data set and multiple sub data sets that can be instantiated by charts and crosstabs.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * 
 * @see net.sf.jasperreports.engine.JRDatasetRun
 * @see net.sf.jasperreports.engine.JRReport#getMainDataset()
 * @see net.sf.jasperreports.engine.JRReport#getDatasets()
 */
public interface JRDataset extends JRPropertiesHolder, JRCloneable
{

	
	/**
	 * Returns the dataset name.
	 * 
	 * @return the name of the dataset 
	 */
	public String getName();

	
	/**
	 * The name of the scriptlet class to be used when iterating this dataset.
	 * 
	 * @return the scriplet class name
	 */
	public String getScriptletClass();

	
	/**
	 * Returns the dataset's scriptlets.
	 * 
	 * @return the dataset's scriptlets
	 */
	public JRScriptlet[] getScriptlets();

	
	/**
	 * Returns the dataset's parameters.
	 * 
	 * @return the dataset's parameters
	 */
	public JRParameter[] getParameters();

	
	/**
	 * Returns the query of the dataset.
	 * <p>
	 * The query is used by passing a connection is passed to the dataset when instantiating.
	 * 
	 * @return the query of the dataset
	 */
	public JRQuery getQuery();

	
	/**
	 * Returns the dataset's fields.
	 * 
	 * @return the dataset's fields
	 */
	public JRField[] getFields();

	
	/**
	 * Returns the dataset's sort fields.
	 * 
	 * @return the dataset's sort fields
	 */
	public JRSortField[] getSortFields();

	
	/**
	 * Returns the dataset's variables.
	 * 
	 * @return the dataset's variables
	 */
	public JRVariable[] getVariables();

	
	/**
	 * Returns the dataset's groups.
	 * 
	 * @return the dataset's groups
	 */
	public JRGroup[] getGroups();
	
	
	/**
	 * Decides whether this dataset is the main report dataset or a sub dataset.
	 * 
	 * @return <code>true</code> if and only if this dataset is the main report dataset
	 */
	public boolean isMainDataset();


	/**
	 * Returns the resource bundle base name.
	 * <p>
	 * The resource bundle is used when evaluating expressions.
	 * 
	 * @return the resource bundle base name
	 */
	public String getResourceBundle();


	/**
	 * Returns the resource missing handling type.
	 * 
	 * @return the resource missing handling type
	 */
	public WhenResourceMissingTypeEnum getWhenResourceMissingTypeValue();
	
	
	/**
	 * Sets the resource missing handling type.
	 * @param whenResourceMissingType the resource missing handling type
	 */
	public void setWhenResourceMissingType(WhenResourceMissingTypeEnum whenResourceMissingType);

	
	/**
	 * Returns the dataset filter expression.
	 * <p>
	 * This expression is used to filter the rows of the 
	 * {@link JRDataSource data source} that this dataset will iterate on.
	 * </p>
	 * <p>
	 * This expression (if not null) is evaluated immediately after a new row is 
	 * {@link JRDataSource#next() produced} by the data source.
	 * The evaluation is performed using field and variable values corresponding to the new row. 
	 * When the result of the evaluation is <code>Boolean.TRUE</code> the row gets processed by the report
	 * filling engine.
	 * When the result is null or <code>Boolean.FALSE</code>, the current row will be skipped and the datasource will be asked for the next row.
	 * </p>
	 * 
	 * @return the dataset filter expression
	 */
	public JRExpression getFilterExpression();
}

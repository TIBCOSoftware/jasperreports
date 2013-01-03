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

import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

/**
 * Element datasets are used to represent the report data needed to generate a chart or crosstab.
 * The dataset structure may vary with each chart type or crosstab.
 * This is the superinterface for all datasets and contains common dataset properties.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRElementDataset extends JRCloneable
{

	/**
	 * Gets the reset type. This specifies the range of report data used for filling the dataset.
	 * @return one of the reset constants in {@link ResetTypeEnum}
	 */
	public ResetTypeEnum getResetTypeValue();

	/**
	 * Gets the selected reset group in case of reset type group.
	 */
	public JRGroup getResetGroup();

	/**
	 * Returns the increment type. This specifies dataset values increment step.
	 * @return one of the increment constants in {@link IncrementTypeEnum}.
	 */
	public IncrementTypeEnum getIncrementTypeValue();

	/**
	 * Gets the selected increment group in case of increment type group.
	 */
	public JRGroup getIncrementGroup();

	/**
	 *  
	 */
	public void collectExpressions(JRExpressionCollector collector);

	/**
	 * Returns the sub dataset run for this chart dataset.
	 * 
	 * @return the sub dataset run for this chart dataset
	 */
	public JRDatasetRun getDatasetRun();
	
	/**
	 * Returns the "increment when" expression.
	 * <p>
	 * This expression determines whether a dataset will be incremented or not.
	 * <p>
	 * The expression (if not null) is evaluated before each increment of the dataset.
	 * The increment will be carried on only when the result of the evaluation is <code>Boolean.TRUE</code>;
	 * if the result is null or false, the increment will not be performed.
	 * 
	 * 
	 * @return the "increment when" expression
	 */
	public JRExpression getIncrementWhenExpression();

}

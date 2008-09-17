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
package net.sf.jasperreports.engine;

/**
 * Interface of an sub dataset instantiation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see net.sf.jasperreports.engine.JRDataset
 * @see net.sf.jasperreports.engine.JRChartDataset#getDatasetRun()
 */
public interface JRDatasetRun extends JRCloneable
{
	/**
	 * Returns the sub dataset name.
	 * 
	 * @return the sub dataset name
	 */
	public String getDatasetName();
	
	
	/**
	 * Returns the parameters map expression.
	 * <p>
	 * The result of this expression is used as the parameters map when instantiating the dataset.
	 * 
	 * @return the parameters map expression
	 */
	public JRExpression getParametersMapExpression();
	
	
	/**
	 * Returns the list of parameter values.
	 * 
	 * @return the list of parameter values
	 */
	public JRDatasetParameter[] getParameters();

	
	/**
	 * Returns the DB connection expression.
	 * <p>
	 * The result of this expression is used as the DB connection when instantiating the dataset.
	 * 
	 * @return the DB connection expression
	 */
	public JRExpression getConnectionExpression();

	
	/**
	 * Returns the data source expression.
	 * <p>
	 * The result of this expression is used as the data source when instantiating the dataset.
	 * 
	 * @return the data source expression
	 */
	public JRExpression getDataSourceExpression();

}

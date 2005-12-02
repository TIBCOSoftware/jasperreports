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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.query;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;


/**
 * Query executer interface.
 * <p/>
 * An implementation of this interface is created when the input data of a report/dataset
 * is specified by a query.
 * <p/>
 * The implementation will run the query and create a {@link net.sf.jasperreports.engine.JRDataSource JRDataSource}
 * from the result.
 * <p/>
 * The query executers would usually be initialized by a {@link net.sf.jasperreports.engine.query.JRQueryExecuterFactory JRQueryExecuterFactory}
 * with the query and the parameter values.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see net.sf.jasperreports.engine.query.JRQueryExecuterFactory
 */
public interface JRQueryExecuter
{
	/**
	 * Executes the query and creates a {@link JRDataSource JRDataSource} out of the result.
	 * 
	 * @return a {@link JRDataSource JRDataSource} wrapping the query execution result.
	 * @throws JRException
	 */
	public JRDataSource createDatasource() throws JRException;

	/**
	 * Closes resources kept open during the data source iteration.
	 * <p/>
	 * This method is called after the report is filled or the dataset is iterated.
	 * If a resource is not needed after the data source has been created, it should be
	 * released at the end of {@link #createDatasource() createDatasource}.
	 */
	public void close();

	/**
	 * Cancels the query if it's currently running.
	 * <p/>
	 * This method will be called from a different thread if the client decides to
	 * cancel the filling process.
	 * 
	 * @return <code>true</code> iff the query was running and it has been cancelled
	 * @throws JRException
	 */
	public boolean cancelQuery() throws JRException;
}

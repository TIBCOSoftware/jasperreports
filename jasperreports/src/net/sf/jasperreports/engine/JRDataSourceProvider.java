/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine;

/**
 * This interface abstracts the means of creating and disposing
 * a data source. This interface is meant to be the standard way to
 * plug custom data sources into GUI designers. Typically the report
 * developer will implement this interface to create and return a configured
 * data source of the desired type and then configure the designer to use
 * this implementation. 
 * <br>
 * The following example demonstrates a provider for a 
 * {@link net.sf.jasperreports.engine.data.JRBeanCollectionDataSource JRBeanCollectionDataSource}.
 * <code>
 * <pre>
 * public class MyBeansDataSource extends JRAbstractBeanDataSourceProvider {
 * 
 * 	public MyBeansDataSource() {
 * 		super(PersonBean.class);
 * 	}
 * 
 * 	public JRDataSource create(JasperReport report) throws JRException {
 * 		ArrayList list = new ArrayList();
 * 		list.add(new PersonBean("Teodor"));
 * 		list.add(new PersonBean("Peter"));
 * 		return new JRBeanCollectionDataSource(list);
 * 	}
 *	
 * 	public void dispose(JRDataSource dataSource) throws JRException {
 *		// nothing to dispose
 * 	}
 * }
 * </pre>
 * </code>
 * @author Peter Severin (peter_s@sourceforge.net, contact@jasperassistant.com)
 * @version $Id$
 */
public interface JRDataSourceProvider 
{

	/**
	 * Returns true if the provider supports the {@link #getFields() getFields} 
	 * operation. By returning true in this method the data source provider indicates
	 * that it is able to introspect the data source and discover the available fields.
	 * 
	 * @return true if the {@link #getFields() getFields} operation is supported.
	 */
	public boolean supportsGetFieldsOperation();
	
	/**
	 * Returns the fields that are available from the data source.
	 * 
	 * @throws UnsupportedOperationException is the method is not supported
	 * @throws JRException if an error occurs. 
	 * @return a non null fields array. If there are no fields then an empty array must be returned.
	 */
	public JRField[] getFields() throws JRException, UnsupportedOperationException;
	
	/**
	 * Creates and returns a new instance of the provided data source.
	 *  
	 * @param report the report that will be filled using the created data source.
	 * @throws JRException if the data source creation has failed
	 */
	public JRDataSource create(JasperReport report) throws JRException;

	/**
	 * Disposes the data source previously obtained using the 
	 * {@link #createDataSource() createDataSource()} method.
	 * This method must close any resources associated with the 
	 * data source. For instance the database connection should be 
	 * closed in case of the
	 * {@link JRResultSetDataSource JRResultSetDataSource}. 
	 * <br>
	 * Note: The provider must take care of the resource - data source association. 
	 * For example in case of the {@link JRResultSetDataSource JRResultSetDataSource}
	 * a subclass of this data source can be created. This subclass will
	 * hold the database connection and the prepared statement that were
	 * used to obtain the ResultSet. On the time of the dispose these resources
	 * can be retrieved from the data source object and closed.
	 *
	 * @param dataSource the data source to dispose 
	 * @throws JRException if the data source could not be disposed
	 */
	public void dispose(JRDataSource dataSource) throws JRException;
	
}

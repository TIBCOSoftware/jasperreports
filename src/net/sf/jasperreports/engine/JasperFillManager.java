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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.fill.JRFiller;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;


/**
 * Façade class for filling compiled report designs with data from report data sources, 
 * in order to produce page-oriented documents, ready-to-print.
 * <p>
 * All methods receive a Map object that should contain the values for the report parameters.
 * These value are retrieved by the engine using the corresponding report parameter name as the key. 
 * <p>
 * There are two types of method signatures with regards to the data source
 * provided for filling the report:
 * <ul>
 * <li>Methods that receive an instance of the {@link net.sf.jasperreports.engine.JRDataSource} interface
 * and use it directly for retrieving report data;
 * <li>Methods that receive an instance of the {@link java.sql.Connection} interface and retrieve
 * the report data by executing the report internal SQL query through this JDBC connection and wrapping 
 * the returned {@link java.sql.ResultSet} object inside a {@link net.sf.jasperreports.engine.JRResultSetDataSource}
 * instance. 
 * </ul>
 * 
 * @see net.sf.jasperreports.engine.JasperReport
 * @see net.sf.jasperreports.engine.JRDataSource
 * @see net.sf.jasperreports.engine.fill.JRFiller
 * @see net.sf.jasperreports.engine.JasperPrint
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JasperFillManager
{


	/**
	 * Fills the compiled report design loaded from the specified file.
	 * The result of this operation is another file that will contain the serialized  
	 * {@link JasperPrint} object representing the generated document,
	 * having the same name as the report design as declared in the source file, 
	 * plus the <code>*.jrprint</code> extension, located in the same directory as the source file. 
	 * 
	 * @param sourceFileName source file containing the compile report design
	 * @param parameters     report parameters map
	 * @param connection     JDBC connection object to use for executing the report internal SQL query
	 */
	public static String fillReportToFile(
		String sourceFileName, 
		Map parameters,
		Connection connection
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperReport.getName() + ".jrprint");
		String destFileName = destFile.toString();

		fillReportToFile(jasperReport, destFileName, parameters, connection);
		
		return destFileName;
	}

	
	/**
	 * Fills the compiled report design loaded from the file received as the first parameter
	 * and places the result in the file specified by the second parameter.
	 * 
	 * @param sourceFileName source file containing the compile report design
	 * @param destFileName   file name to place the generated report into
	 * @param parameters     report parameters map
	 * @param connection     JDBC connection object to use for executing the report internal SQL query
	 */
	public static void fillReportToFile(
		String sourceFileName, 
		String destFileName, 
		Map parameters,
		Connection conn
		) throws JRException
	{
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFileName);

		fillReportToFile(jasperReport, destFileName, parameters, conn);
	}

	
	/**
	 * Fills the compiled report design received as the first parameter
	 * and places the result in the file specified by the second parameter.
	 * 
	 * @param jasperReport compiled report design object to use for filling
	 * @param destFileName file name to place the generated report into
	 * @param parameters   report parameters map
	 * @param connection   JDBC connection object to use for executing the report internal SQL query
	 */
	public static void fillReportToFile(
		JasperReport jasperReport, 
		String destFileName, 
		Map parameters,
		Connection conn
		) throws JRException
	{
		JasperPrint jasperPrint = fillReport(jasperReport, parameters, conn);

		JRSaver.saveObject(jasperPrint, destFileName);
	}

	
	/**
	 * Fills the compiled report design loaded from the specified file and returns
	 * the generated report object.
	 * 
	 * @param sourceFileName source file containing the compile report design
	 * @param parameters     report parameters map
	 * @param connection     JDBC connection object to use for executing the report internal SQL query
	 * @return generated report object
	 */
	public static JasperPrint fillReport(
		String sourceFileName, 
		Map parameters,
		Connection conn
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		return fillReport(jasperReport, parameters, conn);
	}

	
	/**
	 * Fills the compiled report design loaded from the supplied input stream and writes
	 * the generated report object to the output stream specified by the second parameter.
	 * 
	 * @param inputStream  input stream to read the compiled report design object from
	 * @param outputStream output stream to write the generated report object to
	 * @param parameters   report parameters map
	 * @param connection   JDBC connection object to use for executing the report internal SQL query
	 */
	public static void fillReportToStream(
		InputStream inputStream, 
		OutputStream outputStream, 
		Map parameters,
		Connection conn
		) throws JRException
	{
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(inputStream);

		fillReportToStream(jasperReport, outputStream, parameters, conn);
	}

	
	/**
	 * Fills the compiled report design supplied as the first parameter and writes
	 * the generated report object to the output stream specified by the second parameter.
	 * 
	 * @param jasperReport compiled report design object to use for filling
	 * @param outputStream output stream to write the generated report object to
	 * @param parameters   report parameters map
	 * @param connection   JDBC connection object to use for executing the report internal SQL query
	 */
	public static void fillReportToStream(
		JasperReport jasperReport, 
		OutputStream outputStream, 
		Map parameters,
		Connection conn
		) throws JRException
	{
		JasperPrint jasperPrint = fillReport(jasperReport, parameters, conn);

		JRSaver.saveObject(jasperPrint, outputStream);
	}

	
	/**
	 * Fills the compiled report design loaded from the supplied input stream and returns
	 * the generated report object.
	 * 
	 * @param inputStream  input stream to read the compiled report design object from
	 * @param parameters   report parameters map
	 * @param connection   JDBC connection object to use for executing the report internal SQL query
	 * @return generated report object
	 */
	public static JasperPrint fillReport(
		InputStream inputStream, 
		Map parameters,
		Connection conn
		) throws JRException
	{
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(inputStream);

		return fillReport(jasperReport, parameters, conn);
	}

	
	/**
	 * Fills the compiled report design supplied as the first parameter and returns
	 * the generated report object.
	 * 
	 * @param jasperReport compiled report design object to use for filling
	 * @param parameters   report parameters map
	 * @param connection   JDBC connection object to use for executing the report internal SQL query
	 * @return generated report object
	 */
	public static JasperPrint fillReport(
		JasperReport jasperReport, 
		Map parameters, 
		Connection conn
		) throws JRException
	{
		return JRFiller.fillReport(jasperReport, parameters, conn);
	}

	
	/**
	 * Fills the compiled report design loaded from the specified file.
	 * The result of this operation is another file that will contain the serialized  
	 * {@link JasperPrint} object representing the generated document,
	 * having the same name as the report design as declared in the source file, 
	 * plus the <code>*.jrprint</code> extension, located in the same directory as the source file. 
	 * 
	 * @param sourceFileName source file containing the compile report design
	 * @param parameters     report parameters map
	 * @param dataSource     data source object
	 */
	public static String fillReportToFile(
		String sourceFileName, 
		Map parameters,
		JRDataSource dataSource
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperReport.getName() + ".jrprint");
		String destFileName = destFile.toString();

		fillReportToFile(jasperReport, destFileName, parameters, dataSource);
		
		return destFileName;
	}

	
	/**
	 * Fills the compiled report design loaded from the file received as the first parameter
	 * and places the result in the file specified by the second parameter.
	 * 
	 * @param sourceFileName source file containing the compile report design
	 * @param destFileName   file name to place the generated report into
	 * @param parameters     report parameters map
	 * @param dataSource     data source object
	 */
	public static void fillReportToFile(
		String sourceFileName, 
		String destFileName, 
		Map parameters,
		JRDataSource dataSource
		) throws JRException
	{
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFileName);

		fillReportToFile(jasperReport, destFileName, parameters, dataSource);
	}

	
	/**
	 * Fills the compiled report design received as the first parameter
	 * and places the result in the file specified by the second parameter.
	 * 
	 * @param jasperReport compiled report design object to use for filling
	 * @param destFileName file name to place the generated report into
	 * @param parameters   report parameters map
	 * @param dataSource   data source object
	 */
	public static void fillReportToFile(
		JasperReport jasperReport, 
		String destFileName, 
		Map parameters,
		JRDataSource dataSource
		) throws JRException
	{
		JasperPrint jasperPrint = fillReport(jasperReport, parameters, dataSource);

		JRSaver.saveObject(jasperPrint, destFileName);
	}

	
	/**
	 * Fills the compiled report design loaded from the specified file and returns
	 * the generated report object.
	 * 
	 * @param sourceFileName source file containing the compile report design
	 * @param parameters     report parameters map
	 * @param dataSource     data source object
	 * @return generated report object
	 */
	public static JasperPrint fillReport(
		String sourceFileName, 
		Map parameters,
		JRDataSource dataSource
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		return fillReport(jasperReport, parameters, dataSource);
	}

	
	/**
	 * Fills the compiled report design loaded from the supplied input stream and writes
	 * the generated report object to the output stream specified by the second parameter.
	 * 
	 * @param inputStream  input stream to read the compiled report design object from
	 * @param outputStream output stream to write the generated report object to
	 * @param parameters   report parameters map
	 * @param dataSource   data source object
	 */
	public static void fillReportToStream(
		InputStream inputStream, 
		OutputStream outputStream, 
		Map parameters,
		JRDataSource dataSource
		) throws JRException
	{
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(inputStream);

		fillReportToStream(jasperReport, outputStream, parameters, dataSource);
	}

	
	/**
	 * Fills the compiled report design supplied as the first parameter and writes
	 * the generated report object to the output stream specified by the second parameter.
	 * 
	 * @param jasperReport compiled report design object to use for filling
	 * @param outputStream output stream to write the generated report object to
	 * @param parameters   report parameters map
	 * @param dataSource   data source object
	 */
	public static void fillReportToStream(
		JasperReport jasperReport, 
		OutputStream outputStream, 
		Map parameters,
		JRDataSource dataSource
		) throws JRException
	{
		JasperPrint jasperPrint = fillReport(jasperReport, parameters, dataSource);

		JRSaver.saveObject(jasperPrint, outputStream);
	}

	
	/**
	 * Fills the compiled report design loaded from the supplied input stream and returns
	 * the generated report object.
	 * 
	 * @param inputStream  input stream to read the compiled report design object from
	 * @param parameters   report parameters map
	 * @param dataSource   data source object
	 * @return generated report object
	 */
	public static JasperPrint fillReport(
		InputStream inputStream, 
		Map parameters,
		JRDataSource dataSource
		) throws JRException
	{
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(inputStream);

		return fillReport(jasperReport, parameters, dataSource);
	}

	
	/**
	 * Fills the compiled report design supplied as the first parameter and returns
	 * the generated report object.
	 * 
	 * @param jasperReport compiled report design object to use for filling
	 * @param parameters   report parameters map
	 * @param dataSource   data source object
	 * @return generated report object
	 */
	public static JasperPrint fillReport(
		JasperReport jasperReport, 
		Map parameters, 
		JRDataSource dataSource
		) throws JRException
	{
		return JRFiller.fillReport(jasperReport, parameters, dataSource);
	}


}

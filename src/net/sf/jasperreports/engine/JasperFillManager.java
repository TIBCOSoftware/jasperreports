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
package dori.jasper.engine;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Map;

import dori.jasper.engine.fill.JRFiller;
import dori.jasper.engine.util.JRLoader;
import dori.jasper.engine.util.JRSaver;


/**
 * Façade class for the JasperReports engine. 
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JasperFillManager
{


	/**
	 *
	 */
	public static String fillReportToFile(
		String sourceFileName, 
		Map parameters,
		Connection conn
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperReport.getName() + ".jrprint");
		String destFileName = destFile.toString();

		fillReportToFile(jasperReport, destFileName, parameters, conn);
		
		return destFileName;
	}

	
	/**
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
	 */
	public static String fillReportToFile(
		String sourceFileName, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperReport.getName() + ".jrprint");
		String destFileName = destFile.toString();

		fillReportToFile(jasperReport, destFileName, parameters, jrDataSource);
		
		return destFileName;
	}

	
	/**
	 *
	 */
	public static void fillReportToFile(
		String sourceFileName, 
		String destFileName, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFileName);

		fillReportToFile(jasperReport, destFileName, parameters, jrDataSource);
	}

	
	/**
	 *
	 */
	public static void fillReportToFile(
		JasperReport jasperReport, 
		String destFileName, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		JasperPrint jasperPrint = fillReport(jasperReport, parameters, jrDataSource);

		JRSaver.saveObject(jasperPrint, destFileName);
	}

	
	/**
	 *
	 */
	public static JasperPrint fillReport(
		String sourceFileName, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		return fillReport(jasperReport, parameters, jrDataSource);
	}

	
	/**
	 *
	 */
	public static void fillReportToStream(
		InputStream inputStream, 
		OutputStream outputStream, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(inputStream);

		fillReportToStream(jasperReport, outputStream, parameters, jrDataSource);
	}

	
	/**
	 *
	 */
	public static void fillReportToStream(
		JasperReport jasperReport, 
		OutputStream outputStream, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		JasperPrint jasperPrint = fillReport(jasperReport, parameters, jrDataSource);

		JRSaver.saveObject(jasperPrint, outputStream);
	}

	
	/**
	 *
	 */
	public static JasperPrint fillReport(
		InputStream inputStream, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(inputStream);

		return fillReport(jasperReport, parameters, jrDataSource);
	}

	
	/**
	 *
	 */
	public static JasperPrint fillReport(
		JasperReport jasperReport, 
		Map parameters, 
		JRDataSource jrDataSource
		) throws JRException
	{
		return JRFiller.fillReport(jasperReport, parameters, jrDataSource);
	}


}

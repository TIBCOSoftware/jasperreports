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
import java.util.Collection;

import dori.jasper.engine.design.JRDefaultCompiler;
import dori.jasper.engine.design.JRVerifier;
import dori.jasper.engine.design.JasperDesign;
import dori.jasper.engine.util.JRLoader;
import dori.jasper.engine.util.JRSaver;
import dori.jasper.engine.xml.JRXmlLoader;
import dori.jasper.engine.xml.JRXmlWriter;


/**
 * Façade class for the JasperReports engine.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JasperCompileManager
{


	/**
	 *
	 */
	public static String compileReportToFile(String sourceFileName) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		JasperDesign jasperDesign = JRXmlLoader.load(sourceFileName);

		File destFile = new File(sourceFile.getParent(), jasperDesign.getName() + ".jasper");
		String destFileName = destFile.toString();

		compileReportToFile(jasperDesign, destFileName);
		
		return destFileName;
	}


	/**
	 *
	 */
	public static void compileReportToFile(
		String sourceFileName,
		String destFileName
		) throws JRException
	{
		JasperDesign jasperDesign = JRXmlLoader.load(sourceFileName);

		compileReportToFile(jasperDesign, destFileName);
	}


	/**
	 *
	 */
	public static void compileReportToFile(
		JasperDesign jasperDesign,
		String destFileName
		) throws JRException
	{
		JasperReport jasperReport = new JRDefaultCompiler().compileReport(jasperDesign);

		JRSaver.saveObject(jasperReport, destFileName);
	}


	/**
	 *
	 */
	public static JasperReport compileReport(String sourceFileName) throws JRException
	{
		JasperDesign jasperDesign = JRXmlLoader.load(sourceFileName);

		return compileReport(jasperDesign);
	}


	/**
	 *
	 */
	public static void compileReportToStream(
		InputStream inputStream,
		OutputStream outputStream
		) throws JRException
	{
		JasperDesign jasperDesign = JRXmlLoader.load(inputStream);

		compileReportToStream(jasperDesign, outputStream);
	}


	/**
	 *
	 */
	public static void compileReportToStream(
		JasperDesign jasperDesign,
		OutputStream outputStream
		) throws JRException
	{
		JasperReport jasperReport = new JRDefaultCompiler().compileReport(jasperDesign);

		JRSaver.saveObject(jasperReport, outputStream);
	}


	/**
	 *
	 */
	public static JasperReport compileReport(InputStream inputStream) throws JRException
	{
		JasperDesign jasperDesign = JRXmlLoader.load(inputStream);

		return compileReport(jasperDesign);
	}


	/**
	 *
	 */
	public static JasperReport compileReport(JasperDesign jasperDesign) throws JRException
	{
		return new JRDefaultCompiler().compileReport(jasperDesign);
	}


	/**
	 *
	 */
	public static Collection verifyDesign(JasperDesign jasperDesign) throws JRException
	{
		return JRVerifier.verifyDesign(jasperDesign);
	}


	/**
	 *
	 */
	public static String writeReportToXmlFile(
		String sourceFileName
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		/* We need the report name. */
		JRReport report = (JRReport)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), report.getName() + ".jasper.xml");
		String destFileName = destFile.toString();
		
		writeReportToXmlFile(
			report, 
			destFileName
			);
		
		return destFileName;
	}


	/**
	 *
	 */
	public static void writeReportToXmlFile(
		String sourceFileName, 
		String destFileName
		) throws JRException
	{
		JRReport report = (JRReport)JRLoader.loadObject(sourceFileName);

		writeReportToXmlFile(
			report, 
			destFileName
			);
	}

	
	/**
	 *
	 */
	public static void writeReportToXmlFile(
		JRReport report,
		String destFileName
		) throws JRException
	{
		JRXmlWriter.writeReport(
			report,
			destFileName,
			"UTF-8"
			);
	}


	/**
	 *
	 */
	public static void writeReportToXmlStream(
		InputStream inputStream, 
		OutputStream outputStream
		) throws JRException
	{
		JRReport report = (JRReport)JRLoader.loadObject(inputStream);

		writeReportToXmlStream(report, outputStream);
	}

	
	/**
	 *
	 */
	public static void writeReportToXmlStream(
		JRReport report, 
		OutputStream outputStream
		) throws JRException
	{
		JRXmlWriter.writeReport(
			report, 
			outputStream,
			"UTF-8"
			);
	}


	/**
	 *
	 */
	public static String writeReportToXml(JRReport report) throws JRException
	{
		return JRXmlWriter.writeReport(report, "UTF-8");
	}


}

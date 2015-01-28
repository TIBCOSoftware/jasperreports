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
package net.sf.jasperreports.virtualization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import net.sf.jasperreports.data.BuiltinDataFileServiceFactory;
import net.sf.jasperreports.data.DataAdapterParameterContributorFactory;
import net.sf.jasperreports.data.DataFileServiceFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.ParameterContributorFactory;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ReportTest
{

	private static final Log log = LogFactory.getLog(ReportTest.class);
	
	private final String DIGEST = "ece1c2b966beca403f642d6e0f72d5d460852554";
	
	private JasperReport report;
	private JasperFillManager fillManager;

	@BeforeClass
	public void compileReport() throws JRException, IOException
	{
		InputStream jrxmlInput = JRLoader.getResourceInputStream("net/sf/jasperreports/virtualization/repo/FirstJasper.jrxml");
		JasperDesign design;
		try
		{
			design = JRXmlLoader.load(jrxmlInput);
		}
		finally
		{
			jrxmlInput.close();
		}
		
		report = JasperCompileManager.compileReport(design);
		
		SimpleJasperReportsContext jasperReportsContext = new SimpleJasperReportsContext();
		// for some reason data adapter extensions are not registered by default
		jasperReportsContext.setExtensions(ParameterContributorFactory.class, 
				Collections.singletonList(DataAdapterParameterContributorFactory.getInstance()));
		jasperReportsContext.setExtensions(DataFileServiceFactory.class, 
				Collections.singletonList(BuiltinDataFileServiceFactory.instance()));
		
		fillManager = JasperFillManager.getInstance(jasperReportsContext);
	}
	
	@Test
	public void baseReport() throws JRException, NoSuchAlgorithmException, IOException
	{
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(JRParameter.REPORT_LOCALE, Locale.US);
		params.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone("GMT"));
		JasperPrint print = fillManager.fill(report, params);
		assert !print.getPages().isEmpty();
		
		String digestString = xmlDigest(print);
		log.debug("Plain report got " + digestString);
		assert digestString.equals(DIGEST);
	}
	
	@Test
	public void virtualizedReport() throws JRException, NoSuchAlgorithmException, IOException
	{
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(JRParameter.REPORT_LOCALE, Locale.US);
		params.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone("GMT"));
		
		JRGzipVirtualizer virtualizer = new JRGzipVirtualizer(3);
		params.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		
		JasperPrint print = fillManager.fill(report, params);
		virtualizer.setReadOnly(true);
		assert !print.getPages().isEmpty();
		
		String digestString = xmlDigest(print);
		log.debug("Virtualized report got " + digestString);
		assert digestString.equals(DIGEST);
	}

	protected String xmlDigest(JasperPrint print) 
			throws NoSuchAlgorithmException, FileNotFoundException, JRException, IOException
	{
		File outputFile = createXmlOutputFile();
		log.debug("XML export output at " + outputFile.getAbsolutePath());
		
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		FileOutputStream output = new FileOutputStream(outputFile);
		try
		{
			DigestOutputStream out = new DigestOutputStream(output, digest);
			xmlExport(print, out);
		}
		finally
		{
			output.close();
		}
		
		byte[] digestBytes = digest.digest();
		StringBuilder digestString = new StringBuilder(digestBytes.length * 2);
		for (byte b : digestBytes)
		{
			digestString.append(String.format("%02x", b));
		}
		return digestString.toString();
	}
	
	protected File createXmlOutputFile() throws IOException
	{
		String outputDirPath = System.getProperty("xmlOutputDir");
		File outputFile;
		if (outputDirPath == null)
		{
			outputFile = File.createTempFile("jr_tests_", ".jrpxml");
		}
		else
		{
			File outputDir = new File(outputDirPath);
			outputFile = File.createTempFile("jr_tests_", ".jrpxml", outputDir);
		}
		return outputFile;
	}

	protected void xmlExport(JasperPrint print, OutputStream out) throws JRException, IOException
	{
		JRXmlExporter exporter = new JRXmlExporter();
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRXmlExporterParameter.IS_EMBEDDING_IMAGES, true);
		exporter.exportReport();
		out.close();
	}
}

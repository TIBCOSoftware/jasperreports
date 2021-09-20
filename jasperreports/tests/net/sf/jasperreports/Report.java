/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports;

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
import java.util.Map;
import java.util.TimeZone;
import java.util.function.BiConsumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.data.BuiltinDataFileServiceFactory;
import net.sf.jasperreports.data.DataAdapterParameterContributorFactory;
import net.sf.jasperreports.data.DataFileServiceFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.ParameterContributorFactory;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleXmlExporterOutput;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class Report
{

	private static final Log log = LogFactory.getLog(Report.class);
	
	private String jrxml;
	private String jrpxml;
	private BiConsumer<Report, JasperPrint> printConsumer = Report::checkDigest;
	
	public Report(String basename)
	{
		this(basename + ".jrxml", basename + ".jrpxml");
	}
	
	public Report(String jrxml, String jrpxml)
	{
		this.jrxml = jrxml;
		this.jrpxml = jrpxml;
	}
	
	protected SimpleJasperReportsContext jasperReportsContext;
	protected JasperReport report;
	private JasperFillManager fillManager;
	private String referenceJRPXMLDigest;

	public void init()
	{
		jasperReportsContext = new SimpleJasperReportsContext();
		// for some reason data adapter extensions are not registered by default
		jasperReportsContext.setExtensions(ParameterContributorFactory.class, 
				Collections.singletonList(DataAdapterParameterContributorFactory.getInstance()));
		jasperReportsContext.setExtensions(DataFileServiceFactory.class, 
				Collections.singletonList(BuiltinDataFileServiceFactory.instance()));
		
		try
		{
			compileReport();
			readReferenceDigest();
		}
		catch (JRException | IOException | NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public String getJRXML()
	{
		return jrxml;
	}
	
	public void addPrintConsumer(BiConsumer<Report, JasperPrint> printConsumer)
	{
		this.printConsumer = this.printConsumer.andThen(printConsumer);
	}
	
	public JasperReport compileReport() throws JRException, IOException
	{
		InputStream jrxmlInput = JRLoader.getResourceInputStream(jrxml);
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
		
		//TODO do we need this here?
		fillManager = JasperFillManager.getInstance(jasperReportsContext);
		
		return report;
	}

	protected void readReferenceDigest() throws JRException, NoSuchAlgorithmException
	{
		byte[] jrpxmlBytes = JRLoader.loadBytesFromResource(jrpxml);
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.update(jrpxmlBytes);
		referenceJRPXMLDigest = toDigestString(digest);
		log.debug("Reference report digest for " + jrpxml + " is " + referenceJRPXMLDigest);
	}
	
	public void runReport(Map<String, Object> params)
	{
		Map<String, Object> reportParams = reportParams(params);
		try
		{
			JasperPrint print = fillManager.fill(report, reportParams);
			reportComplete(reportParams, print);
		}
		catch (JRException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected Map<String, Object> reportParams(Map<String, Object> params)
	{
		if (params == null)
		{
			params = new HashMap<String, Object>();
		}
		params.put(JRParameter.REPORT_LOCALE, Locale.US);
		params.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone("GMT"));
		return params;
	}

	protected void reportComplete(Map<String, Object> params, JasperPrint print)
	{
		JRVirtualizer virtualizer = (JRVirtualizer) params.get(JRParameter.REPORT_VIRTUALIZER);
		if (virtualizer instanceof JRAbstractLRUVirtualizer)
		{
			((JRAbstractLRUVirtualizer) virtualizer).setReadOnly(true);
		}
		
		assert !print.getPages().isEmpty();
		
		printConsumer.accept(this, print);
		
		if (virtualizer != null)
		{
			virtualizer.cleanup();
		}
	}

	public void checkDigest(JasperPrint print)
	{
		try
		{
			String digestString = xmlDigest(print);
			log.debug("Report " + jrxml + " got " + digestString);
			assert digestString.equals(referenceJRPXMLDigest);
		} 
		catch (NoSuchAlgorithmException | JRException | IOException e)
		{
			throw new RuntimeException(e);
		}
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
		
		return toDigestString(digest);
	}

	protected String toDigestString(MessageDigest digest)
	{
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
		outputFile.deleteOnExit();
		return outputFile;
	}

	protected void xmlExport(JasperPrint print, OutputStream out) throws JRException, IOException
	{
		JRXmlExporter exporter = new JRXmlExporter();
		exporter.setExporterInput(new SimpleExporterInput(print));
		SimpleXmlExporterOutput output = new SimpleXmlExporterOutput(out);
		output.setEmbeddingImages(true);
		exporter.setExporterOutput(output);
		exporter.exportReport();
		out.close();
	}

}

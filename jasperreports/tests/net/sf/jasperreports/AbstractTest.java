/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.BeforeClass;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.XmlResourceHandler;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRResourcesUtil;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleXmlExporterOutput;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractTest
{
	private static final Log log = LogFactory.getLog(AbstractTest.class);
	
	private static final String TEST = "TEST";

	private JasperReportsContext jasperReportsContext;

	@BeforeClass
	public void init() throws JRException, IOException
	{
		jasperReportsContext = new SimpleJasperReportsContext();
	}

	protected Object[][] runReportArgs(String folderName, String fileNamePrefix, int maxFileNumber)
	{
		return runReportArgs(folderName, fileNamePrefix, fileNamePrefix, maxFileNumber);
	}

	protected Object[][] runReportArgs(String folderName, String fileNamePrefix, String exportFileNamePrefix, int maxFileNumber)
	{
		Object[][] args = new Object[maxFileNumber][];
		for (int i = 1; i <= maxFileNumber; i++)
		{
			String jrxmlFileName = folderName + "/" + fileNamePrefix + "." + i + ".jrxml";
			String referenceFileNamePrefix = folderName + "/" + exportFileNamePrefix + "." + i + ".reference";
			args[i - 1] = new Object[] {jrxmlFileName, referenceFileNamePrefix};
		}
		return args;
	}

	protected void runReport(String jrxmlFileName, String referenceFileNamePrefix) 
			throws JRException, IOException, NoSuchAlgorithmException, FileNotFoundException
	{
		JasperFillManager fillManager = JasperFillManager.getInstance(getJasperReportsContext());
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(JRParameter.REPORT_LOCALE, Locale.US);
		params.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone("GMT"));
		params.put(TEST, this);
		
		log.debug("Running report " + jrxmlFileName);
		
		try
		{
			JasperReport report = compileReport(jrxmlFileName);
			if (report != null)
			{
				JasperPrint print = fillManager.fill(report, params);
				
				assert !print.getPages().isEmpty();
				
				String exportDigest = xmlExportDigest(print);
				log.debug("Plain report got " + exportDigest);
				
				String referenceExportDigest = getFileDigest(referenceFileNamePrefix + ".jrpxml");
				assert exportDigest.equals(referenceExportDigest);
			}
		}
		catch (Throwable t)
		{
			String errorDigest = errExportDigest(t);
			String referenceErrorDigest = getFileDigest(referenceFileNamePrefix + ".err");
			if (referenceErrorDigest == null)
			{
				log.error("Report " + jrxmlFileName + " failed", t);
				//we don't have a reference error, it's an unexpected exception
				throw t;
			}
			
			assert errorDigest.equals(referenceErrorDigest);
		}
	}

	protected JasperReportsContext getJasperReportsContext()
	{
		return jasperReportsContext;
	}

	protected void setJasperReportsContext(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}

	/**
	 * This method is used for compiling subreports.
	 */
	public JasperReport compileReport(String jrxmlFileName) throws JRException, IOException
	{
		JasperReport jasperReport = null;
		
		InputStream jrxmlInput = JRLoader.getResourceInputStream(jrxmlFileName);

		if (jrxmlInput != null)
		{
			JasperDesign design;
			try
			{
				design = JRXmlLoader.load(jrxmlInput);
			}
			finally
			{
				jrxmlInput.close();
			}
			jasperReport = JasperCompileManager.compileReport(design);
		}
		
		return jasperReport;
	}

	protected JasperReport compileReport(File jrxmlFile) throws JRException, IOException
	{
		JasperDesign design = JRXmlLoader.load(jrxmlFile);
		
		return JasperCompileManager.compileReport(design);
	}

	protected String getFileDigest(String fileName) throws JRException, NoSuchAlgorithmException
	{
		URL resourceURL = JRResourcesUtil.findClassLoaderResource(fileName, null);
		if (resourceURL == null)
		{
			log.debug("did not find resource " + fileName);
			return null;
		}
		
		byte[] bytes = JRLoader.loadBytes(resourceURL);
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		messageDigest.update(bytes);
		String digest = toDigestString(messageDigest);
		log.debug("Reference report digest is " + digest + " for " + fileName);
		return digest;
	}
	
	protected String xmlExportDigest(JasperPrint print) 
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

	protected String errExportDigest(Throwable t) 
			throws NoSuchAlgorithmException, FileNotFoundException, JRException, IOException
	{
		File outputFile = createXmlOutputFile();
		log.debug("Error stack trace at " + outputFile.getAbsolutePath());
		
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		FileOutputStream output = new FileOutputStream(outputFile);
		OutputStreamWriter osw = null;
		try
		{
			DigestOutputStream out = new DigestOutputStream(output, digest);
			//PrintStream ps = new PrintStream(out);
			//t.printStackTrace(ps);
			osw = new OutputStreamWriter(out, "UTF-8");
			osw.write(String.valueOf(t.getMessage()));
		}
		finally
		{
			osw.close();
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
		
		return outputFile;
	}

	protected void xmlExport(JasperPrint print, OutputStream out) throws JRException, IOException
	{
		JRXmlExporter exporter = new JRXmlExporter();
		
		exporter.setExporterInput(new SimpleExporterInput(print));
		SimpleXmlExporterOutput output = new SimpleXmlExporterOutput(out);
		String imagesNoEmbed = print.getProperty("net.sf.jasperreports.export.xml.images.no.embed");
		if (Boolean.valueOf(imagesNoEmbed))
		{
			output.setEmbeddingImages(false);
			output.setImageHandler(new XmlResourceHandler() {

				@Override
				public void handleResource(String id, byte[] data) {
				}
				
				@Override
				public String getResourceSource(String id) {
					return id;
				}
			});
		}
		else
		{
			output.setEmbeddingImages(true);
		}
		exporter.setExporterOutput(output);
		exporter.exportReport();
		out.close();
	}
}

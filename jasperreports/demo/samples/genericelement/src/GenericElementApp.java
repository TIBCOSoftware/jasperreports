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

import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class GenericElementApp extends AbstractSampleApp
{


	/**
	 *
	 */
	public static void main(String[] args)
	{
		main(new GenericElementApp(), args);
	}
	
	
	/**
	 *
	 */
	public void test() throws JRException
	{
		fill();
		xmlEmbed();
		xml();
		html();
		xhtml();
	}


	/**
	 *
	 */
	public void fill() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperFillManager.fillReportToFile("build/reports/GenericElementReport.jasper", null);
		System.err.println("Filling time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void xml() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToXmlFile("build/reports/GenericElementReport.jrprint", false);
		System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void xmlEmbed() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToXmlFile("build/reports/GenericElementReport.jrprint", true);
		System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void html() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToHtmlFile("build/reports/GenericElementReport.jrprint");
		System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	public void xhtml() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/GenericElementReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".x.html");
		
		net.sf.jasperreports.engine.export.JRXhtmlExporter exporter = 
			new net.sf.jasperreports.engine.export.JRXhtmlExporter();
		
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleHtmlExporterOutput(destFile));
		
		exporter.exportReport();

		System.err.println("XHTML creation time : " + (System.currentTimeMillis() - start));
	}
	
	
}

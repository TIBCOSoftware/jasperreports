/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;

import com.lowagie.text.pdf.PdfWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: PdfEncryptApp.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class PdfEncryptApp extends AbstractSampleApp
{


	/**
	 *
	 */
	public static void main(String[] args)
	{
		main(new PdfEncryptApp(), args);
	}
	
	
	/**
	 *
	 */
	public void test() throws JRException
	{
		fill();
		pdf();
	}


	/**
	 *
	 */
	public void fill() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperFillManager.fillReportToFile("build/reports/PdfEncryptReport.jasper", null, new JREmptyDataSource());
		System.err.println("Filling time : " + (System.currentTimeMillis() - start));
	}


	/**
	 *
	 */
	public void pdf() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/PdfEncryptReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".pdf");
		
		JRPdfExporter exporter = new JRPdfExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE, destFile);
		exporter.setParameter(JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE);
		exporter.setParameter(JRPdfExporterParameter.IS_128_BIT_KEY, Boolean.TRUE);
		exporter.setParameter(JRPdfExporterParameter.USER_PASSWORD, "jasper");
		exporter.setParameter(JRPdfExporterParameter.OWNER_PASSWORD, "reports");
		exporter.setParameter(
			JRPdfExporterParameter.PERMISSIONS, 
			new Integer(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING)
			);
		
		exporter.exportReport();

		System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
	}


}

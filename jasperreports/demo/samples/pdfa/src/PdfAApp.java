/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class PdfAApp extends AbstractSampleApp
{


	/**
	 *
	 */
	public static void main(String[] args)
	{
		main(new PdfAApp(), args);
	}
	
	
	@Override
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
		JasperFillManager.fillReportToFile("build/reports/PdfAReport.jasper", null, new JREmptyDataSource());
		System.err.println("Filling time : " + (System.currentTimeMillis() - start));
	}


	/**
	 *
	 */
	public void pdf() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/PdfAReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".pdf");
		
		JRPdfExporter exporter = new JRPdfExporter();
		
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
                
                configuration.setTagged(true);
                configuration.setCompressed(false);
                configuration.setMetadataTitle("Some title");
                configuration.setMetadataAuthor("Some author");
                configuration.setMetadataSubject("Some subject");
                configuration.setMetadataKeywords("The first keyword, the second keyword");
                configuration.setMetadataCreator("Some author");
                configuration.setEmbedIccProfile(Boolean.TRUE);
                configuration.setIccProfilePath("./sRGB_IEC61966-2-1_no_black_scaling.icc");
//                configuration.setPdfVersion(PdfVersionEnum.VERSION_1_4);
//                JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).setProperty("net.sf.jasperreports.export.pdf.glyph.renderer.add.actual.text", "false");
                configuration.setPdfaConformance(PdfaConformanceEnum.PDFA_2A);
                
		exporter.setConfiguration(configuration);
		exporter.exportReport();

		System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
	}


}

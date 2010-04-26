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
import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: NoXmlDesignApp.java 3420 2010-02-18 09:17:47Z teodord $
 */
public class NoXmlDesignApp extends AbstractSampleApp
{


	/**
	 *
	 */
	public static void main(String[] args) 
	{
		main(new NoXmlDesignApp(), args);
	}
	
	
	/**
	 *
	 */
	public void test() throws JRException
	{
		writeXml();
		fill();
		pdf();
		xmlEmbed();
		xml();
		html();
		rtf();
		xls();
		jxl();
		csv();
		odt();
		ods();
		docx();
		xlsx();
		pptx();
		xhtml();
	}
	
	
	/**
	 *
	 */
	public void compile() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperDesign jasperDesign = getJasperDesign();
		JasperCompileManager.compileReportToFile(jasperDesign, "build/reports/NoXmlDesignReport.jasper");
		System.err.println("Compile time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void fill() throws JRException
	{
		long start = System.currentTimeMillis();
		//Preparing parameters
		Map parameters = new HashMap();
		parameters.put("ReportTitle", "Address Report");
		parameters.put("OrderByClause", "ORDER BY City");

		JasperFillManager.fillReportToFile("build/reports/NoXmlDesignReport.jasper", parameters, getDemoHsqldbConnection());
		System.err.println("Filling time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void print() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperPrintManager.printReport("build/reports/NoXmlDesignReport.jrprint", true);
		System.err.println("Printing time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void pdf() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToPdfFile("build/reports/NoXmlDesignReport.jrprint");
		System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void rtf() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/NoXmlDesignReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
		
		JRRtfExporter exporter = new JRRtfExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		
		exporter.exportReport();

		System.err.println("RTF creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void xml() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToXmlFile("build/reports/NoXmlDesignReport.jrprint", false);
		System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void xmlEmbed() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToXmlFile("build/reports/NoXmlDesignReport.jrprint", true);
		System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void html() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToHtmlFile("build/reports/NoXmlDesignReport.jrprint");
		System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void xls() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/NoXmlDesignReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
		
		JRXlsExporter exporter = new JRXlsExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		
		exporter.exportReport();

		System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void jxl() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/NoXmlDesignReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".jxl.xls");

		JExcelApiExporter exporter = new JExcelApiExporter();

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);

		exporter.exportReport();

		System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void csv() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/NoXmlDesignReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");
		
		JRCsvExporter exporter = new JRCsvExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		
		exporter.exportReport();

		System.err.println("CSV creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void odt() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/NoXmlDesignReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".odt");
		
		JROdtExporter exporter = new JROdtExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		
		exporter.exportReport();

		System.err.println("ODT creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void ods() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/NoXmlDesignReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".ods");
		
		JROdsExporter exporter = new JROdsExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
		
		exporter.exportReport();

		System.err.println("ODS creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void docx() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/NoXmlDesignReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".docx");
		
		JRDocxExporter exporter = new JRDocxExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		
		exporter.exportReport();

		System.err.println("DOCX creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void xlsx() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/NoXmlDesignReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xlsx");
		
		JRXlsxExporter exporter = new JRXlsxExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		
		exporter.exportReport();

		System.err.println("XLSX creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void pptx() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/NoXmlDesignReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".pptx");
		
		JRPptxExporter exporter = new JRPptxExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());

		exporter.exportReport();

		System.err.println("PPTX creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void xhtml() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("build/reports/NoXmlDesignReport.jrprint");

		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".x.html");
		
		JRXhtmlExporter exporter = new JRXhtmlExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		
		exporter.exportReport();

		System.err.println("XHTML creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void writeXml() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperCompileManager.writeReportToXmlFile("build/reports/NoXmlDesignReport.jasper");
		System.err.println("XML design creation time : " + (System.currentTimeMillis() - start));
	}


	/**
	 *
	 */
	private static JasperDesign getJasperDesign() throws JRException
	{
		//JasperDesign
		JasperDesign jasperDesign = new JasperDesign();
		jasperDesign.setName("NoXmlDesignReport");
		jasperDesign.setPageWidth(595);
		jasperDesign.setPageHeight(842);
		jasperDesign.setColumnWidth(515);
		jasperDesign.setColumnSpacing(0);
		jasperDesign.setLeftMargin(40);
		jasperDesign.setRightMargin(40);
		jasperDesign.setTopMargin(50);
		jasperDesign.setBottomMargin(50);
		
		//Fonts
		JRDesignStyle normalStyle = new JRDesignStyle();
		normalStyle.setName("Sans_Normal");
		normalStyle.setDefault(true);
		normalStyle.setFontName("DejaVu Sans");
		normalStyle.setFontSize(12);
		normalStyle.setPdfFontName("Helvetica");
		normalStyle.setPdfEncoding("Cp1252");
		normalStyle.setPdfEmbedded(false);
		jasperDesign.addStyle(normalStyle);

		JRDesignStyle boldStyle = new JRDesignStyle();
		boldStyle.setName("Sans_Bold");
		boldStyle.setFontName("DejaVu Sans");
		boldStyle.setFontSize(12);
		boldStyle.setBold(true);
		boldStyle.setPdfFontName("Helvetica-Bold");
		boldStyle.setPdfEncoding("Cp1252");
		boldStyle.setPdfEmbedded(false);
		jasperDesign.addStyle(boldStyle);

		JRDesignStyle italicStyle = new JRDesignStyle();
		italicStyle.setName("Sans_Italic");
		italicStyle.setFontName("DejaVu Sans");
		italicStyle.setFontSize(12);
		italicStyle.setItalic(true);
		italicStyle.setPdfFontName("Helvetica-Oblique");
		italicStyle.setPdfEncoding("Cp1252");
		italicStyle.setPdfEmbedded(false);
		jasperDesign.addStyle(italicStyle);
		
		//Parameters
		JRDesignParameter parameter = new JRDesignParameter();
		parameter.setName("ReportTitle");
		parameter.setValueClass(java.lang.String.class);
		jasperDesign.addParameter(parameter);

		parameter = new JRDesignParameter();
		parameter.setName("OrderByClause");
		parameter.setValueClass(java.lang.String.class);
		jasperDesign.addParameter(parameter);

		//Query
		JRDesignQuery query = new JRDesignQuery();
		query.setText("SELECT * FROM Address $P!{OrderByClause}");
		jasperDesign.setQuery(query);

		//Fields
		JRDesignField field = new JRDesignField();
		field.setName("Id");
		field.setValueClass(java.lang.Integer.class);
		jasperDesign.addField(field);

		field = new JRDesignField();
		field.setName("FirstName");
		field.setValueClass(java.lang.String.class);
		jasperDesign.addField(field);

		field = new JRDesignField();
		field.setName("LastName");
		field.setValueClass(java.lang.String.class);
		jasperDesign.addField(field);

		field = new JRDesignField();
		field.setName("Street");
		field.setValueClass(java.lang.String.class);
		jasperDesign.addField(field);

		field = new JRDesignField();
		field.setName("City");
		field.setValueClass(java.lang.String.class);
		jasperDesign.addField(field);

		//Variables
		JRDesignVariable variable = new JRDesignVariable();
		variable.setName("CityNumber");
		variable.setValueClass(java.lang.Integer.class);
		variable.setResetType(ResetTypeEnum.GROUP);
		JRDesignGroup group = new JRDesignGroup();
		group.setName("CityGroup");
		variable.setResetGroup(group);
		variable.setCalculation(CalculationEnum.SYSTEM);
		JRDesignExpression expression = new JRDesignExpression();
		expression.setValueClass(java.lang.Integer.class);
		expression.setText("($V{CityNumber} != null)?(new Integer($V{CityNumber}.intValue() + 1)):(new Integer(1))");
		variable.setInitialValueExpression(expression);
		jasperDesign.addVariable(variable);

		variable = new JRDesignVariable();
		variable.setName("AllCities");
		variable.setValueClass(java.lang.String.class);
		variable.setResetType(ResetTypeEnum.REPORT);
		variable.setCalculation(CalculationEnum.SYSTEM);
		jasperDesign.addVariable(variable);

		//Groups
		group.setMinHeightToStartNewPage(60);
		expression = new JRDesignExpression();
		expression.setValueClass(java.lang.String.class);
		expression.setText("$F{City}");
		group.setExpression(expression);

		JRDesignBand band = new JRDesignBand();
		band.setHeight(20);
		JRDesignTextField textField = new JRDesignTextField();
		textField.setX(0);
		textField.setY(4);
		textField.setWidth(515);
		textField.setHeight(15);
		textField.setBackcolor(new Color(0xC0, 0xC0, 0xC0));
		textField.setMode(ModeEnum.OPAQUE);
		textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
		textField.setStyle(boldStyle);
		expression = new JRDesignExpression();
		expression.setValueClass(java.lang.String.class);
		expression.setText("\"  \" + String.valueOf($V{CityNumber}) + \". \" + String.valueOf($F{City})");
		textField.setExpression(expression);
		band.addElement(textField);
		JRDesignLine line = new JRDesignLine();
		line.setX(0);
		line.setY(19);
		line.setWidth(515);
		line.setHeight(0);
		band.addElement(line);
		((JRDesignSection)group.getGroupHeaderSection()).addBand(band);

		band = new JRDesignBand();
		band.setHeight(20);
		line = new JRDesignLine();
		line.setX(0);
		line.setY(-1);
		line.setWidth(515);
		line.setHeight(0);
		band.addElement(line);
		JRDesignStaticText staticText = new JRDesignStaticText();
		staticText.setX(400);
		staticText.setY(0);
		staticText.setWidth(60);
		staticText.setHeight(15);
		staticText.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
		staticText.setStyle(boldStyle);
		staticText.setText("Count : ");
		band.addElement(staticText);
		textField = new JRDesignTextField();
		textField.setX(460);
		textField.setY(0);
		textField.setWidth(30);
		textField.setHeight(15);
		textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
		textField.setStyle(boldStyle);
		expression = new JRDesignExpression();
		expression.setValueClass(java.lang.Integer.class);
		expression.setText("$V{CityGroup_COUNT}");
		textField.setExpression(expression);
		band.addElement(textField);
		((JRDesignSection)group.getGroupFooterSection()).addBand(band);

		jasperDesign.addGroup(group);

		//Title
		band = new JRDesignBand();
		band.setHeight(50);
		line = new JRDesignLine();
		line.setX(0);
		line.setY(0);
		line.setWidth(515);
		line.setHeight(0);
		band.addElement(line);
		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(0);
		textField.setY(10);
		textField.setWidth(515);
		textField.setHeight(30);
		textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		textField.setStyle(normalStyle);
		textField.setFontSize(22);
		expression = new JRDesignExpression();
		expression.setValueClass(java.lang.String.class);
		expression.setText("$P{ReportTitle}");
		textField.setExpression(expression);
		band.addElement(textField);
		jasperDesign.setTitle(band);
		
		//Page header
		band = new JRDesignBand();
		band.setHeight(20);
		JRDesignFrame frame = new JRDesignFrame();
		frame.setX(0);
		frame.setY(5);
		frame.setWidth(515);
		frame.setHeight(15);
		frame.setForecolor(new Color(0x33, 0x33, 0x33));
		frame.setBackcolor(new Color(0x33, 0x33, 0x33));
		frame.setMode(ModeEnum.OPAQUE);
		band.addElement(frame);
		staticText = new JRDesignStaticText();
		staticText.setX(0);
		staticText.setY(0);
		staticText.setWidth(55);
		staticText.setHeight(15);
		staticText.setForecolor(Color.white);
		staticText.setBackcolor(new Color(0x33, 0x33, 0x33));
		staticText.setMode(ModeEnum.OPAQUE);
		staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		staticText.setStyle(boldStyle);
		staticText.setText("ID");
		frame.addElement(staticText);
		staticText = new JRDesignStaticText();
		staticText.setX(55);
		staticText.setY(0);
		staticText.setWidth(205);
		staticText.setHeight(15);
		staticText.setForecolor(Color.white);
		staticText.setBackcolor(new Color(0x33, 0x33, 0x33));
		staticText.setMode(ModeEnum.OPAQUE);
		staticText.setStyle(boldStyle);
		staticText.setText("Name");
		frame.addElement(staticText);
		staticText = new JRDesignStaticText();
		staticText.setX(260);
		staticText.setY(0);
		staticText.setWidth(255);
		staticText.setHeight(15);
		staticText.setForecolor(Color.white);
		staticText.setBackcolor(new Color(0x33, 0x33, 0x33));
		staticText.setMode(ModeEnum.OPAQUE);
		staticText.setStyle(boldStyle);
		staticText.setText("Street");
		frame.addElement(staticText);
		jasperDesign.setPageHeader(band);

		//Column header
		band = new JRDesignBand();
		jasperDesign.setColumnHeader(band);

		//Detail
		band = new JRDesignBand();
		band.setHeight(20);
		textField = new JRDesignTextField();
		textField.setX(0);
		textField.setY(4);
		textField.setWidth(50);
		textField.setHeight(15);
		textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
		textField.setStyle(normalStyle);
		expression = new JRDesignExpression();
		expression.setValueClass(java.lang.Integer.class);
		expression.setText("$F{Id}");
		textField.setExpression(expression);
		band.addElement(textField);
		textField = new JRDesignTextField();
		textField.setStretchWithOverflow(true);
		textField.setX(55);
		textField.setY(4);
		textField.setWidth(200);
		textField.setHeight(15);
		textField.setPositionType(PositionTypeEnum.FLOAT);
		textField.setStyle(normalStyle);
		expression = new JRDesignExpression();
		expression.setValueClass(java.lang.String.class);
		expression.setText("$F{FirstName} + \" \" + $F{LastName}");
		textField.setExpression(expression);
		band.addElement(textField);
		textField = new JRDesignTextField();
		textField.setStretchWithOverflow(true);
		textField.setX(260);
		textField.setY(4);
		textField.setWidth(255);
		textField.setHeight(15);
		textField.setPositionType(PositionTypeEnum.FLOAT);
		textField.setStyle(normalStyle);
		expression = new JRDesignExpression();
		expression.setValueClass(java.lang.String.class);
		expression.setText("$F{Street}");
		textField.setExpression(expression);
		band.addElement(textField);
		line = new JRDesignLine();
		line.setX(0);
		line.setY(19);
		line.setWidth(515);
		line.setHeight(0);
		line.setForecolor(new Color(0x80, 0x80, 0x80));
		line.setPositionType(PositionTypeEnum.FLOAT);
		band.addElement(line);
		((JRDesignSection)jasperDesign.getDetailSection()).addBand(band);

		//Column footer
		band = new JRDesignBand();
		jasperDesign.setColumnFooter(band);

		//Page footer
		band = new JRDesignBand();
		jasperDesign.setPageFooter(band);

		//Summary
		band = new JRDesignBand();
		jasperDesign.setSummary(band);
		
		return jasperDesign;
	}
	

}

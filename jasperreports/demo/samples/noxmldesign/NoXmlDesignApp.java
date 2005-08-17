/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
import java.awt.Color;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JRDesignReportFont;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class NoXmlDesignApp
{


	/**
	 *
	 */
	private static final String TASK_COMPILE = "compile";
	private static final String TASK_FILL = "fill";
	private static final String TASK_PRINT = "print";
	private static final String TASK_PDF = "pdf";
	private static final String TASK_RTF = "rtf";
	private static final String TASK_XML = "xml";
	private static final String TASK_XML_EMBED = "xmlEmbed";
	private static final String TASK_HTML = "html";
	private static final String TASK_XLS = "xls";
	private static final String TASK_CSV = "csv";
	private static final String TASK_RUN = "run";
	private static final String TASK_WRITE_XML = "writeXml";
	
	
	/**
	 *
	 */
	public static void main(String[] args)
	{
		String fileName = null;
		String taskName = null;

		if(args.length == 0)
		{
			usage();
			return;
		}
				
		int k = 0;
		while ( args.length > k )
		{
			if ( args[k].startsWith("-T") )
				taskName = args[k].substring(2);
			if ( args[k].startsWith("-F") )
				fileName = args[k].substring(2);
			
			k++;	
		}

		try
		{
			long start = System.currentTimeMillis();
			if (TASK_COMPILE.equals(taskName))
			{
				JasperDesign jasperDesign = getJasperDesign();
				JasperCompileManager.compileReportToFile(jasperDesign, fileName);
				System.err.println("Compile time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_FILL.equals(taskName))
			{
				//Preparing parameters
				Map parameters = new HashMap();
				parameters.put("ReportTitle", "Address Report");
				parameters.put("OrderByClause", "ORDER BY City");

				JasperFillManager.fillReportToFile(fileName, parameters, getConnection());
				System.err.println("Filling time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_PRINT.equals(taskName))
			{
				JasperPrintManager.printReport(fileName, true);
				System.err.println("Printing time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_PDF.equals(taskName))
			{
				JasperExportManager.exportReportToPdfFile(fileName);
				System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_RTF.equals(taskName))
			{
				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
				
				JRRtfExporter exporter = new JRRtfExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
				exporter.exportReport();

				System.err.println("RTF creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_XML.equals(taskName))
			{
				JasperExportManager.exportReportToXmlFile(fileName, false);
				System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_XML_EMBED.equals(taskName))
			{
				JasperExportManager.exportReportToXmlFile(fileName, true);
				System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_HTML.equals(taskName))
			{
				JasperExportManager.exportReportToHtmlFile(fileName);
				System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_XLS.equals(taskName))
			{
				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
				
				JRXlsExporter exporter = new JRXlsExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				
				exporter.exportReport();

				System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_CSV.equals(taskName))
			{
				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");
				
				JRCsvExporter exporter = new JRCsvExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
				exporter.exportReport();

				System.err.println("CSV creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_RUN.equals(taskName))
			{
				//Preparing parameters
				Map parameters = new HashMap();
				parameters.put("ReportTitle", "Address Report");
				parameters.put("OrderByClause", "ORDER BY City");
				
				JasperRunManager.runReportToPdfFile(fileName, parameters, getConnection());
				System.err.println("PDF running time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_WRITE_XML.equals(taskName))
			{
				JasperCompileManager.writeReportToXmlFile(fileName);
				System.err.println("XML design creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else
			{
				usage();
				System.exit(0);
			}
		}
		catch (JRException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}


	/**
	 *
	 */
	private static void usage()
	{
		System.out.println( "NoXmlDesignApp usage:" );
		System.out.println( "\tjava NoXmlDesignApp -Ttask -Ffile" );
		System.out.println( "\tTasks : compile | fill | print | pdf | xml | xmlEmbed | html | xls | csv | run | writeXml" );
	}


	/**
	 *
	 */
	private static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		//Change these settings according to your local configuration
		String driver = "org.hsqldb.jdbcDriver";
		String connectString = "jdbc:hsqldb:hsql://localhost";
		String user = "sa";
		String password = "";


		Class.forName(driver);
		Connection conn = DriverManager.getConnection(connectString, user, password);
		return conn;
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
		JRDesignReportFont normalFont = new JRDesignReportFont();
		normalFont.setName("Arial_Normal");
		normalFont.setDefault(true);
		normalFont.setFontName("Arial");
		normalFont.setSize(12);
		normalFont.setPdfFontName("Helvetica");
		normalFont.setPdfEncoding("Cp1252");
		normalFont.setPdfEmbedded(false);
		jasperDesign.addFont(normalFont);

		JRDesignReportFont boldFont = new JRDesignReportFont();
		boldFont.setName("Arial_Bold");
		boldFont.setDefault(false);
		boldFont.setFontName("Arial");
		boldFont.setSize(12);
		boldFont.setBold(true);
		boldFont.setPdfFontName("Helvetica-Bold");
		boldFont.setPdfEncoding("Cp1252");
		boldFont.setPdfEmbedded(false);
		jasperDesign.addFont(boldFont);

		JRDesignReportFont italicFont = new JRDesignReportFont();
		italicFont.setName("Arial_Italic");
		italicFont.setDefault(false);
		italicFont.setFontName("Arial");
		italicFont.setSize(12);
		italicFont.setItalic(true);
		italicFont.setPdfFontName("Helvetica-Oblique");
		italicFont.setPdfEncoding("Cp1252");
		italicFont.setPdfEmbedded(false);
		jasperDesign.addFont(italicFont);
		
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
		variable.setResetType(JRVariable.RESET_TYPE_GROUP);
		JRDesignGroup group = new JRDesignGroup();
		group.setName("CityGroup");
		variable.setResetGroup(group);
		variable.setCalculation(JRVariable.CALCULATION_SYSTEM);
		JRDesignExpression expression = new JRDesignExpression();
		expression.setValueClass(java.lang.Integer.class);
		expression.setText("($V{CityNumber} != null)?(new Integer($V{CityNumber}.intValue() + 1)):(new Integer(1))");
		variable.setInitialValueExpression(expression);
		jasperDesign.addVariable(variable);

		variable = new JRDesignVariable();
		variable.setName("AllCities");
		variable.setValueClass(java.lang.String.class);
		variable.setResetType(JRVariable.RESET_TYPE_REPORT);
		variable.setCalculation(JRVariable.CALCULATION_SYSTEM);
		jasperDesign.addVariable(variable);

		//Groups
		group.setMinHeightToStartNewPage(60);
		expression = new JRDesignExpression();
		expression.setValueClass(java.lang.String.class);
		expression.setText("$F{City}");
		group.setExpression(expression);

		JRDesignBand band = new JRDesignBand();
		band.setHeight(20);
		JRDesignRectangle rectangle = new JRDesignRectangle();
		rectangle.setX(0);
		rectangle.setY(4);
		rectangle.setWidth(515);
		rectangle.setHeight(15);
		rectangle.setForecolor(new Color(0xC0, 0xC0, 0xC0));
		rectangle.setBackcolor(new Color(0xC0, 0xC0, 0xC0));
		band.addElement(rectangle);
		JRDesignTextField textField = new JRDesignTextField();
		textField.setX(0);
		textField.setY(4);
		textField.setWidth(515);
		textField.setHeight(15);
		textField.setBackcolor(new Color(0xC0, 0xC0, 0xC0));
		textField.setMode(JRElement.MODE_OPAQUE);
		textField.setTextAlignment(JRTextElement.TEXT_ALIGN_LEFT);
		textField.setFont(boldFont);
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
		group.setGroupHeader(band);

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
		staticText.setTextAlignment(JRTextElement.TEXT_ALIGN_RIGHT);
		staticText.setFont(boldFont);
		staticText.setText("Count : ");
		band.addElement(staticText);
		textField = new JRDesignTextField();
		textField.setX(460);
		textField.setY(0);
		textField.setWidth(30);
		textField.setHeight(15);
		textField.setTextAlignment(JRTextElement.TEXT_ALIGN_RIGHT);
		textField.setFont(boldFont);
		expression = new JRDesignExpression();
		expression.setValueClass(java.lang.Integer.class);
		expression.setText("$V{CityGroup_COUNT}");
		textField.setExpression(expression);
		band.addElement(textField);
		group.setGroupFooter(band);

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
		textField.setTextAlignment(JRTextElement.TEXT_ALIGN_CENTER);
		JRDesignReportFont bigFont = new JRDesignReportFont();
		bigFont.setName("Arial_Normal");
		bigFont.setDefault(true);
		bigFont.setFontName("Arial");
		bigFont.setSize(22);
		bigFont.setPdfFontName("Helvetica");
		bigFont.setPdfEncoding("Cp1252");
		bigFont.setPdfEmbedded(false);
		textField.setFont(bigFont);
		expression = new JRDesignExpression();
		expression.setValueClass(java.lang.String.class);
		expression.setText("$P{ReportTitle}");
		textField.setExpression(expression);
		band.addElement(textField);
		jasperDesign.setTitle(band);
		
		//Page header
		band = new JRDesignBand();
		band.setHeight(20);
		rectangle = new JRDesignRectangle();
		rectangle.setX(0);
		rectangle.setY(5);
		rectangle.setWidth(515);
		rectangle.setHeight(15);
		rectangle.setForecolor(new Color(0x33, 0x33, 0x33));
		rectangle.setBackcolor(new Color(0x33, 0x33, 0x33));
		band.addElement(rectangle);
		staticText = new JRDesignStaticText();
		staticText.setX(0);
		staticText.setY(5);
		staticText.setWidth(55);
		staticText.setHeight(15);
		staticText.setForecolor(Color.white);
		staticText.setBackcolor(new Color(0x33, 0x33, 0x33));
		staticText.setMode(JRElement.MODE_OPAQUE);
		staticText.setTextAlignment(JRTextElement.TEXT_ALIGN_CENTER);
		staticText.setFont(boldFont);
		staticText.setText("ID");
		band.addElement(staticText);
		staticText = new JRDesignStaticText();
		staticText.setX(55);
		staticText.setY(5);
		staticText.setWidth(205);
		staticText.setHeight(15);
		staticText.setForecolor(Color.white);
		staticText.setBackcolor(new Color(0x33, 0x33, 0x33));
		staticText.setMode(JRElement.MODE_OPAQUE);
		staticText.setFont(boldFont);
		staticText.setText("Name");
		band.addElement(staticText);
		staticText = new JRDesignStaticText();
		staticText.setX(260);
		staticText.setY(5);
		staticText.setWidth(255);
		staticText.setHeight(15);
		staticText.setForecolor(Color.white);
		staticText.setBackcolor(new Color(0x33, 0x33, 0x33));
		staticText.setMode(JRElement.MODE_OPAQUE);
		staticText.setFont(boldFont);
		staticText.setText("Street");
		band.addElement(staticText);
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
		textField.setTextAlignment(JRTextElement.TEXT_ALIGN_RIGHT);
		textField.setFont(normalFont);
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
		textField.setPositionType(JRElement.POSITION_TYPE_FLOAT);
		textField.setFont(normalFont);
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
		textField.setPositionType(JRElement.POSITION_TYPE_FLOAT);
		textField.setFont(normalFont);
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
		line.setPositionType(JRElement.POSITION_TYPE_FLOAT);
		band.addElement(line);
		jasperDesign.setDetail(band);

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

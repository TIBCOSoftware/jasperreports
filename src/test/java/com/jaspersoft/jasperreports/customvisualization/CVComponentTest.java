/*******************************************************************************
 * Copyright (C) 2005 - 2016 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * The Custom Visualization Component program and the accompanying materials
 * has been dual licensed under the the following licenses:
 * 
 * Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Custom Visualization Component is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.jaspersoft.jasperreports.customvisualization;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

/**
 * Unit test for simple App.
 */
public class CVComponentTest extends TestCase
{
	static final String SCRIPT_PROPERTY_PREFIX = "com.jaspersoft.jasperreports.components.customvisualization.script.path.";

	JasperReportsContext context = null;
	private File outputDir;

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public CVComponentTest(String testName)
	{
		super(testName);

	}

	@Override
	protected void setUp() throws Exception
	{
		setupProperties();
		outputDir = new File("target/reports");
		if (!outputDir.exists())
		{
			outputDir.mkdir();
		}
	}

	/**
	 * Setup of the JasperReports properties for non interactive export
	 */
	private final void setupProperties() throws IOException
	{
		context = DefaultJasperReportsContext.getInstance();

		String currentDirectory = new File(".").getCanonicalPath();
		String scriptsDirectory = new File(".", "target/test-classes/scripts").getCanonicalPath();
		// String templatesDirectory = new File(".",
		// "target/test-classes/templates").getCanonicalPath();

		System.out.println("Current directory:   " + currentDirectory);
		// System.out.println("Scripts directory: " + scriptsDirectory );
		// System.out.println("Templates directory: " + templatesDirectory );
		System.out.println("Default encoding: " + Charset.defaultCharset().displayName());

		context.setProperty(CVConstants.CV_REQUIREJS_PROPERTY, "file://" + scriptsDirectory + "/require.js");
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite(CVComponentTest.class);
	}

	// public void testCircle() throws Exception
	// {
	// testReport("d3_Circle_sample.jrxml", false);
	// }

	public void testCircleWithGroups() throws Exception
	{
		// testReport("d3_Circle_sample_with_groups.jrxml", false);
		// testReport("subdataset_test_sample.jrxml", false);
		//testReport("sparkline_sample.jrxml", true);
	}

	// public void testDendogram() throws Exception
	// {
	// testReport("reports/d3_Dendogram.jrxml");
	// }
	//
	// public void testCirclePacking() throws Exception
	// {
	// testReport("reports/d3_Circle_Packing.jrxml");
	// }

	// public void testZoomableCirclePacking() throws Exception
	// {
	// testReport("reports/d3_Zoomable_Circle_Packing.jrxml");
	// }

	// public void testTreemap() throws Exception
	// {
	// testReport("reports/d3_Treemap.jrxml");
	// }
	//
	// public void testAnimatedSVG() throws Exception
	// {
	// testReport("reports/Animated_svg.jrxml");
	// }
	//
	// public void testEmptyComponent() throws Exception
	// {
	// testReport("reports/Empty_Component.jrxml");
	// }
	//
	// public void testHighChartsSparkline() throws Exception
	// {
	// testReport("reports/HighCharts_sparkline.jrxml");
	// }
	//
	// public void testHighChartsDonut() throws Exception
	// {
	// testReport("reports/HighCharts_donut.jrxml");
	// }
	//
	// public void testQRCode() throws Exception
	// {
	// testReport("reports/QR_Code.jrxml");
	// }
	//
	// public void testRaphaelJSDots() throws Exception
	// {
	// testReport("reports/RaphaelJS_dots.jrxml");
	// }

	/**
	 * Rigourous Test :-)
	 */
	private void testReport(String filename, boolean useEmptyDatasource) throws Exception
	{
		Connection connection = null;
		InputStream jrxmlStream = null;
		JasperReport report = null;
		JasperPrint jasperPrint = null;
		JasperDesign template = null;

		try
		{
			jrxmlStream = JRLoader.getResourceInputStream(filename);
			template = JRXmlLoader.load(jrxmlStream);
			report = JasperCompileManager.compileReport(template);
			Map<String, Object> params = new HashMap<String, Object>();

			if (useEmptyDatasource)
			{

				params.put("REPORT_DATA_SOURCE", new JREmptyDataSource(1));
			}
			else
			{
				connection = getHsql();
				params.put("REPORT_CONNECTION", connection);
			}

			jasperPrint = JasperFillManager.fillReport(report, params);
			export(jasperPrint);

		}
		finally
		{
			if (jrxmlStream != null)
			{
				try
				{
					jrxmlStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (connection != null)
			{
				connection.close();
			}
		}

	}

	protected void export(JasperPrint jasperPrint) throws Exception
	{
		JasperExportManager.exportReportToHtmlFile(jasperPrint,
				new File(outputDir, jasperPrint.getName() + ".html").getPath());

		JasperExportManager.exportReportToPdfFile(jasperPrint,
				new File(outputDir, jasperPrint.getName() + ".pdf").getPath());

		JRDocxExporter docxExporter = new JRDocxExporter();
		docxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		docxExporter.setExporterOutput(
				new SimpleOutputStreamExporterOutput(new File(outputDir, jasperPrint.getName() + ".docx")));
		docxExporter.exportReport();

		JRPptxExporter pptxExporter = new JRPptxExporter();
		pptxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		pptxExporter.setExporterOutput(
				new SimpleOutputStreamExporterOutput(new File(outputDir, jasperPrint.getName() + ".pptx")));
		pptxExporter.exportReport();

		JRXlsxExporter xlsxExporter = new JRXlsxExporter();
		xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		xlsxExporter.setExporterOutput(
				new SimpleOutputStreamExporterOutput(new File(outputDir, jasperPrint.getName() + ".xlsx")));
		xlsxExporter.exportReport();

		JROdsExporter odsExporter = new JROdsExporter();
		odsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		odsExporter.setExporterOutput(
				new SimpleOutputStreamExporterOutput(new File(outputDir, jasperPrint.getName() + ".ods")));
		odsExporter.exportReport();

		JROdtExporter odtExporter = new JROdtExporter();
		odtExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		odtExporter.setExporterOutput(
				new SimpleOutputStreamExporterOutput(new File(outputDir, jasperPrint.getName() + ".odt")));
		odtExporter.exportReport();

		JRRtfExporter rtfExporter = new JRRtfExporter();
		rtfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		rtfExporter
				.setExporterOutput(new SimpleWriterExporterOutput(new File(outputDir, jasperPrint.getName() + ".rtf")));
		rtfExporter.exportReport();

		// Other exporters, while possibly supported, are deprecated or no
		// longer used.
	}

	protected Connection getHsql() throws Exception
	{
		Class.forName("org.hsqldb.jdbcDriver");

		String connectString = "jdbc:hsqldb:file:target/hsqldb/test;shutdown=true";
		String user = "sa";
		String password = "";
		return DriverManager.getConnection(connectString, user, password);
	}
}

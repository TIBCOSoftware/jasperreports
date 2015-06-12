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

/*
 * Contributors:
 * Mirko Wawrowsky - mawawrosky@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.CsvExporterConfiguration;
import net.sf.jasperreports.export.CsvReportConfiguration;
import net.sf.jasperreports.export.ExportInterruptedException;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.WriterExporterOutput;


/**
 * Exports a JasperReports document to CSV format.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class JRAbstractCsvExporter<RC extends CsvReportConfiguration, C extends CsvExporterConfiguration, E extends JRExporterContext> 
	extends JRAbstractExporter<RC, C, WriterExporterOutput, E>
{
	public static final String BOM_CHARACTER = "\uFEFF";
	protected static final String CSV_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.csv.";

	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getElementHandler(JRGenericElementType, String)}.
	 */
	public static final String CSV_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "csv";

	/**
	 *
	 */
	protected Writer writer;
	
	protected ExporterNature nature;

	protected int pageIndex;

	
	/**
	 * @see #JRAbstractCsvExporter(JasperReportsContext)
	 */
	public JRAbstractCsvExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}

	
	/**
	 *
	 */
	public JRAbstractCsvExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}

	
	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		/*   */
		ensureJasperReportsContext();
		ensureInput();
		
		initExport();
		
		ensureOutput();
		
		writer = getExporterOutput().getWriter();

		try
		{
			exportReportToWriter();
		}
		catch (IOException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_OUTPUT_WRITER_ERROR,
					new Object[]{jasperPrint.getName()}, 
					e);
		}
		finally
		{
			getExporterOutput().close();
		}
	}

	
	/**
	 *
	 */
	protected void exportReportToWriter() throws JRException, IOException
	{
		CsvExporterConfiguration configuration = getCurrentConfiguration();
		if (configuration.isWriteBOM())
		{
			WriterExporterOutput output = getExporterOutput();
			Charset charset = Charset.forName(output.getEncoding());
			CharsetEncoder charsetEncoder = charset.newEncoder();
			if (charsetEncoder.canEncode(BOM_CHARACTER))
			{
				writer.write(BOM_CHARACTER);
			}
		}

		List<ExporterInputItem> items = exporterInput.getItems();
		
		for(int reportIndex = 0; reportIndex < items.size(); reportIndex++)
		{
			ExporterInputItem item = items.get(reportIndex);

			setCurrentExporterInputItem(item);

			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				PageRange pageRange = getPageRange();
				int startPageIndex = (pageRange == null || pageRange.getStartPageIndex() == null) ? 0 : pageRange.getStartPageIndex();
				int endPageIndex = (pageRange == null || pageRange.getEndPageIndex() == null) ? (pages.size() - 1) : pageRange.getEndPageIndex();

				for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
				{
					if (Thread.interrupted())
					{
						throw new ExportInterruptedException();
					}
				
					JRPrintPage page = pages.get(pageIndex);

					/*   */
					exportPage(page);
				}
			}
		}
				
		writer.flush();
	}


	/**
	 *
	 */
	protected abstract void exportPage(JRPrintPage page) throws IOException;
	
	
	@Override
	public JRStyledText getStyledText(JRPrintText textElement)
	{
		return textElement.getFullStyledText(noneSelector);
	}


	/**
	 *
	 */
	protected String prepareText(String source)
	{
		String str = null;
		
		if (source != null)
		{
			boolean putQuotes = false;
			
			CsvExporterConfiguration configuration = getCurrentConfiguration();
			String fieldDelimiter = configuration.getFieldDelimiter();
			String recordDelimiter = configuration.getRecordDelimiter();
			
			if (
				source.indexOf(fieldDelimiter) >= 0
				|| source.indexOf(recordDelimiter) >= 0
				)
			{
				putQuotes = true;
			}
			
			StringBuffer sbuffer = new StringBuffer();
			StringTokenizer tkzer = new StringTokenizer(source, "\"\n", true);
			String token = null;
			while(tkzer.hasMoreTokens())
			{
				token = tkzer.nextToken();
				if ("\"".equals(token))
				{
					putQuotes = true;
					sbuffer.append("\"\"");
				}
				else if ("\n".equals(token))
				{
					//sbuffer.append(" ");
					putQuotes = true;
					sbuffer.append("\n");
				}
				else
				{
					sbuffer.append(token);
				}
			}
			
			str = sbuffer.toString();
			
			if (putQuotes)
			{
				str = "\"" + str + "\"";
			}
		}
		
		return str;
	}
	
	
	@Override
	protected void initExport()
	{
		super.initExport();
	}
	
	
	@Override
	protected void initReport()
	{
		super.initReport();

		nature = new JRCsvExporterNature(jasperReportsContext, filter);
	}


	public String getExporterKey()
	{
		return CSV_EXPORTER_KEY;
	}

	
	public String getExporterPropertiesPrefix()
	{
		return CSV_EXPORTER_PROPERTIES_PREFIX;
	}
}

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
package net.sf.jasperreports.engine.export.ooxml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.zip.ExportZipEntry;
import net.sf.jasperreports.engine.export.zip.FileBufferedZip;
import net.sf.jasperreports.repo.RepositoryUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class XlsxZip extends FileBufferedZip
{
	public static final String EXCEPTION_MESSAGE_KEY_MACRO_TEMPLATE_NOT_FOUND = "export.xlsx.macro.template.not.found";
	/**
	 * 
	 */
	private final JasperReportsContext jasperReportsContext;

	/**
	 * 
	 */
	private ExportZipEntry workbookEntry;
	private ExportZipEntry stylesEntry;
	private ExportZipEntry relsEntry;
	private ExportZipEntry contentTypesEntry;
	
	/**
	 * @deprecated Replaced by {@link #XlsxZip(JasperReportsContext)}.
	 */
	public XlsxZip() throws IOException
	{
		this(DefaultJasperReportsContext.getInstance());
	}
	
	/**
	 * 
	 */
	public XlsxZip(JasperReportsContext jasperReportsContext) throws IOException
	{
		this(jasperReportsContext, null);
	}
	
	/**
	 * 
	 */
	public XlsxZip(JasperReportsContext jasperReportsContext, Integer memoryThreshold) throws IOException
	{
		super(memoryThreshold);

		this.jasperReportsContext = jasperReportsContext;
		
		workbookEntry = createEntry("xl/workbook.xml");
		addEntry(workbookEntry);
		
		stylesEntry = createEntry("xl/styles.xml");
		addEntry(stylesEntry);
		
		relsEntry = createEntry("xl/_rels/workbook.xml.rels");
		addEntry(relsEntry);
		
		contentTypesEntry = createEntry("[Content_Types].xml");
		addEntry(contentTypesEntry);
		
		addEntry("_rels/.rels", "net/sf/jasperreports/engine/export/ooxml/xlsx/_rels/xml.rels");
	}
	
	/**
	 *
	 */
	public ExportZipEntry getWorkbookEntry()
	{
		return workbookEntry;
	}
	
	/**
	 *
	 */
	public ExportZipEntry getStylesEntry()
	{
		return stylesEntry;
	}
	
	/**
	 *
	 */
	public ExportZipEntry getRelsEntry()
	{
		return relsEntry;
	}
	
	/**
	 *
	 */
	public ExportZipEntry getContentTypesEntry()
	{
		return contentTypesEntry;
	}
	
	/**
	 * 
	 */
	public ExportZipEntry addSheet(int index)
	{
		ExportZipEntry sheetEntry = createEntry("xl/worksheets/sheet" + index + ".xml");

		exportZipEntries.add(sheetEntry);

		return sheetEntry;
	}
	
	/**
	 * 
	 */
	public ExportZipEntry addSheetRels(int index)
	{
		ExportZipEntry sheetRelsEntry = createEntry("xl/worksheets/_rels/sheet" + index + ".xml.rels");

		exportZipEntries.add(sheetRelsEntry);
		
		return sheetRelsEntry;
	}
	
	/**
	 * 
	 */
	public ExportZipEntry addDrawing(int index)
	{
		ExportZipEntry drawingEntry = createEntry("xl/drawings/drawing" + index + ".xml");

		exportZipEntries.add(drawingEntry);

		return drawingEntry;
	}
	
	/**
	 * 
	 */
	public ExportZipEntry addDrawingRels(int index)
	{
		ExportZipEntry drawingRelsEntry = createEntry("xl/drawings/_rels/drawing" + index + ".xml.rels");

		exportZipEntries.add(drawingRelsEntry);

		return drawingRelsEntry;
	}

	/**
	 * 
	 */
	public void addMacro(String template)
	{
		InputStream templateIs = null;
		ZipInputStream templateZipIs = null;
		try
		{
			templateIs = RepositoryUtil.getInstance(jasperReportsContext).getInputStreamFromLocation(template);
			if (templateIs == null)
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_MACRO_TEMPLATE_NOT_FOUND,
						new Object[]{template});
			}
			else
			{
				templateZipIs = new ZipInputStream(templateIs);
				
				ZipEntry entry = null;
				while ((entry = templateZipIs.getNextEntry()) != null)
				{
					if ("xl/vbaProject.bin".equals(entry.getName()))
					{
						break;
					}
				}
				
				if (entry != null)
				{
					ExportZipEntry macroEntry = createEntry("xl/vbaProject.bin");
					OutputStream entryOs = macroEntry.getOutputStream();

					long entryLength = entry.getSize();
					
					byte[] bytes = new byte[10000];
					int ln = 0;
					long readBytesLength = 0;
					while (readBytesLength < entryLength && (ln = templateZipIs.read(bytes)) >= 0)
					{
						readBytesLength += ln;
						entryOs.write(bytes, 0, ln);
					}
					
					exportZipEntries.add(macroEntry);
				}
			}
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			if (templateZipIs != null)
			{
				try
				{
					templateZipIs.close();
				}
				catch (IOException e)
				{
				}
			}

			if (templateIs != null)
			{
				try
				{
					templateIs.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}
	
}

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
 *(at your option) any later version.
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

import net.sf.jasperreports.engine.export.zip.ExportZipEntry;
import net.sf.jasperreports.engine.export.zip.FileBufferedZip;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: OoxmlZip.java 2976 2009-07-28 14:38:22Z teodord $
 */
public class XlsxZip extends FileBufferedZip
{

	/**
	 * 
	 */
	private ExportZipEntry workbookEntry;
	private ExportZipEntry stylesEntry;
	private ExportZipEntry relsEntry;
	private ExportZipEntry contentTypesEntry;
	
	/**
	 * 
	 */
	public XlsxZip() throws IOException
	{
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
	
}

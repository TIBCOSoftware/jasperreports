/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.export.ooxml2;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import net.sf.jasperreports.engine.export.oasis.JROpenDocumentExporterNature;
import net.sf.jasperreports.engine.export.zip.AbstractZip;
import net.sf.jasperreports.engine.export.zip.ExportZipEntry;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: OoxmlZip.java 2922 2009-07-22 14:47:33Z teodord $
 */
public abstract class OoxmlZip extends AbstractZip
{

	/**
	 * 
	 */
	private ExportZipEntry documentEntry = null;
	private ExportZipEntry stylesEntry = null;
	private ExportZipEntry relsEntry = null;
	
	public OoxmlZip() throws IOException
	{
		this(JROpenDocumentExporterNature.ODT_NATURE);
	}

	/**
	 * 
	 */
	public OoxmlZip(byte openDocumentNature) throws IOException
	{
		exportZipEntries = new ArrayList();

		documentEntry = createEntry("word/document.xml");
		exportZipEntries.add(documentEntry);
		
		stylesEntry = createEntry("word/styles.xml");
		exportZipEntries.add(stylesEntry);
		
		relsEntry = createEntry("word/_rels/document.xml.rels");
		exportZipEntries.add(relsEntry);
		
		createRelsEntry();

		createContentTypesEntry();
	}
	
	/**
	 *
	 */
	public ExportZipEntry getDocumentEntry()
	{
		return documentEntry;
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
	private void createRelsEntry() throws IOException
	{
		ExportZipEntry relsEntry = createEntry("_rels/.rels");
		Writer relsWriter = null;
		try
		{
			relsWriter = relsEntry.getWriter();
			relsWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			relsWriter.write("<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n");
			relsWriter.write(" <Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"word/document.xml\"/>\n"); 
			relsWriter.write("</Relationships>\n"); 
			relsWriter.flush();
			exportZipEntries.add(relsEntry);
		}
		finally
		{
			if (relsWriter != null)
			{
				try
				{
					relsWriter.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}
	
	/**
	 * 
	 */
	private void createContentTypesEntry() throws IOException
	{
		ExportZipEntry contentTypesEntry = createEntry("[Content_Types].xml");
		Writer ctWriter = null;
		try
		{
			ctWriter = contentTypesEntry.getWriter();
			ctWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n");
			ctWriter.write("<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\"> \r\n");
//			ctWriter.write("  <Override PartName=\"/word/footnotes.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.footnotes+xml\"/> \r\n");
			ctWriter.write("  <Default Extension=\"jpeg\" ContentType=\"image/jpeg\"/> \r\n");
			ctWriter.write("  <Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/> \r\n");
			ctWriter.write("  <Default Extension=\"xml\" ContentType=\"application/xml\"/> \r\n");
			ctWriter.write("  <Override PartName=\"/word/document.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml\"/> \r\n");
			ctWriter.write("  <Override PartName=\"/word/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/word/endnotes.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.endnotes+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/docProps/app.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.extended-properties+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/word/settings.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/word/footer2.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.footer+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/word/footer3.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.footer+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/word/footer1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.footer+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/word/theme/theme1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.theme+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/word/header2.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.header+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/word/header3.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.header+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/word/fontTable.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.fontTable+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/word/webSettings.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.webSettings+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/word/header1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.header+xml\"/> \r\n");
//			ctWriter.write("  <Override PartName=\"/docProps/core.xml\" ContentType=\"application/vnd.openxmlformats-package.core-properties+xml\"/> \r\n");
			ctWriter.write("</Types> \r\n");
			ctWriter.flush();
			exportZipEntries.add(contentTypesEntry);
		}
		finally
		{
			if (ctWriter != null)
			{
				try
				{
					ctWriter.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}
	
}

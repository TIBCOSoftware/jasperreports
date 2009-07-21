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
package net.sf.jasperreports.engine.export.oasis.zip;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import net.sf.jasperreports.engine.export.oasis.JROpenDocumentExporterNature;
import net.sf.jasperreports.engine.export.zip.AbstractZip;
import net.sf.jasperreports.engine.export.zip.EmptyZipEntry;
import net.sf.jasperreports.engine.export.zip.ExportZipEntry;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class OasisZip extends AbstractZip
{

	/**
	 * 
	 */
	private ExportZipEntry contentEntry = null;
	private ExportZipEntry stylesEntry = null;
	
	public OasisZip() throws IOException
	{
		this(JROpenDocumentExporterNature.ODT_NATURE);
	}
	/**
	 * 
	 */
	public OasisZip(byte openDocumentNature) throws IOException
	{
		
		exportZipEntries = new ArrayList();

		contentEntry = createEntry("content.xml");
		exportZipEntries.add(contentEntry);
		
		exportZipEntries.add(new EmptyZipEntry("meta.xml"));
		exportZipEntries.add(new EmptyZipEntry("settings.xml"));

		stylesEntry = createEntry("styles.xml");
		exportZipEntries.add(stylesEntry);
		
		String mimetype;
		
		switch(openDocumentNature)
		{
			case JROpenDocumentExporterNature.ODS_NATURE:
				mimetype = "spreadsheet";
				break;
			case JROpenDocumentExporterNature.ODT_NATURE:
			default:
				mimetype = "text";
		}
		ExportZipEntry mimeEntry = createEntry("mimetype");
		Writer mimeWriter = null;
		try
		{
			mimeWriter = mimeEntry.getWriter();
			mimeWriter.write("application/vnd.oasis.opendocument."+mimetype);
			mimeWriter.flush();
			exportZipEntries.add(mimeEntry);
		}
		finally
		{
			if (mimeWriter != null)
			{
				try
				{
					mimeWriter.close();
				}
				catch (IOException e)
				{
				}
			}
		}

		ExportZipEntry manifestEntry = createEntry("META-INF/manifest.xml");
		Writer manifestWriter = null;
		try
		{
			manifestWriter = manifestEntry.getWriter();
			manifestWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n");
			manifestWriter.write("<!DOCTYPE manifest:manifest PUBLIC \"-//OpenOffice.org//DTD Manifest 1.0//EN\" \"Manifest.dtd\"> \r\n");
			manifestWriter.write("<manifest:manifest xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\"> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"application/vnd.oasis.opendocument." + mimetype + "\" manifest:full-path=\"/\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"application/vnd.sun.xml.ui.configuration\" manifest:full-path=\"Configurations2/\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Pictures/\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"content.xml\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"styles.xml\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"meta.xml\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Thumbnails/thumbnail.png\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Thumbnails/\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"settings.xml\"/> \r\n");
			manifestWriter.write("</manifest:manifest> \r\n");
			manifestWriter.flush();
			exportZipEntries.add(manifestEntry);
		}
		finally
		{
			if (mimeWriter != null)
			{
				try
				{
					mimeWriter.close();
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
	public ExportZipEntry getContentEntry()
	{
		return contentEntry;
	}
	
	/**
	 *
	 */
	public ExportZipEntry getStylesEntry()
	{
		return stylesEntry;
	}
	
}

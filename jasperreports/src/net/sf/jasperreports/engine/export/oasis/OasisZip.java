/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.export.oasis;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

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
	private ExportZipEntry contentEntry;
	private ExportZipEntry stylesEntry;
	
	/**
	 * 
	 */
	public OasisZip() throws IOException
	{
		this(JROpenDocumentExporterNature.ODT_NATURE);
	}

	/**
	 * 
	 */
	public OasisZip(byte openDocumentNature) throws IOException
	{
		exportZipEntries = new ArrayList<ExportZipEntry>();

		contentEntry = createEntry("content.xml");
		exportZipEntries.add(contentEntry);
		
		createMetaEntry();
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

		createMimeEntry(mimetype);

		createManifestEntry(mimetype);
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
	
	/**
	 * 
	 */
	private void createMimeEntry(String mimetype) throws IOException
	{
		ExportZipEntry mimeEntry = createEntry("mimetype");
		Writer mimeWriter = null;
		try
		{
			mimeWriter = mimeEntry.getWriter();
			mimeWriter.write("application/vnd.oasis.opendocument." + mimetype);
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
	}

	/**
	 * 
	 */
	private void createManifestEntry(String mimetype) throws IOException
	{
		ExportZipEntry manifestEntry = createEntry("META-INF/manifest.xml");
		Writer manifestWriter = null;
		try
		{
			manifestWriter = manifestEntry.getWriter();
			manifestWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			manifestWriter.write("<!DOCTYPE manifest:manifest PUBLIC \"-//OpenOffice.org//DTD Manifest 1.0//EN\" \"Manifest.dtd\">\n");
			manifestWriter.write("<manifest:manifest xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\">\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"application/vnd.oasis.opendocument." + mimetype + "\" manifest:full-path=\"/\"/>\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"application/vnd.sun.xml.ui.configuration\" manifest:full-path=\"Configurations2/\"/>\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Pictures/\"/>\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"content.xml\"/>\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"styles.xml\"/>\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"meta.xml\"/>\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Thumbnails/thumbnail.png\"/>\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Thumbnails/\"/>\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"settings.xml\"/>\n");
			manifestWriter.write("</manifest:manifest>\n");
			manifestWriter.flush();
			exportZipEntries.add(manifestEntry);
		}
		finally
		{
			if (manifestWriter != null)
			{
				try
				{
					manifestWriter.close();
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
	private void createMetaEntry() throws IOException
	{
		ExportZipEntry metaEntry = createEntry("meta.xml");
		Writer metaWriter = null;
		try
		{
			metaWriter = metaEntry.getWriter();
			metaWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			metaWriter.flush();
			exportZipEntries.add(metaEntry);
		}
		finally
		{
			if (metaWriter != null)
			{
				try
				{
					metaWriter.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}
	
}

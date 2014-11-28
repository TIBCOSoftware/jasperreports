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
package net.sf.jasperreports.engine.export.oasis;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import net.sf.jasperreports.engine.export.zip.AbstractZip;
import net.sf.jasperreports.engine.export.zip.ExportZipEntry;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class OasisZip extends AbstractZip
{
	
	/**
	 * 
	 */
	public static final String MIME_TYPE_ODT = "text";
	public static final String MIME_TYPE_ODS = "spreadsheet";
	public static final String PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	
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
		this(MIME_TYPE_ODT);
	}

	/**
	 * 
	 */
	public OasisZip(String mimeType) throws IOException
	{
		exportZipEntries = new ArrayList<ExportZipEntry>();

		contentEntry = createEntry("content.xml");
		exportZipEntries.add(contentEntry);
		
		createMetaEntry();
		createSettingsEntry();

		stylesEntry = createEntry("styles.xml");
		exportZipEntries.add(stylesEntry);
		
		createMimeEntry(mimeType);

		createManifestEntry(mimeType);
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
			manifestWriter.write(PROLOG);
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
			metaWriter.write(PROLOG);
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
	
	/**
	 * 
	 */
	private void createSettingsEntry() throws IOException
	{
		ExportZipEntry settingsEntry = createEntry("settings.xml");
		Writer settingsWriter = null;
		try
		{
			settingsWriter = settingsEntry.getWriter();
			settingsWriter.write(PROLOG);
			settingsWriter.write("<office:document-settings \n");
			settingsWriter.write("xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" \n");
			settingsWriter.write("xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n");
			settingsWriter.write("xmlns:config=\"urn:oasis:names:tc:opendocument:xmlns:config:1.0\" \n");
			settingsWriter.write("xmlns:ooo=\"http://openoffice.org/2004/office\"/>\n");
			settingsWriter.flush();
			exportZipEntries.add(settingsEntry);
		}
		finally
		{
			if (settingsWriter != null)
			{
				try
				{
					settingsWriter.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}
	
}

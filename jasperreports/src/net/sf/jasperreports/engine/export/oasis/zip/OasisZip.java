/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.export.oasis.zip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRDataUtils.java 1330 2006-07-10 12:09:48 +0300 (Mon, 10 Jul 2006) lucianc $
 */
public class OasisZip
{

	/**
	 * 
	 */
	private List oasisZipEntries = null;

	/**
	 * 
	 */
	private OasisZipEntry contentEntry = null;
	private OasisZipEntry stylesEntry = null;
	
	/**
	 * 
	 */
	public OasisZip()
	{
		oasisZipEntries = new ArrayList();

		//FIXMEODT contentsEntry = new FileOasisZipEntry("content.xml", new File("content.xml"));
		contentEntry = new ByteArrayOasisZipEntry("content.xml");
		oasisZipEntries.add(contentEntry);
		
		oasisZipEntries.add(new EmptyOasisZipEntry("meta.xml"));
		oasisZipEntries.add(new EmptyOasisZipEntry("settings.xml"));

		//stylesEntry = new FileOasisZipEntry("styles.xml", new File("styles.xml"));
		stylesEntry = new ByteArrayOasisZipEntry("styles.xml");
		oasisZipEntries.add(stylesEntry);

		try
		{
			//OasisZipEntry mimeEntry = new FileOasisZipEntry("mimetype", new File("mimetype"));
			OasisZipEntry mimeEntry = new ByteArrayOasisZipEntry("mimetype");
			Writer mimeWriter = mimeEntry.getWriter();
			mimeWriter.write("application/vnd.oasis.opendocument.text");
			mimeWriter.flush();
			oasisZipEntries.add(mimeEntry);

			//OasisZipEntry manifestEntry = new FileOasisZipEntry("manifest.xml", new File("manifest.xml"));
			OasisZipEntry manifestEntry = new ByteArrayOasisZipEntry("META-INF/manifest.xml");
			Writer manifestWriter = manifestEntry.getWriter();
			manifestWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n");
			manifestWriter.write("<!DOCTYPE manifest:manifest PUBLIC \"-//OpenOffice.org//DTD Manifest 1.0//EN\" \"Manifest.dtd\"> \r\n");
			manifestWriter.write("<manifest:manifest xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\"> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"application/vnd.oasis.opendocument.text\" manifest:full-path=\"/\"/> \r\n");
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
			oasisZipEntries.add(manifestEntry);
			//FIXMEODT close writers everytime
		}
		catch (IOException e)
		{
			e.printStackTrace();
			//FIXMEODT
		}
	}
	
	/**
	 *
	 */
	public OasisZipEntry getContentEntry()
	{
		return contentEntry;
	}
	
	/**
	 *
	 */
	public OasisZipEntry getStylesEntry()
	{
		return stylesEntry;
	}
	
	/**
	 *
	 */
	public void zipEntries(OutputStream os) throws IOException
	{
		ZipOutputStream zipos = new ZipOutputStream(os);
		zipos.setMethod(ZipOutputStream.DEFLATED);
		
		byte[] bytes = new byte[4096];
		int bytesRead = 0;
	
		for (int i = 0; i < oasisZipEntries.size(); i++) 
		{
			OasisZipEntry oasisZipEntry = (OasisZipEntry)oasisZipEntries.get(i);
			InputStream is = oasisZipEntry.getInputStream();
			ZipEntry zipEntry = new ZipEntry(oasisZipEntry.getName());
			zipos.putNextEntry(zipEntry);
			
			while ((bytesRead = is.read(bytes)) != -1)
			{
				zipos.write(bytes, 0, bytesRead);
			}
			
			is.close(); 
		}
		
		zipos.flush();
		zipos.close();//FIXMEODT zip os needs to be closed
	}
	
}

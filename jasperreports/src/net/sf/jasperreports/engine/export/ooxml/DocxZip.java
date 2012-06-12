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
package net.sf.jasperreports.engine.export.ooxml;

import java.io.IOException;

import net.sf.jasperreports.engine.export.zip.ExportZipEntry;
import net.sf.jasperreports.engine.export.zip.FileBufferedZip;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class DocxZip extends FileBufferedZip
{

	/**
	 * 
	 */
	private ExportZipEntry documentEntry;
	private ExportZipEntry stylesEntry;
	private ExportZipEntry settingsEntry;
	private ExportZipEntry relsEntry;
	
	/**
	 * 
	 */
	public DocxZip() throws IOException
	{
		documentEntry = createEntry("word/document.xml");
		addEntry(documentEntry);
		
		stylesEntry = createEntry("word/styles.xml");
		addEntry(stylesEntry);
		
		settingsEntry = createEntry("word/settings.xml");
		addEntry(settingsEntry);
		
		relsEntry = createEntry("word/_rels/document.xml.rels");
		addEntry(relsEntry);
		
		addEntry("_rels/.rels", "net/sf/jasperreports/engine/export/ooxml/docx/_rels/xml.rels");
		addEntry("[Content_Types].xml", "net/sf/jasperreports/engine/export/ooxml/docx/Content_Types.xml");
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
	public ExportZipEntry getSettingsEntry()
	{
		return settingsEntry;
	}
	
	/**
	 *
	 */
	public ExportZipEntry getRelsEntry()
	{
		return relsEntry;
	}
	
}

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
package net.sf.jasperreports.engine.export.ooxml;

import java.io.IOException;
import java.io.Writer;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: OoxmlZip.java 2908 2009-07-21 14:32:01Z teodord $
 */
public class RelsHelper extends BaseHelper
{

	/**
	 * 
	 */
	public RelsHelper(Writer writer)
	{
		super(writer);
	}

	/**
	 * 
	 */
	public void exportHeader() throws IOException
	{
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n");
		writer.write(" <Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/>\n");
	}
	
	/**
	 * 
	 */
	public void exportImage(String imageName) throws IOException
	{
		writer.write(" <Relationship Id=\"" + imageName + "\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/image\" Target=\"media/" + imageName + ".jpeg\"/>\n");
	}
	
//	/**
//	 * 
//	 */
//	public void exportHyperlink(String id, String href) throws IOException
//	{
//		writer.write(" <Relationship Id=\"" + id + "\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink\" Target=\"" + href + "\"/>\n");
//	}
	
	/**
	 * 
	 */
	public void exportFooter() throws IOException
	{
		writer.write("</Relationships>\n");
	}
	
}

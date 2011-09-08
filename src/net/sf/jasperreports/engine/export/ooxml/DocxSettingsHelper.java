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
import java.io.Writer;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.export.LengthUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class DocxSettingsHelper extends BaseHelper
{
	
	/**
	 * 
	 */
	public DocxSettingsHelper(Writer writer)
	{
		super(writer);
	}

	/**
	 * 
	 */
	public void export(JasperPrint jasperPrint) throws IOException
	{
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		writer.write("<w:settings\n");
		writer.write(" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">\n"); 
		writer.write("  <w:defaultTabStop w:val=\"" +
				LengthUtil.twip(new JRBasePrintText(jasperPrint.getDefaultStyleProvider()).getParagraph().getTabStopWidth()) +
				"\"/>\n");
		writer.write("</w:settings>");
	}
	
}

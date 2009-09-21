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
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRStyle;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ReportStyleHelper.java 3033 2009-08-27 11:46:22Z teodord $
 */
public class XlsxStyleHelper extends BaseHelper
{
	/**
	 * 
	 */
	private ParagraphHelper paragraphHelper = null;
	private RunHelper runHelper = null;
	
	/**
	 * 
	 */
	public XlsxStyleHelper(Writer writer, Map fontMap)
	{
		super(writer);
		
		paragraphHelper = new ParagraphHelper(writer, false);
		runHelper = new RunHelper(writer, fontMap);
	}

	/**
	 * 
	 */
	public void export(List jasperPrintList) throws IOException
	{
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">\n");
		writer.write("<fonts count=\"1\"><font><sz val=\"11\"/><color theme=\"1\"/><name val=\"Calibri\"/><family val=\"2\"/><scheme val=\"minor\"/></font></fonts>\n");
		writer.write("<fills count=\"2\"><fill><patternFill patternType=\"none\"/></fill><fill><patternFill patternType=\"gray125\"/></fill></fills>\n");
		writer.write("<borders count=\"1\"><border><left/><right/><top/><bottom/><diagonal/></border></borders>\n");
		writer.write("<cellStyleXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\"/></cellStyleXfs>\n");
		writer.write("<cellXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\" xfId=\"0\"/></cellXfs>\n");
		writer.write("<cellStyles count=\"1\"><cellStyle name=\"Normal\" xfId=\"0\" builtinId=\"0\"/></cellStyles>\n");
		writer.write("<dxfs count=\"0\"/><tableStyles count=\"0\" defaultTableStyle=\"TableStyleMedium9\" defaultPivotStyle=\"PivotStyleLight16\"/>\n");

		
//		for(int reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
//		{
//			JasperPrint jasperPrint = (JasperPrint)jasperPrintList.get(reportIndex);
//			
//			JRStyle[] styles = jasperPrint.getStyles();
//			if (styles != null)
//			{
//				for(int i = 0; i < styles.length; i++)
//				{
//					JRStyle style = styles[i];
//					exportHeader(style);
//					paragraphHelper.exportProps(style);
//					runHelper.exportProps(style);
//					exportFooter();
//				}
//			}
//		}

		writer.write("</styleSheet>\n");
	}
	
	/**
	 * 
	 */
	private void exportHeader(JRStyle style) throws IOException
	{
		//writer.write(" <w:style w:type=\"paragraph\" w:default=\"1\" w:styleId=\"" + style.getName() + "\">\n");
		writer.write(" <w:style w:type=\"paragraph\" w:styleId=\"" + style.getName() + "\"");
		if (style.isDefault())
		{
			writer.write(" w:default=\"1\"");
		}
		writer.write(">\n");
		writer.write("  <w:name w:val=\"" + style.getName() + "\" />\n");
		writer.write("  <w:qFormat />\n");
		String styleNameReference = style.getStyle() == null ? null : style.getStyle().getName();//FIXMEDOCX why getStyleNameReference is not working?
		if (styleNameReference != null)
		{
			writer.write("  <w:basedOn w:val=\"" + styleNameReference + "\" />\n");
		}
	}
	
	/**
	 * 
	 */
	private void exportFooter() throws IOException
	{
		writer.write("</styleSheet>\n");
	}
	
}

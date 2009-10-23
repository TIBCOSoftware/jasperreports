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
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.util.FileBufferedWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ReportStyleHelper.java 3033 2009-08-27 11:46:22Z teodord $
 */
public class XlsxStyleHelper extends BaseHelper
{
	/**
	 * 
	 */
	private FileBufferedWriter fontsWriter = new FileBufferedWriter();
	private FileBufferedWriter fillsWriter = new FileBufferedWriter();
	private FileBufferedWriter cellXfsWriter = new FileBufferedWriter();
	
	private Map styleCache = new HashMap();//FIXMEXLSX use soft cache? check other exporter caches as well
	
	/**
	 * 
	 */
	public XlsxStyleHelper(Writer writer, Map fontMap, String exporterKey)
	{
		super(writer);
	}

	/**
	 * 
	 */
	public int getCellStyle(JRExporterGridCell gridCell)
	{
		XlsxStyleInfo styleInfo = new XlsxStyleInfo(gridCell);
		Integer styleIndex = (Integer)styleCache.get(styleInfo.getId());
		if (styleIndex == null)
		{
			styleIndex = new Integer(styleCache.size());
			exportCellStyle(styleInfo, styleIndex);
			styleCache.put(styleInfo.getId(), styleIndex);
		}
		return styleIndex.intValue();
	}

	/**
	 * 
	 */
	private void exportCellStyle(XlsxStyleInfo styleInfo, Integer styleIndex)
	{
		try
		{
			fontsWriter.write(
				"<font><sz val=\"" + styleInfo.fontSize + "\"/>" 
				+ "<color rgb=\"" + styleInfo.forecolor + "\"/>"
				+ "<name val=\"" + styleInfo.fontName + "\"/>"
				+ "<b val=\"" + styleInfo.isBold + "\"/>"
				+ "<i val=\"" + styleInfo.isItalic + "\"/>"
				+ "<u val=\"" + (styleInfo.isUnderline ? "single" : "none") + "\"/>"
				+ "<strike val=\"" + styleInfo.isStrikeThrough + "\"/>"
				+ "<family val=\"2\"/></font>\n");
			fillsWriter.write("<fill><patternFill patternType=\"solid\"><fgColor rgb=\"" + styleInfo.backcolor + "\"/></patternFill></fill>\n");
			cellXfsWriter.write(
				"<xf numFmtId=\"" + styleIndex
				+ "\" fontId=\"" + (styleIndex.intValue() + 1)
				+ "\" fillId=\"" + (styleIndex.intValue() + 2)
				+ "\" borderId=\"0\" xfId=\"" + styleIndex + "\">"
				+ "<alignment wrapText=\"1\""
				+ " horizontal=\"" + styleInfo.horizontalAlign
				+ "\" vertical=\"" + styleInfo.verticalAlign
				+ "\"/></xf>\n");
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 * 
	 */
	public void export()
	{
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		write("<styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">\n");
		
		write("<fonts>\n");// count=\"1\">\n");
		write("<font><sz val=\"11\"/><color theme=\"1\"/><name val=\"Calibri\"/><family val=\"2\"/><scheme val=\"minor\"/></font>\n");
		fontsWriter.writeData(writer);
		write("</fonts>\n");

		write("<fills>\n");// count=\"2\">\n");
		write("<fill><patternFill patternType=\"none\"/></fill>\n");
		write("<fill><patternFill patternType=\"gray125\"/></fill>\n");
		fillsWriter.writeData(writer);
		write("</fills>\n");
		write("<borders count=\"1\"><border><left/><right/><top/><bottom/><diagonal/></border></borders>\n");
		write("<cellStyleXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\"/></cellStyleXfs>\n");

		write("<cellXfs>\n");// count=\"1\">\n");
		cellXfsWriter.writeData(writer);
		write("</cellXfs>\n");
		
		write("<cellStyles count=\"1\"><cellStyle name=\"Normal\" xfId=\"0\" builtinId=\"0\"/></cellStyles>\n");
		write("<dxfs count=\"0\"/><tableStyles count=\"0\" defaultTableStyle=\"TableStyleMedium9\" defaultPivotStyle=\"PivotStyleLight16\"/>\n");

		write("</styleSheet>\n");
	}
	
}

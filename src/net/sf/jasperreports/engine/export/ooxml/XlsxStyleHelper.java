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
import java.util.Locale;
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
	private FileBufferedWriter formatsWriter = new FileBufferedWriter();
	private FileBufferedWriter fontsWriter = new FileBufferedWriter();
	private FileBufferedWriter fillsWriter = new FileBufferedWriter();
	private FileBufferedWriter bordersWriter = new FileBufferedWriter();
	private FileBufferedWriter cellXfsWriter = new FileBufferedWriter();
	
	private Map styleCache = new HashMap();//FIXMEXLSX use soft cache? check other exporter caches as well
	
	private XlsxFormatHelper formatHelper;
	private XlsxFontHelper fontHelper;
	private XlsxBorderHelper borderHelper;
	
	private boolean isWhitePageBackground;
	private boolean isIgnoreCellBackground;
	
	/**
	 * 
	 */
	public XlsxStyleHelper(
		Writer writer, 
		Map fontMap, 
		String exporterKey,
		boolean isWhitePageBackground,
		boolean isIgnoreCellBorder,
		boolean isIgnoreCellBackground,
		boolean isFontSizeFixEnabled		
		)
	{
		super(writer);
		
		this.isWhitePageBackground = isWhitePageBackground;
		this.isIgnoreCellBackground = isIgnoreCellBackground;
		
		formatHelper = new XlsxFormatHelper(formatsWriter);
		fontHelper = new XlsxFontHelper(fontsWriter, fontMap, exporterKey, isFontSizeFixEnabled);
		borderHelper = new XlsxBorderHelper(bordersWriter, isIgnoreCellBorder);
	}

	/**
	 * 
	 */
	public int getCellStyle(
		JRExporterGridCell gridCell, 
		String pattern, 
		Locale locale,
		boolean isWrapText, 
		boolean isHidden, 
		boolean isLocked 
		)
	{
		XlsxStyleInfo styleInfo = 
			new XlsxStyleInfo(
				formatHelper.getFormat(pattern) + 1,
				fontHelper.getFont(gridCell, locale) + 1,
				borderHelper.getBorder(gridCell) + 1,
				gridCell,
				isWrapText,
				isHidden,
				isLocked
				);
		Integer styleIndex = (Integer)styleCache.get(styleInfo.getId());
		if (styleIndex == null)
		{
			styleIndex = Integer.valueOf(styleCache.size() + 1);
			exportCellStyle(gridCell, styleInfo, styleIndex);
			styleCache.put(styleInfo.getId(), styleIndex);
		}
		return styleIndex.intValue();
	}

	/**
	 * 
	 */
	private void exportCellStyle(JRExporterGridCell gridCell, XlsxStyleInfo styleInfo, Integer styleIndex)
	{
		try
		{
			if (isIgnoreCellBackground || styleInfo.backcolor == null)
			{
				if (isWhitePageBackground)
				{
					fillsWriter.write("<fill><patternFill patternType=\"solid\"><fgColor rgb=\"FFFFFF\"/></patternFill></fill>\n");
				}
				else
				{
					fillsWriter.write("<fill><patternFill patternType=\"none\"/></fill>\n");
				}
			}
			else
			{
				fillsWriter.write("<fill><patternFill patternType=\"solid\"><fgColor rgb=\"" + styleInfo.backcolor + "\"/></patternFill></fill>\n");
			}
			
			cellXfsWriter.write(
				"<xf numFmtId=\"" + styleInfo.formatIndex
				+ "\" fontId=\"" + styleInfo.fontIndex
				+ "\" fillId=\"" + (styleIndex.intValue() + 1)
				+ "\" borderId=\"" + styleInfo.borderIndex
				+ "\" xfId=\"" + styleIndex + "\">"
				+ "<alignment wrapText=\"" + styleInfo.isWrapText + "\""
				+ (styleInfo.horizontalAlign == null ? "" : " horizontal=\"" + styleInfo.horizontalAlign + "\"")
				+ (styleInfo.verticalAlign == null ? "" : " vertical=\"" + styleInfo.verticalAlign + "\"")
//				+ (" shrinkToFit=\"" + styleInfo.isFontSizeFixEnabled + "\"")
				+ "/>"
				);
			cellXfsWriter.write("<protection hidden=\"" + styleInfo.isHidden + "\" locked=\"" + styleInfo.isLocked + "\"/>");
			cellXfsWriter.write("</xf>\n");
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
		
		write("<numFmts>\n");// count=\"1\">\n");
		write("<numFmt numFmtId=\"0\" formatCode=\"General\"/>\n");
		formatsWriter.writeData(writer);
		write("</numFmts>\n");

		write("<fonts>\n");// count=\"1\">\n");
		write("<font><sz val=\"11\"/><color theme=\"1\"/><name val=\"Calibri\"/><family val=\"2\"/><scheme val=\"minor\"/></font>\n");
		fontsWriter.writeData(writer);
		write("</fonts>\n");

		write("<fills>\n");// count=\"2\">\n");
		write("<fill><patternFill patternType=\"none\"/></fill>\n");
		write("<fill><patternFill patternType=\"solid\"><fgColor rgb=\"FFFFFF\"/></patternFill></fill>\n");
		fillsWriter.writeData(writer);
		write("</fills>\n");
		write("<borders>\n");// count=\"1\">\n");
		write("<border><left/><right/><top/><bottom/><diagonal/></border>\n");
		bordersWriter.writeData(writer);
		write("</borders>\n");
		//write("<cellStyleXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\"/></cellStyleXfs>\n");

		write("<cellXfs>\n");// count=\"1\">\n");
		write("<xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\" xfId=\"0\"/>\n");
		cellXfsWriter.writeData(writer);
		write("</cellXfs>\n");
		
		//write("<cellStyles count=\"1\"><cellStyle name=\"Normal\" xfId=\"0\" builtinId=\"0\"/></cellStyles>\n");
		write("<dxfs count=\"0\"/><tableStyles count=\"0\" defaultTableStyle=\"TableStyleMedium9\" defaultPivotStyle=\"PivotStyleLight16\"/>\n");

		write("</styleSheet>\n");
	}
	
}

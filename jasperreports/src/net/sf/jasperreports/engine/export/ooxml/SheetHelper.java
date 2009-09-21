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
 *(at your option) any later version.
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


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: StyleBuilder.java 2908 2009-07-21 14:32:01Z teodord $
 */
public class SheetHelper extends BaseHelper
{
	/**
	 * 
	 */
	public SheetHelper(Writer writer)
	{
		super(writer);
	}

	/**
	 *
	 */
	public void exportHeader() throws IOException
	{
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<worksheet\n");
		writer.write(" xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\"\n");
		writer.write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">\n");

//		<dimension ref="A1:B10"/>
//		<sheetViews>
//		 <sheetView tabSelected="1" workbookViewId="0"/></sheetViews>
//		 <sheetFormatPr defaultRowHeight="15"/>
//		 <sheetData>
//		  <row r="1"><c r="A1" t="inlineStr"><is><t>This is inline string example</t></is></c><c r="B1"><v>111</v></c></row>
//		  <row r="2"><c r="A2" t="s"><v>1</v></c><c r="B2"><v>222</v></c></row>
//		  <row r="3"><c r="A3" t="s"><v>2</v></c><c r="B3"><v>333</v></c></row>
//		  <row r="4"><c r="A4" t="s"><v>3</v></c><c r="B4"><v>444</v></c></row>
//		  <row r="5"><c r="A5" t="s"><v>4</v></c><c r="B5"><v>555</v></c></row>
//		  <row r="6"><c r="A6" t="s"><v>5</v></c><c r="B6"><v>666</v></c></row>
//		  <row r="7"><c r="A7" t="s"><v>6</v></c><c r="B7"><v>777</v></c></row>
//		  <row r="8"><c r="A8" t="s"><v>7</v></c><c r="B8"><v>888</v></c></row>
//		  <row r="9"><c r="A9" t="s"><v>8</v></c><c r="B9"><v>999</v></c></row>
//		  <row r="10"><c r="A10" t="s"><v>9</v></c><c r="B10"><f>SUM(B1:B9)</f><v>4995</v></c></row>
//		</sheetData>

		writer.write("<dimension ref=\"A1\"/><sheetViews><sheetView workbookViewId=\"0\"/></sheetViews>\n");
		writer.write("<sheetFormatPr defaultRowHeight=\"15\"/>\n");
		writer.write("<sheetData/>\n");
		writer.write("<pageMargins left=\"0.7\" right=\"0.7\" top=\"0.75\" bottom=\"0.75\" header=\"0.3\" footer=\"0.3\"/>\n");
		//writer.write("<pageSetup orientation=\"portrait\" r:id=\"rId1\"/>\n");		
	}
	

	/**
	 *
	 */
	public void exportFooter() throws IOException
	{
		writer.write("</worksheet>");		
	}
}

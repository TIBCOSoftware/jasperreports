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

/*
 * Special thanks to Google 'Summer of Code 2005' program for supporting this development
 * 
 * Contributors:
 * Majid Ali Khan - majidkk@users.sourceforge.net
 * Frank Schönheit - Frank.Schoenheit@Sun.COM
 */
package net.sf.jasperreports.engine.export.ooxml;

import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.export.CutsInfo;
import net.sf.jasperreports.engine.export.JRExporterGridCell;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class TableHelper extends BaseHelper
{
	/**
	 * 
	 */
	private CellHelper cellHelper = null;
	private ParagraphHelper paragraphHelper = null;
	private RunHelper runHelper = null;


	/**
	 * 
	 */
	protected TableHelper(
		Writer writer,
		RunHelper runHelper,
		boolean pageBreak
		) 
	{
		super(writer);

		this.cellHelper = new CellHelper(writer);
		this.paragraphHelper = new ParagraphHelper(writer, pageBreak);
		this.runHelper = runHelper;
	}


	/**
	 * 
	 */
	public CellHelper getCellHelper() 
	{
		return cellHelper;
	}
	

	/**
	 * 
	 */
	public ParagraphHelper getParagraphHelper() 
	{
		return paragraphHelper;
	}
	

	/**
	 * 
	 */
	public void exportHeader(CutsInfo xCuts) throws IOException 
	{
		writer.write("  <w:tbl> \r\n");
		writer.write("   <w:tblPr> \r\n");
		writer.write("    <w:tblLayout w:type=\"fixed\"/> \r\n");
//		writer.write("    <w:tblStyle w:val=\"TableNormal\" /> \r\n");
//		writer.write("    <w:tblW w:w=\"0\" w:type=\"pct\" /> \r\n");
//		writer.write("    <w:tblLook w:val=\"04A0\" /> \r\n");
		writer.write("   </w:tblPr> \r\n");
		writer.write("   <w:tblGrid> \r\n");
		for(int col = 1; col < xCuts.size(); col++)
		{
			writer.write("    <w:gridCol w:w=\"" + Utility.twip(xCuts.getCut(col) - xCuts.getCut(col - 1)) + "\"/> \r\n");
		}
		writer.write("   </w:tblGrid> \r\n");
	}
	
	public void exportFooter() throws IOException 
	{
		writer.write("  </w:tbl> \r\n");
	}
	
	public void exportRowHeader(int rowHeight) throws IOException 
	{
		writer.write("   <w:tr> \r\n");
		writer.write("    <w:trPr> \r\n");
		writer.write("     <w:trHeight w:hRule=\"exact\" w:val=\"" +  + Utility.twip(rowHeight) + "\" /> \r\n");
		writer.write("    </w:trPr> \r\n");
	}
	
	public void exportRowFooter() throws IOException 
	{
		writer.write("   </w:tr> \r\n");//FIXMEDOCX really need rn?
	}
	
	public void exportEmptyCell(JRExporterGridCell gridCell, int emptyCellColSpan) throws IOException
	{
		writer.write("    <w:tc> \r\n");
		writer.write("     <w:tcPr> \r\n");
		if (emptyCellColSpan > 1)
		{
			writer.write("      <w:gridSpan w:val=\"" + emptyCellColSpan +"\" /> \r\n");
		}
		
		if (gridCell != null)
		{
			cellHelper.exportProps(gridCell);
		}
		
		writer.write("     </w:tcPr> \r\n");
		
		paragraphHelper.exportEmptyParagraph();

		writer.write("    </w:tc> \r\n");
	}

	public void exportOccupiedCells(JRExporterGridCell gridCell) throws IOException
	{
		writer.write("    <w:tc> \r\n");
		writer.write("     <w:tcPr> \r\n");
		if (gridCell.getColSpan() > 1)
		{
			writer.write("      <w:gridSpan w:val=\"" + gridCell.getColSpan() +"\" /> \r\n");
		}
		writer.write("      <w:vMerge w:val=\"continue\" /> \r\n");
		
		cellHelper.exportProps(gridCell.getElement(), gridCell);
		
		writer.write("     </w:tcPr> \r\n");
		
		paragraphHelper.exportEmptyParagraph();

		cellHelper.exportFooter();
	}

}
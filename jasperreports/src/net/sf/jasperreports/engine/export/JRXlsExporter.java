/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */

/*
 * Contributors:
 * Wolfgang - javabreak@users.sourceforge.net
 */
package dori.jasper.engine.export;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import dori.jasper.engine.JRAbstractExporter;
import dori.jasper.engine.JRAlignment;
import dori.jasper.engine.JRElement;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExporterParameter;
import dori.jasper.engine.JRFont;
import dori.jasper.engine.JRPrintElement;
import dori.jasper.engine.JRPrintEllipse;
import dori.jasper.engine.JRPrintImage;
import dori.jasper.engine.JRPrintLine;
import dori.jasper.engine.JRPrintPage;
import dori.jasper.engine.JRPrintRectangle;
import dori.jasper.engine.JRPrintText;
import dori.jasper.engine.JRTextElement;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.base.JRBasePrintPage;
import dori.jasper.engine.design.JRDesignFont;
import dori.jasper.engine.util.JRLoader;


/**
 *
 */
public class JRXlsExporter extends JRAbstractExporter
{


	/**
	 *
	 */
	private JasperPrint jasperPrint = null;

	/**
	 *
	 */
	private int pageHeight = 0;

	/**
	 *
	 */
	private HSSFWorkbook workbook = null;
	private HSSFSheet sheet = null;
	private HSSFRow row = null;
	private HSSFCell cell = null;
	private HSSFCellStyle emptyCellStyle = null;

	/**
	 *
	 */
	private JRFont defaultFont = null;
	private List loadedFonts = new ArrayList();
	private List loadedCellStyles = new ArrayList();

	/**
	 *
	 */
	private boolean isOnePagePerSheet = false;
	private boolean isRemoveEmptySpace = false;
	private boolean isWhitePageBackground = true;

	/**
	 *
	 */
	private short whiteIndex = (new HSSFColor.WHITE()).getIndex();
	private short backgroundMode = HSSFCellStyle.SOLID_FOREGROUND;

	/**
	 *
	 */
	private JRExporterGridCell grid[][] = null;
	private boolean isRowNotEmpty[] = null;
	private List xCuts = null;
	private List yCuts = null;


	/**
	 *
	 */
	private JRFont getDefaultFont()
	{
		if (defaultFont == null)
		{
			defaultFont = jasperPrint.getDefaultFont();
			if (defaultFont == null)
			{
				defaultFont = new JRDesignFont();
			}
		}
		
		return defaultFont;
	}


	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		this.jasperPrint = (JasperPrint)this.parameters.get(JRExporterParameter.JASPER_PRINT);
		if (jasperPrint == null)
		{
			InputStream is = (InputStream)this.parameters.get(JRExporterParameter.INPUT_STREAM);
			if (is != null)
			{
				this.jasperPrint = (JasperPrint)JRLoader.loadObject(is);
			}
			else
			{
				URL url = (URL)this.parameters.get(JRExporterParameter.INPUT_URL);
				if (url != null)
				{
					this.jasperPrint = (JasperPrint)JRLoader.loadObject(url);
				}
				else
				{
					File file = (File)this.parameters.get(JRExporterParameter.INPUT_FILE);
					if (file != null)
					{
						this.jasperPrint = (JasperPrint)JRLoader.loadObject(file);
					}
					else
					{
						String fileName = (String)this.parameters.get(JRExporterParameter.INPUT_FILE_NAME);
						if (fileName != null)
						{
							this.jasperPrint = (JasperPrint)JRLoader.loadObject(fileName);
						}
						else
						{
							throw new JRException("No input source supplied to the exporter.");
						}
					}
				}
			}
		}

		Boolean isOnePagePerSheetParameter = (Boolean)this.parameters.get(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET);
		if (isOnePagePerSheetParameter != null)
		{
			this.isOnePagePerSheet = isOnePagePerSheetParameter.booleanValue();
		}

		Boolean isRemoveEmptySpaceParameter = (Boolean)this.parameters.get(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS);
		if (isRemoveEmptySpaceParameter != null)
		{
			this.isRemoveEmptySpace = isRemoveEmptySpaceParameter.booleanValue();
		}
		
		Boolean isWhitePageBackgroundParameter = (Boolean)this.parameters.get(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND);
		if (isWhitePageBackgroundParameter != null)
		{
			this.isWhitePageBackground = isWhitePageBackgroundParameter.booleanValue();
			this.backgroundMode = HSSFCellStyle.SOLID_FOREGROUND;
		}
		
		OutputStream os = (OutputStream)this.parameters.get(JRExporterParameter.OUTPUT_STREAM);
		if (os != null)
		{
			this.exportReportToStream(os);
		}
		else
		{
			File destFile = (File)this.parameters.get(JRExporterParameter.OUTPUT_FILE);
			if (destFile == null)
			{
				String fileName = (String)this.parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
				if (fileName != null)
				{
					destFile = new File(fileName);
				}
				else
				{
					throw new JRException("No output specified for the exporter.");
				}
			}

			try
			{
				os = new FileOutputStream(destFile);
				this.exportReportToStream(os);
				os.flush();
			}
			catch (IOException e)
			{
				throw new JRException("Error trying to export to file : " + destFile, e);
			}
			finally
			{
				if (os != null)
				{
					try
					{
						os.close();
					}
					catch(IOException e)
					{
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private void exportReportToStream(OutputStream os) throws JRException
	{
		workbook = new HSSFWorkbook();
		emptyCellStyle = workbook.createCellStyle();
		emptyCellStyle.setFillForegroundColor((new HSSFColor.WHITE()).getIndex());
		emptyCellStyle.setFillPattern(backgroundMode);
		
		try
		{
			List pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				JRPrintPage page = null;
				
				if (this.isOnePagePerSheet)
				{
					this.pageHeight = jasperPrint.getPageHeight();
					
					for(int i = 0; i < pages.size(); i++)
					{
						if (Thread.currentThread().isInterrupted())
						{
							throw new JRException("Current thread interrupted.");
						}
				
						page = (JRPrintPage)pages.get(i);
		
						sheet = workbook.createSheet("Page " + (i + 1));
	
						/*   */
						this.exportPage(page);
					}
				}
				else
				{
					this.pageHeight = jasperPrint.getPageHeight() * pages.size();

					JRPrintPage allPages = new JRBasePrintPage();
					Collection elements = null;
					JRPrintElement element = null;
					for(int i = 0; i < pages.size(); i++)
					{
						if (Thread.currentThread().isInterrupted())
						{
							throw new JRException("Current thread interrupted.");
						}
				
						page = (JRPrintPage)pages.get(i);
		
						elements = page.getElements();
						if (elements != null && elements.size() > 0)
						{
							for(Iterator it = elements.iterator(); it.hasNext();)
							{
								element = (JRPrintElement)it.next();
								element.setY(element.getY() + jasperPrint.getPageHeight() * i);
								allPages.addElement(element);
							}
						}
					}

					sheet = workbook.createSheet("Sheet1");

					/*   */
					this.exportPage(allPages);
				}
			}

			workbook.write(os);
		}
		catch(IOException e) 
		{
			throw new JRException("Error generating XLS report : " + jasperPrint.getName(), e);
		}
	}


	/**
	 *
	 */
	private void exportPage(JRPrintPage page) throws JRException
	{
		this.layoutGrid(page);

		int width = 0;
		for(int i = 1; i < xCuts.size(); i++)
		{
			width = ((Integer)xCuts.get(i)).intValue() - ((Integer)xCuts.get(i - 1)).intValue();
			sheet.setColumnWidth((short)(i - 1), (short)(width * 43));
		}

		JRPrintElement element = null;
		HSSFCell emptyCell = null;
		for(int y = 0; y < grid.length; y++)
		{
			if (isRowNotEmpty[y] || !isRemoveEmptySpace)
			{
				row = this.sheet.createRow((short)y);
	
				int emptyCellColSpan = 0;
				int emptyCellWidth = 0;
				int lastRowHeight = grid[y][0].height;
	
				row.setHeight((short)(lastRowHeight * 19));
	
				int x = 0;
				for(x = 0; x < grid[y].length; x++)
				{
					emptyCell = row.createCell((short)x);
					emptyCell.setCellStyle(emptyCellStyle);
	
					if(grid[y][x].element != null)
					{
						if (emptyCellColSpan > 0)
						{
							if (emptyCellColSpan > 1)
							{
								//this.sbuffer.append(" colspan=" + emptyCellColSpan);
								//sheet.addMergedRegion(new Region(y, (short)(x - emptyCellColSpan - 1), y, (short)(x - 1)));
							}
							emptyCellColSpan = 0;
							emptyCellWidth = 0;
						}
	
						element = grid[y][x].element;
	
						if (element instanceof JRPrintLine)
						{
							this.exportLine((JRPrintLine)element, grid[y][x], x, y);
						}
						else if (element instanceof JRPrintRectangle)
						{
							this.exportRectangle(element, grid[y][x], x, y);
						}
						else if (element instanceof JRPrintEllipse)
						{
							this.exportRectangle(element, grid[y][x], x, y);
						}
						else if (element instanceof JRPrintText)
						{
							this.exportText((JRPrintText)element, grid[y][x], x, y);
						}
	
						x += grid[y][x].colSpan - 1;
					}
					else
					{
						emptyCellColSpan++;
						emptyCellWidth += grid[y][x].width;
					}
				}
	
				if (emptyCellColSpan > 0)
				{
					if (emptyCellColSpan > 1)
					{
						//this.sbuffer.append(" colspan=" + emptyCellColSpan);
						//sheet.addMergedRegion(new Region(y, (short)x, y, (short)(x + emptyCellColSpan - 1)));
					}
				}
			}
			else
			{
				row = this.sheet.createRow((short)y);
				row.setHeight((short)0);
	
				for(int x = 0; x < grid[y].length; x++)
				{
					emptyCell = row.createCell((short)x);
					emptyCell.setCellStyle(emptyCellStyle);
				}
			}
		}
	}


	/**
	 *
	 */
	private void exportLine(JRPrintLine line, JRExporterGridCell gridCell, int x, int y)
	{
		if (gridCell.colSpan > 1 || gridCell.rowSpan > 1)
		{
			sheet.addMergedRegion(new Region(y, (short)x, (y + gridCell.rowSpan - 1), (short)(x + gridCell.colSpan - 1)));
		}

		short forecolor = getNearestColor(line.getForecolor()).getIndex();

		HSSFFont cellFont = this.getLoadedFont(getDefaultFont(), forecolor);

		HSSFCellStyle cellStyle = 
			this.getLoadedCellStyle(
				HSSFCellStyle.SOLID_FOREGROUND,
				forecolor, 
				HSSFCellStyle.ALIGN_LEFT, 
				HSSFCellStyle.VERTICAL_TOP, 
				cellFont
				);

		cell = row.createCell((short)x);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("");
		cell.setCellStyle(cellStyle);
	}


	/**
	 *
	 */
	private void exportRectangle(JRPrintElement element, JRExporterGridCell gridCell, int x, int y)
	{
		if (gridCell.colSpan > 1 || gridCell.rowSpan > 1)
		{
			sheet.addMergedRegion(new Region(y, (short)x, (y + gridCell.rowSpan - 1), (short)(x + gridCell.colSpan - 1)));
		}

		short forecolor = getNearestColor(element.getForecolor()).getIndex();

		short mode = this.backgroundMode;
		short backcolor = this.whiteIndex;
		if (element.getMode() == JRElement.MODE_OPAQUE)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getNearestColor(element.getBackcolor()).getIndex();
		}

		HSSFFont cellFont = this.getLoadedFont(getDefaultFont(), forecolor);

		HSSFCellStyle cellStyle = 
			this.getLoadedCellStyle(
				mode,
				backcolor, 
				HSSFCellStyle.ALIGN_LEFT, 
				HSSFCellStyle.VERTICAL_TOP, 
				cellFont
				);

		cell = row.createCell((short)x);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("");
		cell.setCellStyle(cellStyle);
	}
	
	
	/**
	 *
	 */
	private void exportText(JRPrintText text, JRExporterGridCell gridCell, int x, int y)
	{
		if (gridCell.colSpan > 1 || gridCell.rowSpan > 1)
		{
			sheet.addMergedRegion(new Region(y, (short)x, (y + gridCell.rowSpan - 1), (short)(x + gridCell.colSpan - 1)));
		}

		if (text.getText() != null)// && text.getText().length() > 0)
		{
			JRFont font = text.getFont();
			if (font == null)
			{
				font = this.getDefaultFont();
			}

			short forecolor = getNearestColor(text.getForecolor()).getIndex();

			HSSFFont cellFont = this.getLoadedFont(font, forecolor);

			short horizontalAlignment = HSSFCellStyle.ALIGN_LEFT;

			switch (text.getTextAlignment())
			{
				case JRAlignment.HORIZONTAL_ALIGN_LEFT :
				{
					horizontalAlignment = HSSFCellStyle.ALIGN_LEFT;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_CENTER :
				{
					horizontalAlignment = HSSFCellStyle.ALIGN_CENTER;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
				{
					horizontalAlignment = HSSFCellStyle.ALIGN_RIGHT;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
				{
					horizontalAlignment = HSSFCellStyle.ALIGN_JUSTIFY;
					break;
				}
				default :
				{
					horizontalAlignment = HSSFCellStyle.ALIGN_LEFT;
				}
			}

			short verticalAlignment = HSSFCellStyle.ALIGN_LEFT;

			switch (text.getVerticalAlignment())
			{
				case JRTextElement.VERTICAL_ALIGN_TOP :
				{
					verticalAlignment = HSSFCellStyle.VERTICAL_TOP;
					break;
				}
				case JRTextElement.VERTICAL_ALIGN_MIDDLE :
				{
					verticalAlignment = HSSFCellStyle.VERTICAL_CENTER;
					break;
				}
				case JRTextElement.VERTICAL_ALIGN_BOTTOM :
				{
					verticalAlignment = HSSFCellStyle.VERTICAL_BOTTOM;
					break;
				}
				default :
				{
					verticalAlignment = HSSFCellStyle.VERTICAL_TOP;
				}
			}

			short mode = this.backgroundMode;
			short backcolor = this.whiteIndex;
			if (text.getMode() == JRElement.MODE_OPAQUE)
			{
				mode = HSSFCellStyle.SOLID_FOREGROUND;
				backcolor = getNearestColor(text.getBackcolor()).getIndex();
			}

			HSSFCellStyle cellStyle = 
				this.getLoadedCellStyle(
					mode,
					backcolor, 
					horizontalAlignment, 
					verticalAlignment, 
					cellFont
					);

			cell = row.createCell((short)x);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			try
			{
				cell.setCellValue(Double.parseDouble(text.getText()));
			}
			catch(NumberFormatException e)
			{
				cell.setCellValue(text.getText());
			}
			cell.setCellStyle(cellStyle);
		}
	}


	/**
	 *
	 */
	private void layoutGrid(JRPrintPage page)
	{
		xCuts = new ArrayList();
		yCuts = new ArrayList();

		xCuts.add(new Integer(0));
		xCuts.add(new Integer(jasperPrint.getPageWidth()));
		yCuts.add(new Integer(0));
		yCuts.add(new Integer(this.pageHeight));

		Integer x = null;
		Integer y = null;

		JRPrintElement element = null;

		List elems = page.getElements();
		for(Iterator it = elems.iterator(); it.hasNext();)
		{
			element = ((JRPrintElement)it.next());

			if (!(element instanceof JRPrintImage))
			{
				x = new Integer(element.getX());
				if (!xCuts.contains(x))
				{
					xCuts.add(x);
				}
				x = new Integer(element.getX() + element.getWidth());
				if (!xCuts.contains(x))
				{
					xCuts.add(x);
				}
				y = new Integer(element.getY());
				if (!yCuts.contains(y))
				{
					yCuts.add(y);
				}
				y = new Integer(element.getY() + element.getHeight());
				if (!yCuts.contains(y))
				{
					yCuts.add(y);
				}
			}
		}

		Collections.sort(xCuts);
		Collections.sort(yCuts);

		int xCellCount = xCuts.size() - 1;
		int yCellCount = yCuts.size() - 1;

		grid = new JRExporterGridCell[yCellCount][xCellCount];
		isRowNotEmpty = new boolean[yCellCount];

		for(int j = 0; j < yCellCount; j++)
		{
			for(int i = 0; i < xCellCount; i++)
			{
				grid[j][i] =
					new JRExporterGridCell(
						null,
						((Integer)xCuts.get(i + 1)).intValue() - ((Integer)xCuts.get(i)).intValue(),
						((Integer)yCuts.get(j + 1)).intValue() - ((Integer)yCuts.get(j)).intValue(),
						1,
						1
						);
			}
		}

		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;
		int xi = 0;
		int yi = 0;
		boolean isOverlap = false;

		for(int i = elems.size() - 1; i >= 0; i--)
		{
			element = ((JRPrintElement)elems.get(i));
			
			if (!(element instanceof JRPrintImage))
			{
				x1 = xCuts.indexOf(new Integer(element.getX()));
				y1 = yCuts.indexOf(new Integer(element.getY()));
				x2 = xCuts.indexOf(new Integer(element.getX() + element.getWidth()));
				y2 = yCuts.indexOf(new Integer(element.getY() + element.getHeight()));

				isOverlap = false;
				yi = y1;
				while(yi < y2 && !isOverlap)
				{
					xi = x1;
					while(xi < x2 && !isOverlap)
					{
						if(grid[yi][xi].element != null)
						{
							isOverlap = true;
						}
						xi++;
					}
					yi++;
				}

				if (!isOverlap)
				{
					yi = y1;
					while(yi < y2)
					{
						xi = x1;
						while(xi < x2)
						{
							grid[yi][xi] = JRExporterGridCell.OCCUPIED_CELL;
							xi++;
						}
						isRowNotEmpty[yi] = true;
						yi++;
					}

					grid[y1][x1] = 
						new JRExporterGridCell(
							element,
							element.getWidth(),
							element.getHeight(),
							x2 - x1,
							y2 - y1
							);
				}
			}
		}
	}


	/**
	 *
	 */
	private static HSSFColor getNearestColor(Color awtColor)
	{
		HSSFColor color = null;

		Map triplets = HSSFColor.getTripletHash();
		if (triplets != null)
		{
			Collection keys = triplets.keySet();
			if (keys != null && keys.size() > 0)
			{
				Object key = null;
				HSSFColor crtColor = null;
				short[] rgb = null;
				int diff = 0;
				int minDiff = 999;
				for(Iterator it = keys.iterator(); it.hasNext();)
				{
					key = it.next();

					crtColor = (HSSFColor)triplets.get(key);
					rgb = crtColor.getTriplet();

					diff = Math.abs(rgb[0] - awtColor.getRed()) +
						Math.abs(rgb[1] - awtColor.getGreen()) +
						Math.abs(rgb[2] - awtColor.getBlue());

					if (diff < minDiff)
					{
						minDiff = diff;
						color = crtColor;
					}
				}
			}
		}

		return color;
	}


	/**
	 *
	 */
	private HSSFFont getLoadedFont(JRFont font, short forecolor)
	{
		HSSFFont cellFont = null;

		if (this.loadedFonts != null && this.loadedFonts.size() > 0)
		{
			HSSFFont cf = null;
			for (int i = 0; i < this.loadedFonts.size(); i++)
			{
				cf = (HSSFFont)this.loadedFonts.get(i);
				
				if (
					cf.getFontName().equals(font.getFontName()) &&
					(cf.getColor() == forecolor) &&
					(cf.getFontHeight() == (short)(font.getSize() * 19)) &&
					((cf.getUnderline() == HSSFFont.U_SINGLE)?(font.isUnderline()):(!font.isUnderline())) &&
					(cf.getStrikeout() == font.isStrikeThrough()) &&
					((cf.getBoldweight() == HSSFFont.BOLDWEIGHT_BOLD)?(font.isBold()):(!font.isBold())) &&
					(cf.getItalic() == font.isItalic())
					)
				{
					cellFont = cf;
					break;
				}
			}
		}
		
		if (cellFont == null)
		{
			cellFont = workbook.createFont();
			cellFont.setFontName(font.getFontName());
			cellFont.setColor(forecolor);
			cellFont.setFontHeight((short)(font.getSize() * 19));

			if (font.isUnderline())
			{
				cellFont.setUnderline(HSSFFont.U_SINGLE);
			}
			if (font.isStrikeThrough())
			{
				cellFont.setStrikeout(true);
			}
			if (font.isBold())
			{
				cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			}
			if (font.isItalic())
			{
				cellFont.setItalic(true);
			}
			
			this.loadedFonts.add(cellFont);
		}
			
		return cellFont;
	}


	/**
	 *
	 */
	private HSSFCellStyle getLoadedCellStyle(
		short mode, 
		short backcolor, 
		short horizontalAlignment, 
		short verticalAlignment, 
		HSSFFont font
		)
	{
		HSSFCellStyle cellStyle = null;

		if (this.loadedCellStyles != null && this.loadedCellStyles.size() > 0)
		{
			HSSFCellStyle cs = null;
			for (int i = 0; i < this.loadedCellStyles.size(); i++)
			{
				cs = (HSSFCellStyle)this.loadedCellStyles.get(i);
				
				if (
					cs.getFillPattern() == mode &&
					cs.getFillForegroundColor() == backcolor &&
					cs.getAlignment() == horizontalAlignment &&
					cs.getVerticalAlignment() == verticalAlignment &&
					cs.getFontIndex() == font.getIndex()
					)
				{
					cellStyle = cs;
					break;
				}
			}
		}
		
		if (cellStyle == null)
		{
			cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(backcolor);
			cellStyle.setFillPattern(mode);
			cellStyle.setAlignment(horizontalAlignment);
			cellStyle.setVerticalAlignment(verticalAlignment);
			cellStyle.setFont(font);
			cellStyle.setWrapText(true);
			
			this.loadedCellStyles.add(cellStyle);
		}
			
		return cellStyle;
	}


}

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
 * Mario Daepp - mdaepp@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import org.xml.sax.SAXException;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBasePrintElement;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXlsExporter extends JRAbstractExporter
{


	/**
	 *
	 */
	protected JRStyledTextParser styledTextParser = new JRStyledTextParser();

	/**
	 *
	 */
	protected int pageHeight = 0;

	/**
	 *
	 */
	protected HSSFWorkbook workbook = null;
	protected HSSFSheet sheet = null;
	protected HSSFRow row = null;
	protected HSSFCell cell = null;
	protected HSSFCellStyle emptyCellStyle = null;

	protected JRExportProgressMonitor progressMonitor = null;

	protected int reportIndex = 0;

	/**
	 *
	 */
	protected JRFont defaultFont = null;
	protected List loadedFonts = new ArrayList();
	protected List loadedCellStyles = new ArrayList();

	/**
	 *
	 */
	protected boolean isOnePagePerSheet = false;
	protected boolean isRemoveEmptySpace = false;
	protected boolean isAutoDetectCellType = true;

	/**
	 *
	 */
	protected short whiteIndex = (new HSSFColor.WHITE()).getIndex();
	protected short backgroundMode = HSSFCellStyle.SOLID_FOREGROUND;

	/**
	 *
	 */
	protected JRExporterGridCell grid[][] = null;
	protected boolean isRowNotEmpty[] = null;
	protected List xCuts = null;
	protected List yCuts = null;


	/**
	 *
	 */
	protected JRFont getDefaultFont()
	{
		if (defaultFont == null)
		{
			defaultFont = jasperPrint.getDefaultFont();
			if (defaultFont == null)
			{
				defaultFont = new JRBaseFont();
			}
		}
		
		return defaultFont;
	}


	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		
		/*   */
		setOffset();

		/*   */
		setInput();

		/*   */
		if (!isModeBatch)
		{
			setPageRange();
		}

		Boolean isOnePagePerSheetParameter = (Boolean)parameters.get(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET);
		if (isOnePagePerSheetParameter != null)
		{
			isOnePagePerSheet = isOnePagePerSheetParameter.booleanValue();
		}

		Boolean isRemoveEmptySpaceParameter = (Boolean)parameters.get(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS);
		if (isRemoveEmptySpaceParameter != null)
		{
			isRemoveEmptySpace = isRemoveEmptySpaceParameter.booleanValue();
		}
		
		Boolean isWhitePageBackgroundParameter = (Boolean)parameters.get(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND);
		if (isWhitePageBackgroundParameter != null)
		{
			if (!isWhitePageBackgroundParameter.booleanValue())
			{
				backgroundMode = HSSFCellStyle.NO_FILL;
			}
		}
		
		Boolean isAutoDetectCellTypeParameter = (Boolean)parameters.get(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE);
		if (isAutoDetectCellTypeParameter != null)
		{
			isAutoDetectCellType = isAutoDetectCellTypeParameter.booleanValue();
		}
		
		OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
		if (os != null)
		{
			exportReportToStream(os);
		}
		else
		{
			File destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
			if (destFile == null)
			{
				String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
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
				exportReportToStream(os);
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
	protected void exportReportToStream(OutputStream os) throws JRException
	{
		workbook = new HSSFWorkbook();
		emptyCellStyle = workbook.createCellStyle();
		emptyCellStyle.setFillForegroundColor((new HSSFColor.WHITE()).getIndex());
		emptyCellStyle.setFillPattern(backgroundMode);
		
		try
		{
			for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
			{
				jasperPrint = (JasperPrint)jasperPrintList.get(reportIndex);
				defaultFont = null;

				List pages = jasperPrint.getPages();
				if (pages != null && pages.size() > 0)
				{
					if (isModeBatch)
					{
						startPageIndex = 0;
						endPageIndex = pages.size() - 1;
					}

					if (isOnePagePerSheet)
					{
						pageHeight = jasperPrint.getPageHeight();
					
						for(int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
						{
							if (Thread.currentThread().isInterrupted())
							{
								throw new JRException("Current thread interrupted.");
							}
				
							JRPrintPage page = (JRPrintPage)pages.get(pageIndex);
		
							sheet = workbook.createSheet("Page " + (pageIndex + 1));
	
							/*   */
							exportPage(page, page);
						}
					}
					else
					{
						pageHeight = jasperPrint.getPageHeight() * (endPageIndex - startPageIndex + 1);

						JRPrintPage alterYAllPages = new JRBasePrintPage();
						JRPrintPage allPages = new JRBasePrintPage();
						Collection elements = null;
						JRPrintElement alterYElement = null;
						JRPrintElement element = null;
						for(int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
						{
							if (Thread.currentThread().isInterrupted())
							{
								throw new JRException("Current thread interrupted.");
							}
				
							JRPrintPage page = (JRPrintPage)pages.get(pageIndex);
		
							elements = page.getElements();
							if (elements != null && elements.size() > 0)
							{
								for(Iterator it = elements.iterator(); it.hasNext();)
								{
									element = (JRPrintElement)it.next();
									allPages.addElement(element);
								
									alterYElement = new JRBasePrintElement();
									alterYElement.setY(element.getY() + globalOffsetY + jasperPrint.getPageHeight() * pageIndex);
									alterYAllPages.addElement(alterYElement);
								}
							}
						}

						sheet = workbook.createSheet(jasperPrint.getName());

						/*   */
						exportPage(alterYAllPages, allPages);
					}
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
	protected void exportPage(JRPrintPage alterYPage, JRPrintPage page) throws JRException
	{
		layoutGrid(alterYPage, page);

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
				row = sheet.getRow((short)y);
				
				if (row == null)
				{
					row = sheet.createRow((short)y);
				}
	
				int emptyCellColSpan = 0;
				int emptyCellWidth = 0;
				int lastRowHeight = grid[y][0].height;
	
				row.setHeightInPoints((short)lastRowHeight);
	
				int x = 0;
				for(x = 0; x < grid[y].length; x++)
				{
					emptyCell = row.getCell((short)x);
					if (emptyCell == null)
					{
						emptyCell = row.createCell((short)x);
						emptyCell.setCellStyle(emptyCellStyle);
					}
	
					if(grid[y][x].element != null)
					{
						if (emptyCellColSpan > 0)
						{
							if (emptyCellColSpan > 1)
							{
								//sbuffer.append(" colspan=" + emptyCellColSpan);
								//sheet.addMergedRegion(new Region(y, (short)(x - emptyCellColSpan - 1), y, (short)(x - 1)));
							}
							emptyCellColSpan = 0;
							emptyCellWidth = 0;
						}
	
						element = grid[y][x].element;
	
						if (element instanceof JRPrintLine)
						{
							exportLine((JRPrintLine)element, grid[y][x], x, y);
						}
						else if (element instanceof JRPrintRectangle)
						{
							exportRectangle(element, grid[y][x], x, y);
						}
						else if (element instanceof JRPrintEllipse)
						{
							exportRectangle(element, grid[y][x], x, y);
						}
//						else if (element instanceof JRPrintImage)//FIXME at least the border should appear
//						{
//							exportImage(element, grid[y][x], x, y);
//						}
						else if (element instanceof JRPrintText)
						{
							exportText((JRPrintText)element, grid[y][x], x, y);
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
						//sbuffer.append(" colspan=" + emptyCellColSpan);
						//sheet.addMergedRegion(new Region(y, (short)x, y, (short)(x + emptyCellColSpan - 1)));
					}
				}
			}
			else
			{
				row = sheet.getRow((short)y);
				if (row == null)
				{
					row = sheet.createRow((short)y);
				}
				row.setHeight((short)0);
	
				for(int x = 0; x < grid[y].length; x++)
				{
					emptyCell = row.getCell((short)x);
					if (emptyCell == null)
					{
						emptyCell = row.createCell((short)x);
						emptyCell.setCellStyle(emptyCellStyle);
					}
				}
			}
		}
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	/**
	 *
	 */
	protected void exportLine(JRPrintLine line, JRExporterGridCell gridCell, int x, int y)
	{
		if (gridCell.colSpan > 1 || gridCell.rowSpan > 1)
		{
			sheet.addMergedRegion(new Region(y, (short)x, (y + gridCell.rowSpan - 1), (short)(x + gridCell.colSpan - 1)));
		}

		short forecolor = getNearestColor(line.getForecolor()).getIndex();

		HSSFFont cellFont = getLoadedFont(getDefaultFont(), forecolor);

		HSSFCellStyle cellStyle = 
			getLoadedCellStyle(
				HSSFCellStyle.SOLID_FOREGROUND,
				forecolor, 
				HSSFCellStyle.ALIGN_LEFT, 
				HSSFCellStyle.VERTICAL_TOP, 
				(short)0,
				cellFont,
				HSSFCellStyle.BORDER_NONE,
				forecolor,
				HSSFCellStyle.BORDER_NONE,
				forecolor,
				HSSFCellStyle.BORDER_NONE,
				forecolor,
				HSSFCellStyle.BORDER_NONE,
				forecolor
				);

		cell = row.createCell((short)x);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("");
		cell.setCellStyle(cellStyle);
	}


	/**
	 *
	 */
	protected void exportRectangle(JRPrintElement element, JRExporterGridCell gridCell, int x, int y)
	{
		if (gridCell.colSpan > 1 || gridCell.rowSpan > 1)
		{
			sheet.addMergedRegion(new Region(y, (short)x, (y + gridCell.rowSpan - 1), (short)(x + gridCell.colSpan - 1)));
		}

		short forecolor = getNearestColor(element.getForecolor()).getIndex();

		short mode = backgroundMode;
		short backcolor = whiteIndex;
		if (element.getMode() == JRElement.MODE_OPAQUE)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getNearestColor(element.getBackcolor()).getIndex();
		}

		HSSFFont cellFont = getLoadedFont(getDefaultFont(), forecolor);

		HSSFCellStyle cellStyle = 
			getLoadedCellStyle(
				mode,
				backcolor, 
				HSSFCellStyle.ALIGN_LEFT, 
				HSSFCellStyle.VERTICAL_TOP,
				(short)0, 
				cellFont,
				HSSFCellStyle.BORDER_NONE,
				backcolor,
				HSSFCellStyle.BORDER_NONE,
				backcolor,
				HSSFCellStyle.BORDER_NONE,
				backcolor,
				HSSFCellStyle.BORDER_NONE,
				backcolor
				);

		cell = row.createCell((short)x);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("");
		cell.setCellStyle(cellStyle);
	}
	
	
	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		JRStyledText styledText = null;

		String text = textElement.getText();
		if (text != null)
		{
			if (textElement.isStyledText())
			{
				try
				{
					styledText = styledTextParser.parse(null, text);
				}
				catch (SAXException e)
				{
					//ignore if invalid styled text and treat like normal text
				}
			}
		
			if (styledText == null)
			{
				styledText = new JRStyledText();
				styledText.append(text);
				styledText.addRun(new JRStyledText.Run(null, 0, text.length()));
			}
		}
		
		return styledText;
	}


	/**
	 *
	 */
	protected void exportText(JRPrintText textElement, JRExporterGridCell gridCell, int x, int y)
	{
		JRStyledText styledText = getStyledText(textElement);

		if (styledText == null)
		{
			return;
		}

		JRFont font = textElement.getFont();
		if (font == null)
		{
			font = getDefaultFont();
		}

		short forecolor = getNearestColor(textElement.getForecolor()).getIndex();

		HSSFFont cellFont = getLoadedFont(font, forecolor);

		short horizontalAlignment = HSSFCellStyle.ALIGN_LEFT;
		short verticalAlignment = HSSFCellStyle.ALIGN_LEFT;
		short rotation = 0;

		switch (textElement.getRotation())
		{
			case JRTextElement.ROTATION_LEFT :
			{
				rotation = 90;

				switch (textElement.getTextAlignment())
				{
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					{
						verticalAlignment = HSSFCellStyle.VERTICAL_BOTTOM;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
					{
						verticalAlignment = HSSFCellStyle.VERTICAL_CENTER;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
					{
						verticalAlignment = HSSFCellStyle.VERTICAL_TOP;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
					{
						verticalAlignment = HSSFCellStyle.VERTICAL_JUSTIFY;
						break;
					}
					default :
					{
						verticalAlignment = HSSFCellStyle.VERTICAL_BOTTOM;
					}
				}

				switch (textElement.getVerticalAlignment())
				{
					case JRTextElement.VERTICAL_ALIGN_TOP :
					{
						horizontalAlignment = HSSFCellStyle.ALIGN_LEFT;
						break;
					}
					case JRTextElement.VERTICAL_ALIGN_MIDDLE :
					{
						horizontalAlignment = HSSFCellStyle.ALIGN_CENTER;
						break;
					}
					case JRTextElement.VERTICAL_ALIGN_BOTTOM :
					{
						horizontalAlignment = HSSFCellStyle.ALIGN_RIGHT;
						break;
					}
					default :
					{
						horizontalAlignment = HSSFCellStyle.ALIGN_LEFT;
					}
				}

				break;
			}
			case JRTextElement.ROTATION_RIGHT :
			{
				rotation = -90;

				switch (textElement.getTextAlignment())
				{
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					{
						verticalAlignment = HSSFCellStyle.VERTICAL_TOP;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
					{
						verticalAlignment = HSSFCellStyle.VERTICAL_CENTER;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
					{
						verticalAlignment = HSSFCellStyle.VERTICAL_BOTTOM;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
					{
						verticalAlignment = HSSFCellStyle.VERTICAL_JUSTIFY;
						break;
					}
					default :
					{
						verticalAlignment = HSSFCellStyle.VERTICAL_TOP;
					}
				}

				switch (textElement.getVerticalAlignment())
				{
					case JRTextElement.VERTICAL_ALIGN_TOP :
					{
						horizontalAlignment = HSSFCellStyle.ALIGN_RIGHT;
						break;
					}
					case JRTextElement.VERTICAL_ALIGN_MIDDLE :
					{
						horizontalAlignment = HSSFCellStyle.ALIGN_CENTER;
						break;
					}
					case JRTextElement.VERTICAL_ALIGN_BOTTOM :
					{
						horizontalAlignment = HSSFCellStyle.ALIGN_LEFT;
						break;
					}
					default :
					{
						horizontalAlignment = HSSFCellStyle.ALIGN_RIGHT;
					}
				}

				break;
			}
			case JRTextElement.ROTATION_NONE :
			default :
			{
				rotation = 0;

				switch (textElement.getTextAlignment())
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

				switch (textElement.getVerticalAlignment())
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
			}
		}

		short mode = backgroundMode;
		short backcolor = whiteIndex;
		if (textElement.getMode() == JRElement.MODE_OPAQUE)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getNearestColor(textElement.getBackcolor()).getIndex();
		}

		short topBorder = HSSFCellStyle.BORDER_NONE;
		short topBorderColor = backcolor;
		short leftBorder = HSSFCellStyle.BORDER_NONE;
		short leftBorderColor = backcolor;
		short bottomBorder = HSSFCellStyle.BORDER_NONE;
		short bottomBorderColor = backcolor;
		short rightBorder = HSSFCellStyle.BORDER_NONE;
		short rightBorderColor = backcolor;
		
		if (textElement.getBox() != null)
		{
			topBorder = getBorder(textElement.getBox().getTopBorder()); 
			topBorderColor = 
				getNearestColor(
					textElement.getBox().getTopBorderColor() == null ? textElement.getForecolor() : textElement.getBox().getTopBorderColor()
					).getIndex();
			leftBorder = getBorder(textElement.getBox().getLeftBorder()); 
			leftBorderColor = 
				getNearestColor(
					textElement.getBox().getLeftBorderColor() == null ? textElement.getForecolor() : textElement.getBox().getLeftBorderColor()
					).getIndex();
			bottomBorder = getBorder(textElement.getBox().getBottomBorder()); 
			bottomBorderColor = 
				getNearestColor(
					textElement.getBox().getBottomBorderColor() == null ? textElement.getForecolor() : textElement.getBox().getBottomBorderColor()
					).getIndex();
			rightBorder = getBorder(textElement.getBox().getRightBorder()); 
			rightBorderColor = 
				getNearestColor(
					textElement.getBox().getRightBorderColor() == null ? textElement.getForecolor() : textElement.getBox().getRightBorderColor()
					).getIndex();
		}
		
		HSSFCellStyle cellStyle = 
			getLoadedCellStyle(
				mode,
				backcolor, 
				horizontalAlignment, 
				verticalAlignment,
				rotation, 
				cellFont,
				topBorder,
				topBorderColor,
				leftBorder,
				leftBorderColor,
				bottomBorder,
				bottomBorderColor,
				rightBorder,
				rightBorderColor
				);

		if (gridCell.colSpan > 1 || gridCell.rowSpan > 1)
		{
			sheet.addMergedRegion(new Region(y, (short)x, (y + gridCell.rowSpan - 1), (short)(x + gridCell.colSpan - 1)));

			for(int i = 0; i < gridCell.rowSpan; i++)
			{
				HSSFRow spanRow = sheet.getRow(y + i); 
				if (spanRow == null)
				{
					spanRow = sheet.createRow(y + i);
				}
				for(int j = 0; j < gridCell.colSpan; j++)
				{
					HSSFCell spanCell = spanRow.getCell((short)(x + j));
					if (spanCell == null)
					{
						spanCell = spanRow.createCell((short)(x + j));
					}
					spanCell.setCellStyle(cellStyle);
				}
			}
		}

		cell = row.createCell((short)x);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		if (isAutoDetectCellType)
		{
			try
			{
				cell.setCellValue(Double.parseDouble(styledText.getText()));
			}
			catch(NumberFormatException e)
			{
				cell.setCellValue(styledText.getText());
			}
		}
		else
		{
			cell.setCellValue(styledText.getText());
		}
		cell.setCellStyle(cellStyle);
	}


	/**
	 *
	 */
	protected void layoutGrid(JRPrintPage alterYPage, JRPrintPage page)
	{
		xCuts = new ArrayList();
		yCuts = new ArrayList();

		xCuts.add(new Integer(0));
		xCuts.add(new Integer(jasperPrint.getPageWidth()));
		yCuts.add(new Integer(0));
		yCuts.add(new Integer(pageHeight));

		Integer x = null;
		Integer y = null;

		JRPrintElement alterYElement = null;
		JRPrintElement element = null;

		List alterYElems = alterYPage.getElements();
		List elems = page.getElements();
		for(int i = 0; i < elems.size(); i++)
		{
			alterYElement = (JRPrintElement)alterYElems.get(i);
			element = (JRPrintElement)elems.get(i);

			if (!(element instanceof JRPrintImage))
			{
				x = new Integer(element.getX() + globalOffsetX);
				if (!xCuts.contains(x))
				{
					xCuts.add(x);
				}
				x = new Integer(element.getX() + globalOffsetX + element.getWidth());
				if (!xCuts.contains(x))
				{
					xCuts.add(x);
				}
				y = new Integer(alterYElement.getY());
				if (!yCuts.contains(y))
				{
					yCuts.add(y);
				}
				y = new Integer(alterYElement.getY() + element.getHeight());
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
			alterYElement = (JRPrintElement)alterYElems.get(i);
			element = (JRPrintElement)elems.get(i);
			
			if (!(element instanceof JRPrintImage))
			{
				x1 = xCuts.indexOf(new Integer(element.getX() + globalOffsetX));
				y1 = yCuts.indexOf(new Integer(alterYElement.getY()));
				x2 = xCuts.indexOf(new Integer(element.getX() + globalOffsetX + element.getWidth()));
				y2 = yCuts.indexOf(new Integer(alterYElement.getY() + element.getHeight()));

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

					if (x2 - x1 != 0 && y2 - y1 != 0)
					{
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
	}


	/**
	 *
	 */
	protected static HSSFColor getNearestColor(Color awtColor)
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
	protected HSSFFont getLoadedFont(JRFont font, short forecolor)
	{
		HSSFFont cellFont = null;

		if (loadedFonts != null && loadedFonts.size() > 0)
		{
			HSSFFont cf = null;
			for (int i = 0; i < loadedFonts.size(); i++)
			{
				cf = (HSSFFont)loadedFonts.get(i);
				
				if (
					cf.getFontName().equals(font.getFontName()) &&
					(cf.getColor() == forecolor) &&
					(cf.getFontHeightInPoints() == (short)font.getSize()) &&
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
			cellFont.setFontHeightInPoints((short)font.getSize());

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
			
			loadedFonts.add(cellFont);
		}
			
		return cellFont;
	}


	/**
	 *
	 */
	protected HSSFCellStyle getLoadedCellStyle(
		short mode, 
		short backcolor, 
		short horizontalAlignment, 
		short verticalAlignment,
		short rotation,
		HSSFFont font,
		short topBorder,
		short topBorderColor,
		short leftBorder,
		short leftBorderColor,
		short bottomBorder,
		short bottomBorderColor,
		short rightBorder,
		short rightBorderColor
		)
	{
		HSSFCellStyle cellStyle = null;

		if (loadedCellStyles != null && loadedCellStyles.size() > 0)
		{
			HSSFCellStyle cs = null;
			for (int i = 0; i < loadedCellStyles.size(); i++)
			{
				cs = (HSSFCellStyle)loadedCellStyles.get(i);
				
				if (
					cs.getFillPattern() == mode 
					&& cs.getFillForegroundColor() == backcolor 
					&& cs.getAlignment() == horizontalAlignment 
					&& cs.getVerticalAlignment() == verticalAlignment 
					&& cs.getRotation() == rotation 
					&& cs.getFontIndex() == font.getIndex()
					&& cs.getBorderTop() == topBorder 
					&& cs.getTopBorderColor() == topBorderColor 
					&& cs.getBorderLeft() == leftBorder 
					&& cs.getLeftBorderColor() == leftBorderColor 
					&& cs.getBorderBottom() == bottomBorder 
					&& cs.getBottomBorderColor() == bottomBorderColor 
					&& cs.getBorderRight() == rightBorder 
					&& cs.getRightBorderColor() == rightBorderColor 
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
			cellStyle.setRotation(rotation);
			cellStyle.setFont(font);
			cellStyle.setWrapText(true);
			
			cellStyle.setBorderTop(topBorder);
			cellStyle.setTopBorderColor(topBorderColor);
			cellStyle.setBorderLeft(leftBorder);
			cellStyle.setLeftBorderColor(leftBorderColor);
			cellStyle.setBorderBottom(bottomBorder);
			cellStyle.setBottomBorderColor(bottomBorderColor);
			cellStyle.setBorderRight(rightBorder);
			cellStyle.setRightBorderColor(rightBorderColor);
			
			loadedCellStyles.add(cellStyle);
		}
			
		return cellStyle;
	}


	/**
	 *
	 */
	private static short getBorder(byte pen)
	{
		short border = HSSFCellStyle.BORDER_NONE;
		
		switch (pen)
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				border = HSSFCellStyle.BORDER_DASHED;
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				border = HSSFCellStyle.BORDER_THICK;
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				border = HSSFCellStyle.BORDER_THICK;
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				border = HSSFCellStyle.BORDER_THIN;
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				border = HSSFCellStyle.BORDER_NONE;
				break;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				border = HSSFCellStyle.BORDER_MEDIUM;
				break;
			}
		}
		
		return border;
	}


}

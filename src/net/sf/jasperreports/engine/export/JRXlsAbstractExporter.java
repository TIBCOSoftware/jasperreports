/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;

import org.xml.sax.SAXException;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRXlsAbstractExporter extends JRAbstractExporter
{

	protected static class TextAlignHolder
	{
		public final short horizontalAlignment;
		public final short verticalAlignment;
		public final short rotation;

		public TextAlignHolder(short horizontalAlignment, short verticalAlignment, short rotation)
		{
			this.horizontalAlignment = horizontalAlignment;
			this.verticalAlignment = verticalAlignment;
			this.rotation = rotation;
		}
	}
	
	/**
	 * 
	 */
	protected int pageHeight = 0;

	/**
	 * 
	 */
	protected List loadedFonts = new ArrayList();

	/**
	 * 
	 */
	protected boolean isOnePagePerSheet = false;

	protected boolean isRemoveEmptySpace = false;

	protected boolean isWhitePageBackground = true;

	protected boolean isAutoDetectCellType = true;
	protected boolean isDetectCellType = false;

	protected boolean isFontSizeFixEnabled = false;
	
	protected String[] sheetNames = null;

	/**
	 *
	 */
	protected JRStyledTextParser styledTextParser = new JRStyledTextParser();

	protected JRExportProgressMonitor progressMonitor = null;

	protected int reportIndex = 0;
	

	protected Map fontMap = null;

	private int skippedRows = 0;
	
	/**
	 *
	 */
	protected JRFont defaultFont = null;

	/**
	 * used for counting the total number of sheets
	 */
	protected int sheetIndex = 0;

	/**
	 * used when indexing the identical sheet generated names with ordering numbers;
	 * contains sheet names as keys and the number of occurences of each sheet name as values
	 */
	protected Map sheetNamesMap = null;
	
	/**
	 *
	 */
	protected JRFont getDefaultFont()
	{
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

		try
		{
			/*   */
			setExportContext();
	
			/*   */
			setInput();
	
			/*   */
			if (!isModeBatch)
			{
				setPageRange();
			}
	
			setParameters();
			
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
		finally
		{
			resetExportContext();
		}
	}

	protected void setParameters()
	{
		Boolean isOnePagePerSheetParameter = (Boolean)parameters.get(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET);
		if (isOnePagePerSheetParameter != null)
		{
			isOnePagePerSheet = isOnePagePerSheetParameter.booleanValue();
		}

		Boolean isRemoveEmptySpaceParameter = (Boolean)parameters.get(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS);
		if (isRemoveEmptySpaceParameter != null)
		{
			isRemoveEmptySpace = isRemoveEmptySpaceParameter.booleanValue();
		}
		
		Boolean isWhitePageBackgroundParameter = (Boolean)parameters.get(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND);
		if (isWhitePageBackgroundParameter != null)
		{
			isWhitePageBackground = isWhitePageBackgroundParameter.booleanValue();
			setBackground();
		}
		
		Boolean isAutoDetectCellTypeParameter = (Boolean)parameters.get(JRXlsAbstractExporterParameter.IS_AUTO_DETECT_CELL_TYPE);
		if (isAutoDetectCellTypeParameter != null)
		{
			isAutoDetectCellType = isAutoDetectCellTypeParameter.booleanValue();
		}
		
		Boolean isDetectCellTypeParameter = (Boolean) parameters.get(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE);
		isDetectCellType = isDetectCellTypeParameter != null && isDetectCellTypeParameter.booleanValue();

		Boolean isFontSizeFixEnabledParameter = (Boolean) this.parameters.get(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED);
		if (isFontSizeFixEnabledParameter != null)
		{
			isFontSizeFixEnabled = isFontSizeFixEnabledParameter.booleanValue();
		}

		sheetNames = (String[])parameters.get(JRXlsAbstractExporterParameter.SHEET_NAMES);

		fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);
	}

	protected abstract void setBackground();
	
	protected void exportReportToStream(OutputStream os) throws JRException
	{
		openWorkbook(os);
		sheetNamesMap = new HashMap();
		
		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
		{
			jasperPrint = (JasperPrint)jasperPrintList.get(reportIndex);
			defaultFont = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());

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
						
						if (sheetNames != null && sheetIndex < sheetNames.length)
						{
							createSheet(getSheetName(sheetNames[sheetIndex]));
						}
						else
						{
							createSheet("Page " + (sheetIndex + 1));
						}

						// we need to count all sheets generated for all exported documents
						sheetIndex++;
						
						/*   */
						exportPage(null, page);
					}
				}
				else
				{
					pageHeight = jasperPrint.getPageHeight() * (endPageIndex - startPageIndex + 1);

					List alterYs = new ArrayList();
					JRPrintPage allPages = new JRBasePrintPage();
					Collection elements = null;
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
							
								alterYs.add(new Integer(element.getY() + jasperPrint.getPageHeight() * pageIndex));
							}
						}
					}
					
					if (sheetNames != null && sheetIndex < sheetNames.length)
					{
						createSheet(getSheetName(sheetNames[sheetIndex]));
					}
					else
					{
						createSheet(getSheetName(jasperPrint.getName()));
					}
					
					// we need to count all sheets generated for all exported documents
					sheetIndex++;

					/*   */
					exportPage(alterYs, allPages);
				}
			}
		}

		closeWorkbook(os);
	}
	
	/**
	 *
	 */
	protected void exportPage(List alterYs, JRPrintPage page) throws JRException
	{
		JRGridLayout layout = new JRGridLayout(page.getElements(), alterYs,
				jasperPrint.getPageWidth(), pageHeight,
				globalOffsetX, globalOffsetY, getExporterElements(), true, true, false, null);

		JRExporterGridCell grid[][] = layout.getGrid();
		boolean isRowNotEmpty[] = layout.getIsRowNotEmpty();
		List xCuts = layout.getXCuts();

		int width = 0;
		for(int i = 1; i < xCuts.size(); i++)
		{
			width = ((Integer)xCuts.get(i)).intValue() - ((Integer)xCuts.get(i - 1)).intValue();
			setColumnWidth((short)(i - 1), (short)(width * 43));
		}

		skippedRows = 0;
		for(int y = 0; y < grid.length; y++)
		{
			int rowIndex = y - skippedRows;
			
			if (isRowNotEmpty[y] || !isRemoveEmptySpace)
			{
				JRExporterGridCell[] gridRow = grid[y];
				
				int emptyCellColSpan = 0;
				int emptyCellWidth = 0;
				int lastRowHeight = JRGridLayout.getRowHeight(gridRow);
				
				setRowHeight(rowIndex, lastRowHeight);
	
				for(int x = 0; x < gridRow.length; x++)
				{
					setCell(x, rowIndex);
	
					JRExporterGridCell gridCell = gridRow[x];
					if(gridCell.element != null)
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
	
						JRPrintElement element = gridCell.element;
	
						if (element instanceof JRPrintLine)
						{
							exportLine((JRPrintLine)element, gridCell, x, rowIndex);
						}
						else if (element instanceof JRPrintRectangle)
						{
							exportRectangle(element, gridCell, x, rowIndex);
						}
						else if (element instanceof JRPrintEllipse)
						{
							exportRectangle(element, gridCell, x, rowIndex);
						}
						else if (element instanceof JRPrintImage)
						{
							exportImage((JRPrintImage) element, gridCell, x, rowIndex);
						}
						else if (element instanceof JRPrintText)
						{
							exportText((JRPrintText)element, gridCell, x, rowIndex);
						}
						else if (element instanceof JRPrintFrame)
						{
							exportFrame((JRPrintFrame) element, gridCell, x, y);//FIXME rowIndex?
						}

						x += gridCell.colSpan - 1;
					}
					else
					{
						emptyCellColSpan++;
						emptyCellWidth += gridCell.width;
						addBlankCell(gridCell, x, rowIndex);
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
				skippedRows++;
//				setRowHeight(y, 0);
//
//				for(int x = 0; x < grid[y].length; x++)
//				{
//					addBlankCell(x, y);
//					setCell(x, y);
//				}
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

	protected static TextAlignHolder getTextAlignHolder(JRPrintText textElement)
	{
		short horizontalAlignment;
		short verticalAlignment;
		short rotation = textElement.getRotation();

		switch (textElement.getRotation())
		{
			case JRTextElement.ROTATION_LEFT :
			{
				switch (textElement.getHorizontalAlignment())
				{
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_BOTTOM;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_MIDDLE;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_TOP;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_BOTTOM;
					}
				}

				switch (textElement.getVerticalAlignment())
				{
					case JRAlignment.VERTICAL_ALIGN_TOP :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_LEFT;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_CENTER;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_RIGHT;
						break;
					}
					default :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_LEFT;
					}
				}

				break;
			}
			case JRTextElement.ROTATION_RIGHT :
			{
				switch (textElement.getHorizontalAlignment())
				{
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_TOP;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_MIDDLE;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_BOTTOM;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_TOP;
					}
				}

				switch (textElement.getVerticalAlignment())
				{
					case JRAlignment.VERTICAL_ALIGN_TOP :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_RIGHT;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_CENTER;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_LEFT;
						break;
					}
					default :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_RIGHT;
					}
				}

				break;
			}
			case JRTextElement.ROTATION_UPSIDE_DOWN:
			case JRTextElement.ROTATION_NONE :
			default :
			{
				horizontalAlignment = textElement.getHorizontalAlignment();
				verticalAlignment = textElement.getVerticalAlignment();
			}
		}

		return new TextAlignHolder(horizontalAlignment, verticalAlignment, rotation);
	}
	
	/**
	 *
	 */
	private String getSheetName(String sheetName)
	{
		// sheet names must be unique
		if(!sheetNamesMap.containsKey(sheetName))
		{
			// first time this sheet name is found;
			sheetNamesMap.put(sheetName, new Integer(1));
			return sheetName;
		}

		int currentIndex = ((Integer)sheetNamesMap.get(sheetName)).intValue() + 1;
		sheetNamesMap.put(sheetName, new Integer(currentIndex));
		return sheetName + " " + currentIndex;
	}
	
	protected abstract JRGridLayout.ExporterElements getExporterElements();

	protected abstract void openWorkbook(OutputStream os) throws JRException;
	
	protected abstract void createSheet(String name);

	protected abstract void closeWorkbook(OutputStream os) throws JRException;
	
	protected abstract void setColumnWidth (short index, short width);

	protected abstract void setRowHeight(int rowIndex, int lastRowHeight) throws JRException;

	protected abstract void setCell(int colIndex, int rowIndex);

	protected abstract void addBlankCell(JRExporterGridCell gridCell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportText(JRPrintText text, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportImage(JRPrintImage image, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportRectangle(JRPrintElement element, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportLine(JRPrintLine line, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;
	
	protected abstract void exportFrame(JRPrintFrame frame, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;
}

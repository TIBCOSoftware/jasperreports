package net.sf.jasperreports.engine.export;

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

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
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

import org.xml.sax.SAXException;

public abstract class JRXlsAbstractExporter extends JRAbstractExporter
{

	static class TextAlignHolder
	{
		final short horizontalAlignment;
		final short verticalAlignment;
		final short rotation;

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
	protected JRFont defaultFont = null;

	protected List loadedFonts = new ArrayList();

	protected List loadedCellStyles = new ArrayList();

	/**
	 * 
	 */
	protected boolean isOnePagePerSheet = false;

	protected boolean isRemoveEmptySpace = false;

	protected boolean isWhitePageBackground = true;

	protected boolean isAutoDetectCellType = true;

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
	protected JRStyledTextParser styledTextParser = new JRStyledTextParser();

	protected JRExportProgressMonitor progressMonitor = null;

	protected int reportIndex = 0;

	protected Map fontMap = null;

	
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

	protected void setParameters()
	{
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
			isWhitePageBackground = isWhitePageBackgroundParameter.booleanValue();
			setBackground();
		}
		
		Boolean isAutoDetectCellTypeParameter = (Boolean)parameters.get(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE);
		if (isAutoDetectCellTypeParameter != null)
		{
			isAutoDetectCellType = isAutoDetectCellTypeParameter.booleanValue();
		}

		fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);
	}

	protected abstract void setBackground();
	
	protected void exportReportToStream(OutputStream os) throws JRException
	{
		openWorkbook(os);
		
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

						createSheet("Page " + (pageIndex + 1));

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

					createSheet(jasperPrint.getName());

					/*   */
					exportPage(alterYAllPages, allPages);
				}
			}
		}

		closeWorkbook(os);
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

			if (isToExport(element))
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
			
			if (isToExport(element))
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
	protected void exportPage(JRPrintPage alterYPage, JRPrintPage page) throws JRException
	{
		layoutGrid(alterYPage, page);

		int width = 0;
		for(int i = 1; i < xCuts.size(); i++)
		{
			width = ((Integer)xCuts.get(i)).intValue() - ((Integer)xCuts.get(i - 1)).intValue();
			setColumnWidth((short)(i - 1), (short)(width * 43));
		}

		JRPrintElement element = null;
		for(int y = 0; y < grid.length; y++)
		{
			if (isRowNotEmpty[y] || !isRemoveEmptySpace)
			{
				int emptyCellColSpan = 0;
				int emptyCellWidth = 0;
				int lastRowHeight = grid[y][0].height;
				
				setRowHeight(y, lastRowHeight);
	
				int x = 0;
				for(x = 0; x < grid[y].length; x++)
				{
					setCell(x, y);
	
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
						else if (element instanceof JRPrintImage)
						{
							exportImage((JRPrintImage) element, grid[y][x], x, y);
						}
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
						addBlankCell(x, y);
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
				setRowHeight(y, 0);

				for(int x = 0; x < grid[y].length; x++)
				{
					addBlankCell(x, y);
					setCell(x, y);
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
			case JRTextElement.ROTATION_NONE :
			default :
			{
				horizontalAlignment = textElement.getHorizontalAlignment();
				verticalAlignment = textElement.getVerticalAlignment();
			}
		}

		return new TextAlignHolder(horizontalAlignment, verticalAlignment, rotation);
	}
	
	protected abstract boolean isToExport(JRPrintElement element);

	protected abstract void openWorkbook(OutputStream os) throws JRException;
	
	protected abstract void createSheet(String name);

	protected abstract void closeWorkbook(OutputStream os) throws JRException;
	
	protected abstract void setColumnWidth (short index, short width);

	protected abstract void setRowHeight(int y, int lastRowHeight) throws JRException;

	protected abstract void setCell(int x, int y);

	protected abstract void addBlankCell(int x, int y) throws JRException;

	protected abstract void exportText(JRPrintText text, JRExporterGridCell cell, int x, int y) throws JRException;

	protected abstract void exportImage(JRPrintImage image, JRExporterGridCell cell, int x, int y) throws JRException;

	protected abstract void exportRectangle(JRPrintElement element, JRExporterGridCell cell, int x, int y) throws JRException;

	protected abstract void exportLine(JRPrintLine line, JRExporterGridCell cell, int x, int y) throws JRException;
}

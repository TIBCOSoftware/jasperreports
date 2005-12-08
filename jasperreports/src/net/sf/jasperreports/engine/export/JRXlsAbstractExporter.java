package net.sf.jasperreports.engine.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
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
import net.sf.jasperreports.engine.base.JRBaseFont;
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

	/**
	 * 
	 */
	protected boolean isOnePagePerSheet = false;

	protected boolean isRemoveEmptySpace = false;

	protected boolean isWhitePageBackground = true;

	protected boolean isAutoDetectCellType = true;

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
		setClassLoader();

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

		/*   */
		resetClassLoader();
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

		sheetNames = (String[])parameters.get(JRXlsExporterParameter.SHEET_NAMES);

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

						if (sheetNames != null && pageIndex < sheetNames.length)
							createSheet(sheetNames[pageIndex]);
						else
							createSheet("Page " + (pageIndex + 1));

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

					createSheet(jasperPrint.getName());

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
				int emptyCellColSpan = 0;
				int emptyCellWidth = 0;
				int lastRowHeight = JRGridLayout.getRowHeight(grid, y);
				
				setRowHeight(rowIndex, lastRowHeight);
	
				for(int x = 0; x < grid[y].length; x++)
				{
					setCell(x, rowIndex);
	
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
	
						JRPrintElement element = grid[y][x].element;
	
						if (element instanceof JRPrintLine)
						{
							exportLine((JRPrintLine)element, grid[y][x], x, rowIndex);
						}
						else if (element instanceof JRPrintRectangle)
						{
							exportRectangle(element, grid[y][x], x, rowIndex);
						}
						else if (element instanceof JRPrintEllipse)
						{
							exportRectangle(element, grid[y][x], x, rowIndex);
						}
						else if (element instanceof JRPrintImage)
						{
							exportImage((JRPrintImage) element, grid[y][x], x, rowIndex);
						}
						else if (element instanceof JRPrintText)
						{
							exportText((JRPrintText)element, grid[y][x], x, rowIndex);
						}
						else if (element instanceof JRPrintFrame)
						{
							exportFrame((JRPrintFrame) element, grid[y][x], x, y);
						}

						x += grid[y][x].colSpan - 1;
					}
					else
					{
						emptyCellColSpan++;
						emptyCellWidth += grid[y][x].width;
						addBlankCell(grid[y][x], x, rowIndex);
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
			case JRTextElement.ROTATION_NONE :
			default :
			{
				horizontalAlignment = textElement.getHorizontalAlignment();
				verticalAlignment = textElement.getVerticalAlignment();
			}
		}

		return new TextAlignHolder(horizontalAlignment, verticalAlignment, rotation);
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

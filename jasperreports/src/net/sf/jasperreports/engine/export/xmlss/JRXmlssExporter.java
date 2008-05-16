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

/*
 * Special thanks to Google 'Summer of Code 2005' program for supporting this development
 *
 * Contributors:
 * Majid Ali Khan - majidkk@users.sourceforge.net
 * Frank Schönheit - Frank.Schoenheit@Sun.COM
 */
package net.sf.jasperreports.engine.export.xmlss;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintElementIndex;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.export.CutsInfo;
import net.sf.jasperreports.engine.export.ExporterNature;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRGridLayout;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.JRHyperlinkProducerFactory;
import net.sf.jasperreports.engine.export.JROriginExporterFilter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ResetableExporterFilter;
import net.sf.jasperreports.engine.export.data.BooleanTextValue;
import net.sf.jasperreports.engine.export.data.DateTextValue;
import net.sf.jasperreports.engine.export.data.NumberTextValue;
import net.sf.jasperreports.engine.export.data.TextValue;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * Exports a JasperReports document to XML Spreadsheet format. It has character output type and exports the document to a
 * grid-based layout.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlssExporter extends JRAbstractExporter
{

	private static final String ODT_ORIGIN_EXPORTER_FILTER_PREFIX = JRProperties.PROPERTY_PREFIX + "export.odt.exclude.origin.";

	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";

	/**
	 *
	 */
	protected static final String HORIZONTAL_ALIGN_LEFT = "start";
	protected static final String HORIZONTAL_ALIGN_RIGHT = "end";
	protected static final String HORIZONTAL_ALIGN_CENTER = "center";
	protected static final String HORIZONTAL_ALIGN_JUSTIFY = "justified";

	/**
	 *
	 */
	protected static final String VERTICAL_ALIGN_TOP = "top";
	protected static final String VERTICAL_ALIGN_MIDDLE = "middle";
	protected static final String VERTICAL_ALIGN_BOTTOM = "bottom";

	public static final String IMAGE_NAME_PREFIX = "img_";
	protected static final int IMAGE_NAME_PREFIX_LEGTH = IMAGE_NAME_PREFIX.length();

	protected static final String[] PAGE_LAYOUT = new String[] {"Portrait", "Portrait", "Landscape"};
	
	/**
	 *
	 */
	protected Writer tempBodyWriter = null;
	protected Writer tempStyleWriter = null;

	protected JRExportProgressMonitor progressMonitor = null;
	protected Map rendererToImagePathMap = null;
	protected Map imageMaps;
	protected List imagesToProcess = null;

	protected int reportIndex = 0;
	protected int pageIndex = 0;
	protected int tableIndex = 0;
	protected boolean startPage;

	/**
	 *
	 */
	protected String encoding = null;


	/**
	 *
	 */
	protected boolean isWrapBreakWord = false;

	protected Map fontMap = null;

	private LinkedList backcolorStack;
	private Color backcolor;

	private XmlssStyleCache styleCache = null;

	protected JRHyperlinkProducerFactory hyperlinkProducerFactory;
	protected ExporterNature nature = null;

	protected File destFile = null;

	/**
	 *
	 */
	protected boolean isOnePagePerSheet;
	protected boolean isRemoveEmptySpaceBetweenRows;
	protected boolean isRemoveEmptySpaceBetweenColumns;
	protected boolean isWhitePageBackground;
	protected boolean isAutoDetectCellType = false;
	protected boolean isDetectCellType;
	protected boolean isFontSizeFixEnabled;
	protected boolean isIgnoreGraphics;
	protected boolean isCollapseRowSpan;
	protected boolean isIgnoreCellBorder;
	protected boolean isIgnorePageMargins;

	protected int maxRowsPerSheet;

	protected String[] sheetNames = null;

	/**
	 * used for counting the total number of sheets
	 */
	protected int sheetIndex = 0;
	
	/**
	 * used when indexing the identical sheet generated names with ordering numbers;
	 * contains sheet names as keys and the number of occurences of each sheet name as values
	 */
	protected Map sheetNamesMap = null;
	protected String currentSheetName = null;
	
	/**
	 *
	 */
	protected JRFont defaultFont = null;
	
	protected Map formatPatternsMap = null;
	
	protected byte pageOrientation;
	
	public JRXmlssExporter()
	{
		backcolorStack = new LinkedList();
		backcolor = null;
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

			if (!parameters.containsKey(JRExporterParameter.FILTER))
			{
				filter = JROriginExporterFilter.getFilter(jasperPrint.getPropertiesMap(), ODT_ORIGIN_EXPORTER_FILTER_PREFIX);
			}

			/*   */
			if (!isModeBatch)
			{
				setPageRange();
			}

			setParameters();
			
			nature = new JRXmlssExporterNature(filter, isIgnorePageMargins);
			pageOrientation = jasperPrint.getOrientation();
			
			StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
			if (sb != null)
			{
				StringBuffer buffer = exportReportToBuffer();
				sb.append(buffer.toString());
			}
			else
			{
				Writer outWriter = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
				if (outWriter != null)
				{
					try
					{
						exportReportToStream(outWriter);
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to writer : " + jasperPrint.getName(), e);
					}
				}
				else
				{
					OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
					if (os != null)
					{
						try
						{
							exportReportToStream(new OutputStreamWriter(os, encoding));
						}
						catch (Exception e)
						{
							throw new JRException("Error writing to OutputStream : " + jasperPrint.getName(), e);
						}
					}
					else
					{
						destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
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
						
						exportReportToFile();
					}
				}
			}
		}
		finally
		{
			resetExportContext();
		}

	}


	/**
	*
	*/
	protected void exportReportToFile() throws JRException
	{
		Writer writer = null;
		try
		{
			OutputStream fileOutputStream = new FileOutputStream(destFile);
			writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, encoding));
			exportReportToStream(writer);
		}
		catch (IOException e)
		{
			throw new JRException("Error writing to file : " + destFile, e);
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch(IOException e)
				{
				}
			}
		}
		
	}


	/**
	*
	*/
	protected StringBuffer exportReportToBuffer() throws JRException
	{
		StringWriter buffer = new StringWriter();
		try
		{
			exportReportToStream(buffer);
		}
		catch (IOException e)
		{
			throw new JRException("Error while exporting report to buffer", e);
		}
		return buffer.getBuffer();
	}

	protected void setHyperlinkProducerFactory()
	{
		hyperlinkProducerFactory = (JRHyperlinkProducerFactory) parameters.get(JRExporterParameter.HYPERLINK_PRODUCER_FACTORY);
	}


	/**
	 *
	 */
	protected void exportReportToStream(Writer writer) throws JRException, IOException
	{
		tempBodyWriter = new StringWriter();
		tempStyleWriter = new StringWriter();

		styleCache = new XmlssStyleCache(tempStyleWriter, fontMap);

		sheetNamesMap = new HashMap();
		sheetNamesMap.put("Page", new Integer(0)); // in order to skip first sheet name that would have no index

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
					for(int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
					{
						if (Thread.currentThread().isInterrupted())
						{
							throw new JRException("Current thread interrupted.");
						}

						JRPrintPage page = (JRPrintPage)pages.get(pageIndex);

						if (sheetNames != null && sheetIndex < sheetNames.length)
						{
							tempBodyWriter.write("<Worksheet ss:Name=\""+getSheetName(sheetNames[sheetIndex])+"\">\n");
						}
						else
						{
							tempBodyWriter.write("<Worksheet ss:Name=\""+getSheetName("Page")+"\">\n");
						}

						// we need to count all sheets generated for all exported documents
						sheetIndex++;
						
						tempBodyWriter.write("<Table>\n");

						exportPage(page, null, 0, null, true);
						
						tempBodyWriter.write("</Table>\n");
						
						closeWorksheet();
					}
				}
				else
				{
					// Create the sheet before looping.
					if (sheetNames != null && sheetIndex < sheetNames.length)
					{
						tempBodyWriter.write("<Worksheet ss:Name=\""+getSheetName(sheetNames[sheetIndex])+"\">\n");
					}
					else
					{
						tempBodyWriter.write("<Worksheet ss:Name=\""+getSheetName(jasperPrint.getName())+"\">\n");
					}
					tempBodyWriter.write("<Table>\n");

					// we need to count all sheets generated for all exported documents
					sheetIndex++;

					/*
					 * Make a pass and calculate the X cuts for all pages on this sheet.
					 * The Y cuts can be calculated as each page is exported.
					 */
					CutsInfo xCuts = 
						JRGridLayout.calculateXCuts(
							nature, pages, startPageIndex, endPageIndex,
							jasperPrint.getPageWidth(), globalOffsetX
							);
					//clear the filter's internal cache that might have built up
					if (filter instanceof ResetableExporterFilter)
						((ResetableExporterFilter)filter).reset();
					
					int startRow = 0;

					for(int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
					{
						if (Thread.currentThread().isInterrupted())
						{
							throw new JRException("Current thread interrupted.");
						}
						JRPrintPage page = (JRPrintPage)pages.get(pageIndex);
						
						startRow = exportPage(page, xCuts, startRow, null, pageIndex == startPageIndex);
					}
					
					if (isRemoveEmptySpaceBetweenColumns)
					{
						//FIXME: to remove empty columns when isRemoveEmptySpaceBetweenColumns
						//removeEmptyColumns(xCuts);
					}
					tempBodyWriter.write("</Table>\n");
					closeWorksheet();
				}
			}
			
		}

		tempBodyWriter.flush();
		tempStyleWriter.flush();

		tempBodyWriter.close();
		tempStyleWriter.close();


		/*   */
		XmlssContentBuilder xmlssContentBuilder =
			new XmlssContentBuilder(
				writer,
				tempStyleWriter,
				tempBodyWriter
				);
		xmlssContentBuilder.build();
		
	}

	/**
	 *
	 * @return the number of rows added.
	 */
	protected int exportPage(JRPrintPage page, CutsInfo xCuts, int startRow, JRPrintElementIndex frameIndex, boolean isNewSheet) throws JRException
	{
		try 
		{
	        JRGridLayout layout =
	        	new JRGridLayout(
	        		nature,
	        		page.getElements(),
	        		jasperPrint.getPageWidth(),
	        		jasperPrint.getPageHeight(),
	        		globalOffsetX,
	        		globalOffsetY,
	        		xCuts
	        		);

	        JRExporterGridCell grid[][] = layout.getGrid();

	        boolean createXCuts = (xCuts == null); 
	        
	        if (createXCuts) 
	        {
	        	xCuts = layout.getXCuts();
	        }

	        CutsInfo yCuts = layout.getYCuts();

	        int skippedRows = 0;
	        int rowIndex = startRow;

	        XmlssTableBuilder tableBuilder = frameIndex == null
	        ? new XmlssTableBuilder(reportIndex, pageIndex, tempBodyWriter, tempStyleWriter)
	        : new XmlssTableBuilder(frameIndex.toString(), tempBodyWriter, tempStyleWriter);
	        
	        if(isNewSheet)
	        {
	        	buildColumns(xCuts, tableBuilder);
	        }

	        for(int y = 0; y < grid.length; y++)
	        {
	        	rowIndex = y - skippedRows + startRow;

	        	//if number of rows is too large a new sheet is created and populated with remaining rows
	        	if(maxRowsPerSheet > 0 && rowIndex >= maxRowsPerSheet)
	        	{
	        		tableBuilder.buildTableFooter();
	        		closeWorksheet();
	        		tempBodyWriter.write("<Worksheet ss:Name=\""+getSheetName(currentSheetName)+"\">\n");
	    	        tableBuilder.buildTableHeader();
	    	        buildColumns(xCuts, tableBuilder);
	        		startRow = 0;
	        		rowIndex = 0;
	        		skippedRows = y;
	        	}
	        	
	        	if (
	        		yCuts.isCutNotEmpty(y)
	        		|| ((!isRemoveEmptySpaceBetweenRows || yCuts.isCutSpanned(y))
	        		&& !isCollapseRowSpan)
	        		)
	        	{
	        		JRExporterGridCell[] gridRow = grid[y];

	        		int emptyCellColSpan = 0;
	        		int emptyCellRowSpan = 0;
	        		int emptyCellWidth = 0;
	        		int rowHeight = isCollapseRowSpan ? JRGridLayout.getMaxRowHeight(gridRow) : JRGridLayout.getRowHeight(gridRow);

	        		tableBuilder.buildRowHeader(rowIndex, rowHeight);

	        		int emptyCols = 0;
	        		for(int colIndex = 0; colIndex < gridRow.length; colIndex++)
	        		{
	        			emptyCols += (isRemoveEmptySpaceBetweenColumns && (!(xCuts.isCutNotEmpty(colIndex) || xCuts.isCutSpanned(colIndex))) ? 1 : 0);
	        			
	        			JRExporterGridCell gridCell = gridRow[colIndex];
	        			if(gridCell.getWrapper() != null)
	        			{
							if (emptyCellColSpan > 0)
							{
								tableBuilder.buildEmptyCell(emptyCellColSpan, emptyCellRowSpan);
								emptyCellColSpan = 0;
								emptyCellWidth = 0;
							}

	        				JRPrintElement element = gridCell.getWrapper().getElement();

	        				if (element instanceof JRPrintLine)
	        				{
	        					//exportLine((JRPrintLine)element, gridCell, colIndex, rowIndex);
	        				}
	        				else if (element instanceof JRPrintRectangle)
	        				{
	        					//exportRectangle((JRPrintRectangle)element, gridCell, colIndex, rowIndex);
	        				}
	        				else if (element instanceof JRPrintEllipse)
	        				{
	        					//exportRectangle((JRPrintEllipse)element, gridCell, colIndex, rowIndex);
	        				}
	        				else if (element instanceof JRPrintImage)
	        				{
	        					//exportImage((JRPrintImage) element, gridCell, colIndex, rowIndex, emptyCols);
	        				}
	        				else if (element instanceof JRPrintText)
	        				{
	        					exportText(tableBuilder, (JRPrintText)element, gridCell);
	        				}
	        				else if (element instanceof JRPrintFrame)
	        				{
	        					//exportFrame(tableBuilder, (JRPrintFrame)element, gridCell);
	        				}

	        				colIndex += gridCell.getColSpan() - 1;
	        			}
	        			else
	        			{
	        				emptyCellColSpan++;
	        				emptyCellWidth += gridCell.getWidth();
	        			}
	        		}

					if (emptyCellColSpan > 0)
					{
						tableBuilder.buildEmptyCell(emptyCellColSpan, emptyCellRowSpan);
						emptyCellColSpan = 0;
						emptyCellWidth = 0;
					}
					
	        		tableBuilder.buildRowFooter();
	        		//increment row index to return proper value
	        		++rowIndex;
	        	}
	        	else
	        	{
	        		skippedRows++;
	        	}
	        }

	        if (createXCuts && isRemoveEmptySpaceBetweenColumns)
	        {
	        	//FIXME: to remove empty columns when isRemoveEmptySpaceBetweenColumns
//				removeEmptyColumns(xCuts);
	        }
	        
	        if (progressMonitor != null)
	        {
	        	progressMonitor.afterPageExport();
	        }

	        // Return the number of rows added
	        return rowIndex;
        } 
		catch (IOException e) 
		{
	        throw new JRException(e);
        }
	}

	/**
	 *
	 */
	protected void exportText(XmlssTableBuilder tableBuilder, JRPrintText text, JRExporterGridCell gridCell) throws IOException
	{
		JRStyledText styledText = getStyledText(text);
		String pattern = getConvertedPattern(getTextValue(text,styledText.getText()));
		int colspan = gridCell.getColSpan(), rowspan = gridCell.getRowSpan();
		
		String formula = text.getPropertiesMap().getProperty(JRAbstractExporter.PROPERTY_CELL_FORMULA);
		
		//FIXME: transfer the font properties to the cell style
		tableBuilder.buildCellHeader(styleCache.getCellStyle(text, gridCell.getBackcolor(), pattern, isFontSizeFixEnabled, defaultFont, fontMap), 
				colspan, 
				rowspan, 
				getHyperlinkURL(text), 
				text.getHyperlinkTooltip(),
				formula
				);


		int textLength = 0;

		if (styledText != null)
		{
			textLength = styledText.length();
		}

		tempBodyWriter.write(" <ss:Data ss:Type=\""+getType(getTextValue(text,styledText.getText()))+"\" xmlns=\"http://www.w3.org/TR/REC-html40\">");

		//FIXME: when the font properties will be transfered to the cell style, uncomment this 
//		if(JRCommonText.MARKUP_NONE.equals(text.getMarkup()))
//		{
//			tempBodyWriter.write((JRStringUtil.xmlEncode(styledText.getText())).replaceAll("\n", "&#10;"));
//		}
//		else if (textLength > 0)
		if (textLength > 0)
		{
			exportStyledText(styledText);
		}

		tempBodyWriter.write(" </ss:Data>\n");

		tableBuilder.buildCellFooter();
	}


	/**
	 *
	 */
	protected void exportStyledText(JRStyledText styledText) throws IOException
	{
		String text = styledText.getText();
		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			exportStyledTextRun(iterator.getAttributes(), text.substring(iterator.getIndex(), runLimit));

			iterator.setIndex(runLimit);
		}
	}


	/**
	 *
	 */
	protected void exportStyledTextRun(Map attributes, String text) throws IOException
	{
		boolean hasFont = false;
		boolean isBold = false;
		boolean isItalic = false;
		boolean isStrikethrough = false;
		boolean isUnderline = false;
		boolean isSubscript = false;
		boolean isSuperscript = false;

		if (TextAttribute.WEIGHT_BOLD.equals(attributes.get(TextAttribute.WEIGHT)))
		{
			isBold = true;
			tempBodyWriter.write("<B>");
		}
		if (TextAttribute.POSTURE_OBLIQUE.equals(attributes.get(TextAttribute.POSTURE)))
		{
			isItalic = true;
			tempBodyWriter.write("<I>");
		}
		if (TextAttribute.UNDERLINE_ON.equals(attributes.get(TextAttribute.UNDERLINE)))
		{
			isUnderline = true;
			tempBodyWriter.write("<U>");
		}
		if (TextAttribute.STRIKETHROUGH_ON.equals(attributes.get(TextAttribute.STRIKETHROUGH)))
		{
			isStrikethrough = true;
			tempBodyWriter.write("<S>");
		}

		if (TextAttribute.SUPERSCRIPT_SUPER.equals(attributes.get(TextAttribute.SUPERSCRIPT)))
		{
			isSuperscript = true;
			tempBodyWriter.write("<Sup>");
		}
		else if (TextAttribute.SUPERSCRIPT_SUB.equals(attributes.get(TextAttribute.SUPERSCRIPT)))
		{
			isSubscript = true;
			tempBodyWriter.write("<Sub>");
		}
		
//		String fontFamilyAttr = (String)attributes.get(TextAttribute.FAMILY);
//		String fontFamily = (fontMap != null && fontMap.containsKey(fontFamilyAttr)) 
//			? (String) fontMap.get(fontFamilyAttr) 
//			: fontFamilyAttr;
//
//		if(fontFamily != null)
//		{
//			hasFont = true;
//			startFontTag();
//			tempBodyWriter.write(" x:Family=\"" + fontFamily+"\"");
//		}
		
		Font font = (Font)attributes.get(TextAttribute.FONT);
		if(font != null)
		{
			String fontFace = font.getFontName();
			if(fontFace != null)
			{
				if(!hasFont)
				{
					hasFont = true;
					startFontTag();
				}
				tempBodyWriter.write(" html:Face=\"" + fontFace +"\"");
			}

			int size = font.getSize();
			if(size > 0)
			{
				if(!hasFont)
				{
					hasFont = true;
					startFontTag();
				}
				tempBodyWriter.write(" html:Size=\"" + size +"\"");
			}
		}
		else
		{
			//FIXME: how to distinguish between font name and font family
			String fontFamilyAttr = (String)attributes.get(TextAttribute.FAMILY);
			String fontFamily = (fontMap != null && fontMap.containsKey(fontFamilyAttr)) 
				? (String) fontMap.get(fontFamilyAttr) 
				: fontFamilyAttr;
			if(fontFamily != null)
			{
				if(!hasFont)
				{
					hasFont = true;
					startFontTag();
				}
				tempBodyWriter.write(" html:Face=\"" + fontFamily +"\"");
			}

			Number size = (Number)attributes.get(TextAttribute.SIZE);
			if(size != null)
			{
				if(! hasFont)
				{
					hasFont = true;
					startFontTag();
				}
				tempBodyWriter.write(" html:Size=\"" + size +"\"");
			}
		}
		
		Color forecolor = (Color)attributes.get(TextAttribute.FOREGROUND);
		if(forecolor != null && !forecolor.equals(Color.BLACK))
		{
			if(!hasFont)
			{
				hasFont =true;
				startFontTag();
			}
			tempBodyWriter.write(" html:Color=\"#" + JRColorUtil.getColorHexa(forecolor)+"\"");
		}
		
		if(hasFont)
		{
			tempBodyWriter.write(">");
		}
		
		if (text != null)
		{
			tempBodyWriter.write((JRStringUtil.xmlEncode(text)).replaceAll("\n", "&#10;"));
		}

		if(hasFont)
		{
			tempBodyWriter.write("</Font>");
		}
		
		if(isSubscript)
		{
			tempBodyWriter.write("</Sub>");
		}
		
		if(isSuperscript)
		{
			tempBodyWriter.write("</Sup>");
		}
		
		if(isStrikethrough)
		{
			tempBodyWriter.write("</S>");
		}
		
		if(isUnderline)
		{
			tempBodyWriter.write("</U>");
		}
		
		if(isItalic)
		{
			tempBodyWriter.write("</I>");
		}
		
		if(isBold)
		{
			tempBodyWriter.write("</B>");
		}
	}
	
	/**
	 * 
	 * @param gridCell
	 * @return
	 */
	protected JRPrintElementIndex getElementIndex(JRExporterGridCell gridCell)
	{
		JRPrintElementIndex imageIndex =
			new JRPrintElementIndex(
					reportIndex,
					pageIndex,
					gridCell.getWrapper().getAddress()
					);
		return imageIndex;
	}


	/**
	 *
	 */
	public static JRPrintElementIndex getPrintElementIndex(String imageName)
	{
		if (!imageName.startsWith(IMAGE_NAME_PREFIX))
		{
			throw new JRRuntimeException("Invalid image name: " + imageName);
		}

		return JRPrintElementIndex.parsePrintElementIndex(imageName.substring(IMAGE_NAME_PREFIX_LEGTH));
	}


	/**
	 *
	 */
	protected void exportFrame(XmlssTableBuilder tableBuilder, JRPrintFrame frame, JRExporterGridCell gridCell) throws IOException, JRException
	{
		tableBuilder.buildCellHeader(styleCache.getCellStyle(frame, gridCell.getBackcolor(), null, isFontSizeFixEnabled, defaultFont, fontMap), gridCell.getColSpan(), gridCell.getRowSpan(), null, null, null);

		boolean appendBackcolor =
			frame.getMode() == JRElement.MODE_OPAQUE
			&& (backcolor == null || frame.getBackcolor().getRGB() != backcolor.getRGB());

		if (appendBackcolor)
		{
			setBackcolor(frame.getBackcolor());
		}
		try
		{
			JRGridLayout layout = gridCell.getLayout();
			JRPrintElementIndex frameIndex =
				new JRPrintElementIndex(
						reportIndex,
						pageIndex,
						gridCell.getWrapper().getAddress()
						);
			//FIXME: make it functional
//			exportPage(layout, frameIndex);
		}
		finally
		{
			if (appendBackcolor)
			{
				restoreBackcolor();
			}
		}

		tableBuilder.buildCellFooter();
	}

	/**
	 *
	 */
	protected void setBackcolor(Color color)
	{
		backcolorStack.addLast(backcolor);

		backcolor = color;
	}


	protected void restoreBackcolor()
	{
		backcolor = (Color) backcolorStack.removeLast();
	}

	protected void writeHyperlink(JRPrintHyperlink link) throws IOException
	{
		String href = getHyperlinkURL(link);
		if(link != null)
		{
			tempBodyWriter.write(" ss:Href=\"");
			tempBodyWriter.write(href);
			tempBodyWriter.write("\"");

			if (link.getHyperlinkTooltip() != null)
			{
				tempBodyWriter.write(" x:HRefScreenTip=\"");
				tempBodyWriter.write(JRStringUtil.xmlEncode(link.getHyperlinkTooltip()));
				tempBodyWriter.write("\"");
			}

			tempBodyWriter.write(">");
			
		}
	}

	protected String getHyperlinkURL(JRPrintHyperlink link)
	{
		//FIXME: find the appropriate way to write the local anchor and local page hyperlinks
		String href = null;
		JRHyperlinkProducer customHandler = getCustomHandler(link);
		if (customHandler == null)
		{
			switch(link.getHyperlinkType())
			{
				case JRHyperlink.HYPERLINK_TYPE_REFERENCE :
				{
					if (link.getHyperlinkReference() != null)
					{
						href = link.getHyperlinkReference();
					}
					break;
				}
				case JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR :
				{
					if (link.getHyperlinkAnchor() != null)
					{
						href = "#" + link.getHyperlinkAnchor();
					}
					break;
				}
				case JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE :
				{
					if (link.getHyperlinkPage() != null)
					{
						href = "#" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString();
					}
					break;
				}
				case JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR :
				{
					if (
						link.getHyperlinkReference() != null &&
						link.getHyperlinkAnchor() != null
						)
					{
						href = link.getHyperlinkReference() + "#" + link.getHyperlinkAnchor();
					}
					break;
				}
				case JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE :
				{
					if (
						link.getHyperlinkReference() != null &&
						link.getHyperlinkPage() != null
						)
					{
						href = link.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + link.getHyperlinkPage().toString();
					}
					break;
				}
				case JRHyperlink.HYPERLINK_TYPE_NONE :
				default :
				{
					break;
				}
			}
		}
		else
		{
			href = customHandler.getHyperlink(link);
		}

		return href;
	}


	protected JRHyperlinkProducer getCustomHandler(JRPrintHyperlink link)
	{
		return hyperlinkProducerFactory == null ? null : hyperlinkProducerFactory.getHandler(link.getLinkType());
	}


	protected void setParameters()
	{
		isOnePagePerSheet = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,
				JRXlsAbstractExporterParameter.PROPERTY_ONE_PAGE_PER_SHEET,
				false
				);

		isRemoveEmptySpaceBetweenRows = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				false
				);

		isRemoveEmptySpaceBetweenColumns = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
				JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
				false
				);

		isWhitePageBackground = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND,
				JRXlsAbstractExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND,
				false
				);

		Boolean isAutoDetectCellTypeParameter = (Boolean)parameters.get(JRXlsAbstractExporterParameter.IS_AUTO_DETECT_CELL_TYPE);
		if (isAutoDetectCellTypeParameter != null)
		{
			isAutoDetectCellType = isAutoDetectCellTypeParameter.booleanValue();
		}

		isDetectCellType = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE,
				JRXlsAbstractExporterParameter.PROPERTY_DETECT_CELL_TYPE,
				false
				);

		isFontSizeFixEnabled = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_FONT_SIZE_FIX_ENABLED,
				JRXlsAbstractExporterParameter.PROPERTY_FONT_SIZE_FIX_ENABLED,
				false
				);

		isIgnoreGraphics = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_IGNORE_GRAPHICS,
				JRXlsAbstractExporterParameter.PROPERTY_IGNORE_GRAPHICS,
				false
				);

		isCollapseRowSpan = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_COLLAPSE_ROW_SPAN,
				JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN,
				false
				);

		isIgnoreCellBorder = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_IGNORE_CELL_BORDER,
				JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BORDER,
				false
				);

		sheetNames = (String[])parameters.get(JRXlsAbstractExporterParameter.SHEET_NAMES);

		fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);
		formatPatternsMap = (Map)getParameter(JRXlsExporterParameter.FORMAT_PATTERNS_MAP);

		maxRowsPerSheet = 
			getIntegerParameter(
				JRXlsAbstractExporterParameter.MAXIMUM_ROWS_PER_SHEET,
				JRXlsAbstractExporterParameter.PROPERTY_MAXIMUM_ROWS_PER_SHEET,
				0
				);
		
		isIgnorePageMargins = 
			getBooleanParameter(
				JRExporterParameter.IGNORE_PAGE_MARGINS, 
				JRExporterParameter.PROPERTY_IGNORE_PAGE_MARGINS, 
				false
				); 
		
		encoding =
			getStringParameterOrDefault(
				JRExporterParameter.CHARACTER_ENCODING,
				JRExporterParameter.PROPERTY_CHARACTER_ENCODING
				);

		setHyperlinkProducerFactory();
		
	}

	protected void startFontTag() throws IOException
	{
		tempBodyWriter.write("<Font");
	}
	
	protected void endFontTag() throws IOException
	{
		tempBodyWriter.write("</Font>");
	}
	/**
	 *
	 */
	private String getSheetName(String sheetName)
	{
		currentSheetName = sheetName;

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
	
	protected void buildColumns(CutsInfo xCuts, XmlssTableBuilder tableBuilder)
	{
		for(int col = 0; col < xCuts.size() - 1; col++)
		{
			if (!isRemoveEmptySpaceBetweenColumns || (xCuts.isCutNotEmpty(col) || xCuts.isCutSpanned(col)))
			{
				int width = xCuts.getCut(col + 1) - xCuts.getCut(col);
				try {
	                tableBuilder.buildColumnTag(col+1, width);
                } catch (IOException e) {
	                throw new JRRuntimeException(e);
                }
			}
		}
	}
	
	String getType(TextValue textValue)
	{
		String type = "String";
		if(textValue instanceof NumberTextValue)
		{
			type = "Number";
		}
		else if(textValue instanceof DateTextValue)
		{
			type = "DateTime";
		}
		else if(textValue instanceof BooleanTextValue)
		{
			type = "Boolean";
		}
		return type;
	}

	/**
	 * This method is intended to modify a given format pattern so to include
	 * only the accepted proprietary format characters. The resulted pattern
	 * will possibly truncate the original pattern
	 * @param pattern
	 * @return pattern converted to accepted proprietary formats
	 */
	String getConvertedPattern(TextValue textValue)
	{
		String pattern = "General";
		if(textValue instanceof NumberTextValue && ((NumberTextValue)textValue).getPattern() != null)
		{
			pattern = ((NumberTextValue)textValue).getPattern();
		}
		else if(textValue instanceof DateTextValue && ((DateTextValue)textValue).getPattern() != null)
		{
			pattern = ((DateTextValue)textValue).getPattern();
		}
		if (formatPatternsMap != null && formatPatternsMap.containsKey(pattern))
		{
			return (String) formatPatternsMap.get(pattern);
		}
		return pattern;
	}
	
	private void closeWorksheet() throws IOException
	{
		tempBodyWriter.write("<x:WorksheetOptions>\n");
		tempBodyWriter.write(" <x:PageSetup>\n");
		tempBodyWriter.write("  <x:Layout x:Orientation=\"" +PAGE_LAYOUT[pageOrientation]+"\"/>\n");
		tempBodyWriter.write(" </x:PageSetup>\n");
		tempBodyWriter.write("</x:WorksheetOptions>\n");
		tempBodyWriter.write("</Worksheet>\n");
	}
}


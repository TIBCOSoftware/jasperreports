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
 * Contributors:
 * Alex Parfenov - aparfeno@users.sourceforge.net
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export.odt;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintElementIndex;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRGridLayout;
import net.sf.jasperreports.engine.export.JRHyperlinkProducerFactory;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * Exports a JasperReports document to ODF format. It has character output type and exports the document to a
 * grid-based layout.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRHtmlExporter.java 1600 2007-02-23 15:12:16Z shertage $
 */
public class JROdtExporter extends JRAbstractExporter
{

	/**
	 *
	 */
	public static final String VERSION = "1.0";

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

	/**
	 *
	 */
	protected OasisZip oasisZip = new OasisZip();
	protected Writer tempBodyWriter = null;
	protected Writer tempStyleWriter = null;

	protected JRExportProgressMonitor progressMonitor = null;
	protected Map rendererToImagePathMap = null;
	protected Map imageMaps;
	protected List imagesToProcess = null;

	protected int reportIndex = 0;
	protected int pageIndex = 0;
	protected int tableIndex = 0;

	/**
	 *
	 */
	protected String encoding = null;


	/**
	 *
	 */
	protected static final int colorMask = Integer.parseInt("FFFFFF", 16);

	protected boolean isWrapBreakWord = false;

	protected Map fontMap = null;

	private LinkedList backcolorStack;
	private Color backcolor;
	
	private Set fontFaces = null;
	private Map paragraphStyles = null;//FIXMEODT these should be soft cache
	private int paragraphStylesCounter = 0;
	private Map textSpanStyles = null;
	private int textSpanStylesCounter = 0;

	protected JRHyperlinkProducerFactory hyperlinkProducerFactory;


	public JROdtExporter()
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
	
			/*   */
			if (!isModeBatch)
			{
				setPageRange();
			}
	
			encoding = (String)parameters.get(JRExporterParameter.CHARACTER_ENCODING);
			if (encoding == null)
			{
				encoding = "UTF-8";
			}
	
			rendererToImagePathMap = new HashMap();
			imageMaps = new HashMap();
			imagesToProcess = new ArrayList();
			
			fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);
						
			setHyperlinkProducerFactory();
	
			OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
			if (os != null)
			{
				try
				{
					exportReportToOasisZip();
					oasisZip.zipEntries(os);
				}
				catch (IOException e)
				{
					throw new JRException("Error trying to export to output stream : " + jasperPrint.getName(), e);
				}
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
					exportReportToOasisZip();
					oasisZip.zipEntries(os);
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
	
//			if ((imagesToProcess != null && imagesToProcess.size() > 0))
//			{
//				for(Iterator it = imagesToProcess.iterator(); it.hasNext();)
//				{
//					JRPrintElementIndex imageIndex = (JRPrintElementIndex)it.next();
//
//					JRPrintImage image = getImage(jasperPrintList, imageIndex);
//					JRRenderable renderer = image.getRenderer();
//					if (renderer.getType() == JRRenderable.TYPE_SVG)
//					{
//						renderer =
//							new JRWrappingSvgRenderer(
//								renderer,
//								new Dimension(image.getWidth(), image.getHeight()),
//								JRElement.MODE_OPAQUE == image.getMode() ? image.getBackcolor() : null
//								);
//					}
//
//					byte[] imageData = renderer.getImageData();
//
//					File imageFile = new File(new File("Pictures"), getImageName(imageIndex));//FIXMEODT new File(imagesDir, getImageName(imageIndex));
//					FileOutputStream fos = null;
//
//					try
//					{
//						fos = new FileOutputStream(imageFile);
//						fos.write(imageData, 0, imageData.length);
//					}
//					catch (IOException e)
//					{
//						throw new JRException("Error writing to image file : " + imageFile, e);
//					}
//					finally
//					{
//						if (fos != null)
//						{
//							try
//							{
//								fos.close();
//							}
//							catch(IOException e)
//							{
//							}
//						}
//					}
//				}
//			}
		}
		finally
		{
			resetExportContext();
		}
	}


	protected void setHyperlinkProducerFactory()
	{
		hyperlinkProducerFactory = (JRHyperlinkProducerFactory) parameters.get(JRExporterParameter.HYPERLINK_PRODUCER_FACTORY);
	}


	/**
	 * 
	 *
	public static JRPrintImage getImage(List jasperPrintList, String imageName)
	{
		return getImage(jasperPrintList, getPrintElementIndex(imageName));
	}


	public static JRPrintImage getImage(List jasperPrintList, JRPrintElementIndex imageIndex)
	{
		JasperPrint report = (JasperPrint)jasperPrintList.get(imageIndex.getReportIndex());
		JRPrintPage page = (JRPrintPage)report.getPages().get(imageIndex.getPageIndex());

		Integer[] elementIndexes = imageIndex.getElementIndexes();
		Object element = page.getElements().get(elementIndexes[0].intValue());

		for (int i = 1; i < elementIndexes.length; ++i)
		{
			JRPrintFrame frame = (JRPrintFrame) element;
			element = frame.getElements().get(elementIndexes[i].intValue());
		}

		return (JRPrintImage) element;
	}
	
	
	/**
	 *
	 */
	protected void exportReportToOasisZip() throws JRException, IOException
	{
		OasisZipEntry tempBodyEntry = new ByteArrayOasisZipEntry(null);
		OasisZipEntry tempStyleEntry = new ByteArrayOasisZipEntry(null);

		tempBodyWriter = tempBodyEntry.getWriter();
		tempStyleWriter = tempStyleEntry.getWriter();

		fontFaces = new HashSet();
		paragraphStyles = new HashMap();
		paragraphStylesCounter = 0;
		textSpanStyles = new HashMap();
		textSpanStylesCounter = 0;

		Writer stylesWriter = oasisZip.getStylesEntry().getWriter();
		
		StyleBuilder styleBuilder = new StyleBuilder(jasperPrintList, stylesWriter);
		styleBuilder.build();

		stylesWriter.flush();
		stylesWriter.close();
		
		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
		{
			jasperPrint = (JasperPrint)jasperPrintList.get(reportIndex);

			List pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				if (isModeBatch)
				{
					startPageIndex = 0;
					endPageIndex = pages.size() - 1;
				}

				JRPrintPage page = null;
				for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
				{
					if (Thread.currentThread().isInterrupted())
					{
						throw new JRException("Current thread interrupted.");
					}

					page = (JRPrintPage)pages.get(pageIndex);

					exportPage(page);
				}
			}
		}
		

		tempBodyWriter.flush();
		tempStyleWriter.flush();
		

		tempBodyWriter.close();
		tempStyleWriter.close();
		

		/*   */
		ContentBuilder contentBuilder = 
			new ContentBuilder(
				oasisZip.getContentEntry(),
				tempStyleEntry,
				tempBodyEntry,
				fontFaces
				);
		contentBuilder.build();
		
		//FIXMEODT close/delete readers
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, IOException
	{
		JRGridLayout layout = 
			new JRGridLayout(
				page.getElements(),
				jasperPrint.getPageWidth(), 
				jasperPrint.getPageHeight(),
				globalOffsetX, 
				globalOffsetY, 
				JRGridLayout.UNIVERSAL_EXPORTER, 
				false, //deep 
				true, //splitSharedRowSpan
				true, //spanCells
				true, //setElementIndexes
				null
				);

		exportGrid(layout, null);

		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	/**
	 *
	 */
	protected void exportGrid(JRGridLayout gridLayout, JRPrintElementIndex frameIndex) throws IOException, JRException
	{
		List xCuts = gridLayout.getXCuts();
		JRExporterGridCell[][] grid = gridLayout.getGrid();

		TableBuilder tableBuilder = frameIndex == null 
			? new TableBuilder(reportIndex, pageIndex, tempBodyWriter, tempStyleWriter)
			: new TableBuilder(frameIndex.toString(), tempBodyWriter, tempStyleWriter);
		
		tableBuilder.buildTableStyle();
		tableBuilder.buildTableHeader();
		
		for(int col = 1; col < xCuts.size(); col++)
		{
			tableBuilder.buildColumnStyle(
					col - 1, 
					((Integer)xCuts.get(col)).intValue() - ((Integer)xCuts.get(col - 1)).intValue()
					);
			tableBuilder.buildColumnHeader(col - 1);
			tableBuilder.buildColumnFooter();
		}

		JRPrintElement element = null;
		for(int row = 0; row < grid.length; row++)
		{
			//JRExporterGridCell[] gridRow = grid[row];
			
			int emptyCellColSpan = 0;
			int emptyCellWidth = 0;
			int rowHeight = gridLayout.getRowHeight(row);
			
			tableBuilder.buildRowStyle(row, rowHeight);
			tableBuilder.buildRowHeader(row);

			for(int col = 0; col < grid[0].length; col++)
			{
				JRExporterGridCell gridCell = grid[row][col];
				if (gridCell == JRExporterGridCell.OCCUPIED_CELL)
				{
					if (emptyCellColSpan > 0)
					{
						writeEmptyCell(gridCell, emptyCellColSpan, emptyCellWidth, rowHeight);
						emptyCellColSpan = 0;
						emptyCellWidth = 0;
					}

					writeOccupiedCells(1);
				}
				else if(gridCell.element != null)
				{
					if (emptyCellColSpan > 0)
					{
						writeEmptyCell(gridCell, emptyCellColSpan, emptyCellWidth, rowHeight);
						emptyCellColSpan = 0;
						emptyCellWidth = 0;
					}

					element = gridCell.element;

					if (element instanceof JRPrintLine)
					{
						exportLine(tableBuilder, (JRPrintLine)element, gridCell);
					}
					else if (element instanceof JRPrintRectangle)
					{
						exportRectangle(tableBuilder, (JRPrintRectangle)element, gridCell);
					}
					else if (element instanceof JRPrintEllipse)
					{
						exportEllipse(tableBuilder, (JRPrintEllipse)element, gridCell);
					}
					else if (element instanceof JRPrintImage)
					{
						writeEmptyCell(null, gridCell.colSpan, gridCell.width, gridCell.height);//FIXMEODT
					}
					else if (element instanceof JRPrintText)
					{
						exportText(tableBuilder, (JRPrintText)element, gridCell);
					}
					else if (element instanceof JRPrintFrame)
					{
						exportFrame(tableBuilder, (JRPrintFrame)element, gridCell);
					}

					//x += gridCell.colSpan - 1;
				}
				else
				{
					emptyCellColSpan++;
					emptyCellWidth += gridCell.width;
				}
			}

			if (emptyCellColSpan > 0)
			{
				writeEmptyCell(null, emptyCellColSpan, emptyCellWidth, rowHeight);
			}

			tableBuilder.buildRowFooter();
		}

		tableBuilder.buildTableFooter();
	}


	private void writeEmptyCell(JRExporterGridCell gridCell, int emptyCellColSpan, int emptyCellWidth, int rowHeight) throws IOException
	{
		tempBodyWriter.write("<table:table-cell");
		//tempBodyWriter.write(" office:value-type=\"string\"");
		tempBodyWriter.write(" table:style-name=\"empty-cell\"");
		if (emptyCellColSpan > 1)
		{
			tempBodyWriter.write(" table:number-columns-spanned=\"" + emptyCellColSpan + "\"");
		}
		tempBodyWriter.write("/>\n");

		writeOccupiedCells(emptyCellColSpan - 1);
	}


	private void writeOccupiedCells(int count) throws IOException
	{
		for(int i = 0; i < count; i++)
		{
			tempBodyWriter.write("<table:covered-table-cell/>\n");
		}
	}


	/**
	 *
	 */
	protected void exportLine(TableBuilder tableBuilder, JRPrintLine line, JRExporterGridCell gridCell) throws IOException
	{
		tableBuilder.buildCellStyleHeader(line);
		tableBuilder.buildCellBorderStyle(line, getBox(line));
		tableBuilder.buildCellStyleFooter();
		tableBuilder.buildCellHeader(gridCell.colSpan, gridCell.rowSpan);
		tableBuilder.buildCellFooter();
	}


	/**
	 *
	 */
	protected void exportRectangle(TableBuilder tableBuilder, JRPrintRectangle rectangle, JRExporterGridCell gridCell) throws IOException
	{
		tableBuilder.buildCellStyleHeader(rectangle);
		tableBuilder.buildCellBackcolorStyle(rectangle);
		tableBuilder.buildCellBorderStyle(rectangle, getBox(rectangle));
		tableBuilder.buildCellStyleFooter();
		tableBuilder.buildCellHeader(gridCell.colSpan, gridCell.rowSpan);
		tableBuilder.buildCellFooter();
	}


	/**
	 *
	 */
	protected void exportEllipse(TableBuilder tableBuilder, JRPrintEllipse ellipse, JRExporterGridCell gridCell) throws IOException
	{
		tableBuilder.buildCellStyleHeader(ellipse);
		tableBuilder.buildCellStyleFooter();
		tableBuilder.buildCellHeader(gridCell.colSpan, gridCell.rowSpan);
		tempBodyWriter.write(
			"<text:p " 
			//+ "text:style-name=\"Standard\"" 
			+ ">" 
			+ "<draw:ellipse text:anchor-type=\"paragraph\" draw:z-index=\"0\" " 
			//+ "draw:style-name=\"gr1\" draw:text-style-name=\"P1\" " 
			+ "svg:width=\"" + Utility.translatePixelsToInches(ellipse.getWidth()) + "in\" " 
			+ "svg:height=\"" + Utility.translatePixelsToInches(ellipse.getHeight()) + "in\" " 
			+ "svg:x=\"0in\" " 
			+ "svg:y=\"0in\">" 
			+ "<text:p/></draw:ellipse></text:p>"
			);
		tableBuilder.buildCellFooter();
	}


	/**
	 *
	 */
	protected void exportText(TableBuilder tableBuilder, JRPrintText text, JRExporterGridCell gridCell) throws IOException
	{
		tableBuilder.buildCellStyleHeader(text);
		tableBuilder.buildCellBackcolorStyle(text);
		tableBuilder.buildCellBorderStyle(text, text);
		tableBuilder.buildCellAlignmentStyle(text);
		tableBuilder.buildCellStyleFooter();
		tableBuilder.buildCellHeader(gridCell.colSpan, gridCell.rowSpan);

		JRStyledText styledText = getStyledText(text);

		int textLength = 0;

		if (styledText != null)
		{
			textLength = styledText.length();
		}

		StringBuffer paragraphStyleIdBuffer = new StringBuffer();
		
		String verticalAlignment = VERTICAL_ALIGN_TOP;
		switch (text.getVerticalAlignment())
		{
			case JRAlignment.VERTICAL_ALIGN_BOTTOM :
			{
				verticalAlignment = VERTICAL_ALIGN_BOTTOM;
				break;
			}
			case JRAlignment.VERTICAL_ALIGN_MIDDLE :
			{
				verticalAlignment = VERTICAL_ALIGN_MIDDLE;
				break;
			}
			case JRAlignment.VERTICAL_ALIGN_TOP :
			default :
			{
				verticalAlignment = VERTICAL_ALIGN_TOP;
			}
		}
		paragraphStyleIdBuffer.append(verticalAlignment);

		String runDirection = null;
		if (text.getRunDirection() == JRPrintText.RUN_DIRECTION_RTL)
		{
			runDirection = "rl";
		}
		paragraphStyleIdBuffer.append(runDirection);

		String horizontalAlignment = HORIZONTAL_ALIGN_LEFT;
		switch (text.getHorizontalAlignment())
		{
			case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
			{
				horizontalAlignment = HORIZONTAL_ALIGN_RIGHT;
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_CENTER :
			{
				horizontalAlignment = HORIZONTAL_ALIGN_CENTER;
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
			{
				horizontalAlignment = HORIZONTAL_ALIGN_JUSTIFY;
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_LEFT :
			default :
			{
				horizontalAlignment = HORIZONTAL_ALIGN_LEFT;
			}
		}
		paragraphStyleIdBuffer.append(horizontalAlignment);

//		if (isWrapBreakWord)
//		{
//			styleBuffer.append("width: " + gridCell.width + "; ");
//			styleBuffer.append("word-wrap: break-word; ");
//		}
		
//		if (text.getLineSpacing() != JRTextElement.LINE_SPACING_SINGLE)
//		{
//			styleBuffer.append("line-height: " + text.getLineSpacingFactor() + "; ");
//		}

//		if (styleBuffer.length() > 0)
//		{
//			writer.write(" style=\"");
//			writer.write(styleBuffer.toString());
//			writer.write("\"");
//		}
//
//		writer.write(">");

//		if (text.getAnchorName() != null)
//		{
//			writer.write("<a name=\"");
//			writer.write(text.getAnchorName());
//			writer.write("\"/>");
//		}

//		boolean startedHyperlink = startHyperlink(text);

		String paragraphStyleId = paragraphStyleIdBuffer.toString();
		String paragraphStyleName = (String)paragraphStyles.get(paragraphStyleId);
		if (paragraphStyleName == null)
		{
			paragraphStyleName = "P" + paragraphStylesCounter++;
			paragraphStyles.put(paragraphStyleId, paragraphStyleName);
			
			tempStyleWriter.write("<style:style style:name=\"" + paragraphStyleName + "\"");
			tempStyleWriter.write(" style:family=\"paragraph\">\n");
			tempStyleWriter.write("<style:paragraph-properties");
//			tempStyleWriter.write(" fo:line-height=\"" + pLineHeight + "\"");
//			tempStyleWriter.write(" style:line-spacing=\"" + pLineSpacing + "\"");
			tempStyleWriter.write(" fo:text-align=\"" + horizontalAlignment + "\"");
//			tempStyleWriter.write(" fo:keep-together=\"" + pKeepTogether + "\"");
//			tempStyleWriter.write(" fo:margin-left=\"" + pMarginLeft + "\"");
//			tempStyleWriter.write(" fo:margin-right=\"" + pMarginRight + "\"");
//			tempStyleWriter.write(" fo:margin-top=\"" + pMarginTop + "\"");
//			tempStyleWriter.write(" fo:margin-bottom=\"" + pMarginBottom + "\"");
//			tempStyleWriter.write(" fo:background-color=\"#" + pBackGroundColor + "\"");
			tempStyleWriter.write(" style:vertical-align=\"" + verticalAlignment + "\"");
			if (runDirection != null)
			{
				tempStyleWriter.write(" style:writing-mode=\"" + runDirection + "\"");
			}
			tempStyleWriter.write("> \r\n");
			tempStyleWriter.write("</style:paragraph-properties>\n");
			tempStyleWriter.write("</style:style>\n");
		}
		
		tempBodyWriter.write("<text:p");
		tempBodyWriter.write(" text:style-name=\"" + paragraphStyleName + "\"");
		tempBodyWriter.write(">");
		
		if (textLength > 0) 
		{
			exportStyledText(styledText);
		}
		
//		if (startedHyperlink)
//		{
//			endHyperlink();
//		}

		tempBodyWriter.write("</text:p>\n");		

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
		String fontFamily;
		String fontFamilyAttr = (String)attributes.get(TextAttribute.FAMILY);
		if (fontMap != null && fontMap.containsKey(fontFamilyAttr))
		{
			fontFamily = (String) fontMap.get(fontFamilyAttr);
		}
		else
		{
			fontFamily = fontFamilyAttr;
		}
		fontFaces.add(fontFamily);
		
		StringBuffer textSpanStyleIdBuffer = new StringBuffer();
		textSpanStyleIdBuffer.append(fontFamily);

		String forecolorHexa = null;
		Color forecolor = (Color)attributes.get(TextAttribute.FOREGROUND);
		if (!Color.black.equals(forecolor))
		{
			forecolorHexa = Integer.toHexString(forecolor.getRGB() & colorMask).toUpperCase();
			forecolorHexa = ("000000" + forecolorHexa).substring(forecolorHexa.length());
			textSpanStyleIdBuffer.append(forecolorHexa);
		}

		String backcolorHexa = null;
		Color runBackcolor = (Color)attributes.get(TextAttribute.BACKGROUND);
		if (runBackcolor != null)
		{
			backcolorHexa = Integer.toHexString(runBackcolor.getRGB() & colorMask).toUpperCase();
			backcolorHexa = ("000000" + backcolorHexa).substring(backcolorHexa.length());
			textSpanStyleIdBuffer.append(backcolorHexa);
		}

		String size = String.valueOf(attributes.get(TextAttribute.SIZE));
		textSpanStyleIdBuffer.append(size);

		String weight = null;
		if (TextAttribute.WEIGHT_BOLD.equals(attributes.get(TextAttribute.WEIGHT)))
		{
			weight = "bold";
			textSpanStyleIdBuffer.append(weight);
		}
		String posture = null;
		if (TextAttribute.POSTURE_OBLIQUE.equals(attributes.get(TextAttribute.POSTURE)))
		{
			posture = "italic";
			textSpanStyleIdBuffer.append(posture);
		}
		String underline = null;
		if (TextAttribute.UNDERLINE_ON.equals(attributes.get(TextAttribute.UNDERLINE)))
		{
			underline = "single";
			textSpanStyleIdBuffer.append(underline);
		}
		String strikeThrough = null;
		if (TextAttribute.STRIKETHROUGH_ON.equals(attributes.get(TextAttribute.STRIKETHROUGH)))
		{
			strikeThrough = "single";
			textSpanStyleIdBuffer.append(strikeThrough);
		}

//		if (TextAttribute.SUPERSCRIPT_SUPER.equals(attributes.get(TextAttribute.SUPERSCRIPT)))
//		{
//			textSpanStyleIdBuffer.append(" vertical-align: super;");
//		}
//		else if (TextAttribute.SUPERSCRIPT_SUB.equals(attributes.get(TextAttribute.SUPERSCRIPT)))
//		{
//			textSpanStyleIdBuffer.append(" vertical-align: sub;");
//		}

		String textSpanStyleId = textSpanStyleIdBuffer.toString();
		String textSpanStyleName = (String)textSpanStyles.get(textSpanStyleId);
		if (textSpanStyleName == null)
		{
			textSpanStyleName = "T" + textSpanStylesCounter++;
			textSpanStyles.put(textSpanStyleId, textSpanStyleName);
			
			tempStyleWriter.write("<style:style style:name=\"" + textSpanStyleName + "\"");
			tempStyleWriter.write(" style:family=\"text\">\n");
			tempStyleWriter.write("<style:text-properties");
			if (forecolorHexa != null)
			{
				tempStyleWriter.write(" fo:color=\"#" + forecolorHexa+ "\"");
			}
			tempStyleWriter.write(" style:font-name=\"" + fontFamily + "\"");
			tempStyleWriter.write(" fo:font-size=\"" + size + "pt\"");
			if (posture != null)
			{
				tempStyleWriter.write(" fo:font-style=\"" + posture + "\"");
			}
			if (weight != null)
			{
				tempStyleWriter.write(" fo:font-weight=\"" + weight + "\"");
			}
			if (backcolorHexa != null)
			{
				tempStyleWriter.write(" fo:background-color=\"#" + backcolorHexa + "\"");
			}
//			tempStyleWriter.write(" style:text-rotation-angle=\"" + textRotationAngle + "\"");
//			tempStyleWriter.write(" style:text-rotation-scale=\"" + textRotationScale + "\"");
			if (underline != null)
			{
				tempStyleWriter.write(" style:text-underline-type=\"" + underline + "\"");
			}
			if (strikeThrough != null)
			{
				tempStyleWriter.write(" style:text-line-through-type=\"" + strikeThrough + "\"");
			}
			tempStyleWriter.write(">\n");
			tempStyleWriter.write("</style:text-properties>\n");	
			tempStyleWriter.write("</style:style>\n");
		}
		
		tempBodyWriter.write("<text:span");
		tempBodyWriter.write(" text:style-name=\"" + textSpanStyleName + "\"");
		tempBodyWriter.write(">");
		
		if (text != null) 
		{
			tempBodyWriter.write(Utility.replaceNewLineWithLineBreak(JRStringUtil.xmlEncode(text)));//FIXMEODT try something nicer for replace
		}

		tempBodyWriter.write("</text:span>");		
	}


	/**
	 *
	 * 
	protected boolean startHyperlink(JRPrintHyperlink link) throws IOException
	{
		String href = getHyperlinkURL(link);

		if (href != null)
		{
			writer.write("<a href=\"");
			writer.write(href);
			writer.write("\"");

			String target = getHyperlinkTarget(link);
			if (target != null)
			{
				writer.write(" target=\"");
				writer.write(target);
				writer.write("\"");
			}

			if (link.getHyperlinkTooltip() != null)
			{
				writer.write(" title=\"");
				writer.write(JRStringUtil.xmlEncode(link.getHyperlinkTooltip()));
				writer.write("\"");
			}
			
			writer.write(">");
		}
		
		return href != null;
	}


	protected String getHyperlinkTarget(JRPrintHyperlink link)
	{
		String target = null;
		switch(link.getHyperlinkTarget())
		{
			case JRHyperlink.HYPERLINK_TARGET_BLANK :
			{
				target = "_blank";
				break;
			}
			case JRHyperlink.HYPERLINK_TARGET_SELF :
			default :
			{
				break;
			}
		}
		return target;
	}


	protected String getHyperlinkURL(JRPrintHyperlink link)
	{
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


	protected void endHyperlink() throws IOException
	{
		writer.write("</a>");
	}


	protected Color appendBackcolorStyle(JRPrintElement element, StringBuffer styleBuffer)
	{
		if (element.getMode() == JRElement.MODE_OPAQUE && (backcolor == null || element.getBackcolor().getRGB() != backcolor.getRGB()))
		{
			styleBuffer.append("background-color: #");
			String hexa = Integer.toHexString(element.getBackcolor().getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			styleBuffer.append(hexa);
			styleBuffer.append("; ");

			return element.getBackcolor();
		}

		return null;
	}


	/**
	 *
	 *
	protected void exportImage(JRPrintImage image, JRExporterGridCell gridCell) throws JRException, IOException
	{
		writeCellTDStart(gridCell);

		String horizontalAlignment = CSS_TEXT_ALIGN_LEFT;

		switch (image.getHorizontalAlignment())
		{
			case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
			{
				horizontalAlignment = CSS_TEXT_ALIGN_RIGHT;
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_CENTER :
			{
				horizontalAlignment = CSS_TEXT_ALIGN_CENTER;
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_LEFT :
			default :
			{
				horizontalAlignment = CSS_TEXT_ALIGN_LEFT;
			}
		}

		if (!horizontalAlignment.equals(CSS_TEXT_ALIGN_LEFT))
		{
			writer.write(" align=\"");
			writer.write(horizontalAlignment);
			writer.write("\"");
		}

		String verticalAlignment = HTML_VERTICAL_ALIGN_TOP;

		switch (image.getVerticalAlignment())
		{
			case JRAlignment.VERTICAL_ALIGN_BOTTOM :
			{
				verticalAlignment = HTML_VERTICAL_ALIGN_BOTTOM;
				break;
			}
			case JRAlignment.VERTICAL_ALIGN_MIDDLE :
			{
				verticalAlignment = HTML_VERTICAL_ALIGN_MIDDLE;
				break;
			}
			case JRAlignment.VERTICAL_ALIGN_TOP :
			default :
			{
				verticalAlignment = HTML_VERTICAL_ALIGN_TOP;
			}
		}

		if (!verticalAlignment.equals(HTML_VERTICAL_ALIGN_TOP))
		{
			writer.write(" valign=\"");
			writer.write(verticalAlignment);
			writer.write("\"");
		}

		StringBuffer styleBuffer = new StringBuffer();
		appendBackcolorStyle(image, styleBuffer);
		appendBorderStyle(image, image, styleBuffer);

		if (styleBuffer.length() > 0)
		{
			writer.write(" style=\"");
			writer.write(styleBuffer.toString());
			writer.write("\"");
		}

		writer.write(">");

		if (image.getAnchorName() != null)
		{
			writer.write("<a name=\"");
			writer.write(image.getAnchorName());
			writer.write("\"/>");
		}
		
		JRRenderable renderer = image.getRenderer();
		JRRenderable originalRenderer = renderer;
		boolean imageMapRenderer = renderer != null && renderer instanceof JRImageMapRenderer;

		boolean startedHyperlink = false;

		if(renderer != null)
		{
			startedHyperlink = !imageMapRenderer && startHyperlink(image);
			writer.write("<img");
			String imagePath = null;
			String imageMapName = null;
			List imageMapAreas = null;
	
			byte scaleImage = image.getScaleImage();
			if (renderer.getType() == JRRenderable.TYPE_IMAGE && rendererToImagePathMap.containsKey(renderer.getId()))
			{
				imagePath = (String)rendererToImagePathMap.get(renderer.getId());
			}
			else
			{
				if (image.isLazy())
				{
					imagePath = ((JRImageRenderer)renderer).getImageLocation();
				}
				else
				{
					JRPrintElementIndex imageIndex = getElementIndex(gridCell);
					imagesToProcess.add(imageIndex);

					String imageName = getImageName(imageIndex);
					imagePath = "imagesURI" + imageName;
				}

				rendererToImagePathMap.put(renderer.getId(), imagePath);
			}
			
			if (imageMapRenderer)
			{
				Rectangle renderingArea = new Rectangle(image.getWidth(), image.getHeight());
				
				if (renderer.getType() == JRRenderable.TYPE_IMAGE)
				{
					imageMapName = (String) imageMaps.get(new Pair(renderer.getId(), renderingArea));
				}

				if (imageMapName == null)
				{
					imageMapName = "map_" + getElementIndex(gridCell).toString();
					imageMapAreas = ((JRImageMapRenderer) originalRenderer).getImageAreaHyperlinks(renderingArea);
					
					if (renderer.getType() == JRRenderable.TYPE_IMAGE)
					{
						imageMaps.put(new Pair(renderer.getId(), renderingArea), imageMapName);
					}
				}
			}
	
			writer.write(" src=\"");
			if (imagePath != null)
				writer.write(imagePath);
			writer.write("\"");
		
			int borderWidth = 0;
			switch (image.getPen())
			{
				case JRGraphicElement.PEN_DOTTED :
				{
					borderWidth = 1;
					break;
				}
				case JRGraphicElement.PEN_4_POINT :
				{
					borderWidth = 4;
					break;
				}
				case JRGraphicElement.PEN_2_POINT :
				{
					borderWidth = 2;
					break;
				}
				case JRGraphicElement.PEN_NONE :
				{
					borderWidth = 0;
					break;
				}
				case JRGraphicElement.PEN_THIN :
				{
					borderWidth = 1;
					break;
				}
				case JRGraphicElement.PEN_1_POINT :
				default :
				{
					borderWidth = 1;
					break;
				}
			}
		
			writer.write(" border=\"");
			writer.write(String.valueOf(borderWidth));
			writer.write("\"");
		
			int imageWidth = image.getWidth() - image.getLeftPadding() - image.getRightPadding();
			if (imageWidth < 0)
			{
				imageWidth = 0;
			}
		
			int imageHeight = image.getHeight() - image.getTopPadding() - image.getBottomPadding();
			if (imageHeight < 0)
			{
				imageHeight = 0;
			}
		
			switch (scaleImage)
			{
				case JRImage.SCALE_IMAGE_FILL_FRAME :
				{
					writer.write(" style=\"width: ");
					writer.write(String.valueOf(imageWidth));
					writer.write("; height: ");
					writer.write(String.valueOf(imageHeight));
					writer.write("\"");
		
					break;
				}
				case JRImage.SCALE_IMAGE_CLIP : //FIXMEIMAGE image clip could be achieved by cutting the image and preserving the image type
				case JRImage.SCALE_IMAGE_RETAIN_SHAPE :
				default :
				{
					double normalWidth = imageWidth;
					double normalHeight = imageHeight;
		
					if (!image.isLazy())
					{
						Dimension2D dimension = renderer.getDimension();
						if (dimension != null)
						{
							normalWidth = dimension.getWidth();
							normalHeight = dimension.getHeight();
						}
					}
		
					if (imageHeight > 0)
					{
						double ratio = normalWidth / normalHeight;
		
						if( ratio > (double)imageWidth / (double)imageHeight )
						{
							writer.write(" style=\"width: ");
							writer.write(String.valueOf(imageWidth));
							writer.write("\"");
						}
						else
						{
							writer.write(" style=\"height: ");
							writer.write(String.valueOf(imageHeight));
							writer.write("\"");
						}
					}
				}
			}
			
			if (imageMapName != null)
			{
				writer.write(" usemap=\"#" + imageMapName + "\"");
			}
			
			writer.write(" alt=\"\"/>");
		
			if (startedHyperlink)
			{
				endHyperlink();
			}
			
			if (imageMapAreas != null)
			{
				writer.write("\n");
				writeImageMap(imageMapName, image, imageMapAreas);
			}
		}
		writer.write("</td>\n");
	}


	protected JRPrintElementIndex getElementIndex(JRExporterGridCell gridCell)
	{
		JRPrintElementIndex imageIndex =
			new JRPrintElementIndex(
					reportIndex,
					pageIndex,
					gridCell.elementIndex
					);
		return imageIndex;
	}


	protected void writeImageMap(String imageMapName, JRPrintHyperlink mainHyperlink, List imageMapAreas) throws IOException
	{
		writer.write("<map name=\"" + imageMapName + "\">\n");

		for (Iterator it = imageMapAreas.iterator(); it.hasNext();)
		{
			JRPrintImageAreaHyperlink areaHyperlink = (JRPrintImageAreaHyperlink) it.next();
			JRPrintImageArea area = areaHyperlink.getArea();

			writer.write("  <area shape=\"" + JRPrintImageArea.getHtmlShape(area.getShape()) + "\"");
			writeImageAreaCoordinates(area);			
			writeImageAreaHyperlink(areaHyperlink.getHyperlink());
			writer.write("/>\n");
		}
		
		if (mainHyperlink.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			writer.write("  <area shape=\"default\"");
			writeImageAreaHyperlink(mainHyperlink);
			writer.write("/>\n");
		}
		
		writer.write("</map>\n");
	}


	protected void writeImageAreaCoordinates(JRPrintImageArea area) throws IOException
	{
		int[] coords = area.getCoordinates();
		if (coords != null && coords.length > 0)
		{
			StringBuffer coordsEnum = new StringBuffer(coords.length * 4);
			coordsEnum.append(coords[0]);
			for (int i = 1; i < coords.length; i++)
			{
				coordsEnum.append(',');
				coordsEnum.append(coords[i]);
			}
			
			writer.write(" coords=\"" + coordsEnum + "\"");
		}
	}


	protected void writeImageAreaHyperlink(JRPrintHyperlink hyperlink) throws IOException
	{
		String href = getHyperlinkURL(hyperlink);
		if (href == null)
		{
			writer.write(" nohref=\"nohref\"");
		}
		else
		{
			writer.write(" href=\"" + href + "\"");
			
			String target = getHyperlinkTarget(hyperlink);
			if (target != null)
			{
				writer.write(" target=\"");
				writer.write(target);
				writer.write("\"");
			}
		}

		if (hyperlink.getHyperlinkTooltip() != null)
		{
			writer.write(" title=\"");
			writer.write(JRStringUtil.xmlEncode(hyperlink.getHyperlinkTooltip()));
			writer.write("\"");
		}
	}


	/**
	 *
	 *
	public static String getImageName(JRPrintElementIndex printElementIndex)
	{
		return IMAGE_NAME_PREFIX + printElementIndex.toString();
	}


	/**
	 *
	 *
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
	protected void exportFrame(TableBuilder tableBuilder, JRPrintFrame frame, JRExporterGridCell gridCell) throws IOException, JRException
	{
		tableBuilder.buildCellStyleHeader(frame);
		tableBuilder.buildCellStyleFooter();
		tableBuilder.buildCellHeader(gridCell.colSpan, gridCell.rowSpan);

//		StringBuffer styleBuffer = new StringBuffer();
//		Color frameBackcolor = appendBackcolorStyle(frame, styleBuffer);
//		appendBorderStyle(frame, frame, styleBuffer);
//
//		if (styleBuffer.length() > 0)
//		{
//			writer.write(" style=\"");
//			writer.write(styleBuffer.toString());
//			writer.write("\"");
//		}
//
//		writer.write(">\n");
//
//		if (frameBackcolor != null)
//		{
//			setBackcolor(frameBackcolor);
//		}
		try
		{
			JRGridLayout layout = gridCell.getLayout(); 
			JRPrintElementIndex frameIndex =
				new JRPrintElementIndex(
						reportIndex,
						pageIndex,
						gridCell.elementIndex
						);
			exportGrid(layout, frameIndex);
		}
		finally
		{
//			if (frameBackcolor != null)
//			{
//				restoreBackcolor();
//			}
		}

		tableBuilder.buildCellFooter();
	}

	/**
	 * 
	 * 
	protected void setBackcolor(Color color)
	{
		backcolorStack.addLast(backcolor);

		backcolor = color;
	}


	protected void restoreBackcolor()
	{
		backcolor = (Color) backcolorStack.removeLast();
	}
*/

}


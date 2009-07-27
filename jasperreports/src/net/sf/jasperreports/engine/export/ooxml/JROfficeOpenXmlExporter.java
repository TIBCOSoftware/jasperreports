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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
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
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.export.CutsInfo;
import net.sf.jasperreports.engine.export.ElementGridCell;
import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.ExporterNature;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRGridLayout;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.OccupiedGridCell;
import net.sf.jasperreports.engine.export.zip.FileBufferedZipEntry;
import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * Exports a JasperReports document to the Office Open XML format. It has character output type and exports the document to a
 * grid-based layout.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JROfficeOpenXmlExporter extends JRAbstractExporter
{

	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";

	/**
	 *
	 */
	public static final String IMAGE_NAME_PREFIX = "img_";
	protected static final int IMAGE_NAME_PREFIX_LEGTH = IMAGE_NAME_PREFIX.length();

	/**
	 *
	 */
	protected Writer docWriter = null;
	protected Writer relsWriter = null;

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

	protected LinkedList backcolorStack;
	protected Color backcolor;

	private RunHelper runHelper = null;

	protected ExporterNature nature = null;

	
	public JROfficeOpenXmlExporter()
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
				filter = createFilter(getExporterPropertiesPrefix());
			}

			/*   */
			if (!isModeBatch)
			{
				setPageRange();
			}

			encoding =
				getStringParameterOrDefault(
					JRExporterParameter.CHARACTER_ENCODING,
					JRExporterParameter.PROPERTY_CHARACTER_ENCODING
					);

			rendererToImagePathMap = new HashMap();
			imageMaps = new HashMap();
			imagesToProcess = new ArrayList();

			fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);

			setHyperlinkProducerFactory();

			nature = getExporterNature(filter);

			OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
			if (os != null)
			{
				try
				{
					exportReportToOoxmlZip(os);
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
					exportReportToOoxmlZip(os);
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


	/**
	 *
	 */
	public static JRPrintImage getImage(List jasperPrintList, String imageName)
	{
		return getImage(jasperPrintList, getPrintElementIndex(imageName));
	}


	public static JRPrintImage getImage(List jasperPrintList, JRPrintElementIndex imageIndex)
	{
		JasperPrint report = (JasperPrint)jasperPrintList.get(imageIndex.getReportIndex());
		JRPrintPage page = (JRPrintPage)report.getPages().get(imageIndex.getPageIndex());

		Integer[] elementIndexes = imageIndex.getAddressArray();
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
	protected void exportReportToOoxmlZip(OutputStream os) throws JRException, IOException
	{
		OoxmlZip ooxmlZip = new FileBufferedOoxmlZip();

		docWriter = ooxmlZip.getDocumentEntry().getWriter();
		relsWriter = ooxmlZip.getRelsEntry().getWriter();
		
		DocumentHelper.exportHeader(docWriter);
		RelsHelper.exportHeader(relsWriter);
		
		Writer stylesWriter = ooxmlZip.getStylesEntry().getWriter();
		new ReportStyleHelper(stylesWriter, fontMap).export(jasperPrintList);
		stylesWriter.close();

		runHelper = new RunHelper(docWriter, fontMap);

		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
		{
			setJasperPrint((JasperPrint)jasperPrintList.get(reportIndex));

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
		
		DocumentHelper.exportFooter(jasperPrint, docWriter);

		docWriter.close();

		if ((imagesToProcess != null && imagesToProcess.size() > 0))
		{
			for(Iterator it = imagesToProcess.iterator(); it.hasNext();)
			{
				JRPrintElementIndex imageIndex = (JRPrintElementIndex)it.next();

				JRPrintImage image = getImage(jasperPrintList, imageIndex);
				JRRenderable renderer = image.getRenderer();
				if (renderer.getType() == JRRenderable.TYPE_SVG)
				{
					renderer =
						new JRWrappingSvgRenderer(
							renderer,
							new Dimension(image.getWidth(), image.getHeight()),
							JRElement.MODE_OPAQUE == image.getMode() ? image.getBackcolor() : null
							);
				}

				String imageName = getImageName(imageIndex);
				
				ooxmlZip.addEntry(//FIXMEDOCX optimize with a different implementation of entry
					new FileBufferedZipEntry(
						"word/media/" + imageName + ".jpeg",//FIXMEDOCX deal with extension
						renderer.getImageData()
						)
					);
				
				RelsHelper.exportImage(imageName, relsWriter);
			}
		}

		RelsHelper.exportFooter(relsWriter);

		relsWriter.close();

		ooxmlZip.zipEntries(os);

		ooxmlZip.dispose();
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, IOException
	{
		startPage = true;
		JRGridLayout layout =
			new JRGridLayout(
				nature,
				page.getElements(),
				jasperPrint.getPageWidth(),
				jasperPrint.getPageHeight(),
				globalOffsetX,
				globalOffsetY,
				null //address
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
	protected void exportGrid(JRGridLayout gridLayout, JRPrintElementIndex frameIndex) throws JRException, IOException
	{
		CutsInfo xCuts = gridLayout.getXCuts();
		JRExporterGridCell[][] grid = gridLayout.getGrid();

		if (grid[0].length > 63)
		{
			throw new JRException("The DOCX format does not support more than 63 columns in a table.");
		}
		
		TableHelper tableHelper = 
			new TableHelper(
				docWriter, 
				runHelper,
				frameIndex == null && (reportIndex != 0 || pageIndex != startPageIndex)
				);

		tableHelper.exportHeader(xCuts);

		JRPrintElement element = null;
		for(int row = 0; row < grid.length; row++)
		{
			int emptyCellColSpan = 0;
			int emptyCellWidth = 0;
			int rowHeight = gridLayout.getRowHeight(row);

			tableHelper.exportRowHeader(rowHeight);

			for(int col = 0; col < grid[0].length; col++)
			{
				JRExporterGridCell gridCell = grid[row][col];
				if (gridCell.getType() == JRExporterGridCell.TYPE_OCCUPIED_CELL)
				{
					if (emptyCellColSpan > 0)
					{
						//tableHelper.exportEmptyCell(gridCell, emptyCellColSpan);
						emptyCellColSpan = 0;
						emptyCellWidth = 0;
					}

					OccupiedGridCell occupiedGridCell = (OccupiedGridCell)gridCell;
					ElementGridCell elementGridCell = (ElementGridCell)grid[occupiedGridCell.getRow()][occupiedGridCell.getCol()];
					tableHelper.exportOccupiedCells(elementGridCell);
					col += elementGridCell.getColSpan() - 1;
				}
				else if(gridCell.getWrapper() != null)
				{
					if (emptyCellColSpan > 0)
					{
						//writeEmptyCell(tableHelper, gridCell, emptyCellColSpan, emptyCellWidth, rowHeight);
						emptyCellColSpan = 0;
						emptyCellWidth = 0;
					}

					element = gridCell.getWrapper().getElement();

					if (element instanceof JRPrintLine)
					{
						exportLine(tableHelper, (JRPrintLine)element, gridCell);
					}
					else if (element instanceof JRPrintRectangle)
					{
						exportRectangle(tableHelper, (JRPrintRectangle)element, gridCell);
					}
					else if (element instanceof JRPrintEllipse)
					{
						exportEllipse(tableHelper, (JRPrintEllipse)element, gridCell);
					}
					else if (element instanceof JRPrintImage)
					{
						exportImage(tableHelper, (JRPrintImage)element, gridCell);
					}
					else if (element instanceof JRPrintText)
					{
						exportText(tableHelper, (JRPrintText)element, gridCell);
					}
					else if (element instanceof JRPrintFrame)
					{
						exportFrame(tableHelper, (JRPrintFrame)element, gridCell);
					}

					col += gridCell.getColSpan() - 1;
				}
				else
				{
					emptyCellColSpan++;
					emptyCellWidth += gridCell.getWidth();
					tableHelper.exportEmptyCell(gridCell, 1);
				}
			}

			if (emptyCellColSpan > 0)
			{
				//writeEmptyCell(tableHelper, null, emptyCellColSpan, emptyCellWidth, rowHeight);
			}

			tableHelper.exportRowFooter();
		}

		tableHelper.exportFooter();
	}


	/**
	 *
	 */
	protected void exportLine(TableHelper tableHelper, JRPrintLine line, JRExporterGridCell gridCell) throws IOException
	{
		JRLineBox box = new JRBaseLineBox(null);
		JRPen pen = null;
		float ratio = line.getWidth() / line.getHeight();
		if (ratio > 1)
		{
			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				pen = box.getTopPen();
			}
			else
			{
				pen = box.getBottomPen();
			}
		}
		else
		{
			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				pen = box.getLeftPen();
			}
			else
			{
				pen = box.getRightPen();
			}
		}
		pen.setLineColor(line.getLinePen().getLineColor());
		pen.setLineStyle(line.getLinePen().getLineStyle());
		pen.setLineWidth(line.getLinePen().getLineWidth());

		docWriter.write("    <w:tc> \r\n");
		docWriter.write("     <w:tcPr> \r\n");
		if (gridCell.getColSpan() > 1)
		{
			docWriter.write("      <w:gridSpan w:val=\"" + gridCell.getColSpan() +"\" /> \r\n");
		}
		if (gridCell.getRowSpan() > 1)
		{
			docWriter.write("      <w:vMerge w:val=\"restart\" /> \r\n");
		}
		
		tableHelper.getCellHelper().getBorderHelper().export(box);

		docWriter.write("     </w:tcPr> \r\n");

		docWriter.write("<w:p/>");

		docWriter.write("    </w:tc> \r\n");
	}


	/**
	 *
	 */
	protected void exportRectangle(TableHelper tableHelper, JRPrintRectangle rectangle, JRExporterGridCell gridCell) throws IOException
	{
		tableHelper.getCellHelper().exportHeader(rectangle, gridCell);
		docWriter.write("<w:p/>");
		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected void exportEllipse(TableHelper tableHelper, JRPrintEllipse ellipse, JRExporterGridCell gridCell) throws IOException
	{
		tableHelper.getCellHelper().exportHeader(ellipse, gridCell);
		docWriter.write("<w:p/>");
//		tempBodyWriter.write("<text:p>");
//		insertPageAnchor();
//		tempBodyWriter.write(
//			"<draw:ellipse text:anchor-type=\"paragraph\" "
//			+ "draw:style-name=\"" + styleCache.getGraphicStyle(ellipse) + "\" "
//			+ "svg:width=\"" + Utility.translatePixelsToInches(ellipse.getWidth()) + "in\" "
//			+ "svg:height=\"" + Utility.translatePixelsToInches(ellipse.getHeight()) + "in\" "
//			+ "svg:x=\"0in\" "
//			+ "svg:y=\"0in\">"
//			+ "<text:p/></draw:ellipse></text:p>"
//			);
		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected void exportText(TableHelper tableHelper, JRPrintText text, JRExporterGridCell gridCell) throws IOException
	{
		tableHelper.getCellHelper().exportHeader(text, gridCell);

		JRStyledText styledText = getStyledText(text);

		int textLength = 0;

		if (styledText != null)
		{
			textLength = styledText.length();
		}

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
		docWriter.write("     <w:p> \r\n");

		tableHelper.getParagraphHelper().exportProps(text);
		
//		insertPageAnchor();
//		if (text.getAnchorName() != null)
//		{
//			tempBodyWriter.write("<text:bookmark text:name=\"");
//			tempBodyWriter.write(text.getAnchorName());
//			tempBodyWriter.write("\"/>");
//		}
//
//		boolean startedHyperlink = startHyperlink(text, true);

		if (textLength > 0)
		{
			exportStyledText(text.getStyle(), styledText);
		}

//		if (startedHyperlink)
//		{
//			endHyperlink(true);
//		}

		docWriter.write("     </w:p> \r\n");
		docWriter.flush();

		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected void exportStyledText(JRStyle style, JRStyledText styledText) throws IOException
	{
		String text = styledText.getText();

		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			runHelper.export(style, iterator.getAttributes(), text.substring(iterator.getIndex(), runLimit));

			iterator.setIndex(runLimit);
		}
	}


	/**
	 *
	 */
	protected void exportImage(TableHelper tableHelper, JRPrintImage image, JRExporterGridCell gridCell) throws JRException, IOException
	{
		int topPadding = 
			Math.max(image.getLineBox().getTopPadding().intValue(), Math.round(image.getLineBox().getTopPen().getLineWidth().floatValue()));
		int leftPadding = 
			Math.max(image.getLineBox().getLeftPadding().intValue(), Math.round(image.getLineBox().getLeftPen().getLineWidth().floatValue()));
		int bottomPadding = 
			Math.max(image.getLineBox().getBottomPadding().intValue(), Math.round(image.getLineBox().getBottomPen().getLineWidth().floatValue()));
		int rightPadding = 
			Math.max(image.getLineBox().getRightPadding().intValue(), Math.round(image.getLineBox().getRightPen().getLineWidth().floatValue()));

		int availableImageWidth = image.getWidth() - leftPadding - rightPadding;
		availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

		int availableImageHeight = image.getHeight() - topPadding - bottomPadding;
		availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

		int width = availableImageWidth;
		int height = availableImageHeight;

		int xoffset = 0;
		int yoffset = 0;

		tableHelper.getCellHelper().exportHeader(image, gridCell);

		docWriter.write("<w:p>");

		JRRenderable renderer = image.getRenderer();

		if (
			renderer != null &&
			availableImageWidth > 0 &&
			availableImageHeight > 0
			)
		{
			if (renderer.getType() == JRRenderable.TYPE_IMAGE && !image.isLazy())
			{
				// Non-lazy image renderers are all asked for their image data at some point.
				// Better to test and replace the renderer now, in case of lazy load error.
				renderer = JRImageRenderer.getOnErrorRendererForImageData(renderer, image.getOnErrorType());
			}
		}
		else
		{
			renderer = null;
		}

		if (renderer != null)
		{
			float xalignFactor = getXAlignFactor(image);
			float yalignFactor = getYAlignFactor(image);

			switch (image.getScaleImage())
			{
				case JRImage.SCALE_IMAGE_FILL_FRAME :
				{
					width = availableImageWidth;
					height = availableImageHeight;
					xoffset = 0;
					yoffset = 0;
					break;
				}
				case JRImage.SCALE_IMAGE_CLIP :
				case JRImage.SCALE_IMAGE_RETAIN_SHAPE :
				default :
				{
					double normalWidth = availableImageWidth;
					double normalHeight = availableImageHeight;

					if (!image.isLazy())
					{
						// Image load might fail.
						JRRenderable tmpRenderer =
							JRImageRenderer.getOnErrorRendererForDimension(renderer, image.getOnErrorType());
						Dimension2D dimension = tmpRenderer == null ? null : tmpRenderer.getDimension();
						// If renderer was replaced, ignore image dimension.
						if (tmpRenderer == renderer && dimension != null)
						{
							normalWidth = dimension.getWidth();
							normalHeight = dimension.getHeight();
						}
					}

					if (availableImageHeight > 0)
					{
						double ratio = (double)normalWidth / (double)normalHeight;

						if( ratio > availableImageWidth / (double)availableImageHeight )
						{
							width = availableImageWidth;
							height = (int)(width/ratio);

						}
						else
						{
							height = availableImageHeight;
							width = (int)(ratio * height);
						}
					}

					xoffset = (int)(xalignFactor * (availableImageWidth - width));
					yoffset = (int)(yalignFactor * (availableImageHeight - height));
				}
			}

//			insertPageAnchor();
//			if (image.getAnchorName() != null)
//			{
//				tempBodyWriter.write("<text:bookmark text:name=\"");
//				tempBodyWriter.write(image.getAnchorName());
//				tempBodyWriter.write("\"/>");
//			}


//			boolean startedHyperlink = startHyperlink(image,false);

//			docWriter.write("<draw:frame text:anchor-type=\"paragraph\" "
//					+ "draw:style-name=\"" + styleCache.getGraphicStyle(image) + "\" "
//					+ "svg:x=\"" + Utility.translatePixelsToInches(leftPadding + xoffset) + "in\" "
//					+ "svg:y=\"" + Utility.translatePixelsToInches(topPadding + yoffset) + "in\" "
//					+ "svg:width=\"" + Utility.translatePixelsToInches(width) + "in\" "
//					+ "svg:height=\"" + Utility.translatePixelsToInches(height) + "in\">"
//					);
//			docWriter.write("<draw:image ");
//			docWriter.write(" xlink:href=\"" + getImagePath(renderer, image.isLazy(), gridCell) + "\"");
//			docWriter.write(" xlink:type=\"simple\"");
//			docWriter.write(" xlink:show=\"embed\"");
//			docWriter.write(" xlink:actuate=\"onLoad\"");
//			docWriter.write("/>\n");


			docWriter.write("<w:r>\n"); 
			docWriter.write("<w:drawing>\n");
			docWriter.write("<wp:inline distT=\"0\" distB=\"0\" distL=\"0\" distR=\"0\">\n");
			docWriter.write("<wp:extent cx=\"" + Utility.emu(width) + "\" cy=\"" + Utility.emu(height) + "\"/>\n");
			docWriter.write("<wp:effectExtent l=\"19050\" t=\"0\" r=\"0\" b=\"0\"/>\n");
			docWriter.write("<wp:docPr id=\"1\" name=\"Picture 0\" descr=\"Water lilies.jpg\"/>\n");
			docWriter.write("<wp:cNvGraphicFramePr>\n");
			docWriter.write("<a:graphicFrameLocks noChangeAspect=\"1\"/>\n");
			docWriter.write("</wp:cNvGraphicFramePr>\n");
			docWriter.write("<a:graphic>\n");
			docWriter.write("<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">\n");
			docWriter.write("<pic:pic>\n");
			docWriter.write("<pic:nvPicPr><pic:cNvPr id=\"0\" name=\"Water lilies.jpg\"/><pic:cNvPicPr/></pic:nvPicPr>\n");
			docWriter.write("<pic:blipFill>\n");
			docWriter.write("<a:blip r:embed=\"" + getImagePath(renderer, image.isLazy(), gridCell) + "\"/><a:stretch><a:fillRect/></a:stretch>\n");
			docWriter.write("</pic:blipFill>\n");
			docWriter.write("<pic:spPr><a:xfrm><a:off x=\"0\" y=\"0\"/><a:ext cx=\"" + Utility.emu(width) + "\" cy=\"" + Utility.emu(height) + "\"/></a:xfrm><a:prstGeom prst=\"rect\"><a:avLst/></a:prstGeom></pic:spPr>\n");
			docWriter.write("</pic:pic>\n");
			docWriter.write("</a:graphicData>\n");
			docWriter.write("</a:graphic>\n");
			docWriter.write("</wp:inline>\n");
			docWriter.write("</w:drawing>\n");
			docWriter.write("</w:r>"); 

//			if(startedHyperlink)
//			{
//				endHyperlink(false);
//			}
		}

		docWriter.write("</w:p>");

		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected String getImagePath(JRRenderable renderer, boolean isLazy, JRExporterGridCell gridCell) throws IOException
	{
		String imagePath = null;

		if (renderer != null)
		{
			if (renderer.getType() == JRRenderable.TYPE_IMAGE && rendererToImagePathMap.containsKey(renderer.getId()))
			{
				imagePath = (String)rendererToImagePathMap.get(renderer.getId());
			}
			else
			{
//				if (isLazy)//FIXMEDOCX learn how to link images
//				{
//					imagePath = ((JRImageRenderer)renderer).getImageLocation();
//				}
//				else
//				{
					JRPrintElementIndex imageIndex = getElementIndex(gridCell);
					imagesToProcess.add(imageIndex);

					String imageName = getImageName(imageIndex);
					imagePath = imageName;
					//imagePath = "Pictures/" + imageName;
//				}

				rendererToImagePathMap.put(renderer.getId(), imagePath);
			}
		}

		return imagePath;
	}


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
	 *
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
	 */
	public static String getImageName(JRPrintElementIndex printElementIndex)
	{
		return IMAGE_NAME_PREFIX + printElementIndex.toString();
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
	 * In deep grids, this is called only for empty frames.
	 */
	protected void exportFrame(TableHelper tableHelper, JRPrintFrame frame, JRExporterGridCell gridCell) throws IOException, JRException
	{
		tableHelper.getCellHelper().exportHeader(frame, gridCell);
//		tableHelper.getCellHelper().exportProps(gridCell);

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
			exportGrid(layout, frameIndex);
		}
		finally
		{
			if (appendBackcolor)
			{
				restoreBackcolor();
			}
		}
		
		tableHelper.getParagraphHelper().exportEmptyParagraph();
		tableHelper.getCellHelper().exportFooter();
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


	private float getXAlignFactor(JRPrintImage image)
	{
		float xalignFactor = 0f;
		switch (image.getHorizontalAlignment())
		{
			case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
			{
				xalignFactor = 1f;
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_CENTER :
			{
				xalignFactor = 0.5f;
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_LEFT :
			default :
			{
				xalignFactor = 0f;
				break;
			}
		}
		return xalignFactor;
	}


	private float getYAlignFactor(JRPrintImage image)
	{
		float yalignFactor = 0f;
		switch (image.getVerticalAlignment())
		{
			case JRAlignment.VERTICAL_ALIGN_BOTTOM :
			{
				yalignFactor = 1f;
				break;
			}
			case JRAlignment.VERTICAL_ALIGN_MIDDLE :
			{
				yalignFactor = 0.5f;
				break;
			}
			case JRAlignment.VERTICAL_ALIGN_TOP :
			default :
			{
				yalignFactor = 0f;
				break;
			}
		}
		return yalignFactor;
	}

//	protected boolean startHyperlink(JRPrintHyperlink link, boolean isText) throws IOException
//	{
//		String href = getHyperlinkURL(link);
//
//		if (href != null)
//		{
//			if(isText)
//			{
//				tempBodyWriter.write("<text:a xlink:href=\"");
//			}
//			else
//			{
//				tempBodyWriter.write("<draw:a xlink:type=\"simple\" xlink:href=\"");
//			}
//			tempBodyWriter.write(href);
//			tempBodyWriter.write("\"");
//
//
//			String target = getHyperlinkTarget(link);//FIXMETARGET
//			if (target != null)
//			{
//				tempBodyWriter.write(" office:target-frame-name=\"");
//				tempBodyWriter.write(target);
//				tempBodyWriter.write("\"");
//				if(target.equals("_blank"))
//				{
//					tempBodyWriter.write(" xlink:show=\"new\"");
//				}
//			}
///*
// * tooltips are unavailable for the moment
// *
//			if (link.getHyperlinkTooltip() != null)
//			{
//				tempBodyWriter.write(" xlink:title=\"");
//				tempBodyWriter.write(JRStringUtil.xmlEncode(link.getHyperlinkTooltip()));
//				tempBodyWriter.write("\"");
//			}
//*/
//			tempBodyWriter.write(">");
//		}
//
//		return href != null;
//	}


	protected String getHyperlinkTarget(JRPrintHyperlink link)
	{
		String target = null;
		switch(link.getHyperlinkTarget())
		{
			case JRHyperlink.HYPERLINK_TARGET_SELF :
			{
				target = "_self";
				break;
			}
			case JRHyperlink.HYPERLINK_TARGET_BLANK :
			default :
			{
				target = "_blank";
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


//	protected void endHyperlink(boolean isText) throws IOException
//	{
//		if(isText)
//			tempBodyWriter.write("</text:a>");
//		else
//			tempBodyWriter.write("</draw:a>");
//	}

//	protected void insertPageAnchor() throws IOException
//	{
//		if(startPage)
//		{
//			tempBodyWriter.write("<text:bookmark text:name=\"");
//			tempBodyWriter.write(JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1));
//			tempBodyWriter.write("\"/>\n");
//			startPage = false;
//		}
//	}
	
	protected abstract ExporterNature getExporterNature(ExporterFilter filter);

	protected abstract String getExporterPropertiesPrefix();//FIXMEDOCX move this to abstract exporter

}


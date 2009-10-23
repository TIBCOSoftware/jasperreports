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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElementIndex;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
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
import net.sf.jasperreports.engine.export.ExporterNature;
import net.sf.jasperreports.engine.export.GenericElementHandlerEnviroment;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.zip.ExportZipEntry;
import net.sf.jasperreports.engine.export.zip.FileBufferedZipEntry;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRTypeSniffer;


/**
 * Exports a JasperReports document to XLSX format. It has character output type and exports the document to a
 * grid-based layout.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRDocxExporter.java 2943 2009-07-23 19:45:12Z teodord $
 */
public class JRXlsxExporter extends JRXlsAbstractExporter
{
	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String XLSX_EXPORTER_KEY = JRProperties.PROPERTY_PREFIX + "xlsx";

	protected static final String XLSX_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.xlsx.";

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
	protected XlsxZip xlsxZip = null;
	protected WorkbookHelper wbHelper = null;
	protected Writer wbWriter = null;
	protected XlsxRelsHelper relsHelper = null;
	protected Writer relsWriter = null;
	protected ContentTypesHelper ctHelper = null;
	protected Writer ctWriter = null;
	protected SheetHelper sheetHelper = null;
	//protected Writer sheetWriter = null;
	//protected TableHelper tableHelper = null;
	protected XlsxCellHelper cellHelper = null;//FIXMEXLSX maybe cell helper should be part of sheet helper, just like in table helper

	protected JRExportProgressMonitor progressMonitor = null;
	protected Map rendererToImagePathMap = null;
	protected Map imageMaps;
	protected List imagesToProcess = null;
//	protected Map hyperlinksMap = null;

	protected int reportIndex = 0;
	protected int pageIndex = 0;
	protected int tableIndex = 0;
	protected boolean startPage;


	/**
	 *
	 */
	protected boolean isWrapBreakWord = false;

	protected Map fontMap = null;

	protected LinkedList backcolorStack;
	protected Color backcolor;

	private XlsxRunHelper runHelper = null;

	protected ExporterNature nature = null;

	
	public JRXlsxExporter()
	{
		backcolorStack = new LinkedList();
		backcolor = null;
	}


	/**
	 *
	 */
	protected void setParameters()
	{
		super.setParameters();
//
//		formatPatternsMap = (Map)getParameter(JRXlsExporterParameter.FORMAT_PATTERNS_MAP);

		nature = new JRXlsxExporterNature(filter);
//		
//		password = 
//			getStringParameter(
//				JExcelApiExporterParameter.PASSWORD,
//				JExcelApiExporterParameter.PROPERTY_PASSWORD
//				);
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
	 *
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
	*/
	
//	protected int exportPage(
//		JRPrintPage page, 
//		CutsInfo xCuts, 
//		int startRow
//		) throws JRException 
//	{
//		tableHelper = 
//			new TableHelper(
//				sheetWriter, 
//				xCuts,
//				runHelper,
//				reportIndex != 0 || pageIndex != startPageIndex
//				);
//
//		tableHelper.exportHeader();
//		
//		int result = super.exportPage(page, xCuts, startRow);
//		
//		tableHelper.exportFooter(
//			reportIndex != jasperPrintList.size() - 1 && pageIndex == endPageIndex, 
//			jasperPrint.getPageWidth(), 
//			jasperPrint.getPageHeight()
//			);
//		
//		return result;
//	}


	/**
	 *
	 *
	protected void exportGrid(JRGridLayout gridLayout, JRPrintElementIndex frameIndex) throws JRException, IOException
	{
		CutsInfo xCuts = gridLayout.getXCuts();
		JRExporterGridCell[][] grid = gridLayout.getGrid();

		if (grid.length > 0 && grid[0].length > 63)
		{
			throw new JRException("The DOCX format does not support more than 63 columns in a table.");
		}
		
		TableHelper tableHelper = 
			new TableHelper(
				wbWriter, 
				xCuts,
				runHelper,
				frameIndex == null && (reportIndex != 0 || pageIndex != startPageIndex)
				);

		tableHelper.exportHeader();

		JRPrintElement element = null;
		for(int row = 0; row < grid.length; row++)
		{
			int emptyCellColSpan = 0;
			int emptyCellWidth = 0;

			int maxBottomPadding = 0; //for some strange reason, the bottom margin affects the row height; subtracting it here
			for(int col = 0; col < grid[0].length; col++)
			{
				JRExporterGridCell gridCell = grid[row][col];
				JRLineBox box = gridCell.getBox();
				if (
					box != null 
					&& box.getBottomPadding() != null 
					&& maxBottomPadding < box.getBottomPadding().intValue()
					)
				{
					maxBottomPadding = box.getBottomPadding().intValue();
				}
			}
			int rowHeight = gridLayout.getRowHeight(row) - maxBottomPadding;
			
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

		tableHelper.exportFooter(
			frameIndex == null && reportIndex != jasperPrintList.size() - 1 && pageIndex == endPageIndex , 
			jasperPrint.getPageWidth(), 
			jasperPrint.getPageHeight()
			);
	}


	/**
	 *
	 */
	protected void exportLine(DocxTableHelper tableHelper, JRPrintLine line, JRExporterGridCell gridCell) throws IOException
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

		gridCell.setBox(box);//caution: this is the only exporter that sets the cell box
		
		tableHelper.getCellHelper().exportHeader(line, gridCell);
		wbWriter.write("<w:p/>");
		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected void exportRectangle(DocxTableHelper tableHelper, JRPrintRectangle rectangle, JRExporterGridCell gridCell) throws IOException
	{
		JRLineBox box = new JRBaseLineBox(null);
		JRPen pen = box.getPen();
		pen.setLineColor(rectangle.getLinePen().getLineColor());
		pen.setLineStyle(rectangle.getLinePen().getLineStyle());
		pen.setLineWidth(rectangle.getLinePen().getLineWidth());

		gridCell.setBox(box);//caution: this is the only exporter that sets the cell box
		
		tableHelper.getCellHelper().exportHeader(rectangle, gridCell);
		wbWriter.write("<w:p/>");
		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected void exportEllipse(DocxTableHelper tableHelper, JRPrintEllipse ellipse, JRExporterGridCell gridCell) throws IOException
	{
		JRLineBox box = new JRBaseLineBox(null);
		JRPen pen = box.getPen();
		pen.setLineColor(ellipse.getLinePen().getLineColor());
		pen.setLineStyle(ellipse.getLinePen().getLineStyle());
		pen.setLineWidth(ellipse.getLinePen().getLineWidth());

		gridCell.setBox(box);//caution: this is the only exporter that sets the cell box
		
		tableHelper.getCellHelper().exportHeader(ellipse, gridCell);
		wbWriter.write("<w:p/>");
		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected void exportStyledText(JRStyle style, JRStyledText styledText, Locale locale)
	{
		String text = styledText.getText();

		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			runHelper.export(
				style, iterator.getAttributes(), 
				text.substring(iterator.getIndex(), runLimit),
				locale
				);

			iterator.setIndex(runLimit);
		}
	}


	/**
	 *
	 */
	protected void exportImage(DocxTableHelper tableHelper, JRPrintImage image, JRExporterGridCell gridCell) throws JRException, IOException
	{
		int leftPadding = image.getLineBox().getLeftPadding().intValue();
		int topPadding = image.getLineBox().getTopPadding().intValue();//FIXMEDOCX maybe consider border thickness
		int rightPadding = image.getLineBox().getRightPadding().intValue();
		int bottomPadding = image.getLineBox().getBottomPadding().intValue();

		int availableImageWidth = image.getWidth() - leftPadding - rightPadding;
		availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

		int availableImageHeight = image.getHeight() - topPadding - bottomPadding;
		availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

		tableHelper.getCellHelper().exportHeader(image, gridCell);

		wbWriter.write("<w:p>");

		JRRenderable renderer = image.getRenderer();

		if (
			renderer != null &&
			availableImageWidth > 0 &&
			availableImageHeight > 0
			)
		{
			if (renderer.getType() == JRRenderable.TYPE_IMAGE)
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
			int width = availableImageWidth;
			int height = availableImageHeight;

			double normalWidth = availableImageWidth;
			double normalHeight = availableImageHeight;

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

			double cropTop = 0;
			double cropLeft = 0;
			double cropBottom = 0;
			double cropRight = 0;
			
			switch (image.getScaleImage())
			{
				case JRImage.SCALE_IMAGE_FILL_FRAME :
				{
					width = availableImageWidth;
					height = availableImageHeight;
					break;
				}
				case JRImage.SCALE_IMAGE_CLIP :
				{
					if (normalWidth > availableImageWidth)
					{
						switch (image.getHorizontalAlignment())
						{
							case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
							{
								cropLeft = 65536 * (normalWidth - availableImageWidth) / normalWidth;
								cropRight = 0;
								break;
							}
							case JRAlignment.HORIZONTAL_ALIGN_CENTER :
							{
								cropLeft = 65536 * (- availableImageWidth + normalWidth) / normalWidth / 2;
								cropRight = cropLeft;
								break;
							}
							case JRAlignment.HORIZONTAL_ALIGN_LEFT :
							default :
							{
								cropLeft = 0;
								cropRight = 65536 * (normalWidth - availableImageWidth) / normalWidth;
								break;
							}
						}
						width = availableImageWidth;
						cropLeft = cropLeft / 0.75d;
						cropRight = cropRight / 0.75d;
					}
					else
					{
						width = (int)normalWidth;
					}

					if (normalHeight > availableImageHeight)
					{
						switch (image.getVerticalAlignment())
						{
							case JRAlignment.VERTICAL_ALIGN_TOP :
							{
								cropTop = 0;
								cropBottom = 65536 * (normalHeight - availableImageHeight) / normalHeight;
								break;
							}
							case JRAlignment.VERTICAL_ALIGN_MIDDLE :
							{
								cropTop = 65536 * (normalHeight - availableImageHeight) / normalHeight / 2;
								cropBottom = cropTop;
								break;
							}
							case JRAlignment.VERTICAL_ALIGN_BOTTOM :
							default :
							{
								cropTop = 65536 * (normalHeight - availableImageHeight) / normalHeight;
								cropBottom = 0;
								break;
							}
						}
						height = availableImageHeight;
						cropTop = cropTop / 0.75d;
						cropBottom = cropBottom / 0.75d;
					}
					else
					{
						height = (int)normalHeight;
					}

					break;
				}
				case JRImage.SCALE_IMAGE_RETAIN_SHAPE :
				default :
				{
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
				}
			}

//			insertPageAnchor();
//			if (image.getAnchorName() != null)
//			{
//				tempBodyWriter.write("<text:bookmark text:name=\"");
//				tempBodyWriter.write(image.getAnchorName());
//				tempBodyWriter.write("\"/>");
//			}


			boolean startedHyperlink = startHyperlink(image,false);

			wbWriter.write("<w:r>\n"); 
			wbWriter.write("<w:drawing>\n");
			wbWriter.write("<wp:anchor distT=\"0\" distB=\"0\" distL=\"0\" distR=\"0\" simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"0\" locked=\"1\" layoutInCell=\"1\" allowOverlap=\"1\">");
			wbWriter.write("<wp:simplePos x=\"0\" y=\"0\"/>");
			wbWriter.write("<wp:positionH relativeFrom=\"column\"><wp:align>" + XlsxParagraphHelper.getHorizontalAlignment(new Byte(image.getHorizontalAlignment())) + "</wp:align></wp:positionH>");
			wbWriter.write("<wp:positionV relativeFrom=\"line\"><wp:posOffset>0</wp:posOffset></wp:positionV>");
//			wbWriter.write("<wp:positionV relativeFrom=\"line\"><wp:align>" + CellHelper.getVerticalAlignment(new Byte(image.getVerticalAlignment())) + "</wp:align></wp:positionV>");
			
			wbWriter.write("<wp:extent cx=\"" + Utility.emu(width) + "\" cy=\"" + Utility.emu(height) + "\"/>\n");
			wbWriter.write("<wp:wrapNone/>");
			wbWriter.write("<wp:docPr id=\"" + image.hashCode() + "\" name=\"Picture\"/>\n");
			wbWriter.write("<a:graphic>\n");
			wbWriter.write("<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">\n");
			wbWriter.write("<pic:pic>\n");
			wbWriter.write("<pic:nvPicPr><pic:cNvPr id=\"" + image.hashCode() + "\" name=\"Picture\"/><pic:cNvPicPr/></pic:nvPicPr>\n");
			wbWriter.write("<pic:blipFill>\n");
			wbWriter.write("<a:blip r:embed=\"" + getImagePath(renderer, image.isLazy(), gridCell) + "\"/>");
			wbWriter.write("<a:srcRect");
			if (cropLeft > 0)
				wbWriter.write(" l=\"" + (int)cropLeft + "\"");
			if (cropTop > 0)
				wbWriter.write(" t=\"" + (int)cropTop + "\"");
			if (cropRight > 0)
				wbWriter.write(" r=\"" + (int)cropRight + "\"");
			if (cropBottom > 0)
				wbWriter.write(" b=\"" + (int)cropBottom + "\"");
			wbWriter.write("/>");
			wbWriter.write("<a:stretch><a:fillRect/></a:stretch>\n");
			wbWriter.write("</pic:blipFill>\n");
			wbWriter.write("<pic:spPr><a:xfrm><a:off x=\"0\" y=\"0\"/><a:ext cx=\"" + Utility.emu(width) + "\" cy=\"" + Utility.emu(height) + "\"/>");
			wbWriter.write("</a:xfrm><a:prstGeom prst=\"rect\"></a:prstGeom></pic:spPr>\n");
			wbWriter.write("</pic:pic>\n");
			wbWriter.write("</a:graphicData>\n");
			wbWriter.write("</a:graphic>\n");
			wbWriter.write("</wp:anchor>\n");
			wbWriter.write("</w:drawing>\n");
			wbWriter.write("</w:r>"); 

			if(startedHyperlink)
			{
				endHyperlink(false);
			}
		}

		wbWriter.write("</w:p>");

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
	 *
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

	protected boolean startHyperlink(JRPrintHyperlink link, boolean isText)
	{
		String href = getHyperlinkURL(link);

		if (href != null)
		{
//			String id = (String)hyperlinksMap.get(href);
//			if (id == null)
//			{
//				id = "link" + hyperlinksMap.size();
//				hyperlinksMap.put(href, id);
//			}
//			
//			wbWriter.write("<w:hyperlink r:id=\"" + id + "\"");
//
//			String target = getHyperlinkTarget(link);//FIXMETARGET
//			if (target != null)
//			{
//				wbWriter.write(" tgtFrame=\"" + target + "\"");
//			}
//
//			wbWriter.write(">\n");

			try
			{
				wbWriter.write("<w:r><w:fldChar w:fldCharType=\"begin\"/></w:r>\n");
				wbWriter.write("<w:r><w:instrText xml:space=\"preserve\"> HYPERLINK \"" + href + "\"");

				String target = getHyperlinkTarget(link);//FIXMETARGET
				if (target != null)
				{
					wbWriter.write(" \\t \"" + target + "\"");
				}

				String tooltip = link.getHyperlinkTooltip(); 
				if (tooltip != null)
				{
					wbWriter.write(" \\o \"" + JRStringUtil.xmlEncode(tooltip) + "\"");
				}

				wbWriter.write(" </w:instrText></w:r>\n");
				wbWriter.write("<w:r><w:fldChar w:fldCharType=\"separate\"/></w:r>\n");
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}

		return href != null;
	}


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
//					if (link.getHyperlinkAnchor() != null)
//					{
//						href = "#" + link.getHyperlinkAnchor();
//					}
					break;
				}
				case JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE :
				{
//					if (link.getHyperlinkPage() != null)
//					{
//						href = "#" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString();
//					}
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
//					if (
//						link.getHyperlinkReference() != null &&
//						link.getHyperlinkPage() != null
//						)
//					{
//						href = link.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + link.getHyperlinkPage().toString();
//					}
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


	protected void endHyperlink(boolean isText)
	{
//		wbWriter.write("</w:hyperlink>\n");
		try
		{
			wbWriter.write("<w:r><w:fldChar w:fldCharType=\"end\"/></w:r>\n");
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

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
	
	/**
	 *
	 */
	protected String getExporterPropertiesPrefix()
	{
		return XLSX_EXPORTER_PROPERTIES_PREFIX;
	}


	protected void addBlankCell(JRExporterGridCell gridCell, int colIndex,
			int rowIndex) throws JRException {
		// TODO Auto-generated method stub
		
	}


	protected void closeWorkbook(OutputStream os) throws JRException 
	{
		closeSheet();

		try
		{
			wbHelper.exportFooter();

			wbWriter.close();

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

					String mimeType = JRTypeSniffer.getImageMimeType(renderer.getImageType());
					if (mimeType == null)
					{
						mimeType = JRRenderable.MIME_TYPE_JPEG;
					}
					String extension = mimeType.substring(mimeType.lastIndexOf('/') + 1);
					
					String imageName = getImageName(imageIndex);
					
					xlsxZip.addEntry(//FIXMEDOCX optimize with a different implementation of entry
						new FileBufferedZipEntry(
							"word/media/" + imageName + "." + extension,
							renderer.getImageData()
							)
						);
					
					relsHelper.exportImage(imageName, extension);
				}
			}

//			if ((hyperlinksMap != null && hyperlinksMap.size() > 0))
//			{
//				for(Iterator it = hyperlinksMap.keySet().iterator(); it.hasNext();)
//				{
//					String href = (String)it.next();
//					String id = (String)hyperlinksMap.get(href);
	//
//					relsHelper.exportHyperlink(id, href);
//				}
//			}

			relsHelper.exportFooter();

			relsWriter.close();
			
			ctHelper.exportFooter();
			
			ctWriter.close();

			xlsxZip.zipEntries(os);

			xlsxZip.dispose();
		}
		catch (IOException e)
		{
			throw new JRException(e);
		}
	}


	protected void createSheet(String name)
	{
		closeSheet();
		
		wbHelper.exportSheet(sheetIndex + 1);
		ctHelper.exportSheet(sheetIndex + 1);
		relsHelper.exportSheet(sheetIndex + 1);

		ExportZipEntry sheetEntry = xlsxZip.addSheet(sheetIndex + 1);
		Writer sheetWriter = sheetEntry.getWriter();
		sheetHelper = new SheetHelper(sheetWriter);

		cellHelper = new XlsxCellHelper(sheetWriter);
		runHelper = new XlsxRunHelper(sheetWriter, fontMap, null);//FIXMEXLSX check this null

		sheetHelper.exportHeader();
	}


	protected void closeSheet()
	{
		if (sheetHelper != null)
		{
			sheetHelper.exportFooter();
			
			sheetHelper.close();
		}
	}


	protected void exportFrame(JRPrintFrame frame, JRExporterGridCell cell,
			int colIndex, int rowIndex) throws JRException {
		// TODO Auto-generated method stub
		
	}


	protected void exportImage(JRPrintImage image, JRExporterGridCell cell,
			int colIndex, int rowIndex, int emptyCols) throws JRException {
		// TODO Auto-generated method stub
		
	}


	protected void exportLine(JRPrintLine line, JRExporterGridCell cell,
			int colIndex, int rowIndex) throws JRException {
		// TODO Auto-generated method stub
		
	}


	protected void exportRectangle(JRPrintGraphicElement element,
			JRExporterGridCell cell, int colIndex, int rowIndex)
			throws JRException {
		// TODO Auto-generated method stub
		
	}


	protected void exportText(
		JRPrintText text, 
		JRExporterGridCell gridCell,
		int colIndex, 
		int rowIndex
		) throws JRException
	{
		cellHelper.exportHeader(text, gridCell, rowIndex, colIndex);
		sheetHelper.exportMergedCells(rowIndex, colIndex, gridCell.getRowSpan(), gridCell.getColSpan());

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
		
		sheetHelper.write("<is>");//FIXMENOW make writer util

//		tableHelper.getParagraphHelper().exportProps(text);
		
//		insertPageAnchor();
//		if (text.getAnchorName() != null)
//		{
//			tempBodyWriter.write("<text:bookmark text:name=\"");
//			tempBodyWriter.write(text.getAnchorName());
//			tempBodyWriter.write("\"/>");
//		}

		boolean startedHyperlink = startHyperlink(text, true);

		if (textLength > 0)
		{
			exportStyledText(text.getStyle(), styledText, getTextLocale(text));
		}

		if (startedHyperlink)
		{
			endHyperlink(true);
		}

		sheetHelper.write("</is>");
		sheetHelper.flush();

		cellHelper.exportFooter();
	}


	protected void exportGenericElement(
		JRGenericPrintElement element, 
		JRExporterGridCell gridCell, 
		int colIndex, 
		int rowIndex, 
		int emptyCols
		) throws JRException
	{
		// TODO Auto-generated method stub
		
	}


	protected ExporterNature getNature() 
	{
		return nature;
	}


	protected void openWorkbook(OutputStream os) throws JRException 
	{
		rendererToImagePathMap = new HashMap();
		imageMaps = new HashMap();
		imagesToProcess = new ArrayList();
//		hyperlinksMap = new HashMap();

		try
		{
			xlsxZip = new XlsxZip();

			wbWriter = xlsxZip.getWorkbookEntry().getWriter();
			wbHelper = new WorkbookHelper(wbWriter);
			wbHelper.exportHeader();

			relsWriter = xlsxZip.getRelsEntry().getWriter();
			relsHelper = new XlsxRelsHelper(relsWriter);
			relsHelper.exportHeader();

			ctWriter = xlsxZip.getContentTypesEntry().getWriter();
			ctHelper = new ContentTypesHelper(ctWriter);
			ctHelper.exportHeader();
			
			Writer stylesWriter = xlsxZip.getStylesEntry().getWriter();
			new XlsxStyleHelper(stylesWriter, fontMap, getExporterKey()).export(jasperPrintList);
			stylesWriter.close();
		}
		catch (IOException e)
		{
			throw new JRException(e);
		}

//		runHelper = new RunHelper(sheetWriter, fontMap, null);//FIXMEXLSX check this null
	}


	protected void removeColumn(int col) {
		// TODO Auto-generated method stub
		
	}


	protected void setBackground() {
		// TODO Auto-generated method stub
		
	}


	protected void setCell(int colIndex, int rowIndex) {
		// TODO Auto-generated method stub
		
	}


	protected void setColumnWidth(int col, int width) 
	{
		sheetHelper.exportColumn(col, width);
	}


	protected void setRowHeight(
		int rowIndex, 
		int rowHeight
		) throws JRException 
	{
		sheetHelper.exportRow(rowHeight);
	}

	/**
	 *
	 */
	protected String getExporterKey()
	{
		return XLSX_EXPORTER_KEY;
	}
}


/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Special thanks to Google 'Summer of Code 2005' program for supporting this development
 *
 * Contributors:
 * Majid Ali Khan - majidkk@users.sourceforge.net
 * Frank Sch�nheit - Frank.Schoenheit@Sun.COM
 */
package net.sf.jasperreports.engine.export.oasis;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.PrintPageFormat;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.export.Cut;
import net.sf.jasperreports.engine.export.CutsInfo;
import net.sf.jasperreports.engine.export.ElementGridCell;
import net.sf.jasperreports.engine.export.GenericElementHandlerEnviroment;
import net.sf.jasperreports.engine.export.HyperlinkUtil;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRGridLayout;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.LengthUtil;
import net.sf.jasperreports.engine.export.OccupiedGridCell;
import net.sf.jasperreports.engine.export.XlsRowLevelInfo;
import net.sf.jasperreports.engine.export.zip.ExportZipEntry;
import net.sf.jasperreports.engine.export.zip.FileBufferedZipEntry;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.OdsExporterConfiguration;
import net.sf.jasperreports.export.OdsReportConfiguration;
import net.sf.jasperreports.export.XlsReportConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Exports a JasperReports document to Open Document Spreadsheet format. It has character output type
 * and exports the document to a grid-based layout.
 * <p/>
 * The {@link net.sf.jasperreports.engine.export.oasis.JROdsExporter} exporter
 * implementation produces documents that comply with the Open Document Format for
 * Office Applications specifications for spreadsheets. These documents use the 
 * <code>.ods</code> file extension.
 * <p/>
 * Because spreadsheet documents are made of sheets containing cells, this exporter is a
 * grid exporter, as well, therefore having the known limitations of grid exporters. 
 * <p/>
 * Special exporter configuration settings, that can be applied to a 
 * {@link net.sf.jasperreports.engine.export.oasis.JROdsExporter} instance
 * to control its behavior, can be found in {@link net.sf.jasperreports.export.OdsReportConfiguration} 
 * and in its {@link net.sf.jasperreports.export.XlsReportConfiguration} superclass.
 * 
 * @see net.sf.jasperreports.export.OdsReportConfiguration
 * @see net.sf.jasperreports.export.XlsReportConfiguration
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JROdsExporter extends JRXlsAbstractExporter<OdsReportConfiguration, OdsExporterConfiguration, JROdsExporterContext>
{
	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";
	protected static final String DEFAULT_COLUMN = "A";
	protected static final String DEFAULT_ADDRESS = "$A$1";

	/**
	 * 
	 */
	protected OasisZip oasisZip;
	protected ExportZipEntry tempBodyEntry;
	protected ExportZipEntry tempStyleEntry;
	protected WriterHelper tempBodyWriter;
	protected WriterHelper tempStyleWriter;
	protected WriterHelper stylesWriter;

	protected StyleCache styleCache;

	protected DocumentBuilder documentBuilder;
	protected TableBuilder tableBuilder;
	protected StyleBuilder styleBuilder;

	protected boolean startPage;
	protected PrintPageFormat oldPageFormat;
	protected int pageFormatIndex;
	
	protected StringBuffer namedExpressions;

	protected Map<Integer, String> rowStyles = new HashMap<Integer, String>();
	protected Map<Integer, String> columnStyles = new HashMap<Integer, String>();

	@Override
	protected void openWorkbook(OutputStream os) throws JRException, IOException
	{
		oasisZip = new FileBufferedOasisZip(OasisZip.MIME_TYPE_ODS);

		tempBodyEntry = new FileBufferedZipEntry(null);
		tempStyleEntry = new FileBufferedZipEntry(null);

		tempBodyWriter = new WriterHelper(jasperReportsContext, tempBodyEntry.getWriter());
		tempStyleWriter = new WriterHelper(jasperReportsContext, tempStyleEntry.getWriter());

		rowStyles.clear();
		columnStyles.clear();
		documentBuilder = new OdsDocumentBuilder(oasisZip);
		
		styleCache = new StyleCache(jasperReportsContext, tempStyleWriter, getExporterKey());

		stylesWriter = new WriterHelper(jasperReportsContext, oasisZip.getStylesEntry().getWriter());

		styleBuilder = new StyleBuilder(stylesWriter);
		styleBuilder.buildBeforeAutomaticStyles(jasperPrint);

		namedExpressions = new StringBuffer("<table:named-expressions>\n");
		
		pageFormatIndex = -1;
		
		maxColumnIndex = 1023;
	}

	@Override
	protected int exportPage(JRPrintPage page, CutsInfo xCuts, int startRow, String defaultSheetName) throws JRException
	{
		if (oldPageFormat != pageFormat)
		{
			styleBuilder.buildPageLayout(++pageFormatIndex, pageFormat);
			oldPageFormat = pageFormat;
		}

		return super.exportPage(page, xCuts, startRow, defaultSheetName);
	}
	
	@Override
	protected void createSheet(CutsInfo xCuts, SheetInfo sheetInfo)
	{
		startPage = true;
		
//		CutsInfo xCuts = gridLayout.getXCuts();
//		JRExporterGridCell[][] grid = gridLayout.getGrid();

//		TableBuilder tableBuilder = frameIndex == null
//			? new TableBuilder(reportIndex, pageIndex, tempBodyWriter, tempStyleWriter)
//			: new TableBuilder(frameIndex.toString(), tempBodyWriter, tempStyleWriter);
		tableBuilder = new OdsTableBuilder(
							documentBuilder, 
							jasperPrint, 
							pageFormatIndex, 
							pageIndex, 
							tempBodyWriter, 
							tempStyleWriter, 
							styleCache, 
							rowStyles, 
							columnStyles, 
							sheetInfo.sheetName,
							sheetInfo.tabColor);

//		tableBuilder.buildTableStyle(gridLayout.getWidth());
		tableBuilder.buildTableStyle(xCuts.getLastCutOffset());//FIXMEODS
		tableBuilder.buildTableHeader();

//		for(int col = 1; col < xCuts.size(); col++)
//		{
//			tableBuilder.buildColumnStyle(
//					col - 1,
//					xCuts.getCutOffset(col) - xCuts.getCutOffset(col - 1)
//					);
//			tableBuilder.buildColumnHeader(col - 1);
//			tableBuilder.buildColumnFooter();
//		}
	}


	@Override
	protected void closeSheet()
	{
		if (tableBuilder != null)
		{
			tableBuilder.buildRowFooter();
			tableBuilder.buildTableFooter();
		}
	}

	@Override
	protected void closeWorkbook(OutputStream os) throws JRException, IOException
	{
		styleBuilder.buildMasterPages(pageFormatIndex);
		
		stylesWriter.flush();
		tempBodyWriter.flush();
		tempStyleWriter.flush();


		stylesWriter.close();
		tempBodyWriter.close();
		tempStyleWriter.close();
		namedExpressions.append("</table:named-expressions>\n");

		/*   */
		ContentBuilder contentBuilder =
			new ContentBuilder(
				oasisZip.getContentEntry(),
				tempStyleEntry,
				tempBodyEntry,
				styleCache.getFontFaces(),
				OasisZip.MIME_TYPE_ODS,
				namedExpressions
				);
		contentBuilder.build();

		tempStyleEntry.dispose();
		tempBodyEntry.dispose();

		oasisZip.zipEntries(os);

		oasisZip.dispose();
	}

	@Override
	protected void setColumnWidth(int col, int width, boolean autoFit)
	{
		tableBuilder.buildColumnStyle(col - 1, width);
		tableBuilder.buildColumnHeader(width);
		tableBuilder.buildColumnFooter();
	}

	@Override
	protected void setRowHeight(
		int rowIndex, 
		int lastRowHeight, 
		Cut yCut,
		XlsRowLevelInfo levelInfo
		) throws JRException 
	{
		boolean isFlexibleRowHeight = getCurrentItemConfiguration().isFlexibleRowHeight();
		tableBuilder.buildRowStyle(rowIndex, isFlexibleRowHeight ? -1 : lastRowHeight);
		tableBuilder.buildRow(rowIndex, isFlexibleRowHeight ? -1 : lastRowHeight);
	}

	protected void addRowBreak(int rowIndex)
	{
		//FIXMEODS sheet.setRowBreak(rowIndex);
	}

//	@Override
//	protected void setCell(
//		JRExporterGridCell gridCell, 
//		int colIndex,
//		int rowIndex) 
//	{
//		//nothing to do
//	}

	@Override
	protected void addBlankCell(
		JRExporterGridCell gridCell, 
		int colIndex,
		int rowIndex
		) 
	{
		tempBodyWriter.write("<table:table-cell");
		//tempBodyWriter.write(" office:value-type=\"string\"");
		if (gridCell == null)
		{
			tempBodyWriter.write(" table:style-name=\"empty-cell\"");
		}
		else
		{
			tempBodyWriter.write(" table:style-name=\"" + styleCache.getCellStyle(gridCell) + "\"");
		}
//		if (emptyCellColSpan > 1)
//		{
//			tempBodyWriter.write(" table:number-columns-spanned=\"" + emptyCellColSpan + "\"");
//		}
		tempBodyWriter.write("/>\n");
//
//		exportOccupiedCells(emptyCellColSpan - 1);
	}

	@Override
	protected void addOccupiedCell(
		OccupiedGridCell occupiedGridCell,
		int colIndex, 
		int rowIndex
		) throws JRException 
	{
		ElementGridCell elementGridCell = (ElementGridCell)occupiedGridCell.getOccupier();
		addBlankCell(elementGridCell, colIndex, rowIndex);
	}

	@Override
	public void exportText(
		JRPrintText text, 
		JRExporterGridCell gridCell,
		int colIndex, 
		int rowIndex
		) throws JRException
	{
		tableBuilder.exportText(text, gridCell, isShrinkToFit(text), isWrapText(text), isIgnoreTextFormatting(text));
		XlsReportConfiguration configuration = getCurrentItemConfiguration();
		if (!configuration.isIgnoreAnchors() && text.getAnchorName() != null)
		{
			String cellAddress = "$&apos;" + tableBuilder.getTableName() + "&apos;." + getCellAddress(rowIndex, colIndex);
			int lastCol = Math.max(0, colIndex + gridCell.getColSpan() -1);
			String cellRangeAddress = getCellAddress(rowIndex + gridCell.getRowSpan() - 1, lastCol);
			namedExpressions.append("<table:named-range table:name=\""+ JRStringUtil.xmlEncode(text.getAnchorName()) +"\" table:base-cell-address=\"" + cellAddress +"\" table:cell-range-address=\"" + cellAddress +":" +cellRangeAddress +"\"/>\n");
		}
	}

	@Override
	public void exportImage(
		JRPrintImage image, 
		JRExporterGridCell gridCell,
		int colIndex, 
		int rowIndex, 
		int emptyCols, 
		int yCutsRow,
		JRGridLayout layout
		) throws JRException 
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

		tableBuilder.buildCellHeader(styleCache.getCellStyle(gridCell), gridCell.getColSpan(), gridCell.getRowSpan());

		Renderable renderer = image.getRenderable();

		if (
			renderer != null &&
			availableImageWidth > 0 &&
			availableImageHeight > 0
			)
		{
			if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE && !image.isLazy())
			{
				// Non-lazy image renderers are all asked for their image data at some point.
				// Better to test and replace the renderer now, in case of lazy load error.
				renderer = RenderableUtil.getInstance(getJasperReportsContext()).getOnErrorRendererForImageData(renderer, image.getOnErrorTypeValue());
			}
		}
		else
		{
			renderer = null;
		}

		if (renderer != null)
		{
			float xalignFactor = tableBuilder.getXAlignFactor(image);
			float yalignFactor = tableBuilder.getYAlignFactor(image);

			switch (image.getScaleImageValue())
			{
				case FILL_FRAME :
				{
					width = availableImageWidth;
					height = availableImageHeight;
					xoffset = 0;
					yoffset = 0;
					break;
				}
				case CLIP :
				case RETAIN_SHAPE :
				default :
				{
					double normalWidth = availableImageWidth;
					double normalHeight = availableImageHeight;

					if (!image.isLazy())
					{
						// Image load might fail.
						Renderable tmpRenderer =
							RenderableUtil.getInstance(getJasperReportsContext()).getOnErrorRendererForDimension(renderer, image.getOnErrorTypeValue());
						Dimension2D dimension = tmpRenderer == null ? null : tmpRenderer.getDimension(getJasperReportsContext());
						// If renderer was replaced, ignore image dimension.
						if (tmpRenderer == renderer && dimension != null)
						{
							normalWidth = dimension.getWidth();
							normalHeight = dimension.getHeight();
						}
					}

					if (availableImageHeight > 0)
					{
						double ratio = normalWidth / normalHeight;

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

			XlsReportConfiguration configuration = getCurrentItemConfiguration();
			
			boolean isOnePagePerSheet = configuration.isOnePagePerSheet();
			
//				tempBodyWriter.write("<text:p>");
			documentBuilder.insertPageAnchor(tableBuilder);
			if (!configuration.isIgnoreAnchors() && image.getAnchorName() != null)
			{
				tableBuilder.exportAnchor(JRStringUtil.xmlEncode(image.getAnchorName()));
				String cellAddress = "$&apos;" + tableBuilder.getTableName() + "&apos;." + getCellAddress(rowIndex, colIndex);
				int lastCol = Math.max(0, colIndex + gridCell.getColSpan() - 1);
				String cellRangeAddress = getCellAddress(rowIndex + gridCell.getRowSpan() - 1, lastCol);
				namedExpressions.append("<table:named-range table:name=\""+ image.getAnchorName() +"\" table:base-cell-address=\"" + cellAddress +"\" table:cell-range-address=\"" + cellAddress +":" + cellRangeAddress +"\"/>\n");
			}

			boolean startedHyperlink = tableBuilder.startHyperlink(image,false, isOnePagePerSheet);

			//String cellAddress = getCellAddress(rowIndex + gridCell.getRowSpan(), colIndex + gridCell.getColSpan() - 1);
			String cellAddress = getCellAddress(rowIndex + gridCell.getRowSpan(), colIndex + gridCell.getColSpan());
			cellAddress = cellAddress == null ? "" : "table:end-cell-address=\"" + cellAddress + "\" ";
			
			tempBodyWriter.write("<draw:frame text:anchor-type=\"frame\" "
					+ "draw:style-name=\"" + styleCache.getGraphicStyle(image) + "\" "
					+ cellAddress
//						+ "table:end-x=\"" + LengthUtil.inchRound(image.getWidth()) + "in\" "
//						+ "table:end-y=\"" + LengthUtil.inchRound(image.getHeight()) + "in\" "
					+ "table:end-x=\"0in\" "
					+ "table:end-y=\"0in\" "
//						+ "svg:x=\"" + LengthUtil.inch(image.getX() + leftPadding + xoffset) + "in\" "
//						+ "svg:y=\"" + LengthUtil.inch(image.getY() + topPadding + yoffset) + "in\" "
					+ "svg:x=\"0in\" "
					+ "svg:y=\"0in\" "
					+ "svg:width=\"" + LengthUtil.inchRound(image.getWidth()) + "in\" "
					+ "svg:height=\"" + LengthUtil.inchRound(image.getHeight()) + "in\"" 
					+ ">"
					);
			tempBodyWriter.write("<draw:image ");
			String imagePath = documentBuilder.getImagePath(renderer, image, gridCell);
			tempBodyWriter.write(" xlink:href=\"" + JRStringUtil.xmlEncode(imagePath) + "\"");
			tempBodyWriter.write(" xlink:type=\"simple\"");
			tempBodyWriter.write(" xlink:show=\"embed\"");
			tempBodyWriter.write(" xlink:actuate=\"onLoad\"");
			tempBodyWriter.write("/>\n");

			tempBodyWriter.write("</draw:frame>");
			if(startedHyperlink)
			{
				tableBuilder.endHyperlink(false);
			}
//				tempBodyWriter.write("</text:p>");
		}

		tableBuilder.buildCellFooter();
	}
	
	protected String getCellAddress(int row, int col)
	{
		String address = null;
		if(row > -1 && row < 1048577 && col > -1 && col < maxColumnIndex)
		{
			address = "$" + getColumIndexName(col, maxColumnIndex) + "$" + (row + 1);
		}
		return address == null ? DEFAULT_ADDRESS : address;
	}
	
	@Override
	protected void exportRectangle(
		JRPrintGraphicElement rectangle,
		JRExporterGridCell gridCell, 
		int colIndex, 
		int rowIndex
		) throws JRException 
	{
		tableBuilder.exportRectangle(rectangle, gridCell);
	}

	@Override
	protected void exportLine(
		JRPrintLine line, 
		JRExporterGridCell gridCell,
		int colIndex, 
		int rowIndex
		) throws JRException 
	{
		JRLineBox box = new JRBaseLineBox(null);
		JRPen pen = null;
		float ratio = line.getWidth() / line.getHeight();
		if (ratio > 1)
		{
			if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
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
			if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
			{
				pen = box.getLeftPen();
			}
			else
			{
				pen = box.getRightPen();
			}
		}
		pen.setLineColor(line.getLinePen().getLineColor());
		pen.setLineStyle(line.getLinePen().getLineStyleValue());
		pen.setLineWidth(line.getLinePen().getLineWidth());

		gridCell.setBox(box);//CAUTION: only some exporters set the cell box

		tableBuilder.buildCellHeader(styleCache.getCellStyle(gridCell), gridCell.getColSpan(), gridCell.getRowSpan());

//		double x1, y1, x2, y2;
//
//		if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
//		{
//			x1 = Utility.translatePixelsToInches(0);
//			y1 = Utility.translatePixelsToInches(0);
//			x2 = Utility.translatePixelsToInches(line.getWidth() - 1);
//			y2 = Utility.translatePixelsToInches(line.getHeight() - 1);
//		}
//		else
//		{
//			x1 = Utility.translatePixelsToInches(0);
//			y1 = Utility.translatePixelsToInches(line.getHeight() - 1);
//			x2 = Utility.translatePixelsToInches(line.getWidth() - 1);
//			y2 = Utility.translatePixelsToInches(0);
//		}

		tempBodyWriter.write("<text:p>");
//FIXMEODS		insertPageAnchor();
//		tempBodyWriter.write(
//				"<draw:line text:anchor-type=\"paragraph\" "
//				+ "draw:style-name=\"" + styleCache.getGraphicStyle(line) + "\" "
//				+ "svg:x1=\"" + x1 + "in\" "
//				+ "svg:y1=\"" + y1 + "in\" "
//				+ "svg:x2=\"" + x2 + "in\" "
//				+ "svg:y2=\"" + y2 + "in\">"
//				//+ "</draw:line>"
//				+ "<text:p/></draw:line>"
//				);
		tempBodyWriter.write("</text:p>");
		tableBuilder.buildCellFooter();
	}

	@Override
	protected void exportFrame(
		JRPrintFrame frame, 
		JRExporterGridCell cell,
		int colIndex, 
		int rowIndex
		) throws JRException 
	{
		addBlankCell(cell, colIndex, rowIndex);
	}

	@Override
	protected void exportGenericElement(
		JRGenericPrintElement element,
		JRExporterGridCell gridCell, 
		int colIndex, 
		int rowIndex, 
		int emptyCols,
		int yCutsRow, 
		JRGridLayout layout
		) throws JRException 
	{
		GenericElementOdsHandler handler = (GenericElementOdsHandler) 
		GenericElementHandlerEnviroment.getInstance(getJasperReportsContext()).getElementHandler(
				element.getGenericType(), ODS_EXPORTER_KEY);

		if (handler != null)
		{
			JROdsExporterContext exporterContext = new ExporterContext(tableBuilder);

			handler.exportElement(exporterContext, element, gridCell, colIndex, rowIndex, emptyCols, yCutsRow, layout);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("No ODS generic element handler for " 
						+ element.getGenericType());
			}
		}
	}

	@Override
	protected void setFreezePane(int rowIndex, int colIndex) {
		// nothing to do here
	}
	
	/**
	 * @deprecated to be removed; replaced by {@link #setFreezePane(int, int)}
	 */ 
	protected void setFreezePane(int rowIndex, int colIndex, boolean isRowEdge, boolean isColumnEdge) {
		// nothing to do here
	}

	@Override
	protected void setSheetName(String sheetName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setAutoFilter(String autoFilterRange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setRowLevels(XlsRowLevelInfo levelInfo, String level) {
		// TODO Auto-generated method stub
		
	}

	
	private static final Log log = LogFactory.getLog(JROdsExporter.class);
	
	protected static final String ODS_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.ods.";
	
	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getElementHandler(JRGenericElementType, String)}.
	 */
	public static final String ODS_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "ods";
	
	protected class ExporterContext extends BaseExporterContext implements JROdsExporterContext
	{
		TableBuilder tableBuilder = null;
		
		public ExporterContext(TableBuilder tableBuidler)
		{
			this.tableBuilder = tableBuidler;
		}
		
		public TableBuilder getTableBuilder()
		{
			return tableBuilder;
		}
	}
	
	protected class OdsDocumentBuilder extends DocumentBuilder
	{
		public OdsDocumentBuilder(OasisZip oasisZip) 
		{
			super(oasisZip);
		}

		@Override
		public JRStyledText getStyledText(JRPrintText text) 
		{
			return JROdsExporter.this.getStyledText(text);
		}

		@Override
		public Locale getTextLocale(JRPrintText text) 
		{
			return JROdsExporter.this.getTextLocale(text);
		}

		@Override
		public String getInvalidCharReplacement() 
		{
			return JROdsExporter.this.invalidCharReplacement;
		}

		@Override
		protected void insertPageAnchor(TableBuilder tableBuilder) 
		{
			JROdsExporter.this.insertPageAnchor(tableBuilder);
		}

		@Override
		protected JRHyperlinkProducer getHyperlinkProducer(JRPrintHyperlink link) 
		{
			return JROdsExporter.this.getHyperlinkProducer(link);
		}

		@Override
		protected JasperReportsContext getJasperReportsContext() 
		{
			return JROdsExporter.this.getJasperReportsContext();
		}

		@Override
		protected int getReportIndex() 
		{
			return JROdsExporter.this.reportIndex;
		}

		@Override
		protected int getPageIndex() 
		{
			return JROdsExporter.this.pageIndex;
		}
	}

	
	protected class OdsTableBuilder extends TableBuilder
	{
		protected OdsTableBuilder(
				DocumentBuilder documentBuilder, 
				JasperPrint jasperPrint,
				int pageFormatIndex, 
				int pageIndex, 
				WriterHelper bodyWriter,
				WriterHelper styleWriter, 
				StyleCache styleCache, 
				Map<Integer, String> rowStyles, 
				Map<Integer, String> columnStyles,
				Color tabColor) 
		{
			super(
				documentBuilder, 
				jasperPrint, 
				pageFormatIndex, 
				pageIndex, 
				bodyWriter, 
				styleWriter, 
				styleCache, 
				rowStyles, 
				columnStyles, 
				tabColor);
		}
		
		protected OdsTableBuilder(
				DocumentBuilder documentBuilder, 
				JasperPrint jasperPrint,
				int pageFormatIndex, 
				int pageIndex, 
				WriterHelper bodyWriter,
				WriterHelper styleWriter, 
				StyleCache styleCache, 
				Map<Integer, String> rowStyles, 
				Map<Integer, String> columnStyles, 
				String sheetName,
				Color tabColor) 
		{
			super(
				documentBuilder, 
				jasperPrint, 
				pageFormatIndex, 
				pageIndex, 
				bodyWriter, 
				styleWriter, 
				styleCache, 
				rowStyles, 
				columnStyles,
				tabColor);
			this.tableName = sheetName;
		}

		@Override
		protected String getIgnoreHyperlinkProperty()
		{
			return XlsReportConfiguration.PROPERTY_IGNORE_HYPERLINK;
		}
		
		@Override
		protected void exportTextContents(JRPrintText textElement)
		{
			String href = null;
			
			String ignLnkPropName = getIgnoreHyperlinkProperty();
			Boolean ignoreHyperlink = HyperlinkUtil.getIgnoreHyperlink(ignLnkPropName, textElement);
			boolean isIgnoreTextFormatting = isIgnoreTextFormatting(textElement);
			if (ignoreHyperlink == null)
			{
				ignoreHyperlink = getPropertiesUtil().getBooleanProperty(jasperPrint, ignLnkPropName, false);
			}

			if (!ignoreHyperlink)
			{
				href = documentBuilder.getHyperlinkURL(textElement, getCurrentItemConfiguration().isOnePagePerSheet());
			}

			if (href == null)
			{
				exportStyledText(textElement, false, isIgnoreTextFormatting);
			}
			else
			{
				JRStyledText styledText = getStyledText(textElement);
				if (styledText != null && styledText.length() > 0)
				{
					String text = styledText.getText();
					Locale locale = getTextLocale(textElement);
					
					int runLimit = 0;
					AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();
					while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
					{
						// ODS does not like text:span inside text:a
						// writing one text:a inside text:span for each style run
						String runText = text.substring(iterator.getIndex(), runLimit);
						startTextSpan(
								iterator.getAttributes(), 
								runText, 
								locale,
								isIgnoreTextFormatting);
						writeHyperlink(textElement, href, true);
						writeText(runText);
						endHyperlink(true);
						endTextSpan();
						iterator.setIndex(runLimit);
					}
				}
			}
		}
	}
	
	
	/**
	 * @see #JROdsExporter(JasperReportsContext)
	 */
	public JROdsExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}


	/**
	 *
	 */
	public JROdsExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
		
		exporterContext = new ExporterContext(null);
	}


	/**
	 *
	 */
	protected Class<OdsExporterConfiguration> getConfigurationInterface()
	{
		return OdsExporterConfiguration.class;
	}


	/**
	 *
	 */
	protected Class<OdsReportConfiguration> getItemConfigurationInterface()
	{
		return OdsReportConfiguration.class;
	}
	

	@Override
	protected void initExport()
	{
		super.initExport();

//		macroTemplate =  macroTemplate == null ? getPropertiesUtil().getProperty(jasperPrint, PROPERTY_MACRO_TEMPLATE) : macroTemplate;
//		
//		password = 
//			getStringParameter(
//				JExcelApiExporterParameter.PASSWORD,
//				JExcelApiExporterParameter.PROPERTY_PASSWORD
//				);
	}
	

	@Override
	protected void initReport()
	{
		super.initReport();

		XlsReportConfiguration configuration = getCurrentItemConfiguration();
		
		//FIXMEODS setBackground()

		nature = 
			new JROdsExporterNature(
				jasperReportsContext, 
				filter, 
				configuration.isIgnoreGraphics(),
				configuration.isIgnorePageMargins()
				);
	}


//	/**
//	 *
//	 */
//	protected void exportEllipse(TableBuilder tableBuilder, JRPrintEllipse ellipse, JRExporterGridCell gridCell) throws IOException
//	{
//		JRLineBox box = new JRBaseLineBox(null);
//		JRPen pen = box.getPen();
//		pen.setLineColor(ellipse.getLinePen().getLineColor());
//		pen.setLineStyle(ellipse.getLinePen().getLineStyleValue());
//		pen.setLineWidth(ellipse.getLinePen().getLineWidth());
//
//		gridCell.setBox(box);//CAUTION: only some exporters set the cell box
//		
//		tableBuilder.buildCellHeader(styleCache.getCellStyle(gridCell), gridCell.getColSpan(), gridCell.getRowSpan());
//		tempBodyWriter.write("<text:p>");
//		insertPageAnchor();
////		tempBodyWriter.write(
////			"<draw:ellipse text:anchor-type=\"paragraph\" "
////			+ "draw:style-name=\"" + styleCache.getGraphicStyle(ellipse) + "\" "
////			+ "svg:width=\"" + Utility.translatePixelsToInches(ellipse.getWidth()) + "in\" "
////			+ "svg:height=\"" + Utility.translatePixelsToInches(ellipse.getHeight()) + "in\" "
////			+ "svg:x=\"0in\" "
////			+ "svg:y=\"0in\">"
////			+ "<text:p/></draw:ellipse>"
////			);
//		tempBodyWriter.write("</text:p>");
//		tableBuilder.buildCellFooter();
//	}


	/**
	 *
	 */
	public String getExporterKey()
	{
		return ODS_EXPORTER_KEY;
	}

	
	/**
	 *
	 */
	public String getExporterPropertiesPrefix()
	{
		return ODS_EXPORTER_PROPERTIES_PREFIX;
	}

	
	/**
	 * 
	 */
	protected void insertPageAnchor(TableBuilder tableBuilder)
	{
		if(startPage)
		{
			String pageName = DocumentBuilder.JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (sheetIndex - sheetsBeforeCurrentReport);
			String cellAddress = "$&apos;" + tableBuilder.getTableName() + "&apos;.$A$1";
			tableBuilder.exportAnchor(pageName);
			namedExpressions.append("<table:named-range table:name=\""+ pageName +"\" table:base-cell-address=\"" + cellAddress +"\" table:cell-range-address=\"" +cellAddress +"\"/>\n");
			startPage = false;
		}
	}
	
	@Override
	protected void exportEmptyReport() throws JRException, IOException 
	{
		// does nothing in ODS export
	}
	
}

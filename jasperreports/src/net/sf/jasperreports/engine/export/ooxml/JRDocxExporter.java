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
package net.sf.jasperreports.engine.export.ooxml;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.font.TextAttribute;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
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
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.PrintPageFormat;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.export.CutsInfo;
import net.sf.jasperreports.engine.export.ElementGridCell;
import net.sf.jasperreports.engine.export.ExporterNature;
import net.sf.jasperreports.engine.export.GenericElementHandlerEnviroment;
import net.sf.jasperreports.engine.export.Grid;
import net.sf.jasperreports.engine.export.GridRow;
import net.sf.jasperreports.engine.export.HyperlinkUtil;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRGridLayout;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.LengthUtil;
import net.sf.jasperreports.engine.export.OccupiedGridCell;
import net.sf.jasperreports.engine.export.zip.FileBufferedZipEntry;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRTextAttribute;
import net.sf.jasperreports.export.DocxExporterConfiguration;
import net.sf.jasperreports.export.DocxReportConfiguration;
import net.sf.jasperreports.export.ExportInterruptedException;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.ReportExportConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Exports a JasperReports document to DOCX format. It has binary output type and exports the document to a
 * grid-based layout, therefore having the known limitations of grid exporters.
 * <p/>
 * It can work in batch mode and supports all types of
 * exporter input and output, content filtering, and font mappings.
 * <p/>
 * Currently, there are the following special configurations that can be made to a DOCX
 * exporter instance (see {@link net.sf.jasperreports.export.DocxReportConfiguration}):
 * <ul>
 * <li>Forcing the use of nested tables to render the content of frame elements using either
 * the {@link net.sf.jasperreports.export.DocxReportConfiguration#isFramesAsNestedTables() isFramesAsNestedTables()} 
 * exporter configuration flag or its corresponding exporter hint called
 * {@link net.sf.jasperreports.export.DocxReportConfiguration#PROPERTY_FRAMES_AS_NESTED_TABLES net.sf.jasperreports.export.docx.frames.as.nested.tables}.</li>
 * <li>Allowing table rows to adjust their height if more text is typed into their cells using
 * the Word editor. This is controlled using either the
 * {@link net.sf.jasperreports.export.DocxReportConfiguration#isFlexibleRowHeight() isFlexibleRowHeight()} 
 * exporter configuration flag, or its corresponding exporter hint called
 * {@link net.sf.jasperreports.export.DocxReportConfiguration#PROPERTY_FLEXIBLE_ROW_HEIGHT net.sf.jasperreports.export.docx.flexible.row.height}.</li>
 * <li>Ignoring hyperlinks in generated documents if they are not intended for the DOCX output format. This can be 
 * customized using either the 
 * {@link net.sf.jasperreports.export.DocxReportConfiguration#isIgnoreHyperlink() isIgnoreHyperlink()} 
 * exporter configuration flag, or its corresponding exporter hint called
 * {@link net.sf.jasperreports.export.DocxReportConfiguration#PROPERTY_IGNORE_HYPERLINK net.sf.jasperreports.export.docx.ignore.hyperlink}</li>
 * </ul>
 * 
 * @see net.sf.jasperreports.export.DocxReportConfiguration
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class JRDocxExporter extends JRAbstractExporter<DocxReportConfiguration, DocxExporterConfiguration, OutputStreamExporterOutput, JRDocxExporterContext>
{
	private static final Log log = LogFactory.getLog(JRDocxExporter.class);
	
	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getElementHandler(JRGenericElementType, String)}.
	 */
	public static final String DOCX_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "docx";
	
	public static final String EXCEPTION_MESSAGE_KEY_COLUMN_COUNT_OUT_OF_RANGE = "export.docx.column.count.out.of.range";
	
	protected static final String DOCX_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.docx.";

	/**
	 * @deprecated Replaced by {@link DocxReportConfiguration#PROPERTY_IGNORE_HYPERLINK}.
	 */
	public static final String PROPERTY_IGNORE_HYPERLINK = DocxReportConfiguration.PROPERTY_IGNORE_HYPERLINK;

	/**
	 * This property is used to mark text elements as being hidden either for printing or on-screen display.
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_HIDDEN_TEXT = JRPropertiesUtil.PROPERTY_PREFIX + "export.docx.hidden.text";

	/**
	 *
	 */
	public static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";

	/**
	 *
	 */
	public static final String IMAGE_NAME_PREFIX = "img_";
	protected static final int IMAGE_NAME_PREFIX_LEGTH = IMAGE_NAME_PREFIX.length();
	public static final String IMAGE_LINK_PREFIX = "link_" + IMAGE_NAME_PREFIX;
	
	/**
	 *
	 */
	protected DocxZip docxZip;
	protected DocxDocumentHelper docHelper;
	protected Writer docWriter;

	protected Map<String, String> rendererToImagePathMap;
//	protected Map imageMaps;

	protected int reportIndex;
	protected int pageIndex;
	protected int startPageIndex;
	protected int endPageIndex;
	protected int tableIndex;
	protected boolean startPage;
	protected String invalidCharReplacement;
	protected PrintPageFormat pageFormat;
	protected JRGridLayout pageGridLayout;

	protected LinkedList<Color> backcolorStack = new LinkedList<Color>();
	protected Color backcolor;

	protected DocxRunHelper runHelper;

	protected ExporterNature nature;

	protected long bookmarkIndex;
	
	protected String pageAnchor;
	
	protected DocxRelsHelper relsHelper;
	
	boolean emptyPageState;
	

	protected class ExporterContext extends BaseExporterContext implements JRDocxExporterContext
	{
		DocxTableHelper tableHelper = null;
		
		public ExporterContext(DocxTableHelper tableHelper)
		{
			this.tableHelper = tableHelper;
		}
		
		public DocxTableHelper getTableHelper()
		{
			return tableHelper;
		}
	}
	
	
	/**
	 * @see #JRDocxExporter(JasperReportsContext)
	 */
	public JRDocxExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}


	/**
	 *
	 */
	public JRDocxExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
		
		exporterContext = new ExporterContext(null);
	}


	/**
	 *
	 */
	protected Class<DocxExporterConfiguration> getConfigurationInterface()
	{
		return DocxExporterConfiguration.class;
	}


	/**
	 *
	 */
	protected Class<DocxReportConfiguration> getItemConfigurationInterface()
	{
		return DocxReportConfiguration.class;
	}
	

	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	protected void ensureOutput()
	{
		if (exporterOutput == null)
		{
			exporterOutput = 
				new net.sf.jasperreports.export.parameters.ParametersOutputStreamExporterOutput(
					getJasperReportsContext(),
					getParameters(),
					getCurrentJasperPrint()
					);
		}
	}
	

	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		/*   */
		ensureJasperReportsContext();
		ensureInput();

		initExport();
		
		ensureOutput();
		
		OutputStream outputStream = getExporterOutput().getOutputStream();

		try
		{
			exportReportToStream(outputStream);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			getExporterOutput().close();
			resetExportContext();
		}
	}

	
	@Override
	protected void initExport()
	{
		super.initExport();

		rendererToImagePathMap = new HashMap<String,String>();
//		imageMaps = new HashMap();
//		hyperlinksMap = new HashMap();
	}


	@Override
	protected void initReport()
	{
		super.initReport();
		
		if(jasperPrint.hasProperties() && jasperPrint.getPropertiesMap().containsProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS))
		{
			// allows null values for the property
			invalidCharReplacement = jasperPrint.getProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS);
		}
		else
		{
			invalidCharReplacement = getPropertiesUtil().getProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS, jasperPrint);
		}

		DocxReportConfiguration configuration = getCurrentItemConfiguration();
		
		nature = 
			new JRDocxExporterNature(
				jasperReportsContext, 
				filter, 
				!configuration.isFramesAsNestedTables()
				);
	}

	
	/**
	 *
	 */
	protected void exportReportToStream(OutputStream os) throws JRException, IOException
	{
		docxZip = new DocxZip();

		docWriter = docxZip.getDocumentEntry().getWriter();
		
		docHelper = new DocxDocumentHelper(jasperReportsContext, docWriter);
		docHelper.exportHeader();
		
		relsHelper = new DocxRelsHelper(jasperReportsContext, docxZip.getRelsEntry().getWriter());
		relsHelper.exportHeader();
		
		List<ExporterInputItem> items = exporterInput.getItems();

		DocxStyleHelper styleHelper = 
			new DocxStyleHelper(
				jasperReportsContext,
				docxZip.getStylesEntry().getWriter(), 
				getExporterKey()
				);
		styleHelper.export(exporterInput);
		styleHelper.close();

		DocxSettingsHelper settingsHelper = 
			new DocxSettingsHelper(
				jasperReportsContext,
				docxZip.getSettingsEntry().getWriter()
				);
		settingsHelper.export(jasperPrint);
		settingsHelper.close();

		runHelper = new DocxRunHelper(jasperReportsContext, docWriter, getExporterKey());
		
		pageFormat = null;
		PrintPageFormat oldPageFormat = null;

		for(reportIndex = 0; reportIndex < items.size(); reportIndex++)
		{
			ExporterInputItem item = items.get(reportIndex);

			setCurrentExporterInputItem(item);

			bookmarkIndex = 0;
			emptyPageState = false;
			
			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				PageRange pageRange = getPageRange();
				startPageIndex = (pageRange == null || pageRange.getStartPageIndex() == null) ? 0 : pageRange.getStartPageIndex();
				endPageIndex = (pageRange == null || pageRange.getEndPageIndex() == null) ? (pages.size() - 1) : pageRange.getEndPageIndex();

				JRPrintPage page = null;
				for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
				{
					if (Thread.interrupted())
					{
						throw new ExportInterruptedException();
					}

					page = pages.get(pageIndex);

					pageFormat = jasperPrint.getPageFormat(pageIndex);
					
					if (oldPageFormat != null && oldPageFormat != pageFormat)
					{
						docHelper.exportSection(oldPageFormat, pageGridLayout, false);
					}
					
					exportPage(page);

					oldPageFormat = pageFormat;
				}
			}
		}
		
		if (oldPageFormat != null)
		{
			docHelper.exportSection(oldPageFormat, pageGridLayout, true);
		}

		docHelper.exportFooter();
		docHelper.close();

//		if ((hyperlinksMap != null && hyperlinksMap.size() > 0))
//		{
//			for(Iterator it = hyperlinksMap.keySet().iterator(); it.hasNext();)
//			{
//				String href = (String)it.next();
//				String id = (String)hyperlinksMap.get(href);
//
//				relsHelper.exportHyperlink(id, href);
//			}
//		}

		relsHelper.exportFooter();

		relsHelper.close();

		docxZip.zipEntries(os);

		docxZip.dispose();
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException
	{
		startPage = true;
		pageAnchor = JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1);
		
		ReportExportConfiguration configuration = getCurrentItemConfiguration();

		pageGridLayout =
			new JRGridLayout(
				nature,
				page.getElements(),
				pageFormat.getPageWidth(),
				pageFormat.getPageHeight(),
				configuration.getOffsetX() == null ? 0 : configuration.getOffsetX(), 
				configuration.getOffsetY() == null ? 0 : configuration.getOffsetY(),
				null //address
				);

		exportGrid(pageGridLayout, null);
		
		JRExportProgressMonitor progressMonitor = configuration.getProgressMonitor();
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	/**
	 *
	 */
	protected void exportGrid(JRGridLayout gridLayout, JRPrintElementIndex frameIndex) throws JRException
	{
		
		CutsInfo xCuts = gridLayout.getXCuts();
		Grid grid = gridLayout.getGrid();
		DocxTableHelper tableHelper = null;

		int rowCount = grid.getRowCount();
		if (rowCount > 0 && grid.getColumnCount() > 63)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_COLUMN_COUNT_OUT_OF_RANGE,  
					new Object[]{grid.getColumnCount()} 
					);
		}
		
		// an empty page is encountered; 
		// if it's the first one in a series of consecutive empty pages, emptyPageState == false, otherwise emptyPageState == true
		if(rowCount == 0 && (pageIndex < endPageIndex || !emptyPageState))
		{
			tableHelper = 
					new DocxTableHelper(
						jasperReportsContext,
						docWriter, 
						xCuts,
						false,
						pageFormat,
						frameIndex
						);
			int maxReportIndex = exporterInput.getItems().size() - 1;
			
			// while the first and last page in the JasperPrint list need single breaks, all the others require double-breaking 
			boolean twice = 
					(pageIndex > startPageIndex && pageIndex < endPageIndex && !emptyPageState)
					||(reportIndex < maxReportIndex && pageIndex == endPageIndex);
			tableHelper.getParagraphHelper().exportEmptyPage(pageAnchor, bookmarkIndex, twice);
			bookmarkIndex++;
			emptyPageState = true;
			return;
		}
		
		tableHelper = 
				new DocxTableHelper(
					jasperReportsContext,
					docWriter, 
					xCuts,
					frameIndex == null && (reportIndex != 0 || pageIndex != startPageIndex),
					pageFormat,
					frameIndex
					);

		tableHelper.exportHeader();
		
		boolean isFlexibleRowHeight = getCurrentItemConfiguration().isFlexibleRowHeight();

		for(int row = 0; row < rowCount; row++)
		{
			int emptyCellColSpan = 0;
			//int emptyCellWidth = 0;

			boolean allowRowResize = false;
			int maxBottomPadding = 0; //for some strange reason, the bottom margin affects the row height; subtracting it here
			GridRow gridRow = grid.getRow(row);
			int rowSize = gridRow.size();
			for(int col = 0; col < rowSize; col++)
			{
				JRExporterGridCell gridCell = gridRow.get(col);
				JRLineBox box = gridCell.getBox();
				if (
					box != null 
					&& box.getBottomPadding() != null 
					&& maxBottomPadding < box.getBottomPadding().intValue()
					)
				{
					maxBottomPadding = box.getBottomPadding().intValue();
				}
				
				allowRowResize = 
					isFlexibleRowHeight 
					&& (allowRowResize 
						|| (gridCell.getElement() instanceof JRPrintText 
							|| (gridCell.getType() == JRExporterGridCell.TYPE_OCCUPIED_CELL
								&& ((OccupiedGridCell)gridCell).getOccupier().getElement() instanceof JRPrintText)
							)
						);
			}

			int rowHeight = gridLayout.getRowHeight(row) - maxBottomPadding;
			if (row == 0 && frameIndex == null)
			{
				rowHeight -= Math.min(rowHeight, pageFormat.getTopMargin());
			}

			tableHelper.exportRowHeader(
				rowHeight,
				allowRowResize
				);

			for(int col = 0; col < rowSize; col++)
			{
				JRExporterGridCell gridCell = gridRow.get(col);
				if (gridCell.getType() == JRExporterGridCell.TYPE_OCCUPIED_CELL)
				{
					if (emptyCellColSpan > 0)
					{
						//tableHelper.exportEmptyCell(gridCell, emptyCellColSpan);
						emptyCellColSpan = 0;
						//emptyCellWidth = 0;
					}

					OccupiedGridCell occupiedGridCell = (OccupiedGridCell)gridCell;
					ElementGridCell elementGridCell = (ElementGridCell)occupiedGridCell.getOccupier();
					tableHelper.exportOccupiedCells(elementGridCell, startPage, bookmarkIndex, pageAnchor);
					if(startPage)
					{
						// increment the bookmarkIndex for the first cell in the sheet, due to page anchor creation
						bookmarkIndex++;
					}
					col += elementGridCell.getColSpan() - 1;
				}
				else if(gridCell.getType() == JRExporterGridCell.TYPE_ELEMENT_CELL)
				{
					if (emptyCellColSpan > 0)
					{
						//writeEmptyCell(tableHelper, gridCell, emptyCellColSpan, emptyCellWidth, rowHeight);
						emptyCellColSpan = 0;
						//emptyCellWidth = 0;
					}

					JRPrintElement element = gridCell.getElement();

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
					else if (element instanceof JRGenericPrintElement)
					{
						exportGenericElement(tableHelper, (JRGenericPrintElement)element, gridCell);
					}

					col += gridCell.getColSpan() - 1;
				}
				else
				{
					emptyCellColSpan++;
					//emptyCellWidth += gridCell.getWidth();
					tableHelper.exportEmptyCell(gridCell, 1, startPage, bookmarkIndex, pageAnchor);
					if(startPage)
					{
						// increment the bookmarkIndex for the first cell in the sheet, due to page anchor creation
						bookmarkIndex++;
					}
				}
				startPage = false;
			}

//			if (emptyCellColSpan > 0)
//			{
//				//writeEmptyCell(tableHelper, null, emptyCellColSpan, emptyCellWidth, rowHeight);
//			}

			tableHelper.exportRowFooter();
		}

		tableHelper.exportFooter();
		// if a non-empty page was exported, the series of previous empty pages is ended
		emptyPageState = false;
	}


	/**
	 *
	 */
	protected void exportLine(DocxTableHelper tableHelper, JRPrintLine line, JRExporterGridCell gridCell)
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
		
		tableHelper.getCellHelper().exportHeader(line, gridCell);
		tableHelper.getParagraphHelper().exportEmptyParagraph(startPage, bookmarkIndex, pageAnchor);
		if(startPage)
		{
			// increment the bookmarkIndex for the first cell in the sheet, due to page anchor creation
			bookmarkIndex++;
		}
		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected void exportRectangle(DocxTableHelper tableHelper, JRPrintRectangle rectangle, JRExporterGridCell gridCell)
	{
		JRLineBox box = new JRBaseLineBox(null);
		JRPen pen = box.getPen();
		pen.setLineColor(rectangle.getLinePen().getLineColor());
		pen.setLineStyle(rectangle.getLinePen().getLineStyleValue());
		pen.setLineWidth(rectangle.getLinePen().getLineWidth());

		gridCell.setBox(box);//CAUTION: only some exporters set the cell box
		
		tableHelper.getCellHelper().exportHeader(rectangle, gridCell);
		tableHelper.getParagraphHelper().exportEmptyParagraph(startPage, bookmarkIndex, pageAnchor);
		if(startPage)
		{
			// increment the bookmarkIndex for the first cell in the sheet, due to page anchor creation
			bookmarkIndex++;
		}
		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected void exportEllipse(DocxTableHelper tableHelper, JRPrintEllipse ellipse, JRExporterGridCell gridCell)
	{
		JRLineBox box = new JRBaseLineBox(null);
		JRPen pen = box.getPen();
		pen.setLineColor(ellipse.getLinePen().getLineColor());
		pen.setLineStyle(ellipse.getLinePen().getLineStyleValue());
		pen.setLineWidth(ellipse.getLinePen().getLineWidth());

		gridCell.setBox(box);//CAUTION: only some exporters set the cell box
		
		tableHelper.getCellHelper().exportHeader(ellipse, gridCell);
		tableHelper.getParagraphHelper().exportEmptyParagraph(startPage, bookmarkIndex, pageAnchor);
		if(startPage)
		{
			// increment the bookmarkIndex for the first cell in the sheet, due to page anchor creation
			bookmarkIndex++;
		}
		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	public void exportText(DocxTableHelper tableHelper, JRPrintText text, JRExporterGridCell gridCell)
	{
		tableHelper.getCellHelper().exportHeader(text, gridCell);

		JRStyledText styledText = getStyledText(text);

		int textLength = 0;

		if (styledText != null)
		{
			textLength = styledText.length();
		}

//		if (styleBuffer.length() > 0)
//		{
//			writer.write(" style=\"");
//			writer.write(styleBuffer.toString());
//			writer.write("\"");
//		}
//
//		writer.write(">");
		docHelper.write("     <w:p>\n");

		tableHelper.getParagraphHelper().exportProps(text);
		if(startPage)
		{
			insertBookmark(pageAnchor, docHelper);
		}
		if (text.getAnchorName() != null)
		{
			insertBookmark(text.getAnchorName(), docHelper);
		}

		boolean startedHyperlink = startHyperlink(text, true);
		boolean isNewLineAsParagraph = false;
		if(HorizontalTextAlignEnum.JUSTIFIED.equals(text.getHorizontalTextAlign()))
		{
			if(text.hasProperties() && text.getPropertiesMap().containsProperty(DocxReportConfiguration.PROPERTY_NEW_LINE_AS_PARAGRAPH))
			{
				isNewLineAsParagraph = getPropertiesUtil().getBooleanProperty(text, DocxReportConfiguration.PROPERTY_NEW_LINE_AS_PARAGRAPH, false);
			}
			else
			{
				isNewLineAsParagraph = getCurrentItemConfiguration().isNewLineAsParagraph();
			}
		}

		if (textLength > 0)
		{
			exportStyledText(
				JRStyleResolver.getBaseStyle(text), 
				styledText, 
				getTextLocale(text),
				getPropertiesUtil().getBooleanProperty(text, PROPERTY_HIDDEN_TEXT, false),
				startedHyperlink, 
				isNewLineAsParagraph
				);
		}

		if (startedHyperlink)
		{
			endHyperlink(true);
		}

		docHelper.write("     </w:p>\n");

		tableHelper.getCellHelper().exportFooter();
	}


	/**
	 *
	 */
	protected void exportStyledText(JRStyle style, JRStyledText styledText, Locale locale, boolean hiddenText, boolean startedHyperlink, boolean isNewLineJustified)
	{
		Color elementBackcolor = null;
		Map<AttributedCharacterIterator.Attribute, Object> globalAttributes = styledText.getGlobalAttributes();
		if (globalAttributes != null)
		{
			elementBackcolor = (Color)styledText.getGlobalAttributes().get(TextAttribute.BACKGROUND);
		}
		
		String text = styledText.getText();

		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			Map<Attribute,Object> attributes = iterator.getAttributes();
			
			boolean localHyperlink = false;

			if (!startedHyperlink)
			{
				JRPrintHyperlink hyperlink = (JRPrintHyperlink)attributes.get(JRTextAttribute.HYPERLINK);
				if (hyperlink != null)
				{
					localHyperlink = startHyperlink(hyperlink, true);
				}
			}
			
			runHelper.export(
				style, 
				iterator.getAttributes(), 
				text.substring(iterator.getIndex(), runLimit),
				locale,
				hiddenText,
				invalidCharReplacement,
				elementBackcolor,
				isNewLineJustified
				);
			
			if (localHyperlink)
			{
				endHyperlink(true);
			}

			iterator.setIndex(runLimit);
		}
	}


	/**
	 *
	 */
	public void exportImage(DocxTableHelper tableHelper, JRPrintImage image, JRExporterGridCell gridCell) throws JRException
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

		docHelper.write("<w:p>");//FIXMEDOCX why is this here and not further down?

		Renderable renderer = image.getRenderable();

		if (
			renderer != null &&
			availableImageWidth > 0 &&
			availableImageHeight > 0
			)
		{
			if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE)
			{
				// Non-lazy image renderers are all asked for their image data at some point.
				// Better to test and replace the renderer now, in case of lazy load error.
				renderer = RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForImageData(renderer, image.getOnErrorTypeValue());
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
			Renderable tmpRenderer =
				RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForDimension(renderer, image.getOnErrorTypeValue());
			Dimension2D dimension = tmpRenderer == null ? null : tmpRenderer.getDimension(jasperReportsContext);
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
			
			switch (image.getScaleImageValue())
			{
				case FILL_FRAME :
				{
					width = availableImageWidth;
					height = availableImageHeight;
					break;
				}
				case CLIP :
				{
					if (normalWidth > availableImageWidth)
					{
						switch (image.getHorizontalImageAlign())
						{
							case RIGHT :
							{
								cropLeft = 65536 * (normalWidth - availableImageWidth) / normalWidth;
								cropRight = 0;
								break;
							}
							case CENTER :
							{
								cropLeft = 65536 * (- availableImageWidth + normalWidth) / normalWidth / 2;
								cropRight = cropLeft;
								break;
							}
							case LEFT :
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
						switch (image.getVerticalImageAlign())
						{
							case TOP :
							{
								cropTop = 0;
								cropBottom = 65536 * (normalHeight - availableImageHeight) / normalHeight;
								break;
							}
							case MIDDLE :
							{
								cropTop = 65536 * (normalHeight - availableImageHeight) / normalHeight / 2;
								cropBottom = cropTop;
								break;
							}
							case BOTTOM :
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
				case RETAIN_SHAPE :
				default :
				{
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
				}
			}

			if(startPage)
			{
				insertBookmark(pageAnchor, docHelper);
			}
			if (image.getAnchorName() != null)
			{
				insertBookmark(image.getAnchorName(), docHelper);
			}


//			boolean startedHyperlink = startHyperlink(image,false);

			docHelper.write("<w:r>\n"); 
			docHelper.write("<w:drawing>\n");
			docHelper.write("<wp:anchor distT=\"0\" distB=\"0\" distL=\"0\" distR=\"0\" simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"0\" locked=\"1\" layoutInCell=\"1\" allowOverlap=\"1\">");
			docHelper.write("<wp:simplePos x=\"0\" y=\"0\"/>");
			docHelper.write("<wp:positionH relativeFrom=\"column\"><wp:align>" + DocxParagraphHelper.getHorizontalImageAlign(image.getHorizontalImageAlign()) + "</wp:align></wp:positionH>");
			docHelper.write("<wp:positionV relativeFrom=\"line\"><wp:posOffset>0</wp:posOffset></wp:positionV>");
//			docHelper.write("<wp:positionV relativeFrom=\"line\"><wp:align>" + CellHelper.getVerticalAlignment(new Byte(image.getVerticalAlignment())) + "</wp:align></wp:positionV>");
			docHelper.write("<wp:extent cx=\"" + LengthUtil.emu(width) + "\" cy=\"" + LengthUtil.emu(height) + "\"/>\n");
			docHelper.write("<wp:wrapNone/>");
			int imageId = image.hashCode() > 0 ? image.hashCode() : -image.hashCode();
			String rId = IMAGE_LINK_PREFIX + getElementIndex(gridCell);
			docHelper.write("<wp:docPr id=\"" + imageId + "\" name=\"Picture\">\n");
			if(getHyperlinkURL(image) != null)
			{
				docHelper.write("<a:hlinkClick xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" r:id=\"" + rId + "\"/>\n");
			}
			docHelper.write("</wp:docPr>\n");
			docHelper.write("<a:graphic>\n");
			docHelper.write("<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">\n");
			docHelper.write("<pic:pic>\n");
			docHelper.write("<pic:nvPicPr><pic:cNvPr id=\"" + imageId + "\" name=\"Picture\"/><pic:cNvPicPr/></pic:nvPicPr>\n");
			docHelper.write("<pic:blipFill>\n");

			String imagePath = null;

			if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE && rendererToImagePathMap.containsKey(renderer.getId()))
			{
				imagePath = rendererToImagePathMap.get(renderer.getId());
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

					if (renderer.getTypeValue() == RenderableTypeEnum.SVG)
					{
						renderer =
							new JRWrappingSvgRenderer(
								renderer,
								new Dimension(image.getWidth(), image.getHeight()),
								ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
								);
					}

					String mimeType = renderer.getImageTypeValue().getMimeType();
					if (mimeType == null)
					{
						mimeType = ImageTypeEnum.JPEG.getMimeType();
					}
					String extension = mimeType.substring(mimeType.lastIndexOf('/') + 1);
					String imageName = IMAGE_NAME_PREFIX + imageIndex.toString() + "." + extension;
					
					docxZip.addEntry(//FIXMEDOCX optimize with a different implementation of entry
						new FileBufferedZipEntry(
							"word/media/" + imageName,
							renderer.getImageData(jasperReportsContext)
							)
						);
					
					relsHelper.exportImage(imageName);

					imagePath = imageName;
					//imagePath = "Pictures/" + imageName;
//				}

				rendererToImagePathMap.put(renderer.getId(), imagePath);
			}

			docHelper.write("<a:blip r:embed=\"" + imagePath + "\"/>");
			docHelper.write("<a:srcRect");
			if (cropLeft > 0)
			{
				docHelper.write(" l=\"" + (int)cropLeft + "\"");
			}
			if (cropTop > 0)
			{
				docHelper.write(" t=\"" + (int)cropTop + "\"");
			}
			if (cropRight > 0)
			{
				docHelper.write(" r=\"" + (int)cropRight + "\"");
			}
			if (cropBottom > 0)
			{
				docHelper.write(" b=\"" + (int)cropBottom + "\"");
			}
			docHelper.write("/>");
			docHelper.write("<a:stretch><a:fillRect/></a:stretch>\n");
			docHelper.write("</pic:blipFill>\n");
			docHelper.write("<pic:spPr><a:xfrm><a:off x=\"0\" y=\"0\"/><a:ext cx=\"" + LengthUtil.emu(width) + "\" cy=\"" + LengthUtil.emu(height) + "\"/>");
			docHelper.write("</a:xfrm><a:prstGeom prst=\"rect\"></a:prstGeom></pic:spPr>\n");
			docHelper.write("</pic:pic>\n");
			docHelper.write("</a:graphicData>\n");
			docHelper.write("</a:graphic>\n");
			docHelper.write("</wp:anchor>\n");
			docHelper.write("</w:drawing>\n");
			docHelper.write("</w:r>"); 

			String url =  getHyperlinkURL(image);

			if(url != null)
			{
				String targetMode = "";
				switch(image.getHyperlinkTypeValue())
				{
					case LOCAL_PAGE:
					case LOCAL_ANCHOR:
					{
						relsHelper.exportImageLink(rId, "#"+url.replaceAll("\\W", ""), targetMode);
						break;
					}
					
					case REMOTE_PAGE:
					case REMOTE_ANCHOR:
					case REFERENCE:
					{
						targetMode = " TargetMode=\"External\"";
						relsHelper.exportImageLink(rId, url, targetMode);
						break;
					}
					default:
					{
						break;
					}
				}
			}
			
//			if(startedHyperlink)
//			{
//				endHyperlink(false);
//			}
		}

		docHelper.write("</w:p>");

		tableHelper.getCellHelper().exportFooter();
	}


	protected JRPrintElementIndex getElementIndex(JRExporterGridCell gridCell)
	{
		JRPrintElementIndex imageIndex =
			new JRPrintElementIndex(
					reportIndex,
					pageIndex,
					gridCell.getElementAddress()
					);
		return imageIndex;
	}


	/**
	 *
	 *
	protected void writeImageMap(String imageMapName, JRPrintHyperlink mainHyperlink, List imageMapAreas)
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

		if (mainHyperlink.getHyperlinkTypeValue() != NONE)
		{
			writer.write("  <area shape=\"default\"");
			writeImageAreaHyperlink(mainHyperlink);
			writer.write("/>\n");
		}

		writer.write("</map>\n");
	}


	protected void writeImageAreaCoordinates(JRPrintImageArea area)
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


	protected void writeImageAreaHyperlink(JRPrintHyperlink hyperlink)
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
	public static JRPrintElementIndex getPrintElementIndex(String imageName)
	{
		if (!imageName.startsWith(IMAGE_NAME_PREFIX))
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_INVALID_IMAGE_NAME,
					new Object[]{imageName});
		}

		return JRPrintElementIndex.parsePrintElementIndex(imageName.substring(IMAGE_NAME_PREFIX_LEGTH));
	}


	/**
	 * In deep grids, this is called only for empty frames.
	 */
	protected void exportFrame(DocxTableHelper tableHelper, JRPrintFrame frame, JRExporterGridCell gridCell) throws JRException
	{
		tableHelper.getCellHelper().exportHeader(frame, gridCell);
//		tableHelper.getCellHelper().exportProps(gridCell);

		boolean appendBackcolor =
			frame.getModeValue() == ModeEnum.OPAQUE
			&& (backcolor == null || frame.getBackcolor().getRGB() != backcolor.getRGB());

		if (appendBackcolor)
		{
			setBackcolor(frame.getBackcolor());
		}

		try
		{
			JRGridLayout layout = ((ElementGridCell) gridCell).getLayout();
			JRPrintElementIndex frameIndex =
				new JRPrintElementIndex(
						reportIndex,
						pageIndex,
						gridCell.getElementAddress()
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
	protected void exportGenericElement(DocxTableHelper tableHelper, JRGenericPrintElement element, JRExporterGridCell gridCell)
	{
		GenericElementDocxHandler handler = (GenericElementDocxHandler) 
		GenericElementHandlerEnviroment.getInstance(getJasperReportsContext()).getElementHandler(
				element.getGenericType(), DOCX_EXPORTER_KEY);

		if (handler != null)
		{
			JRDocxExporterContext exporterContext = new ExporterContext(tableHelper);

			handler.exportElement(exporterContext, element, gridCell);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("No DOCX generic element handler for " 
						+ element.getGenericType());
			}
		}
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
		backcolor = backcolorStack.removeLast();
	}


//	private float getXAlignFactor(JRPrintImage image)
//	{
//		float xalignFactor = 0f;
//		switch (image.getHorizontalAlignmentValue())
//		{
//			case RIGHT :
//			{
//				xalignFactor = 1f;
//				break;
//			}
//			case CENTER :
//			{
//				xalignFactor = 0.5f;
//				break;
//			}
//			case LEFT :
//			default :
//			{
//				xalignFactor = 0f;
//				break;
//			}
//		}
//		return xalignFactor;
//	}


//	private float getYAlignFactor(JRPrintImage image)
//	{
//		float yalignFactor = 0f;
//		switch (image.getVerticalAlignmentValue())
//		{
//			case BOTTOM :
//			{
//				yalignFactor = 1f;
//				break;
//			}
//			case MIDDLE :
//			{
//				yalignFactor = 0.5f;
//				break;
//			}
//			case TOP :
//			default :
//			{
//				yalignFactor = 0f;
//				break;
//			}
//		}
//		return yalignFactor;
//	}

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
//			docHelper.write("<w:hyperlink r:id=\"" + id + "\"");
//
//			String target = getHyperlinkTarget(link);//FIXMETARGET
//			if (target != null)
//			{
//				docHelper.write(" tgtFrame=\"" + target + "\"");
//			}
//
//			docHelper.write(">\n");

			docHelper.write("<w:r><w:fldChar w:fldCharType=\"begin\"/></w:r>\n");
			String localType = (HyperlinkTypeEnum.LOCAL_ANCHOR == link.getHyperlinkTypeValue() || 
					HyperlinkTypeEnum.LOCAL_PAGE == link.getHyperlinkTypeValue()) ? "\\l " : "";
					
			docHelper.write("<w:r><w:instrText xml:space=\"preserve\"> HYPERLINK " + localType +"\"" + JRStringUtil.xmlEncode(href,invalidCharReplacement) + "\"");

			String target = getHyperlinkTarget(link);//FIXMETARGET
			if (target != null)
			{
				docHelper.write(" \\t \"" + target + "\"");
			}

			String tooltip = link.getHyperlinkTooltip(); 
			if (tooltip != null)
			{
				docHelper.write(" \\o \"" + JRStringUtil.xmlEncode(tooltip, invalidCharReplacement) + "\"");
			}

			docHelper.write(" </w:instrText></w:r>\n");
			docHelper.write("<w:r><w:fldChar w:fldCharType=\"separate\"/></w:r>\n");
		}

		return href != null;
	}


	protected String getHyperlinkTarget(JRPrintHyperlink link)
	{
		String target = null;
		switch(link.getHyperlinkTargetValue())
		{
			case SELF :
			{
				target = "_self";
				break;
			}
			case BLANK :
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

		Boolean ignoreHyperlink = HyperlinkUtil.getIgnoreHyperlink(DocxReportConfiguration.PROPERTY_IGNORE_HYPERLINK, link);
		if (ignoreHyperlink == null)
		{
			ignoreHyperlink = getCurrentItemConfiguration().isIgnoreHyperlink();
		}

		if (!ignoreHyperlink)
		{
			JRHyperlinkProducer customHandler = getHyperlinkProducer(link);
			if (customHandler == null)
			{
				switch(link.getHyperlinkTypeValue())
				{
					case REFERENCE :
					{
						if (link.getHyperlinkReference() != null)
						{
							href = link.getHyperlinkReference();
						}
						break;
					}
					case LOCAL_ANCHOR :
					{
						if (link.getHyperlinkAnchor() != null)
						{
							href = link.getHyperlinkAnchor().replaceAll("\\W", "");
						}
						break;
					}
					case LOCAL_PAGE :
					{
						if (link.getHyperlinkPage() != null)
						{
							href = JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString();
						}
						break;
					}
					case REMOTE_ANCHOR :
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
					case REMOTE_PAGE :
					{
						if (
							link.getHyperlinkReference() != null &&
							link.getHyperlinkPage() != null
							)
						{
							href = link.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString();
						}
						break;
					}
					case NONE :
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
		}

		return href;
	}


	protected void endHyperlink(boolean isText)
	{
//		docHelper.write("</w:hyperlink>\n");
		docHelper.write("<w:r><w:fldChar w:fldCharType=\"end\"/></w:r>\n");
	}

	protected void insertBookmark(String bookmark, BaseHelper helper)
	{
		helper.write("<w:bookmarkStart w:id=\"" + bookmarkIndex);
		helper.write("\" w:name=\"" + (bookmark == null ? null : bookmark.replaceAll("\\W", "")));
		helper.write("\"/><w:bookmarkEnd w:id=\"" + bookmarkIndex++);
		helper.write("\"/>");
	}
	
	/**
	 *
	 */
	protected void ensureInput()
	{
		super.ensureInput();
	}

	/**
	 *
	 */
	public String getExporterKey()
	{
		return DOCX_EXPORTER_KEY;
	}

	/**
	 * 
	 */
	public String getExporterPropertiesPrefix()
	{
		return DOCX_EXPORTER_PROPERTIES_PREFIX;
	}
	
}


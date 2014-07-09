/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElement;
import net.sf.jasperreports.crosstabs.interactive.CrosstabInteractiveJsonHandler;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.ImageMapRenderable;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintElementIndex;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintImageArea;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.PrintElementId;
import net.sf.jasperreports.engine.PrintElementVisitor;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.export.tabulator.Cell;
import net.sf.jasperreports.engine.export.tabulator.CellVisitor;
import net.sf.jasperreports.engine.export.tabulator.Column;
import net.sf.jasperreports.engine.export.tabulator.ElementCell;
import net.sf.jasperreports.engine.export.tabulator.FrameCell;
import net.sf.jasperreports.engine.export.tabulator.LayeredCell;
import net.sf.jasperreports.engine.export.tabulator.Row;
import net.sf.jasperreports.engine.export.tabulator.SplitCell;
import net.sf.jasperreports.engine.export.tabulator.Table;
import net.sf.jasperreports.engine.export.tabulator.TableCell;
import net.sf.jasperreports.engine.export.tabulator.TablePosition;
import net.sf.jasperreports.engine.export.tabulator.Tabulator;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.HyperlinkData;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRTextAttribute;
import net.sf.jasperreports.engine.util.Pair;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.HtmlExporterConfiguration;
import net.sf.jasperreports.export.HtmlExporterOutput;
import net.sf.jasperreports.export.HtmlReportConfiguration;
import net.sf.jasperreports.search.HitTermInfo;
import net.sf.jasperreports.search.SpansInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class HtmlExporterCompactCss extends AbstractHtmlExporter<HtmlReportConfiguration, HtmlExporterConfiguration>
{
	private static final Log log = LogFactory.getLog(HtmlExporterCompactCss.class);

	/**
	 * The exporter key, as used in
	 * {@link net.sf.jasperreports.engine.export.GenericElementHandlerEnviroment#getElementHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String HTML_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "html";

	/**
	 *
	 */
	public static final String HTML_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.html.";

	/**
	 * @deprecated Replaced by {@link net.sf.jasperreports.export.HtmlReportConfiguration#PROPERTY_IGNORE_HYPERLINK}.
	 */
	public static final String PROPERTY_IGNORE_HYPERLINK = HtmlReportConfiguration.PROPERTY_IGNORE_HYPERLINK;

	/**
	 * Property that provides the value for the <code>class</code> CSS style property to be applied
	 * to elements in the table generated for the report. The value of this property
	 * will be used as the value for the <code>class</code> attribute of the <code>&lt;td&gt;</code> tag for the element when exported to HTML and/or
	 * the <code>class</code> attribute of the <code>&lt;span&gt;</code> or <code>&lt;div&gt;</code> tag for the element, when exported to XHTML/CSS.
	 */
	public static final String PROPERTY_HTML_CLASS = HTML_EXPORTER_PROPERTIES_PREFIX + "class";

	/**
	 *
	 */
	public static final String PROPERTY_HTML_ID = HTML_EXPORTER_PROPERTIES_PREFIX + "id";

	/**
	 * @deprecated Replaced by {@link net.sf.jasperreports.export.HtmlReportConfiguration#PROPERTY_BORDER_COLLAPSE}.
	 */
	public static final String PROPERTY_BORDER_COLLAPSE = HtmlReportConfiguration.PROPERTY_BORDER_COLLAPSE;

	public static final String CSS_DEFAULT_STYLE_NAME = "jrDflt";

	public static final String CSS_TEXT_SUFFIX = "_txt";
	public static final String CSS_GRAPHIC_SUFFIX = "_grphc";
	public static final String CSS_IMAGE_SUFFIX = "_img";

	protected JRHyperlinkTargetProducerFactory targetProducerFactory;

	protected Map<String,String> rendererToImagePathMap;
	protected Map<Pair<String, Rectangle>,String> imageMaps;

	protected Map<String, HtmlFont> fontsToProcess;

	protected Writer writer;
	protected int reportIndex;
	protected int pageIndex;

	protected LinkedList<Color> backcolorStack = new LinkedList<Color>();

	protected ExporterFilter tableFilter;

	protected int pointerEventsNoneStack = 0;

	private List<HyperlinkData> hyperlinksData = new ArrayList<HyperlinkData>();

	private JRStyle jrDefaultStyle;

	public HtmlExporterCompactCss()
	{
		this(DefaultJasperReportsContext.getInstance());
	}

	public HtmlExporterCompactCss(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);

		exporterContext = new ExporterContext();
	}
	
	@Override
	public String getExporterKey()
	{
		return HTML_EXPORTER_KEY;
	}

	@Override
	public String getExporterPropertiesPrefix()
	{
		return HTML_EXPORTER_PROPERTIES_PREFIX;
	}

	@Override
	public void exportReport() throws JRException
	{
		/*   */
		ensureJasperReportsContext();
		ensureInput();

		rendererToImagePathMap = new HashMap<String,String>();
		imageMaps = new HashMap<Pair<String, Rectangle>,String>();

		fontsToProcess = new HashMap<String, HtmlFont>();
		
		//FIXMENOW check all exporter properties that are supposed to work at report level
		
		initExport();
		
		ensureOutput();

		writer = getExporterOutput().getWriter();

		try
		{
			exportReportToWriter();
		}
		catch (IOException e)
		{
			throw new JRException("Error writing to output writer : " + jasperPrint.getName(), e);
		}
		finally
		{
			getExporterOutput().close();
			resetExportContext();
		}
	}

	
	/**
	 *
	 */
	protected Class<HtmlExporterConfiguration> getConfigurationInterface()
	{
		return HtmlExporterConfiguration.class;
	}

	
	/**
	 *
	 */
	protected Class<HtmlReportConfiguration> getItemConfigurationInterface()
	{
		return HtmlReportConfiguration.class;
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
				new net.sf.jasperreports.export.parameters.ParametersHtmlExporterOutput(
					getJasperReportsContext(),
					getParameters(),
					getCurrentJasperPrint()
					);
		}
	}


	@Override
	protected void initExport()
	{
		super.initExport();
	}


	@Override
	protected void initReport()
	{
		super.initReport();

		HtmlReportConfiguration configuration = getCurrentItemConfiguration();
		
		if (configuration.isRemoveEmptySpaceBetweenRows())
		{
			log.info("Removing empty space between rows not supported");
		}

		// this is the filter used to create the table, taking in consideration unhandled generic elements
		tableFilter = new GenericElementsFilterDecorator(jasperReportsContext, HTML_EXPORTER_KEY, filter);
	}
	

	@Override
	protected void setJasperReportsContext(JasperReportsContext jasperReportsContext)
	{
		super.setJasperReportsContext(jasperReportsContext);
		
		targetProducerFactory = new DefaultHyperlinkTargetProducerFactory(jasperReportsContext);
	}

	
	protected void exportReportToWriter() throws JRException, IOException
	{
		HtmlExporterConfiguration configuration = getCurrentConfiguration(); 
		String htmlHeader = configuration.getHtmlHeader();
		String betweenPagesHtml = configuration.getBetweenPagesHtml();
		String htmlFooter = configuration.getHtmlFooter();
		boolean flushOutput = configuration.isFlushOutput();//FIXMEEXPORT maybe move flush flag to output

		List<ExporterInputItem> items = exporterInput.getItems();
		StringBuilder reportStyles = new StringBuilder();

		// get CSS
		for(reportIndex = 0; reportIndex < items.size(); reportIndex++)
		{
			ExporterInputItem item = items.get(reportIndex);
			setCurrentExporterInputItem(item);
			addReportStyles(reportStyles);
		}

		if (htmlHeader == null)
		{
			String encoding = getExporterOutput().getEncoding();

			writer.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
			writer.write("<html>\n");
			writer.write("<head>\n");
			writer.write("  <title></title>\n");
			writer.write("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + encoding + "\"/>\n");
			writer.write("  <style type=\"text/css\">\n");
			writer.write("    a {text-decoration: none}\n");

			if (reportStyles.length() > 0) {
				writer.write(reportStyles.toString());
			}

			writer.write("  </style>\n");
			writer.write("</head>\n");
			writer.write("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\">\n");
			writer.write("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n");
			writer.write("<tr><td width=\"50%\">&nbsp;</td><td align=\"center\">\n");
			writer.write("\n");
		}
		else
		{
			// FIXME: define styles placeholder and replace it with reportStyles
			writer.write(htmlHeader);
		}

		for(reportIndex = 0; reportIndex < items.size(); reportIndex++)
		{
			ExporterInputItem item = items.get(reportIndex);

			setCurrentExporterInputItem(item);

			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				PageRange pageRange = getPageRange();
				int startPageIndex = (pageRange == null || pageRange.getStartPageIndex() == null) ? 0 : pageRange.getStartPageIndex();
				int endPageIndex = (pageRange == null || pageRange.getEndPageIndex() == null) ? (pages.size() - 1) : pageRange.getEndPageIndex();

				JRPrintPage page = null;
				for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
				{
					if (Thread.interrupted())
					{
						throw new JRException("Current thread interrupted.");
					}

					page = pages.get(pageIndex);

					writer.write("<a name=\"" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1) + "\"></a>\n");

					/*   */
					exportPage(page);

					if (reportIndex < items.size() - 1 || pageIndex < endPageIndex)
					{
						if (betweenPagesHtml == null)
						{
							writer.write("<br/>\n<br/>\n");
						}
						else
						{
							writer.write(betweenPagesHtml);
						}
					}

					writer.write("\n");
				}
			}
		}

		ReportContext reportContext = getReportContext();
		if (fontsToProcess != null && fontsToProcess.size() > 0)// when no fontHandler and/or resourceHandler, fonts are not processed 
		{
			@SuppressWarnings("deprecation")
			HtmlResourceHandler fontHandler = 
				getExporterOutput().getFontHandler() == null
				? getFontHandler()
				: getExporterOutput().getFontHandler();
			ObjectMapper mapper = null;
			ArrayNode webFonts = null;

			if (reportContext != null) {
				mapper = new ObjectMapper();
				webFonts = mapper.createArrayNode();
				reportContext.setParameterValue("net.sf.jasperreports.html.webfonts", webFonts);	// FIXME: use constant
			}

			for (HtmlFont htmlFont : fontsToProcess.values())
			{
				if (reportContext != null) {
					ObjectNode objNode = mapper.createObjectNode();
					objNode.put("id", htmlFont.getId());
					objNode.put("path", fontHandler.getResourcePath(htmlFont.getId()));
					webFonts.add(objNode);
				} else {
					writer.write("<link class=\"jrWebFont\" rel=\"stylesheet\" href=\"" + fontHandler.getResourcePath(htmlFont.getId()) + "\">\n");
				}
			}
		}

		// place hyperlinksData on reportContext
		if (hyperlinksData.size() > 0) {
			reportContext.setParameterValue("net.sf.jasperreports.html.hyperlinks", hyperlinksData);
		}
		
//		if (!isOutputResourcesToDir)
		if (reportContext == null) // generate script tag on static export only
		{
			writer.write("<![if IE]>\n");
			writer.write("<script>\n");
			writer.write("var links = document.querySelectorAll('link.jrWebFont');\n");
			writer.write("setTimeout(function(){ if (links) { for (var i = 0; i < links.length; i++) { links.item(i).href = links.item(i).href; } } }, 0);\n");
			writer.write("</script>\n");
			writer.write("<![endif]>\n");
		}

		if (htmlFooter == null)
		{
			writer.write("</td><td width=\"50%\">&nbsp;</td></tr>\n");
			writer.write("</table>\n");
			writer.write("</body>\n");
			writer.write("</html>\n");
		}
		else
		{
			writer.write(htmlFooter);
		}

		if (flushOutput)
		{
			writer.flush();
		}
	}
	
	protected void exportPage(JRPrintPage page) throws IOException
	{
		Tabulator tabulator = new Tabulator(tableFilter, page.getElements());
		tabulator.tabulate();

		HtmlReportConfiguration configuration = getCurrentItemConfiguration(); 
		
		boolean isIgnorePageMargins = configuration.isIgnorePageMargins();
		if (!isIgnorePageMargins)
		{
			tabulator.addMargins(jasperPrint.getPageWidth(), jasperPrint.getPageHeight());
		}
		
		Table table = tabulator.getTable();
		
		boolean isWhitePageBackground = configuration.isWhitePageBackground();
		if (isWhitePageBackground)
		{
			setBackcolor(Color.white);
		}
		
		CellElementVisitor elementVisitor = new CellElementVisitor();
		TableVisitor tableVisitor = new TableVisitor(tabulator, elementVisitor);
		
		exportTable(tableVisitor, table, isWhitePageBackground, true);
		
		if (isWhitePageBackground)
		{
			restoreBackcolor();
		}
		
		JRExportProgressMonitor progressMonitor = configuration.getProgressMonitor();
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}

	public void exportElements(List<JRPrintElement> elements) throws IOException
	{
		Tabulator tabulator = new Tabulator(tableFilter, elements);
		tabulator.tabulate();
		
		Table table = tabulator.getTable();
		
		CellElementVisitor elementVisitor = new CellElementVisitor();
		TableVisitor tableVisitor = new TableVisitor(tabulator, elementVisitor);
		
		exportTable(tableVisitor, table, false, false);
	}

	protected void exportTable(TableVisitor tableVisitor, Table table, boolean whiteBackground, boolean isMainReportTable) throws IOException
	{
		SortedSet<Column> columns = table.getColumns().getUserEntries();
		SortedSet<Row> rows = table.getRows().getUserEntries();
		if (columns.isEmpty() || rows.isEmpty())
		{
			// TODO lucianc empty page
			return;
		}
		

		if (isMainReportTable)
		{
			int totalWidth = columns.last().getEndCoord() - columns.first().getStartCoord();
			writer.write("<table class=\"jrPage\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"empty-cells: show; width: ");
			writer.write(toSizeUnit(totalWidth));
			writer.write(";");
		}
		else
		{
			writer.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"empty-cells: show; width: 100%;");
		}
		
		String borderCollapse = getCurrentItemConfiguration().getBorderCollapse();
		if (borderCollapse != null)
		{
			writer.write(" border-collapse: ");
			writer.write(borderCollapse);
			writer.write(";");
		}
		
		if (whiteBackground)
		{
			writer.write(" background-color: white;");
		}
		writer.write("\">\n");

		// TODO lucianc check whether we can use the first row for setting col widths
		writer.write("<tr valign=\"top\" style=\"height:0\">\n");
		for (Column col : columns)
		{
			writer.write("<td style=\"width:");
			writer.write(toSizeUnit(col.getExtent()));
			writer.write("\"></td>\n");
		}
		writer.write("</tr>\n");
		
		for (Row row : rows)
		{
			writer.write("<tr valign=\"top\" style=\"height:");
			writer.write(toSizeUnit(row.getExtent()));
			writer.write("\">\n");

			int emptySpan = 0;
			for (Column col : columns)
			{
				Cell cell = row.getCell(col);
				if (cell == null)
				{
					++emptySpan;
				}
				else
				{
					if (emptySpan > 0)
					{
						writeEmptyCell(emptySpan, 1);
					}
					emptySpan = 0;

					TablePosition position = new TablePosition(table, col, row);
					cell.accept(tableVisitor, position);
				}
			}
			if (emptySpan > 0)
			{
				writeEmptyCell(emptySpan, 1);
			}
			
			writer.write("</tr>\n");
		}
		
		writer.write("</table>\n");
	}

	protected void writeOwnText(JRPrintText text, TableCell cell)
			throws IOException
	{
		JRStyledText styledText = getStyledText(text);
		int textLength = styledText == null ? 0 : styledText.length();

		JRStyle textStyle = text.getStyle();
		if (textStyle == null) {
			textStyle = getDefaultStyle();
		}

		startCell(text, cell);

		if (text.getRunDirectionValue() == RunDirectionEnum.RTL)
		{
			writer.write(" dir=\"rtl\"");
		}

		StringBuilder styleBuffer = new StringBuilder();

		appendElementCellGenericStyle(cell, styleBuffer);
		appendOwnBackcolorStyle(cell, styleBuffer, textStyle);
		appendOwnBorderStyle(cell.getBox(), styleBuffer, textStyle);
		appendOwnPaddingStyle(text.getLineBox(), styleBuffer, textStyle);

		if (getCurrentItemConfiguration().isWrapBreakWord())
		{
			styleBuffer.append("width: " + toSizeUnit(text.getWidth()) + "; ");
			styleBuffer.append("word-wrap: break-word; ");
		}

		if (text.getLineBreakOffsets() != null)
		{
			//if we have line breaks saved in the text, set nowrap so that
			//the text only wraps at the explicit positions
			styleBuffer.append("white-space: nowrap; ");
		}

		if (text.getParagraph().getOwnFirstLineIndent() != null ) {
			styleBuffer.append("text-indent: " + text.getParagraph().getOwnFirstLineIndent().intValue() + "px; ");
		}

		String rotationValue = null;
		StringBuilder spanStyleBuffer = new StringBuilder();
		StringBuilder divStyleBuffer = new StringBuilder();

		if (text.getRotationValue() == RotationEnum.NONE)
		{
			VerticalAlignEnum ownValign = text.getOwnVerticalAlignmentValue();
			VerticalAlignEnum styleValign = textStyle.getVerticalAlignmentValue();
			if (ownValign != null && ownValign != VerticalAlignEnum.TOP && (styleValign == null || (styleValign != null && ownValign != styleValign)))
			{
				styleBuffer.append(" vertical-align: ");
				styleBuffer.append(ownValign.getName().toLowerCase());
				styleBuffer.append(";");
			}
		}
		else
		{
			rotationValue = setRotationStyles(text, spanStyleBuffer, divStyleBuffer);
		}

		writeStyle(styleBuffer);

		finishStartCell();

		if (text.getAnchorName() != null)
		{
			writer.write("<a name=\"");
			writer.write(text.getAnchorName());
			writer.write("\"/>");
		}

		if (text.getBookmarkLevel() != JRAnchor.NO_BOOKMARK)
		{
			writer.write("<a name=\"");
			writer.write(JR_BOOKMARK_ANCHOR_PREFIX + reportIndex + "_" + pageIndex + "_" + cell.getElementAddress());
			writer.write("\"/>");
		}

		if (rotationValue != null)
		{
			writer.write("<div style=\"position: relative; overflow: hidden; ");
			writer.write(divStyleBuffer.toString());
			writer.write("\">\n");
			writer.write("<span style=\"position: absolute; display: table; ");
			writer.write(spanStyleBuffer.toString());
			writer.write("\">");
			writer.write("<span style=\"display: table-cell; vertical-align:"); //display:table-cell conflicts with overflow: hidden;
			writer.write(text.getVerticalAlignmentValue().getName().toLowerCase());
			writer.write(";\">");
		}

		boolean hyperlinkStarted = startHyperlink(text);

		if (textLength > 0)
		{
			exportStyledText(text, styledText, text.getHyperlinkTooltip(), hyperlinkStarted);
		}

		if (hyperlinkStarted)
		{
			endHyperlink();
		}

		if (rotationValue != null)
		{
			writer.write("</span></span></div>");
		}

		endCell();
	}

	protected String setRotationStyles(JRPrintText text, StringBuilder spanStyleBuffer, StringBuilder divStyleBuffer)
	{
		String rotationValue;
		int textWidth = text.getWidth() - text.getLineBox().getLeftPadding() - text.getLineBox().getRightPadding();
		int textHeight = text.getHeight() - text.getLineBox().getTopPadding() - text.getLineBox().getBottomPadding();
		int rotatedWidth;
		int rotatedHeight;

		int rotationIE;
		int rotationAngle;
		int translateX;
		int translateY;
		switch (text.getRotationValue())
		{
			case LEFT :
			{
				translateX = - (textHeight - textWidth) / 2;
				translateY = (textHeight - textWidth) / 2;
				rotatedWidth = textHeight;
				rotatedHeight = textWidth;
				rotationIE = 3;
				rotationAngle = -90;
				rotationValue = "left";
				break;
			}
			case RIGHT :
			{
				translateX = - (textHeight - textWidth) / 2;
				translateY = (textHeight - textWidth) / 2;
				rotatedWidth = textHeight;
				rotatedHeight = textWidth;
				rotationIE = 1;
				rotationAngle = 90;
				rotationValue = "right";
				break;
			}
			case UPSIDE_DOWN :
			{
				translateX = 0;
				translateY = 0;
				rotatedWidth = textWidth;
				rotatedHeight = textHeight;
				rotationIE = 2;
				rotationAngle = 180;
				rotationValue = "upsideDown";
				break;
			}
			case NONE :
			default :
			{
				throw new JRRuntimeException("Unexpected rotation value " + text.getRotationValue());
			}
		}

		appendSizeStyle(textWidth, textHeight, divStyleBuffer);
		appendSizeStyle(rotatedWidth, rotatedHeight, spanStyleBuffer);

		spanStyleBuffer.append("text-align: ");
		spanStyleBuffer.append(text.getHorizontalAlignmentValue().getName().toLowerCase());
		spanStyleBuffer.append(";");

		spanStyleBuffer.append("-webkit-transform: translate(" + translateX + "px," + translateY + "px) ");
		spanStyleBuffer.append("rotate(" + rotationAngle + "deg); ");
		spanStyleBuffer.append("-moz-transform: translate(" + translateX + "px," + translateY + "px) ");
		spanStyleBuffer.append("rotate(" + rotationAngle + "deg); ");
		spanStyleBuffer.append("-ms-transform: translate(" + translateX + "px," + translateY + "px) ");
		spanStyleBuffer.append("rotate(" + rotationAngle + "deg); ");
		spanStyleBuffer.append("-o-transform: translate(" + translateX + "px," + translateY + "px) ");
		spanStyleBuffer.append("rotate(" + rotationAngle + "deg); ");
		spanStyleBuffer.append("filter: progid:DXImageTransform.Microsoft.BasicImage(rotation=" + rotationIE + "); ");
		return rotationValue;
	}

	protected void appendSizeStyle(int width, int height, StringBuilder styleBuffer)
	{
		styleBuffer.append("width:");
		styleBuffer.append(toSizeUnit(width));
		styleBuffer.append(";");

		styleBuffer.append("height:");
		styleBuffer.append(toSizeUnit(height));
		styleBuffer.append(";");
	}

	protected void writeOwnImage(JRPrintImage image, TableCell cell)
			throws IOException, JRException
	{
		startCell(image, cell);

		int imageWidth = image.getWidth() - image.getLineBox().getLeftPadding() - image.getLineBox().getRightPadding();
		if (imageWidth < 0)
		{
			imageWidth = 0;
		}
	
		int imageHeight = image.getHeight() - image.getLineBox().getTopPadding() - image.getLineBox().getBottomPadding();
		if (imageHeight < 0)
		{
			imageHeight = 0;
		}

		StringBuilder styleBuffer = new StringBuilder();
		ScaleImageEnum scaleImage = image.getScaleImageValue();
		if (scaleImage != ScaleImageEnum.CLIP)
		{
			// clipped images are absolutely positioned within a div
			setImageHorizontalAlignmentStyle(image, styleBuffer);
			setImageVerticalAlignmentStyle(image, styleBuffer);
		}
		else if (imageHeight > 0)
		{
			// some browsers need td height so that height: 100% works on the div used for clipped images.
			// we're using the height without paddings because that's closest to the HTML size model.
			styleBuffer.append("height: ");
			styleBuffer.append(toSizeUnit(imageHeight));
			styleBuffer.append("; ");
		}

		JRStyle imageStyle = image.getStyle();
		if (imageStyle == null)
		{
			imageStyle = getDefaultStyle();
		}

		appendElementCellGenericStyle(cell, styleBuffer);
		appendOwnBackcolorStyle(cell, styleBuffer, imageStyle);
		
		boolean addedToStyle = appendOwnBorderStyle(cell.getBox(), styleBuffer, imageStyle);
		if (!addedToStyle)
		{
			appendGraphicElementBorder(image, imageStyle, styleBuffer, null);
		}

		appendOwnPaddingStyle(image.getLineBox(), styleBuffer, imageStyle);

		writeStyle(styleBuffer);

		finishStartCell();

		if (image.getAnchorName() != null)
		{
			writer.write("<a name=\"");
			writer.write(image.getAnchorName());
			writer.write("\"/>");
		}
		
		if (image.getBookmarkLevel() != JRAnchor.NO_BOOKMARK)
		{
			writer.write("<a name=\"");
			writer.write(JR_BOOKMARK_ANCHOR_PREFIX + reportIndex + "_" + pageIndex + "_" + cell.getElementAddress());
			writer.write("\"/>");
		}
		
		Renderable renderer = image.getRenderable();
		Renderable originalRenderer = renderer;
		boolean imageMapRenderer = renderer != null 
				&& renderer instanceof ImageMapRenderable
				&& ((ImageMapRenderable) renderer).hasImageAreaHyperlinks();

		boolean hasHyperlinks = false;

		if(renderer != null)
		{
			boolean startedDiv = false;
			if (scaleImage == ScaleImageEnum.CLIP)
			{
				writer.write("<div style=\"width: 100%; height: 100%; position: relative; overflow: hidden;\">\n");
				startedDiv = true;
			}
			
			boolean hyperlinkStarted;
			if (imageMapRenderer)
			{
				hyperlinkStarted = false;
				hasHyperlinks = true;
			}
			else
			{
				hyperlinkStarted = startHyperlink(image);
				hasHyperlinks = hyperlinkStarted;
			}
			
			writer.write("<img");
			String imagePath = null;
			String imageMapName = null;
			List<JRPrintImageAreaHyperlink> imageMapAreas = null;
			
			if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE && rendererToImagePathMap.containsKey(renderer.getId()))
			{
				imagePath = rendererToImagePathMap.get(renderer.getId());
			}
			else
			{
				if (image.isLazy())
				{
					imagePath = ((JRImageRenderer)renderer).getImageLocation();
				}
				else
				{
					@SuppressWarnings("deprecation")
					HtmlResourceHandler imageHandler = 
						getImageHandler() == null 
						? getExporterOutput().getImageHandler() 
						: getImageHandler();
					if (imageHandler != null)
					{
						JRPrintElementIndex imageIndex = getElementIndex(cell);
						String imageName = getImageName(imageIndex);

						if (renderer.getTypeValue() == RenderableTypeEnum.SVG)
						{
							renderer =
								new JRWrappingSvgRenderer(
									renderer,
									new Dimension(image.getWidth(), image.getHeight()),
									ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
									);
						}

						byte[] imageData = renderer.getImageData(jasperReportsContext);

						if (imageHandler != null)
						{
							imageHandler.handleResource(imageName, imageData);
							
							imagePath = imageHandler.getResourcePath(imageName);
						}
					}
				}

				rendererToImagePathMap.put(renderer.getId(), imagePath);
			}
			
			if (imageMapRenderer)
			{
				Rectangle renderingArea = new Rectangle(image.getWidth(), image.getHeight());
				
				if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE)
				{
					imageMapName = imageMaps.get(new Pair<String, Rectangle>(renderer.getId(), renderingArea));
				}

				if (imageMapName == null)
				{
					imageMapName = "map_" + getElementIndex(cell).toString();
					imageMapAreas = ((ImageMapRenderable) originalRenderer).getImageAreaHyperlinks(renderingArea);//FIXMECHART
					
					if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE)
					{
						imageMaps.put(new Pair<String, Rectangle>(renderer.getId(), renderingArea), imageMapName);
					}
				}
			}

			writer.write(" src=\"");
			if (imagePath != null)
			{
				writer.write(imagePath);
			}
			writer.write("\"");
		
			switch (scaleImage)
			{
				case FILL_FRAME :
				{
					writer.write(" style=\"width: ");
					writer.write(toSizeUnit(imageWidth));
					writer.write("; height: ");
					writer.write(toSizeUnit(imageHeight));
					writer.write("\"");
		
					break;
				}
				case CLIP :
				{
					int positionLeft;
					int positionTop;
					
					HorizontalAlignEnum horizontalAlign = image.getHorizontalAlignmentValue();
					VerticalAlignEnum verticalAlign = image.getVerticalAlignmentValue();
					if (horizontalAlign == HorizontalAlignEnum.LEFT && verticalAlign == VerticalAlignEnum.TOP)
					{
						// no need to compute anything
						positionLeft = 0;
						positionTop = 0;
					}
					else
					{
						double[] normalSize = getImageNormalSize(image, originalRenderer, imageWidth, imageHeight);
						// these calculations assume that the image td does not stretch due to other cells.
						// when that happens, the image will not be properly aligned.
						float xAlignFactor = horizontalAlign == HorizontalAlignEnum.RIGHT ? 1f
								: (horizontalAlign == HorizontalAlignEnum.CENTER ? 0.5f : 0f);
						float yAlignFactor = verticalAlign == VerticalAlignEnum.BOTTOM ? 1f
								: (verticalAlign == VerticalAlignEnum.MIDDLE ? 0.5f : 0f);
						positionLeft = (int) (xAlignFactor * (imageWidth - normalSize[0]));
						positionTop = (int) (yAlignFactor * (imageHeight - normalSize[1]));
					}
					
					writer.write(" style=\"position: absolute; left:");
					writer.write(toSizeUnit(positionLeft));
					writer.write("; top: ");
					writer.write(toSizeUnit(positionTop));
					// not setting width, height and clip as it doesn't seem needed plus it fixes clip for lazy images
					writer.write(";\"");

					break;
				}
				case RETAIN_SHAPE :
				default :
				{
		
					if (imageHeight > 0)
					{
						double[] normalSize = getImageNormalSize(image, originalRenderer, imageWidth, imageHeight);
						double ratio = normalSize[0] / normalSize[1];
		
						if( ratio > (double)imageWidth / (double)imageHeight )
						{
							writer.write(" style=\"width: ");
							writer.write(toSizeUnit(imageWidth));
							writer.write("\"");
						}
						else
						{
							writer.write(" style=\"height: ");
							writer.write(toSizeUnit(imageHeight));
							writer.write("\"");
						}
					}
				}
			}
			
			if (imageMapName != null)
			{
				writer.write(" usemap=\"#" + imageMapName + "\"");
			}
			
			writer.write(" alt=\"\"");
			
			if (hasHyperlinks)
			{
				writer.write(" border=\"0\"");
			}
			
			if (image.getHyperlinkTooltip() != null)
			{
				writer.write(" title=\"");
				writer.write(JRStringUtil.xmlEncode(image.getHyperlinkTooltip()));
				writer.write("\"");
			}
			
			writer.write("/>");

			if (hyperlinkStarted)
			{
				endHyperlink();
			}
			
			if (startedDiv)
			{
				writer.write("</div>");
			}
			
			if (imageMapAreas != null)
			{
				writer.write("\n");
				writeImageMap(imageMapName, image, imageMapAreas);
			}
		}
		
		endCell();
	}

	protected void setImageHorizontalAlignmentStyle(JRPrintImage image, StringBuilder styleBuffer)
	{
		String horizontalAlignment = CSS_TEXT_ALIGN_LEFT;
		switch (image.getHorizontalAlignmentValue())
		{
			case RIGHT :
			{
				horizontalAlignment = CSS_TEXT_ALIGN_RIGHT;
				break;
			}
			case CENTER :
			{
				horizontalAlignment = CSS_TEXT_ALIGN_CENTER;
				break;
			}
			case LEFT :
			default :
			{
				horizontalAlignment = CSS_TEXT_ALIGN_LEFT;
			}
		}

		if (!horizontalAlignment.equals(CSS_TEXT_ALIGN_LEFT))
		{
			styleBuffer.append("text-align: ");
			styleBuffer.append(horizontalAlignment);
			styleBuffer.append(";");
		}
	}

	protected void setImageVerticalAlignmentStyle(JRPrintImage image, StringBuilder styleBuffer)
	{
		String verticalAlignment = HTML_VERTICAL_ALIGN_TOP;
		switch (image.getVerticalAlignmentValue())
		{
			case BOTTOM :
			{
				verticalAlignment = HTML_VERTICAL_ALIGN_BOTTOM;
				break;
			}
			case MIDDLE :
			{
				verticalAlignment = HTML_VERTICAL_ALIGN_MIDDLE;
				break;
			}
			case TOP :
			default :
			{
				verticalAlignment = HTML_VERTICAL_ALIGN_TOP;
			}
		}

		if (!verticalAlignment.equals(HTML_VERTICAL_ALIGN_TOP))
		{
			styleBuffer.append(" vertical-align: ");
			styleBuffer.append(verticalAlignment);
			styleBuffer.append(";");
		}
	}

	protected double[] getImageNormalSize(JRPrintImage image, Renderable renderer, int imageWidth, int imageHeight) throws JRException
	{
		double normalWidth = imageWidth;
		double normalHeight = imageHeight;

		if (!image.isLazy())
		{
			// Image load might fail. 
			Renderable tmpRenderer = RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForDimension(
					renderer, image.getOnErrorTypeValue());
			Dimension2D dimension = tmpRenderer == null ? null : tmpRenderer.getDimension(jasperReportsContext);
			// If renderer was replaced, ignore image dimension.
			if (tmpRenderer == renderer && dimension != null)
			{
				normalWidth = dimension.getWidth();
				normalHeight = dimension.getHeight();
			}
		}
		
		return new double[]{normalWidth, normalHeight};
	}
	
	protected JRPrintElementIndex getElementIndex(TableCell cell)
	{
		String elementAddress = cell.getElementAddress();
		JRPrintElementIndex elementIndex = new JRPrintElementIndex(reportIndex, pageIndex,
						elementAddress);
		return elementIndex;
	}

	protected void writeImageMap(String imageMapName, JRPrintImage image, List<JRPrintImageAreaHyperlink> imageMapAreas) throws IOException
	{
		writer.write("<map name=\"" + imageMapName + "\">\n");

		for (ListIterator<JRPrintImageAreaHyperlink> it = imageMapAreas.listIterator(imageMapAreas.size()); it.hasPrevious();)
		{
			JRPrintImageAreaHyperlink areaHyperlink = it.previous();
			JRPrintHyperlink link = areaHyperlink.getHyperlink();
			JRPrintImageArea area = areaHyperlink.getArea();

			writer.write("  <area shape=\"" + JRPrintImageArea.getHtmlShape(area.getShape()) + "\"");
			
			writeImageAreaCoordinates(area.getCoordinates());			
			writeImageAreaHyperlink(link);
			writer.write("/>\n");
		}
		
		if (image.getHyperlinkTypeValue() != HyperlinkTypeEnum.NONE)
		{
			writer.write("  <area shape=\"default\"");
			writeImageAreaCoordinates(new int[]{0, 0, image.getWidth(), image.getHeight()});//for IE
			writeImageAreaHyperlink(image);
			writer.write("/>\n");
		}
		
		writer.write("</map>\n");
	}
	
	protected void writeImageAreaCoordinates(int[] coords) throws IOException
	{
		if (coords != null && coords.length > 0)
		{
			StringBuilder coordsEnum = new StringBuilder(coords.length * 4);
			coordsEnum.append(toZoom(coords[0]));
			for (int i = 1; i < coords.length; i++)
			{
				coordsEnum.append(',');
				coordsEnum.append(toZoom(coords[i]));
			}
			writer.write(" coords=\"" + coordsEnum + "\"");
		}		
	}


	protected void writeImageAreaHyperlink(JRPrintHyperlink hyperlink) throws IOException
	{
		if (getReportContext() != null)
		{
			if (hyperlink.getLinkType() != null)
			{
				int id = hyperlink.hashCode() & 0x7FFFFFFF;
				writer.write(" class=\"_jrHyperLink " + hyperlink.getLinkType() + "\" data-id=\"" + id + "\"");

				HyperlinkData hyperlinkData = new HyperlinkData();
				hyperlinkData.setId(String.valueOf(id));
				hyperlinkData.setHref(getHyperlinkURL(hyperlink));
				hyperlinkData.setSelector("._jrHyperLink." + hyperlink.getLinkType());
				hyperlinkData.setHyperlink(hyperlink);

				hyperlinksData.add(hyperlinkData);
			}
		}
		else
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
		}

		if (hyperlink.getHyperlinkTooltip() != null)
		{
			writer.write(" title=\"");
			writer.write(JRStringUtil.xmlEncode(hyperlink.getHyperlinkTooltip()));
			writer.write("\"");
		}
	}

	protected void writeOwnRectangle(JRPrintGraphicElement element, TableCell cell) throws IOException
	{
		startCell(element, cell);

		JRStyle elementStyle = element.getStyle();
		if (elementStyle == null)
		{
			elementStyle = getDefaultStyle();
		}

		StringBuilder styleBuffer = new StringBuilder();

		appendElementCellGenericStyle(cell, styleBuffer);
		appendOwnBackcolorStyle(cell, styleBuffer, elementStyle);
		appendGraphicElementBorder(element, elementStyle, styleBuffer, null);

		writeStyle(styleBuffer);
		finishStartCell();
		endCell();
	}

	protected void appendGraphicElementBorder(JRPrintGraphicElement element, JRStyle elementStyle, StringBuilder sb, String side)
	{
		float borderWidth = -1f;
		LineStyleEnum lineStyle = null;
		Color lineColor = null;
		JRPen stylePen = elementStyle.getLinePen();

		if (stylePen != null)
		{
			if (element.getLinePen().getOwnLineWidth() != null
					&& !element.getLinePen().getOwnLineWidth().equals(stylePen.getLineWidth()))
			{
				borderWidth = element.getLinePen().getOwnLineWidth();
			}
			if (element.getLinePen().getOwnLineStyleValue() != null
					&& !element.getLinePen().getOwnLineStyleValue().equals(stylePen.getLineStyleValue()))
			{
				lineStyle = element.getLinePen().getOwnLineStyleValue();
			}
			if (element.getLinePen().getOwnLineColor() != null
					&& !element.getLinePen().getOwnLineColor().equals(stylePen.getLineColor()))
			{
				lineColor = element.getLinePen().getOwnLineColor();
			}
		}
		else
		{
			borderWidth = element.getLinePen().getLineWidth();
			lineStyle = element.getLinePen().getLineStyleValue();
			lineColor = element.getLinePen().getLineColor();
		}

		if (0f < borderWidth && borderWidth < 1f)
		{
			borderWidth = 1f;
		}

		appendOwnPen(
			sb,
			borderWidth,
			lineStyle,
			lineColor,
			side
		);
	}

	protected void writeOwnLine(JRPrintLine line, TableCell cell)
			throws IOException
	{
		startCell(line, cell);

		StringBuilder styleBuffer = new StringBuilder();

		JRStyle lineStyle = line.getStyle();
		if (lineStyle == null) {
			lineStyle = getDefaultStyle();
		}

		appendElementCellGenericStyle(cell, styleBuffer);
		appendOwnBackcolorStyle(cell, styleBuffer, lineStyle);

		String[] sides = new String[] {"top", "bottom", "left", "right"};
		int sideIndex;
		float ratio = line.getWidth() / line.getHeight();
		if (ratio > 1)
		{
			if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
			{
				sideIndex = 0;
			}
			else
			{
				sideIndex = 1;
			}
		}
		else
		{
			if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
			{
				sideIndex = 2;
			}
			else
			{
				sideIndex = 3;
			}
		}

		appendGraphicElementBorder(line, lineStyle, styleBuffer, sides[sideIndex]);

		// clear the border width for the other sides
		for (int i = 0; i < 4; i++)
		{
			if (i != sideIndex)
			{
				styleBuffer.append("border-").append(sides[i]).append("-width: 0; ");
			}
		}

		writeStyle(styleBuffer);
		finishStartCell();
		endCell();
	}
	
	protected void writeGenericElement(JRGenericPrintElement element, TableCell cell) throws IOException
	{
		GenericElementHtmlHandler handler = (GenericElementHtmlHandler) 
				GenericElementHandlerEnviroment.getInstance(getJasperReportsContext()).getElementHandler(
						element.getGenericType(), HTML_EXPORTER_KEY);
		
		if (handler == null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("No HTML generic element handler for " 
						+ element.getGenericType());
			}
			
			writeEmptyCell(cell.getColumnSpan(), cell.getRowSpan());// TODO lucianc backcolor/borders?
		}
		else
		{
			startCell(element, cell);

			StringBuilder styleBuffer = new StringBuilder();
			appendElementCellGenericStyle(cell, styleBuffer);
			appendBackcolorStyle(cell, styleBuffer);
			appendBorderStyle(cell.getBox(), styleBuffer);
			if (styleBuffer.length() > 0)
			{
				writer.write(" style=\"");
				writer.write(styleBuffer.toString());
				writer.write("\"");
			}

			finishStartCell();
			
			String htmlFragment = handler.getHtmlFragment(exporterContext, element);
			if (htmlFragment != null)
			{
				writer.write(htmlFragment);
			}
			
			endCell();
		}
	}
	
	protected void writeLayers(List<Table> layers, TableVisitor tableVisitor, TableCell cell) throws IOException
	{
		startCell(cell);

		StringBuilder styleBuffer = new StringBuilder();
		appendElementCellGenericStyle(cell, styleBuffer);
		appendBackcolorStyle(cell, styleBuffer);
		appendBorderStyle(cell.getBox(), styleBuffer);
		writeStyle(styleBuffer);

		finishStartCell();
		
		// layers need to always specify backcolors
		setBackcolor(null);
		writer.write("<div style=\"width: 100%; height: 100%; position: relative;\">\n");

		for (ListIterator<Table> it = layers.listIterator(); it.hasNext();)
		{
			Table table = it.next();
			
			StringBuilder layerStyleBuffer = new StringBuilder();
			if (it.hasNext()) {
				layerStyleBuffer.append("position: absolute; overflow: hidden; ");
			} else {
				layerStyleBuffer.append("position: relative; ");
			}
			layerStyleBuffer.append("width: 100%; height: 100%; ");
			
			if (it.previousIndex() > 0) {
				layerStyleBuffer.append("pointer-events: none; ");
			}

			writer.write("<div style=\"");
			writer.write(layerStyleBuffer.toString());
			writer.write("\">\n");

			++pointerEventsNoneStack;
			exportTable(tableVisitor, table, false, false);
			--pointerEventsNoneStack;
			
			writer.write("</div>\n");
		}
		
		writer.write("</div>\n");
		restoreBackcolor();

		endCell();
	}

	protected void startCell(JRPrintElement element, TableCell cell) throws IOException
	{
		startCell(cell.getColumnSpan(), cell.getRowSpan());

		String dataAttr = getDataAttributes(element, cell);
		if (dataAttr != null)
		{
			writer.write(dataAttr);
		}
	}
	
	public String getDataAttributes(JRPrintElement element, TableCell cell)
	{
		StringBuffer sbuffer = new StringBuffer();
		StringBuffer cssClassBuffer = new StringBuffer();

		String id = getCellProperty(element, cell, PROPERTY_HTML_ID);
		if (id != null)
		{
			sbuffer.append(" id=\"" + id +"\"");
		}

		// Add CSS class based on element type
		JRStyle elementStyle = element.getStyle();
		if (elementStyle == null)
		{
			elementStyle = getDefaultStyle();
		}
		if (element instanceof JRPrintText)
		{
			cssClassBuffer.append(elementStyle.getName()).append(CSS_TEXT_SUFFIX);
		}
		else if (element instanceof  JRPrintGraphicElement)
		{
			if (element instanceof JRPrintImage)
			{
				cssClassBuffer.append(elementStyle.getName()).append(CSS_IMAGE_SUFFIX);
			}
			else
			{
				cssClassBuffer.append(elementStyle.getName()).append(CSS_GRAPHIC_SUFFIX);
			}
		}

		String clazz = getCellProperty(element, cell, PROPERTY_HTML_CLASS);
		if (clazz != null)
		{
			cssClassBuffer.append(" ").append(clazz);
		}

		if (cssClassBuffer.length() > 0)
		{
			sbuffer.append(" class=\"").append(cssClassBuffer).append("\"");
		}

		// Restrict interactivity attributes for static export
		if (getReportContext() != null)
		{
			String colUuid = getCellProperty(element, cell, HeaderToolbarElement.PROPERTY_COLUMN_UUID);//FIXMEJIVE register properties like this in a pluggable way; extensions?
			if (colUuid != null)
			{
				sbuffer.append(" data-coluuid=\"" + colUuid + "\"");
			}
			String cellId = getCellProperty(element, cell, HeaderToolbarElement.PROPERTY_CELL_ID);
			if (cellId != null)
			{
				sbuffer.append(" data-cellid=\"" + cellId + "\"");
			}
			String tableUuid = getCellProperty(element, cell, HeaderToolbarElement.PROPERTY_TABLE_UUID);
			if (tableUuid != null)
			{
				sbuffer.append(" data-tableuuid=\"" + tableUuid + "\"");
			}
			String columnIndex = getCellProperty(element, cell, HeaderToolbarElement.PROPERTY_COLUMN_INDEX);
			if (columnIndex != null)
			{
				sbuffer.append(" data-colidx=\"" + columnIndex + "\"");
			}

			String xtabId = getCellProperty(element, cell, CrosstabInteractiveJsonHandler.PROPERTY_CROSSTAB_ID);
			if (xtabId != null)
			{
				sbuffer.append(" " + CrosstabInteractiveJsonHandler.ATTRIBUTE_CROSSTAB_ID + "=\""
						+ JRStringUtil.htmlEncode(xtabId) + "\"");
			}

			String xtabColIdx = getCellProperty(element, cell, CrosstabInteractiveJsonHandler.PROPERTY_COLUMN_INDEX);
			if (xtabColIdx != null)
			{
				sbuffer.append(" " + CrosstabInteractiveJsonHandler.ATTRIBUTE_COLUMN_INDEX + "=\""
						+ JRStringUtil.htmlEncode(xtabColIdx) + "\"");
			}
		}
		
		return sbuffer.length() > 0 ? sbuffer.toString() : null;
	}
	
	protected String getCellProperty(JRPrintElement element, TableCell cell, String key)
	{
		String property = null;
		if (element != null)
		{
			property = getPropertiesUtil().getProperty(element, key);
		}
		
		if (property == null)
		{
			Tabulator tabulator = cell.getTabulator();
			for (FrameCell parentCell = cell.getCell().getParent(); 
					parentCell != null && property == null;
					parentCell = parentCell.getParent())
			{
				JRPrintElement parentElement = tabulator.getCellElement(parentCell);
				property = getPropertiesUtil().getProperty(parentElement, key);
			}
		}
		return property;
	}
	
	protected void startCell(TableCell cell) throws IOException
	{
		startCell(cell.getElement(), cell);
	}

	protected void startCell(int colSpan, int rowSpan) throws IOException
	{
		writer.write("<td");
		if (colSpan > 1)
		{
			writer.write(" colspan=\"");
			writer.write(Integer.toString(colSpan));
			writer.write("\"");
		}
		if (rowSpan > 1)
		{
			writer.write(" rowspan=\"");
			writer.write(Integer.toString(rowSpan));
			writer.write("\"");
		}		
	}
	
	protected void finishStartCell() throws IOException
	{
		writer.write(">\n");
	}
	
	protected void endCell() throws IOException
	{
		writer.write("</td>\n");
	}
	
	protected void writeEmptyCell(int colSpan, int rowSpan) throws IOException
	{
		startCell(colSpan, rowSpan);
		finishStartCell();
		endCell();
	}
	
	protected void writeFrameCell(TableCell cell) throws IOException
	{
		startCell(cell);
		
		StringBuilder styleBuffer = new StringBuilder();
		appendElementCellGenericStyle(cell, styleBuffer);
		appendBackcolorStyle(cell, styleBuffer);
		appendBorderStyle(cell.getBox(), styleBuffer);
		writeStyle(styleBuffer);

		finishStartCell();
		endCell();
	}

	protected void writeStyle(StringBuilder styleBuffer) throws IOException
	{
		if (styleBuffer.length() > 0)
		{
			writer.write(" style=\"");
			writer.write(styleBuffer.toString());
			writer.write("\"");
		}
	}
	
	protected void appendElementCellGenericStyle(TableCell cell, StringBuilder styleBuffer)
	{
		if (pointerEventsNoneStack > 0 && cell.getElement() != null)
		{
			styleBuffer.append("pointer-events: auto; ");
		}
	}

	protected void setBackcolor(Color color)
	{
		backcolorStack.addFirst(color);
	}

	protected void restoreBackcolor()
	{
		backcolorStack.removeFirst();
	}

	protected boolean matchesBackcolor(Color backcolor)
	{
		if (backcolorStack.isEmpty())
		{
			return false;
		}
		
		Color currentBackcolor = backcolorStack.getFirst();
		return currentBackcolor != null && backcolor.getRGB() == currentBackcolor.getRGB();
	}
	
	protected Color appendBackcolorStyle(TableCell cell, StringBuilder styleBuffer)
	{
		Color cellBackcolor = cell.getBackcolor();
		if (cellBackcolor != null && !matchesBackcolor(cellBackcolor))
		{
			styleBuffer.append("background-color: ");
			styleBuffer.append(JRColorUtil.getCssColor(cellBackcolor));
			styleBuffer.append("; ");

			return cellBackcolor;
		}

		return null;
	}

	protected Color appendOwnBackcolorStyle(TableCell cell, StringBuilder styleBuffer, JRStyle style)
	{
		Color cellBackcolor = cell.getBackcolor();
		if (cellBackcolor != null && !matchesBackcolor(cellBackcolor) && !cellBackcolor.equals(style.getBackcolor()))
		{
			styleBuffer.append("background-color: ");
			styleBuffer.append(JRColorUtil.getCssColor(cellBackcolor));
			styleBuffer.append("; ");

			return cellBackcolor;
		}

		return null;
	}

	protected boolean appendBorderStyle(JRLineBox box, StringBuilder styleBuffer)
	{
		boolean addedToStyle = false;

		if (box != null)
		{
			LineStyleEnum tps = box.getTopPen().getLineStyleValue();
			LineStyleEnum lps = box.getLeftPen().getLineStyleValue();
			LineStyleEnum bps = box.getBottomPen().getLineStyleValue();
			LineStyleEnum rps = box.getRightPen().getLineStyleValue();
			
			float tpw = box.getTopPen().getLineWidth().floatValue();
			float lpw = box.getLeftPen().getLineWidth().floatValue();
			float bpw = box.getBottomPen().getLineWidth().floatValue();
			float rpw = box.getRightPen().getLineWidth().floatValue();
			
			if (0f < tpw && tpw < 1f) {
				tpw = 1f;
			}
			if (0f < lpw && lpw < 1f) {
				lpw = 1f;
			}
			if (0f < bpw && bpw < 1f) {
				bpw = 1f;
			}
			if (0f < rpw && rpw < 1f) {
				rpw = 1f;
			}
			
			Color tpc = box.getTopPen().getLineColor();
			
			// try to compact all borders into one css property
			if (tps == lps &&												// same line style
					tps == bps &&
					tps == rps &&
					tpw == lpw &&											// same line width
					tpw == bpw &&
					tpw == rpw &&
					(tpc == null || (tpc != null &&
					tpc.equals(box.getLeftPen().getLineColor()) &&			// same line color
					tpc.equals(box.getBottomPen().getLineColor()) &&
					tpc.equals(box.getRightPen().getLineColor()))))
			{
				addedToStyle |= appendPen(
						styleBuffer,
						box.getTopPen(),
						null
						);
			} else {
				addedToStyle |= appendPen(
					styleBuffer,
					box.getTopPen(),
					"top"
					);
				addedToStyle |= appendPen(
					styleBuffer,
					box.getLeftPen(),
					"left"
					);
				addedToStyle |= appendPen(
					styleBuffer,
					box.getBottomPen(),
					"bottom"
					);
				addedToStyle |= appendPen(
					styleBuffer,
					box.getRightPen(),
					"right"
					);
			}
		}
		
		return addedToStyle;
	}

	protected boolean appendOwnBorderStyle(JRLineBox box, StringBuilder styleBuffer, JRStyle elementStyle)
	{
		boolean addedToStyle = false;

		if (box != null)
		{
			float tpw = -1f, lpw = -1f, bpw = -1f, rpw = -1f;
			LineStyleEnum tps = null, lps = null, bps = null, rps = null;
			Color tpc = null, lpc = null, bpc = null, rpc = null;

			JRLineBox styleBox = elementStyle.getLineBox();
			if (styleBox != null)
			{
				if (box.getTopPen().getOwnLineWidth() != null
						&& !box.getTopPen().getOwnLineWidth().equals(styleBox.getTopPen().getLineWidth()))
				{
					tpw = box.getTopPen().getOwnLineWidth().floatValue();
				}
				if (box.getLeftPen().getOwnLineWidth() != null
						&& !box.getLeftPen().getOwnLineWidth().equals(styleBox.getLeftPen().getLineWidth()))
				{
					lpw = box.getLeftPen().getOwnLineWidth().floatValue();
				}
				if (box.getBottomPen().getOwnLineWidth() != null
						&& !box.getBottomPen().getOwnLineWidth().equals(styleBox.getBottomPen().getLineWidth()))
				{
					bpw = box.getBottomPen().getOwnLineWidth().floatValue();
				}
				if (box.getRightPen().getOwnLineWidth() != null
						&& !box.getRightPen().getOwnLineWidth().equals((styleBox.getRightPen().getLineWidth())))
				{
					rpw = box.getRightPen().getOwnLineWidth().floatValue();
				}

				if (box.getTopPen().getOwnLineStyleValue() != styleBox.getTopPen().getLineStyleValue())
				{
					tps = box.getTopPen().getOwnLineStyleValue();
				}
				if (box.getLeftPen().getOwnLineStyleValue() != styleBox.getLeftPen().getLineStyleValue())
				{
					lps = box.getLeftPen().getOwnLineStyleValue();
				}
				if (box.getBottomPen().getOwnLineStyleValue() != styleBox.getBottomPen().getLineStyleValue())
				{
					bps = box.getBottomPen().getOwnLineStyleValue();
				}
				if (box.getRightPen().getOwnLineStyleValue() != styleBox.getRightPen().getLineStyleValue())
				{
					rps = box.getRightPen().getOwnLineStyleValue();
				}

				if (box.getTopPen().getOwnLineColor() != null
						&& !box.getTopPen().getOwnLineColor().equals(styleBox.getTopPen().getLineColor()))
				{
					tpc = box.getTopPen().getOwnLineColor();
				}
				if (box.getLeftPen().getOwnLineColor() != null
						&& !box.getLeftPen().getOwnLineColor().equals(styleBox.getLeftPen().getLineColor()))
				{
					lpc = box.getLeftPen().getOwnLineColor();
				}
				if (box.getBottomPen().getOwnLineColor() != null
						&& !box.getBottomPen().getOwnLineColor().equals(styleBox.getBottomPen().getLineColor()))
				{
					bpc = box.getBottomPen().getOwnLineColor();
				}
				if (box.getRightPen().getOwnLineColor() != null
						&& !box.getRightPen().getOwnLineColor().equals(styleBox.getRightPen().getLineColor()))
				{
					rpc = box.getRightPen().getOwnLineColor();
				}
			}
			else
			{
				tpw = box.getTopPen().getLineWidth().floatValue();
				lpw = box.getLeftPen().getLineWidth().floatValue();
				bpw = box.getBottomPen().getLineWidth().floatValue();
				rpw = box.getRightPen().getLineWidth().floatValue();

				tps = box.getTopPen().getLineStyleValue();
				lps = box.getLeftPen().getLineStyleValue();
				bps = box.getBottomPen().getLineStyleValue();
				rps = box.getRightPen().getLineStyleValue();

				tpc = box.getTopPen().getLineColor();
				lpc = box.getLeftPen().getLineColor();
				bpc = box.getBottomPen().getLineColor();
				rpc = box.getRightPen().getLineColor();
			}

			if (0f < tpw && tpw < 1f)
			{
				tpw = 1f;
			}
			if (0f < lpw && lpw < 1f)
			{
				lpw = 1f;
			}
			if (0f < bpw && bpw < 1f)
			{
				bpw = 1f;
			}
			if (0f < rpw && rpw < 1f)
			{
				rpw = 1f;
			}

			// try to compact all borders into one css property
			if (tps == lps &&												// same line style
					tps == bps &&
					tps == rps &&
					tpw == lpw &&											// same line width
					tpw == bpw &&
					tpw == rpw &&
					(tpc == null || (tpc != null &&
					tpc.equals(lpc) &&										// same line color
					tpc.equals(bpc) &&
					tpc.equals(rpc))))
			{
				addedToStyle |= appendOwnPen(styleBuffer, tpw, tps, tpc, null);
			}
			else
			{
				addedToStyle |= appendOwnPen(styleBuffer, tpw, tps, tpc, "top");
				addedToStyle |= appendOwnPen(styleBuffer, lpw, lps, lpc, "left");
				addedToStyle |= appendOwnPen(styleBuffer, bpw, bps, bpc, "bottom");
				addedToStyle |= appendOwnPen(styleBuffer, rpw, rps, rpc, "right");
			}
		}

		return addedToStyle;
	}

	protected boolean appendPen(StringBuilder sb, JRPen pen, String side)
	{
		boolean addedToStyle = false;
		float borderWidth = pen.getLineWidth().floatValue();

		if (borderWidth > 0f)
		{
			if (0f < borderWidth && borderWidth < 1f)
			{
				borderWidth = 1f;
			}

			StringBuilder borderProp = new StringBuilder("border");
			if (side != null)
			{
				borderProp.append("-");
				borderProp.append(side);
			}

			// compact form makes sense only when all three attributes(width, style, color) are specified
			if (pen.getLineStyleValue() != null && pen.getLineColor() != null)
			{
				sb.append(borderProp);
				sb.append(": ");
				sb.append(toSizeUnit((int)borderWidth));

				sb.append(" ");
				sb.append(pen.getLineStyleValue().getName().toLowerCase());

				sb.append(" ");
				sb.append(JRColorUtil.getCssColor(pen.getLineColor()));
				sb.append("; ");
			}
			else
			{
				sb.append(borderProp);
				sb.append("-width: ");
				sb.append(toSizeUnit((int)borderWidth));
				sb.append("; ");

				if (pen.getLineStyleValue() != null)
				{
					sb.append(borderProp);
					sb.append("-style: ");
					sb.append(pen.getLineStyleValue().getName().toLowerCase());
					sb.append("; ");
				}

				if (pen.getLineColor() != null)
				{
					sb.append(borderProp);
					sb.append("-color: ");
					sb.append(JRColorUtil.getCssColor(pen.getLineColor()));
					sb.append("; ");
				}
			}

			addedToStyle = true;
		}

		return addedToStyle;
	}

	protected boolean appendOwnPen(StringBuilder sb, float borderWidth, LineStyleEnum lineStyle, Color lineColor, String side)
	{
		boolean addedToStyle = false;
		boolean sameBorderWidth = borderWidth == -1f;

		if (borderWidth == 0f) // when border width is 0, other properties do not matter
		{
			sb.append("border");
			if (side != null)
			{
				sb.append("-").append(side);
			}
			sb.append("-width: ").append(toSizeUnit(borderWidth)).append(";");

			addedToStyle = true;
		}
		else
		{
			StringBuilder borderProp = new StringBuilder("border");
			if (side != null)
			{
				borderProp.append("-");
				borderProp.append(side);
			}

			// compact form makes sense only when all three attributes(width, style, color) are specified
			if (!sameBorderWidth && lineStyle != null && lineColor != null)
			{
				sb.append(borderProp);
				sb.append(": ");
				sb.append(toSizeUnit(borderWidth));

				sb.append(" ");
				sb.append(lineStyle.getName().toLowerCase());

				sb.append(" ");
				sb.append(JRColorUtil.getCssColor(lineColor));
				sb.append("; ");

				addedToStyle = true;
			}
			else
			{
				if (!sameBorderWidth)
				{
					sb.append(borderProp);
					sb.append("-width: ");
					sb.append(toSizeUnit(borderWidth));
					sb.append("; ");

					addedToStyle = true;
				}

				if (lineStyle != null)
				{
					sb.append(borderProp);
					sb.append("-style: ");
					sb.append(lineStyle.getName().toLowerCase());
					sb.append("; ");

					addedToStyle = true;
				}

				if (lineColor != null)
				{
					sb.append(borderProp);
					sb.append("-color: ");
					sb.append(JRColorUtil.getCssColor(lineColor));
					sb.append("; ");

					addedToStyle = true;
				}
			}
		}

		return  addedToStyle;
	}

	protected boolean appendPaddingStyle(JRLineBox box, StringBuilder styleBuffer)
	{
		boolean addedToStyle = false;
		
		if (box != null)
		{
			Integer tp = box.getTopPadding();
			Integer lp = box.getLeftPadding();
			Integer bp = box.getBottomPadding();
			Integer rp = box.getRightPadding();
			
			// try to compact all paddings into one css property
			if (tp.equals(lp) && tp.equals(bp) && tp.equals(rp))
			{
				addedToStyle |= appendPadding(
						styleBuffer,
						tp,
						null
						);
			} else 
			{
				addedToStyle |= appendPadding(
						styleBuffer,
						box.getTopPadding(),
						"top"
						);
				addedToStyle |= appendPadding(
						styleBuffer,
						box.getLeftPadding(),
						"left"
						);
				addedToStyle |= appendPadding(
						styleBuffer,
						box.getBottomPadding(),
						"bottom"
						);
				addedToStyle |= appendPadding(
						styleBuffer,
						box.getRightPadding(),
						"right"
						);
			}
		}
		
		return addedToStyle;
	}

	protected boolean appendOwnPaddingStyle(JRLineBox box, StringBuilder styleBuffer,  JRStyle elementStyle)
	{
		boolean addedToStyle = false;

		if (box != null)
		{
			Integer tp = null, lp = null, bp = null, rp = null;
			JRLineBox styleBox = elementStyle.getLineBox();

			if (styleBox != null)
			{
				if (!box.getTopPadding().equals(styleBox.getTopPadding()))
				{
					tp = box.getTopPadding();
				}
				if (!box.getLeftPadding().equals(styleBox.getLeftPadding()))
				{
					lp = box.getLeftPadding();
				}
				if (!box.getBottomPadding().equals(styleBox.getBottomPadding()))
				{
					bp = box.getBottomPadding();
				}
				if (!box.getRightPadding().equals(styleBox.getRightPadding()))
				{
					rp = box.getRightPadding();
				}
			}
			else
			{
				tp = box.getTopPadding();
				lp = box.getLeftPadding();
				bp = box.getBottomPadding();
				rp = box.getRightPadding();
			}

			// try to compact all paddings into one css property
			if (tp != null && tp.equals(lp) && tp.equals(bp) && tp.equals(rp))
			{
				addedToStyle |= appendPadding(styleBuffer, tp, null);
			} else
			{
				addedToStyle |= appendPadding(styleBuffer, tp, "top");
				addedToStyle |= appendPadding(styleBuffer, lp, "left");
				addedToStyle |= appendPadding(styleBuffer, bp, "bottom");
				addedToStyle |= appendPadding(styleBuffer, rp, "right");
			}
		}

		return addedToStyle;
	}

	protected boolean appendPadding(StringBuilder sb, Integer padding, String side)
	{
		boolean addedToStyle = false;
		
		if (padding != null && padding.intValue() > 0)
		{
			sb.append("padding");
			if (side != null)
			{
				sb.append("-");
				sb.append(side);
			}
			sb.append(": ");
			sb.append(toSizeUnit(padding.intValue()));
			sb.append("; ");

			addedToStyle = true;
		}
		
		return addedToStyle;
	}

	protected boolean startHyperlink(JRPrintHyperlink link) throws IOException
	{
		boolean hyperlinkStarted = false,
				canWrite = false;

		if (getReportContext() != null)
		{
			Boolean ignoreHyperlink = HyperlinkUtil.getIgnoreHyperlink(HtmlReportConfiguration.PROPERTY_IGNORE_HYPERLINK, link);
			if (ignoreHyperlink == null)
			{
				ignoreHyperlink = getCurrentItemConfiguration().isIgnoreHyperlink();
			}

			if (!ignoreHyperlink && link.getLinkType() != null)
			{
				canWrite = true;
				int id = link.hashCode() & 0x7FFFFFFF;

				writer.write("<span class=\"_jrHyperLink " + link.getLinkType() + "\" data-id=\"" + id + "\"");

				HyperlinkData hyperlinkData = new HyperlinkData();
				hyperlinkData.setId(String.valueOf(id));
				hyperlinkData.setHref(getHyperlinkURL(link));
				hyperlinkData.setSelector("._jrHyperLink." + link.getLinkType());
				hyperlinkData.setHyperlink(link);

				hyperlinksData.add(hyperlinkData);
				hyperlinkStarted = true;
			}
		}
		else
		{
			String href = getHyperlinkURL(link);

			if (href != null)
			{
				canWrite = true;
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
			}

			hyperlinkStarted = href != null;
		}

		if (canWrite)
		{
			if (link.getHyperlinkTooltip() != null)
			{
				writer.write(" title=\"");
				writer.write(JRStringUtil.xmlEncode(link.getHyperlinkTooltip()));
				writer.write("\"");
			}

			writer.write(">");
		}

		return hyperlinkStarted;
	}

	protected void endHyperlink() throws IOException
	{
		if (getReportContext() != null) {
			writer.write("</span>");
		}
		else
		{
			writer.write("</a>");
		}
	}

	protected String getHyperlinkURL(JRPrintHyperlink link)
	{
		return resolveHyperlinkURL(reportIndex, link);
	}
	
	protected String resolveHyperlinkURL(int reportIndex, JRPrintHyperlink link)
	{
		String href = null;
		
		Boolean ignoreHyperlink = HyperlinkUtil.getIgnoreHyperlink(HtmlReportConfiguration.PROPERTY_IGNORE_HYPERLINK, link);
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
							href = "#" + link.getHyperlinkAnchor();
						}
						break;
					}
					case LOCAL_PAGE :
					{
						if (link.getHyperlinkPage() != null)
						{
							href = "#" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString();
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
							href = link.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + link.getHyperlinkPage().toString();
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

	protected String getHyperlinkTarget(JRPrintHyperlink link)
	{
		String target = null;
		JRHyperlinkTargetProducer producer = targetProducerFactory.getHyperlinkTargetProducer(link.getLinkTarget());		
		if (producer == null)
		{
			switch(link.getHyperlinkTargetValue())
			{
				case BLANK :
				{
					target = "_blank";//FIXME make reverse for html markup hyperlinks
					break;
				}
				case PARENT :
				{
					target = "_parent";
					break;
				}
				case TOP :
				{
					target = "_top";
					break;
				}
				case CUSTOM :
				{
					boolean paramFound = false;
					List<JRPrintHyperlinkParameter> parameters = link.getHyperlinkParameters() == null ? null : link.getHyperlinkParameters().getParameters();
					if (parameters != null)
					{
						for(Iterator<JRPrintHyperlinkParameter> it = parameters.iterator(); it.hasNext();)
						{
							JRPrintHyperlinkParameter parameter = it.next();
							if (link.getLinkTarget().equals(parameter.getName()))
							{
								target = parameter.getValue() == null ? null : parameter.getValue().toString();
								paramFound = true;
								break;
							}
						}
					}
					if (!paramFound)
					{
						target = link.getLinkTarget();
					}
					break;
				}
				case SELF :
				default :
				{
				}
			}
		}
		else
		{
			target = producer.getHyperlinkTarget(link);
		}

		return target;
	}

	public String toSizeUnit(float size)
	{
		Number number = toZoom(size);
		if (number.intValue() == number.floatValue())
		{
			number = number.intValue();
		}

		return String.valueOf(number) + getCurrentItemConfiguration().getSizeUnit().getName();
	}

	/**
	 * @deprecated Replaced by {@link #toSizeUnit(float)}.
	 */
	public String toSizeUnit(int size)
	{
		return toSizeUnit((float)size);
	}

	protected float toZoom(float size)//FIXMEEXPORT cache this
	{
		float zoom = DEFAULT_ZOOM;
		
		Float zoomRatio = getCurrentItemConfiguration().getZoomRatio();
		if (zoomRatio != null)
		{
			zoom = zoomRatio.floatValue();
			if (zoom <= 0)
			{
				throw new JRRuntimeException("Invalid zoom ratio : " + zoom);
			}
		}

		return (zoom * size);
	}

	/**
	 * @deprecated Replaced by {@link #toZoom(float)}.
	 */
	protected int toZoom(int size)
	{
		return (int)toZoom((float)size);
	}

	protected JRStyledText getStyledText(JRPrintText textElement,
			boolean setBackcolor)
	{
		JRStyledText styledText = super.getStyledText(textElement, setBackcolor);
		
		if (styledText != null)
		{
			short[] lineBreakOffsets = textElement.getLineBreakOffsets();
			if (lineBreakOffsets != null && lineBreakOffsets.length > 0)
			{
				//insert new lines at the line break positions saved at fill time
				//cloning the text first
				//FIXME do we need this?  styled text instances are no longer shared
				styledText = styledText.cloneText();
				styledText.insert("\n", lineBreakOffsets);
			}
		}
		
		return styledText;
	}

	private void addSearchAttributes(JRStyledText styledText, JRPrintText textElement) {
		ReportContext reportContext = getReportContext();
		if (reportContext != null) {
			SpansInfo spansInfo = (SpansInfo) reportContext.getParameterValue("net.sf.jasperreports.search.term.highlighter");
			PrintElementId pei = PrintElementId.forElement(textElement);

			if (spansInfo != null && spansInfo.hasHitTermsInfo(pei.toString())) {
				List<HitTermInfo> hitTermInfos = JRCloneUtils.cloneList(spansInfo.getHitTermsInfo(pei.toString()));

				short[] lineBreakOffsets = textElement.getLineBreakOffsets();
				if (lineBreakOffsets != null && lineBreakOffsets.length > 0) {
					int sz = lineBreakOffsets.length;
					for (HitTermInfo ti: hitTermInfos) {
						for (int i = 0; i < sz; i++) {
							if (lineBreakOffsets[i] <= ti.getStart()) {
								ti.setStart(ti.getStart() + 1);
								ti.setEnd(ti.getEnd() + 1);
							} else {
								break;
							}
						}
					}
				}

				AttributedString attributedString = styledText.getAttributedString();
				for (int i = 0, ln = hitTermInfos.size(); i < ln; i = i + spansInfo.getTermsPerQuery()) {
					attributedString.addAttribute(JRTextAttribute.SEARCH_HIGHLIGHT, Color.yellow, hitTermInfos.get(i).getStart(), hitTermInfos.get(i + spansInfo.getTermsPerQuery() - 1).getEnd());
				}
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("No ReportContext to hold search data!");
			}
		}
	}
	
	protected void exportStyledText(JRPrintText printText, JRStyledText styledText, String tooltip, boolean hyperlinkStarted) throws IOException
	{
		Locale locale = getTextLocale(printText);
		LineSpacingEnum lineSpacing = printText.getParagraph().getLineSpacing();
		Float lineSpacingSize = printText.getParagraph().getLineSpacingSize();
		float lineSpacingFactor = printText.getLineSpacingFactor();
		Color backcolor = printText.getBackcolor();
		
		String text = styledText.getText();

		int runLimit = 0;

		addSearchAttributes(styledText, printText);

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		boolean first = true;
		boolean startedSpan = false;

		boolean highlightStarted = false;

		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			//if there are several text runs, write the tooltip into a parent <span>
			if (first && runLimit < styledText.length() && tooltip != null)
			{
				startedSpan = true;
				writer.write("<span title=\"");
				writer.write(JRStringUtil.xmlEncode(tooltip));
				writer.write("\">");
				//reset the tooltip so that inner <span>s to not use it
				tooltip = null;
			}
			first = false;

			Map<Attribute,Object> attributes = iterator.getAttributes();
			Color highlightColor = (Color) attributes.get(JRTextAttribute.SEARCH_HIGHLIGHT);
			if (highlightColor != null && !highlightStarted) {
				highlightStarted = true;
				writer.write("<span class=\"jr_search_result\">");
			} else if (highlightColor == null && highlightStarted) {
				highlightStarted = false;
				writer.write("</span>");
			}

			exportOwnStyledTextRun(
				printText,
				attributes,
				text.substring(iterator.getIndex(), runLimit),
				tooltip,
				locale,
				lineSpacing,
				lineSpacingSize,
				lineSpacingFactor,
				backcolor,
				hyperlinkStarted
			);

			iterator.setIndex(runLimit);
		}

		if (highlightStarted) {
			writer.write("</span>");
		}
		
		if (startedSpan)
		{
			writer.write("</span>");
		}
	}
	
	protected void exportOwnStyledTextRun(
			JRPrintText printText,
			Map<Attribute,Object> attributes,
			String text,
			String tooltip,
			Locale locale,
			LineSpacingEnum lineSpacing,
			Float lineSpacingSize,
			float lineSpacingFactor,
			Color backcolor,
			boolean hyperlinkStarted
			) throws IOException
	{
		JRStyle style = printText.getStyle();
		if (style == null) {
			style = getDefaultStyle();
		}

		boolean isBold = TextAttribute.WEIGHT_BOLD.equals(attributes.get(TextAttribute.WEIGHT));
		boolean isItalic = TextAttribute.POSTURE_OBLIQUE.equals(attributes.get(TextAttribute.POSTURE));

		String fontFamilyAttr = (String)attributes.get(TextAttribute.FAMILY);
		String fontFamily = fontFamilyAttr;

		FontInfo fontInfo = FontUtil.getInstance(jasperReportsContext).getFontInfo(fontFamilyAttr, locale);
		if (fontInfo != null)
		{
			//fontName found in font extensions
			FontFamily family = fontInfo.getFontFamily();
			String exportFont = family.getExportFont(getExporterKey());
			if (exportFont == null)
			{
				HtmlExporterOutput output = getExporterOutput();
				@SuppressWarnings("deprecation")
				HtmlResourceHandler fontHandler =
					output.getFontHandler() == null
					? getFontHandler()
					: output.getFontHandler();
				@SuppressWarnings("deprecation")
				HtmlResourceHandler resourceHandler =
					getExporterOutput().getResourceHandler() == null
					? getResourceHandler()
					: getExporterOutput().getResourceHandler();
				if (fontHandler != null && resourceHandler != null)
				{
					HtmlFont htmlFont = HtmlFont.getInstance(locale, fontInfo, isBold, isItalic);

					if (htmlFont != null)
					{
						if (!fontsToProcess.containsKey(htmlFont.getId()))
						{
							fontsToProcess.put(htmlFont.getId(), htmlFont);

							HtmlFontUtil.getInstance(jasperReportsContext).handleHtmlFont(resourceHandler, htmlFont);
						}

						fontFamily = htmlFont.getId();
					}
				}
			}
			else
			{
				fontFamily = exportFont;
			}
		}

		boolean localHyperlink = false;
		JRPrintHyperlink hyperlink = (JRPrintHyperlink)attributes.get(JRTextAttribute.HYPERLINK);
		if (!hyperlinkStarted && hyperlink != null)
		{
			localHyperlink = startHyperlink(hyperlink);
		}

		StringBuilder sb = new StringBuilder();
		writer.write("<span");
		if (style.getFontName() != fontFamilyAttr)
		{
			sb.append("font-family: ");
			sb.append(fontFamily);
			sb.append("; ");
		}

		Color forecolor = (Color)attributes.get(TextAttribute.FOREGROUND);
		Color styleForeColor = style.getForecolor();
		if (styleForeColor == null) {
			styleForeColor = Color.black;
		}
		if (!forecolor.equals(styleForeColor) && (!hyperlinkStarted || !Color.black.equals(forecolor)))
		{
			sb.append("color: ");
			sb.append(JRColorUtil.getCssColor(forecolor));
			sb.append("; ");
		}

		Color runBackcolor = (Color)attributes.get(TextAttribute.BACKGROUND);
		if (runBackcolor != null && !runBackcolor.equals(style.getBackcolor()) && !runBackcolor.equals(backcolor))
		{
			sb.append("background-color: ");
			sb.append(JRColorUtil.getCssColor(runBackcolor));
			sb.append("; ");
		}

		Float fontSize = (Float)attributes.get(TextAttribute.SIZE);
		Float styleFontSize = style.getFontsize();
		if (styleFontSize == null) {
			styleFontSize = JRProperties.getFloatProperty(JRFont.DEFAULT_FONT_SIZE);
		}
		if (!fontSize.equals(styleFontSize)) {
			sb.append("font-size: ");
			sb.append(toSizeUnit(fontSize));
			sb.append(";");
		}

		switch (lineSpacing)
		{
			case SINGLE:
			default:
			{
				if (lineSpacingFactor == 0)
				{
					sb.append(" line-height: 1; *line-height: normal;");
				}
				else
				{
					sb.append(" line-height: " + lineSpacingFactor + ";");
				}
				break;
			}
			case ONE_AND_HALF:
			{
				if (lineSpacingFactor == 0)
				{
					sb.append(" line-height: 1.5;");
				}
				else
				{
					sb.append(" line-height: " + lineSpacingFactor + ";");
				}
				break;
			}
			case DOUBLE:
			{
				if (lineSpacingFactor == 0)
				{
					sb.append(" line-height: 2.0;");
				}
				else
				{
					sb.append(" line-height: " + lineSpacingFactor + ";");
				}
				break;
			}
			case PROPORTIONAL:
			{
				if (lineSpacingSize != null) {
					sb.append(" line-height: " + lineSpacingSize.floatValue() + ";");
				}
				break;
			}
			case AT_LEAST:
			case FIXED:
			{
				if (lineSpacingSize != null) {
					sb.append(" line-height: " + lineSpacingSize.floatValue() + "px;");
				}
				break;
			}
		}

		if (isBold && !Boolean.TRUE.equals(style.isBold()))
		{
			sb.append(" font-weight: bold;");
		}
		if (isItalic && !Boolean.TRUE.equals(style.isItalic()))
		{
			sb.append(" font-style: italic;");
		}
		if (TextAttribute.UNDERLINE_ON.equals(attributes.get(TextAttribute.UNDERLINE)) && !Boolean.TRUE.equals(style.isUnderline()))
		{
			sb.append(" text-decoration: underline;");
		}
		if (TextAttribute.STRIKETHROUGH_ON.equals(attributes.get(TextAttribute.STRIKETHROUGH)) && !Boolean.TRUE.equals(style.isStrikeThrough()))
		{
			sb.append(" text-decoration: line-through;");
		}

		if (TextAttribute.SUPERSCRIPT_SUPER.equals(attributes.get(TextAttribute.SUPERSCRIPT)))
		{
			sb.append(" vertical-align: super;");
		}
		else if (TextAttribute.SUPERSCRIPT_SUB.equals(attributes.get(TextAttribute.SUPERSCRIPT)))
		{
			sb.append(" vertical-align: sub;");
		}

		if (sb.length() > 0) {
			writer.write(" style=\"");
			writer.write(sb.toString());
			writer.write("\"");
		}

		if (tooltip != null)
		{
			writer.write(" title=\"");
			writer.write(JRStringUtil.xmlEncode(tooltip));
			writer.write("\"");
		}

		writer.write(">");

		writer.write(
			JRStringUtil.htmlEncode(text)
			);

		writer.write("</span>");

		if (localHyperlink)
		{
			endHyperlink();
		}
	}

	protected class TableVisitor implements CellVisitor<TablePosition, Void, IOException>
	{
		private final Tabulator tabulator;
		private final PrintElementVisitor<TableCell> elementVisitor;
		
		public TableVisitor(Tabulator tabulator, PrintElementVisitor<TableCell> elementVisitor)
		{
			this.tabulator = tabulator;
			this.elementVisitor = elementVisitor;
		}
		
		@Override
		public Void visit(ElementCell cell, TablePosition position)
		{
			TableCell tableCell = tabulator.getTableCell(position, cell);
			JRPrintElement element = tableCell.getElement();
			element.accept(elementVisitor, tableCell);
			return null;
		}

		@Override
		public Void visit(SplitCell cell, TablePosition position)
		{
			//NOP
			return null;
		}

		@Override
		public Void visit(FrameCell frameCell, TablePosition position) throws IOException
		{
			TableCell tableCell = tabulator.getTableCell(position, frameCell);
			HtmlExporterCompactCss.this.writeFrameCell(tableCell);
			return null;
		}

		@Override
		public Void visit(LayeredCell layeredCell, TablePosition position)
				throws IOException
		{
			TableCell tableCell = tabulator.getTableCell(position, layeredCell);
			HtmlExporterCompactCss.this.writeLayers(layeredCell.getLayers(), this, tableCell);
			return null;
		}
	}

	protected class CellElementVisitor implements PrintElementVisitor<TableCell>
	{
		@Override
		public void visit(JRPrintText textElement, TableCell cell)
		{
			try
			{
				writeOwnText(textElement, cell);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}

		@Override
		public void visit(JRPrintImage image, TableCell cell)
		{
			try
			{
				writeOwnImage(image, cell);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
			catch (JRException e)
			{
				throw new JRRuntimeException(e);
			}
		}

		@Override
		public void visit(JRPrintRectangle rectangle, TableCell cell)
		{
			try
			{
				writeOwnRectangle(rectangle, cell);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}

		@Override
		public void visit(JRPrintLine line, TableCell cell)
		{
			try
			{
				writeOwnLine(line, cell);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}

		@Override
		public void visit(JRPrintEllipse ellipse, TableCell cell)
		{
			try
			{
				writeOwnRectangle(ellipse, cell);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}

		@Override
		public void visit(JRPrintFrame frame, TableCell cell)
		{
			throw new JRRuntimeException("Internal error");
		}

		@Override
		public void visit(JRGenericPrintElement printElement, TableCell cell)
		{
			try
			{
				writeGenericElement(printElement, cell);
			} 
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}
	}

	protected class ExporterContext extends BaseExporterContext implements JRHtmlExporterContext
	{
		public String getHyperlinkURL(JRPrintHyperlink link)
		{
			return HtmlExporterCompactCss.this.getHyperlinkURL(link);
		}
	}

	private void addReportStyles(StringBuilder sb) throws IOException {
		List<JRStyle> styles = jasperPrint.getStylesList();

		for (JRStyle style: styles) {
			sb.append(getCssStyle(style));
		}

		if (jasperPrint.getDefaultStyle() == null) {
			sb.append(getDefaultCssStyle());
		}
	}

	private JRStyle getDefaultStyle() {
		if (jasperPrint.getDefaultStyle() != null) {
			return jasperPrint.getDefaultStyle();
		}

		return jrDefaultStyle;
	}

	private String getCssStyle(JRStyle style) {
		StringBuilder sb = new StringBuilder();

		/*** Base styling ***/
		sb.append(".").append(style.getName()).append(CSS_TEXT_SUFFIX).
				append(", .").append(style.getName()).append(CSS_GRAPHIC_SUFFIX).
				append(", .").append(style.getName()).append(CSS_IMAGE_SUFFIX).append(" {\n");

		// Color
		Color foreColor = style.getForecolor();
		if (foreColor == null) {
			foreColor = Color.black;
		}
		sb.append("color: ").append(JRColorUtil.getCssColor(foreColor)).append(";\n");

		if(style.getModeValue() != null && style.getModeValue().equals(ModeEnum.OPAQUE) && style.getBackcolor() != null) {
			sb.append("background-color: ").append(JRColorUtil.getCssColor(style.getBackcolor())).append(";\n");
		}

		// Text
		String fontFamily = style.getFontName();
		Locale locale = getLocale();

		FontInfo fontInfo = FontUtil.getInstance(jasperReportsContext).getFontInfo(fontFamily, locale);
		if (fontInfo != null)
		{
			//fontName found in font extensions
			FontFamily family = fontInfo.getFontFamily();
			String exportFont = family.getExportFont(getExporterKey());
			if (exportFont == null)
			{
				HtmlExporterOutput output = getExporterOutput();
				@SuppressWarnings("deprecation")
				HtmlResourceHandler fontHandler =
						output.getFontHandler() == null
								? getFontHandler()
								: output.getFontHandler();
				@SuppressWarnings("deprecation")
				HtmlResourceHandler resourceHandler =
						getExporterOutput().getResourceHandler() == null
								? getResourceHandler()
								: getExporterOutput().getResourceHandler();
				if (fontHandler != null && resourceHandler != null)
				{
					HtmlFont htmlFont = HtmlFont.getInstance(locale, fontInfo, Boolean.TRUE.equals(style.isBold()), Boolean.TRUE.equals(style.isItalic()));

					if (htmlFont != null)
					{
						if (!fontsToProcess.containsKey(htmlFont.getId()))
						{
							fontsToProcess.put(htmlFont.getId(), htmlFont);

							HtmlFontUtil.getInstance(jasperReportsContext).handleHtmlFont(resourceHandler, htmlFont);
						}

						fontFamily = htmlFont.getId();
					}
				}
			}
			else
			{
				fontFamily = exportFont;
			}
		}

		sb.append("font-family: ").append(fontFamily).append(";\n");

		Float fontSize = style.getFontsize();
		if (fontSize == null) {
			fontSize = JRProperties.getFloatProperty(JRFont.DEFAULT_FONT_SIZE);
		}
		sb.append("font-size: ").append(toSizeUnit(fontSize)).append(";\n");

		if (Boolean.TRUE.equals(style.isBold())) {
			sb.append("font-weight: bold;\n");
		}
		if (Boolean.TRUE.equals(style.isItalic())) {
			sb.append("font-style: italic;\n");
		}
		if (Boolean.TRUE.equals(style.isUnderline())) {
			sb.append("text-decoration: underline;\n");
		}
		if (Boolean.TRUE.equals(style.isStrikeThrough())) {
			sb.append("text-decoration: line-through;\n");
		}

		if (style.getParagraph().getFirstLineIndent() != null ) {
			sb.append("text-indent: " + style.getParagraph().getFirstLineIndent().intValue() + "px;\n");
		}

		sb.append("}\n");

		/*** Text specific styling ***/
		sb.append(".").append(style.getName()).append(CSS_TEXT_SUFFIX).append(" {\n");

		appendBorderStyle(style.getLineBox(), sb);
		appendPaddingStyle(style.getLineBox(), sb);

		// Alignment
		if (style.getRotationValue() == null || (style.getRotationValue() != null && style.getRotationValue() == RotationEnum.NONE)) {
			if (style.getVerticalAlignmentValue() != null && !(VerticalAlignEnum.TOP == style.getVerticalAlignmentValue())) {
				sb.append("vertical-align: ").append(style.getVerticalAlignmentValue().getName().toLowerCase()).append(";\n");
			}

			//writing text align every time even when it's left
			//because IE8 with transitional defaults to center
			HorizontalAlignEnum halign = style.getHorizontalAlignmentValue() != null ? style.getHorizontalAlignmentValue() : HorizontalAlignEnum.LEFT;
			sb.append("text-align: ").append(halign.getName().toLowerCase()).append(";\n");
		}

		sb.append("}\n");

		/*** Graphic specific styling ***/
		JRPen stylePen = style.getLinePen();
		StringBuilder penBuffer = null;

		sb.append(".").append(style.getName()).append(CSS_GRAPHIC_SUFFIX).append(" {\n");

		if (stylePen.getLineWidth() != null)
		{
			penBuffer = new StringBuilder();

			float borderWidth = stylePen.getLineWidth().floatValue();
			if (0f < borderWidth && borderWidth < 1f)
			{
				borderWidth = 1f;
			}

			appendOwnPen(penBuffer, borderWidth, stylePen.getLineStyleValue(), stylePen.getLineColor(), null);
			sb.append(penBuffer).append("}\n");
		}
		else // defaults
		{
			appendOwnPen(sb,  JRPen.LINE_WIDTH_1, LineStyleEnum.SOLID, Color.black, null);
			sb.append("}\n");
		}


		/*** Image specific styling ***/
		sb.append(".").append(style.getName()).append(CSS_IMAGE_SUFFIX).append(" {\n");
		boolean gotBoxStyle = appendBorderStyle(style.getLineBox(), sb);

		if (!gotBoxStyle && stylePen.getLineWidth() != null)
		{
			sb.append(penBuffer);
		}

		appendPaddingStyle(style.getLineBox(), sb);
		sb.append("}\n");

		return sb.toString();
	}

	private String getDefaultCssStyle() {
		if (jrDefaultStyle == null) {
			jrDefaultStyle = new JRBaseStyle(CSS_DEFAULT_STYLE_NAME);
		}
		return getCssStyle(jrDefaultStyle);
	}

}

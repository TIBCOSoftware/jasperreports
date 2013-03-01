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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.ImageMapRenderable;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
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
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.PrintElementVisitor;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.export.tabulator.BaseElementCell;
import net.sf.jasperreports.engine.export.tabulator.Cell;
import net.sf.jasperreports.engine.export.tabulator.CellVisitor;
import net.sf.jasperreports.engine.export.tabulator.Column;
import net.sf.jasperreports.engine.export.tabulator.ElementCell;
import net.sf.jasperreports.engine.export.tabulator.FrameCell;
import net.sf.jasperreports.engine.export.tabulator.LayeredCell;
import net.sf.jasperreports.engine.export.tabulator.Row;
import net.sf.jasperreports.engine.export.tabulator.SplitCell;
import net.sf.jasperreports.engine.export.tabulator.Table;
import net.sf.jasperreports.engine.export.tabulator.Tabulator;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRTextAttribute;
import net.sf.jasperreports.engine.util.Pair;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class HtmlExporter extends JRAbstractExporter
{
	
	private static final Log log = LogFactory.getLog(HtmlExporter.class);
	
	public static final String HTML_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "html";
	
	protected static final String HTML_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.html.";
	
	public static final String PROPERTY_HTML_HYPERLINK_VISIBLE = HTML_EXPORTER_PROPERTIES_PREFIX + JRPrintHyperlink.PROPERTY_HYPERLINK_VISIBLE_SUFFIX;
	
	protected static final float DEFAULT_ZOOM = 1f;

	public static final String IMAGE_NAME_PREFIX = "img_";
	
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";
	
	public static String getImageName(JRPrintElementIndex printElementIndex)
	{
		return IMAGE_NAME_PREFIX + printElementIndex.toString();
	}

	public static JRPrintImage getImage(List<JasperPrint> jasperPrintList, JRPrintElementIndex imageIndex)
	{
		JasperPrint report = jasperPrintList.get(imageIndex.getReportIndex());
		JRPrintPage page = report.getPages().get(imageIndex.getPageIndex());

		Integer[] elementIndexes = imageIndex.getAddressArray();
		Object element = page.getElements().get(elementIndexes[0].intValue());

		for (int i = 1; i < elementIndexes.length; ++i)
		{
			JRPrintFrame frame = (JRPrintFrame) element;
			element = frame.getElements().get(elementIndexes[i].intValue());
		}

		return (JRPrintImage) element;
	}

	protected JRHyperlinkTargetProducerFactory targetProducerFactory;		
	
	protected JRExportProgressMonitor progressMonitor;
	
	protected String htmlHeader;
	protected String betweenPagesHtml;
	protected String htmlFooter;
	
	protected File imagesDir;
	protected boolean isOutputImagesToDir;
	protected String imagesURI;
	
	protected String encoding;
	
	protected boolean isWhitePageBackground;
	protected boolean isWrapBreakWord;
	protected boolean isIgnorePageMargins;
	protected boolean accessibleHtml;// TODO lucianc
	protected String sizeUnit;
	protected float zoom = DEFAULT_ZOOM;
	
	protected Map<String,String> rendererToImagePathMap;
	protected Map<Pair<String, Rectangle>,String> imageMaps;
	protected List<JRPrintElementIndex> imagesToProcess;
	protected Map<String,byte[]> imageNameToImageDataMap;
	
	protected Writer writer;
	protected int reportIndex;
	protected int pageIndex;
	
	protected JRHtmlExporterContext exporterContext = new ExporterContext();
	protected LinkedList<Color> backcolorStack = new LinkedList<Color>();
	
	public HtmlExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}

	public HtmlExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
		
		targetProducerFactory = new DefaultHyperlinkTargetProducerFactory(jasperReportsContext);		
	}
	
	@Override
	protected String getExporterKey()
	{
		return HTML_EXPORTER_KEY;
	}

	@Override
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);

		setOffset();
		try
		{
			setExportContext();
			setInput();
			
			if (!parameters.containsKey(JRExporterParameter.FILTER))
			{
				filter = createFilter(HTML_EXPORTER_PROPERTIES_PREFIX);
			}

			/*   */
			if (!isModeBatch)
			{
				setPageRange();
			}
	
			htmlHeader = (String)parameters.get(JRHtmlExporterParameter.HTML_HEADER);
			betweenPagesHtml = (String)parameters.get(JRHtmlExporterParameter.BETWEEN_PAGES_HTML);
			htmlFooter = (String)parameters.get(JRHtmlExporterParameter.HTML_FOOTER);
	
			imagesDir = (File)parameters.get(JRHtmlExporterParameter.IMAGES_DIR);
			if (imagesDir == null)
			{
				String dir = (String)parameters.get(JRHtmlExporterParameter.IMAGES_DIR_NAME);
				if (dir != null)
				{
					imagesDir = new File(dir);
				}
			}
	
			boolean isRemoveEmptySpace = 
					getBooleanParameter(
						JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
						JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
						false
						);
			if (isRemoveEmptySpace)
			{
				log.info("Removing empty spalce between rows not supported");
			}
	
			isWhitePageBackground = 
				getBooleanParameter(
					JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND,
					true
					);
	
			Boolean isOutputImagesToDirParameter = (Boolean)parameters.get(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR);
			if (isOutputImagesToDirParameter != null)
			{
				isOutputImagesToDir = isOutputImagesToDirParameter.booleanValue();
			}
	
			String uri = (String)parameters.get(JRHtmlExporterParameter.IMAGES_URI);
			if (uri != null)
			{
				imagesURI = uri;
			}
	
			encoding = 
				getStringParameterOrDefault(
					JRExporterParameter.CHARACTER_ENCODING, 
					JRExporterParameter.PROPERTY_CHARACTER_ENCODING
					);
	
			rendererToImagePathMap = new HashMap<String,String>();
			imageMaps = new HashMap<Pair<String, Rectangle>,String>();
			imagesToProcess = new ArrayList<JRPrintElementIndex>();
	
			//backward compatibility with the IMAGE_MAP parameter
			imageNameToImageDataMap = (Map<String,byte[]>) parameters.get(JRHtmlExporterParameter.IMAGES_MAP);
	//		if (imageNameToImageDataMap == null)
	//		{
	//			imageNameToImageDataMap = new HashMap();
	//		}
			//END - backward compatibility with the IMAGE_MAP parameter
	
			isWrapBreakWord = 
				getBooleanParameter(
					JRHtmlExporterParameter.IS_WRAP_BREAK_WORD,
					JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD,
					false
					);
	
			sizeUnit = 
				getStringParameterOrDefault(
					JRHtmlExporterParameter.SIZE_UNIT,
					JRHtmlExporterParameter.PROPERTY_SIZE_UNIT
					);
	
			Float zoomRatio = (Float)parameters.get(JRHtmlExporterParameter.ZOOM_RATIO);
			if (zoomRatio != null)
			{
				zoom = zoomRatio.floatValue();
				if (zoom <= 0)
				{
					throw new JRException("Invalid zoom ratio : " + zoom);
				}
			}
			else
			{
				zoom = DEFAULT_ZOOM;
			}
			
			isIgnorePageMargins = 
				getBooleanParameter(
					JRExporterParameter.IGNORE_PAGE_MARGINS,
					JRExporterParameter.PROPERTY_IGNORE_PAGE_MARGINS,
					false
					);
			
			accessibleHtml = 
				getPropertiesUtil().getBooleanProperty(
					jasperPrint,
					JRHtmlExporter.PROPERTY_ACCESSIBLE,
					false
					);
			
			setFontMap();
						
			setHyperlinkProducerFactory();

			//FIXMENOW check all exporter properties that are supposed to work at report level
			
			StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
			if (sb != null)
			{
				try
				{
					writer = new StringWriter();
					exportReportToWriter();
					sb.append(writer.toString());
				}
				catch (IOException e)
				{
					throw new JRException("Error writing to StringBuffer writer : " + jasperPrint.getName(), e);
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
			else
			{
				writer = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
				if (writer != null)
				{
					try
					{
						exportReportToWriter();
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
							writer = new OutputStreamWriter(os, encoding);
							exportReportToWriter();
						}
						catch (IOException e)
						{
							throw new JRException("Error writing to OutputStream writer : " + jasperPrint.getName(), e);
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
							writer = new OutputStreamWriter(os, encoding);
						}
						catch (IOException e)
						{
							throw new JRException("Error creating to file writer : " + jasperPrint.getName(), e);
						}
	
						if (imagesDir == null)
						{
							imagesDir = new File(destFile.getParent(), destFile.getName() + "_files");
						}
	
						if (isOutputImagesToDirParameter == null)
						{
							isOutputImagesToDir = true;
						}
	
						if (imagesURI == null)
						{
							imagesURI = imagesDir.getName() + "/";
						}
	
						try
						{
							exportReportToWriter();
						}
						catch (IOException e)
						{
							throw new JRException("Error writing to file writer : " + jasperPrint.getName(), e);
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
				}
			}
	
			if (isOutputImagesToDir)
			{
				if (imagesDir == null)
				{
					throw new JRException("The images directory was not specified for the exporter.");
				}
	
				if (imagesToProcess != null && imagesToProcess.size() > 0)
				{
					if (!imagesDir.exists())
					{
						imagesDir.mkdir();
					}
	
					for(Iterator<JRPrintElementIndex> it = imagesToProcess.iterator(); it.hasNext();)
					{
						JRPrintElementIndex imageIndex = it.next();
	
						JRPrintImage image = getImage(jasperPrintList, imageIndex);
						Renderable renderer = image.getRenderable();
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
	
						File imageFile = new File(imagesDir, getImageName(imageIndex));
						FileOutputStream fos = null;
	
						try
						{
							fos = new FileOutputStream(imageFile);
							fos.write(imageData, 0, imageData.length);
						}
						catch (IOException e)
						{
							throw new JRException("Error writing to image file : " + imageFile, e);
						}
						finally
						{
							if (fos != null)
							{
								try
								{
									fos.close();
								}
								catch(IOException e)
								{
								}
							}
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
	
	protected void exportReportToWriter() throws JRException, IOException
	{
		if (htmlHeader == null)
		{
			// no doctype because of bug 1430880
//			writer.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
//			writer.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
			writer.write("<html>\n");
			writer.write("<head>\n");
			writer.write("  <title></title>\n");
			writer.write("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + encoding + "\"/>\n");
			writer.write("  <style type=\"text/css\">\n");
			writer.write("    a {text-decoration: none}\n");
			writer.write("  </style>\n");
			writer.write("</head>\n");
			writer.write("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\">\n");
			writer.write("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n");
			writer.write("<tr><td width=\"50%\">&nbsp;</td><td align=\"center\">\n");
			writer.write("\n");
		}
		else
		{
			writer.write(htmlHeader);
		}

		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
		{
			setJasperPrint(jasperPrintList.get(reportIndex));

			List<JRPrintPage> pages = jasperPrint.getPages();
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
					if (Thread.interrupted())
					{
						throw new JRException("Current thread interrupted.");
					}

					page = pages.get(pageIndex);

					writer.write("<a name=\"" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1) + "\"></a>\n");

					/*   */
					exportPage(page);

					if (reportIndex < jasperPrintList.size() - 1 || pageIndex < endPageIndex)
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
	}
	
	protected void exportPage(JRPrintPage page) throws IOException
	{
		Tabulator tabulator = new Tabulator(filter, page.getElements());
		tabulator.tabulate();
		if (!isIgnorePageMargins)
		{
			tabulator.addMargins(jasperPrint.getPageWidth(), jasperPrint.getPageHeight());
		}
		
		Table table = tabulator.getTable();
		
		if (isWhitePageBackground)
		{
			setBackcolor(Color.white);
		}
		
		exportTable(tabulator, table, isWhitePageBackground);
		
		if (isWhitePageBackground)
		{
			restoreBackcolor();
		}
	}

	protected void exportTable(Tabulator tabulator, Table table, boolean whiteBackground) throws IOException
	{
		SortedSet<Column> columns = table.getColumns().getUserEntries();
		SortedSet<Row> rows = table.getRows().getUserEntries();
		if (columns.isEmpty() || rows.isEmpty())
		{
			// TODO lucianc empty page
			return;
		}
		
		TableVisitor tableVisitor = new TableVisitor(tabulator, table);
		
		int totalWidth = columns.last().getEndCoord() - columns.first().getStartCoord();
		
		writer.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"empty-cells: show; border-collapse: collapse; width: ");
		writer.write(toSizeUnit(totalWidth));
		writer.write(";");
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

					cell.accept(tableVisitor, new Pair<Column, Row>(col, row));
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

	protected void writeText(JRPrintText text, TableVisitor tableVisitor)
			throws IOException
	{
		JRStyledText styledText = getStyledText(text);
		int textLength = styledText == null ? 0 : styledText.length();
		
		startCell(text, tableVisitor);
		
		if (text.getRunDirectionValue() == RunDirectionEnum.RTL)
		{
			writer.write(" dir=\"rtl\"");
		}

		StringBuilder styleBuffer = new StringBuilder();

		String verticalAlignment = JRHtmlExporter.HTML_VERTICAL_ALIGN_TOP;

		switch (text.getVerticalAlignmentValue())
		{
			case BOTTOM :
			{
				verticalAlignment = JRHtmlExporter.HTML_VERTICAL_ALIGN_BOTTOM;
				break;
			}
			case MIDDLE :
			{
				verticalAlignment = JRHtmlExporter.HTML_VERTICAL_ALIGN_MIDDLE;
				break;
			}
			case TOP :
			default :
			{
				verticalAlignment = JRHtmlExporter.HTML_VERTICAL_ALIGN_TOP;
			}
		}

		if (!verticalAlignment.equals(JRHtmlExporter.HTML_VERTICAL_ALIGN_TOP))
		{
			styleBuffer.append(" vertical-align: ");
			styleBuffer.append(verticalAlignment);
			styleBuffer.append(";");
		}

		appendBackcolorStyle(tableVisitor, styleBuffer);
		appendBorderStyle(tableVisitor.getCellBox(), styleBuffer);
		appendPaddingStyle(text.getLineBox(), styleBuffer);

		String horizontalAlignment = JRHtmlExporter.CSS_TEXT_ALIGN_LEFT;

		if (textLength > 0)
		{
			switch (text.getHorizontalAlignmentValue())
			{
				case RIGHT :
				{
					horizontalAlignment = JRHtmlExporter.CSS_TEXT_ALIGN_RIGHT;
					break;
				}
				case CENTER :
				{
					horizontalAlignment = JRHtmlExporter.CSS_TEXT_ALIGN_CENTER;
					break;
				}
				case JUSTIFIED :
				{
					horizontalAlignment = JRHtmlExporter.CSS_TEXT_ALIGN_JUSTIFY;
					break;
				}
				case LEFT :
				default :
				{
					horizontalAlignment = JRHtmlExporter.CSS_TEXT_ALIGN_LEFT;
				}
			}

			if (
				(text.getRunDirectionValue() == RunDirectionEnum.LTR
				 && !horizontalAlignment.equals(JRHtmlExporter.CSS_TEXT_ALIGN_LEFT))
				|| (text.getRunDirectionValue() == RunDirectionEnum.RTL
					&& !horizontalAlignment.equals(JRHtmlExporter.CSS_TEXT_ALIGN_RIGHT))
				)
			{
				styleBuffer.append("text-align: ");
				styleBuffer.append(horizontalAlignment);
				styleBuffer.append(";");
			}
		}

		if (isWrapBreakWord)
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
		
		writeStyle(styleBuffer);
		
		finishStartCell();
		
		writer.write("<p style=\"overflow: hidden; ");

		writer.write("text-indent: " + text.getParagraph().getFirstLineIndent().intValue() + "px; ");
		writer.write("\">");

		if (text.getAnchorName() != null)
		{
			writer.write("<a name=\"");
			writer.write(text.getAnchorName());
			writer.write("\"/>");
		}

		boolean hyperlinkStarted = startHyperlink(text);

		if (textLength > 0)
		{
			//only use text tooltip when no hyperlink present
			String textTooltip = hyperlinkStarted ? null : text.getHyperlinkTooltip();
			exportStyledText(styledText, textTooltip, 
					getTextLocale(text), text.getParagraph().getLineSpacing(), text.getParagraph().getLineSpacingSize(),
					hyperlinkStarted);
		}

		if (hyperlinkStarted)
		{
			endHyperlink();
		}

		writer.write("</p>");
		
		endCell();
	}

	protected void writeImage(JRPrintImage image, TableVisitor tableVisitor)
			throws IOException, JRException
	{
		startCell(image, tableVisitor);

		StringBuilder styleBuffer = new StringBuilder();

		String horizontalAlignment = JRHtmlExporter.CSS_TEXT_ALIGN_LEFT;

		switch (image.getHorizontalAlignmentValue())
		{
			case RIGHT :
			{
				horizontalAlignment = JRHtmlExporter.CSS_TEXT_ALIGN_RIGHT;
				break;
			}
			case CENTER :
			{
				horizontalAlignment = JRHtmlExporter.CSS_TEXT_ALIGN_CENTER;
				break;
			}
			case LEFT :
			default :
			{
				horizontalAlignment = JRHtmlExporter.CSS_TEXT_ALIGN_LEFT;
			}
		}

		if (!horizontalAlignment.equals(JRHtmlExporter.CSS_TEXT_ALIGN_LEFT))
		{
			styleBuffer.append("text-align: ");
			styleBuffer.append(horizontalAlignment);
			styleBuffer.append(";");
		}

		String verticalAlignment = JRHtmlExporter.HTML_VERTICAL_ALIGN_TOP;

		switch (image.getVerticalAlignmentValue())
		{
			case BOTTOM :
			{
				verticalAlignment = JRHtmlExporter.HTML_VERTICAL_ALIGN_BOTTOM;
				break;
			}
			case MIDDLE :
			{
				verticalAlignment = JRHtmlExporter.HTML_VERTICAL_ALIGN_MIDDLE;
				break;
			}
			case TOP :
			default :
			{
				verticalAlignment = JRHtmlExporter.HTML_VERTICAL_ALIGN_TOP;
			}
		}

		if (!verticalAlignment.equals(JRHtmlExporter.HTML_VERTICAL_ALIGN_TOP))
		{
			styleBuffer.append(" vertical-align: ");
			styleBuffer.append(verticalAlignment);
			styleBuffer.append(";");
		}

		appendBackcolorStyle(tableVisitor, styleBuffer);
		
		boolean addedToStyle = appendBorderStyle(tableVisitor.getCellBox(), styleBuffer);
		if (!addedToStyle)
		{
			appendPen(
				styleBuffer,
				image.getLinePen(),
				null
				);
		}

		appendPaddingStyle(image.getLineBox(), styleBuffer);

		writeStyle(styleBuffer);

		finishStartCell();

		if (image.getAnchorName() != null)
		{
			writer.write("<a name=\"");
			writer.write(image.getAnchorName());
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

			ScaleImageEnum scaleImage = image.getScaleImageValue();
			
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
					JRPrintElementIndex imageIndex = getElementIndex(tableVisitor);
					imagesToProcess.add(imageIndex);

					String imageName = getImageName(imageIndex);
					imagePath = imagesURI + imageName;

					//backward compatibility with the IMAGE_MAP parameter
					if (imageNameToImageDataMap != null)
					{
						if (renderer.getTypeValue() == RenderableTypeEnum.SVG)
						{
							renderer =
								new JRWrappingSvgRenderer(
									renderer,
									new Dimension(image.getWidth(), image.getHeight()),
									ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
									);
						}
						imageNameToImageDataMap.put(imageName, renderer.getImageData(jasperReportsContext));
					}
					//END - backward compatibility with the IMAGE_MAP parameter
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
					imageMapName = "map_" + getElementIndex(tableVisitor).toString();
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
		
			int imageWidth = image.getWidth() - image.getLineBox().getLeftPadding().intValue() - image.getLineBox().getRightPadding().intValue();
			if (imageWidth < 0)
			{
				imageWidth = 0;
			}
		
			int imageHeight = image.getHeight() - image.getLineBox().getTopPadding().intValue() - image.getLineBox().getBottomPadding().intValue();
			if (imageHeight < 0)
			{
				imageHeight = 0;
			}
		
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
				case CLIP : //FIXMEIMAGE image clip could be achieved by cutting the image and preserving the image type
				case RETAIN_SHAPE :
				default :
				{
					double normalWidth = imageWidth;
					double normalHeight = imageHeight;
		
					if (!image.isLazy())
					{
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
					}
		
					if (imageHeight > 0)
					{
						double ratio = normalWidth / normalHeight;
		
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
			
			if (imageMapAreas != null)
			{
				writer.write("\n");
				writeImageMap(imageMapName, image, imageMapAreas);
			}
		}
		
		endCell();
	}

	protected JRPrintElementIndex getElementIndex(TableVisitor tableVisitor)
	{
		String elementAddress = tableVisitor.getElementAddress();
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
			JRPrintImageArea area = areaHyperlink.getArea();

			writer.write("  <area shape=\"" + JRPrintImageArea.getHtmlShape(area.getShape()) + "\"");
			writeImageAreaCoordinates(area.getCoordinates());			
			writeImageAreaHyperlink(areaHyperlink.getHyperlink());
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

	protected void writeRectangle(JRPrintGraphicElement element, TableVisitor tableVisitor) throws IOException
	{
		startCell(element, tableVisitor);

		StringBuilder styleBuffer = new StringBuilder();
		appendBackcolorStyle(tableVisitor, styleBuffer);
		appendPen(
			styleBuffer,
			element.getLinePen(),
			null
			);
		writeStyle(styleBuffer);

		finishStartCell();

		endCell();
	}

	protected void writeLine(JRPrintLine line, TableVisitor tableVisitor)
			throws IOException
	{
		startCell(line, tableVisitor);

		StringBuilder styleBuffer = new StringBuilder();

		appendBackcolorStyle(tableVisitor, styleBuffer);
		
		String side = null;
		float ratio = line.getWidth() / line.getHeight();
		if (ratio > 1)
		{
			if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
			{
				side = "top";
			}
			else
			{
				side = "bottom";
			}
		}
		else
		{
			if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
			{
				side = "left";
			}
			else
			{
				side = "right";
			}
		}

		appendPen(
			styleBuffer,
			line.getLinePen(),
			side
			);

		writeStyle(styleBuffer);

		finishStartCell();

		endCell();
	}
	
	protected void writeGenericElement(JRGenericPrintElement element, TableVisitor tableVisitor) throws IOException
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
			
			writeEmptyCell(tableVisitor.getCellColSpan(), tableVisitor.getCellRowSpan());
		}
		else
		{
			startCell(element, tableVisitor);

			StringBuilder styleBuffer = new StringBuilder();
			appendBackcolorStyle(tableVisitor, styleBuffer);
			appendBorderStyle(tableVisitor.getCellBox(), styleBuffer);
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
	
	protected void writeLayers(List<Table> layers, TableVisitor tableVisitor) throws IOException
	{
		int colSpan = tableVisitor.getCellColSpan();
		int rowSpan = tableVisitor.getCellRowSpan();
		startCell(colSpan, rowSpan);

		StringBuilder styleBuffer = new StringBuilder();
		appendBackcolorStyle(tableVisitor, styleBuffer);
		appendBorderStyle(tableVisitor.getCellBox(), styleBuffer);
		writeStyle(styleBuffer);

		finishStartCell();
		
		writer.write("<div style=\"width: 100%; height: 100%;\">\n");
		
		for (Iterator<Table> it = layers.iterator(); it.hasNext();)
		{
			Table table = it.next();
			
			StringBuilder layerStyleBuffer = new StringBuilder();
			if (it.hasNext()) {
				layerStyleBuffer.append("position: absolute; overflow: hidden;");
			} else {
				layerStyleBuffer.append("position: relative;");
			}

			writer.write("<div style=\"");
			writer.write(layerStyleBuffer.toString());
			writer.write("\">\n");

			exportTable(tableVisitor.tabulator, table, false);
			writer.write("</div>\n");
		}
		
		writer.write("</div>\n");

		endCell();
	}

	protected void startCell(JRPrintElement element, TableVisitor tableVisitor) throws IOException
	{
		int colSpan = tableVisitor.getCellColSpan();
		int rowSpan = tableVisitor.getCellRowSpan();
		startCell(colSpan, rowSpan);

		if (element != null)
		{
			String id = getPropertiesUtil().getProperty(element, JRHtmlExporter.PROPERTY_HTML_ID);
			if (id != null)
			{
				writer.write(" id=\"" + id +"\"");
			}
			String clazz = getPropertiesUtil().getProperty(element, JRHtmlExporter.PROPERTY_HTML_CLASS);
			if (clazz != null)
			{
				writer.write(" class=\"" + clazz +"\"");
			}
		}
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
	
	protected void writeFrameCell(TableVisitor tableVisitor) throws IOException
	{
		startCell(1, 1);
		
		StringBuilder styleBuffer = new StringBuilder();
		appendBackcolorStyle(tableVisitor, styleBuffer);
		appendBorderStyle(tableVisitor.getCellBox(), styleBuffer);
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

	protected void setBackcolor(Color color)
	{
		backcolorStack.addFirst(color);
	}

	protected void restoreBackcolor()
	{
		backcolorStack.removeFirst();
	}

	protected Color appendBackcolorStyle(TableVisitor tableVisitor, StringBuilder styleBuffer)
	{
		Color cellBackcolor = tableVisitor.getCellBackcolor();
		if (cellBackcolor != null && (backcolorStack.isEmpty() || cellBackcolor.getRGB() != backcolorStack.getFirst().getRGB()))
		{
			styleBuffer.append("background-color: #");
			styleBuffer.append(JRColorUtil.getColorHexa(cellBackcolor));
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
					tpc.equals(box.getLeftPen().getLineColor()) &&			// same line color
					tpc.equals(box.getBottomPen().getLineColor()) &&
					tpc.equals(box.getRightPen().getLineColor())) 
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
	
	protected boolean appendPen(StringBuilder sb, JRPen pen, String side)
	{
		boolean addedToStyle = false;
		
		float borderWidth = pen.getLineWidth().floatValue();
		if (0f < borderWidth && borderWidth < 1f)
		{
			borderWidth = 1f;
		}

		String borderStyle = null;
		switch (pen.getLineStyleValue())
		{
			case DOUBLE :
			{
				borderStyle = "double";
				break;
			}
			case DOTTED :
			{
				borderStyle = "dotted";
				break;
			}
			case DASHED :
			{
				borderStyle = "dashed";
				break;
			}
			case SOLID :
			default :
			{
				borderStyle = "solid";
				break;
			}
		}

		if (borderWidth > 0f)
		{
			sb.append("border");
			if (side != null)
			{
				sb.append("-");
				sb.append(side);
			}

			sb.append(": ");
			sb.append(toSizeUnit((int)borderWidth));
			
			sb.append(" ");
			sb.append(borderStyle);

			sb.append(" #");
			sb.append(JRColorUtil.getColorHexa(pen.getLineColor()));
			sb.append("; ");

			addedToStyle = true;
		}

		return addedToStyle;
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
			if (tp == lp && tp == bp && tp == rp)
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
	
	protected boolean appendPadding(StringBuilder sb, Integer padding, String side)
	{
		boolean addedToStyle = false;
		
		if (padding.intValue() > 0)
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
		
		boolean hyperlinkStarted = href != null;
		return hyperlinkStarted;
	}

	protected void endHyperlink() throws IOException
	{
		writer.write("</a>");
	}

	protected String getHyperlinkURL(JRPrintHyperlink link)
	{
		String href = null;
		
		Boolean hyperlinkVisible = HyperlinkUtil.getHyperlinkVisible(PROPERTY_HTML_HYPERLINK_VISIBLE, link);
		if (hyperlinkVisible == null)
		{
			hyperlinkVisible = JRPropertiesUtil.getInstance(jasperReportsContext).getBooleanProperty(jasperPrint, PROPERTY_HTML_HYPERLINK_VISIBLE, true);
		}

		if (hyperlinkVisible)
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

	protected String toSizeUnit(int size)
	{
		return String.valueOf(toZoom(size)) + sizeUnit;
	}

	protected int toZoom(int size)
	{
		return (int) (zoom * size);
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
				styledText = styledText.cloneText();
				styledText.insert("\n", lineBreakOffsets);
			}
		}
		
		return styledText;
	}
	
	protected void exportStyledText(JRStyledText styledText, String tooltip, Locale locale, 
			LineSpacingEnum lineSpacing, Float lineSpacingSize, boolean hyperlinkStarted) throws IOException
	{
		String text = styledText.getText();

		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		boolean first = true;
		boolean startedSpan = false;
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
			
			exportStyledTextRun(
				iterator.getAttributes(), 
				text.substring(iterator.getIndex(), runLimit),
				tooltip,
				locale,
				lineSpacing,
				lineSpacingSize,
				hyperlinkStarted
				);

			iterator.setIndex(runLimit);
		}
		
		if (startedSpan)
		{
			writer.write("</span>");
		}
	}
	
	protected void exportStyledTextRun(
			Map<Attribute,Object> attributes, 
			String text,
			String tooltip,
			Locale locale,
			LineSpacingEnum lineSpacing,
			Float lineSpacingSize, 
			boolean hyperlinkStarted
			) throws IOException
	{
		String fontFamilyAttr = (String)attributes.get(TextAttribute.FAMILY);
		String fontFamily = fontFamilyAttr;
		if (fontMap != null && fontMap.containsKey(fontFamilyAttr))
		{
			fontFamily = fontMap.get(fontFamilyAttr);
		}
		else
		{
			FontInfo fontInfo = FontUtil.getInstance(jasperReportsContext).getFontInfo(fontFamilyAttr, locale);
			if (fontInfo != null)
			{
				//fontName found in font extensions
				FontFamily family = fontInfo.getFontFamily();
				String exportFont = family.getExportFont(getExporterKey());
				if (exportFont != null)
				{
					fontFamily = exportFont;
				}
			}
		}
			
		boolean localHyperlink = false;
		JRPrintHyperlink hyperlink = (JRPrintHyperlink)attributes.get(JRTextAttribute.HYPERLINK);
		if (!hyperlinkStarted && hyperlink != null)
		{
			localHyperlink = startHyperlink(hyperlink);
		}
			
		writer.write("<span style=\"font-family: ");
		writer.write(fontFamily);
		writer.write("; ");

		Color forecolor = (Color)attributes.get(TextAttribute.FOREGROUND);
		if (!hyperlinkStarted || !Color.black.equals(forecolor))
		{
			writer.write("color: #");
			writer.write(JRColorUtil.getColorHexa(forecolor));
			writer.write("; ");
		}

		Color runBackcolor = (Color)attributes.get(TextAttribute.BACKGROUND);
		if (runBackcolor != null)
		{
			writer.write("background-color: #");
			writer.write(JRColorUtil.getColorHexa(runBackcolor));
			writer.write("; ");
		}

		writer.write("font-size: ");
		writer.write(toSizeUnit(((Float)attributes.get(TextAttribute.SIZE)).intValue()));
		writer.write(";");
			
		switch (lineSpacing)
		{
			case SINGLE:
			default:
			{
				writer.write(" line-height: 1; *line-height: normal;");
				break;
			}
			case ONE_AND_HALF:
			{
				writer.write(" line-height: 1.5;");
				break;
			}
			case DOUBLE:
			{
				writer.write(" line-height: 2.0;");
				break;
			}
			case PROPORTIONAL:
			{
				if (lineSpacingSize != null) {
					writer.write(" line-height: " + lineSpacingSize.floatValue() + ";");
				}
				break;
			}
			case AT_LEAST:
			case FIXED:
			{
				if (lineSpacingSize != null) {
					writer.write(" line-height: " + lineSpacingSize.floatValue() + "px;");
				}
				break;
			}
		}

		/*
		if (!horizontalAlignment.equals(CSS_TEXT_ALIGN_LEFT))
		{
			writer.write(" text-align: ");
			writer.write(horizontalAlignment);
			writer.write(";");
		}
		*/

		if (TextAttribute.WEIGHT_BOLD.equals(attributes.get(TextAttribute.WEIGHT)))
		{
			writer.write(" font-weight: bold;");
		}
		if (TextAttribute.POSTURE_OBLIQUE.equals(attributes.get(TextAttribute.POSTURE)))
		{
			writer.write(" font-style: italic;");
		}
		if (TextAttribute.UNDERLINE_ON.equals(attributes.get(TextAttribute.UNDERLINE)))
		{
			writer.write(" text-decoration: underline;");
		}
		if (TextAttribute.STRIKETHROUGH_ON.equals(attributes.get(TextAttribute.STRIKETHROUGH)))
		{
			writer.write(" text-decoration: line-through;");
		}

		if (TextAttribute.SUPERSCRIPT_SUPER.equals(attributes.get(TextAttribute.SUPERSCRIPT)))
		{
			writer.write(" vertical-align: super;");
		}
		else if (TextAttribute.SUPERSCRIPT_SUB.equals(attributes.get(TextAttribute.SUPERSCRIPT)))
		{
			writer.write(" vertical-align: sub;");
		}
			
		writer.write("\"");

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
	
	protected class TableVisitor implements CellVisitor<Pair<Column, Row>, Void, IOException>, PrintElementVisitor<Void>
	{
		private final Tabulator tabulator;
		private final Table table;
		
		private Row currentRow;
		private Column currentColumn;
		
		private Cell currentCell;
		private JRPrintElement currentElement;
		private Color currentBackcolor;
		private JRLineBox currentBox;

		public TableVisitor(Tabulator tabulator, Table table)
		{
			this.tabulator = tabulator;
			this.table = table;
		}

		protected int getCellColSpan()
		{
			return tabulator.getColumnCellSpan(table, currentColumn, currentRow, currentCell);
		}
		
		protected int getCellRowSpan()
		{
			return tabulator.getRowCellSpan(table, currentColumn, currentRow, currentCell);
		}
		
		@Override
		public Void visit(ElementCell cell, Pair<Column, Row> position)
		{
			currentColumn = position.first();
			currentRow = position.second();
			currentCell = cell;
			currentElement = tabulator.getCellElement(cell);
			currentBackcolor = getElementBackcolor(cell);
			
			JRLineBox elementBox = (currentElement instanceof JRBoxContainer) ? ((JRBoxContainer) currentElement).getLineBox() : null;
			currentBox = copyParentBox(cell, currentElement, elementBox, true, true, true, true);
			
			currentElement.accept(this, null);
			return null;
		}
		
		protected Color getElementBackcolor(BaseElementCell cell)
		{
			if (cell == null)
			{
				return null;
			}
			
			JRPrintElement element = tabulator.getCellElement(cell);
			if (element.getModeValue() == ModeEnum.OPAQUE)
			{
				return element.getBackcolor();
			}
			
			return getElementBackcolor(cell.getParent());
		}
		
		protected JRLineBox copyParentBox(Cell cell, JRPrintElement element, JRLineBox baseBox, 
				boolean keepLeft, boolean keepRight, boolean keepTop, boolean keepBottom)
		{
			FrameCell parentCell = cell.getParent();
			if (parentCell == null)
			{
				return baseBox;
			}
			
			// TODO lucianc check this in the table instead?
			JRPrintFrame parentFrame = (JRPrintFrame) tabulator.getCellElement(parentCell);
			keepLeft &= element.getX() == 0;
			keepRight &= (element.getX() + element.getWidth()) == parentFrame.getWidth();
			keepTop &= element.getY() == 0;
			keepBottom &= (element.getY() + element.getHeight()) == parentFrame.getHeight();
			
			JRLineBox resultBox = baseBox;
			if (keepLeft || keepRight || keepTop || keepBottom)
			{
				resultBox = copyFrameBox(parentCell, parentFrame, baseBox, 
						keepLeft, keepRight, keepTop, keepBottom);
			}
			return resultBox;
		}

		protected JRLineBox copyFrameBox(FrameCell frameCell, JRPrintFrame frame, JRLineBox baseBox, 
				boolean keepLeft, boolean keepRight, boolean keepTop, boolean keepBottom)
		{
			JRLineBox resultBox = JRBoxUtil.copyBordersNoPadding(frame.getLineBox(), 
					keepLeft, keepRight, keepTop, keepBottom, baseBox);
			// recurse
			resultBox = copyParentBox(frameCell, frame, resultBox, keepLeft, keepRight, keepTop, keepBottom);
			return resultBox;
		}

		@Override
		public Void visit(SplitCell cell, Pair<Column, Row> position)
		{
			//NOP
			return null;
		}

		@Override
		public Void visit(FrameCell frameCell, Pair<Column, Row> position) throws IOException
		{
			currentColumn = position.first();
			currentRow = position.second();
			currentCell = frameCell;
			currentElement = tabulator.getCellElement(frameCell);
			currentBackcolor = getElementBackcolor(frameCell);
			
			boolean[] borders = tabulator.getFrameCellBorders(table, currentColumn, currentRow, frameCell);
			currentBox = copyFrameBox(frameCell, (JRPrintFrame) currentElement, null, borders[0], borders[1], borders[2], borders[3]);

			HtmlExporter.this.writeFrameCell(this);
			return null;
		}

		@Override
		public Void visit(LayeredCell layeredCell, Pair<Column, Row> position)
				throws IOException
		{
			currentColumn = position.first();
			currentRow = position.second();
			currentCell = layeredCell;
			currentElement = null;
			currentBackcolor = getElementBackcolor(layeredCell.getParent());
			
			FrameCell parentCell = layeredCell.getParent();
			if (parentCell == null)
			{
				currentBox = null;
			}
			else
			{
				boolean[] borders = tabulator.getFrameCellBorders(table, currentColumn, currentRow, parentCell);
				if (borders[0] || borders[1] || borders[2] || borders[3])
				{
					JRPrintFrame parentFrame = (JRPrintFrame) tabulator.getCellElement(parentCell);
					currentBox = copyFrameBox(parentCell, parentFrame, null, borders[0], borders[1], borders[2], borders[3]);
				}
			}
			
			HtmlExporter.this.writeLayers(layeredCell.getLayers(), this);
			return null;
		}

		public String getElementAddress()
		{
			StringBuilder address = new StringBuilder();
			writeElementAddress(address, (BaseElementCell) currentCell);
			return address.toString();
		}

		protected void writeElementAddress(StringBuilder output,
				BaseElementCell cell)
		{
			FrameCell parent = cell.getParent();
			if (parent != null)
			{
				writeElementAddress(output, parent);
				output.append('_');
			}
			output.append(cell.getElementIndex());
		}
		
		public Color getCellBackcolor()
		{
			return currentBackcolor;
		}
		
		public JRLineBox getCellBox()
		{
			return currentBox;
		}

		@Override
		public void visit(JRPrintText textElement, Void arg)
		{
			try
			{
				writeText(textElement, this);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}

		@Override
		public void visit(JRPrintImage image, Void arg)
		{
			try
			{
				writeImage(image, this);
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
		public void visit(JRPrintRectangle rectangle, Void arg)
		{
			try
			{
				writeRectangle(rectangle, this);
			} 
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}

		@Override
		public void visit(JRPrintLine line, Void arg)
		{
			try
			{
				writeLine(line, this);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}

		@Override
		public void visit(JRPrintEllipse ellipse, Void arg)
		{
			try
			{
				writeRectangle(ellipse, this);
			} 
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}

		@Override
		public void visit(JRPrintFrame frame, Void arg)
		{
			throw new JRRuntimeException("Internal error");
		}

		@Override
		public void visit(JRGenericPrintElement printElement, Void arg)
		{
			try
			{
				writeGenericElement(printElement, this);
			} 
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}
	}

	protected class ExporterContext extends BaseExporterContext implements JRHtmlExporterContext
	{
		public String getExportPropertiesPrefix()
		{
			return HTML_EXPORTER_PROPERTIES_PREFIX;
		}

		public String getHyperlinkURL(JRPrintHyperlink link)
		{
			return HtmlExporter.this.getHyperlinkURL(link);
		}
	}

}

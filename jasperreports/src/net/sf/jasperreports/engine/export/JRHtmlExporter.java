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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageMapRenderer;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintElementIndex;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintImageArea;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.Pair;


/**
 * Exports a JasperReports document to HTML format. It has character output type and exports the document to a
 * grid-based layout.
 * <p>
 * Since classic AWT fonts can be sometimes very different from HTML fonts, a font mapping feature was added.
 * By using the {@link JRExporterParameter#FONT_MAP} parameter, a logical font like "sansserif" can be mapped to a
 * list of HTML specific fonts, like "Arial, Verdana, Tahoma". Both map keys and values are strings.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRHtmlExporter extends JRAbstractExporter
{

	/**
	 * @deprecated Replaced by  {@link JRHtmlExporterParameter#PROPERTY_FRAMES_AS_NESTED_TABLES}.
	 */
	public static final String PROPERTY_FRAMES_AS_NESTED_TABLES = JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES;
	
	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";

	/**
	 *
	 */
	protected static final String CSS_TEXT_ALIGN_LEFT = "left";
	protected static final String CSS_TEXT_ALIGN_RIGHT = "right";
	protected static final String CSS_TEXT_ALIGN_CENTER = "center";
	protected static final String CSS_TEXT_ALIGN_JUSTIFY = "justify";

	/**
	 *
	 */
	protected static final String HTML_VERTICAL_ALIGN_TOP = "top";
	protected static final String HTML_VERTICAL_ALIGN_MIDDLE = "middle";
	protected static final String HTML_VERTICAL_ALIGN_BOTTOM = "bottom";
	
	public static final String IMAGE_NAME_PREFIX = "img_";
	protected static final int IMAGE_NAME_PREFIX_LEGTH = IMAGE_NAME_PREFIX.length();

	/**
	 *
	 */
	protected Writer writer = null;
	protected JRExportProgressMonitor progressMonitor = null;
	protected Map rendererToImagePathMap = null;
	protected Map imageMaps;
	protected Map imageNameToImageDataMap = null;
	protected List imagesToProcess = null;
	protected boolean isPxImageLoaded = false;

	protected int reportIndex = 0;
	protected int pageIndex = 0;

	/**
	 *
	 */
	protected File imagesDir = null;
	protected String imagesURI = null;
	protected boolean isOutputImagesToDir = false;
	protected boolean isRemoveEmptySpace;
	protected boolean isWhitePageBackground;
	protected String encoding;
	protected String sizeUnit = null;
	protected boolean isUsingImagesToAlign;
	protected boolean isWrapBreakWord;

	/**
	 *
	 */
	protected String htmlHeader = null;
	protected String betweenPagesHtml = null;
	protected String htmlFooter = null;

	protected StringProvider emptyCellStringProvider = null;


	/**
	 *
	 */
	protected Map fontMap = null;

	private LinkedList backcolorStack;
	private Color backcolor;

	protected JRHyperlinkProducerFactory hyperlinkProducerFactory;
	
	protected boolean deepGrid;


	public JRHtmlExporter()
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
	
			isRemoveEmptySpace = 
				getBooleanParameter(
					JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					false
					);
	
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
	
			rendererToImagePathMap = new HashMap();
			imageMaps = new HashMap();
			imagesToProcess = new ArrayList();
			isPxImageLoaded = false;
	
			//backward compatibility with the IMAGE_MAP parameter
			imageNameToImageDataMap = (Map)parameters.get(JRHtmlExporterParameter.IMAGES_MAP);
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
	
			isUsingImagesToAlign = 
				getBooleanParameter(
					JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
					JRHtmlExporterParameter.PROPERTY_USING_IMAGES_TO_ALIGN,
					true
					);
		
			if (isUsingImagesToAlign)
			{
				emptyCellStringProvider =
					new StringProvider()
					{
						public String getStringForCollapsedTD(Object value, int width, int height, String sizeUnit)
						{
							return "><img alt=\"\" src=\"" + value + "px\" style=\"width: " + width + sizeUnit + "; height: " + height + sizeUnit + ";\"/>";
						}
						public String getStringForEmptyTD(Object value)
						{
							return "<img alt=\"\" src=\"" + value + "px\" border=\"0\"/>";
						}
					};

				loadPxImage();
			}
			else
			{
				emptyCellStringProvider =
					new StringProvider()
					{
						public String getStringForCollapsedTD(Object value, int width, int height, String sizeUnit)
						{
							return " style=\"width: " + width + sizeUnit + "; height: " + height + sizeUnit + ";\">";
						}
						public String getStringForEmptyTD(Object value)
						{
							return "";
						}
					};
			}
	
	
			fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);
						
			setHyperlinkProducerFactory();
			setDeepGrid();
	
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
	
				if (isPxImageLoaded || (imagesToProcess != null && imagesToProcess.size() > 0))
				{
					if (!imagesDir.exists())
					{
						imagesDir.mkdir();
					}
	
					if (isPxImageLoaded)
					{
						JRRenderable pxRenderer =
							JRImageRenderer.getInstance("net/sf/jasperreports/engine/images/pixel.GIF");
						byte[] imageData = pxRenderer.getImageData();
	
						File imageFile = new File(imagesDir, "px");
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
	
						byte[] imageData = renderer.getImageData();
	
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


	protected void setHyperlinkProducerFactory()
	{
		hyperlinkProducerFactory = (JRHyperlinkProducerFactory) parameters.get(JRExporterParameter.HYPERLINK_PRODUCER_FACTORY);
	}
	
	
	protected void setDeepGrid()
	{
		boolean nestedTables;
		Boolean nestedTablesParam = (Boolean) parameters.get(JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES);
		if (nestedTablesParam == null)
		{
			nestedTables = JRProperties.getBooleanProperty(PROPERTY_FRAMES_AS_NESTED_TABLES);
		}
		else
		{
			nestedTables = nestedTablesParam.booleanValue();
		}
		
		deepGrid = !nestedTables;
	}


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

					writer.write("<a name=\"" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1) + "\"/>\n");

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

		writer.flush();
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, IOException
	{
		JRGridLayout layout = 
			new JRGridLayout(
				JRHtmlExporterNature.getInstance(deepGrid),
				page.getElements(),
				jasperPrint.getPageWidth(), 
				jasperPrint.getPageHeight(),
				globalOffsetX, 
				globalOffsetY, 
				null //address
				);

		exportGrid(layout, isWhitePageBackground);

		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}
	

	/**
	 *
	 */
	protected void exportGrid(JRGridLayout gridLayout, boolean whitePageBackground) throws IOException, JRException
	{
		List xCuts = gridLayout.getXCuts();
		JRExporterGridCell[][] grid = gridLayout.getGrid();

		writer.write("<table style=\"width: " + gridLayout.getWidth() + sizeUnit + "\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"");
		if (whitePageBackground)
		{
			writer.write(" bgcolor=\"white\"");
		}
		writer.write(">\n");

		if (whitePageBackground)
		{
			setBackcolor(Color.white);
		}

		writer.write("<tr>\n");
		int width = 0;
		for(int i = 1; i < xCuts.size(); i++)
		{
			width = ((Integer)xCuts.get(i)).intValue() - ((Integer)xCuts.get(i - 1)).intValue();
			writer.write("  <td" + emptyCellStringProvider.getStringForCollapsedTD(imagesURI, width, 1, sizeUnit) + "</td>\n");
		}
		writer.write("</tr>\n");

		for(int y = 0; y < grid.length; y++)
		{
			if (gridLayout.isRowSpanned(y) || !isRemoveEmptySpace)
			{
				JRExporterGridCell[] gridRow = grid[y];
				
				int rowHeight = JRGridLayout.getRowHeight(gridRow);
				
				boolean hasEmptyCell = hasEmptyCell(gridRow);
				
				writer.write("<tr valign=\"top\"");
				if (!hasEmptyCell)
				{
					writer.write(" style=\"height:" + rowHeight + sizeUnit + "\"");
				}
				writer.write(">\n");

				for(int x = 0; x < gridRow.length; x++)
				{
					JRExporterGridCell gridCell = gridRow[x];
					if(gridCell.getWrapper() == null)
					{
						writeEmptyCell(gridCell, rowHeight);						
					}
					else
					{
						JRPrintElement element = gridCell.getWrapper().getElement();

						if (element instanceof JRPrintLine)
						{
							exportLine((JRPrintLine)element, gridCell);
						}
						else if (element instanceof JRPrintRectangle)
						{
							exportRectangle(element, gridCell);
						}
						else if (element instanceof JRPrintEllipse)
						{
							exportRectangle(element, gridCell);
						}
						else if (element instanceof JRPrintImage)
						{
							exportImage((JRPrintImage)element, gridCell);
						}
						else if (element instanceof JRPrintText)
						{
							exportText((JRPrintText)element, gridCell);
						}
						else if (element instanceof JRPrintFrame)
						{
							exportFrame((JRPrintFrame) element, gridCell);
						}
					}

					x += gridCell.getColSpan() - 1;
				}

				writer.write("</tr>\n");
			}
		}

		if (whitePageBackground)
		{
			restoreBackcolor();
		}

		writer.write("</table>\n");
	}


	private boolean hasEmptyCell(JRExporterGridCell[] gridRow)
	{
		if (gridRow[0].getWrapper() == null) // quick exit
		{
			return true;
		}
		
		boolean hasEmptyCell = false;
		for(int x = 1; x < gridRow.length; x++)
		{
			if (gridRow[x].getWrapper() == null)
			{
				hasEmptyCell = true;
				break;
			}
		}

		return hasEmptyCell;
	}


	protected void writeEmptyCell(JRExporterGridCell cell, int rowHeight) throws IOException
	{
		writer.write("  <td");
		if (cell.getColSpan() > 1)
		{
			writer.write(" colspan=\"" + cell.getColSpan() + "\"");
		}

		StringBuffer styleBuffer = new StringBuffer();
		appendBackcolorStyle(cell, styleBuffer);
		appendBorderStyle(cell.getBox(), styleBuffer);

		if (styleBuffer.length() > 0)
		{
			writer.write(" style=\"");
			writer.write(styleBuffer.toString());
			writer.write("\"");
		}

		writer.write(emptyCellStringProvider.getStringForCollapsedTD(imagesURI, cell.getWidth(), rowHeight, sizeUnit));
		writer.write("</td>\n");
	}


	/**
	 *
	 */
	protected void exportLine(JRPrintLine line, JRExporterGridCell gridCell) throws IOException
	{
		writeCellTDStart(gridCell);

		if (
			line.getForecolor().getRGB() != Color.white.getRGB()
			)
		{
			writer.write(" bgcolor=\"#");
			writer.write(JRColorUtil.getColorHexa(line.getForecolor()));
			writer.write("\"");
		}

		writer.write(">");

		writer.write(emptyCellStringProvider.getStringForEmptyTD(imagesURI));

		writer.write("</td>\n");
	}


	/**
	 *
	 */
	protected void writeCellTDStart(JRExporterGridCell gridCell) throws IOException
	{
		writer.write("  <td");
		if (gridCell.getColSpan() > 1)
		{
			writer.write(" colspan=\"" + gridCell.getColSpan() +"\"");
		}
		if (gridCell.getRowSpan() > 1)
		{
			writer.write(" rowspan=\"" + gridCell.getRowSpan() + "\"");
		}
	}


	/**
	 *
	 */
	protected void exportRectangle(JRPrintElement element, JRExporterGridCell gridCell) throws IOException
	{
		writeCellTDStart(gridCell);

		if (
			(backcolor == null || element.getBackcolor().getRGB() != backcolor.getRGB())
			&& element.getMode() == JRElement.MODE_OPAQUE
			)
		{
			writer.write(" bgcolor=\"#");
			writer.write(JRColorUtil.getColorHexa(element.getBackcolor()));
			writer.write("\"");
		}

		writer.write(">");

		writer.write(emptyCellStringProvider.getStringForEmptyTD(imagesURI));

		writer.write("</td>\n");
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
		writer.write("<span style=\"font-family: ");
		writer.write(fontFamily);
		writer.write("; ");

		Color forecolor = (Color)attributes.get(TextAttribute.FOREGROUND);
		if (!Color.black.equals(forecolor))
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
		writer.write(String.valueOf(attributes.get(TextAttribute.SIZE)));
		writer.write(sizeUnit);
		writer.write(";");

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

		writer.write("\">");

		writer.write(
			JRStringUtil.htmlEncode(text)
			);

		writer.write("</span>");
	}


	/**
	 *
	 */
	protected void exportText(JRPrintText text, JRExporterGridCell gridCell) throws IOException
	{
		JRStyledText styledText = getStyledText(text);

		int textLength = 0;

		if (styledText != null)
		{
			textLength = styledText.length();
		}

		writeCellTDStart(gridCell);//FIXME why dealing with cell style if no text to print (textLength == 0)?

		String verticalAlignment = HTML_VERTICAL_ALIGN_TOP;

		switch (text.getVerticalAlignment())
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

		if (text.getRunDirection() == JRPrintText.RUN_DIRECTION_RTL)
		{
			writer.write(" dir=\"rtl\"");
		}

		StringBuffer styleBuffer = new StringBuffer();
		appendBackcolorStyle(gridCell, styleBuffer);
		appendBorderStyle(gridCell.getBox(), styleBuffer);

		String horizontalAlignment = CSS_TEXT_ALIGN_LEFT;

		if (textLength > 0)
		{
			switch (text.getHorizontalAlignment())
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
				case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
				{
					horizontalAlignment = CSS_TEXT_ALIGN_JUSTIFY;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_LEFT :
				default :
				{
					horizontalAlignment = CSS_TEXT_ALIGN_LEFT;
				}
			}

			if (
				(text.getRunDirection() == JRPrintText.RUN_DIRECTION_LTR
				 && !horizontalAlignment.equals(CSS_TEXT_ALIGN_LEFT))
				|| (text.getRunDirection() == JRPrintText.RUN_DIRECTION_RTL
					&& !horizontalAlignment.equals(CSS_TEXT_ALIGN_RIGHT))
				)
			{
				styleBuffer.append("text-align: ");
				styleBuffer.append(horizontalAlignment);
				styleBuffer.append(";");
			}
		}

		if (isWrapBreakWord)
		{
			styleBuffer.append("width: " + gridCell.getWidth() + sizeUnit + "; ");
			styleBuffer.append("word-wrap: break-word; ");
		}
		
		if (text.getLineSpacing() != JRTextElement.LINE_SPACING_SINGLE)
		{
			styleBuffer.append("line-height: " + text.getLineSpacingFactor() + "; ");
		}

		if (styleBuffer.length() > 0)
		{
			writer.write(" style=\"");
			writer.write(styleBuffer.toString());
			writer.write("\"");
		}

		writer.write(">");

		if (text.getAnchorName() != null)
		{
			writer.write("<a name=\"");
			writer.write(text.getAnchorName());
			writer.write("\"/>");
		}

		boolean startedHyperlink = startHyperlink(text);

		if (textLength > 0)
		{
			exportStyledText(styledText);
		}
		else
		{
			writer.write(emptyCellStringProvider.getStringForEmptyTD(imagesURI));
		}

		if (startedHyperlink)
		{
			endHyperlink();
		}

		writer.write("</td>\n");
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


	protected void appendBorderStyle(JRBox box, StringBuffer styleBuffer)
	{
		if (box != null)
		{
			appendBorder(
				styleBuffer,
				box.getTopBorder(),
				box.getTopBorderColor(),
				box.getTopPadding(),
				"top"
				);
			appendBorder(
				styleBuffer,
				box.getLeftBorder(),
				box.getLeftBorderColor(),
				box.getLeftPadding(),
				"left"
				);
			appendBorder(
				styleBuffer,
				box.getBottomBorder(),
				box.getBottomBorderColor(),
				box.getBottomPadding(),
				"bottom"
				);
			appendBorder(
				styleBuffer,
				box.getRightBorder(),
				box.getRightBorderColor(),
				box.getRightPadding(),
				"right"
				);
		}
	}


	protected Color appendBackcolorStyle(JRExporterGridCell gridCell, StringBuffer styleBuffer)
	{
		Color cellBackcolor = gridCell.getCellBackcolor();
		if (cellBackcolor != null && (backcolor == null || cellBackcolor.getRGB() != backcolor.getRGB()))
		{
			styleBuffer.append("background-color: #");
			styleBuffer.append(JRColorUtil.getColorHexa(cellBackcolor));
			styleBuffer.append("; ");

			return backcolor;
		}

		return null;
	}


	/**
	 *
	 */
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
		appendBackcolorStyle(gridCell, styleBuffer);
		appendBorderStyle(gridCell.getBox(), styleBuffer);

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

		if(renderer != null || isUsingImagesToAlign)
		{
			startedHyperlink = !imageMapRenderer && startHyperlink(image);
			writer.write("<img");
			String imagePath = null;
			String imageMapName = null;
			List imageMapAreas = null;
	
			byte scaleImage = image.getScaleImage();
			if (renderer != null)
			{
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
						imagePath = imagesURI + imageName;
	
						//backward compatibility with the IMAGE_MAP parameter
						if (imageNameToImageDataMap != null)
						{
							if (renderer.getType() == JRRenderable.TYPE_SVG)
							{
								renderer =
									new JRWrappingSvgRenderer(
										renderer,
										new Dimension(image.getWidth(), image.getHeight()),
										JRElement.MODE_OPAQUE == image.getMode() ? image.getBackcolor() : null
										);
							}
							imageNameToImageDataMap.put(imageName, renderer.getImageData());
						}
						//END - backward compatibility with the IMAGE_MAP parameter
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
			}
			else 		// ie: 	if(isUsingImagesToAlign)
			{
				loadPxImage();
				imagePath = imagesURI + "px";
				scaleImage = JRImage.SCALE_IMAGE_FILL_FRAME;
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
					writer.write(sizeUnit);
					writer.write("; height: ");
					writer.write(String.valueOf(imageHeight));
					writer.write(sizeUnit);
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
		
					if (imageHeight > 0)
					{
						double ratio = normalWidth / normalHeight;
		
						if( ratio > (double)imageWidth / (double)imageHeight )
						{
							writer.write(" style=\"width: ");
							writer.write(String.valueOf(imageWidth));
							writer.write(sizeUnit);
							writer.write("\"");
						}
						else
						{
							writer.write(" style=\"height: ");
							writer.write(String.valueOf(imageHeight));
							writer.write(sizeUnit);
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
					gridCell.getWrapper().getAddress()
					);
		return imageIndex;
	}


	protected void writeImageMap(String imageMapName, JRPrintImage image, List imageMapAreas) throws IOException
	{
		writer.write("<map name=\"" + imageMapName + "\">\n");

		for (Iterator it = imageMapAreas.iterator(); it.hasNext();)
		{
			JRPrintImageAreaHyperlink areaHyperlink = (JRPrintImageAreaHyperlink) it.next();
			JRPrintImageArea area = areaHyperlink.getArea();

			writer.write("  <area shape=\"" + JRPrintImageArea.getHtmlShape(area.getShape()) + "\"");
			writeImageAreaCoordinates(area.getCoordinates());			
			writeImageAreaHyperlink(areaHyperlink.getHyperlink());
			writer.write("/>\n");
		}
		
		if (image.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
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
	protected void loadPxImage() throws JRException
	{
		isPxImageLoaded = true;
		//backward compatibility with the IMAGE_MAP parameter
		if (imageNameToImageDataMap != null && !imageNameToImageDataMap.containsKey("px"))
		{
			JRRenderable pxRenderer =
				JRImageRenderer.getInstance("net/sf/jasperreports/engine/images/pixel.GIF");
			rendererToImagePathMap.put(pxRenderer.getId(), imagesURI + "px");
			imageNameToImageDataMap.put("px", pxRenderer.getImageData());
		}
		//END - backward compatibility with the IMAGE_MAP parameter
	}


	/**
	 *
	 */
	protected static interface StringProvider
	{

		/**
		 *
		 */
		public String getStringForCollapsedTD(Object value, int width, int height, String sizeUnit);

		/**
		 *
		 */
		public String getStringForEmptyTD(Object value);

	}


	/**
	 *
	 */
	private void appendBorder(StringBuffer sb, byte pen, Color borderColor, int padding, String side)
	{
		String borderStyle = null;
		String borderWidth = null;

		switch (pen)
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				borderStyle = "dashed";
				borderWidth = "1";
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				borderStyle = "solid";
				borderWidth = "4";
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				borderStyle = "solid";
				borderWidth = "2";
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				borderStyle = "solid";
				borderWidth = "1";
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				break;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				borderStyle = "solid";
				borderWidth = "1";
				break;
			}
		}

		if (borderWidth != null)
		{
			sb.append("border-");
			sb.append(side);
			sb.append("-style: ");
			sb.append(borderStyle);
			sb.append("; ");

			sb.append("border-");
			sb.append(side);
			sb.append("-width: ");
			sb.append(borderWidth);
			sb.append(sizeUnit);
			sb.append("; ");

			sb.append("border-");
			sb.append(side);
			sb.append("-color: #");
			sb.append(JRColorUtil.getColorHexa(borderColor));
			sb.append("; ");
		}

		if (padding > 0)
		{
			sb.append("padding-");
			sb.append(side);
			sb.append(": ");
			sb.append(padding);
			sb.append(sizeUnit);
			sb.append("; ");
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


	protected void exportFrame(JRPrintFrame frame, JRExporterGridCell gridCell) throws IOException, JRException
	{
		writeCellTDStart(gridCell);

		StringBuffer styleBuffer = new StringBuffer();
		Color frameBackcolor = appendBackcolorStyle(gridCell, styleBuffer);
		appendBorderStyle(gridCell.getBox(), styleBuffer);

		if (styleBuffer.length() > 0)
		{
			writer.write(" style=\"");
			writer.write(styleBuffer.toString());
			writer.write("\"");
		}

		writer.write(">\n");

		if (frameBackcolor != null)
		{
			setBackcolor(frameBackcolor);
		}
		try
		{
			exportGrid(gridCell.getLayout(), false);
		}
		finally
		{
			if (frameBackcolor != null)
			{
				restoreBackcolor();
			}
		}

		writer.write("</td>\n");
	}


	protected void setBackcolor(Color color)
	{
		backcolorStack.addLast(backcolor);

		backcolor = color;
	}


	protected void restoreBackcolor()
	{
		backcolor = (Color) backcolorStack.removeLast();
	}

}


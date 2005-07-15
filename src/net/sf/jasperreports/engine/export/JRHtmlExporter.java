/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;

import org.xml.sax.SAXException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRHtmlExporter extends JRAbstractExporter
{


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

	/**
	 *
	 */
	protected Writer writer = null;
	protected JRExportProgressMonitor progressMonitor = null;
	protected Map rendererToImagePathMap = null;
	protected Map imageNameToImageDataMap = null;

	protected int reportIndex = 0;

	/**
	 *
	 */
	protected JRFont defaultFont = null;

	/**
	 *
	 */
	protected JRStyledTextParser styledTextParser = new JRStyledTextParser();

	/**
	 *
	 */
	protected File imagesDir = null;
	protected String imagesURI = null;
	protected boolean isOutputImagesToDir = false;
	protected boolean isRemoveEmptySpace = false;
	protected boolean isWhitePageBackground = true;
	protected String encoding = null;

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
	protected static final int colorMask = Integer.parseInt("FFFFFF", 16);

	/**
	 *
	 */
	protected JRExporterGridCell grid[][] = null;
	protected boolean isRowNotEmpty[] = null;
	protected List xCuts = null;
	protected List yCuts = null;

	protected boolean isWrapBreakWord = false;

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

		Boolean isRemoveEmptySpaceParameter = (Boolean)parameters.get(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS);
		if (isRemoveEmptySpaceParameter != null)
		{
			isRemoveEmptySpace = isRemoveEmptySpaceParameter.booleanValue();
		}
		
		Boolean isWhitePageBackgroundParameter = (Boolean)parameters.get(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND);
		if (isWhitePageBackgroundParameter != null)
		{
			isWhitePageBackground = isWhitePageBackgroundParameter.booleanValue();
		}

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

		encoding = (String)parameters.get(JRExporterParameter.CHARACTER_ENCODING);
		if (encoding == null)
		{
			encoding = "UTF-8";
		}
		
		rendererToImagePathMap = new HashMap();

		imageNameToImageDataMap = (Map)parameters.get(JRHtmlExporterParameter.IMAGES_MAP);
		if (imageNameToImageDataMap == null)
		{
			imageNameToImageDataMap = new HashMap();
		}
		
		Boolean isWrapBreakWordParameter = (Boolean)parameters.get(JRHtmlExporterParameter.IS_WRAP_BREAK_WORD);
		if (isWrapBreakWordParameter != null)
		{
			isWrapBreakWord = isWrapBreakWordParameter.booleanValue();
		}
		
		Boolean isUsingImagesToAlignParameter = (Boolean)parameters.get(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN);
		if (isUsingImagesToAlignParameter == null)
		{
			isUsingImagesToAlignParameter = Boolean.TRUE;
		}

		if (isUsingImagesToAlignParameter.booleanValue())
		{
			emptyCellStringProvider = 
				new StringProvider()
				{
					public String getStringForCollapsedTD(Object value)
					{
						return "><img src=\"" + value + "px\"";
					}
					public String getStringForEmptyTD(Object value)
					{
						return "<img src=\"" + value + "px\" border=0>";
					}
				};

			loadPxImage();
		}
		else
		{
			emptyCellStringProvider = 
				new StringProvider()
				{
					public String getStringForCollapsedTD(Object value)
					{
						return "";
					}
					public String getStringForEmptyTD(Object value)
					{
						return "";
					}
				};
		}
		

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

			Collection imageNames = imageNameToImageDataMap.keySet();
			if (imageNames != null && imageNames.size() > 0)
			{
				if (!imagesDir.exists())
				{
					imagesDir.mkdir();
				}
	
				for(Iterator it = imageNames.iterator(); it.hasNext();)
				{
					String imageName = (String)it.next();
					byte[] imageData = (byte[])imageNameToImageDataMap.get(imageName);

					File imageFile = new File(imagesDir, imageName);
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


	/**
	 *
	 */
	protected void exportReportToWriter() throws JRException, IOException
	{
		if (htmlHeader == null)
		{
			writer.write("<html>\n");
			writer.write("<head>\n");
			writer.write("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + encoding + "\">\n");
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
			defaultFont = null;

			List pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				if (isModeBatch)
				{
					startPageIndex = 0;
					endPageIndex = pages.size() - 1;
				}

				JRPrintPage page = null;
				for(int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
				{
					if (Thread.currentThread().isInterrupted())
					{
						throw new JRException("Current thread interrupted.");
					}
				
					page = (JRPrintPage)pages.get(pageIndex);

					writer.write("<a name=\"" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1) + "\"/>\n");

					/*   */
					exportPage(page);
				
					if (betweenPagesHtml == null)
					{
						writer.write("<br>\n<br>\n");
					}
					else
					{
						writer.write(betweenPagesHtml);
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
		writer.write("<table width=" + jasperPrint.getPageWidth() + " cellpadding=0 cellspacing=0 border=0\n");
		if (isWhitePageBackground)
		{
			writer.write(" bgcolor=white");
		}
		writer.write(">\n");

		layoutGrid(page);

		writer.write("<tr>\n");
		int width = 0;
		for(int i = 1; i < xCuts.size(); i++)
		{
			width = ((Integer)xCuts.get(i)).intValue() - ((Integer)xCuts.get(i - 1)).intValue();
			writer.write("  <td" + emptyCellStringProvider.getStringForCollapsedTD(imagesURI) + " width=" + width + " height=1></td>\n");
		}
		writer.write("</tr>\n");

		JRPrintElement element = null;
		for(int y = 0; y < grid.length; y++)
		{
			if (isRowNotEmpty[y] || !isRemoveEmptySpace)
			{
				writer.write("<tr valign=top>\n");
	
				int emptyCellColSpan = 0;
				int emptyCellWidth = 0;
				int lastRowHeight = grid[y][0].height;
	
				for(int x = 0; x < grid[y].length; x++)
				{
					if(grid[y][x].element != null)
					{
						if (emptyCellColSpan > 0)
						{
							writer.write("  <td");
							if (emptyCellColSpan > 1)
							{
								writer.write(" colspan=" + emptyCellColSpan);
							}
							writer.write(emptyCellStringProvider.getStringForCollapsedTD(imagesURI) + " width=" + emptyCellWidth + " height=" + lastRowHeight + "></td>\n");
							emptyCellColSpan = 0;
							emptyCellWidth = 0;
						}
	
						element = grid[y][x].element;
	
						if (element instanceof JRPrintLine)
						{
							exportLine((JRPrintLine)element, grid[y][x]);
						}
						else if (element instanceof JRPrintRectangle)
						{
							exportRectangle(element, grid[y][x]);
						}
						else if (element instanceof JRPrintEllipse)
						{
							exportRectangle(element, grid[y][x]);
						}
						else if (element instanceof JRPrintImage)
						{
							exportImage((JRPrintImage)element, grid[y][x]);
						}
						else if (element instanceof JRPrintText)
						{
							exportText((JRPrintText)element, grid[y][x]);
						}
	
						x += grid[y][x].colSpan - 1;
					}
					else
					{
						emptyCellColSpan++;
						emptyCellWidth += grid[y][x].width;
					}
				}
	
				if (emptyCellColSpan > 0)
				{
					writer.write("  <td");
					if (emptyCellColSpan > 1)
					{
						writer.write(" colspan=" + emptyCellColSpan);
					}
					writer.write(emptyCellStringProvider.getStringForCollapsedTD(imagesURI) + " width=" + emptyCellWidth + " height=" + lastRowHeight + "></td>\n");
				}
	
				writer.write("</tr>\n");
			}
		}

		writer.write("</table>\n");
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	/**
	 *
	 */
	protected void exportLine(JRPrintLine line, JRExporterGridCell gridCell) throws IOException
	{
		writer.write("  <td");
		if (gridCell.colSpan > 1)
		{
			writer.write(" colspan=" + gridCell.colSpan);
		}
		if (gridCell.rowSpan > 1)
		{
			writer.write(" rowspan=" + gridCell.rowSpan);
		}
		
		if (
			line.getForecolor().getRGB() != Color.white.getRGB() 
			)
		{
			writer.write(" bgcolor=#");
			String hexa = Integer.toHexString(line.getForecolor().getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			writer.write(hexa);
		}

		writer.write(">");

		writer.write(emptyCellStringProvider.getStringForEmptyTD(imagesURI));

		writer.write("</td>\n");
	}


	/**
	 *
	 */
	protected void exportRectangle(JRPrintElement element, JRExporterGridCell gridCell) throws IOException
	{
		writer.write("  <td");
		if (gridCell.colSpan > 1)
		{
			writer.write(" colspan=" + gridCell.colSpan);
		}
		if (gridCell.rowSpan > 1)
		{
			writer.write(" rowspan=" + gridCell.rowSpan);
		}
		
		if (
			element.getBackcolor().getRGB() != Color.white.getRGB() 
			&& element.getMode() == JRElement.MODE_OPAQUE
			)
		{
			writer.write(" bgcolor=#");
			String hexa = Integer.toHexString(element.getBackcolor().getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			writer.write(hexa);
		}

		writer.write(">");

		writer.write(emptyCellStringProvider.getStringForEmptyTD(imagesURI));

		writer.write("</td>\n");
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
			JRFont font = textElement.getFont();
			if (font == null)
			{
				font = getDefaultFont();
			}

			Map attributes = new HashMap(); 
			attributes.putAll(font.getAttributes());
			attributes.put(TextAttribute.FOREGROUND, textElement.getForecolor());
			if (textElement.getMode() == JRElement.MODE_OPAQUE)
			{
				attributes.put(TextAttribute.BACKGROUND, textElement.getBackcolor());
			}

			if (textElement.isStyledText())
			{
				try
				{
					styledText = styledTextParser.parse(attributes, text);
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
				styledText.addRun(new JRStyledText.Run(attributes, 0, text.length()));
			}
		}
		
		return styledText;
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
		writer.write("<span style=\"font-family: ");
		writer.write((String)attributes.get(TextAttribute.FAMILY));
		writer.write("; ");

		Color forecolor = (Color)attributes.get(TextAttribute.FOREGROUND);
		if (!Color.black.equals(forecolor))
		{
			writer.write("color: #");
			String hexa = Integer.toHexString(forecolor.getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			writer.write(hexa);
			writer.write("; ");
		}

		Color backcolor = (Color)attributes.get(TextAttribute.BACKGROUND);
		if (backcolor != null)
		{
			writer.write("background-color: #");
			String hexa = Integer.toHexString(backcolor.getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			writer.write(hexa);
			writer.write("; ");
		}

		writer.write("font-size: ");
		writer.write(String.valueOf(attributes.get(TextAttribute.SIZE)));
		writer.write("px;");

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

		writer.write("  <td");
		if (gridCell.colSpan > 1)
		{
			writer.write(" colspan=" + gridCell.colSpan);
		}
		if (gridCell.rowSpan > 1)
		{
			writer.write(" rowspan=" + gridCell.rowSpan);
		}
		
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

		if (text.getBackcolor().getRGB() != Color.white.getRGB() && text.getMode() == JRElement.MODE_OPAQUE)
		{
			styleBuffer.append("background-color: #");
			String hexa = Integer.toHexString(text.getBackcolor().getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			styleBuffer.append(hexa);
			styleBuffer.append("; ");
		}

		if (text.getBox() != null)
		{
			appendBorder(
				styleBuffer, 
				text.getBox().getTopBorder(),
				text.getBox().getTopBorderColor() == null ? text.getForecolor() : text.getBox().getTopBorderColor(),
				text.getBox().getTopPadding(),
				"top"
				);
			appendBorder(
				styleBuffer, 
				text.getBox().getLeftBorder(),
				text.getBox().getLeftBorderColor() == null ? text.getForecolor() : text.getBox().getLeftBorderColor(),
				text.getBox().getLeftPadding(),
				"left"
				);
			appendBorder(
				styleBuffer, 
				text.getBox().getBottomBorder(),
				text.getBox().getBottomBorderColor() == null ? text.getForecolor() : text.getBox().getBottomBorderColor(),
				text.getBox().getBottomPadding(),
				"bottom"
				);
			appendBorder(
				styleBuffer, 
				text.getBox().getRightBorder(),
				text.getBox().getRightBorderColor() == null ? text.getForecolor() : text.getBox().getRightBorderColor(),
				text.getBox().getRightPadding(),
				"right"
				);
		}

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
			styleBuffer.append("width: " + gridCell.width + "px; ");
			styleBuffer.append("word-wrap: break-word; ");
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

		String href = null;
		switch(text.getHyperlinkType())
		{
			case JRHyperlink.HYPERLINK_TYPE_REFERENCE :
			{
				if (text.getHyperlinkReference() != null)
				{
					href = text.getHyperlinkReference();
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR :
			{
				if (text.getHyperlinkAnchor() != null)
				{
					href = "#" + text.getHyperlinkAnchor();
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE :
			{
				if (text.getHyperlinkPage() != null)
				{
					href = "#" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + text.getHyperlinkPage().toString();
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR :
			{
				if (
					text.getHyperlinkReference() != null &&
					text.getHyperlinkAnchor() != null
					)
				{
					href = text.getHyperlinkReference() + "#" + text.getHyperlinkAnchor();
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE :
			{
				if (
					text.getHyperlinkReference() != null &&
					text.getHyperlinkPage() != null
					)
				{
					href = text.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + text.getHyperlinkPage().toString();
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_NONE :
			default :
			{
				break;
			}
		}

		String target = null;
		switch(text.getHyperlinkTarget())
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

		if (href != null)
		{
			writer.write("<a href=\"");
			writer.write(href);
			writer.write("\"");
			if (target != null)
			{
				writer.write(" target=\"");
				writer.write(target);
				writer.write("\"");
			}
			writer.write(">");
		}

		if (textLength > 0)
		{
			exportStyledText(styledText);
		}
		else
		{
			writer.write(emptyCellStringProvider.getStringForEmptyTD(imagesURI));
		}

		if (href != null)
		{
			writer.write("</a>");
		}

		writer.write("</td>\n");
	}


	/**
	 *
	 */
	protected void exportImage(JRPrintImage image, JRExporterGridCell gridCell) throws JRException, IOException
	{
		writer.write("  <td");
		if (gridCell.colSpan > 1)
		{
			writer.write(" colspan=" + gridCell.colSpan);
		}
		if (gridCell.rowSpan > 1)
		{
			writer.write(" rowspan=" + gridCell.rowSpan);
		}
//		if (image.getBackcolor().getRGB() != Color.white.getRGB() && image.getMode() == JRElement.MODE_OPAQUE)
//		{
//			writer.write(" bgcolor=#");
//			String hexa = Integer.toHexString(image.getBackcolor().getRGB() & colorMask).toUpperCase();
//			hexa = ("000000" + hexa).substring(hexa.length());
//			writer.write(hexa);
//		}

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

		if (image.getBackcolor().getRGB() != Color.white.getRGB() && image.getMode() == JRElement.MODE_OPAQUE)
		{
			styleBuffer.append("background-color: #");
			String hexa = Integer.toHexString(image.getBackcolor().getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			styleBuffer.append(hexa);
			styleBuffer.append("; ");
		}

		if (image.getBox() != null)
		{
			appendBorder(
				styleBuffer, 
				image.getBox().getTopBorder(),
				image.getBox().getTopBorderColor() == null ? image.getForecolor() : image.getBox().getTopBorderColor(),
				image.getBox().getTopPadding(),
				"top"
				);
			appendBorder(
				styleBuffer, 
				image.getBox().getLeftBorder(),
				image.getBox().getLeftBorderColor() == null ? image.getForecolor() : image.getBox().getLeftBorderColor(),
				image.getBox().getLeftPadding(),
				"left"
				);
			appendBorder(
				styleBuffer, 
				image.getBox().getBottomBorder(),
				image.getBox().getBottomBorderColor() == null ? image.getForecolor() : image.getBox().getBottomBorderColor(),
				image.getBox().getBottomPadding(),
				"bottom"
				);
			appendBorder(
				styleBuffer, 
				image.getBox().getRightBorder(),
				image.getBox().getRightBorderColor() == null ? image.getForecolor() : image.getBox().getRightBorderColor(),
				image.getBox().getRightPadding(),
				"right"
				);
		}

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

		String href = null;
		switch(image.getHyperlinkType())
		{
			case JRHyperlink.HYPERLINK_TYPE_REFERENCE :
			{
				if (image.getHyperlinkReference() != null)
				{
					href = image.getHyperlinkReference();
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR :
			{
				if (image.getHyperlinkAnchor() != null)
				{
					href = "#" + image.getHyperlinkAnchor();
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE :
			{
				if (image.getHyperlinkPage() != null)
				{
					href = "#" + JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + image.getHyperlinkPage().toString();
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR :
			{
				if (
					image.getHyperlinkReference() != null &&
					image.getHyperlinkAnchor() != null
					)
				{
					href = image.getHyperlinkReference() + "#" + image.getHyperlinkAnchor();
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE :
			{
				if (
					image.getHyperlinkReference() != null &&
					image.getHyperlinkPage() != null
					)
				{
					href = image.getHyperlinkReference() + "#" + JR_PAGE_ANCHOR_PREFIX + "0_" + image.getHyperlinkPage().toString();
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_NONE :
			default :
			{
				break;
			}
		}

		String target = null;
		switch(image.getHyperlinkTarget())
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

		if (href != null)
		{
			writer.write("<a href=\"");
			writer.write(href);
			writer.write("\"");
			if (target != null)
			{
				writer.write(" target=\"");
				writer.write(target);
				writer.write("\"");
			}
			writer.write(">");
		}

		writer.write("<img");

		String imagePath = "";
		
		byte scaleImage = image.getScaleImage();
		JRRenderable renderer = image.getRenderer();
		if (renderer != null)
		{
			if (renderer.getType() == JRRenderable.TYPE_IMAGE && rendererToImagePathMap.containsKey(renderer))
			{
				imagePath = (String)rendererToImagePathMap.get(renderer);
			}
			else
			{
				if (renderer.getType() == JRRenderable.TYPE_SVG)
				{
					renderer = 
						new JRWrappingSvgRenderer(
							renderer, 
							new Dimension(image.getWidth(), image.getHeight()),
							image.getBackcolor()
							);
				}
				
				if (image.isLazy())
				{
					imagePath = ((JRImageRenderer)renderer).getImageLocation();
				}
				else
				{
					String imageName = "img_" + String.valueOf(imageNameToImageDataMap.size());
					imageNameToImageDataMap.put(imageName, renderer.getImageData());
		
					imagePath = imagesURI + imageName;
				}

				rendererToImagePathMap.put(renderer, imagePath);
			}
		}
		else
		{
			loadPxImage();
			imagePath = imagesURI + "px";
			scaleImage = JRImage.SCALE_IMAGE_FILL_FRAME;
		}
		
		writer.write(" src=\"");
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

		writer.write(" border=");
		writer.write(String.valueOf(borderWidth));

		switch (scaleImage)
		{
			case JRImage.SCALE_IMAGE_FILL_FRAME :
			{
				writer.write(" width=");
				writer.write(String.valueOf(image.getWidth()));

				writer.write(" height=");
				writer.write(String.valueOf(image.getHeight()));

				break;
			}
			case JRImage.SCALE_IMAGE_CLIP :
			case JRImage.SCALE_IMAGE_RETAIN_SHAPE :
			default :
			{
				double normalWidth = image.getWidth();
				double normalHeight = image.getHeight();
				
				if (!image.isLazy())
				{
					Dimension2D dimension = renderer.getDimension();
					if (dimension != null)
					{
						normalWidth = dimension.getWidth();
						normalHeight = dimension.getHeight();
					}
				}
		
				if (image.getHeight() > 0)
				{
					double ratio = normalWidth / normalHeight;
					
					if( ratio > (double)image.getWidth() / (double)image.getHeight() )
					{
						writer.write(" width=");
						writer.write(String.valueOf(image.getWidth()));
					}
					else
					{
						writer.write(" height=");
						writer.write(String.valueOf(image.getHeight()));
					}
				}
			}
		}
				
		writer.write(">");

		if (href != null)
		{
			writer.write("</a>");
		}

		writer.write("</td>\n");
	}


	/**
	 *
	 */
	protected void layoutGrid(JRPrintPage page)
	{
		xCuts = new ArrayList();
		yCuts = new ArrayList();

		xCuts.add(new Integer(0));
		xCuts.add(new Integer(jasperPrint.getPageWidth()));
		yCuts.add(new Integer(0));
		yCuts.add(new Integer(jasperPrint.getPageHeight()));

		Integer x = null;
		Integer y = null;
		
		JRPrintElement element = null;

		List elems = page.getElements();
		for(Iterator it = elems.iterator(); it.hasNext();)
		{
			element = ((JRPrintElement)it.next());
			
			/*
			if (element instanceof JRPrintLine)
			{
				//
			}
			else if (element instanceof JRPrintEllipse)
			{
				//
			}
			else if (
				(element instanceof JRPrintRectangle) ||
				(element instanceof JRPrintImage) ||
				(element instanceof JRPrintText)
				)
			*/
			{
				x = new Integer(element.getX() + globalOffsetX);
				if (!xCuts.contains(x))
				{
					xCuts.add(x);
				}
				x = new Integer(element.getX() + globalOffsetX + element.getWidth());
				if (!xCuts.contains(x))
				{
					xCuts.add(x);
				}
				y = new Integer(element.getY() + globalOffsetY);
				if (!yCuts.contains(y))
				{
					yCuts.add(y);
				}
				y = new Integer(element.getY() + globalOffsetY + element.getHeight());
				if (!yCuts.contains(y))
				{
					yCuts.add(y);
				}
			}
		}

		Collections.sort(xCuts);
		Collections.sort(yCuts);

		int xCellCount = xCuts.size() - 1;
		int yCellCount = yCuts.size() - 1;

		grid = new JRExporterGridCell[yCellCount][xCellCount];
		isRowNotEmpty = new boolean[yCellCount];
				
		for(int j = 0; j < yCellCount; j++)
		{ 
			for(int i = 0; i < xCellCount; i++)
			{ 
				grid[j][i] = 
					new JRExporterGridCell(
						null,
						((Integer)xCuts.get(i + 1)).intValue() - ((Integer)xCuts.get(i)).intValue(),
						((Integer)yCuts.get(j + 1)).intValue() - ((Integer)yCuts.get(j)).intValue(),
						1,
						1
						);
			}
		}

		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;
		int xi = 0;
		int yi = 0;
		boolean isOverlap = false;
		
		for(int i = elems.size() - 1; i >= 0; i--)
		{
			element = ((JRPrintElement)elems.get(i));
			
			/*
			if (element instanceof JRPrintLine)
			{
				//
			}
			else if (element instanceof JRPrintEllipse)
			{
				//
			}
			else if (
				(element instanceof JRPrintRectangle) ||
				(element instanceof JRPrintImage) ||
				(element instanceof JRPrintText)
				)
			*/
			{
				x1 = xCuts.indexOf(new Integer(element.getX() + globalOffsetX));
				y1 = yCuts.indexOf(new Integer(element.getY() + globalOffsetY));
				x2 = xCuts.indexOf(new Integer(element.getX() + globalOffsetX + element.getWidth()));
				y2 = yCuts.indexOf(new Integer(element.getY() + globalOffsetY + element.getHeight()));
				
				isOverlap = false;
				yi = y1;
				while(yi < y2 && !isOverlap)
				{
					xi = x1;
					while(xi < x2 && !isOverlap)
					{
						if(grid[yi][xi].element != null)
						{
							isOverlap = true;
						}
						xi++;
					}
					yi++;
				}

				if (!isOverlap)
				{
					yi = y1;
					while(yi < y2)
					{
						xi = x1;
						while(xi < x2)
						{
							grid[yi][xi] = JRExporterGridCell.OCCUPIED_CELL;
							xi++;
						}
						isRowNotEmpty[yi] = true;
						yi++;
					}

					if (x2 - x1 != 0 && y2 - y1 != 0)
					{
						grid[y1][x1] = 
							new JRExporterGridCell(
								element,
								element.getWidth(),
								element.getHeight(),
								x2 - x1,
								y2 - y1
								);
					}
				}
			}
		}
	}
	
	
	/**
	 *
	 */
	protected void loadPxImage() throws JRException
	{
		if (!imageNameToImageDataMap.containsKey("px"))
		{
			JRRenderable pxRenderer = 
				JRImageRenderer.getInstance(
					"net/sf/jasperreports/engine/images/pixel.GIF",
					JRImage.ON_ERROR_TYPE_ERROR
					);
			rendererToImagePathMap.put(pxRenderer, imagesURI + "px");
			imageNameToImageDataMap.put("px", pxRenderer.getImageData());
		}
	}
	
	
	/**
	 * 
	 */
	protected static interface StringProvider
	{
	
		/**
		 * 
		 */
		public String getStringForCollapsedTD(Object value);
	
		/**
		 * 
		 */
		public String getStringForEmptyTD(Object value);

	}


	/**
	 *
	 */
	private static void appendBorder(StringBuffer sb, byte pen, Color borderColor, int padding, String side)
	{
		String borderStyle = null; 
		String borderWidth = null; 

		switch (pen)
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				borderStyle = "dashed"; 
				borderWidth = "1px"; 
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				borderStyle = "solid"; 
				borderWidth = "4px"; 
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				borderStyle = "solid"; 
				borderWidth = "2px"; 
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				borderStyle = "solid"; 
				borderWidth = "1px"; 
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
				borderWidth = "1px"; 
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
			sb.append("; ");
			
			sb.append("border-");
			sb.append(side);
			sb.append("-color: #");
			String hexa = Integer.toHexString(borderColor.getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			sb.append(hexa);
			sb.append("; ");
		}
		
		if (padding > 0)
		{
			sb.append("padding-");
			sb.append(side);
			sb.append(": ");
			sb.append(padding);
			sb.append("px; ");
		}
	}


}

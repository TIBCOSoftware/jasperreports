/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */

/*
 * Contributors:
 * Alex Parfenov - aparfeno@users.sourceforge.net
 */
package dori.jasper.engine.export;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import dori.jasper.engine.JRAbstractExporter;
import dori.jasper.engine.JRAlignment;
import dori.jasper.engine.JRElement;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExporterParameter;
import dori.jasper.engine.JRFont;
import dori.jasper.engine.JRGraphicElement;
import dori.jasper.engine.JRHyperlink;
import dori.jasper.engine.JRImage;
import dori.jasper.engine.JRPrintElement;
import dori.jasper.engine.JRPrintEllipse;
import dori.jasper.engine.JRPrintImage;
import dori.jasper.engine.JRPrintLine;
import dori.jasper.engine.JRPrintPage;
import dori.jasper.engine.JRPrintRectangle;
import dori.jasper.engine.JRPrintText;
import dori.jasper.engine.JRTextElement;
import dori.jasper.engine.base.JRBaseFont;
import dori.jasper.engine.util.JRImageLoader;
import dori.jasper.engine.util.JRStringUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRHtmlExporter extends JRAbstractExporter
{


	/**
	 *
	 */
	public static final String CSS_TEXT_ALIGN_LEFT = "left";
	public static final String CSS_TEXT_ALIGN_RIGHT = "right";
	public static final String CSS_TEXT_ALIGN_CENTER = "center";
	public static final String CSS_TEXT_ALIGN_JUSTIFY = "justify";

	/**
	 *
	 */
	public static final String HTML_VERTICAL_ALIGN_TOP = "top";
	public static final String HTML_VERTICAL_ALIGN_MIDDLE = "middle";
	public static final String HTML_VERTICAL_ALIGN_BOTTOM = "bottom";

	/**
	 *
	 */
	protected Writer writer = null;
	protected JRExportProgressMonitor progressMonitor = null;
	protected Map loadedImagesMap = null;
	protected Map imagesMap = null;

	/**
	 *
	 */
	protected JRFont defaultFont = null;

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
		setInput();

		/*   */
		setPageRange();

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

		imagesMap = (Map)parameters.get(JRHtmlExporterParameter.IMAGES_MAP);
		if(imagesMap == null)
		{
			imagesMap = new HashMap();
		}

		encoding = (String)parameters.get(JRExporterParameter.CHARACTER_ENCODING);
		if (encoding == null)
		{
			encoding = "UTF-8";
		}
		
		loadedImagesMap = new HashMap();
		
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
		

		StringBuffer sb = (StringBuffer)parameters.get(JRXmlExporterParameter.OUTPUT_STRING_BUFFER);
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

			Collection imageKeys = loadedImagesMap.keySet();
			if (imageKeys != null && imageKeys.size() > 0)
			{
				if (!imagesDir.exists())
				{
					imagesDir.mkdir();
				}
	
				byte[] imageData = null;
				File imageFile = null;
				FileOutputStream fos = null;
				for(Iterator it = imageKeys.iterator(); it.hasNext();)
				{
					imageData = (byte[])it.next();
					imageFile = new File(imagesDir, (String)loadedImagesMap.get(imageData));
					try
					{
						fos = new FileOutputStream(imageFile);
						fos.write(imageData, 0, imageData.length);
					}
					catch (Exception e)
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
			writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + encoding + "\">\n");
			writer.write("<head>\n");
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

		List pages = jasperPrint.getPages();
		if (pages != null && pages.size() > 0)
		{
			JRPrintPage page = null;
			
			for(int i = startPageIndex; i <= endPageIndex; i++)
			{
				if (Thread.currentThread().isInterrupted())
				{
					throw new JRException("Current thread interrupted.");
				}
				
				page = (JRPrintPage)pages.get(i);

				writer.write("<a name=\"JR_PAGE_ANCHOR_" + (i + 1) + "\">\n");

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
	protected void exportText(JRPrintText text, JRExporterGridCell gridCell) throws IOException
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

		StringBuffer styleBuffer = new StringBuffer();

		if (text.getBackcolor().getRGB() != Color.white.getRGB() && text.getMode() == JRElement.MODE_OPAQUE)
		{
			styleBuffer.append("background-color: #");
			String hexa = Integer.toHexString(text.getBackcolor().getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			styleBuffer.append(hexa);
			styleBuffer.append("; ");
		}

		String horizontalAlignment = CSS_TEXT_ALIGN_LEFT;

		if (text.getText() != null && text.getText().length() > 0)
		{
			switch (text.getTextAlignment())
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

			if (!horizontalAlignment.equals(CSS_TEXT_ALIGN_LEFT))
			{
				styleBuffer.append("text-align: ");
				styleBuffer.append(horizontalAlignment);
				styleBuffer.append(";");
			}
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
			writer.write("\">");
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
					href = "#JR_PAGE_ANCHOR_" + text.getHyperlinkPage().toString();
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
					href = text.getHyperlinkReference() + "#JR_PAGE_ANCHOR_" + text.getHyperlinkPage().toString();
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_NONE :
			default :
			{
				break;
			}
		}

		if (href != null)
		{
			writer.write("<a href=\"");
			writer.write(href);
			writer.write("\">");
		}

		JRFont font = text.getFont();
		if (font == null)
		{
			font = getDefaultFont();
		}

		if (text.getText() != null && text.getText().length() > 0)
		{
			writer.write("<font face=\"");
			writer.write(font.getFontName());

			writer.write("\" style=\"");

			if (text.getForecolor().getRGB() != Color.black.getRGB())
			{
				writer.write("color: #");
				String hexa = Integer.toHexString(text.getForecolor().getRGB() & colorMask).toUpperCase();
				hexa = ("000000" + hexa).substring(hexa.length());
				writer.write(hexa);
				writer.write("; ");
			}

			writer.write("font-size: ");
			writer.write(String.valueOf(font.getSize()));
			writer.write("px;");

			if (!horizontalAlignment.equals(CSS_TEXT_ALIGN_LEFT))
			{
				writer.write(" text-align: ");
				writer.write(horizontalAlignment);
				writer.write(";");
			}

			if (font.isUnderline())
			{
				writer.write(" text-decoration: underline;");
			}
			if (font.isStrikeThrough())
			{
				writer.write(" text-decoration: line-through;");
			}

			writer.write("\">");

			if (font.isBold())
			{
				writer.write("<b>");
			}
			if (font.isItalic())
			{
				writer.write("<i>");
			}
			
			writer.write(
				replaceNewLineWithBR(
					JRStringUtil.xmlEncode(text.getText())
					)
				);
			
			if (font.isItalic())
			{
				writer.write("</i>");
			}
			if (font.isBold())
			{
				writer.write("</b>");
			}
	
			writer.write("</font>");
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
		if (image.getBackcolor().getRGB() != Color.white.getRGB() && image.getMode() == JRElement.MODE_OPAQUE)
		{
			writer.write(" bgcolor=#");
			String hexa = Integer.toHexString(image.getBackcolor().getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			writer.write(hexa);
		}

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
			case JRTextElement.VERTICAL_ALIGN_BOTTOM : 
			{
				verticalAlignment = HTML_VERTICAL_ALIGN_BOTTOM; 
				break;
			}
			case JRTextElement.VERTICAL_ALIGN_MIDDLE : 
			{
				verticalAlignment = HTML_VERTICAL_ALIGN_MIDDLE;
				break;
			}
			case JRTextElement.VERTICAL_ALIGN_TOP : 
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

		writer.write(">");

		if (image.getAnchorName() != null)
		{
			writer.write("<a name=\"");
			writer.write(image.getAnchorName());
			writer.write("\">");
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
					href = "#JR_PAGE_ANCHOR_" + image.getHyperlinkPage().toString();
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
					href = image.getHyperlinkReference() + "#JR_PAGE_ANCHOR_" + image.getHyperlinkPage().toString();
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_NONE :
			default :
			{
				break;
			}
		}

		if (href != null)
		{
			writer.write("<a href=\"");
			writer.write(href);
			writer.write("\">");
		}

		writer.write("<img");

		String imageSource = "";
		
		byte scaleImage = image.getScaleImage();
		byte[] imageData = image.getImageData();
		if (imageData != null)
		{
			if (loadedImagesMap.containsKey(imageData))
			{
				//imageSource = imagesDir.getName() + "/" + (String)loadedImagesMap.get(imageData);
				imageSource = imagesURI + (String)loadedImagesMap.get(imageData);
			}
			else
			{
				imageSource = "img_" + String.valueOf(loadedImagesMap.size());
				loadedImagesMap.put(imageData, imageSource);
				imagesMap.put(imageSource, imageData);
	
				//imageSource = imagesDir.getName() + "/" + imageSource;
				imageSource = imagesURI + imageSource;
			}
		}
		else
		{
			loadPxImage();
			imageSource = imagesURI + "px";
			scaleImage = JRImage.SCALE_IMAGE_FILL_FRAME;
		}
		
		writer.write(" src=\"");
		writer.write(imageSource);
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
				java.awt.Image awtImage = JRImageLoader.loadImage(imageData);
		
				if (image.getHeight() > 0)
				{
					double ratio = (double)awtImage.getWidth(null) / (double)awtImage.getHeight(null);
					
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
				x = new Integer(element.getX());
				if (!xCuts.contains(x))
				{
					xCuts.add(x);
				}
				x = new Integer(element.getX() + element.getWidth());
				if (!xCuts.contains(x))
				{
					xCuts.add(x);
				}
				y = new Integer(element.getY());
				if (!yCuts.contains(y))
				{
					yCuts.add(y);
				}
				y = new Integer(element.getY() + element.getHeight());
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
				x1 = xCuts.indexOf(new Integer(element.getX()));
				y1 = yCuts.indexOf(new Integer(element.getY()));
				x2 = xCuts.indexOf(new Integer(element.getX() + element.getWidth()));
				y2 = yCuts.indexOf(new Integer(element.getY() + element.getHeight()));
				
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
	protected static String replaceNewLineWithBR(String source)
	{
		String str = null;
		
		if (source != null)
		{
			StringBuffer sbuffer = new StringBuffer();
			StringTokenizer tkzer = new StringTokenizer(source, "\n", true);
			String token = null;
			while(tkzer.hasMoreTokens())
			{
				token = tkzer.nextToken();
				if ("\n".equals(token))
				{
					sbuffer.append("<br>");
				}
				else
				{
					sbuffer.append(token);
				}
			}
			
			str = sbuffer.toString();
		}
		
		return str;
	}


	/**
	 *
	 */
	protected void loadPxImage() throws JRException
	{
		if (!imagesMap.containsKey("px"))
		{
			byte[] pxBytes = JRImageLoader.loadImageDataFromLocation("dori/jasper/engine/images/pixel.GIF");
			loadedImagesMap.put(pxBytes, "px");
			imagesMap.put("px", pxBytes);
		}
	}
	
	
}


/**
 * 
 */
interface StringProvider
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

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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
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
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.base.JRBaseFont;
import dori.jasper.engine.util.JRImageLoader;
import dori.jasper.engine.util.JRLoader;
import dori.jasper.engine.util.JRStringUtil;


/**
 *
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
	private JasperPrint jasperPrint = null;

	/**
	 *
	 */
	private Writer writer = null;
	private Map loadedImagesMap = null;
	private Map imagesMap = null;

	/**
	 *
	 */
	private JRFont defaultFont = null;

	/**
	 *
	 */
	private File imagesDir = null;
	private String imagesURI = null;
	private boolean isOutputImagesToDir = false;
	private boolean isRemoveEmptySpace = false;
	private boolean isWhitePageBackground = true;
	private String encoding = null;

	/**
	 *
	 */
	private int startPageIndex = 0;
	private int endPageIndex = 0;
	private String htmlHeader = null;
	private String betweenPagesHtml = null;
	private String htmlFooter = null;

	/**
	 *
	 */
	private static final int colorMask = Integer.parseInt("FFFFFF", 16);

	/**
	 *
	 */
	private JRExporterGridCell grid[][] = null;
	private boolean isRowNotEmpty[] = null;
	private List xCuts = null;
	private List yCuts = null;


	/**
	 *
	 */
	private JRFont getDefaultFont()
	{
		if (defaultFont == null)
		{
			defaultFont = jasperPrint.getDefaultFont();
			if (defaultFont == null)
			{
				defaultFont = new JRBaseFont();
			}
		}

		return this.defaultFont;
	}


	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		this.jasperPrint = (JasperPrint)this.parameters.get(JRExporterParameter.JASPER_PRINT);
		if (jasperPrint == null)
		{
			InputStream is = (InputStream)this.parameters.get(JRExporterParameter.INPUT_STREAM);
			if (is != null)
			{
				this.jasperPrint = (JasperPrint)JRLoader.loadObject(is);
			}
			else
			{
				URL url = (URL)this.parameters.get(JRExporterParameter.INPUT_URL);
				if (url != null)
				{
					this.jasperPrint = (JasperPrint)JRLoader.loadObject(url);
				}
				else
				{
					File file = (File)this.parameters.get(JRExporterParameter.INPUT_FILE);
					if (file != null)
					{
						this.jasperPrint = (JasperPrint)JRLoader.loadObject(file);
					}
					else
					{
						String fileName = (String)this.parameters.get(JRExporterParameter.INPUT_FILE_NAME);
						if (fileName != null)
						{
							this.jasperPrint = (JasperPrint)JRLoader.loadObject(fileName);
						}
						else
						{
							throw new JRException("No input source supplied to the exporter.");
						}
					}
				}
			}
		}


		int lastPageIndex = -1;
		if (this.jasperPrint.getPages() != null)
		{
			lastPageIndex = this.jasperPrint.getPages().size() - 1;
		}

		Integer start = (Integer)this.parameters.get(JRExporterParameter.START_PAGE_INDEX);
		if (start == null)
		{
			this.startPageIndex = 0;
		}
		else
		{
			this.startPageIndex = start.intValue();
			if (this.startPageIndex < 0 || this.startPageIndex > lastPageIndex)
			{
				throw new JRException("Start page index out of range : " + this.startPageIndex + " of " + lastPageIndex);
			}
		}

		Integer end = (Integer)this.parameters.get(JRExporterParameter.END_PAGE_INDEX);
		if (end == null)
		{
			this.endPageIndex = lastPageIndex;
		}
		else
		{
			this.endPageIndex = end.intValue();
			if (this.endPageIndex < 0 || this.endPageIndex > lastPageIndex)
			{
				throw new JRException("End page index out of range : " + this.endPageIndex + " of " + lastPageIndex);
			}
		}

		Integer index = (Integer)this.parameters.get(JRExporterParameter.PAGE_INDEX);
		if (index != null)
		{
			int pageIndex = index.intValue();
			if (pageIndex < 0 || pageIndex > lastPageIndex)
			{
				throw new JRException("Page index out of range : " + pageIndex + " of " + lastPageIndex);
			}
			else
			{
				this.startPageIndex = pageIndex;
				this.endPageIndex = pageIndex;
			}
		}

		this.htmlHeader = (String)this.parameters.get(JRHtmlExporterParameter.HTML_HEADER);
		this.betweenPagesHtml = (String)this.parameters.get(JRHtmlExporterParameter.BETWEEN_PAGES_HTML);
		this.htmlFooter = (String)this.parameters.get(JRHtmlExporterParameter.HTML_FOOTER);

		this.imagesDir = (File)this.parameters.get(JRHtmlExporterParameter.IMAGES_DIR);
		if (imagesDir == null)
		{
			String dir = (String)this.parameters.get(JRHtmlExporterParameter.IMAGES_DIR_NAME);
			if (dir != null)
			{
				this.imagesDir = new File(dir);
			}
		}

		Boolean isRemoveEmptySpaceParameter = (Boolean)this.parameters.get(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS);
		if (isRemoveEmptySpaceParameter != null)
		{
			this.isRemoveEmptySpace = isRemoveEmptySpaceParameter.booleanValue();
		}
		
		Boolean isWhitePageBackgroundParameter = (Boolean)this.parameters.get(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND);
		if (isWhitePageBackgroundParameter != null)
		{
			this.isWhitePageBackground = isWhitePageBackgroundParameter.booleanValue();
		}

		Boolean isOutputImagesToDirParameter = (Boolean)this.parameters.get(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR);
		if (isOutputImagesToDirParameter != null)
		{
			this.isOutputImagesToDir = isOutputImagesToDirParameter.booleanValue();
		}
				
		String uri = (String)this.parameters.get(JRHtmlExporterParameter.IMAGES_URI);
		if (uri != null)
		{
			this.imagesURI = uri;
		}

		this.imagesMap = (Map)this.parameters.get(JRHtmlExporterParameter.IMAGES_MAP);
		if(this.imagesMap == null)
		{
			this.imagesMap = new HashMap();
		}

		this.encoding = (String)this.parameters.get(JRExporterParameter.CHARACTER_ENCODING);
		if (this.encoding == null)
		{
			this.encoding = "UTF-8";
		}
		
		this.loadedImagesMap = new HashMap();
		byte[] pxBytes = JRImageLoader.loadImageDataFromLocation("dori/jasper/engine/images/pixel.GIF");
		this.loadedImagesMap.put(pxBytes, "px");
		this.imagesMap.put("px", pxBytes);


		StringBuffer sb = (StringBuffer)this.parameters.get(JRXmlExporterParameter.OUTPUT_STRING_BUFFER);
		if (sb != null)
		{
			try
			{
				this.writer = new StringWriter();
				this.exportReportToWriter();
				sb.append(this.writer.toString());
			}
			catch (IOException e)
			{
				throw new JRException("Error writing to StringBuffer writer : " + jasperPrint.getName(), e);
			}
			finally
			{
				if (this.writer != null)
				{
					try
					{
						this.writer.close();
					}
					catch(IOException e)
					{
					}
				}
			}
		}
		else
		{
			this.writer = (Writer)this.parameters.get(JRExporterParameter.OUTPUT_WRITER);
			if (this.writer != null)
			{
				try
				{
					this.exportReportToWriter();
				}
				catch (IOException e)
				{
					throw new JRException("Error writing to writer : " + jasperPrint.getName(), e);
				}
			}
			else
			{
				OutputStream os = (OutputStream)this.parameters.get(JRExporterParameter.OUTPUT_STREAM);
				if (os != null)
				{
					try
					{
						this.writer = new OutputStreamWriter(os, this.encoding); 
						this.exportReportToWriter();
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to OutputStream writer : " + jasperPrint.getName(), e);
					}
				}
				else
				{
					File destFile = (File)this.parameters.get(JRExporterParameter.OUTPUT_FILE);
					if (destFile == null)
					{
						String fileName = (String)this.parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
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
						this.writer = new OutputStreamWriter(os, this.encoding);
					}
					catch (IOException e)
					{
						throw new JRException("Error creating to file writer : " + jasperPrint.getName(), e);
					}
					
					if (this.imagesDir == null)
					{
						this.imagesDir = new File(destFile.getParent(), destFile.getName() + "_files");
					}
	
					if (isOutputImagesToDirParameter == null)
					{
						this.isOutputImagesToDir = true;
					}
	
					if (this.imagesURI == null)
					{
						this.imagesURI = this.imagesDir.getName() + "/";
					}
					
					try
					{
						this.exportReportToWriter();
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to file writer : " + jasperPrint.getName(), e);
					}
					finally
					{
						if (this.writer != null)
						{
							try
							{
								this.writer.close();
							}
							catch(IOException e)
							{
							}
						}
					}
				}
			}
		}

		if (this.isOutputImagesToDir)
		{
			if (this.imagesDir == null)
			{
				throw new JRException("The images directory was not specified for the exporter.");
			}

			Collection imageKeys = this.loadedImagesMap.keySet();
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
					imageFile = new File(imagesDir, (String)this.loadedImagesMap.get(imageData));
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
	private void exportReportToWriter() throws JRException, IOException
	{
		if (this.htmlHeader == null)
		{
			this.writer.write("<html>\n");
			this.writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + this.encoding + "\">\n");
			this.writer.write("<head>\n");
			this.writer.write("  <style type=\"text/css\">\n");
			this.writer.write("    a {text-decoration: none}\n");
			this.writer.write("  </style>\n");
			this.writer.write("</head>\n");
			this.writer.write("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\">\n");
			this.writer.write("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n");
			this.writer.write("<tr><td width=\"50%\">&nbsp;</td><td align=\"center\">\n");
			this.writer.write("\n");
		}
		else
		{
			this.writer.write(this.htmlHeader);
		}

		List pages = jasperPrint.getPages();
		if (pages != null && pages.size() > 0)
		{
			JRPrintPage page = null;
			for(int i = this.startPageIndex; i <= this.endPageIndex; i++)
			{
				if (Thread.currentThread().isInterrupted())
				{
					throw new JRException("Current thread interrupted.");
				}
				
				page = (JRPrintPage)pages.get(i);

				this.writer.write("<a name=\"JR_PAGE_ANCHOR_" + (i + 1) + "\">\n");

				/*   */
				this.exportPage(page);
				
				if (this.betweenPagesHtml == null)
				{
					this.writer.write("<br>\n<br>\n");
				}
				else
				{
					this.writer.write(this.betweenPagesHtml);
				}

				this.writer.write("\n");
			}
		}

		if (this.htmlFooter == null)
		{
			this.writer.write("</td><td width=\"50%\">&nbsp;</td></tr>\n");
			this.writer.write("</table>\n");
			this.writer.write("</body>\n");
			this.writer.write("</html>\n");
		}
		else
		{
			this.writer.write(this.htmlFooter);
		}

		this.writer.flush();
	}


	/**
	 *
	 */
	private void exportPage(JRPrintPage page) throws JRException, IOException
	{
		this.writer.write("<table width=" + jasperPrint.getPageWidth() + " cellpadding=0 cellspacing=0 border=0\n");
		if (this.isWhitePageBackground)
		{
			this.writer.write(" bgcolor=white");
		}
		this.writer.write(">\n");

		this.layoutGrid(page);

		this.writer.write("<tr>\n");
		int width = 0;
		for(int i = 1; i < xCuts.size(); i++)
		{
			width = ((Integer)xCuts.get(i)).intValue() - ((Integer)xCuts.get(i - 1)).intValue();
			this.writer.write("  <td><img src=\"" + this.imagesURI + "px\" width=" + width + " height=1></td>\n");
		}
		this.writer.write("</tr>\n");

		JRPrintElement element = null;
		for(int y = 0; y < grid.length; y++)
		{
			if (isRowNotEmpty[y] || !isRemoveEmptySpace)
			{
				this.writer.write("<tr valign=top>\n");
	
				int emptyCellColSpan = 0;
				int emptyCellWidth = 0;
				int lastRowHeight = grid[y][0].height;
	
				for(int x = 0; x < grid[y].length; x++)
				{
					if(grid[y][x].element != null)
					{
						if (emptyCellColSpan > 0)
						{
							this.writer.write("  <td");
							if (emptyCellColSpan > 1)
							{
								this.writer.write(" colspan=" + emptyCellColSpan);
							}
							this.writer.write("><img src=\"" + this.imagesURI + "px\" width=" + emptyCellWidth + " height=" + lastRowHeight + "></td>\n");
							emptyCellColSpan = 0;
							emptyCellWidth = 0;
						}
	
						element = grid[y][x].element;
	
						if (element instanceof JRPrintLine)
						{
							this.exportLine((JRPrintLine)element, grid[y][x]);
						}
						else if (element instanceof JRPrintRectangle)
						{
							this.exportRectangle(element, grid[y][x]);
						}
						else if (element instanceof JRPrintEllipse)
						{
							this.exportRectangle(element, grid[y][x]);
						}
						else if (element instanceof JRPrintImage)
						{
							this.exportImage((JRPrintImage)element, grid[y][x]);
						}
						else if (element instanceof JRPrintText)
						{
							this.exportText((JRPrintText)element, grid[y][x]);
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
					this.writer.write("  <td");
					if (emptyCellColSpan > 1)
					{
						this.writer.write(" colspan=" + emptyCellColSpan);
					}
					this.writer.write("><img src=\"" + this.imagesURI + "px\" width=" + emptyCellWidth + " height=" + lastRowHeight + "></td>\n");
				}
	
				this.writer.write("</tr>\n");
			}
		}

		this.writer.write("</table>\n");
	}


	/**
	 *
	 */
	private void exportLine(JRPrintLine line, JRExporterGridCell gridCell) throws IOException
	{
		this.writer.write("  <td");
		if (gridCell.colSpan > 1)
		{
			this.writer.write(" colspan=" + gridCell.colSpan);
		}
		if (gridCell.rowSpan > 1)
		{
			this.writer.write(" rowspan=" + gridCell.rowSpan);
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

		this.writer.write(">");

		this.writer.write("<img src=\"" + this.imagesURI + "px\" border=0>");

		this.writer.write("</td>\n");
	}


	/**
	 *
	 */
	private void exportRectangle(JRPrintElement element, JRExporterGridCell gridCell) throws IOException
	{
		this.writer.write("  <td");
		if (gridCell.colSpan > 1)
		{
			this.writer.write(" colspan=" + gridCell.colSpan);
		}
		if (gridCell.rowSpan > 1)
		{
			this.writer.write(" rowspan=" + gridCell.rowSpan);
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

		this.writer.write(">");

		this.writer.write("<img src=\"" + this.imagesURI + "px\" border=0>");

		this.writer.write("</td>\n");
	}


	/**
	 *
	 */
	private void exportText(JRPrintText text, JRExporterGridCell gridCell) throws IOException
	{
		this.writer.write("  <td");
		if (gridCell.colSpan > 1)
		{
			this.writer.write(" colspan=" + gridCell.colSpan);
		}
		if (gridCell.rowSpan > 1)
		{
			this.writer.write(" rowspan=" + gridCell.rowSpan);
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
			this.writer.write(" valign=\"");
			this.writer.write(verticalAlignment);
			this.writer.write("\"");
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
			this.writer.write(" style=\"");
			this.writer.write(styleBuffer.toString());
			this.writer.write("\"");
		}
		
		this.writer.write(">");

		if (text.getAnchorName() != null)
		{
			this.writer.write("<a name=\"");
			this.writer.write(text.getAnchorName());
			this.writer.write("\">");
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
			this.writer.write("<a href=\"");
			this.writer.write(href);
			this.writer.write("\">");
		}

		JRFont font = text.getFont();
		if (font == null)
		{
			font = this.getDefaultFont();
		}

		if (text.getText() != null && text.getText().length() > 0)
		{
			this.writer.write("<font face=\"");
			this.writer.write(font.getFontName());

			this.writer.write("\" style=\"");

			if (text.getForecolor().getRGB() != Color.black.getRGB())
			{
				this.writer.write("color: #");
				String hexa = Integer.toHexString(text.getForecolor().getRGB() & colorMask).toUpperCase();
				hexa = ("000000" + hexa).substring(hexa.length());
				this.writer.write(hexa);
				this.writer.write("; ");
			}

			this.writer.write("font-size: ");
			this.writer.write(String.valueOf(font.getSize()));
			this.writer.write("px;");

			if (!horizontalAlignment.equals(CSS_TEXT_ALIGN_LEFT))
			{
				this.writer.write(" text-align: ");
				this.writer.write(horizontalAlignment);
				this.writer.write(";");
			}

			if (font.isUnderline())
			{
				this.writer.write(" text-decoration: underline;");
			}
			if (font.isStrikeThrough())
			{
				this.writer.write(" text-decoration: line-through;");
			}

			this.writer.write("\">");

			if (font.isBold())
			{
				this.writer.write("<b>");
			}
			if (font.isItalic())
			{
				this.writer.write("<i>");
			}
			
			this.writer.write(
				replaceNewLineWithBR(
					JRStringUtil.xmlEncode(text.getText())
					)
				);
			
			if (font.isItalic())
			{
				this.writer.write("</i>");
			}
			if (font.isBold())
			{
				this.writer.write("</b>");
			}
	
			this.writer.write("</font>");
		}
		else
		{
			this.writer.write("<img src=\"" + this.imagesURI + "px\" border=0>");
		}

		if (href != null)
		{
			this.writer.write("</a>");
		}

		this.writer.write("</td>\n");
	}


	/**
	 *
	 */
	private void exportImage(JRPrintImage image, JRExporterGridCell gridCell) throws JRException, IOException
	{
		this.writer.write("  <td");
		if (gridCell.colSpan > 1)
		{
			this.writer.write(" colspan=" + gridCell.colSpan);
		}
		if (gridCell.rowSpan > 1)
		{
			this.writer.write(" rowspan=" + gridCell.rowSpan);
		}
		if (image.getBackcolor().getRGB() != Color.white.getRGB() && image.getMode() == JRElement.MODE_OPAQUE)
		{
			this.writer.write(" bgcolor=#");
			String hexa = Integer.toHexString(image.getBackcolor().getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			this.writer.write(hexa);
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
			this.writer.write(" align=\"");
			this.writer.write(horizontalAlignment);
			this.writer.write("\"");
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
			this.writer.write(" valign=\"");
			this.writer.write(verticalAlignment);
			this.writer.write("\"");
		}

		this.writer.write(">");

		if (image.getAnchorName() != null)
		{
			this.writer.write("<a name=\"");
			this.writer.write(image.getAnchorName());
			this.writer.write("\">");
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
			this.writer.write("<a href=\"");
			this.writer.write(href);
			this.writer.write("\">");
		}

		this.writer.write("<img");

		String imageSource = "";
		
		byte scaleImage = image.getScaleImage();
		byte[] imageData = image.getImageData();
		if (imageData != null)
		{
			if (this.loadedImagesMap.containsKey(imageData))
			{
				//imageSource = imagesDir.getName() + "/" + (String)this.loadedImagesMap.get(imageData);
				imageSource = this.imagesURI + (String)this.loadedImagesMap.get(imageData);
			}
			else
			{
				imageSource = "img_" + String.valueOf(this.loadedImagesMap.size());
				this.loadedImagesMap.put(imageData, imageSource);
				this.imagesMap.put(imageSource, imageData);
	
				//imageSource = imagesDir.getName() + "/" + imageSource;
				imageSource = this.imagesURI + imageSource;
			}
		}
		else
		{
			imageSource = this.imagesURI + "px";
			scaleImage = JRImage.SCALE_IMAGE_FILL_FRAME;
		}
		
		this.writer.write(" src=\"");
		this.writer.write(imageSource);
		this.writer.write("\"");

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

		this.writer.write(" border=");
		this.writer.write(String.valueOf(borderWidth));

		switch (scaleImage)
		{
			case JRImage.SCALE_IMAGE_FILL_FRAME :
			{
				this.writer.write(" width=");
				this.writer.write(String.valueOf(image.getWidth()));

				this.writer.write(" height=");
				this.writer.write(String.valueOf(image.getHeight()));

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
						this.writer.write(" width=");
						this.writer.write(String.valueOf(image.getWidth()));
					}
					else
					{
						this.writer.write(" height=");
						this.writer.write(String.valueOf(image.getHeight()));
					}
				}
			}
		}
				
		this.writer.write(">");

		if (href != null)
		{
			this.writer.write("</a>");
		}

		this.writer.write("</td>\n");
	}


	/**
	 *
	 */
	private void layoutGrid(JRPrintPage page)
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
	
	
	/**
	 *
	 */
	private static String replaceNewLineWithBR(String source)
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


}

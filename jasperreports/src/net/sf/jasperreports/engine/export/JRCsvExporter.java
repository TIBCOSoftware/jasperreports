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
 * Mirko Wawrowsky - mawawrosky@users.sourceforge.net
 */
package dori.jasper.engine.export;

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
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import dori.jasper.engine.JRAbstractExporter;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExporterParameter;
import dori.jasper.engine.JRPrintElement;
import dori.jasper.engine.JRPrintPage;
import dori.jasper.engine.JRPrintText;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.util.JRLoader;


/**
 *
 */
public class JRCsvExporter extends JRAbstractExporter
{


	/**
	 *
	 */
	private JasperPrint jasperPrint = null;

	/**
	 *
	 */
	private String delimiter = null;

	/**
	 *
	 */
	private Writer writer = null;

	/**
	 *
	 */
	private JRExporterGridCell grid[][] = null;
	private boolean isRowUsed[] = null;
	private boolean isColUsed[] = null;
	private List xCuts = null;
	private List yCuts = null;

	
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

		String encoding = (String)this.parameters.get(JRExporterParameter.CHARACTER_ENCODING);
		if (encoding == null)
		{
			encoding = "ISO-8859-1";
		}
		
		this.delimiter = (String)this.parameters.get(JRCsvExporterParameter.FIELD_DELIMITER);
		if (this.delimiter == null)
		{
			this.delimiter = ",";
		}
		
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
						this.writer = new OutputStreamWriter(os, encoding); 
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
						this.writer = new OutputStreamWriter(os, encoding);
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
	}


	/**
	 *
	 */
	private void exportReportToWriter() throws JRException, IOException
	{
		List pages = jasperPrint.getPages();
		if (pages != null && pages.size() > 0)
		{
			JRPrintPage page = null;
			
			for(int i = 0; i < pages.size(); i++)
			{
				if (Thread.currentThread().isInterrupted())
				{
					throw new JRException("Current thread interrupted.");
				}
				
				page = (JRPrintPage)pages.get(i);

				/*   */
				this.exportPage(page);
			}
		}
		
		this.writer.flush();
	}


	/**
	 *
	 */
	private void exportPage(JRPrintPage page) throws JRException, IOException
	{
		this.layoutGrid(page);

		StringBuffer rowbuffer = null;
		
		JRPrintElement element = null;
		String text = null;
		boolean isFirstColumn = true;
		for(int y = 0; y < grid.length; y++)
		{
			rowbuffer = new StringBuffer();

			if (isRowUsed[y])
			{
				isFirstColumn = true;
				for(int x = 0; x < grid[y].length; x++)
				{
					if(grid[y][x].element != null)
					{
						element = grid[y][x].element;
	
						if (element instanceof JRPrintText)
						{
							text = ((JRPrintText)element).getText();
							if (text == null)
							{
								text = "";
							}
							
							if (!isFirstColumn)
							{
								rowbuffer.append(this.delimiter);
							}
							rowbuffer.append(
								prepareText(text)
								);
							isFirstColumn = false;
						}
					}
					else
					{
						if (isColUsed[x])
						{
							if (!isFirstColumn)
							{
								rowbuffer.append(this.delimiter);
							}
							isFirstColumn = false;
						}
					}
				}
				
				if (rowbuffer.length() > 0)
				{
					this.writer.write(rowbuffer.toString());
					this.writer.write("\n");
				}
			}
		}
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
		yCuts.add(new Integer(jasperPrint.getPageWidth()));

		Integer x = null;
		Integer y = null;
		
		Collection elems = page.getElements();
		for(Iterator it = elems.iterator(); it.hasNext();)
		{
			JRPrintElement element = ((JRPrintElement)it.next());
			
			if (element instanceof JRPrintText)
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
		isRowUsed = new boolean[yCellCount];
		isColUsed = new boolean[xCellCount];
				
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
		for(Iterator it = elems.iterator(); it.hasNext();)
		{
			JRPrintElement element = ((JRPrintElement)it.next());
			
			if (element instanceof JRPrintText)
			{
				x1 = xCuts.indexOf(new Integer(element.getX()));
				y1 = yCuts.indexOf(new Integer(element.getY()));
				x2 = xCuts.indexOf(new Integer(element.getX() + element.getWidth()));
				y2 = yCuts.indexOf(new Integer(element.getY() + element.getHeight()));
				
				grid[y1][x1].element = element;
				grid[y1][x1].width = element.getWidth();
				grid[y1][x1].height = element.getHeight();
				grid[y1][x1].colSpan = x2 - x1;
				grid[y1][x1].rowSpan = y2 - y1;
				
				isRowUsed[y1] = true;
				isColUsed[x1] = true;
			}
		}
	}
	
	
	/**
	 *
	 */
	private String prepareText(String source)
	{
		String str = null;
		
		if (source != null)
		{
			boolean putQuotes = false;
			
			if (source.indexOf(this.delimiter) >= 0)
			{
				putQuotes = true;
			}
			
			StringBuffer sbuffer = new StringBuffer();
			StringTokenizer tkzer = new StringTokenizer(source, ",\"\n", true);
			String token = null;
			while(tkzer.hasMoreTokens())
			{
				token = tkzer.nextToken();
				if (",".equals(token))
				{
					putQuotes = true;
					sbuffer.append(",");
				}
				else if ("\"".equals(token))
				{
					putQuotes = true;
					sbuffer.append("\"\"");
				}
				else if ("\n".equals(token))
				{
					sbuffer.append(" ");
				}
				else
				{
					sbuffer.append(token);
				}
			}
			
			str = sbuffer.toString();
			
			if (putQuotes)
			{
				str = "\"" + str + "\"";
			}
		}
		
		return str;
	}


}

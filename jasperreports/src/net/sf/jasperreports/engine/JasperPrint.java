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
package dori.jasper.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class JasperPrint implements Serializable
{
	

	/**
	 *
	 */
	private static final long serialVersionUID = 501;

	/**
	 *
	 */
	private String name = null;
	private int pageWidth = 0;
	private int pageHeight = 0;
	private byte orientation = JRReport.ORIENTATION_PORTRAIT;

	private JRReportFont defaultFont = null;
	private Map fontsMap = new HashMap();
	private List fontsList = new ArrayList();

	private List pages = new ArrayList();

	private Map anchorIndexes = null;


	/**
	 *
	 */
	public JasperPrint()
	{
	}

	/**
	 *
	 */
	public String getName()
	{
		return this.name;
	}
		
	/**
	 *
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 *
	 */
	public int getPageWidth()
	{
		return this.pageWidth;
	}
		
	/**
	 *
	 */
	public void setPageWidth(int pageWidth)
	{
		this.pageWidth = pageWidth;
	}

	/**
	 *
	 */
	public int getPageHeight()
	{
		return this.pageHeight;
	}
		
	/**
	 *
	 */
	public void setPageHeight(int pageHeight)
	{
		this.pageHeight = pageHeight;
	}


	/**
	 *
	 */
	public byte getOrientation()
	{
		return this.orientation;
	}
		
	/**
	 *
	 */
	public void setOrientation(byte orientation)
	{
		this.orientation = orientation;
	}

	/**
	 *
	 */
	public JRReportFont getDefaultFont()
	{
		return this.defaultFont;
	}

	/**
	 *
	 */
	public void setDefaultFont(JRReportFont font)
	{
		this.defaultFont = font;
	}
		
	/**
	 *
	 */
	public JRReportFont[] getFonts()
	{
		JRReportFont[] fontsArray = new JRReportFont[fontsList.size()];
		
		fontsList.toArray(fontsArray);

		return fontsArray;
	}

	/**
	 *
	 */
	public List getFontsList()
	{
		return this.fontsList;
	}

	/**
	 *
	 */
	public Map getFontsMap()
	{
		return this.fontsMap;
	}

	/**
	 *
	 */
	public void addFont(JRReportFont reportFont) throws JRException
	{
		if (this.fontsMap.containsKey(reportFont.getName()))
		{
			throw new JRException("Duplicate declaration of report font : " + reportFont.getName());
		}

		this.fontsList.add(reportFont);
		this.fontsMap.put(reportFont.getName(), reportFont);
		
		if (reportFont.isDefault())
		{
			this.setDefaultFont(reportFont);
		}
	}

	/**
	 *
	 */
	public JRReportFont removeFont(String name)
	{
		return this.removeFont(
			(JRReportFont)this.fontsMap.get(name)
			);
	}

	/**
	 *
	 */
	public JRReportFont removeFont(JRReportFont reportFont)
	{
		if (reportFont != null)
		{
			if (reportFont.isDefault())
			{
				this.setDefaultFont(null);
			}

			this.fontsList.remove(reportFont);
			this.fontsMap.remove(reportFont.getName());
		}
		
		return reportFont;
	}

	/**
	 *
	 */
	public List getPages()
	{
		return this.pages;
	}
		
	/**
	 *
	 */
	public void addPage(JRPrintPage page)
	{
		this.anchorIndexes = null;
		this.pages.add(page);
	}

	/**
	 *
	 */
	public void addPage(int index, JRPrintPage page)
	{
		this.anchorIndexes = null;
		this.pages.add(index, page);
	}

	/**
	 *
	 */
	public JRPrintPage removePage(int index)
	{
		this.anchorIndexes = null;
		return (JRPrintPage)this.pages.remove(index);
	}

	/**
	 *
	 */
	public Map getAnchorIndexes()
	{
		if (this.anchorIndexes == null)
		{
			this.anchorIndexes = new HashMap();
			
			JRPrintPage page = null;
			int i = 0;
			for(Iterator itp = pages.iterator(); itp.hasNext(); i++)
			{
				page = (JRPrintPage)itp.next();
				Collection elements = page.getElements();
				if (elements != null && elements.size() > 0)
				{
					JRPrintElement element = null;
					for(Iterator it = elements.iterator(); it.hasNext();)
					{
						element = (JRPrintElement)it.next();
						if (element instanceof JRPrintAnchor)
						{
							this.anchorIndexes.put(
								((JRPrintAnchor)element).getAnchorName(), 
								new JRPrintAnchorIndex(i, element)
								);
						}
					}
				}
			}
		}
		
		return this.anchorIndexes;
	}
		

}

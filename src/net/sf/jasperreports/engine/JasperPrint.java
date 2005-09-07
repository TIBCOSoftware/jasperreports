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
 * John Bindel - jbindel@users.sourceforge.net 
 */

package net.sf.jasperreports.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * An instance of this class represents a page-oriented document
 * that can be viewed, printed or exported to other formats.
 * <p>
 * When filling report designs with data, the engine produces instances
 * of this class and these can be transferred over the network,
 * stored in a serialized form on disk or exported to various
 * other formats like PDF, HTML, XLS, CSV or XML.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JasperPrint implements Serializable 
{
	
	/**
	 * A small class for implementing just the font provider functionality.
	 */
	private static class DefaultFontProvider implements JRDefaultFontProvider, Serializable
	{
		private static final long serialVersionUID = 10002;
		
		private JRReportFont defaultFont;

		DefaultFontProvider(JRReportFont font)
		{
			this.defaultFont = font;
		}

		public JRReportFont getDefaultFont()
		{
			return this.defaultFont;
		}

		void setDefaultFont(JRReportFont font)
		{
			this.defaultFont = font;
		}
	}


	/**
	 *
	 */
	private static final long serialVersionUID = 10002;

	/**
	 *
	 */
	private String name = null;
	private int pageWidth = 0;
	private int pageHeight = 0;
	private byte orientation = JRReport.ORIENTATION_PORTRAIT;

	private Map fontsMap = new HashMap();
	private List fontsList = new ArrayList();

	private List pages = new ArrayList();

	private transient Map anchorIndexes = null;
	private DefaultFontProvider defaultFontProvider = null;


	/**
	 * Creates a new empty document. 
	 */
	public JasperPrint()
	{
		defaultFontProvider = new DefaultFontProvider(null);
	}

	/**
	 * @return Returns the name of the document
	 */
	public String getName()
	{
		return this.name;
	}
		
	/**
	 * Sets the name of the document.
	 * 
	 * @param name name of the document
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return Returns the page width
	 */
	public int getPageWidth()
	{
		return this.pageWidth;
	}
		
	/**
	 * Sets the page width.
	 * 
	 * @param pageWidth page width
	 */
	public void setPageWidth(int pageWidth)
	{
		this.pageWidth = pageWidth;
	}

	/**
	 * @return Returns the page height.
	 */
	public int getPageHeight()
	{
		return this.pageHeight;
	}
		
	/**
	 * Sets the page height.
	 * 
	 * @param pageHeight page height
	 */
	public void setPageHeight(int pageHeight)
	{
		this.pageHeight = pageHeight;
	}


	/**
	 * Returns the page orientation.
	 * @see JRReport ORIENTATION_PORTRAIT,
	 * @see JRReport ORIENTATION_LANDSCAPE
	 */
	public byte getOrientation()
	{
		return this.orientation;
	}
		
	/**
	 * Sets the page orientation.
	 * @see JRReport ORIENTATION_PORTRAIT,
	 * @see JRReport ORIENTATION_LANDSCAPE
	 */
	public void setOrientation(byte orientation)
	{
		this.orientation = orientation;
	}

	/**
	 * Returns the default report font.
	 */
	public JRReportFont getDefaultFont()
	{
		return this.defaultFontProvider.getDefaultFont();
	}

	/**
	 * Sets the default report font.
	 */
	public void setDefaultFont(JRReportFont font)
	{
		this.defaultFontProvider.setDefaultFont(font);
	}

	/**
	 * When we want to virtualize pages, we want a font provider that
	 * is <i>not</i> the print object itself.
	 */
	public JRDefaultFontProvider getDefaultFontProvider()
	{
		return this.defaultFontProvider;
	}
		
	/**
	 * Gets an array of report fonts.
	 */
	public JRReportFont[] getFonts()
	{
		JRReportFont[] fontsArray = new JRReportFont[fontsList.size()];
		
		fontsList.toArray(fontsArray);

		return fontsArray;
	}

	/**
	 * Gets a list of report fonts.
	 */
	public List getFontsList()
	{
		return this.fontsList;
	}

	/**
	 * Gets a map of report fonts.
	 */
	public Map getFontsMap()
	{
		return this.fontsMap;
	}

	/**
	 * Adds a new font to the report fonts.
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
	public JRReportFont removeFont(String fontName)
	{
		return this.removeFont(
			(JRReportFont)this.fontsMap.get(fontName)
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
	 * Returns a list of all pages in the filled report.
	 */
	public List getPages()
	{
		return this.pages;
	}
		
	/**
	 * Adds a new page to the document.
	 */
	public void addPage(JRPrintPage page)
	{
		this.anchorIndexes = null;
		this.pages.add(page);
	}

	/**
	 * Adds a new page to the document, placing it at the specified index.
	 */
	public void addPage(int index, JRPrintPage page)
	{
		this.anchorIndexes = null;
		this.pages.add(index, page);
	}

	/**
	 * Removes a page from the document.
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

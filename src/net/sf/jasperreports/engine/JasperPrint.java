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
public class JasperPrint implements Serializable, JRPropertiesHolder
{
	
	/**
	 * A small class for implementing just the style provider functionality.
	 */
	private static class DefaultStyleProvider implements JRDefaultStyleProvider, Serializable
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
		
		private JRReportFont defaultFont;
		private JRStyle defaultStyle;

		DefaultStyleProvider(JRReportFont font, JRStyle style)
		{
			this.defaultFont = font;
			this.defaultStyle = style;
		}

		public JRReportFont getDefaultFont()
		{
			return defaultFont;
		}

		void setDefaultFont(JRReportFont font)
		{
			this.defaultFont = font;
		}

		public JRStyle getDefaultStyle()
		{
			return defaultStyle;
		}

		void setDefaultStyle(JRStyle style)
		{
			this.defaultStyle = style;
		}
	}


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private String name = null;
	private int pageWidth = 0;
	private int pageHeight = 0;
	private byte orientation = JRReport.ORIENTATION_PORTRAIT;

	private Map fontsMap = new HashMap();
	private List fontsList = new ArrayList();
	private Map stylesMap = new HashMap();
	private List stylesList = new ArrayList();
	private Map originsMap = new HashMap();
	private List originsList = new ArrayList();

	private List pages = new ArrayList();

	private transient Map anchorIndexes = null;
	private DefaultStyleProvider defaultStyleProvider = null;
	
	private String formatFactoryClass;
	private String localeCode;
	private String timeZoneId;
	
	private JRPropertiesMap propertiesMap;


	/**
	 * Creates a new empty document. 
	 */
	public JasperPrint()
	{
		defaultStyleProvider = new DefaultStyleProvider(null, null);

		propertiesMap = new JRPropertiesMap();
	}

	/**
	 * @return Returns the name of the document
	 */
	public String getName()
	{
		return name;
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
		return pageWidth;
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
		return pageHeight;
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
		return orientation;
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
	 * 
	 */
	public JRPropertiesMap getPropertiesMap()
	{
		return propertiesMap;
	}

	/**
	 *
	 */
	public String[] getPropertyNames()
	{
		return propertiesMap.getPropertyNames();
	}

	/**
	 *
	 */
	public String getProperty(String propName)
	{
		return propertiesMap.getProperty(propName);
	}

	/**
	 *
	 */
	public void setProperty(String propName, String value)
	{
		propertiesMap.setProperty(propName, value);
	}

	/**
	 *
	 */
	public void removeProperty(String propName)
	{
		propertiesMap.removeProperty(propName);
	}

	/**
	 * Returns the default report font.
	 */
	public JRReportFont getDefaultFont()
	{
		return defaultStyleProvider.getDefaultFont();
	}

	/**
	 * Sets the default report font.
	 */
	public void setDefaultFont(JRReportFont font)
	{
		defaultStyleProvider.setDefaultFont(font);
	}

	/**
	 * When we want to virtualize pages, we want a font provider that
	 * is <i>not</i> the print object itself.
	 */
	public JRDefaultFontProvider getDefaultFontProvider()
	{
		return defaultStyleProvider;
	}
		
	/**
	 * Gets an array of report fonts.
	 * @deprecated
	 */
	public JRReportFont[] getFonts()
	{
		JRReportFont[] fontsArray = new JRReportFont[fontsList.size()];
		
		fontsList.toArray(fontsArray);

		return fontsArray;
	}

	/**
	 * Gets a list of report fonts.
	 * @deprecated
	 */
	public List getFontsList()
	{
		return fontsList;
	}

	/**
	 * Gets a map of report fonts.
	 * @deprecated
	 */
	public Map getFontsMap()
	{
		return fontsMap;
	}

	/**
	 * Adds a new font to the report fonts.
	 * @deprecated
	 */
	public synchronized void addFont(JRReportFont reportFont) throws JRException
	{
		addFont(reportFont, false);
	}

	/**
	 * Adds a new font to the report fonts.
	 * @deprecated
	 */
	public synchronized void addFont(JRReportFont reportFont, boolean isIgnoreDuplicate) throws JRException
	{
		if (fontsMap.containsKey(reportFont.getName()))
		{
			if (!isIgnoreDuplicate)
			{
				throw new JRException("Duplicate declaration of report font : "	+ reportFont.getName());
			}
		}
		else
		{
			fontsList.add(reportFont);
			fontsMap.put(reportFont.getName(), reportFont);
			
			if (reportFont.isDefault())
			{
				setDefaultFont(reportFont);
			}
		}
	}

	/**
	 * @deprecated
	 */
	public synchronized JRReportFont removeFont(String fontName)
	{
		return removeFont(
			(JRReportFont)fontsMap.get(fontName)
			);
	}

	/**
	 * @deprecated
	 */
	public synchronized JRReportFont removeFont(JRReportFont reportFont)
	{
		if (reportFont != null)
		{
			if (reportFont.isDefault())
			{
				setDefaultFont(null);
			}

			fontsList.remove(reportFont);
			fontsMap.remove(reportFont.getName());
		}
		
		return reportFont;
	}

	/**
	 * Returns the default report style.
	 */
	public JRStyle getDefaultStyle()
	{
		return defaultStyleProvider.getDefaultStyle();
	}

	/**
	 * Sets the default report style.
	 */
	public synchronized void setDefaultStyle(JRStyle style)
	{
		defaultStyleProvider.setDefaultStyle(style);
	}

	/**
	 * When we want to virtualize pages, we want a style provider that
	 * is <i>not</i> the print object itself.
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider;
	}
		
	/**
	 * Gets an array of report styles.
	 */
	public JRStyle[] getStyles()
	{
		JRStyle[] stylesArray = new JRStyle[stylesList.size()];
		
		stylesList.toArray(stylesArray);

		return stylesArray;
	}

	/**
	 * Gets a list of report styles.
	 */
	public List getStylesList()
	{
		return stylesList;
	}

	/**
	 * Gets a map of report styles.
	 */
	public Map getStylesMap()
	{
		return stylesMap;
	}

	/**
	 * Adds a new style to the report styles.
	 */
	public synchronized void addStyle(JRStyle style) throws JRException
	{
		addStyle(style, false);
	}

	/**
	 * Adds a new style to the report styles.
	 */
	public synchronized void addStyle(JRStyle style, boolean isIgnoreDuplicate) throws JRException
	{
		if (stylesMap.containsKey(style.getName()))
		{
			if (!isIgnoreDuplicate)
			{
				throw new JRException("Duplicate declaration of report style : " + style.getName());
			}
		}
		else
		{
			stylesList.add(style);
			stylesMap.put(style.getName(), style);
			
			if (style.isDefault())
			{
				setDefaultStyle(style);
			}
		}
	}

	/**
	 *
	 */
	public synchronized JRStyle removeStyle(String styleName)
	{
		return removeStyle(
			(JRStyle)stylesMap.get(styleName)
			);
	}

	/**
	 *
	 */
	public synchronized JRStyle removeStyle(JRStyle style)
	{
		if (style != null)
		{
			if (style.isDefault())
			{
				setDefaultStyle(null);
			}

			stylesList.remove(style);
			stylesMap.remove(style.getName());
		}
		
		return style;
	}

	/**
	 * Gets an array of report origins.
	 */
	public JROrigin[] getOrigins()
	{
		return (JROrigin[]) originsList.toArray(new JROrigin[originsList.size()]);
	}

	/**
	 * Gets a list of report origins.
	 */
	public List getOriginsList()
	{
		return originsList;
	}

	/**
	 * Gets a map of report origins.
	 */
	public Map getOriginsMap()
	{
		return originsMap;
	}

	/**
	 * Adds a new style to the report origins.
	 */
	public synchronized void addOrigin(JROrigin origin)
	{
		if (!originsMap.containsKey(origin))
		{
			originsList.add(origin);
			originsMap.put(origin, new Integer(originsList.size() - 1));
		}
	}

	/**
	 *
	 */
	public synchronized JROrigin removeOrigin(JROrigin origin)
	{
		if (originsMap.containsKey(origin))
		{
			originsList.remove(origin);
			originsMap = new HashMap();
			for(int i = 0; i < originsList.size(); i++)
			{
				originsMap.put(originsList.get(i), new Integer(i));
			}
		}
		
		return origin;
	}

	/**
	 * Returns a list of all pages in the filled report.
	 */
	public List getPages()
	{
		return pages;
	}
		
	/**
	 * Adds a new page to the document.
	 */
	public synchronized void addPage(JRPrintPage page)
	{
		anchorIndexes = null;
		pages.add(page);
	}

	/**
	 * Adds a new page to the document, placing it at the specified index.
	 */
	public synchronized void addPage(int index, JRPrintPage page)
	{
		anchorIndexes = null;
		pages.add(index, page);
	}

	/**
	 * Removes a page from the document.
	 */
	public synchronized JRPrintPage removePage(int index)
	{
		anchorIndexes = null;
		return (JRPrintPage)pages.remove(index);
	}

	/**
	 *
	 */
	public synchronized Map getAnchorIndexes()
	{
		if (anchorIndexes == null)
		{
			anchorIndexes = new HashMap();
			
			int i = 0;
			for(Iterator itp = pages.iterator(); itp.hasNext(); i++)
			{
				JRPrintPage page = (JRPrintPage)itp.next();
				Collection elements = page.getElements();
				collectAnchors(elements, i, 0, 0);
			}
		}
		
		return anchorIndexes;
	}

	protected void collectAnchors(Collection elements, int pageIndex, int offsetX, int offsetY)
	{
		if (elements != null && elements.size() > 0)
		{
			JRPrintElement element = null;
			for(Iterator it = elements.iterator(); it.hasNext();)
			{
				element = (JRPrintElement)it.next();
				if (element instanceof JRPrintAnchor)
				{
					anchorIndexes.put(
						((JRPrintAnchor)element).getAnchorName(), 
						new JRPrintAnchorIndex(pageIndex, element, offsetX, offsetY)
						);
				}
				
				if (element instanceof JRPrintFrame)
				{
					JRPrintFrame frame = (JRPrintFrame) element;
					collectAnchors(frame.getElements(), pageIndex, offsetX + frame.getX(), offsetY + frame.getY());
				}
			}
		}
	}


	/**
	 * Returns the name of the class implementing the {@link net.sf.jasperreports.engine.util.FormatFactory FormatFactory}
	 * interface to use with this document.
	 */
	public String getFormatFactoryClass()
	{
		return formatFactoryClass;
	}


	/**
	 * Sets the name of the class implementing the {@link net.sf.jasperreports.engine.util.FormatFactory FormatFactory}
	 * interface to use with this document.
	 */
	public void setFormatFactoryClass(String formatFactoryClass)
	{
		this.formatFactoryClass = formatFactoryClass;
	}


	/**
	 * Returns the code of the default <code>java.util.Locale</code> to be used for the
	 * elements of this print object.
	 * <p>
	 * When filling a report, the value of the {@link JRParameter#REPORT_LOCALE REPORT_LOCALE} parameter 
	 * (or the default locale if the parameter has no explicit value) 
	 * is saved using this attribute.  Some elements (e.g. elements rendered by a subreport)
	 * in the print object can override this default locale.
	 * </p>
	 * 
	 * @return the code of the default <code>java.util.Locale</code> for this object
	 * @see JRPrintText#getLocaleCode()
	 */
	public String getLocaleCode()
	{
		return localeCode;
	}


	/**
	 * Sets the the code of the default <code>java.util.Locale</code> to be used for this object.
	 * 
	 * @param localeCode the locale code, using the {@link java.util.Locale#toString() java.util.Locale.toString()}
	 * convention.
	 * @see #getLocaleCode()
	 * @see java.util.Locale#toString()
	 */
	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}


	/**
	 * Returns the {@link java.util.TimeZone#getID() ID} of the default <code>java.util.TimeZone</code>
	 * to be used for the elements of this print object.
	 * <p>
	 * When filling a report, the value of the {@link JRParameter#REPORT_TIME_ZONE REPORT_TIME_ZONE} parameter
	 * (or the default time zine if the parameter has no explicit value) 
	 * is saved using this attribute.  Some elements (e.g. elements rendered by a subreport)
	 * in the print object can override this default time zone.
	 * </p>
	 * 
	 * @return the ID of the default <code>java.util.TimeZone</code> for this object
	 * @see JRPrintText#getTimeZoneId()
	 */
	public String getTimeZoneId()
	{
		return timeZoneId;
	}


	/**
	 * Sets the the {@link java.util.TimeZone#getID() ID} of the default <code>java.util.TimeZone</code>
	 * to be used for this object.
	 * 
	 * @param timeZoneId the time zone ID
	 * @see #getTimeZoneId()
	 * @see java.util.TimeZone#getID()
	 */
	public void setTimeZoneId(String timeZoneId)
	{
		this.timeZoneId = timeZoneId;
	}
		

}

/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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


/*
 * Contributors:
 * John Bindel - jbindel@users.sourceforge.net 
 */

package net.sf.jasperreports.engine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.base.StandardPrintParts;
import net.sf.jasperreports.engine.type.OrientationEnum;


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
 */
public class JasperPrint implements Serializable, JRPropertiesHolder
{

	public static final String EXCEPTION_MESSAGE_KEY_DUPLICATE_STYLE = "engine.jasper.print.duplicate.style";
	
	/**
	 * Prefix for JasperReports properties that specify properties to be
	 * transfered from report templates to print objects.
	 * 
	 * @see JRPropertiesUtil#transferProperties(JRPropertiesHolder, JRPropertiesHolder, String)
	 */
	public static final String PROPERTIES_PRINT_TRANSFER_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "print.transfer.";

	/**
	 * 
	 */
	public static final String PROPERTY_CREATE_BOOKMARKS = JRPropertiesUtil.PROPERTY_PREFIX + "print.create.bookmarks";

	/**
	 * 
	 */
	public static final String PROPERTY_COLLAPSE_MISSING_BOOKMARK_LEVELS = 
		JRPropertiesUtil.PROPERTY_PREFIX + "print.collapse.missing.bookmark.levels";
	
	/**
	 * A small class for implementing just the style provider functionality.
	 */
	private static class DefaultStyleProvider implements JRDefaultStyleProvider, Serializable
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
		
		private JRStyle defaultStyle;

		DefaultStyleProvider(JRStyle style)
		{
			this.defaultStyle = style;
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
	private String name;
	private int pageWidth;
	private int pageHeight;
	private Integer topMargin = 0;
	private Integer leftMargin = 0;
	private Integer bottomMargin = 0;
	private Integer rightMargin = 0;
	private OrientationEnum orientationValue = OrientationEnum.PORTRAIT;
	
	private transient PrintPageFormat pageFormat;

	private class DefaultPrintPageFormat implements PrintPageFormat
	{
		@Override
		public Integer getPageWidth() {
			return JasperPrint.this.getPageWidth();
		}

		@Override
		public Integer getPageHeight() {
			return JasperPrint.this.getPageHeight();
		}

		@Override
		public Integer getTopMargin() {
			return JasperPrint.this.getTopMargin();
		}

		@Override
		public Integer getLeftMargin() {
			return JasperPrint.this.getLeftMargin();
		}

		@Override
		public Integer getBottomMargin() {
			return JasperPrint.this.getBottomMargin();
		}

		@Override
		public Integer getRightMargin() {
			return JasperPrint.this.getRightMargin();
		}

		@Override
		public OrientationEnum getOrientation() {
			return JasperPrint.this.getOrientationValue();
		}
	}

	private Map<String, JRStyle> stylesMap = new HashMap<String, JRStyle>();
	private List<JRStyle> stylesList = new ArrayList<JRStyle>();
	private Map<JROrigin, Integer> originsMap = new HashMap<JROrigin, Integer>();
	private List<JROrigin> originsList = new ArrayList<JROrigin>();

	private PrintParts parts;
	//FIXME unsynchronize on serialization?
	private List<JRPrintPage> pages = Collections.synchronizedList(new ArrayList<JRPrintPage>());

	private transient Map<String,JRPrintAnchorIndex> anchorIndexes;
	private DefaultStyleProvider defaultStyleProvider;
	
	private String formatFactoryClass;
	private String localeCode;
	private String timeZoneId;
	
	private JRPropertiesMap propertiesMap;

	private List<PrintBookmark> bookmarks;
	

	/**
	 * Creates a new empty document. 
	 */
	public JasperPrint()
	{
		defaultStyleProvider = new DefaultStyleProvider(null);

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
	 * @return Returns the page format for specified page index.
	 */
	public PrintPageFormat getPageFormat(int pageIndex)
	{
		if (parts == null || !parts.hasParts())
		{
			return getPageFormat();
		}
		else
		{
			PrintPageFormat partPageFormat = parts.getPageFormat(pageIndex);
			if (partPageFormat == null)
			{
				return getPageFormat();
			}
			else
			{
				return partPageFormat;
			}
		}
	}

	/**
	 *
	 */
	public synchronized PrintPageFormat getPageFormat()
	{
		if (pageFormat == null)
		{
			pageFormat = new DefaultPrintPageFormat();
		}
		return pageFormat;
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
	 * @return Returns the top page margin
	 */
	public Integer getTopMargin()
	{
		return topMargin;
	}
		
	/**
	 * Sets the top page margin.
	 * 
	 * @param topMargin top page margin
	 */
	public void setTopMargin(Integer topMargin)
	{
		this.topMargin = topMargin;
	}

	/**
	 * @return Returns the left page margin
	 */
	public Integer getLeftMargin()
	{
		return leftMargin;
	}
		
	/**
	 * Sets the left page margin.
	 * 
	 * @param leftMargin left page margin
	 */
	public void setLeftMargin(Integer leftMargin)
	{
		this.leftMargin = leftMargin;
	}

	/**
	 * @return Returns the bottom page margin
	 */
	public Integer getBottomMargin()
	{
		return bottomMargin;
	}
		
	/**
	 * Sets the bottom page margin.
	 * 
	 * @param bottomMargin bottom page margin
	 */
	public void setBottomMargin(Integer bottomMargin)
	{
		this.bottomMargin = bottomMargin;
	}

	/**
	 * @return Returns the right page margin
	 */
	public Integer getRightMargin()
	{
		return rightMargin;
	}
		
	/**
	 * Sets the right page margin.
	 * 
	 * @param rightMargin right page margin
	 */
	public void setRightMargin(Integer rightMargin)
	{
		this.rightMargin = rightMargin;
	}

	/**
	 * Returns the page orientation.
	 * @see OrientationEnum PORTRAIT,
	 * @see OrientationEnum LANDSCAPE
	 */
	public OrientationEnum getOrientationValue()
	{
		return orientationValue;
	}
		
	/**
	 * Sets the page orientation.
	 * @see OrientationEnum PORTRAIT,
	 * @see OrientationEnum LANDSCAPE
	 */
	public void setOrientation(OrientationEnum orientationValue)
	{
		this.orientationValue = orientationValue;
	}

	public boolean hasProperties()
	{
		return propertiesMap != null && propertiesMap.hasProperties();
	}
	
	/**
	 * 
	 */
	public JRPropertiesMap getPropertiesMap()
	{
		return propertiesMap;
	}

	public JRPropertiesHolder getParentProperties()
	{
		return null;
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
	public List<JRStyle> getStylesList()
	{
		return stylesList;
	}

	/**
	 * Gets a map of report styles.
	 */
	public Map<String, JRStyle> getStylesMap()
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
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_DUPLICATE_STYLE,
						new Object[]{style.getName()});
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
		return removeStyle(stylesMap.get(styleName));
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
		return originsList.toArray(new JROrigin[originsList.size()]);
	}

	/**
	 * Gets a list of report origins.
	 */
	public List<JROrigin> getOriginsList()
	{
		return originsList;
	}

	/**
	 * Gets a map of report origins.
	 */
	public Map<JROrigin, Integer> getOriginsMap()
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
			originsMap.put(origin, Integer.valueOf(originsList.size() - 1));
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
			originsMap = new HashMap<JROrigin, Integer>();
			for(int i = 0; i < originsList.size(); i++)
			{
				originsMap.put(originsList.get(i), Integer.valueOf(i));
			}
		}
		
		return origin;
	}

	/**
	 * Determines whether this document contains parts.
	 * 
	 * @return whether this document contains parts
	 * @see #getParts()
	 */
	public boolean hasParts()
	{
		return parts != null && parts.hasParts();
	}
	
	/**
	 * Returns a list of all parts in the filled report.
	 */
	public PrintParts getParts()
	{
		return parts;
	}

	/**
	 * Adds a new part to the document.
	 */
	public synchronized void addPart(int pageIndex, PrintPart part)
	{
		if (parts == null)
		{
			parts = new StandardPrintParts();
		}

		parts.addPart(pageIndex, part);
	}

	/**
	 * Removes a part from the document.
	 */
	public synchronized PrintPart removePart(int pageIndex)
	{
		if (parts == null)
		{
			return null;
		}

		return parts.removePart(pageIndex);
	}

	/**
	 * Returns a list of all pages in the filled report.
	 */
	public List<JRPrintPage> getPages()
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
		return pages.remove(index);
	}

	/**
	 *
	 */
	public List<PrintBookmark> getBookmarks()
	{
		return bookmarks;
	}

	/**
	 * Adds a new page to the document.
	 */
	public synchronized void addBookmark(PrintBookmark bookmark)
	{
		if (bookmarks == null)
		{
			bookmarks = new ArrayList<PrintBookmark>();
		}
		bookmarks.add(bookmark);
	}
	
	/**
	 *
	 */
	public void setBookmarks(List<PrintBookmark> bookmarks)
	{
		this.bookmarks = bookmarks;
	}

	/**
	 *
	 */
	public synchronized Map<String,JRPrintAnchorIndex> getAnchorIndexes()
	{
		if (anchorIndexes == null)
		{
			anchorIndexes = new HashMap<String,JRPrintAnchorIndex>();
			
			int i = 0;
			for(Iterator<JRPrintPage> itp = pages.iterator(); itp.hasNext(); i++)
			{
				JRPrintPage page = itp.next();
				Collection<JRPrintElement> elements = page.getElements();
				collectAnchors(elements, i, 0, 0);
			}
		}
		
		return anchorIndexes;
	}

	protected void collectAnchors(Collection<JRPrintElement> elements, int pageIndex, int offsetX, int offsetY)
	{
		if (elements != null && elements.size() > 0)
		{
			JRPrintElement element = null;
			for(Iterator<JRPrintElement> it = elements.iterator(); it.hasNext();)
			{
				element = it.next();
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
		
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private byte orientation;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			orientationValue = OrientationEnum.getByValue(orientation);
		}
	}


}

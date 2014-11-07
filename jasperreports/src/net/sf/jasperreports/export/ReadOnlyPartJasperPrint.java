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
package net.sf.jasperreports.export;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPrintAnchorIndex;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.PrintBookmark;
import net.sf.jasperreports.engine.PrintPart;
import net.sf.jasperreports.engine.PrintParts;
import net.sf.jasperreports.engine.type.OrientationEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: SimpleExporterInput.java 6699 2013-11-08 10:19:30Z teodord $
 */
public class ReadOnlyPartJasperPrint extends JasperPrint
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private JasperPrint parentJasperPrint;
	private PrintPart part;
	private List<JRPrintPage> pages;
	
	public ReadOnlyPartJasperPrint(JasperPrint jasperPrint, PrintPart part, int startPageIndex, int endPageIndex)
	{
		this.parentJasperPrint = jasperPrint;
		this.part = part;
		this.pages = jasperPrint.getPages().subList(startPageIndex, endPageIndex);
	}

	public String getName()
	{
		return part == null ? parentJasperPrint.getName() : part.getName();
	}
		
	public void setName(String name)
	{
		throw new UnsupportedOperationException();
	}

	public int getPageWidth()
	{
		return part == null ? parentJasperPrint.getPageWidth() : part.getPageFormat().getPageWidth();
	}
		
	public void setPageWidth(int pageWidth)
	{
		throw new UnsupportedOperationException();
	}

	public int getPageHeight()
	{
		return part == null ? parentJasperPrint.getPageHeight() : part.getPageFormat().getPageHeight();
	}
		
	public void setPageHeight(int pageHeight)
	{
		throw new UnsupportedOperationException();
	}

	public Integer getTopMargin()
	{
		return part == null ? parentJasperPrint.getTopMargin() : part.getPageFormat().getTopMargin();
	}
		
	public void setTopMargin(Integer topMargin)
	{
		throw new UnsupportedOperationException();
	}

	public Integer getLeftMargin()
	{
		return part == null ? parentJasperPrint.getLeftMargin() : part.getPageFormat().getLeftMargin();
	}
		
	public void setLeftMargin(Integer leftMargin)
	{
		throw new UnsupportedOperationException();
	}

	public Integer getBottomMargin()
	{
		return part == null ? parentJasperPrint.getBottomMargin() : part.getPageFormat().getBottomMargin();
	}
		
	public void setBottomMargin(Integer bottomMargin)
	{
		throw new UnsupportedOperationException();
	}

	public Integer getRightMargin()
	{
		return part == null ? parentJasperPrint.getRightMargin() : part.getPageFormat().getRightMargin();
	}
		
	public void setRightMargin(Integer rightMargin)
	{
		throw new UnsupportedOperationException();
	}

	public OrientationEnum getOrientationValue()
	{
		return part == null ? parentJasperPrint.getOrientationValue() : part.getPageFormat().getOrientation();
	}
		
	public void setOrientation(OrientationEnum orientationValue)
	{
		throw new UnsupportedOperationException();
	}

	public boolean hasProperties()
	{
		return parentJasperPrint.hasProperties();
	}
	
	public JRPropertiesMap getPropertiesMap()
	{
		return parentJasperPrint.getPropertiesMap();
	}

	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}
	
	public String[] getPropertyNames()
	{
		return parentJasperPrint.getPropertyNames();
	}

	public String getProperty(String propName)
	{
		return parentJasperPrint.getProperty(propName);
	}

	public void setProperty(String propName, String value)
	{
		throw new UnsupportedOperationException();
	}

	public void removeProperty(String propName)
	{
		throw new UnsupportedOperationException();
	}

	public JRStyle getDefaultStyle()
	{
		return parentJasperPrint.getDefaultStyle();
	}

	public synchronized void setDefaultStyle(JRStyle style)
	{
		throw new UnsupportedOperationException();
	}

	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return parentJasperPrint.getDefaultStyleProvider();
	}
		
	public JRStyle[] getStyles()
	{
		return parentJasperPrint.getStyles();
	}

	public List<JRStyle> getStylesList()
	{
		return parentJasperPrint.getStylesList();
	}

	public Map<String, JRStyle> getStylesMap()
	{
		return parentJasperPrint.getStylesMap();
	}

	public synchronized void addStyle(JRStyle style) throws JRException
	{
		throw new UnsupportedOperationException();
	}

	public synchronized void addStyle(JRStyle style, boolean isIgnoreDuplicate) throws JRException
	{
		throw new UnsupportedOperationException();
	}

	public synchronized JRStyle removeStyle(String styleName)
	{
		throw new UnsupportedOperationException();
	}

	public synchronized JRStyle removeStyle(JRStyle style)
	{
		throw new UnsupportedOperationException();
	}

	public JROrigin[] getOrigins()
	{
		return parentJasperPrint.getOrigins();
	}

	public List<JROrigin> getOriginsList()
	{
		return parentJasperPrint.getOriginsList();
	}

	public Map<JROrigin, Integer> getOriginsMap()
	{
		return parentJasperPrint.getOriginsMap();
	}

	public synchronized void addOrigin(JROrigin origin)
	{
		throw new UnsupportedOperationException();
	}

	public synchronized JROrigin removeOrigin(JROrigin origin)
	{
		throw new UnsupportedOperationException();
	}

	public PrintParts getParts()
	{
		return null;
	}

	public synchronized void addPart(int pageIndex, PrintPart part)
	{
		throw new UnsupportedOperationException();
	}

	public synchronized PrintPart removePart(int pageIndex)
	{
		throw new UnsupportedOperationException();
	}

	public List<JRPrintPage> getPages()
	{
		return pages;
	}

	public synchronized void addPage(JRPrintPage page)
	{
		throw new UnsupportedOperationException();
	}

	public synchronized void addPage(int index, JRPrintPage page)
	{
		throw new UnsupportedOperationException();
	}

	public synchronized JRPrintPage removePage(int index)
	{
		throw new UnsupportedOperationException();
	}

	public List<PrintBookmark> getBookmarks()
	{
		return parentJasperPrint.getBookmarks();
	}

	public synchronized void addBookmark(PrintBookmark bookmark)
	{
		throw new UnsupportedOperationException();
	}
	
	public void setBookmarks(List<PrintBookmark> bookmarks)
	{
		throw new UnsupportedOperationException();
	}

	public synchronized Map<String,JRPrintAnchorIndex> getAnchorIndexes()
	{
		return parentJasperPrint.getAnchorIndexes();
	}

	public String getFormatFactoryClass()
	{
		return parentJasperPrint.getFormatFactoryClass();
	}

	public void setFormatFactoryClass(String formatFactoryClass)
	{
		throw new UnsupportedOperationException();
	}

	public String getLocaleCode()
	{
		return parentJasperPrint.getLocaleCode();
	}

	public void setLocaleCode(String localeCode)
	{
		throw new UnsupportedOperationException();
	}

	public String getTimeZoneId()
	{
		return parentJasperPrint.getTimeZoneId();
	}

	public void setTimeZoneId(String timeZoneId)
	{
		throw new UnsupportedOperationException();
	}
}
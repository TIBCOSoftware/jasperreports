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


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class SimpleTextReportConfiguration extends SimpleReportExportConfiguration implements TextReportConfiguration
{
	private Float charWidth;
	private Float charHeight;
	private Integer pageWidthInChars;
	private Integer pageHeightInChars;

	
	/**
	 * 
	 */
	public SimpleTextReportConfiguration()
	{
	}

	/**
	 * 
	 */
	public Float getCharWidth()
	{
		return charWidth;
	}

	/**
	 * 
	 */
	public void setCharWidth(Float charWidth)
	{
		this.charWidth = charWidth;
	}

	/**
	 * 
	 */
	public Float getCharHeight()
	{
		return charHeight;
	}

	/**
	 * 
	 */
	public void setCharHeight(Float charHeight)
	{
		this.charHeight = charHeight;
	}

	/**
	 * 
	 */
	public Integer getPageWidthInChars()
	{
		return pageWidthInChars;
	}

	/**
	 * 
	 */
	public void setPageWidthInChars(Integer pageWidthInChars)
	{
		this.pageWidthInChars = pageWidthInChars;
	}

	/**
	 * 
	 */
	public Integer getPageHeightInChars()
	{
		return pageHeightInChars;
	}

	/**
	 * 
	 */
	public void setPageHeightInChars(Integer pageHeightInChars)
	{
		this.pageHeightInChars = pageHeightInChars;
	}
}

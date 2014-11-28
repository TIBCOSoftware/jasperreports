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

import net.sf.jasperreports.export.type.HtmlSizeUnitEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class SimpleHtmlReportConfiguration extends SimpleReportExportConfiguration implements HtmlReportConfiguration
{
	private Boolean isRemoveEmptySpaceBetweenRows;
	private Boolean isWhitePageBackground;
	private Boolean isWrapBreakWord;
	private HtmlSizeUnitEnum sizeUnit;
	private String borderCollapse;
	private Boolean isIgnorePageMargins;
	private Boolean accessibleHtml;
	private Float zoomRatio;
	private Boolean isIgnoreHyperlink;

	
	/**
	 * 
	 */
	public SimpleHtmlReportConfiguration()
	{
	}

	/**
	 * 
	 */
	public Boolean isRemoveEmptySpaceBetweenRows()
	{
		return isRemoveEmptySpaceBetweenRows;
	}
	
	/**
	 * 
	 */
	public void setRemoveEmptySpaceBetweenRows(Boolean isRemoveEmptySpaceBetweenRows)
	{
		this.isRemoveEmptySpaceBetweenRows = isRemoveEmptySpaceBetweenRows;
	}

	/**
	 * 
	 */
	public Boolean isWhitePageBackground()
	{
		return isWhitePageBackground;
	}

	/**
	 * 
	 */
	public void setWhitePageBackground(Boolean isWhitePageBackground)
	{
		this.isWhitePageBackground = isWhitePageBackground;
	}
	
	/**
	 * 
	 */
	public Boolean isWrapBreakWord()
	{
		return isWrapBreakWord;
	}
	
	/**
	 * 
	 */
	public void setWrapBreakWord(Boolean isWrapBreakWord)
	{
		this.isWrapBreakWord = isWrapBreakWord;
	}
	
	/**
	 * 
	 */
	public HtmlSizeUnitEnum getSizeUnit()
	{
		return sizeUnit;
	}
	
	/**
	 * 
	 */
	public void setSizeUnit(HtmlSizeUnitEnum sizeUnit)
	{
		this.sizeUnit = sizeUnit;
	}
	
	/**
	 * 
	 */
	public String getBorderCollapse()
	{
		return borderCollapse;
	}
	
	/**
	 * 
	 */
	public void setBorderCollapse(String borderCollapse)
	{
		this.borderCollapse = borderCollapse;
	}
	
	/**
	 * 
	 */
	public Boolean isIgnorePageMargins()
	{
		return isIgnorePageMargins;
	}
	
	/**
	 * 
	 */
	public void setIgnorePageMargins(Boolean isIgnorePageMargins)
	{
		this.isIgnorePageMargins = isIgnorePageMargins;
	}
	
	/**
	 * 
	 */
	public Boolean isAccessibleHtml()
	{
		return accessibleHtml;
	}
	
	/**
	 * 
	 */
	public void setAccessibleHtml(Boolean accessibleHtml)
	{
		this.accessibleHtml = accessibleHtml;
	}
	
	/**
	 * 
	 */
	public Float getZoomRatio()
	{
		return zoomRatio;
	}
	
	/**
	 * 
	 */
	public void setZoomRatio(Float zoomRatio)
	{
		this.zoomRatio = zoomRatio;
	}
	
	/**
	 * 
	 */
	public Boolean isIgnoreHyperlink()
	{
		return isIgnoreHyperlink;
	}
	
	/**
	 * 
	 */
	public void setIgnoreHyperlink(Boolean isIgnoreHyperlink)
	{
		this.isIgnoreHyperlink = isIgnoreHyperlink;
	}
}

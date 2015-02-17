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
public class SimplePdfReportConfiguration extends SimpleReportExportConfiguration implements PdfReportConfiguration
{
	private Boolean isForceSvgShapes;
	private Boolean isCollapseMissingBookmarkLevels;
	private Boolean isSizePageToContent;
	private Boolean isIgnoreHyperlink;
	private Boolean isForceLineBreakPolicy;
	private Integer oddPageOffsetX;
	private Integer oddPageOffsetY;
	private Integer evenPageOffsetX;
	private Integer evenPageOffsetY;

	
	/**
	 * 
	 */
	public SimplePdfReportConfiguration()
	{
	}
	
	/**
	 * 
	 */
	public Boolean isForceSvgShapes()
	{
		return isForceSvgShapes;
	}
	
	/**
	 * 
	 */
	public void setForceSvgShapes(Boolean isForceSvgShapes)
	{
		this.isForceSvgShapes = isForceSvgShapes;
	}
	
	/**
	 * 
	 */
	public Boolean isCollapseMissingBookmarkLevels()
	{
		return isCollapseMissingBookmarkLevels;
	}
	
	/**
	 * 
	 */
	public void setCollapseMissingBookmarkLevels(Boolean isCollapseMissingBookmarkLevels)
	{
		this.isCollapseMissingBookmarkLevels = isCollapseMissingBookmarkLevels;
	}
	
	/**
	 * 
	 */
	public Boolean isSizePageToContent()
	{
		return isSizePageToContent;
	}
	
	/**
	 * 
	 */
	public void setSizePageToContent(Boolean isSizePageToContent)
	{
		this.isSizePageToContent = isSizePageToContent;
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
	
	/**
	 * 
	 */
	public Boolean isForceLineBreakPolicy()
	{
		return isForceLineBreakPolicy;
	}
	
	/**
	 * 
	 */
	public void setForceLineBreakPolicy(Boolean isForceLineBreakPolicy)
	{
		this.isForceLineBreakPolicy = isForceLineBreakPolicy;
	}
	
	/**
	 * 
	 */
	public Integer getOddPageOffsetX()
	{
		return oddPageOffsetX;
	}
	
	/**
	 * 
	 */
	public void setOddPageOffsetX(Integer oddPageOffsetX)
	{
		this.oddPageOffsetX = oddPageOffsetX;
	}
	
	/**
	 * 
	 */
	public Integer getOddPageOffsetY()
	{
		return oddPageOffsetY;
	}
	
	/**
	 * 
	 */
	public void setOddPageOffsetY(Integer oddPageOffsetY)
	{
		this.oddPageOffsetY = oddPageOffsetY;
	}
	
	/**
	 * 
	 */
	public Integer getEvenPageOffsetX()
	{
		return evenPageOffsetX;
	}
	
	/**
	 * 
	 */
	public void setEvenPageOffsetX(Integer evenPageOffsetX)
	{
		this.evenPageOffsetX = evenPageOffsetX;
	}
	
	/**
	 * 
	 */
	public Integer getEvenPageOffsetY()
	{
		return evenPageOffsetY;
	}
	
	/**
	 * 
	 */
	public void setEvenPageOffsetY(Integer evenPageOffsetY)
	{
		this.evenPageOffsetY = evenPageOffsetY;
	}
}

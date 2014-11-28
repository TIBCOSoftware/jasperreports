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
public class SimpleGraphics2DReportConfiguration extends SimpleReportExportConfiguration implements Graphics2DReportConfiguration
{
	private Float zoomRatio;
	private Boolean isMinimizePrinterJobSize;
	private Boolean isIgnoreMissingFont;

	/**
	 * 
	 */
	public SimpleGraphics2DReportConfiguration()
	{
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
	public Boolean isMinimizePrinterJobSize()
	{
		return isMinimizePrinterJobSize;
	}
	
	/**
	 * 
	 */
	public void setMinimizePrinterJobSize(Boolean isMinimizePrinterJobSize)
	{
		this.isMinimizePrinterJobSize = isMinimizePrinterJobSize;
	}

	/**
	 * 
	 */
	public Boolean isIgnoreMissingFont()
	{
		return isIgnoreMissingFont;
	}
	
	/**
	 * 
	 */
	public void setIgnoreMissingFont(Boolean isIgnoreMissingFont)
	{
		this.isIgnoreMissingFont = isIgnoreMissingFont;
	}
}

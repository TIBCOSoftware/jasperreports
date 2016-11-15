/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.export;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class SheetPrintSettings 
{

	private Integer topMargin;
	private Integer leftMargin;
	private Integer bottomMargin;
	private Integer rightMargin;
	private Integer headerMargin;
	private Integer footerMargin;
	
	private String headerLeft;
	private String headerCenter;
	private String headerRight;
	private String footerLeft;
	private String footerCenter;
	private String footerRight;
	
	public SheetPrintSettings() 
	{
	}
	
	public Integer getTopMargin() 
	{
		return topMargin;
	}

	public void setTopMargin(Integer topMargin) 
	{
		this.topMargin = topMargin;
	}

	public Integer getLeftMargin() 
	{
		return leftMargin;
	}

	public void setLeftMargin(Integer leftMargin) 
	{
		this.leftMargin = leftMargin;
	}

	public Integer getBottomMargin() 
	{
		return bottomMargin;
	}

	public void setBottomMargin(Integer bottomMargin) 
	{
		this.bottomMargin = bottomMargin;
	}

	public Integer getRightMargin() 
	{
		return rightMargin;
	}

	public void setRightMargin(Integer rightMargin) 
	{
		this.rightMargin = rightMargin;
	}

	public Integer getHeaderMargin() 
	{
		return headerMargin;
	}

	public void setHeaderMargin(Integer headerMargin) 
	{
		this.headerMargin = headerMargin;
	}

	public Integer getFooterMargin() 
	{
		return footerMargin;
	}

	public void setFooterMargin(Integer footerMargin) 
	{
		this.footerMargin = footerMargin;
	}

	public String getHeaderLeft() 
	{
		return headerLeft;
	}

	public void setHeaderLeft(String headerLeft) 
	{
		this.headerLeft = headerLeft;
	}

	public String getHeaderCenter() 
	{
		return headerCenter;
	}

	public void setHeaderCenter(String headerCenter) 
	{
		this.headerCenter = headerCenter;
	}

	public String getHeaderRight() 
	{
		return headerRight;
	}

	public void setHeaderRight(String headerRight) 
	{
		this.headerRight = headerRight;
	}

	public String getFooterLeft() 
	{
		return footerLeft;
	}

	public void setFooterLeft(String footerLeft) 
	{
		this.footerLeft = footerLeft;
	}

	public String getFooterCenter() 
	{
		return footerCenter;
	}

	public void setFooterCenter(String footerCenter) 
	{
		this.footerCenter = footerCenter;
	}

	public String getFooterRight() 
	{
		return footerRight;
	}

	public void setFooterRight(String footerRight) 
	{
		this.footerRight = footerRight;
	}
}

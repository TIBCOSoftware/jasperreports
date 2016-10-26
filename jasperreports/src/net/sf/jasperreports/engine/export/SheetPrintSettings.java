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

import java.util.Map;

import net.sf.jasperreports.export.XlsReportConfiguration;
import static net.sf.jasperreports.export.XlsReportConfiguration.*;

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
	
	public SheetPrintSettings(XlsReportConfiguration configuration) 
	{
		if(configuration != null)
		{
				this.topMargin = configuration.getPrintPageTopMargin();
				this.leftMargin = configuration.getPrintPageLeftMargin();
				this.bottomMargin = configuration.getPrintPageBottomMargin();
				this.rightMargin = configuration.getPrintPageRightMargin();
				this.headerMargin = configuration.getPrintHeaderMargin();
				this.footerMargin = configuration.getPrintFooterMargin();
				this.headerLeft = configuration.getSheetHeaderLeft();
				this.headerCenter = configuration.getSheetHeaderCenter();
				this.headerRight = configuration.getSheetHeaderRight();
				this.footerLeft = configuration.getSheetFooterLeft();
				this.footerCenter = configuration.getSheetFooterCenter();
				this.footerRight = configuration.getSheetFooterRight();
		}
	}
	
	public SheetPrintSettings(CutsInfo xCuts, XlsReportConfiguration configuration) 
	{
		this(configuration);
		if(xCuts != null)
		{
			Map<String,Object> xCutsProperties = xCuts.getPropertiesMap();
			if(xCutsProperties.containsKey(PROPERTY_PRINT_PAGE_TOP_MARGIN))
			{
				this.topMargin = (Integer)xCutsProperties.get(PROPERTY_PRINT_PAGE_TOP_MARGIN);
			}
			if(xCutsProperties.containsKey(PROPERTY_PRINT_PAGE_LEFT_MARGIN))
			{
				this.leftMargin = (Integer)xCutsProperties.get(PROPERTY_PRINT_PAGE_LEFT_MARGIN);
			}
			if(xCutsProperties.containsKey(PROPERTY_PRINT_PAGE_BOTTOM_MARGIN))
			{
				this.bottomMargin = (Integer)xCutsProperties.get(PROPERTY_PRINT_PAGE_BOTTOM_MARGIN);
			}
			if(xCutsProperties.containsKey(PROPERTY_PRINT_PAGE_RIGHT_MARGIN))
			{
				this.rightMargin = (Integer)xCutsProperties.get(PROPERTY_PRINT_PAGE_RIGHT_MARGIN);
			}
			if(xCutsProperties.containsKey(PROPERTY_PRINT_HEADER_MARGIN))
			{
				this.headerMargin = (Integer)xCutsProperties.get(PROPERTY_PRINT_HEADER_MARGIN);
			}
			if(xCutsProperties.containsKey(PROPERTY_PRINT_FOOTER_MARGIN))
			{
				this.footerMargin = (Integer)xCutsProperties.get(PROPERTY_PRINT_FOOTER_MARGIN);
			}
			if(xCutsProperties.containsKey(PROPERTY_SHEET_HEADER_LEFT))
			{
				this.headerLeft = (String)xCutsProperties.get(PROPERTY_SHEET_HEADER_LEFT);
			}
			if(xCutsProperties.containsKey(PROPERTY_SHEET_HEADER_CENTER))
			{
				this.headerCenter = (String)xCutsProperties.get(PROPERTY_SHEET_HEADER_CENTER);
			}
			if(xCutsProperties.containsKey(PROPERTY_SHEET_HEADER_RIGHT))
			{
				this.headerRight = (String)xCutsProperties.get(PROPERTY_SHEET_HEADER_RIGHT);
			}
			if(xCutsProperties.containsKey(PROPERTY_SHEET_FOOTER_LEFT))
			{
				this.footerLeft = (String)xCutsProperties.get(PROPERTY_SHEET_FOOTER_LEFT);
			}
			if(xCutsProperties.containsKey(PROPERTY_SHEET_FOOTER_CENTER))
			{
				this.footerCenter = (String)xCutsProperties.get(PROPERTY_SHEET_FOOTER_CENTER);
			}
			if(xCutsProperties.containsKey(PROPERTY_SHEET_FOOTER_RIGHT))
			{
				this.footerRight = (String)xCutsProperties.get(PROPERTY_SHEET_FOOTER_RIGHT);
			}
		}
	}
	
	public Integer getTopMargin() 
	{
		return topMargin;
	}

	public Integer getLeftMargin() 
	{
		return leftMargin;
	}

	public Integer getBottomMargin() 
	{
		return bottomMargin;
	}

	public Integer getRightMargin() 
	{
		return rightMargin;
	}

	public Integer getHeaderMargin() 
	{
		return headerMargin;
	}

	public Integer getFooterMargin() 
	{
		return footerMargin;
	}

	public String getHeaderLeft() 
	{
		return headerLeft;
	}

	public String getHeaderCenter() 
	{
		return headerCenter;
	}

	public String getHeaderRight() 
	{
		return headerRight;
	}

	public String getFooterLeft() 
	{
		return footerLeft;
	}

	public String getFooterCenter() 
	{
		return footerCenter;
	}

	public String getFooterRight() 
	{
		return footerRight;
	}

}

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
package net.sf.jasperreports.engine.export.ooxml;

import java.util.Map;

import net.sf.jasperreports.engine.export.CutsInfo;
import net.sf.jasperreports.export.XlsReportConfiguration;
import static net.sf.jasperreports.export.XlsReportConfiguration.*;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class XlsxPrintSettings 
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
	
	public XlsxPrintSettings() 
	{
	}
	public XlsxPrintSettings(CutsInfo xCuts, XlsReportConfiguration configuration) 
	{
		Map<String,Object> xCutsProperties = xCuts.getPropertiesMap();
		Integer pageMarginTop = xCutsProperties.containsKey(PROPERTY_PRINT_PAGE_TOP_MARGIN) 
				? (Integer)xCutsProperties.get(PROPERTY_PRINT_PAGE_TOP_MARGIN)
				: configuration.getPrintPageTopMargin();
		this.topMargin = pageMarginTop;
		
		Integer pageMarginLeft = xCutsProperties.containsKey(PROPERTY_PRINT_PAGE_LEFT_MARGIN) 
				? (Integer)xCutsProperties.get(PROPERTY_PRINT_PAGE_LEFT_MARGIN)
				: configuration.getPrintPageLeftMargin();
		this.leftMargin = pageMarginLeft;

		Integer pageMarginBottom = xCutsProperties.containsKey(PROPERTY_PRINT_PAGE_BOTTOM_MARGIN) 
				? (Integer)xCutsProperties.get(PROPERTY_PRINT_PAGE_BOTTOM_MARGIN)
				: configuration.getPrintPageBottomMargin();
		this.bottomMargin = pageMarginBottom;
		
		Integer pageMarginRight = xCutsProperties.containsKey(PROPERTY_PRINT_PAGE_RIGHT_MARGIN) 
				? (Integer)xCutsProperties.get(PROPERTY_PRINT_PAGE_RIGHT_MARGIN)
				: configuration.getPrintPageRightMargin();
		this.rightMargin = pageMarginRight;
		
		String sheetHeaderLeft = xCutsProperties.containsKey(PROPERTY_SHEET_HEADER_LEFT) 
				? (String)xCutsProperties.get(PROPERTY_SHEET_HEADER_LEFT)
				: configuration.getSheetHeaderLeft();
		this.headerLeft = sheetHeaderLeft;
		
		String sheetHeaderCenter = xCutsProperties.containsKey(PROPERTY_SHEET_HEADER_CENTER) 
				? (String)xCutsProperties.get(PROPERTY_SHEET_HEADER_CENTER)
				: configuration.getSheetHeaderCenter();
		this.headerCenter = sheetHeaderCenter;
		
		String sheetHeaderRight = xCutsProperties.containsKey(PROPERTY_SHEET_HEADER_RIGHT) 
				? (String)xCutsProperties.get(PROPERTY_SHEET_HEADER_RIGHT)
				: configuration.getSheetHeaderRight();
		this.headerRight = sheetHeaderRight;
		
		String sheetFooterLeft = xCutsProperties.containsKey(PROPERTY_SHEET_FOOTER_LEFT) 
				? (String)xCutsProperties.get(PROPERTY_SHEET_FOOTER_LEFT)
				: configuration.getSheetFooterLeft();
		this.footerLeft = sheetFooterLeft;
		
		String sheetFooterCenter = xCutsProperties.containsKey(PROPERTY_SHEET_FOOTER_CENTER) 
				? (String)xCutsProperties.get(PROPERTY_SHEET_FOOTER_CENTER)
				: configuration.getSheetFooterCenter();
		this.footerCenter = sheetFooterCenter;
		
		String sheetFooterRight = xCutsProperties.containsKey(PROPERTY_SHEET_FOOTER_RIGHT) 
				? (String)xCutsProperties.get(PROPERTY_SHEET_FOOTER_RIGHT)
				: configuration.getSheetFooterRight();
		this.footerRight = sheetFooterRight;
		
		Integer printHeaderMargin = xCutsProperties.containsKey(PROPERTY_PRINT_HEADER_MARGIN) 
				? (Integer)xCutsProperties.get(PROPERTY_PRINT_HEADER_MARGIN)
				: configuration.getPrintHeaderMargin();
		this.headerMargin = printHeaderMargin;
				
		Integer printFooterMargin = xCutsProperties.containsKey(PROPERTY_PRINT_FOOTER_MARGIN) 
				? (Integer)xCutsProperties.get(PROPERTY_PRINT_FOOTER_MARGIN)
				: configuration.getPrintFooterMargin();
		this.footerMargin = printFooterMargin;
		
	}
	
	public double getTopMargin() 
	{
		return topMargin;
	}

	public double getLeftMargin() 
	{
		return leftMargin;
	}

	public double getBottomMargin() 
	{
		return bottomMargin;
	}

	public double getRightMargin() 
	{
		return rightMargin;
	}

	public double getHeaderMargin() 
	{
		return headerMargin;
	}

	public double getFooterMargin() 
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

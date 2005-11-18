/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JasperDesignFactory extends JRBaseFactory
{

	
	/**
	 *
	 */
	private static final String ATTRIBUTE_name = "name";
	private static final String ATTRIBUTE_language = "language";
	private static final String ATTRIBUTE_columnCount = "columnCount";
	private static final String ATTRIBUTE_printOrder = "printOrder";
	private static final String ATTRIBUTE_pageWidth = "pageWidth";
	private static final String ATTRIBUTE_pageHeight = "pageHeight";
	private static final String ATTRIBUTE_orientation = "orientation";
	private static final String ATTRIBUTE_whenNoDataType = "whenNoDataType";
	private static final String ATTRIBUTE_columnWidth = "columnWidth";
	private static final String ATTRIBUTE_columnSpacing = "columnSpacing";
	private static final String ATTRIBUTE_leftMargin = "leftMargin";
	private static final String ATTRIBUTE_rightMargin = "rightMargin";
	private static final String ATTRIBUTE_topMargin = "topMargin";
	private static final String ATTRIBUTE_bottomMargin = "bottomMargin";
	private static final String ATTRIBUTE_isTitleNewPage = "isTitleNewPage";
	private static final String ATTRIBUTE_isSummaryNewPage = "isSummaryNewPage";
	private static final String ATTRIBUTE_isFloatColumnFooter = "isFloatColumnFooter";
	private static final String ATTRIBUTE_scriptletClass = "scriptletClass";
	private static final String ATTRIBUTE_resourceBundle = "resourceBundle";
	private static final String ATTRIBUTE_whenResourceMissingType = "whenResourceMissingType";
	public static final String ATTRIBUTE_isIgnorePagination = "isIgnorePagination";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JasperDesign jasperDesign = new JasperDesign();
		
		jasperDesign.setName(atts.getValue(ATTRIBUTE_name));

		jasperDesign.setLanguage(atts.getValue(ATTRIBUTE_language));
		
		String columnCount = atts.getValue(ATTRIBUTE_columnCount);
		if (columnCount != null && columnCount.length() > 0)
		{
			jasperDesign.setColumnCount(Integer.parseInt(columnCount));
		}

		Byte printOrder = (Byte)JRXmlConstants.getPrintOrderMap().get(atts.getValue(ATTRIBUTE_printOrder));
		if (printOrder != null)
		{
			jasperDesign.setPrintOrder(printOrder.byteValue());
		}

		String pageWidth = atts.getValue(ATTRIBUTE_pageWidth);
		if (pageWidth != null && pageWidth.length() > 0)
		{
			jasperDesign.setPageWidth(Integer.parseInt(pageWidth));
		}
		
		String pageHeight = atts.getValue(ATTRIBUTE_pageHeight);
		if (pageHeight != null && pageHeight.length() > 0)
		{
			jasperDesign.setPageHeight(Integer.parseInt(pageHeight));
		}

		Byte orientation = (Byte)JRXmlConstants.getOrientationMap().get(atts.getValue(ATTRIBUTE_orientation));
		if (orientation != null)
		{
			jasperDesign.setOrientation(orientation.byteValue());
		}

		Byte whenNoDataType = (Byte)JRXmlConstants.getWhenNoDataTypeMap().get(atts.getValue(ATTRIBUTE_whenNoDataType));
		if (whenNoDataType != null)
		{
			jasperDesign.setWhenNoDataType(whenNoDataType.byteValue());
		}

		String columnWidth = atts.getValue(ATTRIBUTE_columnWidth);
		if (columnWidth != null && columnWidth.length() > 0)
		{
			jasperDesign.setColumnWidth(Integer.parseInt(columnWidth));
		}

		String columnSpacing = atts.getValue(ATTRIBUTE_columnSpacing);
		if (columnSpacing != null && columnSpacing.length() > 0)
		{
			jasperDesign.setColumnSpacing(Integer.parseInt(columnSpacing));
		}

		String leftMargin = atts.getValue(ATTRIBUTE_leftMargin);
		if (leftMargin != null && leftMargin.length() > 0)
		{
			jasperDesign.setLeftMargin(Integer.parseInt(leftMargin));
		}

		String rightMargin = atts.getValue(ATTRIBUTE_rightMargin);
		if (rightMargin != null && rightMargin.length() > 0)
		{
			jasperDesign.setRightMargin(Integer.parseInt(rightMargin));
		}

		String topMargin = atts.getValue(ATTRIBUTE_topMargin);
		if (topMargin != null && topMargin.length() > 0)
		{
			jasperDesign.setTopMargin(Integer.parseInt(topMargin));
		}

		String bottomMargin = atts.getValue(ATTRIBUTE_bottomMargin);
		if (bottomMargin != null && bottomMargin.length() > 0)
		{
			jasperDesign.setBottomMargin(Integer.parseInt(bottomMargin));
		}

		String isTitleNewPage = atts.getValue(ATTRIBUTE_isTitleNewPage);
		if (isTitleNewPage != null && isTitleNewPage.length() > 0)
		{
			jasperDesign.setTitleNewPage(Boolean.valueOf(isTitleNewPage).booleanValue());
		}

		String isSummaryNewPage = atts.getValue(ATTRIBUTE_isSummaryNewPage);
		if (isSummaryNewPage != null && isSummaryNewPage.length() > 0)
		{
			jasperDesign.setSummaryNewPage(Boolean.valueOf(isSummaryNewPage).booleanValue());
		}

		String isFloatColumnFooter = atts.getValue(ATTRIBUTE_isFloatColumnFooter);
		if (isFloatColumnFooter != null && isFloatColumnFooter.length() > 0)
		{
			jasperDesign.setFloatColumnFooter(Boolean.valueOf(isFloatColumnFooter).booleanValue());
		}

		jasperDesign.setScriptletClass(atts.getValue(ATTRIBUTE_scriptletClass));
		jasperDesign.setResourceBundle(atts.getValue(ATTRIBUTE_resourceBundle));

		Byte whenResourceMissingType = (Byte)JRXmlConstants.getWhenResourceMissingTypeMap().get(atts.getValue(ATTRIBUTE_whenResourceMissingType));
		if (whenResourceMissingType != null)
		{
			jasperDesign.setWhenResourceMissingType(whenResourceMissingType.byteValue());
		}

		String isIgnorePagination = atts.getValue(ATTRIBUTE_isIgnorePagination);
		if (isIgnorePagination != null && isIgnorePagination.length() > 0)
		{
			jasperDesign.setIgnorePagination(Boolean.valueOf(isIgnorePagination).booleanValue());
		}

		return jasperDesign;
	}
	

}

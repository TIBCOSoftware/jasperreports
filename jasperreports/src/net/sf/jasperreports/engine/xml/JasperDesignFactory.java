/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
	public Object createObject(Attributes atts)
	{
		JasperDesign jasperDesign = new JasperDesign();
		
		jasperDesign.setName(atts.getValue(XmlConstants.ATTRIBUTE_name));

		jasperDesign.setLanguage(atts.getValue(XmlConstants.ATTRIBUTE_language));
		
		String columnCount = atts.getValue(XmlConstants.ATTRIBUTE_columnCount);
		if (columnCount != null && columnCount.length() > 0)
		{
			jasperDesign.setColumnCount(Integer.parseInt(columnCount));
		}

		Byte printOrder = (Byte)JRXmlConstants.getPrintOrderMap().get(atts.getValue(XmlConstants.ATTRIBUTE_printOrder));
		if (printOrder != null)
		{
			jasperDesign.setPrintOrder(printOrder.byteValue());
		}

		String pageWidth = atts.getValue(XmlConstants.ATTRIBUTE_pageWidth);
		if (pageWidth != null && pageWidth.length() > 0)
		{
			jasperDesign.setPageWidth(Integer.parseInt(pageWidth));
		}
		
		String pageHeight = atts.getValue(XmlConstants.ATTRIBUTE_pageHeight);
		if (pageHeight != null && pageHeight.length() > 0)
		{
			jasperDesign.setPageHeight(Integer.parseInt(pageHeight));
		}

		Byte orientation = (Byte)JRXmlConstants.getOrientationMap().get(atts.getValue(XmlConstants.ATTRIBUTE_orientation));
		if (orientation != null)
		{
			jasperDesign.setOrientation(orientation.byteValue());
		}

		Byte whenNoDataType = (Byte)JRXmlConstants.getWhenNoDataTypeMap().get(atts.getValue(XmlConstants.ATTRIBUTE_whenNoDataType));
		if (whenNoDataType != null)
		{
			jasperDesign.setWhenNoDataType(whenNoDataType.byteValue());
		}

		String columnWidth = atts.getValue(XmlConstants.ATTRIBUTE_columnWidth);
		if (columnWidth != null && columnWidth.length() > 0)
		{
			jasperDesign.setColumnWidth(Integer.parseInt(columnWidth));
		}

		String columnSpacing = atts.getValue(XmlConstants.ATTRIBUTE_columnSpacing);
		if (columnSpacing != null && columnSpacing.length() > 0)
		{
			jasperDesign.setColumnSpacing(Integer.parseInt(columnSpacing));
		}

		String leftMargin = atts.getValue(XmlConstants.ATTRIBUTE_leftMargin);
		if (leftMargin != null && leftMargin.length() > 0)
		{
			jasperDesign.setLeftMargin(Integer.parseInt(leftMargin));
		}

		String rightMargin = atts.getValue(XmlConstants.ATTRIBUTE_rightMargin);
		if (rightMargin != null && rightMargin.length() > 0)
		{
			jasperDesign.setRightMargin(Integer.parseInt(rightMargin));
		}

		String topMargin = atts.getValue(XmlConstants.ATTRIBUTE_topMargin);
		if (topMargin != null && topMargin.length() > 0)
		{
			jasperDesign.setTopMargin(Integer.parseInt(topMargin));
		}

		String bottomMargin = atts.getValue(XmlConstants.ATTRIBUTE_bottomMargin);
		if (bottomMargin != null && bottomMargin.length() > 0)
		{
			jasperDesign.setBottomMargin(Integer.parseInt(bottomMargin));
		}

		String isTitleNewPage = atts.getValue(XmlConstants.ATTRIBUTE_isTitleNewPage);
		if (isTitleNewPage != null && isTitleNewPage.length() > 0)
		{
			jasperDesign.setTitleNewPage(Boolean.valueOf(isTitleNewPage).booleanValue());
		}

		String isSummaryNewPage = atts.getValue(XmlConstants.ATTRIBUTE_isSummaryNewPage);
		if (isSummaryNewPage != null && isSummaryNewPage.length() > 0)
		{
			jasperDesign.setSummaryNewPage(Boolean.valueOf(isSummaryNewPage).booleanValue());
		}

		String isSummaryWithPageHeaderAndFooter = atts.getValue(XmlConstants.ATTRIBUTE_isSummaryWithPageHeaderAndFooter);
		if (isSummaryWithPageHeaderAndFooter != null && isSummaryWithPageHeaderAndFooter.length() > 0)
		{
			jasperDesign.setSummaryWithPageHeaderAndFooter(Boolean.valueOf(isSummaryWithPageHeaderAndFooter).booleanValue());
		}

		String isFloatColumnFooter = atts.getValue(XmlConstants.ATTRIBUTE_isFloatColumnFooter);
		if (isFloatColumnFooter != null && isFloatColumnFooter.length() > 0)
		{
			jasperDesign.setFloatColumnFooter(Boolean.valueOf(isFloatColumnFooter).booleanValue());
		}

		jasperDesign.setScriptletClass(atts.getValue(XmlConstants.ATTRIBUTE_scriptletClass));
		jasperDesign.setFormatFactoryClass(atts.getValue(XmlConstants.ATTRIBUTE_formatFactoryClass));
		jasperDesign.setResourceBundle(atts.getValue(XmlConstants.ATTRIBUTE_resourceBundle));

		Byte whenResourceMissingType = (Byte)JRXmlConstants.getWhenResourceMissingTypeMap().get(atts.getValue(XmlConstants.ATTRIBUTE_whenResourceMissingType));
		if (whenResourceMissingType != null)
		{
			jasperDesign.setWhenResourceMissingType(whenResourceMissingType.byteValue());
		}

		String isIgnorePagination = atts.getValue(XmlConstants.ATTRIBUTE_isIgnorePagination);
		if (isIgnorePagination != null && isIgnorePagination.length() > 0)
		{
			jasperDesign.setIgnorePagination(Boolean.valueOf(isIgnorePagination).booleanValue());
		}

		return jasperDesign;
	}
	

}

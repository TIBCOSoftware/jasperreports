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
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.SimplePrintPageFormat;
import net.sf.jasperreports.engine.SimplePrintPart;
import net.sf.jasperreports.engine.type.OrientationEnum;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRPrintPageFactory.java 5878 2013-01-07 20:23:13Z teodord $
 */
public class PrintPartFactory extends JRBaseFactory
{
	

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		SimplePrintPart part = new SimplePrintPart();
		
		part.setName(atts.getValue(JRXmlConstants.ATTRIBUTE_name));
		
		SimplePrintPageFormat pageFormat = new SimplePrintPageFormat();
		
		String pageWidth = atts.getValue(JRXmlConstants.ATTRIBUTE_pageWidth);
		if (pageWidth != null && pageWidth.length() > 0)
		{
			pageFormat.setPageWidth(Integer.parseInt(pageWidth));
		}

		String pageHeight = atts.getValue(JRXmlConstants.ATTRIBUTE_pageHeight);
		if (pageHeight != null && pageHeight.length() > 0)
		{
			pageFormat.setPageHeight(Integer.parseInt(pageHeight));
		}

		String topMargin = atts.getValue(JRXmlConstants.ATTRIBUTE_topMargin);
		if (topMargin != null && topMargin.length() > 0)
		{
			pageFormat.setTopMargin(Integer.valueOf(topMargin));
		}

		String leftMargin = atts.getValue(JRXmlConstants.ATTRIBUTE_leftMargin);
		if (leftMargin != null && leftMargin.length() > 0)
		{
			pageFormat.setLeftMargin(Integer.valueOf(leftMargin));
		}

		String bottomMargin = atts.getValue(JRXmlConstants.ATTRIBUTE_bottomMargin);
		if (bottomMargin != null && bottomMargin.length() > 0)
		{
			pageFormat.setBottomMargin(Integer.valueOf(bottomMargin));
		}

		String rightMargin = atts.getValue(JRXmlConstants.ATTRIBUTE_rightMargin);
		if (rightMargin != null && rightMargin.length() > 0)
		{
			pageFormat.setRightMargin(Integer.valueOf(rightMargin));
		}

		OrientationEnum orientation = OrientationEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_orientation));
		if (orientation != null)
		{
			pageFormat.setOrientation(orientation);
		}
		
		part.setPageFormat(pageFormat);
		
		JasperPrint jasperPrint = (JasperPrint)digester.peek(digester.getCount() - 2);
		jasperPrint.addPart(Integer.valueOf(atts.getValue(JRXmlConstants.ATTRIBUTE_pageIndex)), part);

		return part;
	}
	

}

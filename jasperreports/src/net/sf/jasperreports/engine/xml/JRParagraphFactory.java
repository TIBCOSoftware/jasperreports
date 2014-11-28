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

import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRParagraphContainer;
import net.sf.jasperreports.engine.type.LineSpacingEnum;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRParagraphFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRParagraphContainer paragraphContainer = (JRParagraphContainer) digester.peek();
		JRParagraph paragraph = paragraphContainer.getParagraph();
		setParagraphAttributes(atts, paragraph);
		return paragraph;
	}


	public static void setParagraphAttributes(Attributes atts, JRParagraph paragraph)
	{
		LineSpacingEnum lineSpacing = LineSpacingEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_lineSpacing));
		if (lineSpacing != null)
		{
			paragraph.setLineSpacing(lineSpacing);
		}

		String lineSpacingSize = atts.getValue(JRXmlConstants.ATTRIBUTE_lineSpacingSize);
		if (lineSpacingSize != null && lineSpacingSize.length() > 0)
		{
			paragraph.setLineSpacingSize(Float.parseFloat(lineSpacingSize));
		}

		String firstLineIndent = atts.getValue(JRXmlConstants.ATTRIBUTE_firstLineIndent);
		if (firstLineIndent != null && firstLineIndent.length() > 0)
		{
			paragraph.setFirstLineIndent(Integer.parseInt(firstLineIndent));
		}

		String leftIndent = atts.getValue(JRXmlConstants.ATTRIBUTE_leftIndent);
		if (leftIndent != null && leftIndent.length() > 0)
		{
			paragraph.setLeftIndent(Integer.parseInt(leftIndent));
		}

		String rightIndent = atts.getValue(JRXmlConstants.ATTRIBUTE_rightIndent);
		if (rightIndent != null && rightIndent.length() > 0)
		{
			paragraph.setRightIndent(Integer.parseInt(rightIndent));
		}

		String spacingBefore = atts.getValue(JRXmlConstants.ATTRIBUTE_spacingBefore);
		if (spacingBefore != null && spacingBefore.length() > 0)
		{
			paragraph.setSpacingBefore(Integer.parseInt(spacingBefore));
		}

		String spacingAfter = atts.getValue(JRXmlConstants.ATTRIBUTE_spacingAfter);
		if (spacingAfter != null && spacingAfter.length() > 0)
		{
			paragraph.setSpacingAfter(Integer.parseInt(spacingAfter));
		}

		String tabStopWidth = atts.getValue(JRXmlConstants.ATTRIBUTE_tabStopWidth);
		if (tabStopWidth != null && tabStopWidth.length() > 0)
		{
			paragraph.setTabStopWidth(Integer.parseInt(tabStopWidth));
		}
	}

}

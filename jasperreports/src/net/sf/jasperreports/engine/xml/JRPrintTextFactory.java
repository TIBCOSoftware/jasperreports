/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintTextFactory extends JRBaseFactory
{
	private static final Log log = LogFactory.getLog(JRPrintTextFactory.class);

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JasperPrint jasperPrint = (JasperPrint)digester.peek(digester.getCount() - 2);

		JRBasePrintText text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());

		HorizontalAlignEnum horizontalAlignment = HorizontalAlignEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_textAlignment));
		if (horizontalAlignment != null)
		{
			text.setHorizontalAlignment(horizontalAlignment);
		}

		VerticalAlignEnum verticalAlignment = VerticalAlignEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_verticalAlignment));
		if (verticalAlignment != null)
		{
			text.setVerticalAlignment(verticalAlignment);
		}

		RotationEnum rotation = RotationEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_rotation));
		if (rotation != null)
		{
			text.setRotation(rotation);
		}

		RunDirectionEnum runDirection = RunDirectionEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_runDirection));
		if (runDirection != null)
		{
			text.setRunDirection(runDirection);
		}

		String textHeight = atts.getValue(JRXmlConstants.ATTRIBUTE_textHeight);
		if (textHeight != null && textHeight.length() > 0)
		{
			text.setTextHeight(Float.parseFloat(textHeight));
		}

		LineSpacingEnum lineSpacing = LineSpacingEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_lineSpacing));
		if (lineSpacing != null)
		{
			if (log.isWarnEnabled())
			{
				log.warn("The 'lineSpacing' attribute is deprecated. Use the <paragraph> tag instead.");
			}
			text.getParagraph().setLineSpacing(lineSpacing);
		}

		text.setMarkup(atts.getValue(JRXmlConstants.ATTRIBUTE_markup));

		String isStyledText = atts.getValue(JRXmlConstants.ATTRIBUTE_isStyledText);
		if (isStyledText != null && isStyledText.length() > 0)
		{
			if (log.isWarnEnabled())
			{
				log.warn("The 'isStyledText' attribute is deprecated. Use the 'markup' attribute instead.");
			}
				
			text.setMarkup(Boolean.valueOf(isStyledText) ? JRCommonText.MARKUP_STYLED_TEXT : JRCommonText.MARKUP_NONE);
		}

//		String lineSpacingFactor = atts.getValue(JRXmlConstants.ATTRIBUTE_lineSpacingFactor);
//		if (lineSpacingFactor != null && lineSpacingFactor.length() > 0)
//		{
//			text.setLineSpacingFactor(Float.parseFloat(lineSpacingFactor));
//		}

//		String leadingOffset = atts.getValue(JRXmlConstants.ATTRIBUTE_leadingOffset);
//		if (leadingOffset != null && leadingOffset.length() > 0)
//		{
//			text.setLeadingOffset(Float.parseFloat(leadingOffset));
//		}

		text.setLinkType(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkType));
		text.setLinkTarget(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkTarget));
		text.setAnchorName(atts.getValue(JRXmlConstants.ATTRIBUTE_anchorName));
		text.setHyperlinkReference(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkReference));
		text.setHyperlinkAnchor(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkAnchor));
		
		String hyperlinkPage = atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkPage);
		if (hyperlinkPage != null)
		{
			text.setHyperlinkPage(Integer.valueOf(hyperlinkPage));
		}
		
		text.setHyperlinkTooltip(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkTooltip));

		String bookmarkLevelAttr = atts.getValue(JRXmlConstants.ATTRIBUTE_bookmarkLevel);
		if (bookmarkLevelAttr != null)
		{
			text.setBookmarkLevel(Integer.parseInt(bookmarkLevelAttr));
		}
		
		String valueClass = atts.getValue(JRXmlConstants.ATTRIBUTE_valueClass);
		if (valueClass != null)
		{
			text.setValueClassName(valueClass);
		}
		
		String pattern = atts.getValue(JRXmlConstants.ATTRIBUTE_pattern);
		if (pattern != null)
		{
			text.setPattern(pattern);
		}
		
		String formatFactoryClass = atts.getValue(JRXmlConstants.ATTRIBUTE_formatFactoryClass);
		if (formatFactoryClass != null)
		{
			text.setFormatFactoryClass(formatFactoryClass);
		}
		
		String locale = atts.getValue(JRXmlConstants.ATTRIBUTE_locale);
		if (locale != null)
		{
			text.setLocaleCode(locale);
		}
		
		String timezone = atts.getValue(JRXmlConstants.ATTRIBUTE_timezone);
		if (timezone != null)
		{
			text.setTimeZoneId(timezone);
		}
		
		return text;
	}
	

}

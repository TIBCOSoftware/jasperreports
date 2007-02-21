/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintText;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintTextFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JasperPrint jasperPrint = (JasperPrint)digester.peek(digester.getCount() - 2);

		JRBasePrintText text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());

		Byte horizontalAlignment = (Byte)JRXmlConstants.getHorizontalAlignMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_textAlignment));
		if (horizontalAlignment != null)
		{
			text.setHorizontalAlignment(horizontalAlignment);
		}

		Byte verticalAlignment = (Byte)JRXmlConstants.getVerticalAlignMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_verticalAlignment));
		if (verticalAlignment != null)
		{
			text.setVerticalAlignment(verticalAlignment);
		}

		Byte rotation = (Byte)JRXmlConstants.getRotationMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_rotation));
		if (rotation != null)
		{
			text.setRotation(rotation);
		}

		Byte runDirection = (Byte)JRXmlConstants.getRunDirectionMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_runDirection));
		if (runDirection != null)
		{
			text.setRunDirection(runDirection.byteValue());
		}

		String textHeight = atts.getValue(JRXmlConstants.ATTRIBUTE_textHeight);
		if (textHeight != null && textHeight.length() > 0)
		{
			text.setTextHeight(Float.parseFloat(textHeight));
		}

		Byte lineSpacing = (Byte)JRXmlConstants.getLineSpacingMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_lineSpacing));
		if (lineSpacing != null)
		{
			text.setLineSpacing(lineSpacing);
		}

		String isStyledText = atts.getValue(JRXmlConstants.ATTRIBUTE_isStyledText);
		if (isStyledText != null && isStyledText.length() > 0)
		{
			text.setStyledText(Boolean.valueOf(isStyledText));
		}

		String lineSpacingFactor = atts.getValue(JRXmlConstants.ATTRIBUTE_lineSpacingFactor);
		if (lineSpacingFactor != null && lineSpacingFactor.length() > 0)
		{
			text.setLineSpacingFactor(Float.parseFloat(lineSpacingFactor));
		}

		String leadingOffset = atts.getValue(JRXmlConstants.ATTRIBUTE_leadingOffset);
		if (leadingOffset != null && leadingOffset.length() > 0)
		{
			text.setLeadingOffset(Float.parseFloat(leadingOffset));
		}

		String hyperlinkType = atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkType);
		if (hyperlinkType != null)
		{
			text.setLinkType(hyperlinkType);
		}

		Byte hyperlinkTarget = (Byte)JRXmlConstants.getHyperlinkTargetMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkTarget));
		if (hyperlinkTarget != null)
		{
			text.setHyperlinkTarget(hyperlinkTarget.byteValue());
		}

		text.setAnchorName(atts.getValue(JRXmlConstants.ATTRIBUTE_anchorName));
		text.setHyperlinkReference(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkReference));
		text.setHyperlinkAnchor(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkAnchor));
		
		String hyperlinkPage = atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkPage);
		if (hyperlinkPage != null)
		{
			text.setHyperlinkPage(new Integer(hyperlinkPage));
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

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
	private static final String ATTRIBUTE_textAlignment = "textAlignment";
	private static final String ATTRIBUTE_verticalAlignment = "verticalAlignment";
	private static final String ATTRIBUTE_rotation = "rotation";
	private static final String ATTRIBUTE_runDirection = "runDirection";
	private static final String ATTRIBUTE_textHeight = "textHeight";
	private static final String ATTRIBUTE_lineSpacing = "lineSpacing";
	private static final String ATTRIBUTE_isStyledText = "isStyledText";
	private static final String ATTRIBUTE_lineSpacingFactor = "lineSpacingFactor";
	private static final String ATTRIBUTE_leadingOffset = "leadingOffset";
	private static final String ATTRIBUTE_hyperlinkType = "hyperlinkType";
	private static final String ATTRIBUTE_hyperlinkTarget = "hyperlinkTarget";
	private static final String ATTRIBUTE_anchorName = "anchorName";
	private static final String ATTRIBUTE_hyperlinkReference = "hyperlinkReference";
	private static final String ATTRIBUTE_hyperlinkAnchor = "hyperlinkAnchor";
	private static final String ATTRIBUTE_hyperlinkPage = "hyperlinkPage";
	public static final String ATTRIBUTE_hyperlinkTooltip = "hyperlinkTooltip";
	private static final String ATTRIBUTE_bookmarkLevel = "bookmarkLevel";
	public static final String ATTRIBUTE_valueClass = "valueClass";
	public static final String ATTRIBUTE_pattern = "pattern";
	public static final String ATTRIBUTE_formatFactoryClass = "formatFactoryClass";
	public static final String ATTRIBUTE_locale = "locale";
	public static final String ATTRIBUTE_timezone = "timezone";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JasperPrint jasperPrint = (JasperPrint)digester.peek(digester.getCount() - 2);

		JRBasePrintText text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());

		Byte horizontalAlignment = (Byte)JRXmlConstants.getHorizontalAlignMap().get(atts.getValue(ATTRIBUTE_textAlignment));
		if (horizontalAlignment != null)
		{
			text.setHorizontalAlignment(horizontalAlignment);
		}

		Byte verticalAlignment = (Byte)JRXmlConstants.getVerticalAlignMap().get(atts.getValue(ATTRIBUTE_verticalAlignment));
		if (verticalAlignment != null)
		{
			text.setVerticalAlignment(verticalAlignment);
		}

		Byte rotation = (Byte)JRXmlConstants.getRotationMap().get(atts.getValue(ATTRIBUTE_rotation));
		if (rotation != null)
		{
			text.setRotation(rotation);
		}

		Byte runDirection = (Byte)JRXmlConstants.getRunDirectionMap().get(atts.getValue(ATTRIBUTE_runDirection));
		if (runDirection != null)
		{
			text.setRunDirection(runDirection.byteValue());
		}

		String textHeight = atts.getValue(ATTRIBUTE_textHeight);
		if (textHeight != null && textHeight.length() > 0)
		{
			text.setTextHeight(Float.parseFloat(textHeight));
		}

		Byte lineSpacing = (Byte)JRXmlConstants.getLineSpacingMap().get(atts.getValue(ATTRIBUTE_lineSpacing));
		if (lineSpacing != null)
		{
			text.setLineSpacing(lineSpacing);
		}

		String isStyledText = atts.getValue(ATTRIBUTE_isStyledText);
		if (isStyledText != null && isStyledText.length() > 0)
		{
			text.setStyledText(Boolean.valueOf(isStyledText));
		}

		String lineSpacingFactor = atts.getValue(ATTRIBUTE_lineSpacingFactor);
		if (lineSpacingFactor != null && lineSpacingFactor.length() > 0)
		{
			text.setLineSpacingFactor(Float.parseFloat(lineSpacingFactor));
		}

		String leadingOffset = atts.getValue(ATTRIBUTE_leadingOffset);
		if (leadingOffset != null && leadingOffset.length() > 0)
		{
			text.setLeadingOffset(Float.parseFloat(leadingOffset));
		}

		String hyperlinkType = atts.getValue(ATTRIBUTE_hyperlinkType);
		if (hyperlinkType != null)
		{
			text.setLinkType(hyperlinkType);
		}

		Byte hyperlinkTarget = (Byte)JRXmlConstants.getHyperlinkTargetMap().get(atts.getValue(ATTRIBUTE_hyperlinkTarget));
		if (hyperlinkTarget != null)
		{
			text.setHyperlinkTarget(hyperlinkTarget.byteValue());
		}

		text.setAnchorName(atts.getValue(ATTRIBUTE_anchorName));
		text.setHyperlinkReference(atts.getValue(ATTRIBUTE_hyperlinkReference));
		text.setHyperlinkAnchor(atts.getValue(ATTRIBUTE_hyperlinkAnchor));
		
		String hyperlinkPage = atts.getValue(ATTRIBUTE_hyperlinkPage);
		if (hyperlinkPage != null)
		{
			text.setHyperlinkPage(new Integer(hyperlinkPage));
		}
		
		text.setHyperlinkTooltip(atts.getValue(ATTRIBUTE_hyperlinkTooltip));

		String bookmarkLevelAttr = atts.getValue(ATTRIBUTE_bookmarkLevel);
		if (bookmarkLevelAttr != null)
		{
			text.setBookmarkLevel(Integer.parseInt(bookmarkLevelAttr));
		}
		
		String valueClass = atts.getValue(ATTRIBUTE_valueClass);
		if (valueClass != null)
		{
			text.setValueClassName(valueClass);
		}
		
		String pattern = atts.getValue(ATTRIBUTE_pattern);
		if (pattern != null)
		{
			text.setPattern(pattern);
		}
		
		String formatFactoryClass = atts.getValue(ATTRIBUTE_formatFactoryClass);
		if (formatFactoryClass != null)
		{
			text.setFormatFactoryClass(formatFactoryClass);
		}
		
		String locale = atts.getValue(ATTRIBUTE_locale);
		if (locale != null)
		{
			text.setLocaleCode(locale);
		}
		
		String timezone = atts.getValue(ATTRIBUTE_timezone);
		if (timezone != null)
		{
			text.setTimeZoneId(timezone);
		}
		
		return text;
	}
	

}

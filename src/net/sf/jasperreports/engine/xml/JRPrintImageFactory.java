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
 *(at your option) any later version.
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
import net.sf.jasperreports.engine.base.JRBasePrintImage;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintImageFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JasperPrint jasperPrint = (JasperPrint)digester.peek(digester.getCount() - 2);

		JRBasePrintImage image = new JRBasePrintImage(jasperPrint.getDefaultStyleProvider());

		Byte scaleImage = (Byte)JRXmlConstants.getScaleImageMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_scaleImage));
		if (scaleImage != null)
		{
			image.setScaleImage(scaleImage);
		}

		Byte horizontalAlignment = (Byte)JRXmlConstants.getHorizontalAlignMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_hAlign));
		if (horizontalAlignment != null)
		{
			image.setHorizontalAlignment(horizontalAlignment);
		}

		Byte verticalAlignment = (Byte)JRXmlConstants.getVerticalAlignMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_vAlign));
		if (verticalAlignment != null)
		{
			image.setVerticalAlignment(verticalAlignment);
		}

		String isLazy = atts.getValue(JRXmlConstants.ATTRIBUTE_isLazy);
		if (isLazy != null && isLazy.length() > 0)
		{
			image.setLazy(Boolean.valueOf(isLazy).booleanValue());
		}

		Byte onErrorType = (Byte)JRXmlConstants.getOnErrorTypeMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_onErrorType));
		if (onErrorType != null)
		{
			image.setOnErrorType(onErrorType.byteValue());
		}

		image.setLinkType(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkType));
		image.setLinkTarget(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkTarget));
		image.setAnchorName(atts.getValue(JRXmlConstants.ATTRIBUTE_anchorName));
		image.setHyperlinkReference(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkReference));
		image.setHyperlinkAnchor(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkAnchor));
		
		String hyperlinkPage = atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkPage);
		if (hyperlinkPage != null)
		{
			image.setHyperlinkPage(new Integer(hyperlinkPage));
		}
		
		image.setHyperlinkTooltip(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkTooltip));

		String bookmarkLevelAttr = atts.getValue(JRXmlConstants.ATTRIBUTE_bookmarkLevel);
		if (bookmarkLevelAttr != null)
		{
			image.setBookmarkLevel(Integer.parseInt(bookmarkLevelAttr));
		}

		return image;
	}
	

}

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine.xml;

import org.xml.sax.Attributes;

import net.sf.jasperreports.engine.base.JRBasePrintImage;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintImageFactory extends JRBaseFactory
{


	/**
	 *
	 */
	private static final String ATTRIBUTE_scaleImage = "scaleImage";
	private static final String ATTRIBUTE_hAlign = "hAlign";
	private static final String ATTRIBUTE_vAlign = "vAlign";
	private static final String ATTRIBUTE_isLazy = "isLazy";
	private static final String ATTRIBUTE_whenNotAvailableType = "whenNotAvailableType";
	private static final String ATTRIBUTE_hyperlinkType = "hyperlinkType";
	private static final String ATTRIBUTE_hyperlinkTarget = "hyperlinkTarget";
	private static final String ATTRIBUTE_anchorName = "anchorName";
	private static final String ATTRIBUTE_hyperlinkReference = "hyperlinkReference";
	private static final String ATTRIBUTE_hyperlinkAnchor = "hyperlinkAnchor";
	private static final String ATTRIBUTE_hyperlinkPage = "hyperlinkPage";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRBasePrintImage image = new JRBasePrintImage();

		Byte scaleImage = (Byte)JRXmlConstants.getScaleImageMap().get(atts.getValue(ATTRIBUTE_scaleImage));
		if (scaleImage != null)
		{
			image.setScaleImage(scaleImage.byteValue());
		}

		Byte horizontalAlignment = (Byte)JRXmlConstants.getHorizontalAlignMap().get(atts.getValue(ATTRIBUTE_hAlign));
		if (horizontalAlignment != null)
		{
			image.setHorizontalAlignment(horizontalAlignment.byteValue());
		}

		Byte verticalAlignment = (Byte)JRXmlConstants.getVerticalAlignMap().get(atts.getValue(ATTRIBUTE_vAlign));
		if (verticalAlignment != null)
		{
			image.setVerticalAlignment(verticalAlignment.byteValue());
		}

		String isLazy = atts.getValue(ATTRIBUTE_isLazy);
		if (isLazy != null && isLazy.length() > 0)
		{
			image.setLazy(Boolean.valueOf(isLazy).booleanValue());
		}

		Byte whenNotAvailableType = (Byte)JRXmlConstants.getWhenNotAvailableTypeMap().get(atts.getValue(ATTRIBUTE_whenNotAvailableType));
		if (whenNotAvailableType != null)
		{
			image.setWhenNotAvailableType(whenNotAvailableType.byteValue());
		}

		Byte hyperlinkType = (Byte)JRXmlConstants.getHyperlinkTypeMap().get(atts.getValue(ATTRIBUTE_hyperlinkType));
		if (hyperlinkType != null)
		{
			image.setHyperlinkType(hyperlinkType.byteValue());
		}

		Byte hyperlinkTarget = (Byte)JRXmlConstants.getHyperlinkTargetMap().get(atts.getValue(ATTRIBUTE_hyperlinkTarget));
		if (hyperlinkTarget != null)
		{
			image.setHyperlinkTarget(hyperlinkTarget.byteValue());
		}

		image.setAnchorName(atts.getValue(ATTRIBUTE_anchorName));
		image.setHyperlinkReference(atts.getValue(ATTRIBUTE_hyperlinkReference));
		image.setHyperlinkAnchor(atts.getValue(ATTRIBUTE_hyperlinkAnchor));
		
		String hyperlinkPage = atts.getValue(ATTRIBUTE_hyperlinkPage);
		if (hyperlinkPage != null)
		{
			image.setHyperlinkPage(new Integer(hyperlinkPage));
		}

		return image;
	}
	

}

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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

import java.awt.Color;

import org.xml.sax.Attributes;

import net.sf.jasperreports.engine.base.JRBaseBox;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBoxFactory extends JRBaseFactory
{


	/**
	 *
	 */
	private static final String ATTRIBUTE_border = "border";
	private static final String ATTRIBUTE_borderColor = "borderColor";
	private static final String ATTRIBUTE_padding = "padding";
	private static final String ATTRIBUTE_topBorder = "topBorder";
	private static final String ATTRIBUTE_topBorderColor = "topBorderColor";
	private static final String ATTRIBUTE_topPadding = "topPadding";
	private static final String ATTRIBUTE_leftBorder = "leftBorder";
	private static final String ATTRIBUTE_leftBorderColor = "leftBorderColor";
	private static final String ATTRIBUTE_leftPadding = "leftPadding";
	private static final String ATTRIBUTE_bottomBorder = "bottomBorder";
	private static final String ATTRIBUTE_bottomBorderColor = "bottomBorderColor";
	private static final String ATTRIBUTE_bottomPadding = "bottomPadding";
	private static final String ATTRIBUTE_rightBorder = "rightBorder";
	private static final String ATTRIBUTE_rightBorderColor = "rightBorderColor";
	private static final String ATTRIBUTE_rightPadding = "rightPadding";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRBaseBox box = new JRBaseBox();//FIXME design box?

		Byte border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_border));
		if (border != null)
		{
			box.setBorder(border.byteValue());
		}

		Color borderColor = getColor(atts.getValue(ATTRIBUTE_borderColor), Color.black);
		if (borderColor != null)
		{
			box.setBorderColor(borderColor);
		}

		String padding = atts.getValue(ATTRIBUTE_padding);
		if (padding != null && padding.length() > 0)
		{
			box.setPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_topBorder));
		if (border != null)
		{
			box.setTopBorder(border.byteValue());
		}

		borderColor = getColor(atts.getValue(ATTRIBUTE_topBorderColor), Color.black);
		if (borderColor != null)
		{
			box.setTopBorderColor(borderColor);
		}

		padding = atts.getValue(ATTRIBUTE_topPadding);
		if (padding != null && padding.length() > 0)
		{
			box.setTopPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_leftBorder));
		if (border != null)
		{
			box.setLeftBorder(border.byteValue());
		}

		borderColor = getColor(atts.getValue(ATTRIBUTE_leftBorderColor), Color.black);
		if (borderColor != null)
		{
			box.setLeftBorderColor(borderColor);
		}

		padding = atts.getValue(ATTRIBUTE_leftPadding);
		if (padding != null && padding.length() > 0)
		{
			box.setLeftPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_bottomBorder));
		if (border != null)
		{
			box.setBottomBorder(border.byteValue());
		}

		borderColor = getColor(atts.getValue(ATTRIBUTE_bottomBorderColor), Color.black);
		if (borderColor != null)
		{
			box.setBottomBorderColor(borderColor);
		}

		padding = atts.getValue(ATTRIBUTE_bottomPadding);
		if (padding != null && padding.length() > 0)
		{
			box.setBottomPadding(Integer.parseInt(padding));
		}

		border = (Byte)JRXmlConstants.getPenMap().get(atts.getValue(ATTRIBUTE_rightBorder));
		if (border != null)
		{
			box.setRightBorder(border.byteValue());
		}

		borderColor = getColor(atts.getValue(ATTRIBUTE_rightBorderColor), Color.black);
		if (borderColor != null)
		{
			box.setRightBorderColor(borderColor);
		}

		padding = atts.getValue(ATTRIBUTE_rightPadding);
		if (padding != null && padding.length() > 0)
		{
			box.setRightPadding(Integer.parseInt(padding));
		}

		return box;
	}
	

}

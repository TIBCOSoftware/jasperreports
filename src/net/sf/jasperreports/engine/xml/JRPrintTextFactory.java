/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
package dori.jasper.engine.xml;

import org.xml.sax.Attributes;

import dori.jasper.engine.base.JRBasePrintText;


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
	private static final String ATTRIBUTE_textHeight = "textHeight";
	private static final String ATTRIBUTE_lineSpacing = "lineSpacing";
	private static final String ATTRIBUTE_isStyledText = "isStyledText";
	private static final String ATTRIBUTE_absoluteLineSpacing = "absoluteLineSpacing";
	private static final String ATTRIBUTE_absoluteLeading = "absoluteLeading";
	private static final String ATTRIBUTE_hyperlinkType = "hyperlinkType";
	private static final String ATTRIBUTE_anchorName = "anchorName";
	private static final String ATTRIBUTE_hyperlinkReference = "hyperlinkReference";
	private static final String ATTRIBUTE_hyperlinkAnchor = "hyperlinkAnchor";
	private static final String ATTRIBUTE_hyperlinkPage = "hyperlinkPage";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRBasePrintText text = new JRBasePrintText();

		Byte horizontalAlignment = (Byte)JRXmlConstants.getHorizontalAlignMap().get(atts.getValue(ATTRIBUTE_textAlignment));
		if (horizontalAlignment != null)
		{
			text.setTextAlignment(horizontalAlignment.byteValue());
		}

		Byte verticalAlignment = (Byte)JRXmlConstants.getVerticalAlignMap().get(atts.getValue(ATTRIBUTE_verticalAlignment));
		if (verticalAlignment != null)
		{
			text.setVerticalAlignment(verticalAlignment.byteValue());
		}

		Byte rotation = (Byte)JRXmlConstants.getRotationMap().get(atts.getValue(ATTRIBUTE_rotation));
		if (rotation != null)
		{
			text.setRotation(rotation.byteValue());
		}

		String textHeight = atts.getValue(ATTRIBUTE_textHeight);
		if (textHeight != null && textHeight.length() > 0)
		{
			text.setTextHeight(Float.parseFloat(textHeight));
		}

		Byte lineSpacing = (Byte)JRXmlConstants.getLineSpacingMap().get(atts.getValue(ATTRIBUTE_lineSpacing));
		if (lineSpacing != null)
		{
			text.setLineSpacing(lineSpacing.byteValue());
		}

		String isStyledText = atts.getValue(ATTRIBUTE_isStyledText);
		if (isStyledText != null && isStyledText.length() > 0)
		{
			text.setStyledText(Boolean.valueOf(isStyledText).booleanValue());
		}

		String absoluteLineSpacing = atts.getValue(ATTRIBUTE_absoluteLineSpacing);
		if (absoluteLineSpacing != null && absoluteLineSpacing.length() > 0)
		{
			text.setAbsoluteLineSpacing(Float.parseFloat(absoluteLineSpacing));
		}

		String absoluteLeading = atts.getValue(ATTRIBUTE_absoluteLeading);
		if (absoluteLeading != null && absoluteLeading.length() > 0)
		{
			text.setAbsoluteLeading(Float.parseFloat(absoluteLeading));
		}

		Byte hyperlinkType = (Byte)JRXmlConstants.getHyperlinkTypeMap().get(atts.getValue(ATTRIBUTE_hyperlinkType));
		if (hyperlinkType != null)
		{
			text.setHyperlinkType(hyperlinkType.byteValue());
		}

		text.setAnchorName(atts.getValue(ATTRIBUTE_anchorName));
		text.setHyperlinkReference(atts.getValue(ATTRIBUTE_hyperlinkReference));
		text.setHyperlinkAnchor(atts.getValue(ATTRIBUTE_hyperlinkAnchor));
		
		String hyperlinkPage = atts.getValue(ATTRIBUTE_hyperlinkPage);
		if (hyperlinkPage != null)
		{
			text.setHyperlinkPage(new Integer(hyperlinkPage));
		}

		return text;
	}
	

}

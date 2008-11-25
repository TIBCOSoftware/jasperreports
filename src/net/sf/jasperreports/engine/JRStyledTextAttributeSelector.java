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
package net.sf.jasperreports.engine;

import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.util.JRFontUtil;

/**
 * Selector of element-level styled text attributes for print text objects.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 * @see JRPrintText#getStyledText(JRStyledTextAttributeSelector)
 * @see JRPrintText#getFullStyledText(JRStyledTextAttributeSelector)
 */
public interface JRStyledTextAttributeSelector
{

	/**
	 * Selects all styled text attributes, i.e. font attributes plus forecolor
	 * and backcolor.
	 */
	JRStyledTextAttributeSelector ALL = new JRStyledTextAttributeSelector()
	{
		public Map getStyledTextAttributes(JRPrintText printText)
		{
			Map attributes = new HashMap(); 
			JRFontUtil.getAttributes(attributes, printText);
			attributes.put(TextAttribute.FOREGROUND, printText.getForecolor());
			if (printText.getMode() == JRElement.MODE_OPAQUE)
			{
				attributes.put(TextAttribute.BACKGROUND, printText.getBackcolor());
			}
			return attributes;
		}
	};

	/**
	 * Selects all styled text attribute except backcolor, i.e. font attributes
	 * plus forecolor.
	 */
	JRStyledTextAttributeSelector NO_BACKCOLOR = new JRStyledTextAttributeSelector()
	{
		public Map getStyledTextAttributes(JRPrintText printText)
		{
			Map attributes = new HashMap(); 
			JRFontUtil.getAttributes(attributes, printText);
			attributes.put(TextAttribute.FOREGROUND, printText.getForecolor());
			return attributes;
		}
	};

	/**
	 * Doesn't select any styled text attribute.
	 */
	JRStyledTextAttributeSelector NONE = new JRStyledTextAttributeSelector()
	{
		public Map getStyledTextAttributes(JRPrintText printText)
		{
			return null;
		}
	};
	
	/**
	 * Construct a map containing the selected element-level styled text attributes
	 * for a print text element.
	 * 
	 * @param printText the print text object
	 * @return a map containing styled text attributes
	 */
	Map getStyledTextAttributes(JRPrintText printText);
	
}

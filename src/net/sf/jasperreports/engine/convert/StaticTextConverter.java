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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.convert;

import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;

import org.xml.sax.SAXException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
 */
public class StaticTextConverter extends TextElementConverter
{

	/**
	 *
	 */
	private final static StaticTextConverter INSTANCE = new StaticTextConverter();
	
	/**
	 *
	 */
	private final JRStyledTextParser styledTextParser = new JRStyledTextParser();
	
	/**
	 *
	 */
	private StaticTextConverter()
	{
	}

	/**
	 *
	 */
	public static StaticTextConverter getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 *
	 */
	public JRPrintElement convert(ReportConverter reportConverter, JRElement element)
	{
		JRBasePrintText printText = new JRBasePrintText(reportConverter.getDefaultStyleProvider());
		JRStaticText staticText = (JRStaticText)element;
		
		copyTextElement(reportConverter, staticText, printText);
		
		printText.setText(staticText.getText());
		
		measureTextElement(printText);
		
		return printText;
	}

	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText printText)
	{
		String text = printText.getText();
		
		if (text == null)
		{
			text = "";
		}

		Map attributes = new HashMap(); 
		JRFontUtil.setAttributes(attributes, printText);
		attributes.put(TextAttribute.FOREGROUND, printText.getForecolor());

		JRStyledText styledText = null;

		if (printText.isStyledText())
		{
			try
			{
				styledText = styledTextParser.parse(attributes, text);
			}
			catch (SAXException e)
			{
				//ignore if invalid styled text and treat like normal text
			}
		}
	
		if (styledText == null)
		{
			styledText = new JRStyledText();
			styledText.append(text);
			styledText.addRun(new JRStyledText.Run(attributes, 0, text.length()));
		}
		
		return styledText;
	}

}

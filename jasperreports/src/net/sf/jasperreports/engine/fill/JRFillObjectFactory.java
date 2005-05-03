/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBaseReportFont;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillObjectFactory
{


	/**
	 *
	 */
	private JRBaseFiller filler = null;
	private Map fillObjectsMap = new HashMap();

	private JRFont defaultFont = null;


	/**
	 *
	 */
	protected JRFillObjectFactory(JRBaseFiller filler)
	{
		this.filler = filler;
	}


	/**
	 *
	 */
	protected void put(Object object, Object fillObject)
	{
		fillObjectsMap.put(object, fillObject);
	}


	/**
	 *
	 */
	protected JRBaseReportFont getReportFont(JRReportFont font)
	{
		JRBaseReportFont fillFont = null;
		
		if (font != null)
		{
			fillFont = (JRBaseReportFont)fillObjectsMap.get(font);
			if (fillFont == null)
			{
				fillFont = new JRBaseReportFont(font);
				fillFont.setCachingAttributes(true);
				fillObjectsMap.put(font, fillFont);
			}
		}
		
		return fillFont;
	}


	/**
	 *
	 */
	protected JRBaseFont getFont(JRFont font)
	{
		JRBaseFont fillFont = null;
		
		if (font != null)
		{
			fillFont = (JRBaseFont)fillObjectsMap.get(font);
			if (fillFont == null)
			{
				fillFont = 
					new JRBaseFont(
						filler.getJasperPrint(), 
						getReportFont(font.getReportFont()), 
						font
						);
				fillFont.setCachingAttributes(true);
				fillObjectsMap.put(font, fillFont);
			}
		}
		else 
		{
			if (defaultFont == null)
			{
				defaultFont = new JRBaseFont();
			}
			fillFont = getFont(defaultFont);
		}
		
		return fillFont;
	}


	/**
	 *
	 */
	protected  JRFillParameter getParameter(JRParameter parameter)
	{
		JRFillParameter fillParameter = null;
		
		if (parameter != null)
		{
			fillParameter = (JRFillParameter)fillObjectsMap.get(parameter);
			if (fillParameter == null)
			{
				fillParameter = new JRFillParameter(parameter, this);
			}
		}
		
		return fillParameter;
	}


	/**
	 *
	 */
	protected JRFillField getField(JRField field)
	{
		JRFillField fillField = null;
		
		if (field != null)
		{
			fillField = (JRFillField)fillObjectsMap.get(field);
			if (fillField == null)
			{
				fillField = new JRFillField(field, this);
			}
		}
		
		return fillField;
	}


	/**
	 *
	 */
	protected JRFillVariable getVariable(JRVariable variable)
	{
		JRFillVariable fillVariable = null;
		
		if (variable != null)
		{
			fillVariable = (JRFillVariable)fillObjectsMap.get(variable);
			if (fillVariable == null)
			{
				fillVariable = new JRFillVariable(variable, this);
			}
		}
		
		return fillVariable;
	}


	/**
	 *
	 */
	protected JRFillGroup getGroup(JRGroup group)
	{
		JRFillGroup fillGroup = null;
		
		if (group != null)
		{
			fillGroup = (JRFillGroup)fillObjectsMap.get(group);
			if (fillGroup == null)
			{
				fillGroup = new JRFillGroup(group, this);
			}
		}
		
		return fillGroup;
	}


	/**
	 *
	 */
	protected JRFillBand getBand(JRBand band)
	{
		JRFillBand fillBand = null;
		
		//if (band != null)
		//{
		// for null bands, the filler's missingFillBand will be returned
			fillBand = (JRFillBand)fillObjectsMap.get(band);
			if (fillBand == null)
			{
				fillBand = new JRFillBand(filler, band, this);
			}
		//}
		
		return fillBand;
	}


	/**
	 *
	 */
	protected JRFillElementGroup getElementGroup(JRElementGroup elementGroup)
	{
		JRFillElementGroup fillElementGroup = null;
		
		if (elementGroup != null)
		{
			fillElementGroup = (JRFillElementGroup)fillObjectsMap.get(elementGroup);
			if (fillElementGroup == null)
			{
				fillElementGroup = new JRFillElementGroup(elementGroup, this);
			}
		}
		
		return fillElementGroup;
	}


	/**
	 *
	 */
	protected JRFillElement getElement(JRElement element)
	{
		JRFillElement fillElement = null;
		
		if (element instanceof JRLine)
		{
			fillElement = getLine((JRLine)element);
		}
		else if (element instanceof JRRectangle)
		{
			fillElement = getRectangle((JRRectangle)element);
		}
		else if (element instanceof JREllipse)
		{
			fillElement = getEllipse((JREllipse)element);
		}
		else if (element instanceof JRImage)
		{
			fillElement = getImage((JRImage)element);
		}
		else if (element instanceof JRStaticText)
		{
			fillElement = getStaticText((JRStaticText)element);
		}
		else if (element instanceof JRTextField)
		{
			fillElement = getTextField((JRTextField)element);
		}
		else if (element instanceof JRSubreport)
		{
			fillElement = getSubreport((JRSubreport)element);
		}
		
		return fillElement;
	}


	/**
	 *
	 */
	protected JRFillLine getLine(JRLine line)
	{
		JRFillLine fillLine = null;
		
		if (line != null)
		{
			fillLine = (JRFillLine)fillObjectsMap.get(line);
			if (fillLine == null)
			{
				fillLine = new JRFillLine(filler, line, this);
			}
		}
		
		return fillLine;
	}


	/**
	 *
	 */
	protected JRFillRectangle getRectangle(JRRectangle rectangle)
	{
		JRFillRectangle fillRectangle = null;
		
		if (rectangle != null)
		{
			fillRectangle = (JRFillRectangle)fillObjectsMap.get(rectangle);
			if (fillRectangle == null)
			{
				fillRectangle = new JRFillRectangle(filler, rectangle, this);
			}
		}
		
		return fillRectangle;
	}


	/**
	 *
	 */
	protected JRFillEllipse getEllipse(JREllipse ellipse)
	{
		JRFillEllipse fillEllipse = null;
		
		if (ellipse != null)
		{
			fillEllipse = (JRFillEllipse)fillObjectsMap.get(ellipse);
			if (fillEllipse == null)
			{
				fillEllipse = new JRFillEllipse(filler, ellipse, this);
			}
		}
		
		return fillEllipse;
	}


	/**
	 *
	 */
	protected JRFillImage getImage(JRImage image)
	{
		JRFillImage fillImage = null;
		
		if (image != null)
		{
			fillImage = (JRFillImage)fillObjectsMap.get(image);
			if (fillImage == null)
			{
				fillImage = new JRFillImage(filler, image, this);
			}
		}
		
		return fillImage;
	}


	/**
	 *
	 */
	protected JRFillStaticText getStaticText(JRStaticText staticText)
	{
		JRFillStaticText fillStaticText = null;
		
		if (staticText != null)
		{
			fillStaticText = (JRFillStaticText)fillObjectsMap.get(staticText);
			if (fillStaticText == null)
			{
				fillStaticText = new JRFillStaticText(filler, staticText, this);
			}
		}
		
		return fillStaticText;
	}


	/**
	 *
	 */
	protected JRFillTextField getTextField(JRTextField textField)
	{
		JRFillTextField fillTextField = null;
		
		if (textField != null)
		{
			fillTextField = (JRFillTextField)fillObjectsMap.get(textField);
			if (fillTextField == null)
			{
				fillTextField = new JRFillTextField(filler, textField, this);
			}
		}
		
		return fillTextField;
	}


	/**
	 *
	 */
	protected JRFillSubreport getSubreport(JRSubreport subreport)
	{
		JRFillSubreport fillSubreport = null;
		
		if (subreport != null)
		{
			fillSubreport = (JRFillSubreport)fillObjectsMap.get(subreport);
			if (fillSubreport == null)
			{
				fillSubreport = new JRFillSubreport(filler, subreport, this);
			}
		}
		
		return fillSubreport;
	}


}

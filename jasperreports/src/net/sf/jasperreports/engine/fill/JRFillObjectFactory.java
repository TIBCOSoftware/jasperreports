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
package dori.jasper.engine.fill;

import java.util.Map;

import dori.jasper.engine.JRBand;
import dori.jasper.engine.JRElement;
import dori.jasper.engine.JRElementGroup;
import dori.jasper.engine.JREllipse;
import dori.jasper.engine.JRField;
import dori.jasper.engine.JRGroup;
import dori.jasper.engine.JRImage;
import dori.jasper.engine.JRLine;
import dori.jasper.engine.JRParameter;
import dori.jasper.engine.JRRectangle;
import dori.jasper.engine.JRStaticText;
import dori.jasper.engine.JRSubreport;
import dori.jasper.engine.JRTextField;
import dori.jasper.engine.JRVariable;


/**
 *
 */
public class JRFillObjectFactory
{


	/**
	 *
	 */
	protected static JRFillParameter getParameter(
		JRBaseFiller filler,
		JRParameter parameter, 
		Map fillObjectsMap
		)
	{
		JRFillParameter fillParameter = null;
		
		if (parameter != null)
		{
			fillParameter = (JRFillParameter)fillObjectsMap.get(parameter);
	
			if (fillParameter == null)
			{
				fillParameter = 
					new JRFillParameter(
						filler,
						parameter, 
						fillObjectsMap
						);
			}
		}
		
		return fillParameter;
	}


	/**
	 *
	 */
	protected static JRFillField getField(
		JRBaseFiller filler,
		JRField field, 
		Map fillObjectsMap
		)
	{
		JRFillField fillField = null;
		
		if (field != null)
		{
			fillField = (JRFillField)fillObjectsMap.get(field);
	
			if (fillField == null)
			{
				fillField = 
					new JRFillField(
						filler,
						field, 
						fillObjectsMap
						);
			}
		}
		
		return fillField;
	}


	/**
	 *
	 */
	protected static JRFillVariable getVariable(
		JRBaseFiller filler,
		JRVariable variable, 
		Map fillObjectsMap
		)
	{
		JRFillVariable fillVariable = null;
		
		if (variable != null)
		{
			fillVariable = (JRFillVariable)fillObjectsMap.get(variable);
	
			if (fillVariable == null)
			{
				fillVariable = 
					new JRFillVariable(
						filler,
						variable, 
						fillObjectsMap
						);
			}
		}
		
		return fillVariable;
	}


	/**
	 *
	 */
	protected static JRFillGroup getGroup(
		JRBaseFiller filler,
		JRGroup group,
		Map fillObjectsMap
		)
	{
		JRFillGroup fillGroup = null;
		
		if (group != null)
		{
			fillGroup = (JRFillGroup)fillObjectsMap.get(group);
	
			if (fillGroup == null)
			{
				fillGroup = 
					new JRFillGroup(
						filler,
						group, 
						fillObjectsMap
						);
			}
		}
		
		return fillGroup;
	}


	/**
	 *
	 */
	protected static JRFillBand getBand(
		JRBaseFiller filler,
		JRBand band,
		Map fillObjectsMap
		)
	{
		JRFillBand fillBand = null;
		
		//if (band != null)
		//{
			fillBand = (JRFillBand)fillObjectsMap.get(band);
	
			if (fillBand == null)
			{
				fillBand = 
					new JRFillBand(
						filler,
						band, 
						fillObjectsMap
						);
			}
		//}
		
		return fillBand;
	}


	/**
	 *
	 */
	protected static JRFillElementGroup getElementGroup(
		JRBaseFiller filler,
		JRElementGroup elementGroup, 
		Map fillObjectsMap
		)
	{
		JRFillElementGroup fillElementGroup = null;
		
		if (elementGroup != null)
		{
			fillElementGroup = (JRFillElementGroup)fillObjectsMap.get(elementGroup);
	
			if (fillElementGroup == null)
			{
				fillElementGroup = 
					new JRFillElementGroup(
						filler,
						elementGroup, 
						fillObjectsMap
						);
			}
		}
		
		return fillElementGroup;
	}


	/**
	 *
	 */
	protected static JRFillElement getElement(
		JRBaseFiller filler,
		JRElement element, 
		Map fillObjectsMap
		)
	{
		JRFillElement fillElement = null;
		
		if (element instanceof JRLine)
		{
			fillElement = getLine(filler, (JRLine)element, fillObjectsMap);
		}
		else if (element instanceof JRRectangle)
		{
			fillElement = getRectangle(filler, (JRRectangle)element, fillObjectsMap);
		}
		else if (element instanceof JREllipse)
		{
			fillElement = getEllipse(filler, (JREllipse)element, fillObjectsMap);
		}
		else if (element instanceof JRImage)
		{
			fillElement = getImage(filler, (JRImage)element, fillObjectsMap);
		}
		else if (element instanceof JRStaticText)
		{
			fillElement = getStaticText(filler, (JRStaticText)element, fillObjectsMap);
		}
		else if (element instanceof JRTextField)
		{
			fillElement = getTextField(filler, (JRTextField)element, fillObjectsMap);
		}
		else if (element instanceof JRSubreport)
		{
			fillElement = getSubreport(filler, (JRSubreport)element, fillObjectsMap);
		}
		
		return fillElement;
	}


	/**
	 *
	 */
	protected static JRFillLine getLine(
		JRBaseFiller filler,
		JRLine line, 
		Map fillObjectsMap
		)
	{
		JRFillLine fillLine = null;
		
		if (line != null)
		{
			fillLine = (JRFillLine)fillObjectsMap.get(line);
	
			if (fillLine == null)
			{
				fillLine = 
					new JRFillLine(
						filler,
						line, 
						fillObjectsMap
						);
			}
		}
		
		return fillLine;
	}


	/**
	 *
	 */
	protected static JRFillRectangle getRectangle(
		JRBaseFiller filler,
		JRRectangle rectangle, 
		Map fillObjectsMap
		)
	{
		JRFillRectangle fillRectangle = null;
		
		if (rectangle != null)
		{
			fillRectangle = (JRFillRectangle)fillObjectsMap.get(rectangle);
	
			if (fillRectangle == null)
			{
				fillRectangle = 
					new JRFillRectangle(
						filler,
						rectangle, 
						fillObjectsMap
						);
			}
		}
		
		return fillRectangle;
	}


	/**
	 *
	 */
	protected static JRFillEllipse getEllipse(
		JRBaseFiller filler,
		JREllipse ellipse, 
		Map fillObjectsMap
		)
	{
		JRFillEllipse fillEllipse = null;
		
		if (ellipse != null)
		{
			fillEllipse = (JRFillEllipse)fillObjectsMap.get(ellipse);
	
			if (fillEllipse == null)
			{
				fillEllipse = 
					new JRFillEllipse(
						filler,
						ellipse, 
						fillObjectsMap
						);
			}
		}
		
		return fillEllipse;
	}


	/**
	 *
	 */
	protected static JRFillImage getImage(
		JRBaseFiller filler,
		JRImage image, 
		Map fillObjectsMap
		)
	{
		JRFillImage fillImage = null;
		
		if (image != null)
		{
			fillImage = (JRFillImage)fillObjectsMap.get(image);
	
			if (fillImage == null)
			{
				fillImage = 
					new JRFillImage(
						filler,
						image, 
						fillObjectsMap
						);
			}
		}
		
		return fillImage;
	}


	/**
	 *
	 */
	protected static JRFillStaticText getStaticText(
		JRBaseFiller filler,
		JRStaticText staticText, 
		Map fillObjectsMap
		)
	{
		JRFillStaticText fillStaticText = null;
		
		if (staticText != null)
		{
			fillStaticText = (JRFillStaticText)fillObjectsMap.get(staticText);
	
			if (fillStaticText == null)
			{
				fillStaticText = 
					new JRFillStaticText(
						filler,
						staticText, 
						fillObjectsMap
						);
			}
		}
		
		return fillStaticText;
	}


	/**
	 *
	 */
	protected static JRFillTextField getTextField(
		JRBaseFiller filler,
		JRTextField textField, 
		Map fillObjectsMap
		)
	{
		JRFillTextField fillTextField = null;
		
		if (textField != null)
		{
			fillTextField = (JRFillTextField)fillObjectsMap.get(textField);
	
			if (fillTextField == null)
			{
				fillTextField = 
					new JRFillTextField(
						filler,
						textField, 
						fillObjectsMap
						);
			}
		}
		
		return fillTextField;
	}


	/**
	 *
	 */
	protected static JRFillSubreport getSubreport(
		JRBaseFiller filler,
		JRSubreport subreport, 
		Map fillObjectsMap
		)
	{
		JRFillSubreport fillSubreport = null;
		
		if (subreport != null)
		{
			fillSubreport = (JRFillSubreport)fillObjectsMap.get(subreport);
	
			if (fillSubreport == null)
			{
				fillSubreport = 
					new JRFillSubreport(
						filler,
						subreport, 
						fillObjectsMap
						);
			}
		}
		
		return fillSubreport;
	}


}

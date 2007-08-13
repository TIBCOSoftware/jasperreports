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

import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignSubreportReturnValue;
import net.sf.jasperreports.engine.design.JRValidationException;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;

/**
 * Factory class for {@link net.sf.jasperreports.engine.JRSubreportReturnValue JRSubreportReturnValue}
 * creation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRSubreportReturnValueFactory extends JRBaseFactory
{

	/**
	 * Creates an object from a subreport copied value XML element.
	 * 
	 * @param atts the element attributes
	 * @return a {@link JRDesignSubreportReturnValue JRDesignSubreportReturnValue} object
	 */
	public Object createObject(Attributes atts)
	{
		JRXmlLoader xmlLoader = (JRXmlLoader) digester.peek(digester.getCount() - 1);
		JasperDesign design = (JasperDesign) digester.peek(digester.getCount() - 2);
		
		JRDesignSubreportReturnValue returnValue = new JRDesignSubreportReturnValue();

		String variableName = atts.getValue(JRXmlConstants.ATTRIBUTE_toVariable);
		JRVariable variable = (JRVariable) design.getVariablesMap().get(variableName);
		if (variable == null)
		{
			xmlLoader.addError(new JRValidationException("Unknown variable " + variableName, returnValue));
		}
		
		returnValue.setSubreportVariable(atts.getValue(JRXmlConstants.ATTRIBUTE_subreportVariable));
		returnValue.setToVariable(variableName);

		Byte calculation = (Byte)JRXmlConstants.getCalculationMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_calculation));
		if (calculation != null)
		{
			returnValue.setCalculation(calculation.byteValue());
		}
		
		String incrementerFactoryClass = atts.getValue(JRXmlConstants.ATTRIBUTE_incrementerFactoryClass);
		if (incrementerFactoryClass != null)
		{
			returnValue.setIncrementerFactoryClassName(incrementerFactoryClass);
		}

		return returnValue;
	}

}

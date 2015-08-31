/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
 * (at your option) any later version.
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

import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.DesignExpressionReturnValue;
import net.sf.jasperreports.engine.design.JRValidationException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.CalculationEnum;

import org.xml.sax.Attributes;

/**
 * Factory class for {@link net.sf.jasperreports.engine.ExpressionReturnValue ExpressionReturnValue}
 * creation.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ExpressionReturnValueFactory extends JRBaseFactory
{

	/**
	 * @param atts the element attributes
	 * @return a {@link DesignExpressionReturnValue DesignExpressionReturnValue} object
	 */
	public Object createObject(Attributes atts)
	{
		JRXmlLoader xmlLoader = (JRXmlLoader) digester.peek(digester.getCount() - 1);
		JasperDesign design = (JasperDesign) digester.peek(digester.getCount() - 2);
		
		DesignExpressionReturnValue returnValue = new DesignExpressionReturnValue();

		String variableName = atts.getValue(JRXmlConstants.ATTRIBUTE_toVariable);
		JRVariable variable = design.getVariablesMap().get(variableName);
		if (variable == null)
		{
			xmlLoader.addError(new JRValidationException("Unknown variable " + variableName, returnValue));
		}
		
		returnValue.setToVariable(variableName);

		CalculationEnum calculation = CalculationEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_calculation));
		if (calculation != null)
		{
			returnValue.setCalculation(calculation);
		}
		
		String incrementerFactoryClass = atts.getValue(JRXmlConstants.ATTRIBUTE_incrementerFactoryClass);
		if (incrementerFactoryClass != null)
		{
			returnValue.setIncrementerFactoryClassName(incrementerFactoryClass);
		}

		return returnValue;
	}

}

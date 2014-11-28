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
package net.sf.jasperreports.crosstabs.xml;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.crosstabs.type.CrosstabPercentageEnum;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.xml.sax.Attributes;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRCrosstabMeasureFactory extends JRBaseFactory
{
	public static final String ELEMENT_measure = "measure";
	public static final String ELEMENT_measureExpression = "measureExpression";

	public static final String ATTRIBUTE_name = "name";
	public static final String ATTRIBUTE_class = "class";
	public static final String ATTRIBUTE_calculation = "calculation";
	public static final String ATTRIBUTE_incrementerFactoryClass = "incrementerFactoryClass";
	public static final String ATTRIBUTE_percentageOf = "percentageOf";
	public static final String ATTRIBUTE_percentageCalculatorClass = "percentageCalculatorClass";

	public Object createObject(Attributes attributes)
	{
		JRDesignCrosstabMeasure measure = new JRDesignCrosstabMeasure();
		
		measure.setName(attributes.getValue(ATTRIBUTE_name));
		measure.setValueClassName(attributes.getValue(ATTRIBUTE_class));
		measure.setIncrementerFactoryClassName(attributes.getValue(ATTRIBUTE_incrementerFactoryClass));
		
		String calcAttr = attributes.getValue(ATTRIBUTE_calculation);
		if (calcAttr != null)
		{
			CalculationEnum calculation = CalculationEnum.getByName(attributes.getValue(JRXmlConstants.ATTRIBUTE_calculation));
			measure.setCalculation(calculation);

		}
		
		CrosstabPercentageEnum percentage = CrosstabPercentageEnum.getByName(attributes.getValue(ATTRIBUTE_percentageOf));
		if (percentage != null)
		{
			measure.setPercentageType(percentage);
		}
		
		String percentageCalcAttr = attributes.getValue(ATTRIBUTE_percentageCalculatorClass);
		if (percentageCalcAttr != null)
		{
			measure.setPercentageCalculatorClassName(percentageCalcAttr);
		}
		
		return measure;
	}
}

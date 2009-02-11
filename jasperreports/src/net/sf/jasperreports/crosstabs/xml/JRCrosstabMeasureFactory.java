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
package net.sf.jasperreports.crosstabs.xml;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.xml.sax.Attributes;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
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
			Byte calc = (Byte) JRXmlConstants.getCalculationMap().get(calcAttr);
			measure.setCalculation(calc.byteValue());
		}
		
		String percentageAttr = attributes.getValue(ATTRIBUTE_percentageOf);
		if (percentageAttr != null)
		{
			Byte percentageType = (Byte) JRXmlConstants.getCrosstabPercentageMap().get(percentageAttr);
			measure.setPercentageOfType(percentageType.byteValue());
		}
		
		String percentageCalcAttr = attributes.getValue(ATTRIBUTE_percentageCalculatorClass);
		if (percentageCalcAttr != null)
		{
			measure.setPercentageCalculatorClassName(percentageCalcAttr);
		}
		
		return measure;
	}
}

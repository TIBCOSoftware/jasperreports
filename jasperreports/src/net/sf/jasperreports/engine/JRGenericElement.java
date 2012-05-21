/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

/**
 * A "generic" report element that will produce a 
 * {@link JRGenericPrintElement generic print element} in the generated
 * report.
 * 
 * <p>
 * Such elements can be used in simple scenarios that involve generating
 * generic print elements.  In more complex scenarios,
 * {@link JRComponentElement component elements} that produce generic
 * print elements can be implemented.
 * 
 * <p>
 * Generic report elements are comprised of a list of parameters that are to
 * be included in the generated print element.
 * At fill time, expressions associated with the parameters are evaluated and
 * the result is preserved in the print element.
 * Generic report elements cannot stretch at fill time, they will always produce
 * a print element that has the same size as the design element.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRGenericElement extends JRElement
{

	/**
	 * Returns the generic type of this element.
	 * This type will be propagated to the generated print element, and used to
	 * resolve export handler for the print element. 
	 * 
	 * @return the generic type of this element
	 */
	JRGenericElementType getGenericType();
	
	/**
	 * Returns the list of parameters of this element.
	 * 
	 * @return the list of parameters 
	 */
	JRGenericElementParameter[] getParameters();
	
	/**
	 * Returns the evaluation time of this element.
	 * 
	 * <p>
	 * The evaluation time determines the moment at which parameter expressions
	 * are evaluated for this element.  All parameters will be evaluated at the
	 * same moment.
	 * 
	 * @return the evaluation time of this element
	 */
	EvaluationTimeEnum getEvaluationTimeValue();
	
	/**
	 * Returns the name of the evaluation group for this element.
	 * The evaluation group is only present when
	 * {@link #getEvaluationTimeValue() the evaluation time} is
	 * {@link EvaluationTimeEnum#GROUP}.
	 * 
	 * @return the name of the evaluation group for this element
	 */
	String getEvaluationGroupName();
	
}

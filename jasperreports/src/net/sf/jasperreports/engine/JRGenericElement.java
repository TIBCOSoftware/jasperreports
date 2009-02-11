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
	byte getEvaluationTime();
	
	/**
	 * Returns the name of the evaluation group for this element.
	 * The evaluation group is only present when
	 * {@link #getEvaluationTime() the evaluation time} is
	 * {@link JRExpression#EVALUATION_TIME_GROUP}.
	 * 
	 * @return the name of the evaluation group for this element
	 */
	String getEvaluationGroupName();
	
}

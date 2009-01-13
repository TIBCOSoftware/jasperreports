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
package net.sf.jasperreports.engine.design;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRVisitor;

/**
 * A implementation of {@link JRGenericElement} that is to be used at report
 * design time.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignGenericElement extends JRDesignElement implements
		JRGenericElement
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_GENERIC_TYPE = "genericType";

	public static final String PROPERTY_EVALUATION_TIME = "evaluationTime";

	public static final String PROPERTY_EVALUATION_GROUP_NAME = "evaluationGroupName";
	
	public static final String PROPERTY_PARAMETERS = "parameters";
	
	private JRGenericElementType genericType;
	private List parameters = new ArrayList();
	private byte evaluationTime = JRExpression.EVALUATION_TIME_NOW;
	private String evaluationGroupName;
	
	/**
	 * Creates a generic report element.
	 * 
	 * @param defaultStyleProvider the default style provider to use for the element
	 */
	public JRDesignGenericElement(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
	}

	public JRGenericElementParameter[] getParameters()
	{
		return (JRGenericElementParameter[]) parameters.toArray(
				new JRGenericElementParameter[parameters.size()]);
	}
	
	/**
	 * Exposes the internal list of element parameters.
	 * 
	 * @return the list of element parameters
	 * @see #getParameters()
	 */
	public List getParamtersList()
	{
		return parameters;
	}
	
	/**
	 * Adds a parameter to the element.
	 * 
	 * @param parameter the parameter to add.
	 * @see #getParameters()
	 */
	public void addParameter(JRGenericElementParameter parameter)
	{
		this.parameters.add(parameter);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_PARAMETERS, 
				parameter, parameters.size() - 1);
	}

	/**
	 * Removes a parameter from the element.
	 * 
	 * @param parameter the parameter to remove
	 * @return whether the parameter has been found and removed
	 */
	public boolean removeParameter(JRGenericElementParameter parameter)
	{
		int idx = parameters.indexOf(parameter);
		if (idx >= 0)
		{
			parameters.remove(idx);
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_PARAMETERS, 
					parameter, idx);
			return true;
		}
		return false;
	}
	
	/**
	 * Removes a parameter by name from the element.
	 * 
	 * @param parameterName the name of the parameter to remove
	 * @return the removed parameter, or <code>null</code> if not found
	 */
	public JRGenericElementParameter removeParameter(String parameterName)
	{
		JRGenericElementParameter removed = null;
		for (ListIterator it = parameters.listIterator(); it.hasNext();)
		{
			JRGenericElementParameter parameter = (JRGenericElementParameter) it.next();
			if (parameter.getName() != null && parameter.getName().equals(parameterName))
			{
				removed = parameter;
				it.remove();
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_PARAMETERS, 
						parameter, it.nextIndex());
				break;
			}
		}
		return removed;
	}

	public JRGenericElementType getGenericType()
	{
		return genericType;
	}

	/**
	 * Sets the type of the generic element.
	 * 
	 * @param genericType the type of the element.
	 * @see #getGenericType()
	 */
	public void setGenericType(JRGenericElementType genericType)
	{
		Object old = this.genericType;
		this.genericType = genericType;
		getEventSupport().firePropertyChange(PROPERTY_GENERIC_TYPE, old, this.genericType);
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	public void visit(JRVisitor visitor)
	{
		visitor.visitGenericElement(this);
	}
	public byte getEvaluationTime()
	{
		return evaluationTime;
	}

	/**
	 * Sets the evaluation time for the element.
	 * 
	 * <p>
	 * The default evaluation time is {@link JRExpression#EVALUATION_TIME_NOW}.
	 * 
	 * @param evaluationTime the element's evaluation time, one of
	 * <ol>
	 * 	<li>{@link JRExpression#EVALUATION_TIME_NOW}
	 * 	<li>{@link JRExpression#EVALUATION_TIME_BAND}
	 * 	<li>{@link JRExpression#EVALUATION_TIME_COLUMN}
	 * 	<li>{@link JRExpression#EVALUATION_TIME_PAGE}
	 * 	<li>{@link JRExpression#EVALUATION_TIME_GROUP}
	 * 	<li>{@link JRExpression#EVALUATION_TIME_REPORT}
	 * 	<li>{@link JRExpression#EVALUATION_TIME_AUTO}
	 * </ul>
	 * @see #getEvaluationTime()
	 */
	public void setEvaluationTime(byte evaluationTime)
	{
		byte old = this.evaluationTime;
		this.evaluationTime = evaluationTime;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_TIME, 
				old, this.evaluationTime);
	}

	public String getEvaluationGroupName()
	{
		return evaluationGroupName;
	}
	
	/**
	 * Sets the name of the evaluation group.
	 * 
	 * @param evaluationGroupName the evaluation group's name
	 * @see #getEvaluationGroupName()
	 */
	public void setEvaluationGroupName(String evaluationGroupName)
	{
		Object old = this.evaluationGroupName;
		this.evaluationGroupName = evaluationGroupName;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_GROUP_NAME, 
				old, this.evaluationGroupName);
	}

}

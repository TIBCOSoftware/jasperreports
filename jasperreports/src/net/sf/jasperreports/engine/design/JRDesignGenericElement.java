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
package net.sf.jasperreports.engine.design;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;

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
	private List<JRGenericElementParameter> parameters = new ArrayList<JRGenericElementParameter>();
	private EvaluationTimeEnum evaluationTimeValue = EvaluationTimeEnum.NOW;
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
		return parameters.toArray(new JRGenericElementParameter[parameters.size()]);
	}
	
	/**
	 * @deprecated Replaced by {@link #getParametersList()}.
	 */
	public List<JRGenericElementParameter> getParamtersList()
	{
		return getParametersList();
	}
	
	/**
	 * Exposes the internal list of element parameters.
	 * 
	 * @return the list of element parameters
	 * @see #getParameters()
	 */
	public List<JRGenericElementParameter> getParametersList()
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
		for (ListIterator<JRGenericElementParameter> it = parameters.listIterator(); it.hasNext();)
		{
			JRGenericElementParameter parameter = it.next();
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

	public EvaluationTimeEnum getEvaluationTimeValue()
	{
		return evaluationTimeValue;
	}

	/**
	 * Sets the evaluation time for the element.
	 * 
	 * <p>
	 * The default evaluation time is {@link EvaluationTimeEnum#NOW}.
	 * 
	 * @param evaluationTimeValue the element's evaluation time, one of
	 * <ol>
	 * 	<li>{@link EvaluationTimeEnum#NOW}
	 * 	<li>{@link EvaluationTimeEnum#BAND}
	 * 	<li>{@link EvaluationTimeEnum#COLUMN}
	 * 	<li>{@link EvaluationTimeEnum#PAGE}
	 * 	<li>{@link EvaluationTimeEnum#GROUP}
	 * 	<li>{@link EvaluationTimeEnum#REPORT}
	 * 	<li>{@link EvaluationTimeEnum#AUTO}
	 * </ul>
	 * @see #getEvaluationTimeValue()
	 */
	public void setEvaluationTime(EvaluationTimeEnum evaluationTimeValue)
	{
		Object old = this.evaluationTimeValue;
		this.evaluationTimeValue = evaluationTimeValue;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_TIME, 
				old, this.evaluationTimeValue);
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
	
	/**
	 * 
	 */
	public Object clone()
	{
		JRDesignGenericElement clone = (JRDesignGenericElement)super.clone();
		clone.parameters = JRCloneUtils.cloneList(parameters);
		return clone;
	}

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private byte evaluationTime;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			evaluationTimeValue = EvaluationTimeEnum.getByValue(evaluationTime);
		}
	}
}

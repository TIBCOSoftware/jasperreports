/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import net.sf.jasperreports.engine.util.JRClassLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseVariable implements JRVariable, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected String name = null;
	protected String valueClassName = java.lang.String.class.getName();
	protected String valueClassRealName = null;
	protected String incrementerFactoryClassName = null;
	protected String incrementerFactoryClassRealName = null;
	protected ResetTypeEnum resetTypeValue = ResetTypeEnum.REPORT;
	protected IncrementTypeEnum incrementTypeValue = IncrementTypeEnum.NONE;
	protected CalculationEnum calculationValue = CalculationEnum.NOTHING;
	protected boolean isSystemDefined = false;

	protected transient Class valueClass = null;
	protected transient Class incrementerFactoryClass = null;

	/**
	 *
	 */
	protected JRExpression expression = null;
	protected JRExpression initialValueExpression = null;
	protected JRGroup resetGroup = null;
	protected JRGroup incrementGroup = null;


	/**
	 *
	 */
	protected JRBaseVariable()
	{
	}
	
	
	/**
	 *
	 */
	protected JRBaseVariable(JRVariable variable, JRBaseObjectFactory factory)
	{
		factory.put(variable, this);
		
		name = variable.getName();
		valueClassName = variable.getValueClassName();
		incrementerFactoryClassName = variable.getIncrementerFactoryClassName();
		resetTypeValue = variable.getResetTypeValue();
		incrementTypeValue = variable.getIncrementTypeValue();
		calculationValue = variable.getCalculationValue();
		isSystemDefined = variable.isSystemDefined();
		
		expression = factory.getExpression(variable.getExpression());
		initialValueExpression = factory.getExpression(variable.getInitialValueExpression());

		resetGroup = factory.getGroup(variable.getResetGroup());
		incrementGroup = factory.getGroup(variable.getIncrementGroup());
	}
		

	/**
	 *
	 */
	public String getName()
	{
		return this.name;
	}
		
	/**
	 *
	 */
	public Class getValueClass()
	{
		if (valueClass == null)
		{
			String className = getValueClassRealName();
			if (className != null)
			{
				try
				{
					valueClass = JRClassLoader.loadClassForName(className);
				}
				catch(ClassNotFoundException e)
				{
					throw new JRRuntimeException(e);
				}
			}
		}
		
		return valueClass;
	}
		
	/**
	 *
	 */
	public String getValueClassName()
	{
		return valueClassName;
	}

	/**
	 *
	 */
	private String getValueClassRealName()
	{
		if (valueClassRealName == null)
		{
			valueClassRealName = JRClassLoader.getClassRealName(valueClassName);
		}
		
		return valueClassRealName;
	}

	/**
	 *
	 */
	public Class getIncrementerFactoryClass()
	{
		if (incrementerFactoryClass == null)
		{
			String className = getIncrementerFactoryClassRealName();
			if (className != null)
			{
				try
				{
					incrementerFactoryClass = JRClassLoader.loadClassForName(className);
				}
				catch(ClassNotFoundException e)
				{
					throw new JRRuntimeException(e);
				}
			}
		}
		
		return incrementerFactoryClass;
	}
		
	/**
	 *
	 */
	public String getIncrementerFactoryClassName()
	{
		return incrementerFactoryClassName;
	}

	/**
	 *
	 */
	private String getIncrementerFactoryClassRealName()
	{
		if (incrementerFactoryClassRealName == null)
		{
			incrementerFactoryClassRealName = JRClassLoader.getClassRealName(incrementerFactoryClassName);
		}
		
		return incrementerFactoryClassRealName;
	}

	/**
	 * @deprecated Replaced by {@link #getResetTypeValue()}
	 */
	public byte getResetType()
	{
		return getResetTypeValue().getValue();
	}
		
	/**
	 *
	 */
	public ResetTypeEnum getResetTypeValue()
	{
		return this.resetTypeValue;
	}
		
	/**
	 * @deprecated Replaced by {@link #getIncrementTypeValue()}
	 */
	public byte getIncrementType()
	{
		return getIncrementTypeValue().getValue();
	}
		
	/**
	 *
	 */
	public IncrementTypeEnum getIncrementTypeValue()
	{
		return this.incrementTypeValue;
	}
		
	/**
	 * @deprecated Replaced by {@link #getCalculationValue()}
	 */
	public byte getCalculation()
	{
		return getCalculationValue().getValue();
	}

	/**
	 * 
	 */
	public CalculationEnum getCalculationValue()
	{
		return this.calculationValue;
	}

	/**
	 *
	 */
	public boolean isSystemDefined()
	{
		return this.isSystemDefined;
	}

	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return this.expression;
	}
		
	/**
	 *
	 */
	public JRExpression getInitialValueExpression()
	{
		return this.initialValueExpression;
	}
		
	/**
	 *
	 */
	public JRGroup getResetGroup()
	{
		return this.resetGroup;
	}
		
	/**
	 *
	 */
	public JRGroup getIncrementGroup()
	{
		return this.incrementGroup;
	}

	/**
	 *
	 */
	public Object clone() 
	{
		JRBaseVariable clone = null;
		
		try
		{
			clone = (JRBaseVariable)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}

		if (expression != null)
		{
			clone.expression = (JRExpression)expression.clone();
		}
		if (initialValueExpression != null)
		{
			clone.initialValueExpression = (JRExpression)initialValueExpression.clone();
		}
		return clone;
	}
		
	/**
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2; //NOPMD
	private byte resetType;
	private byte incrementType;
	private byte calculation;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			resetTypeValue = ResetTypeEnum.getByValue(resetType);
			incrementTypeValue = IncrementTypeEnum.getByValue(incrementType);
			calculationValue = CalculationEnum.getByValue(calculation);
		}
		
	}

}

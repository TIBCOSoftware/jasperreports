/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
import net.sf.jasperreports.engine.util.CloneStore;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.StoreCloneable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRBaseVariable implements JRVariable, Serializable, StoreCloneable<JRBaseVariable>
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected String name;
	protected String valueClassName = java.lang.String.class.getName();
	protected String valueClassRealName;
	protected String incrementerFactoryClassName;
	protected String incrementerFactoryClassRealName;
	protected ResetTypeEnum resetTypeValue = ResetTypeEnum.REPORT;
	protected IncrementTypeEnum incrementTypeValue = IncrementTypeEnum.NONE;
	protected CalculationEnum calculationValue = CalculationEnum.NOTHING;
	protected boolean isSystemDefined;

	protected transient Class<?> valueClass;
	protected transient Class<?> incrementerFactoryClass;

	/**
	 *
	 */
	protected JRExpression expression;
	protected JRExpression initialValueExpression;
	protected JRGroup resetGroup;
	protected JRGroup incrementGroup;


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
		

	@Override
	public String getName()
	{
		return this.name;
	}
		
	@Override
	public Class<?> getValueClass()
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
		
	@Override
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

	@Override
	public Class<?> getIncrementerFactoryClass()
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
		
	@Override
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

	@Override
	public ResetTypeEnum getResetTypeValue()
	{
		return this.resetTypeValue;
	}
		
	@Override
	public IncrementTypeEnum getIncrementTypeValue()
	{
		return this.incrementTypeValue;
	}
		
	@Override
	public CalculationEnum getCalculationValue()
	{
		return this.calculationValue;
	}

	@Override
	public boolean isSystemDefined()
	{
		return this.isSystemDefined;
	}

	@Override
	public JRExpression getExpression()
	{
		return this.expression;
	}
		
	@Override
	public JRExpression getInitialValueExpression()
	{
		return this.initialValueExpression;
	}
		
	@Override
	public JRGroup getResetGroup()
	{
		return this.resetGroup;
	}
		
	@Override
	public JRGroup getIncrementGroup()
	{
		return this.incrementGroup;
	}

	@Override
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

		clone.expression = JRCloneUtils.nullSafeClone(expression);
		clone.initialValueExpression = JRCloneUtils.nullSafeClone(initialValueExpression);
		
		return clone;
	}

	@Override
	public JRBaseVariable clone(CloneStore cloneStore)
	{
		JRBaseVariable clone = (JRBaseVariable) clone();
		//early store for circular dependencies
		cloneStore.store(this, clone);
		clone.resetGroup = cloneStore.clone(resetGroup);
		clone.incrementGroup = cloneStore.clone(incrementGroup);
		return clone;
	}
		
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2; //NOPMD
	/**
	 * @deprecated
	 */
	private byte resetType;
	/**
	 * @deprecated
	 */
	private byte incrementType;
	/**
	 * @deprecated
	 */
	private byte calculation;
	
	@SuppressWarnings("deprecation")
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

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.base.JRBaseVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignVariable extends JRBaseVariable
{


	/**
	 *
	 */
	private static final long serialVersionUID = 606;

	/**
	 *
	 */
	public void setName(String name)
	{
		this.name = name;
	}
		
	/**
	 *
	 */
	public void setValueClass(Class clazz)
	{
		setValueClassName(clazz.getName());
	}
		
	/**
	 *
	 */
	public void setValueClassName(String className)
	{
		valueClassName = className;
		valueClass = null;
	}
		
	/**
	 *
	 */
	public void setIncrementerFactoryClass(Class clazz)
	{
		setIncrementerFactoryClassName(clazz.getName());
	}
		
	/**
	 *
	 */
	public void setIncrementerFactoryClassName(String className)
	{
		incrementerFactoryClassName = className;
		incrementerFactoryClass = null;
	}
		
	/**
	 *
	 */
	public void setResetType(byte resetType)
	{
		this.resetType = resetType;
	}
		
	/**
	 *
	 */
	public void setIncrementType(byte incrementType)
	{
		this.incrementType = incrementType;
	}
		
	/**
	 *
	 */
	public void setCalculation(byte calculation)
	{
		this.calculation = calculation;
	}

	/**
	 *
	 */
	public void setSystemDefined(boolean isSystemDefined)
	{
		this.isSystemDefined = isSystemDefined;
	}

	/**
	 *
	 */
	public void setExpression(JRExpression expression)
	{
		this.expression = expression;
	}
		
	/**
	 *
	 */
	public void setInitialValueExpression(JRExpression expression)
	{
		this.initialValueExpression = expression;
	}
	
	/**
	 *
	 */
	public void setResetGroup(JRGroup group)
	{
		this.resetGroup = group;
	}
		
	/**
	 *
	 */
	public void setIncrementGroup(JRGroup group)
	{
		this.incrementGroup = group;
	}
		
	/**
	 *
	 */
	public void setCountVariable(JRVariable countVariable)
	{
		this.countVariable = countVariable;
	}

	/**
	 *
	 */
	public void setSumVariable(JRVariable sumVariable)
	{
		this.sumVariable = sumVariable;
	}

	/**
	 *
	 */
	public void setVarianceVariable(JRVariable varianceVariable)
	{
		this.varianceVariable = varianceVariable;
	}
		

}

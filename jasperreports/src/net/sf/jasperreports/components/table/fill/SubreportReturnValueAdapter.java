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
package net.sf.jasperreports.components.table.fill;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.VariableReturnValue;
import net.sf.jasperreports.engine.type.CalculationEnum;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class SubreportReturnValueAdapter implements JRSubreportReturnValue
{
	
	private final VariableReturnValue returnValue;
	
	public SubreportReturnValueAdapter(VariableReturnValue returnValue)
	{
		this.returnValue = returnValue;
	}

	/**
	 * @deprecated Replaced by {@link #getFromVariable()}.
	 */
	@Override
	public String getSubreportVariable()
	{
		return getFromVariable();
	}

	@Override
	public String getFromVariable()
	{
		return returnValue.getFromVariable();
	}

	@Override
	public String getToVariable()
	{
		return returnValue.getToVariable();
	}

	/**
	 * @deprecated Replaced by {@link #getCalculation()}.
	 */
	@Override
	public CalculationEnum getCalculationValue()
	{
		return getCalculation();
	}

	@Override
	public CalculationEnum getCalculation()
	{
		return returnValue.getCalculation();
	}

	@Override
	public String getIncrementerFactoryClassName()
	{
		return returnValue.getIncrementerFactoryClassName();
	}

	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}

}

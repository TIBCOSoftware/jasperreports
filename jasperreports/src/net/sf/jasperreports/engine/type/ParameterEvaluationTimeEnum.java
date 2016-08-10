/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.type;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.part.PartEvaluationTime;

/**
 * Defines specific moments in time when the default value expression of a parameter is supposed to be evaluated.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public enum ParameterEvaluationTimeEnum implements NamedEnum
{
	/**
	 * Evaluate the parameter default value expression before parameter contributors.
	 */
	EARLY("Early"),
	/**
	 * Evaluate the parameter default value expression after parameter contributors.
	 * 
	 * @see PartEvaluationTime#getEvaluationGroup()
	 */
	LATE("Late");
	
	/**
	 * Provides a default value for the {@link JRParameter#getEvaluationTime()} property of parameters and can be set either globally or at dataset level.
	 */
	public static final String PROPERTY_EVALUATION_TIME = JRPropertiesUtil.PROPERTY_PREFIX + "parameter.evaluation.time";

	private final String name;
	
	private ParameterEvaluationTimeEnum(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public static ParameterEvaluationTimeEnum byName(String name)
	{
		return EnumUtil.getEnumByName(values(), name);
	}
}

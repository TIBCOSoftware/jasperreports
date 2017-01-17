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
package net.sf.jasperreports.metadata.properties;

import net.sf.jasperreports.annotations.properties.PropertyScopeQualification;
import net.sf.jasperreports.annotations.properties.PropertyScopeQualificationType;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class StandardPropertyMetadataScopeQualification implements PropertyMetadataScopeQualification
{

	private PropertyScopeQualificationType type;
	private String value;
	
	public StandardPropertyMetadataScopeQualification()
	{
	}

	public StandardPropertyMetadataScopeQualification(PropertyScopeQualification qualification)
	{
		this.type = qualification.type();
		this.value = qualification.value();
	}

	@Override
	public PropertyScopeQualificationType getType()
	{
		return type;
	}

	public void setType(PropertyScopeQualificationType type)
	{
		this.type = type;
	}

	@Override
	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

}

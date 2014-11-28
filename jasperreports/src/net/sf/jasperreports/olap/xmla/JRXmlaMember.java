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
package net.sf.jasperreports.olap.xmla;

import net.sf.jasperreports.olap.result.JROlapMember;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRXmlaMember implements JROlapMember
{

	private final String name;
	private final String uniqueName;
	private final String dimensionName;
	private final String levelName;
	private final int depth;

	public JRXmlaMember(String name, String uniqueName, String dimensionName, String levelName, int depth)
	{
		this.name = name;
		this.uniqueName = uniqueName;
		this.dimensionName = dimensionName;
		this.levelName = levelName;
		this.depth = depth;
	}
	
	public int getDepth()
	{
		return depth;
	}

	public String getName()
	{
		return name;
	}

	public JROlapMember getParentMember()
	{
		// not implemented
		return null;
	}

	public Object getPropertyValue(String propertyName)
	{
		throw new UnsupportedOperationException("Member properties are not supported by the XML/A query executer");
	}

	public String getUniqueName()
	{
		return uniqueName;
	}
	
	public String getLevelName()
	{
		return levelName;
	}

	
	public String getDimensionName()
	{
		return dimensionName;
	}

	public Object getMember()
	{
		throw new UnsupportedOperationException("XML/A member cannot be converted to a Member");
	}

}

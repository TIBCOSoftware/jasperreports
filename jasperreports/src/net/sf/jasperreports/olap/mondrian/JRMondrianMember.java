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
package net.sf.jasperreports.olap.mondrian;

import mondrian.olap.Member;
import net.sf.jasperreports.olap.result.JROlapMember;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRMondrianMember implements JROlapMember
{

	private final Member member;
	private final JRMondrianMember parent;

	public JRMondrianMember(Member member, JRMondrianFactory factory)
	{
		this.member = member;
		this.parent = factory.createMember(member.getParentMember());
	}
	
	public int getDepth()
	{
		return member.getDepth();
	}

	public String getName()
	{
		return member.getName();
	}

	public JROlapMember getParentMember()
	{
		return parent;
	}

	public Object getPropertyValue(String propertyName)
	{
		return member.getPropertyValue(propertyName);
	}

	public String getUniqueName()
	{
		return member.getUniqueName();
	}

	public Object getMember()
	{
		return member;
	}

}

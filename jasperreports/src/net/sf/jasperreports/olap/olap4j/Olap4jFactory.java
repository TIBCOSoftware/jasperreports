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
package net.sf.jasperreports.olap.olap4j;

import java.util.HashMap;
import java.util.Map;

import org.olap4j.metadata.Member;


/**
 * @author swood
 */
public class Olap4jFactory
{
	
	private final Map<String, Olap4jMember> members;
	
	public Olap4jFactory()
	{
		members = new HashMap<String, Olap4jMember>();
	}
	
	public Olap4jMember createMember(Member member)
	{
		Olap4jMember mondrianMember;
		if (member == null)
		{
			mondrianMember = null;
		}
		else
		{
			String key = member.getUniqueName();
			mondrianMember = members.get(key);
			if (mondrianMember == null)
			{
				mondrianMember = new Olap4jMember(member, this);
				members.put(key, mondrianMember);
			}
		}
		return mondrianMember;
	}
	
}

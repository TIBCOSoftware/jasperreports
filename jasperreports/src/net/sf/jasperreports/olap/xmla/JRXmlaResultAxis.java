/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.olap.xmla;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.olap.result.JROlapHierarchy;
import net.sf.jasperreports.olap.result.JROlapMemberTuple;
import net.sf.jasperreports.olap.result.JROlapResultAxis;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlaResultAxis implements JROlapResultAxis
{

	private final String axisName;
	private final List hierarchyList;
	private JRXmlaHierarchy[] hierarchies;
	private final List tuples;
	
	public JRXmlaResultAxis(String axisName)
	{
		this.axisName = axisName;
		this.hierarchyList = new ArrayList();
		this.tuples = new ArrayList();
	}
	
	public String getAxisName()
	{
		return axisName;
	}

	public JROlapHierarchy[] getHierarchiesOnAxis()
	{
		return ensureHierarchyArray();
	}

	public JROlapMemberTuple getTuple(int index)
	{
		if (index < 0 || index >= tuples.size())
		{
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + tuples.size());
		}
		
		return (JROlapMemberTuple) tuples.get(index);
	}

	public int getTupleCount()
	{
		return tuples.size();
	}

	public void addHierarchy(JRXmlaHierarchy hierarchy)
	{
		hierarchyList.add(hierarchy);
		resetHierarchyArray();
	}
	
	public void addTuple(JRXmlaMemberTuple tuple)
	{
		tuples.add(tuple);
		
		copyLevelInfo(tuple);
	}

	protected void copyLevelInfo(JRXmlaMemberTuple tuple)
	{
		JRXmlaMember[] members = tuple.getXmlaMembers();
		int idx = 0;
		for (Iterator it = hierarchyList.iterator(); it.hasNext() && idx < members.length; ++idx)
		{
			JRXmlaHierarchy hierarchy = (JRXmlaHierarchy) it.next();
			JRXmlaMember member = members[idx];
			if (hierarchy.getDimensionName().equals(member.getDimensionName()))
			{
				hierarchy.setLevel(member.getLevelName(), member.getDepth());
			}
		}
		
	}

	protected JRXmlaHierarchy[] ensureHierarchyArray()
	{
		if (hierarchies == null)
		{
			hierarchies = new JRXmlaHierarchy[hierarchyList.size()];
			hierarchies = (JRXmlaHierarchy[]) hierarchyList.toArray(hierarchies);
		}
		return hierarchies;
	}

	protected void resetHierarchyArray()
	{
		hierarchies = null;
	}

}

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
package net.sf.jasperreports.olap.mondrian;

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.olap.result.JROlapHierarchy;
import net.sf.jasperreports.olap.result.JROlapMemberTuple;
import net.sf.jasperreports.olap.result.JROlapResultAxis;
import mondrian.olap.Axis;
import mondrian.olap.Hierarchy;
import mondrian.olap.Position;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRMondrianAxis implements JROlapResultAxis
{
	
	private final JRMondrianTuple[] tuples;
	private final JRMondrianHierarchy[] hierarchies;
	
	public JRMondrianAxis(Axis axis, Hierarchy[] axisHierarchies, JRMondrianFactory factory)
	{
		List positions = axis.getPositions();
		tuples = new JRMondrianTuple[positions.size()];
		
		int idx = 0;
		for (Iterator it = positions.iterator(); it.hasNext(); ++idx)
		{
			Position position = (Position) it.next();
			tuples[idx] = new JRMondrianTuple(position, factory);
		}
		
		hierarchies = new JRMondrianHierarchy[axisHierarchies.length];
		for (int i = 0; i < axisHierarchies.length; i++)
		{
			hierarchies[i] = new JRMondrianHierarchy(axisHierarchies[i]);
		}
	}

	public JROlapHierarchy[] getHierarchiesOnAxis()
	{
		return hierarchies;
	}

	public JROlapMemberTuple getTuple(int index)
	{
		if (index < 0 || index >= tuples.length)
		{
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + tuples.length);
		}
		
		return tuples[index];
	}

	public int getTupleCount()
	{
		return tuples.length;
	}

}

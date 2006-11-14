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

import mondrian.olap.Hierarchy;
import mondrian.olap.Level;
import net.sf.jasperreports.olap.result.JROlapHierarchy;
import net.sf.jasperreports.olap.result.JROlapHierarchyLevel;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRMondrianHierarchy implements JROlapHierarchy
{
	
	private final Hierarchy hierarchy;
	private final JRMondrianLevel[] levels;
	
	public JRMondrianHierarchy(Hierarchy hierarchy)
	{
		this.hierarchy = hierarchy;
		
		Level[] hierarchyLevels = hierarchy.getLevels();
		levels = new JRMondrianLevel[hierarchyLevels.length];
		for (int i = 0; i < hierarchyLevels.length; i++)
		{
			levels[i] = new JRMondrianLevel(hierarchyLevels[i]);
		}
	}

	public String getDimensionName()
	{
		return hierarchy.getDimension().getName();
	}

	public JROlapHierarchyLevel[] getLevels()
	{
		return levels;
	}

}

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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.olap.result.JROlapHierarchy;
import net.sf.jasperreports.olap.result.JROlapHierarchyLevel;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlaHierarchy implements JROlapHierarchy
{
	
	private final static Log log = LogFactory.getLog(JRXmlaHierarchy.class);

	private final String dimensionName;
	private final List levels;
	private JRXmlaHierarchyLevel[] levelArray;

	public JRXmlaHierarchy(String dimensionName)
	{
		this.dimensionName = dimensionName;
		this.levels = new ArrayList();
	}

	public String getDimensionName()
	{
		return dimensionName;
	}

	public JROlapHierarchyLevel[] getLevels()
	{
		return ensureLevelArray();
	}

	public void setLevel(String levelName, int depth)
	{
		int levelCount = levels.size();
		if (depth >= levelCount)
		{
			for (int i = levelCount; i <= depth; ++i)
			{
				levels.add(null);
			}
		}
		
		JRXmlaHierarchyLevel level = (JRXmlaHierarchyLevel) levels.get(depth);
		if (level == null)
		{
			level = new JRXmlaHierarchyLevel(levelName, depth);
			levels.set(depth, level);
		}
		else if (!levelName.equals(level.getName()))
		{
			if (log.isWarnEnabled())
			{
				log.warn("Different level name \"" + levelName + "\" found for level \"" + level.getName() + "\" at depth " + depth);
			}
		}
		
		resetLevelArray();
	}

	protected JRXmlaHierarchyLevel[] ensureLevelArray()
	{
		if (levelArray == null)
		{
			levelArray = new JRXmlaHierarchyLevel[levels.size()];
			levelArray = (JRXmlaHierarchyLevel[]) levels.toArray(levelArray);
		}
		return levelArray;
	}

	protected void resetLevelArray()
	{
		levelArray = null;
	}
}

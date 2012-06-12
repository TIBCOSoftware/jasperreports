/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.olap.result.JROlapHierarchy;
import net.sf.jasperreports.olap.result.JROlapHierarchyLevel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlaHierarchy implements JROlapHierarchy
{
	
	private final static Log log = LogFactory.getLog(JRXmlaHierarchy.class);

	private final String dimensionName;
	private final List<JRXmlaHierarchyLevel> levels;
	private JRXmlaHierarchyLevel[] levelArray;

	public JRXmlaHierarchy(String dimensionName)
	{
		this.dimensionName = dimensionName;
		this.levels = new ArrayList<JRXmlaHierarchyLevel>();
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
		
		JRXmlaHierarchyLevel level = levels.get(depth);
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
	
	// MPenningroth 21-April-2009 deal with case when dimension is <dimension>.<hierarchy> form
	public String getHierarchyUniqueName()
	{
		return dimensionName;
	}

	protected JRXmlaHierarchyLevel[] ensureLevelArray()
	{
		if (levelArray == null)
		{
			levelArray = new JRXmlaHierarchyLevel[levels.size()];
			levelArray = levels.toArray(levelArray);
		}
		return levelArray;
	}

	protected void resetLevelArray()
	{
		levelArray = null;
	}
}

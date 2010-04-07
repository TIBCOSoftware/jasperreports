/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.table.fill;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.base.JRBaseGroup;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class TableReportBaseObjectFactory extends JRBaseObjectFactory
{

	private final Map<String, JRBaseGroup> groupMap = 
		new HashMap<String, JRBaseGroup>();
	
	public TableReportBaseObjectFactory(TableReportDataset reportDataset)
	{
		super((JRExpressionCollector) null);
		
		// init all groups to have the dataset object in the cache
		initGroups(reportDataset);
	}

	private void initGroups(TableReportDataset reportDataset)
	{
		for (JRGroup group : reportDataset.getGroups())
		{
			getGroup(group);
		}
	}
	
	@Override
	public JRExpression getExpression(JRExpression expression,
			boolean assignNotUsedId)
	{
		// same expressions are used in the table report
		return expression;
	}

	@Override
	protected JRBaseGroup getGroup(JRGroup group)
	{
		if (group == null)
		{
			return null;
		}
		
		// cache groups by names so that variable groups and dataset groups
		// result in the same objects
		JRBaseGroup baseGroup = groupMap.get(group.getName());
		if (baseGroup == null)
		{
			baseGroup = super.getGroup(group);
			groupMap.put(group.getName(), baseGroup);
		}
		return baseGroup;
	}
	
}

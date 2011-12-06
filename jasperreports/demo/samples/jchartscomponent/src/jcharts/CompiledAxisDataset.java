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
package jcharts;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseElementDataset;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class CompiledAxisDataset extends JRBaseElementDataset implements AxisDataset
{
	
	private static final long serialVersionUID = 1L;
	
	private final JRExpression labelExpression;
	private final JRExpression valueExpression;
	
	public CompiledAxisDataset(AxisDataset dataset, JRBaseObjectFactory factory)
	{
		super(dataset, factory);
		
		labelExpression = factory.getExpression(dataset.getLabelExpression());
		valueExpression = factory.getExpression(dataset.getValueExpression());
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		AxisChartCompiler.collectExpressions(this, collector);
	}

	public JRExpression getLabelExpression()
	{
		return labelExpression;
	}

	public JRExpression getValueExpression()
	{
		return valueExpression;
	}

}

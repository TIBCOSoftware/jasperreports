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
package net.sf.jasperreports.components.ofc;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseElementDataset;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: CompiledPieDataset.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class CompiledPieDataset extends JRBaseElementDataset implements PieDataset
{
	
	private static final long serialVersionUID = 1L;
	
	private final JRExpression keyExpression;
	private final JRExpression valueExpression;
	
	public CompiledPieDataset(PieDataset dataset, JRBaseObjectFactory factory)
	{
		super(dataset, factory);
		
		keyExpression = factory.getExpression(dataset.getKeyExpression());
		valueExpression = factory.getExpression(dataset.getValueExpression());
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		PieChartCompiler.collectExpressions(this, collector);
	}

	public JRExpression getKeyExpression()
	{
		return keyExpression;
	}

	public JRExpression getValueExpression()
	{
		return valueExpression;
	}

}

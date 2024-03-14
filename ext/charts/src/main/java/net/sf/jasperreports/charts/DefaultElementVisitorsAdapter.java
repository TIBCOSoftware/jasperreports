/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.charts;

import net.sf.jasperreports.charts.base.ChartsBaseObjectFactory;
import net.sf.jasperreports.charts.design.ChartsVerifier;
import net.sf.jasperreports.charts.fill.ChartsFillObjectFactory;
import net.sf.jasperreports.charts.util.ChartsApiWriter;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRVerifierVisitor;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.JRApiWriterVisitor;
import net.sf.jasperreports.engine.util.JRElementsVisitor;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DefaultElementVisitorsAdapter implements ElementVisitorAdapter
{

	private static final DefaultElementVisitorsAdapter INSTANCE = new DefaultElementVisitorsAdapter();
	
	public static DefaultElementVisitorsAdapter instance()
	{
		return INSTANCE;
	}
	
	@Override
	public ChartVisitor getChartVisitor(JRVisitor visitor)
	{
		if (visitor instanceof JRElementsVisitor)
		{
			return new ChartsElementsVisitor((JRElementsVisitor)visitor);//FIXME7
		}
		else if (visitor instanceof JRBaseObjectFactory)
		{
			return new ChartsBaseObjectFactory((JRBaseObjectFactory)visitor);//FIXME7
		}
		else if (visitor instanceof JRFillObjectFactory)
		{
			return new ChartsFillObjectFactory((JRFillObjectFactory)visitor);//FIXME7
		}
		else if (visitor instanceof JRVerifierVisitor)
		{
			return new ChartsVerifier((JRVerifierVisitor)visitor);//FIXME7
		}
		else if (visitor instanceof JRApiWriterVisitor)
		{
			return new ChartsApiWriter((JRApiWriterVisitor)visitor);//FIXME7
		}
		return  null;
	}

}

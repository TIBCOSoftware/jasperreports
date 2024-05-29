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
package net.sf.jasperreports.charts.fill;

import java.awt.Color;

import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRItemLabel;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated To be removed
 */
public class JRFillBar3DPlot extends JRFillBarPlot
{
	/**
	 * 
	 */
	public JRFillBar3DPlot( JRBar3DPlot barPlot, ChartsFillObjectFactory factory ){
		super( barPlot, factory );

		JRFillObjectFactory parentFactory = factory.getParent();

		categoryAxisLabelFont = parentFactory.getFont(barPlot.getChart(), barPlot.getCategoryAxisLabelFont()); 
		categoryAxisLabelColor = barPlot.getOwnCategoryAxisLabelColor();
		categoryAxisTickLabelFont = parentFactory.getFont(barPlot.getChart(), barPlot.getCategoryAxisTickLabelFont());
		categoryAxisTickLabelColor = barPlot.getOwnCategoryAxisTickLabelColor();
		categoryAxisLineColor = barPlot.getOwnCategoryAxisLineColor();
		
		valueAxisLabelFont = parentFactory.getFont(barPlot.getChart(), barPlot.getValueAxisLabelFont());
		valueAxisLabelColor = barPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = parentFactory.getFont(barPlot.getChart(), barPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = barPlot.getOwnValueAxisTickLabelColor();
		valueAxisLineColor = barPlot.getOwnValueAxisLineColor();
	}
}

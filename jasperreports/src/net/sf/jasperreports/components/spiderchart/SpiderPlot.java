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
package net.sf.jasperreports.components.spiderchart;

import java.awt.Color;

import net.sf.jasperreports.components.charts.ChartPlot;
import net.sf.jasperreports.components.spiderchart.type.SpiderRotationEnum;
import net.sf.jasperreports.components.spiderchart.type.TableOrderEnum;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: SpiderPlot.java 3889 2010-07-16 10:52:00Z shertage $
 */
public interface SpiderPlot extends ChartPlot
{

	/**
	 * 
	 */
	public JRExpression getMaxValueExpression();
	

	/**
	 * 
	 */
	public JRFont getLabelFont();

	
	/**
	 * 
	 */
	public SpiderRotationEnum getRotation();

	
	/**
	 * 
	 */
	public TableOrderEnum getTableOrder();

	
	/**
	 * 
	 */
	public Boolean getWebFilled();
	

	/**
	 * 
	 */
	public Double getStartAngle();
	

	/**
	 * 
	 */
	public Double getHeadPercent();
	

	/**
	 * 
	 */
	public Double getInteriorGap();
	

	/**
	 * 
	 */
	public Color getAxisLineColor();
	

	/**
	 * 
	 */
	public Float getAxisLineWidth();
	

	/**
	 * 
	 */
	public Double getLabelGap();
	

	/**
	 * 
	 */
	public Color getLabelColor();
}

/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.components.charts;

import java.awt.Color;
import java.io.Serializable;

import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRHyperlink;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public interface ChartSettings extends JRAnchor, JRHyperlink, Serializable
{

	/**
	 * 
	 */
	public Boolean getShowLegend();

	/**
	 *
	 */
	public Color getBackcolor();

	/**
	 *
	 */
	public JRFont getTitleFont();

	/**
	 *
	 */
	public EdgeEnum getTitlePosition();

	/**
	 *
	 */
	public Color getTitleColor();

	/**
	 *
	 */
	public JRFont getSubtitleFont();

	/**
	 *
	 */
	public Color getSubtitleColor();

	/**
	 *
	 */
	public Color getLegendBackgroundColor(); 

	/**
	 *
	 */
	public Color getLegendColor();
	
	/**
	 *
	 */
	public JRFont getLegendFont();

	/**
	 *
	 */
	public EdgeEnum getLegendPosition();


	/**
	 *
	 */
	public JRExpression getTitleExpression();
 
	/**
	 *
	 */
	public JRExpression getSubtitleExpression();

	/**
	 *
	 */
	public byte getChartType();

	/**
	 *
	 */
	public String getRenderType();
	
	/**
	 * Gets a user specified chart customizer class name.
	 * @see ChartCustomizer
 	 */
	public String getCustomizerClass();

	

}

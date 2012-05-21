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
package net.sf.jasperreports.charts.fill;

import java.awt.Color;

import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillValueDisplay implements JRValueDisplay
{

	/**
	 *
	 */
	protected JRValueDisplay parent;

	/**
	 *
	 */
	protected JRChart chart;

	/**
	 *
	 */
	public JRFillValueDisplay(JRValueDisplay valueDisplay, JRFillObjectFactory factory)
	{
		factory.put(valueDisplay, this);

		parent = valueDisplay;
		
		chart = (JRChart)factory.getVisitResult(valueDisplay.getChart());
	}

	/**
	 *
	 */
	public JRChart getChart()
	{
		return chart;
	}
	
	/**
	 *
	 */
	public Color getColor()
	{
		return parent.getColor();
	}

	/**
	 *
	 */
	public String getMask(){
		return parent.getMask();
	}

	/**
	 *
	 */
	public JRFont getFont()
	{
		return parent.getFont();
	}
	
	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}
}

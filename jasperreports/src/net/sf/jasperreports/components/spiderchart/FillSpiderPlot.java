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

import org.jfree.chart.plot.PlotOrientation;

import net.sf.jasperreports.components.spiderchart.type.SpiderRotationEnum;
import net.sf.jasperreports.components.spiderchart.type.TableOrderEnum;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: FillSpiderPlot.java 3889 2010-07-16 10:52:00Z shertage $
 */
public class FillSpiderPlot implements SpiderPlot
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	

	/**
	 *
	 */
	protected SpiderPlot parent;

	/**
	 *
	 */
	public FillSpiderPlot(
		SpiderPlot spiderPlot, 
		JRFillObjectFactory factory
		)
	{
		factory.put(spiderPlot, this);
		parent = spiderPlot;
	}
		

	/**
	 * @return the labelFont
	 */
	public JRFont getLabelFont() {
		return parent.getLabelFont();
	}

	/**
	 * @return the maxValueExpression
	 */
	public JRExpression getMaxValueExpression() {
		return parent.getMaxValueExpression();
	}

	/**
	 * @return the rotation
	 */
	public SpiderRotationEnum getRotation() {
		return parent.getRotation();
	}

	/**
	 * @return the tableOrder
	 */
	public TableOrderEnum getTableOrder() {
		return parent.getTableOrder();
	}

	/**
	 * @return the webFilled
	 */
	public Boolean getWebFilled() {
		return parent.getWebFilled();
	}

	/**
	 * @return the startAngle
	 */
	public Double getStartAngle() {
		return parent.getStartAngle();
	}

	/**
	 * @return the headPercent
	 */
	public Double getHeadPercent() {
		return parent.getHeadPercent();
	}

	/**
	 * @return the interiorGap
	 */
	public Double getInteriorGap() {
		return parent.getInteriorGap();
	}

	/**
	 * @return the axisLineColor
	 */
	public Color getAxisLineColor() {
		return parent.getAxisLineColor();
	}

	/**
	 * @return the axisLineWidth
	 */
	public Float getAxisLineWidth() {
		return parent.getAxisLineWidth();
	}

	/**
	 * @return the labelGap
	 */
	public Double getLabelGap() {
		return parent.getLabelGap();
	}

	/**
	 * @return the labelColor
	 */
	public Color getLabelColor() {
		return parent.getLabelColor();
	}


	public Color getBackcolor() {
		return parent.getBackcolor();
	}


	public Float getBackgroundAlpha() {
		return parent.getBackgroundAlpha();
	}


	public Float getForegroundAlpha() {
		return parent.getForegroundAlpha();
	}

	public PlotOrientation getOrientation()
	{
		return parent.getOrientation();
	}
}

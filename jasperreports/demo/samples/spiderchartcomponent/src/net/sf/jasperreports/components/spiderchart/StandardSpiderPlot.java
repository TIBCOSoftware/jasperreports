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

import net.sf.jasperreports.components.spiderchart.type.SpiderRotationEnum;
import net.sf.jasperreports.components.spiderchart.type.TableOrderEnum;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

import org.jfree.chart.plot.PlotOrientation;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class StandardSpiderPlot implements SpiderPlot
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_LABEL_FONT = "labelFont";
	
	public static final String PROPERTY_MAX_VALUE_EXPRESSION = "maxValueExpression";
	
	public static final String PROPERTY_ROTATION = "rotation";
	
	public static final String PROPERTY_TABLE_ORDER = "tableOrder";
	
	public static final String PROPERTY_WEB_FILLED = "webFilled";
	
	public static final String PROPERTY_START_ANGLE = "startAngle";
	
	public static final String PROPERTY_HEAD_PERCENT = "headPercent";
	
	public static final String PROPERTY_INTERIOR_GAP = "interiorGap";
	
	public static final String PROPERTY_AXIS_LINE_COLOR = "axisLineColor";
	
	public static final String PROPERTY_AXIS_LINE_WIDTH = "axisLineWidth";
	
	public static final String PROPERTY_LABEL_GAP = "labelGap";
	
	public static final String PROPERTY_LABEL_COLOR = "labelColor";

	
	protected JRFont labelFont;
	protected JRExpression maxValueExpression;
	protected SpiderRotationEnum rotation;
	protected TableOrderEnum tableOrder;
	protected Boolean webFilled;
	protected Double startAngle;
	protected Double headPercent;
	protected Double interiorGap;
	protected Color axisLineColor;
	protected Float axisLineWidth;
	protected Double labelGap;
	protected Color labelColor;

	protected Color backcolor;
	protected PlotOrientation orientation;
	protected Float backgroundAlpha;
	protected Float foregroundAlpha;

	/**
	 *
	 */
	public StandardSpiderPlot()
	{
	}

	/**
	 *
	 */
	public StandardSpiderPlot(SpiderPlot spiderPlot, JRBaseObjectFactory factory)
	{
		labelFont = spiderPlot.getLabelFont();
		rotation = spiderPlot.getRotation();
		tableOrder = spiderPlot.getTableOrder();
		webFilled = spiderPlot.getWebFilled();
		startAngle = spiderPlot.getStartAngle();
		headPercent = spiderPlot.getHeadPercent();
		interiorGap = spiderPlot.getInteriorGap();
		axisLineColor = spiderPlot.getAxisLineColor();
		axisLineWidth = spiderPlot.getAxisLineWidth();
		labelGap = spiderPlot.getLabelGap();
		labelColor = spiderPlot.getLabelColor();
		backcolor = spiderPlot.getBackcolor();
		orientation = spiderPlot.getOrientation();
		backgroundAlpha = spiderPlot.getBackgroundAlpha();
		foregroundAlpha = spiderPlot.getForegroundAlpha();

		maxValueExpression = factory.getExpression( spiderPlot.getMaxValueExpression() );
	}

	/**
	 * @return the labelFont
	 */
	public JRFont getLabelFont() {
		return labelFont;
	}

	/**
	 * @return the maxValueExpression
	 */
	public JRExpression getMaxValueExpression() {
		return maxValueExpression;
	}

	/**
	 * @return the rotation
	 */
	public SpiderRotationEnum getRotation() {
		return rotation;
	}

	/**
	 * @return the tableOrder
	 */
	public TableOrderEnum getTableOrder() {
		return tableOrder;
	}

	/**
	 * @return the webFilled
	 */
	public Boolean getWebFilled() {
		return webFilled;
	}

	/**
	 * @return the startAngle
	 */
	public Double getStartAngle() {
		return startAngle;
	}

	/**
	 * @return the headPercent
	 */
	public Double getHeadPercent() {
		return headPercent;
	}

	/**
	 * @return the interiorGap
	 */
	public Double getInteriorGap() {
		return interiorGap;
	}

	/**
	 * @return the axisLineColor
	 */
	public Color getAxisLineColor() {
		return axisLineColor;
	}

	/**
	 * @return the axisLineWidth
	 */
	public Float getAxisLineWidth() {
		return axisLineWidth;
	}

	/**
	 * @return the labelGap
	 */
	public Double getLabelGap() {
		return labelGap;
	}

	/**
	 * @return the labelColor
	 */
	public Color getLabelColor() {
		return labelColor;
	}

	/**
	 *
	 */
	public void setLabelFont(JRFont labelFont)
	{
		this.labelFont = labelFont;
	}

	/**
	 *
	 */
	public void setMaxValueExpression(JRExpression maxValueExpression)
	{
		this.maxValueExpression = maxValueExpression;
	}

	/**
	 *
	 */
	public void setRotation(SpiderRotationEnum rotation)
	{
		this.rotation = rotation;
	}

	/**
	 *
	 */
	public void setTableOrder(TableOrderEnum tableOrder)
	{
		this.tableOrder = tableOrder;
	}

	/**
	 *
	 */
	public void setWebFilled(Boolean webFilled)
	{
		this.webFilled = webFilled;
	}


	/**
	 *
	 */
	public void setStartAngle(Double startAngle)
	{
		this.startAngle = startAngle;
	}


	/**
	 *
	 */
	public void setHeadPercent(Double headPercent)
	{
		this.headPercent = headPercent;
	}


	/**
	 *
	 */
	public void setInteriorGap(Double interiorGap)
	{
		this.interiorGap = interiorGap;
	}


	/**
	 *
	 */
	public void setAxisLineColor(Color axisLineColor)
	{
		this.axisLineColor = axisLineColor;
	}

	
	/**
	 *
	 */
	public void setAxisLineWidth(Float axisLineWidth)
	{
		this.axisLineWidth = axisLineWidth;
	}

	/**
	 *
	 */
	public void setLabelGap(Double labelGap)
	{
		this.labelGap = labelGap;
	}


	/**
	 *
	 */
	public void setLabelColor(Color labelColor)
	{
		this.labelColor = labelColor;
	}
	
	/**
	 * @return the backcolor
	 */
	public Color getBackcolor() {
		return backcolor;
	}

	/**
	 * @param backcolor the backcolor to set
	 */
	public void setBackcolor(Color backcolor) {
		this.backcolor = backcolor;
	}

	/**
	 * @return the orientation
	 */
	public PlotOrientation getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(PlotOrientation orientation) {
		this.orientation = orientation;
	}

	/**
	 * @return the backgroundAlpha
	 */
	public Float getBackgroundAlpha() {
		return backgroundAlpha;
	}

	/**
	 * @param backgroundAlpha the backgroundAlpha to set
	 */
	public void setBackgroundAlpha(Float backgroundAlpha) {
		this.backgroundAlpha = backgroundAlpha;
	}

	/**
	 * @return the foregroundAlpha
	 */
	public Float getForegroundAlpha() {
		return foregroundAlpha;
	}

	/**
	 * @param foregroundAlpha the foregroundAlpha to set
	 */
	public void setForegroundAlpha(Float foregroundAlpha) {
		this.foregroundAlpha = foregroundAlpha;
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		SpiderChartCompiler.collectExpressions(this, collector);
	}

	
	
}

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
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.xml.sax.Attributes;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: SpiderPlotXmlFactory.java 3889 2010-07-16 10:52:00Z shertage $
 */
public class SpiderPlotXmlFactory extends JRBaseFactory
{
	public Object createObject(Attributes atts)
	{
		StandardSpiderPlot plot = new StandardSpiderPlot();

		SpiderRotationEnum rotation = SpiderRotationEnum.getByName(atts.getValue(SpiderChartXmlWriter.ATTRIBUTE_rotation));
		if (rotation != null)
		{
			plot.setRotation(rotation);
		}
		
		TableOrderEnum tableOrder = TableOrderEnum.getByName(atts.getValue(SpiderChartXmlWriter.ATTRIBUTE_tableOrder));
		if (rotation != null)
		{
			plot.setTableOrder(tableOrder);
		}
		
		String webFilled = atts.getValue(SpiderChartXmlWriter.ATTRIBUTE_webFilled);
		if (webFilled != null && webFilled.length() > 0) {
			plot.setWebFilled(Boolean.valueOf(webFilled));
		}
		
		String startAngle = atts.getValue(SpiderChartXmlWriter.ATTRIBUTE_startAngle);
		if(startAngle != null && startAngle.length() > 0){
			plot.setStartAngle(Double.valueOf(startAngle));
		}

		String headPercent = atts.getValue(SpiderChartXmlWriter.ATTRIBUTE_headPercent);
		if(headPercent != null && headPercent.length() > 0){
			plot.setHeadPercent(Double.valueOf(headPercent));
		}

		String interiorGap = atts.getValue(SpiderChartXmlWriter.ATTRIBUTE_interiorGap);
		if(interiorGap != null && interiorGap.length() > 0){
			plot.setInteriorGap(Double.valueOf(interiorGap));
		}

		String axisLineColor = atts.getValue(SpiderChartXmlWriter.ATTRIBUTE_axisLineColor);
		plot.setAxisLineColor(JRColorUtil.getColor(axisLineColor, null));

		String axisLineWidth = atts.getValue(SpiderChartXmlWriter.ATTRIBUTE_axisLineWidth);
		if(axisLineWidth != null && axisLineWidth.length() > 0){
			plot.setAxisLineWidth(Float.valueOf(axisLineWidth));
		}

		String labelGap = atts.getValue(SpiderChartXmlWriter.ATTRIBUTE_labelGap);
		if(labelGap != null && labelGap.length() > 0){
			plot.setLabelGap(Double.valueOf(labelGap));
		}

		String labelColor = atts.getValue(SpiderChartXmlWriter.ATTRIBUTE_labelColor);
		plot.setLabelColor(JRColorUtil.getColor(labelColor, null));

		Color color = JRColorUtil.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_backcolor), Color.black);
		if (color != null)
		{
			plot.setBackcolor(color);
		}

		String foregroundAlpha = atts.getValue(JRXmlConstants.ATTRIBUTE_foregroundAlpha);
		if (foregroundAlpha != null && foregroundAlpha.length() > 0)
		{
			plot.setForegroundAlpha(Float.valueOf(foregroundAlpha));
		}
		
		String backgroundAlpha = atts.getValue(JRXmlConstants.ATTRIBUTE_backgroundAlpha);
		if (backgroundAlpha != null && backgroundAlpha.length() > 0)
		{
			plot.setBackgroundAlpha(Float.valueOf(backgroundAlpha));
		}

		return plot;
	}
}

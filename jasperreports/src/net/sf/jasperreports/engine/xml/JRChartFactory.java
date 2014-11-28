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
package net.sf.jasperreports.engine.xml;

import java.awt.Color;

import net.sf.jasperreports.charts.JRCategoryAxisFormat;
import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.charts.util.JRAxisFormat;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;

import org.xml.sax.Attributes;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 */
public class JRChartFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRXmlLoader xmlLoader = (JRXmlLoader)digester.peek(digester.getCount() - 1);

		JRDesignChart chart = (JRDesignChart) digester.peek();

		String isShowLegend = atts.getValue(JRXmlConstants.ATTRIBUTE_isShowLegend);
		if (isShowLegend != null && isShowLegend.length() > 0)
		{
			chart.setShowLegend(Boolean.valueOf(isShowLegend));
		}
		EvaluationTimeEnum evaluationTime = EvaluationTimeEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_evaluationTime));
		if (evaluationTime != null)
		{
			chart.setEvaluationTime(evaluationTime);
		}
		if (chart.getEvaluationTimeValue() == EvaluationTimeEnum.GROUP)
		{
			xmlLoader.addGroupEvaluatedChart(chart);

			String groupName = atts.getValue(JRXmlConstants.ATTRIBUTE_evaluationGroup);
			if (groupName != null)
			{
				JRDesignGroup group = new JRDesignGroup();
				group.setName(groupName);
				chart.setEvaluationGroup(group);
			}
		}

		chart.setLinkType(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkType));
		chart.setLinkTarget(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkTarget));

		String bookmarkLevelAttr = atts.getValue(JRXmlConstants.ATTRIBUTE_bookmarkLevel);
		if (bookmarkLevelAttr != null)
		{
			chart.setBookmarkLevel(Integer.parseInt(bookmarkLevelAttr));
		}

		String chartCustomizerClass = atts.getValue( JRXmlConstants.ATTRIBUTE_customizerClass );
		if( chartCustomizerClass != null && chartCustomizerClass.length() > 0 )
		{
			chart.setCustomizerClass(chartCustomizerClass);
		}
		
		chart.setRenderType(atts.getValue(JRXmlConstants.ATTRIBUTE_renderType));
		chart.setTheme(atts.getValue(JRXmlConstants.ATTRIBUTE_theme));
		
		return chart;
	}


	/**
	 *
	 */
	public static class JRChartTitleFactory extends JRBaseFactory
	{
		public Object createObject(Attributes atts)
		{
			JRDesignChart chart = (JRDesignChart) digester.peek();

			EdgeEnum position = EdgeEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_position));
			if (position != null)
			{
				chart.setTitlePosition(position);
			}
			
			Color color = JRColorUtil.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_color), Color.black);
			if (color != null)
			{
				chart.setTitleColor(color);
			}

			return chart;
		}
	}


	/**
	 *
	 */
	public static class JRChartSubtitleFactory extends JRBaseFactory
	{
		public Object createObject(Attributes atts)
		{
			JRDesignChart chart = (JRDesignChart) digester.peek();

			Color color = JRColorUtil.getColor(atts.getValue(JRXmlConstants.ATTRIBUTE_color), Color.black);
			if (color != null)
			{
				chart.setSubtitleColor(color);
			}

			return chart;
		}
	}


	/**
	 * A factory responsible for creating new chart legend formatting objects.
	 *
	 * @author Barry Klawans (bklawans@users.sourceforge.net)
	 */
	public static class JRChartLegendFactory extends JRBaseFactory
	{
		/**
		 *
		 */
		public Object createObject(Attributes atts) throws JRException
		{
			// Grab the chart from the object stack.
			JRDesignChart chart = (JRDesignChart)digester.peek();

			// Set the text color
			String attrValue = atts.getValue(JRXmlConstants.ATTRIBUTE_textColor);
			if (attrValue != null && attrValue.length() > 0)
			{
				Color color = JRColorUtil.getColor(attrValue, null);
				chart.setLegendColor(color);
			}

			// Set the background color
			attrValue = atts.getValue(JRXmlConstants.ATTRIBUTE_backgroundColor);
			if (attrValue != null && attrValue.length() > 0)
			{
				Color color = JRColorUtil.getColor(attrValue, null);
				chart.setLegendBackgroundColor(color);
			}

			EdgeEnum position = EdgeEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_position));
			if (position != null)
			{
				chart.setLegendPosition(position);
			}
			// Any font set will be put in the chart directly by the digester

			return chart;
		}
	}


	/**
	 * A factory responsible for creating new chart axis formatting objects.
	 *
	 * @author Barry Klawans (bklawans@users.sourceforge.net)
	 */
	public static class JRChartAxisFormatFactory extends JRBaseFactory
	{
		/**
		 *
		 */
		public Object createObject(Attributes atts) throws JRException
		{
			// Create an empty axis formatting object
			JRAxisFormat axisLabel = new JRAxisFormat();

			// Set the label color
			String attrValue = atts.getValue(JRXmlConstants.ATTRIBUTE_labelColor);
			if (attrValue != null && attrValue.length() > 0)
			{
				Color color = JRColorUtil.getColor(attrValue, null);
				axisLabel.setLabelColor(color);
			}

			// Set the tick label color
			attrValue = atts.getValue(JRXmlConstants.ATTRIBUTE_tickLabelColor);
			if (attrValue != null && attrValue.length() > 0)
			{
				Color color = JRColorUtil.getColor(attrValue, null);
				axisLabel.setTickLabelColor(color);
			}

			// Set the tick mask
			attrValue = atts.getValue(JRXmlConstants.ATTRIBUTE_tickLabelMask);
			if (attrValue != null && attrValue.length() > 0)
			{
				axisLabel.setTickLabelMask(attrValue);
			}

			// Set the vertical tick labels flag
			attrValue = atts.getValue(JRXmlConstants.ATTRIBUTE_verticalTickLabels);
			if (attrValue != null && attrValue.length() > 0)
			{
				axisLabel.setVerticalTickLabel(Boolean.valueOf(attrValue));
			}

			// And finally set the axis line color
			attrValue = atts.getValue(JRXmlConstants.ATTRIBUTE_axisLineColor);
			if (attrValue != null && attrValue.length() > 0)
			{
				Color color = JRColorUtil.getColor(attrValue, null);
				axisLabel.setLineColor(color);
			}

			// Any fonts set will be put in the axis format object by the digester.

			return axisLabel;
		}
	}


	/**
	 * 
	 */
	public static class JRCategoryAxisFormatFactory extends JRBaseFactory
	{
		/**
		 *
		 */
		public Object createObject(Attributes atts) throws JRException
		{
			JRCategoryAxisFormat categoryAxisFormat = (JRCategoryAxisFormat)digester.peek();
			
			String labelRotation = atts.getValue(JRXmlConstants.ATTRIBUTE_labelRotation);
			if (labelRotation != null && labelRotation.length() > 0)
			{
				categoryAxisFormat.setCategoryAxisTickLabelRotation(Double.valueOf(labelRotation));
			}
			return categoryAxisFormat;
		}
	}
}

/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.xml;

import java.awt.Color;
import java.util.Collection;

import net.sf.jasperreports.charts.util.JRAxisFormat;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignGroup;

import org.xml.sax.Attributes;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRChartFactory extends JRBaseFactory
{

	private static final String ATTRIBUTE_isShowLegend = "isShowLegend";
	private static final String ATTRIBUTE_evaluationTime = "evaluationTime";
	private static final String ATTRIBUTE_evaluationGroup = "evaluationGroup";
	private static final String ATTRIBUTE_hyperlinkType = "hyperlinkType";
	private static final String ATTRIBUTE_hyperlinkTarget = "hyperlinkTarget";
	private static final String ATTRIBUTE_bookmarkLevel = "bookmarkLevel";
	private static final String ATTRIBUTE_customizerClass = "customizerClass";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRXmlLoader xmlLoader = (JRXmlLoader)digester.peek(digester.getCount() - 1);
		Collection groupEvaluatedCharts = xmlLoader.getGroupEvaluatedCharts();

		JRDesignChart chart = (JRDesignChart) digester.peek();

		String isShowLegend = atts.getValue(ATTRIBUTE_isShowLegend);
		if (isShowLegend != null && isShowLegend.length() > 0)
			chart.setShowLegend(Boolean.valueOf(isShowLegend).booleanValue());

		Byte evaluationTime = (Byte)JRXmlConstants.getEvaluationTimeMap().get(atts.getValue(ATTRIBUTE_evaluationTime));
		if (evaluationTime != null)
		{
			chart.setEvaluationTime(evaluationTime.byteValue());
		}
		if (chart.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
		{
			groupEvaluatedCharts.add(chart);

			String groupName = atts.getValue(ATTRIBUTE_evaluationGroup);
			if (groupName != null)
			{
				JRDesignGroup group = new JRDesignGroup();
				group.setName(groupName);
				chart.setEvaluationGroup(group);
			}
		}

		String hyperlinkType = atts.getValue(ATTRIBUTE_hyperlinkType);
		if (hyperlinkType != null)
		{
			chart.setLinkType(hyperlinkType);
		}

		Byte hyperlinkTarget = (Byte)JRXmlConstants.getHyperlinkTargetMap().get(atts.getValue(ATTRIBUTE_hyperlinkTarget));
		if (hyperlinkTarget != null)
		{
			chart.setHyperlinkTarget(hyperlinkTarget.byteValue());
		}
		
		String bookmarkLevelAttr = atts.getValue(ATTRIBUTE_bookmarkLevel);
		if (bookmarkLevelAttr != null)
		{
			chart.setBookmarkLevel(Integer.parseInt(bookmarkLevelAttr));
		}		

		String chartCustomizerClass = atts.getValue( ATTRIBUTE_customizerClass );
		if( chartCustomizerClass != null && chartCustomizerClass.length() > 0 ){
			chart.setCustomizerClass(chartCustomizerClass);
		}

		return chart;
	}


	/**                                        	
	 *
	 */
	public static class JRChartTitleFactory extends JRBaseFactory
	{
		private static final String ATTRIBUTE_position = "position";
		private static final String ATTRIBUTE_color = "color";


		public Object createObject(Attributes atts)
		{
			JRDesignChart chart = (JRDesignChart) digester.peek();

			String position = atts.getValue(ATTRIBUTE_position);
			if (position != null && position.length() > 0)
				chart.setTitlePosition(((Byte)JRXmlConstants.getChartTitlePositionMap().get(position)).byteValue());


			Color color = JRXmlConstants.getColor(atts.getValue(ATTRIBUTE_color), Color.black);
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
		private static final String ATTRIBUTE_color = "color";


		public Object createObject(Attributes atts)
		{
			JRDesignChart chart = (JRDesignChart) digester.peek();

			Color color = JRXmlConstants.getColor(atts.getValue(ATTRIBUTE_color), Color.black);
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
         * Attribute used to specify the font color in the legend.
         */		
		public static final String ATTRIBUTE_textColor = "textColor";
		
		/**
		 * Attribute used to specify the background color of the legend.
		 */
		public static final String ATTRIBUTE_backgroundColor = "backgroundColor";
	    
	    /**
	     *
	     */
	    public Object createObject(Attributes atts) throws JRException
	    {
	    	// Grab the chart from the object stack.
	        JRDesignChart chart = (JRDesignChart)digester.peek();
	        
	        // Set the text color
	        String attrValue = atts.getValue(ATTRIBUTE_textColor);
	        if (attrValue != null && attrValue.length() > 0)
	        {
	        	Color color = JRXmlConstants.getColor(attrValue, null);
	        	chart.setLegendColor(color);
	        }

	        // Set the background color
	        attrValue = atts.getValue(ATTRIBUTE_backgroundColor);
	        if (attrValue != null && attrValue.length() > 0)
	        {
		        Color color = JRXmlConstants.getColor(attrValue, null);
	            chart.setLegendBackgroundColor(color);
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
		 * Attribute to use to specify the color of the axis label.
		 */
		public static final String ATTRIBUTE_labelColor = "labelColor";
		
		/**
		 * Attribute to use to specify the color of each tick mark label.
		 */
		public static final String ATTRIBUTE_tickLabelColor = "tickLabelColor";
		
		/**
		 * Attribute to use to specify a formatting mask for each tick mark label.
		 */
		public static final String ATTRIBUTE_tickLabelMask = "tickLabelMask";
		
		/**
		 * Attribute to use to specify the color of the axis line and any tick marks.
		 */
		public static final String ATTRIBUTE_axisLineColor = "axisLineColor";
	    
	    /**
	     *
	     */
	    public Object createObject(Attributes atts) throws JRException
	    {
	    	// Create an empty axis formatting object
	    	JRAxisFormat axisLabel = new JRAxisFormat();
	        
	    	// Set the label color
	        String attrValue = atts.getValue(ATTRIBUTE_labelColor);
	        if (attrValue != null && attrValue.length() > 0)
	        {
	        	Color color = JRXmlConstants.getColor(attrValue, null);
	        	axisLabel.setLabelColor(color);
	        }
	        
	        // Set the tick label color
	        attrValue = atts.getValue(ATTRIBUTE_tickLabelColor);
	        if (attrValue != null && attrValue.length() > 0)
	        {
	        	Color color = JRXmlConstants.getColor(attrValue, null);
	        	axisLabel.setTickLabelColor(color);
	        }
	        
	        // Set the tick mask
	        attrValue = atts.getValue(ATTRIBUTE_tickLabelMask);
	        if (attrValue != null && attrValue.length() > 0)
	        {
	        	axisLabel.setTickLabelMask(attrValue);
	        }
	        
	        // And finally set the axis line color
	        attrValue = atts.getValue(ATTRIBUTE_axisLineColor);
	        if (attrValue != null && attrValue.length() > 0)
	        {
	        	Color color = JRXmlConstants.getColor(attrValue, null);
	        	axisLabel.setLineColor(color);
	        }

	        // Any fonts set will be put in the axis format object by the digester.
	        
	        return axisLabel;
	    }
	}
}

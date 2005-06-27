/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.design;

import java.awt.Color;

import net.sf.jasperreports.charts.design.JRDesignAreaPlot;
import net.sf.jasperreports.charts.design.JRDesignBar3DPlot;
import net.sf.jasperreports.charts.design.JRDesignBarPlot;
import net.sf.jasperreports.charts.design.JRDesignBubblePlot;
import net.sf.jasperreports.charts.design.JRDesignCandlestickPlot;
import net.sf.jasperreports.charts.design.JRDesignCategoryDataset;
import net.sf.jasperreports.charts.design.JRDesignHighLowDataset;
import net.sf.jasperreports.charts.design.JRDesignHighLowPlot;
import net.sf.jasperreports.charts.design.JRDesignIntervalXyDataset;
import net.sf.jasperreports.charts.design.JRDesignLinePlot;
import net.sf.jasperreports.charts.design.JRDesignPie3DPlot;
import net.sf.jasperreports.charts.design.JRDesignPieDataset;
import net.sf.jasperreports.charts.design.JRDesignPiePlot;
import net.sf.jasperreports.charts.design.JRDesignScatterPlot;
import net.sf.jasperreports.charts.design.JRDesignXyDataset;
import net.sf.jasperreports.charts.design.JRDesignXyzDataset;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignChart extends JRDesignElement implements JRChart
{


	/**
	 *
	 */
	private static final long serialVersionUID = 608;

	/**
	 *
	 */
	protected byte chartType = 0;

	/**
	 *
	 */
	protected boolean isShowLegend = false;
	protected byte evaluationTime = JRExpression.EVALUATION_TIME_NOW;
	protected byte hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NONE;
	protected byte hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;
	protected byte titlePosition = JRChart.TITLE_POSITION_TOP;
	protected Color titleColor = Color.black;
	protected Color subtitleColor = Color.black;

	/**
	 *
	 */
	protected JRBox box = null;
	protected JRFont titleFont = null;
	protected JRFont subtitleFont = null;

	/**
	 *
	 */
	protected JRGroup evaluationGroup = null;
	protected JRExpression titleExpression = null;
	protected JRExpression subtitleExpression = null;
	protected JRExpression anchorNameExpression = null;
	protected JRExpression hyperlinkReferenceExpression = null;
	protected JRExpression hyperlinkAnchorExpression = null;
	protected JRExpression hyperlinkPageExpression = null;

	protected JRChartDataset dataset = null;
	protected JRChartPlot plot = null;


	/**
	 *
	 */
	public JRDesignChart(byte chartType)
	{
		setChartType(chartType);
	}


	/**
	 *
	 */
	public boolean isShowLegend()
	{
		return isShowLegend;
	}

	/**
	 *
	 */
	public void setShowLegend(boolean isShowLegend)
	{
		this.isShowLegend = isShowLegend;
	}

	/**
	 *
	 */
	public byte getEvaluationTime()
	{
		return evaluationTime;
	}
		
	/**
	 *
	 */
	public void setEvaluationTime(byte evaluationTime)
	{
		this.evaluationTime = evaluationTime;
	}
		
	/**
	 *
	 */
	public JRGroup getEvaluationGroup()
	{
		return evaluationGroup;
	}
		
	/**
	 *
	 */
	public void setEvaluationGroup(JRGroup group)
	{
		this.evaluationGroup = group;
	}
		
	/**
	 *
	 */
	public JRBox getBox()
	{
		return box;
	}

	/**
	 *
	 */
	public JRFont getTitleFont()
	{
		return titleFont;
	}

	/**
	 *
	 */
	public void setTitleFont(JRFont font)
	{
		this.titleFont = font;
	}
	
	/**
	 *
	 */
	public byte getTitlePosition()
	{
		return titlePosition;
	}

	/**
	 *
	 */
	public void setTitlePosition(byte titlePosition)
	{
		this.titlePosition = titlePosition;
	}

	/**
	 *
	 */
	public Color getTitleColor()
	{
		return titleColor;
	}

	/**
	 *
	 */
	public void setTitleColor(Color titleColor)
	{
		this.titleColor = titleColor;
	}

	/**
	 *
	 */
	public JRFont getSubtitleFont()
	{
		return subtitleFont;
	}

	/**
	 *
	 */
	public void setSubtitleFont(JRFont font)
	{
		this.subtitleFont = font;
	}
	
	/**
	 *
	 */
	public Color getSubtitleColor()
	{
		return subtitleColor;
	}

	/**
	 *
	 */
	public void setSubtitleColor(Color subtitleColor)
	{
		this.subtitleColor = subtitleColor;
	}

	/**
	 *
	 */
	public byte getHyperlinkType()
	{
		return hyperlinkType;
	}
		
	/**
	 *
	 */
	public void setHyperlinkType(byte hyperlinkType)
	{
		this.hyperlinkType = hyperlinkType;
	}
		
	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return hyperlinkTarget;
	}
		
	/**
	 *
	 */
	public void setHyperlinkTarget(byte hyperlinkTarget)
	{
		this.hyperlinkTarget = hyperlinkTarget;
	}
		
	/**
	 *
	 */
	public JRExpression getTitleExpression()
	{
		return titleExpression;
	}

	/**
	 *
	 */
	public void setTitleExpression(JRExpression expression)
	{
		this.titleExpression = expression;
	}

	/**
	 *
	 */
	public JRExpression getSubtitleExpression()
	{
		return subtitleExpression;
	}

	/**
	 *
	 */
	public void setSubtitleExpression(JRExpression expression)
	{
		this.subtitleExpression = expression;
	}

	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return anchorNameExpression;
	}

	/**
	 *
	 */
	public void setAnchorNameExpression(JRExpression anchorNameExpression)
	{
		this.anchorNameExpression = anchorNameExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return hyperlinkReferenceExpression;
	}

	/**
	 *
	 */
	public void setHyperlinkReferenceExpression(JRExpression hyperlinkReferenceExpression)
	{
		this.hyperlinkReferenceExpression = hyperlinkReferenceExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return hyperlinkAnchorExpression;
	}

	/**
	 *
	 */
	public void setHyperlinkAnchorExpression(JRExpression hyperlinkAnchorExpression)
	{
		this.hyperlinkAnchorExpression = hyperlinkAnchorExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkPageExpression()
	{
		return hyperlinkPageExpression;
	}
	
	/**
	 *
	 */
	public void setHyperlinkPageExpression(JRExpression hyperlinkPageExpression)
	{
		this.hyperlinkPageExpression = hyperlinkPageExpression;
	}

	/**
	 *
	 */
	public JRChartDataset getDataset()
	{
		return dataset;
	}

	/**
	 *
	 */
	public JRChartPlot getPlot()
	{
		return plot;
	}


	public byte getChartType()
	{
		return chartType;
	}


	/**
	 *
	 */
	public void setChartType(byte chartType)
	{
		this.chartType = chartType;

		switch(chartType) {
			case CHART_TYPE_AREA:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignAreaPlot(plot);
			    break;
			case CHART_TYPE_BAR:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignBarPlot(plot);
			    break;
			case CHART_TYPE_BAR3D:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignBar3DPlot(plot);
			    break;
			case CHART_TYPE_BUBBLE:
				dataset = new JRDesignXyzDataset(dataset);
				plot = new JRDesignBubblePlot(plot);
			    break;
			case CHART_TYPE_CANDLESTICK:
				dataset = new JRDesignHighLowDataset(dataset);
				plot = new JRDesignCandlestickPlot(plot);
			    break;
			case CHART_TYPE_HIGHLOW:
				dataset = new JRDesignHighLowDataset(dataset);
				plot = new JRDesignHighLowPlot(plot);
			    break;
			case CHART_TYPE_LINE:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignLinePlot(plot);
			    break;
			case CHART_TYPE_PIE:
				dataset = new JRDesignPieDataset(dataset);
				plot = new JRDesignPiePlot(plot);
				break;
			case CHART_TYPE_PIE3D:
				dataset = new JRDesignPieDataset(dataset);
				plot = new JRDesignPie3DPlot(plot);
				break;
			case CHART_TYPE_SCATTER:
				dataset = new JRDesignXyDataset(dataset);
				plot = new JRDesignScatterPlot(plot);
				break;
			case CHART_TYPE_STACKEDBAR:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignBarPlot(plot);
				break;
			case CHART_TYPE_STACKEDBAR3D:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignBar3DPlot(plot);
				break;
			case CHART_TYPE_TIMESERIES:
				// TODO after time series charts are completed
				break;
			case CHART_TYPE_XYAREA:
				dataset = new JRDesignXyDataset(dataset);
				plot = new JRDesignAreaPlot(plot);
				break;
			case CHART_TYPE_XYBAR:
				dataset = new JRDesignIntervalXyDataset(dataset);
				plot = new JRDesignBarPlot(plot);
				break;
			case CHART_TYPE_XYLINE:
				dataset = new JRDesignXyDataset(dataset);
				plot = new JRDesignLinePlot(plot);
				break;
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}
	}


	public JRElement getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getChart( this );
	}


	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	public void writeXml(JRXmlWriter xmlWriter)
	{
		switch(chartType) {
			case CHART_TYPE_AREA:
				xmlWriter.writeAreaChart(this);
			    break;
			case CHART_TYPE_BAR:
				xmlWriter.writeBarChart(this);
			    break;
			case CHART_TYPE_BAR3D:
				xmlWriter.writeBar3DChart(this);
			    break;
			case CHART_TYPE_BUBBLE:
				xmlWriter.writeBubbleChart(this);
			    break;
			case CHART_TYPE_CANDLESTICK:
				xmlWriter.writeCandlestickChart(this);
			    break;
			case CHART_TYPE_HIGHLOW:
				xmlWriter.writeHighLowChart(this);
			    break;
			case CHART_TYPE_LINE:
				xmlWriter.writeLineChart(this);
			    break;
			case CHART_TYPE_PIE:
				xmlWriter.writePieChart(this);
				break;
			case CHART_TYPE_PIE3D:
				xmlWriter.writePie3DChart(this);
				break;
			case CHART_TYPE_SCATTER:
				xmlWriter.writeScatterChart(this);
				break;
			case CHART_TYPE_STACKEDBAR:
				xmlWriter.writeStackedBarChart(this);
				break;
			case CHART_TYPE_STACKEDBAR3D:
				xmlWriter.writeStackedBar3DChart(this);
				break;
			case CHART_TYPE_TIMESERIES:
				// TODO after time series charts are completed
				break;
			case CHART_TYPE_XYAREA:
				xmlWriter.writeXyAreaChart(this);
				break;
			case CHART_TYPE_XYBAR:
				xmlWriter.writeXyBarChart(this);
				break;
			case CHART_TYPE_XYLINE:
				xmlWriter.writeXyLineChart(this);
				break;
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}
	}
}

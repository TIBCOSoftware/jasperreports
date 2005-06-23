/*
 * ============================================================================
 *                   GNU Lesser General Public License
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
package net.sf.jasperreports.engine.base;

import net.sf.jasperreports.charts.JRAreaChart;
import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DChart;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarChart;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRCandlestickChart;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRHighLowChart;
import net.sf.jasperreports.charts.JRIntervalXyDataset;
import net.sf.jasperreports.charts.JRLineChart;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRPie3DChart;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieChart;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRScatterChart;
import net.sf.jasperreports.charts.JRStackedBar3DChart;
import net.sf.jasperreports.charts.JRStackedBarChart;
import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRXyAreaChart;
import net.sf.jasperreports.charts.JRXyBarChart;
import net.sf.jasperreports.charts.JRXyLineChart;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.charts.base.JRBaseAreaChart;
import net.sf.jasperreports.charts.base.JRBaseAreaPlot;
import net.sf.jasperreports.charts.base.JRBaseBar3DChart;
import net.sf.jasperreports.charts.base.JRBaseBar3DPlot;
import net.sf.jasperreports.charts.base.JRBaseBarChart;
import net.sf.jasperreports.charts.base.JRBaseBarPlot;
import net.sf.jasperreports.charts.base.JRBaseCandlestickChart;
import net.sf.jasperreports.charts.base.JRBaseCandlestickPlot;
import net.sf.jasperreports.charts.base.JRBaseCategoryDataset;
import net.sf.jasperreports.charts.base.JRBaseCategorySeries;
import net.sf.jasperreports.charts.base.JRBaseHighLowChart;
import net.sf.jasperreports.charts.base.JRBaseIntervalXyDataset;
import net.sf.jasperreports.charts.base.JRBaseLineChart;
import net.sf.jasperreports.charts.base.JRBaseLinePlot;
import net.sf.jasperreports.charts.base.JRBasePie3DChart;
import net.sf.jasperreports.charts.base.JRBasePie3DPlot;
import net.sf.jasperreports.charts.base.JRBasePieChart;
import net.sf.jasperreports.charts.base.JRBasePieDataset;
import net.sf.jasperreports.charts.base.JRBasePiePlot;
import net.sf.jasperreports.charts.base.JRBaseScatterChart;
import net.sf.jasperreports.charts.base.JRBaseStackedBar3DChart;
import net.sf.jasperreports.charts.base.JRBaseStackedBarChart;
import net.sf.jasperreports.charts.base.JRBaseTimeSeries;
import net.sf.jasperreports.charts.base.JRBaseXyAreaChart;
import net.sf.jasperreports.charts.base.JRBaseXyBarChart;
import net.sf.jasperreports.charts.base.JRBaseXyLineChart;
import net.sf.jasperreports.charts.base.JRBaseXySeries;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseObjectFactory extends JRAbstractObjectFactory
{

	
	/**
	 *
	 */
	private JRBaseReport report = null;


	/**
	 *
	 */
	protected JRBaseObjectFactory(JRBaseReport baseReport)
	{
		this.report = baseReport;
	}


	/**
	 *
	 */
	public JRReportFont getReportFont(JRReportFont font)
	{
		JRBaseReportFont baseFont = null;
		
		if (font != null)
		{
			baseFont = (JRBaseReportFont)get(font);
			if (baseFont == null)
			{
				baseFont = new JRBaseReportFont(font);
				put(font, baseFont);
			}
		}
		
		return baseFont;
	}


	/**
	 *
	 */
	public JRFont getFont(JRFont font)
	{
		JRBaseFont baseFont = null;
		
		if (font != null)
		{
			baseFont = (JRBaseFont)get(font);
			if (baseFont == null)
			{
				baseFont = 
					new JRBaseFont(
						report, 
						getReportFont(font.getReportFont()), 
						font
						);
				put(font, baseFont);
			}
		}
		
		return baseFont;
	}


	/**
	 *
	 */
	protected JRBaseParameter getParameter(JRParameter parameter)
	{
		JRBaseParameter baseParameter = null;
		
		if (parameter != null)
		{
			baseParameter = (JRBaseParameter)get(parameter);
			if (baseParameter == null)
			{
				baseParameter = new JRBaseParameter(parameter, this);
			}
		}
		
		return baseParameter;
	}


	/**
	 *
	 */
	protected JRBaseQuery getQuery(JRQuery query)
	{
		JRBaseQuery baseQuery = null;
		
		if (query != null)
		{
			baseQuery = (JRBaseQuery)get(query);
			if (baseQuery == null)
			{
				baseQuery = new JRBaseQuery(query, this);
			}
		}
		
		return baseQuery;
	}


	/**
	 *
	 */
	protected JRBaseQueryChunk getQueryChunk(JRQueryChunk queryChunk)
	{
		JRBaseQueryChunk baseQueryChunk = null;
		
		if (queryChunk != null)
		{
			baseQueryChunk = (JRBaseQueryChunk)get(queryChunk);
			if (baseQueryChunk == null)
			{
				baseQueryChunk = new JRBaseQueryChunk(queryChunk, this);
			}
		}
		
		return baseQueryChunk;
	}


	/**
	 *
	 */
	protected JRBaseField getField(JRField field)
	{
		JRBaseField baseField = null;
		
		if (field != null)
		{
			baseField = (JRBaseField)get(field);
			if (baseField == null)
			{
				baseField = new JRBaseField(field, this);
			}
		}
		
		return baseField;
	}


	/**
	 *
	 */
	protected JRBaseVariable getVariable(JRVariable variable)
	{
		JRBaseVariable baseVariable = null;
		
		if (variable != null)
		{
			baseVariable = (JRBaseVariable)get(variable);
			if (baseVariable == null)
			{
				baseVariable = new JRBaseVariable(variable, this);
			}
		}
		
		return baseVariable;
	}


	/**
	 *
	 */
	public JRBaseExpression getExpression(JRExpression expression)
	{
		JRBaseExpression baseExpression = null;
		
		if (expression != null)
		{
			baseExpression = (JRBaseExpression)get(expression);
			if (baseExpression == null)
			{
				baseExpression = new JRBaseExpression(expression, this);
			}
		}
		
		return baseExpression;
	}


	/**
	 *
	 */
	protected JRBaseExpressionChunk getExpressionChunk(JRExpressionChunk expressionChunk)
	{
		JRBaseExpressionChunk baseExpressionChunk = null;
		
		if (expressionChunk != null)
		{
			baseExpressionChunk = (JRBaseExpressionChunk)get(expressionChunk);
			if (baseExpressionChunk == null)
			{
				baseExpressionChunk = new JRBaseExpressionChunk(expressionChunk, this);
			}
		}
		
		return baseExpressionChunk;
	}


	/**
	 *
	 */
	protected JRBaseGroup getGroup(JRGroup group)
	{
		JRBaseGroup baseGroup = null;
		
		if (group != null)
		{
			baseGroup = (JRBaseGroup)get(group);
			if (baseGroup == null)
			{
				baseGroup = new JRBaseGroup(group, this);
			}
		}
		
		return baseGroup;
	}


	/**
	 *
	 */
	protected JRBaseBand getBand(JRBand band)
	{
		JRBaseBand baseBand = null;
		
		if (band != null)
		{
			baseBand = (JRBaseBand)get(band);
			if (baseBand == null)
			{
				baseBand = new JRBaseBand(band, this);
			}
		}
		
		return baseBand;
	}


	/**
	 *
	 */
	protected JRBaseElementGroup getElementGroup(JRElementGroup elementGroup)
	{
		JRBaseElementGroup baseElementGroup = null;
		
		if (elementGroup != null)
		{
			baseElementGroup = (JRBaseElementGroup)get(elementGroup);
			if (baseElementGroup == null)
			{
				baseElementGroup = new JRBaseElementGroup(elementGroup, this);
			}
		}
		
		return baseElementGroup;
	}


	/**
	 *
	 */
	public JRLine getLine(JRLine line)
	{
		JRBaseLine baseLine = null;
		
		if (line != null)
		{
			baseLine = (JRBaseLine)get(line);
			if (baseLine == null)
			{
				baseLine = new JRBaseLine(line, this);
			}
		}
		
		return baseLine;
	}


	/**
	 *
	 */
	public JRRectangle getRectangle(JRRectangle rectangle)
	{
		JRBaseRectangle baseRectangle = null;
		
		if (rectangle != null)
		{
			baseRectangle = (JRBaseRectangle)get(rectangle);
			if (baseRectangle == null)
			{
				baseRectangle = new JRBaseRectangle(rectangle, this);
			}
		}
		
		return baseRectangle;
	}


	/**
	 *
	 */
	public JREllipse getEllipse(JREllipse ellipse)
	{
		JRBaseEllipse baseEllipse = null;
		
		if (ellipse != null)
		{
			baseEllipse = (JRBaseEllipse)get(ellipse);
			if (baseEllipse == null)
			{
				baseEllipse = new JRBaseEllipse(ellipse, this);
			}
		}
		
		return baseEllipse;
	}


	/**
	 *
	 */
	public JRImage getImage(JRImage image)
	{
		JRBaseImage baseImage = null;
		
		if (image != null)
		{
			baseImage = (JRBaseImage)get(image);
			if (baseImage == null)
			{
				baseImage = new JRBaseImage(image, this);
			}
		}
		
		return baseImage;
	}


	/**
	 *
	 */
	public JRStaticText getStaticText(JRStaticText staticText)
	{
		JRBaseStaticText baseStaticText = null;
		
		if (staticText != null)
		{
			baseStaticText = (JRBaseStaticText)get(staticText);
			if (baseStaticText == null)
			{
				baseStaticText = new JRBaseStaticText(staticText, this);
			}
		}
		
		return baseStaticText;
	}


	/**
	 *
	 */
	public JRTextField getTextField(JRTextField textField)
	{
		JRBaseTextField baseTextField = null;
		
		if (textField != null)
		{
			baseTextField = (JRBaseTextField)get(textField);
			if (baseTextField == null)
			{
				baseTextField = new JRBaseTextField(textField, this);
			}
		}
		
		return baseTextField;
	}


	/**
	 *
	 */
	public JRSubreport getSubreport(JRSubreport subreport)
	{
		JRBaseSubreport baseSubreport = null;
		
		if (subreport != null)
		{
			baseSubreport = (JRBaseSubreport)get(subreport);
			if (baseSubreport == null)
			{
				baseSubreport = new JRBaseSubreport(subreport, this);
			}
		}
		
		return baseSubreport;
	}


	/**
	 *
	 */
	protected JRBaseSubreportParameter getSubreportParameter(JRSubreportParameter subreportParameter)
	{
		JRBaseSubreportParameter baseSubreportParameter = null;
		
		if (subreportParameter != null)
		{
			baseSubreportParameter = (JRBaseSubreportParameter)get(subreportParameter);
			if (baseSubreportParameter == null)
			{
				baseSubreportParameter = new JRBaseSubreportParameter(subreportParameter, this);
				put(subreportParameter, baseSubreportParameter);
			}
		}
		
		return baseSubreportParameter;
	}
	

	/**
	 *
	 */
	public JRPieChart getPieChart(JRPieChart pieChart)
	{
		JRBasePieChart basePieChart = null;
		
		if (pieChart != null)
		{
			basePieChart = (JRBasePieChart)get(pieChart);
			if (basePieChart == null)
			{
				basePieChart = new JRBasePieChart(pieChart, this);
				put(pieChart, basePieChart);//FIXME NOW need this?
			}
		}
		
		return basePieChart;
	}
	

	/**
	 *
	 */
	public JRPieDataset getPieDataset(JRPieDataset pieDataset)
	{
		JRBasePieDataset basePieDataset = null;
		
		if (pieDataset != null)
		{
			basePieDataset = (JRBasePieDataset)get(pieDataset);
			if (basePieDataset == null)
			{
				basePieDataset = new JRBasePieDataset(pieDataset, this);
				put(pieDataset, basePieDataset);//FIXME NOW need this?
			}
		}
		
		return basePieDataset;
	}
	

	/**
	 *
	 */
	public JRPiePlot getPiePlot(JRPiePlot piePlot)
	{
		JRBasePiePlot basePiePlot = null;
		
		if (piePlot != null)
		{
			basePiePlot = (JRBasePiePlot)get(piePlot);
			if (basePiePlot == null)
			{
				basePiePlot = new JRBasePiePlot(piePlot, this);
				put(piePlot, basePiePlot);//FIXME NOW need this?
			}
		}
		
		return basePiePlot;
	}
	

	/**
	 *
	 */
	public JRPie3DChart getPie3DChart(JRPie3DChart pie3DChart)
	{
		JRBasePie3DChart basePie3DChart = null;
		
		if (pie3DChart != null)
		{
			basePie3DChart = (JRBasePie3DChart)get(pie3DChart);
			if (basePie3DChart == null)
			{
				basePie3DChart = new JRBasePie3DChart(pie3DChart, this);
				put(pie3DChart, basePie3DChart);//FIXME NOW need this?
			}
		}
		
		return basePie3DChart;
	}
	

	/**
	 *
	 */
	public JRPie3DPlot getPie3DPlot(JRPie3DPlot pie3DPlot)
	{
		JRBasePie3DPlot basePie3DPlot = null;
		
		if (pie3DPlot != null)
		{
			basePie3DPlot = (JRBasePie3DPlot)get(pie3DPlot);
			if (basePie3DPlot == null)
			{
				basePie3DPlot = new JRBasePie3DPlot(pie3DPlot, this);
				put(pie3DPlot, basePie3DPlot);//FIXME NOW need this?
			}
		}
		
		return basePie3DPlot;
	}
	

	/**
	 *
	 */
	public JRBarChart getBarChart(JRBarChart barChart)
	{
		JRBaseBarChart baseBarChart = null;
		
		if (barChart != null)
		{
			baseBarChart = (JRBaseBarChart)get(barChart);
			if (baseBarChart == null)
			{
				baseBarChart = new JRBaseBarChart(barChart, this);
				put(barChart, baseBarChart);//FIXME NOW need this?
			}
		}
		
		return baseBarChart;
	}
	

	/**
	 *
	 */
	public JRStackedBarChart getStackedBarChart(JRStackedBarChart stackedbarChart)
	{
		JRBaseStackedBarChart baseStackedBarChart = null;

		if (stackedbarChart != null)
		{
			baseStackedBarChart = (JRBaseStackedBarChart)get(stackedbarChart);
			if (baseStackedBarChart == null)
			{
				baseStackedBarChart = new JRBaseStackedBarChart(stackedbarChart, this);
				put(stackedbarChart, baseStackedBarChart);//FIXME NOW need this?
			}
		}

		return baseStackedBarChart;
	}


	/**
	 *
	 */
	public JRStackedBar3DChart getStackedBar3DChart(JRStackedBar3DChart stackedbar3DChart)
	{
		JRBaseStackedBar3DChart baseStackedBar3DChart = null;

		if (stackedbar3DChart != null)
		{
			baseStackedBar3DChart = (JRBaseStackedBar3DChart)get(stackedbar3DChart);
			if (baseStackedBar3DChart == null)
			{
				baseStackedBar3DChart = new JRBaseStackedBar3DChart(stackedbar3DChart, this);
				put(stackedbar3DChart, baseStackedBar3DChart);//FIXME NOW need this?
			}
		}

		return baseStackedBar3DChart;
	}


	/**
	 *
	 */
	public JRCategoryDataset getCategoryDataset(JRCategoryDataset categoryDataset)
	{
		JRBaseCategoryDataset baseCategoryDataset = null;
		
		if (categoryDataset != null)
		{
			baseCategoryDataset = (JRBaseCategoryDataset)get(categoryDataset);
			if (baseCategoryDataset == null)
			{
				baseCategoryDataset = new JRBaseCategoryDataset(categoryDataset, this);
				put(categoryDataset, baseCategoryDataset);//FIXME NOW need this?
			}
		}
		
		return baseCategoryDataset;
	}
	

	/**
	 *
	 */
	public JRIntervalXyDataset getIntervalXyDataset(JRIntervalXyDataset intervalXyDataset)
	{
		JRBaseIntervalXyDataset baseIntervalXyDataset = null;
		
		if (intervalXyDataset != null)
		{
			baseIntervalXyDataset = (JRBaseIntervalXyDataset)get(intervalXyDataset);
			if (baseIntervalXyDataset == null)
			{
				baseIntervalXyDataset = new JRBaseIntervalXyDataset(intervalXyDataset, this);
				put(intervalXyDataset, baseIntervalXyDataset);//FIXME NOW need this?
			}
		}
		
		return baseIntervalXyDataset;
	}
	

	/**
	 *
	 */
	public JRCategorySeries getCategorySeries(JRCategorySeries categorySeries)
	{
		JRBaseCategorySeries baseCategorySeries = null;
		
		if (categorySeries != null)
		{
			baseCategorySeries = (JRBaseCategorySeries)get(categorySeries);
			if (baseCategorySeries == null)
			{
				baseCategorySeries = new JRBaseCategorySeries(categorySeries, this);
				put(categorySeries, baseCategorySeries);//FIXME NOW need this?
			}
		}
		
		return baseCategorySeries;
	}
	

	/**
	 *
	 */
	public JRXySeries getXySeries(JRXySeries xySeries)
	{
		JRBaseXySeries baseXySeries = null;
		
		if (xySeries != null)
		{
			baseXySeries = (JRBaseXySeries)get(xySeries);
			if (baseXySeries == null)
			{
				baseXySeries = new JRBaseXySeries(xySeries, this);
				put(xySeries, baseXySeries);//FIXME NOW need this?
			}
		}
		
		return baseXySeries;
	}
	

	/**
	 *
	 */
	public JRTimeSeries getTimeSeries(JRTimeSeries timeSeries)
	{
		JRBaseTimeSeries baseTimeSeries = null;
		
		if (timeSeries != null)
		{
			baseTimeSeries = (JRBaseTimeSeries)get(timeSeries);
			if (baseTimeSeries == null)
			{
				baseTimeSeries = new JRBaseTimeSeries(timeSeries, this);
				put(timeSeries, baseTimeSeries);//FIXME NOW need this?
			}
		}
		
		return baseTimeSeries;
	}
	

	/**
	 *
	 */
	public JRBarPlot getBarPlot(JRBarPlot barPlot)
	{
		JRBaseBarPlot baseBarPlot = null;
		
		if (barPlot != null)
		{
			baseBarPlot = (JRBaseBarPlot)get(barPlot);
			if (baseBarPlot == null)
			{
				baseBarPlot = new JRBaseBarPlot(barPlot, this);
				put(barPlot, baseBarPlot);//FIXME NOW need this?
			}
		}
		
		return baseBarPlot;
	}


	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRAbstractObjectFactory#getBar3DChart(net.sf.jasperreports.charts.JRBar3DChart)
	 */
	public JRBar3DChart getBar3DChart(JRBar3DChart barChart) {
		JRBaseBar3DChart baseBarChart = null;
		
		if (barChart != null)
		{
			baseBarChart = (JRBaseBar3DChart)get(barChart);
			if (baseBarChart == null)
			{
				baseBarChart = new JRBaseBar3DChart(barChart, this);
				put(barChart, baseBarChart);//FIXME NOW need this?
			}
		}
		
		return baseBarChart;
	}


	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRAbstractObjectFactory#getBar3DPlot(net.sf.jasperreports.charts.JRBar3DPlot)
	 */
	public JRBar3DPlot getBar3DPlot(JRBar3DPlot barPlot) {
		JRBaseBar3DPlot baseBarPlot = null;
		
		if (barPlot != null)
		{
			baseBarPlot = (JRBaseBar3DPlot)get(barPlot);
			if (baseBarPlot == null)
			{
				baseBarPlot = new JRBaseBar3DPlot(barPlot, this);
				put(barPlot, baseBarPlot);//FIXME NOW need this?
			}
		}
		
		return baseBarPlot;
	}


  
	

	/**
	 *
	 */
	public JRXyBarChart getXyBarChart(JRXyBarChart xyBarChart)
	{
		JRBaseXyBarChart baseXyBarChart = null;
		
		if (xyBarChart != null)
		{
			baseXyBarChart = (JRBaseXyBarChart)get(xyBarChart);
			if (baseXyBarChart == null)
			{
				baseXyBarChart = new JRBaseXyBarChart(xyBarChart, this);
				put(xyBarChart, baseXyBarChart);//FIXME NOW need this?
			}
		}
		
		return baseXyBarChart;
	}


	/** 
	 * 
	 */
	public JRLineChart getLineChart(JRLineChart lineChart) {
		JRBaseLineChart baseLineChart = null;
		
		if (lineChart != null)
		{
			baseLineChart = (JRBaseLineChart)get(lineChart);
			if (baseLineChart == null)
			{
				baseLineChart = new JRBaseLineChart(lineChart, this);
				put(lineChart, baseLineChart);//FIXME NOW need this?
			}
		}
		
		return baseLineChart;
	}


	/** 
	 * 
	 */
	public JRScatterChart getScatterChart(JRScatterChart scatterChart) {
		JRBaseScatterChart baseScatterChart = null;
		
		if (scatterChart != null)
		{
			baseScatterChart = (JRBaseScatterChart)get(scatterChart);
			if (baseScatterChart == null)
			{
				baseScatterChart = new JRBaseScatterChart(scatterChart, this);
				put(scatterChart, baseScatterChart);//FIXME NOW need this?
			}
		}
		
		return baseScatterChart;
	}


	/** 
	 * 
	 */
	public JRXyLineChart getXyLineChart(JRXyLineChart xyLineChart) {
		JRBaseXyLineChart baseXyLineChart = null;
		
		if (xyLineChart != null)
		{
			baseXyLineChart = (JRBaseXyLineChart)get(xyLineChart);
			if (baseXyLineChart == null)
			{
				baseXyLineChart = new JRBaseXyLineChart(xyLineChart, this);
				put(xyLineChart, baseXyLineChart);//FIXME NOW need this?
			}
		}
		
		return baseXyLineChart;
	}


	/** 
	 * 
	 */
	public JRLinePlot getLinePlot(JRLinePlot linePlot) {
		JRBaseLinePlot baseLinePlot = null;
		
		if (linePlot != null)
		{
			baseLinePlot = (JRBaseLinePlot)get(linePlot);
			if (baseLinePlot == null)
			{
				baseLinePlot = new JRBaseLinePlot(linePlot, this);
				put(linePlot, baseLinePlot);//FIXME NOW need this?
			}
		}
		
		return baseLinePlot;
	}


	/**
	 *  
	 */
	public JRAreaChart getAreaChart(JRAreaChart areaChart) {
		JRBaseAreaChart baseAreaChart = null;
		
		if (areaChart != null)
		{
			baseAreaChart = (JRBaseAreaChart)get(areaChart);
			if (baseAreaChart == null)
			{
				baseAreaChart = new JRBaseAreaChart(areaChart, this);
				put(areaChart, baseAreaChart);//FIXME NOW need this?
			}
		}
		
		return baseAreaChart;
	}


	/**
	 *  
	 */
	public JRXyAreaChart getXyAreaChart(JRXyAreaChart xyAreaChart) {
		JRBaseXyAreaChart baseXyAreaChart = null;
		
		if (xyAreaChart != null)
		{
			baseXyAreaChart = (JRBaseXyAreaChart)get(xyAreaChart);
			if (baseXyAreaChart == null)
			{
				baseXyAreaChart = new JRBaseXyAreaChart(xyAreaChart, this);
				put(xyAreaChart, baseXyAreaChart);//FIXME NOW need this?
			}
		}
		
		return baseXyAreaChart;
	}


	/**
	 * 
	 */
	public JRAreaPlot getAreaPlot(JRAreaPlot areaPlot) {
		JRBaseAreaPlot baseAreaPlot = null;
		
		if (areaPlot != null)
		{
			baseAreaPlot = (JRBaseAreaPlot)get(areaPlot);
			if (baseAreaPlot == null)
			{
				baseAreaPlot = new JRBaseAreaPlot(areaPlot, this);
				put(areaPlot, baseAreaPlot);//FIXME NOW need this?
			}
		}
		
		return baseAreaPlot;
	}
	


	public JRHighLowChart getHighLowChart(JRHighLowChart highLowChart)
	{
		JRBaseHighLowChart baseHighLowChart = null;

		if (highLowChart != null)
		{
			baseHighLowChart = (JRBaseHighLowChart)get(highLowChart);
			if (baseHighLowChart == null)
			{
				baseHighLowChart = new JRBaseHighLowChart(highLowChart, this);
				put(highLowChart, baseHighLowChart);//FIXME NOW need this?
			}
		}

		return baseHighLowChart;
	}


	public JRCandlestickChart getCandlestickChart(JRCandlestickChart candlestickChart)
	{
		JRBaseCandlestickChart baseCandlestickChart = null;

		if (candlestickChart != null)
		{
			baseCandlestickChart = (JRBaseCandlestickChart)get(candlestickChart);
			if (baseCandlestickChart == null)
			{
				baseCandlestickChart = new JRBaseCandlestickChart(candlestickChart, this);
				put(candlestickChart, baseCandlestickChart);//FIXME NOW need this?
			}
		}

		return baseCandlestickChart;
	}


	public JRCandlestickPlot getCandlestickPlot(JRCandlestickPlot candlestickPlot)
	{
		JRBaseCandlestickPlot baseCandlestickPlot = null;

		if (candlestickPlot != null)
		{
			baseCandlestickPlot = (JRBaseCandlestickPlot)get(candlestickPlot);
			if (baseCandlestickPlot == null)
			{
				baseCandlestickPlot = new JRBaseCandlestickPlot(candlestickPlot, this);
				put(candlestickPlot, baseCandlestickPlot);//FIXME NOW need this?
			}
		}

		return baseCandlestickPlot;
	}


}

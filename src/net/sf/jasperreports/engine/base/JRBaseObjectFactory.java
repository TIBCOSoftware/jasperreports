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
package net.sf.jasperreports.engine.base;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRIntervalXyDataset;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;
import net.sf.jasperreports.charts.base.JRBaseAreaPlot;
import net.sf.jasperreports.charts.base.JRBaseBar3DPlot;
import net.sf.jasperreports.charts.base.JRBaseBarPlot;
import net.sf.jasperreports.charts.base.JRBaseBubblePlot;
import net.sf.jasperreports.charts.base.JRBaseCandlestickPlot;
import net.sf.jasperreports.charts.base.JRBaseCategoryDataset;
import net.sf.jasperreports.charts.base.JRBaseCategorySeries;
import net.sf.jasperreports.charts.base.JRBaseHighLowDataset;
import net.sf.jasperreports.charts.base.JRBaseHighLowPlot;
import net.sf.jasperreports.charts.base.JRBaseIntervalXyDataset;
import net.sf.jasperreports.charts.base.JRBaseLinePlot;
import net.sf.jasperreports.charts.base.JRBasePie3DPlot;
import net.sf.jasperreports.charts.base.JRBasePieDataset;
import net.sf.jasperreports.charts.base.JRBasePiePlot;
import net.sf.jasperreports.charts.base.JRBaseScatterPlot;
import net.sf.jasperreports.charts.base.JRBaseTimePeriodDataset;
import net.sf.jasperreports.charts.base.JRBaseTimePeriodSeries;
import net.sf.jasperreports.charts.base.JRBaseTimeSeries;
import net.sf.jasperreports.charts.base.JRBaseTimeSeriesDataset;
import net.sf.jasperreports.charts.base.JRBaseTimeSeriesPlot;
import net.sf.jasperreports.charts.base.JRBaseXyDataset;
import net.sf.jasperreports.charts.base.JRBaseXySeries;
import net.sf.jasperreports.charts.base.JRBaseXyzDataset;
import net.sf.jasperreports.charts.base.JRBaseXyzSeries;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRChart;
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
	public JRPieDataset getPieDataset(JRPieDataset pieDataset)
	{
		JRBasePieDataset basePieDataset = null;
		
		if (pieDataset != null)
		{
			basePieDataset = (JRBasePieDataset)get(pieDataset);
			if (basePieDataset == null)
			{
				basePieDataset = new JRBasePieDataset(pieDataset, this);
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
			}
		}
		
		return basePiePlot;
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
			}
		}
		
		return basePie3DPlot;
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
			}
		}
		
		return baseCategoryDataset;
	}
	
	public JRTimeSeriesDataset getTimeSeriesDataset( JRTimeSeriesDataset timeSeriesDataset ){
		JRBaseTimeSeriesDataset baseTimeSeriesDataset = null;
		if( timeSeriesDataset != null ){
			baseTimeSeriesDataset = (JRBaseTimeSeriesDataset)get( timeSeriesDataset );
			if( baseTimeSeriesDataset == null ){
				baseTimeSeriesDataset = new JRBaseTimeSeriesDataset( timeSeriesDataset, this );
			}
		}
		
		return baseTimeSeriesDataset;
	}
	
	
	public JRTimePeriodDataset getTimePeriodDataset( JRTimePeriodDataset timePeriodDataset ){
		JRBaseTimePeriodDataset baseTimePeriodDataset = null;
		if( timePeriodDataset != null ){
			baseTimePeriodDataset = (JRBaseTimePeriodDataset)get( timePeriodDataset );
			if( baseTimePeriodDataset == null ){
				baseTimePeriodDataset = new JRBaseTimePeriodDataset( timePeriodDataset, this );
			}
		}
		return baseTimePeriodDataset;
	}
	
	public JRIntervalXyDataset getIntervalXyDataset( JRIntervalXyDataset intervalXyDataset ){
		JRBaseIntervalXyDataset baseIntervalXyDataset = null;
		if( intervalXyDataset != null ){
			baseIntervalXyDataset = (JRBaseIntervalXyDataset)get( intervalXyDataset );
			if( baseIntervalXyDataset == null ){
				baseIntervalXyDataset = new JRBaseIntervalXyDataset( intervalXyDataset );
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
			}
		}
		
		return baseTimeSeries;
	}
	
	/**
	 * 
	 */
	public JRTimePeriodSeries getTimePeriodSeries( JRTimePeriodSeries timePeriodSeries ){
		JRBaseTimePeriodSeries baseTimePeriodSeries = null;
		if( timePeriodSeries != null ){
			baseTimePeriodSeries = (JRBaseTimePeriodSeries)get( timePeriodSeries );
			if( baseTimePeriodSeries == null ){
				baseTimePeriodSeries = new JRBaseTimePeriodSeries( timePeriodSeries, this );
			}
		}
		
		return baseTimePeriodSeries;
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
			}
		}
		
		return baseBarPlot;
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
			}
		}
		
		return baseBarPlot;
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
			}
		}
		
		return baseLinePlot;
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
			}
		}
		
		return baseAreaPlot;
	}


	/* 
	 *
	 */
	public JRXyzDataset getXyzDataset(JRXyzDataset xyzDataset) {
		JRBaseXyzDataset baseXyzDataset = null;
		
		if (xyzDataset != null)
		{
			baseXyzDataset = (JRBaseXyzDataset)get(xyzDataset);
			if (baseXyzDataset == null)
			{
				baseXyzDataset = new JRBaseXyzDataset(xyzDataset, this);
			}
		}
		
		return baseXyzDataset;
	}


	/*
	 *
	 */
	public JRXyDataset getXyDataset(JRXyDataset xyDataset) {
		JRBaseXyDataset baseXyDataset = null;

		if (xyDataset != null)
		{
			baseXyDataset = (JRBaseXyDataset)get(xyDataset);
			if (baseXyDataset == null)
			{
				baseXyDataset = new JRBaseXyDataset(xyDataset, this);
			}
		}

		return baseXyDataset;
	}

	/*
	 *
	 */
	public JRHighLowDataset getHighLowDataset(JRHighLowDataset highLowDataset) {
		JRBaseHighLowDataset baseHighLowDataset = null;

		if (highLowDataset != null)
		{
			baseHighLowDataset = (JRBaseHighLowDataset)get(highLowDataset);
			if (baseHighLowDataset == null)
			{
				baseHighLowDataset = new JRBaseHighLowDataset(highLowDataset, this);
			}
		}

		return baseHighLowDataset;
	}


	/**
	 * 
	 */
	public JRXyzSeries getXyzSeries(JRXyzSeries xyzSeries) {
		JRBaseXyzSeries baseXyzSeries = null;
		
		if (xyzSeries != null)
		{
			baseXyzSeries = (JRBaseXyzSeries)get(xyzSeries);
			if (baseXyzSeries == null)
			{
				baseXyzSeries = new JRBaseXyzSeries(xyzSeries, this);
			}
		}
		
		return baseXyzSeries;
	}


	/**
	 *
	 */
	public JRBubblePlot getBubblePlot(JRBubblePlot bubblePlot) {
		JRBaseBubblePlot baseBubblePlot = null;
		
		if (bubblePlot != null)
		{
			baseBubblePlot = (JRBaseBubblePlot)get(bubblePlot);
			if (baseBubblePlot == null)
			{
				baseBubblePlot = new JRBaseBubblePlot(bubblePlot, this);
			}
		}
		
		return baseBubblePlot;
	}
	

	 /**
	  *
	  */
	 public JRCandlestickPlot getCandlestickPlot(JRCandlestickPlot candlestickPlot)
	{
		JRBaseCandlestickPlot baseCandlestickPlot = null;

		if (candlestickPlot != null)
		{
			baseCandlestickPlot = (JRBaseCandlestickPlot)get(candlestickPlot);
			if (baseCandlestickPlot == null)
			{
				baseCandlestickPlot = new JRBaseCandlestickPlot(candlestickPlot, this);
			}
		}

		return baseCandlestickPlot;
	}


	/**
	 *
	 */
	public JRHighLowPlot getHighLowPlot(JRHighLowPlot highLowPlot)
	{
		JRBaseHighLowPlot baseHighLowPlot = null;

		if (highLowPlot != null)
		{
			baseHighLowPlot = (JRBaseHighLowPlot)get(highLowPlot);
			if (baseHighLowPlot == null)
			{
				baseHighLowPlot = new JRBaseHighLowPlot(highLowPlot, this);
			}
		}

		return baseHighLowPlot;
	}


	/**
	 *
	 */
	public JRScatterPlot getScatterPlot(JRScatterPlot scatterPlot)
	{
		JRBaseScatterPlot baseScatterPlot = null;

		if (scatterPlot != null)
		{
			baseScatterPlot = (JRBaseScatterPlot)get(scatterPlot);
			if (baseScatterPlot == null)
			{
				baseScatterPlot = new JRBaseScatterPlot(scatterPlot, this);
			}
		}

		return baseScatterPlot;
	}
	
	

	public JRTimeSeriesPlot getTimeSeriesPlot( JRTimeSeriesPlot plot ){
		JRBaseTimeSeriesPlot basePlot = null;
		if( plot != null ){
			basePlot = (JRBaseTimeSeriesPlot)get( plot );
			if( basePlot == null ){
				basePlot = new JRBaseTimeSeriesPlot( plot, this );
			}
		}
		
		return basePlot;
	}

	/**
	 *
	 */
	public JRChart getChart(JRChart chart)
	{
		JRBaseChart baseChart = null;

		if (chart != null)
		{
			baseChart = (JRBaseChart)get(chart);
			if (baseChart == null)
			{
				baseChart = new JRBaseChart(chart, this);
			}
		}

		return baseChart;
	}

}

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
package net.sf.jasperreports.engine.base;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRGanttDataset;
import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRMultiAxisPlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRPieSeries;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.charts.JRValueDataset;
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
import net.sf.jasperreports.charts.base.JRBaseChartAxis;
import net.sf.jasperreports.charts.base.JRBaseGanttDataset;
import net.sf.jasperreports.charts.base.JRBaseGanttSeries;
import net.sf.jasperreports.charts.base.JRBaseHighLowDataset;
import net.sf.jasperreports.charts.base.JRBaseHighLowPlot;
import net.sf.jasperreports.charts.base.JRBaseLinePlot;
import net.sf.jasperreports.charts.base.JRBaseMeterPlot;
import net.sf.jasperreports.charts.base.JRBaseMultiAxisPlot;
import net.sf.jasperreports.charts.base.JRBasePie3DPlot;
import net.sf.jasperreports.charts.base.JRBasePieDataset;
import net.sf.jasperreports.charts.base.JRBasePiePlot;
import net.sf.jasperreports.charts.base.JRBasePieSeries;
import net.sf.jasperreports.charts.base.JRBaseScatterPlot;
import net.sf.jasperreports.charts.base.JRBaseThermometerPlot;
import net.sf.jasperreports.charts.base.JRBaseTimePeriodDataset;
import net.sf.jasperreports.charts.base.JRBaseTimePeriodSeries;
import net.sf.jasperreports.charts.base.JRBaseTimeSeries;
import net.sf.jasperreports.charts.base.JRBaseTimeSeriesDataset;
import net.sf.jasperreports.charts.base.JRBaseTimeSeriesPlot;
import net.sf.jasperreports.charts.base.JRBaseValueDataset;
import net.sf.jasperreports.charts.base.JRBaseXyDataset;
import net.sf.jasperreports.charts.base.JRBaseXySeries;
import net.sf.jasperreports.charts.base.JRBaseXyzDataset;
import net.sf.jasperreports.charts.base.JRBaseXyzSeries;
import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabDataset;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.base.JRBaseCellContents;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstab;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstabBucket;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstabCell;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstabDataset;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstabMeasure;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstabParameter;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstabRowGroup;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRScriptlet;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.JRStyleSetter;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;


/**
 * Factory of objects used in compiled reports.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseObjectFactory extends JRAbstractObjectFactory
{


	/**
	 *
	 */
	private JRDefaultStyleProvider defaultStyleProvider;

	/**
	 * Expression collector used to retrieve generated expression IDs.
	 */
	private JRExpressionCollector expressionCollector;
	

	/**
	 *
	 */
	protected JRBaseObjectFactory(JRDefaultStyleProvider defaultStyleProvider)
	{
		this.defaultStyleProvider = defaultStyleProvider;
	}


	/**
	 * Constructs a base object factory.
	 *
	 * @param defaultStyleProvider the default style provider
	 * @param expressionCollector the expression collector used as expression ID provider
	 */
	protected JRBaseObjectFactory(JRDefaultStyleProvider defaultStyleProvider, JRExpressionCollector expressionCollector)
	{
		this.defaultStyleProvider = defaultStyleProvider;
		this.expressionCollector = expressionCollector;
	}

	protected JRBaseObjectFactory(JRExpressionCollector expressionCollector)
	{
		this.expressionCollector = expressionCollector;
	}
	
	public void setDefaultStyleProvider(JRDefaultStyleProvider defaultStyleProvider)
	{
		this.defaultStyleProvider = defaultStyleProvider;
	}

	/**
	 *
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider;
	}


	/**
	 *
	 */
	public JRStyle getStyle(JRStyle style)
	{
		JRBaseStyle baseStyle = null;

		if (style != null)
		{
			baseStyle = (JRBaseStyle)get(style);
			if (baseStyle == null)
			{
				baseStyle = new JRBaseStyle(style, this);
				put(style, baseStyle);
			}
		}

		return baseStyle;
	}


	/**
	 * This method preserves both specified styles and external style name references.
	 *
	 * @see JRAbstractObjectFactory#setStyle(JRStyleSetter, JRStyleContainer)
	 */
	public void setStyle(JRStyleSetter setter, JRStyleContainer styleContainer)
	{
		JRStyle style = styleContainer.getStyle();
		String nameReference = styleContainer.getStyleNameReference();
		if (style != null)
		{
			JRStyle newStyle = getStyle(style);
			setter.setStyle(newStyle);
		}
		else if (nameReference != null)
		{
			handleStyleNameReference(setter, nameReference);
		}
	}

	protected void handleStyleNameReference(JRStyleSetter setter, String nameReference)
	{
		setter.setStyleNameReference(nameReference);
	}


	/**
	 *
	 */
	protected JRBaseScriptlet getScriptlet(JRScriptlet scriptlet)
	{
		JRBaseScriptlet baseScriptlet = null;

		if (scriptlet != null)
		{
			baseScriptlet = (JRBaseScriptlet)get(scriptlet);
			if (baseScriptlet == null)
			{
				baseScriptlet = new JRBaseScriptlet(scriptlet, this);
			}
		}

		return baseScriptlet;
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
	protected JRBaseSortField getSortField(JRSortField sortField)
	{
		JRBaseSortField baseSortField = null;

		if (sortField != null)
		{
			baseSortField = (JRBaseSortField)get(sortField);
			if (baseSortField == null)
			{
				baseSortField = new JRBaseSortField(sortField, this);
			}
		}

		return baseSortField;
	}


	/**
	 *
	 */
	public JRBaseVariable getVariable(JRVariable variable)
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


	public JRExpression getExpression(JRExpression expression, boolean assignNotUsedId)
	{
		JRBaseExpression baseExpression = null;

		if (expression != null)
		{
			baseExpression = (JRBaseExpression)get(expression);
			if (baseExpression == null)
			{
				Integer expressionId = getCollectedExpressionId(expression, assignNotUsedId);
				baseExpression = new JRBaseExpression(expression, this, expressionId);
			}
		}

		return baseExpression;
	}


	private Integer getCollectedExpressionId(JRExpression expression, boolean assignNotUsedId)
	{
		Integer expressionId = null;
		if (expressionCollector != null)
		{
			expressionId = expressionCollector.getExpressionId(expression);
			if (expressionId == null)
			{
				if (assignNotUsedId)
				{
					expressionId = JRExpression.NOT_USED_ID;
				}
				else
				{
					throw new JRRuntimeException("Expression ID not found for expression <<" + expression.getText() + ">>.");
				}
			}
		}
		return expressionId;
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
	protected JRBaseSection getSection(JRSection section)
	{
		JRBaseSection baseSection = null;

		if (section != null)
		{
			baseSection = (JRBaseSection)get(section);
			if (baseSection == null)
			{
				baseSection = new JRBaseSection(section, this);
			}
		}

		return baseSection;
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
	public void visitElementGroup(JRElementGroup elementGroup)
	{
		JRElementGroup baseElementGroup = null;

		if (elementGroup != null)
		{
			baseElementGroup = (JRElementGroup)get(elementGroup);
			if (baseElementGroup == null)
			{
				baseElementGroup = new JRBaseElementGroup(elementGroup, this);
			}
		}

		setVisitResult(baseElementGroup);
	}


	/**
	 *
	 */
	public void visitBreak(JRBreak breakElement)
	{
		JRBaseBreak baseBreak = null;

		if (breakElement != null)
		{
			baseBreak = (JRBaseBreak)get(breakElement);
			if (baseBreak == null)
			{
				baseBreak = new JRBaseBreak(breakElement, this);
			}
		}

		setVisitResult(baseBreak);
	}


	/**
	 *
	 */
	public void visitLine(JRLine line)
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

		setVisitResult(baseLine);
	}


	/**
	 *
	 */
	public void visitRectangle(JRRectangle rectangle)
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

		setVisitResult(baseRectangle);
	}


	/**
	 *
	 */
	public void visitEllipse(JREllipse ellipse)
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

		setVisitResult(baseEllipse);
	}


	/**
	 *
	 */
	public void visitImage(JRImage image)
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

		setVisitResult(baseImage);
	}


	/**
	 *
	 */
	public void visitStaticText(JRStaticText staticText)
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

		setVisitResult(baseStaticText);
	}


	/**
	 *
	 */
	public void visitTextField(JRTextField textField)
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

		setVisitResult(baseTextField);
	}


	/**
	 *
	 */
	public void visitSubreport(JRSubreport subreport)
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

		setVisitResult(baseSubreport);
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


	protected JRBaseDatasetParameter getDatasetParameter(JRDatasetParameter datasetParameter)
	{
		JRBaseDatasetParameter baseSubreportParameter = null;

		if (datasetParameter != null)
		{
			baseSubreportParameter = (JRBaseDatasetParameter) get(datasetParameter);
			if (baseSubreportParameter == null)
			{
				baseSubreportParameter = new JRBaseDatasetParameter(datasetParameter, this);
				put(datasetParameter, baseSubreportParameter);
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

	/*
	 * 
	 */
	public JRGanttDataset getGanttDataset(JRGanttDataset ganttDataset)
	{
		JRBaseGanttDataset baseGanttDataset = null;
		
		if (ganttDataset != null)
		{
			baseGanttDataset = (JRBaseGanttDataset)get(ganttDataset);
			if (baseGanttDataset == null)
			{
				baseGanttDataset = new JRBaseGanttDataset(ganttDataset, this);
			}
		}
		
		return baseGanttDataset;
	}

	
	/**
	 *
	 */
	public JRPieSeries getPieSeries(JRPieSeries pieSeries)
	{
		JRBasePieSeries basePieSeries = null;

		if (pieSeries != null)
		{
			basePieSeries = (JRBasePieSeries)get(pieSeries);
			if (basePieSeries == null)
			{
				basePieSeries = new JRBasePieSeries(pieSeries, this);
			}
		}

		return basePieSeries;
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
	public JRGanttSeries getGanttSeries(JRGanttSeries ganttSeries)
	{
		JRBaseGanttSeries baseGanttSeries = null;
		
		if (ganttSeries != null)
		{
			baseGanttSeries = (JRBaseGanttSeries)get(ganttSeries);
			if (baseGanttSeries == null)
			{
				baseGanttSeries = new JRBaseGanttSeries(ganttSeries, this);
			}
		}
		
		return baseGanttSeries;
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
	public JRValueDataset getValueDataset(JRValueDataset valueDataset)
	{
		JRBaseValueDataset baseValueDataset = null;

		if (valueDataset != null)
		{
			baseValueDataset = (JRBaseValueDataset)get(valueDataset);
			if (baseValueDataset == null)
			{
				baseValueDataset = new JRBaseValueDataset(valueDataset, this);
			}
		}

		return baseValueDataset;
	}


	/**
	 *
	 */
	public JRMeterPlot getMeterPlot(JRMeterPlot meterPlot)
	{
		JRBaseMeterPlot baseMeterPlot = null;

		if (meterPlot != null)
		{
			baseMeterPlot = (JRBaseMeterPlot)get(meterPlot);
			if (baseMeterPlot == null)
			{
				baseMeterPlot = new JRBaseMeterPlot(meterPlot, this);
			}
		}

		return baseMeterPlot;
	}


	/**
	 *
	 */
	public JRThermometerPlot getThermometerPlot(JRThermometerPlot thermometerPlot)
	{
		JRBaseThermometerPlot baseThermometerPlot = null;

		if (thermometerPlot != null)
		{
			baseThermometerPlot = (JRBaseThermometerPlot)get(thermometerPlot);
			if (baseThermometerPlot == null)
			{
				baseThermometerPlot = new JRBaseThermometerPlot(thermometerPlot, this);
			}
		}

		return baseThermometerPlot;
	}


	/**
	 *
	 */
	public JRMultiAxisPlot getMultiAxisPlot(JRMultiAxisPlot multiAxisPlot)
	{
		JRBaseMultiAxisPlot baseMultiAxisPlot = null;

		if (multiAxisPlot != null)
		{
			baseMultiAxisPlot = (JRBaseMultiAxisPlot)get(multiAxisPlot);
			if (baseMultiAxisPlot == null)
			{
				baseMultiAxisPlot = new JRBaseMultiAxisPlot(multiAxisPlot, this);
			}
		}

		return baseMultiAxisPlot;
	}


	/**
	 *
	 */
	public void visitChart(JRChart chart)
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

		setVisitResult(baseChart);
	}

	/**
	 *
	 */
	protected JRBaseSubreportReturnValue getSubreportReturnValue(JRSubreportReturnValue returnValue)
	{
		JRBaseSubreportReturnValue baseSubreportReturnValue = null;

		if (returnValue != null)
		{
			baseSubreportReturnValue = (JRBaseSubreportReturnValue)get(returnValue);
			if (baseSubreportReturnValue == null)
			{
				baseSubreportReturnValue = new JRBaseSubreportReturnValue(returnValue, this);
				put(returnValue, baseSubreportReturnValue);
			}
		}

		return baseSubreportReturnValue;
	}


	/**
	 *
	 */
	public JRConditionalStyle getConditionalStyle(JRConditionalStyle conditionalStyle, JRStyle style)
	{
		JRBaseConditionalStyle baseConditionalStyle = null;
		if (conditionalStyle != null)
		{
			baseConditionalStyle = (JRBaseConditionalStyle) get(conditionalStyle);
			if (baseConditionalStyle == null) {
				baseConditionalStyle = new JRBaseConditionalStyle(conditionalStyle, style, this);
				put(conditionalStyle, baseConditionalStyle);
			}
		}
		return baseConditionalStyle;
	}


	public JRBaseCrosstabDataset getCrosstabDataset(JRCrosstabDataset crosstabDataset)
	{
		JRBaseCrosstabDataset baseCrosstabDataset = null;

		if (crosstabDataset != null)
		{
			baseCrosstabDataset = (JRBaseCrosstabDataset) get(crosstabDataset);
			if (baseCrosstabDataset == null)
			{
				baseCrosstabDataset = new JRBaseCrosstabDataset(crosstabDataset, this);
			}
		}

		return baseCrosstabDataset;
	}


	public JRBaseCrosstabRowGroup getCrosstabRowGroup(JRCrosstabRowGroup group)
	{
		JRBaseCrosstabRowGroup baseCrosstabRowGroup = null;

		if (group != null)
		{
			baseCrosstabRowGroup = (JRBaseCrosstabRowGroup) get(group);
			if (baseCrosstabRowGroup == null)
			{
				baseCrosstabRowGroup = new JRBaseCrosstabRowGroup(group, this);
			}
		}

		return baseCrosstabRowGroup;
	}


	public JRBaseCrosstabColumnGroup getCrosstabColumnGroup(JRCrosstabColumnGroup group)
	{
		JRBaseCrosstabColumnGroup baseCrosstabDataset = null;

		if (group != null)
		{
			baseCrosstabDataset = (JRBaseCrosstabColumnGroup) get(group);
			if (baseCrosstabDataset == null)
			{
				baseCrosstabDataset = new JRBaseCrosstabColumnGroup(group, this);
			}
		}

		return baseCrosstabDataset;
	}


	public JRBaseCrosstabBucket getCrosstabBucket(JRCrosstabBucket bucket)
	{
		JRBaseCrosstabBucket baseCrosstabBucket = null;

		if (bucket != null)
		{
			baseCrosstabBucket = (JRBaseCrosstabBucket) get(bucket);
			if (baseCrosstabBucket == null)
			{
				baseCrosstabBucket = new JRBaseCrosstabBucket(bucket, this);
			}
		}

		return baseCrosstabBucket;
	}


	public JRBaseCrosstabMeasure getCrosstabMeasure(JRCrosstabMeasure measure)
	{
		JRBaseCrosstabMeasure baseCrosstabMeasure = null;

		if (measure != null)
		{
			baseCrosstabMeasure = (JRBaseCrosstabMeasure) get(measure);
			if (baseCrosstabMeasure == null)
			{
				baseCrosstabMeasure = new JRBaseCrosstabMeasure(measure, this);
			}
		}

		return baseCrosstabMeasure;
	}


	public void visitCrosstab(JRCrosstab crosstab)
	{
		JRBaseCrosstab baseCrosstab = null;

		if (crosstab != null)
		{
			baseCrosstab = (JRBaseCrosstab) get(crosstab);
			if (baseCrosstab == null)
			{
				Integer id = expressionCollector.getCrosstabId(crosstab);
				if (id == null)
				{
					throw new JRRuntimeException("Crosstab ID not found.");
				}

				baseCrosstab = new JRBaseCrosstab(crosstab, this, id.intValue());
			}
		}

		setVisitResult(baseCrosstab);
	}


	public JRBaseDataset getDataset(JRDataset dataset)
	{
		JRBaseDataset baseDataset = null;

		if (dataset != null)
		{
			baseDataset = (JRBaseDataset)get(dataset);
			if (baseDataset == null)
			{
				baseDataset = new JRBaseDataset(dataset, this);
			}
		}

		return baseDataset;
	}


	public JRBaseDatasetRun getDatasetRun(JRDatasetRun datasetRun)
	{
		JRBaseDatasetRun baseDatasetRun = null;

		if (datasetRun != null)
		{
			baseDatasetRun = (JRBaseDatasetRun)get(datasetRun);
			if (baseDatasetRun == null)
			{
				baseDatasetRun = new JRBaseDatasetRun(datasetRun, this);
			}
		}

		return baseDatasetRun;
	}


	public JRBaseCellContents getCell(JRCellContents cell)
	{
		JRBaseCellContents baseCell = null;

		if (cell != null)
		{
			baseCell = (JRBaseCellContents)get(cell);
			if (baseCell == null)
			{
				baseCell = new JRBaseCellContents(cell, this);
			}
		}

		return baseCell;
	}


	public JRCrosstabCell getCrosstabCell(JRCrosstabCell cell)
	{
		JRBaseCrosstabCell baseCell = null;

		if (cell != null)
		{
			baseCell = (JRBaseCrosstabCell)get(cell);
			if (baseCell == null)
			{
				baseCell = new JRBaseCrosstabCell(cell, this);
			}
		}

		return baseCell;
	}


	public JRBaseCrosstabParameter getCrosstabParameter(JRCrosstabParameter parameter)
	{
		JRBaseCrosstabParameter baseParameter = null;

		if (parameter != null)
		{
			baseParameter = (JRBaseCrosstabParameter) get(parameter);
			if (baseParameter == null)
			{
				baseParameter = new JRBaseCrosstabParameter(parameter, this);
			}
		}

		return baseParameter;
	}


	public void visitFrame(JRFrame frame)
	{
		JRBaseFrame baseFrame = null;

		if (frame != null)
		{
			baseFrame = (JRBaseFrame) get(frame);
			if (baseFrame == null)
			{
				baseFrame = new JRBaseFrame(frame, this);
			}
		}

		setVisitResult(baseFrame);
	}


	public JRHyperlinkParameter getHyperlinkParameter(JRHyperlinkParameter parameter)
	{
		JRHyperlinkParameter baseParameter = null;

		if (parameter != null)
		{
			baseParameter = (JRHyperlinkParameter) get(parameter);
			if (baseParameter == null)
			{
				baseParameter = new JRBaseHyperlinkParameter(parameter, this);
			}
		}

		return baseParameter;
	}


	public JRHyperlink getHyperlink(JRHyperlink hyperlink)
	{
		JRHyperlink link = null;
		if (hyperlink != null)
		{
			link = (JRHyperlink) get(hyperlink);
			if (link == null)
			{
				link = new JRBaseHyperlink(hyperlink, this);
			}
		}
		return link;
	}


	public JRChartAxis getChartAxis(JRChartAxis axis)
	{
		JRChartAxis baseAxis = null;
		if (axis != null)
		{
			baseAxis = (JRChartAxis) get(axis);
			if (baseAxis == null)
			{
				baseAxis = new JRBaseChartAxis(axis, this);
			}
		}
		return baseAxis;
	}

	public JRReportTemplate getReportTemplate(JRReportTemplate template)
	{
		JRReportTemplate baseTemplate = null;
		if (template != null)
		{
			baseTemplate = (JRReportTemplate) get(template);
			if (baseTemplate == null)
			{
				baseTemplate = new JRBaseReportTemplate(template, this);
			}
		}
		return baseTemplate;
	}

	public JRPropertyExpression getPropertyExpression(JRPropertyExpression propertyExpression)
	{
		JRPropertyExpression baseProp = null;
		if (propertyExpression != null)
		{
			baseProp = (JRPropertyExpression) get(propertyExpression);
			if (baseProp == null)
			{
				baseProp = new JRBasePropertyExpression(propertyExpression, this);
			}
		}
		return baseProp;
	}


	public void visitComponentElement(JRComponentElement componentElement)
	{
		JRBaseComponentElement base = null;

		if (componentElement != null)
		{
			base = (JRBaseComponentElement) get(componentElement);
			if (base == null)
			{
				base = new JRBaseComponentElement(componentElement, this);
			}
		}

		setVisitResult(base);
	}


	public JRGenericElementParameter getGenericElementParameter(
			JRGenericElementParameter elementParameter)
	{
		JRGenericElementParameter baseParameter = null;
		if (elementParameter != null)
		{
			baseParameter = (JRGenericElementParameter) get(elementParameter);
			if (baseParameter == null)
			{
				baseParameter = new JRBaseGenericElementParameter(
						elementParameter, this);
			}
		}
		return baseParameter;
	}


	public void visitGenericElement(JRGenericElement element)
	{
		JRBaseGenericElement base = null;
		if (element != null)
		{
			base = (JRBaseGenericElement) get(element);
			if (base == null)
			{
				base = new JRBaseGenericElement(element, this);
			}
		}
		setVisitResult(base);
	}
}

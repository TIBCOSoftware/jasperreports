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
package net.sf.jasperreports.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRGanttDataset;
import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRPieDataset;
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
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabDataset;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentCompiler;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentManager;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;


/**
 * An expression collector traverses a report and collects report expressions
 * out of it.
 * 
 * <p>
 * The expressions are then included into evaluator classes which are compiled
 * and used at report fill time to evaluate expressions.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRExpressionCollector
{

	public static JRExpressionCollector collector(JRReport report)
	{
		JRExpressionCollector collector = new JRExpressionCollector(null, report);
		collector.collect();
		return collector;
	}

	public static List<JRExpression> collectExpressions(JRReport report)
	{
		return collector(report).getExpressions();
	}

	public static JRExpressionCollector collector(JRReport report, JRCrosstab crosstab)
	{
		JRExpressionCollector collector = new JRExpressionCollector(null, report);
		collector.collect(crosstab);
		return collector;
	}

	public static List<JRExpression> collectExpressions(JRReport report, JRCrosstab crosstab)
	{
		return collector(report, crosstab).getExpressions(crosstab);
	}

	private final JRReport report;
	private final JRExpressionCollector parent;

	private Map<JRExpression,Integer> expressionIds;
	
	private LinkedList<Object> contextStack;
	private Map<JRExpression, Object> expressionContextMap;

	protected static class GeneratedIds
	{
		private final TreeMap<Integer, JRExpression> ids = new TreeMap<Integer, JRExpression>();
		private int nextId;
		private List<JRExpression> expressions;

		public JRExpression put(Integer id, JRExpression expression)
		{
			expressions = null;

			return ids.put(id, expression);
		}

		public Integer nextId()
		{
			Integer id = Integer.valueOf(nextId);
			while(ids.containsKey(id))
			{
				id = Integer.valueOf(++nextId);
			}
			return id;
		}

		public List<JRExpression> expressions()
		{
			if (expressions == null)
			{
				expressions = new ArrayList<JRExpression>(ids.values());
			}
			return expressions;
		}

		public JRExpression expression(int id)
		{
			return ids.get(Integer.valueOf(id));
		}
	}
	private GeneratedIds generatedIds = new GeneratedIds();

	private Map<JRCrosstab,Integer> crosstabIds = new HashMap<JRCrosstab,Integer>();

	/**
	 * Collectors for sub datasets indexed by dataset name.
	 */
	private Map<String,JRExpressionCollector> datasetCollectors;

	/**
	 * Collectors for crosstabs.
	 */
	private Map<JRCrosstab,JRExpressionCollector> crosstabCollectors;

	private final Set<JRStyle> collectedStyles;


	protected JRExpressionCollector(JRExpressionCollector parent, JRReport report)
	{
		this.parent = parent;
		this.report = report;

		if (parent == null)
		{
			expressionIds = new HashMap<JRExpression,Integer>();
			datasetCollectors = new HashMap<String,JRExpressionCollector>();
			crosstabCollectors = new HashMap<JRCrosstab,JRExpressionCollector>();
			contextStack = new LinkedList<Object>();
			expressionContextMap = new HashMap<JRExpression, Object>();
		}
		else
		{
			expressionIds = this.parent.expressionIds;
			contextStack = this.parent.contextStack;
			expressionContextMap = this.parent.expressionContextMap;
		}

		collectedStyles = new HashSet<JRStyle>();
	}

	/**
	 * Collects an expression.
	 * 
	 * @param expression the expression to collect
	 */
	public void addExpression(JRExpression expression)
	{
		if (expression != null)
		{
			Integer id = getExpressionId(expression);
			if (id == null)
			{
				id = generatedIds.nextId();
				setGeneratedId(expression, id);
				generatedIds.put(id, expression);
			}
			else
			{
				JRExpression existingExpression = generatedIds.put(id, expression);
				if (existingExpression != null && !existingExpression.equals(expression))
				{
					Integer newId = generatedIds.nextId();
					updateGeneratedId(existingExpression, id, newId);
					generatedIds.put(newId, existingExpression);
				}
			}
			
			setExpressionContext(expression);
		}
	}

	private void setGeneratedId(JRExpression expression, Integer id)
	{
		Object existingId = expressionIds.put(expression, id);
		if (existingId != null && !existingId.equals(id))
		{
			throw new JRRuntimeException("Expression \"" + expression.getText() + "\" has two generated IDs");
		}
	}

	private void updateGeneratedId(JRExpression expression, Integer currentId, Integer newId)
	{
		Object existingId = expressionIds.put(expression, newId);
		if (existingId == null || !existingId.equals(currentId))
		{
			throw new JRRuntimeException("Expression \"" + expression.getText() + "\" not found with id " + currentId);
		}
	}

	protected void pushContextObject(Object context)
	{
		contextStack.addLast(context);
	}

	protected Object popContextObject()
	{
		return contextStack.removeLast();
	}

	protected void setExpressionContext(JRExpression expression)
	{
		if (!contextStack.isEmpty())
		{
			Object context = contextStack.getLast();
			expressionContextMap.put(expression, context);
		}
	}
	
	/**
	 * Returns the expression collector to which expressions in an element
	 * dataset belong.
	 * 
	 * <p>
	 * If the element dataset includes a subdataset run, a (sub) expression
	 * collector that corresponds to the subdataset will be returned.
	 * Otherwise, this/the main expression collector will be returned.
	 * 
	 * @param elementDataset an element dataset
	 * @return the expression collector to be used for the element dataset
	 */
	public JRExpressionCollector getCollector(JRElementDataset elementDataset)
	{
		JRExpressionCollector collector;

		JRDatasetRun datasetRun = elementDataset.getDatasetRun();
		if (datasetRun == null)
		{
			collector = this;
		}
		else
		{
			collector = getDatasetCollector(datasetRun.getDatasetName());
		}

		return collector;
	}


	/**
	 * Returns the expression collector for a report subdataset.
	 * 
	 * @param datasetName the subdataset name
	 * @return the expression collector for the subdataset
	 */
	public JRExpressionCollector getDatasetCollector(String datasetName)
	{
		JRExpressionCollector collector;
		if (parent == null)
		{
			collector = datasetCollectors.get(datasetName);
			if (collector == null)
			{
				collector = new JRExpressionCollector(this, report);
				datasetCollectors.put(datasetName, collector);
			}
		}
		else
		{
			collector = parent.getDatasetCollector(datasetName);
		}
		return collector;
	}


	/**
	 * Returns the expression collector for a dataset.
	 *
	 * @param dataset the dataset
	 * @return the dataset expression collector
	 */
	public JRExpressionCollector getCollector(JRDataset dataset)
	{
		JRExpressionCollector collector;
		if (parent == null)
		{
			if (dataset.isMainDataset() || datasetCollectors == null)
			{
				collector = this;
			}
			else
			{
				collector = getDatasetCollector(dataset.getName());
			}
		}
		else
		{
			collector = parent.getCollector(dataset);
		}
		return collector;
	}


	/**
	 * Returns the expression collector for a crosstab.
	 *
	 * @param crosstab the crosstab
	 * @return the crosstab expression collector
	 */
	public JRExpressionCollector getCollector(JRCrosstab crosstab)
	{
		JRExpressionCollector collector;
		if (parent == null)
		{
			collector = crosstabCollectors.get(crosstab);
			if (collector == null)
			{
				collector = new JRExpressionCollector(this, report);
				crosstabCollectors.put(crosstab, collector);
			}
		}
		else
		{
			collector = parent.getCollector(crosstab);	
		}
		return collector;
	}


	/**
	 * Returns the collected expressions.
	 *
	 * @return the collected expressions
	 */
	public List<JRExpression> getExpressions()
	{
		return new ArrayList<JRExpression>(generatedIds.expressions());
	}


	/**
	 * Returns the expressions collected for a dataset.
	 *
	 * @param dataset the dataset
	 * @return the expressions
	 */
	public List<JRExpression> getExpressions(JRDataset dataset)
	{
		return getCollector(dataset).getExpressions();
	}


	/**
	 * Returns the expressions collected for a crosstab.
	 *
	 * @param crosstab the crosstab
	 * @return the expressions
	 */
	public List<JRExpression> getExpressions(JRCrosstab crosstab)
	{
		return getCollector(crosstab).getExpressions();
	}


	public Integer getExpressionId(JRExpression expression)
	{
		return expressionIds.get(expression);
	}


	public JRExpression getExpression(int expressionId)
	{
		return generatedIds.expression(expressionId);
	}


	public Integer getCrosstabId(JRCrosstab crosstab)
	{
		return crosstabIds.get(crosstab);
	}

	public Object getExpressionContext(JRExpression expression)
	{
		return expressionContextMap.get(expression);
	}

	/**
	 *
	 */
	public Collection<JRExpression> collect()
	{
		collectTemplates();

		collect(report.getDefaultStyle());

		collect(report.getMainDataset());

		JRDataset[] datasets = report.getDatasets();
		if (datasets != null && datasets.length > 0)
		{
			for (int i = 0; i < datasets.length; i++)
			{
				JRExpressionCollector collector = getCollector(datasets[i]);
				collector.collect(datasets[i]);
			}
		}

		collect(report.getBackground());
		collect(report.getTitle());
		collect(report.getPageHeader());
		collect(report.getColumnHeader());
		collect(report.getDetailSection());
		collect(report.getColumnFooter());
		collect(report.getPageFooter());
		collect(report.getLastPageFooter());
		collect(report.getSummary());
		collect(report.getNoData());

		return getExpressions();
	}


	protected void collectTemplates()
	{
		JRReportTemplate[] templates = report.getTemplates();
		if (templates != null)
		{
			for (int i = 0; i < templates.length; i++)
			{
				JRReportTemplate template = templates[i];
				collect(template);
			}
		}
	}

	protected void collect(JRReportTemplate template)
	{
		addExpression(template.getSourceExpression());
	}

	/**
	 * Collects expressions used in a style definition.
	 * 
	 * @param style the style to collect expressions from
	 */
	public void collect(JRStyle style)
	{
		if (style != null && collectedStyles.add(style))
		{
			JRConditionalStyle[] conditionalStyles = style.getConditionalStyles();

			if (conditionalStyles != null && conditionalStyles.length > 0)
			{
				for (int i = 0; i < conditionalStyles.length; i++)
				{
					addExpression(conditionalStyles[i].getConditionExpression());
				}
			}

			collect(style.getStyle());
		}
	}


	/**
	 *
	 */
	private void collect(JRParameter[] parameters)
	{
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				addExpression(parameters[i].getDefaultValueExpression());
			}
		}
	}

	/**
	 *
	 */
	private void collect(JRVariable[] variables)
	{
		if (variables != null && variables.length > 0)
		{
			for(int i = 0; i < variables.length; i++)
			{
				JRVariable variable = variables[i];
				addExpression(variable.getExpression());
				addExpression(variable.getInitialValueExpression());
			}
		}
	}

	/**
	 *
	 */
	private void collect(JRGroup[] groups)
	{
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				JRGroup group = groups[i];
				addExpression(group.getExpression());

				collect(group.getGroupHeaderSection());
				collect(group.getGroupFooterSection());
			}
		}
	}

	/**
	 *
	 */
	private void collect(JRSection section)
	{
		if (section != null)
		{
			JRBand[] bands = section.getBands();
			if (bands != null && bands.length > 0)
			{
				for(int i = 0; i < bands.length; i++)
				{
					collect(bands[i]);
				}
			}
		}
	}

	/**
	 *
	 */
	private void collect(JRBand band)
	{
		if (band != null)
		{
			addExpression(band.getPrintWhenExpression());

			JRElement[] elements = band.getElements();
			if (elements != null && elements.length > 0)
			{
				for(int i = 0; i < elements.length; i++)
				{
					elements[i].collectExpressions(this);
				}
			}
		}
	}

	/**
	 *
	 */
	private void collectElement(JRElement element)
	{
		collect(element.getStyle());
		addExpression(element.getPrintWhenExpression());
		collectPropertyExpressions(element.getPropertyExpressions());
	}

	protected void collectPropertyExpressions(
			JRPropertyExpression[] propertyExpressions)
	{
		if (propertyExpressions != null && propertyExpressions.length > 0)
		{
			for (int i = 0; i < propertyExpressions.length; i++)
			{
				collectPropertyExpression(propertyExpressions[i]);
			}
		}
	}

	protected void collectPropertyExpression(
			JRPropertyExpression propertyExpression)
	{
		addExpression(propertyExpression.getValueExpression());
	}

	/**
	 *
	 */
	private void collectAnchor(JRAnchor anchor)
	{
		addExpression(anchor.getAnchorNameExpression());
	}


	public void collectHyperlink(JRHyperlink hyperlink)
	{
		if (hyperlink != null)
		{
			addExpression(hyperlink.getHyperlinkReferenceExpression());
			addExpression(hyperlink.getHyperlinkAnchorExpression());
			addExpression(hyperlink.getHyperlinkPageExpression());
			addExpression(hyperlink.getHyperlinkTooltipExpression());

			JRHyperlinkParameter[] hyperlinkParameters = hyperlink.getHyperlinkParameters();
			if (hyperlinkParameters != null)
			{
				for (int i = 0; i < hyperlinkParameters.length; i++)
				{
					JRHyperlinkParameter parameter = hyperlinkParameters[i];
					collectHyperlinkParameter(parameter);
				}
			}
		}
	}

	protected void collectHyperlinkParameter(JRHyperlinkParameter parameter)
	{
		if (parameter != null)
		{
			addExpression(parameter.getValueExpression());
		}
	}

	/**
	 *
	 */
	public void collect(JRBreak breakElement)
	{
		collectElement(breakElement);
	}

	/**
	 *
	 */
	public void collect(JRLine line)
	{
		collectElement(line);
	}

	/**
	 *
	 */
	public void collect(JRRectangle rectangle)
	{
		collectElement(rectangle);
	}

	/**
	 *
	 */
	public void collect(JREllipse ellipse)
	{
		collectElement(ellipse);
	}

	/**
	 *
	 */
	public void collect(JRImage image)
	{
		collectElement(image);
		addExpression(image.getExpression());
		collectAnchor(image);
		collectHyperlink(image);
	}

	/**
	 *
	 */
	public void collect(JRStaticText staticText)
	{
		collectElement(staticText);
	}

	/**
	 *
	 */
	public void collect(JRTextField textField)
	{
		collectElement(textField);
		addExpression(textField.getExpression());
		addExpression(textField.getPatternExpression());
		collectAnchor(textField);
		collectHyperlink(textField);
	}

	/**
	 *
	 */
	public void collect(JRSubreport subreport)
	{
		collectElement(subreport);
		addExpression(subreport.getParametersMapExpression());

		JRSubreportParameter[] parameters = subreport.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int j = 0; j < parameters.length; j++)
			{
				addExpression(parameters[j].getExpression());
			}
		}

		addExpression(subreport.getConnectionExpression());
		addExpression(subreport.getDataSourceExpression());
		addExpression(subreport.getExpression());
	}

	/**
	 *
	 */
	public void collect(JRChart chart)
	{
		collectElement(chart);
		collectAnchor(chart);
		collectHyperlink(chart);

		addExpression(chart.getTitleExpression());
		addExpression(chart.getSubtitleExpression());

		chart.getDataset().collectExpressions(this);
		chart.getPlot().collectExpressions(this);
	}

	/**
	 *
	 */
	public void collect(JRPieDataset pieDataset)
	{
		collect((JRElementDataset) pieDataset);

		JRPieSeries[] pieSeries = pieDataset.getSeries();
		if (pieSeries != null && pieSeries.length > 0)
		{
			JRExpressionCollector collector = getCollector(pieDataset);
			for(int j = 0; j < pieSeries.length; j++)
			{
				collector.collect(pieSeries[j]);
			}
		}

		JRExpressionCollector collector = getCollector(pieDataset);
		collector.addExpression(pieDataset.getOtherKeyExpression());
		collector.addExpression(pieDataset.getOtherLabelExpression());
		collector.collectHyperlink(pieDataset.getOtherSectionHyperlink());
	}

	/**
	 *
	 */
	public void collect(JRCategoryDataset categoryDataset)
	{
		collect((JRElementDataset) categoryDataset);

		JRCategorySeries[] categorySeries = categoryDataset.getSeries();
		if (categorySeries != null && categorySeries.length > 0)
		{
			JRExpressionCollector collector = getCollector(categoryDataset);
			for(int j = 0; j < categorySeries.length; j++)
			{
				collector.collect(categorySeries[j]);
			}
		}
	}

	/**
	 *
	 */
	public void collect(JRXyDataset xyDataset)
	{
		collect((JRElementDataset) xyDataset);

		JRXySeries[] xySeries = xyDataset.getSeries();
		if (xySeries != null && xySeries.length > 0)
		{
			JRExpressionCollector collector = getCollector(xyDataset);
			for(int j = 0; j < xySeries.length; j++)
			{
				collector.collect(xySeries[j]);
			}
		}
	}

	/**
	 *
	 */
	public void collect( JRTimeSeriesDataset timeSeriesDataset ){
		collect((JRElementDataset) timeSeriesDataset);

		JRTimeSeries[] timeSeries = timeSeriesDataset.getSeries();
		if( timeSeries != null && timeSeries.length > 0 ){
			JRExpressionCollector collector = getCollector(timeSeriesDataset);
			for( int i = 0; i <  timeSeries.length; i++ ){
				collector.collect(timeSeries[i]);
			}
		}
	}

	/**
	 *
	 */
	public void collect( JRTimePeriodDataset timePeriodDataset ){
		collect((JRElementDataset) timePeriodDataset);

		JRTimePeriodSeries[] timePeriodSeries = timePeriodDataset.getSeries();
		if( timePeriodSeries != null && timePeriodSeries.length > 0 ){
			JRExpressionCollector collector = getCollector(timePeriodDataset);
			for( int i = 0; i < timePeriodSeries.length; i++ ){
				collector.collect(timePeriodSeries[i]);
			}
		}
	}

	/**
	 *
	 */
	public void collect(JRGanttDataset ganttDataset)
	{
		collect((JRElementDataset) ganttDataset);
		
		JRGanttSeries[] ganttSeries = ganttDataset.getSeries();
		if (ganttSeries != null && ganttSeries.length > 0)
		{
			JRExpressionCollector collector = getCollector(ganttDataset);
			for(int j = 0; j < ganttSeries.length; j++)
			{
				collector.collect(ganttSeries[j]);
			}
		}
	}

	/**
	 *
	 */
	public void collect( JRValueDataset valueDataset ){
		collect((JRElementDataset) valueDataset);

		JRExpressionCollector collector = getCollector(valueDataset);
		collector.addExpression(valueDataset.getValueExpression());
	}

	/**
	 *
	 */
	private void collect(JRXySeries xySeries)
	{
		addExpression(xySeries.getSeriesExpression());
		addExpression(xySeries.getXValueExpression());
		addExpression(xySeries.getYValueExpression());
		addExpression(xySeries.getLabelExpression());

		collectHyperlink(xySeries.getItemHyperlink());
	}

	/**
	 *
	 */
	private void collect(JRPieSeries pieSeries)
	{
		addExpression(pieSeries.getKeyExpression());
		addExpression(pieSeries.getValueExpression());
		addExpression(pieSeries.getLabelExpression());

		collectHyperlink(pieSeries.getSectionHyperlink());
	}

	/**
	 *
	 */
	private void collect(JRCategorySeries categorySeries)
	{
		addExpression(categorySeries.getSeriesExpression());
		addExpression(categorySeries.getCategoryExpression());
		addExpression(categorySeries.getValueExpression());
		addExpression(categorySeries.getLabelExpression());

		collectHyperlink(categorySeries.getItemHyperlink());
	}

	/**
	 * 
	 */
	private void collect(JRGanttSeries ganttSeries)
	{
		addExpression(ganttSeries.getSeriesExpression());
		addExpression(ganttSeries.getTaskExpression());
		addExpression(ganttSeries.getSubtaskExpression());
		addExpression(ganttSeries.getStartDateExpression());
		addExpression(ganttSeries.getEndDateExpression());
		addExpression(ganttSeries.getPercentExpression());
		addExpression(ganttSeries.getLabelExpression());

		collectHyperlink(ganttSeries.getItemHyperlink());
	}

	/**
	 *
	 */
	public void collect(JRBarPlot barPlot)
	{
		addExpression(barPlot.getCategoryAxisLabelExpression());
		addExpression(barPlot.getValueAxisLabelExpression());
		addExpression(barPlot.getDomainAxisMinValueExpression());
		addExpression(barPlot.getDomainAxisMaxValueExpression());
		addExpression(barPlot.getRangeAxisMinValueExpression());
		addExpression(barPlot.getRangeAxisMaxValueExpression());
	}

	/**
	 *
	 */
	public void collect(JRBar3DPlot barPlot)
	{
		addExpression(barPlot.getCategoryAxisLabelExpression());
		addExpression(barPlot.getValueAxisLabelExpression());
		addExpression(barPlot.getRangeAxisMinValueExpression());
		addExpression(barPlot.getRangeAxisMaxValueExpression());
	}

	/**
	 *
	 */
	public void collect( JRLinePlot linePlot ){
		addExpression( linePlot.getCategoryAxisLabelExpression() );
		addExpression( linePlot.getValueAxisLabelExpression() );
		addExpression(linePlot.getDomainAxisMinValueExpression());
		addExpression(linePlot.getDomainAxisMaxValueExpression());
		addExpression(linePlot.getRangeAxisMinValueExpression());
		addExpression(linePlot.getRangeAxisMaxValueExpression());
	}

	/**
	 *
	 */
	public void collect( JRTimeSeriesPlot timeSeriesPlot ){
		addExpression( timeSeriesPlot.getTimeAxisLabelExpression() );
		addExpression( timeSeriesPlot.getValueAxisLabelExpression() );
		addExpression(timeSeriesPlot.getDomainAxisMinValueExpression());
		addExpression(timeSeriesPlot.getDomainAxisMaxValueExpression());
		addExpression(timeSeriesPlot.getRangeAxisMinValueExpression());
		addExpression(timeSeriesPlot.getRangeAxisMaxValueExpression());
	}

	/**
	 *
	 */
	public void collect( JRScatterPlot scatterPlot ){
		addExpression( scatterPlot.getXAxisLabelExpression() );
		addExpression( scatterPlot.getYAxisLabelExpression() );
		addExpression(scatterPlot.getDomainAxisMinValueExpression());
		addExpression(scatterPlot.getDomainAxisMaxValueExpression());
		addExpression(scatterPlot.getRangeAxisMinValueExpression());
		addExpression(scatterPlot.getRangeAxisMaxValueExpression());
	}

	/**
	 *
	 */
	public void collect( JRAreaPlot areaPlot ){
		addExpression( areaPlot.getCategoryAxisLabelExpression() );
		addExpression( areaPlot.getValueAxisLabelExpression() );
		addExpression(areaPlot.getDomainAxisMinValueExpression());
		addExpression(areaPlot.getDomainAxisMaxValueExpression());
		addExpression(areaPlot.getRangeAxisMinValueExpression());
		addExpression(areaPlot.getRangeAxisMaxValueExpression());
	}

	/**
	 *
	 */
	private void collect(JRTimeSeries timeSeries)
	{
		addExpression(timeSeries.getSeriesExpression());
		addExpression(timeSeries.getTimePeriodExpression());
		addExpression(timeSeries.getValueExpression());
		addExpression(timeSeries.getLabelExpression());

		collectHyperlink(timeSeries.getItemHyperlink());
	}

	/**
	 *
	 */
	private void collect(JRTimePeriodSeries timePeriodSeries ){
		addExpression(timePeriodSeries.getSeriesExpression());
		addExpression(timePeriodSeries.getStartDateExpression());
		addExpression(timePeriodSeries.getEndDateExpression());
		addExpression(timePeriodSeries.getValueExpression());
		addExpression(timePeriodSeries.getLabelExpression());
		collectHyperlink(timePeriodSeries.getItemHyperlink());
	}

	/**
	 *
	 */
	public void collect(JRXyzDataset xyzDataset) {
		collect((JRElementDataset) xyzDataset);

		JRXyzSeries[] xyzSeries = xyzDataset.getSeries();
		if (xyzSeries != null && xyzSeries.length > 0)
		{
			JRExpressionCollector collector = getCollector(xyzDataset);
			for(int j = 0; j < xyzSeries.length; j++)
			{
				collector.collect(xyzSeries[j]);
			}
		}

	}

	/**
	 *
	 */
	private void collect(JRXyzSeries xyzSeries) {
		addExpression(xyzSeries.getSeriesExpression());
		addExpression(xyzSeries.getXValueExpression());
		addExpression(xyzSeries.getYValueExpression());
		addExpression(xyzSeries.getZValueExpression());
		collectHyperlink(xyzSeries.getItemHyperlink());
	}

	/**
	 *
	 */
	public void collect(JRBubblePlot bubblePlot) {
		addExpression(bubblePlot.getXAxisLabelExpression());
		addExpression(bubblePlot.getYAxisLabelExpression());
		addExpression(bubblePlot.getDomainAxisMinValueExpression());
		addExpression(bubblePlot.getDomainAxisMaxValueExpression());
		addExpression(bubblePlot.getRangeAxisMinValueExpression());
		addExpression(bubblePlot.getRangeAxisMaxValueExpression());
	}

	/**
	 *
	 */
	public void collect(JRHighLowPlot highLowPlot)
	{
		addExpression(highLowPlot.getTimeAxisLabelExpression());
		addExpression(highLowPlot.getValueAxisLabelExpression());
		addExpression(highLowPlot.getDomainAxisMinValueExpression());
		addExpression(highLowPlot.getDomainAxisMaxValueExpression());
		addExpression(highLowPlot.getRangeAxisMinValueExpression());
		addExpression(highLowPlot.getRangeAxisMaxValueExpression());
	}

	/**
	 *
	 */
	public void collect(JRDataRange dataRange)
	{
		if (dataRange != null)
		{
			addExpression(dataRange.getLowExpression());
			addExpression(dataRange.getHighExpression());
		}
	}

	/**
	 *
	 */
	public void collect(JRMeterPlot meterPlot)
	{
		List<JRMeterInterval> intervals = meterPlot.getIntervals();
		if (intervals != null)
		{
			Iterator<JRMeterInterval> iter = intervals.iterator();
			while (iter.hasNext())
			{
				JRMeterInterval interval = iter.next();
				collect(interval.getDataRange());
			}
		}
		collect(meterPlot.getDataRange());
	}

	/**
	 *
	 */
	public void collect(JRThermometerPlot thermometerPlot)
	{
		collect(thermometerPlot.getDataRange());
		collect(thermometerPlot.getLowRange());
		collect(thermometerPlot.getMediumRange());
		collect(thermometerPlot.getHighRange());
	}


	/**
	 *
	 */
	public void collect(JRHighLowDataset highLowDataset)
	{
		collect((JRElementDataset) highLowDataset);

		JRExpressionCollector collector = getCollector(highLowDataset);
		collector.addExpression(highLowDataset.getSeriesExpression());
		collector.addExpression(highLowDataset.getDateExpression());
		collector.addExpression(highLowDataset.getHighExpression());
		collector.addExpression(highLowDataset.getLowExpression());
		collector.addExpression(highLowDataset.getOpenExpression());
		collector.addExpression(highLowDataset.getCloseExpression());
		collector.addExpression(highLowDataset.getVolumeExpression());

		collector.collectHyperlink(highLowDataset.getItemHyperlink());
	}

	/**
	 *
	 */
	public void collect(JRCandlestickPlot candlestickPlot)
	{
		addExpression(candlestickPlot.getTimeAxisLabelExpression());
		addExpression(candlestickPlot.getValueAxisLabelExpression());
		addExpression(candlestickPlot.getDomainAxisMinValueExpression());
		addExpression(candlestickPlot.getDomainAxisMaxValueExpression());
		addExpression(candlestickPlot.getRangeAxisMinValueExpression());
		addExpression(candlestickPlot.getRangeAxisMaxValueExpression());
	}


	/**
	 * Collects expressions from a crosstab.
	 *
	 * @param crosstab the crosstab
	 */
	public void collect(JRCrosstab crosstab)
	{
		collectElement(crosstab);

		createCrosstabId(crosstab);

		JRCrosstabDataset dataset = crosstab.getDataset();
		collect(dataset);

		JRExpressionCollector datasetCollector = getCollector(dataset);
		JRExpressionCollector crosstabCollector = getCollector(crosstab);

		crosstabCollector.collect(report.getDefaultStyle());

		addExpression(crosstab.getParametersMapExpression());

		JRCrosstabParameter[] parameters = crosstab.getParameters();
		if (parameters != null)
		{
			for (int i = 0; i < parameters.length; i++)
			{
				addExpression(parameters[i].getExpression());
			}
		}

		crosstabCollector.collect(crosstab.getHeaderCell());

		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		if (rowGroups != null)
		{
			for (int i = 0; i < rowGroups.length; i++)
			{
				JRCrosstabRowGroup rowGroup = rowGroups[i];
				JRCrosstabBucket bucket = rowGroup.getBucket();
				datasetCollector.addExpression(bucket.getExpression());
				
				crosstabCollector.pushContextObject(rowGroup);
				//order by expression is in the crosstab context
				crosstabCollector.addExpression(bucket.getOrderByExpression());
				addExpression(bucket.getComparatorExpression());
				crosstabCollector.collect(rowGroup.getHeader());
				crosstabCollector.collect(rowGroup.getTotalHeader());
				crosstabCollector.popContextObject();
			}
		}

		JRCrosstabColumnGroup[] colGroups = crosstab.getColumnGroups();
		if (colGroups != null)
		{
			for (int i = 0; i < colGroups.length; i++)
			{
				JRCrosstabColumnGroup columnGroup = colGroups[i];
				JRCrosstabBucket bucket = columnGroup.getBucket();
				datasetCollector.addExpression(bucket.getExpression());
				
				crosstabCollector.pushContextObject(columnGroup);
				//order by expression is in the crosstab context
				crosstabCollector.addExpression(bucket.getOrderByExpression());
				addExpression(bucket.getComparatorExpression());
				crosstabCollector.collect(columnGroup.getHeader());
				crosstabCollector.collect(columnGroup.getTotalHeader());
				crosstabCollector.popContextObject();
			}
		}

		JRCrosstabMeasure[] measures = crosstab.getMeasures();
		if (measures != null)
		{
			for (int i = 0; i < measures.length; i++)
			{
				datasetCollector.addExpression(measures[i].getValueExpression());
			}
		}

		crosstabCollector.collect(crosstab.getWhenNoDataCell());

		collectCrosstabCells(crosstab, crosstabCollector);
	}


	private void createCrosstabId(JRCrosstab crosstab)
	{
		crosstabIds.put(crosstab, Integer.valueOf(crosstabIds.size()));
	}


	private void collectCrosstabCells(JRCrosstab crosstab, JRExpressionCollector crosstabCollector)
	{
		if (crosstab instanceof JRDesignCrosstab)
		{
			List<JRCrosstabCell> cellsList = ((JRDesignCrosstab) crosstab).getCellsList();

			if (cellsList != null)
			{
				for (Iterator<JRCrosstabCell> iter = cellsList.iterator(); iter.hasNext();)
				{
					JRCrosstabCell cell = iter.next();
					crosstabCollector.collect(cell.getContents());
				}
			}
		}
		else
		{
			JRCrosstabCell[][] cells = crosstab.getCells();
			if (cells != null)
			{
				for (int i = 0; i < cells.length; ++i)
				{
					for (int j = 0; j < cells[i].length; j++)
					{
						if (cells[i][j] != null)
						{
							crosstabCollector.collect(cells[i][j].getContents());
						}
					}
				}
			}
		}
	}


	/**
	 * Collects expressions from a dataset.
	 *
	 * @param dataset the dataset
	 * @return collected expressions
	 */
	public Collection<JRExpression> collect(JRDataset dataset)
	{
		JRExpressionCollector collector = getCollector(dataset);
		collector.collect(dataset.getParameters());
		collector.collect(dataset.getVariables());
		collector.collect(dataset.getGroups());

		collector.addExpression(dataset.getFilterExpression());

		return getExpressions(dataset);
	}


	/**
	 * Collects expressions from an element dataset.
	 *
	 * @param dataset the element dataset
	 */
	public void collect(JRElementDataset dataset)
	{
		collect(dataset.getDatasetRun());

		JRExpression incrementWhenExpression = dataset.getIncrementWhenExpression();
		if (incrementWhenExpression != null)
		{
			JRExpressionCollector datasetCollector = getCollector(dataset);
			datasetCollector.addExpression(incrementWhenExpression);
		}
	}


	/**
	 * Collects expressions from a subdataset run object.
	 * 
	 * @param datasetRun the subdataset run
	 */
	public void collect(JRDatasetRun datasetRun)
	{
		if (datasetRun != null)
		{
			addExpression(datasetRun.getParametersMapExpression());
			addExpression(datasetRun.getConnectionExpression());
			addExpression(datasetRun.getDataSourceExpression());

			JRDatasetParameter[] parameters = datasetRun.getParameters();
			if (parameters != null && parameters.length > 0)
			{
				for (int i = 0; i < parameters.length; i++)
				{
					addExpression(parameters[i].getExpression());
				}
			}
		}
	}


	protected void collect(JRCellContents cell)
	{
		if (cell != null)
		{
			collect(cell.getStyle());
			JRElement[] elements = cell.getElements();
			if (elements != null && elements.length > 0)
			{
				for(int i = 0; i < elements.length; i++)
				{
					elements[i].collectExpressions(this);
				}
			}
		}
	}


	public void collect(JRFrame frame)
	{
		collectElement(frame);
		JRElement[] elements = frame.getElements();
		if (elements != null)
		{
			for (int i = 0; i < elements.length; i++)
			{
				elements[i].collectExpressions(this);
			}
		}
	}
	
	/**
	 * Collects expressions from a component element wrapper.
	 * 
	 * <p>
	 * Common element expressions are collected, and then the component
	 * compiler's
	 * {@link ComponentCompiler#collectExpressions(Component, JRExpressionCollector)}
	 * is called to collect component expressions.
	 * 
	 * @param componentElement the component element
	 */
	public void collect(JRComponentElement componentElement)
	{
		collectElement(componentElement);
		
		ComponentKey componentKey = componentElement.getComponentKey();
		ComponentManager manager = ComponentsEnvironment.getComponentManager(componentKey);
		Component component = componentElement.getComponent();
		manager.getComponentCompiler().collectExpressions(component, this);
	}
	
	/**
	 * Collects expressions from a generic element.
	 * 
	 * @param element the generic element
	 */
	public void collect(JRGenericElement element)
	{
		collectElement(element);
		
		JRGenericElementParameter[] parameters = element.getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			JRGenericElementParameter parameter = parameters[i];
			addExpression(parameter.getValueExpression());
		}
	}
}

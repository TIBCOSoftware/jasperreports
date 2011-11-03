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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabDataset;
import net.sf.jasperreports.crosstabs.JRCrosstabGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.fill.JRCrosstabExpressionEvaluator;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabCell;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabGroup;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabMeasure;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabObjectFactory;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabParameter;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition.Bucket;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketingService;
import net.sf.jasperreports.crosstabs.fill.calculation.CrosstabCell;
import net.sf.jasperreports.crosstabs.fill.calculation.HeaderCell;
import net.sf.jasperreports.crosstabs.fill.calculation.MeasureDefinition;
import net.sf.jasperreports.crosstabs.fill.calculation.MeasureDefinition.MeasureValue;
import net.sf.jasperreports.crosstabs.type.CrosstabColumnPositionEnum;
import net.sf.jasperreports.crosstabs.type.CrosstabPercentageEnum;
import net.sf.jasperreports.crosstabs.type.CrosstabRowPositionEnum;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyleResolver;

import org.jfree.data.general.Dataset;

/**
 * Fill-time implementation of a {@link net.sf.jasperreports.crosstabs.JRCrosstab crosstab}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillCrosstab extends JRFillElement implements JRCrosstab, JROriginProvider
{
	final protected JRCrosstab parentCrosstab;

	protected JRFillCrosstabDataset dataset;

	protected JRFillCrosstabRowGroup[] rowGroups;

	protected Map<String,Integer> rowGroupsMap;

	protected JRFillCrosstabColumnGroup[] columnGroups;

	protected Map<String,Integer> columnGroupsMap;

	protected JRFillCrosstabMeasure[] measures;

	protected BucketingService bucketingService;

	protected JRFillVariable[] variables;

	protected Map<String,JRFillVariable> variablesMap;
	
	protected JRFillVariable[][][] totalVariables;
	protected boolean[][] retrieveTotal;

	protected JRFillCrosstabParameter[] parameters;

	protected Map<String,JRFillParameter> parametersMap;
	
	protected boolean ignoreWidth;

	protected JRCrosstabExpressionEvaluator crosstabEvaluator;

	protected JRFillCrosstabCell[][] crossCells;
	protected JRFillCellContents headerCell;
	protected JRFillCellContents whenNoDataCell;

	protected boolean hasData;
	protected HeaderCell[][] columnHeadersData;
	protected HeaderCell[][] rowHeadersData;
	protected CrosstabCell[][] cellData;
	protected MeasureValue[] grandTotals;

	private boolean percentage;

	private CrosstabFiller crosstabFiller;
	private int overflowStartPage;
	
	private List<JRTemplatePrintFrame> printFrames;
	
	public JRFillCrosstab(JRBaseFiller filler, JRCrosstab crosstab, JRFillObjectFactory factory)
	{
		super(filler, crosstab, factory);

		parentCrosstab = crosstab;

		loadEvaluator(filler.getJasperReport());

		JRFillCrosstabObjectFactory crosstabFactory = new JRFillCrosstabObjectFactory(
				factory, crosstabEvaluator);
		crosstabFactory.setParentOriginProvider(this);
		
		headerCell = crosstabFactory.getCell(crosstab.getHeaderCell(), 
				JRCellContents.TYPE_CROSSTAB_HEADER);

		copyRowGroups(crosstab, crosstabFactory);
		copyColumnGroups(crosstab, crosstabFactory);
		
		copyMeasures(crosstab, crosstabFactory);
		copyCells(crosstab, crosstabFactory);
		whenNoDataCell = crosstabFactory.getCell(crosstab.getWhenNoDataCell(),
				JRCellContents.TYPE_NO_DATA_CELL);
		
		dataset = factory.getCrosstabDataset(crosstab.getDataset(), this);
		crosstabEvaluator.setFillDataset(dataset.getFillDataset());

		copyParameters(crosstab, factory);
		copyVariables(crosstab, crosstabFactory);
		
		crosstabFiller = new CrosstabFiller();
	}

	private boolean isIgnoreWidth(JRBaseFiller filler, JRCrosstab crosstab)
	{
		Boolean crosstabIgnoreWidth = crosstab.getIgnoreWidth();
		// crosstab attribute overrides all 
		if (crosstabIgnoreWidth != null)
		{
			return crosstabIgnoreWidth.booleanValue();
		}
		
		// report level property
		String reportProperty = filler.jasperReport.getPropertiesMap().getProperty(
				PROPERTY_IGNORE_WIDTH);
		if (reportProperty != null)
		{
			return JRProperties.asBoolean(reportProperty);
		}
		
		// report pagination parameter from the master filler
		Boolean ignorePaginationParam = (Boolean) filler.getMasterFiller().getParameterValue(
				JRParameter.IS_IGNORE_PAGINATION);
		if (ignorePaginationParam != null && ignorePaginationParam.booleanValue())
		{
			return ignorePaginationParam.booleanValue();
		}
		
		// global property
		return JRProperties.getBooleanProperty(PROPERTY_IGNORE_WIDTH);
	}

	/**
	 *
	 */
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getMode(this, ModeEnum.TRANSPARENT);
	}

	private void copyRowGroups(JRCrosstab crosstab, JRFillCrosstabObjectFactory factory)
	{
		JRCrosstabRowGroup[] groups = crosstab.getRowGroups();
		rowGroups = new JRFillCrosstabRowGroup[groups.length];
		rowGroupsMap = new HashMap<String,Integer>();
		for (int i = 0; i < groups.length; ++i)
		{
			JRFillCrosstabRowGroup group = factory.getCrosstabRowGroup(groups[i]);
			group.getFillHeader().setVerticalPositionType(groups[i].getPositionValue());

			rowGroups[i] = group;
			rowGroupsMap.put(group.getName(), Integer.valueOf(i));
		}
	}

	private void copyColumnGroups(JRCrosstab crosstab, JRFillCrosstabObjectFactory factory)
	{
		JRCrosstabColumnGroup[] groups = crosstab.getColumnGroups();
		columnGroups = new JRFillCrosstabColumnGroup[groups.length];
		columnGroupsMap = new HashMap<String,Integer>();
		for (int i = 0; i < groups.length; ++i)
		{
			JRFillCrosstabColumnGroup group = factory.getCrosstabColumnGroup(groups[i]);
			columnGroups[i] = group;
			columnGroupsMap.put(group.getName(), Integer.valueOf(i));
		}
	}

	private void copyMeasures(JRCrosstab crosstab, JRFillCrosstabObjectFactory factory)
	{
		JRCrosstabMeasure[] crossMeasures = crosstab.getMeasures();
		measures = new JRFillCrosstabMeasure[crossMeasures.length];
		for (int i = 0; i < crossMeasures.length; i++)
		{
			measures[i] = factory.getCrosstabMeasure(crossMeasures[i]);
		}
	}

	private void copyParameters(JRCrosstab crosstab, JRFillObjectFactory factory)
	{
		JRCrosstabParameter[] crossParams = crosstab.getParameters();
		parameters = new JRFillCrosstabParameter[crossParams.length];
		parametersMap = new HashMap<String,JRFillParameter>();
		for (int i = 0; i < crossParams.length; i++)
		{
			parameters[i] = factory.getCrosstabParameter(crossParams[i]);
			parametersMap.put(parameters[i].getName(), parameters[i]);
		}
	}

	private void copyCells(JRCrosstab crosstab, JRFillCrosstabObjectFactory factory)
	{
		JRCrosstabCell[][] crosstabCells = crosstab.getCells();		
		crossCells = new JRFillCrosstabCell[rowGroups.length + 1][columnGroups.length + 1];
		for (int i = 0; i <= rowGroups.length; ++i)
		{
			for (int j = 0; j <= columnGroups.length; ++j)
			{
				if (crosstabCells[i][j] != null)
				{
					crossCells[i][j] = factory.getCrosstabCell(crosstabCells[i][j]);
				}
			}
		}
	}

	private void copyVariables(JRCrosstab crosstab, JRFillObjectFactory factory)
	{
		JRVariable[] vars = crosstab.getVariables();
		variables = new JRFillVariable[vars.length];
		variablesMap = new HashMap<String,JRFillVariable>();
		for (int i = 0; i < variables.length; i++)
		{
			variables[i] = factory.getVariable(vars[i]);
			variablesMap.put(variables[i].getName(), variables[i]);
		}
		
		Map<String,int[]> totalVarPos = new HashMap<String,int[]>();
		totalVariables = new JRFillVariable[rowGroups.length + 1][columnGroups.length + 1][measures.length];
		for (int row = 0; row <= rowGroups.length; ++row)
		{
			JRCrosstabRowGroup rowGroup = row == rowGroups.length ? null : rowGroups[row];
			for (int col = 0; col <= columnGroups.length; ++col)
			{
				JRCrosstabColumnGroup colGroup = col == columnGroups.length ? null : columnGroups[col];
				
				if (row < rowGroups.length || col < columnGroups.length)
				{
					for (int m = 0; m < measures.length; m++)
					{
						String totalVariableName = JRDesignCrosstab.getTotalVariableName(measures[m], rowGroup, colGroup);
						totalVariables[row][col][m] = variablesMap.get(totalVariableName);
						totalVarPos.put(totalVariableName, new int[]{row, col});
					}
				}
			}
		}
		
		Set<String> measureVars = new HashSet<String>();
		for (JRFillCrosstabMeasure measure : measures)
		{
			measureVars.add(measure.getFillVariable().getName());
		}

		retrieveTotal = new boolean[rowGroups.length + 1][columnGroups.length + 1];
		
		//FIXME avoid this
		JRExpressionCollector collector = JRExpressionCollector.collector(filler.getJasperReport(), crosstab);
		List<JRExpression> expressions = collector.getExpressions(crosstab);
		for (Iterator<JRExpression> iter = expressions.iterator(); iter.hasNext();)
		{
			JRExpression expression = iter.next();
			Object expressionContext = collector.getExpressionContext(expression);
			boolean groupHeaderExpression = expressionContext instanceof JRCrosstabGroup;
			JRExpressionChunk[] chunks = expression.getChunks();
			if (chunks != null)
			{
				for (int i = 0; i < chunks.length; i++)
				{
					JRExpressionChunk chunk = chunks[i];
					if (chunk.getType() == JRExpressionChunk.TYPE_VARIABLE)
					{
						String varName = chunk.getText();
						int[] pos = totalVarPos.get(varName);
						if (pos != null)
						{
							retrieveTotal[pos[0]][pos[1]] = true;
						}
						
						// if a measure variable is used inside a group header, compute all totals.
						// in theory we could have a finer grained rule here, but it complicates
						// the logic without a singnificant gain.
						if (groupHeaderExpression && (pos != null || measureVars.contains(varName)))
						{
							retrieveTotal[0][0] = true;
						}
					}
				}
			}
		}
	}

	protected void loadEvaluator(JasperReport jasperReport)
	{
		try
		{
			JREvaluator evaluator = JasperCompileManager.loadEvaluator(jasperReport, parentCrosstab);
			crosstabEvaluator = new JRCrosstabExpressionEvaluator(evaluator);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException("Could not load evaluator for crosstab.", e);
		}
	}

	private BucketingService createService(byte evaluation) throws JRException
	{
		boolean hasOrderByExpression = false;
		List<BucketDefinition> rowBuckets = new ArrayList<BucketDefinition>(rowGroups.length);
		for (int i = 0; i < rowGroups.length; ++i)
		{
			JRFillCrosstabRowGroup group = rowGroups[i];
			rowBuckets.add(createServiceBucket(group, evaluation));
			hasOrderByExpression |= group.getBucket().getOrderByExpression() != null;
		}

		List<BucketDefinition> colBuckets = new ArrayList<BucketDefinition>(columnGroups.length);
		for (int i = 0; i < columnGroups.length; ++i)
		{
			JRFillCrosstabColumnGroup group = columnGroups[i];
			colBuckets.add(createServiceBucket(group, evaluation));
			hasOrderByExpression |= group.getBucket().getOrderByExpression() != null;
		}

		percentage = false;
		List<MeasureDefinition> measureList = new ArrayList<MeasureDefinition>(measures.length);
		for (int i = 0; i < measures.length; ++i)
		{
			measureList.add(createServiceMeasure(measures[i]));
			percentage |= measures[i].getPercentageType() == CrosstabPercentageEnum.GRAND_TOTAL;
		}

		// if a group has order by expression, compute totals as they might be used
		// in the expression
		// TODO refine this
		if (percentage || hasOrderByExpression)
		{
			rowBuckets.get(0).setComputeTotal();
			colBuckets.get(0).setComputeTotal();
		}
		
		return new BucketingService(this, rowBuckets, colBuckets, measureList, dataset.isDataPreSorted(), retrieveTotal);
	}

	private BucketDefinition createServiceBucket(JRCrosstabGroup group, byte evaluation) throws JRException
	{
		JRCrosstabBucket bucket = group.getBucket();

		Comparator<Object> comparator = null;
		JRExpression comparatorExpression = bucket.getComparatorExpression();
		if (comparatorExpression != null)
		{
			comparator = (Comparator<Object>) evaluateExpression(comparatorExpression, evaluation);
		}

		return new BucketDefinition(bucket.getValueClass(),
				bucket.getOrderByExpression(), comparator, bucket.getOrderValue(), 
				group.getTotalPositionValue());
	}

	private MeasureDefinition createServiceMeasure(JRFillCrosstabMeasure measure)
	{
		return new MeasureDefinition(
				measure.getValueClass(), 
				measure.getCalculationValue(), 
				measure.getIncrementerFactory()); 
	}

	public Object evaluateExpression(JRExpression expression, MeasureValue[] measureValues)
			throws JRException
	{
		for (int i = 0; i < measures.length; i++)
		{
			Object value = measureValues[i].getValue();
			measures[i].getFillVariable().setValue(value);
		}
		
		return crosstabEvaluator.evaluate(expression, JRExpression.EVALUATION_DEFAULT);
	}
	
	protected void reset()
	{
		super.reset();

		for (int i = 0; i < variables.length; i++)
		{
			variables[i].setValue(null);
			variables[i].setInitialized(true);
		}
		
		printFrames = null;
	}

	protected void evaluate(byte evaluation) throws JRException
	{
		reset();

		evaluatePrintWhenExpression(evaluation);
		evaluateProperties(evaluation);

		if (isPrintWhenExpressionNull() || isPrintWhenTrue())
		{
			dataset.evaluateDatasetRun(evaluation);

			initEvaluator(evaluation);

			bucketingService.processData();

			hasData = bucketingService.hasData();
			
			if (hasData)
			{
				columnHeadersData = bucketingService.getColumnHeaders();
				rowHeadersData = bucketingService.getRowHeaders();
				cellData = bucketingService.getCrosstabCells();
				if (percentage)
				{
					grandTotals = bucketingService.getGrandTotals();
				}

				crosstabFiller.initCrosstab();
			}
			
			overflowStartPage = 0;

			ignoreWidth = isIgnoreWidth(filler, parentCrosstab);
		}
	}

	protected void initEvaluator(byte evaluation) throws JRException
	{
		Map<String,Object> parameterValues = 
			JRFillSubreport.getParameterValues(
				filler, 
				getParametersMapExpression(), 
				getParameters(), 
				evaluation, 
				true, 
				false,//hasResourceBundle
				false//hasFormatFactory
				);
		
		ResourceBundle resBdl = (ResourceBundle) parameterValues.get(JRParameter.REPORT_RESOURCE_BUNDLE);
		if (resBdl == null)
		{
			JRFillParameter resourceBundleParam = filler.getParametersMap().get(JRParameter.REPORT_RESOURCE_BUNDLE);
			parameterValues.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundleParam.getValue());
		}
		
		parameterValues.put(JRParameter.REPORT_PARAMETERS_MAP, parameterValues);

		for (int i = 0; i < parameters.length; i++)
		{
			Object value = parameterValues.get(parameters[i].getName());
			parameters[i].setValue(value);
		}

		crosstabEvaluator.init(parametersMap, variablesMap, filler.getWhenResourceMissingType());
	}

	protected void initBucketingService()
	{
		if (bucketingService == null)
		{
			try
			{
				bucketingService = createService(JRExpression.EVALUATION_DEFAULT);
			}
			catch (JRException e)
			{
				throw new JRRuntimeException("Could not create bucketing service", e);
			}
		}
		else
		{
			bucketingService.clear();
		}
	}

	protected boolean prepare(int availableHeight, boolean isOverflow) throws JRException
	{
		super.prepare(availableHeight, isOverflow);

		if (!isToPrint())
		{
			return false;
		}

		if (availableHeight < getRelativeY() + getHeight())
		{
			setToPrint(false);
			return true;
		}

		if (isOverflow && crosstabFiller.ended() && isAlreadyPrinted())
		{
			if (isPrintWhenDetailOverflows())
			{
				rewind();
				setReprinted(true);
			}
			else
			{
				setStretchHeight(getHeight());
				setToPrint(false);

				return false;
			}
		}

		if (isOverflow && isPrintWhenDetailOverflows())
		{
			setReprinted(true);
		}

		printFrames = new ArrayList<JRTemplatePrintFrame>();
		crosstabFiller.fill(availableHeight - getRelativeY());

		if (!printFrames.isEmpty())
		{
			// crosstab content has been filled, reset overflowPage
			overflowStartPage = 0;
		}
		else
		{
			int pageCount = filler.getCurrentPageCount();
			if (overflowStartPage == 0)
			{
				// first empty page
				overflowStartPage = pageCount;
			}
			else if (pageCount >= overflowStartPage + 2)
			{
				throw new JRRuntimeException("Crosstab has not printed anything on 3 consecutive pages, "
						+ "likely infinite loop");
			}
		}
		
		boolean willOverflow = crosstabFiller.willOverflow();
		setStretchHeight(willOverflow ? availableHeight - getRelativeY() : crosstabFiller.getUsedHeight());
		
		return willOverflow;
	}

	protected void addCrosstabChunk(List<JRPrintElement> elements, int yOffset)
	{
		JRTemplatePrintFrame printFrame = new JRTemplatePrintFrame(getTemplateFrame(), elementId);
		printFrame.setX(0);
		printFrame.setY(yOffset);
		
		Collections.sort(elements, new JRYXComparator());//FIXME make singleton comparator; same for older comparator

		int xLimit = Integer.MIN_VALUE;
		int yLimit = Integer.MIN_VALUE;
		for (Iterator<JRPrintElement> it = elements.iterator(); it.hasNext();)
		{
			JRPrintElement element = it.next();
			if (element.getX() + element.getWidth() > xLimit)
			{
				xLimit = element.getX() + element.getWidth();
			}
			if (element.getY() + element.getHeight() > yLimit)
			{
				yLimit = element.getY() + element.getHeight();
			}
		}
		
		printFrame.setWidth(xLimit);
		if (getRunDirectionValue() == RunDirectionEnum.RTL)
		{
			// if RTL filling, move the frame to the left
			printFrame.setX(getWidth() - xLimit);
		}
		printFrame.setHeight(yLimit);
		
		if (getRunDirectionValue() == RunDirectionEnum.RTL)
		{
			mirrorPrintElements(elements, xLimit);
		}
		
		// dump all elements into the print frame
		printFrame.addElements(elements);
		
		// add this frame to the list to the list of crosstab chunks
		printFrames.add(printFrame);
	}
	
	protected JRPrintElement fill()
	{
		// don't return anything, see getPrintElements()
		return null;
	}

	protected JRTemplateFrame getTemplateFrame()
	{
		return (JRTemplateFrame) getElementTemplate();
	}

	protected JRTemplateElement createElementTemplate()
	{
		JRTemplateFrame template = new JRTemplateFrame(getElementOrigin(), 
				filler.getJasperPrint().getDefaultStyleProvider());
		template.setElement(this);
		template.copyBox(getLineBox());
		return template;
	}

	protected void rewind()
	{
		crosstabFiller.initCrosstab();
		
		overflowStartPage = 0;
	}

	protected List<? extends JRPrintElement> getPrintElements()
	{
		return printFrames;
	}

	protected void mirrorPrintElements(List<JRPrintElement> printElements, int width)
	{
		for (Iterator<JRPrintElement> it = printElements.iterator(); it.hasNext();)
		{
			JRPrintElement element = it.next();
			int mirrorX = width - element.getX() - element.getWidth();
			element.setX(mirrorX);
		}
	}

	protected void resolveElement(JRPrintElement element, byte evaluation)
	{
		// nothing
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void visit(JRVisitor visitor)
	{
		visitor.visitCrosstab(this);
	}

	public int getId()
	{
		return parentCrosstab.getId();
	}

	public JRCrosstabDataset getDataset()
	{
		return dataset;
	}

	public JRCrosstabRowGroup[] getRowGroups()
	{
		return rowGroups;
	}

	public JRCrosstabColumnGroup[] getColumnGroups()
	{
		return columnGroups;
	}

	public JRCrosstabMeasure[] getMeasures()
	{
		return measures;
	}

	
	/**
	 * Fill-time crosstab input dataset implementation.
	 *  
	 * @author Lucian Chirita (lucianc@users.sourceforge.net)
	 */
	public class JRFillCrosstabDataset extends JRFillElementDataset implements JRCrosstabDataset
	{
		private Object[] bucketValues;

		private Object[] measureValues;

		public JRFillCrosstabDataset(JRCrosstabDataset dataset, JRFillObjectFactory factory)
		{
			super(dataset, factory);

			this.bucketValues = new Object[rowGroups.length + columnGroups.length];
			this.measureValues = new Object[measures.length];
		}

		protected void customInitialize()
		{
			initBucketingService();
		}

		protected void customEvaluate(JRCalculator calculator) throws JRExpressionEvalException
		{
			for (int i = 0; i < rowGroups.length; i++)
			{
				bucketValues[i] = calculator.evaluate(rowGroups[i].getBucket().getExpression());
			}

			for (int i = 0; i < columnGroups.length; ++i)
			{
				bucketValues[i + rowGroups.length] = calculator.evaluate(columnGroups[i].getBucket().getExpression());
			}

			for (int i = 0; i < measures.length; i++)
			{
				measureValues[i] = calculator.evaluate(measures[i].getValueExpression());
			}
		}

		protected void customIncrement()
		{
			try
			{
				bucketingService.addData(bucketValues, measureValues);
			}
			catch (JRException e)
			{
				throw new JRRuntimeException("Error incrementing crosstab dataset", e);
			}
		}

		protected Dataset getCustomDataset()
		{
			return null;
		}

		public void collectExpressions(JRExpressionCollector collector)
		{
		}

		public boolean isDataPreSorted()
		{
			return ((JRCrosstabDataset) parent).isDataPreSorted();
		}
	}
	
	/**
	 * Crosstab filler class.
	 *  
	 * @author Lucian Chirita (lucianc@users.sourceforge.net)
	 */
	protected class CrosstabFiller
	{
		private int yOffset;
		private int yChunkOffset;
		private boolean willOverflow;

		private int[] rowHeadersXOffsets;
		
		private boolean[] columnBreakable;
		private boolean[] rowBreakable;
		private int[] columnCount;
		private int[] rowCount;
		private int[] columnXOffsets;
		
		private boolean noDataCellPrinted;
		
		private int startRowIndex;
		private int startColumnIndex;
		private int lastColumnIndex;
		private List<HeaderCell[]> columnHeaders;

		private List<List<JRPrintElement>> printRows;

		private HeaderCell[] spanHeaders;
		private int[] spanHeadersStart;

		private List<Integer> rowYs = new ArrayList<Integer>();
		private int rowIdx;
		
		private List<JRFillCellContents> preparedRow = new ArrayList<JRFillCellContents>();
		private int preparedRowHeight;
		
		private boolean printRowHeaders;
		private boolean printColumnHeaders;
		
		private JRFillVariable rowCountVar;
		private JRFillVariable colCountVar;

		protected CrosstabFiller()
		{
			setRowHeadersXOffsets();

			printRows = new ArrayList<List<JRPrintElement>>();
			
			rowCountVar = (JRFillVariable) variablesMap.get(JRCrosstab.VARIABLE_ROW_COUNT);
			colCountVar = (JRFillVariable) variablesMap.get(JRCrosstab.VARIABLE_COLUMN_COUNT);
		}
		
		protected void initCrosstab()
		{
			columnXOffsets = computeOffsets(columnHeadersData, columnGroups, true);
			columnBreakable = computeBreakableHeaders(columnHeadersData, columnGroups, columnXOffsets, true, true);
			columnCount = computeCounts(columnHeadersData);
			
			int[] rowYOffsets = computeOffsets(rowHeadersData, rowGroups, false);
			rowBreakable = computeBreakableHeaders(rowHeadersData, rowGroups, rowYOffsets, false, false);
			rowCount = computeCounts(rowHeadersData);
			
			spanHeaders = new HeaderCell[rowGroups.length - 1];
			spanHeadersStart = new int[rowGroups.length - 1];
			
			startRowIndex = 0;
			startColumnIndex = 0;
			lastColumnIndex = 0;
			noDataCellPrinted = false;
		}

		protected void setRowHeadersXOffsets()
		{
			rowHeadersXOffsets = new int[rowGroups.length + 1];
			rowHeadersXOffsets[0] = 0;
			for (int i = 0; i < rowGroups.length; i++)
			{
				rowHeadersXOffsets[i + 1] = rowHeadersXOffsets[i] + rowGroups[i].getWidth();
			}
		}

		protected int[] computeOffsets(HeaderCell[][] headersData, JRFillCrosstabGroup[] groups, boolean width)
		{
			int[] offsets = new int[headersData[0].length + 1];
			offsets[0] = 0;
			for (int i = 0; i < headersData[0].length; i++)
			{
				int size = 0;
				for (int j = groups.length - 1; j >= 0; --j)
				{
					if (headersData[j][i] != null)
					{
						JRFillCellContents cell = headersData[j][i].isTotal() ? groups[j].getFillTotalHeader() : groups[j].getFillHeader();
						size = cell == null ? 0 : (width ? cell.getWidth() : cell.getHeight());
						break;
					}
				}

				offsets[i + 1] = offsets[i] + size;
			}

			return offsets;
		}

		protected boolean[] computeBreakableHeaders(HeaderCell[][] headersData, JRFillCrosstabGroup[] groups, int[] offsets, boolean width, boolean startHeaders)
		{
			boolean[] breakable = new boolean[headersData[0].length];
			for (int i = 0; i < breakable.length; i++)
			{
				breakable[i] = true;
			}

			for (int j = 0; j < groups.length; ++j)
			{
				JRFillCellContents fillHeader = groups[j].getFillHeader();
				
				if (fillHeader != null)
				{
					int size = width ? fillHeader.getWidth() : fillHeader.getHeight();
					
					for (int i = 0; i < headersData[0].length; i++)
					{
						HeaderCell header = headersData[j][i];
						if (header != null && !header.isTotal() && header.getLevelSpan() > 1)
						{
							int span = header.getLevelSpan();
							
							if (startHeaders)
							{
								for (int k = i + 1; k < i + span && offsets[k] - offsets[i] < size; ++k)
								{
									breakable[k] = false;
								}
							}
							
							for (int k = i + span - 1; k > i && offsets[i + span] - offsets[k] < size; --k)
							{
								breakable[k] = false;
							}
						}
					}
				}
			}

			return breakable;
		}

		private int[] computeCounts(HeaderCell[][] headersData)
		{
			int[] counts = new int[headersData[0].length];
			
			HeaderCell[] lastHeaders = headersData[headersData.length - 1];
			for (int i = 0, c = 0; i < counts.length; ++i)
			{
				HeaderCell lastHeader = lastHeaders[i];
				if (lastHeader != null && !lastHeader.isTotal())
				{
					++c;
				}
				
				counts[i] = c;
			}
			
			return counts;
		}

		protected void fill(int availableHeight) throws JRException
		{
			printRows.clear();

			yOffset = 0;
			yChunkOffset = 0;
			willOverflow = false;
			
			fillVerticalCrosstab(availableHeight);
		}
		
		protected boolean willOverflow()
		{
			return willOverflow;
		}
		
		protected int getUsedHeight()
		{
			return yChunkOffset + yOffset;
		}
		
		protected boolean ended()
		{
			return hasData ? (startRowIndex >= rowHeadersData[0].length && startColumnIndex >= columnHeadersData[0].length) : noDataCellPrinted;
		}
		
		protected void fillVerticalCrosstab(int availableHeight) throws JRException
		{
			if (!hasData)
			{
				fillNoDataCell(availableHeight);			
				if (!printRows.isEmpty())
				{
					addFilledRows();
				}
				return;
			}
			
			printRowHeaders = startColumnIndex == 0 || isRepeatRowHeaders();
			int rowHeadersXOffset = printRowHeaders ? rowHeadersXOffsets[rowGroups.length] : 0;

			if (startColumnIndex == lastColumnIndex)
			{
				int availableWidth = getWidth();

				columnHeaders = getGroupHeaders(availableWidth - rowHeadersXOffset, columnXOffsets, columnBreakable, startColumnIndex, columnHeadersData, columnGroups);
				lastColumnIndex = startColumnIndex + columnHeaders.size();

				if (startColumnIndex == lastColumnIndex)
				{
					throw new JRRuntimeException("Not enough space to render the crosstab.");
				}
			}
			
			printColumnHeaders = startRowIndex == 0 || isRepeatColumnHeaders();
			List<List<JRPrintElement>> columnHeaderRows = null;
			if (printColumnHeaders)
			{
				columnHeaderRows = fillColumnHeaders(rowHeadersXOffset, availableHeight - yOffset);
				if (willOverflow)
				{
					return;
				}
			}

			int lastRowIndex = fillRows(rowHeadersXOffset, availableHeight - yOffset);

			if (lastRowIndex == startRowIndex)
			{
				willOverflow = true;
				return;
			}

			if (columnHeaderRows != null)
			{
				printRows.addAll(columnHeaderRows);
			}
			
			if (!printRows.isEmpty())
			{
				addFilledRows();
			}
			
			if (lastRowIndex >= rowHeadersData[0].length)
			{
				startColumnIndex = lastColumnIndex;

				if (startColumnIndex < columnHeadersData[0].length)
				{
					startRowIndex = lastRowIndex = 0;

					// set the chunk offset and compute the remaining height
					int yAdvance = yOffset + getColumnBreakOffset();
					yChunkOffset += yAdvance;
					int remainingHeight = availableHeight - yAdvance;
					
					// reset the elements offset
					yOffset = 0;
					
					// fill a new chunk
					fillVerticalCrosstab(remainingHeight);
					return;
				}
			}

			boolean fillEnded = lastRowIndex >= rowHeadersData[0].length && lastColumnIndex >= columnHeadersData[0].length;
			if (fillEnded)
			{
				setStretchHeight(yOffset);
			}
			else
			{
				setStretchHeight(availableHeight);
			}

			startRowIndex = lastRowIndex;

			willOverflow = !fillEnded;
		}

		protected void addFilledRows()
		{
			List<JRPrintElement> prints = new ArrayList<JRPrintElement>();
			for (Iterator<List<JRPrintElement>> it = printRows.iterator(); it.hasNext();)
			{
				List<JRPrintElement> rowPrints = it.next();
				prints.addAll(rowPrints);
			}
			
			// add the crosstan chunk to the element
			addCrosstabChunk(prints, yChunkOffset);
			
			// clear the added rows
			printRows.clear();
		}
		
		protected List<HeaderCell[]> getGroupHeaders(
				int available, 
				int[] offsets, 
				boolean[] breakable, 
				int firstIndex, 
				HeaderCell[][] headersData, 
				JRFillCrosstabGroup[] groups
				)
		{
			List<HeaderCell[]> headers = new ArrayList<HeaderCell[]>();

			int maxOffset = available + offsets[firstIndex];
			int lastIndex;
			for (lastIndex = firstIndex; 
				lastIndex < headersData[0].length 
					&& (ignoreWidth || offsets[lastIndex + 1] <= maxOffset); 
				++lastIndex)
			{
				HeaderCell[] groupHeaders = new HeaderCell[groups.length];

				for (int j = 0; j < groups.length; ++j)
				{
					groupHeaders[j] = headersData[j][lastIndex];
				}

				headers.add(groupHeaders);
			}

			
			if (lastIndex < headersData[0].length)
			{
				while(lastIndex > firstIndex && !breakable[lastIndex])
				{
					--lastIndex;
					headers.remove(headers.size() - 1);
				}
			}
			
			if (lastIndex > firstIndex)
			{
				if (firstIndex > 0)
				{
					HeaderCell[] firstHeaders = headers.get(0);

					for (int j = 0; j < groups.length; ++j)
					{
						HeaderCell header = headersData[j][firstIndex];

						if (header == null)
						{
							int spanIndex = getSpanIndex(firstIndex, j, headersData);
							if (spanIndex >= 0)
							{
								HeaderCell spanCell = headersData[j][spanIndex];
								int headerEndIdx = spanCell.getLevelSpan() + spanIndex;
								if (headerEndIdx > lastIndex)
								{
									headerEndIdx = lastIndex;
								}
								firstHeaders[j] = HeaderCell.createLevelSpanCopy(spanCell, headerEndIdx - firstIndex);
							}
						}
					}
				}

				if (lastIndex < headersData[0].length)
				{
					for (int j = 0; j < groups.length; ++j)
					{
						HeaderCell header = headersData[j][lastIndex];

						if (header == null)
						{
							int spanIndex = getSpanIndex(lastIndex, j, headersData);
							if (spanIndex >= firstIndex)
							{
								HeaderCell spanCell = headersData[j][spanIndex];
								HeaderCell[] headerCells = headers.get(spanIndex - firstIndex);
								headerCells[j] = HeaderCell.createLevelSpanCopy(spanCell, lastIndex - spanIndex);
							}
						}
					}
				}
			}

			return headers;
		}

		
		protected int getSpanIndex(int i, int j, HeaderCell[][] headersData)
		{
			int spanIndex = i - 1;
			while (spanIndex >= 0 && headersData[j][spanIndex] == null)
			{
				--spanIndex;
			}

			if (spanIndex >= 0)
			{
				HeaderCell spanCell = headersData[j][spanIndex];
				int span = spanCell.getLevelSpan();

				if (span > i - spanIndex)
				{
					return spanIndex;
				}
			}

			return -1;
		}
		
		
		protected void fillNoDataCell(int availableHeight) throws JRException
		{
			if (whenNoDataCell == null)
			{
				noDataCellPrinted = true;
			}
			else
			{
				if (availableHeight < whenNoDataCell.getHeight())
				{
					willOverflow = true;
				}
				else
				{
					whenNoDataCell.evaluate(JRExpression.EVALUATION_DEFAULT);
					whenNoDataCell.prepare(availableHeight);
					
					willOverflow = whenNoDataCell.willOverflow();
					
					if (!willOverflow)
					{
						whenNoDataCell.setX(0);
						whenNoDataCell.setY(0);
						
						JRPrintFrame printCell = whenNoDataCell.fill();
						List<JRPrintElement> noDataRow = new ArrayList<JRPrintElement>(1);
						noDataRow.add(printCell);
						addPrintRow(noDataRow);
						
						yOffset += whenNoDataCell.getPrintHeight();
						noDataCellPrinted = true;
					}
				}
			}
		}
		

		protected List<List<JRPrintElement>> fillColumnHeaders(int rowHeadersXOffset, int availableHeight) throws JRException
		{
			JRFillCellContents[][] columnHeaderRows = new JRFillCellContents[columnGroups.length][lastColumnIndex - startColumnIndex + 1];
			
			rowYs.clear();
			rowYs.add(Integer.valueOf(0));
			
			if (printRowHeaders && headerCell != null)
			{
				JRFillCellContents contents = fillHeader(availableHeight);

				if (willOverflow)
				{
					return null;
				}

				columnHeaderRows[columnGroups.length - 1][0] = contents;
			}
			
			rows:
			for (rowIdx = 0; rowIdx < columnGroups.length; rowIdx++)
			{
				for (int columnIdx = startColumnIndex; columnIdx < lastColumnIndex; ++columnIdx)
				{
					HeaderCell[] headers = columnHeaders.get(columnIdx - startColumnIndex);
					HeaderCell cell = headers[rowIdx];
					
					if (cell != null)
					{
						JRFillCellContents contents = prepareColumnHeader(cell, columnIdx, rowHeadersXOffset, availableHeight);
						columnHeaderRows[rowIdx + cell.getDepthSpan() - 1][columnIdx - startColumnIndex + 1] = contents;
						
						if (willOverflow)
						{
							break rows;
						}
					}
				}

				int rowStretchHeight = stretchColumnHeadersRow(columnHeaderRows[rowIdx]);
				rowYs.add(Integer.valueOf(rowYs.get(rowIdx).intValue() + rowStretchHeight));
			}
			
			List<List<JRPrintElement>> headerRows;
			if (willOverflow)
			{
				headerRows = null;				
				releaseColumnHeaderCells(columnHeaderRows);
			}
			else
			{
				headerRows = fillColumnHeaders(columnHeaderRows);
				yOffset += rowYs.get(columnGroups.length).intValue();
			}

			resetVariables();
			
			return headerRows;
		}

		
		private void setCountVars(int rowIdx, int colIdx)
		{
			if (rowIdx == -1)
			{
				rowCountVar.setValue(null);
			}
			else
			{
				rowCountVar.setValue(Integer.valueOf(rowCount[rowIdx]));
			}
			
			if (colIdx == -1)
			{
				colCountVar.setValue(null);
			}
			else
			{
				colCountVar.setValue(Integer.valueOf(columnCount[colIdx]));
			}
		}
		
		private JRFillCellContents fillHeader(int availableHeight) throws JRException
		{
			setCountVars(-1, -1);
			
			JRFillCellContents contents = headerCell.getWorkingClone();
			contents.evaluate(JRExpression.EVALUATION_DEFAULT);
			contents.prepare(availableHeight);
			
			willOverflow = contents.willOverflow();
			
			if (!willOverflow)
			{
				contents.setX(0);
				contents.setY(yOffset);
				contents.setVerticalSpan(columnGroups.length);
				contents.setHorizontalSpan(rowGroups.length);
			}
			return contents;
		}
		
		private JRFillCellContents prepareColumnHeader(HeaderCell cell, int columnIdx, int xOffset, int availableHeight) throws JRException
		{
			JRFillCrosstabColumnGroup group = columnGroups[rowIdx];
			JRFillCellContents contents = cell.isTotal() ? group.getFillTotalHeader() : group.getFillHeader();

			int width = columnXOffsets[columnIdx + cell.getLevelSpan()] - columnXOffsets[columnIdx];
			int height = contents.getHeight();
			
			if (width <= 0 || height <= 0)
			{
				return null;
			}
			
			JRFillCellContents preparedContents = null;
			
			int rowY = rowYs.get(rowIdx).intValue();
			
			if (availableHeight >=  rowY + height)
			{
				setCountVars(-1, columnIdx);
				setGroupVariables(columnGroups, cell.getBucketValues());
				setGroupMeasureVariables(cell, false);
				
				contents = contents.getTransformedContents(width, height, group.getPositionValue(), CrosstabRowPositionEnum.TOP);
				boolean firstOnRow = columnIdx == startColumnIndex && (!printRowHeaders || headerCell == null);
				contents = contents.getBoxContents(
						firstOnRow && getRunDirectionValue() == RunDirectionEnum.LTR,
						firstOnRow && getRunDirectionValue() == RunDirectionEnum.RTL,
						false);
				contents = contents.getWorkingClone();

				contents.evaluate(JRExpression.EVALUATION_DEFAULT);
				contents.prepare(availableHeight - rowY);

				if (contents.willOverflow())
				{
					willOverflow = true;
				}
				else
				{
					contents.setX(columnXOffsets[columnIdx] - columnXOffsets[startColumnIndex] + xOffset);
					contents.setY(rowY + yOffset);
					contents.setVerticalSpan(cell.getDepthSpan());
					contents.setHorizontalSpan(cell.getLevelSpan());
					
					preparedContents = contents;
				}
			}
			else
			{
				willOverflow = true;
			}
			
			return preparedContents;
		}

		
		private int stretchColumnHeadersRow(JRFillCellContents[] headers)
		{
			int rowY = rowYs.get(rowIdx).intValue();
			
			int rowStretchHeight = 0;
			for (int j = 0; j < headers.length; j++)
			{
				JRFillCellContents contents = headers[j];
				
				if (contents != null)
				{
					int startRowY = rowY;
					if (contents.getVerticalSpan() > 1)
					{
						startRowY = rowYs.get(rowIdx - contents.getVerticalSpan() + 1).intValue();
					}
					
					int height = contents.getPrintHeight() - rowY + startRowY;
					
					if (height > rowStretchHeight)
					{
						rowStretchHeight = height;
					}
				}
			}
			
			for (int j = 0; j < headers.length; j++)
			{
				JRFillCellContents contents = headers[j];
				
				if (contents != null)
				{
					int startRowY = rowY;
					if (contents.getVerticalSpan() > 1)
					{
						startRowY = rowYs.get(rowIdx - contents.getVerticalSpan() + 1).intValue();
					}
					
					contents.stretchTo(rowStretchHeight + rowY - startRowY);
				}
			}
			
			return rowStretchHeight;
		}

		
		private List<List<JRPrintElement>> fillColumnHeaders(JRFillCellContents[][] columnHeaderRows) throws JRException
		{
			List<List<JRPrintElement>> headerRows = new ArrayList<List<JRPrintElement>>(columnGroups.length);
			
			for (int i = 0; i < columnHeaderRows.length; ++i)
			{
				List<JRPrintElement> headerRow = new ArrayList<JRPrintElement>(lastColumnIndex - startColumnIndex);
				headerRows.add(headerRow);
				
				for (int j = 0; j < columnHeaderRows[i].length; j++)
				{
					JRFillCellContents contents = columnHeaderRows[i][j];
					
					if (contents != null)
					{
						headerRow.add(contents.fill());
						contents.releaseWorkingClone();
					}
				}
			}
			
			return headerRows;
		}

		private void releaseColumnHeaderCells(JRFillCellContents[][] columnHeaderRows) throws JRException
		{
			for (int i = 0; i < columnHeaderRows.length; ++i)
			{
				for (int j = 0; j < columnHeaderRows[i].length; j++)
				{
					JRFillCellContents contents = columnHeaderRows[i][j];
					
					if (contents != null)
					{
						contents.rewind();
						contents.releaseWorkingClone();
					}
				}
			}
		}
		
		protected int fillRows(int xOffset, int availableHeight) throws JRException
		{
			rowYs.clear();			
			rowYs.add(Integer.valueOf(0));

			for (rowIdx = 0; rowIdx < cellData.length - startRowIndex; ++rowIdx)
			{
				initPreparedRow();
				
				prepareRow(xOffset, availableHeight);

				if (willOverflow)
				{
					break;
				}
				
				fillRow();
				
				rowYs.add(Integer.valueOf(rowYs.get(rowIdx).intValue() + preparedRowHeight));
			}
			
			if (rowIdx < cellData.length - startRowIndex)//overflow
			{
				releasePreparedRow();
				
				if (printRowHeaders)
				{
					fillContinuingRowHeaders(xOffset, availableHeight);
				}
			}
			
			yOffset += rowYs.get(rowIdx).intValue();

			return rowIdx + startRowIndex;
		}

		private void initPreparedRow()
		{
			preparedRow.clear();
			preparedRowHeight = 0;
		}

		private void removeFilledRows(int rowsToRemove)
		{
			if (rowsToRemove > 0)
			{
				for (int i = 0; i < rowsToRemove; ++i)
				{
					printRows.remove(printRows.size() - 1);
					rowYs.remove(rowYs.size() - 1);
				}
				
				rowIdx -= rowsToRemove;
			}
		}

		private void releasePreparedRow() throws JRException
		{
			for (Iterator<JRFillCellContents> it = preparedRow.iterator(); it.hasNext();)
			{
				JRFillCellContents cell = it.next();
				cell.rewind();
				cell.releaseWorkingClone();
			}
			
			preparedRow.clear();
		}

		private void fillRow() throws JRException
		{
			int rowY = rowYs.get(rowIdx).intValue();
			
			List<JRPrintElement> rowPrints = new ArrayList<JRPrintElement>(preparedRow.size());
			for (Iterator<JRFillCellContents> it = preparedRow.iterator(); it.hasNext();)
			{
				JRFillCellContents cell = it.next();
				
				int spanHeight = 0;
				if (cell.getVerticalSpan() > 1)
				{
					spanHeight = rowY - rowYs.get(rowIdx - cell.getVerticalSpan() + 1).intValue();
				}
				
				cell.stretchTo(preparedRowHeight + spanHeight);
				rowPrints.add(cell.fill());
				
				cell.releaseWorkingClone();
			}
			
			addPrintRow(rowPrints);
		}
		
		private void prepareRow(int xOffset, int availableHeight) throws JRException
		{
			for (int col = startColumnIndex; col < lastColumnIndex; ++col)
			{
				CrosstabCell data = cellData[rowIdx + startRowIndex][col];
				boolean overflow = prepareDataCell(data, col, availableHeight, xOffset);
				
				if (overflow)
				{
					willOverflow = true;
					return;
				}
			}
			
			resetVariables();
			
			if (printRowHeaders)
			{
				for (int j = 0; j < rowGroups.length; j++)
				{
					HeaderCell cell = rowHeadersData[j][rowIdx + startRowIndex];
					int vSpan = 0;
					if (cell == null)
					{
						// if we have a span header
						if (toCloseRowHeader(j))
						{
							cell = spanHeaders[j];
							vSpan = cell.getLevelSpan();
							if (spanHeadersStart[j] < startRowIndex)//continuing from the prev page
							{
								vSpan += spanHeadersStart[j] - startRowIndex;
							}
						}
					}
					else
					{
						if (cell.getLevelSpan() > 1)
						{
							spanHeaders[j] = cell;
							spanHeadersStart[j] = rowIdx + startRowIndex;
							continue;
						}
						
						vSpan = 1;
					}
					
					if (cell != null)
					{
						boolean overflow = prepareRowHeader(j, cell, vSpan, availableHeight);						
						if (overflow)
						{
							willOverflow = true;
							return;
						}
					}
				}
				
				// successfully prepared a row, reset the span headers
				for (int j = 0; j < rowGroups.length; j++)
				{
					// if we have a span header
					if (rowHeadersData[j][rowIdx + startRowIndex] == null 
							&& toCloseRowHeader(j))
					{
						// reset it
						spanHeaders[j] = null;
					}
				}
				
				resetVariables();
			}
		}
		
		private boolean prepareDataCell(CrosstabCell data, int column, int availableHeight, int xOffset) throws JRException
		{
			int rowY = rowYs.get(rowIdx).intValue();
			
			JRFillCrosstabCell cell = crossCells[data.getRowTotalGroupIndex()][data.getColumnTotalGroupIndex()];
			JRFillCellContents contents = cell == null ? null : cell.getFillContents();
			if (contents == null || contents.getWidth() <= 0 || contents.getHeight() <= 0)
			{
				return false;
			}
			
			boolean overflow = availableHeight < rowY + contents.getHeight();
			if (!overflow)
			{
				boolean leftEmpty = startColumnIndex != 0 && !isRepeatRowHeaders();
				boolean topEmpty = startRowIndex != 0 && !isRepeatColumnHeaders();
				
				setCountVars(rowIdx + startRowIndex, column);
				setGroupVariables(rowGroups, data.getRowBucketValues());
				setGroupVariables(columnGroups, data.getColumnBucketValues());
				setMeasureVariables(data);
				
				boolean firstOnRow = leftEmpty && column == startColumnIndex;
				contents = contents.getBoxContents(
						firstOnRow && getRunDirectionValue() == RunDirectionEnum.LTR,
						firstOnRow && getRunDirectionValue() == RunDirectionEnum.RTL,
						topEmpty && rowIdx == 0);
				contents = contents.getWorkingClone();
				
				contents.evaluate(JRExpression.EVALUATION_DEFAULT);
				contents.prepare(availableHeight - rowY);
								
				preparedRow.add(contents);
				
				overflow = contents.willOverflow();
				
				if (!overflow)
				{
					contents.setX(columnXOffsets[column] - columnXOffsets[startColumnIndex] + xOffset);
					contents.setY(rowY + yOffset);

					int rowCellHeight = contents.getPrintHeight();
					if (rowCellHeight > preparedRowHeight)
					{
						preparedRowHeight = rowCellHeight;
					}
				}
			}
			
			return overflow;
		}

		private boolean prepareRowHeader(int rowGroup, HeaderCell cell, int vSpan, int availableHeight) throws JRException
		{
			JRFillCrosstabRowGroup group = rowGroups[rowGroup];
			JRFillCellContents contents = cell.isTotal() ? group.getFillTotalHeader() : group.getFillHeader();

			if (contents.getWidth() <= 0 || contents.getHeight() <= 0)
			{
				return false;
			}
			
			int spanHeight = 0;
			int headerY = rowYs.get(rowIdx - vSpan + 1).intValue();
			if (vSpan > 1)
			{
				spanHeight += rowYs.get(rowIdx).intValue() - headerY;
			}
			int rowHeight = spanHeight + preparedRowHeight;
			
			boolean stretchContents = group.getPositionValue() == CrosstabRowPositionEnum.STRETCH;
			int contentsHeight = stretchContents ? rowHeight : contents.getHeight();
			
			boolean headerOverflow = availableHeight <  headerY + contentsHeight || rowHeight < contents.getHeight();
			
			if (!headerOverflow)
			{
				setCountVars(rowIdx + startRowIndex - vSpan + 1, -1);
				setGroupVariables(rowGroups, cell.getBucketValues());
				setGroupMeasureVariables(cell, true);

				if (stretchContents)
				{
					contents = contents.getTransformedContents(contents.getWidth(), rowHeight, CrosstabColumnPositionEnum.LEFT, CrosstabRowPositionEnum.STRETCH);
				}
				contents = contents.getBoxContents(false, false, rowIdx + 1 == vSpan && (!printColumnHeaders || headerCell == null));
				contents.getWorkingClone();

				contents.evaluate(JRExpression.EVALUATION_DEFAULT);
				contents.prepare(availableHeight - headerY);
				
				preparedRow.add(contents);

				headerOverflow = contents.willOverflow();
				
				if (!headerOverflow)
				{
					contents.setX(rowHeadersXOffsets[rowGroup]);
					contents.setY(headerY + yOffset);
					contents.setVerticalSpan(vSpan);
					contents.setHorizontalSpan(cell.getDepthSpan());
					
					int rowCellHeight = contents.getPrintHeight() - spanHeight;
					if (rowCellHeight > preparedRowHeight)
					{
						preparedRowHeight = rowCellHeight;
					}
				}
			}
			
			if (headerOverflow)
			{
				removeFilledRows(vSpan - 1);
			}
			
			return headerOverflow;
		}

		protected boolean toCloseRowHeader(int rowGroup)
		{
			return rowGroup < rowGroups.length - 1 && 
					spanHeaders[rowGroup] != null && 
					spanHeaders[rowGroup].getLevelSpan() + spanHeadersStart[rowGroup] == 
						rowIdx + startRowIndex + 1;
		}

		private void removeExceedingSpanHeaders()
		{
			for (int j = rowGroups.length - 2; j >= 0; --j)
			{
				if (spanHeaders[j] != null && spanHeadersStart[j] >= rowIdx + startRowIndex)
				{
					spanHeaders[j] = null;
				}
			}
		}

		private void setBackSpanHeaders()
		{
			for (int j = rowGroups.length - 2; j >= 0 && spanHeaders[j] == null; --j)
			{
				int spanIndex = getSpanIndex(rowIdx + startRowIndex, j, rowHeadersData);
				
				if (spanIndex >= 0)
				{
					spanHeaders[j] = rowHeadersData[j][spanIndex];
					spanHeadersStart[j] = spanIndex;
				}
			}
		}

		private void fillContinuingRowHeaders(int xOffset, int availableHeight) throws JRException
		{
			boolean done = false;
			breakCrosstab:
			do
			{
				removeExceedingSpanHeaders();
				
				if (!rowBreakable[rowIdx + startRowIndex])
				{
					removeFilledRows(1);
					setBackSpanHeaders();
					continue;
				}

				initPreparedRow();
				
				//fill continuing headers
				for (int j = 0; j < rowGroups.length - 1; ++j)
				{
					if (spanHeaders[j] != null)
					{
						boolean headerOverflow = prepareContinuingRowHeader(j, availableHeight);
						
						if (headerOverflow)
						{
							releasePreparedRow();
							continue breakCrosstab;
						}
					}
				}

				if (!preparedRow.isEmpty())
				{
					int lastRowHeight = rowYs.get(rowIdx).intValue() - rowYs.get(rowIdx - 1).intValue();
					
					if (preparedRowHeight > lastRowHeight)//need to stretch already filled row by refilling
					{
						refillLastRow(xOffset, availableHeight);
					}
					else
					{
						fillContinuingHeaders(lastRowHeight);
					}
				}
				
				done = true;
			}
			while (!done && rowIdx > 0);
		}

		private void fillContinuingHeaders(int lastRowHeight) throws JRException
		{
			int nextToLastHeaderY = rowYs.get(rowIdx - 1).intValue();
			List<JRPrintElement> lastPrintRow = getLastPrintRow();
			
			for (int j = 0; j < preparedRow.size(); ++j)
			{
				JRFillCellContents contents = preparedRow.get(j);
				
				int headerY = rowYs.get(rowIdx - contents.getVerticalSpan()).intValue();
				
				contents.stretchTo(nextToLastHeaderY - headerY + lastRowHeight);
				lastPrintRow.add(contents.fill());
				contents.releaseWorkingClone();
			}
		}

		private void refillLastRow(int xOffset, int availableHeight) throws JRException
		{
			removeFilledRows(1);
			setBackSpanHeaders();
			
			prepareRow(xOffset, availableHeight);
			fillRow();
			
			rowYs.add(Integer.valueOf(rowYs.get(rowIdx).intValue() + preparedRowHeight));
			++rowIdx;
		}

		private boolean prepareContinuingRowHeader(int rowGroup, int availableHeight) throws JRException
		{
			HeaderCell cell = spanHeaders[rowGroup];
			int vSpan = rowIdx + startRowIndex - spanHeadersStart[rowGroup];

			if (spanHeadersStart[rowGroup] < startRowIndex)//continuing from the prev page
			{
				vSpan += spanHeadersStart[rowGroup] - startRowIndex;
			}

			int headerY = rowYs.get(rowIdx - vSpan).intValue();
			int lastHeaderY = rowYs.get(rowIdx).intValue();
			int headerHeight = lastHeaderY - headerY;
			int nextToLastHeaderY = rowYs.get(rowIdx - 1).intValue();
			int stretchHeight = nextToLastHeaderY - headerY;
			
			JRFillCrosstabRowGroup group = rowGroups[rowGroup];
			JRFillCellContents contents = cell.isTotal() ? group.getFillTotalHeader() : group.getFillHeader();
			
			boolean stretchContents = group.getPositionValue() == CrosstabRowPositionEnum.STRETCH;
			int contentsHeight = stretchContents ? headerHeight : contents.getHeight();
			
			boolean headerOverflow = availableHeight < headerY + contentsHeight || headerHeight < contents.getHeight();
			if (!headerOverflow)
			{
				setCountVars(rowIdx + startRowIndex - vSpan, -1);
				setGroupVariables(rowGroups, cell.getBucketValues());
				setGroupMeasureVariables(cell, true);

				if (stretchContents)
				{
					contents = contents.getTransformedContents(contents.getWidth(), headerHeight, CrosstabColumnPositionEnum.LEFT, CrosstabRowPositionEnum.STRETCH);
				}
				
				contents = contents.getBoxContents(false, false, rowIdx == vSpan && (!printColumnHeaders || headerCell == null));
				contents.getWorkingClone();

				contents.evaluate(JRExpression.EVALUATION_DEFAULT);
				contents.prepare(availableHeight - headerY);
				
				preparedRow.add(contents);

				headerOverflow = contents.willOverflow();

				if (!headerOverflow)
				{
					contents.setX(rowHeadersXOffsets[rowGroup]);
					contents.setY(headerY + yOffset);
					contents.setVerticalSpan(vSpan);
					contents.setHorizontalSpan(cell.getDepthSpan());
					
					int rowHeight = contents.getPrintHeight() - stretchHeight;
					if (rowHeight > preparedRowHeight)
					{
						preparedRowHeight = rowHeight;
					}
				}
			}

			if (headerOverflow)
			{
				removeFilledRows(vSpan);
			}
			
			return headerOverflow;
		}
		
		protected void addPrintRow(List<JRPrintElement> printRow)
		{
			printRows.add(printRow);
		}
		
		protected List<JRPrintElement> getLastPrintRow()
		{
			return printRows.get(printRows.size() - 1);
		}

		protected void setGroupVariables(JRFillCrosstabGroup[] groups, Bucket[] bucketValues)
		{
			for (int i = 0; i < groups.length; i++)
			{
				Object value = null;
				if (bucketValues[i] != null && !bucketValues[i].isTotal())
				{
					value = bucketValues[i].getValue();
				}
				groups[i].getFillVariable().setValue(value);
			}
		}

		protected void setGroupMeasureVariables(HeaderCell cell, boolean rowGroup)
		{
			MeasureValue[][] totals = cell.getTotals();
			for (int m = 0; m < measures.length; m++)
			{
				for (int row = 0; row <= rowGroups.length; row++)
				{
					for (int col = 0; col <= columnGroups.length; col++)
					{
						// setting the same values for all row/column groups when filling column/row headers
						MeasureValue[] vals = rowGroup ? totals[row] : totals[col];
						if (row == rowGroups.length && col == columnGroups.length)
						{
							// setting the base measure variable
							Object value = measureValue(vals, m);
							measures[m].getFillVariable().setValue(value);
						}
						else if (retrieveTotal[row][col])
						{
							JRFillVariable totalVar = totalVariables[row][col][m];
							Object value = measureValue(vals, m);
							totalVar.setValue(value);
						}
					}
				}
			}
		}

		protected void setMeasureVariables(CrosstabCell cell)
		{
			MeasureValue[] values = cell.getMesureValues();
			for (int i = 0; i < measures.length; i++)
			{
				Object value = measureValue(values, i);
				measures[i].getFillVariable().setValue(value);
			}
			
			MeasureValue[][][] totals = cell.getTotals();
			for (int row = 0; row <= rowGroups.length; row++)
			{
				for (int col = 0; col <= columnGroups.length; col++)
				{
					MeasureValue[] vals = totals[row][col];
					if (retrieveTotal[row][col])
					{
						for (int m = 0; m < measures.length; m++)
						{
							JRFillVariable totalVar = totalVariables[row][col][m];
							Object value = measureValue(vals, m);
							totalVar.setValue(value);
						}
					}
				}
			}
		}

		
		protected Object measureValue(MeasureValue[] values, int measureIdx)
		{
			if (values == null)
			{
				return null;
			}
			
			Object value;
			if (measures[measureIdx].getPercentageType() == CrosstabPercentageEnum.GRAND_TOTAL)
			{
				if (values[measureIdx].isInitialized())
				{
					value = values[measureIdx].getValue();
				}
				else
				{
					value = measures[measureIdx].getPercentageCalculator().calculatePercentage(values[measureIdx], grandTotals[measureIdx]);
				}
			}
			else
			{
				value = values[measureIdx].getValue();
			}
			return value;
		}

		
		protected void resetVariables()
		{
			for (int i = 0; i < rowGroups.length; i++)
			{
				rowGroups[i].getFillVariable().setValue(null);
			}
			
			for (int i = 0; i < columnGroups.length; i++)
			{
				columnGroups[i].getFillVariable().setValue(null);
			}
			
			for (int i = 0; i < measures.length; i++)
			{
				measures[i].getFillVariable().setValue(null);
			}
			
			for (int row = 0; row <= rowGroups.length; ++row)
			{
				for (int col = 0; col <= columnGroups.length; ++col)
				{
					if (retrieveTotal[row][col])
					{
						for (int i = 0; i < measures.length; i++)
						{
							totalVariables[row][col][i].setValue(null);
						}
					}
				}
			}
		}
	}

	public int getColumnBreakOffset()
	{
		return parentCrosstab.getColumnBreakOffset();
	}

	public boolean isRepeatColumnHeaders()
	{
		return parentCrosstab.isRepeatColumnHeaders();
	}

	public boolean isRepeatRowHeaders()
	{
		return parentCrosstab.isRepeatRowHeaders();
	}

	public JRCrosstabCell[][] getCells()
	{
		return crossCells;
	}

	public JRCellContents getWhenNoDataCell()
	{
		return whenNoDataCell;
	}

	public JRCrosstabParameter[] getParameters()
	{
		return parameters;
	}

	public JRExpression getParametersMapExpression()
	{
		return parentCrosstab.getParametersMapExpression();
	}

	
	public JRElement getElementByKey(String elementKey)
	{
		return JRBaseCrosstab.getElementByKey(this, elementKey);
	}

	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		//not needed
		return null;
	}

	public JRCellContents getHeaderCell()
	{
		return headerCell;
	}

	public JRVariable[] getVariables()
	{
		return variables;
	}

	/**
	 *
	 */
	public RunDirectionEnum getRunDirectionValue()
	{
		return parentCrosstab.getRunDirectionValue();
	}

	/**
	 *
	 */
	public void setRunDirection(RunDirectionEnum runDirection)
	{
		throw new UnsupportedOperationException();
	}

	public JROrigin getOrigin()
	{
		return getElementOrigin();
	}

	public Boolean getIgnoreWidth()
	{
		return parentCrosstab.getIgnoreWidth();
	}

	public void setIgnoreWidth(Boolean ignoreWidth)
	{
		throw new UnsupportedOperationException();
	}

	public void setIgnoreWidth(boolean ignoreWidth)
	{
		throw new UnsupportedOperationException();
	}

	public Color getDefaultLineColor()
	{
		return parentCrosstab.getDefaultLineColor();
	}

	public JRLineBox getLineBox()
	{
		return parentCrosstab.getLineBox();
	}

}

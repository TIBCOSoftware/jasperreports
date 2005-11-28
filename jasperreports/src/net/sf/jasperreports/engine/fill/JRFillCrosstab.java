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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import net.sf.jasperreports.crosstabs.fill.JRCrosstabExpressionEvaluator;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabCell;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabGroup;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabMeasure;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabParameter;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketingService;
import net.sf.jasperreports.crosstabs.fill.calculation.CrosstabCell;
import net.sf.jasperreports.crosstabs.fill.calculation.HeaderCell;
import net.sf.jasperreports.crosstabs.fill.calculation.MeasureDefinition;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition.Bucket;
import net.sf.jasperreports.crosstabs.fill.calculation.MeasureDefinition.MeasureValue;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDefaultCompiler;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import org.jfree.data.general.Dataset;

/**
 * Fill-time implementation of a {@link net.sf.jasperreports.crosstabs.JRCrosstab crosstab}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillCrosstab extends JRFillElement implements JRCrosstab
{
	final protected JRCrosstab parentCrosstab;

	protected JRFillCrosstabDataset dataset;

	protected JRFillCrosstabRowGroup[] rowGroups;

	protected Map rowGroupsMap;

	protected JRFillCrosstabColumnGroup[] columnGroups;

	protected Map columnGroupsMap;

	protected JRFillCrosstabMeasure[] measures;

	protected BucketingService bucketingService;

	protected JRFillVariable[] variables;

	protected Map variablesMap;

	protected JRFillCrosstabParameter[] parameters;

	protected Map parametersMap;

	protected JRCrosstabExpressionEvaluator crosstabEvaluator;

	protected JRFillCrosstabCell[][] crossCells;
	protected JRFillCellContents whenNoDataCell;

	protected boolean hasData;
	protected HeaderCell[][] columnHeadersData;
	protected HeaderCell[][] rowHeadersData;
	protected CrosstabCell[][] cellData;
	protected MeasureValue[] grandTotals;

	private boolean percentage;

	private CrosstabFiller crosstabFiller;
	
	public JRFillCrosstab(JRBaseFiller filler, JRCrosstab crosstab, JRFillObjectFactory factory)
	{
		super(filler, crosstab, factory);

		parentCrosstab = crosstab;

		loadEvaluator(filler.getJasperReport());

		JRFillObjectFactory crosstabFactory = new JRFillObjectFactory(filler, this);

		copyRowGroups(crosstab, crosstabFactory);
		copyColumnGroups(crosstab, crosstabFactory);
		
		copyMeasures(crosstab, crosstabFactory);
		copyCells(crosstab, crosstabFactory);
		whenNoDataCell = crosstabFactory.getCell(crosstab.getWhenNoDataCell());
		
		dataset = factory.getCrosstabDataset(crosstab.getDataset(), this);

		copyParameters(crosstab, factory);

		initVariables();

		crosstabFiller = new CrosstabFiller();
	}

	private void copyRowGroups(JRCrosstab crosstab, JRFillObjectFactory factory)
	{
		JRCrosstabRowGroup[] groups = crosstab.getRowGroups();
		rowGroups = new JRFillCrosstabRowGroup[groups.length];
		rowGroupsMap = new HashMap();
		for (int i = 0; i < groups.length; ++i)
		{
			JRFillCrosstabRowGroup group = factory.getCrosstabRowGroup(groups[i]);
			group.getFillHeader().setVerticalPositionType(groups[i].getPosition());

			rowGroups[i] = group;
			rowGroupsMap.put(group.getName(), new Integer(i));
		}
	}

	private void copyColumnGroups(JRCrosstab crosstab, JRFillObjectFactory factory)
	{
		JRCrosstabColumnGroup[] groups = crosstab.getColumnGroups();
		columnGroups = new JRFillCrosstabColumnGroup[groups.length];
		columnGroupsMap = new HashMap();
		for (int i = 0; i < groups.length; ++i)
		{
			JRFillCrosstabColumnGroup group = factory.getCrosstabColumnGroup(groups[i]);
			columnGroups[i] = group;
			columnGroupsMap.put(group.getName(), new Integer(i));
		}
	}

	private void copyMeasures(JRCrosstab crosstab, JRFillObjectFactory factory)
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
		parametersMap = new HashMap();
		for (int i = 0; i < crossParams.length; i++)
		{
			parameters[i] = factory.getCrosstabParameter(crossParams[i]);
			parametersMap.put(parameters[i].getName(), parameters[i]);
		}
	}

	private void copyCells(JRCrosstab crosstab, JRFillObjectFactory factory)
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

	private void initVariables()
	{
		variables = new JRFillVariable[rowGroups.length + columnGroups.length + measures.length];

		int c = 0;

		for (int i = 0; i < rowGroups.length; i++)
		{
			variables[c++] = rowGroups[i].getFillVariable();
		}

		for (int i = 0; i < columnGroups.length; i++)
		{
			variables[c++] = columnGroups[i].getFillVariable();
		}

		for (int i = 0; i < measures.length; i++)
		{
			variables[c++] = measures[i].getFillVariable();
		}

		variablesMap = new HashMap();
		for (int i = 0; i < variables.length; i++)
		{
			variablesMap.put(variables[i].getName(), variables[i]);
		}
	}

	protected void loadEvaluator(JasperReport jasperReport)
	{
		try
		{
			JREvaluator evaluator = JRDefaultCompiler.getInstance().loadEvaluator(jasperReport, parentCrosstab);
			crosstabEvaluator = new JRCrosstabExpressionEvaluator(evaluator);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException("Could not load evaluator for crosstab.", e);
		}
	}

	private BucketingService createService(byte evaluation) throws JRException
	{
		List rowBuckets = new ArrayList(rowGroups.length);
		for (int i = 0; i < rowGroups.length; ++i)
		{
			rowBuckets.add(createServiceBucket(rowGroups[i], evaluation));
		}

		List colBuckets = new ArrayList(columnGroups.length);
		for (int i = 0; i < columnGroups.length; ++i)
		{
			colBuckets.add(createServiceBucket(columnGroups[i], evaluation));
		}

		percentage = false;
		List measureList = new ArrayList(measures.length);
		for (int i = 0; i < measures.length; ++i)
		{
			measureList.add(createServiceMeasure(measures[i]));
			percentage |= measures[i].getPercentageOfType() == JRCrosstabMeasure.PERCENTAGE_TYPE_GRAND_TOTAL;
		}

		return new BucketingService(rowBuckets, colBuckets, measureList, dataset.isDataPreSorted(), percentage);
	}

	private BucketDefinition createServiceBucket(JRCrosstabGroup group, byte evaluation) throws JRException
	{
		JRCrosstabBucket bucket = group.getBucket();

		Comparator comparator = null;
		JRExpression comparatorExpression = bucket.getComparatorExpression();
		if (comparatorExpression != null)
		{
			comparator = (Comparator) evaluateExpression(comparatorExpression, evaluation);
		}

		byte totalPosition = group.getTotalPosition();
		return new BucketDefinition(bucket.getExpression().getValueClass(), comparator, bucket.getOrder(), totalPosition);
	}

	private MeasureDefinition createServiceMeasure(JRFillCrosstabMeasure measure)
	{
		return new MeasureDefinition(
				measure.getValueClass(), 
				measure.getCalculation(), 
				measure.getIncrementerFactory()); 
	}

	public JRFillExpressionEvaluator getExpressionEvaluator()
	{
		return crosstabEvaluator;
	}

	protected void reset()
	{
		super.reset();

		for (int i = 0; i < variables.length; i++)
		{
			variables[i].setValue(null);
			variables[i].setInitialized(true);
		}
	}

	protected void evaluate(byte evaluation) throws JRException
	{
		reset();

		evaluatePrintWhenExpression(evaluation);

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
		}
	}

	protected void initEvaluator(byte evaluation) throws JRException
	{
		Map parameterValues = JRFillSubreport.getParameterValues(filler, getParametersMapExpression(), getParameters(), evaluation, true);
		
		ResourceBundle resBdl = (ResourceBundle) parameterValues.get(JRParameter.REPORT_RESOURCE_BUNDLE);
		if (resBdl == null)
		{
			JRFillParameter resourceBundleParam = (JRFillParameter) filler.getParametersMap().get(JRParameter.REPORT_RESOURCE_BUNDLE);
			parameterValues.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundleParam.getValue());
		}
		
		parameterValues.put(JRParameter.REPORT_PARAMETERS_MAP, parameterValues);

		for (int i = 0; i < parameters.length; i++)
		{
			Object value = parameterValues.get(parameters[i].getName());
			parameters[i].setValue(value);
		}

		JRFillParameter resourceBundleParam = (JRFillParameter) parametersMap.get(JRParameter.REPORT_RESOURCE_BUNDLE);
		crosstabEvaluator.init(parametersMap, variablesMap, resourceBundleParam, filler.getWhenResourceMissingType());
	}

	protected void initBucketingService()
	{
		if (bucketingService == null)
		{
			try
			{
				bucketingService = createService(JRExpression.EVALUATION_TIME_NOW);
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

	protected boolean prepare(int availableStretchHeight, boolean isOverflow) throws JRException
	{
		super.prepare(availableStretchHeight, isOverflow);

		if (!isToPrint())
		{
			return false;
		}

		if (availableStretchHeight < getRelativeY() - getY() - getBandBottomY())
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

		int availableHeight = getHeight() + availableStretchHeight - getRelativeY() + getY() + getBandBottomY();
		crosstabFiller.fill(availableHeight);
		
		boolean willOverflow = crosstabFiller.willOverflow();
		setStretchHeight(willOverflow ? availableHeight : crosstabFiller.getUsedHeight());
		
		return willOverflow;
	}

	protected JRPrintElement fill() throws JRException
	{
		JRPrintRectangle printRectangle = null;

		printRectangle = new JRTemplatePrintRectangle(getJRTemplateRectangle());
		printRectangle.setX(getX());
		printRectangle.setY(getRelativeY());
		printRectangle.setWidth(getWidth());
		printRectangle.setHeight(getStretchHeight());

		return printRectangle;
	}

	protected JRTemplateRectangle getJRTemplateRectangle()
	{
		if (template == null)
		{
			JRDesignRectangle rectangle = new JRDesignRectangle();

			rectangle.setKey(getKey());
			rectangle.setPositionType(getPositionType());
			// rectangle.setPrintRepeatedValues(isPrintRepeatedValues());
			rectangle.setMode(getMode());
			rectangle.setX(getX());
			rectangle.setY(getY());
			rectangle.setWidth(getWidth());
			rectangle.setHeight(getHeight());
			rectangle.setRemoveLineWhenBlank(isRemoveLineWhenBlank());
			rectangle.setPrintInFirstWholeBand(isPrintInFirstWholeBand());
			rectangle.setPrintWhenDetailOverflows(isPrintWhenDetailOverflows());
			rectangle.setPrintWhenGroupChanges(getPrintWhenGroupChanges());
			rectangle.setForecolor(getForecolor());
/*			rectangle.setBackcolor(getBackcolor());
*/			rectangle.setPen(JRGraphicElement.PEN_NONE);

			template = new JRTemplateRectangle(filler.getJasperPrint().getDefaultStyleProvider(), rectangle);
		}

		return (JRTemplateRectangle) template;
	}

	protected void rewind() throws JRException
	{
		crosstabFiller.initCrosstab();
	}

	protected List getPrintElements()
	{
		return crosstabFiller.getPrintElements();
	}

	protected void resolveElement(JRPrintElement element, byte evaluation) throws JRException
	{
		// nothing
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getCrosstab(this);
	}

	public void writeXml(JRXmlWriter writer) throws IOException
	{
		writer.writeCrosstab(this);
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
		private boolean willOverflow;

		private int[] rowHeadersXOffsets;
		
		private boolean[] columnBreakable;
		private boolean[] rowBreakable;
		private int[] columnXOffsets;
		
		private boolean noDataCellPrinted;
		
		private int startRowIndex;
		private int startColumnIndex;
		private int lastColumnIndex;
		private List columnHeaders;

		private List printRows;

		private HeaderCell[] spanHeaders;
		private int[] spanHeadersStart;

		private List rowYs = new ArrayList();
		private int rowIdx;
		
		private List preparedRow = new ArrayList();
		private int preparedRowHeight;

		protected CrosstabFiller()
		{
			setRowHeadersXOffsets();
		}
		
		protected void initCrosstab()
		{
			columnXOffsets = computeOffsets(columnHeadersData, columnGroups, true);
			columnBreakable = computeBreakableHeaders(columnHeadersData, columnGroups, columnXOffsets, true, true);
			
			int[] rowYOffsets = computeOffsets(rowHeadersData, rowGroups, false);
			rowBreakable = computeBreakableHeaders(rowHeadersData, rowGroups, rowYOffsets, false, false);
			
			spanHeaders = new HeaderCell[rowGroups.length - 1];
			spanHeadersStart = new int[rowGroups.length - 1];

			printRows = new ArrayList();
			
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
						HeaderCell headerCell = headersData[j][i];
						if (headerCell != null && !headerCell.isTotal() && headerCell.getLevelSpan() > 1)
						{
							int span = headerCell.getLevelSpan();
							
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
		
		protected void fill(int availableHeight) throws JRException
		{
			printRows.clear();

			yOffset = 0;
			willOverflow = false;
			
			fillVerticalCrosstab(availableHeight);
		}
		
		protected boolean willOverflow()
		{
			return willOverflow;
		}
		
		protected int getUsedHeight()
		{
			return yOffset;
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
				return;
			}
			
			boolean printRowHeaders = startColumnIndex == 0 || isRepeatRowHeaders();
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

			List columnHeaderRows = null;			
			if (startRowIndex == 0 || isRepeatColumnHeaders())
			{
				columnHeaderRows = fillColumnHeaders(rowHeadersXOffset, availableHeight - yOffset);
				if (willOverflow)
				{
					return;
				}
			}

			int lastRowIndex = fillRows(rowHeadersXOffset, availableHeight - yOffset, printRowHeaders);

			if (lastRowIndex == startRowIndex)
			{
				willOverflow = true;
				return;
			}

			if (columnHeaderRows != null)
			{
				printRows.addAll(columnHeaderRows);
			}
			
			if (lastRowIndex >= rowHeadersData[0].length)
			{
				startColumnIndex = lastColumnIndex;

				if (startColumnIndex < columnHeadersData[0].length)
				{
					startRowIndex = lastRowIndex = 0;

					yOffset += getColumnBreakOffset();
					fillVerticalCrosstab(availableHeight);
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

		
		protected List getGroupHeaders(int available, int[] offsets, boolean[] breakable, int firstIndex, HeaderCell[][] headersData, JRFillCrosstabGroup[] groups)
		{
			List headers = new ArrayList();

			int maxOffset = available + offsets[firstIndex];
			int lastIndex;
			for (lastIndex = firstIndex; lastIndex < headersData[0].length && offsets[lastIndex + 1] <= maxOffset; ++lastIndex)
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
					HeaderCell[] firstHeaders = (HeaderCell[]) headers.get(0);

					for (int j = 0; j < groups.length; ++j)
					{
						HeaderCell header = headersData[j][firstIndex];

						if (header == null)
						{
							int spanIndex = getSpanIndex(firstIndex, j, headersData);
							if (spanIndex >= 0)
							{
								HeaderCell spanCell = headersData[j][spanIndex];
								firstHeaders[j] = HeaderCell.createLevelSpanCopy(spanCell, spanCell.getLevelSpan() - firstIndex + spanIndex);
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
								HeaderCell[] headerCells = (HeaderCell[]) headers.get(spanIndex - firstIndex);
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
					whenNoDataCell.prepare(availableHeight - whenNoDataCell.getHeight());
					
					willOverflow = whenNoDataCell.willOverflow();
					
					if (!willOverflow)
					{
						whenNoDataCell.setX(0);
						whenNoDataCell.setY(0);
						
						JRPrintFrame printCell = whenNoDataCell.fill();
						List noDataRow = new ArrayList(1);
						noDataRow.add(printCell);
						addPrintRow(noDataRow);
						
						yOffset += whenNoDataCell.getPrintHeight();
						noDataCellPrinted = true;
					}
				}
			}
		}
		

		protected List fillColumnHeaders(int xOffset, int availableHeight) throws JRException
		{
			JRFillCellContents[][] columnHeaderRows = new JRFillCellContents[columnGroups.length][lastColumnIndex - startColumnIndex];
			
			rowYs.clear();
			rowYs.add(new Integer(0));
			
			rows:
			for (rowIdx = 0; rowIdx < columnGroups.length; rowIdx++)
			{
				for (int columnIdx = startColumnIndex; columnIdx < lastColumnIndex; ++columnIdx)
				{
					HeaderCell[] headers = (HeaderCell[]) columnHeaders.get(columnIdx - startColumnIndex);
					HeaderCell cell = headers[rowIdx];
					
					if (cell != null)
					{
						JRFillCellContents contents = prepareColumnHeader(cell, columnIdx, xOffset, availableHeight);
						columnHeaderRows[rowIdx + cell.getDepthSpan() - 1][columnIdx - startColumnIndex] = contents;
						
						if (willOverflow)
						{
							break rows;
						}
					}
				}
				
				int rowStretchHeight = stretchColumnHeadersRow(columnHeaderRows[rowIdx]);
				rowYs.add(new Integer(((Integer) rowYs.get(rowIdx)).intValue() + rowStretchHeight));
			}
			
			List headerRows;
			if (willOverflow)
			{
				headerRows = null;				
				releaseColumnHeaderCells(columnHeaderRows);
			}
			else
			{
				headerRows = fillColumnHeaders(columnHeaderRows);
				yOffset += ((Integer) rowYs.get(columnGroups.length)).intValue();
			}

			resetVariables();
			
			return headerRows;
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
			
			int rowY = ((Integer) rowYs.get(rowIdx)).intValue();
			int cellAvailableStretch = availableHeight - rowY - height;
			
			if (cellAvailableStretch >= 0)
			{
				setGroupVariables(columnGroups, cell.getBucketValues());
				
				contents = contents.getTransformedContents(width, height, group.getPosition(), JRCellContents.POSITION_Y_TOP);
				contents = contents.getBoxContents(columnIdx == startColumnIndex, false);
				contents = contents.getWorkingClone();

				contents.evaluate(JRExpression.EVALUATION_DEFAULT);
				contents.prepare(cellAvailableStretch);

				if (contents.willOverflow())
				{
					willOverflow = true;
				}
				else
				{
					contents.setX(columnXOffsets[columnIdx] - columnXOffsets[startColumnIndex] + xOffset);
					contents.setY(rowY + yOffset);
					contents.setVerticalSpan(cell.getDepthSpan());
					
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
			int rowY = ((Integer) rowYs.get(rowIdx)).intValue();
			
			int rowStretchHeight = 0;
			for (int j = 0; j < headers.length; j++)
			{
				JRFillCellContents contents = headers[j];
				
				if (contents != null)
				{
					int startRowY = rowY;
					if (contents.getVerticalSpan() > 1)
					{
						startRowY = ((Integer) rowYs.get(rowIdx - contents.getVerticalSpan() + 1)).intValue();
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
						startRowY = ((Integer) rowYs.get(rowIdx - contents.getVerticalSpan() + 1)).intValue();
					}
					
					contents.stretchTo(rowStretchHeight + rowY - startRowY);
				}
			}
			
			return rowStretchHeight;
		}

		
		private List fillColumnHeaders(JRFillCellContents[][] columnHeaderRows) throws JRException
		{
			List headerRows = new ArrayList(columnGroups.length);
			
			for (int i = 0; i < columnHeaderRows.length; ++i)
			{
				List headerRow = new ArrayList(lastColumnIndex - startColumnIndex);
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
		
		protected int fillRows(int xOffset, int availableHeight, boolean printRowHeaders) throws JRException
		{
			rowYs.clear();			
			rowYs.add(new Integer(0));

			for (rowIdx = 0; rowIdx < cellData.length - startRowIndex; ++rowIdx)
			{
				initPreparedRow();
				
				prepareRow(xOffset, availableHeight, printRowHeaders);

				if (willOverflow)
				{
					break;
				}
				
				fillRow();
				
				rowYs.add(new Integer(((Integer) rowYs.get(rowIdx)).intValue() + preparedRowHeight));
			}
			
			if (rowIdx < cellData.length - startRowIndex)//overflow
			{
				releasePreparedRow();
				
				fillContuingRowHeaders(xOffset, availableHeight, printRowHeaders);
			}
			
			yOffset += ((Integer) rowYs.get(rowIdx)).intValue();

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
			for (Iterator it = preparedRow.iterator(); it.hasNext();)
			{
				JRFillCellContents cell = (JRFillCellContents) it.next();
				cell.rewind();
				cell.releaseWorkingClone();
			}
			
			preparedRow.clear();
		}

		private void fillRow() throws JRException
		{
			int rowY = ((Integer) rowYs.get(rowIdx)).intValue();
			
			List rowPrints = new ArrayList(preparedRow.size());
			for (Iterator it = preparedRow.iterator(); it.hasNext();)
			{
				JRFillCellContents cell = (JRFillCellContents) it.next();
				
				int spanHeight = 0;
				if (cell.getVerticalSpan() > 1)
				{
					spanHeight = rowY - ((Integer) rowYs.get(rowIdx - cell.getVerticalSpan() + 1)).intValue();
				}
				
				cell.stretchTo(preparedRowHeight + spanHeight);
				rowPrints.add(cell.fill());
				
				cell.releaseWorkingClone();
			}
			
			addPrintRow(rowPrints);
		}
		
		private void prepareRow(int xOffset, int availableHeight, boolean printRowHeaders) throws JRException
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
					
					boolean overflow = false;
					if (cell == null)
					{
						overflow = prepareClosingRowHeader(j, availableHeight);
					}
					else
					{
						if (cell.getLevelSpan() > 1)
						{
							spanHeaders[j] = cell;
							spanHeadersStart[j] = rowIdx + startRowIndex;
							continue;
						}

						overflow = prepareRowHeader(j, cell, 1, availableHeight);
					}
					
					if (overflow)
					{
						willOverflow = true;
						return;
					}
				}
				
				resetVariables();
			}
		}
		
		private boolean prepareDataCell(CrosstabCell data, int column, int availableHeight, int xOffset) throws JRException
		{
			int rowY = ((Integer) rowYs.get(rowIdx)).intValue();
			
			JRFillCrosstabCell cell = crossCells[data.getRowTotalGroupIndex()][data.getColumnTotalGroupIndex()];
			JRFillCellContents contents = cell == null ? null : cell.getFillContents();
			if (contents == null || contents.getWidth() <= 0 || contents.getHeight() <= 0)
			{
				return false;
			}
			
			int cellAvailableStretch = availableHeight - rowY - contents.getHeight();
			boolean overflow = cellAvailableStretch < 0;
			if (!overflow)
			{
				boolean leftEmpty = startColumnIndex != 0 && !isRepeatRowHeaders();
				boolean topEmpty = startRowIndex != 0 && !isRepeatColumnHeaders();
				
				setGroupVariables(rowGroups, data.getRowBucketValues());
				setGroupVariables(columnGroups, data.getColumnBucketValues());
				setMeasureVariables(data.getMesureValues());
				
				contents = contents.getBoxContents(leftEmpty && column == startColumnIndex, topEmpty && rowIdx == 0);
				contents = contents.getWorkingClone();
				
				contents.evaluate(JRExpression.EVALUATION_DEFAULT);
				contents.prepare(cellAvailableStretch);
								
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
			int headerY = ((Integer) rowYs.get(rowIdx - vSpan + 1)).intValue();
			if (vSpan > 1)
			{
				spanHeight += ((Integer) rowYs.get(rowIdx)).intValue() - headerY;
			}
			int rowHeight = spanHeight + preparedRowHeight;
			
			boolean stretchContents = group.getPosition() == JRCellContents.POSITION_Y_STRETCH;
			int contentsHeight = stretchContents ? rowHeight : contents.getHeight();
			
			int cellAvailableStretch = availableHeight - headerY - contentsHeight;
			boolean headerOverflow = cellAvailableStretch < 0 || rowHeight < contents.getHeight();
			
			if (!headerOverflow)
			{
				setGroupVariables(rowGroups, cell.getBucketValues());

				if (stretchContents)
				{
					contents = contents.getTransformedContents(contents.getWidth(), rowHeight, JRCellContents.POSITION_X_LEFT, JRCellContents.POSITION_Y_STRETCH);
				}
				contents = contents.getBoxContents(false, rowIdx + 1 == vSpan);
				contents.getWorkingClone();

				contents.evaluate(JRExpression.EVALUATION_DEFAULT);
				contents.prepare(cellAvailableStretch);
				
				preparedRow.add(contents);

				headerOverflow = contents.willOverflow();
				
				if (!headerOverflow)
				{
					contents.setX(rowHeadersXOffsets[rowGroup]);
					contents.setY(headerY + yOffset);
					contents.setVerticalSpan(vSpan);
					
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

		private boolean prepareClosingRowHeader(int rowGroup, int availableHeight) throws JRException
		{
			if (rowGroup < rowGroups.length - 1 && 
					spanHeaders[rowGroup] != null && 
					spanHeaders[rowGroup].getLevelSpan() + spanHeadersStart[rowGroup] == rowIdx + startRowIndex + 1)
			{
				HeaderCell cell = spanHeaders[rowGroup];
				int vSpan = cell.getLevelSpan();
				if (spanHeadersStart[rowGroup] < startRowIndex)//continuing from the prev page
				{
					vSpan += spanHeadersStart[rowGroup] - startRowIndex;
				}
				spanHeaders[rowGroup] = null;
				
				return prepareRowHeader(rowGroup, cell, vSpan, availableHeight);
			}
			
			return false;
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

		private void fillContuingRowHeaders(int xOffset, int availableHeight, boolean printRowHeaders) throws JRException
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
					int lastRowHeight = ((Integer) rowYs.get(rowIdx)).intValue() - ((Integer) rowYs.get(rowIdx - 1)).intValue();
					
					if (preparedRowHeight > lastRowHeight)//need to stretch already filled row by refilling
					{
						refillLastRow(xOffset, availableHeight, printRowHeaders);
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
			int nextToLastHeaderY = ((Integer) rowYs.get(rowIdx - 1)).intValue();
			List lastPrintRow = getLastPrintRow();
			
			for (int j = 0; j < preparedRow.size(); ++j)
			{
				JRFillCellContents contents = (JRFillCellContents) preparedRow.get(j);
				
				int headerY = ((Integer) rowYs.get(rowIdx - contents.getVerticalSpan())).intValue();
				
				contents.stretchTo(nextToLastHeaderY - headerY + lastRowHeight);
				lastPrintRow.add(contents.fill());
				contents.releaseWorkingClone();
			}
		}

		private void refillLastRow(int xOffset, int availableHeight, boolean printRowHeaders) throws JRException
		{
			removeFilledRows(1);
			setBackSpanHeaders();
			
			prepareRow(xOffset, availableHeight, printRowHeaders);
			fillRow();
			
			rowYs.add(new Integer(((Integer) rowYs.get(rowIdx)).intValue() + preparedRowHeight));
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

			int headerY = ((Integer) rowYs.get(rowIdx - vSpan)).intValue();
			int lastHeaderY = ((Integer) rowYs.get(rowIdx)).intValue();
			int headerHeight = lastHeaderY - headerY;
			int nextToLastHeaderY = ((Integer) rowYs.get(rowIdx - 1)).intValue();
			int stretchHeight = nextToLastHeaderY - headerY;
			
			JRFillCrosstabRowGroup group = rowGroups[rowGroup];
			JRFillCellContents contents = cell.isTotal() ? group.getFillTotalHeader() : group.getFillHeader();
			
			boolean stretchContents = group.getPosition() == JRCellContents.POSITION_Y_STRETCH;
			int contentsHeight = stretchContents ? headerHeight : contents.getHeight();
			
			int cellAvailableStretch = availableHeight - headerY - contentsHeight;
			boolean headerOverflow = cellAvailableStretch < 0 || headerHeight < contents.getHeight();
			if (!headerOverflow)
			{
				setGroupVariables(rowGroups, cell.getBucketValues());

				if (stretchContents)
				{
					contents = contents.getTransformedContents(contents.getWidth(), headerHeight, JRCellContents.POSITION_X_LEFT, JRCellContents.POSITION_Y_STRETCH);
				}
				
				contents = contents.getBoxContents(false, rowIdx == vSpan);
				contents.getWorkingClone();

				contents.evaluate(JRExpression.EVALUATION_DEFAULT);
				contents.prepare(cellAvailableStretch);
				
				preparedRow.add(contents);

				headerOverflow = contents.willOverflow();

				if (!headerOverflow)
				{
					contents.setX(rowHeadersXOffsets[rowGroup]);
					contents.setY(headerY + yOffset);
					contents.setVerticalSpan(vSpan);
					
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
		
		protected void addPrintRow(List printRow)
		{
			printRows.add(printRow);
		}
		
		protected List getLastPrintRow()
		{
			return (List) printRows.get(printRows.size() - 1);
		}

		protected List getPrintElements()
		{
			List prints = new ArrayList();
			
			for (Iterator it = printRows.iterator(); it.hasNext();)
			{
				List rowPrints = (List) it.next();
				prints.addAll(rowPrints);
			}
			
			return prints;
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

		protected void setMeasureVariables(MeasureValue[] values)
		{
			for (int i = 0; i < measures.length; i++)
			{
				Object value;
				
				if (measures[i].getPercentageOfType() == JRCrosstabMeasure.PERCENTAGE_TYPE_GRAND_TOTAL)
				{
					if (values[i].isInitialized())
					{
						value = values[i].getValue();
					}
					else
					{
						value = measures[i].getPercentageCalculator().calculatePercentage(values[i], grandTotals[i]);
					}
				}
				else
				{
					value = values[i].getValue();
				}
				
				measures[i].getFillVariable().setValue(value);
			}
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

	public JRCloneable createClone(JRFillCloneFactory factory)
	{
		//not needed
		return null;
	}
}

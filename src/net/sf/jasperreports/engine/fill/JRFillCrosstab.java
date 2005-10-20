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
package net.sf.jasperreports.engine.fill;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	protected int[] rowYOffsets;

	protected int[] rowHeadersXOffsets;

	protected int[] columnXOffsets;

	protected int[] columnHeadersYOffsets;

	private boolean[] columnBreakable;
	private boolean[] rowBreakable;

	protected List printCells;

	protected int rowIndex;

	protected int columnIndex;

	protected int lastColumnIndex;

	private List fillColumnHeaders;

	private boolean percentage;

	public JRFillCrosstab(JRBaseFiller filler, JRCrosstab crosstab, JRFillObjectFactory factory)
	{
		super(filler, crosstab, factory);

		parentCrosstab = crosstab;

		loadEvaluator(filler.getJasperReport());

		JRFillObjectFactory crosstabFactory = new JRFillObjectFactory(filler, this);

		copyRowGroups(crosstab, crosstabFactory);
		setRowHeadersXOffsets();

		copyColumnGroups(crosstab, crosstabFactory);
		setColumnHeadersYOffsets();

		copyMeasures(crosstab, crosstabFactory);

		copyCells(crosstab, crosstabFactory);

		whenNoDataCell = crosstabFactory.getCell(crosstab.getWhenNoDataCell());
		
		dataset = factory.getCrosstabDataset(crosstab.getDataset(), this);

		copyParameters(crosstab, factory);

		initVariables();

		printCells = new ArrayList();
	}

	private void copyRowGroups(JRCrosstab crosstab, JRFillObjectFactory factory)
	{
		JRCrosstabRowGroup[] groups = crosstab.getRowGroups();
		rowGroups = new JRFillCrosstabRowGroup[groups.length];
		rowGroupsMap = new HashMap();
		for (int i = 0; i < groups.length; ++i)
		{
			JRFillCrosstabRowGroup group = factory.getCrosstabRowGroup(groups[i]);

			rowGroups[i] = group;
			rowGroupsMap.put(group.getName(), new Integer(i));
		}
	}

	private void setRowHeadersXOffsets()
	{
		rowHeadersXOffsets = new int[rowGroups.length + 1];
		rowHeadersXOffsets[0] = 0;
		for (int i = 0; i < rowGroups.length; i++)
		{
			rowHeadersXOffsets[i + 1] = rowHeadersXOffsets[i] + rowGroups[i].getWidth();
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

	private void setColumnHeadersYOffsets()
	{
		columnHeadersYOffsets = new int[columnGroups.length + 1];
		columnHeadersYOffsets[0] = 0;
		for (int i = 0; i < columnGroups.length; i++)
		{
			columnHeadersYOffsets[i + 1] = columnHeadersYOffsets[i] + columnGroups[i].getHeight();
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
			throw new JRRuntimeException("Could not load evaluator for crosstab " + getName(), e);
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
		if (group.getTotalHeader() == null)
		{
			totalPosition = BucketDefinition.TOTAL_POSITION_NONE;
		}
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

				columnXOffsets = computeOffsets(columnHeadersData, columnGroups, true);
				columnBreakable = computeBreakableHeaders(columnHeadersData, columnGroups, columnXOffsets, true);
				rowYOffsets = computeOffsets(rowHeadersData, rowGroups, false);
				rowBreakable = computeBreakableHeaders(rowHeadersData, rowGroups, rowYOffsets, false);
			}
			
			initFill();
		}
	}

	private void initFill()
	{
		rowIndex = 0;
		columnIndex = 0;
		lastColumnIndex = 0;
	}

	protected void initEvaluator(byte evaluation) throws JRException
	{
		Map parameterValues = JRFillSubreport.getParameterValues(filler, getParametersMapExpression(), getParameters(), evaluation, true);

		for (int i = 0; i < parameters.length; i++)
		{
			Object value = parameterValues.get(parameters[i].getName());
			parameters[i].setValue(value);
		}

		JRFillParameter resourceBundleParam = (JRFillParameter) parametersMap.get(JRParameter.REPORT_RESOURCE_BUNDLE);
		if (resourceBundleParam == null)
		{
			resourceBundleParam = (JRFillParameter) filler.getParametersMap().get(JRParameter.REPORT_RESOURCE_BUNDLE);
		}

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

	private static int[] computeOffsets(HeaderCell[][] headersData, JRFillCrosstabGroup[] groups, boolean width)
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

	private static boolean[] computeBreakableHeaders(HeaderCell[][] headersData, JRFillCrosstabGroup[] groups, int[] offsets, boolean width)
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
						
						for (int k = i + 1; k < i + span && offsets[k] - offsets[i] < size; ++k)
						{
							breakable[k] = false;
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

		printCells.clear();

		boolean fillEnded = !hasData || (rowIndex >= rowHeadersData[0].length && columnIndex >= columnHeadersData[0].length);
		if (isOverflow && fillEnded && isAlreadyPrinted())
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

		return fillVerticalCrosstab(availableStretchHeight, 0);
	}

	
	protected void fillNoDataCell() throws JRException
	{
		if (whenNoDataCell != null)
		{
			JRPrintFrame printCell = fillCellContents(whenNoDataCell, 0, 0);
			addPrintCell(printCell);
		}
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
		initFill();
	}

	protected boolean fillVerticalCrosstab(int availableStretchHeight, int yOffset) throws JRException
	{
		if (!hasData)
		{
			fillNoDataCell();
			return false;
		}
		
		boolean printRowHeaders = columnIndex == 0 || isRepeatRowHeaders();
		int rowHeadersXOffset = printRowHeaders ? rowHeadersXOffsets[rowGroups.length] : 0;

		if (columnIndex == lastColumnIndex)
		{
			int availableWidth = getWidth();

			fillColumnHeaders = getGroupHeaders(availableWidth - rowHeadersXOffset, columnXOffsets, columnBreakable, columnIndex, columnHeadersData, columnGroups);
			lastColumnIndex = columnIndex + fillColumnHeaders.size();

			if (columnIndex == lastColumnIndex)
			{
				throw new JRRuntimeException("Not enough space to render the crosstab.");
			}
		}

		boolean printColumnHeaders = rowIndex == 0 || isRepeatColumnHeaders();
		int columnHeadersYOffset = printColumnHeaders ? columnHeadersYOffsets[columnGroups.length] : 0;

		int availableHeight = getHeight() + availableStretchHeight - getRelativeY() + getY() + getBandBottomY();
		List rowHeaders = getGroupHeaders(availableHeight - yOffset - columnHeadersYOffset, rowYOffsets, rowBreakable, rowIndex, rowHeadersData, rowGroups);
		int lastRowIndex = rowIndex + rowHeaders.size();

		if (lastRowIndex == rowIndex)
		{
			setStretchHeight(availableHeight);
			return true;
		}

		if (printColumnHeaders)
		{
			fillColumnHeaders(rowHeadersXOffset, yOffset);
		}

		if (printRowHeaders)
		{
			fillRowHeaders(rowHeaders, yOffset + columnHeadersYOffset);
		}

		fillDataCells(lastRowIndex, rowHeadersXOffset, yOffset + columnHeadersYOffset);

		int usedHeight = columnHeadersYOffset + rowYOffsets[lastRowIndex] - rowYOffsets[rowIndex];

		if (lastRowIndex >= rowHeadersData[0].length)
		{
			columnIndex = lastColumnIndex;

			if (columnIndex < columnHeadersData[0].length)
			{
				rowIndex = lastRowIndex = 0;

				return fillVerticalCrosstab(availableStretchHeight, yOffset + usedHeight + getColumnBreakOffset());
			}
		}

		boolean fillEnded = lastRowIndex >= rowHeadersData[0].length && lastColumnIndex >= columnHeadersData[0].length;
		if (fillEnded)
		{
			setStretchHeight(yOffset + usedHeight);
		}
		else
		{
			setStretchHeight(availableHeight);
		}

		rowIndex = lastRowIndex;

		return !fillEnded;
	}

	
	private static List getGroupHeaders(int available, int[] offsets, boolean[] breakable, int firstIndex, HeaderCell[][] headersData, JRFillCrosstabGroup[] groups)
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

	private static int getSpanIndex(int i, int j, HeaderCell[][] headersData)
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

	protected void fillColumnHeaders(int xOffset, int yOffset) throws JRException
	{
		for (int i = 0; i < columnGroups.length; i++)
		{
			JRFillCrosstabColumnGroup group = columnGroups[i];
			JRFillCellContents header = group.getFillHeader();
			JRFillCellContents totalHeader = group.getFillTotalHeader();
			byte position = group.getPosition();

			for (int j = columnIndex; j < lastColumnIndex; ++j)
			{
				HeaderCell[] headers = (HeaderCell[]) fillColumnHeaders.get(j - columnIndex);
				HeaderCell cell = headers[i];
				if (cell != null)
				{
					setGroupVariables(columnGroups, cell.getBucketValues());

					JRFillCellContents contents;
					if (cell.isTotal())
					{
						contents = totalHeader;
					}
					else
					{
						contents = header;
					}

					int width = columnXOffsets[j + cell.getLevelSpan()] - columnXOffsets[j];
					if (contents != null && width > 0 && contents.getHeight() > 0)
					{
						contents = JRFillCellContents.getTransformedContents(filler, this, contents, width, contents.getHeight(), position, JRCellContents.POSITION_Y_TOP);
						
						if (j == columnIndex)
						{
							contents = JRFillCellContents.getBoxContents(contents, true, false);
						}

						JRPrintFrame printCell = fillCellContents(contents, 
								columnXOffsets[j] - columnXOffsets[columnIndex] + xOffset, 
								columnHeadersYOffsets[i] + yOffset);
						addPrintCell(printCell);
					}
				}
			}
		}
		
		setGroupVariablesNull(columnGroups);
	}

	protected void addPrintCell(JRPrintFrame cell)
	{
		printCells.add(cell);
	}

	protected void fillRowHeaders(List rowHeaders, int yOffset) throws JRException
	{
		for (int i = 0; i < rowHeaders.size(); ++i)
		{
			HeaderCell[] headers = (HeaderCell[]) rowHeaders.get(i);
			for (int j = 0; j < rowGroups.length; j++)
			{
				JRFillCrosstabRowGroup group = rowGroups[j];

				HeaderCell cell = headers[j];
				if (cell != null)
				{
					setGroupVariables(rowGroups, cell.getBucketValues());

					JRFillCellContents contents;
					if (cell.isTotal())
					{
						contents = group.getFillTotalHeader();
					}
					else
					{
						contents = group.getFillHeader();
					}

					int height = rowYOffsets[i + rowIndex + cell.getLevelSpan()] - rowYOffsets[i + rowIndex];
					if (contents != null && contents.getWidth() > 0 && height > 0)
					{
						contents = JRFillCellContents.getTransformedContents(filler, this, contents, contents.getWidth(), height, JRCellContents.POSITION_X_LEFT, group.getPosition());
						
						if (i == 0)
						{
							contents = JRFillCellContents.getBoxContents(contents, false, true);
						}

						JRPrintFrame printCell = fillCellContents(contents, 
								rowHeadersXOffsets[j], 
								rowYOffsets[i + rowIndex] - rowYOffsets[rowIndex] + yOffset);
						addPrintCell(printCell);
					}
				}
			}
		}
		
		setGroupVariablesNull(rowGroups);
	}

	private static void setGroupVariables(JRFillCrosstabGroup[] groups, Bucket[] bucketValues)
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

	private static void setGroupVariablesNull(JRFillCrosstabGroup[] groups)
	{
		for (int i = 0; i < groups.length; i++)
		{
			groups[i].getFillVariable().setValue(null);
		}
	}

	private void setMeasureVariables(MeasureValue[] values)
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

	
	private void setMeasureVariablesNull()
	{
		for (int i = 0; i < measures.length; i++)
		{
			measures[i].getFillVariable().setValue(null);
		}
	}

	
	private void fillDataCells(int lastRowIndex, int xOffset, int yOffset) throws JRException
	{
		final boolean leftEmpty = columnIndex != 0 && !isRepeatRowHeaders();
		final boolean topEmpty = rowIndex != 0 && !isRepeatColumnHeaders();
		
		for (int i = rowIndex; i < lastRowIndex; ++i)
		{
			for (int j = columnIndex; j < lastColumnIndex; ++j)
			{
				CrosstabCell data = cellData[i][j];

				JRFillCrosstabCell cell = crossCells[data.getRowTotalGroupIndex()][data.getColumnTotalGroupIndex()];

				setGroupVariables(rowGroups, data.getRowBucketValues());
				setGroupVariables(columnGroups, data.getColumnBucketValues());
				setMeasureVariables(data.getMesureValues());

				JRFillCellContents contents = cell == null ? null : cell.getFillContents();
				if (contents != null && contents.getWidth() > 0 && contents.getHeight() > 0)
				{
					boolean left = leftEmpty && j == columnIndex;
					boolean top = topEmpty && i == rowIndex;
					
					if (left || top)
					{
						contents = JRFillCellContents.getBoxContents(contents, left, top);
					}
					
					JRPrintFrame printCell = fillCellContents(contents, 
							columnXOffsets[j] - columnXOffsets[columnIndex] + xOffset, 
							rowYOffsets[i] - rowYOffsets[rowIndex] + yOffset);
					addPrintCell(printCell);
				}
			}
		}
		
		setGroupVariablesNull(rowGroups);		
		setGroupVariablesNull(columnGroups);
		setMeasureVariablesNull();
	}

	private JRPrintFrame fillCellContents(JRFillCellContents contents, int x, int y) throws JRException
	{
		contents.evaluate(JRExpression.EVALUATION_DEFAULT);

		return contents.fill(0, x, y);
	}

	protected List getPrintElements()
	{
		return printCells;
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

	public String getName()
	{
		return parentCrosstab.getName();
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
		return parentCrosstab.getParameters();
	}

	public JRExpression getParametersMapExpression()
	{
		return parentCrosstab.getParametersMapExpression();
	}

	
	public JRElement getElementByKey(String elementKey)
	{
		return JRBaseCrosstab.getElementByKey(this, elementKey);
	}
}

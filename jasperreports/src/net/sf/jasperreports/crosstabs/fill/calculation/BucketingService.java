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
package net.sf.jasperreports.crosstabs.fill.calculation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition.Bucket;
import net.sf.jasperreports.crosstabs.fill.calculation.MeasureDefinition.MeasureValue;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fill.JRCalculable;
import net.sf.jasperreports.engine.fill.JRFillCrosstab;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * Crosstab bucketing engine.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BucketingService
{
	
	public static final String PROPERTY_BUCKET_MEASURE_LIMIT = JRProperties.PROPERTY_PREFIX + "crosstab.bucket.measure.limit";
	
	protected static final byte DIMENSION_ROW = 0;

	protected static final byte DIMENSION_COLUMN = 1;

	protected static final int DIMENSIONS = 2;

	private final JRFillCrosstab fillCrosstab;

	protected final BucketDefinition[] allBuckets;
	protected final BucketDefinition[][] buckets;

	protected final int rowBucketCount;
	protected final int colBucketCount;

	protected final boolean[][] retrieveTotal;
	private boolean[] rowRetrTotals;
	private int rowRetrTotalMin;
	private int rowRetrTotalMax;
	private int[] rowRetrColMax;

	protected final MeasureDefinition[] measures;
	protected final int origMeasureCount;
	protected final int[] measureIndexes;

	protected final boolean sorted;

	protected final BucketMap bucketValueMap;
	protected long dataCount;
	protected boolean processed;
	
	protected HeaderCell[][] colHeaders;
	protected HeaderCell[][] rowHeaders;
	protected CrosstabCell[][] cells;

	private final MeasureValue[] zeroUserMeasureValues;

	private final int bucketMeasureLimit;
	private int runningBucketMeasureCount;
	
	/**
	 * Creates a crosstab bucketing engine.
	 * 
	 * @param fillCrosstab 
	 * @param rowBuckets the row bucket definitions
	 * @param columnBuckets the column bucket definitions
	 * @param measures the measure definitions
	 * @param sorted whether the data is presorted
	 * @param retrieveTotal totals to retrieve along with the cell values
	 */
	public BucketingService(
			JRFillCrosstab fillCrosstab, 
			List<BucketDefinition> rowBuckets, 
			List<BucketDefinition> columnBuckets, 
			List<MeasureDefinition> measures, 
			boolean sorted, 
			boolean[][] retrieveTotal
			)
	{
		this.fillCrosstab = fillCrosstab;
		
		this.sorted = sorted;

		buckets = new BucketDefinition[DIMENSIONS][];
		
		rowBucketCount = rowBuckets.size();
		buckets[DIMENSION_ROW] = new BucketDefinition[rowBucketCount];
		rowBuckets.toArray(buckets[DIMENSION_ROW]);
		
		colBucketCount = columnBuckets.size();
		buckets[DIMENSION_COLUMN] = new BucketDefinition[colBucketCount];
		columnBuckets.toArray(buckets[DIMENSION_COLUMN]);

		allBuckets = new BucketDefinition[rowBucketCount + colBucketCount];
		System.arraycopy(buckets[DIMENSION_ROW], 0, allBuckets, 0, rowBucketCount);
		System.arraycopy(buckets[DIMENSION_COLUMN], 0, allBuckets, rowBucketCount, colBucketCount);

		origMeasureCount = measures.size();
		List<MeasureDefinition> measuresList = new ArrayList<MeasureDefinition>(measures.size() * 2);
		List<Integer> measureIndexList = new ArrayList<Integer>(measures.size() * 2);
		for (int i = 0; i < measures.size(); ++i)
		{
			MeasureDefinition measure =  measures.get(i);
			addMeasure(measure, i, measuresList, measureIndexList);
		}
		this.measures = new MeasureDefinition[measuresList.size()];
		measuresList.toArray(this.measures);
		this.measureIndexes = new int[measureIndexList.size()];
		for (int i = 0; i < measureIndexes.length; ++i)
		{
			measureIndexes[i] = measureIndexList.get(i).intValue();
		}

		this.retrieveTotal = retrieveTotal;
		checkTotals();
		
		bucketValueMap = createBucketMap(0);
		
		zeroUserMeasureValues = initUserMeasureValues();
		
		bucketMeasureLimit = JRProperties.getIntegerProperty(PROPERTY_BUCKET_MEASURE_LIMIT, 0);
	}


	protected void checkTotals()
	{
		rowRetrTotalMin = rowBucketCount + 1;
		rowRetrTotalMax = -1;
		rowRetrTotals = new boolean[rowBucketCount + 1];
		rowRetrColMax = new int[rowBucketCount + 1];
		for (int row = 0; row <= rowBucketCount; ++row)
		{
			rowRetrColMax[row] = -1;
			boolean total = false;
			for (int col = 0; col <= colBucketCount; ++col)
			{
				if (retrieveTotal[row][col])
				{
					total = true;
					rowRetrColMax[row] = col;
				}
			}
			
			rowRetrTotals[row] = total;
			if (total)
			{
				if (row < rowRetrTotalMin)
				{
					rowRetrTotalMin = row;
				}
				rowRetrTotalMax = row;
				
				if (row < rowBucketCount)
				{
					allBuckets[row].setComputeTotal();
				}
			}
		}
		
		for (int col = 0; col < colBucketCount; ++col)
		{
			BucketDefinition colBucket = allBuckets[rowBucketCount + col];
			if (!colBucket.computeTotal())
			{
				boolean total = false;
				for (int row = 0; !total && row <= rowBucketCount; ++row)
				{
					total = retrieveTotal[row][col];
				}
				
				if (total)
				{
					colBucket.setComputeTotal();
				}
			}
		}
		
		for (int d = 0; d < DIMENSIONS; ++d)
		{
			boolean dTotal = false;
			
			for (int i = 0; i < buckets[d].length; ++i)
			{
				if (dTotal)
				{
					buckets[d][i].setComputeTotal();
				}
				else
				{
					dTotal = buckets[d][i].computeTotal();
				}
			}
		}
	}


	/**
	 * Clears all the accumulated and computed data.
	 */
	public void clear()
	{
		bucketValueMap.clear();
		processed = false;
		dataCount = 0;
		runningBucketMeasureCount = 0;
	}
	
	protected BucketMap createBucketMap(int level)
	{
		BucketMap map;
		if (sorted)
		{
			map = new BucketListMap(level, false);
		}
		else
		{
			map = new BucketTreeMap(level);
		}
		return map;
	}
	
	protected BucketListMap createCollectBucketMap(int level)
	{
		return new BucketListMap(level, true);
	}

	protected void addMeasure(
			MeasureDefinition measure, 
			int index, 
			List<MeasureDefinition> measuresList, 
			List<Integer> measureIndexList
			)
	{
		switch (measure.getCalculation())
		{
			case AVERAGE:
			case VARIANCE:
			{
				MeasureDefinition sumMeasure = MeasureDefinition.createHelperMeasure(measure, CalculationEnum.SUM);
				addMeasure(sumMeasure, index, measuresList, measureIndexList);
				MeasureDefinition countMeasure = MeasureDefinition.createHelperMeasure(measure, CalculationEnum.COUNT);
				addMeasure(countMeasure, index, measuresList, measureIndexList);
				break;
			}
			case STANDARD_DEVIATION:
			{
				MeasureDefinition varianceMeasure = MeasureDefinition.createHelperMeasure(measure, CalculationEnum.VARIANCE);
				addMeasure(varianceMeasure, index, measuresList, measureIndexList);
				break;
			}
			case DISTINCT_COUNT:
			{
				MeasureDefinition countMeasure = MeasureDefinition.createDistinctCountHelperMeasure(measure);
				addMeasure(countMeasure, index, measuresList, measureIndexList);
				break;
			}
		}

		measuresList.add(measure);
		measureIndexList.add(Integer.valueOf(index));
	}

	
	/**
	 * Feeds data to the engine.
	 *  
	 * @param bucketValues the bucket values
	 * @param measureValues the measure values
	 * @throws JRException
	 */
	public void addData(Object[] bucketValues, Object[] measureValues) throws JRException
	{
		if (processed)
		{
			throw new JRException("Crosstab data has already been processed.");
		}
		
		++dataCount;
		
		Bucket[] bucketVals = getBucketValues(bucketValues);

		MeasureValue[] values = bucketValueMap.insertMeasureValues(bucketVals);

		for (int i = 0; i < measures.length; ++i)
		{
			Object measureValue = measureValues[measureIndexes[i]];
			values[i].addValue(measureValue);
		}
	}
	
	protected void bucketMeasuresCreated()
	{
		runningBucketMeasureCount += origMeasureCount;
		
		checkBucketMeasureCount(runningBucketMeasureCount);
	}

	protected Bucket[] getBucketValues(Object[] bucketValues)
	{
		Bucket[] bucketVals = new Bucket[allBuckets.length];

		for (int i = 0; i < allBuckets.length; ++i)
		{
			BucketDefinition bucket = allBuckets[i];
			Object value = bucketValues[i];
			bucketVals[i] = bucket.create(value);
		}
		
		return bucketVals;
	}

	protected MeasureValue[] initMeasureValues()
	{
		MeasureValue[] values;
		values = new MeasureValue[measures.length];

		for (int i = 0; i < measures.length; ++i)
		{
			MeasureDefinition measure = measures[i];
			values[i] = measure.new MeasureValue();

			switch (measure.getCalculation())
			{
				case AVERAGE:
				case VARIANCE:
				{
					values[i].setHelper(values[i - 2], JRCalculable.HELPER_SUM);
					values[i].setHelper(values[i - 1], JRCalculable.HELPER_COUNT);
					break;
				}
				case STANDARD_DEVIATION:
				{
					values[i].setHelper(values[i - 1], JRCalculable.HELPER_VARIANCE);
				}
				case DISTINCT_COUNT:
				{
					values[i].setHelper(values[i - 1], JRCalculable.HELPER_COUNT);
				}
			}
		}
		return values;
	}

	protected MeasureValue[] initUserMeasureValues()
	{
		MeasureValue[] vals = new MeasureValue[origMeasureCount];
		
		for (int c = 0, i = 0; i < measures.length; ++i)
		{
			if (!measures[i].isSystemDefined())
			{
				vals[c] = measures[i].new MeasureValue();
				++c;
			}
		}
		
		return vals;
	}

	
	/**
	 * Processes the data which was fed to the engine.
	 * <p>
	 * This method should be called after the data has been exhausted.
	 * The processing consists of total calculations and crosstab table creation.
	 * 
	 * @throws JRException
	 */
	public void processData() throws JRException
	{
		if (!processed)
		{
			if (dataCount > 0)
			{
				if (allBuckets[rowBucketCount - 1].computeTotal() || allBuckets[allBuckets.length - 1].computeTotal())
				{
					computeTotals(bucketValueMap);
				}

				createCrosstab();
			}
			
			processed = true;
		}
	}

	
	/**
	 * Checks whether there is any data accumulated by the engine.
	 * 
	 * @return <code>true</code> if and only if the engine has any accumulated data
	 */
	public boolean hasData()
	{
		return dataCount > 0;
	}
	
	
	/**
	 * Returns the crosstab column headers.
	 * <p>
	 * {@link #processData() processData()} has to be called before this.
	 * 
	 * @return the crosstab column headers
	 */
	public HeaderCell[][] getColumnHeaders()
	{
		return colHeaders;
	}
	
	
	/**
	 * Returns the crosstab row headers.
	 * <p>
	 * {@link #processData() processData()} has to be called before this.
	 * 
	 * @return the crosstab row headers
	 */
	public HeaderCell[][] getRowHeaders()
	{
		return rowHeaders;
	}
	
	
	/**
	 * Returns the crosstab data cells.
	 * <p>
	 * {@link #processData() processData()} has to be called before this.
	 * 
	 * @return the crosstab data cells
	 */
	public CrosstabCell[][] getCrosstabCells()
	{
		return cells;
	}
	
	
	/**
	 * Returns the measure values for a set of bucket values.
	 * 
	 * @param bucketValues the bucket values
	 * @return the measure values corresponding to the bucket values
	 */
	public MeasureValue[] getMeasureValues(Bucket[] bucketValues)
	{
		BucketMap map = bucketValueMap;
		
		for (int i = 0; map != null && i < allBuckets.length - 1; ++i)
		{
			map = (BucketMap) map.get(bucketValues[i]);
		}
		
		return map == null ? null : (MeasureValue[]) map.get(bucketValues[allBuckets.length - 1]);
	}

	protected MeasureValue[] getUserMeasureValues(MeasureValue[] values)
	{
		MeasureValue[] vals = new MeasureValue[origMeasureCount];
		
		for (int c = 0, i = 0; i < measures.length; ++i)
		{
			if (!measures[i].isSystemDefined())
			{
				vals[c] = values[i];
				++c;
			}
		}
		
		return vals;
	}

	
	/**
	 * Returns the grand total measure values.
	 * 
	 * @return the grand total measure values
	 */
	public MeasureValue[] getGrandTotals()
	{
		BucketMap map = bucketValueMap;
		
		for (int i = 0; map != null && i < allBuckets.length - 1; ++i)
		{
			map = (BucketMap) map.getTotalEntry().getValue();
		}
		
		return map == null ? null : (MeasureValue[]) map.getTotalEntry().getValue();
	}

	
	protected void computeTotals(BucketMap bucketMap) throws JRException
	{
		byte dimension = bucketMap.level < rowBucketCount ? DIMENSION_ROW : DIMENSION_COLUMN;
		
		if (dimension == DIMENSION_COLUMN && !allBuckets[allBuckets.length - 1].computeTotal())
		{
			return;
		}
		
		if (!bucketMap.last)
		{
			for (Iterator<Map.Entry<Bucket, Object>> it = bucketMap.entryIterator(); it.hasNext();)
			{
				Map.Entry<Bucket, Object> entry = it.next();

				computeTotals((BucketMap) entry.getValue());
			}
		}
		
		if (allBuckets[bucketMap.level].computeTotal())
		{
			if (dimension == DIMENSION_COLUMN)
			{
				computeColumnTotal(bucketMap);
			}
			else
			{
				computeRowTotals(bucketMap);
			}
		}
	}


	protected void sumVals(MeasureValue[] totals, MeasureValue[] vals) throws JRException
	{
		for (int i = 0; i < measures.length; i++)
		{
			totals[i].addValue(vals[i]);
		}
	}
	
	protected void computeColumnTotal(BucketMap bucketMap) throws JRException
	{
		MeasureValue[] totals = initMeasureValues();
		
		for (Iterator<Map.Entry<Bucket, Object>> it = bucketMap.entryIterator(); it.hasNext();)
		{
			Map.Entry<Bucket, Object> entry = it.next();
			
			for (int i = bucketMap.level + 1; i < allBuckets.length; ++i)
			{
				entry = ((BucketMap) entry.getValue()).getTotalEntry();
			}
			
			sumVals(totals, (MeasureValue[]) entry.getValue());
		}
				
		for (int i = bucketMap.level + 1; i < allBuckets.length; ++i)
		{
			bucketMap = bucketMap.addTotalNextMap();
		}
		
		bucketMap.addTotalEntry(totals);
	}


	protected void computeRowTotals(BucketMap bucketMap) throws JRException
	{
		BucketListMap totals = createCollectBucketMap(rowBucketCount);
		
		for (Iterator<Map.Entry<Bucket, Object>> it = bucketMap.entryIterator(); it.hasNext();)
		{
			Map.Entry<Bucket, Object> entry = it.next();
			
			for (int i = bucketMap.level + 1; i < rowBucketCount; ++i)
			{
				entry = ((BucketMap) entry.getValue()).getTotalEntry();
			}
			
			totals.collectVals((BucketMap) entry.getValue(), true);			
		}
		
		BucketMap totalBucketMap = bucketMap;
		for (int i = bucketMap.level + 1; i < rowBucketCount; ++i)
		{
			totalBucketMap = totalBucketMap.addTotalNextMap();
		}
		
		totalBucketMap.addTotalEntry(totals);
	}

	
	static protected class MapEntry implements Map.Entry<Bucket, Object>, Comparable
	{
		final Bucket key;

		final Object value;
		
		MapEntry(Bucket key, Object value)
		{
			this.key = key;
			this.value = value;
		}

		public Bucket getKey()
		{
			return key;
		}

		public Object getValue()
		{
			return value;
		}

		public Object setValue(Object value)
		{
			throw new UnsupportedOperationException();
		}

		public int compareTo(Object o)
		{
			return key.compareTo(((MapEntry) o).key);
		}
		
		public String toString()
		{
			return key + "=" + value;
		}
	}
	
	protected abstract class BucketMap
	{
		final int level;
		final boolean last;
		final Bucket totalKey;

		BucketMap(int level)
		{
			this.level = level;
			this.last = level == allBuckets.length - 1;
			totalKey = allBuckets[level].VALUE_TOTAL;
		}

		BucketMap addTotalNextMap()
		{
			BucketMap nextMap = createBucketMap(level + 1);
			addTotalEntry(nextMap);
			return nextMap;
		}

		abstract void clear();

		abstract Iterator<Map.Entry<Bucket, Object>> entryIterator();

		abstract Object get(Bucket key);

		abstract MeasureValue[] insertMeasureValues(Bucket[] bucketValues);

/*		abstract void fillKeys(Collection collectedKeys);*/

		abstract void addTotalEntry(Object val);

		abstract int size();
		
		abstract MapEntry getTotalEntry();
	}

	protected class BucketTreeMap extends BucketMap
	{
		TreeMap<Bucket, Object> map;

		BucketTreeMap(int level)
		{
			super(level);

			map = new TreeMap<Bucket, Object>();
		}
		
		void clear()
		{
			map.clear();
		}

		Iterator<Map.Entry<Bucket, Object>> entryIterator()
		{
			return map.entrySet().iterator();
		}

		Object get(Bucket key)
		{
			return map.get(key);
		}

		MeasureValue[] insertMeasureValues(Bucket[] bucketValues)
		{
			BucketTreeMap levelMap = (BucketTreeMap) bucketValueMap;
			for (int i = 0; i < bucketValues.length - 1; i++)
			{
				BucketTreeMap nextMap = (BucketTreeMap) levelMap.get(bucketValues[i]);
				if (nextMap == null)
				{
					nextMap = new BucketTreeMap(i + 1);
					levelMap.map.put(bucketValues[i], nextMap);
				}

				levelMap = nextMap;
			}

			MeasureValue[] values = (MeasureValue[]) levelMap.get(bucketValues[bucketValues.length - 1]);
			if (values == null)
			{
				values = initMeasureValues();
				levelMap.map.put(bucketValues[bucketValues.length - 1], values);
				
				bucketMeasuresCreated();
			}

			return values;
		}

		int size()
		{
			return map.size();
		}

		void addTotalEntry(Object value)
		{
			map.put(totalKey, value);
		}
		
		MapEntry getTotalEntry()
		{
			Object value = get(totalKey);
			return value == null ? null : new MapEntry(totalKey, value);
		}
		
		
		public String toString()
		{
			return map.toString();
		}
	}

	protected class BucketListMap extends BucketMap
	{
		List<Map.Entry<Bucket, Object>> entries;
		// we maintain a map as well in order to have fast search by key
		// TODO implement this in a single structure
		Map<Bucket, Object> entryMap;

		BucketListMap(int level, boolean linked)
		{
			super(level);

			if (linked)
			{
				entries = new LinkedList<Map.Entry<Bucket, Object>>();
			}
			else
			{
				entries = new ArrayList<Map.Entry<Bucket, Object>>();
			}
			
			entryMap = new HashMap<Bucket, Object>();
		}

		void clear()
		{
			entries.clear();
			entryMap.clear();
		}
		
		Iterator<Map.Entry<Bucket, Object>> entryIterator()
		{
			return entries.iterator();
		}

		private void add(Bucket key, Object value)
		{
			entries.add(new MapEntry(key, value));
			entryMap.put(key, value);
		}

		Object get(Bucket key)
		{
			return entryMap.get(key);
		}

		MeasureValue[] insertMeasureValues(Bucket[] bucketValues)
		{
			int i = 0;
			Object levelObj = this;
			BucketListMap map = null;
			while (i < allBuckets.length)
			{
				map = (BucketListMap) levelObj;
				int size = map.entries.size();
				if (size == 0)
				{
					break;
				}

				MapEntry lastEntry = (MapEntry)map.entries.get(size - 1);
				if (!lastEntry.key.equals(bucketValues[i]))
				{
					break;
				}
				
				++i;
				levelObj = lastEntry.value;
			}

			if (i == allBuckets.length)
			{
				return (MeasureValue[]) levelObj;
			}

			while (i < allBuckets.length - 1)
			{
				BucketListMap nextMap = new BucketListMap(i + 1, false);
				map.add(bucketValues[i], nextMap);
				map = nextMap;
				++i;
			}

			MeasureValue[] values = initMeasureValues();
			map.add(bucketValues[i], values);
			
			bucketMeasuresCreated();

			return values;
		}

		int size()
		{
			return entries.size();
		}

		void addTotalEntry(Object value)
		{
			add(totalKey, value);
		}

		MapEntry getTotalEntry()
		{
			MapEntry lastEntry = (MapEntry)entries.get(entries.size() - 1);
			if (lastEntry.key.isTotal())
			{
				return lastEntry;
			}
			
			return null;
		}

		
		void collectVals(BucketMap map, boolean sum) throws JRException
		{
			ListIterator<Map.Entry<Bucket, Object>> totalIt = entries.listIterator();
			MapEntry totalItEntry = totalIt.hasNext() ? (MapEntry) totalIt.next() : null;
			
			Iterator<Map.Entry<Bucket, Object>> it = map.entryIterator();
			Map.Entry<Bucket, Object> entry = it.hasNext() ? it.next() : null;
			while(entry != null)
			{
				Bucket key = entry.getKey();
				
				int compare = totalItEntry == null ? -1 : key.compareTo(totalItEntry.key);
				if (compare <= 0)
				{
					Object addVal = null;
					
					if (last)
					{
						if (sum)
						{
							MeasureValue[] totalVals = compare == 0 ? (MeasureValue[]) totalItEntry.value : null;

							if (totalVals == null)
							{
								totalVals = initMeasureValues();
								addVal = totalVals;
							}

							sumVals(totalVals, (MeasureValue[]) entry.getValue());
						}
					}
					else
					{
						BucketListMap nextTotals = compare == 0 ? (BucketListMap) totalItEntry.value : null;
						
						if (nextTotals == null)
						{
							nextTotals = createCollectBucketMap(level + 1);
							addVal = nextTotals;
						}
						
						nextTotals.collectVals((BucketMap) entry.getValue(), sum);
					}
					
					if (compare < 0)
					{
						if (totalItEntry != null)
						{
							totalIt.previous();
						}
						
						totalIt.add(new MapEntry(key, addVal));
						entryMap.put(key, addVal);
						
						if (totalItEntry != null)
						{
							totalIt.next();
						}
					}
					
					entry = it.hasNext() ? it.next() : null;
				}
				
				if (compare >= 0)
				{
					totalItEntry = totalIt.hasNext() ? (MapEntry) totalIt.next() : null;
				}
			}
		}
		
		public String toString()
		{
			StringBuffer sb = new StringBuffer();
			sb.append('{');
			for (Iterator<Map.Entry<Bucket, Object>> it = entries.iterator(); it.hasNext();)
			{
				Map.Entry<Bucket, Object> entry = it.next();
				sb.append(entry);
				if (it.hasNext())
				{
					sb.append(", ");
				}
			}
			sb.append('}');
			return sb.toString();
		}
	}

	
	
	protected void createCrosstab() throws JRException
	{
		CollectedList[] collectedHeaders = new CollectedList[BucketingService.DIMENSIONS];
		collectedHeaders[DIMENSION_ROW] = createHeadersList(DIMENSION_ROW, bucketValueMap, 0, false);
		
		BucketMap columnTotalsMap = null;
		BucketListMap collectedCols;
		if (allBuckets[0].computeTotal())
		{
			columnTotalsMap = bucketValueMap;
			for (int i = 0; i < rowBucketCount; ++i)
			{
				columnTotalsMap = (BucketMap) columnTotalsMap.getTotalEntry().getValue();
			}
			collectedCols = (BucketListMap) columnTotalsMap;
		}
		else
		{
			collectedCols = createCollectBucketMap(rowBucketCount);
			collectCols(collectedCols, bucketValueMap);
		}
		collectedHeaders[DIMENSION_COLUMN] = createHeadersList(DIMENSION_COLUMN, collectedCols, 0, false);
		
		int rowBuckets = collectedHeaders[BucketingService.DIMENSION_ROW].span;
		int colBuckets = collectedHeaders[BucketingService.DIMENSION_COLUMN].span;

		int bucketMeasureCount = rowBuckets * colBuckets * origMeasureCount;
		checkBucketMeasureCount(bucketMeasureCount);
		
		colHeaders = createHeaders(BucketingService.DIMENSION_COLUMN, collectedHeaders, columnTotalsMap);
		rowHeaders = createHeaders(BucketingService.DIMENSION_ROW, collectedHeaders, bucketValueMap);
		
		cells = new CrosstabCell[rowBuckets][colBuckets];
		fillCells(collectedHeaders, bucketValueMap, 0, new int[]{0, 0}, new ArrayList<Bucket>(), new ArrayList<BucketMap>());
	}


	protected void checkBucketMeasureCount(int bucketMeasureCount)
	{
		if (bucketMeasureLimit > 0 && bucketMeasureCount > bucketMeasureLimit)
		{
			throw new JRRuntimeException("Crosstab bucket/measure limit (" + bucketMeasureLimit + ") exceeded.");
		}
	}


	protected void collectCols(BucketListMap collectedCols, BucketMap bucketMap) throws JRException
	{
		if (allBuckets[bucketMap.level].computeTotal())
		{
			BucketMap map = bucketMap;
			for (int i = bucketMap.level; i < rowBucketCount; ++i)
			{
				map = (BucketMap) map.getTotalEntry().getValue();
			}
			collectedCols.collectVals(map, false);
			
			return;
		}
		
		for (Iterator<Map.Entry<Bucket, Object>> it = bucketMap.entryIterator(); it.hasNext();)
		{
			Map.Entry<Bucket, Object> entry = it.next();
			BucketMap nextMap = (BucketMap) entry.getValue();
			if (bucketMap.level == rowBucketCount - 1)
			{
				collectedCols.collectVals(nextMap, false);
			}
			else
			{
				collectCols(collectedCols, nextMap);
			}
		}
	}
	
	
	protected CollectedList createHeadersList(byte dimension, BucketMap bucketMap, int level, boolean total) 
			throws JRException
	{
		BucketDefinition bucketDefinition = allBuckets[bucketMap.level];
		CrosstabTotalPositionEnum totalPosition = bucketDefinition.getTotalPosition();
		CollectedList headers;
		if (bucketDefinition.hasOrderValues())
		{
			headers = new OrderedCollectedList(bucketDefinition);
		}
		else
		{
			headers = new SequentialCollectedList(totalPosition);
		}

		for (Iterator<Map.Entry<Bucket, Object>> it = bucketMap.entryIterator(); it.hasNext();)
		{
			Map.Entry<Bucket, Object> entry = it.next();
			Bucket bucketValue = entry.getKey();

			boolean totalBucket = bucketValue.isTotal();
			boolean createHeader = !totalBucket || total || totalPosition != CrosstabTotalPositionEnum.NONE;

			if (createHeader)
			{
				CollectedList nextHeaders;
				if (level + 1 < buckets[dimension].length)
				{
					BucketMap nextMap = (BucketMap) entry.getValue();
					nextHeaders = createHeadersList(dimension, nextMap, level + 1, total || totalBucket);
				}
				else
				{
					nextHeaders = new SequentialCollectedList(CrosstabTotalPositionEnum.NONE);
					nextHeaders.span = 1;
				}
				nextHeaders.key = bucketValue;
				if (bucketDefinition.hasOrderValues())
				{
					Object orderValue = evaluateOrderValue(bucketMap, bucketValue);
					nextHeaders.orderValue = orderValue;
				}
				headers.add(nextHeaders);
			}
		}

		if (headers.span == 0)
		{
			headers.span = 1;
		}

		return headers;
	}
	
	
	protected Object evaluateOrderValue(BucketMap bucketMap, Bucket bucket) throws JRException
	{
		Object bucketValue = bucketMap.get(bucket);
		for (int idx = bucketMap.level + 1; idx < rowBucketCount + colBucketCount; ++idx)
		{
			bucketValue = ((BucketMap) bucketValue).getTotalEntry().getValue();
		}
		MeasureValue[] totals = (MeasureValue[]) bucketValue;
		
		MeasureValue[] userTotals = getUserMeasureValues(totals);
		return fillCrosstab.evaluateExpression(
				allBuckets[bucketMap.level].getOrderByExpression(), 
				userTotals);
	}


	protected HeaderCell[][] createHeaders(byte dimension, CollectedList[] headersLists, BucketMap totalsMap)
	{
		HeaderCell[][] headers = new HeaderCell[buckets[dimension].length][headersLists[dimension].span];
		
		List<Bucket> vals = new ArrayList<Bucket>();
		fillHeaders(dimension, headers, 0, 0, headersLists[dimension], vals, totalsMap);
		
		return headers;
	}

	
	protected void fillHeaders(byte dimension, HeaderCell[][] headers, int level, int col, CollectedList list, 
			List<Bucket> vals, BucketMap totalsMap)
	{
		if (level == buckets[dimension].length)
		{
			return;
		}
		
		for (Iterator<CollectedList> it = list.iterator(); it.hasNext();)
		{
			CollectedList subList = it.next();
			
			vals.add(subList.key);
			
			int depthSpan = subList.key.isTotal() ? buckets[dimension].length - level : 1;
			Bucket[] values = new Bucket[buckets[dimension].length];
			vals.toArray(values);
			
			MeasureValue[][] totals = retrieveHeaderTotals(dimension, values, totalsMap);
			headers[level][col] = new HeaderCell(values, subList.span, depthSpan, totals);
			
			if (!subList.key.isTotal())
			{
				fillHeaders(dimension, headers, level + 1, col, subList, vals, totalsMap);
			}
			
			col += subList.span;
			vals.remove(vals.size() - 1);
		}
	}


	private MeasureValue[][] retrieveHeaderTotals(byte dimension, Bucket[] values, BucketMap totalsMap)
	{
		// an array to advance on bucket levels with values and totals
		int levelCount = buckets[dimension].length;
		Object[] levelBuckets = new Object[levelCount + 1];
		levelBuckets[0] = totalsMap;
		
		for (int idx = 0; idx < levelCount; ++idx)
		{
			// save this as it gets modified
			Object valueBucket = levelBuckets[idx];
			
			// advance with totals
			for (int lIdx = 0; lIdx <= idx; ++lIdx)
			{
				if (levelBuckets[lIdx] != null)
				{
					MapEntry entry = ((BucketMap) levelBuckets[lIdx]).getTotalEntry();
					levelBuckets[lIdx] = entry == null ? null : entry.getValue();
				}
			}
			
			// advance with value if it exists, or total otherwise
			if (valueBucket != null)
			{
				if (idx < values.length && values[idx] != null)
				{
					levelBuckets[idx + 1] = ((BucketMap) valueBucket).get(values[idx]);
				}
				else
				{
					// this is the total computed in the previous loop
					levelBuckets[idx + 1] = levelBuckets[idx];
				}
			}
		}
		
		if (dimension == DIMENSION_ROW)
		{
			// we need to advance through column totals
			for (int idx = 0; idx < colBucketCount; ++idx)
			{
				for (int lIdx = 0; lIdx <= levelCount; ++lIdx)
				{
					if (levelBuckets[lIdx] != null)
					{
						MapEntry entry = ((BucketMap) levelBuckets[lIdx]).getTotalEntry();
						levelBuckets[lIdx] = entry == null ? null : entry.getValue();
					}
				}
			}
		}
		
		MeasureValue[][] totals = new MeasureValue[levelCount + 1][];
		for (int lIdx = 0; lIdx <= levelCount; ++lIdx)
		{
			MeasureValue[] measureValues = (MeasureValue[]) levelBuckets[lIdx];
			if (measureValues != null)
			{
				totals[lIdx] = getUserMeasureValues(measureValues);
			}
		}
		return totals;
	}


	protected void fillCells(CollectedList[] collectedHeaders, BucketMap bucketMap, int level, int[] pos, List<Bucket> vals, List<BucketMap> bucketMaps)
	{
		bucketMaps.add(bucketMap);
		
		byte dimension = level < rowBucketCount ? DIMENSION_ROW : DIMENSION_COLUMN;
		boolean last = level == allBuckets.length - 1;

		CollectedList[] nextCollected = null;
		if (!last)
		{
			nextCollected = new CollectedList[DIMENSIONS];
			for (int d = 0; d < DIMENSIONS; ++d)
			{
				if (d != dimension)
				{
					nextCollected[d] = collectedHeaders[d];
				}
			}
		}
		
		boolean incrementRow = level == buckets[BucketingService.DIMENSION_ROW].length - 1;
				
		CollectedList collectedList = collectedHeaders[dimension];
		
		for (Iterator<CollectedList> it = collectedList.iterator(); it.hasNext();)
		{
			CollectedList list = it.next();
			Object bucketValue = bucketMap == null ? null : bucketMap.get(list.key);
			
			vals.add(list.key);
			if (last)
			{
				fillCell(pos, vals, bucketMaps, (MeasureValue[]) bucketValue);
			}
			else
			{				
				nextCollected[dimension] = list;
				BucketMap nextMap = bucketValue == null ? null : (BucketMap) bucketValue;
				
				fillCells(nextCollected, nextMap, level + 1, pos, vals, bucketMaps);
			}
			vals.remove(vals.size() - 1);
				
			if (incrementRow)
			{
				++pos[0];
				pos[1] = 0;
			}
		}
		
		bucketMaps.remove(bucketMaps.size() - 1);
	}


	protected void fillCell(int[] pos, List<Bucket> vals, List<BucketMap> bucketMaps, MeasureValue[] values)
	{
		Iterator<Bucket> valsIt = vals.iterator();
		Bucket[] rowValues = new Bucket[buckets[BucketingService.DIMENSION_ROW].length];
		for (int i = 0; i < rowValues.length; i++)
		{
			rowValues[i] = valsIt.next();
		}
		
		Bucket[] columnValues = new Bucket[buckets[BucketingService.DIMENSION_COLUMN].length];
		for (int i = 0; i < columnValues.length; i++)
		{
			columnValues[i] = valsIt.next();
		}
		
		MeasureValue[] measureVals = values == null ? zeroUserMeasureValues : getUserMeasureValues(values);
		MeasureValue[][][] totals = retrieveTotals(vals, bucketMaps);
		cells[pos[0]][pos[1]] = new CrosstabCell(rowValues, columnValues, measureVals, totals);
		++pos[1];
	}
	
	
	protected MeasureValue[][][] retrieveTotals(List<Bucket> vals, List<BucketMap> bucketMaps)
	{
		MeasureValue[][][] totals = new MeasureValue[rowBucketCount + 1][colBucketCount + 1][];
		
		for (int row = rowRetrTotalMax; row >= rowRetrTotalMin; --row)
		{
			if (!rowRetrTotals[row])
			{
				continue;
			}
			
			BucketMap rowMap = bucketMaps.get(row);
			for (int i = row; rowMap != null && i < rowBucketCount; ++i)
			{
				MapEntry totalEntry = rowMap.getTotalEntry();
				rowMap = totalEntry == null ? null : (BucketMap) totalEntry.getValue();
			}

			for (int col = 0; col <= rowRetrColMax[row]; ++col)
			{
				BucketMap colMap = rowMap;
				
				if (col < colBucketCount - 1)
				{
					if (row == rowBucketCount)
					{
						rowMap = bucketMaps.get(rowBucketCount + col + 1);
					}
					else if (rowMap != null)
					{
						rowMap = (BucketMap) rowMap.get(vals.get(rowBucketCount + col));
					}
				}
				
				if (!retrieveTotal[row][col])
				{
					continue;
				}
				
				for (int i = col + 1; colMap != null && i < colBucketCount; ++i)
				{
					colMap = (BucketMap) colMap.getTotalEntry().getValue();
				}
				
				if (colMap != null)
				{
					if (col == colBucketCount)
					{
						MeasureValue[] measureValues = (MeasureValue[]) colMap.get(vals.get(rowBucketCount + colBucketCount - 1));
						if (measureValues != null)
						{
							totals[row][col] = getUserMeasureValues(measureValues);
						}
					}
					else
					{
						MapEntry totalEntry = colMap.getTotalEntry();
						if (totalEntry != null)
						{
							MeasureValue[] totalValues = (MeasureValue[]) totalEntry.getValue();
							totals[row][col] = getUserMeasureValues(totalValues);
						}
					}
				}
				
				if (totals[row][col] == null)
				{
					totals[row][col] = zeroUserMeasureValues;
				}
			}
		}

		return totals;
	}
	
	protected static abstract class CollectedList
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		int span;
		Bucket key;
		Object orderValue;
		
		CollectedList()
		{
			span = 0;
		}

		public abstract Iterator<CollectedList> iterator();
		
		public void add(CollectedList sublist)
		{
			addSublist(sublist);
			incrementSpan(sublist);
		}

		protected abstract void addSublist(CollectedList sublist);

		private void incrementSpan(CollectedList sublist)
		{
			if (sublist != null)
			{
				span += sublist.span;
			}
			else
			{
				span += 1;
			}
		}
		
		public String toString()
		{
			return key + "/" + span + ": " + super.toString();
		}
	}
	
	protected static class SequentialCollectedList extends CollectedList
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		final CrosstabTotalPositionEnum totalPosition;
		final LinkedList<CollectedList> list;
		
		SequentialCollectedList(CrosstabTotalPositionEnum totalPosition)
		{
			this.totalPosition = totalPosition;
			
			list = new LinkedList<CollectedList>();
		}

		public Iterator<CollectedList> iterator()
		{
			return list.iterator();
		}

		protected void addSublist(CollectedList sublist)
		{
			if (sublist.key.isTotal() && totalPosition == CrosstabTotalPositionEnum.START)
			{
				list.addFirst(sublist);
			}
			else
			{
				list.add(sublist);
			}
		}
	}
	
	protected static class OrderedCollectedList extends CollectedList
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		final TreeSet<CollectedList> list;
		
		OrderedCollectedList(BucketDefinition bucketDefinition)
		{
			super();
			
			CollectedListComparator comparator = 
				new CollectedListComparator(bucketDefinition);
			list = new TreeSet<CollectedList>(comparator);
		}

		public Iterator<CollectedList> iterator()
		{
			return list.iterator();
		}

		protected void addSublist(CollectedList sublist)
		{
			list.add(sublist);
		}
	}
	
	protected static class CollectedListComparator implements Comparator
	{
		final BucketDefinition bucketDefinition;
		final boolean totalFirst;
		
		CollectedListComparator(BucketDefinition bucketDefinition)
		{
			this.bucketDefinition = bucketDefinition;
			this.totalFirst = bucketDefinition.getTotalPosition() 
					== CrosstabTotalPositionEnum.START;
		}

		public int compare(Object o1, Object o2)
		{
			if (o1 == o2)
			{
				return 0;
			}
			
			CollectedList l1 = (CollectedList) o1;
			CollectedList l2 = (CollectedList) o2;
			
			int order;
			if (l1.key.isTotal())
			{
				if (l2.key.isTotal())
				{
					// this should not happen
					throw new JRRuntimeException("Two total keys in the same list");
				}
				
				order = totalFirst ? -1 : 1;
			}
			else if (l2.key.isTotal())
			{
				order = totalFirst ? 1 : -1;
			}
			else
			{
				// first compare the order values
				order = bucketDefinition.compareOrderValues(
						l1.orderValue, l2.orderValue);
				
				if (order == 0)
				{
					// if order values are equal, fallback to bucket value order
					order = l1.key.compareTo(l2.key);
				}
			}
			
			return order;
		}		
	}
}

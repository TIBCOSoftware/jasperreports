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
package net.sf.jasperreports.crosstabs.fill.calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition.Bucket;
import net.sf.jasperreports.crosstabs.fill.calculation.MeasureDefinition.MeasureValue;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.fill.JRCalculable;

/**
 * Crosstab bucketing engine.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BucketingService
{
	protected static final byte DIMENSION_ROW = 0;

	protected static final byte DIMENSION_COLUMN = 1;

	protected static final int DIMENSIONS = 2;

	protected BucketDefinition[] allBuckets;
	protected BucketDefinition[][] buckets;

	protected MeasureDefinition[] measures;
	protected int origMeasureCount;
	protected int[] measureIndexes;

	protected final boolean sorted;

	protected BucketMap bucketValueMap;
	protected long dataCount;
	protected boolean processed;
	
	protected HeaderCell[][] colHeaders;
	protected HeaderCell[][] rowHeaders;
	protected CrosstabCell[][] cells;

	
	/**
	 * Creates a crosstab bucketing engine.
	 * 
	 * @param rowBuckets the row bucket definitions
	 * @param columnBuckets the column bucket definitions
	 * @param measures the measure definitions
	 * @param sorted whether the data is presorted
	 * @param computeGrandTotal whether grand total computation is required
	 */
	public BucketingService(List rowBuckets, List columnBuckets, List measures, boolean sorted, boolean computeGrandTotal)
	{
		this.sorted = sorted;

		buckets = new BucketDefinition[DIMENSIONS][];
		buckets[DIMENSION_ROW] = new BucketDefinition[rowBuckets.size()];
		rowBuckets.toArray(buckets[DIMENSION_ROW]);
		buckets[DIMENSION_COLUMN] = new BucketDefinition[columnBuckets.size()];
		columnBuckets.toArray(buckets[DIMENSION_COLUMN]);

		allBuckets = new BucketDefinition[rowBuckets.size() + columnBuckets.size()];
		System.arraycopy(buckets[DIMENSION_ROW], 0, allBuckets, 0, rowBuckets.size());
		System.arraycopy(buckets[DIMENSION_COLUMN], 0, allBuckets, rowBuckets.size(), columnBuckets.size());

		origMeasureCount = measures.size();
		List measuresList = new ArrayList(measures.size() * 2);
		List measureIndexList = new ArrayList(measures.size() * 2);
		for (int i = 0; i < measures.size(); ++i)
		{
			MeasureDefinition measure = (MeasureDefinition) measures.get(i);
			addMeasure(measure, i, measuresList, measureIndexList);
		}
		this.measures = new MeasureDefinition[measuresList.size()];
		measuresList.toArray(this.measures);
		this.measureIndexes = new int[measureIndexList.size()];
		for (int i = 0; i < measureIndexes.length; ++i)
		{
			measureIndexes[i] = ((Integer) measureIndexList.get(i)).intValue();
		}

		checkTotals(computeGrandTotal);
		
		bucketValueMap = createBucketMap(0);
	}

	
	protected void checkTotals(boolean computeGrandTotal)
	{
		for (int d = 0; d < DIMENSIONS; ++d)
		{
			boolean dTotal = computeGrandTotal;
			
			for (int i = 0; i < buckets[d].length; ++i)
			{
				if (dTotal)
				{
					buckets[d][i].setComputeTotal();
				}
				else
				{
					dTotal = buckets[d][i].getTotalPosition() != BucketDefinition.TOTAL_POSITION_NONE;
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

	protected void addMeasure(MeasureDefinition measure, int index, List measuresList, List measureIndexList)
	{
		switch (measure.getCalculation())
		{
			case JRVariable.CALCULATION_AVERAGE:
			case JRVariable.CALCULATION_VARIANCE:
			{
				MeasureDefinition sumMeasure = MeasureDefinition.createHelperMeasure(measure, JRVariable.CALCULATION_SUM);
				addMeasure(sumMeasure, index, measuresList, measureIndexList);
				MeasureDefinition countMeasure = MeasureDefinition.createHelperMeasure(measure, JRVariable.CALCULATION_COUNT);
				addMeasure(countMeasure, index, measuresList, measureIndexList);
				break;
			}
			case JRVariable.CALCULATION_STANDARD_DEVIATION:
			{
				MeasureDefinition varianceMeasure = MeasureDefinition.createHelperMeasure(measure, JRVariable.CALCULATION_VARIANCE);
				addMeasure(varianceMeasure, index, measuresList, measureIndexList);
				break;
			}
		}

		measuresList.add(measure);
		measureIndexList.add(new Integer(index));
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
			values[i].addValue(measureValues[measureIndexes[i]]);
		}
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
				case JRVariable.CALCULATION_AVERAGE:
				case JRVariable.CALCULATION_VARIANCE:
				{
					values[i].setHelper(values[i - 2], JRCalculable.HELPER_SUM);
					values[i].setHelper(values[i - 1], JRCalculable.HELPER_COUNT);
					break;
				}
				case JRVariable.CALCULATION_STANDARD_DEVIATION:
				{
					values[i].setHelper(values[i - 1], JRCalculable.HELPER_VARIANCE);
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
				if (allBuckets[buckets[DIMENSION_ROW].length - 1].computeTotal() || allBuckets[allBuckets.length - 1].computeTotal())
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
	 * @return <code>true</code> iff the engine has any accumulated data
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
		byte dimension = bucketMap.level < buckets[DIMENSION_ROW].length ? DIMENSION_ROW : DIMENSION_COLUMN;
		
		if (dimension == DIMENSION_COLUMN && !allBuckets[allBuckets.length - 1].computeTotal())
		{
			return;
		}
		
		if (!bucketMap.last)
		{
			for (Iterator it = bucketMap.entryIterator(); it.hasNext();)
			{
				Map.Entry entry = (Map.Entry) it.next();

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
		
		for (Iterator it = bucketMap.entryIterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			
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
		
		bucketMap.addTotals(totals);
	}


	protected void computeRowTotals(BucketMap bucketMap) throws JRException
	{
		BucketListMap totals = createCollectBucketMap(buckets[DIMENSION_ROW].length);
		
		for (Iterator it = bucketMap.entryIterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			
			for (int i = bucketMap.level + 1; i < buckets[DIMENSION_ROW].length; ++i)
			{
				entry = ((BucketMap) entry.getValue()).getTotalEntry();
			}
			
			totals.collectVals((BucketMap) entry.getValue(), true);
		}

		for (int i = bucketMap.level + 1; i < buckets[DIMENSION_ROW].length; ++i)
		{
			bucketMap = bucketMap.addTotalNextMap();
		}
		
		bucketMap.addTotals(totals);
	}

	
	static protected class MapEntry implements Map.Entry, Comparable
	{
		final Bucket key;

		final Object value;
		
		MapEntry(Bucket key, Object value)
		{
			this.key = key;
			this.value = value;
		}

		public Object getKey()
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
			return key + ":" + value;
		}
	}
	
	protected abstract class BucketMap
	{
		final int level;

		final boolean last;

		BucketMap(int level)
		{
			this.level = level;
			this.last = level == allBuckets.length - 1;
		}

		void addTotals(Object totals)
		{
			addTotalEntry(totals);
		}

		BucketMap addTotalNextMap()
		{
			BucketMap nextMap = createBucketMap(level + 1);
			addTotalEntry(nextMap);
			return nextMap;
		}
		
		abstract void set(Bucket key, Object value);

		abstract void clear();

		abstract Iterator entryIterator();

		abstract Object get(Bucket key);

		abstract MeasureValue[] insertMeasureValues(Bucket[] bucketValues);

/*		abstract void fillKeys(Collection collectedKeys);*/

		abstract void addTotalEntry(Object val);

		abstract int size();
		
		abstract Entry getTotalEntry();
	}

	protected class BucketTreeMap extends BucketMap
	{
		TreeMap map;

		BucketTreeMap(int level)
		{
			super(level);

			map = new TreeMap();
		}
		
		void clear()
		{
			map.clear();
		}

		Iterator entryIterator()
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
			}

			return values;
		}

		int size()
		{
			return map.size();
		}

		void addTotalEntry(Object value)
		{
			map.put(allBuckets[level].VALUE_TOTAL, value);
		}
		
		Entry getTotalEntry()
		{
			Object value = get(allBuckets[level].VALUE_TOTAL);
			return value == null ? null : new MapEntry(allBuckets[level].VALUE_TOTAL, value);
		}
		
		
		public String toString()
		{
			return map.toString();
		}

		void set(Bucket key, Object value)
		{
			map.put(key, value);
		}
	}

	protected class BucketListMap extends BucketMap
	{
		List entries;

		BucketListMap(int level, boolean linked)
		{
			super(level);

			if (linked)
			{
				entries = new LinkedList();
			}
			else
			{
				entries = new ArrayList();
			}
		}

		void clear()
		{
			entries.clear();
		}
		
		Iterator entryIterator()
		{
			return entries.iterator();
		}

		private void add(Bucket key, Object value)
		{
			entries.add(new MapEntry(key, value));
		}

		Object get(Bucket key)
		{
			int idx = Collections.binarySearch(entries, new MapEntry(key, null));
			return idx >= 0 ? ((MapEntry) entries.get(idx)).value : null;
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

				MapEntry lastEntry = (MapEntry) map.entries.get(size - 1);
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

			return values;
		}

		int size()
		{
			return entries.size();
		}

		void addTotalEntry(Object value)
		{
			add(allBuckets[level].VALUE_TOTAL, value);
		}

		Entry getTotalEntry()
		{
			MapEntry lastEntry = (MapEntry) entries.get(entries.size() - 1);
			if (lastEntry.key.isTotal())
			{
				return lastEntry;
			}
			
			return null;
		}

		void set(Bucket key, Object value)
		{
			MapEntry mapEntry = new MapEntry(key, value);
			int idx = Collections.binarySearch(entries, mapEntry);
			int ins = -idx - 1;
			entries.add(ins, mapEntry);
		}

		
		void collectVals(BucketMap map, boolean sum) throws JRException
		{
			ListIterator totalIt = entries.listIterator();
			MapEntry totalItEntry = totalIt.hasNext() ? (MapEntry) totalIt.next() : null;
			
			Iterator it = map.entryIterator();
			Map.Entry entry = it.hasNext() ? (Map.Entry) it.next() : null;
			while(entry != null)
			{
				Bucket key = (Bucket) entry.getKey();
				
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
						if (totalItEntry != null)
						{
							totalIt.next();
						}
					}
					
					entry = it.hasNext() ? (Map.Entry) it.next() : null;
				}
				
				if (compare >= 0)
				{
					totalItEntry = totalIt.hasNext() ? (MapEntry) totalIt.next() : null;
				}
			}
		}
	}

	
	
	protected void createCrosstab() throws JRException
	{
		CollectedList[] collectedHeaders = new CollectedList[BucketingService.DIMENSIONS];
		collectedHeaders[DIMENSION_ROW] = createHeadersList(DIMENSION_ROW, bucketValueMap, 0);
		
		BucketListMap collectedCols;
		if (allBuckets[0].computeTotal())
		{
			BucketMap map = bucketValueMap;
			for (int i = 0; i < buckets[DIMENSION_ROW].length; ++i)
			{
				map = (BucketMap) map.getTotalEntry().getValue();
			}
			collectedCols = (BucketListMap) map;
		}
		else
		{
			collectedCols = createCollectBucketMap(buckets[DIMENSION_ROW].length);
			collectCols(collectedCols, bucketValueMap);
		}
		collectedHeaders[DIMENSION_COLUMN] = createHeadersList(DIMENSION_COLUMN, collectedCols, 0);

		colHeaders = createHeaders(BucketingService.DIMENSION_COLUMN, collectedHeaders);
		rowHeaders = createHeaders(BucketingService.DIMENSION_ROW, collectedHeaders);
		
		cells = new CrosstabCell[collectedHeaders[BucketingService.DIMENSION_ROW].span][collectedHeaders[BucketingService.DIMENSION_COLUMN].span];
		fillCells(collectedHeaders, bucketValueMap, 0, new int[]{0, 0}, new ArrayList());
	}


	protected void collectCols(BucketListMap collectedCols, BucketMap bucketMap) throws JRException
	{
		if (allBuckets[bucketMap.level].computeTotal())
		{
			BucketMap map = bucketMap;
			for (int i = bucketMap.level; i < buckets[DIMENSION_ROW].length; ++i)
			{
				map = (BucketMap) map.getTotalEntry().getValue();
			}
			collectedCols.collectVals(map, false);
			
			return;
		}
		
		for (Iterator it = bucketMap.entryIterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			BucketMap nextMap = (BucketMap) entry.getValue();
			if (bucketMap.level == buckets[DIMENSION_ROW].length - 1)
			{
				collectedCols.collectVals(nextMap, false);
			}
			else
			{
				collectCols(collectedCols, nextMap);
			}
		}
	}
	
	
	protected CollectedList createHeadersList(byte dimension, BucketMap bucketMap, int level)
	{
		CollectedList headers = new CollectedList();

		for (Iterator it = bucketMap.entryIterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			Bucket bucketValue = (Bucket) entry.getKey();

			byte totalPosition = allBuckets[bucketMap.level].getTotalPosition();
			boolean createHeader = !bucketValue.isTotal() || totalPosition != BucketDefinition.TOTAL_POSITION_NONE;

			if (createHeader)
			{
				CollectedList nextHeaders;
				if (level + 1 < buckets[dimension].length)
				{
					BucketMap nextMap = (BucketMap) entry.getValue();
					nextHeaders = createHeadersList(dimension, nextMap, level + 1);
				}
				else
				{
					nextHeaders = new CollectedList();
					nextHeaders.span = 1;
				}
				nextHeaders.key = bucketValue;

				if (bucketValue.isTotal())
				{
					if (totalPosition == BucketDefinition.TOTAL_POSITION_START)
					{
						headers.addFirst(nextHeaders);
					}
					else
					{
						headers.add(nextHeaders);
					}
				}
				else
				{
					headers.add(nextHeaders);
				}
			}
		}

		if (headers.span == 0)
		{
			headers.span = 1;
		}

		return headers;
	}
	
	protected HeaderCell[][] createHeaders(byte dimension, CollectedList[] headersLists)
	{
		HeaderCell[][] headers = new HeaderCell[buckets[dimension].length][headersLists[dimension].span];
		
		List vals = new ArrayList();
		fillHeaders(dimension, headers, 0, 0, headersLists[dimension], vals);
		
		return headers;
	}

	
	protected void fillHeaders(byte dimension, HeaderCell[][] headers, int level, int col, CollectedList list, List vals)
	{
		if (level == buckets[dimension].length)
		{
			return;
		}
		
		for (Iterator it = list.iterator(); it.hasNext();)
		{
			CollectedList subList = (CollectedList) it.next();
			
			vals.add(subList.key);
			
			int depthSpan = subList.key.isTotal() ? buckets[dimension].length - level : 1;
			Bucket[] values = new Bucket[buckets[dimension].length];
			vals.toArray(values);
			
			headers[level][col] = new HeaderCell(values, subList.span, depthSpan);
			
			if (!subList.key.isTotal())
			{
				fillHeaders(dimension, headers, level + 1, col, subList, vals);
			}
			
			col += subList.span;
			vals.remove(vals.size() - 1);
		}
	}


	protected void fillCells(CollectedList[] collectedHeaders, BucketMap bucketMap, int level, int[] pos, List vals)
	{
		byte dimension = level < buckets[DIMENSION_ROW].length ? DIMENSION_ROW : DIMENSION_COLUMN;
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
		
		Iterator bucketIt = bucketMap == null ? null : bucketMap.entryIterator();
		Map.Entry bucketItEntry = bucketIt != null && bucketIt.hasNext() ? (Map.Entry) bucketIt.next() : null;
		for (Iterator it = collectedList.iterator(); it.hasNext();)
		{
			CollectedList list = (CollectedList) it.next();
			
			Map.Entry bucketEntry = null;
			if (list.key.isTotal())
			{
				if (bucketMap != null)
				{
					bucketEntry = bucketMap.getTotalEntry();
				}
			}
			else
			{
				if (bucketItEntry != null && bucketItEntry.getKey().equals(list.key))
				{
					bucketEntry = bucketItEntry;
					bucketItEntry = bucketIt.hasNext() ? (Map.Entry) bucketIt.next() : null;
				}
			}
			
			vals.add(list.key);
			if (last)
			{
				fillCell(pos, vals, bucketEntry);
			}
			else
			{				
				nextCollected[dimension] = list;
				BucketMap nextMap = bucketEntry == null ? null : (BucketMap) bucketEntry.getValue();
				
				fillCells(nextCollected, nextMap, level + 1, pos, vals);
			}
			vals.remove(vals.size() - 1);
				
			if (incrementRow)
			{
				++pos[0];
				pos[1] = 0;
			}
		}
	}


	protected void fillCell(int[] pos, List vals, Map.Entry bucketEntry)
	{
		Iterator valsIt = vals.iterator();
		Bucket[] rowValues = new Bucket[buckets[BucketingService.DIMENSION_ROW].length];
		for (int i = 0; i < rowValues.length; i++)
		{
			rowValues[i] = (Bucket) valsIt.next();
		}
		
		Bucket[] columnValues = new Bucket[buckets[BucketingService.DIMENSION_COLUMN].length];
		for (int i = 0; i < columnValues.length; i++)
		{
			columnValues[i] = (Bucket) valsIt.next();
		}
		
		MeasureValue[] measureVals = bucketEntry == null ? initUserMeasureValues() : getUserMeasureValues((MeasureValue[]) bucketEntry.getValue());
		cells[pos[0]][pos[1]] = new CrosstabCell(rowValues, columnValues, measureVals);
		++pos[1];
	}
	
	protected static class CollectedList extends LinkedList
	{
		int span;
		Bucket key;
		
		CollectedList()
		{
			super();
			
			span = 0;
		}

		public boolean add(Object o)
		{
			boolean added = super.add(o);
			
			incrementSpan(o);
			
			return added;
		}

		public void addFirst(Object o)
		{
			super.addFirst(o);
			
			incrementSpan(o);
		}

		public void addLast(Object o)
		{
			super.add(o);

			incrementSpan(o);
		}

		private void incrementSpan(Object o)
		{
			if (o != null && o instanceof CollectedList)
			{
				span += ((CollectedList) o).span;
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
}

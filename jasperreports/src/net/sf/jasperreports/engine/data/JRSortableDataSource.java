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
package net.sf.jasperreports.engine.data;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.fill.DatasetSortUtil;
import net.sf.jasperreports.engine.fill.JRFillInterruptedException;
import net.sf.jasperreports.engine.type.SortOrderEnum;


/**
 * This data source implementation is now deprecated because it does not know about sorting on 
 * dataset variables and should no longer be used.
 * It will be removed from the library in future versions.
 * @deprecated Replaced by {@link ListOfArrayDataSource} and {@link DatasetSortUtil}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRSortableDataSource implements JRRewindableDataSource
{


	/**
	 *
	 */
	private List<Object[]> records = new ArrayList<Object[]>();
	private Iterator<Object[]> iterator;
	private Object[] currentRecord;
	private Map<String,Integer> fieldIndexMap = new HashMap<String,Integer>();

	protected Collator collator;


	/**
	 *
	 */
	public JRSortableDataSource(JRDataSource ds, JRField[] fields, JRSortField[] sortFields, Locale locale) throws JRException
	{
		if (fields == null)
		{
			//avoid testing for null later
			fields = new JRField[0];
		}

		if (sortFields == null)
		{
			//avoid testing for null later
			sortFields = new JRSortField[0];
		}

		/*   */
		verifySortFields(fields, sortFields);

		collator = Collator.getInstance(locale);

		for(int i = 0; i < fields.length; i++)
		{
			fieldIndexMap.put(fields[i].getName(), Integer.valueOf(i));
		}

		int[] sortIndexes = new int[sortFields.length];
		int[] sortOrders = new int[sortFields.length];
		boolean[] collatorFlags = new boolean[sortFields.length];
		for(int i = 0; i < sortFields.length; i++)
		{
			JRSortField sortField = sortFields[i];
			sortIndexes[i] = fieldIndexMap.get(sortField.getName()).intValue();
			sortOrders[i] = (SortOrderEnum.ASCENDING == sortField.getOrderValue() ? 1 : -1);

			collatorFlags[i] = false;
			for(int j = 0; j < fields.length; j++)
			{
				JRField field = fields[j];
				if (sortField.getName().equals(field.getName()))
				{
					//it is certain that a matching field will be found, due to verifySortFields();
					collatorFlags[i] = String.class.getName().equals(field.getValueClassName());
					break;
				}
			}
		}

		if (ds != null)
		{
			while(ds.next())
			{
				// check whether the fill thread was interrupted
				if (Thread.currentThread().isInterrupted())
				{
					throw new JRFillInterruptedException();
				}
				
				Object[] record = new Object[fields.length];
				for(int i = 0; i < fields.length; i++)
				{
					record[i] = ds.getFieldValue(fields[i]);
				}
				records.add(record);
			}
		}

		/*   */
		Collections.sort(records, new DataSourceComparator(sortIndexes, sortOrders, collatorFlags));

		iterator = records.iterator();
	}


	/**
	 *
	 */
	public boolean next()
	{
		boolean hasNext = false;

		if (iterator != null)
		{
			hasNext = iterator.hasNext();

			if (hasNext)
			{
				currentRecord = iterator.next();
			}
		}

		return hasNext;
	}


	/**
	 *
	 */
	public Object getFieldValue(JRField jrField)
	{
		Integer fieldIndex = fieldIndexMap.get(jrField.getName());

		if (fieldIndex == null)
		{
			throw new JRRuntimeException("Field \"" + jrField.getName() + "\" not found in sortable data source.");
		}

		return currentRecord[fieldIndex.intValue()];
	}


	/**
	 *
	 */
	public void moveFirst()
	{
		iterator = records.iterator();
	}


	/**
	 *
	 */
	public static void verifySortFields(JRField[] fields, JRSortField[] sortFields)
	{
		if (fields != null && sortFields != null)
		{
			for(int i = 0; i < sortFields.length; i++)
			{
				String sortFieldName = sortFields[i].getName();

				boolean isFound = false;

				int j = 0;
				while (!isFound && j < fields.length)
				{
					isFound = sortFieldName.equals(fields[j].getName());
					j++;
				}

				if (!isFound)
				{
					throw new JRRuntimeException("Sort field \"" + sortFieldName + "\" not found in the list of data source fields.");
				}
			}
		}
	}


	/**
	 *
	 */
	class DataSourceComparator implements Comparator
	{
		int[] sortIndexes = null;
		int[] sortOrders = null;
		boolean[] collatorFlags = null;

		public DataSourceComparator(int[] sortIndexes, int[] sortOrders, boolean[] collatorFlags)
		{
			this.sortIndexes = sortIndexes;
			this.sortOrders = sortOrders;
			this.collatorFlags = collatorFlags;
		}

		public int compare(Object arg1, Object arg2)
		{
			Object[] record1 = (Object[])arg1;
			Object[] record2 = (Object[])arg2;

			int ret = 0;

			for(int i = 0; i < sortIndexes.length; i++)
			{
				int sortIndex = sortIndexes[i];
				Comparable field1 = (Comparable)record1[sortIndex];
				Comparable field2 = (Comparable)record2[sortIndex];

				if (field1 == null)
				{
					ret = (field2 == null) ? 0 : -1;
				}
				else if (field2 == null)
				{
					ret = 1;
				}
				else
				{
					if (collatorFlags[i])
					{
						ret = collator.compare(field1, field2);
					}
					else
					{
						ret = field1.compareTo(field2);
					}
				}

				ret = ret * sortOrders[i];

				if (ret != 0)
				{
					return ret;
				}
			}

			return ret;
		}
	}


}



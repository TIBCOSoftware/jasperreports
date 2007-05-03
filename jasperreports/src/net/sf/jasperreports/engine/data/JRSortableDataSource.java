/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRSortableDataSource implements JRRewindableDataSource
{


	/**
	 *
	 */
	private List records = new ArrayList();
	private Iterator iterator = null;
	private Object[] currentRecord = null;
	private Map fieldIndexMap = new HashMap();

	protected Collator collator = null;


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
			fieldIndexMap.put(fields[i].getName(), new Integer(i));
		}
		
		int[] sortIndexes = new int[sortFields.length];
		int[] sortOrders = new int[sortFields.length];
		boolean[] collatorFlags = new boolean[sortFields.length];
		for(int i = 0; i < sortFields.length; i++)
		{
			JRSortField sortField = sortFields[i];
			sortIndexes[i] = ((Integer)fieldIndexMap.get(sortField.getName())).intValue();
			sortOrders[i] = (JRSortField.SORT_ORDER_ASCENDING == sortField.getOrder() ? 1 : -1);

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
				currentRecord = (Object[])iterator.next();
			}
		}
		
		return hasNext;
	}


	/**
	 *
	 */
	public Object getFieldValue(JRField jrField)
	{
		Integer fieldIndex = (Integer)fieldIndexMap.get(jrField.getName());

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



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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.data.ListOfArrayDataSource;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class DatasetSortUtil
{


	/**
	 * Returns all current sort field criteria, including the dynamic ones provided as report parameter.
	 */
	public static JRSortField[] getAllSortFields(JRFillDataset dataset)
	{
		List<JRSortField> allSortFields = new ArrayList<JRSortField>();
		
		JRSortField[] staticSortFields = dataset.getSortFields();
		if (staticSortFields != null)
		{
			allSortFields.addAll(Arrays.asList(staticSortFields));
		}

		List<JRSortField> dynamicSortFields = (List<JRSortField>)dataset.getParameterValue(JRParameter.SORT_FIELDS, true);
		if (dynamicSortFields != null)
		{
			allSortFields.addAll(dynamicSortFields);
		}

		return allSortFields.toArray(new JRSortField[allSortFields.size()]);
	}


	/**
	 *
	 */
	public static boolean needSorting(JRFillDataset dataset)
	{
		JRSortField[] staticSortFields = dataset.getSortFields();
		List<JRSortField> dynamicSortFields = (List<JRSortField>)dataset.getParameterValue(JRParameter.SORT_FIELDS, true);
		
		return 
			(staticSortFields != null
			&& staticSortFields.length > 0)
			|| (dynamicSortFields != null
			&& dynamicSortFields.size() > 0);
	}


	/**
	 *
	 */
	public static JRDataSource getSortedDataSource(
		JRBaseFiller filler, 
		JRFillDataset dataset, 
		Locale locale 
		) throws JRException
	{
		SortInfo sortInfo = createSortInfo(dataset);
		
		SortFillDatasetRun sortDatasetRun = new SortFillDatasetRun(filler, dataset, sortInfo);
		
		List<Object[]> records = sortDatasetRun.sort();
		
		/*   */
		Collections.sort(
			records, 
			new DataSourceComparator(
				sortInfo.sortFieldInfo, 
				locale
				)
			);
		
		return new ListOfArrayDataSource(records, sortInfo.fieldNames.toArray(new String[sortInfo.fieldNames.size()]));
	}


	/**
	 *
	 */
	private static SortInfo createSortInfo(JRFillDataset dataset) throws JRException
	{
		SortInfo sortInfo = new SortInfo();

		Map<String, JRField> fieldsMap = new HashMap<String, JRField>();
		Map<String, Integer> fieldIndexMap = new HashMap<String, Integer>();
		JRField[] fields = dataset.getFields();
		if (fields != null)
		{
			for(int i = 0; i < fields.length; i++)
			{
				JRField field = fields[i];
				fieldsMap.put(field.getName(), field);
				fieldIndexMap.put(field.getName(), Integer.valueOf(i));
				sortInfo.fieldNames.add(field.getName());
			}
		}

		Map<String, JRVariable> variablesMap = new HashMap<String, JRVariable>();
		JRVariable[] variables = dataset.getVariables();
		if (variables != null)
		{
			for(int i = 0; i < variables.length; i++)
			{
				variablesMap.put(variables[i].getName(), variables[i]);
			}
		}

		JRSortField[] sortFields = getAllSortFields(dataset);
		if (sortFields != null)
		{
			sortInfo.sortFieldInfo = new SortFieldInfo[sortFields.length];

			for(int i = 0; i < sortFields.length; i++)
			{
				JRSortField sortField = sortFields[i];

				String sortFieldName = sortField.getName();
				
				SortFieldInfo info = new SortFieldInfo();
				
				info.name = sortFieldName;
				info.isVariable = sortField.getType() == SortFieldTypeEnum.VARIABLE;
				info.order = (SortOrderEnum.ASCENDING == sortField.getOrderValue() ? 1 : -1);

				Integer index;
				if (info.isVariable)
				{
					JRVariable variable = variablesMap.get(sortFieldName);
					if (variable == null)
					{
						throw new JRRuntimeException("Sort variable \"" + sortFieldName + "\" not found in dataset.");
					}
					
					index = new Integer(sortInfo.fieldNames.size());
					info.collatorFlag = String.class.getName().equals(variable.getValueClassName());
					
					sortInfo.fieldNames.add(variable.getName());
				}
				else
				{
					JRField field = fieldsMap.get(sortFieldName);
					if (field == null)
					{
						throw new JRRuntimeException("Sort field \"" + sortFieldName + "\" not found in dataset.");
					}
					
					index = fieldIndexMap.get(sortField.getName());

					info.collatorFlag = String.class.getName().equals(field.getValueClassName());
				}
				info.index = index.intValue();

				sortInfo.sortFieldInfo[i] = info; 
			}
		}
		
		return sortInfo;
	}

	
}


/**
 *
 */
class DataSourceComparator implements Comparator
{
	Collator collator;
	SortFieldInfo[] sortFieldInfo;

	public DataSourceComparator(SortFieldInfo[] sortFieldInfo, Locale locale)
	{
		this.collator = Collator.getInstance(locale);
		this.sortFieldInfo = sortFieldInfo;
	}

	public int compare(Object arg1, Object arg2)
	{
		Object[] record1 = (Object[])arg1;
		Object[] record2 = (Object[])arg2;

		int ret = 0;

		for(int i = 0; i < sortFieldInfo.length; i++)
		{
			SortFieldInfo info = sortFieldInfo[i];
			
			Comparable field1 = (Comparable)record1[info.index];
			Comparable field2 = (Comparable)record2[info.index];

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
				if (info.collatorFlag)
				{
					ret = collator.compare(field1, field2);
				}
				else
				{
					ret = field1.compareTo(field2);
				}
			}

			ret = ret * info.order;

			if (ret != 0)
			{
				return ret;
			}
		}

		return ret;
	}
}


/**
 *
 */
class SortInfo
{
	public List<String> fieldNames = new ArrayList<String>();
	public SortFieldInfo[] sortFieldInfo;
}


/**
 *
 */
class SortFieldInfo
{
	public String name;
	public boolean isVariable;
	public int order;
	public int index;
	public boolean collatorFlag;
}


/**
 * Used to iterate on a subdataset and create a sorted data source.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
class SortFillDatasetRun extends JRFillDatasetRun
{

	private JRSortField[] allSortFields;
	private SortInfo sortInfo;
	private List<Object[]> records;

	
	public SortFillDatasetRun(JRBaseFiller filler, JRFillDataset dataset, SortInfo sortInfo) throws JRException
	{
		super(
			filler, 
			new JRDesignDatasetRun(), //we don't need anything from a dataset run. just avoid NPEs down the call 
			dataset
			);
		
		allSortFields = DatasetSortUtil.getAllSortFields(dataset);

		this.sortInfo = sortInfo;
	}

	
	public List<Object[]> sort() throws JRException
	{
		records = new ArrayList<Object[]>();

		try
		{
			//all parameters are already set onto the dataset by the main fill process

			iterate();
		}
		finally
		{
			dataset.closeDatasource();
		}
		
		return records;
	}

	
	@Override
	protected void detail() throws JRScriptletException, JRException 
	{
		super.detail();
		
		JRField[] fields = dataset.getFields();
		Object[] record = new Object[sortInfo.fieldNames.size()];
		if(fields != null)
		{
			for(int i = 0; i < fields.length; i++)
			{
				record[i] = dataset.getFieldValue(fields[i].getName());
			}
		}
		for(int i = 0; i < allSortFields.length; i++)
		{
			JRSortField sortField = allSortFields[i];
			if (sortField.getType() == SortFieldTypeEnum.VARIABLE)
			{
				record[sortInfo.sortFieldInfo[i].index] = dataset.getVariableValue(sortField.getName());
			}
		}
		records.add(record);
	}


	@Override
	protected boolean advanceDataset() throws JRException
	{
		// do not filter records before sorting in order to support "Top 5 sorted" cases.
		// FIXME optimize by filtering when the filters are on fields (and other cases).
		return dataset.next(false);
	}
	
	
}


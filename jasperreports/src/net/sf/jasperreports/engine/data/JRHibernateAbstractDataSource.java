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

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuter;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.type.Type;

/**
 * Base abstract Hibernate data source.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRHibernateAbstractDataSource implements JRDataSource
{
	private final boolean useFieldDescription;
	private final Map<String, FieldReader> fieldReaders;
	protected final JRHibernateQueryExecuter queryExecuter;
	private Object currentReturnValue;
	
	
	/**
	 * Creates a Hibernate data source.
	 * 
	 * @param queryExecuter the query executer
	 * @param useFieldDescription whether to use field descriptions for fields to results mapping
	 * @param useIndexOnSingleReturn whether to use indexed addressing even when the query has only one return column
	 */
	protected JRHibernateAbstractDataSource(JRHibernateQueryExecuter queryExecuter, boolean useFieldDescription, boolean useIndexOnSingleReturn)
	{
		this.useFieldDescription = useFieldDescription;
		
		this.queryExecuter = queryExecuter;

		fieldReaders = assignReaders(useIndexOnSingleReturn);
	}
	
	/**
	 * Assigns field readers to report fields.
	 * 
	 * @param useIndexOnSingleReturn  whether to use indexed addressing even when the query has only one return column
	 * @return a report field name to field reader mapping
	 * @see FieldReader
	 */
	protected Map<String, FieldReader> assignReaders(boolean useIndexOnSingleReturn)
	{
		Map<String, FieldReader> readers = new HashMap<String, FieldReader>();
		
		JRField[] fields = queryExecuter.getDataset().getFields();
		Type[] returnTypes = queryExecuter.getReturnTypes();
		String[] aliases = queryExecuter.getReturnAliases();
		
		Map<String, Integer> aliasesMap = new HashMap<String, Integer>();
		if (aliases != null)
		{
			for (int i = 0; i < aliases.length; i++)
			{
				aliasesMap.put(aliases[i], Integer.valueOf(i));
			}
		}
		
		if (returnTypes.length == 1)
		{
			if (returnTypes[0].isEntityType() || returnTypes[0].isComponentType())
			{
				for (int i = 0; i < fields.length; i++)
				{
					JRField field = fields[i];
					readers.put(field.getName(), getFieldReaderSingleReturn(aliasesMap, field, useIndexOnSingleReturn));
				}
			}
			else
			{
				if (fields.length > 1)
				{
					throw new JRRuntimeException("The HQL query returns only one non-entity and non-component result but there are more than one fields.");
				}
				
				if (fields.length == 1)
				{
					JRField field = fields[0];
					if (useIndexOnSingleReturn)
					{
						readers.put(field.getName(), new IndexFieldReader(0));
					}
					else
					{
						readers.put(field.getName(), new IdentityFieldReader());
					}
				}
			}
		}
		else
		{
			for (int i = 0; i < fields.length; i++)
			{
				JRField field = fields[i];
				readers.put(field.getName(), getFieldReader(returnTypes, aliasesMap, field));				
			}
		}

		return readers;
	}

	protected FieldReader getFieldReaderSingleReturn(Map<String,Integer> aliasesMap, JRField field, boolean useIndex)
	{
		FieldReader reader;
		
		String fieldMapping = getFieldMapping(field);
		if (aliasesMap.containsKey(fieldMapping))
		{
			if (useIndex)
			{
				reader = new IndexFieldReader(0);
			}
			else
			{
				reader = new IdentityFieldReader();
			}
		}
		else
		{
			int firstNestedIdx = fieldMapping.indexOf(PropertyUtils.NESTED_DELIM);

			if (firstNestedIdx >= 0 && aliasesMap.containsKey(fieldMapping.substring(0, firstNestedIdx)))
			{
				fieldMapping = fieldMapping.substring(firstNestedIdx + 1);
			}

			if (useIndex)
			{
				reader = new IndexPropertyFieldReader(0, fieldMapping);
			}
			else
			{
				reader = new PropertyFieldReader(fieldMapping);
			}
		}
		
		return reader;
	}

	protected FieldReader getFieldReader(Type[] returnTypes, Map<String,Integer> aliasesMap, JRField field)
	{
		FieldReader reader;
		
		String fieldMapping = getFieldMapping(field);
		Integer fieldIdx = aliasesMap.get(fieldMapping);
		if (fieldIdx == null)
		{
			int firstNestedIdx = fieldMapping.indexOf(PropertyUtils.NESTED_DELIM);
			
			if (firstNestedIdx < 0)
			{
				throw new JRRuntimeException("Unknown HQL return alias \"" + fieldMapping + "\".");
			}
			
			String fieldAlias = fieldMapping.substring(0, firstNestedIdx);
			String fieldProperty = fieldMapping.substring(firstNestedIdx + 1);
			
			fieldIdx = aliasesMap.get(fieldAlias);
			if (fieldIdx == null)
			{
				throw new JRRuntimeException("The HQL query does not have a \"" + fieldAlias + "\" alias.");
			}
			
			Type type = returnTypes[fieldIdx.intValue()];
			if (!type.isEntityType() && !type.isComponentType())
			{
				throw new JRRuntimeException("The HQL query does not have a \"" + fieldAlias + "\" alias.");
			}
			
			reader = new IndexPropertyFieldReader(fieldIdx.intValue(), fieldProperty);
		}
		else
		{
			reader = new IndexFieldReader(fieldIdx.intValue());
		}
		
		return reader;
	}

	
	/**
	 * Sets the current row of the query result.
	 * 
	 * @param currentReturnValue the current row value
	 */
	protected void setCurrentRowValue(Object currentReturnValue)
	{
		this.currentReturnValue = currentReturnValue;
	}
	
	
	public Object getFieldValue(JRField jrField) throws JRException
	{
		FieldReader reader = fieldReaders.get(jrField.getName());
		if (reader == null)
		{
			throw new JRRuntimeException("No filed reader for " + jrField.getName());
		}
		return reader.getFieldValue(currentReturnValue);
	}

	
	protected String getFieldMapping(JRField field)
	{
		return (useFieldDescription ? 
					JRAbstractBeanDataSource.FIELD_DESCRIPTION_PROPERTY_NAME_PROVIDER : 
					JRAbstractBeanDataSource.FIELD_NAME_PROPERTY_NAME_PROVIDER)
				.getPropertyName(field);
	}
	
	
	/**
	 * Interface used to get the value of a report field from a result row.
	 */
	protected static interface FieldReader
	{
		Object getFieldValue(Object resultValue) throws JRException;
	}
	
	protected static class IdentityFieldReader implements FieldReader
	{
		public Object getFieldValue(Object resultValue)
		{
			return resultValue;
		}	
	}
	
	protected static class IndexFieldReader implements FieldReader
	{
		private final int idx;

		protected IndexFieldReader(int idx)
		{
			this.idx = idx;
		}
		
		public Object getFieldValue(Object resultValue)
		{
			return ((Object[]) resultValue)[idx];
		}
	}
	
	protected static class PropertyFieldReader implements FieldReader
	{
		private final String property;

		protected PropertyFieldReader(String property)
		{
			this.property = property;
		}
		
		public Object getFieldValue(Object resultValue) throws JRException
		{
			return JRAbstractBeanDataSource.getBeanProperty(resultValue, property);
		}
	}
	
	protected static class IndexPropertyFieldReader implements FieldReader
	{
		private final int idx;
		private final String property;

		protected IndexPropertyFieldReader(int idx, String property)
		{
			this.idx = idx;
			this.property = property;
		}
		
		public Object getFieldValue(Object resultValue) throws JRException
		{
			return JRAbstractBeanDataSource.getBeanProperty(((Object[]) resultValue)[idx], property);
		}
	}
}

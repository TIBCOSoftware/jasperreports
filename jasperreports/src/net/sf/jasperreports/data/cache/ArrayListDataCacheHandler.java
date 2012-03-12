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
package net.sf.jasperreports.data.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.IndexedDataSource;
import net.sf.jasperreports.engine.data.ListOfArrayDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Report data cache handler that collects data in lists of arrays.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ArrayListDataCacheHandler implements DataCacheHandler
{

	private static final Log log = LogFactory.getLog(ArrayListDataCacheHandler.class);

	private boolean enabled;
	private volatile boolean populated;
	
	private Map<Object, ArrayDataCollector> cachers = new HashMap<Object, ArrayDataCollector>();
	private Map<Object, ListOfArrayDataSource> cachedDataSources = new HashMap<Object, ListOfArrayDataSource>();
	
	public ArrayListDataCacheHandler()
	{
		enabled = true;
	}
	
	public boolean isCachingEnabled()
	{
		return enabled;
	}

	public void setCachePopulated()
	{
		populated = true;
	}

	public boolean isCachePopulated()
	{
		return populated;
	}

	public DataCollector getCollector(Object key)
	{
		ArrayDataCollector cacher = cachers.get(key);
		if (cacher == null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Creating ArrayDataCacher for " + key);
			}
			
			cacher = new ArrayDataCollector(key);
			cachers.put(key, cacher);
		}

		return cacher;
	}

	public boolean hasCachedData(Object key)
	{
		ListOfArrayDataSource dataSource = cachedDataSources.get(key);
		return dataSource != null;
	}

	public IndexedDataSource getCachedData(Object key)
	{
		ListOfArrayDataSource dataSource = cachedDataSources.get(key);
		if (dataSource == null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("No cached data exists for " + key);
			}
			return null;
		}
		
		if (log.isDebugEnabled())
		{
			log.debug("Found cached data source of " + dataSource.size() + " records for " + key);
		}
		
		// rewind to make sure it starts with the first record
		dataSource.moveFirst();
		
		return dataSource;
	}
	
	protected void addDataSource(Object key, ListOfArrayDataSource dataSource)
	{
		cachedDataSources.put(key, dataSource);
	}
	
	class ArrayDataCollector implements DataCollector
	{
		private final Object key;
		protected JRField[] fields;
		protected final ArrayList<Object[]> records = new ArrayList<Object[]>();
		protected boolean ended;
		
		public ArrayDataCollector(Object key)
		{
			this.key = key;
		}

		public void init(JRField[] fields)
		{
			this.fields = fields;
			
			// remove any previous data
			records.clear();
			ended = false;
		}

		public void addRecord(Object[] values)
		{
			records.add(values);
		}

		public void end()
		{
			String[] fieldNames;
			if (fields == null)
			{
				fieldNames = new String[0];
			}
			else
			{
				fieldNames = new String[fields.length];
				for (int i = 0; i < fields.length; i++)
				{
					fieldNames[i] = fields[i].getName();
				}
			}
			
			ListOfArrayDataSource dataSource = new ListOfArrayDataSource(records, fieldNames);
			addDataSource(key, dataSource);
			
			if (log.isDebugEnabled())
			{
				log.debug("Recorded cached data source of " + records.size() + " records");
			}
			
			ended = true;
		}

		public boolean hasEnded()
		{
			return ended;
		}
	}
}

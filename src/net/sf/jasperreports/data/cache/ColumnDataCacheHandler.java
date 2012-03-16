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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import net.sf.jasperreports.engine.JRField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Report data cache handler that collects data in column stores.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ColumnDataCacheHandler implements DataCacheHandler
{

	private static final Log log = LogFactory.getLog(ColumnDataCacheHandler.class);

	private final static int DEFAULT_BUFFER_STORE_SIZE = 4096;
	
	private boolean enabled;

	private volatile DataSnapshot snapshot;
	private int bufferStoreSize = DEFAULT_BUFFER_STORE_SIZE;
	
	public ColumnDataCacheHandler()
	{
		enabled = true;
	}
	
	public boolean isRecordingEnabled()
	{
		return enabled;
	}

	public DataRecorder createDataRecorder()
	{
		if (log.isDebugEnabled())
		{
			log.debug("creating data recorder");
		}
		
		return new DataCollector();
	}

	public DataSnapshot getDataSnapshot()
	{
		return snapshot;
	}
	
	public void invalidateDataSnapshot()
	{
		this.snapshot = null;
	}
	
	protected void disableCaching()
	{
		if (log.isDebugEnabled())
		{
			log.debug("caching disabled");
		}
		
		this.enabled = false;
	}

	protected void setDataSnapshot(DataSnapshot snapshot)
	{
		this.snapshot = snapshot;
	}

	public boolean isSnapshotPopulated()
	{
		return snapshot != null;
	}
	
	protected ColumnStore createColumnStore(JRField field)
	{
		return new TypedColumnStore(this, field.getValueClass());
	}
	
	protected ColumnStore createColumnStore(Class<?> type)
	{
		BufferColumnStore bufferStore = null;
		if (Integer.class.equals(type))
		{
			bufferStore = new LongArrayStore(bufferStoreSize, NumberToIntegerTransformer.instance());
		}
		else if (Long.class.equals(type))
		{
			bufferStore = new LongArrayStore(bufferStoreSize, NumberToLongTransformer.instance());
		}
		else if (Short.class.equals(type))
		{
			bufferStore = new LongArrayStore(bufferStoreSize, NumberToShortTransformer.instance());
		}
		else if (Byte.class.equals(type))
		{
			bufferStore = new LongArrayStore(bufferStoreSize, NumberToByteTransformer.instance());
		}
		else if (Character.class.equals(type))
		{
			bufferStore = new LongArrayStore(bufferStoreSize, NumberToCharTransformer.instance());
		}
		else if (Double.class.equals(type))
		{
			bufferStore = new DoubleArrayStore(bufferStoreSize);
		}
		else if (Float.class.equals(type))
		{
			bufferStore = new FloatArrayStore(bufferStoreSize); 
		}
		else if (String.class.equals(type))
		{
			bufferStore = new ObjectArrayStore(bufferStoreSize); 
		}
		else if (Date.class.equals(type))
		{
			bufferStore = new DateStore(bufferStoreSize, NumberToDateTransformer.instance()); 
		}
		else if (java.sql.Date.class.equals(type))
		{
			bufferStore = new DateStore(bufferStoreSize, NumberToSQLDateTransformer.instance()); 
		}
		else if (Timestamp.class.equals(type))
		{
			bufferStore = new TimestampStore(bufferStoreSize);
		}
		else if (Time.class.equals(type))
		{
			bufferStore = new DateStore(bufferStoreSize, NumberToSQLTimeTransformer.instance()); 
		}
		else if (BigInteger.class.equals(type))
		{
			bufferStore = new BigIntegerStore(bufferStoreSize);
		}
		else if (BigDecimal.class.equals(type))
		{
			bufferStore = new BigDecimalStore(bufferStoreSize);
		}
		else if (Boolean.class.equals(type))
		{
			bufferStore = new BooleanStore(bufferStoreSize);
		}
		
		ColumnStore store = null;
		if (bufferStore != null)
		{
			store = new BlockColumnStore(bufferStore);
			
			if (log.isDebugEnabled())
			{
				log.debug("created block store " + store + " with buffer " + bufferStore
						+ ", buffer size " + bufferStoreSize);
			}
		}
		return store;
	}
	
	class DataCollector implements DataRecorder
	{

		private final ColumnDataSnapshot dataSnapshot;
		
		public DataCollector()
		{
			this.dataSnapshot = new ColumnDataSnapshot();
		}

		public DatasetRecorder createRecorder(Object key)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Creating ColumnDataCollector for " + key);
			}
			
			ColumnDataCollector collector = new ColumnDataCollector(this, key);
			return collector;
		}
		
		protected void addCacheData(Object key, ColumnCacheData data)
		{
			dataSnapshot.addCachedData(key, data);
		}

		public void setSnapshotPopulated()
		{
			setDataSnapshot(dataSnapshot);
		}
		
	}
	
	class ColumnDataCollector implements DatasetRecorder
	{
		private final DataCollector collector;
		private final Object key;
		private JRField[] fields;
		private ColumnStore[] columns;
		private int size;
		private boolean ended;
		
		public ColumnDataCollector(DataCollector collector, Object key)
		{
			this.collector = collector;
			this.key = key;
		}

		public void start(JRField[] datasetFields)
		{
			fields = (datasetFields == null) ? new JRField[0] : datasetFields;
			
			columns = new ColumnStore[fields.length];
			for (int i = 0; i < fields.length; i++)
			{
				JRField field = fields[i];
				ColumnStore columnStore = createColumnStore(field);
				if (columnStore == null)
				{
					if (log.isDebugEnabled())
					{
						log.debug("Field " + field.getName() + " of type " + field.getValueClassName()
								+ " from " + key + " not cacheable");
					}
					
					disableCaching();
					return;
				}
				
				if (log.isDebugEnabled())
				{
					log.debug("created store " + columnStore + " for field " + field.getName()
							+ " at key " + key);
				}
				
				columns[i] = columnStore;
			}
			
			size = 0;
			ended = false;
		}

		public void addRecord(Object[] values)
		{
			if (!isRecordingEnabled())
			{
				// nothing to do
				return;
			}
			
			if (values == null || values.length != columns.length)
			{
				throw new IllegalArgumentException();
			}
			
			for (int i = 0; i < columns.length; i++)
			{
				columns[i].addValue(values[i]);
			}
			
			++size;
		}

		public void end()
		{
			if (!isRecordingEnabled())
			{
				// nothing to do
				return;
			}
			
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
			
			ColumnValues[] values = new ColumnValues[columns.length];
			for (int i = 0; i < columns.length; i++)
			{
				values[i] = columns[i].createValues();
			}
			
			ColumnCacheData data = new ColumnCacheData(fieldNames, size, values);
			collector.addCacheData(key, data);
			
			if (log.isDebugEnabled())
			{
				log.debug("Recorded cached data source of " + size + " records");
			}
			
			ended = true;
		}

		public boolean hasEnded()
		{
			return ended;
		}
	}
}

/*
 * ============================================================================
 *                   GNU Lesser General Public License
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

/*
 * Contributors:
 * S. Brett Sutton - bsutton@idatam.com.au
 */
package net.sf.jasperreports.engine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRResultSetDataSource implements JRDataSource
{


	/**
	 *
	 */
	private ResultSet resultSet = null;
	private Map columnIndexMap = new HashMap();
	

	/**
	 *
	 */
	public JRResultSetDataSource(ResultSet rs)
	{
		resultSet = rs;
	}


	/**
	 *
	 */
	public boolean next() throws JRException
	{
		boolean hasNext = false;
		
		if (resultSet != null)
		{
			try
			{
				hasNext = resultSet.next();
			}
			catch (SQLException e)
			{
				throw new JRException("Unable to get next record.", e);
			}
		}
		
		return hasNext;
	}


	/**
	 *
	 */
	public Object getFieldValue(JRField field) throws JRException
	{
		Object objValue = null;

		if (field != null && resultSet != null)
		{
			Integer columnIndex = getColumnIndex(field.getName());
			Class clazz = field.getValueClass();

			try
			{
				if (clazz.equals(java.lang.Boolean.class))
				{
					objValue = resultSet.getBoolean(columnIndex.intValue()) ? Boolean.TRUE : Boolean.FALSE;
				}
				else if (clazz.equals(java.lang.Byte.class))
				{
					objValue = new Byte(resultSet.getByte(columnIndex.intValue()));
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.util.Date.class))
				{
					objValue = resultSet.getDate(columnIndex.intValue());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.sql.Timestamp.class))
				{
					objValue = resultSet.getTimestamp(columnIndex.intValue());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.sql.Time.class))
				{
					objValue = resultSet.getTime(columnIndex.intValue());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.lang.Double.class))
				{
					objValue = new Double(resultSet.getDouble(columnIndex.intValue()));
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.lang.Float.class))
				{
					objValue = new Float(resultSet.getFloat(columnIndex.intValue()));
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.lang.Integer.class))
				{
					objValue = new Integer(resultSet.getInt(columnIndex.intValue()));
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.io.InputStream.class))
				{
					objValue = resultSet.getBinaryStream(columnIndex.intValue());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
					else
					{
						InputStream is = (InputStream)objValue;
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] bytes = new byte[1000];
						int ln = 0;
						try
						{
							while ((ln = is.read(bytes)) > 0)
							{
								baos.write(bytes, 0, ln);
							}
							baos.flush();
						}
						finally
						{
							try
							{
								baos.close();
							}
							catch(IOException e)
							{
							}
						}
						objValue = new ByteArrayInputStream(baos.toByteArray());
					}					
				}
				else if (clazz.equals(java.lang.Long.class))
				{
					objValue = new Long(resultSet.getLong(columnIndex.intValue()));
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.lang.Short.class))
				{
					objValue = new Short(resultSet.getShort(columnIndex.intValue()));
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.math.BigDecimal.class))
				{
					objValue = resultSet.getBigDecimal(columnIndex.intValue());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.lang.String.class))
				{
					objValue = resultSet.getString(columnIndex.intValue());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
                else
                {
                    objValue = resultSet.getObject(columnIndex.intValue());
                }
			}
			catch (Exception e)
			{
				throw new JRException("Unable to get value for field '" + field.getName() + "' of class '" + clazz.getName() + "'", e);
			}
		}
		
		return objValue;
	}


	/**
	 *
	 */
	private Integer getColumnIndex(String fieldName) throws JRException
	{
		Integer columnIndex = (Integer)columnIndexMap.get(fieldName);
		if (columnIndex == null)
		{
			try
			{
				ResultSetMetaData metadata = resultSet.getMetaData();
				for(int i = 1; i <= metadata.getColumnCount(); i++)
				{
					if (fieldName.equalsIgnoreCase(metadata.getColumnName(i)))
					{
						columnIndex = new Integer(i);
						break;
					}
				}
				
				if (columnIndex == null)
				{
					if (fieldName.startsWith("COLUMN_"))
					{
						columnIndex = new Integer(fieldName.substring(7));
						if (
							columnIndex.intValue() <= 0
							|| columnIndex.intValue() > resultSet.getMetaData().getColumnCount()
							)
						{
							throw new JRException("Column index out of range : " + columnIndex);
						}
					}
					else
					{
						throw new JRException("Unknown column name : " + fieldName);
					}
				}
			}
			catch (SQLException e)
			{
				throw new JRException("Unable to retrieve result set metadata.", e);
			}

			columnIndexMap.put(fieldName, columnIndex);
		}
		
		return columnIndex;
	}


}

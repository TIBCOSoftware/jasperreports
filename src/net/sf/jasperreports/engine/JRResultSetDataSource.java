/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
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
import java.sql.SQLException;


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
			Class clazz = field.getValueClass();

			try
			{
				if (clazz.equals(java.lang.Object.class))
				{
					objValue = resultSet.getObject(field.getName());
				}
				else if (clazz.equals(java.lang.Boolean.class))
				{
					objValue = resultSet.getBoolean(field.getName()) ? Boolean.TRUE : Boolean.FALSE;
				}
				else if (clazz.equals(java.lang.Byte.class))
				{
					objValue = new Byte(resultSet.getByte(field.getName()));
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.util.Date.class))
				{
					objValue = resultSet.getDate(field.getName());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.sql.Timestamp.class))
				{
					objValue = resultSet.getTimestamp(field.getName());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.sql.Time.class))
				{
					objValue = resultSet.getTime(field.getName());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.lang.Double.class))
				{
					objValue = new Double(resultSet.getDouble(field.getName()));
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.lang.Float.class))
				{
					objValue = new Float(resultSet.getFloat(field.getName()));
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.lang.Integer.class))
				{
					objValue = new Integer(resultSet.getInt(field.getName()));
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.io.InputStream.class))
				{
					objValue = resultSet.getBinaryStream(field.getName());
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
					objValue = new Long(resultSet.getLong(field.getName()));
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.lang.Short.class))
				{
					objValue = new Short(resultSet.getShort(field.getName()));
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.math.BigDecimal.class))
				{
					objValue = resultSet.getBigDecimal(field.getName());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
				else if (clazz.equals(java.lang.String.class))
				{
					objValue = resultSet.getString(field.getName());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
				}
			}
			catch (Exception e)
			{
				throw new JRException("Unable to get value for field '" + field.getName() + "' of class '" + clazz.getName() + "'", e);
			}
		}
		
		return objValue;
	}


}

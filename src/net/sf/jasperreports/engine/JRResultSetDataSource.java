/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
package dori.jasper.engine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
					objValue = resultSet.getString(field.getName());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
					else
					{
						objValue = new Byte((String)objValue);
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
					objValue = resultSet.getString(field.getName());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
					else
					{
						objValue = new Double((String)objValue);
					}
				}
				else if (clazz.equals(java.lang.Float.class))
				{
					objValue = resultSet.getString(field.getName());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
					else
					{
						objValue = new Float((String)objValue);
					}
				}
				else if (clazz.equals(java.lang.Integer.class))
				{
					objValue = resultSet.getString(field.getName());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
					else
					{
						objValue = new Integer((String)objValue);
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
						if (is.available() > 0)
						{
							ByteArrayOutputStream baos = 
								new ByteArrayOutputStream(is.available());
							byte[] bytes = new byte[4096];
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
						else
						{
							objValue = new ByteArrayInputStream(new byte[0]);
						}
					}					
				}
				else if (clazz.equals(java.lang.Long.class))
				{
					objValue = resultSet.getString(field.getName());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
					else
					{
						objValue = new Long((String)objValue);
					}
				}
				else if (clazz.equals(java.lang.Short.class))
				{
					objValue = resultSet.getString(field.getName());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
					else
					{
						objValue = new Short((String)objValue);
					}
				}
				else if (clazz.equals(java.math.BigDecimal.class))
				{
					objValue = resultSet.getString(field.getName());
					if(resultSet.wasNull())
					{
						objValue = null;
					}
					else
					{
						objValue = new BigDecimal((String)objValue);
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

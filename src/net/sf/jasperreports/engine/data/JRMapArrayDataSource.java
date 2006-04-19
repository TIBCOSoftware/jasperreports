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

import java.util.Map;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRMapArrayDataSource implements JRRewindableDataSource
{
	

	/**
	 *
	 */
	private Object[] records = null;
	private int index = -1;
	

	/**
	 *
	 */
	public JRMapArrayDataSource(Object[] array)
	{
		records = array;
	}
	

	/**
	 *
	 */
	public boolean next()
	{
		index++;

		if (records != null)
		{
			return (index < records.length);
		}

		return false;
	}
	
	
	/**
	 *
	 */
	public Object getFieldValue(JRField field)
	{
		Object value = null;
		
		Map currentRecord = (Map)records[index];

		if (currentRecord != null)
		{
			value = currentRecord.get(field.getName());
		}

		return value;
	}

	
	/**
	 *
	 */
	public void moveFirst()
	{
		this.index = -1;
	}


}

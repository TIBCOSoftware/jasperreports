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
package net.sf.jasperreports.engine;


/**
 * A simple data source implementation that simulates a data source with a given number of virtual records inside.
 * It is called empty data source because even though it has one or more records inside, all the report fields
 * are null for all the virtual records of the data source.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JREmptyDataSource implements JRRewindableDataSource
{


	/**
	 *
	 */
	private int count = 1;
	private int index = 0;


	/**
	 *
	 */
	public JREmptyDataSource()
	{
	}


	/**
	 *
	 */
	public JREmptyDataSource(int count)
	{
		this.count = count;
	}


	/**
	 *
	 */
	public boolean next()
	{
		return (index++ < count);
	}


	/**
	 *
	 */
	public Object getFieldValue(JRField field)
	{
		return null;
	}


	/**
	 *
	 */
	public void moveFirst()
	{
		this.index = 0;
	}


}

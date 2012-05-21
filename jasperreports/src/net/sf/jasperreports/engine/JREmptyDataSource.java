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
	private int index;


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

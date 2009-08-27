/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.list;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * Standard {@link ListComponent} implementation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class StandardListComponent implements Serializable, ListComponent
{

	private static final long serialVersionUID = 1L;
	
	private JRDatasetRun datasetRun;
	private ListContents contents;

	public JRDatasetRun getDatasetRun()
	{
		return datasetRun;
	}
	
	/**
	 * Sets the subdataset run information that will be used by this list.
	 * 
	 * @param datasetRun the subdataset run information
	 * @see #getDatasetRun()
	 */
	public void setDatasetRun(JRDatasetRun datasetRun)
	{
		this.datasetRun = datasetRun;
	}
	
	public ListContents getContents()
	{
		return contents;
	}
	
	/**
	 * Sets the list item contents.
	 * 
	 * @param contents the list item contents
	 * @see #getContents()
	 */
	public void setContents(ListContents contents)
	{
		this.contents = contents;
	}
	
	public Object clone()
	{
		try
		{
			StandardListComponent clone = (StandardListComponent) super.clone();
			clone.datasetRun = (JRDatasetRun) JRCloneUtils.nullSafeClone(datasetRun);
			clone.contents = (ListContents) JRCloneUtils.nullSafeClone(contents);
			return clone;
		} 
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}
}

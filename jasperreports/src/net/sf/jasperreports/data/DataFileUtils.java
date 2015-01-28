/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.data;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DataFileUtils
{
	
	private static final Log log = LogFactory.getLog(DataFileUtils.class);

	public static DataFileUtils instance(JasperReportsContext jasperReportsContext)
	{
		return new DataFileUtils(jasperReportsContext);
	}
	
	private final JasperReportsContext jasperReportsContext;

	protected DataFileUtils(JasperReportsContext jasperReportsContext)
	{
		super();
		this.jasperReportsContext = jasperReportsContext;
	}
	
	public DataFileConnection createConnection(DataFile dataFile, String dataLocation, Map<String, Object> parameters) throws JRException
	{
		if (dataFile == null)
		{
			dataFile = new StandardRepositoryDataLocation(dataLocation);
		}
		
		DataFileResolver dataFileResolver = DataFileResolver.instance(jasperReportsContext);
		DataFileService dataFileService = dataFileResolver.getService(dataFile);
		
		DataFileConnection dataConnection = dataFileService.getDataFileConnection(parameters);
		return dataConnection;
	}
	
	public void dispose(DataFileConnection connection)
	{
		try
		{
			connection.dispose();
		}
		catch (JRRuntimeException e)//catch RuntimeException?
		{
			log.warn("Failed to dispose connection for " + connection);
		}
	}
	
}

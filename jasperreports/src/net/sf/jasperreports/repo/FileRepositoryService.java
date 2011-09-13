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
package net.sf.jasperreports.repo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.XmlUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRResourcesUtil;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

import org.exolab.castor.mapping.Mapping;
import org.xml.sax.InputSource;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class FileRepositoryService extends DefaultRepositoryService
{
	/**
	 * 
	 */
	private static final Mapping mapping = new Mapping();
	static
	{
		loadMapping(mapping, "net/sf/jasperreports/data/csv/CsvDataAdapterImpl.xml");
		loadMapping(mapping, "net/sf/jasperreports/data/ds/DataSourceDataAdapterImpl.xml");
		loadMapping(mapping, "net/sf/jasperreports/data/empty/EmptyDataAdapterImpl.xml");
		loadMapping(mapping, "net/sf/jasperreports/data/jdbc/JdbcDataAdapterImpl.xml");
		loadMapping(mapping, "net/sf/jasperreports/data/jndi/JndiDataAdapterImpl.xml");
		loadMapping(mapping, "net/sf/jasperreports/data/provider/DataSourceProviderDataAdapterImpl.xml");
		loadMapping(mapping, "net/sf/jasperreports/data/qe/QueryExecuterDataAdapterImpl.xml");
		loadMapping(mapping, "net/sf/jasperreports/data/xls/XlsDataAdapterImpl.xml");
		loadMapping(mapping, "net/sf/jasperreports/data/xml/RemoteXmlDataAdapterImpl.xml");
		loadMapping(mapping, "net/sf/jasperreports/data/xml/XmlDataAdapterImpl.xml");
	}
	
	/**
	 *
	 */
	private static void loadMapping(Mapping mapping, String mappingFile)
	{
		try
		{
			byte[] mappingFileData = JRLoader.loadBytesFromResource(mappingFile);
			InputSource mappingSource = new InputSource(new ByteArrayInputStream(mappingFileData));

			mapping.loadMapping(mappingSource);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	
	@Override
	public void saveResource(String uri, Resource resource)
	{
		if (ReportResource.class.getName().equals(resource.getClass().getName()))
		{
			SimpleFileResolver fileResolver = (SimpleFileResolver)JRResourcesUtil.getFileResolver(null);
			File rootFolder = fileResolver.getFolders().get(0);
			File jasperFile = new File(rootFolder, uri);
			
			try
			{
				JRSaver.saveObject(((ReportResource)resource).getValue(), jasperFile);
			}
			catch (JRException e)
			{
				throw new JRRuntimeException(e);
			}
		}
	}
	
	@Override
	public <K extends Resource> K getResource(String uri, Class<K> resourceType)
	{
		if (ReportResource.class.getName().equals(resourceType.getName()))
		{
			String reportUri = uri;
			String lcReportUri = reportUri.toLowerCase();
			if (lcReportUri.endsWith(".jasper"))
			{
				reportUri = reportUri.substring(0, lcReportUri.lastIndexOf(".jasper"));
			}
			else if (lcReportUri.endsWith(".jrxml"))
			{
				reportUri = reportUri.substring(0, lcReportUri.lastIndexOf(".jrxml"));
			}
			String jasperUri = reportUri + ".jasper"; 
			String jrxmlUri = reportUri + ".jrxml";
			
			InputStream jrxmlIs = getInputStream(jrxmlUri);
			InputStream jasperIs = getInputStream(jasperUri);
			boolean toCompile = false;
			if (jasperIs == null)
			{
				if (jrxmlIs == null)
				{
					throw new JRRuntimeException("Report not found: " + reportUri);
				}
				else
				{
					toCompile = true;
					//jasperFile = new File(jrxmlFile.getParentFile(), new File(reportUri).getName() + ".jasper");
				}
			}
			else
			{
				if (jrxmlIs == null)
				{
					//jrxmlFile = new File(jasperFile.getParentFile(), new File(reportUri).getName() + ".jrxml");
				}
			}

			JasperReport jasperReport = null;

			if (
				toCompile
//				!jasperFile.exists()
//				|| (jasperFile.lastModified() < jrxmlFile.lastModified())
				)
			{
				//JasperCompileManager.compileReportToFile(jrxmlFile.getAbsolutePath(), jasperFile.getAbsolutePath());
				try
				{
					jasperReport = JasperCompileManager.compileReport(jrxmlIs);
					ReportResource resource = new ReportResource();
					resource.setReport(jasperReport);
					saveResource(jasperUri, resource);
				}
				catch (JRException e)
				{
					throw new JRRuntimeException(e);
				}
			}
			else
			{
				try
				{
					jasperReport = (JasperReport)JRLoader.loadObject(jasperIs);
				}
				catch (JRException e)
				{
					throw new JRRuntimeException(e);
				}
				finally
				{
					try
					{
						jasperIs.close();
					}
					catch (IOException e)
					{
					}
				}
			}
			
//			request.getSession().setAttribute(DEFAULT_JASPER_FILE_SESSION_ATTRIBUTE, jasperFile);
//			request.getSession().setAttribute(DEFAULT_JRXML_FILE_SESSION_ATTRIBUTE, jrxmlFile);

//			jasperReport = RepositoryUtil.getReport(jasperUri);
////					null,
////					null,
////					getFileResolver()
////					);
			ReportResource resource = null;
			resource = new ReportResource();
			resource.setReport(jasperReport);
			return (K)resource;
		}
		else if (DataAdapter.class.isAssignableFrom(resourceType))
		{
			DataAdapter dataAdapter = null;
			InputStream is = getInputStream(uri);
			
			if (is != null)
			{
				try
				{
					dataAdapter = (DataAdapter)XmlUtil.read(is, mapping);
				}
				finally
				{
					if (is != null)
					{
						try
						{
							is.close();
						}
						catch (IOException e)
						{
						}
					}
				}
			}
			
			return (K)dataAdapter;
		}
		
		return (K)super.getResource(uri, resourceType);
	}
}

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.util.ThreadLocalStack;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class RepositoryUtil
{
	//private static final Log log = LogFactory.getLog(RepositoryUtil.class);

	/**
	 * @deprecated To be removed.
	 */
	private static ThreadLocalStack localContextStack = new ThreadLocalStack();
	
	/**
	 * @deprecated To be removed.
	 */
	private static final ThreadLocal<ReportContext> threadReportContext = new InheritableThreadLocal<ReportContext>();

	private AtomicReference<List<RepositoryService>> repositoryServices = new AtomicReference<List<RepositoryService>>();
	

	private JasperReportsContext jasperReportsContext;


	/**
	 *
	 */
	private RepositoryUtil(JasperReportsContext jasperReportsContext)//FIXMECONTEXT try to reuse utils as much as you can
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
	
	/**
	 *
	 */
	private static RepositoryUtil getDefaultInstance()
	{
		return new RepositoryUtil(DefaultJasperReportsContext.getInstance());
	}
	
	
	/**
	 *
	 */
	public static RepositoryUtil getInstance(JasperReportsContext jasperReportsContext)
	{
		return new RepositoryUtil(jasperReportsContext);
	}
	
	
	/**
	 * 
	 */
	private List<RepositoryService> getServices()
	{
		List<RepositoryService> cachedServices = repositoryServices.get();
		if (cachedServices != null)
		{
			return cachedServices;
		}
		
		List<RepositoryService> services = jasperReportsContext.getExtensions(RepositoryService.class);
		
		// set if not already set
		if (repositoryServices.compareAndSet(null, services))
		{
			return services;
		}
		
		// already set in the meantime by another thread
		return repositoryServices.get();
	}
	
	
	/**
	 * @deprecated Replaced by {@link #getServices()}.
	 */
	public static List<RepositoryService> getRepositoryServices()
	{
		return getDefaultInstance().getServices();
	}
	
	
	/**
	 * 
	 *
	private static RepositoryContext getRepositoryContext()
	{
		return (RepositoryContext)localContextStack.top();
	}
	
	
	/**
	 * @deprecated To be removed.
	 */
	public static void setRepositoryContext(RepositoryContext context)
	{
		List<RepositoryService> services = getRepositoryServices();
		if (services != null)
		{
			for (RepositoryService service : services)
			{
				service.setContext(context);
			}
		}
		localContextStack.push(context);
	}
	
	
	/**
	 * @deprecated To be removed.
	 */
	public static void revertRepositoryContext()
	{
		//RepositoryContext repositoryContext = getRepositoryContext();
		List<RepositoryService> services = getRepositoryServices();
		if (services != null)
		{
			for (RepositoryService service : services)
			{
				service.revertContext();//FIXMEREPO context?
			}
		}
		localContextStack.pop();
	}
	
	
	/**
	 * @deprecated To be removed.
	 */
	public static ReportContext getThreadReportContext()
	{
		return threadReportContext.get();
	}

	/**
	 * @deprecated To be removed.
	 */
	public static void setThreadReportContext(ReportContext reportContext)
	{
		threadReportContext.set(reportContext);
	}

	/**
	 * @deprecated To be removed.
	 */
	public static void resetThreadReportContext()
	{
		threadReportContext.set(null);
	}

	
	/**
	 *
	 */
	public JasperReport getReport(ReportContext reportContext, String location) throws JRException 
	{
		JasperReport jasperReport = null;
		
		JasperDesignCache cache = JasperDesignCache.getInstance(jasperReportsContext, reportContext);
		if (cache != null)
		{
			jasperReport = cache.getJasperReport(location);
		}

		if (jasperReport == null)
		{
			ReportResource resource = getResourceFromLocation(location, ReportResource.class);
			if (resource == null)
			{
				throw new JRException("Report not found at : " + location);
			}

			jasperReport = resource.getReport();

			if (cache != null)
			{
				cache.set(location, jasperReport);
			}
		}

		return jasperReport;
	}


	/**
	 * @deprecated Replaced by {@link #getReport(ReportContext, String)}.
	 */
	public static JasperReport getReport(String location) throws JRException 
	{
		return getDefaultInstance().getReport(getThreadReportContext(), location);
	}


	/**
	 * 
	 */
	public <K extends Resource> K getResourceFromLocation(String location, Class<K> resourceType) throws JRException
	{
		K resource = null;
		List<RepositoryService> services = getServices();
		if (services != null)
		{
			for (RepositoryService service : services)
			{
				resource = service.getResource(location, resourceType);
				if (resource != null)
				{
					break;
				}
			}
		}
		if (resource == null)
		{
			throw new JRException("Resource not found at : " + location);//FIXMEREPO decide whether to return null or throw exception; check everywhere
		}
		return resource;
	}


	/**
	 * @deprecated Replaced by {@link #getResourceFromLocation(String, Class)}.
	 */
	public static <K extends Resource> K getResource(String location, Class<K> resourceType) throws JRException
	{
		return getDefaultInstance().getResourceFromLocation(location, resourceType);
	}


	/**
	 *
	 */
	public InputStream getInputStreamFromLocation(String location) throws JRException
	{
		InputStream is = findInputStream(location);
		if (is == null)
		{
			throw new JRException("Input stream not found at : " + location);
		}
		return is;
	}


	/**
	 * @deprecated Replaced by {@link #getInputStreamFromLocation(String)}.
	 */
	public static InputStream getInputStream(String location) throws JRException
	{
		return getDefaultInstance().getInputStreamFromLocation(location);
	}
	
	
	/**
	 *
	 */
	private InputStream findInputStream(String location) throws JRException
	{
		InputStreamResource inputStreamResource = null;
		List<RepositoryService> services = getServices();
		if (services != null)
		{
			for (RepositoryService service : services)
			{
				inputStreamResource = service.getResource(location, InputStreamResource.class);
				if (inputStreamResource != null)
				{
					break;
				}
			}
		}
		return inputStreamResource == null ? null : inputStreamResource.getInputStream();
	}
	
	
	/**
	 *
	 */
	public byte[] getBytesFromLocation(String location) throws JRException
	{
		InputStream is = findInputStream(location);
		
		if (is == null)
		{
			throw new JRException("Byte data not found at : " + location);
		}

		ByteArrayOutputStream baos = null;

		try
		{
			baos = new ByteArrayOutputStream();

			byte[] bytes = new byte[10000];
			int ln = 0;
			while ((ln = is.read(bytes)) > 0)
			{
				baos.write(bytes, 0, ln);
			}

			baos.flush();
		}
		catch (IOException e)
		{
			throw new JRException("Error loading byte data from : " + location, e);
		}
		finally
		{
			if (baos != null)
			{
				try
				{
					baos.close();
				}
				catch(IOException e)
				{
				}
			}

			if (is != null)
			{
				try
				{
					is.close();
				}
				catch(IOException e)
				{
				}
			}
		}

		return baos.toByteArray();
	}
	
	
	/**
	 * @deprecated Replaced by {@link #getBytesFromLocation(String)}.
	 */
	public static byte[] getBytes(String location) throws JRException
	{
		return getDefaultInstance().getBytesFromLocation(location);
	}
}

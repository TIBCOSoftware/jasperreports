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
package net.sf.jasperreports.repo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class RepositoryUtil
{
	public static final String EXCEPTION_MESSAGE_KEY_BYTE_DATA_LOADING_ERROR = "repo.byte.data.loading.error";
	public static final String EXCEPTION_MESSAGE_KEY_BYTE_DATA_NOT_FOUND = "repo.byte.data.not.found";
	public static final String EXCEPTION_MESSAGE_KEY_INPUT_STREAM_NOT_FOUND = "repo.input.stream.not.found";
	public static final String EXCEPTION_MESSAGE_KEY_REPORT_NOT_FOUND = "repo.report.not.found";
	public static final String EXCEPTION_MESSAGE_KEY_RESOURCET_NOT_FOUND = "repo.resource.not.found";
	
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
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_REPORT_NOT_FOUND,
						new Object[]{location});
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
			throw 
			new JRException(
				EXCEPTION_MESSAGE_KEY_RESOURCET_NOT_FOUND,
				new Object[]{location});	//FIXMEREPO decide whether to return null or throw exception; check everywhere
		}
		return resource;
	}


	/**
	 *
	 */
	public InputStream getInputStreamFromLocation(String location) throws JRException
	{
		InputStream is = findInputStream(location);
		if (is == null)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_INPUT_STREAM_NOT_FOUND,
					new Object[]{location});
		}
		return is;
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
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_BYTE_DATA_NOT_FOUND,
					new Object[]{location});
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
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_BYTE_DATA_LOADING_ERROR,
					new Object[]{location},
					e);
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
}

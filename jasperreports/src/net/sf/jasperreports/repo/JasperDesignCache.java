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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JasperDesignCache
{
	public static final String EXCEPTION_MESSAGE_KEY_INVALID_ENTRY = "repo.invalid.entry";
	
	/**
	 * 
	 */
	private static final String PARAMETER_JASPER_DESIGN_CACHE = "net.sf.jasperreports.parameter.jasperdesign.cache";

	/**
	 * 
	 */
	private JasperReportsContext jasperReportsContext;
	private Map<String, JasperDesignReportResource> cachedResourcesMap = new HashMap<String, JasperDesignReportResource>();
	//private Map<UUID, String> cachedSubreportsMap = new HashMap<UUID, String>();

	/**
	 * 
	 */
	public static JasperDesignCache getInstance(JasperReportsContext jasperReportsContext, ReportContext reportContext)//FIXMECONTEXT a jr context change would be inconsistent
	{
		JasperDesignCache cache = null;

		if (reportContext != null)
		{
			cache = (JasperDesignCache)reportContext.getParameterValue(PARAMETER_JASPER_DESIGN_CACHE);
			
			if (cache == null)
			{
				cache = new JasperDesignCache(jasperReportsContext);
				reportContext.setParameterValue(PARAMETER_JASPER_DESIGN_CACHE, cache);
			}
		}
		
		return cache;
	}
	
	public static JasperDesignCache getExistingInstance(ReportContext reportContext)
	{
		JasperDesignCache cache = null;
		if (reportContext != null)
		{
			cache = (JasperDesignCache) reportContext.getParameterValue(PARAMETER_JASPER_DESIGN_CACHE);
		}
		return cache;
	}
	
	/**
	 * 
	 */
	private JasperDesignCache(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
	/**
	 * 
	 */
	public JasperReport getJasperReport(String uri)
	{
		JasperDesignReportResource resource = getResource(uri);
		if (resource != null)
		{
			return resource.getReport();
		}
		return null;
	}

	/**
	 * 
	 */
	public JasperDesign getJasperDesign(String uri)
	{
		JasperDesignReportResource resource = getResource(uri);
		if (resource != null)
		{
			return resource.getJasperDesign();
		}
		return null;
	}

	/**
	 * 
	 */
	public void set(String uri, JasperReport jasperReport)
	{
		JasperDesignReportResource resource = new JasperDesignReportResource();
		resource.setReport(jasperReport);
		cachedResourcesMap.put(uri, resource);
	}

	/**
	 * 
	 */
	public void set(String uri, JasperDesign jasperDesign)
	{
		JasperDesignReportResource resource = new JasperDesignReportResource();
		resource.setJasperDesign(jasperDesign);
		cachedResourcesMap.put(uri, resource);
	}

	/**
	 * 
	 */
	public void resetJasperReport(String uri)
	{
		JasperDesignReportResource resource = cachedResourcesMap.get(uri);
		if (resource != null)
		{
			resource.setReport(null);
		}
		//cachedResourcesMap.put(uri, resource);
	}

	public JasperDesignReportResource remove(String uri)
	{
		return cachedResourcesMap.remove(uri);
	}
	
	public void set(String uri, JasperDesignReportResource resource)
	{
		cachedResourcesMap.put(uri, resource);
	}
	
	public void clear()
	{
		cachedResourcesMap.clear();
	}
	
	/**
	 * 
	 */
	private JasperDesignReportResource getResource(String uri)
	{
		JasperDesignReportResource resource = cachedResourcesMap.get(uri);
		
		if (resource != null)
		{
			JasperDesign jasperDesign = resource.getJasperDesign();
			JasperReport jasperReport = resource.getReport();
			
			if (jasperDesign == null)
			{
				if (jasperReport == null)
				{
					throw 
						new JRRuntimeException(
							EXCEPTION_MESSAGE_KEY_INVALID_ENTRY,
							new Object[]{"JasperDesignCache"});
				}
				else
				{
					ByteArrayInputStream bais = null;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try
					{
						new JRXmlWriter(jasperReportsContext).write(jasperReport, baos, "UTF-8");
						bais = new ByteArrayInputStream(baos.toByteArray());
						jasperDesign = JRXmlLoader.load(bais);
						resource.setJasperDesign(jasperDesign);
					}
					catch (JRException e)
					{
						throw new JRRuntimeException(e);
					}
					finally
					{
						try
						{
							baos.close();
							if (bais != null)
							{
								bais.close();
							}
						}
						catch (IOException e)
						{
						}
					}
				}
			}
			else
			{
				if (jasperReport == null)
				{
					try
					{
						jasperReport = JasperCompileManager.getInstance(jasperReportsContext).compile(jasperDesign);
						resource.setReport(jasperReport);
					}
					catch (JRException e)
					{
						throw new JRRuntimeException(e);
					}
				}
				else
				{
					//nothing to do?
				}
			}
		}
		
		return resource;
	}


	/**
	 * 
	 */
	public Map<String, JasperDesignReportResource> getCachedResources()
	{
		return cachedResourcesMap;
	}
}

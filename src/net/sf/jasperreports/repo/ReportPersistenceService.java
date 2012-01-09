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

import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: FileRepositoryService.java 4819 2011-11-28 15:24:25Z lucianc $
 */
public class ReportPersistenceService extends CastorObjectPersistenceService
{

	/**
	 * 
	 */
	public Resource load(String uri, RepositoryService repositoryService)
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
		
		InputStreamResource jrxmlResource = repositoryService.getResource(jrxmlUri, InputStreamResource.class);
		InputStreamResource jasperResource = repositoryService.getResource(jasperUri, InputStreamResource.class);
		
		InputStream jrxmlIs = jrxmlResource == null ? null : jrxmlResource.getInputStream();
		InputStream jasperIs = jasperResource == null ? null : jasperResource.getInputStream();
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

		ReportResource resource = new ReportResource();

		JasperReport jasperReport = null;

		if (
			toCompile
//			!jasperFile.exists()
//			|| (jasperFile.lastModified() < jrxmlFile.lastModified())
			)
		{
			//JasperCompileManager.compileReportToFile(jrxmlFile.getAbsolutePath(), jasperFile.getAbsolutePath());
			try
			{
				jasperReport = JasperCompileManager.compileReport(jrxmlIs);
				resource.setReport(jasperReport);
				save(resource, jasperUri, repositoryService);//FIXMEREPO maybe we want the repository service to save, not this same persistence service
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
		
//		request.getSession().setAttribute(DEFAULT_JASPER_FILE_SESSION_ATTRIBUTE, jasperFile);
//		request.getSession().setAttribute(DEFAULT_JRXML_FILE_SESSION_ATTRIBUTE, jrxmlFile);

//		jasperReport = RepositoryUtil.getReport(jasperUri);
////				null,
////				null,
////				getFileResolver()
////				);

//		resource = new ReportResource();
//		resource.setReport(jasperReport);
		return resource;
	}

}

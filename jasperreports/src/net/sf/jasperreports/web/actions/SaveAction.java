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
package net.sf.jasperreports.web.actions;

import java.io.File;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.repo.JasperDesignReportResource;



/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class SaveAction extends AbstractAction {

	public SaveAction() {
	}

	public String getName() {
		return "save_action";
	}

	public void performAction() 
	{
//		JasperDesign jasperDesign = getJasperDesign();
		JasperDesignCache cache = JasperDesignCache.getInstance(getJasperReportsContext(), getReportContext());
		Map<String, JasperDesignReportResource> cachedResources = cache.getCachedResources();
		for (String uri : cachedResources.keySet())
		{
			JasperDesignReportResource resource = cachedResources.get(uri);
			JasperDesign jasperDesign = resource.getJasperDesign();
			if (jasperDesign != null)
			{
				JasperReport jasperReport = resource.getReport();
				String appRealPath = null;//FIXMECONTEXT WebFileRepositoryService.getApplicationRealPath();
				try
				{
					JRSaver.saveObject(jasperReport, new File(new File(new File(appRealPath), "WEB-INF/repository"), uri));//FIXMEJIVE harcoded
				}
				catch (JRException e)
				{
					throw new JRRuntimeException(e);
				}
			}
		}
	}

}

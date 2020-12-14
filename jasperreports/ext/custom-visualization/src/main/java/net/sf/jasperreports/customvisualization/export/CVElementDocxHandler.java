/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.customvisualization.export;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.ooxml.GenericElementDocxHandler;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterContext;
import net.sf.jasperreports.repo.RepositoryContext;

/**
 * @author Giulio Toffoli
 */
public class CVElementDocxHandler implements GenericElementDocxHandler
{
	private static final CVElementDocxHandler INSTANCE = new CVElementDocxHandler();
	private static final Log log = LogFactory.getLog(CVElementDocxHandler.class);

	public static CVElementDocxHandler getInstance()
	{
		return INSTANCE;
	}

	@Override
	public boolean toExport(JRGenericPrintElement element)
	{
		return true;
	}

	@Override
	public void exportElement(
		JRDocxExporterContext exporterContext,
		JRGenericPrintElement element,
		JRExporterGridCell gridCell
		)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Exporting to DOCX " + element);
		}

		try
		{
			RepositoryContext repositoryContext = exporterContext.getRepository().getRepositoryContext();
			JRPrintImage chartImage =
					CVElementImageProvider.getInstance().getImage(repositoryContext, element);
						
			JRDocxExporter exporter = (JRDocxExporter) exporterContext.getExporterRef();
	  		exporter.exportImage(exporterContext.getTableHelper(), chartImage, gridCell);
		}
		catch (Exception e)
		{
			throw new JRRuntimeException(e);
		}
	}
}

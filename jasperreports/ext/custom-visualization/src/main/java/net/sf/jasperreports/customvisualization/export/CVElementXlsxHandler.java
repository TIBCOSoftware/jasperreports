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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.ooxml.GenericElementXlsxHandler;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporterContext;
import net.sf.jasperreports.repo.RepositoryContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVElementXlsxHandler implements GenericElementXlsxHandler
{
	private static final CVElementXlsxHandler INSTANCE = new CVElementXlsxHandler();
	private static final Log log = LogFactory.getLog(CVElementXlsxHandler.class);

	public static CVElementXlsxHandler getInstance()
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
		JRXlsxExporterContext exporterContext,
		JRGenericPrintElement element,
		JRExporterGridCell gridCell,
		int colIndex,
		int rowIndex
		) throws JRException
	{
		if (log.isDebugEnabled())
		{
			log.debug("Exporting to XLSX " + element);
		}

		try
		{
			RepositoryContext repositoryContext = exporterContext.getRepository().getRepositoryContext();
			JRPrintImage chartImage =
					CVElementImageProvider.getInstance().getImage(repositoryContext, element);

			JRXlsxExporter exporter = (JRXlsxExporter) exporterContext.getExporterRef();
			exporter.exportImage(chartImage, gridCell, colIndex, rowIndex, 0, 0, null);// TODO lucianc is this OK?
		}
		catch (Exception e)
		{
			throw new JRRuntimeException(e);
		}
	}

	@Override
	public JRPrintImage getImage(JRXlsxExporterContext jrxec, JRGenericPrintElement jrgpe) throws JRException
	{
		RepositoryContext repositoryContext = jrxec.getRepository().getRepositoryContext();
		return CVElementImageProvider.getInstance().getImage(repositoryContext, jrgpe);
	}
}

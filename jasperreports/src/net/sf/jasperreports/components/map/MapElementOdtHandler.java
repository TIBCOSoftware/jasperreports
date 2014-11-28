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
package net.sf.jasperreports.components.map;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.oasis.GenericElementOdtHandler;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporterContext;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class MapElementOdtHandler implements GenericElementOdtHandler
{
	private static final MapElementOdtHandler INSTANCE = new MapElementOdtHandler();
	
	public static MapElementOdtHandler getInstance()
	{
		return INSTANCE;
	}
	
	public void exportElement(
		JROdtExporterContext exporterContext,
		JRGenericPrintElement element,
		JRExporterGridCell gridCell
		)
	{
		try
		{
			JROdtExporter exporter = (JROdtExporter)exporterContext.getExporterRef();
			exporter.exportImage(
				exporterContext.getTableBuilder(), 
				MapElementImageProvider.getImage(exporterContext.getJasperReportsContext(), element), 
				gridCell
				);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}

}

/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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

import net.sf.jasperreports.components.map.imageprovider.DefaultMapElementImageProvider;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRGridLayout;
import net.sf.jasperreports.poi.export.GenericElementXlsHandler;
import net.sf.jasperreports.poi.export.JRXlsExporter;
import net.sf.jasperreports.poi.export.JRXlsExporterContext;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class MapElementXlsHandler implements GenericElementXlsHandler
{
	private static final MapElementXlsHandler INSTANCE = new MapElementXlsHandler();
	
	public static MapElementXlsHandler getInstance()
	{
		return INSTANCE;
	}
	
	@Override
	public void exportElement(
		JRXlsExporterContext exporterContext,
		JRGenericPrintElement element,
		JRExporterGridCell gridCell,
		int colIndex,
		int rowIndex,
		int emptyCols,
		int yCutsRow, 
		JRGridLayout layout
		)
	{
		try
		{
			JRXlsExporter exporter = (JRXlsExporter)exporterContext.getExporterRef();
			JRPrintImage mapImage = DefaultMapElementImageProvider
					.getInstance()
					.getImage(exporterContext.getJasperReportsContext(), element);

			exporter.exportImage(mapImage, gridCell, colIndex, rowIndex, emptyCols, yCutsRow, layout);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}

}

/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2022 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.export;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.PrintPart;
import net.sf.jasperreports.engine.PrintParts;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class PrintPartUnrollExporterInput implements ExporterInput
{
	private List<ExporterInputItem> partItems;
	
	/**
	 * 
	 */
	public PrintPartUnrollExporterInput(ExporterInput exporterInput)
	{
		partItems = new ArrayList<>();
		
		for (ExporterInputItem item : exporterInput.getItems())
		{
			JasperPrint jasperPrint = item.getJasperPrint();
			ReportExportConfiguration configuration = item.getConfiguration();
			//SortedMap<Integer, PrintPart> parts = jasperPrint.getParts();
			if (jasperPrint.hasParts())
			{
				int startPageIndex = configuration.getStartPageIndex() == null ? 0 : configuration.getStartPageIndex();
				int endPageIndex = configuration.getEndPageIndex() == null ? jasperPrint.getPages().size() 
						: (configuration.getEndPageIndex() + 1);
				PrintParts parts = jasperPrint.getParts();
				Iterator<Map.Entry<Integer, PrintPart>> it = parts.partsIterator();
				Map.Entry<Integer, PrintPart> part = it.next();
				while (it.hasNext())
				{
					Map.Entry<Integer, PrintPart> next = it.next();
					if (Math.max(startPageIndex, part.getKey()) <= Math.min(endPageIndex, next.getKey()))
					{
						partItems.add(
								new SimpleExporterInputItem(
									new ReadOnlyPartJasperPrint(jasperPrint, part.getValue(), 
											Math.max(startPageIndex, part.getKey()), Math.min(endPageIndex, next.getKey())),
									configuration
									)
								);
					}
					part = next;
				}
				
				if (Math.max(startPageIndex, part.getKey()) <= endPageIndex)
				{
					partItems.add(
							new SimpleExporterInputItem(
								new ReadOnlyPartJasperPrint(jasperPrint, part.getValue(), 
										Math.max(startPageIndex, part.getKey()), endPageIndex),
								configuration
								)
							);
				}
			}
			else
			{
				partItems.add(item);
			}
		}
	}
	
	@Override
	public List<ExporterInputItem> getItems()
	{
		return partItems;
	}
}
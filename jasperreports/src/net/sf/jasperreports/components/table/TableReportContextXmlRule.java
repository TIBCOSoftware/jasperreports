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
package net.sf.jasperreports.components.table;

import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.XmlLoaderReportContext;

import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class TableReportContextXmlRule extends Rule
{

	private static final Log log = LogFactory.getLog(TableReportContextXmlRule.class);
	
	@Override
	public void begin(String namespace, String name, Attributes attributes)
			throws Exception
	{
		JRXmlLoader xmlLoader = getXmlLoader();
		TableComponent table = getTableComponent();
		JRDatasetRun datasetRun = table.getDatasetRun();
		String datasetName = datasetRun == null ? null : datasetRun.getDatasetName();
		
		if (log.isDebugEnabled())
		{
			log.debug("Pushing report context for dataset name " + datasetName);
		}
		
		XmlLoaderReportContext reportContext = new XmlLoaderReportContext(datasetName);
		xmlLoader.pushReportContext(reportContext);
	}

	@Override
	public void end(String namespace, String name) throws Exception
	{
		JRXmlLoader xmlLoader = getXmlLoader();
		
		if (log.isDebugEnabled())
		{
			log.debug("Popping report context");
		}
		
		xmlLoader.popReportContext();
	}
	
	protected JRXmlLoader getXmlLoader()
	{
		return (JRXmlLoader) digester.peek(digester.getCount() - 1);
	}
	
	protected TableComponent getTableComponent()
	{
		TableComponent table = null;
		int stackCount = digester.getCount();
		for (int idx = 0; idx < stackCount; ++idx)
		{
			Object stackObject = digester.peek(idx);
			if (stackObject instanceof TableComponent)
			{
				table = (TableComponent) stackObject;
				break;
			}
		}
		
		if (table == null)
		{
			throw new JRRuntimeException("Could not locate TableComponent object on the digester stack");
		}
		
		return table;
	}
	
}

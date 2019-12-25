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
package net.sf.jasperreports.engine.xml;

import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

import net.sf.jasperreports.engine.DatasetRunHolder;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DatasetRunReportContextRule<T extends DatasetRunHolder> extends Rule
{

	private static final Log log = LogFactory.getLog(DatasetRunReportContextRule.class);
	
	public static final String EXCEPTION_MESSAGE_KEY_CANNOT_LOCATE_STACK_OBJECT = "xml.digester.stack.cannot.locate.object";
	
	private Class<T> type;
	
	public DatasetRunReportContextRule(Class<T> type)
	{
		this.type = type;
	}
	
	@Override
	public void begin(String namespace, String name, Attributes attributes)
			throws Exception
	{
		JRXmlLoader xmlLoader = getXmlLoader();
		T datasetRunHolder = findOnDigesterStack();
		JRDatasetRun datasetRun = datasetRunHolder.getDatasetRun();
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
	
	@SuppressWarnings("unchecked")
	protected T findOnDigesterStack()
	{
		T element = null;
		int stackCount = digester.getCount();
		for (int idx = 0; idx < stackCount; ++idx)
		{
			Object stackObject = digester.peek(idx);
			if (type.isInstance(stackObject))
			{
				element = (T) stackObject;
				break;
			}
		}
		
		if (element == null)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_CANNOT_LOCATE_STACK_OBJECT,
					new Object[]{type.getName()});
		}
		
		return element;
	}
	

}

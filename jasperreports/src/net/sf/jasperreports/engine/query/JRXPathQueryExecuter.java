/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.query;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

/**
 * XPath query executer implementation.
 * <p/>
 * The XPath query of the report is executed against the document specified by the
 * {@link net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory#PARAMETER_XML_DATA_DOCUMENT PARAMETER_XML_DATA_DOCUMENT}
 * parameter.
 * <p/>
 * All the paramters in the XPath query are replaced by calling <code>String.valueOf(Object)</code>
 * on the parameter value.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRXPathQueryExecuter extends JRAbstractQueryExecuter
{
	private static final Log log = LogFactory.getLog(JRXPathQueryExecuter.class);
	
	private final Document document;
	
	public JRXPathQueryExecuter(JRDataset dataset, Map parametersMap)
	{
		super(dataset, parametersMap);
				
		document = (Document) getParameterValue(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT);

		if (document == null)
		{
			log.warn("The supplied org.w3c.dom.Document object is null.");
		}
		
		parseQuery();
	}

	protected String getParameterReplacement(String parameterName)
	{
		return String.valueOf(getParameterValue(parameterName));
	}

	public JRDataSource createDatasource() throws JRException
	{
		JRDataSource datasource = null;
		
		String xPath = getQueryString();
		if (document != null && xPath != null)
		{
			datasource = new JRXmlDataSource(document, xPath);
		}
		
		return datasource;
	}

	public void close()
	{
		//nothing to do
	}

	public boolean cancelQuery() throws JRException
	{
		//nothing to cancel
		return false;
	}
}

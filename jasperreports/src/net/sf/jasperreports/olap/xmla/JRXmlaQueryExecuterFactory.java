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
package net.sf.jasperreports.olap.xmla;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRQueryExecuter;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;


/**
 * @author Michael Günther (m.guenther at users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlaQueryExecuterFactory implements JRQueryExecuterFactory
{

	public final static String PARAMETER_XMLA_URL = "XMLA_URL";

	public final static String PARAMETER_XMLA_DATASOURCE = "XMLA_DATASOURCE";

	public final static String PARAMETER_XMLA_CATALOG = "XMLA_CATALOG";
	
	public final static String PARAMETER_XMLA_USER = "XMLA_USER";
	
	public final static String PARAMETER_XMLA_PASSWORD = "XMLA_PASSWORD";


	private final static Object[] XMLA_BUILTIN_PARAMETERS = { 
		PARAMETER_XMLA_URL, String.class, 
		PARAMETER_XMLA_DATASOURCE, String.class, 
		PARAMETER_XMLA_CATALOG, String.class,
		PARAMETER_XMLA_USER, String.class,
		PARAMETER_XMLA_PASSWORD, String.class,
	};

	public Object[] getBuiltinParameters()
	{
		return XMLA_BUILTIN_PARAMETERS;
	}

	public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map parameters) throws JRException
	{
		return new JRXmlaQueryExecuter(dataset, parameters);
	}

	public boolean supportsQueryParameterType(String className)
	{
		return true;
	}

}

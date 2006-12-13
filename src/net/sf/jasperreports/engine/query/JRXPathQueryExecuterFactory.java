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

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;

/**
 * XPath query executer factory.
 * <p/>
 * The factory creates {@link net.sf.jasperreports.engine.query.JRXPathQueryExecuter JRXPathQueryExecuter}
 * query executers.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRXPathQueryExecuterFactory implements JRQueryExecuterFactory
{
	/**
	 * Built-in parameter holding the value of the org.w3c.dom.Document used to run the XPath query.
	 */
	public final static String PARAMETER_XML_DATA_DOCUMENT = "XML_DATA_DOCUMENT";
	
	/**
	 * Parameter holding the format pattern used to instantiate java.util.Date instances.
	 */
	public final static String XML_DATE_PATTERN = "XML_DATE_PATTERN";
	
	/**
	 * Parameter holding the format pattern used to instantiate java.lang.Number instances.
	 */
	public final static String XML_NUMBER_PATTERN = "XML_NUMBER_PATTERN";

//	 uncomment below block when patterns will be more dedicated
	
//	/**
//	 * Parameter holding the format pattern used to instantiate java.math.BigDecimal instances.
//	 */
//	public final static String XML_BIGDECIMAL_PATTERN = "XML_BIGDECIMAL_PATTERN";
//	
//	/**
//	 * Parameter holding the format pattern used to instantiate java.math.BigInteger instances.
//	 */
//	public final static String XML_BIGINTEGER_PATTERN = "XML_BIGINTEGER_PATTERN";
//	
//	/**
//	 * Parameter holding the format pattern used to instantiate java.lang.Byte instances.
//	 */
//	public final static String XML_BYTE_PATTERN = "XML_BYTE_PATTERN";
//	
//	/**
//	 * Parameter holding the format pattern used to instantiate java.lang.Double instances.
//	 */
//	public final static String XML_DOUBLE_PATTERN = "XML_DOUBLE_PATTERN";
//	
//	/**
//	 * Parameter holding the format pattern used to instantiate java.lang.Float instances.
//	 */
//	public final static String XML_FLOAT_PATTERN = "XML_FLOAT_PATTERN";
//	
//	/**
//	 * Parameter holding the format pattern used to instantiate java.lang.Integer instances.
//	 */
//	public final static String XML_INTEGER_PATTERN = "XML_INTEGER_PATTERN";
//	
//	/**
//	 * Parameter holding the format pattern used to instantiate java.lang.Long instances.
//	 */
//	public final static String XML_LONG_PATTERN = "XML_LONG_PATTERN";
//	
//	/**
//	 * Parameter holding the format pattern used to instantiate java.lang.Short instances.
//	 */
//	public final static String XML_SHORT_PATTERN = "XML_SHORT_PATTERN";
//	
//	/**
//	 * Parameter holding the format pattern used to instantiate java.sql.Date instances.
//	 */
//	public final static String XML_SQLDATE_PATTERN = "XML_SQLDATE_PATTERN";
//	
//	/**
//	 * Parameter holding the format pattern used to instantiate java.sql.Time instances.
//	 */
//	public final static String XML_SQLTIME_PATTERN = "XML_SQLTIME_PATTERN";
//	
//	/**
//	 * Parameter holding the format pattern used to instantiate java.sql.Timestamp instances.
//	 */
//	public final static String XML_SQLTIMESTAMP_PATTERN = "XML_SQLTIMESTAMP_PATTERN";
//	
//	/**
//	 * Parameter holding the format pattern used to instantiate java.lang.String instances.
//	 */
//	public final static String XML_STRING_PATTERN = "XML_STRING_PATTERN";
	
	/**
	 * Parameter holding the value of the datasource Locale
	 */
	public final static String XML_LOCALE = "XML_LOCALE";
	
	private final static Object[] XPATH_BUILTIN_PARAMETERS = {
		PARAMETER_XML_DATA_DOCUMENT,  org.w3c.dom.Document.class,
		XML_DATE_PATTERN, java.lang.String.class,
		XML_NUMBER_PATTERN, java.lang.String.class,
		
		// uncomment below block when patterns will be more dedicated
/*		
		XML_BIGDECIMAL_PATTERN, java.lang.String.class,
		XML_BIGINTEGER_PATTERN, java.lang.String.class,
		XML_BYTE_PATTERN, java.lang.String.class,
		XML_DOUBLE_PATTERN, java.lang.String.class,
		XML_FLOAT_PATTERN, java.lang.String.class,
		XML_INTEGER_PATTERN, java.lang.String.class,
		XML_LONG_PATTERN, java.lang.String.class,
		XML_SHORT_PATTERN, java.lang.String.class,
		XML_SQLDATE_PATTERN, java.lang.String.class,
		XML_SQLTIME_PATTERN, java.lang.String.class,
		XML_SQLTIMESTAMP_PATTERN, java.lang.String.class,
		XML_STRING_PATTERN, java.lang.String.class,
*/		
		XML_LOCALE, java.util.Locale.class,
		};

	public Object[] getBuiltinParameters()
	{
		return XPATH_BUILTIN_PARAMETERS;
	}

	public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map parameters)
			throws JRException
	{
		return new JRXPathQueryExecuter(dataset, parameters);
	}

	public boolean supportsQueryParameterType(String className)
	{
		return true;
	}
}

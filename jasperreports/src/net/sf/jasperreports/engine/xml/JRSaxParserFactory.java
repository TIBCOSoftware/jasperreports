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

import javax.xml.parsers.SAXParser;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.properties.PropertyConstants;

/**
 * A factory of {@link SAXParser} objects used by JasperReports
 * parsers/digesters.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public interface JRSaxParserFactory
{

	/**
	 * A property that gives a parser factory class which should be used
	 * for parsing JRXMLs.
	 * 
	 * <p>
	 * By default, this property is set to use {@link JRReportSaxParserFactory}
	 * as report parser factory.
	 */
	@Property(
			category = PropertyConstants.CATEGORY_COMPILE,
			defaultValue = "net.sf.jasperreports.engine.xml.JRReportSaxParserFactory",
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_3_1_0
			)
	String PROPERTY_REPORT_PARSER_FACTORY = JRPropertiesUtil.PROPERTY_PREFIX + "compiler.xml.parser.factory";

	/**
	 * A property that gives a parser factory class which should be used
	 * for parsing XML exports.
	 * 
	 * <p>
	 * By default, this property is set to use {@link PrintSaxParserFactory}
	 * as XML export parser factory.
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			defaultValue = "net.sf.jasperreports.engine.xml.PrintSaxParserFactory",
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_4_1_3
			)
	String PROPERTY_PRINT_PARSER_FACTORY = JRPropertiesUtil.PROPERTY_PREFIX + "export.xml.parser.factory";

	/**
	 * A property that gives a parser factory class which should be used
	 * for parsing style templates.
	 * 
	 * <p>
	 * By default, this property is set to use {@link TemplateSaxParserFactory}
	 * as style template parser factory.
	 */
	@Property(
			category = PropertyConstants.CATEGORY_COMPILE,
			defaultValue = "net.sf.jasperreports.engine.xml.TemplateSaxParserFactory",
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_3_0
			)
	String PROPERTY_TEMPLATE_PARSER_FACTORY = JRPropertiesUtil.PROPERTY_PREFIX + "template.xml.parser.factory";
	
	/**
	 * Creates a SAX parser.
	 * 
	 * @return the created parser
	 */
	SAXParser createParser();
	
}

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
package net.sf.jasperreports.engine.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.component.ComponentsXmlParser;
import net.sf.jasperreports.engine.util.JRProperties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The default report SAX parser factory.
 * 
 * <p>
 * This factory creates a parser via the default SAX parser factory
 * (<code>javax.xml.parsers.SAXParserFactory.newInstance()</code>).
 * 
 * <p>
 * JRXMLs are always validated using W3C XML schemas.  Reports that refer
 * the JasperReports DTD (which has been deprecated) are validated using an
 * internal XML schema equivalent to the DTD.
 * 
 * <p>
 * To improve performance, XML schemas can be cached when using a Xerces
 * SAX parser.  See {@link #PROPERTY_CACHE_SCHEMAS}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRReportSaxParserFactory extends BaseSaxParserFactory
{
	
	private static final Log log = LogFactory.getLog(JRReportSaxParserFactory.class);

	@Override
	protected boolean isValidating()
	{
		return JRProperties.getBooleanProperty(JRProperties.COMPILER_XML_VALIDATION);
	}
	
	@Override
	protected List<String> getSchemaLocations()
	{
		List<String> schemas = new ArrayList<String>();
		schemas.add(getResourceURI(JRXmlConstants.JASPERREPORT_XSD_RESOURCE));
		schemas.add(getResourceURI(JRXmlConstants.JASPERREPORT_XSD_DTD_COMPAT_RESOURCE));
		
		Collection<ComponentsBundle> components = ComponentsEnvironment.getComponentBundles();
		for (Iterator<ComponentsBundle> it = components.iterator(); it.hasNext();)
		{
			ComponentsBundle componentManager = it.next();
			ComponentsXmlParser xmlParser = componentManager.getXmlParser();
			
			String schemaURI;
			String schemaResource = xmlParser.getInternalSchemaResource();
			if (schemaResource != null)
			{
				schemaURI = getResourceURI(schemaResource);
			}
			else
			{
				schemaURI = xmlParser.getPublicSchemaLocation();
			}

			if (log.isDebugEnabled())
			{
				log.debug("Adding components schema at " + schemaURI);
			}
			
			schemas.add(schemaURI);
		}
		return schemas;
	}


}

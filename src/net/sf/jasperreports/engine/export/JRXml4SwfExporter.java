/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

/*
 * Contributors:
 * Joakim Sandstr�m - sanjoa@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.util.JRProperties;


/**
 * This is a modified version of the JRXmlExporter class, which produces an XML document that is used by the Flash viewer.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRXmlExporter.java 3033 2009-08-27 11:46:22Z teodord $
 */
public class JRXml4SwfExporter extends JRXmlExporter
{
	/**
	 *
	 */
	private static final String XML4SWF_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.xml4swf.";

	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String XML4SWF_EXPORTER_KEY = JRProperties.PROPERTY_PREFIX + "xml4swf";
	

	/**
	 *
	 */
	protected String getExporterPropertiesPrefix()
	{
		return XML4SWF_EXPORTER_PROPERTIES_PREFIX;
	}

	/**
	 *
	 */
	protected String getExporterKey()
	{
		return XML4SWF_EXPORTER_KEY;
	}
}

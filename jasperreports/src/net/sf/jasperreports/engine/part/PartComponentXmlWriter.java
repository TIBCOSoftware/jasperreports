/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.part;

import java.io.IOException;

import net.sf.jasperreports.engine.JRPart;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * A component writer is responsible for producing a XML representation of
 * component instances.
 * 
 * <p>
 * Its function is inverse to the one of {@link XmlDigesterConfigurer}, which
 * transforms XML fragments into object instances.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: ComponentXmlWriter.java 5878 2013-01-07 20:23:13Z teodord $
 */
public interface PartComponentXmlWriter
{

	/**
	 * Specifies whether this component should be written out. This is useful for versioning.
	 */
	boolean isToWrite(JRPart part, JRXmlWriter reportWriter);

	/**
	 * Outputs the XML representation of a component.
	 * 
	 * @param part the part
	 * @param reportWriter the report writer to which output is to be written
	 * @throws IOException exceptions produced while writing to the
	 * output stream
	 * @see ComponentKey#getNamespacePrefix()
	 * @see JRXmlWriter#getXmlWriteHelper()
	 */
	void writeToXml(JRPart part, JRXmlWriter reportWriter) throws IOException;

}

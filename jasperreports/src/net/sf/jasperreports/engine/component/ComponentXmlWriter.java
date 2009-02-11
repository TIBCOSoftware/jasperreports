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
package net.sf.jasperreports.engine.component;

import java.io.IOException;

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
 * @version $Id$
 */
public interface ComponentXmlWriter
{

	/**
	 * Outputs the XML representation of a component.
	 * 
	 * @param componentKey the component type key
	 * @param component the component instance
	 * @param reportWriter the report writer to which output is to be written
	 * @throws IOException exceptions produced while writing to the
	 * output stream
	 * @see ComponentKey#getNamespacePrefix()
	 * @see JRXmlWriter#getXmlWriteHelper()
	 */
	void writeToXml(ComponentKey componentKey, Component component, 
			JRXmlWriter reportWriter) throws IOException;

}

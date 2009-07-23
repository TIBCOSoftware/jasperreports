/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Special thanks to Google 'Summer of Code 2005' program for supporting this development
 *
 * Contributors:
 * Majid Ali Khan - majidkk@users.sourceforge.net
 * Frank Sch�nheit - Frank.Schoenheit@Sun.COM
 */
package net.sf.jasperreports.engine.export.ooxml2;

import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.ExporterNature;
import net.sf.jasperreports.engine.util.JRProperties;


/**
 * Exports a JasperReports document to ODF format. It has character output type and exports the document to a
 * grid-based layout.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRDocxExporter extends JROfficeOpenXmlExporter
{
	protected static final String DOCX_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.docx.";

	/**
	 *
	 * @see net.sf.jasperreports.engine.export.oasis.JROpenDocumentExporter#getExporterNature(net.sf.jasperreports.engine.export.ExporterFilter)
	 */
	protected ExporterNature getExporterNature(ExporterFilter filter) {
		return new JRDocxExporterNature(filter);
	}


	protected String getExporterPropertiesPrefix()
	{
		return DOCX_EXPORTER_PROPERTIES_PREFIX;
	}

}


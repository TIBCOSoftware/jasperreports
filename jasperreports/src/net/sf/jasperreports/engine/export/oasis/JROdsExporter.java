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
 *(at your option) any later version.
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
 * Special thanks to Google 'Summer of Code 2005' program for supporting this development
 *
 * Contributors:
 * Majid Ali Khan - majidkk@users.sourceforge.net
 * Frank Schönheit - Frank.Schoenheit@Sun.COM
 */
package net.sf.jasperreports.engine.export.oasis;

import java.util.LinkedList;

import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.ExporterNature;


/**
 * Exports a JasperReports document to Open Document Spreadsheet format. It has character output type
 * and exports the document to a grid-based layout.
 *
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JROdsExporter extends JROpenDocumentExporter
{

	public JROdsExporter()
	{
		super();
		exporterPropertiesPrefix = JROpenDocumentExporter.ODS_EXPORTER_PROPERTIES_PREFIX;
	}


	/**
	 *
	 * @see net.sf.jasperreports.engine.export.oasis.JROpenDocumentExporter#getExporterNature(net.sf.jasperreports.engine.export.ExporterFilter)
	 */
	protected ExporterNature getExporterNature(ExporterFilter filter) {
		return new JROdsExporterNature(filter);
	}
}


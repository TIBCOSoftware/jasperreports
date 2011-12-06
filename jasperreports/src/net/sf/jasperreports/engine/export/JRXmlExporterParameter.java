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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Contains parameters useful for export in XML format. The generated XML has a custom structure whose DTD defintion can be
 * found in the net.sf.jasperreports.engine.dtds package of the JasperReports library. Because of this custom format it's easy
 * to import back the XML file to a {@link JasperPrint} object.
 * <p>
 * The report images can be either stored internally in the resulting XML document or as separate files on disk. The internally stored
 * images are saved as BASE64 encoded byte arrays in CDATA sections.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlExporterParameter extends JRExporterParameter
{


	/**
	 *
	 */
	protected JRXmlExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * A boolean value specifying whether images should be store internally or rather as files on disk.
	 */
	public static final JRXmlExporterParameter IS_EMBEDDING_IMAGES = new JRXmlExporterParameter("Is Embedding Images Flag");


	/**
	 * A utility parameter that allows users to specify an alternate location for the DTD file. This is useful for users
	 * who want to open the generated XML files in various editors that try to actually load the DTD for error checking.
	 * 
	 * @deprecated The parameter has been deprecated with the XML export transition to XML schema.
	 * Setting a value for the parameter doesn't have any effect other than a warning log message.
	 */
	public static final JRXmlExporterParameter DTD_LOCATION = new JRXmlExporterParameter("DTD Location");


}

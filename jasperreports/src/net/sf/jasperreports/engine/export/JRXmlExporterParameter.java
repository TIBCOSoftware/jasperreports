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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.export.XmlExporterOutput;


/**
 * Contains parameters useful for export in XML format. The generated XML has a custom structure whose DTD defintion can be
 * found in the net.sf.jasperreports.engine.dtds package of the JasperReports library. Because of this custom format it's easy
 * to import back the XML file to a {@link JasperPrint} object.
 * <p>
 * The report images can be either stored internally in the resulting XML document or as separate files on disk. The internally stored
 * images are saved as BASE64 encoded byte arrays in CDATA sections.
 *
 * @deprecated Replaced by {@link XmlExporterOutput}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
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
	 * @deprecated Replaced by {@link XmlExporterOutput#isEmbeddingImages()}.
	 */
	public static final JRXmlExporterParameter IS_EMBEDDING_IMAGES = new JRXmlExporterParameter("Is Embedding Images Flag");


}

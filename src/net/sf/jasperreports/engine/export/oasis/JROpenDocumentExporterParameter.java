/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.export.oasis;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.export.OdsExporterConfiguration;
import net.sf.jasperreports.export.OdtExporterConfiguration;


/**
 * Contains parameters useful for export in Open Document format.
 * @deprecated Replaced by {@link OdtExporterConfiguration} and {@link OdsExporterConfiguration}.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JROpenDocumentExporterParameter extends JRExporterParameter
{

	/**
	 *
	 */
	protected JROpenDocumentExporterParameter(String name)
	{
		super(name);
	}

	/**
	 * @deprecated Replaced by {@link OdtExporterConfiguration#isFlexibleRowHeight()}.
	 */
	public static final JROpenDocumentExporterParameter ODT_FLEXIBLE_ROW_HEIGHT = new JROpenDocumentExporterParameter("ODT Flexible Row Height");

	/**
	 * @deprecated Replaced by {@link OdtExporterConfiguration#PROPERTY_ODT_FLEXIBLE_ROW_HEIGHT}.
	 */
	public static final String PROPERTY_ODT_FLEXIBLE_ROW_HEIGHT = OdtExporterConfiguration.PROPERTY_ODT_FLEXIBLE_ROW_HEIGHT;

	/**
	 * @deprecated Replaced by {@link OdsExporterConfiguration#isFlexibleRowHeight()}.
	 */
	public static final JROpenDocumentExporterParameter ODS_FLEXIBLE_ROW_HEIGHT = new JROpenDocumentExporterParameter("ODS Flexible Row Height");

	/**
	 * @deprecated Replaced by {@link OdsExporterConfiguration#PROPERTY_ODS_FLEXIBLE_ROW_HEIGHT}.
	 */
	public static final String PROPERTY_ODS_FLEXIBLE_ROW_HEIGHT = OdsExporterConfiguration.PROPERTY_ODS_FLEXIBLE_ROW_HEIGHT;

}

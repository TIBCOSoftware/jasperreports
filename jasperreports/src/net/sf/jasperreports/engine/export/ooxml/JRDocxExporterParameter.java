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
package net.sf.jasperreports.engine.export.ooxml;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.util.JRProperties;


/**
 * Contains parameters useful for export in DOCX format.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRHtmlExporterParameter.java 2793 2009-05-20 12:17:11Z lucianc $
 */
public class JRDocxExporterParameter extends JRExporterParameter
{


	/**
	 *
	 */
	protected JRDocxExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * Indicates whether {@link JRPrintFrame frames} are to be exported as nested tables.
	 * <p>
	 * The type of the parameter is <code>java.lang.Boolean</code>.
	 * </p>
	 * <p>
	 * Is set to <code>false</code>, the frame contents will be integrated into the master/page table.
	 * </p>
	 * @see #PROPERTY_FRAMES_AS_NESTED_TABLES
	 */
	public static final JRDocxExporterParameter FRAMES_AS_NESTED_TABLES = new JRDocxExporterParameter("Export Frames as Nested Tables");
	

	/**
	 * This property serves as default value for the {@link #FRAMES_AS_NESTED_TABLES FRAMES_AS_NESTED_TABLES}
	 * export parameter.
	 * <p>
	 * The property itself defaults to <code>true</code>.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_FRAMES_AS_NESTED_TABLES = JRProperties.PROPERTY_PREFIX + "export.docx.frames.as.nested.tables";
	

	/**
	 * Indicates whether table rows can grow if more text is added into cells.
	 * <p>
	 * The type of the parameter is <code>java.lang.Boolean</code>.
	 * </p>
	 * <p>
	 * Is set to <code>false</code>, the table rows do not increase in height automatically and the user has to enlarge them manually.
	 * </p>
	 * @see #PROPERTY_FLEXIBLE_ROW_HEIGHT
	 */
	public static final JRDocxExporterParameter FLEXIBLE_ROW_HEIGHT = new JRDocxExporterParameter("Flexible Row Height");
	

	/**
	 * This property serves as default value for the {@link #FLEXIBLE_ROW_HEIGHT FLEXIBLE_ROW_HEIGHT}
	 * export parameter.
	 * <p>
	 * The property itself defaults to <code>false</code>.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_FLEXIBLE_ROW_HEIGHT = JRProperties.PROPERTY_PREFIX + "export.docx.flexible.row.height";
	

}

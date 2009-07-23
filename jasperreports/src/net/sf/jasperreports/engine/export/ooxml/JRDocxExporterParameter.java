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
	public static final JRDocxExporterParameter FRAMES_AS_NESTED_TABLES = new JRDocxExporterParameter("Export frames as nested tables");
	

	/**
	 * This property serves as default value for the {@link #FRAMES_AS_NESTED_TABLES FRAMES_AS_NESTED_TABLES}
	 * export parameter.
	 * <p>
	 * The propery itself defaults to <code>true</code>.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_FRAMES_AS_NESTED_TABLES = JRProperties.PROPERTY_PREFIX + "export.docx.frames.as.nested.tables";
	

}

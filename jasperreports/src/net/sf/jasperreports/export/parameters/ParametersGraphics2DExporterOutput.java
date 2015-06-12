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
package net.sf.jasperreports.export.parameters;

import java.awt.Graphics2D;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.export.Graphics2DExporterOutput;


/**
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ParametersGraphics2DExporterOutput implements Graphics2DExporterOutput
{
	public static final String EXCEPTION_MESSAGE_KEY_NO_OUTPUT_SPECIFIED = "export.parameters.graphics2d.no.output.specified";
	
	/**
	 * 
	 */
	private Graphics2D grx;
	
	/**
	 * 
	 */
	public ParametersGraphics2DExporterOutput(Map<JRExporterParameter, Object> parameters)
	{
		grx = (Graphics2D)parameters.get(JRGraphics2DExporterParameter.GRAPHICS_2D);
		if (grx == null)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_NO_OUTPUT_SPECIFIED,
					(Object[])null);
		}
	}

	/**
	 * 
	 */
	public Graphics2D getGraphics2D()
	{
		return grx;
	}
}

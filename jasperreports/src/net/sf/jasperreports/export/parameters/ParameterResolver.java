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

import net.sf.jasperreports.engine.JRExporterParameter;


/**
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface ParameterResolver
{
	/**
	 * 
	 */
	public String getStringParameter(JRExporterParameter parameter, String property);
	
	/**
	 * 
	 */
	public String[] getStringArrayParameter(JRExporterParameter parameter, String propertyPrefix);

	/**
	 * 
	 */
	public String getStringParameterOrDefault(JRExporterParameter parameter, String property);
	
	/**
	 * 
	 */
	public boolean getBooleanParameter(JRExporterParameter parameter, String property, boolean defaultValue);
	
	/**
	 * 
	 */
	public int getIntegerParameter(JRExporterParameter parameter, String property, int defaultValue);

	/**
	 * 
	 */
	public float getFloatParameter(JRExporterParameter parameter, String property, float defaultValue);

	/**
	 * 
	 */
	public Character getCharacterParameter(JRExporterParameter parameter, String property);
}

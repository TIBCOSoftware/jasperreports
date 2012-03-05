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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.xml.JRXmlTemplateLoader;


/**
 * A static template reference, consisting of a location from which the template
 * can be loaded.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JRTemplate#getIncludedTemplates()
 * @see JRXmlTemplateLoader#load(String)
 */
public class JRTemplateReference
{

	private String location;

	/**
	 * Creates an empty reference.
	 */
	public JRTemplateReference()
	{
	}

	/**
	 * Creates a reference for a specific location.
	 * 
	 * @param location the template location
	 */
	public JRTemplateReference(String location)
	{
		this.location = location;
	}

	/**
	 * Returns the template location.
	 * 
	 * @return the template location
	 */
	public String getLocation()
	{
		return location;
	}
	
	/**
	 * Sets the template location.
	 * 
	 * @param location the location of the template
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	
}

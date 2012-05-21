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
package net.sf.jasperreports.engine.base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.PrintElementVisitor;

/**
 * A basic implementation of {@link JRGenericPrintElement}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseGenericPrintElement extends JRBasePrintElement 
		implements JRGenericPrintElement
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private JRGenericElementType genericType;
	private Map<String,Object> parameters = new LinkedHashMap<String,Object>();

	/**
	 * Creates a generic print element.
	 * 
	 * @param defaultStyleProvider the default style provider to use for the element
	 * @see JasperPrint#getDefaultStyleProvider()
	 */
	public JRBaseGenericPrintElement(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
	}

	public JRGenericElementType getGenericType()
	{
		return genericType;
	}
	
	/**
	 * Sets the type of this element.
	 * 
	 * @param genericType the type of the element
	 * @see #getGenericType()
	 */
	public void setGenericType(JRGenericElementType genericType)
	{
		this.genericType = genericType;
	}

	public Set<String> getParameterNames()
	{
		return parameters.keySet();
	}

	public Object getParameterValue(String name)
	{
		return parameters.get(name);
	}

	public boolean hasParameter(String name)
	{
		return parameters.containsKey(name);
	}

	public void setParameterValue(String name, Object value)
	{
		parameters.put(name, value);
	}

	public <T> void accept(PrintElementVisitor<T> visitor, T arg)
	{
		visitor.visit(this, arg);
	}

}

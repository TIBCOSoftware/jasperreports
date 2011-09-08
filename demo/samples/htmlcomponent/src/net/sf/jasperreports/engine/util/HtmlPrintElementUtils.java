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
package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRSingletonCache;

/**
 * Helper class used to instantiate {@link HtmlPrintElement}.
 * <p/>
 * The {@link HtmlPrintElementFactory html print element factory} class name is given by the
 * {@link #PROPERTY_HTML_PRINTELEMENT_FACTORY net.sf.jasperreports.html.printelement.factory} property.
 * The class should have a public default constructor so that it can be instantiated via reflection.
 * <p/>
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public final class HtmlPrintElementUtils
{
	
	/**
	 * Property that holds the {@link HtmlPrintElementFactory html print element factory} class name.
	 */
	public static final String PROPERTY_HTML_PRINTELEMENT_FACTORY = JRProperties.PROPERTY_PREFIX + "html.printelement.factory";
	
	private static final JRSingletonCache cache = new JRSingletonCache(HtmlPrintElementFactory.class);
	
	
	/**
	 * Return an {@link HtmlPrintElementFactory factory} instance.
	 * 
	 * @return a HtmlPrintElementFactory instance
	 * @throws JRException if the {@link #PROPERTY_HTML_PRINTELEMENT_FACTORY factory property} is not defined
	 * or the factory cannot be instantiated.
	 */
	public static HtmlPrintElementFactory getHtmlPrintElementFactory() throws JRException
	{
		String factoryClassName = JRProperties.getProperty(PROPERTY_HTML_PRINTELEMENT_FACTORY);
		if (factoryClassName == null)
		{
			factoryClassName = DefaultHtmlPrintElementFactory.class.getName();
		}
		
		return (HtmlPrintElementFactory) cache.getCachedInstance(factoryClassName);
	}
	
	
	/**
	 * Produces an {@link HtmlPrintElement} instance by means of the factory
	 * returned by {@link #getHtmlPrintElementFactory() getHtmlPrintElementFactory()}.
	 * 
	 * @return an HtmlPrintElement instance
	 * @throws JRException if the {@link #PROPERTY_HTML_PRINTELEMENT_FACTORY html print element property} is not defined
	 * or the factory cannot be instantiated.
	 */
	public static HtmlPrintElement getHtmlPrintElement() throws JRException
	{
		HtmlPrintElementFactory printElementFactory = getHtmlPrintElementFactory();
		return printElementFactory.getHtmlPrintElement();
	}
	

	private HtmlPrintElementUtils()
	{
	}
}

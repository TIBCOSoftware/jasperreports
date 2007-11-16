/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRText;
import net.sf.jasperreports.engine.fill.JRTextMeasurer;
import net.sf.jasperreports.engine.fill.JRTextMeasurerFactory;

/**
 * Text measurer utility class.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 * @see JRTextMeasurer
 * @see JRTextMeasurerFactory
 */
public class JRTextMeasurerUtil
{

	/**
	 * Property that specifies a text measurer factory.
	 * 
	 * <p>
	 * This property can either hold the name of a text measurer factory class, e.g.
	 * <code>net.sf.jasperreports.text.measurer.factory=org.me.MyTextMeasurerFactory</code>,
	 * or hold an alias of a text measurer factory class property, e.g.
	 * <code>
	 * net.sf.jasperreports.text.measurer.factory=myTextMeasurer
	 * ...
	 * net.sf.jasperreports.text.measurer.factory.myTextMeasurer=org.me.MyTextMeasurerFactory
	 * </code>
	 * </p>
	 * 
	 * @see JRTextMeasurerFactory
	 */
	public static final String PROPERTY_TEXT_MEASURER_FACTORY = 
		JRProperties.PROPERTY_PREFIX + "text.measurer.factory";
	
	private static final JRSingletonCache cache = new JRSingletonCache(JRTextMeasurerFactory.class);
	
	/**
	 * Creates a text measurer for a text object.
	 * 
	 * <p>
	 * If the text objectis an instance of {@link JRPropertiesHolder}, its properties
	 * are used when determining the text measurer factory.
	 * </p>
	 * 
	 * @param text the text object
	 * @return a text measurer for the text object
	 */
	public static JRTextMeasurer createTextMeasurer(JRText text)
	{
		JRPropertiesHolder propertiesHolder =
			text instanceof JRPropertiesHolder ? (JRPropertiesHolder) text : null;
		return createTextMeasurer(text, propertiesHolder);
	}
	
	/**
	 * Creates a text measurer for a text object.
	 * 
	 * @param text the text object
	 * @param propertiesHolder the properties to use for determining the text measurer factory;
	 * can be <code>null</code>
	 * @return a text measurer for the text object
	 */
	public static JRTextMeasurer createTextMeasurer(JRText text, JRPropertiesHolder propertiesHolder)
	{
		JRTextMeasurerFactory factory = getTextMeasurerFactory(propertiesHolder);
		return factory.createMeasurer(text);
	}
	
	/**
	 * Returns the text measurer factory given a set of properties.
	 * 
	 * @param propertiesHolder the properties holder
	 * @return the text measurer factory
	 */
	public static JRTextMeasurerFactory getTextMeasurerFactory(JRPropertiesHolder propertiesHolder)
	{
		String factoryClass = getTextMeasurerFactoryClass(propertiesHolder);
		try
		{
			return (JRTextMeasurerFactory) cache.getCachedInstance(factoryClass);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	protected static String getTextMeasurerFactoryClass(JRPropertiesHolder propertiesHolder)
	{
		String factory = JRProperties.getProperty(propertiesHolder, PROPERTY_TEXT_MEASURER_FACTORY);
		String factoryClassProperty = PROPERTY_TEXT_MEASURER_FACTORY + '.' + factory;
		String factoryClass = JRProperties.getProperty(propertiesHolder, factoryClassProperty);
		if (factoryClass == null)
		{
			factoryClass = factory;
		}
		return factoryClass;
	}
	
}

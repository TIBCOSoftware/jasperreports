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
package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fill.JRMeasuredText;
import net.sf.jasperreports.engine.fill.JRTextMeasurer;

/**
 * Text measurer utility class.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see JRTextMeasurer
 * @see JRTextMeasurerFactory
 */
public final class JRTextMeasurerUtil
{
	private final JasperReportsContext jasperReportsContext;
	private final JRStyledTextAttributeSelector noBackcolorSelector;//FIXMECONTEXT make this a context object everywhere and retrieve using a constant key
	private final JRStyledTextUtil styledTextUtil;


	/**
	 *
	 */
	private JRTextMeasurerUtil(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.noBackcolorSelector = JRStyledTextAttributeSelector.getNoBackcolorSelector(jasperReportsContext);
		this.styledTextUtil = JRStyledTextUtil.getInstance(jasperReportsContext);
	}
	
	
	/**
	 *
	 */
	public static JRTextMeasurerUtil getInstance(JasperReportsContext jasperReportsContext)
	{
		return new JRTextMeasurerUtil(jasperReportsContext);
	}
	
	
	/**
	 * Property that specifies a text measurer factory.
	 * 
	 * <p>
	 * This property can either hold the name of a text measurer factory class, e.g.
	 * <code>
	 * <pre>
	 * net.sf.jasperreports.text.measurer.factory=org.me.MyTextMeasurerFactory
	 * </pre>
	 * </code>
	 * or hold an alias of a text measurer factory class property, e.g.
	 * <code>
	 * <pre>
	 * net.sf.jasperreports.text.measurer.factory=myTextMeasurer
	 * ...
	 * net.sf.jasperreports.text.measurer.factory.myTextMeasurer=org.me.MyTextMeasurerFactory
	 * </pre>
	 * </code>
	 * </p>
	 * 
	 * @see JRTextMeasurerFactory
	 */
	public static final String PROPERTY_TEXT_MEASURER_FACTORY = 
		JRPropertiesUtil.PROPERTY_PREFIX + "text.measurer.factory";
	
	@SuppressWarnings("deprecation")
	private static final JRSingletonCache<net.sf.jasperreports.engine.fill.JRTextMeasurerFactory> cache = 
			new JRSingletonCache<net.sf.jasperreports.engine.fill.JRTextMeasurerFactory>(net.sf.jasperreports.engine.fill.JRTextMeasurerFactory.class);
	
	/**
	 * Creates a text measurer for a text object.
	 * 
	 * <p>
	 * If the text object is an instance of {@link JRPropertiesHolder}, its properties
	 * are used when determining the text measurer factory.
	 * </p>
	 * 
	 * @param text the text object
	 * @return a text measurer for the text object
	 */
	public JRTextMeasurer createTextMeasurer(JRCommonText text)
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
	public JRTextMeasurer createTextMeasurer(JRCommonText text, JRPropertiesHolder propertiesHolder)
	{
		JRTextMeasurerFactory factory = getFactory(propertiesHolder);
		return factory.createMeasurer(jasperReportsContext, text);
	}
	
	/**
	 * @deprecated Replaced by {@link #getFactory(JRPropertiesHolder)}.
	 */
	public net.sf.jasperreports.engine.fill.JRTextMeasurerFactory getTextMeasurerFactory(JRPropertiesHolder propertiesHolder)
	{
		return getFactory(propertiesHolder);
	}

	/**
	 * Returns the text measurer factory given a set of properties.
	 * 
	 * @param propertiesHolder the properties holder
	 * @return the text measurer factory
	 */
	public JRTextMeasurerFactory getFactory(JRPropertiesHolder propertiesHolder)
	{
		String factoryClass = getTextMeasurerFactoryClass(propertiesHolder);
		try
		{
			@SuppressWarnings("deprecation")
			net.sf.jasperreports.engine.fill.JRTextMeasurerFactory factory = cache.getCachedInstance(factoryClass);
			if (factory instanceof JRTextMeasurerFactory)
			{
				return (JRTextMeasurerFactory)factory;
			}
			
			return new WrappingTextMeasurerFactory(factory);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	protected String getTextMeasurerFactoryClass(JRPropertiesHolder propertiesHolder)
	{
		String factory = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(propertiesHolder, PROPERTY_TEXT_MEASURER_FACTORY);
		String factoryClassProperty = PROPERTY_TEXT_MEASURER_FACTORY + '.' + factory;
		String factoryClass = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(propertiesHolder, factoryClassProperty);
		if (factoryClass == null)
		{
			factoryClass = factory;
		}
		return factoryClass;
	}

	
	/**
	 * 
	 */
	public void measureTextElement(JRPrintText printText)
	{
		String text = styledTextUtil.getTruncatedText(printText);
		
		JRTextMeasurer textMeasurer = createTextMeasurer(printText);//FIXME use element properties?
		
		if (text == null)
		{
			text = "";
		}
		JRStyledText styledText = 
			JRStyledTextParser.getInstance().getStyledText(
				noBackcolorSelector.getStyledTextAttributes(printText), 
				text, 
				JRCommonText.MARKUP_STYLED_TEXT.equals(printText.getMarkup()),//FIXMEMARKUP only static styled text appears on preview. no other markup
				JRStyledTextAttributeSelector.getTextLocale(printText)
				);
		
		JRMeasuredText measuredText = textMeasurer.measure(
				styledText, 
				0,
				0,
				false
				);
		printText.setTextHeight(measuredText.getTextHeight() < printText.getHeight() ? measuredText.getTextHeight() : printText.getHeight());
		printText.setLeadingOffset(measuredText.getLeadingOffset());
		printText.setLineSpacingFactor(measuredText.getLineSpacingFactor());
		
		int textEnd = measuredText.getTextOffset();
		String printedText;
		if (JRCommonText.MARKUP_STYLED_TEXT.equals(printText.getMarkup()))
		{
			printedText = JRStyledTextParser.getInstance().write(styledText, 0, textEnd);
		}
		else
		{
			printedText = text.substring(0, textEnd);
		}
		printText.setText(printedText);
	}

	/**
	 * @deprecated To be removed.
	 */
	public static class WrappingTextMeasurerFactory implements JRTextMeasurerFactory
	{
		private net.sf.jasperreports.engine.fill.JRTextMeasurerFactory factory;
		
		public WrappingTextMeasurerFactory(net.sf.jasperreports.engine.fill.JRTextMeasurerFactory factory)
		{
			this.factory = factory;
		}

		public JRTextMeasurer createMeasurer(JRCommonText text) {
			return factory.createMeasurer(text);
		}

		public JRTextMeasurer createMeasurer(
				JasperReportsContext jasperReportsContext, JRCommonText text) {
			return factory.createMeasurer(text);
		}
	}
}

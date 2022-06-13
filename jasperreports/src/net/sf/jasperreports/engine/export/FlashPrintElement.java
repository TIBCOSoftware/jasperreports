/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2022 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.export;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.base.JRBaseGenericPrintElement;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.properties.PropertyConstants;

/**
 * Utility class that creates generic print elements of Flash type.
 * 
 * <p>
 * Such an element has a parameter that provides the URL of the SWF movie,
 * and a list of parameters that acts a Flash variables.
 * </p>
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see FlashHtmlHandler
 * @deprecated To be removed.
 */
public final class FlashPrintElement
{
	
	/**
	 * The name of Flash generic elements.
	 */
	public static final String FLASH_ELEMENT_NAME = "flash";
	
	/**
	 * The qualified type of Flash generic elements. 
	 */
	public static final JRGenericElementType FLASH_ELEMENT_TYPE = 
		new JRGenericElementType(JRXmlConstants.JASPERREPORTS_NAMESPACE, FLASH_ELEMENT_NAME);
	
	/**
	 * The name of the parameter that provides the URL of the SWF movie.
	 */
	public static final String PARAMETER_SWF_URL = "SWF_URL";
	
	/**
	 * The prefix of parameter names that acts as Flash variables.
	 */
	public static final String PARAMETER_FLASH_VAR_PREFIX = "FLASH_VAR_";

	/**
	 * Boolean property used to avoid setting the width and height of the SWFLoader control in the Flash report viewer.
	 * <p>
	 * Defaults to <code>false</code>.
	 */
	@Deprecated 
	public static final String PROPERTY_IGNORE_SIZE = JRPropertiesUtil.PROPERTY_PREFIX + "export.swf.ignore.size";
	
	/**
	 * Property that determines the value of the allowScriptAccess parameter for Flash elements.
	 * 
	 * <p>
	 * The property can be set at element, report and system levels.
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			defaultValue = PropertyConstants.SAME_DOMAIN,
			scopes = {PropertyScope.CONTEXT, PropertyScope.REPORT, PropertyScope.GENERIC_ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_1_3
			)
	public static final String PROPERTY_ALLOW_SCRIPT_ACCESS = 
			JRPropertiesUtil.PROPERTY_PREFIX + "export.flash.element.allow.script.access";
	
	private static final GenericElementHandlerBundle HANDLER_BUNDLE = new GenericElementHandlerBundle()
	{
		@Override
		public String getNamespace()
		{
			return JRXmlConstants.JASPERREPORTS_NAMESPACE;
		}
		
		@Override
		public GenericElementHandler getHandler(String elementName,
				String exporterKey)
		{
			if (FLASH_ELEMENT_NAME.equals(elementName) 
					&& HtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey))
			{
				return FlashHtmlHandler.getInstance();
			}
			return null;
		}
	};
	
	/**
	 * Returns the bundle of export handlers for Flash elements.
	 * 
	 * @return Flash elements export handler bundle
	 */
	public static GenericElementHandlerBundle getHandlerBundle()
	{
		return HANDLER_BUNDLE;
	}
	
	/**
	 * Creates a Flash generic element by copying all base element attributes
	 * from a template instance.
	 * 
	 * @param template the element from which to copy base attributes
	 * @param swfUrl the URL of the SWF movie
	 * @param flashVars a map of Flash variables
	 * @param elementParameters additional parameters to be set on the Flash element.
	 * Hyperlink objects need to be set as element parameters.
	 * @return a Flash generic element
	 */
	public static JRGenericPrintElement makeFlashElement(JRPrintElement template,
			String swfUrl, Map<String,Object> flashVars, Map<String,Object> elementParameters)
	{
		// TODO use JRTemplateGenericElement
		JRBaseGenericPrintElement flashEl = new JRBaseGenericPrintElement(
				template.getDefaultStyleProvider());
		// copy all attribute from the template element
		flashEl.setUUID(template.getUUID());
		flashEl.setX(template.getX());
		flashEl.setY(template.getY());
		flashEl.setWidth(template.getWidth());
		flashEl.setHeight(template.getHeight());
		flashEl.setStyle(template.getStyle());
		flashEl.setMode(template.getOwnModeValue());
		flashEl.setBackcolor(template.getOwnBackcolor());
		flashEl.setForecolor(template.getOwnForecolor());
		flashEl.setOrigin(template.getOrigin());
		flashEl.setKey(template.getKey());
		
		flashEl.setGenericType(FLASH_ELEMENT_TYPE);
		flashEl.setParameterValue(PARAMETER_SWF_URL, swfUrl);
		for (Iterator<Map.Entry<String,Object>> it = flashVars.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<String,Object> entry = it.next();
			String name = entry.getKey();
			Object value = entry.getValue();
			String paramName = PARAMETER_FLASH_VAR_PREFIX + name;
			flashEl.setParameterValue(paramName, value);
		}
		
		if (elementParameters != null && !elementParameters.isEmpty())
		{
			for (Iterator<Map.Entry<String,Object>> it = elementParameters.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry<String,Object> entry = it.next();
				String name = entry.getKey();
				Object value = entry.getValue();
				flashEl.setParameterValue(name, value);
			}
		}
		
		return flashEl;
	}
	
	/**
	 * @deprecated Replaced by {@link HyperlinkUtil#makeLinkParameterName(JRPrintHyperlink)}.
	 */
	public static String makeLinkParameterName(JRPrintHyperlink hyperlink)
	{
		return HyperlinkUtil.makeLinkParameterName(hyperlink);
	}
	
	/**
	 * @deprecated Replaced by {@link HyperlinkUtil#makeLinkPlaceholder(JRPrintHyperlink)}.
	 */
	public static String makeLinkPlaceholder(JRPrintHyperlink hyperlink)
	{
		return HyperlinkUtil.makeLinkPlaceholder(hyperlink);
	}
	
	/**
	 * @deprecated Replaced by {@link HyperlinkUtil#makeLinkPlaceholder(String)}.
	 */
	public static String makeLinkPlaceholder(String linkId)
	{
		return HyperlinkUtil.makeLinkPlaceholder(linkId);
	}
	
	/**
	 * @deprecated Replaced by {@link HyperlinkUtil#LINK_PATTERN}.
	 */
	protected static final Pattern LINK_PATTERN = HyperlinkUtil.LINK_PATTERN;

	/**
	 * @deprecated Replaced by {@link HyperlinkUtil#LINK_PARAM_NAME_GROUP}.
	 */
	protected static final int LINK_PARAM_NAME_GROUP = HyperlinkUtil.LINK_PARAM_NAME_GROUP;

	/**
	 * @deprecated Replaced by {@link HyperlinkUtil#resolveLinks(String, JRGenericPrintElement, JRHyperlinkProducer, boolean)}.
	 */
	public static String resolveLinks(
		String text,
		JRGenericPrintElement element,
		JRHyperlinkProducer linkProducer,
		boolean prepareForSerialization
		)
	{
		return HyperlinkUtil.resolveLinks(text, element, linkProducer, prepareForSerialization);
	}
	
	
	private FlashPrintElement()
	{
	}
}

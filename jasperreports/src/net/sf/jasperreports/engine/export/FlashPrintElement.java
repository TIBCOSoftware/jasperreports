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
package net.sf.jasperreports.engine.export;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseGenericPrintElement;
import net.sf.jasperreports.engine.util.HyperlinkData;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 */
public final class FlashPrintElement
{
	
	private static final Log log = LogFactory.getLog(FlashPrintElement.class);
	
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
	public static final String PROPERTY_IGNORE_SIZE = JRPropertiesUtil.PROPERTY_PREFIX + "export.swf.ignore.size";
	
	/**
	 * Property that determines the value of the allowScriptAccess parameter for Flash elements.
	 * 
	 * <p>
	 * The property can be set at element, report and system levels.
	 */
	public static final String PROPERTY_ALLOW_SCRIPT_ACCESS = 
			JRPropertiesUtil.PROPERTY_PREFIX + "export.flash.element.allow.script.access";
	
	private static final GenericElementHandlerBundle HANDLER_BUNDLE = new GenericElementHandlerBundle()
	{
		public String getNamespace()
		{
			return JRXmlConstants.JASPERREPORTS_NAMESPACE;
		}
		
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
	 * Returns the name of the parameter to be used for a hyperlink, as used
	 * by {@link #makeLinkPlaceholder(JRPrintHyperlink)} and
	 * {@link #resolveLinks(String, JRGenericPrintElement, JRHyperlinkProducer, boolean)}.
	 * 
	 * @param hyperlink the hyperlink
	 * @return the hyperlink parameter name
	 * @see #makeLinkPlaceholder(JRPrintHyperlink)
	 */
	public static String makeLinkParameterName(JRPrintHyperlink hyperlink)
	{
		return "link-" + System.identityHashCode(hyperlink);
	}
	
	/**
	 * Returns a placeholder to be used in a Flash variable for a hyperlink.
	 * 
	 * <p>
	 * This method uses <code>System.identityHashCode(hyperlink)</code> as link Id.
	 * </p>
	 * 
	 * @param hyperlink the hyperlink
	 * @return the link placeholder
	 * @see #makeLinkPlaceholder(String)
	 */
	public static String makeLinkPlaceholder(JRPrintHyperlink hyperlink)
	{
		String id = makeLinkParameterName(hyperlink);
		return makeLinkPlaceholder(id);
	}
	
	/**
	 * Returns a placeholder to be used in a Flash variable for a hyperlink.
	 * 
	 * <p>
	 * The placeholders will be resolved to links at export time by
	 * {@link #resolveLinks(String, JRGenericPrintElement, JRHyperlinkProducer, boolean)}.
	 * </p>
	 * 
	 * @param linkId the Id of the link, which needs to be used as hyperlink
	 * parameter name
	 * @return the link placeholder
	 */
	public static String makeLinkPlaceholder(String linkId)
	{
		return "{" + linkId + "}";
	}
	
	protected static final Pattern LINK_PATTERN = 
		Pattern.compile("\\{(link\\-[\\-\\w]+)\\}");
	
	protected static final int LINK_PARAM_NAME_GROUP = 1;

	/**
	 * Resolves hyperlink placeholders to URLs in a Flash variable.
	 * 
	 * @param text the text in which hyperlink placeholders are to be replaced
	 * @param element the print element where hyperlink parameters will be looked for
	 * @param linkProducer the hyperlink producer which transforms hyperlink
	 * objects to String URLs
	 * @return the text with hyperlink placeholders replaced by URLs
	 * @see #makeLinkPlaceholder(String)
	 */
	public static String resolveLinks(String text,
			JRGenericPrintElement element,
			JRHyperlinkProducer linkProducer,
			boolean prepareForSerialization)
	{
		Matcher matcher = LINK_PATTERN.matcher(text);
		StringBuffer xml = new StringBuffer();
		List<HyperlinkData> hyperlinksData = new ArrayList<HyperlinkData>();

		while (matcher.find())
		{
			String paramName = matcher.group(LINK_PARAM_NAME_GROUP);
			JRPrintHyperlink hyperlink = 
				(JRPrintHyperlink) element.getParameterValue(paramName);
			
			String replacement;
			if (hyperlink == null)
			{
				if (log.isWarnEnabled())
				{
					log.warn("Hyperlink parameter " + paramName 
							+ " not found in element");
				}
				
				replacement = null;
			}
			else
			{
				replacement = linkProducer.getHyperlink(hyperlink);
				if (prepareForSerialization) {
					String id = String.valueOf(hyperlink.hashCode() & 0x7FFFFFFF);

					HyperlinkData hyperlinkData = new HyperlinkData();
					hyperlinkData.setId(id);
					hyperlinkData.setHref(replacement);
					hyperlinkData.setHyperlink(hyperlink);
					hyperlinkData.setSelector("._jrHyperLink." + hyperlink.getLinkType());
					replacement = "jrhl-" + id + ";" + hyperlink.getLinkType();

					hyperlinksData.add(hyperlinkData);
				}
			}
			
			if (replacement == null)
			{
				replacement = "";
			}
			else
			{
				try
				{
					if (!prepareForSerialization) {
						replacement = URLEncoder.encode(replacement, "UTF-8");
					}
				}
				catch (UnsupportedEncodingException e)
				{
					throw new JRRuntimeException(e);
				}
			}
			
			matcher.appendReplacement(xml, replacement);
		}
		matcher.appendTail(xml);

		if (hyperlinksData.size() > 0) {
			element.setParameterValue("hyperlinksData", hyperlinksData);
		}
		
		return xml.toString();

	}
	
	
	private FlashPrintElement()
	{
	}
}

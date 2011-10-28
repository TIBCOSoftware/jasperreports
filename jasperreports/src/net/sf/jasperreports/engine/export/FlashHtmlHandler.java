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
package net.sf.jasperreports.engine.export;

import java.util.Iterator;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.util.FlashUtils;

/**
 * A HTML export handler for generic print elements produced by
 * {@link FlashPrintElement}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FlashHtmlHandler implements GenericElementHtmlHandler//FIXME need to make a similar one for XHTML?
{

	private static final FlashHtmlHandler INSTANCE = new FlashHtmlHandler();
	
	public static FlashHtmlHandler getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * Always exports.
	 */
	public boolean toExport(JRGenericPrintElement element)
	{
		return true;
	}

	/**
	 * Outputs an HTML fragment that embeds a Flash movie.
	 * 
	 * @see FlashPrintElement#PARAMETER_SWF_URL
	 * @see FlashPrintElement#PARAMETER_FLASH_VAR_PREFIX
	 */
	public String getHtmlFragment(JRHtmlExporterContext exporterContext,
			JRGenericPrintElement element)
	{
		String swfURL = (String) element.getParameterValue(
				FlashPrintElement.PARAMETER_SWF_URL);
		
		JRHyperlinkProducer hyperlinkProducer = 
			new HtmlExporterHyperlinkProducerAdapter(exporterContext);
		
		StringBuilder flashVarsBuf = new StringBuilder();
		for (Iterator<String> it = element.getParameterNames().iterator(); it.hasNext();)
		{
			String paramName = it.next();
			if (paramName.startsWith(FlashPrintElement.PARAMETER_FLASH_VAR_PREFIX))
			{
				String varName = paramName.substring(
						FlashPrintElement.PARAMETER_FLASH_VAR_PREFIX.length());
				Object value = element.getParameterValue(paramName);
				flashVarsBuf.append('&');
				flashVarsBuf.append(varName);
				flashVarsBuf.append('=');
				
				if (value instanceof String)
				{
					//TODO have a flag to determine if this is needed
					String text = (String) value;
					text = FlashPrintElement.resolveLinks(
							text, element, hyperlinkProducer);
					value = FlashUtils.encodeFlashVariable(text);
				}
				
				flashVarsBuf.append(value);
			}
		}
		String flashVars = flashVarsBuf.toString();
		
		String id = "jrflash_" + System.identityHashCode(element);
		int width = element.getWidth();
		int height = element.getHeight();
		String allowScriptAccess = FlashUtils.getAllowScriptAccess(exporterContext, element);
		
		StringBuilder out = new StringBuilder();
		out.append("\n<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" width=\"");
		out.append(width);
		out.append("\" height=\"");
		out.append(height);
		out.append("\" id=\"");
		out.append(id);
		out.append("\">\n");
		out.append("<param name=\"allowScriptAccess\" value=\"");
		out.append(allowScriptAccess);
		out.append("\"/>\n");
		out.append("<param name=\"movie\" value=\"");
		out.append(swfURL);
		out.append("\"/>\n");
		out.append("<param name=\"FlashVars\" value=\"");
		out.append(flashVars);
		out.append("\"/>\n");
		out.append("<param name=\"quality\" value=\"high\"/>\n");
		out.append("<embed src=\"");
		out.append(swfURL);
		out.append("\" FlashVars=\"");
		out.append(flashVars);
		out.append("\" quality=\"high\" width=\"");
		out.append(width);
		out.append("\" height=\"");
		out.append(height);
		out.append("\" name=\"");
		out.append(id);
		out.append("\" allowScriptAccess=\"");
		out.append(allowScriptAccess);
		out.append("\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\"/>\n");
		out.append("</object>\n");
		return out.toString();
	}

}

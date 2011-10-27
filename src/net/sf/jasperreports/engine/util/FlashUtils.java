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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.FlashPrintElement;
import net.sf.jasperreports.engine.export.JRExporterContext;

/**
 * Utility methods related to Flash objects.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FlashUtils
{

	/**
	 * Encodes a text used as Flash variable.
	 * 
	 * @param text the text to encode
	 * @return the encoded text
	 */
	public static String encodeFlashVariable(String text)
	{
		try
		{
			// URL/percent encoding is used for FlashVars
			// always using UTF-8 for now
			return URLEncoder.encode(text, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}
	
	/**
	 * Determines the allowScriptAccess parameter for a Flash element.
	 * 
	 * @param context
	 * @param element
	 * @return
	 * @see FlashPrintElement#PROPERTY_ALLOW_SCRIPT_ACCESS
	 */
	public static String getAllowScriptAccess(
			JRExporterContext context, JRGenericPrintElement element)
	{
		return JRProperties.getProperty(FlashPrintElement.PROPERTY_ALLOW_SCRIPT_ACCESS, 
				element, context.getExportedReport());
	}
}

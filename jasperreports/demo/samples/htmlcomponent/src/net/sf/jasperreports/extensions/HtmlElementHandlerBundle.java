/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.extensions;

import net.sf.jasperreports.engine.export.GenericElementHandler;
import net.sf.jasperreports.engine.export.GenericElementHandlerBundle;
import net.sf.jasperreports.engine.export.HtmlElementHtmlHandler;
import net.sf.jasperreports.engine.export.HtmlElementJExcelHandler;
import net.sf.jasperreports.engine.export.HtmlElementPdfHandler;
import net.sf.jasperreports.engine.export.HtmlElementXhtmlHandler;
import net.sf.jasperreports.engine.export.HtmlElementXlsHandler;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public final class HtmlElementHandlerBundle implements GenericElementHandlerBundle
{
	public static final String NAMESPACE = "http://jasperreports.sourceforge.net/jasperreports/html";
	
	public static final String NAME = "htmlelement";
	
	private static final HtmlElementHandlerBundle INSTANCE = new HtmlElementHandlerBundle();
	
	public static HtmlElementHandlerBundle getInstance()
	{
		return INSTANCE;
	}
	
	public String getNamespace()
	{
		return NAMESPACE;
	}
	
	public GenericElementHandler getHandler(String elementName,
			String exporterKey)
	{
		if (NAME.equals(elementName) 
				&& JRXhtmlExporter.XHTML_EXPORTER_KEY.equals(exporterKey))
		{
			return new HtmlElementXhtmlHandler();
		} 
		else if (NAME.equals(elementName)
				&& JRPdfExporter.PDF_EXPORTER_KEY.equals(exporterKey))
		{
			return new HtmlElementPdfHandler();
		}
		else if (NAME.equals(elementName)
				&& JRHtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey))
		{
			return new HtmlElementHtmlHandler();
		}
		else if (NAME.equals(elementName)
				&& JRXlsExporter.XLS_EXPORTER_KEY.equals(exporterKey))
		{
			return new HtmlElementXlsHandler();
		}
		else if (NAME.equals(elementName)
				&& JExcelApiExporter.JXL_EXPORTER_KEY.equals(exporterKey))
		{
			return new HtmlElementJExcelHandler();
		}		
		return null;
	}
}
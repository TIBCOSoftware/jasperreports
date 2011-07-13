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
package net.sf.jasperreports.components.sort;

import net.sf.jasperreports.engine.export.GenericElementHandler;
import net.sf.jasperreports.engine.export.GenericElementHandlerBundle;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: FlashPrintElement.java 4063 2010-11-30 16:37:20Z teodord $
 */
public final class SortElementHandlerBundle implements GenericElementHandlerBundle
{
	public static final String NAME = "sort";
	public static final String NAMESPACE = "http://jasperreports.sourceforge.net/jasperreports/sort";
	
	private static final SortElementHandlerBundle INSTANCE = new SortElementHandlerBundle();
	
	public static SortElementHandlerBundle getInstance()
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
			return new SortElementHtmlHandler();
		}
		return null;
	}
}